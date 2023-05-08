package tda;

/**
 * File simplement chaînée
 * 
 * @author Fred Simard | ETS
 * @revision hiver 2021
 */

public class FileSChainee <E>{

	// classe interne définissant le noeud
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
	 * Méthode permettant d'enfiler un élément
	 * @param element à enfiler
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
	 * Méthode permettant de défiler un élément
	 * @return élément retirer de la file
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
	 * informatrice sur le nombre d'élément dans la file
	 * @return nombre d'élément
	 */
	public int getNbElements() {
		return nbElements;
	}
}
