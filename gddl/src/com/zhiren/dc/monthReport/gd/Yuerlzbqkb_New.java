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

import com.zhiren.common.CustomMaths;
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
 * ���ߣ���衻�
 * ʱ�䣺2010-08-25
 * ������
 *   ���ݹ�������  �����¶�ȼ�Ϲ���ָ����� �±�����
 */
/*
 * ���ߣ�songy
 * ʱ�䣺2011-4-18
 * ������
 *   �޸ı���sql,��¯��ֵ���㲻��,�����������򲻶�
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-05-27
 * ���������ݹ��繫˾�����޸Ķ�ѡ�糧����Ĭ�ϳ�ʼֵ��Ϊ�糧�������2�����е�λ����
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-07-04
 * ������������¯�ۺϱ�ú���۵ļ��㹫ʽ,����yuezbdyb�е���¯�ۺϱ�ú���۹�ʽ�ó�
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-01-12
 * ������������ָ��������������Ϳ��ӺĴ�ϼ���ȡ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-02-01
 * ������������ָ�����������ú���Լ���ú����������ϢӦ��������
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-02-02
 * ������������ָ����������糧�������
 */

/*
 * ���ߣ����
 * ʱ�䣺2012-02-12
 * ����: �����е������û�Ϊ�������ʱֻ��ʾ���ϱ������ݣ���״̬Ϊ1��3�����ݣ���
 * 		 �����볧��ú���۵ļ��㹫ʽ���볧�ܳɱ������볧�۱�ú����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-03-02
 * ����: �������ڹ��������±������ú�����ɱ�����������
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-03-05
 * ����: ��������������¯��ú������¯��ú���۵�ȡֵ�ͼ��㹫ʽ
 * 		 ������¯��ú����ȡֵ��ʽ
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-03-08
 * ����: ����������ص���Լ��ʽ���ڲ�ȡ������Լ
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-04-05
 * ����: �����볧��ú����sql
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-05-15
 * ���÷�Χ���������
 * ���������������ѯ���ݣ��Լ��볧��Ϣ��ȡֵ��ʽ
 */

public class Yuerlzbqkb_New extends BasePage {
	
	
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
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
	}
	
	// ���������
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

	// �·�������
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
	
	
//	���ӵ糧��ѡ���ļ���
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
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
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
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	
//	�ֳ���
	public boolean isFencb() {
		return ((Visit) this.getPage().getVisit()).isFencb();
	}
	
	// ҳ���ʼ����
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
	
	private void init(){
		Visit visit = (Visit) getPage().getVisit();
		visit.setProSelectionModel1(null);
		visit.setDropDownBean1(null);
		visit.setProSelectionModel2(null);
		visit.setDropDownBean2(null);
		visit.setDefaultTree(null);
		setDiancmcModel(null);
		initDiancTree();
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
		this.getSelectData();
	}

	// ��������
	public String getPrintTable() {	
		return getYuezbqkb();
	}
	
	private String getYuezbqkb() {
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = getTreeid();		
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" �� "+getYuefValue().getValue()+" ��";
		StringBuffer sql=new StringBuffer();
		StringBuffer where_sql=new StringBuffer();

		String tiaoj1="";
		String tiaoj2="";
		String tiaoj3="";
		String tiaoj4="";
		String tiaoj5="";
		Visit visit = (Visit) getPage().getVisit();
		if(visit.getDiancxxb_id()==112)
		{
			tiaoj1=" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3) \n ";
			tiaoj2=" AND (HC.ZHUANGT=1 OR HC.ZHUANGT=3)\n ";
			tiaoj3=" AND (y.zhuangt=1 OR y.zhuangt=3) \n";
			tiaoj4=" AND (z.zhuangt=1 OR z.zhuangt=3)\n ";
			tiaoj5=" AND (js.zhuangt=1 OR js.zhuangt=3) \n";
		}
		
		where_sql.append("where id in("+diancxxb_id+")\n");
		
		sql.append("SELECT mingc,fenx,nvl(laiml,0)laiml,nvl(haoyl,0)haoyl,nvl(kuc,0)kuc,nvl(youhyl,0)youhyl,rez,rulrz, NVL(rez,0)-NVL(rulrz,0) rezc,\n" );
		sql.append("NVL(rucmj,0)rucmj,NVL(rucyj,0)rucyj,NVL(ruczf,0)ruczf,(NVL(rucmj,0)+NVL(rucyj,0)+NVL(ruczf,0))ruczhj,NVL(rulbml,0)rulbml,\n");
		sql.append("NVL(rulbmdj,0)rulbmdj,nvl(kuctrmj,0) kuctrmj,nvl(kuctrmrz,0)kuctrmrz,NVL(gusl,0)gusl,NVL(zgj,0)zgj,\n");
		sql.append("(NVL(rucmj,0)+NVL(rucyj,0)+NVL(ruczf,0)-NVL(zgj,0)-NVL(rucmjs,0)-NVL(rucyjs,0)-NVL(ruczfs,0))junjc\n");
		sql.append("FROM (select --GROUPING(dianc.fenx)a,GROUPING(dianc.fuid)b,GROUPING(dianc.mingc)c,GROUPING(dianc.xuh)d,\n");
		sql.append("--dianc.fenx,dianc.fuid,dianc.mingc, dianc.xuh,\n"); 
		sql.append("DECODE(grouping(dianc.fuid),1, '�ܼ�',dianc.mingc)  mingc,");
		sql.append("dianc.fenx,\n"); 
		sql.append("round_new(sum(shul.laiml), 0) laiml,\n"); 
		sql.append("round_new(sum(shul.haoyl), 0) haoyl,\n"); 
		sql.append("round_new(sum(shul.kuc), 0) kuc,\n"); 
		sql.append("round_new(sum(haoy.haoyl), 2) youhyl,\n"); 
		sql.append("round_new(decode(sum(shul.jincml),0,0,sum(shul.rez * shul.jincml) / sum(shul.jincml)),2) rez,\n"); 
		sql.append("round_new(decode(sum(zhibb.fadgrytrml),0,0,sum(zhibb.rultrmpjfrl*decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml)) /sum(decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml))),2) rulrz,\n"); 
		sql.append("round_new(nvl(decode(sum(shul.jincml),0,0,sum(shul.rez * shul.jincml) /sum(shul.jincml)),0),2) -\n"); 
		sql.append("round_new(decode(sum(zhibb.fadgrytrml),0,0,sum(zhibb.rultrmpjfrl*decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml)) /sum(decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml))),2) rezc,\n"); 
		
		sql.append("round_new(sum(decode(dianc.id,215,zhibb.rulbml * 0.4,zhibb.rulbml)), 0)rulbml,\n");
		sql.append("round_new(decode(sum(zhibb.rulbml),0,0,sum(zhibb.bmdj*decode(dianc.id,215,zhibb.rulbml * 0.4,zhibb.rulbml)) /sum(decode(dianc.id,215,zhibb.rulbml * 0.4,zhibb.rulbml))),2)rulbmdj,\n"); 
		sql.append("decode(grouping(dianc.fuid),1,0,MAX(zhibb.kuctrmj))kuctrmj,\n");
		sql.append("decode(grouping(dianc.fuid),1,0,MAX(zhibb.kuctrmrz))kuctrmrz,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.rucmj*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) rucmj,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.rucyj*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) rucyj,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.ruczf*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) ruczf,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.rucmjs*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) rucmjs,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.rucyjs*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) rucyjs,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.ruczfs*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) ruczfs,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.zgj*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) zgj,\n");
		sql.append("round_new(sum(jiesbm.gusl),0)gusl\n");
//		ͷ��Ϣ
		sql.append("from (select id, mingc, xuh, fuid, nvl('����', '') fenx\n"); 
		sql.append("from diancxxb\n"); 
		sql.append(where_sql); 
		sql.append("union\n"); 
		sql.append("select id, mingc, xuh, fuid, nvl('�ۼ�', '') fenx\n"); 
		sql.append("from diancxxb\n"); 
		sql.append(where_sql); 
		sql.append("union\n"); 
		sql.append("select id, mingc, xuh, fuid, nvl('ͬ���ۼ�', '') fenx\n"); 
		sql.append("from diancxxb\n"); 
		sql.append(where_sql+") dianc,\n"); 
		sql.append("\n"); 
//		��ú����ú�����
		sql.append("(SELECT DC.ID DIANCXXB_ID,\n" ); 
		sql.append("       DC.FENX,\n" );  
		sql.append("       NVL(LM.LAIML, 0) LAIML,\n");
		sql.append("       NVL(HC.HAOYL, 0) HAOYL,\n");
		sql.append("       NVL(HC.KUC, 0) KUC, NVL(LM.JINCML, 0) JINCML, NVL(LM.REZ, 0) REZ\n");
		sql.append("FROM (SELECT KJ.DIANCXXB_ID,\n" +
				"               sl.fenx fenx,\n" + 
				"               SUM(DECODE(KJ.DIANCXXB_ID, 215, (SL.JINGZ+SL.YUNS) * 0.4, (SL.JINGZ+SL.YUNS))) LAIML,\n" + 
				"               sum(decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS))) jincml,\n" + 
				"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS))) /sum(decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS)))),2) rez\n" + 
				"          FROM YUESLB SL,YUEZLB ZL,\n" + 
				"               (SELECT ID,\n" + 
				"                       DIANCXXB_ID,\n" + 
				"                       Y.GONGYSB_ID,\n" + 
				"                       Y.JIHKJB_ID,\n" + 
				"                       Y.PINZB_ID\n" + 
				"                  FROM YUETJKJB Y\n" + 
				"                 WHERE RIQ = DATE '"+strDate+"')kj\n" + 
				"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
				"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
				tiaoj1 + 
				"           GROUP BY KJ.DIANCXXB_ID, sl.FENX\n" + 
				"           UNION\n" + 
				"             SELECT KJ.DIANCXXB_ID,\n" + 
				"               NVL('ͬ���ۼ�', '') FENX,\n" + 
				"               SUM(DECODE(KJ.DIANCXXB_ID, 215, (SL.JINGZ+SL.YUNS) * 0.4, (SL.JINGZ+SL.YUNS))) LAIML,\n" + 
				"               sum(decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS))) jincml,\n" + 
				"               decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS))) /sum(decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS)))) rez\n" + 
				"               FROM YUESLB SL, YUEZLB ZL,\n" + 
				"                (SELECT ID,\n" + 
				"                       DIANCXXB_ID,\n" + 
				"                       NVL('�ۼ�', '') AS FENX,\n" + 
				"                       Y.GONGYSB_ID,\n" + 
				"                       Y.JIHKJB_ID,\n" + 
				"                       Y.PINZB_ID\n" + 
				"                  FROM YUETJKJB Y\n" + 
				"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)) KJ\n" + 
				"         WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
				"           AND KJ.ID=ZL.YUETJKJB_ID\n" + 
				"           AND KJ.FENX = SL.FENX AND KJ.FENX = ZL.FENX\n" + 
				"         GROUP BY KJ.DIANCXXB_ID) LM,\n");
		sql.append("       (SELECT HC.DIANCXXB_ID,\n");
		sql.append("               NVL('ͬ���ۼ�', '') FENX,\n");
		sql.append("               SUM(DECODE(HC.DIANCXXB_ID,\n");
		sql.append("                          215,\n");
		sql.append("                          (HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH) * 0.4,\n");
		sql.append("                          HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH)) HAOYL,\n");
		sql.append("               SUM(DECODE(HC.DIANCXXB_ID, 215, HC.KUC * 0.4, HC.KUC)) KUC\n");
		sql.append("          FROM YUESHCHJB HC\n");
		sql.append("         WHERE HC.RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n");
		sql.append("           AND HC.FENX = '�ۼ�'\n");
		sql.append("         GROUP BY HC.DIANCXXB_ID\n");
		sql.append("        UNION\n");
		sql.append("        SELECT HC.DIANCXXB_ID,\n");
		sql.append("               HC.FENX FENX,\n");
		sql.append("               SUM(DECODE(HC.DIANCXXB_ID,\n");
		sql.append("                          215,\n");
		sql.append("                          (HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH) * 0.4,\n");
		sql.append("                          HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH)) HAOYL,\n");
		sql.append("               SUM(DECODE(HC.DIANCXXB_ID, 215, HC.KUC * 0.4, HC.KUC)) KUC\n");
		sql.append("          FROM YUESHCHJB HC\n");
		sql.append("         WHERE HC.RIQ = DATE '"+strDate+"' "+tiaoj2+"\n");
		sql.append("         GROUP BY HC.DIANCXXB_ID, HC.FENX) HC,\n");
		sql.append("       (SELECT DC.ID, FX.FENX\n");
		sql.append("          FROM DIANCXXB DC,\n");
		sql.append("               (SELECT NVL('����', '') FENX\n");
		sql.append("                  FROM DUAL\n");
		sql.append("                UNION\n");
		sql.append("                SELECT NVL('�ۼ�', '') FENX\n");
		sql.append("                  FROM DUAL\n");
		sql.append("                UNION\n");
		sql.append("                SELECT NVL('ͬ���ۼ�', '') FENX FROM DUAL) FX) DC\n");
		sql.append(" WHERE DC.ID = HC.DIANCXXB_ID(+)\n");
		sql.append("   AND DC.ID = LM.DIANCXXB_ID(+)\n");
		sql.append("   AND DC.FENX = HC.FENX(+)\n");
		sql.append("   AND DC.FENX = LM.FENX(+) ) shul,");
//		�ͺ���
		sql.append("\n"); 
		sql.append("(select y.diancxxb_id,\n"); 
		sql.append("y.fenx fenx,\n"); 
		sql.append("sum(decode(diancxxb_id,215,(y.fadyy + y.gongry + y.qithy + y.sunh) * 0.4,y.fadyy + y.gongry + y.qithy + y.sunh)) haoyl\n"); 
		sql.append("from yueshcyb y\n"); 
		sql.append("where y.riq = date'"+strDate+"' "+tiaoj3+"\n"); 
		sql.append("group by y.diancxxb_id, y.fenx\n"); 
		sql.append("union\n"); 
		sql.append("select y.diancxxb_id,\n"); 
		sql.append("NVL('ͬ���ۼ�', '') fenx,\n"); 
		sql.append("sum(decode(diancxxb_id,215,(y.fadyy + y.gongry + y.qithy + y.sunh) * 0.4,y.fadyy + y.gongry + y.qithy + y.sunh)) haoyl\n"); 
		sql.append("from yueshcyb y\n"); 
		sql.append("where y.riq = add_months(date'"+strDate+"',-12)\n"); 
		sql.append("and y.fenx = '�ۼ�'\n"); 
		sql.append("group by y.diancxxb_id, y.fenx) haoy,\n"); 
		sql.append("\n"); 
//		ָ�깫ʽ
		sql.append("(SELECT Z.DIANCXXB_ID,\n" );
		sql.append("       Z.FENX,\n");
		sql.append("       Z.FADGRYTRML FADGRYTRML,\n");
		sql.append("       Z.RULTRMPJFRL RULTRMPJFRL,\n");
		sql.append("      (Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml)RULBML,\n");
		sql.append("       Z.RULZHBMDJ BMDJ,\n");
		sql.append("   		DECODE(Z.FENX,'�ۼ�',0,z.kuctrmj) kuctrmj,\n");
		sql.append("   		DECODE(Z.FENX,'�ۼ�',0,z.kuctrmrz) kuctrmrz\n");
		sql.append("  FROM YUEZBB Z\n");
		sql.append(" WHERE Z.RIQ = date '"+strDate+"' "+tiaoj4+"\n");
		sql.append("UNION\n");
		sql.append("SELECT Z.DIANCXXB_ID,\n");
		sql.append("       NVL('ͬ���ۼ�', '') FENX,\n");
		sql.append("       Z.FADGRYTRML FADGRYTRML,\n");
		sql.append("       Z.RULTRMPJFRL RULTRMPJFRL,\n");
		sql.append("       (Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml) RULBML,\n");
		sql.append("       Z.RULZHBMDJ BMDJ,\n");
		sql.append("   		0 kuctrmj,\n");
		sql.append("   		0 kuctrmrz\n");
		sql.append("  FROM YUEZBB Z\n");
		sql.append(" WHERE Z.RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n");
		sql.append("   AND Z.FENX = '�ۼ�') zhibb,\n");
		sql.append("\n"); 
		
//		�����ú����
		sql.append( "(SELECT JS.DIANCXXB_ID,\n");
		sql.append( "       JS.FENX FENX,\n");
		sql.append( "       SUM(DECODE(JS.DIANCXXB_ID, 215, JS.rucsl * 0.4, JS.rucsl)) JIESL,\n");
		sql.append( "       SUM(DECODE(JS.DIANCXXB_ID, 215, JS.gusl * 0.4, JS.gusl)) gusl,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucmj*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucmj,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucyj*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucyj,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.ruczf*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) ruczf,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucmjs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucmjs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucyjs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucyjs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.ruczfs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) ruczfs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.gusl*0.4,js.gusl)),0,0,SUM((js.gusmj+js.gusyj+js.guszf-js.gusmjs-js.gusyjs-js.guszfs)*DECODE(js.diancxxb_id,215,js.gusl*0.4,js.gusl))/SUM(DECODE(js.diancxxb_id,215,js.gusl*0.4,js.gusl))) zgj\n");
		sql.append( "  FROM YUERCBMDJ_GYS JS\n");
		sql.append( " WHERE RIQ = DATE '"+strDate+"'\n");
		sql.append(tiaoj5);
		sql.append( " GROUP BY JS.DIANCXXB_ID, JS.FENX\n");
		sql.append( "UNION\n");
		sql.append( "SELECT JS.DIANCXXB_ID,\n");
		sql.append( "      NVL('ͬ���ۼ�', '')  FENX,\n");
		sql.append( "       SUM(DECODE(JS.DIANCXXB_ID, 215, JS.rucsl * 0.4, JS.rucsl)) JIESL,\n");
		sql.append( "       SUM(DECODE(JS.DIANCXXB_ID, 215, JS.gusl * 0.4, JS.gusl)) gusl,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucmj*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucmj,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucyj*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucyj,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.ruczf*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) ruczf,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucmjs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucmjs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucyjs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucyjs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.ruczfs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) ruczfs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.gusl*0.4,js.gusl)),0,0,SUM((js.gusmj+js.gusyj+js.guszf-js.gusmjs-js.gusyjs-js.guszfs)*DECODE(js.diancxxb_id,215,js.gusl*0.4,js.gusl))/SUM(DECODE(js.diancxxb_id,215,js.gusl*0.4,js.gusl))) zgj\n");
		sql.append( "  FROM YUERCBMDJ_GYS JS\n");
		sql.append( " WHERE RIQ = add_months(date'"+strDate+"',-12)\n");
		sql.append( " AND js.fenx='�ۼ�'\n");
		sql.append( " GROUP BY JS.DIANCXXB_ID) jiesbm\n");
		
		sql.append("\n"); 
		sql.append("where dianc.id = shul.diancxxb_id(+)\n"); 
		sql.append("and dianc.id = haoy.diancxxb_id(+)\n"); 
		sql.append("and dianc.fenx = shul.fenx(+)\n"); 
		sql.append("and dianc.fenx = haoy.fenx(+)\n"); 
		sql.append("and dianc.id = zhibb.diancxxb_id(+)\n"); 
		sql.append("and dianc.fenx = zhibb.fenx(+)\n"); 
		sql.append("and dianc.id = jiesbm.diancxxb_id(+)\n"); 
		sql.append("and dianc.fenx = jiesbm.fenx(+)\n"); 
		sql.append("and dianc.id not in (300,112,303)\n"); 
		
		sql.append("group by rollup( dianc.fenx,dianc.fuid,dianc.mingc, dianc.xuh)\n");
		sql.append("HAVING GROUPING(dianc.fenx)+GROUPING(dianc.fuid)=1 OR GROUPING(dianc.fenx)+GROUPING(dianc.xuh)=0\n");
		sql.append("ORDER BY GROUPING(dianc.fuid)DESC,dianc.fuid,dianc.xuh,dianc.mingc,dianc.fenx)sr\n");
		
//		System.out.println(sql.toString());
		
		ResultSetList rs=cn.getResultSetList(sql.toString());
//		����н������ô��������
		if(rs.next()){
			rs.beforefirst();
//			��ʼ������List
			List arrStrCur =rs.getResultSetlist();
			List arrStrNew = new ArrayList();
//			��3��ѭ��������
			for(int i=0;i<arrStrCur.size();i=i+3){
				String[] beny = (String[])arrStrCur.get(i);
				String[] leij = (String[])arrStrCur.get(i+1);
				String[] tongqlj = (String[])arrStrCur.get(i+2);
				String[] leijc = subArr(leij, tongqlj);
				arrStrNew.add(beny);
				arrStrNew.add(leij);
				arrStrNew.add(tongqlj);
				arrStrNew.add(leijc);
			}
			rs.setResultSetlist(arrStrNew);
		}
		Report rt=new Report();
		 
		//�����ͷ����
		 String ArrHeader[][]=new String[2][20];
		 ArrHeader[0]=new String[] {"��λ����","����","��ú��","��ú��","��ĩ���","������",
				 "����ú<br>��ֵ","��¯ú<br>��ֵ","��ֵ��","ú��<br>(��˰)","�˼�","�ӷ�",
				 "�볧ԭú<br>�ۺϵ���<br>(��˰)","��¯��ú��<br>(���͡���)","��¯�ۺϱ�ú����",
				 "�����Ȼú��(����˰)","���ú��ֵ","�ݹ���","�ݹ���(���˷Ѳ���˰)","�ݹ����뱾�½���ú���۲�"};
		 
		 ArrHeader[1]=new String[] {"��","��","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","19"};

		 int ArrWidth[]=new int[] {65,63,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52};

		 rt.setTitle(strMonth+"���¶�ȼ�Ϲ���ָ�������", ArrWidth);
		 rt.setDefaultTitle(1,13,"��λ��"+this.getTianzdwQuanc(),Table.ALIGN_LEFT);
		 rt.setDefaultTitle(17, 4, "��λ���֡�MJ/Kg��Ԫ/��", Table.ALIGN_RIGHT);
		//����ҳ��
		
//		����
		rt.setBody(new Table(rs,2,0,0));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.mergeCol(1);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);

		rt.getPages();

		rt.body.ShowZero=true;//reportShowZero();
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		
		return rt.getAllPagesHtml();		
	}
	
//	�ۼ�-ͬ���ۼƣ��õ��µ��ַ�������
	private String[] subArr(String[] leij, String[] tongqlj){
		String[] v = new String[leij.length];
		v[0]=leij[0];
		v[1]="�ۼƲ�";
		for(int i=2;i<15;i++){
			v[i]= CustomMaths.sub(leij[i], tongqlj[i]);
		}
		return v;
	}
	
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}

}