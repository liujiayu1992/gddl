package com.zhiren.common.ext.tree;

import java.util.List;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
/*
 * 作者：王磊
 * 时间：2009-08-20
 * 描述：增加通过list为树加载节点的方法
 */
/**
 * @author Rock
 * @since 2008-12-20
 * @version v1.1.2.3
 * @discription 树的处理
 */
public class TreeOperation {
	public TreeNode getTreeRootNode(String sql) {
		return getTreeRootNode(sql,false);
	}
	public TreeNode getTreeRootNode(List treeList, boolean checkedBox){
		TreeNode RootNode = null;
		for(int i = 0; i < treeList.size(); i++){
			String[] nodeInfo = (String[])treeList.get(i);
			String id = nodeInfo[0];
			String text = nodeInfo[1];
			String parentId = nodeInfo[2];
			TreeNode node = new TreeNode(id, text, checkedBox);
			if(i == 0){
				RootNode = node;
				continue;
			}
			TreeNode parentNode = (TreeNode)RootNode.getNodeById(parentId);
			parentNode.appendChild(node);
		}
		return RootNode;
	}
	public TreeNode getTreeRootNode(String sql,boolean checkedBox) {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		TreeNode parentNode = null;
		TreeNode RootNode = null;
		int lastjib = 0;
		while (rsl.next()) {
			int curjib = rsl.getInt(2);
			TreeNode node = new TreeNode(rsl.getString(0), rsl.getString(1), checkedBox);
			if (parentNode == null) {
				RootNode = node;
				node.setCheckbox(checkedBox);
				parentNode = node;
				lastjib = curjib + 1;
				continue;
			}
			if (lastjib < curjib) {
				parentNode = (TreeNode) parentNode.getLastChild();
			} else if (lastjib > curjib) {
				for (int i = 0; i < lastjib - curjib; i++)
					parentNode = (TreeNode) parentNode.getParentNode();
			}
			lastjib = curjib;
			parentNode.appendChild(node);
		}
		con.Close();
		return RootNode;
	}
	
	public String getTreeNodeScript(String treeId,TreeNode node) {
		StringBuffer sb = new StringBuffer();
		getNodeScript(treeId, sb, node);
		return sb.toString();
	}

	public void getNodeScript(String treeId,StringBuffer sb, TreeNode node) {
		while (node != null) {
			String pathid = treeId + node.getPathID(); 
			sb.append("var ").append(pathid).append(node.getId())
					.append(" = ").append(node.getScript()).append(";\n");
			if (node.getParentNode() != null) {
				sb.append("\t").append(pathid).append(".appendChild([")
						.append(pathid).append(node.getId())
						.append("]);\n");
			}
			TreeNode n = (TreeNode) node.getFirstChild();
			getNodeScript(treeId, sb, n);
			node = (TreeNode) node.getNextSibling();
		}
		return;
	}
}
