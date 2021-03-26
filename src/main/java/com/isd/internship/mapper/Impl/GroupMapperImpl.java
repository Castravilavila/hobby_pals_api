package com.isd.internship.mapper.Impl;

import com.isd.internship.dto.CategoryDto;
import com.isd.internship.dto.GroupDto;
import com.isd.internship.entity.Category;
import com.isd.internship.entity.Group;
import com.isd.internship.mapper.GroupMapper;

public class GroupMapperImpl implements GroupMapper {
    @Override
    public GroupDto entityToDto(Group group) {
        if ( group == null ) {
            return null;
        }
        GroupDto groupDto = new GroupDto();
        groupDto.setGroupTitle ( group.getGroupTitle() );
        groupDto.setGroupDescription(group.getGroupDescription());
        groupDto.setGroupAcces(group.getGroupAccess());
        return groupDto;
    }

    @Override
    public Group dtoToEntity(GroupDto groupDto) {
        if ( groupDto == null ) {
            return null;
        }
        Group group = new Group();
        group.setGroupTitle( groupDto.getGroupTitle() );
        group.setGroupDescription( groupDto.getGroupDescription() );
        groupDto.setGroupAcces(groupDto.getGroupAcces());
        return group;
    }
}
