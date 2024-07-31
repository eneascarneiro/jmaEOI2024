package com.eoi.ejemplobasicoseguridad.repository;

import java.util.Optional;

import com.eoi.ejemplobasicoseguridad.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);
}
