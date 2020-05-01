package dao;

import model.Ansatt;
import model.Avdeling;

import javax.management.InvalidAttributeValueException;
import javax.management.OperationsException;
import javax.persistence.*;
import java.util.List;

public class AnsattDao implements Dao<Ansatt> {

    private final EntityManagerFactory emf;

    private static final int BRUKERNAVN_MAX_LENGDE = 10;

    public AnsattDao() {
        emf = Persistence.createEntityManagerFactory("oblig3_1");
    }

    public Ansatt finn(int id) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Ansatt.class, id);
        } finally {
            em.close();
        }
    }

    public List<Ansatt> finn(String delstrengNavn) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Ansatt> query = em.createQuery("select a from Ansatt a where " +
                    "lower(concat(a.fornavn, ' ', a.etternavn)) like lower('%" + delstrengNavn + "%')", Ansatt.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Ansatt finnMedBrukernavn(String brukernavn) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Ansatt> query = em.createQuery("select a from Ansatt a where lower(a.brukernavn) like lower(:brukernavn)",
                    Ansatt.class);
            query.setParameter("brukernavn", brukernavn);
            if (query.getResultList().size() == 0) {
                return null;
            } else {
                return query.getSingleResult();

            }
        } finally {
            em.close();
        }
    }

    public int leggTil(Ansatt ansatt) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            if (finnMedBrukernavn(ansatt.getBrukernavn()) != null) { // debug
                throw new InvalidAttributeValueException("Brukernavn eksisterer fra før.");
            }

            if (ansatt.getBrukernavn().length() >= BRUKERNAVN_MAX_LENGDE) {
                throw new InvalidAttributeValueException(String.format("Brukernavn er for langt (%s).", BRUKERNAVN_MAX_LENGDE));
            }

            tx.begin();

            Avdeling avdeling = em.merge(ansatt.getAvdeling());
            avdeling.getAnsatte().add(ansatt);

            em.persist(ansatt);
            tx.commit();
            return ansatt.getId();

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


    public boolean fjern(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class, id);
            if (ansatt.getDeltagelser().size() > 0) {
                throw new OperationsException("Prøver å fjerne ansatt som har deltagelse i prosjekter.");
            }

            ansatt.getAvdeling().getAnsatte().remove(ansatt); // managed avdeling
            em.remove(ansatt);
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
    public int lagre(Ansatt ansatt) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(ansatt);
            tx.commit();
            return ansatt.getId();
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
    public List<Ansatt> hentAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("SELECT a FROM Ansatt a", Ansatt.class).getResultList();
        } finally {
            em.close();
        }
    }
}
