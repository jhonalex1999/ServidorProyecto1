/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.proyecto1.ServidorLaboratorio.dto.CursoDTO;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.UsuarioDTO;
import com.proyecto1.ServidorLaboratorio.firebase.FirebaseInitializer;
import com.proyecto1.ServidorLaboratorio.service.UsuarioManagementService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 */
@Service
public class UsuarioManagementServiceImpl implements UsuarioManagementService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public boolean prueba() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean ingresarUsuario(String correo, String nombre) {
        UsuarioDTO usuario = new UsuarioDTO();
        Boolean existe = buscarUsuario(correo);
        if (existe == false) {
            //Aumenta el ID en 1.
            int id = enQueVaId();
            String rol = "Estudiante";
            usuario.setIdUsuario(id);
            usuario.setNombreCompleto(nombre);
            usuario.setRol(rol);
            usuario.setCorreo(correo);
            Map<String, Object> docData = getDocData(usuario);
            ApiFuture<WriteResult> writeResultApiFuture = getCollection("USUARIO").document().create(docData);
            try {
                if (null != writeResultApiFuture.get()) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        } else {
            System.out.println("Ya existe");
            return false;
        }
    }

    private int enQueVaId() {
        UsuarioDTO usuario;
        List<Integer> idsActuales = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("USUARIO").get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                usuario = doc.toObject(UsuarioDTO.class);
                idsActuales.add(usuario.getIdUsuario());
                //int id_usuario = usuario.getIdUsuario();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(UsuarioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(UsuarioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (idsActuales.isEmpty()) {
            return 1;
        } else {
            return idsActuales.size() + 1;
        }
    }

    @Override
    public Boolean agregarCurso(String correo_institucional, String codigo_curso) {
        String Agendamiento = "vacio";
        //Primero buscara el curso
        String nombre_curso = buscarCodigoCurso(codigo_curso);
        int id_usuario = buscarIdUsuario(correo_institucional);
        //Si lo encontro matriculara y devolvera un TRUE, de lo contrario mandara un FALSE
        if (id_usuario > 0) {
            if (!"".equals(nombre_curso)) {
                ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("USUARIO").whereEqualTo("idUsuario", id_usuario).get();
                try {
                    for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                        Agendamiento = doc.getId();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                ApiFuture<WriteResult> writeResultApiFuture = getCollection("USUARIO").document(Agendamiento).update("idUsuario", id_usuario, "Cursos", FieldValue.arrayUnion(nombre_curso));
                try {
                    if (null != writeResultApiFuture.get()) {
                        return Boolean.TRUE;
                    }
                    return Boolean.FALSE;
                } catch (Exception e) {
                    return Boolean.FALSE;
                }
            } else {
                System.out.println("No existe curso");
                return false;
            }
        } else {
            System.out.println("No existe estudiante");
            return false;
        }
    }

    private CollectionReference getCollection(String Colecion) {
        return firebase.getFirestore().collection(Colecion);
    }

    @Override
    public ArrayList<String> buscarCursosMatriculados(String correo) {
        ArrayList<String> cursos;
        UsuarioDTO grupo;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("USUARIO").whereEqualTo("correo", correo).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                grupo = doc.toObject(UsuarioDTO.class);
                grupo.setId(doc.getId());
                cursos = grupo.getCursos();
                //System.out.println(cursos);
                return cursos;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public Boolean cambiarEstadoParticipanteEntrada(String correo) {
        String Participante = BuscarParticipante(correo);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("PARTICIPANTES").document(Participante).update("estado", 1);
        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean cambiarEstadoParticipanteSalida(String correo) {
        String Participante = BuscarParticipante(correo);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("PARTICIPANTES").document(Participante).update("estado", 0);
        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    private String BuscarParticipante(String correo) {
        String Participante = "vacio";
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").whereEqualTo("correo", correo).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                Participante = doc.getId();
                return Participante;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return Participante;
    }

    //Metodo para saber el ID del usuario
    public Boolean buscarUsuario(String correo_institucional) {
        UsuarioDTO usuario;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("USUARIO").whereEqualTo("correo", correo_institucional).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                usuario = doc.toObject(UsuarioDTO.class);
                usuario.setId(doc.getId());
                //int id_usuario = usuario.getIdUsuario();
                return true;
            }
            return false;
        } catch (Exception e) {
            return null;
        }
    }

    public int buscarIdUsuario(String correo_institucional) {
        UsuarioDTO usuario;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("USUARIO").whereEqualTo("correo", correo_institucional).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                usuario = doc.toObject(UsuarioDTO.class);
                usuario.setId(doc.getId());
                int id_usuario = usuario.getIdUsuario();
                return id_usuario;
            }
            return 0;
        } catch (InterruptedException | ExecutionException e) {
            return -1;
        }
    }

    public String buscarCodigoCurso(String codigo_curso) {
        CursoDTO curso;
        String nombre_curso = "";
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("CURSO").whereEqualTo("codigoMatricula", codigo_curso).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                curso = doc.toObject(CursoDTO.class);
                curso.setNombreCurso(curso.getNombreCurso());
                nombre_curso = curso.getNombreCurso();
                return nombre_curso;
            }
            return nombre_curso;
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> getDocData(UsuarioDTO usuario) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("idUsuario", usuario.getIdUsuario());
        docData.put("correo", usuario.getCorreo());
        docData.put("rol", usuario.getRol());
        docData.put("nombreCompleto", usuario.getNombreCompleto());
        return docData;
    }

    private Map<String, Object> getDataCurso(CursoDTO curso) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("nombreCurso", curso.getNombreCurso());
        docData.put("codigoMatricula", curso.getCodigoMatricula());
        docData.put("fechaCreacion", curso.getFechaCreacion());
        docData.put("fechaEliminacion", curso.getFechaEliminacion());
        return docData;
    }

}
