package com.santidev.accountbook.service;

import com.santidev.accountbook.model.Balance;
import com.santidev.accountbook.model.Transaction;
import com.santidev.accountbook.repository.BalanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static com.santidev.accountbook.model.Transaction.CREDIT;
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
    public void notificationMarketingIsNotSendDoToRuleTest() {

        //Given
        //I delete all Notifications
        when(balanceRepository.findByAccountId("2")).thenReturn(List.of(getDummyBalance()));

        //When
        Transaction transaction = new Transaction();
        transaction.setIdUserAccount("2");
        transaction.setType(CREDIT);
        transaction.setAmount(new BigDecimal(20));
        transactionService.refreshBlance(transaction);

        //Then
        verify(balanceRepository).save(any());
        //todo
    }

    private Balance getDummyBalance() {
        return Balance.builder().id("3").total(BigDecimal.ZERO).accountId("2").build();
    }

}
