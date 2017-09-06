package com.zhiren.dc.huaygl.huayhb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-07-06 19：13
 * 描述：数据保存时对核对标志的更新错误
 */
public class Huayhb extends BasePage implements PageValidateListener {
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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _CreateChick = false;
	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	
	private boolean _ComputerChick = false;
	public void Computer(IRequestCycle cycle) {
		_ComputerChick = true;
	}


	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList drsl = getExtGrid().getDeleteResultSet(Change);
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql_Update = new StringBuffer("");	//Update
		while (drsl.next()) {
			//删除
			sql.append("delete from " ).append(" huayhblb ").append(" where id=")
				.append(drsl.getString("ID")).append(";\n");
		}
		drsl.close();
//		添加、修改
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(Change);
		while (mdrsl.next()) {
				sql_Update.append(" update huayhblb ").append("set").append(" jingz =")
						.append(mdrsl.getString("JINGZ")).append(",")
						.append(" Qnet_ar=").append(mdrsl.getString("QNET_AR")).append(",")
						.append(" mt = ").append(mdrsl.getString("MT")).append(",")
						.append(" mad = ").append(mdrsl.getString("MAD")).append(",")
						.append(" aad = ").append(mdrsl.getString("AAD")).append(",")
						.append(" vad = ").append(mdrsl.getString("VAD")).append(",")
						.append(" stad = ").append(mdrsl.getString("STAD")).append(",")
						.append(" std = ").append(mdrsl.getString("STD")).append(",")
						.append(" star = ").append(mdrsl.getString("STAR")).append(",")
						.append(" had = ").append(mdrsl.getString("HAD")).append(",")
						.append(" qbad = ").append(mdrsl.getString("QBAD")).append(",")
						.append(" qgrad = ").append(mdrsl.getString("QGRAD"))
						.append(" where id=").append(mdrsl.getString("ID")).append(";\n");
		      }
		mdrsl.close();
		sql.append(sql_Update);
		sql.append("end;");
        if(sql.length()>13){
			if(con.getUpdate(sql.toString())>=0){
				
				setMsg("保存成功！");
			}else{
				
				setMsg("保存失败！");
			}
		}
		con.Close();
	}

	
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CreateChick) {
			_CreateChick = false;
			Create();
		}
		if(_ComputerChick){
			_ComputerChick=false;
			Computer();
		}
	}
	
	private String Parameters;

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}
	
	
	private String Parameters1;

	public String getParameters1() {
		
		return Parameters1;
	}

	public void setParameters1(String value) {
		
		Parameters1 = value;
	}
	
    public void Create(){
//    	double liuzb=Double.parseDouble(this.getParameters());
		JDBCcon con = new JDBCcon();
		String sql="select distinct zhilb_id,f.meikxxb_id as meikxxb_id, z.huaybh as bianm,f.jingz,f.daohrq,\n" +
        "   to_char(z.huaysj,'yyyy-mm-dd') as huaysj, \n"+
        "   z.qnet_ar,\n" + 
        "   z.aar,\n" + 
        "   z.ad,\n" + 
        "   z.vdaf,\n" + 
        "   z.mt,\n" + 
        "   z.stad as stad,\n"+
//        "   round_new(gethuayhbl(z.stad,"+liuzb+"),2) as stad,\n" + 
        "   z.aad,\n" + 
        "   z.mad,\n" + 
        "   z.qbad,\n" + 
        "   z.had,\n" + 
        "   z.vad,\n" + 
        "   z.fcad,\n" + 
        "   z.std,\n" + 
        "   z.qgrad,\n" + 
        "   z.hdaf,\n" + 
        "   z.qgrad_daf,\n" + 
        "   z.sdaf,\n" + 
        "   z.HUAYY,z.LURY,\n" + 
        "   z.STAR\n" + 
        "  from fahb f,zhilb z\n" + 
        "   where f.daohrq >="+DateUtil.FormatOracleDate(getBRiq())+" \n"+
        "   and f.daohrq <= "+DateUtil.FormatOracleDate(getERiq())+" \n"+
        "   and f.diancxxb_id="+this.getTreeid()+"\n"+
        "   and f.zhilb_id=z.id\n" + 
        "order by f.daohrq";
		ResultSetList rs = con.getResultSetList(sql);
		String insertSql="";
			while(rs.next()){
				double stad=rs.getDouble("stad");
				double mad=rs.getDouble("mad");
				double Mt=rs.getDouble("mt");
				double ad=rs.getDouble("ad");
				double std=rs.getDouble("std");
				double sdaf=rs.getDouble("sdaf");
//				double std=CustomMaths.Round_new(stad * 100/ (100 - mad), 2);
//				double sdaf= CustomMaths.Round_new((100 / (100 - ad)) * std,2);
				double Qbad=rs.getDouble("qbad");
				double Had=rs.getDouble("had");
				double Star=rs.getDouble("star");
				double Qgrad=rs.getDouble("qgrad");
				double Qnetar=rs.getDouble("qnet_ar");
//				double Star=CustomMaths.Round_new((100-Mt)*stad/(100-mad),2);
//				double dblA = 0;
//				double Qgrad=0;
//				double Qnetar=0;
//				if (Qbad < 16.72) {
//					dblA = 0.001;
//				} else {
//					if (Qbad >= 16.72 && Qbad <= 25.1) {
//						dblA = 0.0012;
//					} else {
//						dblA = 0.0016;
//					}
//				}
//				Qgrad = CustomMaths.Round_new(
//						(Qbad - (0.0941 * stad + dblA * Qbad)), 3);
//				if (mad == 100) {
//					Qnetar = -23;
//				} else {
//				   Qnetar = CustomMaths.Round_new(((Qgrad - 0.206 * Had)
//							* (100 - Mt) / (100 - mad) - 0.023 * Mt), 3);
//				}
				
				insertSql+="delete from huayhblb where zhilb_id="+rs.getString("zhilb_id")+";\n";
				insertSql+="insert into huayhblb(id,huaybh,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qgrad,hdaf,qgrad_daf,\n"+
				"      sdaf,huayy,lury,star,zhilb_id,jingz,meikxxb_id) values(getnewid("+getTreeid()+"),'"+rs.getString("bianm")+"',"+DateUtil.FormatOracleDate(rs.getString("huaysj"))+",\n"+
				"      "+Qnetar+","+rs.getString("aar")+","+ad+","+rs.getString("vdaf")+","+rs.getString("mt")+",\n"+
				"      "+stad+","+rs.getString("aad")+","+mad+","+rs.getString("qbad")+","+rs.getString("had")+",\n"+
				"      "+rs.getString("vad")+","+rs.getString("fcad")+","+std+","+Qgrad+","+rs.getString("hdaf")+",\n"+
				"      "+rs.getString("qgrad_daf")+","+sdaf+",'"+rs.getString("huayy")+"','"+rs.getString("lury")+"',"+Star+",\n"+
				"      "+rs.getString("zhilb_id")+","+rs.getString("jingz")+","+rs.getString("meikxxb_id")+");\n";
	    }
			insertSql=" begin \n"+insertSql+" end ;";
			
			con.getUpdate(insertSql);
			
			con.Close();	
	
    }
    
    public void Computer(){
    	double liuzb=Double.parseDouble(this.getParameters());
    	double tiaozz=Double.parseDouble(this.getParameters1());
		JDBCcon con = new JDBCcon();
		String sql="select z.zhilb_id,z.qnet_ar,\n" + 
        "   z.aar,\n" + 
        "   z.ad,\n" + 
        "   z.vdaf,\n" + 
        "   z.mt,\n" + 
//        "   z.stad as stad,\n"+
        "   round_new(gethuayhbl(z.stad,"+liuzb+","+tiaozz+"),2) as stad,\n" + 
        "   z.aad,\n" + 
        "   z.mad,\n" + 
        "   z.qbad,\n" + 
        "   z.had,\n" + 
        "   z.vad,\n" + 
        "   z.fcad,\n" + 
        "   z.std,\n" + 
        "   z.qgrad,\n" + 
        "   z.hdaf,\n" + 
        "   z.qgrad_daf,\n" + 
        "   z.sdaf,\n" + 
        "   z.STAR\n" + 
        "  from fahb f,huayhblb z\n" + 
        "   where f.daohrq >="+DateUtil.FormatOracleDate(getBRiq())+" \n"+
        "   and f.daohrq <= "+DateUtil.FormatOracleDate(getERiq())+" \n"+
        "   and f.diancxxb_id="+this.getTreeid()+"\n"+
        "   and f.zhilb_id=z.zhilb_id\n" + 
        "   and z.stad>"+liuzb +" order by f.daohrq";
		ResultSetList rs = con.getResultSetList(sql);
		String insertSql="";
		int i = 0;
			while(rs.next()){
				double stad=rs.getDouble("stad");
				double num =0;
				if(i%2==1){
					num = 0.1;
					stad = stad - num;
				}else {
					num = 0.2;
					stad = stad - num;
				}
				double mad=rs.getDouble("mad");
				double Mt=rs.getDouble("mt");
				double ad=rs.getDouble("ad");
//				double std=rs.getDouble("std");
//				double sdaf=rs.getDouble("sdaf");
				double std=CustomMaths.Round_new(stad * 100/ (100 - mad), 2);
				double sdaf= CustomMaths.Round_new((100 / (100 - ad)) * std,2);
				double Qbad=rs.getDouble("qbad");
				double Had=rs.getDouble("had");
//				double Star=rs.getDouble("star");
//				double Qgrad=rs.getDouble("qgrad");
//				double Qnetar=rs.getDouble("qnet_ar");
				double Star=CustomMaths.Round_new((100-Mt)*stad/(100-mad),2);
				double dblA = 0;
				double Qgrad=0;
				double Qnetar=0;
				if (Qbad < 16.72) {
					dblA = 0.001;
				} else {
					if (Qbad >= 16.72 && Qbad <= 25.1) {
						dblA = 0.0012;
					} else {
						dblA = 0.0016;
					}
				}
				Qgrad = CustomMaths.Round_new(
						(Qbad - (0.0941 * stad + dblA * Qbad)), 3);
				if (mad == 100) {
					Qnetar = -23;
				} else {
				   Qnetar = CustomMaths.Round_new(((Qgrad - 0.206 * Had)
							* (100 - Mt) / (100 - mad) - 0.023 * Mt), 3);
				}
				insertSql+="update huayhblb set qnet_ar="+Qnetar+",stad="+stad+",std="+std+",qgrad="+Qgrad+",\n"+
				"      sdaf="+sdaf+",star="+Star+",huanblz="+liuzb+",tiaozz="+tiaozz+" where zhilb_id="+rs.getString("zhilb_id")+";\n";
				i++;
			}
			insertSql=" begin \n"+insertSql+" end ;";
			
			con.getUpdate(insertSql);
			
			con.Close();	
	
    }
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sb =	"select h.id,m.mingc as meikdw,\n" +
		"       h.huaybh as bianm,\n" + 
		"       h.jingz,\n" + 
		"       h.Qnet_ar,\n" + 
		"       h.mt,\n" + 
		"       h.mad,\n" + 
		"       h.aad,\n" + 
		"       h.vad,\n" + 
		"       h.vdaf,\n" + 
		"       decode(h.huanblz,null,to_char(h.stad,'0.99'),'<font color=\"red\">'|| to_char(h.stad,'0.99') ||'</font>') as stad, \n"+
		//"       h.stad,\n" + 
		"       h.std,\n" + 
		"       h.star,\n" + 
		"       h.had,\n" + 
		"       h.qbad,\n" + 
		"       h.qgrad\n" + 
		"  from HUAYHBLB h,fahb f,meikxxb m\n" + 
		"  where f.daohrq>="+DateUtil.FormatOracleDate(getBRiq())+"\n"+
		"        and f.daohrq<="+DateUtil.FormatOracleDate(getERiq())+"\n"+
		"        and f.diancxxb_id="+this.getTreeid()+"\n"+
		"        and h.zhilb_id=f.zhilb_id \n"+
		"        and h.meikxxb_id=m.id \n"+
		"        order by f.daohrq";

		ResultSetList rsl = con.getResultSetList(sb);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(23);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("meikdw").editor=null;
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("bianm").editor=null;
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("jingz").editor=null;
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(70);
		egu.getColumn("Qnet_ar").editor=null;
		egu.getColumn("Qnet_ar").setHeader("收到基低位发<p>热量(MJ/Kg)</p>");
		egu.getColumn("Qnet_ar").setWidth(80);
		egu.getColumn("mt").editor=null;
		egu.getColumn("mt").setHeader("全水分(%)");
		egu.getColumn("mt").setWidth(70);
		egu.getColumn("mad").editor=null;
		egu.getColumn("mad").setHeader("空气干燥<p>基水分(%)</p>");
		egu.getColumn("mad").setWidth(70);
		egu.getColumn("aad").editor=null;
		egu.getColumn("aad").setHeader("空气干燥<p>基灰分(%)</p>");
		egu.getColumn("aad").setWidth(70);
		egu.getColumn("vad").editor=null;
		egu.getColumn("vad").setHeader("空气干燥基<p>挥发分(%)</p>");
		egu.getColumn("vad").setWidth(75);

		egu.getColumn("vdaf").editor=null;
		egu.getColumn("vdaf").setHeader("干燥无灰基<p>挥发分(%)</p>");
		egu.getColumn("vdaf").setWidth(75);
		egu.getColumn("stad").editor=null;
		egu.getColumn("stad").setHeader("空气干燥</p>基硫(%)</p>");
		egu.getColumn("stad").setWidth(75);
		egu.getColumn("std").editor=null;
		egu.getColumn("std").setHeader("干燥基</p>全硫(%)</p>");
		egu.getColumn("std").setWidth(65);
		egu.getColumn("star").editor=null;
		egu.getColumn("star").setHeader("收到基</p>硫(%)</p>");
		egu.getColumn("star").setWidth(65);
		egu.getColumn("had").editor=null;
		egu.getColumn("had").setHeader("空气干燥<p>基氢(%)</p>");
		egu.getColumn("had").setWidth(70);
		egu.getColumn("qbad").editor=null;
		egu.getColumn("qbad").setHeader("弹筒发热<p>量(MJ/Kg)</p>");
		egu.getColumn("qgrad").editor=null;
		egu.getColumn("qgrad").setHeader("空气干燥基高<p>位热值(MJ/Kg)</p>");
		egu.getColumn("qbad").setWidth(70);
		egu.getColumn("qgrad").setWidth(80);
		
//		
		egu.addTbarText("电厂:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 

		egu.addTbarText("-");
		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getBRiq());
		df.Binding("BRIQ", "Form0");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getERiq());
		df1.Binding("ERiq", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("after");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");

		egu.addTbarText("环保硫指标：");
		TextField liuzb=new TextField();
		liuzb.setId("liuzb");
		liuzb.setWidth(60);
		StringBuffer ls = new StringBuffer();
		ls.append("change:function bij(){")
           .append("for(var i=0;i<gridDiv_ds.getCount();i++){")
				.append("if(gridDiv_ds.getAt(i).get('STAD').indexOf('<font color=\"red\">')>-1){")
				.append("gridDiv_ds.getAt(i).set('STAD',gridDiv_ds.getAt(i).get('STAD').substring(gridDiv_ds.getAt(i).get('STAD').indexOf('<font color=\"red\">')+18,gridDiv_ds.getAt(i).get('STAD').indexOf('</font>')));")
				.append("}")
				.append("if(eval(gridDiv_ds.getAt(i).get('STAD'))>liuzb.getValue()){")
				
					.append("gridDiv_ds.getAt(i).set('STAD','<font color=\"red\">'+gridDiv_ds.getAt(i).get('STAD')+'</font>');")
			.append("}}}");
		liuzb.setListeners(ls.toString());
		egu.addToolbarItem(liuzb.getScript());
		egu.addTbarText("环保硫调整值：");
		TextField tiaozz=new TextField();
		tiaozz.setId("tiaozz");
		tiaozz.setWidth(60);
		egu.addToolbarItem(tiaozz.getScript());
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		StringBuffer rb = new StringBuffer();
//		rb.append("function (){if(liuzb.getValue().toString()==''){Ext.MessageBox.alert('提示信息','请输入硫值!');return;} else{")
//		.append("document.getElementById('PARAMETERS').value=liuzb.getValue().toString();" )
		rb.append("function(){document.getElementById('CreateButton').click();}");
		GridButton gb = new GridButton("生成",rb.toString());
		gb.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gb);
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){if(liuzb.getValue().toString()==''){Ext.MessageBox.alert('提示信息','请输入环保硫值!');return;} else{")
		.append("if(tiaozz.getValue().toString()==''){Ext.MessageBox.alert('提示信息','请输入环保硫调整值!');return;} else{")
		.append("document.getElementById('PARAMETERS').value=liuzb.getValue().toString();" )
		.append("document.getElementById('PARAMETERS1').value=tiaozz.getValue().toString();" )
		.append("document.getElementById('Computer').click();}}}");
		GridButton gbr = new GridButton("重新计算",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbr);
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addPaging(0);
		setExtGrid(egu);
		con.Close();
	}

	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	
	//	tree_begin
	public String getTreeid() {
		
		if(((Visit) getPage().getVisit()).getString1()==null||((Visit) getPage().getVisit()).getString1().equals("")){
			
			((Visit) getPage().getVisit()).setString1(String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString1().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString1(treeid);
			((Visit) getPage().getVisit()).setboolean1(true);
		}
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
//	tree_end

	
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
//	 绑定日期
	boolean briqchange = false;
	
	private String briq;

	public String getBRiq() {
		if (briq == null || briq.equals("")) {
			briq = DateUtil.FormatDate(new Date());
		}
		return briq;
	}

	public void setBRiq(String briq) {

		if (this.briq != null && !this.briq.equals(briq)) {
			this.briq = briq;
			briqchange = true;
		}

	}

	boolean eriqchange = false;
	
	private String eriq;

	public String getERiq() {
		if (eriq == null || eriq.equals("")) {
			eriq = DateUtil.FormatDate(new Date());
		}
		return eriq;
	}

	public void setERiq(String eriq) {

		if (this.eriq != null && !this.eriq.equals(eriq)) {
			this.eriq = eriq;
			eriqchange=true;
		}
	}

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			briq=null;
			briqchange = false;
			eriq=null;
			eriqchange = false;
			setTreeid("");
			setTree(null);
		
		}
		if(briqchange || eriqchange){
			briqchange = false;
			eriqchange = false;
			getSelectData();
		}
		getSelectData();
	} 
}