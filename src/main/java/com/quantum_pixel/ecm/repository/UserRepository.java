package com.quantum_pixel.ecm.repository;

import com.quantum_pixel.ecm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}