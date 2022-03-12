/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service.impl;

import com.google.api.client.util.IOUtils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import static com.itextpdf.text.pdf.PdfName.DATA;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.proyecto1.ServidorLaboratorio.dto.CaidaLibreDTO;
import com.proyecto1.ServidorLaboratorio.dto.AgendamientoDTO;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.LeyHookeDTO;
import com.proyecto1.ServidorLaboratorio.dto.MovimientoParabolicoDTO;
import com.proyecto1.ServidorLaboratorio.dto.ParticipantesDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import com.proyecto1.ServidorLaboratorio.dto.UsuarioDTO;
import com.proyecto1.ServidorLaboratorio.firebase.FirebaseInitializer;
import com.proyecto1.ServidorLaboratorio.firebase.RealTime;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.servlet.ModelAndView;
import static org.springframework.web.servlet.function.RequestPredicates.param;

/**
 *
 * @author admin
 */
@Service
public class LaboratorioManagementServiceImpl implements LaboratorioManagementService {

    @Autowired
    private FirebaseInitializer firebase;

    @Autowired
    private RealTime firebase2;

    @Override
    public Boolean descargar() throws MalformedURLException, IOException, Exception {
        try {
            String ruta = System.getProperty("user.home");
            // Url con la foto
            URL url = new URL(
                    "https://firebasestorage.googleapis.com/v0/b/post-proyecto1.appspot.com/o/prueba.jpeg?alt=media&token=8d7b3131-6094-4580-8a2b-fd8f4fae5d46");

            // establecemos conexion
            URLConnection urlCon = url.openConnection();

            // Sacamos por pantalla el tipo de fichero
            System.out.println(urlCon.getContentType());

            // Se obtiene el inputStream de la foto web y se abre el fichero
            // local.
            InputStream is = urlCon.getInputStream();
            FileOutputStream fos = new FileOutputStream(ruta + "/Downloads/1.jpg");

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
        }
        /*String ruta = System.getProperty("user.home");
        download("https://firebasestorage.googleapis.com/v0/b/post-proyecto1.appspot.com/o/1.jpg?alt=media&token=cacf003c-1278-4ea0-adef-be5e50379e86", "1.jpg?alt=media&token=cacf003c-1278-4ea0-adef-be5e50379e86", ruta + "/Downloads/");
        return true;*/
        return true;
    }

    @Override
    public Boolean probarCSV() {
        try {
            String ruta = System.getProperty("user.home");
            String currentPath = Paths.get("").toAbsolutePath().normalize().toString();
            String downloadFolder = ruta + "/Downloads/";
            String downloadPath = ruta + "/Downloads/";
            File newFolder = new File(downloadPath);
            boolean dirCreated = newFolder.mkdir();

            // get current time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-dd_HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));
            String fileName = "Reporte_" + dtf.format(now) + ".csv";

            // Whatever the file path is.
            File statText = new File(downloadPath + "/" + fileName);
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);

            for (int i = 0; i < 10; i++) {
                w.write("isac ñáéíóúü, xxxx, yyyy, zzzz, aaaa, bbbb, ccccc, dddd, eeee, ffff, gggg, erick\n");
            }
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file " + e);
        }
        return true;
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
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").get();
        try {
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

    

    /*@Override
    public Boolean crearPdf() {
        List<Laboratorio_Caida_LibreDTO> response = new ArrayList<>();
        Laboratorio_Caida_LibreDTO post;

        Document documento = new Document();

        String ruta = System.getProperty("user.home");
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Downloads/Datos_Practica_Caida_Libre.pdf"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        documento.open();

        PdfPTable tabla = new PdfPTable(3);
        tabla.addCell("altura");
        tabla.addCell("numLanzamientos");
        tabla.addCell("tiempo");

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("LABORATORIO_CAIDA_LIBRE").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(Laboratorio_Caida_LibreDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < response.size(); i++) {
            tabla.addCell(String.valueOf(response.get(i).getAltura()));
            tabla.addCell(String.valueOf(response.get(i).getTiempo()));
            tabla.addCell(String.valueOf(response.get(i).getNumLanzamientos()));

        }
        try {
            documento.add(tabla);
        } catch (DocumentException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        documento.close();

        return Boolean.TRUE;
    }*/
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
        List<String> response = new ArrayList<>();
        List<String> agendamiento = new ArrayList<>();
        List<String> nombres = new ArrayList<>();
        response = buscarGrupo(codGrupal);
        agendamiento = buscarAgendamiento(codGrupal);
        Boolean bandera = false;
        Map<String, Object> docData = new HashMap<>();
        nombres = obtenerNombresEstudiantes(codGrupal);
        docData.put("codGrupal", codGrupal);
        docData.put("nombres", nombres);
        ApiFuture<WriteResult> writeResultApiFutureHistorial = getCollection("HISTORIAL").document().create(docData);
        //ApiFuture<WriteResult> writeResultApiFutureNombres = getCollection("HISTORIAL").document(Agendamiento).update("codGrupal", codGrupal, "nombres", FieldValue.arrayUnion(nombres));
        for (int i = 0; i < agendamiento.size(); i++) {
            //ApiFuture<WriteResult> writeResultApiFutureAgendamiento = getCollection("AGENDAMIENTO").document(agendamiento.get(i)).delete();
        }
        for (int id = 0; id < response.size(); id++) {

            ApiFuture<WriteResult> writeResultApiFuture = getCollection("PARTICIPANTES").document(response.get(id)).delete();
            try {
                if (null != writeResultApiFutureHistorial.get()) {
                    bandera = true;
                } else {
                    bandera = false;
                }
            } catch (Exception e) {
                bandera = false;
            }
        }
        return bandera;
    }

    public ArrayList<String> obtenerNombresEstudiantes(int codGrupal) {
        ArrayList<String> nombres = new ArrayList();
        ParticipantesDTO participantes;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").whereEqualTo("codGrupal", codGrupal).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                participantes = doc.toObject(ParticipantesDTO.class
                );
                participantes.setId(doc.getId());
                nombres.add(participantes.getCorreo());
                //System.out.println(cursos);
            }
            return nombres;
            //return cursos;
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> buscarAgendamiento(int codGrupal) {
        List<String> response = new ArrayList<>();
        ParticipantesDTO participantes;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("AGENDAMIENTO").whereEqualTo("codGrupal", codGrupal).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                participantes = doc.toObject(ParticipantesDTO.class
                );
                participantes.setId(doc.getId());
                response.add(participantes.getId());

            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> buscarGrupo(int codGrupal) {
        List<String> response = new ArrayList<>();
        ParticipantesDTO participantes;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").whereEqualTo("codGrupal", codGrupal).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                participantes = doc.toObject(ParticipantesDTO.class
                );
                participantes.setId(doc.getId());
                response.add(participantes.getId());

            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean buscarCompletitudEstudiantes(int codGrupal) {
        GrupoDTO grupo;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").whereEqualTo("codGrupal", codGrupal).get();

        int contados = 0;
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                grupo = doc.toObject(GrupoDTO.class
                );
                grupo.setId(doc.getId());
                String id = grupo.getId();
                if (grupo.getEstado() == 1) {
                    contados += 1;
                }
            }
            //AQUI DEBE PONERSE DEPENDIENDO DEL NUMERO POR GRUPOS
            if (contados == 3) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String buscarQuienEsLider(String correo) {
        ParticipantesDTO participantes;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").whereEqualTo("correo", correo).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                participantes = doc.toObject(ParticipantesDTO.class
                );
                return participantes.getRol();
            }
            return "";

        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public Integer saberCodigoGrupo(String correo) {
        ParticipantesDTO participantes;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").whereEqualTo("correo", correo).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                participantes = doc.toObject(ParticipantesDTO.class
                );
                return participantes.getCodGrupal();
            }
            return 0;

        } catch (Exception e) {
            return -1;
        }
    }

    private CollectionReference getCollection(String Colecion) {
        return firebase.getFirestore().collection(Colecion);
    }

    private Map<String, Object> getDocDataParticipantes(ParticipantesDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("codGrupal", post.getCodGrupal());
        docData.put("estado", post.getEstado());
        docData.put("correo", post.getCorreo());
        docData.put("rol", post.getRol());
        return docData;
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

    @Override
    public ArrayList<String> listar_Altura_CL(int codigo_planta) {
        ArrayList<String> rangos_altura;
        CaidaLibreDTO laboratorio_caida_libre;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_CAIDA_LIBRE").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_caida_libre = doc.toObject(CaidaLibreDTO.class
                );
                laboratorio_caida_libre.setId(doc.getId());
                rangos_altura = laboratorio_caida_libre.getRangos_altura();
                return rangos_altura;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<String> listar_Elongacion_LH(int codigo_planta) {
        ArrayList<String> rangos_elongacion;
        LeyHookeDTO laboratorio_ley_hooke;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_LEY_HOOKE").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_ley_hooke = doc.toObject(LeyHookeDTO.class
                );
                laboratorio_ley_hooke.setId(doc.getId());
                rangos_elongacion = laboratorio_ley_hooke.getRangos_elongacion();
                return rangos_elongacion;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<String> listar_Fuerza_LH(int codigo_planta) {
        ArrayList<String> rangos_fuerza;
        LeyHookeDTO laboratorio_ley_hooke;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_LEY_HOOKE").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_ley_hooke = doc.toObject(LeyHookeDTO.class
                );
                laboratorio_ley_hooke.setId(doc.getId());
                rangos_fuerza = laboratorio_ley_hooke.getRangos_fuerza();
                return rangos_fuerza;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<String> listar_Angulo_MP(int codigo_planta) {
        ArrayList<String> rangos_angulo;
        MovimientoParabolicoDTO laboratorio_movimiento_parabolico;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_MOVIMIENTO_PARABOLICO").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_movimiento_parabolico = doc.toObject(MovimientoParabolicoDTO.class
                );
                laboratorio_movimiento_parabolico.setId(doc.getId());
                rangos_angulo = laboratorio_movimiento_parabolico.getRango_angulo();
                return rangos_angulo;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<String> listar_Velocidad_MP(int codigo_planta) {
        ArrayList<String> rangos_velocidad;
        MovimientoParabolicoDTO laboratorio_movimiento_parabolico;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_MOVIMIENTO_PARABOLICO").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_movimiento_parabolico = doc.toObject(MovimientoParabolicoDTO.class
                );
                laboratorio_movimiento_parabolico.setId(doc.getId());
                rangos_velocidad = laboratorio_movimiento_parabolico.getRango_velocidad();
                return rangos_velocidad;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public Boolean iniciarProceso() {
        firebase2.iniciar();
        return true;
    }

    @Override
    public Boolean GuardarCaidaLibre() {
        ArrayList<String> elongaciones = new ArrayList<>();
        String prueba = firebase2.consultas("Planta1", "peso").toString();
        System.out.println(prueba);
        //firebase2.setElongaciones(Consultas.child("Planta1").child("peso").getValue().toString());
        String prueba2 = firebase2.consultas("Planta1", "elongaciones").toString();
        for (int i = 3; i < 7; i++) {
            String valor2 = prueba2.split("=")[i];
            String valorReal2 = valor2.split(",")[0];
            elongaciones.add(valorReal2);

        }
        String ultimo_valor = elongaciones.get(3).replace("}", "");
        elongaciones.set(3, ultimo_valor);

        System.out.println(elongaciones);

        //System.out.println(valorReal);
        return true;
    }

    @Override
    public Boolean GuardarLeyHooke() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean GuardarMovimientoParaolico() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
