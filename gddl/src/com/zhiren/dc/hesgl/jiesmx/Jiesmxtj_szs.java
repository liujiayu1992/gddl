package com.zhiren.dc.hesgl.jiesmx;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jiesmxtj_szs extends BasePage implements PageValidateListener {

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	// 报表初始设置
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// submit
	public void submit(IRequestCycle cycle) {

	}

	// 报表主体
	public String getPrintTable() {
		return getjiesmxtj();
	}
	
//	结算统计
	private String getjiesmxtj(){
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String strSQL= new String();
		String beginsj=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		Date lastday=DateUtil.getLastDayOfMonth(DateUtil.getDate(beginsj));
		String endsj=DateUtil.Formatdate("yyyy-MM-dd", lastday);
		strSQL = 
			"select " +
//			显示外连接
			"'<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Jiesmx_szs_da&jiesb_id='||j.id||'>'||j.bianm||'</a>',\n" +
			"       h.hetbh,\n" + 
			"       gongysmc,\n" + 
			"       to_char(fahksrq,'yyyy-mm-dd') || '至' || to_char(fahjzrq,'yyyy-mm-dd'),\n" + 
			"       to_char(jiesrq,'yyyy-mm-dd'),\n" + 
			"       jiessl,\n" + 
			"       hansdj,\n" + 
			"       yunfhsdj,\n" + 
			"       hansmk + hansyf,\n" + 
			"       hansdj + yunfhsdj,\n" + 
			"       round_new((hansdj + yunfhsdj)*29.271/(select round_new(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),2) from fahb f,zhilb z,jiesb jj where f.zhilb_id=z.id and f.jiesb_id=jj.id and jj.id=j.id),2),\n" + 
			"       round_new((hansdj + yunfhsdj)*29.271/(select round_new(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),2) from fahb f,zhilb z,jiesb jj where f.zhilb_id=z.id and f.jiesb_id=jj.id and jj.id=j.id)*(1-0.17),2)\n" +
			"  from jiesb j, hetb h\n" + 
			" where j.hetb_id = h.id\n" + 
			"	and j.diancxxb_id=" + getTreeid() + "\n" +
			"   and jiesrq>=date'"+beginsj+"'\n" + 
			"   and jiesrq<=date'"+endsj+"' order by jiesrq";
		
		strSQL = 
			"select jiesbh,hetbh,gongysmc,riq,jiesrq,\n" +
			"       sum(jiessl),\n" + 
			"       decode(sum(jiessl),0,0,round_new(sum(hansdj*jiessl)/sum(jiessl),2)) hansdj,\n" + 
			"       decode(sum(jiessl),0,0,round_new(sum(yunfhsdj*jiessl)/sum(jiessl),2)) yunfhsdj,\n" + 
			"       sum(jine),\n" + 
			"       decode(sum(jiessl),0,0,round_new(sum(daocj*jiessl)/sum(jiessl),2)) daocj,\n" + 
			"       decode(sum(jiessl),0,0,round_new(sum(hansbmdj*jiessl)/sum(jiessl),2)) hansbmdj,\n" + 
			"       decode(sum(jiessl),0,0,round_new(sum(buhsbmdj*jiessl)/sum(jiessl),2)) buhsbmdj\n" + 
			"  from\n" + 
			"(select '<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Jiesmx_szs_da&jiesb_id='||j.id||'>'||j.bianm||'</a>' jiesbh,\n" + 
			"       h.hetbh,\n" + 
			"       gongysmc,\n" + 
			"       to_char(fahksrq,'yyyy-mm-dd') || '至' || to_char(fahjzrq,'yyyy-mm-dd') riq,\n" + 
			"       to_char(jiesrq,'yyyy-mm-dd') jiesrq,\n" + 
			"       jiessl,\n" + 
			"       hansdj,\n" + 
			"       yunfhsdj,\n" + 
			"       hansmk + hansyf jine,\n" + 
			"       hansdj + yunfhsdj daocj,\n" + 
			"       round_new((hansdj + yunfhsdj)*29.271/(select round_new(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),2) from fahb f,zhilb z,jiesb jj where f.zhilb_id=z.id and f.jiesb_id=jj.id and jj.id=j.id),2) hansbmdj,\n" + 
			"       round_new((hansdj + yunfhsdj)*29.271/(select round_new(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),2) from fahb f,zhilb z,jiesb jj where f.zhilb_id=z.id and f.jiesb_id=jj.id and jj.id=j.id)*(1-0.17),2) buhsbmdj\n" + 
			"  from jiesb j, hetb h\n" + 
			" where j.hetb_id = h.id\n" + 
			"  and j.diancxxb_id=" + getTreeid() + "\n" + 
			"   and jiesrq>=date'"+beginsj+"'\n" + 
			"   and jiesrq<=date'"+endsj+"'\n" + 
			")\n" + 
			"group by rollup ((jiesbh,hetbh,gongysmc,riq,jiesrq,jiessl,hansdj,yunfhsdj,jine,daocj,hansbmdj,buhsbmdj))";

		//报表表头定义
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
//		int iFixedRows=0;//固定行号
		//报表内容
		ArrHeader=new String[1][12];
		ArrHeader[0]=new String[] {"结算单编号","合同编号","供应商名称","发货开始-<br>结束日期","结算日期","结算数量","煤款单价","运费单价","总金额","到厂<br>综合价","不含税<br>标煤单价","含税<br>标煤单价"};
			 
		ArrWidth=new int[] {100,100,100,150,90,60,60,60,60,60,60,60};
//		iFixedRows=1;
//		String arrFormat[]=new String[]{"","","0.000","0.000","0.000","0.000"};
					
		ResultSet rs = cn.getResultSet(strSQL.toString());
		Table tb = new Table(rs,1, 0, 1);
		rt.setBody(tb);
		
		rt.setTitle("结 算 统 计 表", ArrWidth);
//		rt.setDefaultTitle(1, 4, "填报单位(盖章):"+getDiancmcById(strDiancId), Table.ALIGN_LEFT);
//		rt.setDefaultTitle(6, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(15, 3, "单位:元", Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = false;
//		rt.body.setColFormat(arrFormat);
	 
		for (int i=1;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		//页脚 
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton button = new ToolbarButton(null, "刷新",
				"function() {document.forms[0].submit();}");
		button.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(button);
		setToolbar(tb1);
	}

	// 页面初始设置
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setNianfValue(null);
			setYuefValue(null);
			this.setTreeid(null);
			this.getTree();
		}
		
		if (blnDiancChange){
			blnDiancChange=false;
		}
		getToolBars();
	}

	// 页面判定方法
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
	
//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
	}
	
	private boolean blnDiancChange=false;
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		String strDiancID=((Visit) getPage().getVisit()).getString2();
		if (treeid==null){
			blnDiancChange=true;
		} else if(!treeid.equals(strDiancID)){
			blnDiancChange=true;
		}
		
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
	
	
	
//	 年份下拉框
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
}
