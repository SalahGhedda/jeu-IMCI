package modele.reseau;

/**
 * Gestionnaire du réseau, s'occupe du réseau cellulaire haut niveau
 * relaye les communications entre antennes et gère les connexions
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import modele.communication.Connexion;
import modele.communication.Message;
import modele.gestionnaires.GestionnaireScenario;
import modele.physique.Carte;
import modele.physique.Position;
import observer.MonObservable;
import vue.PanneauDessin;

public class GestionnaireReseau extends MonObservable implements Runnable {

	public static final int PERIODE_SIMULATION_MS = 100;
	public static final double VITESSE = 10;
	public static final double DEVIATION_STANDARD = 0.05;
	public static final int NB_CELLULAIRES = 30;
	public static final int NB_CRIMINELS = 10;
	public static final int NB_ANTENNES = 10;
	public static final int CODE_NON_CONNECTE = -1;

	private static Random rand = new Random();
	
	
	private ArrayList<Antenne> antennes = new ArrayList<Antenne>();
	private ArrayList<Cellulaire> cellulaires = new ArrayList<Cellulaire>();
	private ArrayList<Connexion> connexions = new ArrayList<Connexion>();
	private ArrayList<IntercepteurOndes> intercepteursOndes = new ArrayList<IntercepteurOndes>();

	private boolean mondeEnVie = true;
	private GestionnaireScenario scenario = GestionnaireScenario.getInstance();

	/*----------------------------
	 * Implémentation Singleton
	 *-------------------- --------
	 */

	// instance (implémentation singleton)
	private static GestionnaireReseau instance = new GestionnaireReseau();

	// constructeur privée (implémentation singleton)
	public static GestionnaireReseau getInstance() {
		return instance;
	}

	// accesseur instance (implémentation singleton)
	private GestionnaireReseau() {
		scenario.attacherReseau(this);
	}
	
	/**
	 * informatrice sur le nombre de conversations qui prend place
	 * @return nombre de conversation
	 */
	public int getNbConversations() {
		return connexions.size();
	}

	/**
	 * informatrice liste de connexions
	 * @return liste de connexions
	 */
	public ArrayList<Connexion> getConnexions(){
		return connexions;
	}
	
	/**
	 * informatrice comme quoi le jeu est en exécution
	 * @return vrai si monde est en vie
	 */
	public boolean mondeEstEnVie() {
		return mondeEnVie;
	}

	/**
	 * mutatrice sur jeu est en exécution
	 * @param mondeEnVie
	 */
	public void setMondeEnVie(boolean mondeEnVie) {
		this.mondeEnVie = mondeEnVie;
	}

	/**
	 * informatrice liste de cellulaires
	 * @return liste de cellulaire
	 */
	public ArrayList<Cellulaire> getCellulaires(){
		return cellulaires;
	}

	/**
	 * informatrice liste d'antennes
	 * @return liste d'antenne
	 */
	public ArrayList<Antenne> getAntennes(){
		return antennes;
	}
	
	/**
	 * méthode permettant d'obtenir l'antenne la plus proche
	 * @param position position de référence
	 * @return référence à l'antenne
	 */
	public Antenne getAntennePlusProche(Position position) {
		
		// algorithme qui cherche la distance la plus petite
		double distMin = antennes.get(0).distance(position);
		Antenne antenneMin = antennes.get(0);
		
		for(Antenne antenne : antennes) {
			if(antenne.distance(position)<distMin){
				distMin = antenne.distance(position);
				antenneMin = antenne;
			}
		}
		
		return antenneMin;
	}

	/**
	 * relaye un appel, de l'appelant vers l'appelé et retourne un booléen indiquant
	 * si l'appel a été accepté.
	 * @param numeroAppele numéro source
	 * @param numeroAppelant numéro appelé
	 * @param antenneAppelant Antenne appelant
	 * @return numéro de connexion ou CODE_NON_CONNECTE si pas de connecté
	 */
	public int relayerAppel(String numeroAppele, String numeroAppelant, Antenne antenneAppelant) {

		// obtient un nouveau numéro de connexion
		int numeroConnexion = getNouveauNumeroConnexion();
		
		// parcour les antennes à la recherche de la destination
		for(Antenne antenne : antennes) {

			Cellulaire repondant = antenne.repondre(numeroAppele, numeroAppelant, numeroConnexion);
			
			if(repondant!=null) {
				ajouterConnexion(new Connexion(numeroConnexion, antenneAppelant, antenne));
				return numeroConnexion; // remplace par id unique
			}
		}
		
		return CODE_NON_CONNECTE; // remplace par CODE_NON_CONNECTE
	}
	
	/**
	 * Méthode qui relaye un message d'une antenne à l'autre
	 * @param msg message échangé
	 * @param numeroConnexion numéro de connexion à utiliser
	 * @param antenneSrc référence à l'antenne source
	 */
	public void relayerMessage(Message msg, int numeroConnexion, Antenne antenneSrc) {
		
		// utilise numéro de connexion pour identifier antenne destination
		try {
			Antenne antenneDest = getConnexionAvecNumero(numeroConnexion).getAntenneDest(antenneSrc);
			antenneDest.recevoir(msg);
		}catch (Exception e) {
			System.out.println(msg.getNumeroDest() + e.getMessage());
		}
		
	}

	/**
	 * relaye une commande de fin d'appel
	 * @param numeroAppele numéro du cellulaire appelé
	 * @param numeroConnexion numéro du cellulaire appelant
	 * @param antenneSrc référence à l'antenne source
	 */
	public void relayerFinAppel(String numeroAppele, int numeroConnexion, Antenne antenneSrc) {
		try {
			Connexion connexion = enleverConnexionAvecNumero(numeroConnexion);
			Antenne antenneDest = connexion.getAntenneDest(antenneSrc);
			antenneDest.finAppelDistant(numeroAppele, numeroConnexion);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Méthode permettant de mettre à jours une connexion, lors d'un changement d'antenne
	 * @param numeroConnexion numéro de la connexion à modifier
	 * @param ancienneAntenne référence à l'ancienne antenne
	 * @param nouvelleAntenne référence à la nouvelle antenne
	 */
	public void mettreAJourConnexion(int numeroConnexion, Antenne ancienneAntenne, Antenne nouvelleAntenne){
		try {
			getConnexionAvecNumero(numeroConnexion).miseAJourAntenne(ancienneAntenne, nouvelleAntenne);
		}catch (Exception e) {
			System.out.println("mettreAJourConnexion" + e.getMessage());
		}
	}

	/**
	 * Méthode permettant d'initialiser la population d'antennes
	 */
	private void creeAntennes() {

		for(int i=0;i<NB_ANTENNES;i++) {
			antennes.add(new Antenne(Carte.getPositionAlea()));			
		}
	}

	/**
	 * Méthode permettant d'initialiser la population de cellulaires
	 * normaux et cellulaires
	 */
	private void creeCellulaires() {
		for(int i=0;i<NB_CELLULAIRES;i++) {
			cellulaires.add(new Cellulaire(scenario.obtenirNouveauNumeroStandard(), 
					                       Carte.getPositionAlea(), VITESSE, 
					                       (2*Math.PI)*DEVIATION_STANDARD));
		}
		
		for(int i=0;i<NB_CRIMINELS;i++) {
			cellulaires.add(new Cellulaire(scenario.obtenirNouveauNumeroCriminel(), 
					                       Carte.getPositionAlea(), VITESSE, 
					                       (2*Math.PI)*DEVIATION_STANDARD, true));
		}
			
	}
	
	@Override
	public void run() {
		
		// crée les éléments
		creeAntennes();
		creeCellulaires();
		this.avertirLesObservers();

		// boucle simulant le système
		while(this.mondeEnVie) {	
			
			// simule les cellulaires
			for(Cellulaire cell : cellulaires) {
				cell.effectuerTour();
			}
			
			this.avertirLesObservers();
			
			// pause
			try {
				Thread.sleep(PERIODE_SIMULATION_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	

	public void ajouterConnexion(Connexion connexion)
	{
		connexions.add(connexion);
	}
	
	public int getNouveauNumeroConnexion() {
		return rand.nextInt(Integer.MAX_VALUE);
	}
	
	public Connexion getConnexionAvecNumero(int numero) throws Exception{
		
		for(Connexion connexion : connexions) {
			if(connexion.getNumeroConnexion()==numero) {
				return connexion;
			}
		}
		
		throw new Exception("Erreur la connexion a été fermé");
	}

	public Connexion enleverConnexionAvecNumero(int numeroConnexion) throws Exception{
		Connexion connexion = getConnexionAvecNumero(numeroConnexion);
		connexions.remove(connexion);
		return connexion;
	}

	/**
	 * Ajoute un intercepteur d'ondes à la collection des intercepteurs d'ondes
	 * @param intercepteurOndes un intercepteurs d'ondes
	 */

	public void ajouterIntercepteurOndes(IntercepteurOndes intercepteurOndes)
	{
		intercepteursOndes.add(intercepteurOndes);
	}

	/**
	 * Enleve un intercepteur d'ondes de la collection des intercepteurs d'ondes
	 * @param intercepteurOndes un intercepteurs d'ondes
	 */
	public void enleverIntercepteurOndes(IntercepteurOndes intercepteurOndes)
	{
		intercepteursOndes.remove(intercepteurOndes);
	}

	/**
	 * Méthode qui verifie si la position du clique est comprise entre le centre de l'intercepteur d'onde et son rayon
	 *
	 * @param e evenement lors du clique de souris
	 * @param intercepteurOndes un intercepteurs d'ondes
	 * @return
	 */
	private boolean rangeDuIMSI(MouseEvent e, IntercepteurOndes intercepteurOndes)
	{
		if(intercepteurOndes.getPosition().getX() <= e.getX() + PanneauDessin.RAYON_COLLISIONNEUR && intercepteurOndes.getPosition().getX() >= e.getX() - PanneauDessin.RAYON_COLLISIONNEUR
				&& intercepteurOndes.getPosition().getY() <= e.getY() + PanneauDessin.RAYON_COLLISIONNEUR && intercepteurOndes.getPosition().getY() >= e.getY() - PanneauDessin.RAYON_COLLISIONNEUR)
		{
			return true;
		}
		return false;
	}

	/**
	 *
	 *Vérifie si un IMSI a été cliqué ou non
	 *
	 * @param e evenement lors du clique de souris
	 * @return true si l'utilisateur clique sur un IMSI , false dans le cas contraire
	 */
	public boolean presenceIMSI(MouseEvent e)
	{
		//on parcourt toutes les elements dans la collection d'intercepteurs d'ondes
		for (IntercepteurOndes intercepteurOndes : intercepteursOndes)
		{
			//verfie si la position cliqué est dans le rayon de de l'intercepteur d'ondes
			if (rangeDuIMSI(e, intercepteurOndes))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 *Methode retournant l'Intercepteur d'ondes cliqué. Si aucun intercepteur d'ondes
	 * n'est cliqué,la méthode retourne null
	 *
	 * @param e evenement lors du clique de souris
	 * @return l'instance de l'intercepteurOndes cliqué, sinon retourne null
	 */
	public IntercepteurOndes getIMSIClique(MouseEvent e)
	{
		//on parcourt toutes les elements dans la collection d'intercepteurs d'ondes
		for (IntercepteurOndes intercepteurOndes : intercepteursOndes)
		{
			//verfie si la position cliqué est dans le rayon de de l'intercepteur d'ondes
			if (rangeDuIMSI(e, intercepteurOndes))
			{
				return intercepteurOndes; //return l'instance cliquee
			}
		}
		return null;
	}
}
