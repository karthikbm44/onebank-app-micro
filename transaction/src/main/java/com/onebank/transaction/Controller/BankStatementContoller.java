package com.onebank.transaction.Controller;

import com.onebank.transaction.Entity.Transaction;
import com.onebank.transaction.Service.BankStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class BankStatementContoller {

    @Autowired
    private BankStatementService service;

    @GetMapping("/bankStatement")
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String fromDate,
                                                   @RequestParam String toDate){

        return service.generateBankStatement(accountNumber,fromDate,toDate);
    }
}
