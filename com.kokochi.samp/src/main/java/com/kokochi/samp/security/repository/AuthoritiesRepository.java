package com.kokochi.samp.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kokochi.samp.domain.User;
import com.kokochi.samp.domain.User_Auth;

public interface AuthoritiesRepository extends JpaRepository<User, Integer> {
	List<User_Auth> findByUserId(String user_id);
}
