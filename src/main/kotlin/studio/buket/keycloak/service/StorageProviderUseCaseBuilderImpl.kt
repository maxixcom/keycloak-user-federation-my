package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database

class StorageProviderUseCaseBuilderImpl(
    private val db: Database
) : StorageProviderUseCaseBuilder {
    override fun newGetUserByUsername(): GetUserByUsername = GetUserByUsernameImpl(db)
    override fun newGetUserByEmail(): GetUserByEmail = GetUserByEmailImpl(db)
    override fun newSearchForUser(): SearchForUser = SearchForUserImpl(db)
    override fun newGetUsers(): GetUsers = GetUsersImpl(db)
    override fun newValidatePassword(): ValidatePassword = ValidatePasswordImpl(db)
    override fun newAddUser(): AddUser = AddUserImpl(db)
    override fun newRemoveUser(): RemoveUser = RemoveUserImpl(db)
    override fun newDisableCredentialTypeImpl(): DisableCredentialType = DisableCredentialTypeImpl(db)
}
