package com.carry_guide.carry_guide_admin.jwt.models.entity;

import com.carry_guide.carry_guide_admin.jwt.models.state.UserState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user_table")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_state_id")
    private Integer userStateId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "user_state_identifier")
    private UserState userState;

    @OneToMany(mappedBy = "user_state", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JsonBackReference
    @ToString.Exclude
    private Set<UserAccount> users = new HashSet<>();

    public UserType(UserState userState) {
        this.userState = userState;
    }
}
