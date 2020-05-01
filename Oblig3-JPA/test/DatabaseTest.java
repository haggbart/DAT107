import model.Ansatt;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class DatabaseTest {

    @Test
    void dataTest() {
        String jpql = "SELECT a FROM Ansatt a";

        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("oblig3_1");

        EntityManager em = emf.createEntityManager();

        System.out.println("Kobler til database...");

        try {
            TypedQuery<Ansatt> query = em.createQuery(jpql, Ansatt.class);
            List<Ansatt> ansatte = query.getResultList();

            for (Ansatt ansatt: ansatte) {
                System.out.printf("Id: %d, Navn: %s %s%s\n",
                        ansatt.getId(), ansatt.getFornavn(), ansatt.getEtternavn(),
                        (ansatt.erSjef() ? " (sjef for " + ansatt.getAvdeling().getNavn() + ")" : ""));
            }
        } finally {
            em.close();
        }

        System.out.println("Ferdig!");
    }
}
