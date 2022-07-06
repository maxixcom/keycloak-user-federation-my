package studio.buket.keycloak.service

import org.keycloak.models.RealmModel
import studio.buket.keycloak.dao.User

interface SearchForUser {
    fun search(request: Request): Response

    sealed class Request(val realm: RealmModel)

    class RequestBySearchString(
        realm: RealmModel,
        val search: String?,
    ) : Request(realm)

    class RequestPageBySearchString(
        realm: RealmModel,
        val search: String?,
        val firstResult: Int,
        val maxResults: Int,
    ) : Request(realm)

    class RequestByParams(
        realm: RealmModel,
        val params: MutableMap<String, String>,
    ) : Request(realm)

    class RequestPageByParams(
        realm: RealmModel,
        val params: MutableMap<String, String>,
        val firstResult: Int,
        val maxResults: Int,
    ) : Request(realm)

    class RequestPageByUserAttribute(
        realm: RealmModel,
        val attrName: String,
        val attrValue: String,
    ) : Request(realm)

    data class Response(
        val users: List<User>
    )
}
