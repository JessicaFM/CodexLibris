/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 *
 * @author jessica
 */

@Data
public class OpenLibraryResponseDTO {
    @JsonProperty("docs")
    private List<OpenLibraryDocDTO> docs;

    private int numFound;
}
