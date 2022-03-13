/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.dto;


import java.util.HashMap;
import lombok.Data;



@Data
public class CaidaLibreDTO {
   private HashMap<String,Double>errores;
   private HashMap<String,Double> gravedadN;
   private int nRep;
   private HashMap<String,Double>  tiempo;
}
