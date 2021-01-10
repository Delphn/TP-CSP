/*
 * ===================================================================
 *
 * TP Programation Orientée Contraintes
 *
 * Authors: Delphin Rukundo
 *        & Emmanuel Zakaryan
 *
 * ===================================================================
 */

package csp;

import java.util.ArrayList;
import java.util.Random;

public class CspGenerator {
	
	public ArrayList<Node> nodeList;
	public ArrayList<Edge> edgeList;
	public ArrayList<ArrayList<Edge>> inputEdgeList;
	public ArrayList<ArrayList<Edge>> outputEdgeList;
	public ArrayList<ArrayList<Integer>> DomainsList;
	
	public CspGenerator() {
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
		inputEdgeList = new ArrayList<ArrayList<Edge>>();
		outputEdgeList = new ArrayList<ArrayList<Edge>>();
		DomainsList = new ArrayList<ArrayList<Integer>>();
	}
	
	public void generateCSP(int nbNode, int domainSize, double density, double hardness) {
		
		int nbEdge = 0;
		int nbMaxEdge = 0;
		int nbConstraints = 0;
		int nbMaxConstraints = 0;
		double currentDensity;
		double currentHardness;

		ArrayList<Integer> domains = new ArrayList<Integer>();
		
		for(int i = 0; i < nbNode; i++) {
			for(int j = 0; j < domainSize; j++) {
				domains.add(new Random().nextInt(100));
			}
			this.nodeList.add(new Node(i, domains));
			domains.clear();
		}
		
		for(int i = 0; i < this.nodeList.size(); i++) {
			Node node = this.nodeList.get(i);
			for(int j = 0; j < this.nodeList.size(); j++) {
				if(node.getNodeId() != this.nodeList.get(j).getNodeId()) {
					Edge arc = new Edge(node, this.nodeList.get(j));
					this.edgeList.add(arc);
				}
			}
		}
		
		for(int i = 0; i < this.edgeList.size(); i++) {
			this.edgeList.get(i).generateConstraintsList();
		}
		
		nbMaxEdge = this.edgeList.size();
		nbEdge = nbMaxEdge;
		currentDensity = nbEdge / nbMaxEdge;
		while (currentDensity > density) {
			this.edgeList.remove(new Random().nextInt(this.edgeList.size()));
			nbEdge = this.edgeList.size();
			currentDensity = nbEdge / nbMaxEdge;
		}
		
		nbMaxConstraints = this.edgeList.get(0).getConstraintsList().size();
		for (int i = 0; i < this.edgeList.size(); i++) {
			nbConstraints = this.edgeList.get(i).getConstraintsList().size();
			currentHardness = nbConstraints / nbMaxConstraints;
			
			while (currentHardness > hardness) {
				this.edgeList.get(i).getConstraintsList().remove(new Random().nextInt(nbConstraints));
				nbConstraints = this.edgeList.get(i).getConstraintsList().size();
				currentHardness = nbConstraints / nbMaxConstraints;
			}
		}
		FillLists();
	}

	public void FillLists() {
		
		for (int i = 0; i < this.nodeList.size(); i++){
			ArrayList<Edge> inputEdge = new ArrayList<Edge>();
			ArrayList<Edge> outputEdge = new ArrayList<Edge>();
			
			for (int j = 0; j < edgeList.size(); j++){
				if (this.edgeList.get(j).getN1().getNodeId() == i)
					outputEdge.add(this.edgeList.get(j));
				else if (this.edgeList.get(j).getN2().getNodeId() == i)
					inputEdge.add(this.edgeList.get(j));
			}
			this.inputEdgeList.add(i, inputEdge);
			this.outputEdgeList.add(i, outputEdge);
		}
	}
	
	public void displayCSP() {
		// Affichage de la liste des noeuds et leurs domains
		System.out.println("\n Liste des noeuds générés avec leurs domains --> \n");
		for (int i = 0; i < this.nodeList.size(); i++) {
			System.out.print(this.nodeList.get(i).toString() + " : ");
			this.nodeList.get(i).displayDomains();
		}
		// Affichage des arcs 
		System.out.println("\n Liste des arcs avec la densité souhaitée --> \n");
		for (int i = 0; i < this.edgeList.size(); i++) {
			System.out.println(this.edgeList.get(i).toString() + " , ");
		}
		
		// Affichage des constraints 
		System.out.println("\n Liste des constraints avec la dureté souhaitée --> \n");
		for (int i = 0; i < this.edgeList.size(); i++) {
			this.edgeList.get(i).DisplayConstraintsList();
		}
	}
	
	public boolean VerifyCSP() {
		boolean OK = true;
		FillDomainList();
		for (int i = 0; i < this.DomainsList.size(); i++)
			if (this.DomainsList.get(i).isEmpty()){
				if (!this.inputEdgeList.get(i).isEmpty() || !this.outputEdgeList.get(i).isEmpty())
					return false;
				else {
					this.nodeList.get(i).setNodeValue(this.nodeList.get(i).getDomain().get(new Random().nextInt(this.nodeList.get(i).getDomain().size())));
					this.DomainsList.get(i).add(this.nodeList.get(i).getNodeValue());
				}
			}
		ConstraintsFilter();
		return OK;
	}

	private void ConstraintsFilter() {
		int constraint1, constraint2, noeud;
		for (int i = 0; i < this.nodeList.size(); i++) {
			for (int j = 0; j < this.outputEdgeList.get(i).size(); j++) {
				for (int k = 0; k < this.outputEdgeList.get(i).get(j).getConstraintsList().size(); k++) {
					constraint1 = this.outputEdgeList.get(i).get(j).getConstraintsList().get(k).getContrainte1();
					noeud 	 = this.outputEdgeList.get(i).get(j).getN2().getNodeId();
					constraint2 = this.outputEdgeList.get(i).get(j).getConstraintsList().get(k).getContrainte2();
					if (!this.DomainsList.get(i).contains(constraint1) || !this.DomainsList.get(noeud).contains(constraint2)){
						this.outputEdgeList.get(i).get(j).getConstraintsList().remove(k);
						k--;
					}
				}
			}
		}
	}

	private void FillDomainList() {
		for (int i = 0; i < this.nodeList.size(); i++){
			this.DomainsList.add(i, DomainsFilter(i));
		}
		
	}
	
	private ArrayList<Integer> DomainsFilter(int idNoeud) {
		
		int val;
		ArrayList<Integer> domains = new ArrayList<Integer>();

		// La liste des domains correspondante au noeud passé en paramètre
		domains = this.nodeList.get(idNoeud).getDomain();
				
		// Pour chaque valeur du domaine du noeud, on vérifie la consistance
		for (int i = 0; i < this.nodeList.get(idNoeud).getDomain().size(); i++){
			val = this.nodeList.get(idNoeud).getDomain().get(i);
			if(verifyConsistency(val, idNoeud) == false)
				// On retire du domaine les valeurs qui rendent le CSP inconsistant
				domains.remove(domains.indexOf(val));
		}
		
		// On retire de domains les doublons
		for (int i = 0; i < domains.size(); i++) {
			while (NbRepetition(domains.get(i), domains) > 1){
				domains.remove(domains.indexOf(domains.get(i)));
			}
		}
		return domains;
	}

	public int NbRepetition(int valeur, ArrayList<Integer> domains) {
		int i = 0;
		for(int j = 0; j < domains.size(); j++){
			if(domains.get(j) == valeur)
				i++;
		}
		return i;
	}

	private boolean verifyConsistency(int val, int nodeId) {
		
		boolean OK = false;
		
		if (this.outputEdgeList.get(nodeId).isEmpty() && this.inputEdgeList.get(nodeId).isEmpty())
			OK = true;
		
		for (int i = 0; i < this.outputEdgeList.get(nodeId).size(); i++){
			OK = false;
			for (int j = 0; j < this.outputEdgeList.get(nodeId).get(i).getConstraintsList().size(); j++){
				if (val == this.outputEdgeList.get(nodeId).get(i).getConstraintsList().get(j).getContrainte1()){
					OK = true;
				}
			}
			if(OK == false){
				return OK;
			}
		}
		
		for (int i = 0; i < this.inputEdgeList.get(nodeId).size(); i++){
			OK = false;
			for (int j = 0; j < this.inputEdgeList.get(nodeId).get(i).getConstraintsList().size(); j++){
				if(val == this.inputEdgeList.get(nodeId).get(i).getConstraintsList().get(j).getContrainte2()){
					OK = true;
				}
			}
			if(OK == false){
				return OK;
			}
		}
		return OK;
	}
	
	/*
	BackTracking implementation
	 */
	public long BackTracking() {
		
		int i = 0;
		int value = 0;
		boolean verify;
		long startTime = System.nanoTime();
		long tempsExecution;
		
		ArrayList<ArrayList<Integer>> domaines = new ArrayList<ArrayList<Integer>>();
		domaines = DomainsList();
		
		while (i >= 0 && i < this.nodeList.size()) {
			verify = false;
			while (!verify && !domaines.get(i).isEmpty()){
				value = domaines.get(i).remove(0);
   				if (VerifierassignationConsistance(value, this.nodeList.get(i).getNodeId())){
   					verify = true;
					this.nodeList.get(i).setNodeValue(value);
				}
			}
			if (!verify) {
				domaines.get(i).clear();
				for(int j = 0; j < this.DomainsList.get(i).size(); j++)
					domaines.get(i).add(this.DomainsList.get(i).get(j));
				i--;
			} else
				i++;
		}
		if (i < 0){
			System.out.println("BackTracking : UNSAT");
			tempsExecution = System.nanoTime();
			return tempsExecution - startTime;
		} else {
			tempsExecution = System.nanoTime();
			displaySolution();
			return tempsExecution - startTime;
		}
	}

	public ArrayList<ArrayList<Integer>> DomainsList() {
		ArrayList<ArrayList<Integer>> domaines1 = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < this.DomainsList.size(); i++){
			ArrayList<Integer> domaines2 = new ArrayList<Integer>();
			for(int j = 0; j < this.DomainsList.get(i).size(); j++)
				domaines2.add(this.DomainsList.get(i).get(j));
			domaines1.add(domaines2);
		}
		return domaines1;
	}

	private void displaySolution() {
		System.out.println("\n ---------------------------- Solution --------------------------- \n");
		for (int i = 0; i < this.nodeList.size(); i++)
			System.out.println(" Node value " + i + " : " + this.nodeList.get(i).getNodeValue() + "\n");
	}

	/*
	Vérification de la cohérence de l'assignation des valeurs à des variables
	 */
	private boolean VerifierassignationConsistance(int val, int idNoeud) {
		
		boolean verify = false;
		for (int i = 0; i < this.outputEdgeList.get(idNoeud).size(); i++){
			verify = false;
			if(this.outputEdgeList.get(idNoeud).get(i).getN2().getNodeValue() == -1)
				verify = true;
			else { 
				for(int j = 0; j < this.outputEdgeList.get(idNoeud).get(i).getConstraintsList().size(); j++)
					if(val == this.outputEdgeList.get(idNoeud).get(i).getConstraintsList().get(j).getContrainte1()
						&& this.outputEdgeList.get(idNoeud).get(i).getN2().getNodeValue() == this.outputEdgeList.get(idNoeud).get(i).getConstraintsList().get(j).getContrainte2())
						verify = true;
			}
			if (verify == false)
				return verify;
		}
		for(int i = 0; i < this.inputEdgeList.get(idNoeud).size(); i++){
			verify = false;
			if(this.inputEdgeList.get(idNoeud).get(i).getN1().getNodeValue() == -1)
				verify = true;
			else { 
				for(int j = 0; j < this.inputEdgeList.get(idNoeud).get(i).getConstraintsList().size(); j++)
					if(val == this.inputEdgeList.get(idNoeud).get(i).getConstraintsList().get(j).getContrainte2()
						&& this.inputEdgeList.get(idNoeud).get(i).getN1().getNodeValue() == this.inputEdgeList.get(idNoeud).get(i).getConstraintsList().get(j).getContrainte1())
						verify = true;
			}
			if (verify == false)
				return verify;
		}
		return verify;
	}
	/*
	BackJumping implementation
	 */
	public long backjumping() {
		int i = 0, j = 0, valeur = 0;
		boolean verify;
		long temps = System.nanoTime();
		long tempsExecution;
		boolean consistant;
		
		ArrayList<ArrayList<Integer>> domains = new ArrayList<ArrayList<Integer>>();
		domains = DomainsList();
		
		ArrayList<Integer> coupables = new ArrayList<Integer>();
		for (int k = 0; k < this.nodeList.size(); k++) {
			coupables.add(k, 0);
		}
			
		while (i >= 0 && i < this.nodeList.size()) {
			verify = false;
			while(!verify && !domains.get(i).isEmpty()){
				valeur = domains.get(i).remove(0);
				consistant = true;
				j = 0;
				while (j > i && consistant) {
					if (j > coupables.get(i)){
						coupables.add(i, j);
					}
	   				if (VerifierassignationConsistance(valeur, this.nodeList.get(i).getNodeId())){
	   					j++;
	   				} else {
	   					consistant = false;
	   				}
				}
				
				if (consistant) {
					verify = true;
					this.nodeList.get(i).setNodeValue(valeur);
				}
			}
			
			if (!verify) {
				this.DomainsList.get(i).clear();
				for (int z = 0; z < this.DomainsList.get(i).size(); z++)
					domains.get(i).add(this.DomainsList.get(i).get(z));
				i = coupables.get(i);
			} else {
				i++;
				coupables.add(i, 0);
			}
		}
		
		if (i < 0) {
			System.out.println("BackJumping : UNSAT");
			tempsExecution = System.nanoTime();			
			return tempsExecution - temps;
		} else {
			tempsExecution = System.nanoTime();
			displaySolution();
			return tempsExecution - temps;
		}
	}

	/*
	Forward-Checking implementation
	 */
	public long forwardChecking() {
		int i = 0;
		int j = 0;
		int valeur = 0;
		long temps = System.nanoTime();
		long tempsExecution;
		Boolean OK = false;
		ArrayList<ArrayList<Integer>> listeDomaines = this.DomainsList();

		while (i < this.nodeList.size()) {
			OK = false;
			while(!OK && !listeDomaines.get(i).isEmpty()){
				System.out.println("index: " + i);
				valeur = listeDomaines.get(i).remove(0);
				for (int k = i; k <= this.nodeList.size(); k++) {
					revise(k, i);
				}
			}
			if(!OK) {
				this.nodeList.get(i).setDomains(listeDomaines.get(i));
			}
			i++;
		}

		tempsExecution = System.nanoTime();
		return tempsExecution - temps;
	}

	public void revise(int m, int n) {
		Edge linkToWatch = null;

		for (Edge a : this.edgeList) {
			if(a.getN1().getNodeId() == n && a.getN2().getNodeId() == m) {
				linkToWatch = a;
			}
		}

		if(linkToWatch != null) {
			for(int i = 1; i <= this.edgeList.size(); i++) {
				this.edgeList.set(m -1 , linkToWatch);

				if(!verifyConsistency(linkToWatch.getN1().getNodeValue(), linkToWatch.getN2().getNodeId()) && !verifyConsistency(linkToWatch.getN1().getNodeValue(), linkToWatch.getN2().getNodeId())) {
					this.edgeList.remove(m-1);
				}
			}
		}
	}

}
