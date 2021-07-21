package com.kokochi.samp.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kokochi.samp.domain.User;
import com.kokochi.samp.domain.User_Auth;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserId(String username);
}
