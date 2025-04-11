package com.example.smart_cricket_tournament.repository;


import com.example.smart_cricket_tournament.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long >, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
}
