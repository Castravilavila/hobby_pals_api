package com.isd.internship.service;

import com.isd.internship.entity.Category;
import com.isd.internship.entity.Group;
import com.isd.internship.payload.CategoryResponse;
import com.isd.internship.payload.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {
    ResponseEntity<Object> createCategory(Category category);

    PagedResponse<CategoryResponse> readAllCategories(int page,Long currentUserId);

    Category readCategoryById(Long ProjectId);

    ResponseEntity<Object> updateCategory(Category category,Long id);

    void deleteCategory(Long categoryId);

    CategoryResponse findById(Long categoryId, Long userId);

    Page<Category> getCategoriesByTitle(String title, PageRequest pageRequest);

    Page<Category> getCategoriesFollowedByUser(Long userId, int page);

    List<Category> getAllCategoriesFollowedByUser(Long userId);
}
