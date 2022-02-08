/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.controller;

import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.service.UsuarioManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioManagementService service;

    @PostMapping(value = "/agregarUsuario")
    public ResponseEntity add(@RequestBody PostDTO post) {
        return new ResponseEntity(service.agregarUsuario(post), HttpStatus.OK);
    }

    @GetMapping(value = "/{codCurso}/buscarCodigoCurso")
    public ResponseEntity buscarCodigoCurso(@PathVariable(value = "codCurso") int codCurso) {
        return new ResponseEntity(service.buscarCodigoCurso(codCurso), HttpStatus.OK);
    }
}
