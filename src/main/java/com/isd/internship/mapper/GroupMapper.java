package com.isd.internship.mapper;

import com.isd.internship.dto.CategoryDto;
import com.isd.internship.dto.GroupDto;
import com.isd.internship.entity.Category;
import com.isd.internship.entity.Group;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface GroupMapper {
    GroupDto entityToDto(Group group);
    Group dtoToEntity(GroupDto groupDto);
}
