/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Clases.Propietario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class PropietarioJpaController implements Serializable {

    public PropietarioJpaController() {
        this.emf = Persistence.createEntityManagerFactory("com.mycompany_Adicional_P_Vehiculos_jar_1.0-SNAPSHOTPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

public void create(Propietario propietario) {
    if (propietario.getVehiculo() == null) {
        propietario.setVehiculo(new ArrayList<Vehiculo>());
    }
    EntityManager em = null;
    try {
        em = getEntityManager();
        em.getTransaction().begin();

        List<Vehiculo> attachedVehiculos = new ArrayList<Vehiculo>();
        for (Vehiculo vehiculoToAttach : propietario.getVehiculo()) {
            // Solo hacer referencia si el vehículo ya tiene ID
            if (vehiculoToAttach.getId() != null) {
                vehiculoToAttach = em.getReference(vehiculoToAttach.getClass(), vehiculoToAttach.getId());
            }
            attachedVehiculos.add(vehiculoToAttach);
        }
        propietario.setVehiculo(attachedVehiculos);

        // Persistir propietario
        em.persist(propietario);
        
        // Asignar propietario a los vehículos nuevos (si tienen ID, ya están referenciados)
        for (Vehiculo vehiculo : propietario.getVehiculo()) {
            Propietario oldPropietario = vehiculo.getPropietario();
            vehiculo.setPropietario(propietario);
            em.merge(vehiculo);

            // Si el vehículo tenía otro propietario, eliminar de su lista
            if (oldPropietario != null && !oldPropietario.equals(propietario)) {
                oldPropietario.getVehiculo().remove(vehiculo);
                em.merge(oldPropietario);
            }
        }

        em.getTransaction().commit();
    } finally {
        if (em != null) {
            em.close();
        }
    }
}

    public void edit(Propietario propietario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Propietario persistentPropietario = em.find(Propietario.class, propietario.getIdprop());
            List<Vehiculo> vehiculosOld = persistentPropietario.getVehiculo();
            List<Vehiculo> vehiculosNew = propietario.getVehiculo();
            List<Vehiculo> attachedVehiculosNew = new ArrayList<Vehiculo>();
            for (Vehiculo vehiculosNewVehiculoToAttach : vehiculosNew) {
                vehiculosNewVehiculoToAttach = em.getReference(vehiculosNewVehiculoToAttach.getClass(), vehiculosNewVehiculoToAttach.getId());
                attachedVehiculosNew.add(vehiculosNewVehiculoToAttach);
            }
            vehiculosNew = attachedVehiculosNew;
            propietario.setVehiculo(vehiculosNew);
            propietario = em.merge(propietario);
            for (Vehiculo vehiculosOldVehiculo : vehiculosOld) {
                if (!vehiculosNew.contains(vehiculosOldVehiculo)) {
                    vehiculosOldVehiculo.setPropietario(null);
                    vehiculosOldVehiculo = em.merge(vehiculosOldVehiculo);
                }
            }
            for (Vehiculo vehiculosNewVehiculo : vehiculosNew) {
                if (!vehiculosOld.contains(vehiculosNewVehiculo)) {
                    Propietario oldPropietarioOfVehiculosNewVehiculo = vehiculosNewVehiculo.getPropietario();
                    vehiculosNewVehiculo.setPropietario(propietario);
                    vehiculosNewVehiculo = em.merge(vehiculosNewVehiculo);
                    if (oldPropietarioOfVehiculosNewVehiculo != null && !oldPropietarioOfVehiculosNewVehiculo.equals(propietario)) {
                        oldPropietarioOfVehiculosNewVehiculo.getVehiculo().remove(vehiculosNewVehiculo);
                        oldPropietarioOfVehiculosNewVehiculo = em.merge(oldPropietarioOfVehiculosNewVehiculo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = propietario.getIdprop();
                if (findPropietario(id) == null) {
                    throw new NonexistentEntityException("The propietario with id " + id + " no longer exists.");
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
            Propietario propietario;
            try {
                propietario = em.getReference(Propietario.class, id);
                propietario.getIdprop();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The propietario with id " + id + " no longer exists.", enfe);
            }
            List<Vehiculo> vehiculos = propietario.getVehiculo();
            for (Vehiculo vehiculosVehiculo : vehiculos) {
                vehiculosVehiculo.setPropietario(null);
                vehiculosVehiculo = em.merge(vehiculosVehiculo);
            }
            em.remove(propietario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Propietario> findPropietarioEntities() {
        return findPropietarioEntities(true, -1, -1);
    }

    public List<Propietario> findPropietarioEntities(int maxResults, int firstResult) {
        return findPropietarioEntities(false, maxResults, firstResult);
    }

    private List<Propietario> findPropietarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Propietario.class));
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

    public Propietario findPropietario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Propietario.class, id);
        } finally {
            em.close();
        }
    }

    public int getPropietarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Propietario> rt = cq.from(Propietario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
