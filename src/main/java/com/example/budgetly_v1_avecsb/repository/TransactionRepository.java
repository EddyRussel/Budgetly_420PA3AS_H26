package com.example.budgetly_v1_avecsb.repository;

import com.example.budgetly_v1_avecsb.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

//Pour les requêtes complexes, on utilise @Query avec du JPQL (c'est comme du SQL mais avec les noms de classes Java)

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // Toutes les transactions d'un user
    List<Transaction> findByUserId(int userId);

    // Transactions d'un user triées par date décroissante
    List<Transaction> findByUserIdOrderByDateDesc(int userId);

    // Transactions d'un user entre deux dates
    List<Transaction> findByUserIdAndDateBetween(int userId, LocalDate start, LocalDate end);

    // Transactions d'un user pour une catégorie donnée
    List<Transaction> findByUserIdAndCategoryId(int userId, int categoryId);

    // Transactions d'un user pour un mois/année donnés
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
            "AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    List<Transaction> findByUserIdAndMonthAndYear(@Param("userId") int userId,
                                                  @Param("month") int month,
                                                  @Param("year") int year);

    // Seulement les dépenses d'un user
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
            "AND TYPE(t) = Depense ORDER BY t.date DESC")
    List<Transaction> findDepensesByUserId(@Param("userId") int userId);

    // Seulement les revenus d'un user
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
            "AND TYPE(t) = Revenu ORDER BY t.date DESC")
    List<Transaction> findRevenusByUserId(@Param("userId") int userId);
}