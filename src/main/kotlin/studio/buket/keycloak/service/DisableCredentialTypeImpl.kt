package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.keycloak.models.UserModel
import studio.buket.keycloak.dao.User
import studio.buket.keycloak.dao.Users

class DisableCredentialTypeImpl(
    private val db: Database
) : DisableCredentialType {
    override fun execute(user: UserModel, credentialType: String) {
        transaction(db) {
            User.find {
                Users.username eq user.username
            }.forEach {
                it.password = "UNSET_PASSWORD"
            }
        }
    }
}
