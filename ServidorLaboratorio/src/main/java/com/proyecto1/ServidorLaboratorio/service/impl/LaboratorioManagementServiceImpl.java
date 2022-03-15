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
import com.proyecto1.ServidorLaboratorio.dto.Variable_CaidaLibreDTO;
import com.proyecto1.ServidorLaboratorio.dto.AgendamientoDTO;
import com.proyecto1.ServidorLaboratorio.dto.CaidaLibreDTO;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.LeyHookeDTO;
import com.proyecto1.ServidorLaboratorio.dto.MovimientoParabolicoDTO;
import com.proyecto1.ServidorLaboratorio.dto.Variable_LeyHookeDTO;
import com.proyecto1.ServidorLaboratorio.dto.Variable_MovimientoParabolicoDTO;
import com.proyecto1.ServidorLaboratorio.dto.ParticipantesDTO;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
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
    public Boolean descargarDatos(int codigo_planta) {
        try {
            //Datos
            ParticipantesDTO participantes;
            LeyHookeDTO labLeyHooke;
            CaidaLibreDTO labCaidaLibre;
            MovimientoParabolicoDTO labMovimientoParabolico;
            int codigo_grupo = 0;
            String lider = "";
            String nombre_planta = "";
            ArrayList<String> nombres_estudiantes = new ArrayList<>();
            //Array de cada planta
            //Planta 1
            ArrayList<Double> valores_elongaciones = new ArrayList<>();
            ArrayList<Double> valores_pesos = new ArrayList<>();
            //Planta 2
            ArrayList<Double> valores_errores = new ArrayList<>();
            ArrayList<Double> valores_tiempo = new ArrayList<>();
            //Planta 3
            ArrayList<Double> valores_x = new ArrayList<>();
            ArrayList<Double> valores_y = new ArrayList<>();
            //Carpeta
            String ruta = System.getProperty("user.home");
            String currentPath = Paths.get("").toAbsolutePath().normalize().toString();
            //String downloadFolder = ruta + "/Downloads/";
            String downloadPath = ruta + "/Downloads/";
            File newFolder = new File(downloadPath);
            boolean dirCreated = newFolder.mkdir();

            if (codigo_planta == 1) {
                nombre_planta = "Ley de Hooke";
            } else if (codigo_planta == 2) {
                nombre_planta = "Caida Libre";
            } else {
                nombre_planta = "Movimiento Parabolico";
            }

            // get current time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-dd_HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));
            String fileName = "Entrega de datos_Planta_" + codigo_planta + "_" + nombre_planta + "_" + dtf.format(now) + ".csv";

            // Whatever the file path is.
            File statText = new File(downloadPath + "/" + fileName);
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);

            //Sacamos los datos del grupo
            try {
                //Datos necesarios
                ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").get();
                for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                    participantes = doc.toObject(ParticipantesDTO.class);
                    nombres_estudiantes.add(participantes.getCorreo());
                    codigo_grupo = participantes.getCodGrupal();
                    if (participantes.getRol().equals("Lider")) {
                        lider = participantes.getCorreo();
                    }
                }
                //Datos de cada planta
                if (codigo_planta == 1) {
                    ApiFuture<QuerySnapshot> querySnapshotApiFuture2 = firebase.getFirestore().collection("LABORATORIO_LEY_HOOKE").get();
                    for (DocumentSnapshot doc : querySnapshotApiFuture2.get().getDocuments()) {
                        labLeyHooke = doc.toObject(LeyHookeDTO.class);
                        Collection<Double> valores = labLeyHooke.getElongaciones().values();
                        ArrayList<Double> elongaciones = new ArrayList<>(valores);
                        for (int i = 0; i < elongaciones.size(); i++) {
                            valores_elongaciones.add(elongaciones.get(i));
                        }
                        Collection<Double> peso = labLeyHooke.getPesos().values();
                        ArrayList<Double> pesos = new ArrayList<>(peso);
                        for (int j = 0; j < pesos.size(); j++) {
                            valores_pesos.add(pesos.get(j));
                        }
                    }
                } else if (codigo_planta == 2) {
                    ApiFuture<QuerySnapshot> querySnapshotApiFuture2 = firebase.getFirestore().collection("LABORATORIO_CAIDA_LIBRE").get();
                    for (DocumentSnapshot doc : querySnapshotApiFuture2.get().getDocuments()) {
                        labCaidaLibre = doc.toObject(CaidaLibreDTO.class);
                        Collection<Double> tiempo = labCaidaLibre.getTiempo().values();
                        ArrayList<Double> tiempos = new ArrayList<>(tiempo);
                        for (int i = 0; i < labCaidaLibre.getErrores().size(); i++) {
                            valores_errores.add(tiempos.get(i));
                        }
                        Collection<Double> altura = labCaidaLibre.getTiempo().values();
                        ArrayList<Double> alturas = new ArrayList<>(altura);
                        for (int j = 0; j < labCaidaLibre.getTiempo().size(); j++) {
                            valores_tiempo.add(alturas.get(j));
                        }
                    }
                } else if (codigo_planta == 3) {
                    ApiFuture<QuerySnapshot> querySnapshotApiFuture2 = firebase.getFirestore().collection("LABORATORIO_MOVIMIENTO_PARABOLICO").get();
                    for (DocumentSnapshot doc : querySnapshotApiFuture2.get().getDocuments()) {
                        labMovimientoParabolico = doc.toObject(MovimientoParabolicoDTO.class);
                        for (int i = 0; i < labMovimientoParabolico.getDatos_x().size(); i++) {
                            valores_x.add(labMovimientoParabolico.getDatos_x().get(i));
                        }
                        for (int j = 0; j < labMovimientoParabolico.getDatos_y().size(); j++) {
                            valores_y.add(labMovimientoParabolico.getDatos_y().get(j));
                        }
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(UsuarioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(codigo_grupo);
            w.write("------------------------------------------------------ \n");
            w.write("Codigo Planta: " + codigo_planta + "\n");
            w.write("Nombre Planta: " + nombre_planta + "\n");
            w.write("Codigo Grupo: " + codigo_grupo + "\n");
            w.write("Correos estudiantes: " + nombres_estudiantes + "\n");
            w.write("Lider y simulador del equipo: " + lider + "\n");
            if (codigo_planta == 1) {
                w.write("----------------------Valores X--------------------- \n");
                for (int i = 0; i < valores_elongaciones.size(); i++) {
                    w.write("Elongacion: " + valores_elongaciones.get(i) + "\n");
                }
                w.write("----------------------Valores Y--------------------- \n");
                for (int j = 0; j < valores_pesos.size(); j++) {
                    w.write("Peso: " + valores_pesos.get(j) + "\n");
                }
            } else if (codigo_planta == 2) {
                w.write("----------------------Valores X--------------------- \n");
                for (int i = 0; i < valores_errores.size(); i++) {
                    w.write("Error: " + valores_errores.get(i) + "\n");
                }
                w.write("----------------------Valores Y--------------------- \n");
                for (int j = 0; j < valores_tiempo.size(); j++) {
                    w.write("Tiempo: " + valores_tiempo.get(j) + "\n");
                }
            } else if (codigo_planta == 3) {
                w.write("----------------------Valores X--------------------- \n");
                for (int i = 0; i < valores_x.size(); i++) {
                    w.write("X: " + valores_x.get(i) + "\n");
                }
                w.write("----------------------Valores Y--------------------- \n");
                for (int j = 0; j < valores_y.size(); j++) {
                    w.write("Y: " + valores_y.get(j) + "\n");
                }
            }
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file " + e);
            return false;
        }
        return true;
    }

    @Override
    public List<Variable_LeyHookeDTO> listarDatosHardwareLeyDeHooke() {
        List<Variable_LeyHookeDTO> response = new ArrayList<>();
        Variable_LeyHookeDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("LABORATORIO_LEY_HOOKE").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(Variable_LeyHookeDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Variable_MovimientoParabolicoDTO> listarDatosHardwareMovimientoParabolico() {
        List<Variable_MovimientoParabolicoDTO> response = new ArrayList<>();
        Variable_MovimientoParabolicoDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("LABORATORIO_MOVIMIENTO_PARABOLICO").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(Variable_MovimientoParabolicoDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Variable_CaidaLibreDTO> listarDatosHardwareCaidaLibre() {
        List<Variable_CaidaLibreDTO> response = new ArrayList<>();
        Variable_CaidaLibreDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("LABORATORIO_CAIDA_LIBRE").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(Variable_CaidaLibreDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
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
        UsuarioDTO usuario;
        ParticipantesDTO participantes;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("PARTICIPANTES").whereEqualTo("codGrupal", codGrupal).get();

        int contados = 0;
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                participantes = doc.toObject(ParticipantesDTO.class);
                participantes.setId(doc.getId());
                String id = participantes.getId();
                String correo = participantes.getCorreo();
                ApiFuture<QuerySnapshot> querySnapshotApiFutureUsuario = firebase.getFirestore().collection("USUARIO").whereEqualTo("correo", correo).get();
                try {
                    for (DocumentSnapshot doc2 : querySnapshotApiFutureUsuario.get().getDocuments()) {
                        usuario = doc2.toObject(UsuarioDTO.class);
                        usuario.setId(doc2.getId());
                        if (usuario.getEstado() == 1) {
                            contados += 1;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
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

    @Override
    public ArrayList<String> listar_Altura_CL(int codigo_planta) {
        ArrayList<String> rangos_altura;
        Variable_CaidaLibreDTO laboratorio_caida_libre;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_CAIDA_LIBRE").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_caida_libre = doc.toObject(Variable_CaidaLibreDTO.class);
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
        Variable_LeyHookeDTO laboratorio_ley_hooke;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_LEY_HOOKE").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_ley_hooke = doc.toObject(Variable_LeyHookeDTO.class);
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
        Variable_LeyHookeDTO laboratorio_ley_hooke;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_LEY_HOOKE").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_ley_hooke = doc.toObject(Variable_LeyHookeDTO.class);
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
        Variable_MovimientoParabolicoDTO laboratorio_movimiento_parabolico;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_MOVIMIENTO_PARABOLICO").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_movimiento_parabolico = doc.toObject(Variable_MovimientoParabolicoDTO.class);
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
        Variable_MovimientoParabolicoDTO laboratorio_movimiento_parabolico;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("VARIABLE_MOVIMIENTO_PARABOLICO").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                laboratorio_movimiento_parabolico = doc.toObject(Variable_MovimientoParabolicoDTO.class);
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

    public Boolean GuardarCaidaLibre() {
        CaidaLibreDTO objCaidaLibre = firebase2.getCaidaLibre();
        if (pasarCaidaLibre(objCaidaLibre)) {
            return true;
        }
        return false;
    }

    private boolean pasarCaidaLibre(CaidaLibreDTO objCaidaLibre) {
        Map<String, Object> docData = new HashMap<>();
        Map<String, Double> altura = new HashMap<>();
        objCaidaLibre.setCodigo_planta(2);
        altura.put("h1", 10.0);
        altura.put("h2", 20.0);
        altura.put("h3", 30.0);
        altura.put("h4", 40.0);
        altura.put("h5", 50.0);
        docData.put("codigo_planta", objCaidaLibre.getCodigo_planta());
        docData.put("errores", objCaidaLibre.getErrores());
        docData.put("gravedadN", objCaidaLibre.getGravedadN());
        docData.put("nRep", objCaidaLibre.getNRep());
        docData.put("tiempo", objCaidaLibre.getTiempo());
        docData.put("altura", altura);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection("LABORATORIO_CAIDA_LIBRE").document().create(docData);

        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public Boolean GuardarLeyHooke() {

        LeyHookeDTO objHooke = firebase2.getLeyHooke();
        if (pasarDatosLeyHooke(objHooke)) {
            return true;
        }
        return false;
    }

    private boolean pasarDatosLeyHooke(LeyHookeDTO objHooke) {
        Map<String, Object> docData = new HashMap<>();
        objHooke.setCodigo_planta(1);
        docData.put("codigo_planta", objHooke.getCodigo_planta());
        docData.put("elongaciones", objHooke.getElongaciones());
        docData.put("nRep", objHooke.getNRep());
        docData.put("pesos", objHooke.getPesos());
        ApiFuture<WriteResult> writeResultApiFuture = getCollection("LABORATORIO_LEY_HOOKE").document().create(docData);

        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }


    public Boolean GuardarMovimientoParabolico() {
        MovimientoParabolicoDTO objMovimientoParabolico = firebase2.getMovimientoParabolico();
        if (pasarDatosMovimientoParabolico(objMovimientoParabolico)) {
            return true;
        }
        return false;
    }

    private boolean pasarDatosMovimientoParabolico(MovimientoParabolicoDTO objMovimientoParabolico) {
        Map<String, Object> docData = new HashMap<>();
        objMovimientoParabolico.setCodigo_planta(3);
        docData.put("codigo_planta", objMovimientoParabolico.getCodigo_planta());
        docData.put("datos_x", objMovimientoParabolico.getDatos_x());
        docData.put("datos_y", objMovimientoParabolico.getDatos_y());
        docData.put("nRep", objMovimientoParabolico.getNRep());
        docData.put("tiempo", objMovimientoParabolico.getTiempo());
        docData.put("velocidad", objMovimientoParabolico.getVelocidad());
        docData.put("url", objMovimientoParabolico.getUrl_imagen());

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("LABORATORIO_MOVIMIENTO_PARABOLICO").document().create(docData);

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
    public Boolean finalizarProceso(String planta) {
        if (planta.equals("1")) {
            GuardarLeyHooke();
        }else if(planta.equals("2")){
            GuardarCaidaLibre();
        }else{
            GuardarMovimientoParabolico();
        }
        firebase2.finalizarProceso(planta);
        return true;
    }

    @Override
    public ArrayList<Double> retornarTiempo(int codigo_planta) {
        CaidaLibreDTO caida_libre;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("LABORATORIO_CAIDA_LIBRE").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                caida_libre = doc.toObject(CaidaLibreDTO.class);
                caida_libre.setId(doc.getId());
                Collection<Double> valores = caida_libre.getTiempo().values();
                ArrayList<Double> tiempo = new ArrayList<>(valores);
                //datos_tiempo =;
                //System.out.println(cursos);
                return tiempo;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Double> retornarAltura(int codigo_planta) {
        CaidaLibreDTO caida_libre;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("LABORATORIO_CAIDA_LIBRE").whereEqualTo("codigo_planta", codigo_planta).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                caida_libre = doc.toObject(CaidaLibreDTO.class);
                caida_libre.setId(doc.getId());
                Collection<Double> valores = caida_libre.getAltura().values();
                ArrayList<Double> altura = new ArrayList<>(valores);
                return altura;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Double> retornarElongaciones(int codigo_planta) {
        LeyHookeDTO ley_hooke;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("LABORATORIO_LEY_HOOKE").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                ley_hooke = doc.toObject(LeyHookeDTO.class);
                ley_hooke.setId(doc.getId());
                Collection<Double> valores = ley_hooke.getElongaciones().values();
                ArrayList<Double> elongaciones = new ArrayList<>(valores);
                return elongaciones;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Double> retornarPesos(int codigo_planta) {
        LeyHookeDTO ley_hooke;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("LABORATORIO_LEY_HOOKE").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                ley_hooke = doc.toObject(LeyHookeDTO.class);
                ley_hooke.setId(doc.getId());
                Collection<Double> valores = ley_hooke.getPesos().values();
                ArrayList<Double> pesos = new ArrayList<>(valores);
                return pesos;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Double> retornarX(int codigo_planta) {
        MovimientoParabolicoDTO movimiento_parabolico;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("LABORATORIO_MOVIMIENTO_PARABOLICO").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                movimiento_parabolico = doc.toObject(MovimientoParabolicoDTO.class);
                movimiento_parabolico.setId(doc.getId());
                ArrayList<Double> x = movimiento_parabolico.getDatos_x();
                return x;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Double> retornarY(int codigo_planta) {
        MovimientoParabolicoDTO movimiento_parabolico;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("LABORATORIO_MOVIMIENTO_PARABOLICO").whereEqualTo("codigo_planta", codigo_planta).get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                movimiento_parabolico = doc.toObject(MovimientoParabolicoDTO.class);
                movimiento_parabolico.setId(doc.getId());
                ArrayList<Double> y = movimiento_parabolico.getDatos_y();
                return y;
            }
            //return cursos;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public Boolean iniciarLeyHooke(int peso) {
        int pesobd=0;
        if(peso==50){
             pesobd=1;
        }else if(peso==100){
             pesobd=2;
        }else if(peso==150){
             pesobd=3;
        }else if(peso==200){
             pesobd=4;
        }else if(peso==250){
             pesobd=5;
        }
        
        Boolean bandera = firebase2.iniciarLeyHooke(pesobd);
        return bandera;  
    }

    @Override
    public Boolean iniciarCaidaLibre(int peso) {
         int pesobd=0;
        if(peso==50){
             pesobd=1;
        }else if(peso==100){
             pesobd=2;
        }else if(peso==150){
             pesobd=3;
        }else if(peso==200){
             pesobd=4;
        }else if(peso==250){
             pesobd=5;
        }
        
        Boolean bandera = firebase2.iniciarCaidaLibre(pesobd);
        return bandera; 
    }

    @Override
    public Boolean iniciarMovimientoParabolico(int angulo, int velocidad) {
          int angulobd=0;
          int velocidadbd=0;
        if(angulo==50){
             angulobd=1;
        }else if(angulo==100){
              angulobd=2;
        }else if(angulo==150){
              angulobd=3;
        }else if(angulo==200){
              angulobd=4;
        }else if(angulo==250){
              angulobd=5;
        }
          
        if(velocidad==50){
            velocidadbd=1;
        }else if(velocidad==100){
             velocidadbd=2;
        }else if(velocidad==150){
             velocidadbd=3;
        }else if(velocidad==200){
             velocidadbd=4;
        }else if(velocidad==250){
             velocidadbd=5;
        }
        Boolean bandera = firebase2.iniciarMovimientoParabolico(angulobd, velocidadbd);
        return bandera; 
    }
}
