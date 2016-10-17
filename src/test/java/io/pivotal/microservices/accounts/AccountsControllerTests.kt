package io.pivotal.microservices.accounts

import java.util.ArrayList

import org.junit.Before

class AccountsControllerTests : AbstractAccountControllerTests() {

    protected class TestAccountRepository : AccountRepository {

        override fun findByNumber(accountNumber: String): Account? {
            if (accountNumber == AbstractAccountControllerTests.ACCOUNT_1)
                return theAccount
            else
                return null
        }

        override fun findByOwnerContainingIgnoreCase(partialName: String): List<Account> {
            val accounts = ArrayList<Account>()

            if (AbstractAccountControllerTests.ACCOUNT_1_NAME.toLowerCase().indexOf(partialName.toLowerCase()) != -1)
                accounts.add(theAccount)

            return accounts
        }

        override fun countAccounts(): Int {
            return 1
        }
    }

    protected var testRepo = TestAccountRepository()

    @Before
    fun setup() {
        accountController = AccountsController(testRepo)
    }

    companion object {

        protected val theAccount = Account(AbstractAccountControllerTests.ACCOUNT_1,
                AbstractAccountControllerTests.ACCOUNT_1_NAME)
    }
}
