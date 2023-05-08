package modele.physique;

/**
 * Classe abstraite définissant un objet physique
 * 
 * L'objet physique possède une position.
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */
public abstract class ObjetPhysique {

	protected Position position;
	
	/**
	 * constructeur par paramètre
	 * @param position
	 */
	public ObjetPhysique(Position position) {
		this.position = position;
	}

	/**
	 * accesseur de la position
	 * @return position
	 */
	public Position getPosition() {
		return position;
	}

}
