package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.dto.UserListRequest;
import com.example.smart_cricket_tournament.dto.UserListResponse;
import com.example.smart_cricket_tournament.entity.User;
import com.example.smart_cricket_tournament.repository.UserRepository;
import com.example.smart_cricket_tournament.repository.specification.UserSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public UserListResponse<User> getAllUsers(UserListRequest request) {
        Sort.Direction direction = request.getOrder().equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(request.getStart() ,request.getLimit(),
                 Sort.by(direction, request.getSort()));


        Specification<User> spec = UserSpecification.filterUsers(request.getSearch(), request.getFilter());
        Page<User> userPage = userRepository.findAll(spec, pageable);

        return new UserListResponse<>(
                userPage.getContent(),
                userPage.getTotalElements()
        );
    }
}
