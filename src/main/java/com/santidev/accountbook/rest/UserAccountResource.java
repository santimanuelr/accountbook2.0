package com.santidev.accountbook.rest;

import com.santidev.accountbook.model.Balance;
import com.santidev.accountbook.model.UserAccount;
import com.santidev.accountbook.repository.BalanceRepository;
import com.santidev.accountbook.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.santidev.accountbook.model.UserAccount}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class UserAccountResource {

    private static final String ENTITY_NAME = "accountbookUserAccount";


    private final UserAccountRepository userAccountRepository;
    private final BalanceRepository balanceRepository;

    public UserAccountResource(UserAccountRepository userAccountRepository,
                               BalanceRepository balanceRepository) {
        this.userAccountRepository = userAccountRepository;
        this.balanceRepository = balanceRepository;
    }

    /**
     * {@code POST  /user-accounts} : Create a new userAccount.
     *
     * @param userAccount the userAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAccount, or with status {@code 400 (Bad Request)} if the userAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-accounts")
    public ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccount userAccount) throws URISyntaxException {
        log.debug("REST request to save UserAccount : {}", userAccount);
        if (userAccount.getId() != null) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new userAccount cannot already have an ID");
        }
        UserAccount result = userAccountRepository.save(userAccount);
        Balance newBalance = new Balance();
        
        //For testing we create a balance too. To prevent execute other request
        newBalance.setAccountId(result.getId());
        newBalance.setTotal(new BigDecimal(0l));
        balanceRepository.save(newBalance);
        
        return ResponseEntity.created(new URI("/api/user-accounts/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /user-accounts} : Updates an existing userAccount.
     *
     * @param userAccount the userAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccount,
     * or with status {@code 400 (Bad Request)} if the userAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-accounts")
    public ResponseEntity<UserAccount> updateUserAccount(@RequestBody UserAccount userAccount) throws URISyntaxException {
        log.debug("REST request to update UserAccount : {}", userAccount);
        if (userAccount.getId() == null) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        UserAccount result = userAccountRepository.save(userAccount);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * {@code GET  /user-accounts} : get all the userAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAccounts in body.
     */
    @GetMapping("/user-accounts")
    public List<UserAccount> getAllUserAccounts() {
        log.debug("REST request to get all UserAccounts");
        return userAccountRepository.findAll();
    }

    /**
     * {@code GET  /user-accounts/:id} : get the "id" userAccount.
     *
     * @param id the id of the userAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-accounts/{id}")
    public ResponseEntity<UserAccount> getUserAccount(@PathVariable String id) {
        log.debug("REST request to get UserAccount : {}", id);
        Optional<UserAccount> userAccount = userAccountRepository.findById(id);
        return userAccount.map(response -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * {@code DELETE  /user-accounts/:id} : delete the "id" userAccount.
     *
     * @param id the id of the userAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-accounts/{id}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable String id) {
        log.debug("REST request to delete UserAccount : {}", id);
        userAccountRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
