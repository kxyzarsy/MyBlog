
package com.myblog.myblog.repository;

import com.myblog.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // 方法名必须为 findBy[字段名]
    boolean existsByUsername(String username);
}