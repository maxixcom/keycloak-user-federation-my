package studio.buket.keycloak.persistence

import com.zaxxer.hikari.HikariDataSource
import org.jboss.logging.Logger
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

    override fun close() {
        if (hikariDataSourceDelegate.isInitialized()) {
            logger.info("Close Hikari Data Source")
            hikariDataSource.close()
        }
    }

    private fun createDataSource(): HikariDataSource {
        val hikariDataSource = HikariDataSource(config.toHikariConfig())
        logger.info("Config: $hikariDataSource")
        hikariDataSource.validate()
        return hikariDataSource
    }

    companion object {
        private val logger: Logger = Logger.getLogger(DataSourceProvider::class.java)
    }
}
