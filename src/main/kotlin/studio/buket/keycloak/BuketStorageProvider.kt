package studio.buket.keycloak

import org.keycloak.component.ComponentModel
import org.keycloak.credential.CredentialInput
import org.keycloak.credential.CredentialInputValidator
import org.keycloak.models.GroupModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.storage.UserStorageProvider
import org.keycloak.storage.user.UserLookupProvider
import org.keycloak.storage.user.UserQueryProvider

class BuketStorageProvider : UserStorageProvider, UserLookupProvider, CredentialInputValidator, UserQueryProvider {
    private lateinit var model: ComponentModel
    private lateinit var session: KeycloakSession

    fun setModel(model: ComponentModel) {
        this.model = model
    }

    fun setSession(session: KeycloakSession) {
        this.session = session
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getUserById(id: String?, realm: RealmModel?): UserModel {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getUserByUsername(username: String?, realm: RealmModel?): UserModel {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getUserByEmail(email: String?, realm: RealmModel?): UserModel {
        TODO("Not yet implemented")
    }

    override fun supportsCredentialType(credentialType: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isConfiguredFor(realm: RealmModel?, user: UserModel?, credentialType: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isValid(realm: RealmModel?, user: UserModel?, credentialInput: CredentialInput?): Boolean {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getUsers(realm: RealmModel?): MutableList<UserModel> {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getUsers(realm: RealmModel?, firstResult: Int, maxResults: Int): MutableList<UserModel> {
        TODO("Not yet implemented")
    }

    override fun searchForUser(search: String?, realm: RealmModel?): MutableList<UserModel> = mutableListOf()

    @Deprecated("Deprecated in Java")
    override fun searchForUser(
        search: String?,
        realm: RealmModel?,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        TODO("Not yet implemented")
    }

    override fun searchForUser(params: MutableMap<String, String>?, realm: RealmModel?): MutableList<UserModel> =
        mutableListOf()

    @Deprecated("Deprecated in Java", ReplaceWith("mutableListOf()"))
    override fun searchForUser(
        params: MutableMap<String, String>?,
        realm: RealmModel?,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> = mutableListOf()

    override fun getGroupMembers(realm: RealmModel?, group: GroupModel?): MutableList<UserModel> = mutableListOf()

    override fun getGroupMembers(
        realm: RealmModel?,
        group: GroupModel?,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> = mutableListOf()

    override fun searchForUserByUserAttribute(
        attrName: String?,
        attrValue: String?,
        realm: RealmModel?
    ): MutableList<UserModel> = mutableListOf()
}
