package com.quantum_pixel.arg.repository;

import com.quantum_pixel.arg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}