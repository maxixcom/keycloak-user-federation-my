package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.orWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.keycloak.models.UserModel
import studio.buket.keycloak.dao.User
import studio.buket.keycloak.dao.Users
import studio.buket.keycloak.exposed.JBossSqlInfoLogger

class SearchForUserImpl(
    private val db: Database
) : SearchForUser {
    override fun search(request: SearchForUser.Request): SearchForUser.Response {
        val result = transaction(db) {
            addLogger(JBossSqlInfoLogger)

            val query = when (request) {
                is SearchForUser.RequestByParams -> requestByParams(request)
                is SearchForUser.RequestPageByParams -> requestPageByParams(request)
                is SearchForUser.RequestPageBySearchString -> requestPageBySearchString(request)
                is SearchForUser.RequestBySearchString -> requestBySearchString(request)
                is SearchForUser.RequestPageByUserAttribute -> requestByUserAttribute(request)
            }

            query.map(User::wrapRow).toList()
        }
        return SearchForUser.Response(result)
    }

    private fun requestByUserAttribute(request: SearchForUser.RequestPageByUserAttribute): Query {
        val params = mutableMapOf(
            request.attrName to request.attrValue
        )
        return this.requestByParams(
            SearchForUser.RequestByParams(
                realm = request.realm,
                params = params,
            )
        )
    }

    private fun requestPageBySearchString(request: SearchForUser.RequestPageBySearchString): Query {
        return this.requestBySearchString(
            SearchForUser.RequestBySearchString(
                realm = request.realm,
                search = request.search
            )
        ).limit(request.maxResults, request.firstResult.toLong())
    }

    private fun requestBySearchString(request: SearchForUser.RequestBySearchString): Query {
        return with(request) {
            val query = Users.selectAll()
            if (!search.isNullOrBlank()) {
                query.orWhere { Users.firstName like "%${request.search}%" }
                query.orWhere { Users.lastName like "%${request.search}%" }
                query.orWhere { Users.email like "%${request.search}%" }
                query.orWhere { Users.username like "%${request.search}%" }
            }
            query
        }
    }

    private fun requestPageByParams(request: SearchForUser.RequestPageByParams): Query {
        return requestByParams(
            SearchForUser.RequestByParams(
                realm = request.realm,
                params = request.params
            )
        ).limit(request.maxResults, request.firstResult.toLong())
    }

    /**
     * UserModel.FIRST_NAME - first name (case insensitive string)
     * UserModel.LAST_NAME - last name (case insensitive string)
     * UserModel.EMAIL - email (case insensitive string)
     * UserModel.USERNAME - username (case insensitive string)
     * UserModel.EMAIL_VERIFIED - search only for users with verified/non-verified email (true/false)
     * UserModel.ENABLED - search only for enabled/disabled users (true/false)
     * UserModel.IDP_ALIAS - search only for users that have a federated identity from idp with the given alias configured (case sensitive string)
     * UserModel.IDP_USER_ID - search for users with federated identity with the given userId (case
     */
    private fun requestByParams(request: SearchForUser.RequestByParams): Query {
        return with(request) {
            val query = Users.selectAll()

            if (params.containsKey(UserModel.FIRST_NAME)) {
                query.orWhere { Users.firstName like "%${params[UserModel.FIRST_NAME]}%" }
            }
            if (params.containsKey(UserModel.LAST_NAME)) {
                query.orWhere { Users.lastName like "%${params[UserModel.FIRST_NAME]}%" }
            }
            if (params.containsKey(UserModel.EMAIL)) {
                query.orWhere { Users.email like "%${params[UserModel.EMAIL]}%" }
            }
            if (params.containsKey(UserModel.USERNAME)) {
                query.orWhere { Users.username like "%${params[UserModel.USERNAME]}%" }
            }

            query
        }
    }
}
