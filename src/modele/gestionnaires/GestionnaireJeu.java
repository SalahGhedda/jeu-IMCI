package modele.gestionnaires;

/**
 * Gestionnaire du jeu, implémente les fonctionnalités associés aux règles de la partie
 * jeux, tel que l'achat de IMSI et le pointage.
 * 
 * Implémenté comme un singleton
 * 
 * @author Fred Simard | ETS
 * @version H21
 */

import modele.physique.Carte;
import modele.physique.Position;
import modele.reseau.Cellulaire;
import modele.reseau.GestionnaireReseau;
import modele.reseau.IntercepteurOndes;
import observer.MonObservable;

import java.util.ArrayList;

public class GestionnaireJeu extends MonObservable{

	GestionnaireScenario scenario = GestionnaireScenario.getInstance();

	// économie du jeu
	public static int NB_IMSI_DEPART = 1;
	public static int COUT_IMSI = 10;
	
	// attributs associés au IMSI
	private int nbUnite = NB_IMSI_DEPART;
	private int nbUnitePlace = 0;
	
	// pointage
	private int nbPoints = 0;

	// référence au gestionnaire réseau
	private GestionnaireReseau reseau;
	
	/*----------------------------
	 * Implémentation Singleton *-*
	 *-------------------- --------
	 */
	
	// instance (implémentation singleton)
	private static GestionnaireJeu instance = new GestionnaireJeu();

	// constructeur privée (implémentation singleton)
	private GestionnaireJeu()
	{
		reseau = GestionnaireReseau.getInstance();
		gagnerPoints();
	}

	// accesseur instance (implémentation singleton)
	public static GestionnaireJeu getInstance()
	{
		return instance;
	}

	/**
	 * accesseur nombre unite
	 * @return nb unite
	 */
	public int getnbUnite()
	{
		return this.nbUnite;
	}

	/**
	 * accesseur nombre unite place
	 * @return nb unite place
	 */
	public int getNbUnitePlace()
	{
		return nbUnitePlace;
	}

	/**
	 * accesseur nombre de points
	 * @return nb de points
	 */
	public int getNbPoints()
	{
		return nbPoints;
	}

	/**
	 * Incremente le nombre d'unite
	 */
	public void incrementerUnite()
	{
		nbUnite++;
	}

	/**
	 * decremente le nombre d'unite
	 */
	public void decrementerUnite()
	{
		nbUnite--;
	}

	/**
	 * Incremente le nombre d'unite placé
	 */
	public void incrementerUnitePlacer()
	{
		nbUnitePlace++;
	}

	/**
	 * decremente le nombre d'unite placé
	 */
	public void decrementerUnitePlacer()
	{
		nbUnitePlace--;
	}

	/**
	 * incremente le nombre d'unité placé et décremente le nombre d'unité
	 * quand une unité est placée
	 */
	public void apresPlacementUnite()
	{
		decrementerUnite();
		incrementerUnitePlacer();
	}

	/**
	 * decremente le nombre d'unité placé et incremente le nombre d'unité
	 * quand une unité est enlevée
	 */
	public void apresEnlevementUnite()
	{
		incrementerUnite();
		decrementerUnitePlacer();
	}

	/**
	 * incrémente le nombre de points lors d'un gain de point dans le jeu
	 */
	public void gagnerPoints()
	{
		nbPoints++;
	}

	/**
	 * Calcule le nombre de points restants apres l'achat d'un IMSI
	 */
	public void acheterIMSI()
	{
		nbPoints = nbPoints - COUT_IMSI;
	}




}
