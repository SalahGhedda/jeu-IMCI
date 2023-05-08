package vue;

/**
 * Panneau interface d'utilisateur qui va contenir tous les panneaux d'informations nécessaires
 * pour le joueur
 *
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */


import javax.swing.JPanel;
import java.awt.*;

public class PanneauInterface extends JPanel
{
    //hauteur du panneau interface équivaut au un huitieme de l'ecran
    private static final int hateurPanneauInterface = Toolkit.getDefaultToolkit().getScreenSize().height/8;

    //dimention du panneau interface
    public static final Dimension tailleInterface = new Dimension(0, hateurPanneauInterface); //ignorer la largeur(BorderLayout)

    //Les panneaux information que le panneau interface va contenir
    PanneauInfoReseau panneauInfoReseau;
    PanneauInfoJeu panneauInfoJeu;
    PanneauInfoOperation panneauInfoOperation;

    /**
     * Constructeur du PanneauInterface
     */
    public PanneauInterface()
    {
        // on définit le Layout du panneau interface
        this.setLayout(new GridLayout()); //ce Layout va permettre aux panneaux info d'occuper tout l'espace diponible équitablement

        // définir la taille et la couleur du panneau interface
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(tailleInterface);

        initPanneauxInfos();

    }

    /**
     * initialise les panneaux d'informations et les ajoute au panneau interface
     */
    private void initPanneauxInfos()
    {
        panneauInfoReseau = new PanneauInfoReseau();
        this.add(panneauInfoReseau);

        panneauInfoJeu = new PanneauInfoJeu();
        this.add(panneauInfoJeu);

        panneauInfoOperation = new PanneauInfoOperation();
        this.add(panneauInfoOperation);
    }



}
