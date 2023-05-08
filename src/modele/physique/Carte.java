package modele.physique;

/**
 * La carte est un module utilitaire qui défini les paramètres de la carte de jeu
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */

import java.util.Random;

public class Carte{

	public static final Position DIMENSION_JEU = new Position(3000,2000);
	public static Random rand = new Random();

	/**
	 * Méthode pour obtenir une position aléatoire sur la carte
	 * @return position aléatoire
	 */
	public static Position getPositionAlea() {
		return new Position(rand.nextDouble()*DIMENSION_JEU.getX(),rand.nextDouble()*DIMENSION_JEU.getY());
	}
	
	/**
	 * Méthode pour replacer une position dans la carte selon la logique du torus
	 * @param position position à ajuster
	 */
	public static void ajusterPosition(Position position) {
		
		position.setX((position.getX()+DIMENSION_JEU.getX())%DIMENSION_JEU.getX());
		position.setY((position.getY()+DIMENSION_JEU.getY())%DIMENSION_JEU.getY());
		
	}	

	/**
	 * Méthode permettant de valider si la position est dans la carte
	 * @param position à valider
	 * @return vrai si la position est valide
	 */
	public static boolean validerPosition(Position position) {
		
		if(position.getX()>=0 && position.getX()<DIMENSION_JEU.getX() &&
		   position.getY()>=0 && position.getY()<DIMENSION_JEU.getY()) {
			return true;
		}
		return false;
	}
}
