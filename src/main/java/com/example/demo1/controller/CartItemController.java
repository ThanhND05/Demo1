package com.example.demo1.controller;

import com.example.demo1.payload.CartItemDTO;
import com.example.demo1.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cartitems")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    // Hiển thị danh sách sản phẩm trong giỏ hàng
    @GetMapping("/cart/{cartId}")
    public String getItemsByCartId(@PathVariable("cartId") Integer cartId, Model model) {
        List<CartItemDTO> items = cartItemService.getCartItemsByCartId(cartId);
        model.addAttribute("items", items);
        model.addAttribute("cartId", cartId);
        return "cart/items"; // => templates/cart/items.html
    }

    // Thêm sản phẩm vào giỏ hàng (dùng form Thymeleaf)
    @PostMapping("/add")
    public String addCartItem(@ModelAttribute CartItemDTO cartItemDTO) {
        cartItemService.addCartItem(cartItemDTO);
        return "redirect:/cartitems/cart/" + cartItemDTO.getCartId();
    }

    // Cập nhật sản phẩm trong giỏ hàng
    @PostMapping("/update")
    public String updateCartItem(@ModelAttribute CartItemDTO cartItemDTO) {
        cartItemService.updateCartItem(cartItemDTO);
        return "redirect:/cartitems/cart/" + cartItemDTO.getCartId();
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @GetMapping("/delete/{cartItemId}/{cartId}")
    public String deleteCartItem(@PathVariable Integer cartItemId, @PathVariable Integer cartId) {
        cartItemService.deleteCartItem(cartItemId);
        return "redirect:/cartitems/cart/" + cartId;
    }
}
