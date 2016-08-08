/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.entidades.Transaccion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DDICARK
 */
@Stateless
public class TransaccionFacade extends AbstractFacade<Transaccion> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TransaccionFacade() {
        super(Transaccion.class);
    }
    
    
    /**
     * *****************Inicio de modulo******************* 
     *      Nombre del módulo:
     *          - getNextIdTransac 
     *      Objetivo: 
     *          - realiza una consulta a la base de datos para obtener el id maximo
     *            de los artículos de oro, luego lo convierte en entero
     *            y le suma uno, para obtener el siguiente id
     */
    public int getNextIdTransac() {
        Query query = em.createNamedQuery("Transaccion.maxID");
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
}
