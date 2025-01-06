package com.example.Kurs_salon.model;

import com.example.Kurs_salon.model.UserAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Table(name = "user_roles")
@Entity(name = "user_roles")
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(generator = "user_role_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_role_id_seq", sequenceName = "user_role_id_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserAuthority userAuthority;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}