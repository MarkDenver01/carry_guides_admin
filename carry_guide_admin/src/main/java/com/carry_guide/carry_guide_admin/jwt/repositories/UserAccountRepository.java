package com.carry_guide.carry_guide_admin.jwt.repositories;

import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<UserAccount> findByEmail(String email);
}
