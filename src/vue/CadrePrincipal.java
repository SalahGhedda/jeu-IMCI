package vue;

/**
 * Cadre principal qui contient deux panneaux, l'interface de contrôle en bande PAGE_START
 * et le panneau de dessin CENTER qui est intégré dans un JScrollPane
 * 
 * implémente Runnable
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
	 * Tâche qui initialise le cadre
	 */
	@Override
	public void run() {
		
		initCadre();
		initPanneau();
		initPanneauInterface();

    	setVisible(true);
	}
	
	/**
	 * Méthode qui initialise le cadre
	 */
	private void initCadre() {

    	// maximize la fenÃªtre
    	setExtendedState(JFrame.MAXIMIZED_BOTH);
    	
    	// ajoute une gestion du EXIT par confirmation pop-up
		this.addWindowListener(new WindowAdapter() {
		      
			// gestionnaire d'Ã©vÃ©nement pour la fermeture
			public void windowClosing(WindowEvent we) {
				
				// ajoute une demande de confirmation
		        int result = JOptionPane.showConfirmDialog(null,
		            "Voulez-vous quitter?", "Confirmation de sortie: ",
		            JOptionPane.YES_NO_OPTION);
		        
		        // si la rÃ©ponse est oui
		        if (result == JOptionPane.YES_OPTION){
		        	// ferme la fenÃªtre en activant la gestion de l'Ã©vÃ©nement
		        	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        }
		        else if (result == JOptionPane.NO_OPTION){
		        	// sinon, dÃ©sactive la gestion de l'Ã©vÃ©nement
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
