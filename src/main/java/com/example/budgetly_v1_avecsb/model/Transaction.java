package com.example.budgetly_v1_avecsb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(nullable = false)
    protected double amount;

    @Column(nullable = false)
    protected LocalDate date;

    @Column
    protected String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    protected Category category;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    public Transaction(double amount, LocalDate date, String description, Category category) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        if (category == null)
            throw new IllegalArgumentException("Category cannot be null");

        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
    }


    public abstract double calculateImpact();



    // Vérifie si la transaction est un revenu
    public boolean isRevenu() {
        return this instanceof Revenu;
    }

    // Vérifie si la transaction est une dépense
    public boolean isDepense() {
        return this instanceof Depense;
    }

    // Vérifie si la transaction appartient à une catégorie donnée
    public boolean isInCategory(Category c) {
        return this.category.getId() == c.getId();
    }

    // Getters / Setters

    public int getId() { return id; }

    public double getAmount() { return amount; }

    public LocalDate getDate() { return date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", amount=" + amount +
                ", date=" + date + ", description='" + description +
                "', category=" + category + '}';
    }
}



//SINGLE_TABLE qui met Revenu et Depense dans une seule table avec une colonne type
// qui vaut "REVENU" ou "DEPENSE".

//@ManyToOne sur category et user

//@JoinColumn(name = "category_id")  crée une clé étrangère vers la table categories
//@JoinColumn(name = "user_id") crée une clé étrangère vers la table users


