package com.isd.internship.mapper;

import com.isd.internship.dto.CategoryDto;
import com.isd.internship.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
CategoryDto entityToDto(Category category);
Category dtoToEntity(CategoryDto categoryDto);
}
