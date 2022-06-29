package studio.buket.keycloak.persistence

import com.zaxxer.hikari.HikariConfig
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DataSourceConfig(
    var name: String = "default",
    var jdbcUrl: String = "jdbc:postgresql://localhost:5432/database",
    var user: String = "postgres",
    var password: String = "postgres",
    var testQuery: String = "SELECT 1;",
    var driverClassName: String = "org.postgresql.Driver",
)

fun DataSourceConfig.toHikariConfig(): HikariConfig {
    return HikariConfig().apply {
        val timestampString = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        username = this@toHikariConfig.user
        password = this@toHikariConfig.password
        poolName = "BUKET-USER-PROVIDER-$name-$timestampString"
        jdbcUrl = this@toHikariConfig.jdbcUrl
        connectionTestQuery = this@toHikariConfig.testQuery
        driverClassName = this@toHikariConfig.driverClassName
    }
}
