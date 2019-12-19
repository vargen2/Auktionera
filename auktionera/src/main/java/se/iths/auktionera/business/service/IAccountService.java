package se.iths.auktionera.business.service;

import se.iths.auktionera.business.model.Account;
import se.iths.auktionera.business.model.UpdateAccountRequest;

public interface IAccountService {

    Account getAccount(String authId);


    Account updateAccount(String authId, UpdateAccountRequest updateAccountRequest);
}
