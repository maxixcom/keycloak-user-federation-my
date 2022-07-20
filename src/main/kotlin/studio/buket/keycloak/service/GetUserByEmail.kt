package studio.buket.keycloak.service

import studio.buket.keycloak.dao.User

interface GetUserByEmail {
    fun execute(email: String): User?
}
