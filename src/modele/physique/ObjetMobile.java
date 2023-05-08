package modele.physique;

/**
 * Classe abstraite définissant un objet mobile
 * 
 * L'objet mobile spécialise l'objet physique pour y intégrer les fonctionnalités
 * relative au mouvement.
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */

import java.util.Random;

public abstract class ObjetMobile extends ObjetPhysique{
	
	double direction = 0;
	double vitesse;
	double standardDev;
	
	Random rand = new Random();
	
	/**
	 * constructeur par paramètre
	 * @param position position initiale
	 * @param vitesse vitesse de déplacement
	 * @param standardDev variation de la direction
	 */
	public ObjetMobile(Position position, double vitesse, double standardDev) {
		super(position);
		this.standardDev = standardDev;
		this.vitesse = vitesse;
		
	}
	
	/**
	 * méthode permettant de déplacer l'objet
	 */
	protected void seDeplacer() {
		
		// mets à jours la direction
		direction += rand.nextGaussian()*this.standardDev;
		
		// mets à jours la position
		position.setX(position.getX() + Math.cos(direction)*vitesse);
		position.setY(position.getY() + Math.sin(direction)*vitesse);
		
		// ajuste la position
		Carte.ajusterPosition(position);
	}
	
	
}
