package com.muhammed.springsecurity.security.model.entities;

import com.muhammed.springsecurity.model.BaseEntity;
import com.muhammed.springsecurity.security.model.enums.TokenType;
import com.muhammed.springsecurity.user.model.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Id
    @GeneratedValue
    public Integer id;

    @Column(name = "token", unique = true, columnDefinition="text")
    public String token;

    @Column(name = "revoked")
    public boolean revoked;

    @Column(name = "expired")
    public boolean expired;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
