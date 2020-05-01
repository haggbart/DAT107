package model;

import com.github.javafaker.Faker;
import dao.AnsattDao;
import dao.AvdelingDao;
import model.Ansatt;
import model.Avdeling;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AvdelingTest {


    private final AnsattDao ansattDao;
    private final AvdelingDao avdelingDao;
    private final Faker faker;

    public AvdelingTest() {
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
    void finnAvdelingMedId() {
        Avdeling avdeling = avdelingDao.finn(1);
        assertEquals("Frukt og grønt", avdeling.getNavn());
        System.out.println(avdeling);
    }

    @Test
    void faker() {
        System.out.println("Faker test:");
        System.out.println("navn: " + faker.name().fullName());
        System.out.println("avdeling: " + faker.team().name());
    }



    @Test
    void leggTilAvdeling() {

        // velg ansatt som ikke er sjef
        var ansatte = ansattDao.hentAlle();
        Ansatt sjef = null;
        for (Ansatt ansatt : ansatte) {
            if (!ansatt.erSjef()) sjef = ansatt;
        }
        assertNotNull(sjef);
        System.out.println("Ansatt som skal bli sjef i ny avdeling: ");
        System.out.println(sjef);
        System.out.println("Gammel avdeling før: ");
        Avdeling gammelAvdeling = sjef.getAvdeling();
        System.out.println(gammelAvdeling);

        String navn = faker.team().name();
        Avdeling avdeling = new Avdeling(navn, sjef);
        int avdelingId = avdelingDao.leggTil(avdeling);
        assertNotEquals(-1, avdelingId);
        assertNotEquals(0, avdelingId);
        System.out.println("Lagt til avdeling: ");
        System.out.println(avdelingDao.finn(avdelingId));
        System.out.println("Gammel avdeling etter:");
        System.out.println(avdelingDao.finn(gammelAvdeling.getId()));
    }

    @Test
    void fjern() { // fjern skal ikke være mulig
        var avdelinger = avdelingDao.hentAlle();
        Random random = new Random();
        int index = random.nextInt(avdelinger.size());
        Avdeling avdeling = avdelinger.get(index);
        assertNotNull(avdeling);
        assertThrows(UnsupportedOperationException.class, () -> avdelingDao.fjern(avdeling.getId()));
    }
}
