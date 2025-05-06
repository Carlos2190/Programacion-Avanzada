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
    import Clases.Cliente;
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
    public class ClienteJpaController implements Serializable {

        public ClienteJpaController() {
            this.emf = Persistence.createEntityManagerFactory("com.mycompany_Adicional_P_Celular_jar_1.0-SNAPSHOTPU");

        }
        private EntityManagerFactory emf = null;

        public EntityManager getEntityManager() {
            return emf.createEntityManager();
        }

        public void create(Cliente cliente) {
            if (cliente.getCelular() == null) {
                cliente.setCelular(new ArrayList<Celular>());
            }
            EntityManager em = null;
            try {
                em = getEntityManager();
                em.getTransaction().begin();

                // Adjuntar celulares si tienen ID distinto de 0
                List<Celular> attachedCelulares = new ArrayList<Celular>();
                for (Celular celularToAttach : cliente.getCelular()) {
                    if (celularToAttach.getIdCel() != 0) {
                        celularToAttach = em.getReference(celularToAttach.getClass(), celularToAttach.getIdCel());
                    }
                    attachedCelulares.add(celularToAttach);
                }
                cliente.setCelular(attachedCelulares);

                // Persistir cliente
                em.persist(cliente);

                // Asignar cliente a los celulares
                for (Celular celular : cliente.getCelular()) {
                    Cliente oldCliente = celular.getCliente();
                    celular.setCliente(cliente);
                    em.merge(celular);
                    if (oldCliente != null && !oldCliente.equals(cliente)) {
                        oldCliente.getCelular().remove(celular);
                        em.merge(oldCliente);
                    }
                }

                em.getTransaction().commit();
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }

        public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
            EntityManager em = null;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                Cliente persistentCliente = em.find(Cliente.class, cliente.getIdClie());
                List<Celular> celularOld = persistentCliente.getCelular();
                List<Celular> celularNew = cliente.getCelular();
                List<Celular> attachedCelularNew = new ArrayList<Celular>();
                for (Celular celularNewCelularToAttach : celularNew) {
                    celularNewCelularToAttach = em.getReference(celularNewCelularToAttach.getClass(), celularNewCelularToAttach.getIdCel());
                    attachedCelularNew.add(celularNewCelularToAttach);
                }
                celularNew = attachedCelularNew;
                cliente.setCelular(celularNew);
                cliente = em.merge(cliente);
                for (Celular celularOldCelular : celularOld) {
                    if (!celularNew.contains(celularOldCelular)) {
                        celularOldCelular.setCliente(null);
                        celularOldCelular = em.merge(celularOldCelular);
                    }
                }
                for (Celular celularNewCelular : celularNew) {
                    if (!celularOld.contains(celularNewCelular)) {
                        Cliente oldClienteOfCelularNewCelular = celularNewCelular.getCliente();
                        celularNewCelular.setCliente(cliente);
                        celularNewCelular = em.merge(celularNewCelular);
                        if (oldClienteOfCelularNewCelular != null && !oldClienteOfCelularNewCelular.equals(cliente)) {
                            oldClienteOfCelularNewCelular.getCelular().remove(celularNewCelular);
                            oldClienteOfCelularNewCelular = em.merge(oldClienteOfCelularNewCelular);
                        }
                    }
                }
                em.getTransaction().commit();
            } catch (Exception ex) {
                String msg = ex.getLocalizedMessage();
                if (msg == null || msg.length() == 0) {
                    int id = cliente.getIdClie();
                    if (findCliente(id) == null) {
                        throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
                Cliente cliente;
                try {
                    cliente = em.getReference(Cliente.class, id);
                    cliente.getIdClie();
                } catch (EntityNotFoundException enfe) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
                }
                List<Celular> celular = cliente.getCelular();
                for (Celular celularCelular : celular) {
                    celularCelular.setCliente(null);
                    celularCelular = em.merge(celularCelular);
                }
                em.remove(cliente);
                em.getTransaction().commit();
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }

        public List<Cliente> findClienteEntities() {
            return findClienteEntities(true, -1, -1);
        }

        public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
            return findClienteEntities(false, maxResults, firstResult);
        }

        private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
            EntityManager em = getEntityManager();
            try {
                CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
                cq.select(cq.from(Cliente.class));
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

        public Cliente findCliente(int id) {
            EntityManager em = getEntityManager();
            try {
                return em.find(Cliente.class, id);
            } finally {
                em.close();
            }
        }

        public int getClienteCount() {
            EntityManager em = getEntityManager();
            try {
                CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
                Root<Cliente> rt = cq.from(Cliente.class);
                cq.select(em.getCriteriaBuilder().count(rt));
                Query q = em.createQuery(cq);
                return ((Long) q.getSingleResult()).intValue();
            } finally {
                em.close();
            }
        }

    }
