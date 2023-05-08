package vue;

/**
 * Panneau affichant le réseau cellulaire à l'écran et traitant les clics de souris.
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;

import modele.gestionnaires.GestionnaireJeu;
import modele.physique.Carte;
import modele.physique.Position;
import modele.reseau.Antenne;
import modele.reseau.Cellulaire;
import modele.reseau.GestionnaireReseau;
import modele.reseau.IntercepteurOndes;
import observer.MonObserver;

@SuppressWarnings("serial")
public class PanneauDessin extends JPanel implements MonObserver {

	public static final int RAYON_ANTENNE = 20;
	public static final int RAYON_INDIVIDU = 10;
	public static final int RAYON_COLLISIONNEUR = 20;
	
	Dimension taille = new Dimension();
	GestionnaireReseau reseau = GestionnaireReseau.getInstance();
	GestionnaireJeu jeu = GestionnaireJeu.getInstance();
	
	/**
	 * Constructeur du panneau
	 * @param taille du dessin
	 */
	public PanneauDessin(Dimension taille) {
		
		// initialise la taille du panneau
		this.taille.width = (int) Carte.DIMENSION_JEU.getX();
		this.taille.height = (int) Carte.DIMENSION_JEU.getY(); 
		this.setPreferredSize(this.taille);

		ajouterEnleverIMSI();
		initDessin();
		
		// s'attache au réseau
		reseau.attacherObserver(this);
		
	}
	
	/**
	 * initialise le dessin
	 */
	private void initDessin(){

		validate();
		repaint();		
	}
	
	/**
	 * Callback pour dessiner dans le panneau dessin
	 */
	public void paintComponent(Graphics g) {

		// convertie en engin graphique 2D
		Graphics2D g2 = (Graphics2D) g;
		
		// appel au paint de base
		super.paintComponent(g);
		// efface l'Ã©cran
		g2.clearRect(0, 0, taille.width, taille.height);
		
		dessineCellulaires(g2);
		dessineAntennes(g2);

        //gets rid of the copy
        g2.dispose();
	}
	
	/**
	 * Méthode pour dessiner les cellulaires à l'écran
	 * @param g engin graphique
	 */
	private void dessineCellulaires(Graphics g) {
		
		ArrayList<Cellulaire> cellulaires = reseau.getCellulaires();
		
		for(Cellulaire cellulaire : cellulaires) {
			
			Position position = cellulaire.getPosition();
			
			if(cellulaire.estCriminel()) {
				if(cellulaire.estConnecte()) {
					g.setColor(Color.RED);
				}else {
					g.setColor(Color.BLUE);
				}
				
			}else {
				g.setColor(Color.BLUE);
			}
			
			g.fillOval((int)position.getX()-RAYON_INDIVIDU, (int)position.getY()-RAYON_INDIVIDU, 2*RAYON_INDIVIDU, 2*RAYON_INDIVIDU);	

			// affiche les liens cellulaires-antenne (contribution: Mathieu Sévigny-Lavallée)
			g.drawLine((int)cellulaire.getPosition().getX(),(int)cellulaire.getPosition().getY(), (int)cellulaire.getAntenneConnecte().getPosition().getX(),(int)cellulaire.getAntenneConnecte().getPosition().getY());
			
		}
		
	}

	/**
	 * Méthode pour dessiner les antennes à l'écran
	 * @param g engin graphique
	 */
	private void dessineAntennes(Graphics g) {

		ArrayList<Antenne> antennes = reseau.getAntennes();
		
		for(Antenne antenne : antennes) {

			Position position = antenne.getPosition();

			//verfie si l'antenne est une instance d'intercepteurOndes
			if(antenne instanceof IntercepteurOndes)
			{
				g.setColor(Color.ORANGE);
				g.fillOval((int) position.getX() - RAYON_COLLISIONNEUR, (int) position.getY() - RAYON_COLLISIONNEUR, 2 * RAYON_COLLISIONNEUR, 2 * RAYON_COLLISIONNEUR);
			}
			else
			{
				g.setColor(Color.DARK_GRAY);
				g.fillOval((int)position.getX()-RAYON_ANTENNE, (int)position.getY()-RAYON_ANTENNE, 2*RAYON_ANTENNE, 2*RAYON_ANTENNE);
			}

		}
	}

	/**
	 * Méthode pour ajouter un IMSI sur la carte du jeu lors du clique de souris
	 * si le joueur en possede
	 */

	private void ajouterEnleverIMSI()
	{
		ArrayList<Antenne> antennes = reseau.getAntennes(); //on recupere la collection des antennes
		//Evenement lors du clique de souris
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				super.mousePressed(e);
				//verifie si le boutton gauche est cliqué et qu'il n'y a pas un IMSI deja present sur l'endroit cliqué
				//le joueur doit aussi avoir des unites de disponible
				if(e.getButton() == MouseEvent.BUTTON1 && !reseau.presenceIMSI(e) && jeu.getnbUnite() > 0)
				{
					Position postionClique = new Position(e.getX(), e.getY()); // on recupere la position cliqué
					IntercepteurOndes IMSI = new IntercepteurOndes(postionClique); //ajoute instancie un nouveau intercepteur onde avec la postion cliqué

					reseau.ajouterIntercepteurOndes(IMSI);//ajoute IMSI a la collection d'intecepteur d'ondes
					antennes.add(IMSI);//ajoute IMSI a la collection d'Antennes

					jeu.apresPlacementUnite();//on effectue les changement du nombres d'unites placés et disponibles
				}

				//verie si un IMSI a été cliqué avec le boutton gauche de la souris
				else if(e.getButton() == MouseEvent.BUTTON3 && reseau.presenceIMSI(e))
				{
					IntercepteurOndes IMSI = reseau.getIMSIClique(e);//on recupere l'intercepteur d'ondes cliqué

					//on retire l'instance recupéré des collections
					reseau.enleverIntercepteurOndes(IMSI);
					antennes.remove(IMSI);

					jeu.apresEnlevementUnite(); //on effectue les changement du nombres d'unites placés et disponibles
				}
			}
		});
	}


	/**
	 * Callback demandant de rafraîchir l'écran
	 */
	@Override
	public void avertir()
	{
		validate();
		repaint();		
	}
	
}
