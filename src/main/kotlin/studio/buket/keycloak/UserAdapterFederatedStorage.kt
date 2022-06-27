package studio.buket.keycloak

import org.keycloak.component.ComponentModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage
import studio.buket.keycloak.dao.UserDao
import studio.buket.keycloak.models.User

class UserAdapterFederatedStorage(
    session: KeycloakSession,
    realm: RealmModel,
    storageProviderModel: ComponentModel,
    private val user: User,
    private val userDao: UserDao,
) : AbstractUserAdapterFederatedStorage(session, realm, storageProviderModel) {
    override fun getUsername(): String = user.username ?: ""

    override fun setUsername(username: String?) {
        user.username = username
        userDao.updateUser(user)
    }
}
