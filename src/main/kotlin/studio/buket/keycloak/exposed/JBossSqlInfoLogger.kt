package studio.buket.keycloak.exposed

import org.jboss.logging.Logger
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.TransactionManager

val jbossExposedLogger: Logger = Logger.getLogger("Exposed")

object JBossSqlInfoLogger : SqlLogger {
    override fun log(context: StatementContext, transaction: Transaction) {
        jbossExposedLogger.info(context.expandArgs(TransactionManager.current()))
    }
}
