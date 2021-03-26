package com.isd.internship.serviceImpl;

import com.isd.internship.entity.Category;
import com.isd.internship.entity.User;
import com.isd.internship.exceptions.ResourceNotFoundException;
import com.isd.internship.payload.CategoryResponse;
import com.isd.internship.payload.PagedResponse;
import com.isd.internship.repository.CategoryRepository;
import com.isd.internship.repository.UserRepository;
import com.isd.internship.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Object> createCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{categoryId}")
                .build(savedCategory.getCategoryId());

        return ResponseEntity.created(location).build();
    }

    @Override
    public PagedResponse<CategoryResponse> readAllCategories(int page,Long currentUserId) {

        PageRequest pageRequest = PageRequest.of(page,8);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);
        List<CategoryResponse> categoryResponseList = categoryPage.stream().map(c->getCategoryResponse(c.getCategoryId(),currentUserId)).collect(Collectors.toList());
        return PagedResponse.getPagedResponse(categoryResponseList,categoryPage);
    }

    @Override
    public Category readCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category","Id",id));
    }

    @Override
    public ResponseEntity<Object> updateCategory(Category category, Long id) {
        Optional<Category> taskOptional = categoryRepository.findById(id);

        category.setCategoryId(id);
        categoryRepository.save(category);

        return ResponseEntity.noContent().build();
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    private CategoryResponse getCategoryResponse(Long categoryId,Long userId){

        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
        Category category = readCategoryById(categoryId);
        boolean followedByCurrentUser = user.getCategories().stream().anyMatch((c)->c.getCategoryId()==categoryId);
        return new CategoryResponse(category.getCategoryName(),categoryId,followedByCurrentUser,category.getImageUrl());
    }

    @Override
    public CategoryResponse findById(Long categoryId,Long userId){

        return getCategoryResponse(categoryId,userId);
    }

    @Override
    public Page<Category> getCategoriesByTitle(String title, PageRequest pageRequest) {
        return categoryRepository.findPostsByCategoryTitle(title.toLowerCase(Locale.ROOT), pageRequest);
    }

    @Override
    public Page<Category> getCategoriesFollowedByUser(Long userId, int page){
        PageRequest pageRequest = PageRequest.of(page,8);
        return categoryRepository.findByUsers_Id(userId,pageRequest);
    }

    @Override
    public List<Category> getAllCategoriesFollowedByUser(Long userId) {
        return categoryRepository.findByUsers_Id(userId);
    }
}
