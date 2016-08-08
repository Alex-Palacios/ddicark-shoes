/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Municipios;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DDICARK
 */
@Stateless
public class MunicipiosFacade extends AbstractFacade<Municipios> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MunicipiosFacade() {
        super(Municipios.class);
    }
   
    /*Lista de los MUNICIPIOS DE UN DEPTO DETERMINADO
     * 
     */
    public List<String> getDeptos(){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Municipios.deptoList");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo consultar Departamentos a la BD");
            return resultado;
        }
    
    }
    
    /*Lista de los MUNICIPIOS DE UN DEPTO DETERMINADO
     * 
     */
    public List<String> getMunicipios(String depto){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Municipios.municipiosByDepto");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("departamento",depto);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo consultar municipio de " + depto);
            return resultado;
        }
    
    }
}
