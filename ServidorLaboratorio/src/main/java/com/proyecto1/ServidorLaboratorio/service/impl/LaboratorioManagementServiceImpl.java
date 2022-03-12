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
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
import java.io.IOException;
import java.lang.reflect.Array;
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

    @Autowired
    private RealTime firebase2;

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
            //tabla.addCell(String.valueOf(response.get(i).getAltura()));
            //tabla.addCell(String.valueOf(response.get(i).getTiempo()));
            //tabla.addCell(String.valueOf(response.get(i).getNumLanzamientos()));

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
                post = doc.toObject(AgendamientoDTO.class);
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
            Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
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
                participantes = doc.toObject(ParticipantesDTO.class);
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
                participantes = doc.toObject(ParticipantesDTO.class);
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
                participantes = doc.toObject(ParticipantesDTO.class);
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
                grupo = doc.toObject(GrupoDTO.class);
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
                participantes = doc.toObject(ParticipantesDTO.class);
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
                participantes = doc.toObject(ParticipantesDTO.class);
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
            Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Participante;
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

    @Override
    public Boolean iniciarProceso(String planta) {
        firebase2.iniciar(planta);
        return true;
    }

    @Override
    public Boolean GuardarCaidaLibre() {
     ArrayList<String> errores = new ArrayList<>();
     ArrayList<String> gravedadN= new ArrayList<>();
     ArrayList<String> tiempo = new ArrayList<>();
     CaidaLibreDTO objCaidaLibre= new   CaidaLibreDTO();
        //numero de repeticiones
        String nRepDB = firebase2.consultas("Planta2", "nRep").toString();
        nRepDB = nRepDB.split("=")[2];
        nRepDB= nRepDB.replace("}", "");
        nRepDB= nRepDB.replace(" ", "");
        objCaidaLibre.setNRep(Integer.parseInt(nRepDB));
       
        //array de errores
        int longitud=objCaidaLibre.getNRep()+3;
        
        String erroresDB = firebase2.consultas("Planta2", "errores").toString();
        for (int i = 3; i < longitud; i++) {
            String aux = erroresDB.split("=")[i];
            String valor = aux.split(",")[0];
            errores.add(valor);
        }
        String ultimo_valor_elongaciones=errores.get(objCaidaLibre.getNRep()-1).replace("}", "");
        errores.set(objCaidaLibre.getNRep()-1, ultimo_valor_elongaciones);
        objCaidaLibre.setErrores(errores);
    
           
        // array de gravedadN
       
        String gravedadNDB = firebase2.consultas("Planta2", "gravedadN").toString();
        for (int i = 3; i < longitud; i++) {
            String aux = gravedadNDB.split("=")[i];
            String valor = aux.split(",")[0];
            gravedadN.add(valor);
        }
        String ultimo_valor_pesos=gravedadN.get(objCaidaLibre.getNRep()-1).replace("}", "");
        gravedadN.set(objCaidaLibre.getNRep()-1, ultimo_valor_pesos);
        objCaidaLibre.setGravedadN(gravedadN);
        
        // array tiempo 
        
        String tiempoDB = firebase2.consultas("Planta2", "tiempo").toString();
        for (int i = 3; i < longitud; i++) {
            String aux = tiempoDB.split("=")[i];
            String valor = aux.split(",")[0];
            tiempo.add(valor);
        }
        String ultimo_valor_tiempo=tiempo.get(objCaidaLibre.getNRep()-1).replace("}", "");
        tiempo.set(objCaidaLibre.getNRep()-1, ultimo_valor_tiempo);
        objCaidaLibre.setTiempo(tiempo);
        
       if(pasarCaidaLibre(objCaidaLibre)){
       return true;
       }
        
        return false;
    }
       private boolean pasarCaidaLibre(CaidaLibreDTO objCaidaLibre){
       Map<String, Object> docData = new HashMap<>();
        docData.put("errores", objCaidaLibre.getErrores());
        docData.put("gravedadN", objCaidaLibre.getGravedadN());
        docData.put("nRep", objCaidaLibre.getNRep());
        docData.put("tiempo", objCaidaLibre.getTiempo());
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
    
    @Override
    public Boolean GuardarLeyHooke() {
        ArrayList<String> elongaciones = new ArrayList<>();
        ArrayList<String> pesos = new ArrayList<>();
        LeyHookeDTO objHooke= new LeyHookeDTO();
        //numero de repeticiones
        String nRepDB = firebase2.consultas("Planta1", "nRep").toString();
        nRepDB = nRepDB.split("=")[2];
        nRepDB= nRepDB.replace("}", "");
        nRepDB= nRepDB.replace(" ", "");
        objHooke.setNRep(Integer.parseInt(nRepDB));
       
        //array de longitudes
        int longitud=objHooke.getNRep()+3;
        
        String elongacionesDB = firebase2.consultas("Planta1", "elongaciones").toString();
        for (int i = 3; i < longitud; i++) {
            String aux = elongacionesDB.split("=")[i];
            String valor = aux.split(",")[0];
            elongaciones.add(valor);
        }
        String ultimo_valor_elongaciones=elongaciones.get(objHooke.getNRep()-1).replace("}", "");
        elongaciones.set(objHooke.getNRep()-1, ultimo_valor_elongaciones);
        objHooke.setElongaciones(elongaciones);
    
           
        // array de pesos
       
        String pesosDB = firebase2.consultas("Planta1", "pesos").toString();
        for (int i = 3; i < longitud; i++) {
            String aux = pesosDB.split("=")[i];
            String valor = aux.split(",")[0];
            pesos.add(valor);
        }
        String ultimo_valor_pesos=pesos.get(objHooke.getNRep()-1).replace("}", "");
        pesos.set(objHooke.getNRep()-1, ultimo_valor_pesos);
        objHooke.setPesos(pesos);
        
        System.out.println(objHooke.getElongaciones());
        System.out.println(objHooke.getPesos());
        
       if(pasarDatosLeyHooke(objHooke)){
            return true;
       }

        return false;
    }
    private boolean pasarDatosLeyHooke(LeyHookeDTO objHooke){
       Map<String, Object> docData = new HashMap<>();
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
    
    @Override
    public Boolean GuardarMovimientoParabolico() {
       ArrayList<String> datosX = new ArrayList<>();
       ArrayList<String> datosY = new ArrayList<>();
       ArrayList<String> tiempo = new ArrayList<>();
       ArrayList<String> velocidad = new ArrayList<>();
       MovimientoParabolicoDTO objMovParabolico= new MovimientoParabolicoDTO();
        //numero de repeticiones
        /*String nRepDB = firebase2.consultas("Planta3", "nRep").toString();
        nRepDB = nRepDB.split("=")[2];
        nRepDB= nRepDB.replace("}", "");
        nRepDB= nRepDB.replace(" ", "");
        objMovParabolico.setNRep(Integer.parseInt(nRepDB));
       */
        objMovParabolico.setNRep(41);
        //array datosx
        int longitud=objMovParabolico.getNRep()+3;
        
        String datosXDB = firebase2.consultas("Planta3", "datos_x").toString();
        for (int i = 3; i < longitud; i++) {
            String aux = datosXDB.split("=")[i];
            String valor = aux.split(",")[0];
            datosX.add(valor);
        }
        String ultimo_valor_datosX=datosX.get(objMovParabolico.getNRep()-1).replace("}", "");
        datosX.set(objMovParabolico.getNRep()-1,ultimo_valor_datosX);
        objMovParabolico.setDatosX(datosX);
    
           
        //array datosy

        String datosYDB = firebase2.consultas("Planta3", "datos_y").toString();
        for (int i = 3; i < longitud; i++) {
            String aux =  datosYDB.split("=")[i];
            String valor = aux.split(",")[0];
            datosY.add(valor);
        }
        String ultimo_valor_datosY=datosY.get(objMovParabolico.getNRep()-1).replace("}", "");
        datosY.set(objMovParabolico.getNRep()-1, ultimo_valor_datosY);
        objMovParabolico.setDatosY(datosY);
    
        
        // array de tiempo
       
        String tiempoDB = firebase2.consultas("Planta3", "tiempo").toString();
        for (int i = 2; i < 3; i++) {
            String aux = tiempoDB.split("=")[i];
            //String valor = aux.split(",")[0];
            tiempo.add(aux);
        }
        String ultimo_valor_tiempo=tiempo.get(0).replace("}", "");
        tiempo.set(0, ultimo_valor_tiempo);
        objMovParabolico.setTiempo(tiempo);
        
        // array de velocidad
       
        String velocidadDB = firebase2.consultas("Planta3", "velocidad").toString();
        for (int i = 2; i < 3; i++) {
            String aux = velocidadDB.split("=")[i];
            //String valor = aux.split(",")[0];
            velocidad.add(aux);
        }
        String ultimo_valor_velocidad=velocidad.get(0).replace("}", "");
        velocidad.set(0, ultimo_valor_velocidad);
        objMovParabolico.setVelocidad(velocidad);
        
        
        String urlDB = firebase2.consultas("Planta3", "url_imagen").toString();
        urlDB = urlDB.split("=")[2];
        urlDB= urlDB.replace("}", "");
        objMovParabolico.setUrl(urlDB);
        System.out.println( objMovParabolico.getUrl());
        System.out.println( objMovParabolico.getDatosX());
        System.out.println( objMovParabolico.getDatosY());
        System.out.println( objMovParabolico.getTiempo());
        System.out.println( objMovParabolico.getVelocidad());
        
        if(pasarDatosMovimientoParabolico(objMovParabolico)){
         return true;
        }
        
        return false;
    }
    private boolean pasarDatosMovimientoParabolico(MovimientoParabolicoDTO objMovimientoParabolico){
       Map<String, Object> docData = new HashMap<>();
        docData.put("datos_x", objMovimientoParabolico.getDatosX());
        docData.put("datos_y", objMovimientoParabolico.getDatosY());
        docData.put("nRep", objMovimientoParabolico.getNRep());
        docData.put("tiempo", objMovimientoParabolico.getTiempo());
        docData.put("velocidad", objMovimientoParabolico.getVelocidad());
        docData.put("url", objMovimientoParabolico.getUrl());
               
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
        firebase2.finalizarProceso(planta);
        return true;
    }

}
