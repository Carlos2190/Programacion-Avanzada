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
import Clases.Propietario;
import Clases.Turno;
import Clases.Vehiculo;
import Logica.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Carlos Espinoza
 */
public class VehiculoJpaController implements Serializable {

    public VehiculoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("com.mycompany_Adicional_P_Vehiculos_jar_1.0-SNAPSHOTPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vehiculo vehiculo) {
        if (vehiculo.getTurnos() == null) {
            vehiculo.setTurnos(new ArrayList<Turno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Propietario propietario = vehiculo.getPropietario();
            if (propietario != null) {
                propietario = em.getReference(propietario.getClass(), propietario.getIdprop());
                vehiculo.setPropietario(propietario);
            }
            List<Turno> attachedTurnos = new ArrayList<Turno>();
            for (Turno turnosTurnoToAttach : vehiculo.getTurnos()) {
                turnosTurnoToAttach = em.getReference(turnosTurnoToAttach.getClass(), turnosTurnoToAttach.getId());
                attachedTurnos.add(turnosTurnoToAttach);
            }
            vehiculo.setTurnos(attachedTurnos);
            em.persist(vehiculo);
            if (propietario != null) {
                propietario.getVehiculo().add(vehiculo);
                propietario = em.merge(propietario);
            }
            for (Turno turnosTurno : vehiculo.getTurnos()) {
                Vehiculo oldVehiculoOfTurnosTurno = turnosTurno.getVehiculo();
                turnosTurno.setVehiculo(vehiculo);
                turnosTurno = em.merge(turnosTurno);
                if (oldVehiculoOfTurnosTurno != null) {
                    oldVehiculoOfTurnosTurno.getTurnos().remove(turnosTurno);
                    oldVehiculoOfTurnosTurno = em.merge(oldVehiculoOfTurnosTurno);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vehiculo vehiculo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vehiculo persistentVehiculo = em.find(Vehiculo.class, vehiculo.getId());
            Propietario propietarioOld = persistentVehiculo.getPropietario();
            Propietario propietarioNew = vehiculo.getPropietario();
            List<Turno> turnosOld = persistentVehiculo.getTurnos();
            List<Turno> turnosNew = vehiculo.getTurnos();
            if (propietarioNew != null) {
                propietarioNew = em.getReference(propietarioNew.getClass(), propietarioNew.getIdprop());
                vehiculo.setPropietario(propietarioNew);
            }
            List<Turno> attachedTurnosNew = new ArrayList<Turno>();
            for (Turno turnosNewTurnoToAttach : turnosNew) {
                turnosNewTurnoToAttach = em.getReference(turnosNewTurnoToAttach.getClass(), turnosNewTurnoToAttach.getId());
                attachedTurnosNew.add(turnosNewTurnoToAttach);
            }
            turnosNew = attachedTurnosNew;
            vehiculo.setTurnos(turnosNew);
            vehiculo = em.merge(vehiculo);
            if (propietarioOld != null && !propietarioOld.equals(propietarioNew)) {
                propietarioOld.getVehiculo().remove(vehiculo);
                propietarioOld = em.merge(propietarioOld);
            }
            if (propietarioNew != null && !propietarioNew.equals(propietarioOld)) {
                propietarioNew.getVehiculo().add(vehiculo);
                propietarioNew = em.merge(propietarioNew);
            }
            for (Turno turnosOldTurno : turnosOld) {
                if (!turnosNew.contains(turnosOldTurno)) {
                    turnosOldTurno.setVehiculo(null);
                    turnosOldTurno = em.merge(turnosOldTurno);
                }
            }
            for (Turno turnosNewTurno : turnosNew) {
                if (!turnosOld.contains(turnosNewTurno)) {
                    Vehiculo oldVehiculoOfTurnosNewTurno = turnosNewTurno.getVehiculo();
                    turnosNewTurno.setVehiculo(vehiculo);
                    turnosNewTurno = em.merge(turnosNewTurno);
                    if (oldVehiculoOfTurnosNewTurno != null && !oldVehiculoOfTurnosNewTurno.equals(vehiculo)) {
                        oldVehiculoOfTurnosNewTurno.getTurnos().remove(turnosNewTurno);
                        oldVehiculoOfTurnosNewTurno = em.merge(oldVehiculoOfTurnosNewTurno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vehiculo.getId();
                if (findVehiculo(id) == null) {
                    throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.");
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
            Vehiculo vehiculo;
            try {
                vehiculo = em.getReference(Vehiculo.class, id);
                vehiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.", enfe);
            }
            Propietario propietario = vehiculo.getPropietario();
            if (propietario != null) {
                propietario.getVehiculo().remove(vehiculo);
                propietario = em.merge(propietario);
            }
            List<Turno> turnos = vehiculo.getTurnos();
            for (Turno turnosTurno : turnos) {
                turnosTurno.setVehiculo(null);
                turnosTurno = em.merge(turnosTurno);
            }
            em.remove(vehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vehiculo> findVehiculoEntities() {
        return findVehiculoEntities(true, -1, -1);
    }

    public List<Vehiculo> findVehiculoEntities(int maxResults, int firstResult) {
        return findVehiculoEntities(false, maxResults, firstResult);
    }

    private List<Vehiculo> findVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vehiculo.class));
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

    public Vehiculo findVehiculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vehiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVehiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vehiculo> rt = cq.from(Vehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Vehiculo findByPlaca(String placa) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT v FROM Vehiculo v WHERE v.placa = :placa");
            query.setParameter("placa", placa);
            List<Vehiculo> results = query.getResultList(); // Use getResultList instead of getSingleResult

            if (results != null && !results.isEmpty()) {
                return results.get(0); // Return the first vehicle found (assuming placa is unique)
            } else {
                return null; // No vehicle found with that license plate
            }
        } finally {
            em.close();
        }
    }
    
}
