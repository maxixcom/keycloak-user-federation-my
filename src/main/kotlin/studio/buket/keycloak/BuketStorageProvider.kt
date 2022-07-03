package studio.buket.keycloak

import org.jboss.logging.Logger
import org.keycloak.component.ComponentModel
import org.keycloak.credential.CredentialInput
import org.keycloak.credential.CredentialInputValidator
import org.keycloak.models.GroupModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.storage.StorageId
import org.keycloak.storage.UserStorageProvider
import org.keycloak.storage.user.UserLookupProvider
import org.keycloak.storage.user.UserQueryProvider
import studio.buket.keycloak.model.toUserAdapter
import studio.buket.keycloak.service.SearchForUser
import studio.buket.keycloak.service.StorageProviderUseCaseBuilder

class BuketStorageProvider(
    private val model: ComponentModel,
    private val session: KeycloakSession,
    private val storageProviderUseCaseBuilder: StorageProviderUseCaseBuilder,
) : UserStorageProvider, UserLookupProvider, CredentialInputValidator, UserQueryProvider {

    override fun close() {
        logger.info("close()")
    }

    @Deprecated("Deprecated in Java")
    override fun getUserById(id: String?, realm: RealmModel): UserModel? {
        logger.info("getUserById()")
        val getUserByUsername = storageProviderUseCaseBuilder.newGetUserByUsername()
        val storageId = StorageId(id)
        return getUserByUsername.execute(storageId.externalId)
            ?.toUserAdapter(session, realm, model)
    }

    @Deprecated("Deprecated in Java")
    override fun getUserByUsername(username: String, realm: RealmModel): UserModel? {
        logger.info("getUserByUsername()")
        val getUserByUsername = storageProviderUseCaseBuilder.newGetUserByUsername()
        return getUserByUsername.execute(username)
            ?.toUserAdapter(session, realm, model)
    }

    @Deprecated("Deprecated in Java")
    override fun getUserByEmail(email: String, realm: RealmModel): UserModel? {
        logger.info("getUserByEmail()")
        val getUserByEmail = storageProviderUseCaseBuilder.newGetUserByEmail()
        return getUserByEmail.execute(email)
            ?.toUserAdapter(session, realm, model)
    }

    override fun supportsCredentialType(credentialType: String?): Boolean {
        logger.info("supportsCredentialType()")
        TODO("Not yet implemented")
    }

    override fun isConfiguredFor(realm: RealmModel?, user: UserModel?, credentialType: String?): Boolean {
        logger.info("isConfiguredFor()")
        TODO("Not yet implemented")
    }

    override fun isValid(realm: RealmModel?, user: UserModel?, credentialInput: CredentialInput?): Boolean {
        logger.info("isValid()")
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getUsers(realm: RealmModel?): MutableList<UserModel> {
        logger.info("getUsers() 1")
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getUsers(realm: RealmModel?, firstResult: Int, maxResults: Int): MutableList<UserModel> {
        logger.info("getUsers() 2")
        TODO("Not yet implemented")
    }

    override fun searchForUser(search: String?, realm: RealmModel?): MutableList<UserModel> {
        logger.info("searchForUser()")
        return mutableListOf()
    }

    @Deprecated("Deprecated in Java")
    override fun searchForUser(
        search: String?,
        realm: RealmModel,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        logger.info("searchForUser() 1")

        val searchForUser = storageProviderUseCaseBuilder.newSearchForUser()
        val response = searchForUser.search(
            SearchForUser.RequestPageBySearchString(
                realm = realm,
                search = search,
                firstResult = firstResult,
                maxResults = maxResults
            )
        )
        return response.users.toUserAdapter(session, realm, model)
            .toMutableList()
    }

    override fun searchForUser(params: MutableMap<String, String>?, realm: RealmModel?): MutableList<UserModel> {
        logger.info("searchForUser() 2")
        return mutableListOf()
    }

    @Deprecated("Deprecated in Java", ReplaceWith("mutableListOf()"))
    override fun searchForUser(
        params: MutableMap<String, String>?,
        realm: RealmModel?,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        logger.info("searchForUser() 3")

//        logger.info("params: $params")
//        logger.infof("realm: %s, firstResult: %d, maxResults: %d", realm, firstResult, maxResults)
//
//        userDao.findUserById(UUID.randomUUID())
        return mutableListOf()
    }

    override fun getGroupMembers(realm: RealmModel?, group: GroupModel?): MutableList<UserModel> {
        logger.info("getGroupMembers() 1")
        return mutableListOf()
    }

    override fun getGroupMembers(
        realm: RealmModel?,
        group: GroupModel?,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        logger.info("getGroupMembers() 2")
        return mutableListOf()
    }

    override fun searchForUserByUserAttribute(
        attrName: String?,
        attrValue: String?,
        realm: RealmModel?
    ): MutableList<UserModel> {
        logger.info("searchForUserByUserAttribute()")
        return mutableListOf()
    }

    companion object {
        private val logger = Logger.getLogger(BuketStorageProvider::class.java.name)
    }
}
