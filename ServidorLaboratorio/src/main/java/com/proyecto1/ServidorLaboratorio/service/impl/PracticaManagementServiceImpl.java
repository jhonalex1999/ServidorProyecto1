package com.proyecto1.ServidorLaboratorio.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.firebase.FirebaseInitializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.*;
import java.time.format.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.proyecto1.ServidorLaboratorio.dto.AgendamientoDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import com.proyecto1.ServidorLaboratorio.firebase.RealTime;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.proyecto1.ServidorLaboratorio.service.PracticaManagementService;

@Service
public class PracticaManagementServiceImpl implements PracticaManagementService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public List<PracticaDTO> listarPracticas() {
        List<PracticaDTO> response = new ArrayList<>();
        PracticaDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("PRACTICA").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(PracticaDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String descripcionProfesorPractica(int codigo_planta) {
        PracticaDTO practica;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PRACTICA").whereEqualTo("codigoPlanta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                practica = doc.toObject(PracticaDTO.class);
                return practica.getDescripcion();
            }
            return "";

        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public Boolean add(PostDTO post) {
        Map<String, Object> docData = getDocData(post);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("post").document().create(docData);

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
    public Boolean edit(String id, PostDTO post) {
        Map<String, Object> docData = getDocData(post);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("post").document(id).set(docData);
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
    public Boolean delete(String id) {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection("post").document(id).delete();
        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    private CollectionReference getCollection(String Colecion) {
        return firebase.getFirestore().collection(Colecion);
    }

    private Map<String, Object> getDocData(PostDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", post.getTitle());
        docData.put("content", post.getContent());
        return docData;
    }

    @Override
    public Boolean verificarAgendamiento(int codGrupal, int codigoPlanta) {
        AgendamientoDTO Agendamiento = new AgendamientoDTO();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("AGENDAMIENTO").whereEqualTo("codGrupal", codGrupal).whereEqualTo("codigoPlanta", codigoPlanta).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                Agendamiento = doc.toObject(AgendamientoDTO.class);
            }

            LocalDateTime hoy = LocalDateTime.now();
            int dia = hoy.getDayOfMonth();
            int mes = hoy.getMonthValue();
            int anio = hoy.getYear();
            int minuto = hoy.getMinute();
            int hora = hoy.getHour();

            String fecha = Agendamiento.getFecha();

            int AnioBd = Integer.parseInt(fecha.split("-")[0]);
            int MesBd = Integer.parseInt(fecha.split("-")[1]);
            int DiaBd = Integer.parseInt(fecha.split("-")[2]);

            String horaInicio = Agendamiento.getHoraInicio();
            String horaFinal = Agendamiento.getHoraFin();

            int horaInBd = Integer.parseInt(horaInicio.split(":")[0]);
            int MinutosInBd = Integer.parseInt(horaInicio.split(":")[1]);
            int horaFinBd = Integer.parseInt(horaFinal.split(":")[0]);
            int MinutosFinBd = Integer.parseInt(horaFinal.split(":")[1]);

            if (dia == DiaBd && mes == MesBd && anio == AnioBd) {
                if (hora == horaInBd) {
                    if (minuto >= MinutosInBd) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (hora == horaFinBd) {
                    if (minuto < MinutosFinBd) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (hora > horaInBd && hora < horaFinBd) {
                    return true;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
