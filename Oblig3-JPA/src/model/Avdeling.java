package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Avdeling {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String navn;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "sjef_id", referencedColumnName = "id")
    private Ansatt sjef;

    @OneToMany(mappedBy = "avdeling", fetch = FetchType.EAGER)
    private final Set<Ansatt> ansatte;

    public Avdeling() {
        ansatte = new HashSet<>();
    }

    public Avdeling(String navn, Ansatt sjef) {
        this.sjef = sjef;
        this.navn = navn;
        ansatte = new HashSet<>();
        ansatte.add(sjef);
    }

    public int getId() {
        return id;
    }

    public Ansatt getSjef() {
        return sjef;
    }

    public String getNavn() {
        return navn;
    }

    public Set<Ansatt> getAnsatte() {
        return ansatte;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder(String.format("%s(ID: %d)\n", navn, id));
        sb.append(String.format("Sjef: %s %s\n", sjef.getFornavn(), sjef.getEtternavn()));
        for (Ansatt ansatt : ansatte) {
            sb.append(ansatt).append("\n");
        }

        return sb.toString();
    }
}
