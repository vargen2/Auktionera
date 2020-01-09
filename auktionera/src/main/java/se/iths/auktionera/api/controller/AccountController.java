package se.iths.auktionera.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.iths.auktionera.business.model.Account;
import se.iths.auktionera.business.model.UpdateAccountRequest;
import se.iths.auktionera.business.service.IAccountService;
import se.iths.auktionera.security.CurrentUser;
import se.iths.auktionera.security.UserPrincipal;

import javax.validation.Valid;

@RestController
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("api/account")
    @PreAuthorize("hasRole('USER')")
    public Account getAccount(@CurrentUser UserPrincipal userPrincipal) {
        return accountService.getAccount(userPrincipal);
    }

    @PatchMapping("api/account")
    @PreAuthorize("hasRole('USER')")
    public Account updateAccount(@Valid @RequestBody UpdateAccountRequest fields, @CurrentUser UserPrincipal userPrincipal) {
        return accountService.updateAccount(userPrincipal, fields);
    }
}
