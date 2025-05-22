
package com.myblog.myblog.repository;

import com.myblog.myblog.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // 方法名必须为 findBy[字段名]
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.userId != :userId")
    boolean existsByEmailAndUserIdNot(@Param("email") String email, @Param("userId") Long userId);
}