package vue;

/**
 * Classe abstraite d�finissant un panneau d'information
 *
 * Panneau comporte un titre et sert � donner les informations importantes pour le joueur
 *
 * @author Salah Eddine Ghedda
 * @revision  H2021
 */


import javax.swing.*;
import java.awt.*;

abstract public class PanneauInfo extends JPanel
{
    protected JLabel titre = new JLabel(); // Titre du panneau
    protected final Color COULEUR_ARRIERE_PLAN = Color.DARK_GRAY; // Couleur du panneau
    protected final Color COULEUR_BORDURE = Color.WHITE; // Couleur des bordures du panneau

    /**
     * Constructeur du PanneauInfo
     */
    public PanneauInfo()
    {
        initPanneau();
    }

    /**
     * Initialise le panneau
     */
    public void initPanneau()
    {
        this.setBackground(COULEUR_ARRIERE_PLAN); //d�finit la couleur du panneau
        this.setBorder(BorderFactory.createLineBorder(COULEUR_BORDURE)); // d�finit la couleur des bordures

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //d�finit le Layout du panneau
    }

    /**
     *metode abstraite qui initialise le contenu du panneau
     * des classes filles
     */
    abstract public void initContenu();

    /**
     * Initialise le titre
     * @param titre titre du panneau
     */
    public void initTitre(String titre)
    {
        this.titre.setFont(ConstantesVue.POLICE_TITRES); // d�finit la police du texte
        this.titre.setText(titre); //d�finit le titre
        this.titre.setForeground(Color.WHITE); // d�finit la couleur du texte
        this.titre.setAlignmentX(Box.CENTER_ALIGNMENT); //d�finit la position en X du texte
        this.add(this.titre); // ajoute le Label au PanneauInfo
    }

    /**
     * m�thode abstraite qui refraichit le contenu du panneau
     */
    abstract public void refreshPanneau();




}
