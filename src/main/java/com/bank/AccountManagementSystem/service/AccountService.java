package com.bank.AccountManagementSystem.service;

import com.bank.AccountManagementSystem.db.Model.Account;
import com.bank.AccountManagementSystem.db.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public Account createAccount(String accountNumber) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(0.0);
        return accountRepository.save(account);
    }

    public Account deposit(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Аккаунт не найден"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        kafkaTemplate.send("transactions", "Deposit: " + amount + " to " + accountNumber);
        return account;
    }

    public Account withdraw(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Аккаунт не найден"));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Недостаточно средств");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        kafkaTemplate.send("transactions", "Withdraw: " + amount + " from " + accountNumber);
        return account;
    }
}
