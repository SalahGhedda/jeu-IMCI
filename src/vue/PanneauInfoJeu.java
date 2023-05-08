package vue;

/**
 * Classe définissant le panneau contenant les informations du jeu
 *
 * Le panneau comporte les informations concernant le nombre de criminels et non-criminels
 * sur le jeu ainsi le nombre de conversations en cours
 *
 * @author Salah Eddine Ghedda
 * @version H2021
 */

import modele.gestionnaires.GestionnaireJeu;
import modele.gestionnaires.GestionnaireScenario;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanneauInfoJeu extends PanneauInfo
{
    GestionnaireJeu jeu = GestionnaireJeu.getInstance();
    GestionnaireScenario scenario = GestionnaireScenario.getInstance();

    private final String titreInfoJeu = "Informations Jeu"; // Titre du panneau info jeu

    private JLabel pointage = new JLabel("Pointage : " + jeu.getNbPoints()); // info donnant le nombre de points
    private JLabel ecoulementTemps = new JLabel("Ecoulement du temps :");
    private JProgressBar barre = new JProgressBar();// barre progressive de l'écoulement du temps

    /**
     * constructeur du PanneauInfoJeu
     */
    public PanneauInfoJeu()
    {
        super();
        initContenu();
        refreshPanneau();
    }

    /**
     * Initaile le contenue du panneau info jeu
     */
    @Override
    public void initContenu()
    {
        initTitre(titreInfoJeu);//initialise le titre du panneau info jeu

        pointage.setForeground(Color.WHITE);//définit la couleur du texte
        pointage.setAlignmentX(Box.CENTER_ALIGNMENT);// définit l'alignement en X du panneau
        pointage.setFont(ConstantesVue.POLICE_ETIQUETTES); // définit la police
        this.add(pointage);// ajoute le Label au panneau info jeu

        ecoulementTemps.setForeground(Color.WHITE);//définit la couleur du texte
        ecoulementTemps.setAlignmentX(Box.CENTER_ALIGNMENT);// définit l'alignement en X du panneau
        ecoulementTemps.setFont(ConstantesVue.POLICE_ETIQUETTES);// définit la police
        this.add(ecoulementTemps);// ajoute le Label au panneau info jeu

        this.add(barre);
    }

    /**
     * refraichit le panneau info jeu
     */
        @Override
    public void refreshPanneau()
    {
        // on refraichit a chaque istant la barre progressive et le nombre de points
        Timer timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                pointage.setText("Pointage : " + jeu.getNbPoints()); // change le texte du Label de pointage
                // change la progression de la barre selon la progression en pourcentage du jeu
                barre.setValue((int) Math.round(scenario.getProgression() * 100));
            }
        });
        timer.start();
    }
}
