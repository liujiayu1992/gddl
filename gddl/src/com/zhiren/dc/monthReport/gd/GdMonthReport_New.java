package com.zhiren.dc.monthReport.gd;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：夏峥
 * 时间：2012-05-15
 * 描述：基于GdMonthReport.java 1.1.1.42制作此报表
 */
/*
 * 作者：夏峥
 * 时间：2012-07-04
 * 描述: 调整01表调出量的取值方式
 */
public class GdMonthReport_New extends BasePage {
	
	private static final String ITEM_ONE = "*"; //计划口径合计前添加的符号，最后转化为大写数字序号
	
	private static final String ITEM_TWO = "#"; //统配、地方小计前添加的符号，最后转化为小写数字序号
	
	private static final String GD_DR01 = "diaor01b";
	
	private static final String GD_DR02 = "diaor02b";
	
	private static final String GD_DR03 = "diaor03b";
	
	private static final String GD_DR04 = "diaor04b";
	
	private static final String GD_RANYHCGY = "ranyhcgy";//生产用燃油供应、耗用与结存月报
	
	private static final String GD_RANLCB = "ranlcb";//发电、供热耗用燃料成本月报
	
	private static final String GD_RUCBMDJ = "rucbmdj";//入厂标煤单价
	
	private static final String GD_JIESBMDJ = "jiesbmdj";//结算标煤单价
	
	private String _msg;
	
	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	public boolean getRaw() {
		return true;
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
	
	public int paperStyle;
	
	private void paperStyle() {
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='纸张类型' and diancxxb_id in ( "
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id()+" ) ");
						 
		if (rsl.next()) {
			paperStyle = rsl.getInt("zhi");
		}

		this.paperStyle = paperStyle;
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
	}
	//增加电厂多选树的级联
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	private boolean reportShowZero(){
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}

//	年份下拉框
    public IDropDownBean getNianfValue() {
//    	如果年份值为空那么初始化年份
        if (((Visit)this.getPage().getVisit()).getDropDownBean3() == null) {
//			取得当前年份值
        	int _nianf = DateUtil.getYear(new Date());
//			取得当前月份值
        	int _yuef = DateUtil.getMonth(new Date());
//        	如果月份为1,那么年份值应为上一年
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                	setNianfValue((IDropDownBean) obj);
                    break;
                }
            }
        }
        return ((Visit)this.getPage().getVisit()).getDropDownBean3();
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	((Visit)this.getPage().getVisit()).setDropDownBean3(Value);
    }
    
    public IPropertySelectionModel getNianfModel() {
        if (((Visit)this.getPage().getVisit()).getProSelectionModel3() == null) {
            getNianfModels();
        }
        return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
    }
    
    public void setNianfModel(IPropertySelectionModel _value) {
    	((Visit)this.getPage().getVisit()).setProSelectionModel3(_value);
    }

    public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
    	setNianfModel(new IDropDownModel(listNianf));
    }

//	月份下拉框
	public IDropDownBean getYuefValue() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean4() == null) {
//	    	得到月份值
	    	int _yuef = DateUtil.getMonth(new Date());
//			如果月份为1，那么月份等于12，否则月份等于上个月
	    	if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
		        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
		            Object obj = getYuefModel().getOption(i);
		            if (_yuef == ((IDropDownBean) obj).getId()) {
		            	setYuefValue((IDropDownBean) obj);
		                break;
		        }
	    	}
	    }
	    return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	
	public void setYuefValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(Value);
	}
	
	public IPropertySelectionModel getYuefModel() {
	    if (((Visit)this.getPage().getVisit()).getProSelectionModel4() == null) {
	        getYuefModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	
	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(_value);
    }

    public void getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
    	setYuefModel(new IDropDownModel(listYuef));
    }
	
// 报表类型下拉框
//	类型下拉框
    public IDropDownBean getLeixValue() {
    	if(((Visit)this.getPage().getVisit()).getDropDownBean5()==null){
    		if (getLeixModel().getOptionCount()>0) {
				setLeixValue((IDropDownBean)getLeixModel().getOption(0));
			}
    	}
    	return ((Visit)this.getPage().getVisit()).getDropDownBean5();
    }
    public void setLeixValue(IDropDownBean v) {
    	((Visit)this.getPage().getVisit()).setDropDownBean5(v);
    }
    public IPropertySelectionModel getLeixModel(){
    	if (((Visit)this.getPage().getVisit()).getProSelectionModel5() == null) {
	        getLeixModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel5();
    }
    public void setLeixModel(IPropertySelectionModel _value){
    	((Visit)this.getPage().getVisit()).setProSelectionModel5(_value);
    }
    public void getLeixModels() {
    	List list=new ArrayList();
    	if(this.getReportType().equals(GD_DR04)||getReportType().equals(GD_RUCBMDJ)||getReportType().equals(GD_JIESBMDJ)){
    		list.add(new IDropDownBean("1","供应商汇总"));
        	list.add(new IDropDownBean("2","品种明细"));
    	}else{
    		list.add(new IDropDownBean("1","地区汇总"));
        	list.add(new IDropDownBean("2","分矿明细"));
    	}
    	
    	setLeixModel(new IDropDownModel(list));
    }
	
	public String getTianzdwQuanc(){
		String[] str=getTreeid().split(",");
		if(str.length>1){
			return "组合电厂";
		}else{
			return getTianzdwQuanc(Long.parseLong(str[0]));
		}
	}
	
	public long getDiancxxbId(){	
		return ((Visit)getPage().getVisit()).getDiancxxb_id();
	}
	
	//得到单位全称
	public String getTianzdwQuanc(long gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
	}
	
	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
	
//	分厂别
	public boolean isFencb() {
		return ((Visit) this.getPage().getVisit()).isFencb();
	}
	
	// 页面初始设置
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if(cycle.getRequestContext().getParameter("lx") != null){
			visit.setString1(cycle.getRequestContext().getParameter("lx"));
			init();
		}
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			if (visit.getString1() == null || "".equals(visit.getString1())) {
				visit.setString1(GD_DR01);
			}
			init();
		}
	}
	
	private void init(){
		Visit visit = (Visit) getPage().getVisit();
		visit.setProSelectionModel1(null);
		visit.setDropDownBean1(null);
		visit.setProSelectionModel2(null);
		visit.setDropDownBean2(null);
		setNianfModel(null);
		setNianfValue(null);
		setYuefModel(null);
		setYuefValue(null);
		visit.setExtTree1(null);
		setDiancmcModel(null);
		initDiancTree();
//		初始化类型下拉框
		getLeixModels();
		setLeixModel(null);
		setLeixValue(null);
		paperStyle();
		getSelectData();
	}
//	初始化多选电厂树中的默认值
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" + 
			" WHERE JIB > 2\n" + 
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" + 
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql); 
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
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
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("组合电厂");
		}else{
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}
		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);	
		tb1.addText(new ToolbarText("-"));
		
		if(this.getReportType().equals(GD_DR01)||
				this.getReportType().equals(GD_DR02)||
				this.getReportType().equals(GD_DR03)||
				this.getReportType().equals(GD_DR04)||
				this.getReportType().equals(GD_RUCBMDJ)||
				getReportType().equals(GD_JIESBMDJ)){
			tb1.addText(new ToolbarText("报表类型:"));
			ComboBox leix = new ComboBox();
			leix.setTransform("LeixDropDown");
			leix.setWidth(100);
			tb1.addField(leix);
			tb1.addText(new ToolbarText("-"));
		}


		ToolbarButton tb = new ToolbarButton(null, "查询",
			"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	// submit
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();			
		}
		getSelectData();
	}

	// 报表主体
	public String getPrintTable() {	
		if (getReportType().equals(GD_DR01)) {
			return getGdran01b();
		} else if (getReportType().equals(GD_DR02)) {
			return getGdran02b();
		} else if (getReportType().equals(GD_DR03)) {
			return getGdran03b();			
		} else if (getReportType().equals(GD_DR04)) {
			return getGdran04b();			
		} else if(getReportType().equals(GD_RANYHCGY)){
			return getRanyhcgy();
		} else if(getReportType().equals(GD_RANLCB)){
			return getRanlcb();
		} else if(getReportType().equals(GD_RUCBMDJ)){
			return getRucbmdj();
		} else if (getReportType().equals(GD_JIESBMDJ)){
			return getJiesbmdj();
		}else{
			return "无此报表";
		}		
	}
	
	private String getBaseSql(String strDate, String diancxxb_id) {
		Visit visit = (Visit) getPage().getVisit();
		String SQL="";
		if (this.getReportType().equals(GD_DR01)) {
			SQL="SELECT KJ.GONGYSB_ID,\n" +
			"       KJ.DQID,\n" + 
			"       KJ.DQMC,\n" + 
			"       SHENGFB.QUANC QUANC,\n" + 
			"       DECODE(KJ.JIHKJB_ID, 1, '重点订货', 3, '重点订货', J.MINGC) TJKJ,\n" + 
			"       KJ.JIHKJB_ID,\n" + 
			"       KJ.PINZB_ID,\n" + 
			"       YUNSFSB_ID,\n" + 
			"       DIANCXXB_ID,\n" + 
			"       KJ.FENX,\n" + 
			"       SL.JINGZ AS JINGZ,\n" + 
			"       SL.YUNS,\n" + 
			"       HC.QICKC,\n" + 
			"       HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH AS HEJ,\n" + 
			"       HC.FADY,\n" + 
			"       HC.GONGRY,\n" + 
			"       HC.QITH,\n" + 
			"       HC.SUNH,\n" + 
			"       -HC.DIAOCL DIAOCL,\n" + 
			"       HC.SHUIFCTZ SHUIFTZ,\n" + 
			"       HC.PANYK,\n" + 
			"       HC.KUC\n" + 
			"  FROM YUESLB SL,\n" + 
			"       YUEHCB HC,\n" + 
			"       (SELECT K.ID,\n" + 
			"               DQ.DQID,\n" + 
			"               DQ.DQMC,\n" + 
			"               K.GONGYSB_ID,\n" + 
			"               K.JIHKJB_ID,\n" + 
			"               K.PINZB_ID,\n" + 
			"               K.YUNSFSB_ID,\n" + 
			"               K.DIANCXXB_ID,\n" + 
			"               NVL('本月', '') AS FENX\n" + 
			"          FROM YUETJKJB K, VWGONGYSDQ DQ\n" + 
			"         WHERE K.RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			"           AND DQ.ID = K.GONGYSB_ID\n" + 
			"           AND K.DIANCXXB_ID IN (" + diancxxb_id + ")\n" + 
			"        UNION\n" + 
			"        SELECT K.ID,\n" + 
			"               DQ.DQID,\n" + 
			"               DQ.DQMC,\n" + 
			"               K.GONGYSB_ID,\n" + 
			"               K.JIHKJB_ID,\n" + 
			"               K.PINZB_ID,\n" + 
			"               K.YUNSFSB_ID,\n" + 
			"               K.DIANCXXB_ID,\n" + 
			"               NVL('累计', '') AS FENX\n" + 
			"          FROM YUETJKJB K, VWGONGYSDQ DQ\n" + 
			"         WHERE K.RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			"           AND DQ.ID = K.GONGYSB_ID\n" + 
			"           AND K.DIANCXXB_ID IN (" + diancxxb_id + ")) KJ,\n" + 
			"       JIHKJB J,\n" + 
			"       MEIKDQB,\n" + 
			"       SHENGFB\n"+ 
			" WHERE KJ.ID = SL.YUETJKJB_ID(+) \n";
			if(visit.getDiancxxb_id()==112){SQL+="AND (SL.ZHUANGT=1 OR SL.ZHUANGT=3) AND (HC.ZHUANGT=1 OR HC.ZHUANGT=3)\n";} 
			SQL+="   AND KJ.FENX = SL.FENX(+)\n" + 
			"   AND KJ.ID = HC.YUETJKJB_ID(+)\n" + 
			"   AND KJ.FENX = HC.FENX(+)\n" + 
			"   AND KJ.JIHKJB_ID = J.ID\n" + 
			"   AND KJ.DQID = MEIKDQB.ID\n" + 
			"   AND MEIKDQB.SHENGFB_ID = SHENGFB.ID";
			
		} else if (this.getReportType().equals(GD_DR02)) {
			SQL="SELECT KJ.GONGYSB_ID,\n" +
			"       KJ.DQID,\n" + 
			"       KJ.DQMC,\n" + 
			"       SHENGFB.QUANC QUANC,\n" + 
			"       DECODE(KJ.JIHKJB_ID, 1, '重点订货', 3, '重点订货', J.MINGC) TJKJ,\n" + 
			"       KJ.JIHKJB_ID,\n" + 
			"       KJ.PINZB_ID,\n" + 
			"       KJ.YUNSFSB_ID,\n" + 
			"       DIANCXXB_ID,\n" + 
			"       KJ.FENX,\n" + 
			"       SL.JINGZ+sl.yuns AS JINCML,\n" + 
			"       SL.JINGZ+sl.yuns AS HEJ,\n" + 
			"       SL.JINGZ+sl.yuns AS JIANJL,\n" + 
			"       0 AS JIANCL,\n" + 
			"       0 YINGD,\n" + 
			"       0 YINGDZJE,\n" + 
			"       0 KUID,\n" + 
			"       SL.KUIDZJE,\n" + 
			"       SL.SUOPSL,\n" + 
			"       SL.SUOPJE\n" + 
			"  FROM YUESLB SL,\n" + 
			"       (SELECT K.ID,\n" + 
			"               DQ.DQID,\n" + 
			"               DQ.DQMC,\n" + 
			"               K.GONGYSB_ID,\n" + 
			"               K.JIHKJB_ID,\n" + 
			"               K.PINZB_ID,\n" + 
			"               K.YUNSFSB_ID,\n" + 
			"               K.DIANCXXB_ID,\n" + 
			"               NVL('本月', '') AS FENX\n" + 
			"          FROM YUETJKJB K, VWGONGYSDQ DQ\n" + 
			"         WHERE K.RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			"           AND DQ.ID = K.GONGYSB_ID\n" + 
			"           AND K.DIANCXXB_ID IN (" + diancxxb_id + ")\n" + 
			"        UNION\n" + 
			"        SELECT K.ID,\n" + 
			"               DQ.DQID,\n" + 
			"               DQ.DQMC,\n" + 
			"               K.GONGYSB_ID,\n" + 
			"               K.JIHKJB_ID,\n" + 
			"               K.PINZB_ID,\n" + 
			"               K.YUNSFSB_ID,\n" + 
			"               K.DIANCXXB_ID,\n" + 
			"               NVL('累计', '') AS FENX\n" + 
			"          FROM YUETJKJB K, VWGONGYSDQ DQ\n" + 
			"         WHERE K.RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			"           AND DQ.ID = K.GONGYSB_ID\n" + 
			"           AND K.DIANCXXB_ID IN (" + diancxxb_id + ")) KJ,\n" + 
			"       JIHKJB J,\n" + 
			"       MEIKDQB,\n" + 
			"       SHENGFB\n" + 
			" WHERE KJ.ID = SL.YUETJKJB_ID(+) \n"; 
			if(visit.getDiancxxb_id()==112){SQL+="AND (SL.ZHUANGT=1 OR SL.ZHUANGT=3)\n";} 
			SQL+="   AND KJ.FENX = SL.FENX(+)\n" + 
			"   AND KJ.JIHKJB_ID = J.ID\n" + 
			"   AND KJ.DQID = MEIKDQB.ID\n" + 
			"   AND MEIKDQB.SHENGFB_ID = SHENGFB.ID";

		} else if(this.getReportType().equals(GD_DR03)) {
			SQL="SELECT KJ.GONGYSB_ID,\n" +
			"       KJ.DQID,\n" + 
			"       KJ.DQMC,\n" + 
			"       SHENGFB.QUANC QUANC,\n" + 
			"       DECODE(KJ.JIHKJB_ID, 1, '重点订货', 3, '重点订货', J.MINGC) TJKJ,\n" + 
			"       KJ.JIHKJB_ID,\n" + 
			"       KJ.PINZB_ID,\n" + 
			"       KJ.YUNSFSB_ID,\n" + 
			"       DIANCXXB_ID,\n" + 
			"       KJ.FENX AS FX,\n" + 
			"       SL.JINGZ+sl.yuns AS JINCML,\n" + 
			"       ZL.*\n" + 
			"  FROM YUESLB SL,\n" + 
			"       YUEZLB ZL,\n" + 
			"       (SELECT K.ID,\n" + 
			"               DQ.DQID,\n" + 
			"               DQ.DQMC,\n" + 
			"               K.GONGYSB_ID,\n" + 
			"               K.JIHKJB_ID,\n" + 
			"               K.PINZB_ID,\n" + 
			"               K.YUNSFSB_ID,\n" + 
			"               K.DIANCXXB_ID,\n" + 
			"               NVL('本月', '') AS FENX\n" + 
			"          FROM YUETJKJB K, VWGONGYSDQ DQ\n" + 
			"         WHERE K.RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			"           AND DQ.ID = K.GONGYSB_ID\n" + 
			"           AND K.DIANCXXB_ID IN (" + diancxxb_id + ")\n" + 
			"        UNION\n" + 
			"        SELECT K.ID,\n" + 
			"               DQ.DQID,\n" + 
			"               DQ.DQMC,\n" + 
			"               K.GONGYSB_ID,\n" + 
			"               K.JIHKJB_ID,\n" + 
			"               K.PINZB_ID,\n" + 
			"               K.YUNSFSB_ID,\n" + 
			"               K.DIANCXXB_ID,\n" + 
			"               NVL('累计', '') AS FENX\n" + 
			"          FROM YUETJKJB K, VWGONGYSDQ DQ\n" + 
			"         WHERE K.RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			"           AND DQ.ID = K.GONGYSB_ID\n" + 
			"           AND K.DIANCXXB_ID IN (" + diancxxb_id + ")) KJ,\n" + 
			"       JIHKJB J,\n" + 
			"       MEIKDQB,\n" + 
			"       SHENGFB\n" + 
			" WHERE KJ.ID = SL.YUETJKJB_ID(+) \n";
			if(visit.getDiancxxb_id()==112){SQL+="AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.ZHUANGT=1 OR ZL.ZHUANGT=3)\n";} 
			SQL+="   AND KJ.FENX = SL.FENX(+)\n" + 
			"   AND KJ.ID = ZL.YUETJKJB_ID(+)\n" + 
			"   AND KJ.FENX = ZL.FENX(+)\n" + 
			"   AND KJ.JIHKJB_ID = J.ID\n" + 
			"   AND KJ.DQID = MEIKDQB.ID\n" + 
			"   AND MEIKDQB.SHENGFB_ID = SHENGFB.ID";
		
		} else if (this.getReportType().equals("rcrldbfxb")) {
			SQL="SELECT YUEBJZB.MINGC as JIZMC,\n" +
				"       RUL.FENX,\n" + 
				"       RUL.HAOYL,\n" + 
				"       RUL.RUCDJ,\n" + 
				"       RUL.PINGJJ AS RULDJ,\n" + 
				"       (RUL.PINGJJ-RUL.RUCDJ) AS JIAG_CE,\n" + 
				"       RUC.rez AS RC_rez,\n" + 
				"       RUL.rez AS RL_rez,\n" + 
				"       (RUL.rez-RUC.rez) AS FARL_CE,\n" + 
				"       RUL.YOUHYL,\n" + 
				"       RUL.YOUPJDJ,\n" + 
				"       RUL.YOUPJRL,\n" + 
				"       ROUND_NEW((RUL.HAOYL * RUL.rez + RUL.YOUHYL * RUL.YOUPJDJ) / 29271,0) AS BIAOML,\n" + 
				"       DECODE(NVL(RUC.rez, 0), 0, 0, ROUND_NEW(RUL.RUCDJ * 29271 / RUC.rez,2)) AS RC_BIAOMDJ,\n" + 
				"       (RUL.HAOYL * RUL.PINGJJ + RUL.YOUHYL * RUL.YOUPJDJ) AS RL_DANJ,\n" + 
				"       DECODE(NVL(RUC.REZ, 0),0,0,ROUND_NEW((RUL.PINGJJ-RUL.RUCDJ) * 29271 / RUC.REZ,2)) AS RCH_FYYX,\n" + 
				"       DECODE(NVL(RUL.REZ, 0),0,0,DECODE(NVL(RUC.REZ, 0),0,0,ROUND_NEW(((RUL.REZ-RUC.REZ) * (RUL.PINGJJ / RUC.REZ)) * 29271 / RUL.REZ,2))) RL_RC_FARLYX,\n" + 
				"       DECODE(NVL(RUL.REZ, 0), 0, 0, ROUND_NEW(RUL.PINGJJ * 29271 / RUL.REZ,2)) AS RL_YX\n" + 
				"  FROM (SELECT *\n" + 
				"          FROM YUEBMDJRLZB\n" + 
				"         WHERE RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
				"           AND DIANCXXB_ID IN (" + diancxxb_id + ")) RUL,\n" + 
				"       (SELECT FENX,\n" + 
				"               ROUND_new(DECODE(SUM(SHUL), 0, 0, SUM(REZ * SHUL) / SUM(SHUL)),0) AS rez\n" + 
				"          FROM YUEBMDJRCZB\n" + 
				"         WHERE RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
				"           AND DIANCXXB_ID IN (" + diancxxb_id + ")\n" + 
				"         GROUP BY FENX) RUC,\n" + 
				"          YUEBJZB\n" +
				" WHERE RUL.FENX = RUC.FENX\n" +
				"     AND RUL.YUEBJZB_ID = YUEBJZB.ID";
		}
		return SQL;
	}
	
//	分厂别	
	private String getDiancxxb_id() {
		return this.getTreeid();
	}
	
	/**
	 * 1、（地方矿、统配矿)供应商表取meitly(煤炭来源)字段
	 * 		不填默认为地方矿
	 * 
	 * 2、水分差调整没有取数，以0代替
	 */	
	private String getGdran01b() {
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = this.getDiancxxb_id();		
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		String Having="";
		if(getLeixValue().getId()==1){
			Having="HAVING GROUPING(GONGYSB.MINGC) + GROUPING(Z.FENX) = 1\n";
		}else{
			Having="HAVING NOT(GROUPING(GONGYSB.MINGC) + GROUPING(PINZB.MINGC) = 1 OR GROUPING(FENX) = 1)\n";
		}
		String sql=
			"SELECT " +
			"DECODE(GROUPING(Z.TJKJ),\n" +
			"              1,\n" + 
			"              '总计',\n" + 
			"              DECODE(GROUPING(JIHKJB.MINGC),\n" + 
			"                     1,\n" + 
			"                     '*' || Z.TJKJ,\n" + 
			"                     DECODE(GROUPING(z.QUANC),\n" + 
			"                            1,\n" + 
			"                            '#' || decode(JIHKJB.MINGC,'市场采购','地方矿',JIHKJB.MINGC) || '小计',\n" + 
			"                            DECODE(GROUPING(Z.DQMC),\n" + 
			"                                   1,\n" + 
			"                                   '<I>'||z.QUANC||'</I>',\n" + 
			"                                   DECODE(GROUPING(GONGYSB.MINGC),\n" + 
			"                                          1,\n" + 
			"                                          Z.DQMC || '小计',\n" + 
			"                                          GONGYSB.MINGC))))) AS KUANGB,"+
			"       DECODE(GROUPING(PINZB.MINGC), 1, '－', PINZB.MINGC) AS MEIZ,\n" + 
			"       Z.FENX,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,QICKC*0.4,QICKC)), 0) AS QICKC,\n" +
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,(JINGZ+YUNS)*0.4,(JINGZ+YUNS))), 0) AS JINGZ,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,YUNS*0.4,YUNS)), 0) AS YUNS,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,HEJ*0.4,HEJ)), 0) AS HAOYHJ,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,FADY*0.4,FADY)), 0) AS FADY,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,GONGRY*0.4,GONGRY)), 0) AS GONGRY,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,QITH*0.4,QITH)), 0) AS QITY,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,SUNH*0.4,SUNH)), 0) AS SUNH,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,DIAOCL*0.4,DIAOCL)), 0) AS DIAOCL,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,SHUIFTZ*0.4,SHUIFTZ)), 0) AS SHUIFTZ,\n" + 
			"		ROUND_NEW(SUM(decode(z.diancxxb_id,215,PANYK*0.4,PANYK)), 0) AS PANYK,\n" + 
			"		decode(z.fenx,'本月',ROUND_NEW(SUM(decode(z.diancxxb_id,215,KUC*0.4,KUC)), 0),NULL) AS KUC\n"+
			"FROM (" + getBaseSql(strDate, diancxxb_id) + ") Z,GONGYSB,JIHKJB,PINZB\n" +
			"WHERE Z.GONGYSB_ID = GONGYSB.ID\n" +
			"   AND Z.JIHKJB_ID = JIHKJB.ID\n" + 
			"   AND Z.PINZB_ID = PINZB.ID\n" + 
			"GROUP BY ROLLUP(Z.FENX,Z.TJKJ,JIHKJB.MINGC,z.QUANC,Z.DQMC,GONGYSB.MINGC,PINZB.MINGC)\n" + 
			Having+			
			"ORDER BY Z.TJKJ DESC,JIHKJB.MINGC DESC,z.QUANC DESC, GROUPING(Z.DQMC) DESC,Z.DQMC,GONGYSB.MINGC DESC,PINZB.MINGC,FENX";

		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//定义表头数据

		 String ArrHeader[][]=new String[3][15];
		 ArrHeader[0]=new String[] {"矿别","煤种","分项","期初库存","实际供应数量","实际供应数量",
				 "实际耗用数量","实际耗用数量","实际耗用数量","实际耗用数量","实际耗用数量",
				 "调入(+)或<br>调出(-)","水分差<br>调整","盘盈(+)或<br>盘亏(-)","月末结存"};
		 
		 ArrHeader[1]=new String[] {"矿别","煤种","分项","期初库存","到货数量","其中:运损",
				 "合计","发电用","供热用","其它用","储存损耗",
				 "调入(+)或<br>调出(-)","水分差<br>调整","盘盈(+)或<br>盘亏(-)","月末结存"};
		 
		 ArrHeader[2]=new String[] {"甲","乙","丙","1","2","3","4","5","6","7","8","9","10","11","12"};

		 int ArrWidth[]=new int[] {120,59,59,59,89,59,59,59,59,59,59,89,59,59,59};

	 //设置页标题		 
		 
		Table titleTable = new Table(4, 15);
		
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "填报单位:" + _Danwqc, titleTable.getCols() - 2);
		titleTable.setCellValue(3,titleTable.getCols() - 1, "国电燃01表", 2);
		//titleTable.setCellValue(3, 1, "填报日期:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols() - 2);
		titleTable.setCellValue(4, titleTable.getCols() - 1, "单位:吨、元、% ", 2);
		titleTable.setCellAlign(4, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);			
		
		rt.setTitle(titleTable);	 
		rt.setTitle("国电电力发展股份有限公司生产用燃煤供应、耗用与结存月报", 2);
				
		//设置页面
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		数据
		rt.setBody(new Table(rs,3,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
		rt.getPages();
		
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.ShowZero=reportShowZero();
		
//		添加数子序号
		convertItem(rt.body);
	
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(5, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 1, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		
		return rt.getAllPagesHtml();		
	}
	
	/**
	 * 1、（地方矿、统配矿)供应商表取meitly(煤炭来源)字段
	 * 		不填默认为地方矿
	 * 
	 * 2、检尺盈亏取 0
	 */	
	private String getGdran02b() {
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = this.getDiancxxb_id();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		String Having="";
		if(getLeixValue().getId()==1){
			Having="HAVING GROUPING(GONGYSB.MINGC) + GROUPING(Z.FENX) = 1\n";
		}else{
			Having="HAVING NOT GROUPING(FENX) = 1\n";
		}
		String sql=
			"SELECT " +
			"DECODE(GROUPING(Z.TJKJ),\n" +
			"              1,\n" + 
			"              '总计',\n" + 
			"              DECODE(GROUPING(JIHKJB.MINGC),\n" + 
			"                     1,\n" + 
			"                     '*' || Z.TJKJ,\n" + 
			"                     DECODE(GROUPING(Z.QUANC),\n" + 
			"                            1,\n" + 
			"                            '#' || DECODE(JIHKJB.MINGC,\n" + 
			"                                          '市场采购',\n" + 
			"                                          '地方矿',\n" + 
			"                                          JIHKJB.MINGC) || '小计',\n" + 
			"                            DECODE(GROUPING(Z.DQMC),\n" + 
			"                                   1,\n" + 
			"                                   '<I>' || Z.QUANC || '</I>',\n" + 
			"                                   DECODE(GROUPING(GONGYSB.MINGC),\n" + 
			"                                          1,\n" + 
			"                                          Z.DQMC || '小计',\n" + 
			"                                          GONGYSB.MINGC))))) AS KUANGB,"+
			"     --DECODE(GROUPING(PINZB.MINGC), 1, '－', PINZB.MINGC) AS MEIZ,\n" +
			"     Z.FENX,\n" + 
			"		round(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0) AS JINCML,\n" +
			"		round(SUM(DECODE(Z.DIANCXXB_ID,215,HEJ*0.4,HEJ)),0) AS HEJ,\n" + 
			"		round(SUM(DECODE(Z.DIANCXXB_ID,215,JIANJL*0.4,JIANJL)),0) AS GUOHSL,\n" + 
			"		round(SUM(DECODE(Z.DIANCXXB_ID,215,(HEJ - JIANJL)*0.4,(HEJ - JIANJL))),0) AS JIANCSL,\n" + 
			"		round(DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND_NEW(SUM(DECODE(Z.DIANCXXB_ID,215,JIANJL*0.4,JIANJL)) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)) * 100, 2)),2) AS GUOHL,\n" + 
			"		round(DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND_NEW(SUM(DECODE(Z.DIANCXXB_ID,215,JIANCL*0.4,JIANCL)) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)) * 100, 2)),2) AS JIANCL,\n" + 
			"		round(SUM(DECODE(Z.DIANCXXB_ID,215,(NVL(YINGD, 0) - NVL(KUID, 0))*0.4,(NVL(YINGD, 0) - NVL(KUID, 0)))),0) AS YINGK,\n" + 
			"		round(SUM(DECODE(Z.DIANCXXB_ID,215,(NVL(YINGD, 0) - NVL(KUID, 0))*0.4,(NVL(YINGD, 0) - NVL(KUID, 0)))),0) AS GUOHYK,\n" + 
			"		0 AS JIANCYK,\n" + 
			"		round(SUM(DECODE(Z.DIANCXXB_ID,215,(NVL(KUIDZJE, 0))*0.4,NVL(KUIDZJE, 0))),2) AS KUIDZJE,\n" + 
			"		round(SUM(DECODE(Z.DIANCXXB_ID,215,NVL(SUOPJE, 0)*0.4,NVL(SUOPJE, 0))),2) AS SUOPJE,\n" + 
			"		round(DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,KUIDZJE*0.4,KUIDZJE)),0,0,ROUND_NEW(SUM(NVL(DECODE(Z.DIANCXXB_ID,215,SUOPJE*0.4,SUOPJE), 0)) / SUM(DECODE(Z.DIANCXXB_ID,215,KUIDZJE*0.4,KUIDZJE)) * 100, 2)),2) AS SUOPL \n"+
			"FROM (" + getBaseSql(strDate, diancxxb_id) + ")Z,GONGYSB,JIHKJB,PINZB\n" +
			"WHERE Z.GONGYSB_ID = GONGYSB.ID\n" +
			"   AND Z.JIHKJB_ID = JIHKJB.ID\n" + 
			"   AND Z.PINZB_ID = PINZB.ID\n" + 
			"GROUP BY ROLLUP(Z.FENX,Z.TJKJ,JIHKJB.MINGC,z.QUANC,Z.DQMC,GONGYSB.MINGC)\n" + 
			Having+
			"ORDER BY Z.TJKJ DESC,JIHKJB.MINGC DESC,z.QUANC DESC,GROUPING(Z.DQMC) DESC,Z.DQMC,GONGYSB.MINGC DESC,FENX";
		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//定义表头数据

		 String ArrHeader[][]=new String[3][14];
		 ArrHeader[0]=new String[] {"矿别","分项","进厂煤量","验收数量","验收数量",
				 "验收数量","检斤率","检斤率","盈(+)亏(-)数量","盈(+)亏(-)数量","盈(+)亏(-)数量",
				 "亏吨数量折合金额","索赔金额","索赔率"};
		 
		 ArrHeader[1]=new String[] {"矿别","分项","进厂煤量","小计","过衡数量",
				 "检尺数量","过衡率","检尺率","小计","过衡","检尺",
				 "亏吨数量折合金额","索赔金额","索赔率"};
		 
		 ArrHeader[2]=new String[] {"甲","乙","1","2","3","4","5","6","7","8",
				 "9","10","11","12"};

		 int ArrWidth[]=new int[] {120,59,80,59,59,59,59,59,59,59,59,100,59,59};

	 //设置页标题		 
		 
		Table titleTable = new Table(4, 14);
		
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setWidth(ArrWidth);
		
		titleTable.setCellValue(4, 1, "填报单位:" + _Danwqc, titleTable.getCols() - 2);
		titleTable.setCellValue(3, titleTable.getCols() - 1, "国电燃02表", 2);
		//titleTable.setCellValue(4, 1, "填报日期:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols() - 2);
		titleTable.setCellValue(4, titleTable.getCols() - 1, "单位:吨、元、% ", 2);
		
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(4, titleTable.getCols() - 1, Table.ALIGN_RIGHT);		
		
		rt.setTitle(titleTable);	 
		rt.setTitle("国电电力发展股份有限公司进厂燃煤数量验收及索赔月报", 2);
				
		//设置页面
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		数据
		rt.setBody(new Table(rs,3,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
		rt.getPages();
		
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.ShowZero=reportShowZero();
		
//		添加数子序号
		convertItem(rt.body);
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(3, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 1, "制表:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(10, 2, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();

		return rt.getAllPagesHtml();
		
	}
	
	/**
	 * 1、（地方矿、统配矿)供应商表取meitly(煤炭来源)字段
	 * 		不填默认为地方矿
	 */
	private String getGdran03b() {
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = this.getDiancxxb_id();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		String Having="";
		if(getLeixValue().getId()==1){
			Having="HAVING GROUPING(GONGYSB.MINGC)+ GROUPING(FX) = 1\n";
		}else{
			Having="HAVING NOT GROUPING(FX) = 1\n";
		}
		String sql=
			"SELECT " +
			"DECODE(GROUPING(Z.TJKJ),\n" +
			"              1,\n" + 
			"              '总计',\n" + 
			"              DECODE(GROUPING(JIHKJB.MINGC),\n" + 
			"                     1,\n" + 
			"                     '*' || Z.TJKJ,\n" + 
			"                     DECODE(GROUPING(Z.QUANC),\n" + 
			"                            1,\n" + 
			"                            '#' || DECODE(JIHKJB.MINGC,\n" + 
			"                                          '市场采购',\n" + 
			"                                          '地方矿',\n" + 
			"                                          JIHKJB.MINGC) || '小计',\n" + 
			"                            DECODE(GROUPING(Z.DQMC),\n" + 
			"                                   1,\n" + 
			"                                   '<I>' || Z.QUANC || '</I>',\n" + 
			"                                   DECODE(GROUPING(GONGYSB.MINGC),\n" + 
			"                                          1,\n" + 
			"                                          Z.DQMC || '小计',\n" + 
			"                                          GONGYSB.MINGC))))) AS KUANGB,"+
			"     FX,\n" +
			"	  round(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0) AS JINML,\n" +
			"     round(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0) AS YANSSL,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND_NEW(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)) * 100, 2)) AS JIANZL,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * MT_KF) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS MT_KF,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * AAR_KF) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS AAR_KF,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * Vdaf_KF) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS Vdaf_KF,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * QNET_AR_KF) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS QNET_AR_KF,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * STD_KF) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS ST_D_KF,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * MT) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS Mar,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * AAD) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS AAD,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * AD) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS AD,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * Vdaf) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS Vdaf,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * QNET_AR) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS QNET_AR,\n" + 
			"     DECODE(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 0, 0, ROUND(SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML) * STD) / SUM(DECODE(Z.DIANCXXB_ID,215,JINCML*0.4,JINCML)), 2)) AS ST_D,\n" + 
			"     SUM(DECODE(Z.DIANCXXB_ID,215,NVL(ZHIJBFJE, 0)*0.4,NVL(ZHIJBFJE, 0))) AS JZ_HEJ,\n" + 
			"     SUM(DECODE(Z.DIANCXXB_ID,215,NVL(ZHIJBFJE_M, 0)*0.4,NVL(ZHIJBFJE_M, 0))) AS JZ_SHUIF,\n" + 
			"     SUM(DECODE(Z.DIANCXXB_ID,215,NVL(ZHIJBFJE_A, 0)*0.4,NVL(ZHIJBFJE_A, 0))) AS JZ_HUIF,\n" + 
			"     SUM(DECODE(Z.DIANCXXB_ID,215,NVL(ZHIJBFJE_V, 0)*0.4,NVL(ZHIJBFJE_V, 0))) AS JZ_HUIFF,\n" + 
			"     SUM(DECODE(Z.DIANCXXB_ID,215,NVL(ZHIJBFJE_Q, 0)*0.4,NVL(ZHIJBFJE_Q, 0))) AS JZ_FARL,\n" + 
			"     SUM(DECODE(Z.DIANCXXB_ID,215,NVL(ZHIJBFJE_S, 0)*0.4,NVL(ZHIJBFJE_S, 0))) AS JZ_LIUF,\n" + 
			"     SUM(DECODE(Z.DIANCXXB_ID,215,NVL(ZHIJBFJE_T, 0)*0.4,NVL(ZHIJBFJE_T, 0))) AS JZ_HUIRD,\n" + 
			"     SUM(DECODE(Z.DIANCXXB_ID,215,NVL(SUOPJE, 0)*0.4,NVL(SUOPJE, 0))) AS SUOPJE,\n" + 
			"     decode(SUM(DECODE(Z.DIANCXXB_ID,215,NVL(ZHIJBFJE, 0)*0.4,NVL(ZHIJBFJE, 0))),0,0,round(SUM(DECODE(Z.DIANCXXB_ID,215,NVL(SUOPJE, 0)*0.4,NVL(SUOPJE, 0)))/ SUM(DECODE(Z.DIANCXXB_ID,215,NVL(ZHIJBFJE, 0)*0.4,NVL(ZHIJBFJE, 0)))*100,2)) AS SUOPL\n"+
			"FROM (" + getBaseSql(strDate, diancxxb_id) + ")Z,GONGYSB,JIHKJB,PINZB\n" +
			"WHERE Z.GONGYSB_ID = GONGYSB.ID\n" +
			"   AND Z.JIHKJB_ID = JIHKJB.ID\n" + 
			"   AND Z.PINZB_ID = PINZB.ID\n" + 
			"GROUP BY ROLLUP(FX,Z.TJKJ,JIHKJB.MINGC,z.QUANC,Z.DQMC,GONGYSB.MINGC)\n" + 
			Having+
			"ORDER BY Z.TJKJ DESC,JIHKJB.MINGC DESC,z.QUANC DESC, GROUPING(Z.DQMC) DESC,Z.DQMC,GONGYSB.MINGC DESC,FX";


		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//定义表头数据

		 String ArrHeader[][]=new String[3][25];
		 ArrHeader[0]=new String[] {"矿别","分项","进厂煤量","验收数量","检质率",
				 "矿方化验","矿方化验","矿方化验","矿方化验","矿方化验","进厂质量验收情况",
				 "进厂质量验收情况","进厂质量验收情况","进厂质量验收情况","进厂质量验收情况","进厂质量验收情况",
				 "质价不符折合金额","质价不符折合金额","质价不符折合金额","质价不符折合金额","质价不符折合金额",
				 "质价不符折合金额","质价不符折合金额","索赔金额","索赔率"};
		 
		 ArrHeader[1]=new String[] {"矿别","分项","进厂煤量","验收数量","检质率",
				 "Mt<br>(%)","Aar<br>(%)","Vdaf<br>(%)","Qnet,ar<br>(MJ/Kg)","St,d<br>(%)",
				 "Mt<br>(%)","Aad<br>(%)","Ad<br>(%)","Vdaf<br>(%)","Qnet,ar<br>(%)","St,d<br>(%)",
				 "小计","水分","灰分","挥发分","热值","硫分","灰熔点","索赔金额","索赔率"};
		 
		 ArrHeader[2]=new String[] {"甲","乙","1","2","3","4","5","6","7","8",
				 "9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

		 int ArrWidth[]=new int[] {120,59,59,59,59,40,40,40,40,40,40,40,40,40,40,
				 40,59,59,59,59,59,59,59,59,59,59};

	 //设置页标题		 
		 
		Table titleTable = new Table(4, 25);
		
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "填报单位:" + _Danwqc, titleTable.getCols() - 2);
		titleTable.setCellValue(3, titleTable.getCols() - 1, "国电燃03表", 2);
		//titleTable.setCellValue(4, 1, "填报日期:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols() - 2);
		titleTable.setCellValue(4, titleTable.getCols() - 1, "单位:吨、元、% ", 2);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(4, titleTable.getCols() - 1, Table.ALIGN_RIGHT);		
		
		rt.setTitle(titleTable);	 
		rt.setTitle("国电电力发展股份有限公司进厂燃煤质量验收及索赔月报", 2);
				
		//设置页面
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		数据
		rt.setBody(new Table(rs,3,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.getPages();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		for (int i = 6; i <= 24; i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.ShowZero=reportShowZero();
		
//		添加数子序号
		convertItem(rt.body);
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(10, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(16, 2, "制表:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(20, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();

		return rt.getAllPagesHtml();
		
	}
	private String getGdran04b() {
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = this.getDiancxxb_id();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		
		String Having="";
		if(getLeixValue().getId()==1){
			Having="HAVING GROUPING(Y.FENX)=0 AND GROUPING(MK.MINGC)=1\n";
		}else{
			Having="HAVING GROUPING(Y.FENX)=0\n";
		}
		
		String sql=
			"SELECT SR.DQMC,SR.FENX,\n" +
			"       SR.RUCSL,SR.RUCMJ+SR.RUCYJ+SR.RUCZF DAOCZHJ,\n" + 
			"       SR.RUCMJ,SR.RUCYJ,SR.RUCZF,\n" + 
			"       SR.RUCMJ+SR.RUCYJ+SR.RUCZF-SR.RUCMJS-SR.RUCYJS-SR.RUCZFS DAOCBHSZJ,\n" + 
			"       SR.RUCRZ,\n" + 
			"       ROUND(decode(SR.RUCRZ,0,0,(SR.RUCMJ+SR.RUCYJ+SR.RUCZF-SR.RUCMJS-SR.RUCYJS-SR.RUCZFS)*29.271/SR.RUCRZ),2) BUHSBMDJ,\n" + 
			"       ROUND(decode(SR.RUCRZ,0,0,(SR.RUCMJ+SR.RUCYJ+SR.RUCZF)*29.271/SR.RUCRZ),2) BIAOMDJM \n"+
			"FROM(SELECT GROUPING(Y.FENX)A,GROUPING(J.MINGC)B,GROUPING(G.MINGC)C,GROUPING(MK.MINGC)D,GROUPING(PZ.MINGC)E,\n" + 
			"              DECODE(GROUPING(J.MINGC),1,'总计',DECODE(GROUPING(G.MINGC),1,'*' ||J.MINGC||'小计',DECODE(GROUPING(MK.MINGC),1,'<B>'||G.MINGC||'小计</B>',DECODE(GROUPING(PZ.MINGC),1,'<I>'||MK.MINGC||'小计</I>',PZ.MINGC))))DQMC,\n" + 
			"              Y.FENX FENX,\n" + 
			"              round(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)),0) RUCSL,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCRL * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCRZ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCMJ * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCMJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCMJS * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCMJS,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCYJ * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCYJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCYJS * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCYJS,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCZF * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCZF,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCZFS * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCZFS\n" + 
			"         FROM YUERCBMDJ_GYS Y, GONGYSB G, JIHKJB J,meikxxb mk,pinzb pz\n" + 
			"         WHERE Y.GONGYSB_ID=G.ID AND Y.MEIKXXB_ID=MK.ID AND Y.PINZB_ID=PZ.ID AND Y.JIHKJB_ID=J.ID\n" + 
			"           AND Y.DIANCXXB_ID IN ("+diancxxb_id+")\n" + 
			"           AND Y.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n";
		
			if(visit.getDiancxxb_id()==112){
				sql+="AND (Y.ZHUANGT=1 OR Y.ZHUANGT=3)\n";
			} 
			
			sql+="    GROUP BY ROLLUP(Y.FENX,J.MINGC,G.MINGC,MK.MINGC,PZ.MINGC)\n" + 
			Having+
			"ORDER BY GROUPING(J.MINGC)DESC,J.MINGC,GROUPING(G.MINGC)DESC,G.MINGC,\n" + 
			"GROUPING(MK.MINGC)DESC,MK.MINGC,GROUPING(PZ.MINGC)DESC,PZ.MINGC,Y.FENX\n" + 
			")sr";
			
		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
//		定义表头数据
		 String ArrHeader[][]=new String[2][11]; 
		 ArrHeader[0]=new String[] {"矿别","分项","入厂量(吨)","到厂综合价","煤价(含税)","运费(含税)",
				 "杂费","进厂不含<br>税总价","Qnet,ar<br>(MJ/Kg)","进厂不含<br>税标煤单价","进厂含税<br>标煤单价"};
		 ArrHeader[1]=new String[] {"甲","乙","1","2","3","4","5","6","7","8","9"};
		 int ArrWidth[]=new int[] {120,40,80,80,80,80,80,80,80,80,80};
//	 	设置页标题		 
		Table titleTable = new Table(4, 11);
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "填报单位:" + _Danwqc, titleTable.getCols() - 5);
		titleTable.setCellAlign(4, 1, Table.ALIGN_LEFT);
		titleTable.setCellValue(3, titleTable.getCols() - 1, "国电燃04表", 2);
		//titleTable.setCellValue(4, 1, "填报日期:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols()-2);
		titleTable.setCellValue(4, titleTable.getCols() - 2, "单位:吨、元", 3);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(4, titleTable.getCols() - 2, Table.ALIGN_RIGHT);		
		
		rt.setTitle(titleTable);	 
		rt.setTitle("国电电力发展股份有限公司燃料入厂标煤单价分析月报", 2);
				
		//设置页面
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		数据
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.getPages();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		for (int i = 3; i <= rt.body.getCols(); i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.ShowZero=reportShowZero();
		
//		添加数子序号
		convertItem(rt.body);
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(3, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 1, "制表:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();

		return rt.getAllPagesHtml();
		
	}
	
	private String getJiesbmdj() {
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = this.getDiancxxb_id();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		
		String Having="";
		if(getLeixValue().getId()==1){
			Having="HAVING GROUPING(Y.FENX)=0 AND GROUPING(MK.MINGC)=1\n";
		}else{
			Having="HAVING GROUPING(Y.FENX)=0\n";
		}
		

		String sql=	"SELECT SR.DQMC,SR.FENX,SR.JIESL,SR.QNET_AR,SR.MEIJ,SR.MEIJS,SR.KUANGQYF,SR.YUNJ,SR.YUNJS,SR.ZAF,SR.ZAFS,SR.ZONGHJBHS,\n" +
		"       ROUND(decode(SR.QNET_AR,0,0,(SR.MEIJ+SR.YUNJ+SR.ZAF-SR.MEIJS-SR.YUNJS-SR.ZAFS)*29.271/SR.QNET_AR),2) BUHSBMDJ,\n" + 
		"       ROUND(decode(SR.QNET_AR,0,0,(SR.MEIJ+SR.YUNJ+SR.ZAF)*29.271/SR.QNET_AR),2) BIAOMDJM\n" + 
		"     FROM (SELECT GROUPING(Y.FENX)A,GROUPING(J.MINGC)B,GROUPING(G.MINGC)C,GROUPING(MK.MINGC)D,GROUPING(PZ.MINGC)E,\n" + 
		"              DECODE(GROUPING(J.MINGC),1,'总计',DECODE(GROUPING(G.MINGC),1,'*' ||J.MINGC||'小计',DECODE(GROUPING(MK.MINGC),1,'<B>'||G.MINGC||'小计</B>',DECODE(GROUPING(PZ.MINGC),1,'<I>'||MK.MINGC||'小计</I>',PZ.MINGC))))DQMC,\n" + 
		"              Y.FENX FENX,\n" + 
		"              round(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)),0) JIESL,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)),0,0,SUM((Y.JIESMJ+ Y.JIESYJ+Y.KUANGQYF+Y.JIESZF)*DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) ZONGHJ,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM(Y.JIESMJ * DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) MEIJ,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM(Y.JIESYJ * DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) YUNJ,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM((Y.JIESZF+Y.KUANGQYF) * DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) ZAF,\n" + 
		"              round(decode(sum(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)),0,0,sum((Y.JIESMJ-Y.JIESMJS +Y.JIESYJ-Y.JIESYJS+Y.KUANGQYF+Y.JIESZF-Y.JIESZFS)*DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))/sum(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) zonghjbhs,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM(Y.JIESRL * DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) QNET_AR,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM(Y.JIESBHSBMDJ * DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) BUHSBMDJ,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM(Y.JIESBMDJ * DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) BIAOMDJ,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM(Y.JIESZFS * DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) ZAFS,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM(Y.JIESYJS * DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) YUNJS,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM(Y.JIESMJS * DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) MEIJS,\n" + 
		"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)), 0, 0, SUM(Y.KUANGQYF*DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,JIESL*0.4,JIESL))),2) KUANGQYF\n" + 
		"         FROM YUEJSDJ Y, GONGYSB G, JIHKJB J,meikxxb mk,pinzb pz\n" + 
		"         WHERE Y.GONGYSB_ID=G.ID AND Y.MEIKXXB_ID=MK.ID AND Y.PINZB_ID=PZ.ID AND Y.JIHKJB_ID=J.ID\n" + 
		"           AND Y.DIANCXXB_ID IN ("+diancxxb_id+")\n" + 
		"           AND Y.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n"; 
		if(visit.getDiancxxb_id()==112){
			sql+="AND (Y.ZHUANGT=1 OR Y.ZHUANGT=3)\n";
			} 
		sql+="    GROUP BY ROLLUP(Y.FENX,J.MINGC,G.MINGC,MK.MINGC,PZ.MINGC)\n" + 
			 Having+ 
			 "         ORDER BY GROUPING(J.MINGC)DESC,J.MINGC,GROUPING(G.MINGC)DESC,G.MINGC,GROUPING(MK.MINGC)DESC," +
			 "			MK.MINGC,GROUPING(PZ.MINGC)DESC,PZ.MINGC,Y.FENX)sr";

		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//定义表头数据

		 String ArrHeader[][]=new String[2][14]; 
		 ArrHeader[0]=new String[] {"供应商","分项","结算量<br>(吨)","结算热量<br>(MJ/Kg)","煤价(含税)<br>(元/吨)","煤价税<br>(元/吨)",
				 "矿区运费<br>(元/吨)","运价(含税)<br>(元/吨)","运价税<br>(元/吨)","杂费<br>(元/吨)","杂费税<br>(元/吨)",
				 "不含税总价<br>(元/吨)","不含税<br>标煤单价<br>(元/吨)","含税<br>标煤单价<br>(元/吨)"};
		 ArrHeader[1]=new String[] {"甲","乙","1","2","3","4","5","6","7","8","9","10","11","12"};
		 int ArrWidth[]=new int[] {120,40,80,60,70,60,60,70,60,50,55,75,70,70};
	 //设置页标题		 
		Table titleTable = new Table(4, 14);
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "填报单位:" + _Danwqc, titleTable.getCols() - 5);
		titleTable.setCellAlign(4, 1, Table.ALIGN_LEFT);
		titleTable.setCellValue(3, titleTable.getCols() - 1, "国电燃04表", 2);
		//titleTable.setCellValue(4, 1, "填报日期:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols());
		titleTable.setCellValue(4, titleTable.getCols() - 2, "单位:吨、元", 3);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(4, titleTable.getCols() - 2, Table.ALIGN_RIGHT);		
		
		rt.setTitle(titleTable);	 
		rt.setTitle("国电电力发展股份有限公司燃料结算标煤单价分析月报", 2);
				
		//设置页面
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		数据
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.getPages();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		for (int i = 3; i <= rt.body.getCols(); i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.ShowZero=reportShowZero();
		
//		添加数子序号
		convertItem(rt.body);
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(3, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 1, "制表:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();

		return rt.getAllPagesHtml();
		
	}
	private String getRucbmdj() {
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = this.getDiancxxb_id();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		
		String Having="";
		if(getLeixValue().getId()==1){
			Having="HAVING GROUPING(Y.FENX)=0 AND GROUPING(MK.MINGC)=1\n";
		}else{
			Having="HAVING GROUPING(Y.FENX)=0\n";
		}
		
		String sql=
			" SELECT SR.DQMC,SR.FENX,\n" +
			"        SR.RUCSL,SR.RUCRZ,SR.RUCMJ+SR.RUCYJ+SR.RUCZF DAOCZHJ,SR.RUCMJ,SR.RUCYJ,SR.RUCZF,\n" + 
			"        ROUND(decode(SR.RUCRZ,0,0,(SR.RUCMJ+SR.RUCYJ+SR.RUCZF-SR.RUCMJS-SR.RUCYJS-SR.RUCZFS)*29.271/SR.RUCRZ),2) BUHSBMDJ,\n" + 
			"        ROUND(decode(SR.RUCRZ,0,0,(SR.RUCMJ+SR.RUCYJ+SR.RUCZF)*29.271/SR.RUCRZ),2) BIAOMDJM,\n" + 
			"        SR.JIESRZ,SR.JIESMJ,SR.JIESYJ,SR.JIESZF, SR.GUSRZ,SR.GUSMJ,SR.GUSYJ,SR.GUSZF\n" + 
			"FROM(SELECT GROUPING(Y.FENX)A,GROUPING(J.MINGC)B,GROUPING(G.MINGC)C,GROUPING(MK.MINGC)D,GROUPING(PZ.MINGC)E,\n" + 
			"              DECODE(GROUPING(J.MINGC),1,'总计',DECODE(GROUPING(G.MINGC),1,'*' ||J.MINGC||'小计',DECODE(GROUPING(MK.MINGC),1,'<B>'||G.MINGC||'小计</B>',DECODE(GROUPING(PZ.MINGC),1,'<I>'||MK.MINGC||'小计</I>',PZ.MINGC))))DQMC,\n" + 
			"              Y.FENX FENX,\n" + 
			"              round(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)),0) RUCSL,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCRL * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCRZ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCMJ * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCMJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCMJS * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCMJS,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCYJ * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCYJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCYJS * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCYJS,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCZF * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCZF,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCZFS * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCZFS,\n" + 
			"\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL)), 0, 0, SUM(Y.JIESRL * DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL))),2) JIESRZ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL)), 0, 0, SUM(Y.JIESMJ * DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL))),2) JIESMJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL)), 0, 0, SUM(Y.JIESYJ * DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL))),2) JIESYJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL)), 0, 0, SUM(Y.JIESZF * DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.JIESL*0.4,Y.JIESL))),2) JIESZF,\n" + 
			"\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL)), 0, 0, SUM(Y.GUSRL * DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL))),2) GUSRZ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL)), 0, 0, SUM(Y.GUSMJ * DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL))),2) GUSMJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL)), 0, 0, SUM(Y.GUSYJ * DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL))),2) GUSYJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL)), 0, 0, SUM(Y.GUSZF * DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.GUSL*0.4,Y.GUSL))),2) GUSZF\n" + 
			"         FROM YUERCBMDJ_GYS Y, GONGYSB G, JIHKJB J,meikxxb mk,pinzb pz\n" + 
			"         WHERE Y.GONGYSB_ID=G.ID AND Y.MEIKXXB_ID=MK.ID AND Y.PINZB_ID=PZ.ID AND Y.JIHKJB_ID=J.ID\n" + 
			"           AND Y.DIANCXXB_ID IN ("+diancxxb_id+")\n" + 
			"           AND Y.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n";
		
			if(visit.getDiancxxb_id()==112){
				sql+="AND (Y.ZHUANGT=1 OR Y.ZHUANGT=3)\n";
			} 
			
			sql+="    GROUP BY ROLLUP(Y.FENX,J.MINGC,G.MINGC,MK.MINGC,PZ.MINGC)\n" + 
			Having+
			"ORDER BY GROUPING(J.MINGC)DESC,J.MINGC,GROUPING(G.MINGC)DESC,G.MINGC,\n" + 
			"GROUPING(MK.MINGC)DESC,MK.MINGC,GROUPING(PZ.MINGC)DESC,PZ.MINGC,Y.FENX\n" + 
			")sr";

		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//定义表头数据

		 String ArrHeader[][]=new String[3][18];
		 ArrHeader[0]=new String[] {"供应商","分项","入厂信息","入厂信息","入厂信息","入厂信息","入厂信息","入厂信息","入厂信息","入厂信息","结算信息","结算信息","结算信息","结算信息","估收信息","估收信息","估收信息","估收信息"};
		 ArrHeader[1]=new String[] {"供应商","分项","数量<br>(吨)","热值<br>(MJ/Kg)","到厂综合价<br>(元/吨)","煤价(含税)<br>(元/吨)","运价(含税)<br>(元/吨)","杂费<br>(元/吨)","标煤单价<br>(元/吨)","不含税<br>标煤单价<br>(元/吨)","热值<br>(MJ/Kg)","煤价(含税)<br>(元/吨)","运价(含税)<br>(元/吨)","杂费<br>(元/吨)","热值<br>(MJ/Kg)","煤价(含税)<br>(元/吨)","运价(含税)<br>(元/吨)","杂费<br>(元/吨)"};
		 ArrHeader[2]=new String[] {"甲","乙","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
		 int ArrWidth[]=new int[] {120,40,80,60,80,80,80,60,70,70,60,80,80,60,60,80,80,60};
	 //设置页标题		 
		Table titleTable = new Table(4, 18);
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "填报单位:" + _Danwqc, titleTable.getCols() - 3);
		
		titleTable.setCellValue(3, titleTable.getCols() - 1, "结算估价表", 2);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		
		//titleTable.setCellValue(4, 1, "填报日期:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols() - 2);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		
		titleTable.setCellValue(4, titleTable.getCols() - 2, "单位:吨、元", 3);
		titleTable.setCellAlign(4, titleTable.getCols() - 2, Table.ALIGN_RIGHT);	
		
			
		
		rt.setTitle(titleTable);	 
		rt.setTitle("国电电力发展股份有限公司燃料进厂标煤单价分析月报", 2);
				
		//设置页面
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		数据
		rt.setBody(new Table(rs,3,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.getPages();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		for (int i = 4; i <= rt.body.getCols(); i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.ShowZero=reportShowZero();
		
//		添加数子序号
		convertItem(rt.body);
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(3, 2, "分管领导:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 1, "制表:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();

		return rt.getAllPagesHtml();
		
	}
	
	private void convertItem(Table tb) {
		String tbCell = "";
		String compareCell = "default"; 
		int t = -1;
		int k = 0;
		int j = 0;
		
		for (int i = 1; i< tb.getRows()-1; i++) {
			tbCell = tb.getCellValue(i, 1);
			t = tbCell.indexOf(ITEM_ONE);
			if (t > -1) {
				//防止连续合并的相同数据累加序号k
				if (!compareCell.equals(tbCell)) k++;
				compareCell = tbCell;
				tb.setCellValue(i, 1, getDxValue(k) + "、" + tbCell.substring(t + 1));
				if (k > 1) j = 0;  //当碰见下一个计划口径时，j从零开始
			}
			t = tbCell.indexOf(ITEM_TWO);
			if (t > -1) {
				if (!compareCell.equals(tbCell)) j++;
				compareCell = tbCell;
				tb.setCellValue(i, 1, j + "、" + tbCell.substring(t + 1));
			}
		}
	}
	
	public String getDxValue(int xuh) {
		String reXuh = "";
		String[] dx = { "", "一", "二", "二", "三", "四", 
				"五", "六", "七", "八", "九", "十" };
		String strXuh = String.valueOf(xuh);
		for (int i = 0; i < strXuh.length(); i++)
			reXuh = reXuh + dx[Integer.parseInt(strXuh.substring(i, i + 1))];

		return reXuh;
	}
	
	
	
	private String getRanyhcgy(){

		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = this.getDiancxxb_id();		
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		String tiaoj="";
		if(((Visit) getPage().getVisit()).getDiancxxb_id()==112){tiaoj="AND (Y.ZHUANGT=1 or Y.ZHUANGT=3)\n";}
		String sql=
			"SELECT DECODE(GROUPING(DC.MINGC), 1, '总计', DC.MINGC) DCMC,\n" +
			" DECODE(GROUPING(DC.MINGC) + GROUPING(P.MINGC),2,'-', 1, '小计', P.MINGC) PZMC,\n" + 
			" Y.FENX,\n" + 
			" ROUND_NEW(SUM(DECODE(Y.DIANCXXB_ID,215,Y.QICKC*0.4,Y.QICKC)), 2) QICKC,\n" + 
			" ROUND_NEW(SUM(DECODE(Y.DIANCXXB_ID,215,Y.SHOUYL*0.4,Y.SHOUYL)), 0) SHOUYL,\n" + 
			" ROUND_NEW(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.SHOUYL*0.4,Y.SHOUYL)),0,0,SUM(DECODE(Y.DIANCXXB_ID,215,Y.SHOUYL*0.4,Y.SHOUYL) * Y.YOUJ) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.SHOUYL*0.4,Y.SHOUYL))),2) YOUJ,\n" + 
			" ROUND_NEW(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.SHOUYL*0.4,Y.SHOUYL)),0,0,SUM(DECODE(Y.DIANCXXB_ID,215,Y.SHOUYL*0.4,Y.SHOUYL) * Y.YUNJ) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.SHOUYL*0.4,Y.SHOUYL))),2) YUNJ,\n" + 
			" ROUND_NEW(SUM(DECODE(Y.DIANCXXB_ID,215,(Y.FADYY + Y.GONGRY + Y.QITHY)*0.4,(Y.FADYY + Y.GONGRY + Y.QITHY))), 2) XJ,\n" + 
			" ROUND_NEW(SUM(DECODE(Y.DIANCXXB_ID,215,Y.FADYY*0.4,Y.FADYY)), 2) FADYY,\n" + 
			" ROUND_NEW(SUM(DECODE(Y.DIANCXXB_ID,215,Y.GONGRY*0.4,Y.GONGRY)), 2) GONGRY,\n" + 
			" ROUND_NEW(SUM(DECODE(Y.DIANCXXB_ID,215,Y.QITHY*0.4,Y.QITHY)), 2) QITHY,\n" + 
			" ROUND_NEW(SUM(DECODE(Y.DIANCXXB_ID,215,Y.KUC*0.4,Y.KUC)), 2) KUC\n" + 
			"  FROM YUESHCYB Y, PINZB P, DIANCXXB DC\n" + 
			" WHERE Y.PINZB_ID = P.ID \n" + 
			tiaoj+
			"   AND Y.RIQ = TO_DATE('" +strDate+"', 'yyyy-MM-dd')\n" + 
			"   AND Y.DIANCXXB_ID = DC.ID\n" + 
			"   AND Y.DIANCXXB_ID IN ("+diancxxb_id+")\n" + 
			" GROUP BY ROLLUP(Y.FENX, DC.MINGC, P.MINGC)\n" + 
			"--GROUP BY GROUPING SETS((dc.mingc,P.MINGC,Y.FENX),(dc.mingc,Y.FENX))\n" + 
			"HAVING NOT GROUPING(Y.FENX) = 1\n" + 
			"ORDER BY GROUPING(DC.MINGC) DESC,DC.MINGC,GROUPING(P.MINGC) DESC,P.MINGC,FENX";
			
		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//定义表头数据

		 String ArrHeader[][]=new String[4][17];
		 ArrHeader[0]=new String[] {"单位","品种","分项","期初库存","实际供应情况","实际供应情况","实际供应情况","实际耗用量","实际耗用量","实际耗用量","实际耗用量",
				 "月末库存"};
		 
		 ArrHeader[1]=new String[] {"单位","品种","分项","期初库存","数量","价格构成","价格构成","小计","其中","其中","其中",
		 "月末库存"};
		 
		 ArrHeader[2]=new String[] {"单位","品种","分项","期初库存","数量","含税油价","含税运价","小计","发电","供热","其它",
		 "月末库存"};
		 
		 ArrHeader[3]=new String[] {"甲","乙","丙","1","2","3","4","5","6","7","8","9"};

		 int ArrWidth[]=new int[] {120,80,50,80,80,80,80,80,80,80,80,80};

	 //设置页标题		 
		 
		Table titleTable = new Table(4, 9);
		
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(3, 1, "填报单位:" + _Danwqc, titleTable.getCols() - 2);
		titleTable.setCellValue(3, titleTable.getCols() - 1, "国电燃06表", 2);
		titleTable.setCellValue(4, 1, "填报日期:", 1);
		titleTable.setCellValue(4, 2, strMonth, titleTable.getCols() - 3);
		titleTable.setCellValue(4, titleTable.getCols() - 1, "单位:吨、元、% ", 2);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(4, 2 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(4, titleTable.getCols() - 1, Table.ALIGN_RIGHT);			
		
		rt.setTitle(titleTable);	 
		rt.setTitle("国电电力发展股份有限公司生产用燃油供应、耗用与结存月报", 2);
				
		//设置页面
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		数据
		rt.setBody(new Table(rs,4,0,3));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
		rt.getPages();
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.ShowZero=reportShowZero();
	
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(3, 2,"分管领导:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 1, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 1, "制表:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		
		return rt.getAllPagesHtml();		
	
	}
	
	private String getRanlcb(){
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		 
		//定义表头数据

		 String ArrHeader[][]=new String[1][8];
		 ArrHeader[0]=new String[] {"项目分类","栏号","项目名称","计量单位","本月数值","年累计值","备注"};
		 int ArrWidth[]=new int[] {160,80,160,80,100,100,100};
		
		Report rt=new Report();
		rt.setTitle("国电电力发展股份有限公司发电、供热耗用燃料成本月报(不含税)", ArrWidth);	
			
	    rt.setDefaultTitle(1, 2, "单位:"+_Danwqc, Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(6, 2, "国电燃07表", Table.ALIGN_RIGHT);
		
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		数据
		
//		发电供热燃料费
		StringBuffer sql=new StringBuffer();
		
		String fenl="发电供热燃料费";
		String danw1="元";
		String danw2="元";
		String danw3="元";
		String danw4="元";
		int count=0;
		String xiangm1="发电供热总燃料费";
		String xiangm2="其中：燃煤";
		String xiangm3="燃油";
		String xiangm4="燃气";		
		String zid1="sum(DECODE(Y.DIANCXXB_ID,215,(RANLCB_BHS)*0.4,RANLCB_BHS))";
//		String zid1="y.FADMCB+ FADYCB+ FADRQCB+ GONGRMCB+ GONGRYCB+ GONGRRQCB";
		String zid2="sum(DECODE(Y.DIANCXXB_ID,215,(y.FADMCB+y.GONGRMCB+ Y.QIZ_RANM)*0.4,(y.FADMCB+y.GONGRMCB+ Y.QIZ_RANM)))";
		String zid3="sum(DECODE(Y.DIANCXXB_ID,215,(y.FADYCB+y.GONGRYCB+Y.QIZ_RANY)*0.4,(y.FADYCB+y.GONGRYCB+Y.QIZ_RANY)))";
		String zid4="sum(DECODE(Y.DIANCXXB_ID,215,(y.FADRQCB+y.GONGRRQCB+ Y.QIZ_RANQ)*0.4,(y.FADRQCB+y.GONGRRQCB+ Y.QIZ_RANQ)))";
		
		sql=this.getZbbStr(cn, fenl, count, danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		ResultSetList rs=cn.getResultSetList(sql.toString());
		
		rt.setBody(new Table(rs,1,0,3));
		
		
//		发电燃料费
		
		fenl="发电燃料费";
		danw1="元";
		danw2="元";
		danw3="元";
		danw4="元";
		rs.beforefirst();
		count+=rs.getRows();
		xiangm1="发电燃料费";
		xiangm2="其中：燃煤";
		xiangm3="燃油";
		xiangm4="燃气";
		zid1="sum(DECODE(Y.DIANCXXB_ID,215,(y.FADMCB+y.FADYCB+y.FADRQCB)*0.4,y.FADMCB+y.FADYCB+y.FADRQCB))";
		zid2="sum(DECODE(Y.DIANCXXB_ID,215,y.FADMCB*0.4,y.FADMCB))";
		zid3="sum(DECODE(Y.DIANCXXB_ID,215,y.FADYCB*0.4,y.FADYCB))";
		zid4="sum(DECODE(Y.DIANCXXB_ID,215,y.FADRQCB*0.4,y.FADRQCB))";

		sql=this.getZbbStr(cn, fenl, count,danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);
		
		
		
//		供热用燃料费
		
		fenl="供热用燃料费";
		danw1="元";
		danw2="元";
		danw3="元";
		danw4="元";
		rs.beforefirst();
		count+=rs.getRows();
		xiangm1="供热用燃料费";
		xiangm2="其中：燃煤";
		xiangm3="燃油";
		xiangm4="燃气";
		zid1="sum(DECODE(Y.DIANCXXB_ID,215,(y.GONGRMCB+y.GONGRYCB+y.GONGRRQCB)*0.4,(y.GONGRMCB+y.GONGRYCB+y.GONGRRQCB)))";
		zid2="sum(DECODE(Y.DIANCXXB_ID,215,y.GONGRMCB*0.4,y.GONGRMCB))";
		zid3="sum(DECODE(Y.DIANCXXB_ID,215,y.GONGRYCB*0.4,y.GONGRYCB))";
		zid4="sum(DECODE(Y.DIANCXXB_ID,215,y.GONGRRQCB*0.4,y.GONGRRQCB))";

		sql=this.getZbbStr(cn, fenl, count, danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);
		
		
//		供热用电分摊燃料费
		
		fenl="供热用电分摊燃料费";
		danw1="元";
		danw2="元";
		danw3="元";
		danw4="元";
		rs.beforefirst();
		count+=rs.getRows();
		xiangm1="供热用电分摊燃料费";
		xiangm2="其中：燃煤";
		xiangm3="燃油";
		xiangm4="燃气";
		zid1="sum(DECODE(Y.DIANCXXB_ID,215,(y.GONGRCYDFTRLF)*0.4,(y.GONGRCYDFTRLF)))";
		zid2="sum(DECODE(Y.DIANCXXB_ID,215,(y.QIZ_RANM)*0.4,y.QIZ_RANM))";
		zid3="sum(DECODE(Y.DIANCXXB_ID,215,(y.QIZ_RANY)*0.4,y.QIZ_RANY))";
		zid4="sum(DECODE(Y.DIANCXXB_ID,215,(y.QIZ_RANQ)*0.4,y.QIZ_RANQ))";

		sql=this.getZbbStr(cn, fenl, count, danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);
//		发电供热总标煤量
		
		fenl="发电供热总标煤量";
		danw1="吨";
		danw2="吨";
		danw3="吨";
		danw4="吨";
		rs.beforefirst();
		count+=rs.getRows();
		xiangm1="发电供热总标煤量";
		xiangm2="其中：煤折标煤量";
		xiangm3="油折标煤量";
		xiangm4="气折标煤量";
		zid1="sum(DECODE(Y.DIANCXXB_ID,215,(y.FADMZBML+y.FADYZBZML+y.FADQZBZML+y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML)*0.4,(y.FADMZBML+y.FADYZBZML+y.FADQZBZML+y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML)))";
		zid2="sum(DECODE(Y.DIANCXXB_ID,215,(y.FADMZBML+y.GONGRMZBML)*0.4,(y.FADMZBML+y.GONGRMZBML)))";
		zid3="sum(DECODE(Y.DIANCXXB_ID,215,(y.FADYZBZML+y.GONGRYZBZML)*0.4,(y.FADYZBZML+y.GONGRYZBZML)))";
		zid4="sum(DECODE(Y.DIANCXXB_ID,215,(y.FADQZBZML+y.GONGRQZBZML)*0.4,(y.FADQZBZML+y.GONGRQZBZML)))";

		sql=this.getZbbStr(cn, fenl, count,danw1,danw2,danw3,danw4,xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);	
		
//		发电用标煤量
		
		fenl="发电用标煤量";
		danw1="吨";
		danw2="吨";
		danw3="吨";
		danw4="吨";
		rs.beforefirst();
		count+=rs.getRows();
		xiangm1="发电用标煤量";
		xiangm2="其中：煤折标煤量";
		xiangm3="油折标煤量";
		xiangm4="气折标煤量";
		zid1="sum(DECODE(Y.DIANCXXB_ID,215,(y.FADMZBML+y.FADYZBZML+y.FADQZBZML)*0.4,(y.FADMZBML+y.FADYZBZML+y.FADQZBZML)))";
		zid2="sum(DECODE(Y.DIANCXXB_ID,215,y.FADMZBML*0.4,y.FADMZBML))";
		zid3="sum(DECODE(Y.DIANCXXB_ID,215,y.FADYZBZML*0.4,y.FADYZBZML))";
		zid4="sum(DECODE(Y.DIANCXXB_ID,215,y.FADQZBZML*0.4,y.FADQZBZML))";

		sql=this.getZbbStr(cn, fenl, count, danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);
//		供热用标煤量
		
		fenl="供热用标煤量";
		danw1="吨";
		danw2="吨";
		danw3="吨";
		danw4="吨";
		rs.beforefirst();
		count+=rs.getRows();
		xiangm1="供热用标煤量";
		xiangm2="其中：煤折标煤量";
		xiangm3="油折标煤量";
		xiangm4="气折标煤量";
		zid1="sum(DECODE(Y.DIANCXXB_ID,215,(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML)*0.4,(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML)))";
		zid2="sum(DECODE(Y.DIANCXXB_ID,215,y.GONGRMZBML*0.4,y.GONGRMZBML))";
		zid3="sum(DECODE(Y.DIANCXXB_ID,215,y.GONGRYZBZML*0.4,y.GONGRYZBZML))";
		zid4="sum(DECODE(Y.DIANCXXB_ID,215,y.GONGRQZBZML*0.4,y.GONGRQZBZML))";

		sql=this.getZbbStr(cn, fenl, count, danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);	
//		入炉综合标煤单价
		
		fenl="入炉综合标煤单价";
		danw1="元/吨";
		danw2="元/吨";
		danw3="元/吨";
		danw4="元/吨";
		rs.beforefirst();
		count+=rs.getRows();
		
		xiangm1="入炉综合标煤单价";
		xiangm2="其中：煤折标煤单价";
		xiangm3="油折标煤单价";
		xiangm4="气折标煤单价"; 
		zid1="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(Rulmzbzml+RULYZBZML+RULQZBZML,0)*0.4,nvl(Rulmzbzml+RULYZBZML+RULQZBZML,0))),0,0," +
				"round_new(sum(Y.RULZHBMDJ*DECODE(Y.DIANCXXB_ID,215,nvl(Rulmzbzml+RULYZBZML+RULQZBZML,0)*0.4,nvl(Rulmzbzml+RULYZBZML+RULQZBZML,0)))" +
				"/sum(DECODE(Y.DIANCXXB_ID,215,nvl(Rulmzbzml+RULYZBZML+RULQZBZML,0)*0.4,nvl(Rulmzbzml+RULYZBZML+RULQZBZML,0))),2) )";

		zid2="decode( sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADMZBML+y.GONGRMZBML,0)*0.4,nvl(y.FADMZBML+y.GONGRMZBML,0))),0,0, " +
				"round_new(sum(DECODE(Y.DIANCXXB_ID,215,(Y.FADMCB + Y.GONGRMCB + Y.QIZ_RANM)*0.4,(Y.FADMCB + Y.GONGRMCB + Y.QIZ_RANM)))" +
				"/sum(DECODE(Y.DIANCXXB_ID,215,(y.FADMZBML+y.GONGRMZBML)*0.4,y.FADMZBML+y.GONGRMZBML)),2) )";
		
		zid3="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADYZBZML+y.GONGRYZBZML,0)*0.4,nvl(y.FADYZBZML+y.GONGRYZBZML,0))),0,0, " +
				"round_new(sum(DECODE(Y.DIANCXXB_ID,215,(Y.FADYCB + Y.GONGRYCB +Y.QIZ_RANY)*0.4,Y.FADYCB + Y.GONGRYCB +Y.QIZ_RANY))" +
				"/sum(DECODE(Y.DIANCXXB_ID,215,(y.FADYZBZML+y.GONGRYZBZML)*0.4,y.FADYZBZML+y.GONGRYZBZML)),2) )";
		zid4="decode( sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADQZBZML+y.GONGRQZBZML,0)*0.4,nvl(y.FADQZBZML+y.GONGRQZBZML,0))),0,0, " +
				"round_new(sum(DECODE(Y.DIANCXXB_ID,215,(Y.FADRQCB + Y.GONGRRQCB + Y.QIZ_RANQ)*0.4,(Y.FADRQCB + Y.GONGRRQCB + Y.QIZ_RANQ)))/" +
				"sum(DECODE(Y.DIANCXXB_ID,215,(y.FADQZBZML+y.GONGRQZBZML)*0.4,(y.FADQZBZML+y.GONGRQZBZML))),2) )";

		sql=this.getZbbStr(cn, fenl, count, danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);	
//		发电标煤单价
		
		fenl="发电标煤单价";
		danw1="元/吨";
		danw2="元/吨";
		danw3="元/吨";
		danw4="元/吨";
		rs.beforefirst();
		count+=rs.getRows();
		xiangm1="发电标煤单价";
		xiangm2="其中：煤折标煤单价";
		xiangm3="油折标煤单价";
		xiangm4="气折标煤单价";
		zid1="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADMZBML+y.FADYZBZML+y.FADQZBZML,0)*0.4,nvl(y.FADMZBML+y.FADYZBZML+y.FADQZBZML,0))),0,0," +
				"round_new(sum(FADBZMDJ*DECODE(Y.DIANCXXB_ID,215,nvl(y.FADMZBML+y.FADYZBZML+y.FADQZBZML,0)*0.4,nvl(y.FADMZBML+y.FADYZBZML+y.FADQZBZML,0)))/" +
				"sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADMZBML+y.FADYZBZML+y.FADQZBZML,0)*0.4,nvl(y.FADMZBML+y.FADYZBZML+y.FADQZBZML,0))),2))";
		zid2="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADMZBML,0)*0.4,nvl(y.FADMZBML,0))),0,0," +
				"round_new(sum(QIZ_MEIZBMDJ*DECODE(Y.DIANCXXB_ID,215,nvl(y.FADMZBML,0)*0.4,nvl(y.FADMZBML,0)))/sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADMZBML,0)*0.4,nvl(y.FADMZBML,0))),2))";
		zid3="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADYZBZML,0)*0.4,nvl(y.FADYZBZML,0))),0,0," +
				"round_new(sum(QIZ_YOUZBMDJ*DECODE(Y.DIANCXXB_ID,215,nvl(y.FADYZBZML,0)*0.4,nvl(y.FADYZBZML,0)))/sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADYZBZML,0)*0.4,nvl(y.FADYZBZML,0))),2))";
		zid4="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADQZBZML,0)*0.4,nvl(y.FADQZBZML,0))),0,0," +
				"round_new(sum(QIZ_QIZBMDJ*DECODE(Y.DIANCXXB_ID,215,nvl(y.FADQZBZML,0)*0.4,nvl(y.FADQZBZML,0)))/sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.FADQZBZML,0)*0.4,nvl(y.FADQZBZML,0))),2))";

		sql=this.getZbbStr(cn, fenl, count, danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);	
//		供热标煤单价
		
		fenl="供热标煤单价";
		danw1="元/吨";
		danw2="元/吨";
		danw3="元/吨";
		danw4="元/吨";
		rs.beforefirst();
		count+=rs.getRows();
		xiangm1="供热标煤单价";
		xiangm2="其中：煤折标煤单价";
		xiangm3="油折标煤单价";
		xiangm4="气折标煤单价";
		zid1="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML,0)*0.4,nvl(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML,0))),0,0," +
				"round_new(sum(GONGRBZMDJ*DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML,0)*0.4,nvl(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML,0)))" +
				"/sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML,0)*0.4,nvl(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML,0))),2))";
		zid2="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRMZBML,0)*0.4,nvl(y.GONGRMZBML,0))),0,0," +
				"round_new(sum(QIZ_MEIZBMDJ_GR*DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRMZBML,0)*0.4,nvl(y.GONGRMZBML,0)))/sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRMZBML,0)*0.4,nvl(y.GONGRMZBML,0))),2))";
		zid3="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRYZBZML,0)*0.4,nvl(y.GONGRYZBZML,0))),0,0," +
				"round_new(sum(QIZ_YOUZBMDJ_GR*DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRYZBZML,0)*0.4,nvl(y.GONGRYZBZML,0)) )/sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRYZBZML,0)*0.4,nvl(y.GONGRYZBZML,0))),2))";
		zid4="decode(sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRQZBZML,0)*0.4,nvl(y.GONGRQZBZML,0))),0,0," +
				"round_new(sum(QIZ_QIZBMDJ_GR*DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRQZBZML,0)*0.4,nvl(y.GONGRQZBZML,0)))/sum(DECODE(Y.DIANCXXB_ID,215,nvl(y.GONGRQZBZML,0)*0.4,nvl(y.GONGRQZBZML,0))),2))";
		
		sql=this.getZbbStr(cn, fenl, count, danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);
		
//		生产指标
		fenl="生产指标";
		danw1="万千瓦时";
		danw2="克/千瓦时";
		danw3="吉焦";
		danw4="千克/吉焦";
		rs.beforefirst();
		count+=rs.getRows();
		xiangm1="发电量";
		xiangm2="发电煤耗";
		xiangm3="供热量";
		xiangm4="供热煤耗";
		zid1="sum(DECODE(Y.DIANCXXB_ID,215,y.FADL*0.4,y.FADL))";
		zid2="Round(DECODE (sum(DECODE(Y.DIANCXXB_ID,215,y.FADL*0.4,y.FADL)) ,0,0,sum(DECODE(Y.DIANCXXB_ID,215,(y.FADYZBZML+y.FADMZBML)*0.4,y.FADYZBZML+y.FADMZBML))*100/ sum(DECODE(Y.DIANCXXB_ID,215,y.FADL*0.4,y.FADL))),2) ";
		zid3="sum(DECODE(Y.DIANCXXB_ID,215,GONGRL*0.4,GONGRL))";
		zid4="Round(DECODE(sum(DECODE(Y.DIANCXXB_ID,215,y.GONGRL*0.4,y.GONGRL)),0,0, sum(DECODE(Y.DIANCXXB_ID,215,(y.GONGRMZBML+ y.GONGRYZBZML)*0.4,(y.GONGRMZBML+ y.GONGRYZBZML)))*1000/ sum(DECODE(Y.DIANCXXB_ID,215,y.GONGRL*0.4,y.GONGRL))),2) ";

		sql=this.getZbbStr(cn, fenl, count, danw1,danw2,danw3,danw4, xiangm1, xiangm2, xiangm3, xiangm4, zid1, zid2, zid3, zid4);
		rs=cn.getResultSetList(sql.toString());
		rt.body.AddTableData(rs);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(-1);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
		rt.getPages();
		
		rt.body.mergeCol(1);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.ShowZero=reportShowZero();
	
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2,"分管领导:",Table.ALIGN_RIGHT);
		rt.setDefautlFooter(3, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(5, 1, "制表:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();		
		return rt.getAllPagesHtml();		
	
	
	}
	
	
	private StringBuffer getZbbStr(JDBCcon cn,String fenl,int count,String danw1,String danw2,String danw3,String danw4,String xiangm1,String xiangm2,String xiangm3,String xiangm4,String zid1,String zid2,String zid3,String zid4){
		
		
		String diancxxb_id = this.getDiancxxb_id();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		Visit visit = (Visit) getPage().getVisit();
		String tiaoj="";
		if(visit.getDiancxxb_id()==112){tiaoj="AND (Y.ZHUANGT=1 or Y.ZHUANGT=3)";}
		StringBuffer sql=new StringBuffer();
		
		sql.append(" select * from (");
	    sql.append(" select nvl('"+fenl+"','') fenl,\n");
	    sql.append(" nvl("+(++count)+",1) lanh,");
	    sql.append(" nvl('"+xiangm1+"','') mingc,\n");
	    sql.append(" nvl('"+danw1+"','') danw,\n");
	    sql.append(" ( select "+zid1+"   from yuezbb y,diancxxb d where y.diancxxb_id=d.id and  y.diancxxb_id in ("+diancxxb_id+") \n");
	    sql.append("  and y.riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='本月' "+tiaoj+" ) benyz,");
	    sql.append(" ( select "+zid1+"   from yuezbb y,diancxxb d where y.diancxxb_id=d.id and  y.diancxxb_id in ("+diancxxb_id+") \n");
	    sql.append("  and y.riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='累计' "+tiaoj+" ) leijz,");
	    sql.append(" '' beiz ");
	    sql.append(" from dual ");	    
	    sql.append(" union \n");	    
	    sql.append(" select nvl('"+fenl+"','') fenl,\n");
	    sql.append(" nvl("+(++count)+",1) lanh,");
	    sql.append(" nvl('"+xiangm2+"','') mingc,\n");
	    sql.append(" nvl('"+danw2+"','') danw,\n");
	    sql.append(" ( select "+zid2+"   from yuezbb y,diancxxb d where y.diancxxb_id=d.id and  y.diancxxb_id in ("+diancxxb_id+") \n");
	    sql.append("  and y.riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='本月' "+tiaoj+" ) benyz,");
	    sql.append(" ( select "+zid2+"   from yuezbb y,diancxxb d where y.diancxxb_id=d.id and  y.diancxxb_id in ("+diancxxb_id+") \n");
	    sql.append("  and y.riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='累计' "+tiaoj+" ) leijz,");
	    sql.append(" '' beiz ");
	    sql.append(" from dual ");	    
	    sql.append(" union \n");	    
	    sql.append(" select nvl('"+fenl+"','') fenl,\n");
	    sql.append(" nvl("+(++count)+",1) lanh,");
	    sql.append(" nvl('"+xiangm3+"','') mingc,\n");
	    sql.append(" nvl('"+danw3+"','') danw,\n");
	    sql.append(" ( select "+zid3+"   from yuezbb y,diancxxb d where y.diancxxb_id=d.id and  y.diancxxb_id in ("+diancxxb_id+") \n");
	    sql.append("  and y.riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='本月' "+tiaoj+" ) benyz,");
	    sql.append(" ( select "+zid3+"   from yuezbb y,diancxxb d where y.diancxxb_id=d.id and  y.diancxxb_id in ("+diancxxb_id+") \n");
	    sql.append("  and y.riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='累计' "+tiaoj+" ) leijz,");
	    sql.append(" '' beiz ");
	    sql.append(" from dual ");	    
	    sql.append(" union \n");	    
	    sql.append(" select nvl('"+fenl+"','') fenl,\n");
	    sql.append(" nvl("+(++count)+",1) lanh,");
	    sql.append(" nvl('"+xiangm4+"','') mingc,\n");
	    sql.append(" nvl('"+danw4+"','') danw,\n");
	    sql.append(" ( select "+zid4+"   from yuezbb y,diancxxb d where y.diancxxb_id=d.id and  y.diancxxb_id in ("+diancxxb_id+") \n");
	    sql.append("  and y.riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='本月' "+tiaoj+" ) benyz,");
	    sql.append(" ( select "+zid4+"   from yuezbb y,diancxxb d where y.diancxxb_id=d.id and  y.diancxxb_id in ("+diancxxb_id+") \n");
	    sql.append("  and y.riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='累计' "+tiaoj+" ) leijz,");
	    sql.append(" '' beiz ");
	    sql.append(" from dual ");	    
	    sql.append(" ) order by lanh asc");
		
		return sql;
		
	}

}