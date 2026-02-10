package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.model.WishList;
import com.example.LibraryManagementSystem.payload.dto.WishListDTO;
import org.springframework.stereotype.Component;

@Component
public class WishListMapper {
    private final BookMapper bookMapper;

    public WishListMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public WishListDTO toDTO(WishList wishList){
        if (wishList == null) {
            return null;
        }

        WishListDTO dto = new WishListDTO();
        dto.setId(wishList.getId());

        if (wishList.getUsers() != null){
            dto.setUserId(wishList.getUsers().getId());
            dto.setUserName(wishList.getUsers().getFullName());
        }

        if (wishList.getBook() != null){
            dto.setBook(bookMapper.toDTO(wishList.getBook()));
        }

        dto.setAddedAt(wishList.getAddedAt());
        dto.setNotes(wishList.getNotes());
        return dto;
    }
}
