package studio.buket.keycloak.dao

import studio.buket.keycloak.models.User

interface UserDao : AutoCloseable {
    fun updateUser(user: User): User
}
