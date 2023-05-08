package modele.physique;

/**
 * La carte est un module utilitaire qui d�fini les param�tres de la carte de jeu
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */

import java.util.Random;

public class Carte{

	public static final Position DIMENSION_JEU = new Position(3000,2000);
	public static Random rand = new Random();

	/**
	 * M�thode pour obtenir une position al�atoire sur la carte
	 * @return position al�atoire
	 */
	public static Position getPositionAlea() {
		return new Position(rand.nextDouble()*DIMENSION_JEU.getX(),rand.nextDouble()*DIMENSION_JEU.getY());
	}
	
	/**
	 * M�thode pour replacer une position dans la carte selon la logique du torus
	 * @param position position � ajuster
	 */
	public static void ajusterPosition(Position position) {
		
		position.setX((position.getX()+DIMENSION_JEU.getX())%DIMENSION_JEU.getX());
		position.setY((position.getY()+DIMENSION_JEU.getY())%DIMENSION_JEU.getY());
		
	}	

	/**
	 * M�thode permettant de valider si la position est dans la carte
	 * @param position � valider
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
