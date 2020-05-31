package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;

@Service
public class CustomerService {
    @Autowired
    CustomerDao customerDao;
    @Autowired
    CustomerAuthDao customerAuthDao;

    PasswordCryptographyProvider cryptographyProvider;
    public CustomerEntity getCustomerByContact(String contactNumber){
        return  customerDao.getCustomerByContact(contactNumber);
    }

    @Transactional
    public CustomerAuthEntity authenticate(String contactNumber,String password) throws AuthenticationFailedException {
        CustomerEntity customerEntity = customerDao.getCustomerByContact(contactNumber); // get customer using contactNumber

        // verify the customerEntity has a result, if the value is null throw AuthenticationFailedException
        if(customerEntity==null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        // if true get the salt & verify the hashed password stored in the db with the generated password
        // by the salt stored in the db

        String salt = customerEntity.getSalt(); // get the salt for this customer
        String hashedPassword = customerEntity.getPassword(); // get the hashedPassword
        final String encryptedPassword = cryptographyProvider.encrypt(password,salt); // generate the encryptedPassword using salt & original passowrd

        if(encryptedPassword.equals(hashedPassword)){
            final ZonedDateTime now = ZonedDateTime.now();
            final JwtTokenProvider tokenProvider = new JwtTokenProvider(customerEntity.getPassword());
            final ZonedDateTime expiresAt = now.plusHours(8);
            final String authToken = tokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt);
            final CustomerAuthEntity authTokenEntity = new CustomerAuthEntity();
            authTokenEntity.setAccessToken(authToken);
            authTokenEntity.setLoginAt(now);
            authTokenEntity.setExpiresAt(expiresAt);
            authTokenEntity.setUuid(customerEntity.getUuid());
            authTokenEntity.setCustomer(customerEntity); // setting the customer
            CustomerAuthEntity isExisting = customerAuthDao.getCustomerById(customerEntity.getId());
            if(isExisting==null){
                customerDao.createAuthToken(authTokenEntity); // create new auth token if it is does not exist
            }
            else{
                authTokenEntity.setId(isExisting.getId());
                authTokenEntity.setLogoutAt(isExisting.getLogoutAt());
                customerDao.updateAuthToken(authTokenEntity); // update  auth token if it is existing
            }
            return authTokenEntity;
        }
        else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
    }

}
