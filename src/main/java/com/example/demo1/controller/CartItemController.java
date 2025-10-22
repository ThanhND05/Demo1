package com.example.demo1.controller;

import com.example.demo1.payload.CartItemDTO;
import com.example.demo1.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cartitems")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/cart/{cartId}")
    public String getItemsByCartId(@PathVariable("cartId") Integer cartId, Model model) {
        List<CartItemDTO> items = cartItemService.getCartItemsByCartId(cartId);
        model.addAttribute("items", items);
        model.addAttribute("cartId", cartId);
        return "cart/items";
    }


    @PostMapping("/add")
    public String addCartItem(@ModelAttribute CartItemDTO cartItemDTO,
                              RedirectAttributes redirectAttributes) { // Thêm RedirectAttributes
        try {
            cartItemService.addCartItem(cartItemDTO);
            redirectAttributes.addFlashAttribute("addCartSuccess", "Đã thêm sản phẩm vào giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("addCartError", "Lỗi thêm vào giỏ: " + e.getMessage());
        }

        Integer productId = (cartItemDTO.getProduct() != null) ? cartItemDTO.getProduct().getProductId() : null;
        if (productId != null) {
            return "redirect:/products/id/" + productId;
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping("/update")
    public String updateCartItem(@ModelAttribute CartItemDTO cartItemDTO) {
        cartItemService.updateCartItem(cartItemDTO);
        return "redirect:/cartitems/cart/" + cartItemDTO.getCartId();
    }

    @PostMapping("/delete/{cartItemId}/{cartId}")
    public String deleteCartItem(@PathVariable Integer cartItemId, @PathVariable Integer cartId) {
        cartItemService.deleteCartItem(cartItemId);
        return "redirect:/cartitems/cart/" + cartId;
    }
}
