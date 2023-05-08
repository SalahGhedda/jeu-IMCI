package modele.reseau;

/**
 * Interface permettant d'unifier la construction des éléments cellulaires du système.
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */
import modele.communication.Message;

public abstract interface UniteCellulaire {

	public int appeler(String numeroAppele, String numeroAppelant, Antenne antenneConnecte);
	Cellulaire repondre(String numeroAppele, String numeroAppelant, int numeroConnexion);

	public abstract void finAppelLocal(String numeroAppele, int numeroConnexion);
	public abstract void finAppelDistant(String numeroAppele, int numeroConnexion);
	
	public abstract void envoyer(Message msg, int numeroConnexion);
	public abstract void recevoir(Message msg);
	
}
