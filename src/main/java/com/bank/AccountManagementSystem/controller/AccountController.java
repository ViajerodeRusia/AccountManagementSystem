package com.bank.AccountManagementSystem.controller;

import com.bank.AccountManagementSystem.db.Model.Account;
import com.bank.AccountManagementSystem.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.createAccount(accountNumber));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestParam String accountNumber, @RequestParam Double amount) {
        return ResponseEntity.ok(accountService.deposit(accountNumber, amount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestParam String accountNumber, @RequestParam Double amount) {
        return ResponseEntity.ok(accountService.withdraw(accountNumber, amount));
    }
}
