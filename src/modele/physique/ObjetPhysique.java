package modele.physique;

/**
 * Classe abstraite d�finissant un objet physique
 * 
 * L'objet physique poss�de une position.
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */
public abstract class ObjetPhysique {

	protected Position position;
	
	/**
	 * constructeur par param�tre
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
