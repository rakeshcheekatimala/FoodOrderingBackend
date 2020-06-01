package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthService {

    @Autowired
    CustomerAuthDao customerAuthDao;

    public CustomerAuthEntity getCustomerByToken(final String accessToken) {
        return customerAuthDao.getCustomerByToken(accessToken);
    }


    public Boolean isAuthorizedUser(final String accessToken,CustomerAuthEntity existingAuthEntity) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = null;

        if(existingAuthEntity==null)
            customerAuthEntity = customerAuthDao.getCustomerByToken(accessToken);
        else
            customerAuthEntity = existingAuthEntity;

        if(customerAuthEntity == null){
            return false;
        }
        CustomerAuthTokenVerifier customerAuthTokenVerifier = new CustomerAuthTokenVerifier(customerAuthEntity);

        if(customerAuthTokenVerifier.hasLoggedOut()){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        if (customerAuthTokenVerifier.hasExpired()) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        return true; // this indicates the user token is verified
    }

}
