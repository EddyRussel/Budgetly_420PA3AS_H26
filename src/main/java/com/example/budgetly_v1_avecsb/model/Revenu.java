package com.example.budgetly_v1_avecsb.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("REVENU")
@NoArgsConstructor
public class Revenu extends Transaction {

    public Revenu(double amount, LocalDate date, String description, Category category) {
        super(amount, date, description, category);
    }

    @Override
    public double calculateImpact() {
        return amount; // impact positif sur le solde
    }
}

//@DiscriminatorValue — c'est ce qui remplit la colonne type dans la table transactions
// Quand on cré un Revenu, JPA écrit automatiquement "REVENU" dans cette colonne


//@NoArgsConstructor — requis par JPA, comme pour toutes les entités

//id  géré par MySQL automatiquement, comme dans Transaction


