package modele.reseau;

/**
 * Classe d�finissant une antenne, �l�ment connectant un cellulaire
 * au r�seau.
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */

import java.util.ArrayList;

import modele.communication.Message;
import modele.physique.ObjetPhysique;
import modele.physique.Position;

public class Antenne extends ObjetPhysique implements UniteCellulaire{
	
	// obtient une r�f�rence au gestionnaire r�seau
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
	 * @param position � comparer
	 * @return distance antenne-position
	 */
	public double distance(Position position) {
		return this.position.distance(position);
	}
	
	/**
	 * M�thode utilis� par un cellulaire pour s'enregistrer � l'antenne
	 * @param cellulaire � enregistrer
	 */
	public void enregistrer(Cellulaire cellulaire) {
		cellulaires.add(cellulaire);
	}
	
	/**
	 * M�thode utilis� par un cellulaire pour se d�senregistrer � l'antenne
	 * @param cellulaire � retirer
	 */
	public void desenregistrer(Cellulaire cellulaire) {
		cellulaires.remove(cellulaire);
	}

	/**
	 * M�thode pour mettre � jour une connexion
	 * @param numeroConnexion num�ro de la connexion � mettre � jour
	 * @param ancienneAntenne ancienne antenne � remplacer
	 */
	public void mettreAJourConnexion(int numeroConnexion, Antenne ancienneAntenne) {
		reseau.mettreAJourConnexion(numeroConnexion, ancienneAntenne, this);
	}

	/**
	 * M�thode pour appeler d'un cellulaire part le r�seau
	 * @param numeroAppele num�ro du cellulaire appel�
	 * @param numeroAppelant num�ro du cellulaire appelant
	 * @param antenneConnecte r�f�rence � l'antenne connect� (ignor�)
	 * @return num�ro de connexion
	 */
	@Override
	public int appeler(String numeroAppele, String numeroAppelant, Antenne antenneConnecte) {
		return reseau.relayerAppel(numeroAppele, numeroAppelant, this);
	}
	
	/**
	 * M�thode pour r�pondre � un appel du r�seau
	 * @param numeroAppele num�ro du cellulaire appel�
	 * @param numeroAppelant num�ro du cellulaire appelant
	 * @param numeroConnexion num�ro de la connexion � mettre � jour
	 * @return r�f�rence au cellulaire qui r�pond
	 */
	@Override
	public Cellulaire repondre(String numeroAppele, String numeroAppelant, int numeroConnexion) {
		
		Cellulaire cellConnecte = null;
		
		// parcour les cellulaires
		for(Cellulaire cellulaire : cellulaires) {
			
			// si num�ro correspond, transmet
			if(cellulaire.comparerNumero(numeroAppele)) {
				cellConnecte = cellulaire.repondre(numeroAppele, numeroAppelant, numeroConnexion);
			}
		}
		
		return cellConnecte;
	}
	
	/**
	 * M�thode appel� par un cellulaire pour mettre fin � un appel
	 * @param numeroAppele num�ro du cellulaire appel�
	 * @param numeroConnexion num�ro de la connexion � mettre � jour
	 */
	@Override
	public void finAppelLocal(String numeroAppele, int numeroConnexion) {
		reseau.relayerFinAppel(numeroAppele, numeroConnexion, this);
	}

	/**
	 * M�thode appel� pour indiquer � un cellulaire de mettre fin � un appel
	 * @param numeroAppele num�ro du cellulaire appel�
	 * @param numeroConnexion num�ro de la connexion � mettre � jour
	 */
	@Override
	public void finAppelDistant(String numeroAppele, int numeroConnexion) {

		// parcour les cellulaires
		for(Cellulaire cellulaire : cellulaires) {
			
			// si num�ro correspond, transmet
			if(cellulaire.comparerNumero(numeroAppele)) {
				cellulaire.finAppelDistant(numeroAppele, numeroConnexion);
				return;
			}
		}
	}

	/**
	 * M�thode pour envoyer un message vers le r�seau
	 * @param msg Message � transmettre
	 * @param numeroConnexion num�ro de la connexion � mettre � jour
	 */
	@Override
	public void envoyer(Message msg, int numeroConnexion)
	{
		
		// fait suivre le message � la carte
		reseau.relayerMessage(msg, numeroConnexion, this);
	}

	/**
	 * M�thode pour recevoir un message du r�seau
	 * @param msg Message � recevoir
	 */
	@Override
	public void recevoir(Message msg) {

		// parcour les cellulaires
		for(Cellulaire cellulaire : cellulaires) {
			
			// si num�ro correspond, transmet
			if(cellulaire.comparerNumero(msg.getNumeroDest())) {
				cellulaire.recevoir(msg);
			}
		}
	}


}
