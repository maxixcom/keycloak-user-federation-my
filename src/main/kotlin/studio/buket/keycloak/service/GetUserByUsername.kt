package studio.buket.keycloak.service

import studio.buket.keycloak.dao.User

interface GetUserByUsername {
    fun execute(username: String): User?
}
