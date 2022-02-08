/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.dto;

import lombok.Data;

@Data
public class PostDTO {
    
    //Prueba
    private String id;
    private String title;
    private String content;
    //Usuario
    private Integer idUsuario;
    private String correo;
    private String nombreCompleto;
    private String rol;
    private String archivos;
    private Integer codigo;
}
