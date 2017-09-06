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
 * ���ߣ����
 * ʱ�䣺2012-05-15
 * ����������GdMonthReport_Fc 1.1.2.18�����˱���
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-07-04
 * ����: ����01���������ȡֵ��ʽ
 */
public class GdMonthReport_FcNew extends BasePage {
	
	private static final String GD_DR01 = "diaor01b";
	
	private static final String GD_DR02 = "diaor02b";
	
	private static final String GD_DR03 = "diaor03b";
	
	private static final String GD_DR04 = "diaor04b";
	
	private static final String GD_RUCBMDJ = "rucbmdj";//�볧��ú����
	
	private static final String GD_JIESBMDJ = "jiesbmdj";//�����ú����
	
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
	
	// �����ʼ����
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
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='ֽ������' and diancxxb_id in ( "
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
	//���ӵ糧��ѡ���ļ���
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
	
//	���������
    public IDropDownBean getNianfValue() {
//    	������ֵΪ����ô��ʼ�����
        if (((Visit)this.getPage().getVisit()).getDropDownBean3() == null) {
//			ȡ�õ�ǰ���ֵ
        	int _nianf = DateUtil.getYear(new Date());
//			ȡ�õ�ǰ�·�ֵ
        	int _yuef = DateUtil.getMonth(new Date());
//        	����·�Ϊ1,��ô���ֵӦΪ��һ��
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

//	�·�������
	public IDropDownBean getYuefValue() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean4() == null) {
//	    	�õ��·�ֵ
	    	int _yuef = DateUtil.getMonth(new Date());
//			����·�Ϊ1����ô�·ݵ���12�������·ݵ����ϸ���
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
    
	public String getTianzdwQuanc(){
		String[] str=getTreeid().split(",");
		if(str.length>1){
			return "��ϵ糧";
		}else{
			return getTianzdwQuanc(Long.parseLong(str[0]));
		}
	}
	
	public long getDiancxxbId(){	
		return ((Visit)getPage().getVisit()).getDiancxxb_id();
	}
	
	//�õ���λȫ��
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
	
//	�ֳ���
	public boolean isFencb() {
		return ((Visit) this.getPage().getVisit()).isFencb();
	}
	
	// ҳ���ʼ����
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
		paperStyle();
		getSelectData();
	}
//	��ʼ����ѡ�糧���е�Ĭ��ֵ
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
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("�·�:"));
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
			tf.setValue("��ϵ糧");
		}else{
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}
		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);	
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
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

	// ��������
	public String getPrintTable() {	
		if (getReportType().equals(GD_DR01)) {
			return getGdran01b();
		} else if (getReportType().equals(GD_DR02)) {
			return getGdran02b();
		} else if (getReportType().equals(GD_DR03)) {
			return getGdran03b();			
		} else if (getReportType().equals(GD_DR04)) {
			return getGdran04b();			
		} else if(getReportType().equals(GD_RUCBMDJ)){
			return getRucbmdj();
		}else if(getReportType().equals(GD_JIESBMDJ)){ 
			return getJiesbmdj();
		}else{
			return "�޴˱���";
		}		
	}
	
	private String getBaseSql(String strDate, String diancxxb_id) {
		Visit visit = (Visit) getPage().getVisit();
		String SQL="";
		if (this.getReportType().equals(GD_DR01)) {
			//return 
			SQL="SELECT KJ.GONGYSB_ID,\n" +
			"       KJ.DQID,\n" + 
			"       KJ.DQMC,\n" + 
			"       SHENGFB.QUANC QUANC,\n" + 
			"       DECODE(KJ.JIHKJB_ID, 1, '�ص㶩��', 3, '�ص㶩��', J.MINGC) TJKJ,\n" + 
			"       KJ.JIHKJB_ID,\n" + 
			"       PZ.MINGC PINZB_ID,\n" + 
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
			"               NVL('����', '') AS FENX\n" + 
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
			"               NVL('�ۼ�', '') AS FENX\n" + 
			"          FROM YUETJKJB K, VWGONGYSDQ DQ\n" + 
			"         WHERE K.RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			"           AND DQ.ID = K.GONGYSB_ID\n" + 
			"           AND K.DIANCXXB_ID IN (" + diancxxb_id + ")) KJ,\n" + 
			"       JIHKJB J,PINZB PZ,\n" + 
			"       MEIKDQB,\n" + 
			"       SHENGFB\n" + 
			" WHERE KJ.ID = SL.YUETJKJB_ID(+) AND KJ.PINZB_ID = PZ.ID\n";
			if(visit.getDiancxxb_id()==112){SQL+="AND (SL.ZHUANGT=1 OR SL.ZHUANGT=3) AND (HC.ZHUANGT=1 OR HC.ZHUANGT=3)\n";} 
			 SQL+="   AND KJ.FENX = SL.FENX(+)\n" + 
			"   AND KJ.ID = HC.YUETJKJB_ID(+)\n" + 
			"   AND KJ.FENX = HC.FENX(+)\n" + 
			"   AND KJ.JIHKJB_ID = J.ID\n" + 
			"   AND KJ.DQID = MEIKDQB.ID\n" + 
			"   AND MEIKDQB.SHENGFB_ID = SHENGFB.ID";
			
		} else if (this.getReportType().equals(GD_DR02)) {
			//return 
			SQL="SELECT KJ.GONGYSB_ID,\n" +
			"       KJ.DQID,\n" + 
			"       KJ.DQMC,\n" + 
			"       SHENGFB.QUANC QUANC,\n" + 
			"       DECODE(KJ.JIHKJB_ID, 1, '�ص㶩��', 3, '�ص㶩��', J.MINGC) TJKJ,\n" + 
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
			"               NVL('����', '') AS FENX\n" + 
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
			"               NVL('�ۼ�', '') AS FENX\n" + 
			"          FROM YUETJKJB K, VWGONGYSDQ DQ\n" + 
			"         WHERE K.RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			"           AND DQ.ID = K.GONGYSB_ID\n" + 
			"           AND K.DIANCXXB_ID IN (" + diancxxb_id + ")) KJ,\n" + 
			"       JIHKJB J,\n" + 
			"       MEIKDQB,\n" + 
			"       SHENGFB\n" + 
			" WHERE KJ.ID = SL.YUETJKJB_ID(+)\n" ;
			if(visit.getDiancxxb_id()==112){SQL+="AND (SL.ZHUANGT=1 OR SL.ZHUANGT=3)\n";} 
			SQL+="   AND KJ.FENX = SL.FENX(+)\n" + 
			"   AND KJ.JIHKJB_ID = J.ID\n" + 
			"   AND KJ.DQID = MEIKDQB.ID\n" + 
			"   AND MEIKDQB.SHENGFB_ID = SHENGFB.ID";

		} else if(this.getReportType().equals(GD_DR03)) {
			//return
			SQL="SELECT KJ.GONGYSB_ID,\n" +
			"       KJ.DQID,\n" + 
			"       KJ.DQMC,\n" + 
			"       SHENGFB.QUANC QUANC,\n" + 
			"       DECODE(KJ.JIHKJB_ID, 1, '�ص㶩��', 3, '�ص㶩��', J.MINGC) TJKJ,\n" + 
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
			"               NVL('����', '') AS FENX\n" + 
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
			"               NVL('�ۼ�', '') AS FENX\n" + 
			"          FROM YUETJKJB K, VWGONGYSDQ DQ\n" + 
			"         WHERE K.RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			"           AND DQ.ID = K.GONGYSB_ID\n" + 
			"           AND K.DIANCXXB_ID IN (" + diancxxb_id + ")) KJ,\n" + 
			"       JIHKJB J,\n" + 
			"       MEIKDQB,\n" + 
			"       SHENGFB\n" + 
			" WHERE KJ.ID = SL.YUETJKJB_ID(+)\n" ;
			if(visit.getDiancxxb_id()==112){SQL+="AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.ZHUANGT=1 OR ZL.ZHUANGT=3)\n";} 
			SQL+="   AND KJ.FENX = SL.FENX(+)\n" + 
			"   AND KJ.ID = ZL.YUETJKJB_ID(+)\n" + 
			"   AND KJ.FENX = ZL.FENX(+)\n" + 
			"   AND KJ.JIHKJB_ID = J.ID\n" + 
			"   AND KJ.DQID = MEIKDQB.ID\n" + 
			"   AND MEIKDQB.SHENGFB_ID = SHENGFB.ID";
		
		} else {
			return "";
		}
		return SQL;
		
	}
	
//	�ֳ���	
	private String getDiancxxb_id() {
		return this.getTreeid();
	}
	
	/**
	 * 1�����ط���ͳ���)��Ӧ�̱�ȡmeitly(ú̿��Դ)�ֶ�
	 * 		����Ĭ��Ϊ�ط���
	 * 
	 * 2��ˮ�ֲ����û��ȡ������0����
	 */	
	private String getGdran01b() {
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = this.getDiancxxb_id();		
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" �� "+getYuefValue().getValue()+" ��";

		String sql="SELECT DC.MINGC,SR.PINZ,SR.FENX,SR.QICKC,SR.JINGZ,SR.YUNS,\n" +
		"SR.HAOYHJ,SR.FADY,SR.GONGRY,SR.QITY,SR.SUNH,SR.DIAOCL,SR.SHUIFTZ,SR.PANYK,SR.KUC\n" + 
		"  FROM (SELECT ROWNUM XUH, INSR.* FROM (SELECT GROUPING(Z.FENX) A,\n" + 
		"                       GROUPING(Z.DIANCXXB_ID) B,\n" + 
		"                       GROUPING(Z.PINZB_ID) C,\n" + 
		"                       DECODE(GROUPING(Z.DIANCXXB_ID) + GROUPING(Z.FENX),1,'-1',Z.DIANCXXB_ID) DIANCXXB_ID,\n" + 
		"                       Z.FENX,\n" + 
		"                       DECODE(GROUPING(Z.DIANCXXB_ID) + GROUPING(Z.PINZB_ID),1,'�ϼ�',2,'-',Z.PINZB_ID) PINZ,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,QICKC*0.4,QICKC)), 0) AS QICKC,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,(JINGZ + YUNS)*0.4,(JINGZ + YUNS))), 0) AS JINGZ,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,YUNS*0.4,YUNS)), 0) AS YUNS,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,HEJ*0.4,HEJ)), 0) AS HAOYHJ,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,FADY*0.4,FADY)),0) AS FADY,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,GONGRY*0.4,GONGRY)),0) AS GONGRY,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,QITH*0.4,QITH)),0) AS QITY,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,SUNH*0.4,SUNH)),0) AS SUNH,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,DIAOCL*0.4,DIAOCL)),0) AS DIAOCL,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,SHUIFTZ*0.4,SHUIFTZ)),0) AS SHUIFTZ,\n" + 
		"                       ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,PANYK*0.4,PANYK)),0) AS PANYK,\n" + 
		"                       DECODE(Z.FENX, '����', ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,KUC*0.4,KUC)), 0), NULL) AS KUC\n" + 
		"                  FROM (" + getBaseSql(strDate, diancxxb_id) + ") Z\n" + 
		"                 GROUP BY ROLLUP(Z.FENX, Z.DIANCXXB_ID, Z.PINZB_ID)\n" + 
		"                HAVING NOT GROUPING(Z.FENX) = 1\n" + 
		"                 ORDER BY GROUPING(Z.DIANCXXB_ID) DESC,Z.DIANCXXB_ID,GROUPING(Z.PINZB_ID) DESC,Z.PINZB_ID,Z.FENX) INSR) SR,\n" + 
		"       (SELECT ID, MINGC, XUH FROM DIANCXXB UNION SELECT -1, '�ܼ�', -1 FROM DUAL) DC\n" + 
		" WHERE SR.DIANCXXB_ID = DC.ID\n" + 
		" ORDER BY DC.XUH, SR.XUH";

		
		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//�����ͷ����

		 String ArrHeader[][]=new String[3][15];
		 ArrHeader[0]=new String[] {"���","ú��","����","�ڳ����","ʵ�ʹ�Ӧ����","ʵ�ʹ�Ӧ����",
				 "ʵ�ʺ�������","ʵ�ʺ�������","ʵ�ʺ�������","ʵ�ʺ�������","ʵ�ʺ�������",
				 "����(+)��<br>����(-)","ˮ�ֲ�<br>����","��ӯ(+)��<br>�̿�(-)","��ĩ���"};
		 
		 ArrHeader[1]=new String[] {"���","ú��","����","�ڳ����","��������","����:����",
				 "�ϼ�","������","������","������","�������",
				 "����(+)��<br>����(-)","ˮ�ֲ�<br>����","��ӯ(+)��<br>�̿�(-)","��ĩ���"};
		 
		 ArrHeader[2]=new String[] {"��","��","��","1","2","3","4","5","6","7","8","9","10","11","12"};

		 int ArrWidth[]=new int[] {120,59,59,59,89,59,59,59,59,59,59,89,59,59,59};

	 //����ҳ����		 
		 
		Table titleTable = new Table(4, 15);
		
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "���λ:" + _Danwqc, titleTable.getCols() - 2);
		titleTable.setCellValue(3,titleTable.getCols() - 1, "����ȼ01��", 2);
		//titleTable.setCellValue(3, 1, "�����:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols() - 2);
		titleTable.setCellValue(4, titleTable.getCols() - 1, "��λ:�֡�Ԫ��% ", 2);
		titleTable.setCellAlign(4, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);			
		
		rt.setTitle(titleTable);	 
		rt.setTitle("���������չ�ɷ����޹�˾������ȼú��Ӧ�����������±�", 2);
				
		//����ҳ��
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		����
		rt.setBody(new Table(rs,3,0,2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.mergeFixedRow();
		rt.getPages();
		
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.ShowZero=reportShowZero();
		
//		����������
//		convertItem(rt.body);
	
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(5, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(9, 2, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 1, "���:", Table.ALIGN_LEFT);
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
	 * 1�����ط���ͳ���)��Ӧ�̱�ȡmeitly(ú̿��Դ)�ֶ�
	 * 		����Ĭ��Ϊ�ط���
	 * 
	 * 2�����ӯ��ȡ 0
	 */	
	private String getGdran02b() {
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = this.getDiancxxb_id();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" �� "+getYuefValue().getValue()+" ��";

		String sql=
			"SELECT " +
			"	  DECODE(GROUPING(dc.mingc),1,'�ܼ�',dc.mingc)KUANGB,   \n" +
			"     Z.FENX,\n" + 
			"     ROUND(SUM(DECODE(Z.DIANCXXB_ID, 215, JINCML * 0.4, JINCML)), 0) AS JINCML,\n" + 
			"     ROUND(SUM(DECODE(Z.DIANCXXB_ID, 215, HEJ * 0.4, HEJ)), 0) AS HEJ,\n" + 
			"     ROUND(SUM(DECODE(Z.DIANCXXB_ID, 215, JIANJL * 0.4, JIANJL)), 0) AS GUOHSL,\n" + 
			"     ROUND(SUM(DECODE(z.diancxxb_id,215,(HEJ - JIANJL)*0.4,(HEJ - JIANJL))),0) AS JIANCSL,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,JIANJL*0.4,JIANJL)) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)) * 100, 2)) AS GUOHL,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,JIANJL*0.4,JIANJL)) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)) * 100, 2)) AS JIANCL,\n" + 
			"     SUM(NVL(YINGD, 0) - NVL(KUID, 0)) AS YINGK,\n" + 
			"     SUM(NVL(YINGD, 0) - NVL(KUID, 0)) AS GUOHYK,\n" + 
			"     0 AS JIANCYK,\n" + 
			"     SUM(NVL(KUIDZJE, 0)) AS KUIDZJE,\n" + 
			"     --SUM(NVL(SUOPSL, 0)) AS SUOPSL,\n" + 
			"     SUM(NVL(SUOPJE, 0)) AS SUOPJE,\n" + 
			"     DECODE(SUM(KUIDZJE),0,0,ROUND_NEW(SUM(NVL(SUOPJE, 0)) / SUM(KUIDZJE) * 100, 2)) AS SUOPL\n" +
			"FROM (" + getBaseSql(strDate, diancxxb_id) + ")Z,diancxxb dc\n" +
			"WHERE z.diancxxb_id=dc.id\n" +
			"GROUP BY ROLLUP (z.fenx,dc.mingc,dc.xuh)\n" + 
			"HAVING not(GROUPING(z.fenx)=1 or GROUPING(dc.mingc)+GROUPING(dc.xuh)=1)\n" + 
			"ORDER BY GROUPING(dc.mingc)DESC, dc.xuh,z.fenx";

		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//�����ͷ����

		 String ArrHeader[][]=new String[3][14];
		 ArrHeader[0]=new String[] {"���","����","����ú��","��������","��������",
				 "��������","�����","�����","ӯ(+)��(-)����","ӯ(+)��(-)����","ӯ(+)��(-)����",
				 "���������ۺϽ��","������","������"};
		 
		 ArrHeader[1]=new String[] {"���","����","����ú��","С��","��������",
				 "�������","������","�����","С��","����","���",
				 "���������ۺϽ��","������","������"};
		 
		 ArrHeader[2]=new String[] {"��","��","1","2","3","4","5","6","7","8",
				 "9","10","11","12"};

		 int ArrWidth[]=new int[] {120,59,80,59,59,59,59,59,59,59,59,100,59,59};

	 //����ҳ����		 
		 
		Table titleTable = new Table(4, 14);
		
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setWidth(ArrWidth);
		
		titleTable.setCellValue(4, 1, "���λ:" + _Danwqc, titleTable.getCols() - 2);
		titleTable.setCellValue(3, titleTable.getCols() - 1, "����ȼ02��", 2);
		//titleTable.setCellValue(4, 1, "�����:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols() - 2);
		titleTable.setCellValue(4, titleTable.getCols() - 1, "��λ:�֡�Ԫ��% ", 2);
		
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(4, titleTable.getCols() - 1, Table.ALIGN_RIGHT);		
		
		rt.setTitle(titleTable);	 
		rt.setTitle("���������չ�ɷ����޹�˾����ȼú�������ռ������±�", 2);
				
		//����ҳ��
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		����
		rt.setBody(new Table(rs,3,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.mergeFixedRow();
		rt.getPages();
		
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.ShowZero=reportShowZero();
		
//		����������
//		convertItem(rt.body);
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(3, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 1, "�Ʊ�:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(10, 2, "���:", Table.ALIGN_CENTER);
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
	 * 1�����ط���ͳ���)��Ӧ�̱�ȡmeitly(ú̿��Դ)�ֶ�
	 * 		����Ĭ��Ϊ�ط���
	 */
	private String getGdran03b() {
		String _Danwqc = getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = this.getDiancxxb_id();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" �� "+getYuefValue().getValue()+" ��";

		String sql=
			"SELECT " +
			"     DECODE(GROUPING(dc.mingc),1,'�ܼ�',dc.mingc) KUANGB, \n" +
			"	  FX,\n" +
			"     ROUND (SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0) AS JINML,\n" + 
			"     ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0) AS YANSSL,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND_NEW(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)) * 100, 2)) AS JIANZL,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * MT_KF) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS MT_KF,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * AAR_KF) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS AAR_KF,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(JINCML * Vdaf_KF) / SUM(JINCML), 2)) AS Vdaf_KF,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * QNET_AR_KF) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS QNET_AR_KF,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * STD_KF) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS ST_D_KF,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * MT) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS MAR,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * AAD) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS AAD,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * AD) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS AD,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * Vdaf) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS Vdaf,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)),0,0,ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * QNET_AR) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS QNET_AR,\n" + 
			"     DECODE(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 0, 0, ROUND(SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML) * STD) / SUM(DECODE(z.diancxxb_id,215,JINCML*0.4,JINCML)), 2)) AS ST_D,\n" + 
			"     SUM(NVL(ZHIJBFJE, 0)) AS JZ_HEJ,\n" + 
			"     SUM(NVL(ZHIJBFJE_M, 0)) AS JZ_SHUIF,\n" + 
			"     SUM(NVL(ZHIJBFJE_A, 0)) AS JZ_HUIF,\n" + 
			"     SUM(NVL(ZHIJBFJE_V, 0)) AS JZ_HUIFF,\n" + 
			"     SUM(NVL(ZHIJBFJE_Q, 0)) AS JZ_FARL,\n" + 
			"     SUM(NVL(ZHIJBFJE_S, 0)) AS JZ_LIUF,\n" + 
			"     SUM(NVL(ZHIJBFJE_T, 0)) AS JZ_HUIRD,\n" + 
			"     SUM(NVL(SUOPJE, 0)) AS SUOPJE,\n" + 
			"     decode(SUM(NVL(ZHIJBFJE, 0)),0,0,round(SUM(NVL(SUOPJE, 0))/ SUM(NVL(ZHIJBFJE, 0))*100,2)) AS SUOPL\n" + 
			"FROM (" + getBaseSql(strDate, diancxxb_id) + ")Z,diancxxb dc\n" +
			"WHERE z.diancxxb_id=dc.id\n" +
			"GROUP BY ROLLUP (z.fx,dc.mingc,dc.xuh)\n" +
			"HAVING not(GROUPING(z.fx)=1 or GROUPING(dc.mingc)+GROUPING(dc.xuh)=1)   \n" + 
			"ORDER BY GROUPING(dc.mingc)DESC, dc.xuh,z.fx";

		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//�����ͷ����

		 String ArrHeader[][]=new String[3][25];
		 ArrHeader[0]=new String[] {"���","����","����ú��","��������","������",
				 "�󷽻���","�󷽻���","�󷽻���","�󷽻���","�󷽻���","���������������",
				 "���������������","���������������","���������������","���������������","���������������",
				 "�ʼ۲����ۺϽ��","�ʼ۲����ۺϽ��","�ʼ۲����ۺϽ��","�ʼ۲����ۺϽ��","�ʼ۲����ۺϽ��",
				 "�ʼ۲����ۺϽ��","�ʼ۲����ۺϽ��","������","������"};
		 
		 ArrHeader[1]=new String[] {"���","����","����ú��","��������","������",
				 "Mt<br>(%)","Aar<br>(%)","Vdaf<br>(%)","Qnet,ar<br>(MJ/Kg)","St,d<br>(%)",
				 "Mt<br>(%)","Aad<br>(%)","Ad<br>(%)","Vdaf<br>(%)","Qnet,ar<br>(%)","St,d<br>(%)",
				 "С��","ˮ��","�ҷ�","�ӷ���","��ֵ","���","���۵�","������","������"};
		 
		 ArrHeader[2]=new String[] {"��","��","1","2","3","4","5","6","7","8",
				 "9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

		 int ArrWidth[]=new int[] {120,59,59,59,59,40,40,40,40,40,40,40,40,40,40,
				 40,59,59,59,59,59,59,59,59,59,59};

	 //����ҳ����		 
		 
		Table titleTable = new Table(4, 25);
		
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "���λ:" + _Danwqc, titleTable.getCols() - 2);
		titleTable.setCellValue(3, titleTable.getCols() - 1, "����ȼ03��", 2);
		//titleTable.setCellValue(4, 1, "�����:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols() - 2);
		titleTable.setCellValue(4, titleTable.getCols() - 1, "��λ:�֡�Ԫ��% ", 2);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(4, titleTable.getCols() - 1, Table.ALIGN_RIGHT);		
		
		rt.setTitle(titleTable);	 
		rt.setTitle("���������չ�ɷ����޹�˾����ȼú�������ռ������±�", 2);
				
		//����ҳ��
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		����
		rt.setBody(new Table(rs,3,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.getPages();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		for (int i = 6; i <= 24; i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.ShowZero=reportShowZero();
		
//		����������
//		convertItem(rt.body);
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(10, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(16, 2, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(20, 2, "���:", Table.ALIGN_LEFT);
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
		String strMonth=getNianfValue().getValue()+" �� "+getYuefValue().getValue()+" ��";

		String sql=
			"SELECT SR.DQMC,SR.FENX,\n" +
			"       SR.RUCSL,SR.RUCMJ+SR.RUCYJ+SR.RUCZF DAOCZHJ,\n" + 
			"       SR.RUCMJ,SR.RUCYJ,SR.RUCZF,\n" + 
			"       SR.RUCMJ+SR.RUCYJ+SR.RUCZF-SR.RUCMJS-SR.RUCYJS-SR.RUCZFS DAOCBHSZJ,\n" + 
			"       SR.RUCRZ,\n" + 
			"       ROUND(decode(SR.RUCRZ,0,0,(SR.RUCMJ+SR.RUCYJ+SR.RUCZF-SR.RUCMJS-SR.RUCYJS-SR.RUCZFS)*29.271/SR.RUCRZ),2) BUHSBMDJ,\n" + 
			"       ROUND(decode(SR.RUCRZ,0,0,(SR.RUCMJ+SR.RUCYJ+SR.RUCZF)*29.271/SR.RUCRZ),2) BIAOMDJM \n"+
			"FROM(SELECT GROUPING(Y.FENX) A,GROUPING(DC.ID) B,GROUPING(DC.MINGC) C,GROUPING(DC.XUH) D,\n" +
			"			   GROUPING(J.MINGC) E,GROUPING(G.MINGC) F,GROUPING(MK.MINGC) G,GROUPING(PZ.MINGC) H,\n" + 
			"              DECODE(GROUPING(DC.ID), 1, '�ܼ�', DC.MINGC)DQMC,\n" + 
			"              Y.FENX FENX,\n" + 
			"              round(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)),0) RUCSL,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCRL * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCRZ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCMJ * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCMJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCMJS * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCMJS,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCYJ * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCYJ,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCYJS * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCYJS,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCZF * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCZF,\n" + 
			"              round(DECODE(SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)), 0, 0, SUM(Y.RUCZFS * DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL)) / SUM(DECODE(Y.DIANCXXB_ID,215,Y.RUCSL*0.4,Y.RUCSL))),2) RUCZFS\n" + 
			"         FROM YUERCBMDJ_GYS Y, GONGYSB G, JIHKJB J,meikxxb mk,pinzb pz, DIANCXXB DC\n" + 
			"         WHERE Y.GONGYSB_ID=G.ID AND Y.MEIKXXB_ID=MK.ID AND Y.PINZB_ID=PZ.ID AND Y.JIHKJB_ID=J.ID\n" + 
			"           AND Y.DIANCXXB_ID(+) = DC.ID  AND DC.ID IN ("+diancxxb_id+")\n" + 
			"           AND Y.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n";
		
			if(visit.getDiancxxb_id()==112){
				sql+="AND (Y.ZHUANGT=1 OR Y.ZHUANGT=3)\n";
			} 
			
			sql+="    GROUP BY ROLLUP(Y.FENX,DC.ID,DC.MINGC,DC.XUH,J.MINGC,G.MINGC,MK.MINGC,PZ.MINGC)\n" + 
			"HAVING GROUPING(Y.FENX) + GROUPING(DC.ID) = 1 OR GROUPING(DC.XUH) + GROUPING(J.MINGC) = 1\n" +
			"ORDER BY GROUPING(DC.XUH) DESC, DC.XUH, Y.FENX)sr";
			
		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//�����ͷ����
		 String ArrHeader[][]=new String[2][11]; 
		 ArrHeader[0]=new String[] {"���","����","�볧��(��)","�����ۺϼ�","ú��(��˰)","�˷�(��˰)",
				 "�ӷ�","��������<br>˰�ܼ�","Qnet,ar<br>(MJ/Kg)","��������<br>˰��ú����","������˰<br>��ú����"};
		 ArrHeader[1]=new String[] {"��","��","1","2","3","4","5","6","7","8","9"};
		 int ArrWidth[]=new int[] {120,40,80,80,80,80,80,80,80,80,80};
	 //����ҳ����		 
		Table titleTable = new Table(4, 11);
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "���λ:" + _Danwqc, titleTable.getCols() - 5);
		titleTable.setCellAlign(4, 1, Table.ALIGN_LEFT);
		titleTable.setCellValue(3, titleTable.getCols() - 1, "����ȼ04�ֳ���", 2);
		//titleTable.setCellValue(4, 1, "�����:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols()-2);
		titleTable.setCellValue(4, titleTable.getCols() - 2, "��λ:�֡�Ԫ", 3);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(4, titleTable.getCols() - 2, Table.ALIGN_RIGHT);		
		
		rt.setTitle(titleTable);	 
		rt.setTitle("���������չ�ɷ����޹�˾ȼ���볧��ú���۷����±�", 2);
				
		//����ҳ��
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		����
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.getPages();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		for (int i = 3; i <= rt.body.getCols(); i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.ShowZero=reportShowZero();
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(3, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 1, "�Ʊ�:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 1, "���:", Table.ALIGN_RIGHT);
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
		String strMonth=getNianfValue().getValue()+" �� "+getYuefValue().getValue()+" ��";
		
		String sql=	"SELECT SR.DQMC,SR.FENX,SR.JIESL,SR.QNET_AR,SR.MEIJ,SR.MEIJS,SR.KUANGQYF,SR.YUNJ,SR.YUNJS,SR.ZAF,SR.ZAFS,SR.ZONGHJBHS,\n" +
		"       ROUND(decode(SR.QNET_AR,0,0,(SR.MEIJ+SR.YUNJ+SR.ZAF-SR.MEIJS-SR.YUNJS-SR.ZAFS)*29.271/SR.QNET_AR),2) BUHSBMDJ,\n" + 
		"       ROUND(decode(SR.QNET_AR,0,0,(SR.MEIJ+SR.YUNJ+SR.ZAF)*29.271/SR.QNET_AR),2) BIAOMDJM\n" + 
		"     FROM (SELECT GROUPING(Y.FENX) A,GROUPING(DC.ID) B,GROUPING(DC.MINGC) C,GROUPING(DC.XUH) D,\n" +
		"			   GROUPING(J.MINGC) E,GROUPING(G.MINGC) F,GROUPING(MK.MINGC) G,GROUPING(PZ.MINGC) H,\n" + 
		"              DECODE(GROUPING(DC.ID), 1, '�ܼ�', DC.MINGC) DQMC,\n" + 
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
		"         FROM YUEJSDJ Y, GONGYSB G, JIHKJB J, MEIKXXB MK, PINZB PZ,DIANCXXB DC\n" + 
		"         WHERE Y.GONGYSB_ID=G.ID AND Y.MEIKXXB_ID=MK.ID AND Y.PINZB_ID=PZ.ID AND Y.JIHKJB_ID=J.ID\n" + 
		"           AND Y.DIANCXXB_ID(+) = DC.ID  AND DC.ID IN ("+diancxxb_id+")\n" + 
		"           AND Y.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n"; 
		if(visit.getDiancxxb_id()==112){
			sql+="AND (Y.ZHUANGT=1 OR Y.ZHUANGT=3)\n";
			} 
		sql+="    GROUP BY ROLLUP(Y.FENX,DC.ID,DC.MINGC,DC.XUH,J.MINGC,G.MINGC,MK.MINGC,PZ.MINGC)\n" + 
			 "	  HAVING GROUPING(Y.FENX) + GROUPING(DC.ID) = 1 OR GROUPING(DC.XUH) + GROUPING(J.MINGC) = 1"+	
			 "         ORDER BY GROUPING(DC.XUH) DESC, DC.XUH, Y.FENX)sr";

		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//�����ͷ����

		 String ArrHeader[][]=new String[2][14]; 
		 ArrHeader[0]=new String[] {"��λ","����","������<br>(��)","��������<br>(MJ/Kg)","ú��(��˰)<br>(Ԫ/��)","ú��˰<br>(Ԫ/��)",
				 "�����˷�<br>(Ԫ/��)","�˼�(��˰)<br>(Ԫ/��)","�˼�˰<br>(Ԫ/��)","�ӷ�<br>(Ԫ/��)","�ӷ�˰<br>(Ԫ/��)",
				 "����˰�ܼ�<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)","��˰<br>��ú����<br>(Ԫ/��)"};
		 ArrHeader[1]=new String[] {"��","��","1","2","3","4","5","6","7","8","9","10","11","12"};
		 int ArrWidth[]=new int[] {120,40,80,60,70,60,60,70,60,50,55,75,70,70};
	 //����ҳ����		 
		Table titleTable = new Table(4, 14);
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "���λ:" + _Danwqc, titleTable.getCols() - 5);
		titleTable.setCellAlign(4, 1, Table.ALIGN_LEFT);
		titleTable.setCellValue(3, titleTable.getCols() - 1, "���½����", 2);
		//titleTable.setCellValue(4, 1, "�����:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols());
		titleTable.setCellValue(4, titleTable.getCols() - 2, "��λ:�֡�Ԫ", 3);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		titleTable.setCellAlign(4, titleTable.getCols() - 2, Table.ALIGN_RIGHT);		
		
		rt.setTitle(titleTable);	 
		rt.setTitle("���������չ�ɷ����޹�˾ȼ�Ͻ����ú���۷����±�", 2);
				
		//����ҳ��
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		����
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.getPages();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		for (int i = 3; i <= rt.body.getCols(); i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.ShowZero=reportShowZero();
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(3, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 1, "�Ʊ�:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 1, "���:", Table.ALIGN_RIGHT);
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
		String strMonth=getNianfValue().getValue()+" �� "+getYuefValue().getValue()+" ��";

		String sql=
			" SELECT SR.DQMC,SR.FENX,\n" +
			"        SR.RUCSL,SR.RUCRZ,SR.RUCMJ+SR.RUCYJ+SR.RUCZF DAOCZHJ,SR.RUCMJ,SR.RUCYJ,SR.RUCZF,\n" + 
			"        ROUND(decode(SR.RUCRZ,0,0,(SR.RUCMJ+SR.RUCYJ+SR.RUCZF-SR.RUCMJS-SR.RUCYJS-SR.RUCZFS)*29.271/SR.RUCRZ),2) BUHSBMDJ,\n" + 
			"        ROUND(decode(SR.RUCRZ,0,0,(SR.RUCMJ+SR.RUCYJ+SR.RUCZF)*29.271/SR.RUCRZ),2) BIAOMDJM,\n" + 
			"        SR.JIESRZ,SR.JIESMJ,SR.JIESYJ,SR.JIESZF, SR.GUSRZ,SR.GUSMJ,SR.GUSYJ,SR.GUSZF\n" + 
			"FROM(SELECT GROUPING(Y.FENX) A,GROUPING(DC.ID) B,GROUPING(DC.MINGC) C,GROUPING(DC.XUH) D,\n" +
			"			   GROUPING(J.MINGC) E,GROUPING(G.MINGC) F,GROUPING(MK.MINGC) G,GROUPING(PZ.MINGC) H,\n" + 
			"              DECODE(GROUPING(DC.ID), 1, '�ܼ�', DC.MINGC)DQMC,\n" + 
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
			"         FROM YUERCBMDJ_GYS Y, GONGYSB G, JIHKJB J,meikxxb mk,pinzb pz, DIANCXXB DC\n" + 
			"         WHERE Y.GONGYSB_ID=G.ID AND Y.MEIKXXB_ID=MK.ID AND Y.PINZB_ID=PZ.ID AND Y.JIHKJB_ID=J.ID\n" + 
			"           AND Y.DIANCXXB_ID(+) = DC.ID  AND DC.ID IN ("+diancxxb_id+")\n" + 
			"           AND Y.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')\n";
		
			if(visit.getDiancxxb_id()==112){
				sql+="AND (Y.ZHUANGT=1 OR Y.ZHUANGT=3)\n";
			} 
			
			sql+="    GROUP BY ROLLUP(Y.FENX,DC.ID,DC.MINGC,DC.XUH,J.MINGC,G.MINGC,MK.MINGC,PZ.MINGC)\n" + 
			"HAVING GROUPING(Y.FENX) + GROUPING(DC.ID) = 1 OR GROUPING(DC.XUH) + GROUPING(J.MINGC) = 1\n" +
			"ORDER BY GROUPING(DC.XUH) DESC, DC.XUH, Y.FENX)sr";

		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//�����ͷ����

		 String ArrHeader[][]=new String[3][18];
		 ArrHeader[0]=new String[] {"��λ","����","�볧��Ϣ","�볧��Ϣ","�볧��Ϣ","�볧��Ϣ","�볧��Ϣ","�볧��Ϣ","�볧��Ϣ","�볧��Ϣ","������Ϣ","������Ϣ","������Ϣ","������Ϣ","������Ϣ","������Ϣ","������Ϣ","������Ϣ"};
		 ArrHeader[1]=new String[] {"��λ","����","����<br>(��)","��ֵ<br>(MJ/Kg)","�����ۺϼ�<br>(Ԫ/��)","ú��(��˰)<br>(Ԫ/��)","�˼�(��˰)<br>(Ԫ/��)","�ӷ�<br>(Ԫ/��)","��ú����<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)","��ֵ<br>(MJ/Kg)","ú��(��˰)<br>(Ԫ/��)","�˼�(��˰)<br>(Ԫ/��)","�ӷ�<br>(Ԫ/��)","��ֵ<br>(MJ/Kg)","ú��(��˰)<br>(Ԫ/��)","�˼�(��˰)<br>(Ԫ/��)","�ӷ�<br>(Ԫ/��)"};
		 ArrHeader[2]=new String[] {"��","��","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
		 int ArrWidth[]=new int[] {120,40,80,60,80,80,80,60,70,70,60,80,80,60,60,80,80,60};
	 //����ҳ����		 
		Table titleTable = new Table(4, 18);
		titleTable.setBorderNone();
	  for (int i = 1; i <= titleTable.getRows(); i++)
	    for (int j = 1; j <= titleTable.getCols(); j++)
		    titleTable.setCellBorderNone(i, j);
		
		titleTable.setWidth(ArrWidth);
		titleTable.setCellValue(4, 1, "���λ:" + _Danwqc, titleTable.getCols() - 3);
		
		titleTable.setCellValue(3, titleTable.getCols() - 1, "������۱�", 2);
		titleTable.setCellAlign(3, titleTable.getCols() - 1, Table.ALIGN_RIGHT);
		
		//titleTable.setCellValue(4, 1, "�����:", 1);
		titleTable.setCellValue(3, 1, strMonth, titleTable.getCols() - 2);
		titleTable.setCellAlign(3, 1 , Table.ALIGN_CENTER);
		
		titleTable.setCellValue(4, titleTable.getCols() - 2, "��λ:�֡�Ԫ", 3);
		titleTable.setCellAlign(4, titleTable.getCols() - 2, Table.ALIGN_RIGHT);	
		
		rt.setTitle(titleTable);	 
		rt.setTitle("���������չ�ɷ����޹�˾ȼ�Ͻ�����ú���۷����±�", 2);
				
		//����ҳ��
		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		����
		rt.setBody(new Table(rs,3,0,2));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.getPages();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		for (int i = 4; i <= rt.body.getCols(); i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.ShowZero=reportShowZero();
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(3, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 1, "�Ʊ�:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 1, "���:", Table.ALIGN_RIGHT);
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
	

	public String getDxValue(int xuh) {
		String reXuh = "";
		String[] dx = { "", "һ", "��", "��", "��", "��", 
				"��", "��", "��", "��", "��", "ʮ" };
		String strXuh = String.valueOf(xuh);
		for (int i = 0; i < strXuh.length(); i++)
			reXuh = reXuh + dx[Integer.parseInt(strXuh.substring(i, i + 1))];

		return reXuh;
	}
}