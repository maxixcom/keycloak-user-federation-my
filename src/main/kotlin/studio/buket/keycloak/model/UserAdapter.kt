package studio.buket.keycloak.model

import org.keycloak.component.ComponentModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage
import studio.buket.keycloak.dao.User

class UserAdapter(
    session: KeycloakSession,
    realm: RealmModel,
    storageProviderModel: ComponentModel,
    private val user: User
) : AbstractUserAdapterFederatedStorage(session, realm, storageProviderModel) {

    override fun getUsername(): String? = user.username

    override fun setUsername(username: String?) {
        user.username = username
    }
}

fun User.toUserAdapter(
    session: KeycloakSession,
    realm: RealmModel,
    storageProviderModel: ComponentModel,
): UserAdapter = UserAdapter(session, realm, storageProviderModel, this)

fun List<User>.toUserAdapter(
    session: KeycloakSession,
    realm: RealmModel,
    storageProviderModel: ComponentModel,
): List<UserAdapter> = map { it.toUserAdapter(session, realm, storageProviderModel) }
