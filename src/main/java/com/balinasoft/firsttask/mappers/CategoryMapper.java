package com.balinasoft.firsttask.mappers;

import com.balinasoft.firsttask.domain.Category;
import com.balinasoft.firsttask.dto.CategoryDtoIn;
import com.balinasoft.firsttask.dto.CategoryDtoOut;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryDtoIn categoryDtoIn);

    CategoryDtoOut toDto(Category category);

    List<CategoryDtoOut> toDtoList(List<Category> listCategories);

}
