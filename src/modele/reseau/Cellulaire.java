package modele.reseau;

/**
 * Classe d�finissant un cellulaire dans le jeu. Un cellulaire peut �tre criminel
 * ou non.
 * 
 * Le cellulaire se d�place, lance des appels et r�pond aux appels. 
 * Lorsqu'il est connect�, il �change des messages.
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
	 * Constructeur par param�tre, version permettant d'initialiser
	 * un cellulaire non-criminel
	 * @param numeroLocal num�ro du cellulaire
	 * @param position initiale du cellulaire
	 * @param vitesse de d�placement du cellulaire
	 * @param standardDev variation de la direction
	 */
	public Cellulaire(String numeroLocal, Position position, 
			          double vitesse, double standardDev) {
		super(position, vitesse, standardDev);
		this.numeroLocal = numeroLocal;

		// ajout� au moment d'impl�menter les connexions aux antennes
		antenneConnecte = reseau.getAntennePlusProche(position);
		antenneConnecte.enregistrer(this);
	}
	
	/**
	 * Constructeur par param�tre, version permettant d'initialiser
	 * un cellulaire criminel
	 * @param numeroLocal num�ro du cellulaire
	 * @param position initiale du cellulaire
	 * @param vitesse de d�placement du cellulaire
	 * @param standardDev variation de la direction
	 * @param criminel indicateur qu'il s'agit d'un criminel (optionnel)
	 */
	public Cellulaire(String numeroLocal, Position position, 
			          double vitesse, double standardDev,
			          boolean criminel) {
		super(position, vitesse, standardDev);
		this.numeroLocal = numeroLocal;
		this.criminel = criminel;

		// ajout� au moment d'impl�menter les connexions aux antennes
		antenneConnecte = reseau.getAntennePlusProche(position);
		antenneConnecte.enregistrer(this);
	}
	
	/**
	 * informateur pour d�terminer si cellulaire est criminel
	 * @return vrai si criminel
	 */
	public boolean estCriminel() {
		return this.criminel;
	}

	/**
	 * informateur sur le num�ro local
	 * @return num�ro local
	 */
	public String getNumeroLocal() {
		return numeroLocal;
	}

	/**
	 * informateur sur le num�ro de connexion
	 * @return num�ro de connexion
	 */
	public int getNumeroConnexion() {
		return numeroConnexion;
	}

	/**
	 * Accesseur permettant de savoir si le cellulaire est connect�
	 * @return vrai si connect�, faux sinon
	 */
	public boolean estConnecte() {
		return numeroConnexion != NON_CONNECTE;
	}

	/**
	 * M�thode permettant de comparer un num�ro pour d�terminer si ce cellulaire
	 * est celui que l'on cherche
	 * @param numeroCherche num�ro � comparer
	 * @return vrai si num�ro correspond
	 */
	public boolean comparerNumero(String numeroCherche) {
		return this.numeroLocal.equals(numeroCherche); 
	}

	/**
	 * M�thode pour appeler d'un cellulaire part antenne-r�seau
	 * @param numeroAppele num�ro du cellulaire appel�
	 * @param numeroAppelant num�ro du cellulaire appelant
	 * @param antenneConnecte r�f�rence � l'antenne connect�
	 * @return num�ro de connexion
	 */
	@Override
	public int appeler(String numeroAppele, String numeroAppelant, Antenne antenneConnecte) {
		return antenneConnecte.appeler(numeroAppele, numeroAppelant, antenneConnecte);
	}


	/**
	 * M�thode pour r�pondre � un appel du r�seau-antenne
	 * @param numeroAppele num�ro du cellulaire appel�
	 * @param numeroAppelant num�ro du cellulaire appelant
	 * @param numeroConnexion num�ro de la connexion � mettre � jour
	 * @return r�f�rence au cellulaire courant, s'il r�pond � l'appel, null sinon
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
	 * M�thode pour initier une fin d'appel
	 * @param numeroAppele num�ro du cellulaire appel�
	 * @param numeroConnexion num�ro de la connexion � mettre � jour
	 */
	@Override
	public void finAppelLocal(String numeroAppele, int numeroConnexion) {
		
		// propager
		antenneConnecte.finAppelLocal(numeroAppele, numeroConnexion);
		
		this.numeroConnexion = NON_CONNECTE;
		this.numeroConnecte = null;
	}

	/**
	 * M�thode pour traiter une requ�te de fin d'appel
	 * @param numeroAppele num�ro du cellulaire appel�
	 * @param numeroConnexion num�ro de la connexion � mettre � jour
	 */
	@Override
	public void finAppelDistant(String numeroAppele, int numeroConnexion) {
		
		this.numeroConnexion = NON_CONNECTE;
		this.numeroConnecte = null;
	}
	

	/**
	 * M�thode pour envoyer un message vers antenne-r�seau
	 * @param msg Message � transmettre
	 * @param numeroConnexion num�ro de la connexion � mettre � jour
	 */
	@Override
	public void envoyer(Message msg, int numeroConnexion) {
		
		// doit utiliser connexion
		if(!silencieux) {
			System.out.println(numeroLocal + ": envoi de message, sur connexion: " + numeroConnexion);
		}
		// envoi le message, avec le num�ro de connexion
		// antenne 
		antenneConnecte.envoyer(msg, numeroConnexion);
	}

	/**
	 * M�thode pour recevoir un message de r�seau-antenne
	 * @param msg Message � recevoir
	 */
	@Override
	public void recevoir(Message message) {
	
		// silencieux pour affiche les messages � l'�cran ou non
		if(!silencieux) {
			System.out.println(numeroLocal + ": message re�u: " +message.getMessage());
		}	
	}


	/**
	 * M�thode permettant de simuler un tour
	 */
	public void effectuerTour() {

		// si pas connect�, se d�place
		if(this.numeroConnexion == NON_CONNECTE) {
			this.seDeplacer();
		}
		
		// �value la distance des tours
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
			
			// mettre � jours connexion
			antenneConnecte = antennePlusProche;
		}
		
		// si conversation en cours, poursuit
		if(numeroConnexion != NON_CONNECTE) {
			// probabilit� de raccrocher
			if(rand.nextDouble() < PROB_ENVOI_MSG){
				Message msg = new Message(numeroConnecte, scenario.obtenirMessage(numeroLocal));
				this.envoyer(msg, numeroConnexion);
			}else if(rand.nextDouble() < PROB_DECONNEXION){
				finAppelLocal(numeroConnecte, numeroConnexion);
			}
			
		// sinon, probabilit� de d�marrer une conversation
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
