package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.mapper.WishListMapper;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.model.WishList;
import com.example.LibraryManagementSystem.payload.dto.WishListDTO;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.WishListRepository;
import com.example.LibraryManagementSystem.service.UserService;
import com.example.LibraryManagementSystem.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.event.WindowStateListener;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;
    private final UserService userService;
    private final WishListMapper wishListMapper;
    private final BookRepository bookRepository;

    @Override
    public WishListDTO addToWishList(Long bookId, String notes) {

        Users users = userService.getCurrentUser();

        //1. validate book exists
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        //2. check if book is already in wishlist
        if (wishListRepository.existsByUsersIdAndBookId(users.getId(), bookId)) {
            throw new RuntimeException("Book is already in wishlist");
        }

        //3. create wishlist
        WishList wishList = new WishList();
        wishList.setUsers(users);
        wishList.setBook(book);
        wishList.setNotes(notes);
        WishList savedWishList = wishListRepository.save(wishList);

        return wishListMapper.toDTO(savedWishList);
    }

    @Override
    public void removeFromWishList(Long bookId) {
        Users users = userService.getCurrentUser();

        WishList wishList = wishListRepository.findByUsersIdAndBookId(users.getId(), bookId);

        if (wishList == null) {
            throw new RuntimeException("Book is not in wishlist");
        }

        wishListRepository.delete(wishList);
    }

    @Override
    public PageResponse<WishListDTO> getMyWishList(int page, int size) {
        Users users = userService.getCurrentUser();
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("addedAt").descending());
        
        Page<WishList> wishListPage = wishListRepository.findByUsersId(users.getId(), pageable);
        
        return convertToPageResponse(wishListPage);
    }

    private PageResponse<WishListDTO> convertToPageResponse(Page<WishList> wishListPage) {
        List<WishListDTO> wishListDTOS = wishListPage.getContent()
                .stream()
                .map(wishListMapper::toDTO)
                .toList();

        return new PageResponse<>(
                wishListDTOS,
                wishListPage.getNumber(),
                wishListPage.getSize(),
                wishListPage.getTotalElements(),
                wishListPage.getTotalPages(),
                wishListPage.isLast(),
                wishListPage.isFirst(),
                wishListPage.isEmpty()
        );
    }
}
