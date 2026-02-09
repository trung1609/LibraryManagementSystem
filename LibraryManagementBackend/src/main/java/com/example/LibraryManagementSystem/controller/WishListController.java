package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.payload.response.ApiResponse;
import com.example.LibraryManagementSystem.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @PostMapping("/add/{bookId}")
    public ResponseEntity<?> addBookToWishList(
            @PathVariable Long bookId,
            @RequestParam(required = false) String notes
    ) {
        return ResponseEntity.ok(wishListService.addToWishList(bookId, notes));
    }

    @GetMapping("/my-wishlist")
    public ResponseEntity<?> getMyWishList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(wishListService.getMyWishList(page, size));
    }


    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<?> removeFromWishList(@PathVariable Long bookId) {
        wishListService.removeFromWishList(bookId);
        ApiResponse response = new ApiResponse("Book removed from wishlist successfully", true);
        return ResponseEntity.ok(response);
    }
}
