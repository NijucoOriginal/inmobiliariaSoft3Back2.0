package com.jsebastian.eden.EdenSys.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends Persona {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    private String codigoActivacion;
}
