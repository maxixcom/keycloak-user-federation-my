package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import studio.buket.keycloak.dao.User
import studio.buket.keycloak.dao.Users

class GetUserByEmailImpl(
    private val db: Database
) : GetUserByEmail {
    override fun execute(email: String): User? {
        return transaction(db) {
            User.find { Users.email eq email }.single()
        }
    }
}
