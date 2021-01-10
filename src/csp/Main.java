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

import java.util.Scanner;

public class Main {

	static CspGenerator csp = new CspGenerator();
	
	public static void main(String[] args) {

		int nbNode = 0;
		int domainSize = 0;
		double density = 0;
		double hardness = 0;
		
		System.out.println(" Entrer le nombre de noeuds: ");
		Scanner sc = new Scanner(System.in);
		nbNode = sc.nextInt();
		
		System.out.println(" Entrer la taille du domaine: ");
		domainSize = sc.nextInt();
		
		System.out.println("Entrer la densité: ");
		density = sc.nextDouble();
		
		System.out.println("Entrer la dureté: ");
		hardness = sc.nextDouble();
		sc.close();


		System.out.println(" Le CSP est : --> ");
		csp.generateCSP(nbNode, domainSize, density, hardness);
		csp.displayCSP();

		if(csp.VerifyCSP()){
			long timeBT = csp.BackTracking();
			double backTracking = (double) timeBT / 1000000.0;
			long timeBJ = csp.backjumping();
			double backJumping = (double) timeBJ / 1000000.0;
			long timeFC = csp.forwardChecking();
			double forwardChecking = (double) timeFC / 1000000.0;


			System.out.println("Exec time for backtracking : " + backTracking + " ms");
			System.out.println("Exec time for backjumping : " + backJumping + " ms");
			System.out.println("Exec time for : " + forwardChecking);
		} else
			System.out.println("UNSAT");
		}


}
