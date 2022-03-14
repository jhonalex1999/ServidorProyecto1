package com.proyecto1.ServidorLaboratorio.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
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
import com.proyecto1.ServidorLaboratorio.dto.ParticipantesDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import com.proyecto1.ServidorLaboratorio.firebase.RealTime;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.proyecto1.ServidorLaboratorio.service.PracticaManagementService;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Service
public class PracticaManagementServiceImpl implements PracticaManagementService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public Boolean descargarArchivoProfesor(int codigo_planta) throws MalformedURLException, IOException, Exception {
        try {
            String descripcion = "";
            PracticaDTO practica;

            ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("PRACTICA").whereEqualTo("codigoPlanta", codigo_planta).get();
            if(querySnapshotApiFuture.get().isEmpty()){
                return false;
            }
            try {
                for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                    practica = doc.toObject(PracticaDTO.class);
                    descripcion = practica.getDescripcion();
                    if(descripcion.equals("")){
                        return false;
                    }
                }
            } catch (Exception e) {
                return null;
            }
            String ruta = System.getProperty("user.home");
            // Url con la informacion
            
           
            
            
            URL url = new URL(descripcion);
            
            // establecemos conexion
            URLConnection urlCon = url.openConnection();

            // Sacamos por pantalla el tipo de fichero
            System.out.println(urlCon.getContentType());

            // Se obtiene el inputStream de la foto web y se abre el fichero
            // local.
            InputStream is = urlCon.getInputStream();
            FileOutputStream fos = new FileOutputStream(ruta + "/Downloads/Guia-" + codigo_planta + ".pdf");

            // Lectura de la foto de la web y escritura en fichero local
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            int leido = is.read(array);
            while (leido > 0) {
                fos.write(array, 0, leido);
                leido = is.read(array);
            }

            // cierre de conexion y fichero.
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        /*String ruta = System.getProperty("user.home");
        download("https://firebasestorage.googleapis.com/v0/b/post-proyecto1.appspot.com/o/1.jpg?alt=media&token=cacf003c-1278-4ea0-adef-be5e50379e86", "1.jpg?alt=media&token=cacf003c-1278-4ea0-adef-be5e50379e86", ruta + "/Downloads/");
        return true;*/
        return true;
    }

    @Override
    public List<AgendamientoDTO> listarAgendamiento(int codigoPlanta) {
        List<AgendamientoDTO> response = new ArrayList<>();
        AgendamientoDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("AGENDAMIENTO").whereEqualTo("codigoPlanta", codigoPlanta).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(AgendamientoDTO.class
                );
                post.setIdFranja(doc.getId());
                response.add(post);
                System.out.println(response);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Integer agregarParticipantes(ArrayList<String> participantes, int idFranja) {
        ApiFuture<WriteResult> writeResultApiFuture = null;
        int codGrupal = codGrupal();
        boolean existen = BuscarParticipantes(participantes);
        if (existen == true) {
            for (int i = 0; i < participantes.size(); i++) {
                ParticipantesDTO post = new ParticipantesDTO();

                post.setCodGrupal(codGrupal);
                post.setCorreo(participantes.get(i));
                post.setEstado(0);
                if (i == 0) {
                    post.setRol("Lider");
                } else {
                    post.setRol("Observador");
                }
                Map<String, Object> docData = getDocDataParticipantes(post);
                writeResultApiFuture = getCollection("PARTICIPANTES").document().create(docData);
            }
            try {
                if (null != writeResultApiFuture.get()) {
                    agregarHorario(idFranja, codGrupal);
                    return 1;
                }
                return -1;
            } catch (Exception e) {
                return -1;
            }
        } else {
            return 0;
        }
    }

    private Boolean BuscarParticipantes(ArrayList<String> participantes) {
        boolean bandera = false;
        for (int i = 0; i < participantes.size(); i++) {
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("USUARIO").whereEqualTo("correo", participantes.get(i)).get();
            
            try {
                if (querySnapshotApiFuture.get().isEmpty()) {
                    bandera = false;
                    break;
                } else {
                    bandera = true;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bandera;
    }

    private int codGrupal() {
        ParticipantesDTO participantes;
        List<Integer> codActuales = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").get();
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                participantes = doc.toObject(ParticipantesDTO.class);
                codActuales.add(participantes.getCodGrupal());

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(UsuarioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(UsuarioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (codActuales.isEmpty()) {
            return 1;
        } else {
            int mayor = codActuales.get(0);
            // Recorrer arreglo y ver si no es así
            // (comenzar desde el 1 porque el 0 ya lo tenemos contemplado arriba)
            for (int x = 1; x < codActuales.size(); x++) {
                if (codActuales.get(x) > mayor) {
                    mayor = codActuales.get(x);
                }
            }
            int codigo = mayor + 1;

            return codigo;
        }
    }

    private Map<String, Object> getDocDataParticipantes(ParticipantesDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("codGrupal", post.getCodGrupal());
        docData.put("estado", post.getEstado());
        docData.put("correo", post.getCorreo());
        docData.put("rol", post.getRol());
        return docData;
    }

    private Boolean agregarHorario(int idAgendamiento, int codGrupal) {
        String Agendamiento = BuscarAgendamiento(idAgendamiento);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("AGENDAMIENTO").document(Agendamiento).update("codGrupal", codGrupal, "estadoDisposicion", false);
        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }

    }

    private String BuscarAgendamiento(int idAgendamiento) {
        String Agendamiento = "vacio";
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("AGENDAMIENTO").whereEqualTo("idAgendamiento", idAgendamiento).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                Agendamiento = doc.getId();
                return Agendamiento;

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return Agendamiento;
    }

    @Override
    public Boolean buscarHorario(int idAgendamiento, int codGrupal) {

        ApiFuture<QuerySnapshot> querySnapshotApiFutur = firebase.getFirestore().collection("AGENDAMIENTO").whereEqualTo("idAgendamiento", idAgendamiento).whereEqualTo("codGrupal", codGrupal).get();
        try {
            if (querySnapshotApiFutur.get().isEmpty()) {
                return false;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }

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

    private CollectionReference getCollection(String Colecion) {
        return firebase.getFirestore().collection(Colecion);
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
@Override
    public int duracion(int codGrupal, int codigoPlanta) {
        AgendamientoDTO Agendamiento = new AgendamientoDTO();
        int resultado = 0;
        int resultadoMin = 0;
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

            int horaInBdC = horaInBd * 60 * 60;
            int MinutosInBdC = MinutosInBd * 60;
            int horaFinBdC = horaFinBd * 60 * 60;
            int MinutosFinBdC = MinutosFinBd * 60;
            int minutoC = minuto * 60;
            int horaC = hora * 60 * 60;

            if (dia == DiaBd && mes == MesBd && anio == AnioBd) {
                if (hora == horaInBd) {
                    if (minuto >= MinutosInBd) {

                        if (horaInBd == horaFinBd) {
                            resultado = (MinutosFinBdC - minutoC);
                        } else {
                            resultado = (horaFinBdC + MinutosFinBdC) - (horaInBdC + minutoC) ;
                        }
                        return resultado;
                    } else {
                        return -1;
                    }
                } else if (hora == horaFinBd) {
                    if (minuto < MinutosFinBd) {
                        resultado = (MinutosFinBdC - minutoC);
                        return resultado;
                    } else {
                        return -1;
                    }
                } else if (hora > horaInBd && hora < horaFinBd) {
                    resultado = (horaFinBdC + MinutosFinBdC) - (horaC + minutoC);
                    return resultado;
                }
            } else {
                return -1;
            }

        } catch (Exception e) {
            return -1;
        }
        return -1;
    }


}
