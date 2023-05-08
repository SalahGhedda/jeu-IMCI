package modele.gestionnaires;

/**
 * Le gestionnaire de scénario est un module utilitaire gérant:
 * 	* la création de numéro de téléphone
 *  * les messages
 *  
 *  Les fonctionnalités sont offertes pour les numéros normales et les numéros
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
	 * Implémentation Singleton
	 *-------------------- --------
	 */

	// instance (implémentation singleton)
	private static GestionnaireScenario instance = new GestionnaireScenario();

	// constructeur privée (implémentation singleton)
	public static GestionnaireScenario getInstance() {
		return instance;
	}

	// accesseur instance (implémentation singleton)
	private GestionnaireScenario() {
		
		// charge les conversations
		chargementDuFichier();
		nbConversationsInitial = file.getNbElements();
		
	}

	/**
	 * Méthode permettant d'attacher le réseau
	 * @param reseau
	 */
	public void attacherReseau(GestionnaireReseau reseau) {
		this.reseau= reseau; 
	}
	
	/**
	 * Accesseur sur la progression dans le scénario, référencé à 1.0
	 * @return progression 0-1.0
	 */
	public double getProgression() {
		return (1.0-(double)file.getNbElements()/instance.nbConversationsInitial);
	}

	/**
	 * méthode qui charge le fichier de conversation
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
	 * Méthode utilitaire pour générer une chaîne de caractères aléatoire
	 * @return String aléatoire
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
	 * Méthode retournant un message. Le message renvoyé dépend de 
	 * si le numéro est standard ou criminel. Si criminel, le message est tiré
	 * du scénario. Si standard, chaine de caractère aléatoire
	 * @param numero utilisé pour envoyer le message
	 * @return le message à envoyer
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
	 * Méthode qui retourne un numéro choisi aléatoirement parmis la
	 * liste des numéros criminels, à l'exception de celui reçu en
	 * @return le numéro appartenant aux numéros criminels
	 */
	public String obtenirNumeroCriminelAlea()
	{
		int index = rand.nextInt(numeroCriminel.size());
		return numeroCriminel.get(index);
	}
	
	/**
	 * méthode permettant de tester si un numéro est criminel
	 * @param numero à tester
	 * @return vrai si criminel
	 */
	public boolean numeroEstCriminel(String numero) {
		return numeroCriminel.contains(numero);
	}

	/**
	 * Méthode qui retourne un numéro choisi aléatoirement parmis la
	 * liste des numéros standard, à l'exception de celui reçu en
	 * paramètre
	 * @param exclus le numéro a exclure des possibilités
	 * @return le numéro appartenant aux numéros standards
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
	 * Méthode qui retourne un numéro de téléphone aléatoire
	 * après l'avoir ajouté à la liste des numéros criminels
	 * @return le numéro sous forme the String
	 */
	public String obtenirNouveauNumeroCriminel() {
		String numero = obtenirNouveauNumeroAlea();
		numeroCriminel.add(numero);
		return numero;
	}

	/**
	 * Méthode qui retourne un numéro de téléphone aléatoire
	 * après l'avoir ajouté à la liste des numéros standards
	 * @return le numéro sous forme the String
	 */
	public String obtenirNouveauNumeroStandard() {
		String numero = obtenirNouveauNumeroAlea();
		numeroStandard.add(numero);
		return numero;
	}

	/**
	 * Méthode qui construit un numéro de téléphone aléatoire
	 * avec un préfix constant, tel que PPP-XXX-YYYY
	 * @return le numéro sous forme the String
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
