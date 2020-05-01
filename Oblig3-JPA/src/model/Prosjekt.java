package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Prosjekt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String navn;
    private String beskrivelse;

    @OneToMany(mappedBy = "prosjekt")
    public Set<Prosjektdeltagelse> deltagere = new HashSet<>();

    public Prosjekt() {}

    public Prosjekt(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public int getId() {
        return id;
    }

    public String getNavn() {
        return navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public Set<Prosjektdeltagelse> getDeltagere() {
        return deltagere;
    }


    @Override
    public String toString() {
        return "Prosjekt{" +
                "id=" + id +
                ", navn='" + navn + '\'' +
                ", beskrivelse='" + beskrivelse + '\'' +
                '}';
    }
}
