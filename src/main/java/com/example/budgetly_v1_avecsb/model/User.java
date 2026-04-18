package com.example.budgetly_v1_avecsb.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public User(String name, String email, String password) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");
        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");

        this.name = name;
        this.email = email;
        this.password = password;
    }



    public void addTransaction(Transaction t) {
        if (t == null)
            throw new IllegalArgumentException("Transaction cannot be null");
        t.setUser(this); // lie la transaction à cet utilisateur
        transactions.add(t);
    }

    public void removeTransaction(Transaction t) {
        if (t == null)
            throw new IllegalArgumentException("Transaction cannot be null");
        transactions.remove(t);
    }

    // Calcule le solde total  (calculateImpact)
    public double getBalance() {
        double balance = 0;
        for (Transaction t : transactions) {
            balance += t.calculateImpact();
        }
        return balance;
    }

    //Retourne uniquement les dépenses
    public List<Transaction> getDepenses() {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t instanceof Depense) result.add(t);
        }
        return result;
    }

    // Retourne uniquement les revenus
    public List<Transaction> getRevenus() {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t instanceof Revenu) result.add(t);
        }
        return result;
    }

    // Vérifie si le solde est positif
    public boolean isSolvent() {
        return getBalance() >= 0;
    }

    // Getters / Setters

    public int getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public List<Transaction> getTransactions() { return transactions; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}


//@OneToMany(mappedBy = "user") — un utilisateur possède plusieurs transactions.
// Le mappedBy indique que c'est Transaction qui détient la clé étrangère (user_id dans la table)
//cascade = CascadeType.ALL — si on supprime un user, toutes ses transactions sont supprimées aussi
//orphanRemoval = true — si on retire une transaction de la liste, elle est supprimée de la BD automatiquement
//unique = true sur email — deux users ne peuvent pas avoir le même email



//t.setUser(this) dans addTransaction —  Sans ça JPA ne sait pas à quel user appartient la transaction et ne sauvegarde pas la relation en base.