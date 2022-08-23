package com.balinasoft.firsttask.service;

import com.balinasoft.firsttask.domain.Category;
import com.balinasoft.firsttask.dto.CategoryDtoIn;
import com.balinasoft.firsttask.dto.CategoryDtoOut;

import java.util.List;

public interface CategoryService {
    CategoryDtoOut addCategory(CategoryDtoIn categoryDtoIn);

    List<CategoryDtoOut> viewListCategories();

    List<CategoryDtoOut> deleteCategory(String category);
}
