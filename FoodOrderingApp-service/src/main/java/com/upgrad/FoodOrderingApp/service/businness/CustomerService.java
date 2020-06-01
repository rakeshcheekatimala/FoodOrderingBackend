package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.UtilityProvider;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;

@Service
public class CustomerService {
    @Autowired
    CustomerDao customerDao;

    @Autowired
    UtilityProvider utilityProvider; // It Provides Data Check methods for various cases

    @Autowired
    CustomerService customerService; // Handles all the Service Related Customer.

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    CustomerAuthDao customerAuthDao; //Handles all data related to the customerAuthEntity

    PasswordCryptographyProvider cryptographyProvider;

    public CustomerEntity getCustomerByContact(String contactNumber) {
        return customerDao.getCustomerByContact(contactNumber);
    }

    @Transactional
    public CustomerAuthEntity authenticate(String contactNumber, String password) throws AuthenticationFailedException {
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
                customerDao.updateAuthToken(authTokenEntity); // update  auth token if it is existing
            }
            return authTokenEntity;
        }
        else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
    }

    /* This method is to saveCustomer.Takes the customerEntity and saves the Customer to the DB.
    If error throws exception with error code and error message.
    */
    @Transactional
    public CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException {

        //calls getCustomerByContactNumber method of customerDao to check if customer already exists.
        CustomerEntity existingCustomerEntity = customerDao.getCustomerByContact(customerEntity.getContactNumber());

        if (existingCustomerEntity != null) {//Checking if Customer already Exists if yes throws exception.
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number");
        }

        if (!utilityProvider.isValidSignupRequest(customerEntity)) {//Checking if is Valid Signup Request.
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }

        if (!utilityProvider.isEmailValid(customerEntity.getEmail())) {//Checking if email is valid
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }

        if (!utilityProvider.isContactValid(customerEntity.getContactNumber())) {//Checking if Contact is valid
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }

        if (!utilityProvider.isValidPassword(customerEntity.getPassword())) {//Checking if Password is valid.
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        //If all condition are satisfied the password is encoded using passwordCryptographyProvider and encoded password and salt is added to the customerEntity and persisted.
        String[] encryptedPassword = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedPassword[0]);
        customerEntity.setPassword(encryptedPassword[1]);

        //Calls createCustomer of customerDao to create the customer.
        return customerDao.createCustomer(customerEntity);
    }

    /* This method is to updateCustomer the customer using customerEntity and return the CustomerEntity .
       If error throws exception with error code and error message.
    */
    @Transactional
    public CustomerEntity updateCustomer(CustomerEntity customerEntity) throws UpdateCustomerException {

        //Getting the CustomerEntity by getCustomerByUuid of customerDao
        CustomerEntity customerToBeUpdated = customerDao.getCustomerByUuid(customerEntity.getUuid());

        //Setting the new details to the customer entity .
        customerToBeUpdated.setFirstName(customerEntity.getFirstName());
        customerToBeUpdated.setLastName(customerEntity.getLastName());

        //Calls updateCustomer of customerDao to update the customer data in the DB

        return customerDao.updateCustomer(customerEntity);
    }


    /* This method is to updateCustomerPassword the customer using oldPassword,newPassword & customerEntity and return the CustomerEntity .
        If error throws exception with error code and error message.
     */

    @Transactional
    public CustomerEntity updateCustomerPassword(String oldPassword, String newPassword, CustomerEntity customerEntity) throws UpdateCustomerException {

        if (!utilityProvider.isValidPassword(newPassword)) {//Checking if the Password is Weak.
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }

        //Encrypting the oldpassword enter by user.
        String encryptedOldPassword = PasswordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());

        //Checking the oldPassword is correct as stored in the DB
        if (encryptedOldPassword.equals(customerEntity.getPassword())) {
            CustomerEntity tobeUpdatedCustomerEntity = customerDao.getCustomerByUuid(customerEntity.getUuid());

            //Encyprting newPassword to store in the DB
            String[] encryptedPassword = passwordCryptographyProvider.encrypt(newPassword);
            tobeUpdatedCustomerEntity.setSalt(encryptedPassword[0]);
            tobeUpdatedCustomerEntity.setPassword(encryptedPassword[1]);

            //Updating the Customer with the new password adn salt.

            return customerDao.updateCustomer(tobeUpdatedCustomerEntity);

        } else {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
    }

    /* This method is to getCustomer using accessToken and return the CustomerEntity .
        If error throws exception with error code and error message.
    */

    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(accessToken);

        if (customerAuthEntity == null) {//Checking if Customer not logged In
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        if (customerAuthEntity.getLogoutAt() != null) {//Checking if cutomer is logged Out
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        final ZonedDateTime now = ZonedDateTime.now();

        if (customerAuthEntity.getExpiresAt().compareTo(now) <= 0) {//Checking accessToken is Expired.
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        return customerAuthEntity.getCustomer();

    }

    public CustomerAuthEntity logout(CustomerAuthEntity customerAuthEntity){
        return  customerDao.logout(customerAuthEntity);
    }

    public CustomerAuthEntity logout(String accessToken){
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerByToken(accessToken);
        return  customerDao.logout(customerAuthEntity);
    }
}
