package com.example.budgetly_v1_avecsb.controller;

import com.example.budgetly_v1_avecsb.model.Budget;
import com.example.budgetly_v1_avecsb.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//ResponseEntity.ok = Réponse avec statut 200 OK et un objet en JSON

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*") //Notre frontend tourne sur le port 5500 avec Live Server et notre backend sur le port 8080.
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // GET /api/budgets/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Budget>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(budgetService.getBudgetsByUser(userId));
    }

    // GET /api/budgets/user/{userId}/period/{period}
    @GetMapping("/user/{userId}/period/{period}")
    public ResponseEntity<List<Budget>> getByUserAndPeriod(
            @PathVariable int userId,
            @PathVariable String period) {
        return ResponseEntity.ok(
                budgetService.getBudgetsByUserAndPeriod(userId, period));
    }

    // GET /api/budgets/user/{userId}/exceeded
    @GetMapping("/user/{userId}/exceeded")
    public ResponseEntity<List<Budget>> getExceeded(@PathVariable int userId) {
        return ResponseEntity.ok(budgetService.getExceededBudgets(userId));
    }

    // GET /api/budgets/user/{userId}/near-limit
    @GetMapping("/user/{userId}/near-limit")
    public ResponseEntity<List<Budget>> getNearLimit(@PathVariable int userId) {
        return ResponseEntity.ok(budgetService.getNearLimitBudgets(userId));
    }

    // GET /api/budgets/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Budget> getById(@PathVariable int id) {
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    // POST /api/budgets
    // Body: { "userId":1, "categoryId":2, "maxAmount":300, "period":"2026-04" }
    @PostMapping
    public ResponseEntity<Budget> create(@RequestBody Map<String, String> body) {
        int    userId     = Integer.parseInt(body.get("userId"));
        int    categoryId = Integer.parseInt(body.get("categoryId"));
        double maxAmount  = Double.parseDouble(body.get("maxAmount"));
        String period     = body.get("period");

        return ResponseEntity.ok(
                budgetService.createBudget(userId, categoryId, maxAmount, period));
    }

    // PUT /api/budgets/{id}
    // Body: { "maxAmount": 400 }
    @PutMapping("/{id}")
    public ResponseEntity<Budget> update(@PathVariable int id,
                                         @RequestBody Map<String, String> body) {
        double newMax = Double.parseDouble(body.get("maxAmount"));
        return ResponseEntity.ok(budgetService.updateBudget(id, newMax));
    }

    // DELETE /api/budgets/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/budgets/{id}/reset
    @PutMapping("/{id}/reset")
    public ResponseEntity<Void> reset(@PathVariable int id) {
        budgetService.resetBudget(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/budgets/user/{userId}/summary
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Object>> getSummary(@PathVariable int userId) {
        return ResponseEntity.ok(Map.of(
                "total",    budgetService.getBudgetsByUser(userId).size(),
                "exceeded", budgetService.countExceeded(userId),
                "budgets",  budgetService.getBudgetsByUser(userId)
        ));
    }
}