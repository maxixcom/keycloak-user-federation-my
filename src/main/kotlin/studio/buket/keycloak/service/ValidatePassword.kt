package studio.buket.keycloak.service

import org.keycloak.models.UserCredentialModel
import org.keycloak.models.UserModel

interface ValidatePassword {

    fun execute(request: Request): Boolean

    data class Request(
        val user: UserModel,
        val credentialModel: UserCredentialModel
    )
}
