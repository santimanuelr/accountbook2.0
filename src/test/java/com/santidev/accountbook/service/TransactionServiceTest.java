package com.santidev.accountbook.service;

import com.santidev.accountbook.model.Balance;
import com.santidev.accountbook.model.Transaction;
import com.santidev.accountbook.repository.BalanceRepository;
import com.santidev.accountbook.rest.Exceptions.NegativeBalanceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static com.santidev.accountbook.model.Transaction.CREDIT;
import static com.santidev.accountbook.model.Transaction.DEBIT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private BalanceRepository balanceRepository;

    @Test
    public void refreshBalanceWithCreditTransactionTest() {

        //Given
        //I delete all Notifications
        when(balanceRepository.findByAccountId("2")).thenReturn(List.of(getDummyBalance()));

        //When
        Transaction transaction = getDummyTransaction(CREDIT, 20);
        transactionService.refreshBlance(transaction);

        //Then
        verify(balanceRepository).save(any());
    }

    private Balance getDummyBalance() {
        return Balance.builder().id("3").total(BigDecimal.ZERO).accountId("2").build();
    }

    @Test
    public void negativeBalanceExceptionTest() {

        //Given
        //I delete all Notifications
        when(balanceRepository.findByAccountId("2")).thenReturn(List.of(getDummyBalanceWithMoney()));

        //When
        Transaction transaction = getDummyTransaction(DEBIT, 40);

        //Then
        Assertions.assertThrows(NegativeBalanceException.class, () -> transactionService.processTransaction(transaction));
    }

    private static Transaction getDummyTransaction(String type, int amount) {
        return Transaction.builder().idUserAccount("2").type(type).amount(new BigDecimal(amount)).build();
    }

    private Balance getDummyBalanceWithMoney() {
        return Balance.builder().id("3").total(new BigDecimal(30)).accountId("2").build();
    }

}
