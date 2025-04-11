package com.example.smart_cricket_tournament.repository.specification;

import com.example.smart_cricket_tournament.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class UserSpecification {
    public static Specification<User> filterUsers(String search,String role ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (search != null && !search.isBlank()) {
                Predicate emailPredicate = cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%");
                Predicate namePredicate = cb.like(cb.lower(root.get("fullName")), "%" + search.toLowerCase() + "%");
                predicate = cb.and(predicate, cb.or(emailPredicate, namePredicate));
            }

            if (role != null && !role.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("role"), role));
            }

            return predicate;
        };
    }
}
