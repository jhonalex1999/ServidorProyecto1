/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.dto;

import java.util.ArrayList;
import lombok.Data;



@Data
public class MovimientoParabolicoDTO {
    private ArrayList<String> datosX;
    private ArrayList<String> datosY;
    private int nRep;
    private ArrayList<String> tiempo;
    private ArrayList<String> velocidad;
    private String url;
}
