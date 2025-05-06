/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Clases.Celular;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Cliente;
import Clases.Recargas;
import Logica.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 *
 * @author Carlos Espinoza
 */
public class CelularJpaController implements Serializable {

    public CelularJpaController() {
        this.emf = Persistence.createEntityManagerFactory("com.mycompany_Adicional_P_Celular_jar_1.0-SNAPSHOTPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Celular celular) {
        if (celular.getRecargas() == null) {
            celular.setRecargas(new ArrayList<Recargas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente = celular.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getIdClie());
                celular.setCliente(cliente);
            }
            List<Recargas> attachedRecargas = new ArrayList<Recargas>();
            for (Recargas recargasRecargasToAttach : celular.getRecargas()) {
                recargasRecargasToAttach = em.getReference(recargasRecargasToAttach.getClass(), recargasRecargasToAttach.getIdReca());
                attachedRecargas.add(recargasRecargasToAttach);
            }
            celular.setRecargas(attachedRecargas);
            em.persist(celular);
            if (cliente != null) {
                cliente.getCelular().add(celular);
                cliente = em.merge(cliente);
            }
            for (Recargas recargasRecargas : celular.getRecargas()) {
                Celular oldCelularOfRecargasRecargas = recargasRecargas.getCelular();
                recargasRecargas.setCelular(celular);
                recargasRecargas = em.merge(recargasRecargas);
                if (oldCelularOfRecargasRecargas != null) {
                    oldCelularOfRecargasRecargas.getRecargas().remove(recargasRecargas);
                    oldCelularOfRecargasRecargas = em.merge(oldCelularOfRecargasRecargas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Celular celular) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Celular persistentCelular = em.find(Celular.class, celular.getIdCel());
            Cliente clienteOld = persistentCelular.getCliente();
            Cliente clienteNew = celular.getCliente();
            List<Recargas> recargasOld = persistentCelular.getRecargas();
            List<Recargas> recargasNew = celular.getRecargas();
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getIdClie());
                celular.setCliente(clienteNew);
            }
            List<Recargas> attachedRecargasNew = new ArrayList<Recargas>();
            for (Recargas recargasNewRecargasToAttach : recargasNew) {
                recargasNewRecargasToAttach = em.getReference(recargasNewRecargasToAttach.getClass(), recargasNewRecargasToAttach.getIdReca());
                attachedRecargasNew.add(recargasNewRecargasToAttach);
            }
            recargasNew = attachedRecargasNew;
            celular.setRecargas(recargasNew);
            celular = em.merge(celular);
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getCelular().remove(celular);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getCelular().add(celular);
                clienteNew = em.merge(clienteNew);
            }
            for (Recargas recargasOldRecargas : recargasOld) {
                if (!recargasNew.contains(recargasOldRecargas)) {
                    recargasOldRecargas.setCelular(null);
                    recargasOldRecargas = em.merge(recargasOldRecargas);
                }
            }
            for (Recargas recargasNewRecargas : recargasNew) {
                if (!recargasOld.contains(recargasNewRecargas)) {
                    Celular oldCelularOfRecargasNewRecargas = recargasNewRecargas.getCelular();
                    recargasNewRecargas.setCelular(celular);
                    recargasNewRecargas = em.merge(recargasNewRecargas);
                    if (oldCelularOfRecargasNewRecargas != null && !oldCelularOfRecargasNewRecargas.equals(celular)) {
                        oldCelularOfRecargasNewRecargas.getRecargas().remove(recargasNewRecargas);
                        oldCelularOfRecargasNewRecargas = em.merge(oldCelularOfRecargasNewRecargas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = celular.getIdCel();
                if (findCelular(id) == null) {
                    throw new NonexistentEntityException("The celular with id " + id + " no longer exists.");
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
            Celular celular;
            try {
                celular = em.getReference(Celular.class, id);
                celular.getIdCel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The celular with id " + id + " no longer exists.", enfe);
            }
            Cliente cliente = celular.getCliente();
            if (cliente != null) {
                cliente.getCelular().remove(celular);
                cliente = em.merge(cliente);
            }
            List<Recargas> recargas = celular.getRecargas();
            for (Recargas recargasRecargas : recargas) {
                recargasRecargas.setCelular(null);
                recargasRecargas = em.merge(recargasRecargas);
            }
            em.remove(celular);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Celular> findCelularEntities() {
        return findCelularEntities(true, -1, -1);
    }

    public List<Celular> findCelularEntities(int maxResults, int firstResult) {
        return findCelularEntities(false, maxResults, firstResult);
    }

    private List<Celular> findCelularEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Celular.class));
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

    public Celular findCelular(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Celular.class, id);
        } finally {
            em.close();
        }
    }

    public int getCelularCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Celular> rt = cq.from(Celular.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Celular findByCedula(String cedula) {
    EntityManager em = getEntityManager();
    try {
        return em.createQuery(
            "SELECT c FROM Celular c WHERE c.cliente.Cedula = :cedula", Celular.class)
            .setParameter("cedula", cedula)
            .getSingleResult();
    } catch (NoResultException e) {
        return null; // No se encontró
    } finally {
        em.close();
    }
}
    
}
