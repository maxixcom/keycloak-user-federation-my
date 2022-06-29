package studio.buket.keycloak.dao

import studio.buket.keycloak.models.User
import studio.buket.keycloak.persistence.DataSourceProvider
import java.util.UUID

class UserDaoImpl(
    private val dataSourceProvider: DataSourceProvider
) : UserDao {
    override fun findUserById(id: UUID): User? {
        val dataSource = dataSourceProvider.dataSource
        return null
    }
}
