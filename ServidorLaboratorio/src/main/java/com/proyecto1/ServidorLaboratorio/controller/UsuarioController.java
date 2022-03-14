/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.controller;

import com.proyecto1.ServidorLaboratorio.dto.CursoDTO;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.UsuarioDTO;
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
    int codigo_curso = 0;

    /*@PostMapping(value = "/agregarUsuario")
    public ResponseEntity add(@RequestBody PostDTO post) {
        return new ResponseEntity(service.agregarUsuario(post), HttpStatus.OK);
    }*/
    @PostMapping(value = "{correo}/{codigo_curso}/matricularCurso")
    public ResponseEntity matricularCurso(@PathVariable(value = "codigo_curso") String codigo_curso, @PathVariable(value = "correo") String correo_institucional) {
        return new ResponseEntity(service.agregarCurso(correo_institucional, codigo_curso), HttpStatus.OK);
    }

    @GetMapping(value = "/{correo}/buscarCursosMatriculados")
    public ResponseEntity buscarCursosMatriculados(@PathVariable(value = "correo") String correo) {
        return new ResponseEntity(service.buscarCursosMatriculados(correo), HttpStatus.OK);
    }

    @PostMapping(value = "{correo}/{nombreCompleto}/ingresarUsuario")
    public ResponseEntity ingresarUsuario(@PathVariable(value = "correo") String correo_institucional, @PathVariable(value = "nombreCompleto") String nombre) {
        return new ResponseEntity(service.ingresarUsuario(correo_institucional, nombre), HttpStatus.OK);
    }

    @GetMapping(value = "/{correo}/cambiarEstadoParticipanteEntrada")
    public ResponseEntity cambiarEstadoParticipanteEntrada(@PathVariable(value = "correo") String correo) {
        return new ResponseEntity(service.cambiarEstadoParticipanteEntrada(correo), HttpStatus.OK);
    }

    @GetMapping(value = "/{correo}/cambiarEstadoParticipanteSalida")
    public ResponseEntity cambiarEstadoParticipanteSalida(@PathVariable(value = "correo") String correo) {
        return new ResponseEntity(service.cambiarEstadoParticipanteSalida(correo), HttpStatus.OK);
    }

}
