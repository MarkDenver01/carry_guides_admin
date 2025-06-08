package com.carry_guide.carry_guide_admin.jwt.repositories;

import com.carry_guide.carry_guide_admin.jwt.models.entity.UserType;
import com.carry_guide.carry_guide_admin.jwt.models.state.UserState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    Optional<UserType> findUserTypesBy(UserState userState);
}
