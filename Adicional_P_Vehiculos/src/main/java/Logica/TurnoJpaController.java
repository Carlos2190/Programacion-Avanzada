/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Clases.Turno;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Vehiculo;
import Logica.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Carlos Espinoza
 */
public class TurnoJpaController implements Serializable {

    public TurnoJpaController() {
       this.emf = Persistence.createEntityManagerFactory("com.mycompany_Adicional_P_Vehiculos_jar_1.0-SNAPSHOTPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Turno turno) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vehiculo vehiculo = turno.getVehiculo();
            if (vehiculo != null) {
                vehiculo = em.getReference(vehiculo.getClass(), vehiculo.getId());
                turno.setVehiculo(vehiculo);
            }
            em.persist(turno);
            if (vehiculo != null) {
                vehiculo.getTurnos().add(turno);
                vehiculo = em.merge(vehiculo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Turno turno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turno persistentTurno = em.find(Turno.class, turno.getId());
            Vehiculo vehiculoOld = persistentTurno.getVehiculo();
            Vehiculo vehiculoNew = turno.getVehiculo();
            if (vehiculoNew != null) {
                vehiculoNew = em.getReference(vehiculoNew.getClass(), vehiculoNew.getId());
                turno.setVehiculo(vehiculoNew);
            }
            turno = em.merge(turno);
            if (vehiculoOld != null && !vehiculoOld.equals(vehiculoNew)) {
                vehiculoOld.getTurnos().remove(turno);
                vehiculoOld = em.merge(vehiculoOld);
            }
            if (vehiculoNew != null && !vehiculoNew.equals(vehiculoOld)) {
                vehiculoNew.getTurnos().add(turno);
                vehiculoNew = em.merge(vehiculoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = turno.getId();
                if (findTurno(id) == null) {
                    throw new NonexistentEntityException("The turno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turno turno;
            try {
                turno = em.getReference(Turno.class, id);
                turno.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The turno with id " + id + " no longer exists.", enfe);
            }
            Vehiculo vehiculo = turno.getVehiculo();
            if (vehiculo != null) {
                vehiculo.getTurnos().remove(turno);
                vehiculo = em.merge(vehiculo);
            }
            em.remove(turno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Turno> findTurnoEntities() {
        return findTurnoEntities(true, -1, -1);
    }

    public List<Turno> findTurnoEntities(int maxResults, int firstResult) {
        return findTurnoEntities(false, maxResults, firstResult);
    }

    private List<Turno> findTurnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Turno.class));
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

    public Turno findTurno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Turno.class, id);
        } finally {
            em.close();
        }
    }

    public int getTurnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Turno> rt = cq.from(Turno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    // Buscar turnos por vehículo y día
    public List<Turno> findByVehiculoAndDia(Vehiculo vehiculo, int dia) {
        try {
            EntityManager em = getEntityManager();
            TypedQuery<Turno> query = em.createQuery(
                "SELECT t FROM Turno t WHERE t.vehiculo = :vehiculo AND t.dia = :dia", Turno.class);
            query.setParameter("vehiculo", vehiculo);
            query.setParameter("dia", dia);
            return query.getResultList();
        } finally {
            // Cerrar EntityManager si usas uno diferente
        }
    }

    // Buscar turnos por vehículo y anden
    public List<Turno> findByVehiculoAndAnden(Vehiculo vehiculo, int anden) {
        try {
            EntityManager em = getEntityManager();
            TypedQuery<Turno> query = em.createQuery(
                "SELECT t FROM Turno t WHERE t.vehiculo = :vehiculo AND t.anden = :anden", Turno.class);
            query.setParameter("vehiculo", vehiculo);
            query.setParameter("anden", anden);
            return query.getResultList();
        } finally {
            // Cerrar EntityManager si usas uno diferente
        }
    }
    
}
