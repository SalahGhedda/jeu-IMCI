package vue;

/**
 * Cadre principal qui contient deux panneaux, l'interface de contr�le en bande PAGE_START
 * et le panneau de dessin CENTER qui est int�gr� dans un JScrollPane
 * 
 * impl�mente Runnable
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

@SuppressWarnings("serial")
public class CadrePrincipal extends JFrame implements Runnable{

	PanneauInterface panneauInterface;
	PanneauDessin panneauDessin;

	Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * T�che qui initialise le cadre
	 */
	@Override
	public void run() {
		
		initCadre();
		initPanneau();
		initPanneauInterface();

    	setVisible(true);
	}
	
	/**
	 * M�thode qui initialise le cadre
	 */
	private void initCadre() {

    	// maximize la fenêtre
    	setExtendedState(JFrame.MAXIMIZED_BOTH);
    	
    	// ajoute une gestion du EXIT par confirmation pop-up
		this.addWindowListener(new WindowAdapter() {
		      
			// gestionnaire d'événement pour la fermeture
			public void windowClosing(WindowEvent we) {
				
				// ajoute une demande de confirmation
		        int result = JOptionPane.showConfirmDialog(null,
		            "Voulez-vous quitter?", "Confirmation de sortie: ",
		            JOptionPane.YES_NO_OPTION);
		        
		        // si la réponse est oui
		        if (result == JOptionPane.YES_OPTION){
		        	// ferme la fenêtre en activant la gestion de l'événement
		        	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        }
		        else if (result == JOptionPane.NO_OPTION){
		        	// sinon, désactive la gestion de l'événement
		        	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		        }
		      }
		});
		
	}

	/**
	 * initialisation du contenu des deux panneaux PAGE_START et CENTER
	 */
	private void initPanneau()
	{
		panneauDessin = new PanneauDessin(tailleEcran);
		initScrollBars();
	}

	/**
	 * initialisation du contenue panneau d'interface utilisateur
	 */
	private void initPanneauInterface()
	{
		panneauInterface = new PanneauInterface();
		this.add(panneauInterface, BorderLayout.NORTH); //ajouter le panneau interface au nord du cadre principal
	}

	/**
	 * initialise les barres de defilement du panneau dessin
	 */
	private void initScrollBars()
	{
		//on instancie les barres de defilement et on l'ajoute au cadre principal
		JScrollPane scrollBarVerticale = new JScrollPane(panneauDessin);
		this.add(scrollBarVerticale);
	}


}
