package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import studio.buket.keycloak.dao.User
import studio.buket.keycloak.dao.Users

class RemoveUserImpl(
    private val db: Database
) : RemoveUser {
    override fun execute(username: String) {
        transaction(db) {
            User.find {
                Users.username eq username
            }.forEach(User::delete)
        }
    }
}
