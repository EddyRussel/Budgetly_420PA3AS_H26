package com.example.budgetly_v1_avecsb.controller;

import com.example.budgetly_v1_avecsb.model.Category;
import com.example.budgetly_v1_avecsb.model.Transaction;
import com.example.budgetly_v1_avecsb.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // GET /api/transactions/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }

    // GET /api/transactions/user/{userId}/depenses
    @GetMapping("/user/{userId}/depenses")
    public ResponseEntity<List<Transaction>> getDepenses(@PathVariable int userId) {
        return ResponseEntity.ok(transactionService.getDepensesByUser(userId));
    }

    // GET /api/transactions/user/{userId}/revenus
    @GetMapping("/user/{userId}/revenus")
    public ResponseEntity<List<Transaction>> getRevenus(@PathVariable int userId) {
        return ResponseEntity.ok(transactionService.getRevenusByUser(userId));
    }

    // GET /api/transactions/user/{userId}/month?month=4&year=2026
    @GetMapping("/user/{userId}/month")
    public ResponseEntity<List<Transaction>> getByMonth(
            @PathVariable int userId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByMonth(userId, month, year));
    }

    // GET /api/transactions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable int id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    // POST /api/transactions
    // Body: { "userId":1, "categoryId":2, "type":"DEPENSE",
    //         "amount":62.40, "date":"2026-04-09", "description":"IGA" }
    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Map<String, String> body) {
        int       userId      = Integer.parseInt(body.get("userId"));
        int       categoryId  = Integer.parseInt(body.get("categoryId"));
        String    type        = body.get("type");
        double    amount      = Double.parseDouble(body.get("amount"));
        LocalDate date        = LocalDate.parse(body.get("date"));
        String    description = body.get("description");

        Transaction saved = transactionService.createTransaction(
                userId, categoryId, type, amount, date, description);

        return ResponseEntity.ok(saved);
    }

    // DELETE /api/transactions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    // ── Stats & Dashboard ─────────────────────────────────

    // GET /api/transactions/user/{userId}/balance
    @GetMapping("/user/{userId}/balance")
    public ResponseEntity<Map<String, Double>> getBalance(@PathVariable int userId) {
        return ResponseEntity.ok(Map.of(
                "balance",      transactionService.getBalance(userId),
                "totalIncome",  transactionService.getTotalIncome(userId),
                "totalExpense", transactionService.getTotalExpense(userId)
        ));
    }

    // GET /api/transactions/user/{userId}/stats/month?month=4&year=2026
    @GetMapping("/user/{userId}/stats/month")
    public ResponseEntity<Map<String, Double>> getMonthlyStats(
            @PathVariable int userId,
            @RequestParam int month) {
        Month m = Month.of(month);
        return ResponseEntity.ok(Map.of(
                "monthlyIncome",  transactionService.getMonthlyIncome(userId, m),
                "monthlyExpense", transactionService.getMonthlyExpense(userId, m),
                "savingsRate",    transactionService.getSavingsRate(userId)
        ));
    }

    // GET /api/transactions/user/{userId}/categories/expenses
    @GetMapping("/user/{userId}/categories/expenses")
    public ResponseEntity<Map<Category, Double>> getExpensesByCategory(
            @PathVariable int userId) {
        return ResponseEntity.ok(transactionService.getExpensesByCategory(userId));
    }

    // ── Prévisions ────────────────────────────────────────

    // GET /api/transactions/user/{userId}/prediction
    @GetMapping("/user/{userId}/prediction")
    public ResponseEntity<Map<String, Object>> getPrediction(@PathVariable int userId) {
        int       remainingDays = transactionService.getRemainingDays(userId);
        LocalDate breakDate     = transactionService.getBreakDate(userId);
        double    dailyAverage  = transactionService.getDailyAverage(userId);
        boolean   alert         = transactionService.shouldAlert(userId, 15);

        return ResponseEntity.ok(Map.of(
                "remainingDays", remainingDays,
                "breakDate",     breakDate != null ? breakDate.toString() : "N/A",
                "dailyAverage",  dailyAverage,
                "alert",         alert
        ));
    }
}