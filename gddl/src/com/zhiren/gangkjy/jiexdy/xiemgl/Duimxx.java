package com.zhiren.gangkjy.jiexdy.xiemgl;

import java.sql.ResultSet;
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

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Duimxx extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			save();
			initGrid();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			if(((Visit) getPage().getVisit()).getString2()==null || ((Visit) getPage().getVisit()).getString2().equals("")){
				setMsg("用户信息错误！");
				cycle.activate("Welcome");
			}else{
				cycle.activate(((Visit) getPage().getVisit()).getString2());
			}
		}
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 
			"select d.id,\n" +
			"gs.mingc||'-'||m.mingc duow,\n" + 
			"dw.ches, \n"+
			"d.ruksl,\n" + 
			"d.rukkssj,\n" + 
			"to_char(d.rukkssj,'hh24') rukkss,\n" + 
			"to_char(d.rukkssj,'mi') rukksf,\n" + 
			"d.ruksj,\n" + 
			"to_char(d.ruksj,'hh24') ruks,\n" + 
			"to_char(d.ruksj,'mi') rukf,\n" + 
			"d.beiz,f.zhilb_id \n" + 
			"from duimxxb d, meicb m, duowkcb dw,duowgsb gs,fahb f \n" + 
			"where d.meicb_id = m.id and dw.duiqm_id=d.id and m.duowgsb_id=gs.id and d.fahb_id=f.id \n" + 
			"and d.fahb_id = " + visit.getString10();
		
//		String sqlJingz="select case when maoz-piz<0 then 0 else maoz-piz end  as jingz from fahb where id="+visit.getString10();
		String sqlJingz="select to_char(f.daohrq,'yyyy-mm-dd') rq, nvl(to_char(f.daohrq,'hh24'),'0') xiaos, nvl(to_char(f.daohrq,'mi'),'0') fen,nvl(to_char(f.daohrq+1/1440,'mi'),'0') Xiayf , f.jingz, f.ches from fahb f where f.id="+visit.getString10();
		ResultSetList rslJingz=con.getResultSetList(sqlJingz);
		String Jingz="0";
		String Ches="0";
		String rukks_rq="";
		String rukks_xiaos="";
		String rukks_fenz="";
		String xiaoyf="";
		while(rslJingz.next()){
			rukks_rq=rslJingz.getString("rq");
			rukks_xiaos=rslJingz.getString("xiaos");
			rukks_fenz=rslJingz.getString("fen");
			xiaoyf=rslJingz.getString("Xiayf");
			Jingz=rslJingz.getString("JINGZ");
			Ches=rslJingz.getString("CHES");
		}
		rslJingz.close();
		visit.setString1(Jingz);
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// 新建grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// 设置grid可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置数据不分页
		egu.addPaging(0);
		// 设置grid为单选
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// 设置grid列标题
		egu.getColumn("duow").setHeader(Local.duow);
		egu.getColumn("ches").setHeader(Local.ches);
		egu.getColumn("ruksl").setHeader(Local.ruksl);
		egu.getColumn("rukkssj").setHeader(Local.rukksrq);
		egu.getColumn("rukkss").setHeader(Local.shi);
		egu.getColumn("rukksf").setHeader(Local.fen);
		egu.getColumn("ruksj").setHeader(Local.rukrq);
		egu.getColumn("ruks").setHeader(Local.shi);
		egu.getColumn("rukf").setHeader(Local.fen);
		egu.getColumn("beiz").setHeader(Local.beiz);
		egu.getColumn("zhilb_id").setHeader("化验信息");
//		设置列宽度
		egu.getColumn("duow").setWidth(120);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("ruksl").setWidth(80);
		egu.getColumn("rukkssj").setWidth(80);
		egu.getColumn("rukkss").setWidth(40);
		egu.getColumn("rukksf").setWidth(40);
		egu.getColumn("ruksj").setWidth(80);
		egu.getColumn("ruks").setWidth(40);
		egu.getColumn("rukf").setWidth(40);
		egu.getColumn("beiz").setWidth(400);
		egu.getColumn("zhilb_id").setWidth(50);
		// 设置默认值
	
		egu.getColumn("rukkssj").setDefaultValue(rukks_rq);
		egu.getColumn("ruksj").setDefaultValue(rukks_rq);
		egu.getColumn("rukkss").setDefaultValue(rukks_xiaos);
		egu.getColumn("rukksf").setDefaultValue(rukks_fenz);
		egu.getColumn("ruks").setDefaultValue(rukks_xiaos);
		egu.getColumn("rukf").setDefaultValue(xiaoyf);
		egu.getColumn("ruksl").setDefaultValue(Jingz);
		egu.getColumn("ches").setDefaultValue(Ches);
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("zhilb_id").setHidden(true);
		
		// 数据列下拉框设置
		// 垛位
		ComboBox duow = new ComboBox();
		egu.getColumn("duow").setEditor(duow);
		duow.setEditable(true);

	 sql=	"select d.id,gs.mingc||'-'||d.mingc as mingc from meicb d,duowgsb gs,fahb f"+
		       " where d.diancxxb_id =f.diancxxb_id and gs.id=d.duowgsb_id "+
				"    and f.id="+visit.getString10()+
		       " and   d.mingc<>'直达煤场' order by xuh";
		egu.getColumn("duow").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		// 入库开始时
		ComboBox rukkss = new ComboBox();
		egu.getColumn("rukkss").setEditor(rukkss);
		rukkss.setEditable(true);
		List h = new ArrayList();
		for (int i = 0; i < 24; i++)
			h.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("rukkss")
				.setComboEditor(egu.gridId, new IDropDownModel(h));
		// 入库开始分
		ComboBox rukksf = new ComboBox();
		egu.getColumn("rukksf").setEditor(rukksf);
		rukksf.setEditable(true);
		List m = new ArrayList();
		for (int i = 0; i < 60; i++)
			m.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("rukksf")
				.setComboEditor(egu.gridId, new IDropDownModel(m));
		// 入库结束时
		ComboBox ruks = new ComboBox();
		egu.getColumn("ruks").setEditor(ruks);
		ruks.setEditable(true);
//		List gh = new ArrayList();
//		for (int i = 0; i < 24; i++)
//			gh.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("ruks").setComboEditor(egu.gridId,
				new IDropDownModel(h));
		// 入库结束分
		ComboBox rukf = new ComboBox();
		egu.getColumn("rukf").setEditor(rukf);
		rukf.setEditable(true);
//		List gm = new ArrayList();
//		for (int i = 0; i < 60; i++)
//			gm.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("rukf").setComboEditor(egu.gridId,
				new IDropDownModel(m));
		// 添加按钮
		GridButton insert = new GridButton(GridButton.ButtonType_Insert,
				"gridDiv", egu.getGridColumns(), "");		
		egu.addTbarBtn(insert);
		// 删除按钮
		String insertcondition="var rec=gridDiv_sm.getSelected();if(eval(rec.get('ZHILB_ID'))>0){Ext.MessageBox.alert('提示信息','请先删除对应的采样和化验信息！');return;}\n";
		IGridButton delete = new IGridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "",insertcondition);
//		GridButton delete = new GridButton(GridButton.ButtonType_Delete,
//				"gridDiv", egu.getGridColumns(), insertcondition);
		egu.addTbarBtn(delete);
		// 保存按钮
		//保存时判断车数字段其判断方法与等同于净重的判断方法。在判断时，当车数或入库数量其中一个值为0时，认定其有误。
		GridButton save = new GridButton("保存",GridButton.ButtonType_Save_condition, "gridDiv",
				egu.getGridColumns(), "SaveButton",
						"	var count2=0;"+
						"	var count=0;\n" +
						"	var chkCS; var chkSL; \n"+
						"	var res=gridDiv_grid.getStore().getRange();\n"+
						"	for(var i=0;i<res.length;i++){\n"+
						"		count+=Number(res[i].get('RUKSL'));\n"+
						"		count2+=Number(res[i].get('CHES'));\n" +
						"	if(Number(res[i].get('RUKSL'))==0){chkSL=0;}\n"+
						"	if(Number(res[i].get('CHES'))==0){chkCS=0;}\n"+
						"	} \n" +
						"	if(count==0 && count2==0){}else{\n"+
						"	if(count!="+Jingz+"){\n"+
						"		Ext.MessageBox.alert('提示信息','入库数量与卸煤净重不一致！');\n"+
						"		return;} \n"+
						"	if(count2!="+Ches+"){\n"+
						"		Ext.MessageBox.alert('提示信息','入库车数与卸煤车数不一致！');\n"+
						"		return;}\n" +
						"	if(chkCS==0){\n"+
						"		Ext.MessageBox.alert('提示信息','入库车数有误！');\n"+
						"		return;} \n"+
						"	if(chkSL==0){\n"+
						"		Ext.MessageBox.alert('提示信息','入库数量有误！');\n"+
						"		return;}\n" +
						"	} \n");
		
		egu.addTbarBtn(save);
//		 详细过衡按钮
		GridButton Return = new GridButton("返回", "ReturnXiemxx");
		Return.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(Return);
		
		StringBuffer ChkJingz = new StringBuffer();
		ChkJingz.append("\n gridDiv_grid.on('beforeedit',function(e){")
				.append("\n	Shengy=").append(Jingz).append(";")
				.append("\n	Shengy2=").append(Ches).append(";")
				.append("});");
		
		ChkJingz.append("\n	gridDiv_grid.on('afteredit',function(e){")
				.append("if(e.field=='RUKSL'){  if(eval(Shengy-e.value)<0){Ext.MessageBox.alert('提示信息','不能大于总净重！');e.record.set('RUKSL',0);}} \n" +
						"if(e.field=='CHES'){  if(eval(Shengy2-e.value)<0){Ext.MessageBox.alert('提示信息','不能大于总车数！');e.record.set('CHES',0);}} \n" +
						"});");
		egu.addOtherScript(ChkJingz.toString());
		setExtGrid(egu);
		con.Close();

	}

	private void save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有未提交改动！");
			return;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		con.setAutoCommit(false);
		String sql;
		int flag;
		// 删除数据
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		sql="";
//		sql = "begin \n";
		while (rs.next()) {
			long id = rs.getLong("id");
//			String fahb_id =  visit.getString10();
			long meicb_id = getExtGrid().getColumn("duow").combo.getBeanId(rs
					.getString("duow"));
//			double ruksl = rs.getDouble("ruksl");
			//入库开始时间
//			String strriq = rs.getString("rukkssj") + " "
//					+ rs.getString("rukkss") + ":" + rs.getString("rukksf")
//					+ ":00";
//			String rukkssj = DateUtil.FormatOracleDateTime(strriq);
			//入库结束时间
//			strriq = rs.getString("ruksj") + " " + rs.getString("ruks") + ":"
//					+ rs.getString("rukf") + ":00";
			
//			String ruksj = DateUtil.FormatOracleDateTime(strriq);
//			String beiz = rs.getString("beiz");
			if(rs.getLong("id")!=0){
			sql += "delete from duimxxb where id =" + rs.getString("id") + ";\n";
			
			sql+="update duowkcb set kucl=kucl-(select ruksl from duowkcb where duiqm_id="+id+") where shij>(select shij from duowkcb where duiqm_id="+id+") and meicb_id="+meicb_id+";\n";
			sql+=" delete from duowkcb where duiqm_id="+id+";\n";
			}
		}
//		sql += "end;\n";
		if (rs.getRows() > 0&&sql!="") {
			        sql="begin \n"+sql+"end;\n";
			
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.DeleteDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rs.close();
		// 修改数据
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		while (rs.next()) {
			long id = rs.getLong("id");
			String fahb_id =  visit.getString10();
			long meicb_id = getExtGrid().getColumn("duow").combo.getBeanId(rs
					.getString("duow"));
			double ches=rs.getDouble("ches");
			double ruksl = rs.getDouble("ruksl");
			//入库开始时间
			String strriq = rs.getString("rukkssj") + " "
					+ rs.getString("rukkss") + ":" + rs.getString("rukksf")
					+ ":00";
			String rukkssj = DateUtil.FormatOracleDateTime(strriq);
			//入库结束时间
			strriq = rs.getString("ruksj") + " " + rs.getString("ruks") + ":"
					+ rs.getString("rukf") + ":00";
			
			String ruksj = DateUtil.FormatOracleDateTime(strriq);
			String beiz = rs.getString("beiz");
			if(con.getHasIt("select id from duowkcb where shij="+ruksj+" and meicb_id="+meicb_id+" and duiqm_id<>"+id+"\n")){
				this.setMsg("入库结束时间"+strriq+"与库存有重复");
				con.rollBack();
				return ;
				
			}
			if (id == 0) {
				long newid;
				     try {
				    	 newid=getnewId();
					} catch (Exception e) {
						// TODO 自动生成 catch 块
						this.setMsg("保存出现错误");
						con.rollBack();
						e.printStackTrace();
						return;
					}
				sql += "insert into duimxxb(id,fahb_id,meicb_id,ruksl,rukkssj," +
						"ruksj,caozy,beiz) values("+ newid +
						"," + fahb_id + "," + meicb_id + "," + ruksl + "," +
						rukkssj + "," + ruksj + ",'" + visit.getRenymc() + "','" +
						beiz + "');\n";
				sql+="insert into duowkcb(id,diancxxb_id,riq,duiqm_id,meicb_id,leix,pinz,fahr,chec,shij,shul,kucl,panyk,leib,zhuangt,ches,ruksl,chuksl)values(" 
					
					                     +"getnewid("+visit.getDiancxxb_id()+"),\n";
					                   
					                     long diancxxb_id=-1;
					                     if(visit.getRenyjb()==3)
					                    	 diancxxb_id=visit.getDiancxxb_id();
					                     else if(visit.getRenyjb()==2){
					                    	  ResultSetList diancxxb_id_rsl=con.getResultSetList("select diancxxb_id from fahb where id="+fahb_id+"\n");
					                    	  if(diancxxb_id_rsl.next()){
							                    	 diancxxb_id=diancxxb_id_rsl.getLong("diancxxb_id");
							                     }
					                    	  diancxxb_id_rsl.close();
					                     }
					                     sql+=diancxxb_id+",\n";
					                     
					                     sql+="(select daohrq from fahb where id="+fahb_id+"),\n"
					                     +newid+","
					                     +meicb_id+","
					                     +1+","
					                     +"(select mingc from pinzb where id =(select pinzb_id from fahb where id="+fahb_id+")),"
					                     +"(select mingc from diancxxb where id=(select diancxxb_id from fahb where id="+fahb_id+")),"
					                     +"(select chec from fahb where id="+fahb_id+"),"
					                     +ruksj+","
					                     +ruksl+","
					                     +" (select nvl(max(kucl),0)+"+ruksl+" from duowkcb where shij =(select max(shij) from duowkcb where meicb_id="+meicb_id+"  and shij<"+ruksj+") and meicb_id="+meicb_id+" )"
					                     +","
					                     +"round_new(((select yingk from fahb where id="+fahb_id+")/"+visit.getString1()+"*"+ruksl+"),2),"
					                     +"1,"
					                     +"1,"
					                     +ches+","
					                     +ruksl+","
					                     +"0"
				                         +");\n";
                sql+="update duowkcb set biaoz=ruksl-panyk where duiqm_id="+newid+";\n";
				sql+="update duowkcb b set kucl=(select  b.kucl+ruksl  from duowkcb where duiqm_id="+newid+") where shij>(select shij from duowkcb where duiqm_id="+newid+") and meicb_id="+meicb_id+";\n";
			} else {
				sql += "update duimxxb set \n" + " meicb_id ="+meicb_id+" " //+ meicb_id
						+ ",\n" + " ruksl = " + ruksl + ",\n"
						+ " rukkssj = " + rukkssj + ",\n" + " ruksj = "
						+ ruksj + ",\n" + " caozy = '" 
						+ visit.getRenymc() + "',\n"
						+ " beiz = '" + beiz + "'\n"
						+ " where id=" + id + ";\n";
				

				
				/*
				sql+=" delete from duowkcb where duiqm_id="+rs.getString("id")+";\n";
				sql+="update duowkcb set kucl=kucl-"+rs.getString("ruksl")+" where shij>"+DateUtil.FormatOracleDateTime(rs.getDateTimeString("rukkssj"))+" and meicb_id="+rs.getLong("meicb_id")+";\n";
				sql+="insert into duowkcb(id,diancxxb_id,riq,duiqm_id,meicb_id,leix,pinz,chec,shij,shul,farl,kucl,panyk,leib,zhuangt,ruksl,chuksl)values(" 
					
                    +"getnewid("+visit.getDiancxxb_id()+"),\n"
                    +visit.getDiancxxb_id()+",\n"
                    +"(select daohrq from fahb where id="+fahb_id+"),\n"
                    +id
                    +","
                    +meicb_id
                    +","
                    +1
                    +","
                    +"(select pinz from fahb where id="+fahb_id+")"
                    +","
                    +"(select chec from fahb where id="+fahb_id+")"
                    +","
                    +ruksj
                    +","
                    +" (select distinct shul from duowkcb where shij =(select max(shij) from duowkcb where meicb_id="+meicb_id+" and shij<"+strriq+"))+"+ruksl
                    +","
                    +"(select yingk from fahb where id="+fahb_id+")"
                    +","
                    +"1,"
                    +"1,"
                    +ruksl+","
                    +"0"
                    +");\n";
                      */
				sql+="update duowkcb b set kucl=(select  b.kucl-nvl(max(ruksl),0) from duowkcb where duiqm_id="+id+") where shij>(select shij from duowkcb where duiqm_id="+id+") and meicb_id=(select  meicb_id from duowkcb where duiqm_id="+id+");\n";
//				sql+="update duowkcb set kucl=kucl+"+ruksl+" where shij>"+strriq+" and meicb_id="+meicb_id+";\n";
				
				
				/*
				sql+="update duowkcb set kucl=kucl+" +
						"                       ((select kucl from duowkcb where duiqm_id="+id+")" +
								                "-(select  nvl(max(shul),0) from duowkcb where shij =(select max(shij) from duowkcb where meicb_id="+meicb_id+" and shij<"+ruksj+"))" +
								                "-"+ruksl+") where shij>"+ruksj+" and meicb_id="+meicb_id+";\n";
				*/
				sql+="update duowkcb set diancxxb_id="+visit.getDiancxxb_id()+","
				                            +"riq="+"(select daohrq from fahb where id="+fahb_id+"),\n"
				                            +"meicb_id="+meicb_id+",leix=1,pinz=(select mingc from pinzb where id =(select pinzb_id from fahb where id="+fahb_id+")),"
				                            +"fahr=(select mingc from diancxxb where id=(select diancxxb_id from fahb where id="+fahb_id+")),"
				                            +"chec= (select chec from fahb where id="+fahb_id+"),"
				                            +"shij="+ruksj
				                            +",ches="+ches
				                            +",shul="+ruksl
				                            +",kucl=  ((select nvl(max(kucl),0)+"+ruksl+" from duowkcb where  meicb_id="+meicb_id+" and shij =(select max(shij) from duowkcb where meicb_id="+meicb_id+" and shij<"+ruksj+")))"
				                            +",panyk=(select round_new((select yingk from fahb where id="+fahb_id+")/"+visit.getString1()+"*"+ruksl+" ,2) from dual)" 
				                            +",ruksl= "+ruksl
				                            +"where duiqm_id= "+id+";\n";
				sql+="update duowkcb set biaoz=ruksl-panyk where duiqm_id="+id+";\n"; //按入库数量的不同平摊票重
				sql+="update duowkcb b set kucl= b.kucl+"+ruksl+" where shij>"+ruksj+" and meicb_id="+meicb_id+";\n";                 
			}
		}
		sql += "end;";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.UpdateDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rs.close();
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}
	private long getnewId()throws Exception{
		   long id=0;
        Visit visit=(Visit)this.getPage().getVisit();		
		   JDBCcon con=new JDBCcon();
		   ResultSet rsid=con.getResultSet("select getnewid("+visit.getDiancxxb_id()+") from dual");
			if(rsid.next()){  
			         id =rsid.getLong(1);
			         rsid.close();
			         con.Close();
			 }else{
				 con.rollBack();
				 throw new Exception();
			 }
		  
			this.setMsg("保存出错");
			return id;
		  
	}

	private void init() {
		setExtGrid(null);
		
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			init();
		}
		initGrid();
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
}

class IGridButton extends GridButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8018691269575238278L;
	public IGridButton(int btnType,String parentId,List columns,String tapestryBtnId,String condition){
		super( btnType, parentId, columns, tapestryBtnId,condition);
	}
	public String getDeleteScript() {
		StringBuffer record = new StringBuffer();
		record.append("function() {\n");
		record.append( super.condition+"\n");
		record.append("for(i=0;i<"+parentId+"_sm.getSelections().length;i++){\n");
		record.append("	record = "+parentId+"_sm.getSelections()[i];\n");
		
		StringBuffer sb = new StringBuffer();
		//sb.append(b);
		sb.append(parentId).append("_history += '<result>' ")
		.append("+ '<sign>D</sign>' ");
		
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(gc.update) {
					if("date".equals(gc.datatype)) {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("record.get('")
						.append(gc.dataIndex).append("'))?").append("record.get('")
						.append(gc.dataIndex).append("'):").append("record.get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					}else {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("record.get('").append(gc.dataIndex).append("')");
						if(!gc.datatype.equals(GridColumn.DataType_Float)) {
							sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
						}
						sb.append("+ '</").append(gc.dataIndex).append(">'\n");
					}
				}
			}
		}
		sb.append(" + '</result>' ;");
		record.append(sb);
		
		record.append("	"+parentId+"_ds.remove("+parentId+"_sm.getSelections()[i--]);}}");
		return record.toString();
	}
}