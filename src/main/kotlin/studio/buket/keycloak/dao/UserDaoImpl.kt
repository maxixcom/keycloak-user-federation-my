package studio.buket.keycloak.dao

import org.jboss.logging.Logger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import studio.buket.keycloak.exposed.JBossSqlLogger
import studio.buket.keycloak.models.User
import studio.buket.keycloak.persistence.DataSourceProvider
import java.util.UUID

class UserDaoImpl(
    private val dataSourceProvider: DataSourceProvider
) : UserDao {
    override fun findUserById(id: UUID): User? {
        val db = dataSourceProvider.database

        transaction(db) {
            addLogger(JBossSqlLogger)
            ExposedUsers.selectAll().forEach {
                logger.info(it)
            }
        }

        return null
    }

    companion object {
        private val logger: Logger = Logger.getLogger(UserDaoImpl::class.java)
    }
}
