package com.santidev.accountbook.service;


import com.santidev.accountbook.model.Balance;
import com.santidev.accountbook.model.Transaction;
import com.santidev.accountbook.repository.BalanceRepository;
import com.santidev.accountbook.repository.TransactionRepository;
import com.santidev.accountbook.rest.Exceptions.NegativeBalanceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.santidev.accountbook.model.Transaction.CREDIT;
import static com.santidev.accountbook.model.Transaction.DEBIT;

@Service
@Slf4j
public class TransactionService {

	private final BalanceRepository balanceRepository;
	private final TransactionRepository transactionRepository;

	public TransactionService(BalanceRepository balanceRepository, TransactionRepository transactionRepository) {
		this.balanceRepository = balanceRepository;
		this.transactionRepository = transactionRepository;
	}


	public void customTransactionValidations(Transaction transaction) throws Exception {
		if (!DEBIT.equalsIgnoreCase(transaction.getType())) {
			return;
		}
		List<Balance> balances = balanceRepository.findByAccountId(transaction.getIdUserAccount());
		Optional<Balance> balance = Optional.ofNullable(balances.stream().findFirst().orElse(null));
		if (balance.isPresent()) {
			checkNegativeBalnce(balance.get(), transaction);
		}
    }
	

	private void checkNegativeBalnce(Balance balance, Transaction transaction) throws Exception {
		if (balance.getTotal().subtract(transaction.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
			throw new NegativeBalanceException(HttpStatus.BAD_REQUEST, "Balnce can't reach negative values");
		}
		return;
	}

	public Transaction processTransaction(Transaction transaction) {
		try {
			customTransactionValidations(transaction);
			refreshBlance(transaction);
		} catch (NegativeBalanceException e) {
			throw e;
		} catch (Exception ex) {
			log.error("Fail in createTransaction", ex);
		}
		return transactionRepository.save(transaction);
	}

	
	public void refreshBlance(Transaction transaction) {
		List<Balance> balances = balanceRepository.findByAccountId(transaction.getIdUserAccount());
		Optional<Balance> balance = Optional.ofNullable(balances.stream().findFirst().orElse(null));
		balance.ifPresent(b -> {
			refreshTotal(transaction, b);
			balanceRepository.save(b);
		});
	}

	
	private void refreshTotal(Transaction transaction, Balance b) {
		if (DEBIT.equalsIgnoreCase(transaction.getType())) {
			b.setTotal(b.getTotal().subtract(transaction.getAmount()));
		}
		if (CREDIT.equalsIgnoreCase(transaction.getType())) {
			b.setTotal(b.getTotal().add(transaction.getAmount()));
		}
	} 

}
