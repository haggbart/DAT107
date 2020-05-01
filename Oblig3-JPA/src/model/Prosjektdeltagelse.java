package model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Prosjektdeltagelse {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int arbeidstimer;
    private String rolle;

    @ManyToOne
    @JoinColumn(name = "ansatt_id", referencedColumnName = "id")
    private Ansatt deltager;

    @ManyToOne
    @JoinColumn(name = "prosjekt_id", referencedColumnName = "id")
    private Prosjekt prosjekt;

    public int getId() {
        return id;
    }

    public Prosjektdeltagelse() {}


    public Prosjektdeltagelse(Ansatt deltager, Prosjekt prosjekt, String rolle, int arbeidstimer) {
        this.deltager = deltager;
        this.prosjekt = prosjekt;
        this.rolle = rolle;
        this.arbeidstimer = arbeidstimer;
    }

    public Integer getArbeidstimer() {
        return arbeidstimer;
    }

    public void setArbeidstimer(Integer arbeidstimer) {
        this.arbeidstimer = arbeidstimer;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public Ansatt getDeltager() {
        return deltager;
    }

    public Prosjekt getProsjekt() {
        return prosjekt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prosjektdeltagelse that = (Prosjektdeltagelse) o;
        return (id == that.id) ||
                        (deltager.getId() == that.deltager.getId()) &&
                        (prosjekt.getId() == that.prosjekt.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(deltager.getId(), prosjekt.getId());
    }

    @Override
    public String toString() {
        return "Prosjektdeltagelse{" +
                "prosjektId=" + prosjekt.getId() +
                ", deltager=" + deltager.getNavn() +
                ", prosjekt=" + prosjekt.getNavn() +
                ", rolle='" + rolle + '\'' +
                ", arbeidstimer=" + arbeidstimer +
                '}';
    }
}
