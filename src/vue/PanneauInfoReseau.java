package vue;


import modele.reseau.GestionnaireReseau;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanneauInfoReseau extends PanneauInfo
{
    static GestionnaireReseau gestionnaireReseau = GestionnaireReseau.getInstance();

    //titre du panneau
    private final String titreInfoReseau = "Informations Reseau";

    //Les Label contenant le qui sera affiché au panneau
    JLabel nbCellulaires = new JLabel("Nb Cellulaires : " + GestionnaireReseau.NB_CELLULAIRES);
    JLabel nbCriminels = new JLabel("Nb Criminels : " + GestionnaireReseau.NB_CRIMINELS);
    JLabel nbConversations = new JLabel("Nb Conversations en cours : " + gestionnaireReseau.getNbConversations());


    /**
     * contructeur de PanneauInfoReseau
     */
    public PanneauInfoReseau()
    {
        super();
        initContenu();

        refreshPanneau();
    }

    /**
     * initialise le contenu du panneau info reseau
     */
    @Override
    public void initContenu()
    {
       this.initTitre(titreInfoReseau);

       //définit la couleur et police tu texte
       nbCellulaires.setForeground(Color.WHITE);
       nbCellulaires.setAlignmentX(Box.CENTER_ALIGNMENT);
       nbCellulaires.setFont(ConstantesVue.POLICE_ETIQUETTES);// définit l'alignement au centre du panneau
       this.add(nbCellulaires);// ajoute le label au panneau

        //définit la couleur et police tu texte
       nbCriminels.setForeground(Color.WHITE);
       nbCriminels.setAlignmentX(Box.CENTER_ALIGNMENT);
       nbCriminels.setFont(ConstantesVue.POLICE_ETIQUETTES);
       this.add(nbCriminels);

        //définit la couleur et police tu texte
        nbConversations.setForeground(Color.WHITE);
        nbConversations.setAlignmentX(Box.CENTER_ALIGNMENT);
        nbConversations.setFont(ConstantesVue.POLICE_ETIQUETTES);// définit l'alignement au centre du panneau
        this.add(nbConversations);// ajoute le label au panneau
    }

    /**
     * refraichit le panneau info reseau toutes les 250 ms
     */
    @Override
    public void refreshPanneau()
    {
        Timer timer = new Timer(250, new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e)
            {
                //change le texte du Label
                nbConversations.setText("Nb Conversations en cours : " + gestionnaireReseau.getNbConversations());
            }
        });

        timer.start();
    }
}
