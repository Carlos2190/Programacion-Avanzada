/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Celular;
import Clases.Recargas;
import Logica.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Carlos Espinoza
 */
public class RecargasJpaController implements Serializable {

    public RecargasJpaController() {
        this.emf = Persistence.createEntityManagerFactory("com.mycompany_Adicional_P_Celular_jar_1.0-SNAPSHOTPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recargas recargas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Celular celular = recargas.getCelular();
            if (celular != null) {
                celular = em.getReference(celular.getClass(), celular.getIdCel());
                recargas.setCelular(celular);
            }
            em.persist(recargas);
            if (celular != null) {
                celular.getRecargas().add(recargas);
                celular = em.merge(celular);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recargas recargas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recargas persistentRecargas = em.find(Recargas.class, recargas.getIdReca());
            Celular celularOld = persistentRecargas.getCelular();
            Celular celularNew = recargas.getCelular();
            if (celularNew != null) {
                celularNew = em.getReference(celularNew.getClass(), celularNew.getIdCel());
                recargas.setCelular(celularNew);
            }
            recargas = em.merge(recargas);
            if (celularOld != null && !celularOld.equals(celularNew)) {
                celularOld.getRecargas().remove(recargas);
                celularOld = em.merge(celularOld);
            }
            if (celularNew != null && !celularNew.equals(celularOld)) {
                celularNew.getRecargas().add(recargas);
                celularNew = em.merge(celularNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = recargas.getIdReca();
                if (findRecargas(id) == null) {
                    throw new NonexistentEntityException("The recargas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recargas recargas;
            try {
                recargas = em.getReference(Recargas.class, id);
                recargas.getIdReca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recargas with id " + id + " no longer exists.", enfe);
            }
            Celular celular = recargas.getCelular();
            if (celular != null) {
                celular.getRecargas().remove(recargas);
                celular = em.merge(celular);
            }
            em.remove(recargas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recargas> findRecargasEntities() {
        return findRecargasEntities(true, -1, -1);
    }

    public List<Recargas> findRecargasEntities(int maxResults, int firstResult) {
        return findRecargasEntities(false, maxResults, firstResult);
    }

    private List<Recargas> findRecargasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recargas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Recargas findRecargas(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recargas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecargasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recargas> rt = cq.from(Recargas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
