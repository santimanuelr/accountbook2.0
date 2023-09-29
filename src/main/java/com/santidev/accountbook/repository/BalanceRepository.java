package com.santidev.accountbook.repository;

import com.santidev.accountbook.model.Balance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Balance entity.
 */
public interface BalanceRepository extends MongoRepository<Balance, String> {

	List<Balance> findByAccountId(String idUserAccount);

}
