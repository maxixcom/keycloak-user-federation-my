package studio.buket.keycloak;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import studio.buket.keycloak.dao.UserDao;
import studio.buket.keycloak.dao.UserDaoImpl;
import studio.buket.keycloak.persistence.DataSourceConfig;
import studio.buket.keycloak.persistence.DataSourceProvider;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BuketUserStorageProviderFactory implements UserStorageProviderFactory<BuketStorageProvider> {
    private static final Logger logger = Logger.getLogger(BuketUserStorageProviderFactory.class);

    public static final String DB_JDBC_URL_KEY = "db:url";
    public static final String DB_USERNAME_KEY = "db:username";
    public static final String DB_PASSWORD_KEY = "db:password";

    private Map<String, DataSourceProvider> providerPerInstance = new ConcurrentHashMap<>();

    protected static final List<ProviderConfigProperty> configMetadata;

    static {
        configMetadata = ProviderConfigurationBuilder.create()
                // JDBC connection string
                .property().name(DB_JDBC_URL_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Database Host")
                .defaultValue("jdbc:postgresql://localhost:5432/database")
                .helpText("JDBC connection url")
                .add()

                // DB Username
                .property().name(DB_USERNAME_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Database Username")
                .defaultValue("postgres")
                .add()

                // DB Password
                .property().name(DB_PASSWORD_KEY)
                .type(ProviderConfigProperty.PASSWORD)
                .label("Database Password")
                .defaultValue("postgres")
                .add()

                .build();
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
        MultivaluedHashMap<String, String> configMap = config.getConfig();
        if (StringUtils.isBlank(configMap.getFirst(DB_JDBC_URL_KEY))) {
            throw new ComponentValidationException("JDBC connection url is empty.");
        }
        if (StringUtils.isBlank(configMap.getFirst(DB_USERNAME_KEY))) {
            throw new ComponentValidationException("Database username empty.");
        }
        if (StringUtils.isBlank(configMap.getFirst(DB_PASSWORD_KEY))) {
            throw new ComponentValidationException("Database password empty.");
        }
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public BuketStorageProvider create(KeycloakSession session, ComponentModel model) {
        DataSourceProvider dataSourceProvider = providerPerInstance.computeIfAbsent(
                model.getId(),
                id -> createDataSourceProvider(model)
        );

        UserDao userDao = new UserDaoImpl(dataSourceProvider);

        return new BuketStorageProvider(model, session, userDao);
    }

    @NotNull
    private DataSourceProvider createDataSourceProvider(ComponentModel model) {
        DataSourceConfig config = new DataSourceConfig();
        config.setName(model.getId());
        config.setJdbcUrl(model.get(DB_JDBC_URL_KEY));
        config.setUser(model.get(DB_USERNAME_KEY));
        config.setPassword(model.get(DB_PASSWORD_KEY));
        return new DataSourceProvider(config);
    }

    @Override
    public void close() {
        logger.info("Closing data sources");
        providerPerInstance.forEach((key, value) -> {
            logger.infof("Closing data source: %s", key);
            try {
                value.close();
            } catch (Exception e) {
                logger.errorf("Error closing data source: %s", key);
            }
        });
    }

    @Override
    public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {
        logger.info("Update configuration " + oldModel.getId());
        providerPerInstance.computeIfPresent(oldModel.getId(), (key, oldValue) -> {
            oldValue.close();
            return createDataSourceProvider(newModel);
        });
    }

    @Override
    public void preRemove(KeycloakSession session, RealmModel realm, ComponentModel model) {
        logger.warn("Remove configuration " + model.getId());
        Optional.ofNullable(providerPerInstance.remove(model.getId()))
                .ifPresent(DataSourceProvider::close);
    }

    @Override
    public String getId() {
        return "buket-studio-user-provider";
    }


}
