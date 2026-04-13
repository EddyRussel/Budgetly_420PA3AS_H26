package com.example.budgetly_v1_avecsb.model;

import java.time.LocalDate;
import java.util.List;

public final class PredictionService {

    private PredictionService() {}

    /**
     * Calcule la dépense moyenne journalière sur les N derniers jours
     * (plus fiable qu'une seule date)
     */
    public static double calculateDailyAverage(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) return 0;

        double totalExpense = 0;
        int nbJours = 30; // fenêtre d'analyse : 30 derniers jours
        LocalDate depuis = LocalDate.now().minusDays(nbJours);

        for (Transaction t : transactions) {
            if (t.getDate().isAfter(depuis) && t.calculateImpact() < 0) {
                totalExpense += Math.abs(t.calculateImpact());
            }
        }

        return totalExpense / nbJours;
    }

    /**
     * Prédit le nombre de jours restants avant épuisement du solde
     */
    public static int predictRemainingDays(List<Transaction> transactions, double currentBalance) {
        if (currentBalance <= 0) return 0;

        double dailyAverage = calculateDailyAverage(transactions);

        if (dailyAverage == 0) return Integer.MAX_VALUE;
        return (int) (currentBalance / dailyAverage);
    }

    /**
     * Retourne la date estimée d'épuisement du budget
     */
    public static LocalDate predictBreakDate(List<Transaction> transactions, double currentBalance) {
        int days = predictRemainingDays(transactions, currentBalance);

        if (days == Integer.MAX_VALUE) return null;
        return LocalDate.now().plusDays(days);
    }

    /**
     * Vérifie si une alerte doit être déclenchée (moins de N jours restants)
     */
    public static boolean shouldAlert(List<Transaction> transactions, double currentBalance, int seuilJours) {
        int remaining = predictRemainingDays(transactions, currentBalance);
        return remaining != Integer.MAX_VALUE && remaining <= seuilJours;
    }
}