/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.ejb.facades;


import com.ia.ejb.entidades.Peliculas;
import javax.ejb.Local;
/**
 *
 * @author Kryzpy
 */
@Local
public interface  AdmPanelFacadeLocal {
    
    public Boolean validaLogin(String user, String pass);
    
    public String cargaPelis();
    
    public int nuevaPeli(Peliculas p);
    
    public void modificaPeli(Peliculas p);
    
    public void elimina(Peliculas p);
}
