package DAO;

import Modele_2.Proprietaire;
import config.ConnexionMYSQL;

import java.sql.*;
import java.util.ArrayList;

public class ProprietaireDAO {

    public static ArrayList<Proprietaire> prendreProprietaires () {
        ArrayList<Proprietaire> proprietaires = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, adresse FROM proprietaire";

        try (Connection cn = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = cn.prepareStatement(sql);) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String adresse = rs.getString("adresse");
                Proprietaire p = new Proprietaire(id, nom, prenom, adresse);
                proprietaires.add(p);

            }
        } catch (SQLException ex) {
            System.out.println("Erreur : " + ex.getMessage());
        }

        return proprietaires;
    }

    public static Proprietaire rechercher (int id_proprietaire) {
        String sql = "SELECT id, nom, prenom, adresse FROM proprietaire WHERE id = ?";

        try (Connection cn = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = cn.prepareStatement(sql);) {
            ps.setInt(1, id_proprietaire);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String adresse = rs.getString("adresse");
                return new Proprietaire(id, nom, prenom, adresse);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur : " + ex.getMessage());
        }

        return null;
    }


    public static void ajouterProprietaire(Proprietaire p) {
        String sql = "INSERT INTO proprietaire(nom, prenom, adresse) VALUES (?, ?, ?)";
        try (Connection cn = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getAdresse());
            ps.executeUpdate();

            int lignes = ps.executeUpdate();
            if (lignes == 0) {
                System.out.println("Aucune ligne insérée.");
                return;
            }

            // On met à jour la valeur id de la Classe. Comme nonou.
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                } else {
                    System.out.println("Insertion OK mais id non récupéré.");
                    return;
                }
            }

            System.out.println("Propriétaire ajouté avec succès (id = " + p.getId() + ").");
        } catch (SQLException ex) {
            System.out.println("Erreur : " + ex.getMessage());
        }
    }

    public static void modifierProprietaire(Proprietaire p) {
        String sql = "UPDATE proprietaire SET nom=?, prenom=?, adresse=? WHERE id=?";
        try (Connection cn = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = cn.prepareStatement(sql);) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getAdresse());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
            System.out.println("Opération réussi ! "+ p.getNom() + " " + p.getPrenom() + " " + p.getAdresse() + " créé(e).");
        } catch (SQLException ex) {
            System.out.println("Erreur : " + ex.getMessage());
        }
    }

    public static void supprimerProprietaire(int id) {
        String sql = "DELETE FROM proprietaire WHERE id = ?";
        Proprietaire p = rechercher(id);
        try (Connection cn = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = cn.prepareStatement(sql);) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println(p.getPrenom() + " " + p.getNom() +" supprimé avec succès.");
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppréssion de ("+ p.getPrenom() +" "+ p.getNom() + ") : "+ ex.getMessage());
        }
    }

}
