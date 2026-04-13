package com.example.budgetly_v1_avecsb.service;

import com.example.budgetly_v1_avecsb.model.*;
import com.example.budgetly_v1_avecsb.repository.BudgetRepository;
import com.example.budgetly_v1_avecsb.repository.TransactionRepository;
import com.example.budgetly_v1_avecsb.repository.UserRepository;
import com.example.budgetly_v1_avecsb.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository        userRepository;
    private final CategoryRepository    categoryRepository;
    private final BudgetRepository      budgetRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              CategoryRepository categoryRepository,
                              BudgetRepository budgetRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository        = userRepository;
        this.categoryRepository    = categoryRepository;
        this.budgetRepository      = budgetRepository;
    }

    // ── Lecture ───────────────────────────────────────────

    public List<Transaction> getTransactionsByUser(int userId) {
        return transactionRepository.findByUserIdOrderByDateDesc(userId);
    }

    public List<Transaction> getDepensesByUser(int userId) {
        return transactionRepository.findDepensesByUserId(userId);
    }

    public List<Transaction> getRevenusByUser(int userId) {
        return transactionRepository.findRevenusByUserId(userId);
    }

    public List<Transaction> getTransactionsByMonth(int userId, int month, int year) {
        return transactionRepository.findByUserIdAndMonthAndYear(userId, month, year);
    }

    public List<Transaction> getTransactionsByDateRange(int userId,
                                                        LocalDate start,
                                                        LocalDate end) {
        return transactionRepository.findByUserIdAndDateBetween(userId, start, end);
    }

    public Transaction getTransactionById(int id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction introuvable : " + id));
    }

    // ── Création ──────────────────────────────────────────

    public Transaction createTransaction(int userId, int categoryId,
                                         String type, double amount,
                                         LocalDate date, String description) {
        User     user     = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        // Crée Revenu ou Depense selon le type
        Transaction transaction;
        if ("REVENU".equalsIgnoreCase(type)) {
            transaction = new Revenu(amount, date, description, category);
        } else if ("DEPENSE".equalsIgnoreCase(type)) {
            transaction = new Depense(amount, date, description, category);
        } else {
            throw new RuntimeException("Type invalide : " + type + ". Valeurs acceptées : REVENU, DEPENSE");
        }

        transaction.setUser(user);
        Transaction saved = transactionRepository.save(transaction);

        // Met à jour le budget si c'est une dépense
        if (saved instanceof Depense) {
            updateBudgetOnExpense(userId, categoryId, amount, date);
        }

        return saved;
    }

    // ── Suppression ───────────────────────────────────────

    public void deleteTransaction(int id) {
        Transaction t = getTransactionById(id);

        // Si c'est une dépense, on décrémente le budget
        if (t instanceof Depense) {
            String period = t.getDate().getYear() + "-"
                    + String.format("%02d", t.getDate().getMonthValue());

            budgetRepository
                    .findByUserIdAndCategoryIdAndPeriod(
                            t.getUser().getId(),
                            t.getCategory().getId(),
                            period)
                    .ifPresent(b -> {
                        b.setCurrentAmount(
                                Math.max(0, b.getCurrentAmount() - t.getAmount()));
                        budgetRepository.save(b);
                    });
        }

        transactionRepository.deleteById(id);
    }

    // ── Statistiques (via classe Report) ──────────────────

    public double getTotalIncome(int userId) {
        return Report.getTotalIncome(getTransactionsByUser(userId));
    }

    public double getTotalExpense(int userId) {
        return Report.getTotalExpense(getTransactionsByUser(userId));
    }

    public double getBalance(int userId) {
        return Report.getNetBalance(getTransactionsByUser(userId));
    }

    public Map<Category, Double> getExpensesByCategory(int userId) {
        return Report.getExpensesByCategory(getTransactionsByUser(userId));
    }

    public double getMonthlyIncome(int userId, Month month) {
        return Report.getMonthlyIncome(month, getTransactionsByUser(userId));
    }

    public double getMonthlyExpense(int userId, Month month) {
        return Report.getMonthlyExpense(month, getTransactionsByUser(userId));
    }

    public Category getTopSpendingCategory(int userId) {
        return Report.getTopSpendingCategory(getTransactionsByUser(userId));
    }

    public double getSavingsRate(int userId) {
        return Report.getSavingsRate(getTransactionsByUser(userId));
    }

    // ── Prévisions (via classe PredictionService) ─────────

    public double getDailyAverage(int userId) {
        return PredictionService.calculateDailyAverage(getTransactionsByUser(userId));
    }

    public int getRemainingDays(int userId) {
        List<Transaction> transactions = getTransactionsByUser(userId);
        double balance = Report.getNetBalance(transactions);
        return PredictionService.predictRemainingDays(transactions, balance);
    }

    public LocalDate getBreakDate(int userId) {
        List<Transaction> transactions = getTransactionsByUser(userId);
        double balance = Report.getNetBalance(transactions);
        return PredictionService.predictBreakDate(transactions, balance);
    }

    public boolean shouldAlert(int userId, int seuilJours) {
        List<Transaction> transactions = getTransactionsByUser(userId);
        double balance = Report.getNetBalance(transactions);
        return PredictionService.shouldAlert(transactions, balance, seuilJours);
    }

    // ── Méthode privée ────────────────────────────────────
    //Quand un utilisateur crée une dépense, il faut automatiquement mettre à jour le currentAmount du budget correspondant.

    private void updateBudgetOnExpense(int userId, int categoryId,
                                       double amount, LocalDate date) {
        //On construit la période au format "2026-04" à partir de la date de la transaction.
        String period = date.getYear() + "-" + String.format("%02d", date.getMonthValue());

        budgetRepository
                .findByUserIdAndCategoryIdAndPeriod(userId, categoryId, period)
                //On cherche en base le budget qui correspond exactement à ce user + cette catégorie + cette période
                // Cette méthode retourne un Optional<Budget> ie soit un budget trouvé, soit rien.
                .ifPresent(b -> {
                    //si un budget existe pour cette catégorie ce mois-ci, alors...
                    b.addExpense(amount);
                    budgetRepository.save(b);
                });
    }
}