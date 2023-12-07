package com.quantumpixel.ecommarce.repository;

import com.quantumpixel.ecommarce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}