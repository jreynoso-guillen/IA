/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.backbean.admin;

import com.ia.ejb.entidades.Peliculas;
import com.ia.ejb.facades.AdmPanelFacadeLocal;
import com.ia.ejb.utilerias.XMLTools;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Kryzpy
 */

@ManagedBean
@SessionScoped
public class AdminBackBean implements Serializable{
    
    private String usuario="";
    private String pass="";
    @EJB
    private AdmPanelFacadeLocal admPanel;
    private List<Peliculas> listPeli;
    
    private Peliculas nuevaPeli;
    
    private Peliculas selectedPeli;
    
    public AdminBackBean(){
        listPeli= new ArrayList<>();
    }
    
    public String login(){
        boolean validado=admPanel.validaLogin(usuario, pass);
        if(validado){
            System.out.println("Entro");
            FacesContext fctx = FacesContext.getCurrentInstance();
            ExternalContext ectx = fctx.getExternalContext();
            HttpSession session = (HttpSession) ectx.getSession(false);
            session.setAttribute("sessionId", session.getId());
            session.setAttribute("idUsuario", usuario);
            return "index";
        }else{
            System.out.println("NoEntro");
            FacesMessage message = new FacesMessage(" Usuario o contraseña no valido. " );
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "administracion";
        }
        
    }
    
    public String salir(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "administracion";
    }
    public String cargaPeliculas(){
        
        System.out.println();
        
        String xmlConsul=admPanel.cargaPelis();
        XMLTools xml = new XMLTools();
        List<String[]> lista= xml.parseXMLtoListString(xmlConsul, "hijo");
        
        for(int i=0;i<lista.size();i++){
            Peliculas aux= new Peliculas();
            aux.setIdpelicula(Integer.parseInt(lista.get(i)[0]));
            aux.setTitulo(lista.get(i)[1]);
            aux.setSinopsis(lista.get(i)[2]);
            aux.setPoster(lista.get(i)[3].getBytes());
            aux.setResena(lista.get(i)[4]);
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("dd-MM-yyyy");
            try{
                String fecha=lista.get(i)[5];
                
                Date fechaD = null;
                fechaD=formatoDelTexto.parse(fecha);
                aux.setFechaestreno(fechaD);
            }catch(ParseException e){
                e.printStackTrace();
            }
            
            listPeli.add(aux);
            
        }
        
        return "peliculas";
    }
    
    public void agregaNuevaResena(){
        
        nuevaPeli= new Peliculas();
        
        
        
        
    }
    
    public void guardaNuevaPeli(){
        
        int idNuevaPeli=admPanel.nuevaPeli(nuevaPeli);
        
        if(idNuevaPeli > 0){
            nuevaPeli.setIdpelicula(idNuevaPeli);
            listPeli.add(nuevaPeli);
            FacesMessage message = new FacesMessage(" Reseña agregada " );
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }else{
            FacesMessage message = new FacesMessage("Ocurrio un error " );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        
    }
    
    public void modifica(){
        try{
            admPanel.modificaPeli(selectedPeli);
            FacesMessage message = new FacesMessage("actualizado con éxito. " );
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }catch(Exception e){
            FacesMessage message = new FacesMessage("Ocurrio un error " );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void elimina(){
        try{
            admPanel.elimina(selectedPeli);
            for(Peliculas x:listPeli){
                if(x.getIdpelicula().equals(selectedPeli.getIdpelicula())){
                    listPeli.remove(x);
                }
            }
            FacesMessage message = new FacesMessage("Eliminado con éxito. " );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }catch(Exception e){
        }
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public AdmPanelFacadeLocal getAdmPanel() {
        return admPanel;
    }

    public void setAdmPanel(AdmPanelFacadeLocal admPanel) {
        this.admPanel = admPanel;
    }

    public List<Peliculas> getListPeli() {
        return listPeli;
    }

    public void setListPeli(List<Peliculas> listPeli) {
        this.listPeli = listPeli;
    }

    public Peliculas getNuevaPeli() {
        return nuevaPeli;
    }

    public void setNuevaPeli(Peliculas nuevaPeli) {
        this.nuevaPeli = nuevaPeli;
    }

    public Peliculas getSelectedPeli() {
        return selectedPeli;
    }

    public void setSelectedPeli(Peliculas selectedPeli) {
        this.selectedPeli = selectedPeli;
    }
    
    
    
}
