package model;

import dao.AnsattDao;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Ansatt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String brukernavn;
    private String fornavn;
    private String etternavn;
    private LocalDate ansattdato;
    private String stilling;
    private BigDecimal manedslonn;

    private static final AnsattDao ansattDao = new AnsattDao(); // auto-generering av unike navn


    @ManyToOne
    @JoinColumn(name = "avdeling_id", referencedColumnName = "id")
    private Avdeling avdeling;

    @OneToMany(mappedBy = "deltager")
    private final Set<Prosjektdeltagelse> deltagelser = new HashSet<>();

    public Set<Prosjektdeltagelse> getDeltagelser() {
        return deltagelser;
    }

    public Ansatt() {}

    public Ansatt(String fornavn, String etternavn, Avdeling avdeling) {
        this(fornavn, etternavn, null, avdeling);
    }

    public Ansatt(String fornavn, String etternavn, LocalDate ansattdato, Avdeling avdeling) {

        genererBrukernavn(fornavn, etternavn);
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        if (ansattdato == null) {
            ansattdato = LocalDate.now();
        }
        this.ansattdato = ansattdato;
        this.avdeling = avdeling;
    }

    private void genererBrukernavn(@NotNull String fornavn, @NotNull String etternavn) { // sørger for unike brukernavn

        brukernavn = (fornavn.substring(0, 1) + etternavn.substring(0, 2)).toLowerCase();

        while (ansattDao.finnMedBrukernavn(brukernavn) != null) {
            brukernavn += (int)(Math.random() * 9) + 1;
        }
    }

    public int getId() {
        return id;
    }

    public Avdeling getAvdeling() {
        return avdeling;
    }

    @Column(name = "brukernavn")
    public String getBrukernavn() {
        return brukernavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public LocalDate getAnsattdato() {
        return ansattdato;
    }

    public String getStilling() {
        return stilling;
    }

    public void setStilling(String stilling) {
        this.stilling = stilling;
    }

    public BigDecimal getManedslonn() {
        return manedslonn;
    }

    public void setAvdeling(Avdeling avdeling) { this.avdeling = avdeling; }

    public void setManedslonn(BigDecimal maanedsloenn) {
        this.manedslonn = maanedsloenn;
    }

    public boolean erSjef() {
        return id == avdeling.getSjef().id;
    }

    public String getNavn () {
        return fornavn + " " + etternavn;
    }

    @Override
    public String toString() {
        return "Ansatt{" +
                "id=" + id +
                ", brukernavn='" + brukernavn + '\'' +
                ", navn='" + getNavn() + '\'' +
                ", ansattDato='" + ansattdato + '\'' +
                ", avdeling='" + avdeling.getNavn() + '\'' +
                ", stilling='" + stilling + '\'' +
                ", lønn='" + manedslonn + '\'' +
                ", erSjef='" + erSjef() + '\'' +
                ", deltager i " + deltagelser.size() + " prosjekter" + '\'' +
                '}';
    }
}
