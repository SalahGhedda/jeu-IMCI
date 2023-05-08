package modele.reseau;

/**
 * Gestionnaire du r�seau, s'occupe du r�seau cellulaire haut niveau
 * relaye les communications entre antennes et g�re les connexions
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
	 * Impl�mentation Singleton
	 *-------------------- --------
	 */

	// instance (impl�mentation singleton)
	private static GestionnaireReseau instance = new GestionnaireReseau();

	// constructeur priv�e (impl�mentation singleton)
	public static GestionnaireReseau getInstance() {
		return instance;
	}

	// accesseur instance (impl�mentation singleton)
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
	 * informatrice comme quoi le jeu est en ex�cution
	 * @return vrai si monde est en vie
	 */
	public boolean mondeEstEnVie() {
		return mondeEnVie;
	}

	/**
	 * mutatrice sur jeu est en ex�cution
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
	 * m�thode permettant d'obtenir l'antenne la plus proche
	 * @param position position de r�f�rence
	 * @return r�f�rence � l'antenne
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
	 * relaye un appel, de l'appelant vers l'appel� et retourne un bool�en indiquant
	 * si l'appel a �t� accept�.
	 * @param numeroAppele num�ro source
	 * @param numeroAppelant num�ro appel�
	 * @param antenneAppelant Antenne appelant
	 * @return num�ro de connexion ou CODE_NON_CONNECTE si pas de connect�
	 */
	public int relayerAppel(String numeroAppele, String numeroAppelant, Antenne antenneAppelant) {

		// obtient un nouveau num�ro de connexion
		int numeroConnexion = getNouveauNumeroConnexion();
		
		// parcour les antennes � la recherche de la destination
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
	 * M�thode qui relaye un message d'une antenne � l'autre
	 * @param msg message �chang�
	 * @param numeroConnexion num�ro de connexion � utiliser
	 * @param antenneSrc r�f�rence � l'antenne source
	 */
	public void relayerMessage(Message msg, int numeroConnexion, Antenne antenneSrc) {
		
		// utilise num�ro de connexion pour identifier antenne destination
		try {
			Antenne antenneDest = getConnexionAvecNumero(numeroConnexion).getAntenneDest(antenneSrc);
			antenneDest.recevoir(msg);
		}catch (Exception e) {
			System.out.println(msg.getNumeroDest() + e.getMessage());
		}
		
	}

	/**
	 * relaye une commande de fin d'appel
	 * @param numeroAppele num�ro du cellulaire appel�
	 * @param numeroConnexion num�ro du cellulaire appelant
	 * @param antenneSrc r�f�rence � l'antenne source
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
	 * M�thode permettant de mettre � jours une connexion, lors d'un changement d'antenne
	 * @param numeroConnexion num�ro de la connexion � modifier
	 * @param ancienneAntenne r�f�rence � l'ancienne antenne
	 * @param nouvelleAntenne r�f�rence � la nouvelle antenne
	 */
	public void mettreAJourConnexion(int numeroConnexion, Antenne ancienneAntenne, Antenne nouvelleAntenne){
		try {
			getConnexionAvecNumero(numeroConnexion).miseAJourAntenne(ancienneAntenne, nouvelleAntenne);
		}catch (Exception e) {
			System.out.println("mettreAJourConnexion" + e.getMessage());
		}
	}

	/**
	 * M�thode permettant d'initialiser la population d'antennes
	 */
	private void creeAntennes() {

		for(int i=0;i<NB_ANTENNES;i++) {
			antennes.add(new Antenne(Carte.getPositionAlea()));			
		}
	}

	/**
	 * M�thode permettant d'initialiser la population de cellulaires
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
		
		// cr�e les �l�ments
		creeAntennes();
		creeCellulaires();
		this.avertirLesObservers();

		// boucle simulant le syst�me
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
		
		throw new Exception("Erreur la connexion a �t� ferm�");
	}

	public Connexion enleverConnexionAvecNumero(int numeroConnexion) throws Exception{
		Connexion connexion = getConnexionAvecNumero(numeroConnexion);
		connexions.remove(connexion);
		return connexion;
	}

	/**
	 * Ajoute un intercepteur d'ondes � la collection des intercepteurs d'ondes
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
	 * M�thode qui verifie si la position du clique est comprise entre le centre de l'intercepteur d'onde et son rayon
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
	 *V�rifie si un IMSI a �t� cliqu� ou non
	 *
	 * @param e evenement lors du clique de souris
	 * @return true si l'utilisateur clique sur un IMSI , false dans le cas contraire
	 */
	public boolean presenceIMSI(MouseEvent e)
	{
		//on parcourt toutes les elements dans la collection d'intercepteurs d'ondes
		for (IntercepteurOndes intercepteurOndes : intercepteursOndes)
		{
			//verfie si la position cliqu� est dans le rayon de de l'intercepteur d'ondes
			if (rangeDuIMSI(e, intercepteurOndes))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 *Methode retournant l'Intercepteur d'ondes cliqu�. Si aucun intercepteur d'ondes
	 * n'est cliqu�,la m�thode retourne null
	 *
	 * @param e evenement lors du clique de souris
	 * @return l'instance de l'intercepteurOndes cliqu�, sinon retourne null
	 */
	public IntercepteurOndes getIMSIClique(MouseEvent e)
	{
		//on parcourt toutes les elements dans la collection d'intercepteurs d'ondes
		for (IntercepteurOndes intercepteurOndes : intercepteursOndes)
		{
			//verfie si la position cliqu� est dans le rayon de de l'intercepteur d'ondes
			if (rangeDuIMSI(e, intercepteurOndes))
			{
				return intercepteurOndes; //return l'instance cliquee
			}
		}
		return null;
	}
}
