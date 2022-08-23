package com.balinasoft.firsttask.controller.api1;

import com.balinasoft.firsttask.dto.CategoryDtoIn;
import com.balinasoft.firsttask.dto.ImageDtoOut;
import com.balinasoft.firsttask.dto.ResponseDto;
import com.balinasoft.firsttask.service.CategoryService;
import com.balinasoft.firsttask.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.balinasoft.firsttask.system.StaticWrapper.wrap;

@RestController
@RequestMapping("/api/image")
@Api(tags = "Category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Secured("ROLE_USER")
    @RequestMapping(value = "category", method = RequestMethod.POST)
    @ApiOperation(value = "Create category")
    public ResponseDto createCategory(@Valid @RequestBody CategoryDtoIn category) {
        return wrap(categoryService.addCategory(category));
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "category", method = RequestMethod.GET)
    @ApiOperation(value = "View categories")
    public ResponseDto getCategories() {
        return wrap(categoryService.viewListCategories());
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "category", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete category")
    public ResponseDto deleteCategory(@RequestParam String name) {
        return wrap(categoryService.deleteCategory(name));
    }
}