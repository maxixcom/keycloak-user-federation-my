package studio.buket.keycloak.service

import org.keycloak.models.UserModel

interface DisableCredentialType {
    fun execute(user: UserModel, credentialType: String)
}
