package com.example.budgetly_v1_avecsb.service;

import com.example.budgetly_v1_avecsb.model.Budget;
import com.example.budgetly_v1_avecsb.model.Category;
import com.example.budgetly_v1_avecsb.model.User;
import com.example.budgetly_v1_avecsb.repository.BudgetRepository;
import com.example.budgetly_v1_avecsb.repository.CategoryRepository;
import com.example.budgetly_v1_avecsb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository   budgetRepository;
    private final UserRepository     userRepository;
    private final CategoryRepository categoryRepository;

    public BudgetService(BudgetRepository budgetRepository,
                         UserRepository userRepository,
                         CategoryRepository categoryRepository) {
        this.budgetRepository   = budgetRepository;
        this.userRepository     = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Budget> getBudgetsByUser(int userId) {
        return budgetRepository.findByUserId(userId);
    }

    public List<Budget> getBudgetsByUserAndPeriod(int userId, String period) {
        return budgetRepository.findByUserIdAndPeriod(userId, period);
    }

    public List<Budget> getExceededBudgets(int userId) {
        return budgetRepository.findExceededBudgets(userId);
    }

    public List<Budget> getNearLimitBudgets(int userId) {
        return budgetRepository.findNearLimitBudgets(userId);
    }

    public Budget getBudgetById(int id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget introuvable : " + id));
    }

    public Budget createBudget(int userId, int categoryId,
                               double maxAmount, String period) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        // Vérifie qu'un budget n'existe pas déjà pour cette catégorie/période
        budgetRepository
                .findByUserIdAndCategoryIdAndPeriod(userId, categoryId, period)
                .ifPresent(b -> {
                    throw new RuntimeException(
                            "Un budget existe déjà pour cette catégorie et cette période");
                });

        Budget budget = new Budget(maxAmount, category, user, period);
        return budgetRepository.save(budget);
    }

    public Budget updateBudget(int id, double newMaxAmount) {
        Budget budget = getBudgetById(id);
        budget.setMaxAmount(newMaxAmount);
        return budgetRepository.save(budget);
    }

    public void deleteBudget(int id) {
        getBudgetById(id);
        budgetRepository.deleteById(id);
    }

    public void resetBudget(int id) {
        Budget budget = getBudgetById(id);
        budget.reset();
        budgetRepository.save(budget);
    }

    // Résumé rapide pour le dashboard
    public int countExceeded(int userId) {
        return budgetRepository.findExceededBudgets(userId).size();
    }
}