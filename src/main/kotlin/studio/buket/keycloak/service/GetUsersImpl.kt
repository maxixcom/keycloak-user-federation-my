package studio.buket.keycloak.service

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import studio.buket.keycloak.dao.User
import studio.buket.keycloak.dao.Users
import studio.buket.keycloak.exposed.JBossSqlInfoLogger

class GetUsersImpl(
    private val db: Database
) : GetUsers {
    override fun execute(request: GetUsers.Request): GetUsers.Response {
        val result = transaction(db) {
            addLogger(JBossSqlInfoLogger)

            val query = when (request) {
                is GetUsers.RequestPageUsers -> requestPageUsers(request)
                is GetUsers.RequestUsers -> requestUsers(request)
            }

            query.map(User::wrapRow).toList()
        }
        return GetUsers.Response(result)
    }

    private fun requestUsers(request: GetUsers.RequestUsers): Query = Users.selectAll()

    private fun requestPageUsers(request: GetUsers.RequestPageUsers): Query {
        return Users.selectAll()
            .limit(request.maxResults, request.firstResult.toLong())
    }
}
