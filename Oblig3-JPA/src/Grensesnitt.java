import dao.AnsattDao;
import dao.AvdelingDao;
import dao.ProsjektDao;
import dao.ProsjektdeltagelseDao;
import model.Ansatt;
import model.Avdeling;
import model.Prosjekt;
import model.Prosjektdeltagelse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Grensesnitt {
    private final AnsattDao ansattDao;
    private final AvdelingDao avdelingDao;
    private final ProsjektDao prosjektDao;
    private final ProsjektdeltagelseDao deltagelseDao;

    private final Scanner input;


    public Grensesnitt() {
        ansattDao = new AnsattDao();
        avdelingDao = new AvdelingDao();
        prosjektDao = new ProsjektDao();
        deltagelseDao = new ProsjektdeltagelseDao();
        input = new Scanner(System.in);
    }

    private final String NEWLINES = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

    private final String WORKING = NEWLINES +
            "     ───▄▄▄\n" +
            "     ─▄▀░▄░▀▄\n" +
            "     ─█░█▄▀░█\n" +
            "     ─█░▀▄▄▀█▄█▄▀\n" +
            "...  ▄▄█▄▄▄▄███▀\n";

    private final String HOVEDMENY_HEADER2 = "\n" +
            "────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────\n" +
            "─██████──██████─██████████████─██████──██████─██████████████─████████████───██████──────────██████─██████████████─██████──────────██████─████████──████████─\n" +
            "─██░░██──██░░██─██░░░░░░░░░░██─██░░██──██░░██─██░░░░░░░░░░██─██░░░░░░░░████─██░░██████████████░░██─██░░░░░░░░░░██─██░░██████████──██░░██─██░░░░██──██░░░░██─\n" +
            "─██░░██──██░░██─██░░██████░░██─██░░██──██░░██─██░░██████████─██░░████░░░░██─██░░░░░░░░░░░░░░░░░░██─██░░██████████─██░░░░░░░░░░██──██░░██─████░░██──██░░████─\n" +
            "─██░░██──██░░██─██░░██──██░░██─██░░██──██░░██─██░░██─────────██░░██──██░░██─██░░██████░░██████░░██─██░░██─────────██░░██████░░██──██░░██───██░░░░██░░░░██───\n" +
            "─██░░██████░░██─██░░██──██░░██─██░░██──██░░██─██░░██████████─██░░██──██░░██─██░░██──██░░██──██░░██─██░░██████████─██░░██──██░░██──██░░██───████░░░░░░████───\n" +
            "─██░░░░░░░░░░██─██░░██──██░░██─██░░██──██░░██─██░░░░░░░░░░██─██░░██──██░░██─██░░██──██░░██──██░░██─██░░░░░░░░░░██─██░░██──██░░██──██░░██─────████░░████─────\n" +
            "─██░░██████░░██─██░░██──██░░██─██░░██──██░░██─██░░██████████─██░░██──██░░██─██░░██──██████──██░░██─██░░██████████─██░░██──██░░██──██░░██───────██░░██───────\n" +
            "─██░░██──██░░██─██░░██──██░░██─██░░░░██░░░░██─██░░██─────────██░░██──██░░██─██░░██──────────██░░██─██░░██─────────██░░██──██░░██████░░██───────██░░██───────\n" +
            "─██░░██──██░░██─██░░██████░░██─████░░░░░░████─██░░██████████─██░░████░░░░██─██░░██──────────██░░██─██░░██████████─██░░██──██░░░░░░░░░░██───────██░░██───────\n" +
            "─██░░██──██░░██─██░░░░░░░░░░██───████░░████───██░░░░░░░░░░██─██░░░░░░░░████─██░░██──────────██░░██─██░░░░░░░░░░██─██░░██──██████████░░██───────██░░██───────\n" +
            "─██████──██████─██████████████─────██████─────██████████████─████████████───██████──────────██████─██████████████─██████──────────██████───────██████───────\n" +
            "────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────\n";

    private final String HOVEDMENY = HOVEDMENY_HEADER2 +
            "1 - Legg til en ansatt\n" +
            "2 - Søk etter ansatt\n" +
            "3 - Utlys ansatte\n" +
            "4 - Endre ansatt\n" +
            "5 - Søk etter avdeling\n" +
            "6 - Endre avdeling\n" +
            "7 - Legg til ny avdeling\n" +
            "8 - Prosjektmeny\n" +
            "9 - Skriv ut ansatte på avdeling\n" +
            "0 - avslutt\n\n" +
            "Hva vil du gjøre? ";

    public void launch() {

        System.out.println("Velkommen til denne utrolige applikasjonen!\n");

        int valg;
        while (true) {
            System.out.print(HOVEDMENY);
            try {
                valg = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                continue;
            }

            if (!doValg(valg))
                System.exit(0);

            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }

    private boolean doValg(int valg) {
        switch (valg) {
            case 0:
                System.out.println("Cya later alligater, we snakes on a plane.");
                return false;
            case 1:
                leggTilAnsatt();
                break;
            case 2:
                sokAnsatte();
                break;
            case 3:
                printAnsatte();
                break;
            case 4:
                endreAnsatt();
                break;
            case 5:
                sokAvdeling();
                break;
            case 7:
                nyAvdeling();
                break;
            case 8:
                prosjektMeny();
                break;
            default:
                System.out.println("Valg ikke implementert.");
                pressEnter();
        }
        return true;
    }


    private void leggTilAnsatt() {

        var avdelinger = avdelingDao.hentAlle();
        System.out.println("Velg avdeling ansatt skal jobbe på.");
        for (var avdeling : avdelinger) {
            System.out.printf("%d - %s (sjef: %s (%d))\n", avdeling.getId(), avdeling.getNavn(), avdeling.getSjef().getNavn(), avdeling.getSjef().getId());
        }

        int id = validertInput();
        Avdeling avdeling = avdelingDao.finn(id);
        if (avdeling == null) {
            System.out.println("Fant ikke avdeling. Går tilbake til hovedmenyen...");
            pressEnter();
            return;
        }
        System.out.print("Fornavn: \n");
        String fornavn = input.nextLine();
        System.out.print("Etternavn: \n");
        String etternavn = input.nextLine();


        Ansatt ansatt = new Ansatt(fornavn, etternavn, avdeling);
        int ansattId = ansattDao.leggTil(ansatt);
        if (ansattId == -1) {
            System.out.println("Ansatt ikke lagt til, går tilbake til hovedmenyen...");
            pressEnter();
            return;
        }
        System.out.println("Ny ansatt lagt til:");
        System.out.println(ansattDao.finn(ansattId));
        pressEnter();
    }


    //Denne håper eg også dekker den delen av oppgåva som seier at vi må printe ut de ansatte på en avdeling
    private Avdeling sokAvdeling() {

        Avdeling avdeling = null;

        int valg = -1;

        while (valg != 0) {
            System.out.println("1. Søk etter avdeling med avdelings-id\n0. For å avslutte");

            valg = validertInput();
            if (valg == 1) {

                System.out.println("Skriv inn avdelings-ID for å søke etter avdeling");
                int id = validertInput();
                avdeling = avdelingDao.finn(id);
                if (avdeling == null) {
                    System.out.println("Finner ikke avdeling med ID " + id);
                } else {
                    System.out.println(avdeling);
                }
            }
            if (valg == 0) {
                continue;
            }
            pressEnter();
            if (avdeling != null) {
                return avdeling;
            }

        }

        return null;

    }

    private void nyAvdeling() {

        System.out.println("Du vil nå opprette ny avdeling...\n" +
                "Skriv inn navn på den nye avdelingen");
        String navn = input.nextLine();

        System.out.println("Velg ansatt som skal bli sjef i avdelingen.");
        Ansatt ansatt = sokAnsatte();
        if (ansatt == null) {
            return;
        }

        if (ansatt.erSjef()) {
            System.out.println("Beklager, denne personen er allerede sjef på en annen avdeling.");
        } else {

            Avdeling avdeling = new Avdeling(navn, ansatt);
            avdelingDao.leggTil(avdeling);
            System.out.printf("La til avdeling med sjef=%s\n", avdeling.getSjef().getNavn());
            System.out.println(avdeling);
        }
        pressEnter();
    }

    private void printAnsatte() {
        System.out.println(WORKING);
        List<Ansatt> ansatte = ansattDao.hentAlle();
        System.out.println(NEWLINES);
        System.out.println("Ansatte:");
        for (Ansatt ansatt : ansatte) {
            System.out.println(ansatt);
        }
        pressEnter();
    }

    private void prosjektMeny() {

        System.out.println(NEWLINES);

        int valg;
        while (true) {
            System.out.println(
                    "Prosjektmeny\n" +
                            "1. Søk og utskrift over hvem som jobber i et prosjekt\n" +
                            "2. Registrere prosjektdeltagelse for en ansatt\n" +
                            "3. Føre timer for en ansatt på et prosjekt\n" +
                            "4. Legge til et nytt prosjekt\n" +
                            "0. For å gå tilbake");

            valg = validertInput();
            if (valg == 1) {

                System.out.println("Skriv inn prosjektnummer for å få liste over deltagere");
                int prosjektId = validertInput();

                Prosjekt prosjekt = prosjektDao.finn(prosjektId);
                if (prosjekt == null) {
                    System.out.println("Fant ikke prosjekt med ID: " + prosjektId);
                } else {
                    var deltagere = prosjekt.getDeltagere();
                    if (deltagere == null) {
                        System.out.println("Det er ikke registrert ansatte på dette prosjektet");
                    } else {
                        for (var deltager : deltagere) {
                            System.out.println(deltager);
                        }
                    }
                }
                pressEnter();
            } else if (valg == 2) {

                System.out.println("Skriv inn ID på prosjektet du ønsker å registrere en ansatt til.");
                int prosjektId = validertInput();
                Prosjekt prosjekt = prosjektDao.finn(prosjektId);

                if (prosjekt == null) {
                    System.out.println("Fant ikke prosjekt med ID: " + prosjektId);
                } else {

                    System.out.println("Velg ansatt");
                    Ansatt ansatt = sokAnsatte();
                    if (ansatt == null) {
                        continue;
                    }
                    //  public Prosjektdeltagelse(Ansatt deltager, Prosjekt prosjekt, String rolle, int arbeidstimer) {
                    if (deltagelseDao.finn(ansatt, prosjekt) != null) {
                        System.out.printf("Ansatt %s har allerede deltagelse i dette prosjektet.\n" +
                                "Før timer", ansatt.getNavn());
                        pressEnter();
                        continue;
                    }
                    System.out.printf("Hvilken rolle skal %s ha i prosjektet?\n", ansatt.getNavn());
                    String rolle = input.nextLine();

                    System.out.println("Hvor mange timer skal føres?");
                    int timer = Integer.parseInt(input.nextLine());
                    Prosjektdeltagelse deltagelse = new Prosjektdeltagelse(ansatt, prosjekt, rolle, timer);
                    int deltagelseId = deltagelseDao.leggTil(deltagelse);
                    System.out.println("La til prosjektdeltagelse:");
                    System.out.println(deltagelseDao.finn(deltagelseId));
                }
                pressEnter();
            } else if (valg == 3) {

                System.out.println("Hvilken ansatt vil du føre timer på?");
                Ansatt ansatt = sokAnsatte();
                if (ansatt == null) {
                    continue;
                }

                Set<Prosjektdeltagelse> deltagelser = ansatt.getDeltagelser();
                if (deltagelser.size() < 1) {
                    System.out.printf("Ansatt %s er ikke med i noen prosjekter.\n", ansatt.getNavn());
                }

                for (var deltagelse : deltagelser) {
                    System.out.println(deltagelse.getProsjekt());
                }
                System.out.print("Velg prosjekt (id): ");
                Prosjekt prosjekt = prosjektDao.finn(validertInput());
                System.out.println();
                if (prosjekt == null) {
                    System.out.println("Ugyldig input");
                    pressEnter();
                    continue;
                }

                Prosjektdeltagelse deltagelse = deltagelseDao.finn(ansatt, prosjekt);
                if (deltagelse == null) {
                    System.out.println("Noe gikk galt"); // dette bør ikke skje
                    pressEnter();
                    continue;
                }
                System.out.printf("%s har %d timer i %s\n",
                        ansatt.getNavn(), deltagelse.getArbeidstimer(), prosjekt.getNavn());


                System.out.print("Skriv inn antall nye timer: ");
                int nyeTimer = Integer.parseInt(input.nextLine());
                deltagelse.setArbeidstimer(deltagelse.getArbeidstimer() + nyeTimer);
                deltagelseDao.lagre(deltagelse);
                System.out.printf("Oppdatert deltagelse for %s\n", ansatt.getNavn());
                pressEnter();

            } else if (valg == 4) {

                System.out.println("Skriv inn navn på det nye prosjektet");
                String prosjektnavn = input.nextLine();
                System.out.println("Skriv inn beskrivelse av det nye prosjektet");
                String prosjektbeskrivelse = input.nextLine();
                System.out.println("Oppretter nytt prosjekt med navn " + prosjektnavn + " og prosjektbeskrivelse " + prosjektbeskrivelse);
                Prosjekt nyttProsjekt = new Prosjekt(prosjektnavn, prosjektbeskrivelse);

                int prosjektId = prosjektDao.leggTil(nyttProsjekt);
                System.out.println("La til nytt prosjekt:");
                System.out.println(prosjektDao.finn(prosjektId)); // bruker finn for debug, ser at ID er oppdatert riktig

            } else if (valg == 0) {
                return;
            }
            pressEnter();
        }
    }

    private void pressEnter() {
        System.out.println("\nTrykk enter for å fortsette...");
        input.nextLine();
        System.out.println(NEWLINES);
    }

    private Ansatt sokAnsatte() { // ikke for mye fokus på grensesnitt, så dette blir rotete

        int valg = -1;
        Ansatt ansatt = null;

        while (valg != 0) {

            System.out.println(
                    "1 - søk på id\n" +
                            "2 - søk på brukernavn\n" +
                            "0 - gå tilbake");

            valg = validertInput();
            if (valg == -1) {
                System.out.println("Feil input");
                pressEnter();
                continue;
            }

            if (valg == 1) {
                System.out.print("Id: ");
                int id = validertInput();
                if (id == -1) {
                    System.out.println("Ugyldig input");
                }
                System.out.println(WORKING);
                ansatt = ansattDao.finn(id);
                if (ansatt == null) {
                    System.out.println("Fant ikke ansatt med id = " + id);
                } else {
                    System.out.println(ansatt);
                }
            } else if (valg == 2) {
                System.out.print("Brukernavn: ");
                String brukernavn = input.nextLine();
                System.out.println(WORKING);
                ansatt = ansattDao.finnMedBrukernavn(brukernavn);
                if (ansatt == null) {
                    System.out.println("Fant ikke ansatt");
                } else {
                    System.out.println(ansatt);
                }
            } else if (valg == 0) {
                continue;
            } else {
                System.out.println("Valg finnes ikke");
            }
            pressEnter();
            if (ansatt != null) {
                return ansatt;
            }
        }
        return null;
    }

    private void endreAnsatt() {
        System.out.println("Hvilken ansatt vil du endre?");
        Ansatt ansatt = sokAnsatte();
        if (ansatt == null) return;

        int valg = -1;
        while (valg != 0) {
            System.out.println("Endrer ansatt " + ansatt.getNavn());
            System.out.println("Hva vil du endre?\n" +
                    "1 - Stilling\n" +
                    "2 - Lønn\n" +
                    "3 - Endre avdeling\n" +
                    "0 - gå tilbake");

            valg = validertInput();
            if (valg == -1) {
                System.out.println("Ugyldig input");
                continue;
            }

            if (valg == 1) {
                System.out.printf("Angi ny stilling (%s)\n", ansatt.getStilling());
                String stilling = input.nextLine();
                ansatt.setStilling(stilling);
                ansattDao.lagre(ansatt);
                System.out.println("Endret stilling til " + ansatt.getStilling());
            } else if (valg == 2) {
                System.out.printf("Angi ny lønn (%s)\n", ansatt.getManedslonn());
                var nyLonn = new BigDecimal(input.nextLine());
                ansatt.setManedslonn(nyLonn);
                ansattDao.lagre(ansatt);
                System.out.println("Endret lønn til " + ansatt.getManedslonn());
            } else if (valg == 3) {
                System.out.println("Angi ny avdeling for " + ansatt.getNavn() + "\n");
                if (ansatt.erSjef()) {
                    System.out.println("Beklager, denne ansatte er sjef og kan ikke bytte avdeling");
                } else {
                    ansatt.setAvdeling(sokAvdeling());
                    ansattDao.lagre(ansatt);
                    System.out.println("Endret avdelingen til " + ansatt.getAvdeling().getId());
                }
            } else if (valg == 0) {
                continue;
            }
            pressEnter();
        }
    }

    private int validertInput() {
        try {
            return Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }
}
