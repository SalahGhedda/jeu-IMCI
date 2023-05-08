package modele.communication;

/**
 * Message échangé entre les Cellulaire
 * @author Fred Simard | ETS
 * @version H21
 */

public class Message {

	private String numeroDest;
	private String message;
	
	/**
	 * constructeur par paramètre
	 * @param numeroDest numéro du destinataire
	 * @param message message échangé
	 */
	public Message(String numeroDest, String message) {
		this.numeroDest = numeroDest;
		this.message = message;
	}
	
	/**
	 * accesseur numéro destinataire
	 * @return numéro destinataire
	 */
	public String getNumeroDest() {
		return numeroDest;
	}

	/**
	 * accesseur message
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
}
