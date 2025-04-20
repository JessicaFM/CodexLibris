/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.dto;

import lombok.Data;

/**
 *
 * @author jessica
 */

@Data
public class BookSearchDTO {
    private Integer id;
    private String title;
    private String isbn;
    private Boolean available;
    private String authorName;
    private String genreName;
}