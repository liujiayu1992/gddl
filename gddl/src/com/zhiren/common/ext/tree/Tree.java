package com.zhiren.common.ext.tree;

import java.io.Serializable;

import com.zhiren.common.ext.Component;

public class Tree extends Component implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -470050751728477970L;

	private String id;
	private TreeNode treeRootNode;
	private TreePanel treePanel;
	private boolean isEditable;
	private String expandedNodeid;
	private String selectedNodeid;
	
	public Tree(String id,boolean isEditable,TreeNode treeRootNode,TreePanel treePanel) {
		this.id = id;
		this.isEditable = isEditable;
		this.treeRootNode = treeRootNode;
		this.treePanel = treePanel;
	}
	
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TreeNode getTreeRootNode() {
		return this.treeRootNode;
	}
	public void setTreeRootNode(TreeNode treeNode) {
		this.treeRootNode = treeNode;
	}
	public TreePanel getTreePanel() {
		return this.treePanel;
	}
	public void setTreePanel(TreePanel treePanel) {
		this.treePanel = treePanel;
	}
	public boolean isEditable() {
		return this.isEditable;
	}
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	public String getExpandedNodeid() {
		return expandedNodeid==null?"":expandedNodeid;
	}

	public void setExpandedNodeid(String expandedNodeid) {
		this.expandedNodeid = expandedNodeid;
	}
	
	public void setTreeNodeExpandedNode(String id) {
		TreeNode n = (TreeNode)getTreeRootNode().getNodeById(id);
		if(n== null) {
			return ;
		}
		n.setExpanded(true);
		while(n.getParentNode() != null) {
			n = (TreeNode)n.getParentNode();
			n.setExpanded(true);
		}
	}

	public String getSelectedNodeid() {
		return selectedNodeid==null?"":selectedNodeid;
	}

	public void setSelectedNodeid(String selectedNodeid) {
		this.selectedNodeid = selectedNodeid;
	}

	
	public String getTreeDataScript() {
		if (getTreeRootNode() == null) {
			return null;
		}
		if(!"".equals(getSelectedNodeid())) {
			setTreeNodeExpandedNode(getSelectedNodeid());
		}else
			if(!"".equals(getExpandedNodeid())) {
				setTreeNodeExpandedNode(getExpandedNodeid());
			}
		StringBuffer sb = new StringBuffer();
		getNodeScript(sb, getTreeRootNode());

		return sb.toString();
	}

	public void getNodeScript(StringBuffer sb, TreeNode node) {
		while (node != null) {
			String pathid = id + node.getPathID(); 
			sb.append("var ").append(pathid).append(node.getId())
					.append(" = ").append(node.getScript()).append(";\n");
			if (node.getParentNode() != null) {
				sb.append("\t").append(pathid).append(".appendChild([")
						.append(pathid).append(node.getId())
						.append("]);\n");
			}
			TreeNode n = (TreeNode) node.getFirstChild();
			getNodeScript(sb, n);
			node = (TreeNode) node.getNextSibling();
		}
		return;
	}
	
	public String getScript() {
		StringBuffer sb = new StringBuffer();
		sb.append(getTreeDataScript());
		sb.append(getTreePanel().getRenderScript());
		if(!"".equals(getSelectedNodeid())) {
			TreeNode n = (TreeNode)getTreeRootNode().getNodeById(getSelectedNodeid());
			sb.append(id).append(n.getPathID()).append(getSelectedNodeid()).append(".select();");
		}
		return sb.toString();
	}

}
