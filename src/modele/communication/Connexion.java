package modele.communication;

/**
 * D�finition d'une connexion qui garde en m�moire les antennes reliant 2 Cellulaires
 * @author Fred Simard | ETS
 * @version H21
 */

import modele.reseau.Antenne;
import modele.reseau.Cellulaire;

public class Connexion {
	
	private static final int NB_ANTENNES= 2;
	
	private int numeroConnexion;
	private Antenne[] antennes = new Antenne[NB_ANTENNES];
	
	// constructeur par param�tre
	public Connexion(int numeroConnexion, Antenne antenne1, Antenne antenne2) {
		this.numeroConnexion = numeroConnexion;
		antennes[0] = antenne1;
		antennes[1] = antenne2;
	}

	/**
	 *  informatrice du num�ro de connexion
	 * @return num�ro de connexion
	 */
	public int getNumeroConnexion() {
		return numeroConnexion;
	}	
	
	/**
	 * m�thode comparant les num�ros de connexion
	 * @param numero a comparer
	 * @return vrai si �gaux
	 */
	public boolean equals(int numero) {
		return numeroConnexion==numero;
	}

	/**
	 * Mise � jours de la r�f�rence de l'antenne
	 * @param ancienne antenne � remplacer
	 * @param nouvelle nouvelle antenne
	 */
	public void miseAJourAntenne(Antenne ancienne, Antenne nouvelle){
		
		if(antennes[0] == ancienne) {
			antennes[0] = nouvelle;
		}else {
			antennes[1] = nouvelle;
		}
	}
	
	/**
	 * Accesseur de l'antenne destination
	 * @param antenneSrc antenne oppos�e
	 * @return destination
	 */
	public Antenne getAntenneDest(Antenne antenneSrc){
		
		if(antennes[0] == antenneSrc) {
			return antennes[1];
		}else {
			return antennes[0];
		}

	}


}
