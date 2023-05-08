package programme;

/**
 * Programme principal
 * @author Fred Simard | ETS
 * @version H21
 */

import javax.swing.SwingUtilities;

import modele.reseau.GestionnaireReseau;
import vue.CadrePrincipal;

public class ProgrammePrincipal {

	public static void main(String[] args){
		
    	Thread t2 = new Thread(GestionnaireReseau.getInstance());
    	t2.start();
		
    	SwingUtilities.invokeLater(new CadrePrincipal());
    	
	}
}
