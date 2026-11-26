package DAO;

import Modele_2.Compte;
import Modele_2.Proprietaire;
import config.ConnexionMYSQL;

import java.sql.*;
import java.util.ArrayList;

public class CompteDAO {

    public static void creerCompte(Compte compte, ProprietaireDAO pdao) {
        String sql = "INSERT INTO compte(num_compte, solde, id_prop) VALUES(?, ?, ?)";

        try (Connection c = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, compte.getNumero());
            ps.setDouble(2, compte.getSolde());
            ps.setInt(3, compte.getId_proprietaire());

            int lignes = ps.executeUpdate();
            if (lignes == 0) {
                System.out.println("Aucune ligne insérée.");
                return;
            }

            // On met à jour la valeur id de la Classe. Comme nonou.
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    compte.setId(keys.getInt(1));
                } else {
                    System.out.println("Insertion OK mais id non récupéré.");
                    return;
                }
            }

            System.out.println("Succès ! Compte ("+compte.getNumero()+") de Mr(Mme) "+pdao.rechercher(compte.getId_proprietaire()).getNomComplet()+ " créé avec succès.");
            System.out.println("Solde : "+ compte.getSolde());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion : "+ e.getMessage());
        }
    }

    public static void modifierCompte(Compte compte) {
        String sql = "UPDATE compte SET num_compte=?, solde=? WHERE id=?";

        try (Connection cn = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, compte.getNumero());
            ps.setDouble(2, compte.getSolde());
            ps.setInt(3, compte.getId());
            ps.executeUpdate();

            System.out.println("Mise à jour réussi !");
            System.out.println("Nouvelles infos :");
            System.out.println("Numéro -> "+ compte.getNumero());
            System.out.println("Solde -> "+ compte.getSolde());
        } catch (SQLException e) {
            System.out.println("Echec : "+ e.getMessage());
        }
    }

    public static void supprimerCompte(Compte compte_a_supprimer, ProprietaireDAO pdao) {
        String sql = "DELETE FROM compte WHERE id=?";
        String proprietaire = pdao.rechercher(compte_a_supprimer.getId_proprietaire()).getNomComplet();

        try (Connection cn = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, compte_a_supprimer.getId());
            ps.executeUpdate();

            System.out.println("Compte ("+compte_a_supprimer.getNumero()+") de Mr(Mme) "+ proprietaire +" supprimé avec succès.");
        } catch (SQLException e) {
            System.out.println("Echec de la suppréssion du compte de Mr(Mme) "+proprietaire+". erreur : "+ e.getMessage());
        }
    }

    public static Compte rechercherParNumero(String numero) {
        String sql = "SELECT id, num_compte, solde, id_prop FROM compte WHERE num_compte = ?";

        try (Connection c = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1, numero);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String num_compte = rs.getString("num_compte");
                double solde = rs.getDouble("solde");
                int id_prop = rs.getInt("id_prop");
                System.out.println("Compte trouvé !");
                //return new Compte(id, num_compte, solde, id_prop);
                return new Compte(id, num_compte, solde, id_prop);
            }
        } catch (SQLException ex) {
            System.out.println("Numéro introuvable ou Compte innexistant ou Erreur sur les valeurs saisies.");
        }
        return null;
    }

    public static ArrayList<Compte> listerParProprietaire(int idProprietaire) {
        ArrayList<Compte> comptes_prop = new ArrayList<>();
        String sql = "SELECT * FROM compte WHERE id_prop = ?";

        try (Connection c = ConnexionMYSQL.ouvrir();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idProprietaire);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comptes_prop.add(new Compte(
                            rs.getInt("id"),
                            rs.getString("num_compte"),
                            rs.getDouble("solde"),
                            rs.getInt("id_prop")
                    ));
                }
            }

            String mot = comptes_prop.size() > 1 ? "comptes" : "compte";
            System.out.println("Succès ! -> (" + comptes_prop.size() + ") " + mot + " pour ce propriétaire.");
            return comptes_prop;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des comptes : " + e.getMessage());
            return comptes_prop;
        }
    }

}
