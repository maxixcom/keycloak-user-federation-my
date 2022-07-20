package studio.buket.keycloak.persistence

import com.zaxxer.hikari.HikariDataSource
import org.jboss.logging.Logger
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

class DataSourceProvider(
    private val config: DataSourceConfig
) : AutoCloseable {
    private val hikariDataSourceDelegate = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        createDataSource()
    }
    private val hikariDataSource: HikariDataSource by hikariDataSourceDelegate
    val dataSource: DataSource
        get() = hikariDataSource

    /**
     * val db = Database.connect(dataSource)
     *
     * Note: Starting Exposed 0.10 executing this code more than once per db will create leaks in your application,
     * hence it is recommended to store it for later use. For example:
     * https://github.com/JetBrains/Exposed/wiki/DataBase-and-DataSource
     */
    private val databaseExposedDelegate = lazy { Database.connect(hikariDataSource) }
    private val databaseExposed: Database by databaseExposedDelegate
    val database: Database
        get() = databaseExposed

    private fun createDataSource(): HikariDataSource {
        val hikariDataSource = HikariDataSource(config.toHikariConfig())
        logger.info("Config: $hikariDataSource")
        hikariDataSource.validate()
        return hikariDataSource
    }

    override fun close() {
        if (hikariDataSourceDelegate.isInitialized()) {
            logger.info("Close Hikari Data Source")
            hikariDataSource.close()
        }
    }

    companion object {
        private val logger: Logger = Logger.getLogger(DataSourceProvider::class.java)
    }
}
