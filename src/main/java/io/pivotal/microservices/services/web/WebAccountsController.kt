package io.pivotal.microservices.services.web

import io.pivotal.microservices.services.web.Account
import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * Client controller, fetches Account info from the microservice via
 * [WebAccountsService].

 * @author Paul Chapman
 */
@Controller
class WebAccountsController(

        @Autowired
        protected var accountsService: WebAccountsService) {

    protected var logger = Logger.getLogger(WebAccountsController::class.java.getName())

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.setAllowedFields("accountNumber", "searchText")
    }

    @RequestMapping("/accounts")
    fun goHome(): String {
        return "index"
    }

    @RequestMapping("/accounts/{accountNumber}")
    fun byNumber(model: Model,
                 @PathVariable("accountNumber") accountNumber: String): String {

        logger.info("web-service byNumber() invoked: " + accountNumber)

        val account = accountsService.findByNumber(accountNumber)
        logger.info("web-service byNumber() found: " + account)
        model.addAttribute("account", account)
        return "account"
    }

    @RequestMapping("/accounts/owner/{text}")
    fun ownerSearch(model: Model, @PathVariable("text") name: String): String {
        logger.info("web-service byOwner() invoked: " + name)

        val accounts = accountsService.byOwnerContains(name)
        logger.info("web-service byOwner() found: " + accounts ?: "<nothing found>")
        model.addAttribute("search", name)
        if (accounts != null)
            model.addAttribute("accounts", accounts)
        return "accounts"
    }

    @RequestMapping(value = "/accounts/search", method = arrayOf(RequestMethod.GET))
    fun searchForm(model: Model): String {
        model.addAttribute("searchCriteria", SearchCriteria())
        return "accountSearch"
    }

    @RequestMapping(value = "/accounts/dosearch")
    fun doSearch(model: Model, criteria: SearchCriteria,
                 result: BindingResult): String {
        logger.info("web-service search() invoked: " + criteria)

        criteria.validate(result)

        if (result.hasErrors())
            return "accountSearch"

        val accountNumber = criteria.accountNumber
        if (StringUtils.hasText(accountNumber)) {
            return byNumber(model, accountNumber!!)
        } else {
            // A valid SearchCriteria has either accountNumber not null, or searchText not null.
            // Since accountNumber was empty, searchText must be non-null.
            val searchText = criteria.searchText
            return ownerSearch(model, searchText!!)
        }
    }
}
