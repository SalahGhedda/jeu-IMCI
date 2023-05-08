package modele.gestionnaires;

/**
 * Le gestionnaire de sc�nario est un module utilitaire g�rant:
 * 	* la cr�ation de num�ro de t�l�phone
 *  * les messages
 *  
 *  Les fonctionnalit�s sont offertes pour les num�ros normales et les num�ros
 *  de criminels.
 *  
 *  @author Fred Simard | ETS
 *  @revision hiver 2021
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

import modele.reseau.GestionnaireReseau;
import observer.MonObservable;
import tda.FileSChainee;

public class GestionnaireScenario extends MonObservable{

	public static final String PREFIX = "514-";
	
	GestionnaireReseau reseau;
	FileSChainee<String> file = new FileSChainee<String>();
	ArrayList<String> numeroCriminel = new ArrayList<String>(GestionnaireReseau.NB_CRIMINELS);
	ArrayList<String> numeroStandard = new ArrayList<String>(GestionnaireReseau.NB_CELLULAIRES);

    Random rand = new Random();
    
    int nbConversationsInitial = 0;

	/*----------------------------
	 * Impl�mentation Singleton
	 *-------------------- --------
	 */

	// instance (impl�mentation singleton)
	private static GestionnaireScenario instance = new GestionnaireScenario();

	// constructeur priv�e (impl�mentation singleton)
	public static GestionnaireScenario getInstance() {
		return instance;
	}

	// accesseur instance (impl�mentation singleton)
	private GestionnaireScenario() {
		
		// charge les conversations
		chargementDuFichier();
		nbConversationsInitial = file.getNbElements();
		
	}

	/**
	 * M�thode permettant d'attacher le r�seau
	 * @param reseau
	 */
	public void attacherReseau(GestionnaireReseau reseau) {
		this.reseau= reseau; 
	}
	
	/**
	 * Accesseur sur la progression dans le sc�nario, r�f�renc� � 1.0
	 * @return progression 0-1.0
	 */
	public double getProgression() {
		return (1.0-(double)file.getNbElements()/instance.nbConversationsInitial);
	}

	/**
	 * m�thode qui charge le fichier de conversation
	 */
	private void chargementDuFichier() {
		
		try {
			Scanner scanner = new Scanner(new File("TP3/ressources/conversations.txt"));
			while (scanner.hasNextLine()) {
				file.enfiler(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * M�thode utilitaire pour g�n�rer une cha�ne de caract�res al�atoire
	 * @return String al�atoire
	 * @ref: https://www.baeldung.com/java-random-string
	 */
	private String generatingRandomAlphabeticString() {
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = rand.nextInt(100)+1;

	    String generatedString = rand.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}

	/**
	 * M�thode retournant un message. Le message renvoy� d�pend de 
	 * si le num�ro est standard ou criminel. Si criminel, le message est tir�
	 * du sc�nario. Si standard, chaine de caract�re al�atoire
	 * @param numero utilis� pour envoyer le message
	 * @return le message � envoyer
	 */
	public String obtenirMessage(String numero) {
		
		if(numeroCriminel.contains(numero)) {
			try {
				String msg = file.defiler();
				avertirLesObservers();
				return msg;
			}
			catch(Exception exception){
				reseau.setMondeEnVie(false);

				JOptionPane.showMessageDialog(null, "Fin du jeu");
				System.exit(0);
				
			}
		} else {
			return generatingRandomAlphabeticString();
		}

		return null;
	}

	/**
	 * M�thode qui retourne un num�ro choisi al�atoirement parmis la
	 * liste des num�ros criminels, � l'exception de celui re�u en
	 * @return le num�ro appartenant aux num�ros criminels
	 */
	public String obtenirNumeroCriminelAlea()
	{
		int index = rand.nextInt(numeroCriminel.size());
		return numeroCriminel.get(index);
	}
	
	/**
	 * m�thode permettant de tester si un num�ro est criminel
	 * @param numero � tester
	 * @return vrai si criminel
	 */
	public boolean numeroEstCriminel(String numero) {
		return numeroCriminel.contains(numero);
	}

	/**
	 * M�thode qui retourne un num�ro choisi al�atoirement parmis la
	 * liste des num�ros standard, � l'exception de celui re�u en
	 * param�tre
	 * @param exclus le num�ro a exclure des possibilit�s
	 * @return le num�ro appartenant aux num�ros standards
	 */
	public String obtenirNumeroCriminelAlea(String exclus) {
		int index = rand.nextInt(numeroCriminel.size());
		String numero = numeroCriminel.get(index);
		
		while(numero.equals(exclus)){
			index = rand.nextInt(numeroCriminel.size());
			numero = numeroCriminel.get(index);
		}
		return numero;
	}

	
	public String obtenirNumeroStandardAlea(String exclus) {
		int index = rand.nextInt(numeroStandard.size());
		String numero = numeroStandard.get(index);
		
		while(numero.equals(exclus)){
			index = rand.nextInt(numeroStandard.size());
			numero = numeroStandard.get(index);
		}
		return numero;
	}

	/**
	 * M�thode qui retourne un num�ro de t�l�phone al�atoire
	 * apr�s l'avoir ajout� � la liste des num�ros criminels
	 * @return le num�ro sous forme the String
	 */
	public String obtenirNouveauNumeroCriminel() {
		String numero = obtenirNouveauNumeroAlea();
		numeroCriminel.add(numero);
		return numero;
	}

	/**
	 * M�thode qui retourne un num�ro de t�l�phone al�atoire
	 * apr�s l'avoir ajout� � la liste des num�ros standards
	 * @return le num�ro sous forme the String
	 */
	public String obtenirNouveauNumeroStandard() {
		String numero = obtenirNouveauNumeroAlea();
		numeroStandard.add(numero);
		return numero;
	}

	/**
	 * M�thode qui construit un num�ro de t�l�phone al�atoire
	 * avec un pr�fix constant, tel que PPP-XXX-YYYY
	 * @return le num�ro sous forme the String
	 */
	private String obtenirNouveauNumeroAlea() {
		
		String numero = PREFIX;
		for(int i=0;i<3;i++) {
			numero += rand.nextInt(10);
		}
		
		numero += "-";
		
		for(int i=0;i<4;i++) {
			numero += rand.nextInt(10);
		}
		
		return numero;
		
	}

}
