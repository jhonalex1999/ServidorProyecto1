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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author admin
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/laboratorio")
public class LaboratorioController {
    @Autowired
    private LaboratorioManagementService service;
     
    @GetMapping(value = "/pdf")
    public ResponseEntity pdf(){
        System.out.println("hola pdf");
        return new ResponseEntity(service.crearPdf(), HttpStatus.OK);
    }
    
    @PostMapping(value = "/{idFranjaHoraria}/{idGrupo}/insertarHorario")
    public ResponseEntity insertarHorario(@PathVariable(value = "idFranjaHoraria") String idFranjaHoraria,@PathVariable(value = "idGrupo") String idGrupo){
        return new ResponseEntity(service.insertarHorario(idFranjaHoraria, idGrupo), HttpStatus.OK);
    }
    
    @GetMapping(value = "/{idFranjaHoraria}/{idGrupo}/buscarHorario")
    public ResponseEntity buscarHorario(@PathVariable(value = "idFranjaHoraria") String idFranjaHoraria,@PathVariable(value = "idGrupo") String idGrupo){
     
         return new ResponseEntity(service.buscarHorario(idFranjaHoraria,idGrupo), HttpStatus.OK);
    }
    
    
    @PostMapping(value = "/{idLaboratorio}/{problema}/insertarProblema")
    public ResponseEntity insertarProblema(@PathVariable(value = "idLaboratorio") String idLaboratorio,@PathVariable(value = "problema") String problema){
        return new ResponseEntity(service.insertarProblema(idLaboratorio, problema), HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/{codGrupal}/finalizarPractica")
    public ResponseEntity finalizarPractica(@PathVariable(value = "codGrupal") int codGrupal){
        return new ResponseEntity(service.finalizarPractica(codGrupal), HttpStatus.OK);
    }
}
