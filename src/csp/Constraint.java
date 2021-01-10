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

public class Constraint {
	
	private int constraint1;
	private int constraint2;
	
	public Constraint() {}
	
	public Constraint(int constraint1, int constraint2) {
		this.constraint1 = constraint1;
		this.constraint2 = constraint2;
	}

	public int getContrainte1() {
		return constraint1;
	}

	public void setContrainte1(int constraint1) {
		this.constraint1 = constraint1;
	}

	public int getContrainte2() {
		return constraint2;
	}

	public void setContrainte2(int constraint2) {
		this.constraint2 = constraint2;
	}

	@Override
	public String toString() {
		return "Constraint [constraint1=" + constraint1 + ", constraint2=" + constraint2 + "]";
	}

}
