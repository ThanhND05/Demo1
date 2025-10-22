package com.example.demo1.controller;

import com.example.demo1.payload.CategoryDTO;
import com.example.demo1.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String getAllCategories(Model model) {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories/list"; // templates/categories/list.html
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "categories/form"; // templates/categories/form.html
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createCategory(@Valid @ModelAttribute("category") CategoryDTO category,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "categories/form";
        }
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        CategoryDTO category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "categories/form";
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateCategory(@Valid @ModelAttribute("category") CategoryDTO category,
                                 BindingResult bindingResult) { // <-- Thêm

        if (bindingResult.hasErrors()) {
            return "categories/form";
        }

        categoryService.updateCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/{id}")
    public String getCategoryById(@PathVariable("id") Integer id, Model model) {
        CategoryDTO category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "categories/detail";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCategory(@PathVariable("id") Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    @GetMapping("/search")
    public String searchCategoryByName(@RequestParam("name") String categoryName, Model model) {
        List<CategoryDTO> list = categoryService.searchCategoryByName(categoryName);
        model.addAttribute("categories", list);
        model.addAttribute("searchKey", categoryName);
        return "categories/list";
    }
}
