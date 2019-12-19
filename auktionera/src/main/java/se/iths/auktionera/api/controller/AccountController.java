package se.iths.auktionera.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.iths.auktionera.business.model.Account;
import se.iths.auktionera.business.model.UpdateAccountRequest;
import se.iths.auktionera.business.service.IAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("api/account")
    public Account getAccount(HttpServletRequest request) {
        return accountService.getAccount((String) request.getAttribute("authId"));
    }

    @PatchMapping("api/account")
    public Account updateAccount(@Valid @RequestBody UpdateAccountRequest fields, HttpServletRequest request) {
        return accountService.updateAccount((String) request.getAttribute("authId"), fields);
    }
}
