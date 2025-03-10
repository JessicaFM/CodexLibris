/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author jessica
 */
@Data
public class UserDTO {
    @NotBlank(message = "El nom de usuari es obligatori")
    private String userName;

    @NotBlank(message = "El nom es obligatori")
    private String firstName;

    @NotBlank(message = "El cognom es obligatori")
    private String lastName;

    @NotBlank(message = "El email es obligatori")
    @Email(message = "El email ha de ser vàlid")
    private String email;

    @NotBlank(message = "La contrasenya es obligatoria")
    private String password;

    @NotNull(message = "El rol es obligatori")
    private Integer roleId;
}