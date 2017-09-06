package com.zhiren.gdjh;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;

/*
 * 作者：夏峥
 * 时间：2013-04-11
 * 描述：增加计划日期显示
 */
	public class Jihsh extends BasePage {
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean5()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)+ "';";
		} else {
			return "";
		}
	}
	public boolean isQuanxkz(){
		return((Visit) getPage().getVisit()).getboolean4();
	}
	public void setQuanxkz(boolean value){
		((Visit) getPage().getVisit()).setboolean4(value);
	}
	private int _editTableRow = -1;// 编辑框中选中的行
	public int getEditTableRow() {
		return _editTableRow;
	}
	private String _msg;
	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getXiaox(){
		if(((Visit) getPage().getVisit()).getString1()==null){
			((Visit) getPage().getVisit()).setString1("");
		}
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setXiaox(String xiaox){
		((Visit) getPage().getVisit()).setString1(xiaox);
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	private boolean TijButton = false;
	public void TijButton(IRequestCycle cycle) {
		TijButton = true;
	}
	private boolean HuitButton = false;
	public void HuitButton(IRequestCycle cycle) {
		HuitButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (TijButton) {
			TijButton = false;
			tij();
		}
		if (HuitButton) {
			HuitButton = false;
			huit();
		}
	}

	public void getSelectData() {
		String sql="";
		String xufCondi="";
		JDBCcon con=new JDBCcon();
		String tableName="NIANDJH_Zhib";
		String tableName1="YUEDJH_ZHIB";
		String leib="计划";
		long renyxxb_id=((Visit) getPage().getVisit()).getRenyID();
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),this.getTreeid());
		if(Long.parseLong(getTreeid())!=-1){
			xufCondi=" and diancxxb_id "+
				"in (select id\n" +
				"from(\n" + 
				"select id from diancxxb\n" + 
				"start with (fuid="+Long.parseLong(getTreeid())+" or shangjgsid="+Long.parseLong(getTreeid())+") \n" + 
				"connect by fuid=prior id\n" + 
				")\n" + 
				"union\n" + 
				"select id\n" + 
				"from diancxxb\n" + 
				"where id="+Long.parseLong(getTreeid())+")";
		}
		
		((Visit) getPage().getVisit()).setExtTree1(etu);
			
		String NIANDJH_ZhibID="";
		String YUEDJH_ZhibID="";
		//自己的任务
		if(getweizSelectValue().getId()==1){
			NIANDJH_ZhibID=Liuc.getWodrws(tableName, renyxxb_id, leib);
			YUEDJH_ZhibID=Liuc.getWodrws(tableName1, renyxxb_id, leib);
		}else{
			NIANDJH_ZhibID=Liuc.getLiuczs(tableName, renyxxb_id, leib);
			YUEDJH_ZhibID=Liuc.getLiuczs(tableName1, renyxxb_id, leib);
		}
		
		sql= "SELECT NIANDJH_Zhib.ID,\n" + 
			 "       NVL('年计划', '') AS LEIX,\n" +
			 "		 TO_CHAR(NIANDJH_Zhib.Riq,'YYYY\"年计划\"') AS CHAK,\n"+
			 "       CAIG.MEIL,\n" + 
			 "       CAIG.REZ,\n" + 
			 "       (CAIG.JIH_MEIJ+CAIG.JIH_YUNJ+CAIG.JIH_ZAF+CAIG.JIH_QIT) DAOCJ,\n" + 
			 "       DECODE(CAIG.REZ,0,0,ROUND((CAIG.JIH_MEIJ+CAIG.JIH_YUNJ+CAIG.JIH_ZAF+CAIG.JIH_QIT)*29.271/CAIG.REZ,2))DAOCBMDJ,\n" + 
			 "       NIANDJH_Zhib.RLZHBMDJ,\n" + 
			 "       LIUCZTB.ID LIUCZTB_ID,\n" + 
			 "       LEIBZTB.MINGC ZHUANGT,\n" + 
			 "       LIUCZTB.LIUCB_ID,\n" + 
			 "       '' HISTRYYJ,\n" + 
			 "       '' SHEND,\n" + 
			 "       NVL('Niandjhshcx&&dc='||NIANDJH_Zhib.Diancxxb_Id||'&&lx=', '')||to_char(NIANDJH_Zhib.Riq,'yyyy') YEM\n" + 
			 "FROM  (SELECT RIQ,SUM(JIH_SL) MEIL,\n" + 
			 "      ROUND(DECODE(SUM(DECODE(JIH_REZ, 0, 0, JIH_SL)),0,0,SUM(JIH_REZ * JIH_SL) /SUM(DECODE(JIH_REZ, 0, 0, JIH_SL))),2) REZ,\n" + 
			 "		ROUND(DECODE(SUM(DECODE(JIH_MEIJ, 0, 0, JIH_SL)),0,0,SUM(JIH_MEIJ * JIH_SL) /SUM(DECODE(JIH_MEIJ, 0, 0, JIH_SL))),2) JIH_MEIJ,\n" +
			 "		ROUND(DECODE(SUM(DECODE(JIH_YUNJ, 0, 0, JIH_SL)),0,0,SUM(JIH_YUNJ * JIH_SL) /SUM(DECODE(JIH_YUNJ, 0, 0, JIH_SL))),2) JIH_YUNJ,\n" + 
			 "		ROUND(DECODE(SUM(DECODE(JIH_ZAF, 0, 0, JIH_SL)),0,0,SUM(JIH_ZAF * JIH_SL) /SUM(DECODE(JIH_ZAF, 0, 0, JIH_SL))),2) JIH_ZAF,\n" + 
			 "		ROUND(DECODE(SUM(DECODE(JIH_QIT, 0, 0, JIH_SL)),0,0,SUM(JIH_QIT * JIH_SL) /SUM(DECODE(JIH_QIT, 0, 0, JIH_SL))),2) JIH_QIT\n"+
			 "  FROM NIANDJH_CAIG\n" + 
			 " WHERE TRUNC(RIQ, 'yyyy') = DATE '"+getNianfValue().getStrId()+"-01-01'\n" + 
			 " GROUP BY RIQ)CAIG,NIANDJH_Zhib, LIUCZTB, LEIBZTB, LIUCLBB\n" + 
			 "WHERE LIUCZTB.LEIBZTB_ID = LEIBZTB.ID\n" + 
			 "   AND LEIBZTB.LIUCLBB_ID = LIUCLBB.ID\n" + 
			 "   AND NIANDJH_Zhib.LIUCZTB_ID = LIUCZTB.ID\n" + 
			 "   AND NIANDJH_Zhib.Riq=caig.riq\n" + 
			 "   AND NIANDJH_Zhib.ID IN ("+NIANDJH_ZhibID+")\n" + 
			 "   AND TRUNC(NIANDJH_Zhib.riq, 'YYYY') = date'"+getNianfValue().getStrId()+"-01-01'\n" + 
			 xufCondi+
			 "UNION ALL\n" + 
			 "SELECT YUEDJH_ZHIB.ID,\n" + 
			 "       NVL('月计划', '') AS LEIX,\n" + 
			 "		 to_char(YUEDJH_ZHIB.Riq,'yyyy\"年\"mm\"月计划\"') AS CHAK,\n"+	
			 "       CAIG.MEIL,\n" + 
			 "       CAIG.REZ,\n" + 
			 "       (CAIG.JIH_MEIJ+CAIG.JIH_YUNJ+CAIG.JIH_ZAF) DAOCJ,\n" + 
			 "       DECODE(CAIG.REZ,0,0,ROUND((CAIG.JIH_MEIJ+CAIG.JIH_YUNJ+CAIG.JIH_ZAF)*29.271/CAIG.REZ,2))DAOCBMDJ,\n" + 
			 "       YUEDJH_ZHIB.RLZHBMDJ,\n" + 
			 "       LIUCZTB.ID LIUCZTB_ID,\n" + 
			 "       LEIBZTB.MINGC ZHUANGT,\n" + 
			 "       LIUCZTB.LIUCB_ID,\n" + 
			 "       '' HISTRYYJ,\n" + 
			 "       '' SHEND,\n" + 
			 "       NVL('Yuedjhshcx&&dc='||YUEDJH_Zhib.Diancxxb_Id||'&&lx=', '')||to_char(YUEDJH_Zhib.Riq,'yyyy-mm') YEM\n" + 
			 "FROM  (SELECT RIQ,SUM(JIH_SL) MEIL,\n" + 
			 "      ROUND(DECODE(SUM(DECODE(JIH_REZ, 0, 0, JIH_SL)),0,0,SUM(JIH_REZ * JIH_SL) /SUM(DECODE(JIH_REZ, 0, 0, JIH_SL))),2) REZ,\n" + 
			 "		ROUND(DECODE(SUM(DECODE(JIH_MEIJ, 0, 0, JIH_SL)),0,0,SUM(JIH_MEIJ * JIH_SL) /SUM(DECODE(JIH_MEIJ, 0, 0, JIH_SL))),2) JIH_MEIJ,\n" +
			 "      ROUND(DECODE(SUM(DECODE(JIH_YUNJ, 0, 0, JIH_SL)),0,0,SUM(JIH_YUNJ * JIH_SL) /SUM(DECODE(JIH_YUNJ, 0, 0, JIH_SL))),2) JIH_YUNJ,\n" + 
			 "      ROUND(DECODE(SUM(DECODE(JIH_ZAF, 0, 0, JIH_SL)),0,0,SUM(JIH_ZAF * JIH_SL) /SUM(DECODE(JIH_ZAF, 0, 0, JIH_SL))),2) JIH_ZAF\n"+
			 "  FROM YUEDJH_CAIG\n" + 
			 " WHERE TRUNC(RIQ, 'yyyy') = DATE '"+getNianfValue().getStrId()+"-01-01'\n" + 
			 " GROUP BY RIQ)CAIG,YUEDJH_Zhib, LIUCZTB, LEIBZTB, LIUCLBB\n" + 
			 "WHERE LIUCZTB.LEIBZTB_ID = LEIBZTB.ID\n" + 
			 "   AND LEIBZTB.LIUCLBB_ID = LIUCLBB.ID\n" + 
			 "   AND YUEDJH_Zhib.LIUCZTB_ID = LIUCZTB.ID\n" + 
			 "   AND YUEDJH_Zhib.Riq=caig.riq\n" + 
			 "   AND YUEDJH_Zhib.ID IN ("+YUEDJH_ZhibID+")\n" + 
			 "   AND TRUNC(YUEDJH_Zhib.riq, 'YYYY') = date'"+getNianfValue().getStrId()+"-01-01'\n"+
			 xufCondi ;

		 ResultSetList rs=con.getResultSetList(sql);
		 while(rs.next()){
			 rs.setString("Shend", String.valueOf(Liuc.getShendId(rs.getLong("liucb_id"),rs.getLong("liucztb_id"))));
		 }
		 rs.beforefirst();
			
		ExtGridUtil egu = new ExtGridUtil("SelectFrmDiv", rs);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("liucb_id").setHidden(true);
		egu.getColumn("liucztb_id").setHidden(true);
		egu.getColumn("Shend").setHidden(true);
		egu.getColumn("HISTRYYJ").setHidden(true);
		
		egu.getColumn("LEIX").setHeader("计划类型");
		egu.getColumn("LEIX").setWidth(60);
		
		egu.getColumn("CHAK").setHeader("计划日期");
		egu.getColumn("CHAK").setWidth(100);
		
		egu.getColumn("MEIL").setHeader("煤量<br>(吨)");
		egu.getColumn("MEIL").setWidth(100);
		egu.getColumn("REZ").setHeader("热值<br>(Mj/Kg)");
		egu.getColumn("REZ").setWidth(80);
		egu.getColumn("DAOCJ").setHeader("到厂价<br>(元/吨)");
		egu.getColumn("DAOCJ").setWidth(100);
		egu.getColumn("DAOCBMDJ").setHeader("到厂含税<br>标煤单价<br>(元/吨)");
		egu.getColumn("DAOCBMDJ").setWidth(100);
		egu.getColumn("RLZHBMDJ").setHeader("入炉折标煤单价<br>(元/吨)");
		
		egu.getColumn("ZHUANGT").setHeader("状态");
		egu.getColumn("ZHUANGT").setWidth(120);
		egu.getColumn("YEM").setHeader("查看");
		String str1=" var url = 'http://'+document.location.host+document.location.pathname; \n"+
			    	" var end = url.indexOf(';');\n"+
			    	" url = url.substring(0,end);\n"+
			    	" url = url + '?service=page/'+record.data['YEM'];\n";
		egu.getColumn("YEM").setRenderer(
				"function(value,p,record){" +str1+
				"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>查看</a>\"}"
		);
		
//		如果属于自己的任务则显示提交回退按钮
		if(getweizSelectValue().getId()==1){
			egu.addToolbarItem("{"+new GridButton("提交","function(){if(SelectFrmDiv_sm.hasSelection()){document.getElementById('tijButton').click();}else{Ext.MessageBox.alert('提示信息','请选择一条记录!'); return;}}").getScript()+"}");
			egu.addTbarText("-");
			egu.addToolbarItem("{"+new GridButton("回退","function(){if(SelectFrmDiv_sm.hasSelection()){document.getElementById('huitButton').click();}else{Ext.MessageBox.alert('提示信息','请选择一条记录!'); return;}}").getScript()+"}");
			egu.addTbarText("-");
		}

		egu.addToolbarItem("{"+new GridButton("审核意见","function(){ " +
				"if(SelectFrmDiv_sm.hasSelection()){" +
				" if(weiz.getRawValue()=='流程中'){ " +
				" document.getElementById('DivMy_opinion').className = 'x-hidden';}" +
				"	window_panel.show(); " +
				"  rec = SelectFrmDiv_grid.getSelectionModel().getSelections(); " +
				" for(var i=0;i<rec.length;i++){ " +
				" 	var strtmp=rec[i].get('HISTRYYJ').replace(/_/g,'\\n');" +
				" document.getElementById('Histry_opinion').value=strtmp;}"+
				" }else{ "+
				" 	Ext.MessageBox.alert('提示信息','请选择一条记录!');} "+
				"}").getScript()+"}");
		
		egu.addTbarText("-");
		egu.addTbarText("年份:");
		ComboBox comb1=new ComboBox();
		comb1.setId("nianf");
		comb1.setWidth(100);
		comb1.setTransform("NianfDropDown");
		comb1.setLazyRender(true);//动态绑定
		egu.addToolbarItem(comb1.getScript());
		//
		egu.addTbarText("-");
		egu.addTbarText("状态:");
		ComboBox comb2=new ComboBox();
		comb2.setId("weiz");
		comb2.setWidth(100);
		comb2.setTransform("weizSelect");
		comb2.setLazyRender(true);//动态绑定weizSelect
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("nianf.on('select',function(){document.forms[0].submit();});weiz.on('select',function(){document.forms[0].submit()});");
		
//		自己的任务
		if(getweizSelectValue().getId()==1){
			egu.addOtherScript("SelectFrmDiv_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");
		}

		egu.addTbarText("-");
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
	
//		审核意见
		rs=con.getResultSetList(sql);
		List tmp= new ArrayList();
		for(int i=0;i<rs.getRows();i++){
			String strtmp="";
			tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,0)));
			for(int j=0;j<tmp.size();j++){
				strtmp+=((Yijbean)tmp.get(j)).getXitts()+(((Yijbean)tmp.get(j)).getYij()==null?"":((Yijbean)tmp.get(j)).getYij())+"_";
			}
			egu.setDataValue(i, 11, strtmp);
		}
		rs.close();
		((Visit) this.getPage().getVisit()).setExtGrid1(egu);
		setXiaox("");
	}
	
	
	/**1, 根据合同状态动作表找出下一个状态，进行更新
	 * 2,在更新合同状态的同时书写日志
	 */
	private void tij(){
//		List list=getEditValues();
		if(getEditTableRow()!=-1){
			ExtGridUtil ExtGrid1=((Visit) this.getPage().getVisit()).getExtGrid1();
			String tableName="NIANDJH_Zhib";
			String tableName1="YUEDJH_Zhib";
			long renyxxb_id=((Visit) getPage().getVisit()).getRenyID();
			
			if(ExtGrid1.griddata[getEditTableRow()][1]!=null && ExtGrid1.griddata[getEditTableRow()][1].equals("年计划")){
				Liuc.tij(tableName,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][0]) , renyxxb_id, getXiaox());
			}else if(ExtGrid1.griddata[getEditTableRow()][1]!=null && ExtGrid1.griddata[getEditTableRow()][1].equals("月计划")){
				Liuc.tij(tableName1,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][0]) , renyxxb_id, getXiaox());
			}
//			setEditTableRow(-1);
		}
	}
	/**1, 根据合同状态动作表找出下一个状态，进行更新
	 * 2,在更新合同状态的同时书写日志
	 */
	private void huit(){
//		List list=getEditValues();
		if(getEditTableRow()!=-1){
			ExtGridUtil ExtGrid1=((Visit) this.getPage().getVisit()).getExtGrid1();
			String tableName="NIANDJH_Zhib";
			String tableName1="YUEDJH_Zhib";
			long renyxxb_id=((Visit) getPage().getVisit()).getRenyID();
			
			if(ExtGrid1.griddata[getEditTableRow()][1]!=null && ExtGrid1.griddata[getEditTableRow()][1].equals("年计划")){
				Liuc.huit(tableName,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][0]), renyxxb_id, getXiaox());
			}else if(ExtGrid1.griddata[getEditTableRow()][1]!=null && ExtGrid1.griddata[getEditTableRow()][1].equals("月计划")){
				Liuc.huit(tableName1,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][0]), renyxxb_id, getXiaox());
			}
//			setEditTableRow(-1);
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean5(true);
			return;
		} else {
			visit.setboolean5(false);
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setDropDownBean1(null);
			((Visit) getPage().getVisit()).setProSelectionModel1(null);
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setDropDownBean3(null);
			((Visit) getPage().getVisit()).setProSelectionModel3(null);
			((Visit) getPage().getVisit()).setDropDownBean4(null);
			((Visit) getPage().getVisit()).setProSelectionModel4(null);
			setXiaox(null);
			setTreeid("");
			visit.setboolean4(true);
			visit.setString2("");
		}
		getSelectData();
	}

	//单位
	public IDropDownBean getdanwSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean1()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getdanwSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean1();
    }
 
    public void setdanwSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean1()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean1().getId()){
	    		((Visit) getPage().getVisit()).setboolean3(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean3(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean1(Value);
    	}
    }
    public void setdanwSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public IPropertySelectionModel getdanwSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getdanwSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void getdanwSelectModels() {
        String sql = 
        	"select id,mingc,jib\n" +
        	"from(\n" + 
        	" select id,mingc,0 as jib\n" + 
        	" from diancxxb\n" + 
        	" where id="+ ((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" union\n" + 
        	" select *\n" + 
        	" from(\n" + 
        	" select id,mingc,level as jib\n" + 
        	"  from diancxxb\n" + 
        	" start with fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" connect by fuid=prior id\n" + 
        	" order SIBLINGS by  xuh)\n" + 
        	" )\n" + 
        	" order by jib";
        List dropdownlist = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String mc = rs.getString("mingc");
				int jib=rs.getInt("jib");
				String nbsp=String.valueOf((char)0xA0);
				for(int i=0;i<jib;i++){
					mc=nbsp+nbsp+nbsp+nbsp+mc;
				}
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
        ((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(dropdownlist)) ;
        return ;
    }
    //位置
    public IDropDownBean getweizSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getweizSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
 
    public void setweizSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }
    public void setweizSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getweizSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getweizSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getweizSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"我的任务"));
        list.add(new IDropDownBean(2,"流程中"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
    
    //我的意见下拉框
    public IDropDownBean getwodyjSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getwodyjSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
 
    public void setwodyjSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean4()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean4().getId()){
	    		((Visit) getPage().getVisit()).setboolean6(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean6(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
    	}
    }
    public void setwodyjSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getwodyjSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getwodyjSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public void getwodyjSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"同意"));
        list.add(new IDropDownBean(2,"不同意"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }
    
    //年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setNianfValue(IDropDownBean Value) {
		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean3().getId()){
	    		((Visit) getPage().getVisit()).setboolean2(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean2(false);
	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
    public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date())+2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		 ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listNianf)) ;
	}

    //ext代码
	public String getGridHtml() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1().getHtml();
	}
	public String getGridScript() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1().getGridScript();
	}
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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
//	我的意见
	public void setMy_opinion(String value){
		
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getMy_opinion(){
		
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	//历史意见
	public void setHistry_opinion(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getHistry_opinion(){
		
		return ((Visit) getPage().getVisit()).getString2();
	}
}