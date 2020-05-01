package dao;

import model.Ansatt;
import model.Avdeling;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class AvdelingDao implements Dao<Avdeling> {

    private final EntityManagerFactory emf;


    public AvdelingDao() {
        emf = Persistence.createEntityManagerFactory("oblig3_1");
    }

    public Avdeling finn(int id) {

        EntityManager em = emf.createEntityManager();

        Avdeling avdeling;
        try {
            avdeling = em.find(Avdeling.class, id);
        } finally {
            em.close();
        }

        return avdeling;
    }

    @Override
    public int leggTil(Avdeling avdeling) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {

            tx.begin();

            Ansatt sjef = em.merge(avdeling.getSjef());
            sjef.getAvdeling().getAnsatte().remove(sjef);
            sjef.setAvdeling(avdeling);
            em.persist(avdeling);

            tx.commit();

            return avdeling.getId();

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
    public boolean fjern(int id) {
        throw new UnsupportedOperationException("Prøver å fjerne en avdeling.");
    }

    @Override
    public int lagre(Avdeling avdeling) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(avdeling);
            tx.commit();
            return avdeling.getId();
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
    public List<Avdeling> hentAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT a FROM Avdeling a", Avdeling.class).getResultList();
        } finally {
            em.close();
        }
    }
}
