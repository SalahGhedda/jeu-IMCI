package modele.communication;

/**
 * Message �chang� entre les Cellulaire
 * @author Fred Simard | ETS
 * @version H21
 */

public class Message {

	private String numeroDest;
	private String message;
	
	/**
	 * constructeur par param�tre
	 * @param numeroDest num�ro du destinataire
	 * @param message message �chang�
	 */
	public Message(String numeroDest, String message) {
		this.numeroDest = numeroDest;
		this.message = message;
	}
	
	/**
	 * accesseur num�ro destinataire
	 * @return num�ro destinataire
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
