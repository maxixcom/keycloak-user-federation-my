package studio.buket.keycloak.service

import studio.buket.keycloak.dao.User

interface AddUser {
    fun execute(username: String): User
}
