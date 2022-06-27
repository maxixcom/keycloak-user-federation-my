package studio.buket.keycloak;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
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

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuketUserStorageProviderFactory implements UserStorageProviderFactory<BuketStorageProvider> {
    public static final int PORT_LIMIT = 65535;
    public static final String DB_CONNECTION_NAME_KEY = "db:connectionName";
    public static final String DB_HOST_KEY = "db:host";
    public static final String DB_DATABASE_KEY = "db:database";
    public static final String DB_USERNAME_KEY = "db:username";
    public static final String DB_PASSWORD_KEY = "db:password";
    public static final String DB_PORT_KEY = "db:port";


    protected static final List<ProviderConfigProperty> configMetadata;

    Map<String, EntityManagerFactory> entityManagerFactories = new HashMap<>();

    static {
        configMetadata = ProviderConfigurationBuilder.create()
                // Connection Name
                .property().name(DB_CONNECTION_NAME_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Connection Name")
                .defaultValue("")
                .helpText("Name of the connection, can be chosen individually. Enables connection sharing between providers if the same name is provided. Overrides currently saved connection properties.")
                .add()

                // Connection Host
                .property().name(DB_HOST_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Database Host")
                .defaultValue("localhost")
                .helpText("Host of the connection")
                .add()

                // Connection Database
                .property().name(DB_DATABASE_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Database Name")
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

                // DB Port
                .property().name(DB_PORT_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Database Port")
                .defaultValue("5432")
                .add()
                .build();

    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
        MultivaluedHashMap<String, String> configMap = config.getConfig();
        if (StringUtils.isBlank(configMap.getFirst(DB_CONNECTION_NAME_KEY))) {
            throw new ComponentValidationException("Connection name empty.");
        }
        if (StringUtils.isBlank(configMap.getFirst(DB_HOST_KEY))) {
            throw new ComponentValidationException("Database host empty.");
        }
        if (!StringUtils.isNumeric(configMap.getFirst(DB_PORT_KEY)) || Long.parseLong(configMap.getFirst(DB_PORT_KEY)) > PORT_LIMIT) {
            throw new ComponentValidationException("Invalid port. (Empty or NaN)");
        }
        if (StringUtils.isBlank(configMap.getFirst(DB_DATABASE_KEY))) {
            throw new ComponentValidationException("Database name empty.");
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
        Map<String, Object> properties = new HashMap<>();
        String dbConnectionName = model.getConfig().getFirst(DB_CONNECTION_NAME_KEY);
        EntityManagerFactory entityManagerFactory = entityManagerFactories.get(dbConnectionName);
        if (entityManagerFactory == null) {
            MultivaluedHashMap<String, String> config = model.getConfig();
            properties.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            properties.put("hibernate.connection.url",
                    String.format("jdbc:mysql://%s:%s/%s",
                            config.getFirst(DB_HOST_KEY),
                            config.getFirst(DB_PORT_KEY),
                            config.getFirst(DB_DATABASE_KEY)));
            properties.put("hibernate.connection.username", config.getFirst(DB_USERNAME_KEY));
            properties.put("hibernate.connection.password", config.getFirst(DB_PASSWORD_KEY));
            properties.put("hibernate.show-sql", "true");
            properties.put("hibernate.archive.autodetection", "class, hbm");
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.connection.autocommit", "true");

            entityManagerFactory = new HibernatePersistenceProvider()
                    .createContainerEntityManagerFactory(
                            new BuketPersistenceUnitInfo("userstorage"),
                            properties
                    );
        }
        UserDao userDao = new UserDaoImpl(entityManagerFactory.createEntityManager());

        return new BuketStorageProvider(model, session, userDao);
    }

    @Override
    public String getId() {
        return "buket-studio-user-provider";
    }

}
