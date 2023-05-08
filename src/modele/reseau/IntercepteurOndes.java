package modele.reseau;

import modele.communication.Message;
import modele.gestionnaires.GestionnaireJeu;
import modele.gestionnaires.GestionnaireScenario;
import modele.physique.Position;

public class IntercepteurOndes extends Antenne
{
    GestionnaireScenario scenario = GestionnaireScenario.getInstance();
    GestionnaireJeu jeu = GestionnaireJeu.getInstance();

    /**
     * constructeur d'une antenne
     *
     * @param position de l'antenne
     */
    public IntercepteurOndes(Position position)
    {
        super(position);
    }

    /**
     * Méthode pour envoyer un message vers le réseau
     * @param msg Message à transmettre
     * @param numeroConnexion numéro de la connexion à mettre à jour
     */

    @Override
    public void envoyer(Message msg, int numeroConnexion)
    {
        String numero = msg.getNumeroDest();

        //verifie si la conversation vient d'un numero criminel
        if(scenario.numeroEstCriminel(numero))
        {
            jeu.gagnerPoints(); //le joueur gagne un point
        }

        super.envoyer(msg, numeroConnexion);
    }
}
