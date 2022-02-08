/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.proyecto1.ServidorLaboratorio.dto.CaidaLibreDTO;
import com.proyecto1.ServidorLaboratorio.dto.FranjaHorariaDTO;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.LeyHookeDTO;
import com.proyecto1.ServidorLaboratorio.dto.MovimientoParabolicoDTO;
import com.proyecto1.ServidorLaboratorio.dto.ParticipantesDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import com.proyecto1.ServidorLaboratorio.firebase.FirebaseInitializer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto1.ServidorLaboratorio.service.LaboratorioManagementService;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author admin
 */
@Service
public class LaboratorioManagementServiceImpl implements LaboratorioManagementService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public Boolean agregarParticipantes(ParticipantesDTO post) {
        Map<String, Object> docData = getDocDataParticipantes(post);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("PARTICIPANTES").document().create(docData);

        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public List<LeyHookeDTO> listarDatosHardwareLeyDeHooke() {
        List<LeyHookeDTO> response = new ArrayList<>();
        LeyHookeDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("LABORATORIO_LEY_HOOKE").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(LeyHookeDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MovimientoParabolicoDTO> listarDatosHardwareMovimientoParabolico() {
        List<MovimientoParabolicoDTO> response = new ArrayList<>();
        MovimientoParabolicoDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("LABORATORIO_MOVIMIENTO_PARABOLICO").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(MovimientoParabolicoDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<CaidaLibreDTO> listarDatosHardwareCaidaLibre() {
        List<CaidaLibreDTO> response = new ArrayList<>();
        CaidaLibreDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("LABORATORIO_CAIDA_LIBRE").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(CaidaLibreDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean crearPdf() {
        List<PostDTO> response = new ArrayList<>();
        PostDTO post;

        Document documento = new Document();

        String ruta = System.getProperty("user.home");
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Downloads/Prueba.pdf"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        documento.open();

        PdfPTable tabla = new PdfPTable(3);
        tabla.addCell("id");
        tabla.addCell("titulo");
        tabla.addCell("contenido");

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("post").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(PostDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < response.size(); i++) {
            tabla.addCell(response.get(i).getId());
            tabla.addCell(response.get(i).getTitle());
            tabla.addCell(response.get(i).getContent());

        }
        try {
            documento.add(tabla);
        } catch (DocumentException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        documento.close();

        return Boolean.TRUE;
    }

    @Override
    public List<FranjaHorariaDTO> listarFranjaHoraria() {
        List<FranjaHorariaDTO> response = new ArrayList<>();
        FranjaHorariaDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("FRANJA_HORARIA").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(FranjaHorariaDTO.class);
                post.setIdFranja(doc.getId());
                response.add(post);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean insertarHorario(String idFranjaHoraria, String idGrupo) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("idFranjaHoraria", idFranjaHoraria);
        docData.put("idGrupo", idGrupo);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("AGENDA").document().create(docData);

        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean buscarHorario(String idFranjaHoraria, String idGrupo) {
        ApiFuture<QuerySnapshot> querySnapshotApiFutur = firebase.getFirestore().collection("AGENDA").whereEqualTo("idFranjaHoraria", idFranjaHoraria).whereEqualTo("idGrupo", idGrupo).get();

        try {
            if (querySnapshotApiFutur.get().isEmpty()) {
                return false;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }

    @Override
    public Boolean insertarProblema(String idLaboratorio, String problema) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("idLaboratorio", idLaboratorio);
        docData.put("problema", problema);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection("PROBLEMAS").document().create(docData);

        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean finalizarPractica(int codGrupal) {

        String id = buscarGrupo(codGrupal);

        if (id.equals("no existe")) {
            return false;
        } else {
            ApiFuture<WriteResult> writeResultApiFuture = getCollection("GRUPO").document(id).delete();
            try {
                if (null != writeResultApiFuture.get()) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
    }

    private String buscarGrupo(int codGrupal) {
        GrupoDTO grupo;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("GRUPO").whereEqualTo("codGrupal", codGrupal).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                grupo = doc.toObject(GrupoDTO.class);
                grupo.setId(doc.getId());
                String id = grupo.getId();
                return id;
            }
            return "no existe";
        } catch (Exception e) {
            return "nullo";
        }
    }

    @Override
    public Boolean buscarCompletitudEstudiantes(int codGrupal) {
        GrupoDTO grupo;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").whereEqualTo("codGrupal", codGrupal).get();
        int contados = 0;
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                grupo = doc.toObject(GrupoDTO.class);
                grupo.setId(doc.getId());
                String id = grupo.getId();
                if (grupo.getEstado() == 1) {
                    contados += 1;
                }
            }
            //AQUI DEBE PONERSE DEPENDIENDO DEL NUMERO POR GRUPOS
            if (contados == 4) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private CollectionReference getCollection(String Colecion) {
        return firebase.getFirestore().collection(Colecion);
    }

    private Map<String, Object> getDocDataParticipantes(ParticipantesDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("codGrupal", post.getCodGrupal());
        docData.put("estado", post.getEstado());
        docData.put("idUsuario", post.getIdUsuario());
        docData.put("rol", post.getRol());
        return docData;
    }
    
    

}
