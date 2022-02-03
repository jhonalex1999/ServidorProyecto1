/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto1.ServidorLaboratorio.service.LaboratorioManagementService;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping(value = "/laboratorio")
public class LaboratorioController {
    @Autowired
    private LaboratorioManagementService service;
     
    @GetMapping(value = "/pdf")
    public ResponseEntity pdf(){
        System.out.println("hola pdf");
        return new ResponseEntity(service.crearPdf(), HttpStatus.OK);
    }
    
}
