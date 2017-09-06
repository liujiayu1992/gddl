package com.zhiren.common.ext;
/*
 * chh 2008-09-25 id 
 */
/* 2008-06-10
* zsj
* 重构ExtTreeUtil方法，加入对checkbox设置
*/
/*
 * 作者：夏峥
 * 时间：2011-05-27
 * 描述：修正在构建树时由于int型变量长度不够而导致的转换错误。
 * 		修正方法为将int型变量更新为long型变量
 */

import java.io.Serializable;

import org.apache.tapestry.IPage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.tree.TreeButton;
import com.zhiren.common.ext.tree.TreeNode;

/**
 * @author 王磊
 *
 */
public class ExtTreeUtil implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7020946077527159829L;

	public static final int treeDefaultType_Dianc = 0;
	public static final int treeWindowType_Dianc = 10;
	public static final int treeWindowCheck_Dianc = 11;
	public static final int treeWindowCheck_chez = 12;
	public static final int treeWindowCheck_gongys = 13;
	public static final int treeWindowCheck_gongys_jies = 14;//结算表中供应商
	public static final int treeWindowType_Jit_Fengs = 15; //集团_分公司_树形查询
	public static final int treeWindowCheck_gongys_jieszb = 16;//结算子表(jieszb、jiesyfzb)中的供应商，这两个表在山西阳城电厂拆分结算单时使用
	public static final int treeWindowCheck_yunsdw_datong = 17;//大同亏吨亏卡查询中用到的运输单位复选框
	public static final int treeWindowCheck_meikxx = 18;

	public String treeId;
	public TreeNode treeRootNode;

	public boolean animate;
	public boolean line;
	public boolean enableDD;
	public boolean rootVisible;
	public boolean autoScroll;
	public boolean onlyleafcheckable;

	public int height;
	public int width;

	public String title;
	public String tbar;
	public String bbar;
	public String defaultSelectid;

	//public List buttons;

	public Window window;


	public String treeJsCode = "";
	public String treeJsDs = "";

	public void init() {
		this.animate = true;
		this.line = true;
		this.enableDD = false;
		this.rootVisible = true;
		this.autoScroll = true;
	}

	public ExtTreeUtil() {
	}

	public ExtTreeUtil(String treeId,int treeDefaultType,long diancxxb_id,String defaultSelectid) {
		this.defaultSelectid = defaultSelectid;
		StringBuffer sb = new StringBuffer();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String sql = "";
		switch(treeDefaultType) {
			case treeDefaultType_Dianc:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				sql = sb.toString();
				if(con.getHasIt("select * from diancxxb where ranlgs = 1 and id="+diancxxb_id)) {
					sql = sql.replaceAll("fuid", "shangjgsid");
				}
				rsl = con.getResultSetList(sql);

				getDefaultTree(treeId,rsl,true);
				this.addBbarButton(TreeButton.ButtonType_Ok, "SaveButton");
				this.setWidth(200);
				this.setTitle("单位选择");
				break;
			case treeWindowType_Dianc:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				sql = sb.toString();
				if(con.getHasIt("select * from diancxxb where ranlgs = 1 and id="+diancxxb_id)) {
					sql = sql.replaceAll("fuid", "shangjgsid");
				}
				rsl = con.getResultSetList(sql);
				getDefaultTree(treeId,rsl,false);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowType_Jit_Fengs:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb where jib not in (3)\n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				sql = sb.toString();
				if(con.getHasIt("select * from diancxxb where ranlgs = 1 and id="+diancxxb_id)) {
					sql = sql.replaceAll("fuid", "shangjgsid");
				}
				rsl = con.getResultSetList(sql);
				getDefaultTree(treeId,rsl,false);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_Dianc:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,true);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Checked_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_gongys:
				sb.append("select id,mingc,level jib from \n").append("(select 0 id,'全部' mingc,-1 fuid from dual union \n")
				.append("select gys.id,gys.mingc,0 fuid from gongysb gys,gongysdcglb gysgl \n").append(" where gys.id=gysgl.gongysb_id and gysgl.diancxxb_id="+diancxxb_id+" union \n")
				.append("select m.id,m.mingc,glb.gongysb_id as fuid \n").append(" from meikxxb m,gongysmkglb glb,gongysb gys,gongysdcglb glbdc \n")
				.append(" where m.id=glb.meikxxb_id and glb.gongysb_id=gys.id and gys.id=glbdc.gongysb_id and glbdc.diancxxb_id="+diancxxb_id+")")
				.append("where fuid = -1 or (fuid=0 and level=2) or (fuid>0 and level =3) \n").append("connect by NOCYCLE fuid = PRIOR id").append(" order SIBLINGS by mingc");;
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,false);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_chez:
				sb.append("select id,mingc,level jib from \n").append("(select 0 id,'根' mingc,-1 fuid from dual union \n")
				.append("select id,mingc,0 fuid from lujxxb union \n").append("select id,mingc,lujxxb_id fuid from chezxxb) \n")
				.append("where fuid = -1 or (fuid=0 and level=2) or (fuid>0 and level =3) \n").append("connect by  fuid = PRIOR id");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,false);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.setOnlyleafcheckable(true);
				this.setWidth(200);
				this.setRootVisible(false);
				break;
			default: sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id = -1 \n")
			.append("connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
			rsl = con.getResultSetList(sb.toString());
			getDefaultTree(treeId,rsl,true);
			this.addBbarButton(TreeButton.ButtonType_Ok, null);
			break;
		}
		con.Close();

	}


	public ExtTreeUtil(String treeId,int treeDefaultType,long diancxxb_id,String defaultSelectid,String condition) {
		this.defaultSelectid = defaultSelectid;
		StringBuffer sb = new StringBuffer();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		switch(treeDefaultType) {
			case treeDefaultType_Dianc:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,true);
				this.addBbarButton(TreeButton.ButtonType_Ok, "SaveButton");
				this.setWidth(200);
				this.setTitle("单位选择");
				break;
			case treeWindowType_Dianc:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,false);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_Dianc:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,true);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Checked_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_gongys:
				sb.append("select id,mingc,level jib from \n").append("(select 0 id,'全部' mingc,-1 fuid from dual union \n")
				.append("select distinct gys.id,gys.mingc,0 fuid from gongysb gys,gongysdcglb gysgl,fahb fh \n").append(" where gys.id=gysgl.gongysb_id and gysgl.diancxxb_id="+diancxxb_id+" \n")
				.append(" and gys.id=fh.gongysb_id  and gys.leix=1 ").append(condition+" \n")
				.append("union \n")
				.append("select distinct m.id,m.mingc,glb.gongysb_id as fuid \n").append(" from meikxxb m,gongysmkglb glb,gongysb gys,gongysdcglb glbdc,fahb fh  \n")
				.append(" where m.id=glb.meikxxb_id and glb.gongysb_id=gys.id and gys.id=glbdc.gongysb_id and glbdc.diancxxb_id="+diancxxb_id+" and fh.meikxxb_id=m.id and fh.gongysb_id=gys.id  and gys.leix=1\n")
				.append(condition+") \n")
				.append("where fuid = -1 or (fuid=0 and level=2) or (fuid>0 and level =3) \n").append("connect by NOCYCLE fuid = PRIOR id").append(" order SIBLINGS by mingc");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,false);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_chez:
				sb.append("select id,mingc,level jib from \n").append("(select 0 id,'根' mingc,-1 fuid from dual union \n")
				.append("select id,mingc,0 fuid from lujxxb union \n").append("select id,mingc,lujxxb_id fuid from chezxxb) \n")
				.append("where fuid = -1 or (fuid=0 and level=2) or (fuid>0 and level =3) \n").append("connect by  fuid = PRIOR id");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,false);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.setOnlyleafcheckable(true);
				this.setWidth(200);
				this.setRootVisible(false);
				break;
			case treeWindowCheck_gongys_jies:
				sb.append("select id, mingc, level jib\n from (select 0 id, '全部' mingc, -1 fuid \n")
				.append("          from dual\n         union\n        select distinct gys.id, gys.mingc, 0 fuid\n" )
				.append("          from gongysb gys, gongysdcglb gysgl,\n               (select gongysb_id from jiesb\n")
				.append("                       where\n     ")
				.append(condition)
				.append("\n                       union\n")
				.append("                select gongysb_id from jiesyfb\n                       where\n")
				.append(condition)
				.append("                ) js\n         where gys.id = gysgl.gongysb_id\n               and (gysgl.diancxxb_id = "+diancxxb_id+"\n")
				.append("                   or gysgl.diancxxb_id in\n                      (select id from diancxxb where fuid="+diancxxb_id+"))\n")
				.append("           and gys.id = js.gongysb_id\n        union\n        select distinct m.id, m.mingc, glb.gongysb_id as fuid\n")
				.append("          from meikxxb     m,\n               gongysmkglb glb,\n               gongysb     gys,\n               gongysdcglb glbdc,\n")
				.append("               (select gongysb_id,meikxxb_id from jiesb\n                       where\n")
				.append(condition)
				.append("                       union\n                select gongysb_id,meikxxb_id from jiesyfb\n")
				.append("                       where\n")
				.append(condition)
				.append("                ) js\n         where m.id = glb.meikxxb_id\n           and glb.gongysb_id = gys.id\n           and gys.id = glbdc.gongysb_id\n")
				.append("           and glbdc.diancxxb_id = "+diancxxb_id+"\n                   or glbdc.diancxxb_id in\n                      (select id from diancxxb where fuid="+diancxxb_id+")\n")
				.append("           and js.meikxxb_id = m.id\n           and js.gongysb_id = gys.id\n       )\n where fuid = -1\n    or (fuid = 0 and level = 2)\n    or (fuid > 0 and level = 3)\n")
				.append("connect by NOCYCLE fuid = PRIOR id\n order SIBLINGS by mingc");

				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,false);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;
			default: sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id = -1 \n")
			.append("connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
			rsl = con.getResultSetList(sb.toString());
			getDefaultTree(treeId,rsl,true);
			this.addBbarButton(TreeButton.ButtonType_Ok, null);
			break;
		}
		con.Close();

	}

	public ExtTreeUtil(String treeId,int treeDefaultType,long diancxxb_id,String defaultSelectid,String condition,boolean checkbox) {
//		重构原方法，加入checkbox参数，用于控制数的checkbox显示
		this.defaultSelectid = defaultSelectid;
		StringBuffer sb = new StringBuffer();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		switch(treeDefaultType) {
			case treeDefaultType_Dianc:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,checkbox);
				this.addBbarButton(TreeButton.ButtonType_Ok, "SaveButton");
				this.setWidth(200);
				this.setTitle("单位选择");
				break;
			case treeWindowType_Dianc:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,checkbox);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_Dianc:
				sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
				.append(diancxxb_id).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,checkbox);
				getRootNode().setCheckbox(true);//当模式为电厂多选树时 根节点 可选
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Checked_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_gongys:
				sb.append("select id,mingc,level jib from \n").append("(select 0 id,'全部' mingc,-1 fuid from dual union \n")
				.append("select distinct gys.id,gys.mingc,0 fuid from gongysb gys,gongysdcglb gysgl,fahb fh \n").append(" where gys.id=gysgl.gongysb_id and gysgl.diancxxb_id="+diancxxb_id+" \n")
				.append(" and gys.id=fh.gongysb_id and gys.leix=1 ").append(condition+" \n")
				.append("union \n")
				.append("select distinct m.id,m.mingc,glb.gongysb_id as fuid \n").append(" from meikxxb m,gongysmkglb glb,gongysb gys,gongysdcglb glbdc,fahb fh  \n")
				.append(" where m.id=glb.meikxxb_id and glb.gongysb_id=gys.id and gys.id=glbdc.gongysb_id and glbdc.diancxxb_id="+diancxxb_id+" and fh.meikxxb_id=m.id and fh.gongysb_id=gys.id and gys.leix=1 \n")
				.append(condition+") \n")
				.append("where fuid = -1 or (fuid=0 and level=2) or (fuid>0 and level =3) \n").append("connect by NOCYCLE fuid = PRIOR id").append(" order SIBLINGS by mingc");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,checkbox);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_gongys_jieszb:
				String table = condition.substring(0, condition.indexOf("&"));
				String gys_where = condition.substring(condition.indexOf("&")+1, condition.indexOf("&&"));
				String mk_where = condition.substring(condition.indexOf("&&")+2, condition.length());
				sb.append("select id,mingc,level jib from \n").append("(select 0 id,'全部' mingc,-1 fuid from dual union \n")
				.append("select distinct gys.id,gys.mingc,0 fuid from gongysb gys,gongysdcglb gysgl,"+ table +" \n").append(" where gys.id=gysgl.gongysb_id and gysgl.diancxxb_id="+diancxxb_id+" \n")
				.append(" and gys.leix=1 ").append(gys_where+" \n")
				.append("union \n")
				.append("select distinct m.id,m.mingc,glb.gongysb_id as fuid \n").append(" from meikxxb m,gongysmkglb glb,gongysb gys,gongysdcglb glbdc,"+ table +"  \n")
				.append(" where m.id=glb.meikxxb_id and glb.gongysb_id=gys.id and gys.id=glbdc.gongysb_id and glbdc.diancxxb_id="+diancxxb_id+" and gys.leix=1 \n")
				.append(mk_where+") \n")
				.append("where fuid = -1 or (fuid=0 and level=2) or (fuid>0 and level =3) \n").append("connect by NOCYCLE fuid = PRIOR id").append(" order SIBLINGS by mingc");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,checkbox);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_chez:
				sb.append("select id,mingc,level jib from \n").append("(select 0 id,'根' mingc,-1 fuid from dual union \n")
				.append("select id,mingc,0 fuid from lujxxb union \n").append("select id,mingc,lujxxb_id fuid from chezxxb) \n")
				.append("where fuid = -1 or (fuid=0 and level=2) or (fuid>0 and level =3) \n").append("connect by  fuid = PRIOR id");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,checkbox);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.setOnlyleafcheckable(true);
				this.setWidth(200);
				this.setRootVisible(false);
				break;
			case treeWindowCheck_gongys_jies:
				sb.append("select id, mingc, level jib\n from (select 0 id, '全部' mingc, -1 fuid \n")
				.append("          from dual\n         union\n        select distinct gys.id, gys.mingc, 0 fuid\n" )
				.append("          from gongysb gys, gongysdcglb gysgl,\n               (select gongysb_id from jiesb\n")
				.append("                       where\n     ")
				.append(condition)
				.append("\n                       union\n")
				.append("                select gongysb_id from jiesyfb\n                       where\n")
				.append(condition)
				.append("                ) js\n         where gys.id = gysgl.gongysb_id\n               and (gysgl.diancxxb_id = "+diancxxb_id+"\n")
				.append("                   or gysgl.diancxxb_id in\n                      (select id from diancxxb where fuid="+diancxxb_id+"))\n")
				.append("           and gys.id = js.gongysb_id\n        union\n        select distinct m.id, m.mingc, glb.gongysb_id as fuid\n")
				.append("          from meikxxb     m,\n               gongysmkglb glb,\n               gongysb     gys,\n               gongysdcglb glbdc,\n")
				.append("               (select gongysb_id,meikxxb_id from jiesb\n                       where\n")
				.append(condition)
				.append("                       union\n                select gongysb_id,meikxxb_id from jiesyfb\n")
				.append("                       where\n")
				.append(condition)
				.append("                ) js\n         where m.id = glb.meikxxb_id\n           and glb.gongysb_id = gys.id\n           and gys.id = glbdc.gongysb_id\n")
				.append("           and glbdc.diancxxb_id = "+diancxxb_id+"\n                   or glbdc.diancxxb_id in\n                      (select id from diancxxb where fuid="+diancxxb_id+")\n")
				.append("           and js.meikxxb_id = m.id\n           and js.gongysb_id = gys.id\n       )\n where fuid = -1\n    or (fuid = 0 and level = 2)\n    or (fuid > 0 and level = 3)\n")
				.append("connect by NOCYCLE fuid = PRIOR id\n order SIBLINGS by mingc");

				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,checkbox);
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Ok, null);
				this.setWidth(200);
				break;

			case treeWindowCheck_yunsdw_datong:

				sb.append("select id,  mingc||' -  '||decode(diancxxb_id,301,'一期',302,'二期',303,'三期'), level jib\n");
				sb.append("  from (select 0 id, '全部' mingc, -1 fuid,0 as diancxxb_id from dual\n");
				sb.append("        union  select id, mingc, 0 fuid,diancxxb_id from yunsdwb "+condition+")\n");
				sb.append(" where fuid = -1 or (fuid = 0 and level = 2)\n");
				sb.append(" connect by fuid = PRIOR id  order by jib, mingc");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,checkbox);
				getRootNode().setCheckbox(true);//当模式为运输单位多选树时 根节点 可选
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Checked_Ok, null);
				this.setWidth(200);
				break;
			case treeWindowCheck_meikxx:
				sb.append("select id,  mingc, level jib\n");
				sb.append("  from (select 0 id, '全部' mingc, -1 fuid  from dual\n");
				sb.append("        union  select id, mingc, 0 fuid from meikxxb "+condition+")\n");
				sb.append(" where fuid = -1 or (fuid = 0 and level = 2)\n");
				sb.append(" connect by fuid = PRIOR id  order by jib, mingc");
				rsl = con.getResultSetList(sb.toString());
				getDefaultTree(treeId,rsl,checkbox);
				getRootNode().setCheckbox(true);//当模式为煤矿单位多选树时 根节点 可选
				window = new Window(treeId);
				window.setItems(treeId+"_treePanel");
				this.addBbarButton(TreeButton.ButtonType_Window_Checked_Ok, null);
				this.setWidth(200);
				break;
			default: sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id = -1 \n")
			.append("connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
			rsl = con.getResultSetList(sb.toString());
			getDefaultTree(treeId,rsl,checkbox);
			this.addBbarButton(TreeButton.ButtonType_Ok, null);
			break;
		}
		con.Close();

	}

	public void getDefaultTree(String treeId,ResultSetList rsl,boolean checkbox) {
		this.treeId = treeId;
		init();
		TreeNode parentNode = null;
		TreeNode RootNode = null;
		int lastjib = 0;
		while(rsl.next()) {
			int curjib = rsl.getInt(2);
			TreeNode node = new TreeNode(rsl.getString(0),rsl.getString(1));
			node.setCheckbox(checkbox);
			if(parentNode==null) {
				RootNode = node;
				node.setCheckbox(false);
				parentNode = node;
				lastjib = curjib+1;
				continue;
			}
			if(lastjib < curjib) {
				parentNode = (TreeNode)parentNode.getLastChild();
			}else if(lastjib > curjib){
				for(int i=0;i<lastjib - curjib;i++)
					parentNode = (TreeNode)parentNode.getParentNode();
			}
			lastjib = curjib;
			parentNode.appendChild(node);
		}
		this.setRootNode(RootNode);
		//this.setWidth(200);
		//this.addButton(TreeButton.ButtonType_Ok, "SaveButton");
		//this.setTitle("单位选择");
	}

	public ExtTreeUtil(String treeId) {
		this.treeId = treeId;
		init();
	}

	public boolean isAnimate() {
		return animate;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
	}

	public boolean isAutoScroll() {
		return autoScroll;
	}

	public void setAutoScroll(boolean autoScroll) {
		this.autoScroll = autoScroll;
	}

	public boolean isEnableDD() {
		return enableDD;
	}

	public void setEnableDD(boolean enableDD) {
		this.enableDD = enableDD;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isLine() {
		return line;
	}

	public void setLine(boolean line) {
		this.line = line;
	}

	public boolean isRootVisible() {
		return rootVisible;
	}

	public void setRootVisible(boolean rootVisible) {
		this.rootVisible = rootVisible;
	}

	public boolean isOnlyleafcheckable() {
		return onlyleafcheckable;
	}

	public void setOnlyleafcheckable(boolean onlyleafcheckable) {
		this.onlyleafcheckable = onlyleafcheckable;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getBbar() {
		return bbar==null?"":bbar;
	}

	public void setBbar(String bbar) {
		this.bbar = bbar;
	}

	public String getTbar() {
		return tbar==null?"":tbar;
	}

	public void setTbar(String tbar) {
		this.tbar = tbar;
	}

	public TreeNode getRootNode() {
		return this.treeRootNode;
	}

	public void setRootNode(TreeNode treeRootNode) {
		this.treeRootNode = treeRootNode;
	}

	/**
	 * @param ButtonType 按钮类型
	 * @param tapestryBtnId 对应的tapestry按钮ID
	 */
	public void addBbarButton(int ButtonType,String tapestryBtnId) {
		TreeButton btn = new TreeButton(ButtonType,treeId,tapestryBtnId);
		addBbarButton(btn);
	}

	public void addBbarButton(TreeButton btn) {
		addBbarItem("{"+btn.getScript()+"}");
	}
	public void addBbarItem(String item) {
		StringBuffer sb = new StringBuffer();
		sb.append(getBbar()).append(item).append(",");
		setBbar(sb.toString());
	}

	public void addTbarItem(String item) {
		StringBuffer sb = new StringBuffer();
		sb.append(getTbar()).append("{").append(item).append("},");
		setTbar(sb.toString());
	}

	/**
	 * @return 返回tbar 的Script
	 */
	private String getTbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length()-1);
		tbarScript.append("]");
		return tbarScript.toString();
	}

	/**
	 * @return 返回bbar 的Script
	 */
	private String getBbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("bbar: [");
		tbarScript.append(getBbar());
		tbarScript.deleteCharAt(tbarScript.length()-1);
		tbarScript.append("]");
		return tbarScript.toString();
	}

	public String getTreeDataScript() {
		if(this.treeRootNode == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		getNodeScript(sb,this.treeRootNode);

		return sb.toString();
	}

	public void getNodeScript(StringBuffer sb,TreeNode node) {
		while(node != null) {
			if(isOnlyleafcheckable()) {
				node.checkbox = node.isLeaf();
			}
			String pathid = treeId + node.getPathID() ;
			sb.append("var ").append(pathid).append(node.getId()).append(" = ").append(node.getScript()).append(";\n");
			if(node.getParentNode() != null) {
				sb.append("\t").append(pathid).append(".appendChild([").append(pathid).append(node.getId()).append("]);\n");
			}
			TreeNode n=(TreeNode)node.getFirstChild();
			getNodeScript(sb,n);
			node = (TreeNode)node.getNextSibling();
		}
		return ;
	}

	public String getDefaultSelectScript() {
		if(defaultSelectid==null || "".equals(defaultSelectid)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		TreeNode tn = (TreeNode)getRootNode().getNodeById(defaultSelectid);
		if(tn == null){
			return "";
		}
		sb.append(treeId).append(tn.getPathID()).append(defaultSelectid).append(".select();");
		return sb.toString();
	}

	public void setDefaultChecked(String nodeids) {
		String[] nodeid = nodeids.split(",");
		for(int i=0;i<nodeid.length ; i++) {
			((TreeNode)getRootNode().getNodeById(nodeid[i])).setCheckbox(true);
		}
		//setNodeCheckBox(nodeid,this.treeRootNode,0);
	}

	public void setNodeCheckBox(String[] nodeid,TreeNode node,int i) {
		while(node != null) {
			if(i>=nodeid.length) {
				return;
			}
			if(nodeid[i].equals(node.getId())) {
				//node.checkbox = true;
				node.checked = true;
				i++;
			}
			TreeNode n=(TreeNode)node.getFirstChild();
			setNodeCheckBox(nodeid,n,i);
			node = (TreeNode)node.getNextSibling();
		}
		return ;
	}

	public String getTreePanelScript() {
		StringBuffer sb = new StringBuffer();
		sb.append("var ").append(treeId).append("_treePanel = new Ext.tree.TreePanel({");
		sb.append("el:'").append(treeId).append("',");
		sb.append("animate:").append(animate).append(",");
		sb.append("line:").append(line).append(",");
		sb.append("enableDD:").append(enableDD).append(",");
		sb.append("rootVisible:").append(rootVisible).append(",");
		sb.append(title==null?"":"title:'"+title+"',");
		sb.append(width==0?"":"width:"+width+",");
		sb.append(height==0?"":"height:"+height+",");

		sb.append("root:").append(treeId).append(treeRootNode.getPathID()).append(this.treeRootNode.getId()).append(",");
		sb.append(getBbar().length()>0?getBbarScript()+",":"");
		sb.append(getTbar().length()>0?getTbarScript()+",":"");

		sb.append("autoScroll:").append(autoScroll).append("});");
		sb.append(treeId).append(this.treeRootNode.getId()).append(".expanded = true;\n");
		sb.append(treeId).append("_treePanel.render();");
		sb.append(getDefaultSelectScript());

		return sb.toString();
	}

	public String getTreeScript() {
		StringBuffer sb = new StringBuffer();

		sb.append(getTreeDataScript()).append("\n").append(getTreePanelScript());
		return sb.toString();
	}

	public String getWindowTreeScript() {
		StringBuffer sb = new StringBuffer();

		sb.append(getTreeDataScript()).append("\n").append(getTreePanelScript()).append("\n");
		sb.append(window.getScript()).append("\n");
		//sb.append("var ").append(treeId).append("_w_d = ").append("Ext.get('").append(treeId).append("_w_d');");
		//sb.append(treeId).append("_w_d.on('click', function(){");

		//sb.append(treeId).append("_window.").append("show();");

		//sb.append("});");
		return sb.toString();
	}

	public String getWindowTreeHtml(IPage page) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='").append(treeId).append("_window' class='x-hidden'>");
		//sb.append("<div class='x-window-header'>单位选择</div>");
		sb.append("<div id = '").append(treeId).append("'></div></div>");
//		sb.append("<div class='x-form-field-wrap' id='").append(treeId).append("_w_d' style='widht:149'>");
//		sb.append("<input class='x-form-text x-form-field ' style='width:124px;' readonly=true value='选择...' id='").append(treeId).append("_win_input'/>");
//
//		sb.append("<IMG class='x-form-trigger x-form-arrow-trigger' src='").append(MainGlobal.getHomeContext(page)).append("/ext/resources/images/s.gif'/>");
//		sb.append("</div>");
		return sb.toString();
	}

	public String getDataset() {
		return treeJsDs;
	}


	/**
	 * @param sql id,name,fuid,childs[,checked]
	 * 			childs 说明此节点的孩子数
	 * 			checked 可选项，说明树是否可选中 0为不选中 1为选中
	 * @param rootName 如果 rootName 不为空 则增加一个默认的根
	 */
	public void setTreeDs(String sql,String rootName) {
		if(rootName != null || !rootName.equals("")) {
			treeJsDs = "var node0 = new Ext.tree.TreeNode({id:'0',text:'"+rootName+"'});\n";
		}
		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.getColumnCount() == 4) {
			while(rs.next()) {
				long id = rs.getLong(0);
				String text = rs.getString(1);
				long fuid = rs.getLong(2);
				long childs = rs.getLong(3);
				treeJsDs += "var node"+id+" = new Ext.tree.TreeNode({id:'"
				+id+"',text:'"+text+"'"+(childs>0?"":",leaf:true")+"});\n";
				treeJsDs += "node"+fuid+ ".appendChild([node"+id+"]);\n";
			}
		}else
			if(rs.getColumnCount() == 5) {
				while(rs.next()) {
					long id = rs.getLong(0);
					String text = rs.getString(1);
					long fuid = rs.getLong(2);
					long childs = rs.getLong(3);
					long checked = rs.getLong(4);
					treeJsDs += "var node"+id+" = new Ext.tree.TreeNode({id:'"
					+id+"',text:'"+text+"'"+(childs>0?"":",leaf:true")
					+",checked:"+(checked==0?"false":"true")+"});\n";
					treeJsDs += "node"+fuid+ ".appendChild([node"+id+"]);\n";
				}
			}
		con.Close();
	}
		public void setTreeDs(String sql,String rootName,String fuxk) {
			if(rootName != null || !rootName.equals("")) {
				treeJsDs = "var node0 = new Ext.tree.TreeNode({id:'0',text:'"+rootName+"'});\n";
			}
			JDBCcon con = new JDBCcon();
			ResultSetList rs = con.getResultSetList(sql);
			if(rs.getColumnCount() == 4) {
				while(rs.next()) {
					long id = rs.getInt(0);
					String text = rs.getString(1);
					long fuid = rs.getInt(2);
					long childs = rs.getInt(3);
					treeJsDs += "var node"+id+" = new Ext.tree.TreeNode({id:'"
					+id+"',text:'"+text+"'"+(childs>0?"":",leaf:true")+"});\n";
					treeJsDs += "node"+fuid+ ".appendChild([node"+id+"]);\n";
				}
			}else
				if(rs.getColumnCount() == 5) {
					while(rs.next()) {
//						int id = rs.getInt(0);
						long id=rs.getLong(0);
						String text = rs.getString(1);
						long fuid = rs.getInt(2);
						long childs = rs.getInt(3);
						long checked = rs.getInt(4);
						treeJsDs += "var node"+id+" = new Ext.tree.TreeNode({id:'"
						+id+"',text:'"+text+"'"+(childs>0?"":",leaf:true")
						+"});\n";
						treeJsDs += "node"+fuid+ ".appendChild([node"+id+"]);\n";
					}
				}
			con.Close();
		}

}