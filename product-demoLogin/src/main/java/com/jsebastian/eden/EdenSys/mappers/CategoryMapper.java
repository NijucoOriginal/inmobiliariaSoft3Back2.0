package com.jsebastian.eden.EdenSys.mappers;

import com.example.demo.domain.Category;
import com.example.demo.dtos.CategoryRequest;
import com.example.demo.dtos.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "status", constant = "ACTIVE")
    Category parseOf(CategoryRequest categoryRequest);
    CategoryResponse toCategoryResponse(Category category);
}
