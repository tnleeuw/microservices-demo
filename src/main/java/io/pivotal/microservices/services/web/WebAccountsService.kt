package io.pivotal.microservices.services.web

import java.util.Arrays
import java.util.logging.Logger

import javax.annotation.PostConstruct

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

import io.pivotal.microservices.exceptions.AccountNotFoundException

/**
 * Hide the access to the microservice inside this local service.

 * @author Paul Chapman
 */
@Service
class WebAccountsService(serviceUrl: String) {

    @Autowired
    @LoadBalanced
    protected var restTemplate: RestTemplate? = null

    protected var serviceUrl: String

    protected var logger = Logger.getLogger(WebAccountsService::class.java!!.getName())

    init {
        this.serviceUrl = if (serviceUrl.startsWith("http"))
            serviceUrl
        else
            "http://" + serviceUrl
    }

    /**
     * The RestTemplate works because it uses a custom request-factory that uses
     * Ribbon to look-up the service to use. This method simply exists to show
     * this.
     */
    @PostConstruct
    fun demoOnly() {
        // Can't do this in the constructor because the RestTemplate injection
        // happens afterwards.
        logger.warning("The RestTemplate request factory is " + restTemplate!!.requestFactory.javaClass)
    }

    fun findByNumber(accountNumber: String): Account {

        logger.info("findByNumber() invoked: for " + accountNumber)
        return restTemplate!!.getForObject<Account>(serviceUrl + "/accounts/{number}", Account::class.java!!, accountNumber)
    }

    fun byOwnerContains(name: String): List<Account>? {
        logger.info("byOwnerContains() invoked:  for " + name)
        var accounts: Array<Account>? = null

        try {
            accounts = restTemplate!!.getForObject<Array<Account>>(serviceUrl + "/accounts/owner/{name}", Array<Account>::class.java!!, name)
        } catch (e: HttpClientErrorException) { // 404
            // Nothing found
        }

        if (accounts == null || accounts.size == 0)
            return null
        else
            return Arrays.asList(*accounts)
    }

    fun getByNumber(accountNumber: String): Account {
        val account = restTemplate!!.getForObject<Account>(serviceUrl + "/accounts/{number}", Account::class.java!!, accountNumber)

        if (account == null)
            throw AccountNotFoundException(accountNumber)
        else
            return account
    }
}
