package com.isd.internship.controller;

import com.isd.internship.entity.Category;
import com.isd.internship.entity.Group;
import com.isd.internship.entity.User;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.CategoryResponse;
import com.isd.internship.payload.PagedResponse;
import com.isd.internship.security.CurrentUser;
import com.isd.internship.security.UserPrincipal;
import com.isd.internship.service.CategoryService;
import com.isd.internship.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private final UserService userService;

    @GetMapping("/categories")
    public PagedResponse<CategoryResponse> getCategories(@CurrentUser UserPrincipal currentUser,
                                                         @RequestParam int page){
       return categoryService.readAllCategories(page,currentUser.getId());
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> createCategory(@RequestBody Category category){
        return  categoryService.createCategory(category);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryResponse readCategoryById(@PathVariable Long categoryId, @CurrentUser UserPrincipal currentUser){
        return categoryService.findById(categoryId,currentUser.getId());
    }

    @GetMapping("/categories/update/{categoryId}")
    public ResponseEntity<Object> updateCategory(@RequestBody Category category,@PathVariable long categoryId) {
        return categoryService.updateCategory(category, categoryId);
    }

    @GetMapping("/categories/search")
    @Transactional
    public Page<Category> getCategorySearchFilteredPaginated(@RequestParam Optional<String> title,
                                                          @RequestParam Optional<Integer> page,
                                                          @RequestParam Optional<String> sortBy){
        return categoryService.getCategoriesByTitle(title.orElse(""), PageRequest.of(page.orElse(0), 2, Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @GetMapping("/categories/followedBy")
    public Page<Category> getCategoriesFollowedByUser(@CurrentUser UserPrincipal userPrincipal,@RequestParam int page){

        return categoryService.getCategoriesFollowedByUser(userPrincipal.getId(),page);
    }

    @GetMapping("/categories/followedByList")
    public List<Category> getAllCategoriesFollowedByUser(@CurrentUser UserPrincipal userPrincipal){

        return categoryService.getAllCategoriesFollowedByUser(userPrincipal.getId());
    }



    @PostMapping("/categories/unfollow/{categoryId}")
    public ApiResponse unfollowCategory(@PathVariable Long categoryId, @CurrentUser UserPrincipal currentUser){

        User user = userService.getOne(currentUser.getId());
        Category category = categoryService.readCategoryById(categoryId);
        user.getCategories().remove(category);
        userService.save(user);

        return new ApiResponse(true,"Category unfollowed successfully");
    }

}
