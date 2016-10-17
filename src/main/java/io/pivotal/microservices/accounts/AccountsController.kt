package io.pivotal.microservices.accounts

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import io.pivotal.microservices.exceptions.AccountNotFoundException

/**
 * A RESTFul controller for accessing account information.

 * @author Paul Chapman
 */
@RestController
class AccountsController
/**
 * Create an instance plugging in the respository of Accounts.

 * @param accountRepository
 * *            An account repository implementation.
 */
@Autowired
constructor(protected var accountRepository: AccountRepository) {

    protected var logger = Logger.getLogger(AccountsController::class.java!!.getName())

    init {

        logger.info("AccountRepository says system has "
                + accountRepository.countAccounts() + " accounts")
    }

    /**
     * Fetch an account with the specified account number.

     * @param accountNumber
     * *            A numeric, 9 digit account number.
     * *
     * @return The account if found.
     * *
     * @throws AccountNotFoundException
     * *             If the number is not recognised.
     */
    @RequestMapping("/accounts/{accountNumber}")
    fun byNumber(@PathVariable("accountNumber") accountNumber: String): Account {

        logger.info("accounts-service byNumber() invoked: " + accountNumber)
        val account = accountRepository.findByNumber(accountNumber)
        logger.info("accounts-service byNumber() found: " + account!!)

        if (account == null)
            throw AccountNotFoundException(accountNumber)
        else {
            return account
        }
    }

    /**
     * Fetch accounts with the specified name. A partial case-insensitive match
     * is supported. So `http://.../accounts/owner/a` will find any
     * accounts with upper or lower case 'a' in their name.

     * @param partialName
     * *
     * @return A non-null, non-empty set of accounts.
     * *
     * @throws AccountNotFoundException
     * *             If there are no matches at all.
     */
    @RequestMapping("/accounts/owner/{name}")
    fun byOwner(@PathVariable("name") partialName: String): List<Account> {
        logger.info("accounts-service byOwner() invoked: "
                + accountRepository.javaClass.getName() + " for "
                + partialName)

        val accounts = accountRepository.findByOwnerContainingIgnoreCase(partialName)
        logger.info("accounts-service byOwner() found: " + accounts!!)

        if (accounts == null || accounts.size == 0)
            throw AccountNotFoundException(partialName)
        else {
            return accounts
        }
    }
}
