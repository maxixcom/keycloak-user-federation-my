package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import studio.buket.keycloak.dao.User
import studio.buket.keycloak.dao.Users

class GetUserByUsernameImpl(
    private val db: Database
) : GetUserByUsername {
    override fun execute(username: String): User? {
        return transaction(db) {
            val query = Users.select {
                Users.username eq username
            }.limit(1)
            query.map(User::wrapRow).singleOrNull()
        }
    }
}
