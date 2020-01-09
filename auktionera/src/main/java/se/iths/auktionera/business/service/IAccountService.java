package se.iths.auktionera.business.service;

import se.iths.auktionera.business.model.Account;
import se.iths.auktionera.business.model.UpdateAccountRequest;
import se.iths.auktionera.security.UserPrincipal;

public interface IAccountService {

    Account getAccount(UserPrincipal userPrincipal);

    Account updateAccount(UserPrincipal userPrincipal, UpdateAccountRequest updateAccountRequest);
}
