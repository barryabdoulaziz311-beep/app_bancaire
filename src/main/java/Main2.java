/* D'abord, on tient à vous dire bienvenu sur notre projet commun Mr Gackou.

Les membres de cette groupe sont :
1. Ndeye Fall Mbengue
2. Adji Bèye
3. Abdoul Aziz Barry

On veut souligner aussi que le projet s'est voulu travaillé collectivement.
*/

import DAO.CompteDAO;
import DAO.ProprietaireDAO;
import Modele_2.Compte;
import Modele_2.Proprietaire;

import java.util.ArrayList;
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        ProprietaireDAO pdao = new ProprietaireDAO();
        Proprietaire prop = new Proprietaire("Gakou", "Mamadou", "Dakar");
        ProprietaireDAO.ajouterProprietaire(prop);

        Proprietaire proprietaire = ProprietaireDAO.rechercher(prop.getId());
        if (proprietaire == null) {
            System.out.println("Propriétaire introuvable.");
            return;
        }

        Compte mon_compte = new Compte("1234 5678 9ABC DEFG", 3000350000.0, proprietaire.getId());
        CompteDAO.creerCompte(mon_compte, pdao);

        Compte compte = CompteDAO.rechercherParNumero(mon_compte.getNumero());
        if (compte == null) {
            System.out.println("Compte introuvable.");
            return;
        }

        System.out.println("Tous les comptes bancaires de ce propriétaire :");
        ArrayList<Compte> comptes = CompteDAO.listerParProprietaire(compte.getId_proprietaire());
        if (comptes != null) {
            for (Compte cpt : comptes) {
                System.out.println("ID -> " + cpt.getId() + " ; solde : " + cpt.getSolde());
            }
        }

        String nouvNumCompte = "1234 5678 9123 4567";
        if (compte.getNumero().equalsIgnoreCase(nouvNumCompte)) {
            System.out.println("Valeurs identiques. réessayez.");
        } else {
            compte.setNumero(nouvNumCompte);
            CompteDAO.modifierCompte(compte);
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Voulez-vous supprimer ce compte (Oui/Non) ?");
        String reponse = sc.nextLine();
        if (reponse.equalsIgnoreCase("non")) {
            return;
        } else if (reponse.equalsIgnoreCase("oui")) {
            if (compte.getSolde() > 500) {
                System.out.println("Sa compte bi lu ci nekh bari na déh ("+compte.getSolde()+"), Sénégal laniou nékh, beugue nga réellement supprimé compte bi ? (Waw/Dédéte)");
                String tontou = sc.nextLine();
                if (tontou.equalsIgnoreCase("Dédéte")) {
                    System.out.println("Nii la kay.");
                } else if (tontou.equalsIgnoreCase("Waw")) {
                    CompteDAO.supprimerCompte(compte, pdao);
                    System.out.println("Mana tougne là comprendre !");
                } else {
                    System.out.println("Wakhatal ?");
                }
            } else {
                CompteDAO.supprimerCompte(compte, pdao);
            }
        } else {
            System.out.println("Saisie invalide");
        }



    }
}
