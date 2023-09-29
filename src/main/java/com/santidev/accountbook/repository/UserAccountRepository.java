package com.santidev.accountbook.repository;

import com.santidev.accountbook.model.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the UserAccount entity.
 */
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
}
