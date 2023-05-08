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
     * M�thode pour envoyer un message vers le r�seau
     * @param msg Message � transmettre
     * @param numeroConnexion num�ro de la connexion � mettre � jour
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
