package com.quantum_pixel.arg.user.repository;

import com.quantum_pixel.arg.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}