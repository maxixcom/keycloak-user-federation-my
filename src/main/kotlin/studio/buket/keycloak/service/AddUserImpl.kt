package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import studio.buket.keycloak.dao.User

class AddUserImpl(
    private val db: Database
) : AddUser {
    override fun execute(username: String): User {
        return transaction(db) {
            User.new {
                this@new.username = username
            }
        }
    }
}
