package com.edu.neu.project.service;

import com.edu.neu.project.authority.AuthorityEnum;
import com.edu.neu.project.entity.UserAccount;
import com.edu.neu.project.exception.UsernameAlreadyExistException;
import com.edu.neu.project.model.UserAccountInfo;

import java.util.List;

public interface UserService {
    UserAccount register(UserAccount userAccount) throws UsernameAlreadyExistException;

    UserAccount registerCustomer(UserAccountInfo userAccountInfo);

    UserAccount registerProductManager(UserAccountInfo userAccountInfo);

    UserAccount getUserAccountByUsername(String username);

    String getDisplayNameByUsername(String username);

    UserAccount updateDisplayName(String username, String newDisplayName);

    UserAccount changePassword(String username, String password);

    List<UserAccount> getAllByAuthority(AuthorityEnum authorityEnum);

    List<String> getAllProductManagerUsername();

    void deleteUserAccount(UserAccount userAccount);

    Boolean testPassword(String username, String password);
}
