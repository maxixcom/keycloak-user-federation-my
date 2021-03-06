package studio.buket.keycloak

import org.jboss.logging.Logger
import org.keycloak.component.ComponentModel
import org.keycloak.credential.CredentialInput
import org.keycloak.credential.CredentialInputValidator
import org.keycloak.models.GroupModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserCredentialModel
import org.keycloak.models.UserModel
import org.keycloak.models.credential.PasswordCredentialModel
import org.keycloak.storage.StorageId
import org.keycloak.storage.UserStorageProvider
import org.keycloak.storage.user.UserLookupProvider
import org.keycloak.storage.user.UserQueryProvider
import studio.buket.keycloak.model.toUserAdapter
import studio.buket.keycloak.service.GetUsers
import studio.buket.keycloak.service.SearchForUser
import studio.buket.keycloak.service.StorageProviderUseCaseBuilder
import studio.buket.keycloak.service.ValidatePassword

class BuketStorageProvider(
    private val model: ComponentModel,
    private val session: KeycloakSession,
    private val storageProviderUseCaseBuilder: StorageProviderUseCaseBuilder,
) : UserStorageProvider, UserLookupProvider, CredentialInputValidator, UserQueryProvider {

    override fun close() {
        logger.info("close()")
    }

    override fun getUserById(id: String?, realm: RealmModel): UserModel? {
        logger.info("getUserById()")

        val getUserByUsername = storageProviderUseCaseBuilder.newGetUserByUsername()
        val storageId = StorageId(id)
        return getUserByUsername.execute(storageId.externalId)
            ?.toUserAdapter(session, realm, model)
    }

    override fun getUserByUsername(username: String, realm: RealmModel): UserModel? {
        logger.info("getUserByUsername()")

        val getUserByUsername = storageProviderUseCaseBuilder.newGetUserByUsername()
        return getUserByUsername.execute(username)
            ?.toUserAdapter(session, realm, model)
    }

    override fun getUserByEmail(email: String, realm: RealmModel): UserModel? {
        logger.info("getUserByEmail()")

        val getUserByEmail = storageProviderUseCaseBuilder.newGetUserByEmail()
        return getUserByEmail.execute(email)
            ?.toUserAdapter(session, realm, model)
    }

    override fun supportsCredentialType(credentialType: String?): Boolean {
        logger.info("supportsCredentialType()")

        return PasswordCredentialModel.TYPE == credentialType
    }

    override fun isConfiguredFor(realm: RealmModel?, user: UserModel?, credentialType: String?): Boolean {
        logger.info("isConfiguredFor()")

        return supportsCredentialType(credentialType)
    }

    override fun isValid(realm: RealmModel, user: UserModel, credentialInput: CredentialInput): Boolean {
        logger.info("isValid()")

        if (!supportsCredentialType(credentialInput?.type) || credentialInput !is UserCredentialModel) {
            return false
        }

        val credentialModel: UserCredentialModel = credentialInput
//        TODO("validate user password - not implemented yet")
        // Need to add authentication logic

        println("xxxxxxxxxxxxxxxxxxxx")
        println(credentialModel.value)
        println(credentialModel.credentialId)
        println(credentialModel.challengeResponse)

        println(user?.username)
        println(user?.email)
        println("^^^^^^^^^^^^^^^^^^^^")

        val validatePassword = storageProviderUseCaseBuilder.newValidatePassword()

        return validatePassword.execute(
            ValidatePassword.Request(
                user = user,
                credentialModel = credentialModel
            )
        )
    }

    override fun getUsers(realm: RealmModel): MutableList<UserModel> {
        logger.info("getUsers() 1")

        val getUsers = storageProviderUseCaseBuilder.newGetUsers()
        return getUsers.execute(GetUsers.RequestUsers(realm)).users
            .toUserAdapter(session, realm, model)
            .toMutableList()
    }

    override fun getUsers(realm: RealmModel, firstResult: Int, maxResults: Int): MutableList<UserModel> {
        logger.info("getUsers() 2")

        val getUsers = storageProviderUseCaseBuilder.newGetUsers()
        return getUsers.execute(
            GetUsers.RequestPageUsers(
                realm = realm,
                firstResult = firstResult,
                maxResults = maxResults,
            )
        ).users
            .toUserAdapter(session, realm, model)
            .toMutableList()
    }

    override fun searchForUser(search: String?, realm: RealmModel): MutableList<UserModel> {
        logger.info("searchForUser()")

        val searchForUser = storageProviderUseCaseBuilder.newSearchForUser()
        val request = SearchForUser.RequestBySearchString(
            realm = realm,
            search = search
        )

        return searchForUser.search(request).users
            .toUserAdapter(session, realm, model)
            .toMutableList()
    }

    override fun searchForUser(
        search: String?,
        realm: RealmModel,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        logger.info("searchForUser() 1")

        val searchForUser = storageProviderUseCaseBuilder.newSearchForUser()
        val request = SearchForUser.RequestPageBySearchString(
            realm = realm,
            search = search,
            firstResult = firstResult,
            maxResults = maxResults
        )

        return searchForUser.search(request).users
            .toUserAdapter(session, realm, model)
            .toMutableList()
    }

    override fun searchForUser(params: MutableMap<String, String>, realm: RealmModel): MutableList<UserModel> {
        logger.info("searchForUser() 2")

        val searchForUser = storageProviderUseCaseBuilder.newSearchForUser()
        val request = SearchForUser.RequestByParams(
            realm = realm,
            params = params
        )

        return searchForUser.search(request).users
            .toUserAdapter(session, realm, model)
            .toMutableList()
    }

    override fun searchForUser(
        params: MutableMap<String, String>,
        realm: RealmModel,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        logger.info("searchForUser() 3")

        val searchForUser = storageProviderUseCaseBuilder.newSearchForUser()
        val request = SearchForUser.RequestPageByParams(
            realm = realm,
            params = params,
            firstResult = firstResult,
            maxResults = maxResults
        )

        return searchForUser.search(request).users
            .toUserAdapter(session, realm, model)
            .toMutableList()
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
        attrName: String,
        attrValue: String,
        realm: RealmModel
    ): MutableList<UserModel> {
        logger.info("searchForUserByUserAttribute()")

        val searchForUser = storageProviderUseCaseBuilder.newSearchForUser()
        val request = SearchForUser.RequestPageByUserAttribute(
            realm = realm,
            attrName = attrName,
            attrValue = attrValue,
        )

        return searchForUser.search(request).users
            .toUserAdapter(session, realm, model)
            .toMutableList()
    }

    companion object {
        private val logger = Logger.getLogger(BuketStorageProvider::class.java.name)
    }
}
