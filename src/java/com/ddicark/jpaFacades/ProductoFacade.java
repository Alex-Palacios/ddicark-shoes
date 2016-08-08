/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Producto;
import java.util.List;
import java.sql.ResultSet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DDICARK
 */
@Stateless
public class ProductoFacade extends AbstractFacade<Producto> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductoFacade() {
        super(Producto.class);
    }
    
    
    /*Lista de los productos (estilos) segun el tipo de producto
     *      Entradas: -tipo: tipo de producto
     *      Salidas:   -resultado : lista de productos del tipo especificado
     */
    public List<Producto> getEstilosTipoProducto(int tipo){
         List<Producto> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Producto.findByTipoProducto"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("tipoProducto",tipo);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
           // new funciones().setMsj(1,"ERROR: No se pudo realizar consultar existencia de retaceos por Importaciones a la Bases de Datos");
            return resultado;
        }
    
    }
    
    
    
}
