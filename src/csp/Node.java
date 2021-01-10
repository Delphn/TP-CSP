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

public class Node {
	
	private int nodeId;
	private int nodeValue;
	public ArrayList<Integer> domains;
	
	
	public Node() {
		this.domains = new ArrayList<Integer>();
		this.nodeId = 0;
		this.nodeValue = 0;
	}

	public Node(int nodeId) {
		domains = new ArrayList<Integer>();
		this.nodeId = nodeId;
		this.nodeValue = -1;
	}
	
	public Node(int nodeId, ArrayList<Integer> domains) {
		this.domains = new ArrayList<Integer>();
		this.nodeId = nodeId;
		this.nodeValue = -1;
		this.setDomains(domains);
	}
	
	public void displayDomains(){
		for (Integer domain : domains) {
			System.out.print(domain + ", ");
		}
		System.out.println();
	}

	public int getNodeId() {
		return nodeId;
	}


	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}


	public int getNodeValue() {
		return nodeValue;
	}


	public void setNodeValue(int nodeValue) {
		this.nodeValue = nodeValue;
	}


	public ArrayList<Integer> getDomain() {
		return domains;
	}


	public void setDomains(ArrayList<Integer> domain) {
		this.domains.addAll(domain);
	}


	@Override
	public String toString() {
		return "Node [nodeId=" + nodeId + "]";
	}
	

}
