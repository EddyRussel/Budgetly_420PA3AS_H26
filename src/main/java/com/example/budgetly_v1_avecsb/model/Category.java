package com.example.budgetly_v1_avecsb.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type;

    public Category(String name, CategoryType type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        this.name = name;
        this.type = type;
    }



    /** Vérifie si c'est une catégorie de dépense */
    public boolean isExpense() {
        return this.type == CategoryType.DEPENSE;
    }

    /** Vérifie si c'est une catégorie de revenu */
    public boolean isIncome() {
        return this.type == CategoryType.REVENU;
    }

    // ── Getters / Setters ─────────────────────────────────

    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CategoryType getType() { return type; }

    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "', type=" + type + '}';
    }
}


//@Entity — dit à Spring que cette classe devient une table MySQL
//@Table(name = "categories") — nom de la table dans la base
//@Id + @GeneratedValue — l'id est auto-incrémenté par MySQL, on n'a plus besoin de le passer manuellement
//@Enumerated(EnumType.STRING) — stocke "REVENU" ou "DEPENSE" en texte dans la BD (plus lisible que des chiffres)
//@Column(nullable = false) — empêche les valeurs nulles en base

//@NoArgsConstructor (Lombok) — JPA exige un constructeur vide, Lombok le génère pour toi.