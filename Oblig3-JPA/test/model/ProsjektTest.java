package model;

import com.github.javafaker.Faker;
import dao.AnsattDao;
import dao.ProsjektDao;
import dao.ProsjektdeltagelseDao;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ProsjektTest {


    private final AnsattDao ansattDao;
    private final ProsjektDao prosjektDao;
    private final ProsjektdeltagelseDao deltagelseDao;
    private final Random random;
    private final Faker faker;


    public ProsjektTest() {
        ansattDao = new AnsattDao();
        prosjektDao = new ProsjektDao();
        deltagelseDao = new ProsjektdeltagelseDao();
        random = new Random();
        faker = new Faker(new Locale("nb-NO"));
    }

    @Test
    void leggTilogFjernProsjekt() {
        Prosjekt prosjekt = new Prosjekt("Testnavn", "Testbeskrivelse");
        System.out.println("Legger til testprosjektet " + prosjekt);
        int id = prosjektDao.leggTil(prosjekt);

        System.out.println("id = " + id);
        assertNotEquals(-1, id);

        //sjekker om testprosjektet finnes i databasen(litt hardkoding her, då testprosjektet får id = 0, men 3 i databasen)
        //assertEquals(prosjektDao.finn(3).getId(), 3);
        assertNotNull(prosjektDao.finn(id));


        //Fjerner testprosjektet fra databasen
        assertTrue(prosjektDao.fjern(id));
        assertNull(prosjektDao.finn(id));
    }


    @Test
    void hentAlleProsjekt() {
        System.out.println("Henter alle prosjekter: ");
        List<Prosjekt> prosjekter = prosjektDao.hentAlle();

        assertNotNull(prosjekter);
        assertTrue(prosjekter.size() > 1);

        for (Prosjekt prosjekt : prosjekter) {
            System.out.println(prosjekt);
        }
    }

    @Test
    void hentAlleDeltagelser() {
        System.out.println("Henter alle prosjektdeltagelser: ");
        List<Prosjektdeltagelse> deltagelser = deltagelseDao.hentAlle();

        assertNotNull(deltagelser);
        assertTrue(deltagelser.size() > 1);

        for (var deltagelse : deltagelser) {
            System.out.println(deltagelse);
        }
    }

    @Test
    void leggTilDeltagelse() {
        List<Prosjekt> prosjekter = prosjektDao.hentAlle();
        assertNotNull(prosjekter);
        assertTrue(prosjekter.size() > 1);
        int index = random.nextInt(prosjekter.size());
        Prosjekt tilfeldigProsjekt = prosjekter.get(index);
        System.out.println("Prosjekt:");
        System.out.println(tilfeldigProsjekt);

        // finne ansatt som ikke allerede er med i prosjektet
        List<Ansatt> ansatte = ansattDao.hentAlle();
        Ansatt ansatt = null;
        for (Ansatt a : ansatte) {
            if (deltagelseDao.finn(a, tilfeldigProsjekt) == null) {
                ansatt = a;
                break;
            }
        }

        assertNotNull(ansatt);
        System.out.println("\nValg ansatt: ");
        System.out.println(ansatt);
        for (var deltagelse : ansatt.getDeltagelser()) {
            System.out.println(deltagelse);
        }

        Prosjektdeltagelse prosjektdeltagelse = new Prosjektdeltagelse(ansatt, tilfeldigProsjekt, faker.name().title(), (random.nextInt(50) + 1));
        int deltagelseId = deltagelseDao.leggTil(prosjektdeltagelse);
        assertNotEquals(-1, deltagelseId);
        assertNotEquals(0, deltagelseId);
        prosjektdeltagelse = deltagelseDao.finn(deltagelseId);
        assertNotNull(prosjektdeltagelse);
        System.out.println("\nLa til prosjektdeltagelse:");
        System.out.println(prosjektdeltagelse);
        System.out.println("\nAnsatt etter:");
        ansatt = ansattDao.finn(ansatt.getId());
        System.out.println(ansatt);
        for (var deltagelse : ansatt.getDeltagelser()) {
            System.out.println(deltagelse);
        }
    }


    @Test
    void equalsTest() {
        Ansatt ansatt = ansattDao.finn(5);
        Prosjekt prosjekt = prosjektDao.finn(2);
        var deltagere = prosjekt.getDeltagere();
        for (var deltagelse : ansatt.getDeltagelser()) {
            if (deltagere.contains(deltagelse)) {
                System.out.println(deltagelse);
            }
        }
    }

    @Test
    void fakerRolleTimer() {
        // rolle
        System.out.println(faker.name().title());
    }


}
