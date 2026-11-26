package Modele_2;

public class Compte {
    private int id;
    private String numero;
    private double solde;
    private int id_proprietaire;

    public Compte(int id, String numero, double solde, int id_proprietaire) {
        this.id = id;
        this.numero = numero;
        this.solde = solde;
        this.id_proprietaire = id_proprietaire;
    }

    public Compte(String numero, double solde, int id_proprietaire) {
        this.numero = numero;
        this.solde = solde;
        this.id_proprietaire = id_proprietaire;
    }

    public int getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public int getId_proprietaire() {
        return id_proprietaire;
    }

    public void setId(int id) {
        this.id = id;
    }
}
