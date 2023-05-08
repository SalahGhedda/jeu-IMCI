package modele.physique;

/**
 * Classe abstraite définissant une position cartésienne dans un environnement 2D
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */
public class Position {
	double x;
	double y;
	
	/**
	 * constructeur par paramètre
	 * @param x coord x
	 * @param y coord y
	 */
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * accesseur coord X
	 * @return coord x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * mutateur de la coord X
	 * @param x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * accesseur coord Y
	 * @return coord y
	 */
	public double getY() {
		return y;
	}

	/**
	 * mutateur de la coord Y
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * calculateur de distance entre positions
	 * @param pos position à comparer
	 * @return distance euclédienne
	 */
	public double distance(Position pos) {
		return Math.sqrt(Math.pow(this.x - pos.x,2)+
				         Math.pow(this.y - pos.y,2));
		
	}
	
	/**
	 * retourne une représentation chaîne de caractères de la position
	 */
	public String toString() {
		return "("+this.x+","+this.y+")";
	}
	
}
