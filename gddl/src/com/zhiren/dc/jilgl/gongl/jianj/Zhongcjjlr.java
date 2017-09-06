package com.zhiren.dc.jilgl.gongl.jianj;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:王磊
 * 时间：2009-09-14 09：58
 * 描述：修改未设置车号头表关联不出信息的问题
 */
/*
 * 作者：王磊
 * 时间：2009-09-01 09：58
 * 描述：修改初始化称毛重SQL 解决qiccht表中表头名称部分重复造成检斤录入时出现多条信息的BUG
 * 优化原来的SQL 多次关联chepb会造成查询速度缓慢的问题  
 * 增加称重录入grid程序功能设置 关键字为 Zhongcjjlr_cz
 */
/*
 * 2009-05-13
 * 王磊
 * 处理车号头为空时不能自动关联上一车信息的问题
 */
/*
 * 2009-05-11
 * 王磊
 * 修改车号头选择可以为空
 */
public class Zhongcjjlr extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
//	grid1卸车方式的选项菜单
//	private String Grid1Html;
//	public void setGrid1Html(String Grid1Html){
//		this.Grid1Html=Grid1Html;
//	}
//	public String getGrid1Html(){
//		JDBCcon con=new JDBCcon();
//		Visit v=(Visit)this.getPage().getVisit();
//		ResultSet rs=con.getResultSet("select mingc from xiecfsb where "+Jilcz.filterDcid(v,null).substring(4)+" order by id");
//		StringBuffer sb=new StringBuffer();
//		sb.append("<select style='display:none' id='grid1select'>");
//		try {
//			while(rs.next()){
//			     sb.append("<option value='").append(rs.getString("mingc")).append("'>");
//			     sb.append(rs.getString("mingc"));
//			     sb.append("</option>");
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//		sb.append("</select>");
//		this.setGrid1Html(sb.toString());
//		return this.Grid1Html;
//	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean Suodzt(String cheph){
		boolean issuod=false;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps= null;
		ResultSet rs=null;
		StringBuffer sql= new StringBuffer();
			sql.append("select cl.islocked from chelxxb cl where cl.cheph='");
			sql.append(cheph).append("'");
		ps=con.getPresultSet(sql.toString());
	
		try {
			rs = ps.executeQuery();			
			while(rs.next()){
				if(rs.getString("islocked").equals("1")){
					issuod=true;			
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
		        ps.close();
		        con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		return issuod;
	}
	
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Zhongcjjlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
			if(Suodzt(rsl.getString("qiccht_id")+rsl.getString("cheph"))){
				setMsg("该车已被锁定,操作结束!");
				return;
			}
			long diancxxb_id = 0;
			if(visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
			}else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			sb.append("insert into cheplsb\n");
			sb.append("(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, cheph, maoz, biaoz, sanfsl, meicb_id, jihkjb_id, yunsdwb_id,\n");
			sb.append(" zhongcsj , zhongcjjy, zhongchh, faz_id, daoz_id, fahrq, daohrq, chec, yunsfsb_id, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, lury, beiz, caiyrq,xiecfsb_id)\n");
			sb.append("values (getnewid(").append(diancxxb_id).append("),").append(diancxxb_id).append(",");
			sb.append(((IDropDownModel)getGongysModel()).getBeanId(rsl.getString("gongysb_id"))).append(",");
			sb.append(((IDropDownModel)getMeikModel()).getBeanId(rsl.getString("meikxxb_id"))).append(",");
			sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id"))).append(",");
			sb.append("'").append(rsl.getString("qiccht_id")+rsl.getString("cheph")).append("',");
			sb.append(rsl.getString("maoz")).append(",");
			sb.append(rsl.getString("biaoz")).append(",");
			sb.append(rsl.getString("sanfsl")).append(",");
			sb.append((getExtGrid().getColumn("meicb_id").combo).getBeanId(rsl.getString("meicb_id"))).append(",");
			sb.append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id"))).append(",");
			sb.append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id"))).append(",");
			sb.append(DateUtil.FormatOracleDateTime(new Date())).append(",");
			sb.append("'").append(rsl.getString("zhongcjjy")).append("','").append(rsl.getString("zhongchh")).append("',");
			sb.append("1,1,").append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",");
			sb.append("to_date('2050-12-31','yyyy-mm-dd'),'").append(rsl.getString("chec")).append("',")
			.append(SysConstant.YUNSFS_QIY).append(",").append(SysConstant.CHEB_QC).append(",1,");
			sb.append(diancxxb_id).append(",'");
			sb.append(rsl.getString("meikxxb_id")).append("','").append(rsl.getString("zhongcjjy"));
			sb.append("','").append(rsl.getString("beiz")).append("',");
			sb.append("to_date('").append(DateUtil.FormatDate(new Date())).append("','yyyy-mm-dd'),")
			//wzb,修改卸车方式,和总厂相关联2009-4-13 17:09:11
			  .append("(select id from xiecfsb where mingc='"+rsl.getString("xiecfsb_id")+"' and diancxxb_id= "+visit.getDiancxxb_id()+")")
			  .append(")");
			//System.out.println(sb);
		}
		if(sb.length() == 0) {
			WriteLog.writeErrorLog(ErrorMessage.Zhongcjjlr001);
			setMsg(ErrorMessage.Zhongcjjlr001);
			con.rollBack();
			con.Close();
			return;
		}
		flag = con.getInsert(sb.toString());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+sb);
			setMsg(ErrorMessage.Zhongcjjlr002);
			return;
		}
		flag = Jilcz.Updatezlid(con, visit.getDiancxxb_id(), SysConstant.YUNSFS_QIY, null);
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Zhongcjjlr003);
			setMsg(ErrorMessage.Zhongcjjlr003);
			return;
		}
		flag = Jilcz.INSorUpfahb(con, visit.getDiancxxb_id());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Zhongcjjlr004);
			setMsg(ErrorMessage.Zhongcjjlr004);
			return;
		}
		flag = Jilcz.InsChepb(con, visit.getDiancxxb_id(), SysConstant.YUNSFS_QIY, SysConstant.HEDBZ_TJ);
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Zhongcjjlr005);
			setMsg(ErrorMessage.Zhongcjjlr005);
			return;
		}
		sb.delete(0, sb.length());
		sb.append("select distinct fahb_id from cheplsb");
		rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Zhongcjjlr006);
			setMsg(ErrorMessage.Zhongcjjlr006);
			return;
		}
		while (rsl.next()) {
			flag = Jilcz.updateLieid(con, rsl.getString("fahb_id"));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Zhongcjjlr006);
				setMsg(ErrorMessage.Zhongcjjlr006);
				return;
			}
		}
		con.commit();  
		con.Close();
		setMsg("保存成功！");
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String tempst="";
		
		StringBuffer sb = new StringBuffer();
		sb.append("select id, qiccht_id, cheph, diancxxb_id, gongysb_id, meikxxb_id,\n")
/**
 * huochaoyuan
 * 2009-11-23鉴于发货日期对于汽运煤关系不大，但选错又会引起编码错误，故初始为当天日期
 */		
		.append("pinzb_id,sysdate fahrq, maoz, biaoz, sanfsl, chec, xiecfsb_id, meicb_id,\n")
//end		
		.append("jihkjb_id, yunsdwb_id, zhongcjjy, zhongchh, beiz from (\n")
		.append("select 0 id, qi.mingc qiccht_id, '' cheph, d.mingc diancxxb_id,\n")
		.append("g.mingc gongysb_id, m.mingc meikxxb_id, p.mingc pinzb_id,\n")
		.append("f.fahrq, 0 maoz, 0 biaoz, 0 sanfsl, f.chec, x.mingc xiecfsb_id,\n")
		.append("mc.mingc meicb_id, j.mingc jihkjb_id, y.mingc yunsdwb_id,\n")
		.append("nvl('"+visit.getRenymc()+"', '') zhongcjjy, c.zhongchh, '' beiz from qiccht   qi,\n")
		.append("chepb    c, fahb     f, gongysb  g, meikxxb  m, pinzb    p,\n")
		.append("jihkjb   j, meicb    mc, yunsdwb  y, diancxxb d, xiecfsb  x\n")
		.append("where c.xiecfsb_id = x.id(+) and c.cheph like '%' || qi.mingc(+) || '%'\n")
		.append("and f.id = c.fahb_id and f.yunsfsb_id =").append(SysConstant.YUNSFS_QIY)
		.append("\n").append("and f.gongysb_id = g.id and f.meikxxb_id = m.id\n")
		.append("and f.diancxxb_id = d.id and f.pinzb_id = p.id and f.jihkjb_id = j.id\n")
		.append("and c.meicb_id = mc.id(+) and c.yunsdwb_id = y.id(+) order by c.id desc\n")
		.append(") where rownum = 1");
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		
		ExtGridUtil egu;
	
		if(!rsl.next()) {
			sb.delete(0, sb.length());
			sb.append("select 0 id,'' qiccht_id, '' cheph, '' diancxxb_id, '' gongysb_id, '' meikxxb_id, '' pinzb_id,\n");
			sb.append("sysdate fahrq,0 maoz, 0 biaoz, 0 sanfsl,'' chec,'' xiecfsb_id,'' meicb_id, '' jihkjb_id, '' yunsdwb_id,\n");
			sb.append("nvl('").append(visit.getRenymc()).append("','') zhongcjjy, '' zhongchh, '' beiz from dual\n");
			rsl = con.getResultSetList(sb.toString());
//			获取默认车号头
			String tempsql="select mingc from qiccht where zhuangt=1 order by xuh";
			ResultSetList temprsl=con.getResultSetList(tempsql);
			
			if(temprsl.next()){
				tempst=temprsl.getString("mingc");	 
			}
			temprsl.close();
		}else {
			rsl.beforefirst();
		}
		
		
		egu = new ExtGridUtil("gridDiv", rsl, "Zhongcjjlr_cz");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		//设置页面宽度
		
//		egu.setHeight(88);
		
		//egu.addColumn(gc);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		
		egu.getColumn("qiccht_id").setHeader("车头");
		egu.getColumn("qiccht_id").setWidth(80);
		egu.getColumn("qiccht_id").setEditor(null);
		egu.getColumn("qiccht_id").setDefaultValue(tempst);
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("cheph").setWidth(60);
		if(visit.isFencb()) {
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setEditor(null);
		}
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("maoz").editor.setAllowBlank(false);
		String sql = "select * from shuzhlfwb where leib ='数量' and mingc = '汽车衡毛重' and diancxxb_id = " + visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			egu.getColumn("maoz").editor.setMinValue(rsl.getString("xiax"));
			egu.getColumn("maoz").editor.setMaxValue(rsl.getString("shangx"));
		}
		
		//egu.getColumn("maoz").setEditor(null);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(50);
		((NumberField)egu.getColumn("biaoz").editor).setDecimalPrecision(3);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setWidth(50);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("chec").editor.allowBlank = false;
		egu.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("meicb_id").setWidth(80);
		egu.getColumn("meicb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdwb_id").setWidth(80);
		egu.getColumn("yunsdwb_id").setEditor(null);
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongchh").setHeader(Locale.zhongchh_chepb);
		egu.getColumn("zhongchh").setWidth(70);
		egu.getColumn("zhongchh").setEditor(null);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(90);
		/*
//		设置供应商下拉框
		ComboBox cgys= new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cgys);
		cgys.setEditable(true);
		String gysSql="select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(gysSql));
			
//		设置煤矿单位下拉框
		ComboBox cmk= new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cmk);
		cmk.setEditable(true);
		String mkSql="select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(mkSql));
		*/
//		设置车头下拉框
		ComboBox qic=new ComboBox();
		egu.getColumn("qiccht_id").setEditor(qic);
		qic.setEditable(true);
		String qiccSql="select id ,mingc  from qiccht where zhuangt=1 order by xuh ";
		/**
		* huochaoyuan2009-02-18修改上边取数的排列方法，原来的order by mingc,改为order by xuh,顺序可由用户控制；
		*/		
		egu.getColumn("qiccht_id").setComboEditor(egu.gridId, new IDropDownModel(qiccSql));
		egu.getColumn("qiccht_id").editor.allowBlank = true;
//		设置品种下拉框
		ComboBox cpz=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(cpz);
		cpz.setEditable(true);
		String pinzSql=SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
		
//		设置口径下拉框
		ComboBox ckj=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(ckj);
		ckj.setEditable(true);
		String jihkjSql=SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
		
//		运输单位
		ComboBox cysdw = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(cysdw);
		cysdw.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="
/**
 * huochaoyuan 
 * 2009-11-23运输单位beiz字段排序，用户可控显示顺序，录入简便，长期不用的放置后方
 */			
				+ visit.getDiancxxb_id()+" order by beiz asc";
//end		
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,new IDropDownModel(yunsdwSql));
//      卸车方式下拉框和表头的设置
		egu.getColumn("xiecfsb_id").setHeader("卸车方式");
		ComboBox c11=new ComboBox();
		c11.setTransform("xiecfsSe");
	//	String XiecfsSql="select id,mingc from xiecfsb where diancxxb_id = "+visit.getDiancxxb_id()+" order by id";
		  String XiecfsSql="select id,mingc from xiecfsb where "+Jilcz.filterDcid(visit,null).substring(4)+" order by id";
		c11.setEditable(true);
		egu.getColumn("xiecfsb_id").setEditor(c11);
		egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,new IDropDownModel(XiecfsSql));		
//		煤场
		ComboBox cmc = new ComboBox();
		egu.getColumn("meicb_id").setEditor(cmc);
		cmc.setEditable(true);
		String cmcSql = "select id, mingc from meicb";
		egu.getColumn("meicb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cmcSql));
//		设置按钮
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+"row = irow; \n"
				+"if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"gongysTree_window.show();}});");
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){ "
				+"if(e.field == 'MAOZ'){e.cancel=true;}});\n");
		setExtGrid(egu);
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_kj,"gongysTree"
				,""+visit.getDiancxxb_id(),null,null,null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
		.append("function() { \n")
		.append(
				"var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();return;} \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("if(cks.getDepth() == 3){\n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.text);\n")
		.append("rec.set('MEIKXXB_ID', cks.parentNode.text);\n")
		.append("rec.set('JIHKJB_ID', cks.text);\n")
		.append("}else if(cks.getDepth() == 2){\n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.text);\n")
		.append("rec.set('MEIKXXB_ID', cks.text);\n")
		.append("}else if(cks.getDepth() == 1){\n")
		.append("rec.set('GONGYSB_ID', cks.text); }\n")
		.append("gongysTree_window.hide();\n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		setDefaultTree(dt);
		
		con.Close();
		
		sb.delete(0, sb.length());
		sb.append("select c.id, c.cheph, d.mingc diancxxb_id, g.mingc gongysb_id, m.mingc meikxxb_id, p.mingc pinzb_id,\n");
		sb.append("f.fahrq, c.maoz, c.biaoz, c.sanfsl,f.chec,")
		        .append("x.mingc xiecfsb_id")
				.append(", mc.mingc meicb_id, j.mingc jihkjb_id, \n");
		sb.append("y.mingc yunsdwb_id, c.zhongcjjy, c.zhongchh, c.beiz\n");
		sb.append("from chepb c, fahb f, gongysb g, meikxxb m, pinzb p, jihkjb j, meicb mc, yunsdwb y, diancxxb d,xiecfsb x\n");
		sb.append("where f.id = c.fahb_id and c.piz = 0 and x.id(+) = c.xiecfsb_id and f.yunsfsb_id=")
		.append(SysConstant.YUNSFS_QIY).append(" and c.qingcsj is  null\n");
		/**
		 * 2008-10-26
		 * huochaoyuan
		 * 上边c.zhongcsj is not null改为c.qingcsj is  null，重车检斤页面只显示没有称皮重的数据
		 */
		sb.append("and f.gongysb_id = g.id and f.meikxxb_id = m.id and f.diancxxb_id = d.id\n");
		sb.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id\n");
		sb.append("and c.meicb_id = mc.id(+) and c.yunsdwb_id = y.id(+) order by c.zhongcsj desc\n");
		ResultSetList rsl1 = con.getResultSetList(sb.toString());
		if (rsl1 == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl1);
		//设置页面宽度
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		egu1.setHeight("bodyHeight-206");
		egu1.addPaging(14);
		egu1.getColumn("id").setHidden(true);
		egu1.getColumn("id").editor=null;
		egu1.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu1.getColumn("cheph").setWidth(60);
		egu1.getColumn("diancxxb_id").setHidden(true);
		egu1.getColumn("diancxxb_id").setEditor(null);
		egu1.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu1.getColumn("gongysb_id").setWidth(100);
		egu1.getColumn("gongysb_id").setEditor(null);
		egu1.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu1.getColumn("meikxxb_id").setWidth(100);
		egu1.getColumn("meikxxb_id").setEditor(null);
		egu1.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu1.getColumn("pinzb_id").setWidth(60);
		egu1.getColumn("pinzb_id").setEditor(null);
		egu1.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu1.getColumn("fahrq").setWidth(70);
		egu1.getColumn("fahrq").setEditor(null);
		egu1.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu1.getColumn("maoz").setWidth(50);
		egu1.getColumn("maoz").setEditor(null);
		egu1.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu1.getColumn("biaoz").setWidth(50);
		egu1.getColumn("biaoz").setEditor(null);
		egu1.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu1.getColumn("sanfsl").setWidth(50);
		egu1.getColumn("sanfsl").setEditor(null);
		egu1.getColumn("chec").setHeader(Locale.chec_fahb);
		egu1.getColumn("chec").setWidth(60);
		egu1.getColumn("chec").setEditor(null);
		egu1.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu1.getColumn("meicb_id").setWidth(80);
		egu1.getColumn("meicb_id").setEditor(null);
		egu1.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu1.getColumn("jihkjb_id").setWidth(80);
		egu1.getColumn("jihkjb_id").setEditor(null);
		egu1.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu1.getColumn("yunsdwb_id").setWidth(80);
		egu1.getColumn("yunsdwb_id").setEditor(null);
		egu1.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu1.getColumn("zhongcjjy").setWidth(80);
		egu1.getColumn("zhongcjjy").setEditor(null);
		egu1.getColumn("zhongchh").setHeader(Locale.zhongchh_chepb);
		egu1.getColumn("zhongchh").setWidth(70);
		egu1.getColumn("zhongchh").setEditor(null);
		egu1.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu1.getColumn("beiz").setWidth(90);
		egu1.getColumn("beiz").setEditor(null);
//      卸车方式下拉框和表头的设置
		egu1.getColumn("xiecfsb_id").setHeader("卸车方式");
		egu1.getColumn("xiecfsb_id").setWidth(90);
//		ComboBox c10=new ComboBox();
//		   c10.setTransform("grid1select");
//		   //Visit vi=(Visit)this.getPage().getVisit();
//		   //XiecfsSql="select id,mingc from xiecfsb where "+Jilcz.filterDcid(vi,null).substring(4)+" order by id";
//		   c10.setEditable(true);
//		   egu1.getColumn("xiecfsb_id").setEditor(c10);
		setPizGrid(egu1);
		con.Close();
		
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}
	
	public String getGridScriptPiz() {
		if (getExtGrid() == null) {
			return "";
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public DefaultTree getDefaultTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}
	
	public void setDefaultTree(DefaultTree dftree1) {
		((Visit) this.getPage().getVisit()).setDefaultTree(dftree1);
	}
	
	public String getTreeScript() {
//		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return getDefaultTree().getScript();
	}
   
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}
	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}
	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			init();
		}
	} 
	
	private void init() {
		setGongysModels();
		setMeikModels();
		setExtGrid(null);
		setPizGrid(null);
		setDefaultTree(null);
		getSelectData();
	}
}