package com.example.budgetly_v1_avecsb.repository;

import com.example.budgetly_v1_avecsb.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


//Spring Data JPA génère le SQL automatiquement à partir du nom de la méthode
//findByUserId  >>  SELECT * FROM transactions WHERE user_id = ?
//findByUserIdAndDateBetween  >> SELECT * FROM transactions
//                               WHERE user_id = ? AND date BETWEEN ? AND ?

//Pour les requêtes complexes, on utilise @Query avec du JPQL (c'est comme du SQL mais avec les noms de classes Java)



@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    // Tous les budgets d'un user
    List<Budget> findByUserId(int userId);

    // Budgets d'un user pour une période donnée (ex: "2026-04")
    List<Budget> findByUserIdAndPeriod(int userId, String period);

    // Budget d'un user pour une catégorie et une période
    Optional<Budget> findByUserIdAndCategoryIdAndPeriod(int userId,
                                                        int categoryId,
                                                        String period);

    // Budgets dépassés d'un user
    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId " +
            "AND b.currentAmount > b.maxAmount")
    List<Budget> findExceededBudgets(@Param("userId") int userId);

    // Budgets proches de la limite (>= 80%)
    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId " +
            "AND (b.currentAmount / b.maxAmount) >= 0.8 " +
            "AND b.currentAmount <= b.maxAmount")

    List<Budget> findNearLimitBudgets(@Param("userId") int userId);
}