package com.example.demo1.controller;

import com.example.demo1.payload.ProductDTO;
import com.example.demo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    // thêm mới sản phẩm
    @PostMapping
    public String createProduct(@ModelAttribute("product") ProductDTO productDTO) {
        productService.addProduct(productDTO);
        return "redirect:/products";
    }

    // hiển thị tất cả sản phẩm
    @GetMapping
    public String getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Page<ProductDTO> products = productService.getAllProducts(page, size);
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        return "products/list"; // templates/products/list.html
    }

    // cập nhật sản phẩm
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") ProductDTO productDTO) {
        productService.updateProduct(productDTO);
        return "redirect:/products";
    }

    // lấy sản phẩm theo id
    @GetMapping("/id/{productId}")
    public String getProductById(@PathVariable("productId") Integer productId, Model model) {
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "products/detail"; // templates/products/detail.html
    }

    // lấy sản phẩm theo tên
    @GetMapping("/name/{productName}")
    public String getProductByName(
            @PathVariable("productName") String productName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Page<ProductDTO> list = productService.searchProductsByName(productName, page, size);
        model.addAttribute("products", list.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", list.getTotalPages());
        model.addAttribute("searchKey", productName);
        return "products/list";
    }

    // xóa sản phẩm
    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Integer productId) {
        productService.deleteProduct(productId);
        return "redirect:/products";
    }

    // sản phẩm theo tên category
    @GetMapping("/categories/name/{categoryName}")
    public String getProductsByCategoryName(
            @PathVariable("categoryName") String categoryName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Page<ProductDTO> list = productService.getProductsByCategoryName(categoryName, page, size);
        model.addAttribute("products", list.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", list.getTotalPages());
        model.addAttribute("categoryName", categoryName);
        return "products/list";
    }

    // sản phẩm theo id category
    @GetMapping("/categories/id/{categoryId}")
    public String getProductsByCategoryId(
            @PathVariable("categoryId") Integer categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Page<ProductDTO> list = productService.getProductsByCategoryId(categoryId, page, size);
        model.addAttribute("products", list.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", list.getTotalPages());
        model.addAttribute("categoryId", categoryId);
        return "products/list";
    }
}
