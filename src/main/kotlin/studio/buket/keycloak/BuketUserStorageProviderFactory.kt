package studio.buket.keycloak

import org.jboss.logging.Logger
import org.keycloak.component.ComponentModel
import org.keycloak.component.ComponentValidationException
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.provider.ProviderConfigProperty
import org.keycloak.provider.ProviderConfigurationBuilder
import org.keycloak.storage.UserStorageProviderFactory
import studio.buket.keycloak.persistence.DataSourceConfig
import studio.buket.keycloak.persistence.DataSourceProvider
import studio.buket.keycloak.service.StorageProviderUseCaseBuilderImpl
import java.util.concurrent.ConcurrentHashMap

class BuketUserStorageProviderFactory : UserStorageProviderFactory<BuketStorageProvider> {
    private val providerPerInstance: ConcurrentHashMap<String, DataSourceProvider> = ConcurrentHashMap()

    private fun createDataSourceProvider(model: ComponentModel): DataSourceProvider {
        val config = DataSourceConfig().apply {
            name = model.id
            jdbcUrl = model[DB_JDBC_URL_KEY]
            user = model[DB_USERNAME_KEY]
            password = model[DB_PASSWORD_KEY]
        }
        return DataSourceProvider(config)
    }

    override fun create(session: KeycloakSession, model: ComponentModel): BuketStorageProvider {
        val dataSourceProvider: DataSourceProvider = providerPerInstance.computeIfAbsent(model.id) {
            logger.info("Create Datasource provider: ${model.id}")
            createDataSourceProvider(model)
        }

        return BuketStorageProvider(
            model,
            session,
            StorageProviderUseCaseBuilderImpl(dataSourceProvider.database)
        )
    }

    override fun close() {
        logger.info("Closing data sources")
        providerPerInstance.forEach { (key: String, dataSourceProvider: DataSourceProvider) ->
            logger.infof("Closing data source: %s", key)
            try {
                dataSourceProvider.close()
            } catch (e: Exception) {
                logger.errorf("Error closing data source: %s", key)
            }
        }
    }

    override fun getId(): String {
        return "buket-studio-users-kotlin"
    }

    override fun onUpdate(
        session: KeycloakSession,
        realm: RealmModel,
        oldModel: ComponentModel,
        newModel: ComponentModel
    ) {
        logger.info("Update configuration ${oldModel.id}")
        providerPerInstance.computeIfPresent(oldModel.id) { _, oldDataSourceProvider: DataSourceProvider ->
            oldDataSourceProvider.close()
            createDataSourceProvider(newModel)
        }
    }

    override fun preRemove(session: KeycloakSession, realm: RealmModel, model: ComponentModel) {
        logger.warn("Remove configuration ${model.id}")
        providerPerInstance.remove(model.id)?.close()
    }

    override fun getConfigProperties(): MutableList<ProviderConfigProperty> =
        BuketUserStorageProviderFactory.configMetadata

    override fun validateConfiguration(session: KeycloakSession, realm: RealmModel, config: ComponentModel) {
        val configMap = config.config
        if (configMap.getFirst(DB_JDBC_URL_KEY).isNullOrBlank()) {
            throw ComponentValidationException("JDBC connection url is empty.")
        }
        if (configMap.getFirst(DB_USERNAME_KEY).isNullOrBlank()) {
            throw ComponentValidationException("Database username empty.")
        }
        if (configMap.getFirst(DB_PASSWORD_KEY).isNullOrBlank()) {
            throw ComponentValidationException("Database password empty.")
        }
    }

    companion object {
        const val DB_JDBC_URL_KEY = "db:url"
        const val DB_USERNAME_KEY = "db:username"
        const val DB_PASSWORD_KEY = "db:password"

        private val logger = Logger.getLogger(BuketUserStorageProviderFactory::class.java)

        private val configMetadata =
            ProviderConfigurationBuilder.create()
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
                .build()
    }
}
