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
public class BookSearchExternalDTO {
    private String title;
    private String author;
    private Integer year;
    private String isbn;
}
