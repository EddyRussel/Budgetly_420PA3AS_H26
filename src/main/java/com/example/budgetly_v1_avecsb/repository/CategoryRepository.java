package com.example.budgetly_v1_avecsb.repository;

import com.example.budgetly_v1_avecsb.model.Category;
import com.example.budgetly_v1_avecsb.model.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


//Spring Data JPA génère le SQL automatiquement à partir du nom de la méthode

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // SELECT * FROM categories WHERE type = ?
    List<Category> findByType(CategoryType type);

    // SELECT * FROM categories WHERE name = ?
    Category findByName(String name);

    // SELECT * FROM categories WHERE name LIKE %keyword%
    List<Category> findByNameContainingIgnoreCase(String keyword);
}