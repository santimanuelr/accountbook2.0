package com.santidev.accountbook.repository;

import com.santidev.accountbook.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Transaction entity.
 */
public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
