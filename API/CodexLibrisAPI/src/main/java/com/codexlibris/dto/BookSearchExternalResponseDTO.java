/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author jessica
 */

@Data
public class BookSearchExternalResponseDTO {
    
    private Integer total;
    
    private List<BookSearchExternalDTO> results;
}