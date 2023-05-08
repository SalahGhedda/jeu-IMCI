package modele.physique;

/**
 * Classe abstraite d�finissant un objet mobile
 * 
 * L'objet mobile sp�cialise l'objet physique pour y int�grer les fonctionnalit�s
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
	 * constructeur par param�tre
	 * @param position position initiale
	 * @param vitesse vitesse de d�placement
	 * @param standardDev variation de la direction
	 */
	public ObjetMobile(Position position, double vitesse, double standardDev) {
		super(position);
		this.standardDev = standardDev;
		this.vitesse = vitesse;
		
	}
	
	/**
	 * m�thode permettant de d�placer l'objet
	 */
	protected void seDeplacer() {
		
		// mets � jours la direction
		direction += rand.nextGaussian()*this.standardDev;
		
		// mets � jours la position
		position.setX(position.getX() + Math.cos(direction)*vitesse);
		position.setY(position.getY() + Math.sin(direction)*vitesse);
		
		// ajuste la position
		Carte.ajusterPosition(position);
	}
	
	
}
