package studio.buket.keycloak.dao

import studio.buket.keycloak.models.User
import java.util.UUID

interface UserDao {
    fun findUserById(id: UUID): User?
}
