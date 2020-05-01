package dao;

import model.Ansatt;
import model.Prosjekt;
import model.Prosjektdeltagelse;

import javax.persistence.*;
import java.util.List;

public class ProsjektdeltagelseDao implements Dao<Prosjektdeltagelse> {

    private final EntityManagerFactory emf;

    public ProsjektdeltagelseDao() {
        emf = Persistence.createEntityManagerFactory("oblig3_1");
    }

    @Override
    public int leggTil(Prosjektdeltagelse prosjektdeltagelse) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {

            tx.begin();

            Ansatt deltager = em.merge(prosjektdeltagelse.getDeltager());
            deltager.getDeltagelser().add(prosjektdeltagelse);

            Prosjekt prosjekt = em.merge(prosjektdeltagelse.getProsjekt());
            prosjekt.getDeltagere().add(prosjektdeltagelse);

            em.persist(prosjektdeltagelse);

            tx.commit();

            return prosjektdeltagelse.getId();

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
    public Prosjektdeltagelse finn(int id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Prosjektdeltagelse.class, id);
        } finally {
            em.close();
        }
    }


    public Prosjektdeltagelse finn(Ansatt ansatt, Prosjekt prosjekt) {
        EntityManager em = emf.createEntityManager();

        try {

            TypedQuery<Prosjektdeltagelse> query = em.createQuery(
                    "SELECT p FROM Prosjektdeltagelse p " +
                            "where p.deltager.id = :ansattId and p.prosjekt.id = :prosjektId", Prosjektdeltagelse.class
            );

            query.setParameter("ansattId", ansatt.getId());
            query.setParameter("prosjektId", prosjekt.getId());

            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;

        } finally {
            em.close();
        }

    }

    @Override
    public boolean fjern(int id) {
        throw new UnsupportedOperationException("Prøver å fjerne en prosjektdeltagelse.");
    }

    @Override
    public int lagre(Prosjektdeltagelse prosjektdeltagelse) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(prosjektdeltagelse);
            tx.commit();
            return prosjektdeltagelse.getId();
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
    public List<Prosjektdeltagelse> hentAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT p FROM Prosjektdeltagelse p", Prosjektdeltagelse.class).getResultList();
        } finally {
            em.close();
        }
    }
}
