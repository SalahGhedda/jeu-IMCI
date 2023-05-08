package vue;

/**
 * Classe définissant le panneau contenant les informations d'operation
 *
 * Le panneau comporte les information concernant les unités placées et disponible
 * comporte aussi un button pour acheter des unitées
 *
 * @author Salah Eddine Ghedda
 * @version H2021
 */

import modele.gestionnaires.GestionnaireJeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanneauInfoOperation extends PanneauInfo implements ActionListener
{
    GestionnaireJeu jeu = GestionnaireJeu.getInstance();

    //Le texte présent au panneau
    private final String titreInfoOperation = "Operation IMSI"; // Titre du panneau info jeu
    private final String textNbImsiRestants = "Nombre d'IMSI restant : " +  jeu.getnbUnite();
    private final String textNbImsiPlacer = "Nombre d'IMSI placer : " + jeu.getNbUnitePlace();

    //met le texte dans un label
    private JLabel nbImsiRestants = new JLabel(textNbImsiRestants);
    private JLabel nbImsiPlacer = new JLabel(textNbImsiPlacer);

    //button d'achat IMSI
    private JButton button = new JButton("Acheter IMSI");

    //fenetre de message d'information
    private JOptionPane msgPointsInsuffisants = new JOptionPane();

    /**
     * constructeur du panneauInfoOperation
     */
    public PanneauInfoOperation()
    {
        super();
        initContenu();
        refreshPanneau();
    }

    /**
     * Initaile le contenue du panneau info operation
     */
    @Override
    public void initContenu()
    {
        initTitre(titreInfoOperation); //initialise le titre du panneau info operation

        nbImsiRestants.setForeground(Color.WHITE); //définit la couleur du texte
        nbImsiRestants.setAlignmentX(Box.CENTER_ALIGNMENT);// définit l'alignement en X du panneau
        nbImsiRestants.setFont(ConstantesVue.POLICE_ETIQUETTES); // définit la police
        this.add(nbImsiRestants);// ajoute le Label au panneau info jeu

        nbImsiPlacer.setForeground(Color.WHITE); //définit la couleur du texte
        nbImsiPlacer.setAlignmentX(Box.CENTER_ALIGNMENT); // définit l'alignement en X du panneau
        nbImsiPlacer.setFont(ConstantesVue.POLICE_ETIQUETTES); // définit la police
        this.add(nbImsiPlacer); // ajoute le Label au panneau info jeu

        button.setAlignmentX(Box.CENTER_ALIGNMENT); //définit l'alignement du button
        button.setFocusable(false);
        button.addActionListener(this);
        this.add(button);// ajoute le button au panneau info operation
    }


    /**
     * refraichit les information du panneau operation
     */
    @Override
    public void refreshPanneau()
    {
        Timer timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                nbImsiRestants.setText("Nombre d'IMSI restant : " +  jeu.getnbUnite());
                nbImsiPlacer.setText("Nombre d'IMSI placer : " + jeu.getNbUnitePlace());
            }
        });
        timer.start();
    }


    /**
     * methode effectue les changement necessaire quand le boutton est cliqué
     * acheter des unités si le joueurs a assez de points, afficher un message dans le cas
     * contraire
     *
     * @param e clique du boutton
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //verifier si le boutton est appuyé et que le joueur assez de points
        if(e.getSource()==button && GestionnaireJeu.COUT_IMSI <= jeu.getNbPoints())
        {
            jeu.acheterIMSI();
            jeu.incrementerUnite();
        }
        else {
            //afficher le message indiquant que le joueur n'a pas assez de points
            JOptionPane.showMessageDialog(null,"Vous n'avez pas assez de points pour acheter un nouvel IMSI", "Points Insuffisants", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
