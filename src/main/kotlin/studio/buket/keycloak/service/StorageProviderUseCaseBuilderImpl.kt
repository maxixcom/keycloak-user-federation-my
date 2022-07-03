package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database

class StorageProviderUseCaseBuilderImpl(
    private val db: Database
) : StorageProviderUseCaseBuilder {
    override fun newGetUserByUsername(): GetUserByUsername = GetUserByUsernameImpl(db)
    override fun newGetUserByEmail(): GetUserByEmail = GetUserByEmailImpl(db)
    override fun newSearchForUser(): SearchForUser = SearchForUserImpl(db)
}
