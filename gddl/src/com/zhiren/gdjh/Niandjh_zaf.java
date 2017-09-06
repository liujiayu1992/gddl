package com.zhiren.gdjh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2012-10-18
 * 描述：新增复制计划按钮（如果界面中没有数据则显示该按钮，否则不显示）
 * 		增加总厂不能填报的限制。
 */

/*
 * 作者：赵胜男
 * 时间：2012-11-19
 * 描述：对标题名称进行变更，在界面中增加上年完成金额和未完成金额的合计值且界面自动计算
 */
/*
 * 作者：夏峥
 * 时间：2012-11-19
 * 适用范围：国电电力及其下属电厂
 * 描述：调整年计划指标中标煤单价和入炉综合标煤单价的计算公式。
 */
public  class Niandjh_zaf extends BasePage implements PageValidateListener {
	
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//-------------------------------------------------------------  save()  -----------------------------

	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + this.getTreeid();
		return con.getHasIt(sql);
	}
	
	private void Save() {
  		Visit visit = (Visit) this.getPage().getVisit();
 		int flag=visit.getExtGrid1().Save(getChange(), visit);
 		if(flag!=-1){
 			setMsg(ErrorMessage.SaveSuccessMessage);
 		}
 		JDBCcon con = new JDBCcon();
// 		每次保存时重新计算指标中的相关参数
 		countBMDJ(con,this.getTreeid(),"to_date('"+this.getNianfValue().getValue()+"-01-01','yyyy-mm-dd')");
 		con.Close();
 	}
//	复制功能
	private void CopyData() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(true);
		String strDate=getNianfValue().getValue() +"-01-01";
		
		String CopySql=
			"INSERT INTO NIANDJH_ZAF\n" +
			"(ID,DIANCXXB_ID,RIQ,TBRQ,ZAFMC,YUCJE,YUCSM,SHIJWCJE,YUJWCJE,YUJWCSM,ZHUANGT)\n" + 
			"\n" + 
			"(SELECT GETNEWID("+this.getTreeid()+"),DIANCXXB_ID,ADD_MONTHS(RIQ, 12) RIQ,TBRQ,ZAFMC,\n" + 
			"YUCJE,YUCSM,SHIJWCJE,YUJWCJE,YUJWCSM,0 ZHUANGT\n" + 
			"   FROM NIANDJH_ZAF\n" + 
			"  WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n" + 
			"    AND DIANCXXB_ID = "+this.getTreeid()+")";

		con.getInsert(CopySql);
// 		每次保存时重新计算指标中的相关参数
 		countBMDJ(con,this.getTreeid(),"to_date('"+strDate+"','yyyy-mm-dd')");
 		con.Close();
		setMsg("复制操作完成！");
	}	
	
//	每次保存时重新计算指标中的相关参数
	private int countBMDJ(JDBCcon con, String diancxxb_id, String CurrODate) {
		String upsql = 
			"SELECT MEIZBMDJ,\n" +
			"       ZAFJE,\n" + 
			"       ROUND(DECODE(BIAOMLHJ,0,0,(MEIZBML * MEIZBMDJ + RANYL * RANYDJ + ZAFJE) / BIAOMLHJ),2) RULZHBMDJ\n" + 
			"  FROM (SELECT NVL(CGJH.JIH_REZ, 0) JIH_REZ,\n" + 
			"               NVL(CGJH.JIH_DAOCJ, 0) JIH_DAOCJ,\n" + 
			"               NVL(ZAF.ZAFJE, 0) ZAFJE,\n" + 
			"               NVL(ZHIB.RUCRLRZC, 0) RUCRLRZC,\n" + 
			"               NVL(ZHIB.MEIZBML, 0) MEIZBML,\n" + 
			"               NVL(ZHIB.RANYL, 0) RANYL,\n" + 
			"               NVL(ZHIB.RANYDJ, 0) RANYDJ,\n" + 
			"               NVL(ZHIB.BIAOMLHJ, 0) BIAOMLHJ,\n" + 
			"               ROUND(DECODE((NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0)),0,0,NVL(JIH_DAOCJ, 0) * 29.2712 /(NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0))),2) MEIZBMDJ\n" + 
			"          FROM (SELECT SUM(JIH_SL) JIHSL,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * JIH_REZ) / SUM(JIH_SL)) JIH_REZ,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * (JIH_MEIJBHS+JIH_YUNJBHS+JIH_ZAFBHS+JIH_QITBHS)) / SUM(JIH_SL)) JIH_DAOCJ\n" + 
			"                  FROM NIANDJH_CAIG\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") CGJH,\n" + 
			"               (SELECT SUM(YUCJE) ZAFJE\n" + 
			"                  FROM NIANDJH_ZAF\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND TRUNC(RIQ, 'yyyy') = "+CurrODate+") ZAF,\n" + 
			"               (SELECT RUCRLRZC, MEIZBML, RANYL, RANYDJ, BIAOMLHJ\n" + 
			"                  FROM NIANDJH_ZHIB\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") ZHIB) SJ";
		
		ResultSetList rsl = con.getResultSetList(upsql);
		
		String updateSql="";
		if(rsl.next()) {
			double MEIZBMDJ=rsl.getDouble("MEIZBMDJ"); 
			double QITFY=rsl.getDouble("ZAFJE"); 
			double RULZHBMDJ=rsl.getDouble("RULZHBMDJ"); 
			updateSql = "update niandjh_zhib set MEIZBMDJ="+MEIZBMDJ+", QITFY="+QITFY+", RLZHBMDJ="+RULZHBMDJ+" where riq = " + CurrODate + " and diancxxb_id=" + diancxxb_id;
		}
		rsl.close();
		if(updateSql.length()>1){
			con.getUpdate(updateSql);
			return 1;
		}else{
			return -1;
		}
		
	}
	
//	---------------------------------------------save end--------------------------------------------------
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _Cpyclick = false;

	public void CpyButton(IRequestCycle cycle) {
		_Cpyclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		if(_Cpyclick){
			_Cpyclick=false;
			CopyData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql=
			"SELECT ID,\n" +
			"       DIANCXXB_ID,\n" + 
			"       RIQ,TBRQ,\n" + 
			"       ZAFMC,\n" + 
			"       YUCJE,\n" + 
			"       YUCSM,\n" +
			" (SHIJWCJE+YUJWCJE) AS shnyjwcje,\n" + 
			"       SHIJWCJE,\n" + 
			"       YUJWCJE,\n" + 
			"       YUJWCSM,\n" + 
			"       ZHUANGT\n" + 
			"  FROM NIANDJH_ZAF\n" + 
			" WHERE DIANCXXB_ID = "+this.getTreeid()+"\n" + 
			"   AND RIQ = TO_DATE('"+this.getNianfValue().getValue()+"-01-01','yyyy-mm-dd')\n" +
			"	ORDER BY RIQ,ID";

		ResultSetList rsl = con.getResultSetList(sql);
			
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("NIANDJH_ZAF");
		egu.setWidth("bodyWidth");

		String riq=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date()));
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setDefaultValue(this.getTreeid());
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("RIQ").setHeader("日期");
		egu.getColumn("RIQ").setEditor(null);
		egu.getColumn("RIQ").setHidden(true);
		egu.getColumn("RIQ").setDefaultValue(this.getNianfValue().getValue()+"-01-01");
		egu.getColumn("RIQ").setWidth(50);
		
		egu.getColumn("TBRQ").setHeader("填报日期");
		egu.getColumn("TBRQ").setEditor(null);
		egu.getColumn("TBRQ").setHidden(true);
		egu.getColumn("TBRQ").setDefaultValue(riq);
		egu.getColumn("TBRQ").setWidth(50);
		
		egu.getColumn("ZAFMC").setCenterHeader("费用名称");
		egu.getColumn("ZAFMC").setWidth(150);
		egu.getColumn("ZAFMC").editor.setAllowBlank(false);
		
		egu.getColumn("YUCJE").setCenterHeader((this.getNianfValue().getValue())+"年<br>预测金额<br>(元)");
		egu.getColumn("YUCJE").setWidth(100);
		egu.getColumn("YUCJE").setDefaultValue("0");
		egu.getColumn("YUCJE").editor.setAllowBlank(false);

		egu.getColumn("YUCSM").setCenterHeader("预算年度费用说明");
		egu.getColumn("YUCSM").setWidth(250);
		egu.getColumn("YUCSM").editor.setAllowBlank(false);
		
		egu.getColumn("shnyjwcje").setCenterHeader("上年预计<br>完成金额<br>(元)");
		egu.getColumn("shnyjwcje").setDefaultValue("0");
		egu.getColumn("shnyjwcje").setWidth(100);
		egu.getColumn("shnyjwcje").setEditor(null);
		egu.getColumn("shnyjwcje").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("shnyjwcje").setUpdate(false);
		egu.getColumn("SHIJWCJE").setCenterHeader("上年已完成金额<br>(元)");
		egu.getColumn("SHIJWCJE").setDefaultValue("0");
		egu.getColumn("SHIJWCJE").setWidth(100);
		
		egu.getColumn("YUJWCJE").setCenterHeader("上年未完成金额<br>(元)");
		egu.getColumn("YUJWCJE").setDefaultValue("0");
		egu.getColumn("YUJWCJE").setWidth(100);
		
		egu.getColumn("YUJWCSM").setCenterHeader("上年费用情况说明");
		egu.getColumn("YUJWCSM").setWidth(250);
		
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setDefaultValue("0");
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setEditor(null);
		
//		杂费类型下拉框
		String cbosql="SELECT ID, MINGC\n" +
			"  FROM ITEM\n" + 
			" WHERE ITEMSORTID IN (SELECT ID FROM ITEMSORT WHERE BIANM = 'ZAFMC')\n" + 
			" ORDER BY XUH";
		egu.getColumn("ZAFMC").setEditor(new ComboBox());
		egu.getColumn("ZAFMC").setComboEditor(egu.gridId, new IDropDownModel(cbosql));
		egu.getColumn("ZAFMC").setReturnId(false);//绑定了，页面显示汉字，库里显示数字。
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(0);
		egu.setDefaultsortable(false);//设定页面不自动排序
		
//		工具栏
		egu.addTbarText("预算年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NianfDropdown");
		comb1.setId("NianfDropdown");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(80);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
//		电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		grid数据匹配方法
		String script="gridDiv_grid.on('afteredit',function(e)\n"+
				"{\n"+
			"rec = e.record;\n"+
			"var zafmc=rec.get('ZAFMC');\n"+
			"if(e.field=='ZAFMC')\n"+
			"{\n"+
			"	for (var i=0;i<zafxx.length;i++ ){\n"+
			"		if(zafxx[i][0]==zafmc){\n"+
			"			rec.set('SHIJWCJE',zafxx[i][1]);\n"+
			"			}\n"+
			"	}	\n"+
			"}\n"+
			"	 var shjwcje=parseFloat(e.record.get('SHIJWCJE')==''?0:e.record.get('SHIJWCJE'));\n"+
			"	var yujwcje=parseFloat(e.record.get('YUJWCJE')==''?0:e.record.get('YUJWCJE'));\n"+
			"	var shnyjwcje=shjwcje+yujwcje;\n"+
			"	 e.record.set('SHNYJWCJE',shnyjwcje);\n"+
			"});\n";
		
		egu.addOtherScript(script);
		
		
		
		
//		 判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if (getZhangt(con)) {
			if(visit.getDiancxxb_id()==112){
				egu.addTbarText("-");
				egu.addToolbarButton(GridButton.ButtonType_Insert, null);
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			}else{
				setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
			}
		} else {
			if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("计划模块", "分厂别总厂显示按钮", this.getTreeid(), "否").equals("否")){
				
			}else{
				egu.addTbarText("-");
				egu.addToolbarButton(GridButton.ButtonType_Insert, null);
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//				如果界面中没有数据则显示复制按钮
				if(!con.getHasIt(sql)){
					String handler="function (){document.getElementById('CpyButton').click();"+
					"Ext.MessageBox.show({msg:'正在处理数据,请稍后...'," +
					"progressText:'处理中...',width:300,wait:true,waitConfig: " +
					"{interval:200},icon:Ext.MessageBox.INFO});}";
					GridButton cpy = new GridButton("复制上年度计划", handler);
					cpy.setIcon(SysConstant.Btn_Icon_Copy);
					egu.addTbarBtn(cpy);
				}
			}
		}
		setExtGrid(egu);
		con.Close();
	}
	
	/**
	 * @param con
	 * @return true:已上传状态中 不能修改数据 false:未上传状态中 可以修改数据
	 */
	private boolean getZhangt(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(this.getNianfValue().getValue() + "-01-01");
		String sql = "select s.zhuangt zhuangt\n" + "  from NIANDJH_ZAF s\n"
				+ " where  s.diancxxb_id = " + getTreeid() + "\n"
				+ "   and s.riq = " + CurrODate;
		ResultSetList rs = con.getResultSetList(sql);
		boolean zt = true;
		if (con.getHasIt(sql)) {
			while (rs.next()) {
				if (rs.getInt("zhuangt") == 0 || rs.getInt("zhuangt") == 2) {
					zt = false;
				}
			}
		} else {
			zt = false;
		}
		return zt;
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
	
	public void setZafxx(){
		Visit visit = (Visit) getPage().getVisit();
		String riq=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date()));
		String rslSQL="SELECT ZAFLX.MINGC, NVL(ZAFJE.JINE, 0) ZAFJE\n" +
			"  FROM (SELECT XUH, MINGC\n" + 
			"          FROM ITEM\n" + 
			"         WHERE ITEMSORTID IN (SELECT ID FROM ITEMSORT WHERE BIANM = 'ZAFMC')) ZAFLX,\n" + 
			"       (SELECT SUM(JINE) JINE, MINGC\n" + 
			"          FROM ZAFB\n" + 
			"         WHERE RIQ BETWEEN TRUNC(DATE '"+riq+"', 'yyyy') AND DATE'"+riq+"'\n" + 
			"         GROUP BY MINGC) ZAFJE\n" + 
			" WHERE ZAFLX.MINGC = ZAFJE.MINGC(+)\n" + 
			" ORDER BY ZAFLX.XUH";
		
		JDBCcon con = new JDBCcon();
		ResultSetList list=con.getResultSetList(rslSQL.toString());
		String rsl="var zafxx=[ ";
		while(list.next()){
			String ZAFMC=list.getString("MINGC");
			String ZAFJE=list.getString("ZAFJE");
			rsl+="[\""+ZAFMC+"\",\""+ZAFJE+"\"],";
		}
		rsl=rsl.substring(0, rsl.length()-1);
		rsl+="];";
		list.close();
		con.Close();
		visit.setString3(rsl);
	}
	
	public String getZafxx(){
		Visit visit = (Visit) getPage().getVisit();
		if(visit.getString3()==null ||visit.getString3().equals("")){
			setZafxx();
		}
		return visit.getString3();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			getTreeid();
			this.setNianfValue(null);
			this.getNianfModels();
			setZafxx();
		}
		getSelectData();

	}
	
//	年份下拉框
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			int _nianf = DateUtil.getYear(new Date())+1;
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setNianfValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setNianfModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2009; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(listNianf));
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

}
