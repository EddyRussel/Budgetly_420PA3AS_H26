package com.example.budgetly_v1_avecsb.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Report {

    private Report() {}

    // Total des revenus
    public static double getTotalIncome(List<Transaction> transactions) {
        double total = 0;
        for (Transaction t : transactions) {
            if (t == null)
                throw new IllegalArgumentException("Transaction cannot be null");
            if (t.calculateImpact() > 0)
                total += t.calculateImpact();
        }
        return total;
    }

    // Total des dépenses (valeur positive)
    public static double getTotalExpense(List<Transaction> transactions) {
        double total = 0;
        for (Transaction t : transactions) {
            if (t == null)
                throw new IllegalArgumentException("Transaction cannot be null");
            if (t.calculateImpact() < 0)
                total += t.calculateImpact();
        }
        return Math.abs(total);
    }

    //Solde net = revenus - dépenses
    public static double getNetBalance(List<Transaction> transactions) {
        return getTotalIncome(transactions) - getTotalExpense(transactions);
    }

    //Dépenses regroupées par catégorie
    public static Map<Category, Double> getExpensesByCategory(List<Transaction> transactions) {
        Map<Category, Double> map = new HashMap<>();
        for (Transaction t : transactions) {
            if (t == null)
                throw new IllegalArgumentException("Transaction cannot be null");
            if (t.calculateImpact() < 0) {
                Category cat    = t.getCategory();
                double   amount = Math.abs(t.calculateImpact());
                map.put(cat, map.getOrDefault(cat, 0.0) + amount);
            }
        }
        return map;
    }

    // Filtrage par plage de dates (inclusive)
    public static List<Transaction> filterByDate(LocalDate start, LocalDate end,
                                                 List<Transaction> transactions) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            LocalDate date = t.getDate();
            if (!date.isBefore(start) && !date.isAfter(end))
                result.add(t);
        }
        return result;
    }

    // Transactions d'un mois donné
    public static List<Transaction> getMonthlyReport(Month month,
                                                     List<Transaction> transactions) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getDate().getMonth() == month)
                filtered.add(t);
        }
        return filtered;
    }

    // Transactions d'un mois + année précis
    public static List<Transaction> getMonthlyReport(Month month, int year,
                                                     List<Transaction> transactions) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getDate().getMonth() == month && t.getDate().getYear() == year)
                filtered.add(t);
        }
        return filtered;
    }

    // Transactions d'un jour précis
    public static List<Transaction> getDailyReport(LocalDate date,
                                                   List<Transaction> transactions) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : transactions) {
            if (date.equals(t.getDate()))
                filtered.add(t);
        }
        return filtered;
    }

    //Dépenses d'un mois donné
    public static double getMonthlyExpense(Month month, List<Transaction> transactions) {
        return getTotalExpense(getMonthlyReport(month, transactions));
    }

    // Revenus d'un mois donné
    public static double getMonthlyIncome(Month month, List<Transaction> transactions) {
        return getTotalIncome(getMonthlyReport(month, transactions));
    }

    // Catégorie où on dépense le plus
    public static Category getTopSpendingCategory(List<Transaction> transactions) {
        Map<Category, Double> map = getExpensesByCategory(transactions);
        Category top    = null;
        double   max    = 0;
        for (Map.Entry<Category, Double> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                top = entry.getKey();
            }
        }
        return top;
    }

    //Taux d'épargne en % (revenus - dépenses) / revenus
    public static double getSavingsRate(List<Transaction> transactions) {
        double income = getTotalIncome(transactions);
        if (income == 0) return 0;
        return (getNetBalance(transactions) / income) * 100;
    }
}