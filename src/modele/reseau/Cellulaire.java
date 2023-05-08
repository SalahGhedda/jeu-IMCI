package modele.reseau;

/**
 * Classe définissant un cellulaire dans le jeu. Un cellulaire peut être criminel
 * ou non.
 * 
 * Le cellulaire se déplace, lance des appels et répond aux appels. 
 * Lorsqu'il est connecté, il échange des messages.
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */
import java.util.Random;

import modele.communication.Message;
import modele.gestionnaires.GestionnaireScenario;
import modele.physique.ObjetMobile;
import modele.physique.ObjetPhysique;
import modele.physique.Position;

public class Cellulaire extends ObjetMobile implements UniteCellulaire {

	private static final int NON_CONNECTE = -1;
	private static final double PROB_APPELER = 0.05;
	private static final double PROB_ENVOI_MSG = 0.2;
	private static final double PROB_DECONNEXION = 0.1;

	private boolean silencieux = true;
	
	String numeroLocal;
	
	int numeroConnexion = NON_CONNECTE;
	String numeroConnecte = null;
	
	Antenne antenneConnecte;
	
	Random rand = new Random();
	
	boolean criminel = false;

	private GestionnaireReseau reseau = GestionnaireReseau.getInstance();
	private GestionnaireScenario scenario = GestionnaireScenario.getInstance();

	/**
	 * Constructeur par paramètre, version permettant d'initialiser
	 * un cellulaire non-criminel
	 * @param numeroLocal numéro du cellulaire
	 * @param position initiale du cellulaire
	 * @param vitesse de déplacement du cellulaire
	 * @param standardDev variation de la direction
	 */
	public Cellulaire(String numeroLocal, Position position, 
			          double vitesse, double standardDev) {
		super(position, vitesse, standardDev);
		this.numeroLocal = numeroLocal;

		// ajouté au moment d'implémenter les connexions aux antennes
		antenneConnecte = reseau.getAntennePlusProche(position);
		antenneConnecte.enregistrer(this);
	}
	
	/**
	 * Constructeur par paramètre, version permettant d'initialiser
	 * un cellulaire criminel
	 * @param numeroLocal numéro du cellulaire
	 * @param position initiale du cellulaire
	 * @param vitesse de déplacement du cellulaire
	 * @param standardDev variation de la direction
	 * @param criminel indicateur qu'il s'agit d'un criminel (optionnel)
	 */
	public Cellulaire(String numeroLocal, Position position, 
			          double vitesse, double standardDev,
			          boolean criminel) {
		super(position, vitesse, standardDev);
		this.numeroLocal = numeroLocal;
		this.criminel = criminel;

		// ajouté au moment d'implémenter les connexions aux antennes
		antenneConnecte = reseau.getAntennePlusProche(position);
		antenneConnecte.enregistrer(this);
	}
	
	/**
	 * informateur pour déterminer si cellulaire est criminel
	 * @return vrai si criminel
	 */
	public boolean estCriminel() {
		return this.criminel;
	}

	/**
	 * informateur sur le numéro local
	 * @return numéro local
	 */
	public String getNumeroLocal() {
		return numeroLocal;
	}

	/**
	 * informateur sur le numéro de connexion
	 * @return numéro de connexion
	 */
	public int getNumeroConnexion() {
		return numeroConnexion;
	}

	/**
	 * Accesseur permettant de savoir si le cellulaire est connecté
	 * @return vrai si connecté, faux sinon
	 */
	public boolean estConnecte() {
		return numeroConnexion != NON_CONNECTE;
	}

	/**
	 * Méthode permettant de comparer un numéro pour déterminer si ce cellulaire
	 * est celui que l'on cherche
	 * @param numeroCherche numéro à comparer
	 * @return vrai si numéro correspond
	 */
	public boolean comparerNumero(String numeroCherche) {
		return this.numeroLocal.equals(numeroCherche); 
	}

	/**
	 * Méthode pour appeler d'un cellulaire part antenne-réseau
	 * @param numeroAppele numéro du cellulaire appelé
	 * @param numeroAppelant numéro du cellulaire appelant
	 * @param antenneConnecte référence à l'antenne connecté
	 * @return numéro de connexion
	 */
	@Override
	public int appeler(String numeroAppele, String numeroAppelant, Antenne antenneConnecte) {
		return antenneConnecte.appeler(numeroAppele, numeroAppelant, antenneConnecte);
	}


	/**
	 * Méthode pour répondre à un appel du réseau-antenne
	 * @param numeroAppele numéro du cellulaire appelé
	 * @param numeroAppelant numéro du cellulaire appelant
	 * @param numeroConnexion numéro de la connexion à mettre à jour
	 * @return référence au cellulaire courant, s'il répond à l'appel, null sinon
	 */
	@Override
	public Cellulaire repondre(String numeroAppele, String numeroAppelant, int numeroConnexion) {

		if(this.numeroConnexion == NON_CONNECTE) {
			this.numeroConnecte = numeroAppelant;
			this.numeroConnexion = numeroConnexion;
			return this;
		}
		return null;
	}


	/**
	 * Méthode pour initier une fin d'appel
	 * @param numeroAppele numéro du cellulaire appelé
	 * @param numeroConnexion numéro de la connexion à mettre à jour
	 */
	@Override
	public void finAppelLocal(String numeroAppele, int numeroConnexion) {
		
		// propager
		antenneConnecte.finAppelLocal(numeroAppele, numeroConnexion);
		
		this.numeroConnexion = NON_CONNECTE;
		this.numeroConnecte = null;
	}

	/**
	 * Méthode pour traiter une requête de fin d'appel
	 * @param numeroAppele numéro du cellulaire appelé
	 * @param numeroConnexion numéro de la connexion à mettre à jour
	 */
	@Override
	public void finAppelDistant(String numeroAppele, int numeroConnexion) {
		
		this.numeroConnexion = NON_CONNECTE;
		this.numeroConnecte = null;
	}
	

	/**
	 * Méthode pour envoyer un message vers antenne-réseau
	 * @param msg Message à transmettre
	 * @param numeroConnexion numéro de la connexion à mettre à jour
	 */
	@Override
	public void envoyer(Message msg, int numeroConnexion) {
		
		// doit utiliser connexion
		if(!silencieux) {
			System.out.println(numeroLocal + ": envoi de message, sur connexion: " + numeroConnexion);
		}
		// envoi le message, avec le numéro de connexion
		// antenne 
		antenneConnecte.envoyer(msg, numeroConnexion);
	}

	/**
	 * Méthode pour recevoir un message de réseau-antenne
	 * @param msg Message à recevoir
	 */
	@Override
	public void recevoir(Message message) {
	
		// silencieux pour affiche les messages à l'écran ou non
		if(!silencieux) {
			System.out.println(numeroLocal + ": message reçu: " +message.getMessage());
		}	
	}


	/**
	 * Méthode permettant de simuler un tour
	 */
	public void effectuerTour() {

		// si pas connecté, se déplace
		if(this.numeroConnexion == NON_CONNECTE) {
			this.seDeplacer();
		}
		
		// évalue la distance des tours
		Antenne antennePlusProche = reseau.getAntennePlusProche(position);

		// si une tour est plus proche
		if(antenneConnecte != antennePlusProche) {
			
			if(!silencieux) {
				System.out.println("changement d'antenne");
			}
			
			// change d'antenne
			antenneConnecte.desenregistrer(this);
			
			if(this.numeroConnexion != NON_CONNECTE) {
				antennePlusProche.mettreAJourConnexion(this.numeroConnexion, antenneConnecte);
			}
			
			antennePlusProche.enregistrer(this);
			
			// mettre à jours connexion
			antenneConnecte = antennePlusProche;
		}
		
		// si conversation en cours, poursuit
		if(numeroConnexion != NON_CONNECTE) {
			// probabilité de raccrocher
			if(rand.nextDouble() < PROB_ENVOI_MSG){
				Message msg = new Message(numeroConnecte, scenario.obtenirMessage(numeroLocal));
				this.envoyer(msg, numeroConnexion);
			}else if(rand.nextDouble() < PROB_DECONNEXION){
				finAppelLocal(numeroConnecte, numeroConnexion);
			}
			
		// sinon, probabilité de démarrer une conversation
		}else if(rand.nextDouble() < PROB_APPELER){
			if(criminel) {
				this.numeroConnecte = scenario.obtenirNumeroCriminelAlea(this.numeroLocal);
			}else {
				this.numeroConnecte = scenario.obtenirNumeroStandardAlea(this.numeroLocal);
			}
			this.numeroConnexion = appeler(this.numeroConnecte,this.numeroLocal, this.antenneConnecte);
			if(this.numeroConnexion == NON_CONNECTE) {
				this.numeroConnecte = null;
			}
		}
			
	}

	public Antenne getAntenneConnecte() {
		return this.antenneConnecte;
	}
	
}
