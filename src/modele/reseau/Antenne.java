package modele.reseau;

/**
 * Classe définissant une antenne, élément connectant un cellulaire
 * au réseau.
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */

import java.util.ArrayList;

import modele.communication.Message;
import modele.physique.ObjetPhysique;
import modele.physique.Position;

public class Antenne extends ObjetPhysique implements UniteCellulaire{
	
	// obtient une référence au gestionnaire réseau
	GestionnaireReseau reseau = GestionnaireReseau.getInstance();
	ArrayList<Cellulaire> cellulaires = new ArrayList<Cellulaire>();
	
	/**
	 * constructeur d'une antenne
	 * @param position de l'antenne
	 */
	public Antenne(Position position) {
		super(position);
		this.position = position;
	}

	/**
	 * obtient la distance entre une position et l'antenne
	 * @param position à comparer
	 * @return distance antenne-position
	 */
	public double distance(Position position) {
		return this.position.distance(position);
	}
	
	/**
	 * Méthode utilisé par un cellulaire pour s'enregistrer à l'antenne
	 * @param cellulaire à enregistrer
	 */
	public void enregistrer(Cellulaire cellulaire) {
		cellulaires.add(cellulaire);
	}
	
	/**
	 * Méthode utilisé par un cellulaire pour se désenregistrer à l'antenne
	 * @param cellulaire à retirer
	 */
	public void desenregistrer(Cellulaire cellulaire) {
		cellulaires.remove(cellulaire);
	}

	/**
	 * Méthode pour mettre à jour une connexion
	 * @param numeroConnexion numéro de la connexion à mettre à jour
	 * @param ancienneAntenne ancienne antenne à remplacer
	 */
	public void mettreAJourConnexion(int numeroConnexion, Antenne ancienneAntenne) {
		reseau.mettreAJourConnexion(numeroConnexion, ancienneAntenne, this);
	}

	/**
	 * Méthode pour appeler d'un cellulaire part le réseau
	 * @param numeroAppele numéro du cellulaire appelé
	 * @param numeroAppelant numéro du cellulaire appelant
	 * @param antenneConnecte référence à l'antenne connecté (ignoré)
	 * @return numéro de connexion
	 */
	@Override
	public int appeler(String numeroAppele, String numeroAppelant, Antenne antenneConnecte) {
		return reseau.relayerAppel(numeroAppele, numeroAppelant, this);
	}
	
	/**
	 * Méthode pour répondre à un appel du réseau
	 * @param numeroAppele numéro du cellulaire appelé
	 * @param numeroAppelant numéro du cellulaire appelant
	 * @param numeroConnexion numéro de la connexion à mettre à jour
	 * @return référence au cellulaire qui répond
	 */
	@Override
	public Cellulaire repondre(String numeroAppele, String numeroAppelant, int numeroConnexion) {
		
		Cellulaire cellConnecte = null;
		
		// parcour les cellulaires
		for(Cellulaire cellulaire : cellulaires) {
			
			// si numéro correspond, transmet
			if(cellulaire.comparerNumero(numeroAppele)) {
				cellConnecte = cellulaire.repondre(numeroAppele, numeroAppelant, numeroConnexion);
			}
		}
		
		return cellConnecte;
	}
	
	/**
	 * Méthode appelé par un cellulaire pour mettre fin à un appel
	 * @param numeroAppele numéro du cellulaire appelé
	 * @param numeroConnexion numéro de la connexion à mettre à jour
	 */
	@Override
	public void finAppelLocal(String numeroAppele, int numeroConnexion) {
		reseau.relayerFinAppel(numeroAppele, numeroConnexion, this);
	}

	/**
	 * Méthode appelé pour indiquer à un cellulaire de mettre fin à un appel
	 * @param numeroAppele numéro du cellulaire appelé
	 * @param numeroConnexion numéro de la connexion à mettre à jour
	 */
	@Override
	public void finAppelDistant(String numeroAppele, int numeroConnexion) {

		// parcour les cellulaires
		for(Cellulaire cellulaire : cellulaires) {
			
			// si numéro correspond, transmet
			if(cellulaire.comparerNumero(numeroAppele)) {
				cellulaire.finAppelDistant(numeroAppele, numeroConnexion);
				return;
			}
		}
	}

	/**
	 * Méthode pour envoyer un message vers le réseau
	 * @param msg Message à transmettre
	 * @param numeroConnexion numéro de la connexion à mettre à jour
	 */
	@Override
	public void envoyer(Message msg, int numeroConnexion)
	{
		
		// fait suivre le message à la carte
		reseau.relayerMessage(msg, numeroConnexion, this);
	}

	/**
	 * Méthode pour recevoir un message du réseau
	 * @param msg Message à recevoir
	 */
	@Override
	public void recevoir(Message msg) {

		// parcour les cellulaires
		for(Cellulaire cellulaire : cellulaires) {
			
			// si numéro correspond, transmet
			if(cellulaire.comparerNumero(msg.getNumeroDest())) {
				cellulaire.recevoir(msg);
			}
		}
	}


}
