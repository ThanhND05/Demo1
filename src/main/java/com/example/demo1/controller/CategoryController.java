package com.example.demo1.controller;

import com.example.demo1.payload.CategoryDTO;
import com.example.demo1.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // ----- HIỂN THỊ TẤT CẢ CATEGORY -----
    @GetMapping
    public String getAllCategories(Model model) {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories/list"; // templates/categories/list.html
    }

    // ----- HIỂN THỊ FORM TẠO MỚI -----
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "categories/form"; // templates/categories/form.html
    }

    // ----- XỬ LÝ TẠO MỚI -----
    @PostMapping
    public String createCategory(@ModelAttribute("category") CategoryDTO category) {
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    // ----- HIỂN THỊ FORM CHỈNH SỬA -----
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        CategoryDTO category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "categories/form";
    }

    // ----- XỬ LÝ CẬP NHẬT -----
    @PostMapping("/update")
    public String updateCategory(@ModelAttribute("category") CategoryDTO category) {
        categoryService.updateCategory(category);
        return "redirect:/categories";
    }

    // ----- XEM CHI TIẾT CATEGORY -----
    @GetMapping("/{id}")
    public String getCategoryById(@PathVariable("id") Integer id, Model model) {
        CategoryDTO category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "categories/detail"; // templates/categories/detail.html
    }

    // ----- XOÁ CATEGORY -----
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    // ----- TÌM KIẾM CATEGORY THEO TÊN -----
    @GetMapping("/search")
    public String searchCategoryByName(@RequestParam("name") String categoryName, Model model) {
        List<CategoryDTO> list = categoryService.searchCategoryByName(categoryName);
        model.addAttribute("categories", list);
        model.addAttribute("searchKey", categoryName);
        return "categories/list";
    }
}
