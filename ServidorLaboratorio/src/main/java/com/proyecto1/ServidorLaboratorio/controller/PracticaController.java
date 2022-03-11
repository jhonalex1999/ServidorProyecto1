/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.controller;

import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto1.ServidorLaboratorio.service.PracticaManagementService;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/practica")
public class PracticaController {

    @Autowired
    private PracticaManagementService service;

    @GetMapping(value = "/greet/{name}")
    public String greet(@PathVariable(value = "name") String name) {
        return "Hola" + name;
    }

    @GetMapping(value = "/listarPracticas")
    public ResponseEntity listarPracticas() {
        return new ResponseEntity(service.listarPracticas(), HttpStatus.OK);
    }

    @GetMapping(value = "/{codGrupal}/{codigoPlanta}/verificarAgendamiento")
    public ResponseEntity verificarAgendamiento(@PathVariable(value = "codGrupal") int codGrupal, @PathVariable(value = "codigoPlanta") int codigoPlanta) {
        return new ResponseEntity(service.verificarAgendamiento(codGrupal, codigoPlanta), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity add(@RequestBody PostDTO post) {
        return new ResponseEntity(service.add(post), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/update")
    public ResponseEntity edit(@PathVariable(value = "id") String id, @RequestBody PostDTO post) {
        return new ResponseEntity(service.edit(id, post), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity delete(@PathVariable(value = "id") String id) {
        return new ResponseEntity(service.delete(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{codigo_planta}/descripcionProfesorPractica")
    public ResponseEntity descripcionProfesorPractica(@PathVariable(value = "codigo_planta") int codigo_planta) {
        return new ResponseEntity(service.descripcionProfesorPractica(codigo_planta), HttpStatus.OK);
    }

}
