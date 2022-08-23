package com.balinasoft.firsttask.service;

import com.balinasoft.firsttask.domain.Category;
import com.balinasoft.firsttask.dto.CategoryDtoIn;
import com.balinasoft.firsttask.dto.CategoryDtoOut;
import com.balinasoft.firsttask.mappers.CategoryMapper;
import com.balinasoft.firsttask.repository.CategoryRepository;
import com.balinasoft.firsttask.repository.ImageRepository;
import com.balinasoft.firsttask.service.exceptions.CategoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    @Override
    public CategoryDtoOut addCategory(CategoryDtoIn categoryDtoIn) {
        Category existCategory = categoryRepository.findByName(categoryDtoIn.getName());
        if (existCategory != null) {
            throw new EntityExistsException("Категория с именем '" + categoryDtoIn.getName() + "' уже существует!");
        }
        Category category = categoryRepository.save(categoryMapper.toEntity(categoryDtoIn));
        CategoryDtoOut categoryDtoOut = categoryMapper.toDto(category);
        return categoryDtoOut;
    }

    @Override
    public List<CategoryDtoOut> viewListCategories() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    @Transactional
    @Override
    public List<CategoryDtoOut> deleteCategory(String name) {
        if (!imageRepository.findByCategoryName(name).isEmpty()) {
            throw new EntityExistsException("Категорию под именем '" + name + "' невозможно удалить, так как она используется (есть привязанные фото)!");
        }
        categoryRepository.deleteByName(name);
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }
}