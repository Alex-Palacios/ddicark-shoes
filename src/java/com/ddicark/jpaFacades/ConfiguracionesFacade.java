/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.entidades.Configuraciones;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DDICARK
 */
@Stateless
public class ConfiguracionesFacade extends AbstractFacade<Configuraciones> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionesFacade() {
        super(Configuraciones.class);
    }
    
    /*
     * FUNCION QUE DEVUELVE UNA CONFIGURACION ESPECIFICA
     */
    
    public Configuraciones getConfig(String Tipo, String Nombre){
        Configuraciones config = null;
        Query query = em.createNamedQuery("Configuraciones.configuracion");
        //Se le pasan los parametros a la consulta
        query.setParameter("tipo",Tipo);
        query.setParameter("nombre",Nombre);
        Object consulta = query.getSingleResult();
        if (consulta != null) {
            config = (Configuraciones) consulta;
        }
        return config;
    }
    
}
