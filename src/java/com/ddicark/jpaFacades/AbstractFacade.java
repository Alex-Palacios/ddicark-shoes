/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author DDICARK
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();
    
    //Inserta una nueva entidad a la tabla
    public void create(T entity) {
        getEntityManager().persist(entity);
    }
    
    //Actualiza una entidad y si no existe la inserta
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }
    
    //Elimina una entidad de la tabla
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    //Encuantra una entidad por su id
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    //Encuentra todas las entidades de la tabla
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    //Encuentra las entidades en un rango especifico
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    //Cuenta el numero de entidades en la tabla
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
   
}
