/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Envio;
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
public class EnvioFacade extends AbstractFacade<Envio> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EnvioFacade() {
        super(Envio.class);
    }
    
    
    /*
     * FUNCION QUE DEVUELVE LA SIGUIENTE CLAVE O NUMERO DE ENVIO
     */
    public int getNextIdEnvio() {
        Query query = em.createNamedQuery("Envio.maxID");
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
    
    /*Lista de envios Por Estado(DESPACHADO, FACTURADO)
     */
    public List<Envio> getEnviosEnProceso(){
        List<Envio> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Envio.enProcesoVenta");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de envios en proceso de la BD");
            return resultado;
        }
    
    }
    
    
    /*
     * CONSULTA ENVIOS POR MES Y AÑO
     */
    public List<Envio> consultarEnvios(int month, int year){
        List<Envio> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Envio.byMonthYear"); 
            consulta.setParameter("month", month);
            consulta.setParameter("year", year);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar DE LOS ENVIOS en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    
}
