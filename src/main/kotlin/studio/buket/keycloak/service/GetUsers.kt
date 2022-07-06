package studio.buket.keycloak.service

import org.keycloak.models.RealmModel
import studio.buket.keycloak.dao.User

interface GetUsers {

    fun execute(request: Request): Response

    sealed class Request(val realm: RealmModel)

    class RequestUsers(
        realm: RealmModel,
    ) : Request(realm)

    class RequestPageUsers(
        realm: RealmModel,
        val firstResult: Int,
        val maxResults: Int,
    ) : Request(realm)

    data class Response(
        val users: List<User>
    )
}
