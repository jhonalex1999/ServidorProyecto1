/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.controller;

import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.ParticipantesDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto1.ServidorLaboratorio.service.LaboratorioManagementService;
import java.lang.reflect.Array;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping(value = "/{codigo_planta}/descargarDatos")
    public ResponseEntity descargarDatos(@PathVariable(value = "codigo_planta") int codigo_planta) throws Exception {
        return new ResponseEntity(service.descargarDatos(codigo_planta), HttpStatus.OK);
    }

    /*@GetMapping(value = "/pdf")
    public ResponseEntity pdf() {
        return new ResponseEntity(service.crearPdf(), HttpStatus.OK);
    }*/
    @GetMapping(value = "/listarDatosHardwareLeyDeHooke")
    public ResponseEntity listarDatosHardwareLeyDeHooke() {
        return new ResponseEntity(service.listarDatosHardwareLeyDeHooke(), HttpStatus.OK);
    }

    @GetMapping(value = "/listarDatosHardwareMovimientoParabolico")
    public ResponseEntity listarDatosHardwareMovimientoParabolico() {
        return new ResponseEntity(service.listarDatosHardwareMovimientoParabolico(), HttpStatus.OK);
    }

    @GetMapping(value = "/listarDatosHardwareCaidaLibre")
    public ResponseEntity listarDatosHardwareCaidaLibre() {
        return new ResponseEntity(service.listarDatosHardwareCaidaLibre(), HttpStatus.OK);
    }

    @PostMapping(value = "/{idLaboratorio}/{problema}/insertarProblema")
    public ResponseEntity insertarProblema(@PathVariable(value = "idLaboratorio") String idLaboratorio, @PathVariable(value = "problema") String problema) {
        return new ResponseEntity(service.insertarProblema(idLaboratorio, problema), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{codGrupal}/finalizarPractica")
    public ResponseEntity finalizarPractica(@PathVariable(value = "codGrupal") int codGrupal) {
        return new ResponseEntity(service.finalizarPractica(codGrupal), HttpStatus.OK);
    }

    @GetMapping(value = "/{codCurso}/buscarCompletitudEstudiantes")
    public ResponseEntity buscarCompletitudEstudiantes(@PathVariable(value = "codCurso") int codCurso) {
        return new ResponseEntity(service.buscarCompletitudEstudiantes(codCurso), HttpStatus.OK);
    }

    @GetMapping(value = "/{correo}/buscarQuienEsLider")
    public ResponseEntity buscarQuienEsLider(@PathVariable(value = "correo") String correo) {
        return new ResponseEntity(service.buscarQuienEsLider(correo), HttpStatus.OK);
    }

    @GetMapping(value = "/{correo}/saberCodigoGrupo")
    public ResponseEntity saberCodigoGrupo(@PathVariable(value = "correo") String correo) {
        return new ResponseEntity(service.saberCodigoGrupo(correo), HttpStatus.OK);
    }

    @GetMapping(value = "/{codigo_planta}/listar_Altura_CL")
    public ResponseEntity listar_Altura_CL(@PathVariable(value = "codigo_planta") int codigo_planta) {
        return new ResponseEntity(service.listar_Altura_CL(codigo_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{codigo_planta}/listar_Elongacion_LH")
    public ResponseEntity listar_Elongacion_LH(@PathVariable(value = "codigo_planta") int codigo_planta) {
        return new ResponseEntity(service.listar_Elongacion_LH(codigo_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{codigo_planta}/listar_Fuerza_LH")
    public ResponseEntity listar_Fuerza_LH(@PathVariable(value = "codigo_planta") int codigo_planta) {
        return new ResponseEntity(service.listar_Fuerza_LH(codigo_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{codigo_planta}/listar_Angulo_MP")
    public ResponseEntity listar_Angulo_MP(@PathVariable(value = "codigo_planta") int codigo_planta) {
        return new ResponseEntity(service.listar_Angulo_MP(codigo_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{codigo_planta}/listar_Velocidad_MP")
    public ResponseEntity listar_Velocidad_MP(@PathVariable(value = "codigo_planta") int codigo_planta) {
        return new ResponseEntity(service.listar_Velocidad_MP(codigo_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{peso}/iniciarLeyHooke")
    public ResponseEntity iniciarLeyHooke(@PathVariable(value = "peso") int peso) {
        return new ResponseEntity(service.iniciarLeyHooke(peso), HttpStatus.OK);
    }
     @GetMapping(value = "/{peso}/iniciarCaidaLibre")
    public ResponseEntity iniciarCaidaLibre(@PathVariable(value = "peso") int peso) {
        return new ResponseEntity(service.iniciarCaidaLibre(peso), HttpStatus.OK);
    }
     @GetMapping(value = "/{angulo}/{velocidad}/iniciarMovimientoParabolico")
    public ResponseEntity iniciarMovimientoParabolico(@PathVariable(value = "angulo") int angulo,@PathVariable(value = "velocidad") int velocidad) {
        return new ResponseEntity(service.iniciarMovimientoParabolico(angulo,velocidad), HttpStatus.OK);
    }

    @GetMapping(value = "/{planta}/finalizarProceso")
    public ResponseEntity finalizarProceso(@PathVariable(value = "planta") String planta) {
        return new ResponseEntity(service.finalizarProceso(planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{cod_planta}/retornarAltura")
    public ResponseEntity retornarAltura(@PathVariable(value = "cod_planta") int cod_planta) {
        return new ResponseEntity(service.retornarAltura(cod_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{cod_planta}/retornarTiempo")
    public ResponseEntity retornarTiempo(@PathVariable(value = "cod_planta") int cod_planta) {
        return new ResponseEntity(service.retornarTiempo(cod_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{cod_planta}/retornarElongaciones")
    public ResponseEntity retornarElongaciones(@PathVariable(value = "cod_planta") int cod_planta) {
        return new ResponseEntity(service.retornarElongaciones(cod_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{cod_planta}/retornarPesos")
    public ResponseEntity retornarPesos(@PathVariable(value = "cod_planta") int cod_planta) {
        return new ResponseEntity(service.retornarPesos(cod_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{cod_planta}/retornarX")
    public ResponseEntity retornarX(@PathVariable(value = "cod_planta") int cod_planta) {
        return new ResponseEntity(service.retornarX(cod_planta), HttpStatus.OK);
    }

    @GetMapping(value = "/{cod_planta}/retornarY")
    public ResponseEntity retornarY(@PathVariable(value = "cod_planta") int cod_planta) {
        return new ResponseEntity(service.retornarY(cod_planta), HttpStatus.OK);
    }
}
