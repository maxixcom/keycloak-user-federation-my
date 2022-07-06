package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import studio.buket.keycloak.dao.User
import studio.buket.keycloak.dao.Users

class ValidatePasswordImpl(
    private val db: Database
) : ValidatePassword {
    override fun execute(request: ValidatePassword.Request): Boolean {
        val user = transaction(db) {
            val username = request.user.username
            Users.select { Users.username eq username }
                .limit(1)
                .map(User::wrapRow)
                .singleOrNull()
        }

        return user?.let {
            it.password == request.credentialModel.challengeResponse
        } ?: false
    }
}
