package com.balinasoft.firsttask.repository;

import com.balinasoft.firsttask.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    void deleteByName(String name);

    Category findByName(String name);
}
