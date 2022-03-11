/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.dto;


import java.util.ArrayList;
import lombok.Data;



@Data
public class CaidaLibreDTO {
   private ArrayList<String> errores;
   private ArrayList<String> gravedadN;
   private int nRep;
   private ArrayList<String> tiempo;
}
