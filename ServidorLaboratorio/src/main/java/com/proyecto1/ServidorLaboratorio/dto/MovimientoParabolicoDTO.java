/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.dto;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Data;



@Data
public class MovimientoParabolicoDTO {
    private ArrayList<Double> datos_x;
    private  ArrayList<Double>  datos_y;
    private int nRep;
    private String tiempo;
    private String url_imagen;
    private int velocidad;
}
