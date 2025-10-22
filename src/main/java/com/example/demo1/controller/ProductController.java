package com.example.demo1.controller;

import com.example.demo1.entity.ProductEnum;
import com.example.demo1.entity.User;
import com.example.demo1.payload.CartDTO;
import com.example.demo1.payload.CategoryDTO;
import com.example.demo1.payload.ProductDTO;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.CartService;
import com.example.demo1.service.CategoryService;
import com.example.demo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;
    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String showProductForm(@RequestParam(name = "id", required = false) Integer productId, Model model) {

        if (productId != null) {
            ProductDTO product = productService.getProductById(productId);
            model.addAttribute("product", product);
        } else {
            model.addAttribute("product", new ProductDTO());
        }

         List<CategoryDTO> categories = categoryService.getAllCategories();
         model.addAttribute("categories", categories);
         model.addAttribute("productSizes", ProductEnum.values());
        return "products/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createProduct(@ModelAttribute("product") ProductDTO productDTO,
                                @RequestParam("imageFile") MultipartFile imageFile) {

        if (!imageFile.isEmpty()) {
            String fileName = saveFile(imageFile);
            productDTO.setImageUrl("/uploads/" + fileName);
        }

        productService.addProduct(productDTO);
        return "redirect:/products";
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateProduct(@ModelAttribute("product") ProductDTO productDTO,
                                @RequestParam("imageFile") MultipartFile imageFile) {

        if (!imageFile.isEmpty()) {
            String fileName = saveFile(imageFile);
            productDTO.setImageUrl("/uploads/" + fileName);
        } else {
            ProductDTO existingProduct = productService.getProductById(productDTO.getProductId());
            productDTO.setImageUrl(existingProduct.getImageUrl());
        }

        productService.updateProduct(productDTO);
        return "redirect:/products";
    }

    private String saveFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        Path uploadPath = Paths.get(UPLOAD_DIR);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                return uniqueFileName;
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + originalFileName, e);
        }
    }

    @GetMapping
    public String showProductList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "productName", required = false) String productName,
            Model model) {

        Page<ProductDTO> productsPage;

        if (productName != null && !productName.isEmpty()) {
            productsPage = productService.searchProductsByName(productName, page, size);
            model.addAttribute("searchKey", productName);

        } else if (categoryId != null) {
            productsPage = productService.getProductsByCategoryId(categoryId, page, size);
            model.addAttribute("selectedCategoryId", categoryId);

        } else {
            productsPage = productService.getAllProducts(page, size);
        }
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());

        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "products/list";
    }

    @GetMapping("/id/{productId}")
    public String getProductById(@PathVariable("productId") Integer productId,
                                 Authentication authentication,
                                 @RequestParam(name = "origin", required = false) String origin,
                                 @RequestParam(name = "orderId", required = false) Integer orderId,
                                 Model model) {

        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User currentUser = userOptional.get();
                try {
                    CartDTO cart = cartService.getCartByUserId(currentUser.getUserId());
                    model.addAttribute("currentCartId", cart.getCartId());
                } catch (RuntimeException e) {
                    System.err.println("Lỗi: Không tìm thấy giỏ hàng cho user ID: " + currentUser.getUserId() + " - " + e.getMessage());
                }
            } else {
                System.err.println("Lỗi: Không tìm thấy user với email: " + email + " trong CSDL.");
            }
        } else {
            System.out.println("Info: Người dùng chưa đăng nhập, không thể lấy cartId.");
        }
        if (origin != null) {
            model.addAttribute("origin", origin);
        }
        if (orderId != null) {
            model.addAttribute("orderId", orderId);
        }
        return "products/detail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Integer productId) {
        productService.deleteProduct(productId);
        return "redirect:/products";
    }
}
