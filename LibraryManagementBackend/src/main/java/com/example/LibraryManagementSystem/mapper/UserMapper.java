package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.UsersDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    public static UsersDTO toDTO(Users user) {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setId(user.getId());
        usersDTO.setEmail(user.getEmail());
        usersDTO.setFullName(user.getFullName());
        usersDTO.setPhone(user.getPhone());
        usersDTO.setRole(user.getRole());
        usersDTO.setLastLogin(user.getLastLogin());
        return usersDTO;
    }

    public static List<UsersDTO> toDTOList(List<Users> users) {
        return users.stream().map(UserMapper::toDTO).toList();
    }

    public static Set<UsersDTO> toDTOSet(Set<Users> users) {
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toSet());
    }

    public static Users toEntity(UsersDTO usersDTO) {
        Users users = new Users();
        users.setEmail(usersDTO.getEmail());
        users.setPassword(usersDTO.getPassword());
        users.setFullName(usersDTO.getFullName());
        users.setPhone(usersDTO.getPhone());
        users.setRole(usersDTO.getRole());
        users.setCreatedAt(LocalDateTime.now());
        return users;
    }

}
