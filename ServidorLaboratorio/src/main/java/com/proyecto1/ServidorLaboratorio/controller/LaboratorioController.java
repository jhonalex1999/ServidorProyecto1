/*
*  To change this license header, choose License Headers in Project Properties.
*  To change this template file, choose Tools | Templates
*  and open the template in the editor.
*/
package com.proyecto1.ServidorLaboratorio.controller;

import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.ParticipantesDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
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

    @PostMapping(value = "/{idFranja}/agregarParticipantes")
    public ResponseEntity agregarParticipantes(@RequestBody ArrayList<String> participantes, @PathVariable(value = "idFranja") Integer idFranja) {
        return new ResponseEntity(service.agregarParticipantes(participantes, idFranja), HttpStatus.OK);
    }

    @GetMapping(value = "/{codigoPlanta}/listarAgendamiento")
    public ResponseEntity listarAgendamiento(@PathVariable(value = "codigoPlanta") int codigoPlanta) {
        return new ResponseEntity(service.listarAgendamiento(codigoPlanta), HttpStatus.OK);
    }

    @GetMapping(value = "/{idAgendamiento}/{codGrupal}/buscarHorario")
    public ResponseEntity buscarHorario(@PathVariable(value = "idAgendamiento") int idAgendamiento, @PathVariable(value = "codGrupal") int codGrupal) {
        return new ResponseEntity(service.buscarHorario(idAgendamiento, codGrupal), HttpStatus.OK);
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

    @GetMapping(value = "/{correo}/cambiarEstadoParticipanteEntrada")
    public ResponseEntity cambiarEstadoParticipanteEntrada(@PathVariable(value = "correo") String correo) {
        return new ResponseEntity(service.cambiarEstadoParticipanteEntrada(correo), HttpStatus.OK);
    }

    @GetMapping(value = "/{correo}/cambiarEstadoParticipanteSalida")
    public ResponseEntity cambiarEstadoParticipanteSalida(@PathVariable(value = "correo") String correo) {
        return new ResponseEntity(service.cambiarEstadoParticipanteSalida(correo), HttpStatus.OK);
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

    @GetMapping(value = "/iniciarProceso")
    public ResponseEntity iniciarProceso() {
        return new ResponseEntity(service.iniciarProceso(), HttpStatus.OK);
    }
  @PostMapping(value = "/GuardarCaidaLibre")
    public ResponseEntity GuardarCaidaLibre() {
        return new ResponseEntity(service.GuardarCaidaLibre(), HttpStatus.OK);
    }
}
