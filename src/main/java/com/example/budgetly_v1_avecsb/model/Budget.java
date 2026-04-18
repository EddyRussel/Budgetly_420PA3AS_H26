package com.example.budgetly_v1_avecsb.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "budgets")
@NoArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private double maxAmount;

    @Column(nullable = false)
    private double currentAmount = 0;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category; // null = budget global

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String period; // ex: "2026-04" pour avril 2026

    public Budget(double maxAmount, Category category, User user, String period) {
        if (maxAmount <= 0)
            throw new IllegalArgumentException("Max amount must be positive");
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        this.maxAmount    = maxAmount;
        this.category     = category;
        this.user         = user;
        this.period       = period;
        this.currentAmount = 0;
    }

    // ── Méthodes métier ───────────────────────────────────

    // Ajoute une dépense au budget
    public void addExpense(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        this.currentAmount += amount;
    }

    // Vérifie si le budget est dépassé
    public boolean isExceeded() {
        return currentAmount > maxAmount;
    }

    // Montant restant (négatif si dépassé)
    public double getRemainingAmount() {
        return maxAmount - currentAmount;
    }

    // Montant du dépassement (0 si pas dépassé)
    public double getExceededAmount() {
        return currentAmount > maxAmount ? currentAmount - maxAmount : 0;
    }

    // Pourcentage utilisé (peut dépasser 100%)
    public double getUsagePercent() {
        return maxAmount > 0 ? (currentAmount / maxAmount) * 100 : 0;
    }

    // Vérifie si le budget approche la limite (>= 80%) */
    public boolean isNearLimit() {
        return getUsagePercent() >= 80 && !isExceeded();
    }

    // Remet le budget à zéro (pour un nouveau mois) */
    public void reset() {
        this.currentAmount = 0;
    }

    //  Getters / Setters

    public int getId() { return id; }

    public double getMaxAmount() { return maxAmount; }
    public void setMaxAmount(double maxAmount) { this.maxAmount = maxAmount; }

    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }

    public Category getCategory() { return category; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    @Override
    public String toString() {
        return "Budget{id=" + id + ", maxAmount=" + maxAmount +
                ", currentAmount=" + currentAmount +
                ", category=" + (category != null ? category.getName() : "Global") +
                ", period='" + period + "'}";
    }
}


//@ManyToOne sur user — un budget appartient à un utilisateur
//period
//Validation dans addExpense — on vérifie que le montant est positif avant de l'ajouter

//getUsagePercent() — retourne le % utilisé,  utile pour les barres de progression du frontend
//isNearLimit() — détecte le seuil d'alerte à 80%, utilisé dans le frontend pour afficher les badges orange
//reset() — remet currentAmount à 0 pour un nouveau mois