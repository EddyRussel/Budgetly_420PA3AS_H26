package com.example.budgetly_v1_avecsb.service;

import com.example.budgetly_v1_avecsb.model.Category;
import com.example.budgetly_v1_avecsb.model.CategoryType;
import com.example.budgetly_v1_avecsb.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByType(CategoryType type) {
        return categoryRepository.findByType(type);
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable : " + id));
    }

    public Category createCategory(Category category) {
        if (categoryRepository.findByName(category.getName()) != null)
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        return categoryRepository.save(category);
    }

    public Category updateCategory(int id, Category updated) {
        Category existing = getCategoryById(id);
        existing.setName(updated.getName());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(int id) {
        getCategoryById(id); // vérifie qu'elle existe
        categoryRepository.deleteById(id);
    }
}