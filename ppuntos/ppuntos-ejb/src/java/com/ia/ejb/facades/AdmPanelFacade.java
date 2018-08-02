/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.ejb.facades;

import com.ia.ejb.entidades.Peliculas;
import com.ia.ejb.entidades.Usuariosadmin;
import com.ia.ejb.utilerias.XMLTools;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Kryzpy
 */

@Stateless
public class AdmPanelFacade implements AdmPanelFacadeLocal{
    
    @PersistenceContext(unitName = "ppuntos-ejbPU")
    private EntityManager em;
    @Resource
    SessionContext ctx;
    
    
    public Boolean validaLogin(String user, String pass){
        
        Boolean retorno = false;
        
        String sql="SELECT * FROM `usuariosadmin` u WHERE u.`USUARIO`='"+user+"' AND u.`PASS`='"+pass+"'";
        List<Usuariosadmin> u=  em.createNativeQuery(sql, Usuariosadmin.class).getResultList();
        
        if(u.size()>0){
            if(u.get(0).getUsuario().equals(user) && u.get(0).getPass().equals(pass)){
                retorno=true;
            }
        }
        
        
        return retorno;
        
    }
    
    public String cargaPelis(){
        String sql="SELECT * FROM `peliculas` p ORDER BY p.`IDPELICULA` DESC";
        
        return XMLTools.xmlQueryBase(sql, "base", "hijo");
    }
    
    public int nuevaPeli(Peliculas p){
        
        em.persist(p);
        
        return p.getIdpelicula();
        
    }
    
    public void modificaPeli(Peliculas p){
        
        Peliculas pe= (Peliculas) em.find(Peliculas.class, p.getIdpelicula());
        
        pe.setTitulo(p.getTitulo());
        pe.setSinopsis(p.getSinopsis());
        pe.setPoster(p.getPoster());
        pe.setResena(p.getResena());
        pe.setFechaestreno(p.getFechaestreno());
        
        em.merge(pe);
        
    }
    
    public void elimina(Peliculas p){
        Peliculas pe= (Peliculas) em.find(Peliculas.class, p.getIdpelicula());
        em.remove(pe);
    }
    
}
