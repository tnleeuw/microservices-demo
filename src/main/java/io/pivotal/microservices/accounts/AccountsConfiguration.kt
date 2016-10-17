package io.pivotal.microservices.accounts

import java.math.BigDecimal
import java.util.Random
import java.util.logging.Logger

import javax.sql.DataSource

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.orm.jpa.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder

/**
 * The accounts Spring configuration.

 * @author Paul Chapman
 */
@Configuration
@ComponentScan
@EntityScan("io.pivotal.microservices.accounts")
@EnableJpaRepositories("io.pivotal.microservices.accounts")
@PropertySource("classpath:db-config.properties")
open class AccountsConfiguration {

    protected var logger: Logger

    init {
        logger = Logger.getLogger(javaClass.getName())
    }

    /**
     * Creates an in-memory "rewards" database populated with test data for fast
     * testing
     */
    @Bean
    open fun dataSource(): DataSource {
        logger.info("dataSource() invoked")

        // Create an in-memory H2 relational database containing some demo
        // accounts.
        val dataSource = EmbeddedDatabaseBuilder().addScript("classpath:testdb/schema.sql").addScript("classpath:testdb/data.sql").build()

        logger.info("dataSource = " + dataSource)

        // Sanity check
        val jdbcTemplate = JdbcTemplate(dataSource)
        val accounts = jdbcTemplate.queryForList("SELECT number FROM T_ACCOUNT")
        logger.info("System has " + accounts.size + " accounts")

        // Populate with random balances
        val rand = Random()

        for (item in accounts) {
            val number = item["number"] as String
            val balance = BigDecimal(rand.nextInt(10000000) / 100.0).setScale(2, BigDecimal.ROUND_HALF_UP)
            jdbcTemplate.update("UPDATE T_ACCOUNT SET balance = ? WHERE number = ?", balance, number)
        }

        return dataSource
    }
}
