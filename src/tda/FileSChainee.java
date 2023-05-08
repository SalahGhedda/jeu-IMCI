package tda;

/**
 * File simplement cha�n�e
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */

public class FileSChainee <E>{

	// classe interne d�finissant le noeud
	public class Noeud{
		
		Noeud suivant;
		E element;
		
		public Noeud(E element, Noeud suivant) {
			this.suivant = suivant;
			this.element = element;
		}	
	}
	

	Noeud tete;
	Noeud queue;
	int nbElements=0;
	
	/**
	 * M�thode permettant d'enfiler un �l�ment
	 * @param element � enfiler
	 */
	public void enfiler(E element) {
		
		if(tete==null) {
			
			tete = new Noeud(element, null);
			queue = tete;
			
		}else {
			
			Noeud nouveau = new Noeud(element, null);
			
			queue.suivant = nouveau; 
			queue = nouveau;
		}
		nbElements++;
		
	}

	/**
	 * M�thode permettant de d�filer un �l�ment
	 * @return �l�ment retirer de la file
	 * @throws Exception
	 */
	public E defiler() throws Exception {
		
		if(tete == null) {
			throw new Exception("[FileSChainee.java] File vide");
		}
		
		E elem = tete.element;
		tete = tete.suivant;

		nbElements--;
		
		return elem;
	}
	
	/**
	 * informatrice sur le nombre d'�l�ment dans la file
	 * @return nombre d'�l�ment
	 */
	public int getNbElements() {
		return nbElements;
	}
}
