package com.santidev.accountbook.rest;

import com.santidev.accountbook.model.Balance;
import com.santidev.accountbook.repository.BalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.santidev.accountbook.model.Balance}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class BalanceResource {

    private static final String ENTITY_NAME = "accountbookBalance";

    private final BalanceRepository balanceRepository;

    public BalanceResource(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    /**
     * {@code POST  /balances} : Create a new balance.
     *
     * @param balance the balance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new balance, or with status {@code 400 (Bad Request)} if the balance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/balances")
    public ResponseEntity<Balance> createBalance(@RequestBody Balance balance) throws URISyntaxException {
        log.debug("REST request to save Balance : {}", balance);
        if (balance.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new balance cannot already have an ID");
        }
        Balance result = balanceRepository.save(balance);
        return ResponseEntity.created(new URI("/api/balances/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /balances} : Updates an existing balance.
     *
     * @param balance the balance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated balance,
     * or with status {@code 400 (Bad Request)} if the balance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the balance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/balances")
    public ResponseEntity<Balance> updateBalance(@RequestBody Balance balance) throws URISyntaxException {
        log.debug("REST request to update Balance : {}", balance);
        if (balance.getId() == null) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        Balance result = balanceRepository.save(balance);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /balances} : get all the balances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of balances in body.
     */
    @GetMapping("/balances")
    public List<Balance> getAllBalances() {
        log.debug("REST request to get all Balances");
        return balanceRepository.findAll();
    }

    /**
     * {@code GET  /balances/:id} : get the "id" balance.
     *
     * @param id the id of the balance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the balance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/balances/{id}")
    public ResponseEntity<Balance> getBalance(@PathVariable String id) {
        log.debug("REST request to get Balance : {}", id);
        Optional<Balance> balance = balanceRepository.findById(id);
        return balance.map(response -> ResponseEntity.ok().body(response))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * {@code DELETE  /balances/:id} : delete the "id" balance.
     *
     * @param id the id of the balance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/balances/{id}")
    public ResponseEntity<Void> deleteBalance(@PathVariable String id) {
        log.debug("REST request to delete Balance : {}", id);
        balanceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
