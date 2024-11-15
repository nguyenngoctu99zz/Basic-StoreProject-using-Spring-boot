package com.boostmytool.beststore.dao;

import com.boostmytool.beststore.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsername(String username);

}
