package studio.buket.keycloak.service

interface StorageProviderUseCaseBuilder {
    fun newGetUserByUsername(): GetUserByUsername
    fun newGetUserByEmail(): GetUserByEmail
    fun newSearchForUser(): SearchForUser
}
