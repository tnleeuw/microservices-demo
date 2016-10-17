package io.pivotal.microservices.services.web

import org.springframework.util.StringUtils
import org.springframework.validation.Errors

class SearchCriteria {
    var accountNumber: String? = null

    var searchText: String? = null

    val isValid: Boolean
        get() {
            if (StringUtils.hasText(accountNumber))
                return !StringUtils.hasText(searchText)
            else
                return StringUtils.hasText(searchText)
        }

    fun validate(errors: Errors): Boolean {
        if (StringUtils.hasText(accountNumber)) {
            if (accountNumber!!.length != 9)
                errors.rejectValue("accountNumber", "badFormat",
                        "Account number should be 9 digits")
            else {
                try {
                    Integer.parseInt(accountNumber!!)
                } catch (e: NumberFormatException) {
                    errors.rejectValue("accountNumber", "badFormat",
                            "Account number should be 9 digits")
                }

            }

            if (StringUtils.hasText(searchText)) {
                errors.rejectValue("searchText", "nonEmpty",
                        "Cannot specify account number and search text")
            }
        } else if (StringUtils.hasText(searchText)) {
        }// Nothing to do
        else {
            errors.rejectValue("accountNumber", "nonEmpty",
                    "Must specify either an account number or search text")

        }

        return errors.hasErrors()
    }

    override fun toString(): String {
        // TODO Auto-generated method stub
        return (if (StringUtils.hasText(accountNumber))
            "number: " + accountNumber!!
        else
            "") + if (StringUtils.hasText(searchText))
            " text: " + searchText!!
        else
            ""
    }
}
