package dao;

import java.util.List;

public interface Dao<T> {

    /**
     * Legge til nye objekter i tabellen
     *
     * @param t objekt som skal legges til (persist)
     * @return integer id (private key) til objektet, -1 hvis mislykket
     */
    int leggTil(T t);


    /**
     * Søk etter objekt i tabellen
     *
     * @param id (private key) som det søkes på
     * @return objektet id tilhører
     */
    T finn(int id);


    /**
     * Fjern objekt i tabellen
     *
     * @param id (private key) som skal slettes
     * @return true om vellykket operasjon (remove)
     */
    boolean fjern(int id);


    /**
     * Lagre objekt i tabellen
     *
     * @param t objekt som skal lagres (merge)
     * @return integer id (private key) til objektet, -1 hvis mislykket
     */
    int lagre(T t);


    /**
     * Hent alle objekter i tabellen (SELECT * FROM t spørring)
     *
     * @return alle objektene i tabellen
     */
    List<T> hentAlle();
}
