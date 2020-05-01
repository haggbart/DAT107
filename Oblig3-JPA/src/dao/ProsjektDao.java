package dao;

import model.Prosjekt;

import javax.management.OperationsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class ProsjektDao implements Dao<Prosjekt> {

    private final EntityManagerFactory emf;

    public ProsjektDao() {
        emf = Persistence.createEntityManagerFactory("oblig3_1");
    }

    @Override
    public int leggTil(Prosjekt prosjekt) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.persist(prosjekt);

            tx.commit();
            return prosjekt.getId();

        } catch (Throwable e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return -1;
        } finally {
            em.close();
        }

    }

    @Override
    public Prosjekt finn(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Prosjekt prosjekt = em.find(Prosjekt.class, id);
            tx.commit();
            return prosjekt;
        } catch (Throwable e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
        return null;
    }

    @Override
    public boolean fjern(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {

            tx.begin();
            Prosjekt prosjekt = em.find(Prosjekt.class, id);

            if (prosjekt.getDeltagere().size() > 0) {
                throw new OperationsException("Prøver å fjerne prosjekt som har deltagelse.");
            }

            em.remove(prosjekt);
            tx.commit();
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
        return false;
    }

    @Override
    public int lagre(Prosjekt prosjekt) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(prosjekt);
            tx.commit();
            return prosjekt.getId();
        } catch (Throwable e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return -1;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Prosjekt> hentAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT a FROM Prosjekt a", Prosjekt.class).getResultList();
        } finally {
            em.close();
        }
    }
}
