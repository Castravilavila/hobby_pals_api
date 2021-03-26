package com.isd.internship.repository;

import com.isd.internship.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("SELECT c from Category c WHERE c.categoryName LIKE %?1%")
    Page<Category> findPostsByCategoryTitle(String title, Pageable pageable);

    Page<Category> findByUsers_Id(Long userId,Pageable pageable);

    List<Category> findByUsers_Id(Long userId);

    Page<Category> findAll(Pageable pageable);
}
