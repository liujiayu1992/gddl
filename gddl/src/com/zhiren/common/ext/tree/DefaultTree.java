package com.zhiren.common.ext.tree;

import java.io.Serializable;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.Window;
/*
 * 作者：王磊
 * 时间：2009-09-01
 * 描述：陕西提出  修改gys_mk_sql 的数据来源vwgongysmkcz 为vwgongysmkczkj 
 * 所涉及到的模块均为陕西所用 一是PidcIndex.java 二是 jt/dtsx/Qichcx.java
 * 经测试可用
 */
/*
 * 作者：王磊
 * 时间：2009-05-20
 * 描述：增加空构造
 */
public class DefaultTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -840588747928291766L;

	public final static int tree_dianc_win = 0;

	public final static int tree_gongys_win = 1;

	public final static int tree_gys_mk_cz = 2;

	public final static int tree_gys_mk = 3;

	public final static int tree_hybh_win = 4;
	
	public final static int tree_dgys_xgys = 5;
	
	public final static int tree_gys_mk_cz_kj = 6;
	
	public final static int tree_gys_mk_kj = 7;
	
	public final static int tree_sf_cs = 8;
	
	public final static int tree_mkdq_mk_sx=9;
/**
*huochaoyuan 2010-03-02添加煤矿地区与煤矿单位对应的树结构
*大唐陕西分公司调运部提出的日报，按煤矿地区和煤矿单位的对应关系进行计划录入和查询，故添加
*/
	private String meikdq_mk_sx="select id,mingc,jib from (\n"+
	" select m.id as id,m.mingc as mingc,m.meikdq_id as id2,2 as xuh1,g.xuh as xuh2 ,3 as jib\n"+
	" from\n"+ 
	" meikxxb m,gongysb g\n"+ 
	" where m.meikdq_id=g.id\n"+ 
	" union\n"+ 
	" select id as id,mingc as mingc,id as id2,1 as xuh1,xuh as xuh2,2 as jib\n"+ 
	" from gongysb\n"+ 
	" where leix=0\n"+  
	"union\n"+  
	"select 0 as id,'全部' as mingc,0as id2,0 as xuh1,0 as xuh2,1 as jib from dual)\n"+  
	" order by xuh2,id2 ,xuh1";
//end
	private String dianc_sql = "select id, mingc, level as jib from diancxxb start with id = dcid connect by fuid = prior id order SIBLINGS by xuh";

	private String gongys_sql = "select id,mingc,level jib from (select 0 id,'全部' mingc,-1 fuid from dual union \n"
			+ "select gys.id,gys.mingc,0 fuid from gongysb gys,gongysdcglb gysgl  where gys.id=gysgl.gongysb_id and gys.leix=1 and zhuangt=1 and gysgl.diancxxb_id= dcid \n"
			+ "union select m.id,m.mingc,glb.gongysb_id as fuid  from meikxxb m,gongysmkglb glb,gongysb gys,gongysdcglb glbdc \n"
			+ "where m.id=glb.meikxxb_id and glb.gongysb_id=gys.id and gys.leix=1 and gys.id=glbdc.gongysb_id and glbdc.diancxxb_id=dcid and m.zhuangt=1) \n"
			+ "where fuid = -1 or (fuid=0 and level=2) or (fuid>0 and level =3) connect by NOCYCLE fuid = PRIOR id";

	private String gys_mk_cz_sql = "select id,mingc,jib,level ll,fuid from  \n"
			+ "(select 0 id, '全部' mingc, -1 fuid, 1 jib from dual union \n"
			+ "select id,mingc,fuid,jib from vwgongysmkcz where diancxxb_id = dcid) \n"
			+ "where jib = level start with id = 0 \n"
			+ "connect by fuid = prior id order SIBLINGS by fuid ";
	
	private String gys_mk_cz_kj_sql = 
		"select wid id,mingc,jib,level ll,fuid from\n" +
		"\t\t\t(select 0 wid,0 id, '全部' mingc, -1 fuid, 1 jib from dual union\n" + 
		"\t\t\tselect wid,id,mingc,fuid,jib from\n" + 
		"      vwgongysmkczkj where diancxxb_id = dcid)\n" + 
		"\t\t\twhere jib = level and CONNECT_BY_ISCYCLE = 0\n" + 
		"\t\t\tconnect by NOCYCLE fuid = prior id order SIBLINGS by fuid";
	
	private String gys_mk_kj_sql = 
		"select wid id,mingc,jib,level ll,fuid from\n" +
		"\t\t\t(select 0 wid,0 id, '全部' mingc, -1 fuid, 1 jib from dual union\n" + 
		"\t\t\tselect wid,id,mingc,fuid,jib from\n" + 
		"      vwgongysmkkj where diancxxb_id = dcid)\n" + 
		"\t\t\twhere jib = level and CONNECT_BY_ISCYCLE = 0\n" + 
		"\t\t\tconnect by NOCYCLE fuid = prior id order SIBLINGS by /*fuid*/mingc";

	private String gys_mk_sql = "select id,mingc,jib,level ll,fuid from  \n"
			+ "(select 0 id, '全部' mingc, -1 fuid, 1 jib from dual union \n"
			+ "select id,mingc,fuid,jib from vwgongysmkczkj where jib < 4 and diancxxb_id = dcid) \n"
			+ "where jib = level start with id = 0 \n"
			+ "connect by fuid = prior id order SIBLINGS by fuid ";

	private String huaybh_sql = "select id, bianm, level jib\n"
			+ "  from (select 0 as id, '请选择' as bianm, -1 as fuid\n"
			+ "          from dual\n"
			+ "        union\n"
			+ "        select z.id, z.bianm, z.fuid\n"
			+ "          from (select 1 as id, '未录入' as bianm, 0 as fuid\n"
			+ "                  from dual\n"
			+ "                union\n"
			+ "                select 2 as id, '未提交' as bianm, 0 as fuid from dual) z\n"
			+ "        union\n" + "        select zm.zhillsb_id as id,\n"
			+ "               zm.bianm as bianm,\n"
			+ "               decode(z.shenhzt, 0, 1, 1, 2) as fuid\n"
			+ "          from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z\n"
			+ "         where zm.zhuanmlb_id = lb.id\n"
			+ "           and lb.jib = 3\n"
			+ "           and y.zhilblsb_id = zm.zhillsb_id\n"
			+ "           and z.id = zm.zhillsb_id)\n" + " where fuid = -1\n"
			+ "    or (fuid = 0 and level = 2)\n"
			+ "    or (fuid > 0 and level = 3)\n"
			+ "connect by NOCYCLE fuid = PRIOR id";
	
	private String dgys_xgys_sql = 


		"select id,mingc,jib,level ll,fuid from\n" +
		"\t\t\t(select '0' id,'全部'  mingc, -1 fuid, 1 jib from dual union\n" + 
		"\t\t\tselect to_char(id) as id,mingc,0 fuid,2 jib from gongysb\n" + 
		"where (fuid=0 or fuid=-1) and leix=1 \n" + 
		"union\n" + 
		"select * from (\n" + 
		"select decode(grouping(mingc),1,'11111',min(id)) as id,decode(grouping(mingc),1,'全部',mingc) as mingc,\n" + 
		"decode(grouping(fuid),1,fuid,fuid) as fuid,3 jib\n" + 
		"from gongysb where (fuid!=0 and fuid!=-1) and leix=1 \n" + 
		"group by rollup (fuid,mingc) having not grouping(fuid)=1\n" + 
		"\n" + 
		")   )\n" + 
		"where jib = level start with id = 0\n" + 
		"connect by fuid = prior id order SIBLINGS by fuid,decode(id,11111,0,id)";

	private String sf_cs_sql = // 省份_城市_树形查询
		"select d.id, d.quanc as mingc, level as jib, level as ll, d.fuid\n" +
		"  from (select 0 id, '全部' quanc, -1 fuid from dual\n" + 
		"        union\n" + 
		"        select sf.id, sf.quanc, 0 as fuid from shengfb sf\n" + 
		"        union\n" + 
		"        select cs.id, cs.quanc, cs.shengfb_id as fuid from chengsb cs) d\n" + 
		" start with d.id = 0\n" + 
		"connect by prior d.id = d.fuid";

	private Tree tree;

	private Window window;

	public DefaultTree(){
		
	}
	/**
	 * @param type
	 *            类型
	 * @param id
	 *            对象id
	 * @param dianc
	 *            电厂id
	 * @param submitor
	 *            树按钮提交的表单名称 例:forms[0]
	 * @param selectid
	 *            选中的节点( 此属性被设置后 expandedid 属性将不会起作用)
	 * @param expandedid
	 *            打开的节点
	 */
	public DefaultTree(int type, String id, String dianc, String submitor,
			String selectid, String expandedid) {
		switch (type) {
		case tree_dianc_win:
			setTree_dianc_window(id, dianc, submitor, selectid, expandedid);
			break;
		case tree_gongys_win:
			setTree_window(gongys_sql, id, dianc, submitor, selectid,
					expandedid);
			break;
		case tree_gys_mk_cz:
			setTree_window(gys_mk_cz_sql, id, dianc, submitor, selectid,
					expandedid);
			break;
		case tree_gys_mk:
			setTree_window(gys_mk_sql, id, dianc, submitor, selectid,
					expandedid);
			break;
		case tree_hybh_win:
			setTree_window(huaybh_sql, id, dianc, submitor, selectid,
					expandedid);
			break;
		case tree_dgys_xgys:
			setTree_window(dgys_xgys_sql, id, dianc, submitor, selectid,
					expandedid);
			break;
		case tree_gys_mk_cz_kj:
			setTree_window(gys_mk_cz_kj_sql, id, dianc, submitor, selectid,
					expandedid);
			break;
		case tree_gys_mk_kj:
			setTree_window(gys_mk_kj_sql, id, dianc, submitor, selectid,
					expandedid);
			break;
		case tree_mkdq_mk_sx:
			setTree_window(meikdq_mk_sx, id, dianc, submitor, selectid,
					expandedid);
			break;	
		case tree_sf_cs:
			setTree_window(sf_cs_sql, id, dianc, submitor, selectid,
					expandedid);
			break;
		default:
			break;
		}
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	public TreeNode TreeRootNode(String sql) {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		TreeNode parentNode = null;
		TreeNode RootNode = null;
		int lastjib = 0;
		while (rsl.next()) {
			int curjib = rsl.getInt(2);
			TreeNode node = new TreeNode(rsl.getString(0), rsl.getString(1));
			if (parentNode == null) {
				RootNode = node;
				node.setCheckbox(false);
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

	/**
	 * @param id
	 * @param page
	 * @return
	 */
	public Tree getTree(String Ysql, String id, String diancid) {
		String sql = Ysql.replaceAll("dcid", diancid);
		if(dianc_sql.equals(Ysql)){
			JDBCcon con = new JDBCcon();
			if(con.getHasIt("select * from diancxxb where ranlgs = 1 and id="+diancid)) {
				sql = sql.replaceAll("fuid", "shangjgsid");
			}
			con.Close();
		}
		TreeNode tn = TreeRootNode(sql);
		TreePanel tp = new TreePanel(id);
		tp.setTreeRootNodeid(tn.getId());
		tp.setWidth(200);
		Tree tree = new Tree(id, false, tn, tp);
		return tree;
	}

	public Tree getTree_dianc(String id, String diancid) {
		String sql = dianc_sql.replaceAll("dcid", diancid);
		JDBCcon con = new JDBCcon();
		if(con.getHasIt("select * from diancxxb where ranlgs = 1 and id="+diancid)) {
			sql = sql.replaceAll("fuid", "shangjgsid");
		}
		con.Close();
		TreeNode tn = TreeRootNode(sql);
		TreePanel tp = new TreePanel(id);
		tp.setTreeRootNodeid(tn.getId());
		tp.setWidth(200);
		Tree tree = new Tree(id, false, tn, tp);
		return tree;
	}

	public void setTree_window(String sql, String id, String diancid,
			String submitor, String selectid, String expandedid) {
		tree = getTree(sql, id, diancid);
		tree.getTreeRootNode().setExpanded(true);
		tree.setExpandedNodeid(expandedid);
		tree.setSelectedNodeid(selectid);
		// 构造按钮的handler
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() {\nvar cks = ")
				.append(id)
				.append(
						"_treePanel.getSelectionModel().getSelectedNode();\nif(cks==null){")
				.append(id)
				.append(
						"_window.hide();return;}\nvar obj0 = document.getElementById('")
				.append(id).append("_id');\nobj0.value = cks.id;\n").append(id)
				.append("_text.setValue(cks.text);\n").append(id).append(
						"_window.hide();\n");
		if (submitor != null && !"".equals(submitor)) {
			handler.append("document.").append(submitor).append(".submit()");
		}
		handler.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		// 构造承载按钮的toolbar
		Toolbar tb = new Toolbar(null);
		tb.addItem(btn);
		tree.getTreePanel().addBbar(tb);
		tree.getTreePanel().setRender("'body'");
		window = new Window(id);
		window.addItem(id + "_treePanel");
	}

	public void setTree_dianc_window(String id, String diancid,
			String submitor, String selectid, String expandedid) {
		tree = getTree_dianc(id, diancid);
		tree.getTreeRootNode().setExpanded(true);
		tree.setExpandedNodeid(expandedid);
		tree.setSelectedNodeid(selectid);
		// 构造按钮的handler
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() {\nvar cks = ")
				.append(id)
				.append(
						"_treePanel.getSelectionModel().getSelectedNode();\nif(cks==null){")
				.append(id)
				.append(
						"_window.hide();return;}\nvar obj0 = document.getElementById('")
				.append(id).append("_id');\nobj0.value = cks.id;\n").append(id)
				.append("_text.setValue(cks.text);\n").append(id).append(
						"_window.hide();\n");
		if (submitor != null && !"".equals(submitor)) {
			handler.append("document.").append(submitor).append(".submit()");
		}
		handler.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		// 构造承载按钮的toolbar
		Toolbar tb = new Toolbar(null);
		tb.addItem(btn);
		tree.getTreePanel().addBbar(tb);
		tree.getTreePanel().setRender("'body'");
		window = new Window(id);
		window.addItem(id + "_treePanel");
	}

	public String getScript() {
		StringBuffer sb = new StringBuffer();
		if (tree != null) {
			sb.append(tree.getScript());
		}
		if (window != null) {
			sb.append(window.getScript());
		}
		return sb.toString();
	}
}
