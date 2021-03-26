package com.isd.internship.mapper.Impl;

import com.isd.internship.dto.CategoryDto;
import com.isd.internship.entity.Category;
import com.isd.internship.mapper.CategoryMapper;

public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public CategoryDto entityToDto(Category category) {
        if ( category == null ) {
            return null;
        }
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName( category.getCategoryName() );
        return categoryDto;
    }

    @Override
    public Category dtoToEntity(CategoryDto categoryDto) {
        if ( categoryDto == null ) {
            return null;
        }
        Category category = new Category();
        category.setCategoryName( category.getCategoryName() );
        return category;
    }
}
