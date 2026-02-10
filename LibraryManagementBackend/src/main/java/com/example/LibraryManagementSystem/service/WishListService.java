package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.payload.dto.WishListDTO;
import com.example.LibraryManagementSystem.payload.response.PageResponse;

public interface WishListService {
    WishListDTO addToWishList(Long bookId, String notes);

    void removeFromWishList(Long bookId);

    PageResponse<WishListDTO> getMyWishList(int page, int size);


}
