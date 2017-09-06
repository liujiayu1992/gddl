package com.zhiren.common.ext.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Node implements Serializable{
	
	private String id;
	private Node parentNode;
	private List childNodes;
	private boolean leaf;
	
//	public Object attributes;
	
	public Node(String id) {
		this.id = id;
		this.leaf = true;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public Node getRootNode() {
		Node n = this;
		while(n.parentNode != null) {
			n = n.parentNode;
		}
		return n;
	}
	public int getLevel() {
		int level = 1;
		Node n = this;
		while(n.parentNode != null) {
			n = n.parentNode;
			level++;
		}
		return level;
	}
	public String getPathID() {
		String path = "";
		Node n = this;
		while(n.parentNode != null) {
			n = n.parentNode;
			path = n.getId() + path;
		}
		return path;
	}
	public Node getParentNode() {
		return parentNode;
	}
	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}
	
	public List getChildNodes() {
		if(childNodes == null) {
			childNodes = new ArrayList();
		}
		return childNodes;
	}
	
	public void setChildNodes(List childNodes) {
		this.childNodes = childNodes;
	}
	
	public Node getFirstChild() {
		if(getChildNodes().size() == 0) {
			return null;
		}
		return (Node)getChildNodes().get(0);
	}
	public Node getLastChild() {
		if(getChildNodes().size() == 0) {
			return null;
		}
		return (Node)getChildNodes().get(getChildNodes().size()-1);
	}
	
	public Node getNextSibling() {
		if(this.parentNode == null) {
			return null;
		}
		List nodes = this.parentNode.getChildNodes();
		int i =0;
		for(; i < nodes.size() ; i++) {
			if(this.equals((Node)nodes.get(i))) {
				break;
			}
		}
		if(i >= nodes.size()-1) {
			return null;
		}
		return (Node)nodes.get(i+1);
	}
	
	public Node getPreviousSibling() {
		if(this.parentNode == null) {
			return null;
		}
		List nodes = this.parentNode.getChildNodes();
		int i = 0;
		for(; i < nodes.size() ; i++) {
			if(this.equals((Node)nodes.get(i))) {
				break;
			}
		}
		if(i <= 0) {
			return null;
		}
		return (Node)nodes.get(i-1);
	}
	
	private Node getChildById(String id,Node node) {
		while (node != null) {
			if(node.getId().equals(id)) {
				return node;
			}
			Node n = node.getFirstChild();
			Node reNode = getChildById(id,n);
			if(reNode!= null) {
				return reNode;
			}
			node = node.getNextSibling();
		}
		return null;
		
	}
	
	public Node getNodeById(String nodeid) {
		if(nodeid == null || "".equals(nodeid)) {
			return null;
		}
		if(id.equals(nodeid)) {
			return this;
		}
		Node renode = getChildById(nodeid,this);
		return renode;
	}
	
	public boolean isLeaf() {
		return getChildNodes().size()==0 && this.leaf;
	}
	
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	public void appendChild(Node child) {
		child.setParentNode(this);
		getChildNodes().add(child);
	}
	
	public void remove() {
		if(this.parentNode == null) {
			return;
		}
		List nodes = this.parentNode.getChildNodes();
		for(int i =0; i < nodes.size() ; i++) {
			if(this.equals((Node)nodes.get(i))) {
				nodes.remove(i);
				break;
			}
		}
	}
	
	public abstract String getScript();
	
}
