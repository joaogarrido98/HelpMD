package com.example.database

import com.example.entities.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

/**
 * class used to manage the resources of database
 */
object DatabaseManager {
    /**
     * connection to the database and creation of the tables
     */
    fun init() {
        Database.connect(hikariLocal())
        transaction {
            SchemaUtils.create(PatientTable)
            SchemaUtils.create(DoctorTable)
            SchemaUtils.create(TimetableTable)
            SchemaUtils.create(ActivationTable)
            SchemaUtils.create(ActiveDoctorTable)
            SchemaUtils.create(SchedulesTable)
            SchemaUtils.create(PrescriptionsTable)
            SchemaUtils.create(PatientHistoryTable)
            SchemaUtils.create(ScheduleDoctorTable)
            SchemaUtils.create(BookingsTable)
        }
    }

    /**
     * server configuration for real server
     */
    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER")
        val uri = URI(System.getenv("DATABASE_URL"))
        val username = uri.userInfo.split(":").toTypedArray()[0]
        val password = uri.userInfo.split(":").toTypedArray()[1]
        config.jdbcUrl = "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require" +
                "&user=$username&password=$password"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    /**
     * server configuration for localhost
     */
    private fun hikariLocal(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER")
        config.jdbcUrl = System.getenv("DATABASE_URL")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }



    /**
     * function that takes a function as parameter and executes it inside a coroutine
     */
    suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) { transaction { block() } }
}