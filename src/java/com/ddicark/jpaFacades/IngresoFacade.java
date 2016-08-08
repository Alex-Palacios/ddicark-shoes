/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.entidades.Ingreso;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DDICARK
 */
@Stateless
public class IngresoFacade extends AbstractFacade<Ingreso> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IngresoFacade() {
        super(Ingreso.class);
    }
    
    /*
     * FUNCION QUE EL SIGUIENTE CLAVE DE INGRESO
     */
    public int getNextIdIngreso() {
        Query query = em.createNamedQuery("Ingreso.maxID");
        /* Objetivo: Realiza una consulta a la base de datos
         */
        Object consulta = query.getSingleResult();
        /* Objetivo: Obtiene un unico resutado de la consulta realizada
         */
        int resultado = 0;
        /* Objetivo: almacena el siguiente id valido
         */
        if (consulta != null) {
            resultado = Integer.parseInt(consulta.toString());
        }
        resultado=resultado==0?1:++resultado;
        return resultado;
    }



}//FIN CLASE INGRESO CONTROLLER
