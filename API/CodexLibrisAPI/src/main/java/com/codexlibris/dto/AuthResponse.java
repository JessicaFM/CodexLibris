/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 *
 * @author jessica
 */
@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private Integer roleId;
    private Integer id;
}

