/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Remesa;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ALEX
 */
@Stateless
public class RemesaFacade extends AbstractFacade<Remesa> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RemesaFacade() {
        super(Remesa.class);
    }
    
    
    
    /*
     * FUNCION QUE EL SIGUIENTE CLAVE DE REMESA
     */
    public int getNextIdRemesa() {
        Query query = em.createNamedQuery("Remesa.maxID");
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
    
    
    /*
     * CONSULTA FACTURAS EMITIDAS POR MES Y AÑO
     */
    public List<Remesa> consultarRemesas(int month, int year){
        List<Remesa> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Remesa.byMonthYear"); 
            consulta.setParameter("month", month);
            consulta.setParameter("year", year);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LAS REMESAS en la Bases de Datos");
            return resultado;
        }
        
    }
}
