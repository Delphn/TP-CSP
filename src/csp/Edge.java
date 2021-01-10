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

public class Edge {
	
	private Node node1;
	private Node node2;
	public ArrayList<Constraint> ConstraintsList;
	
	
	public Edge() {}


	public Edge(Node node1, Node node2) {
		this.ConstraintsList = new ArrayList<Constraint>();
		this.node1 = node1;
		this.node2 = node2;
	}

	public void generateConstraintsList() {
		
		for(int i = 0; i < node1.getDomain().size(); i++) {
			for(int j = 0; j < node2.getDomain().size(); j++) {
				Constraint constraint = new Constraint(node1.getDomain().get(i), node2.getDomain().get(j));
				ConstraintsList.add(constraint);
			}
		}			
	}
	
	public void DisplayConstraintsList() {
		
		for(int i = 0; i < ConstraintsList.size(); i++) {
			System.out.println(ConstraintsList.get(i).toString() + ", ");
		}
	}
	

	@Override
	public String toString() {
		return "Edge [node1=" + node1.toString() + ", node2=" + node2.toString() + "]";
	}


	public Node getN1() {
		return node1;
	}


	public void setN1(Node node1) {
		this.node1 = node1;
	}


	public Node getN2() {
		return node2;
	}


	public void setN2(Node node2) {
		this.node2 = node2;
	}


	public ArrayList<Constraint> getConstraintsList() {
		return ConstraintsList;
	}

	public void setListeContraintes(ArrayList<Constraint> ConstraintsList) {
		this.ConstraintsList = ConstraintsList;
	}
	
	
	

}
