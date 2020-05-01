package model;

import com.github.javafaker.Faker;
import dao.AnsattDao;
import dao.AvdelingDao;
import model.Ansatt;
import model.Avdeling;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AnsattTest {

    private final AnsattDao ansattDao;
    private final AvdelingDao avdelingDao;
    private final Faker faker;

    public AnsattTest() {
        ansattDao = new AnsattDao();
        avdelingDao = new AvdelingDao();
        faker = new Faker(new Locale("nb-NO"));
    }

    @Test
    void genererAnsatte() {
        var avdelinger = avdelingDao.hentAlle();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(avdelinger.size());
            Ansatt ansatt = new Ansatt(faker.name().firstName(), faker.name().lastName(), avdelinger.get(index));

            ansatt.setStilling(faker.company().profession());
            ansatt.setManedslonn(new BigDecimal(faker.random().nextInt(15000, 60000)));

            int id = ansattDao.leggTil(ansatt);
            assertNotEquals(-1, id);
            System.out.println("La til ansatt:");
            System.out.println(ansattDao.finn(id));
        }
    }

    @Test
    void finnAnsattMedId() {
        Ansatt ansatt = ansattDao.finn(2);
        assertEquals("Arvid", ansatt.getFornavn());
        System.out.println(ansatt);
    }

    @Test
    void endreLonn() {
        System.out.println("Endre lønn for Ansatt med id 2 til 50000");
        BigDecimal nyLonn = new BigDecimal("50000.00");
        Ansatt ansatt = ansattDao.finn(2);
        System.out.println(ansatt);
        System.out.println("Før: " + ansatt.getManedslonn());
        ansatt.setManedslonn(new BigDecimal("50000.00"));
        ansattDao.lagre(ansatt);
        assertEquals(nyLonn, ansatt.getManedslonn());
        var lonn = ansattDao.finn(2).getManedslonn();
        System.out.println("Etter: " + lonn);
        assertEquals(nyLonn,lonn);
        // reset lønn til gammel
        ansatt.setManedslonn(new BigDecimal(faker.random().nextInt(15000, 60000)));
        ansattDao.lagre(ansatt);
    }

    @Test
    void endreStilling() {
        System.out.println("Endre stilling for Ansatt med id 2 til BigBoss");
        String nyStilling = "BigBoss";
        Ansatt ansatt = ansattDao.finn(2);
        String gammelStilling = ansatt.getStilling();
        System.out.println(ansatt);
        System.out.println("Tidligere stilling " + ansatt.getStilling());
        ansatt.setStilling(nyStilling);
        ansattDao.lagre(ansatt);
        assertEquals(nyStilling, ansatt.getStilling());
        var stilling = ansattDao.finn(2).getStilling();
        assertEquals(nyStilling, stilling);

        //reset stilling til gammel
        ansatt.setStilling(gammelStilling);
        ansattDao.lagre(ansatt);
    }
    @Test
    void finnAnsattMedDelStreng() {
        System.out.println("Søk på \"marius\":");
        List<Ansatt> ansatte = ansattDao.finn("marius");
        assertEquals("Marius", ansatte.get(0).getFornavn());
        for (Ansatt ansatt : ansatte) {
            System.out.println(ansatt);
        }
    }

    @Test
    void leggTilAnsatt() {
        System.out.println("Legg til ansatt \"Bjørnar\" i avdeling 3:");
        Avdeling avdeling = avdelingDao.finn(3);
        Ansatt ansatt = new Ansatt("Bjørnar", "Henriksen", avdeling);
        ansattDao.leggTil(ansatt);
        System.out.println(ansatt);
        ansattDao.fjern(ansattDao.finnMedBrukernavn(ansatt.getBrukernavn()).getId());
        System.out.println("Slettet");
    }

    @Test
    void slettAnsatt() {
        Avdeling avdeling = avdelingDao.finn(1);
        Ansatt ansatt;
        System.out.println(avdeling);
        if (ansattDao.finnMedBrukernavn("tbj") == null) {
            ansatt = new Ansatt("Tor", "Bjelland", avdeling);
            assertNotEquals(-1, ansattDao.leggTil(ansatt));
        } else {
            ansatt = ansattDao.finnMedBrukernavn("tbj");
        }
        System.out.println(ansatt);
        System.out.println(avdeling);
        assertNotNull(ansatt);
        assertTrue(ansattDao.fjern(ansatt.getId()));
    }

    @Test
    void leggTil_og_fjern_ansatt() {
        System.out.println("Legg til og fjern ansatt:");
        Avdeling avdeling = avdelingDao.finn(2);
        System.out.println(avdeling);

        Ansatt ansatt = new Ansatt(faker.name().firstName(), faker.name().lastName(), avdeling);

        int ansattId = ansattDao.leggTil(ansatt);

        assertNotEquals(-1, ansattId);


        Avdeling avdeling2 = avdelingDao.finn(2);
        System.out.println(avdeling2);
        ansattDao.fjern(ansattId);
    }

    @Test
    void finnAnsattMedBrukernavn() {
        System.out.println("Finn bruker med brukernavn=bbj:");
        System.out.println(ansattDao.finnMedBrukernavn("bbj"));
    }

    @Test
    void oppdaterAnsatt() {
        System.out.println("Oppdater navn på bruker med id=1:");
        Ansatt ansatt = ansattDao.finn(1);
        String oldName = ansatt.getFornavn();
        System.out.printf("Navn før: %s %s\n", ansatt.getFornavn(), ansatt.getEtternavn());
        ansatt.setFornavn("Espen");
        ansattDao.lagre(ansatt);
        Ansatt ansattEtter = ansattDao.finn(1);
        System.out.printf("Navn Etter: %s %s\n", ansatt.getFornavn(), ansatt.getEtternavn());
        assertEquals("Espen", ansattEtter.getFornavn());
        ansattEtter.setFornavn(oldName); // reverser endringer
        ansattDao.lagre(ansattEtter);
    }

    @Test
    void hentAlleAnsatte() {
        System.out.println("Henter alle ansatte:");
        List<Ansatt> ansatte = ansattDao.hentAlle();
        for (Ansatt ansatt : ansatte) {
            System.out.println(ansatt);
        }
        assertTrue(ansatte.size() > 2);
    }

    @Test
    void hentProsjekter() {
        System.out.println("Prosjekter for Ansatt med id: 5");
        Ansatt ansatt = ansattDao.finn(5);
        System.out.println(ansatt);

        var prosjekter = ansatt.getDeltagelser();

        for (var prosjektDeltagelse : prosjekter) {
            System.out.println(prosjektDeltagelse.getProsjekt());
            System.out.println(prosjektDeltagelse);
        }
    }
}