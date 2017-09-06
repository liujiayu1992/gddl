package com.zhiren.jt.zdt.xitgl.daimgl.gongyscx;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;



public class Gongyscx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
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
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}


	
	
	
	
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	public void submit(IRequestCycle cycle) {
		
		
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}

	
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
	
		String str = "";
	
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		
		}

		
		String chaxun = "select distinct dc.fuid,dc.xuh as dcxh,g.xuh as gxuh,gc.xuh as gcxh, dc.mingc as diancxxb_id,g.mingc as meikdq,g.bianm as diqbm,gc.mingc as meikjc,gc.quanc as meikqc,\n"
				+ "gc.bianm as meikbm,s.quanc as shengf,c.mingc as chengs\n"
				+ " from gongysb g,gongysb gc,fahb f,shengfb s,chengsb c,diancxxb dc\n"
				+ "  where g.id=gc.fuid\n"
				+ "  and f.gongysb_id=gc.id\n"
				+ "  and gc.shengfb_id=s.id(+)\n"
				+ "  and gc.chengsb_id=c.id(+)\n"
				+ "  and f.diancxxb_id=dc.id\n"
				+ "  "+str+"\n"
				+ "   order by dc.fuid,dc.xuh,dc.mingc,g.xuh,g.mingc,gc.xuh,gc.mingc";

 ;

		
		
		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("gongysmksqb");
   	egu.setWidth("bodyWidth");

   	egu.getColumn("fuid").setHidden(true);
   	egu.getColumn("dcxh").setHidden(true);
   	egu.getColumn("gxuh").setHidden(true);
   	egu.getColumn("gcxh").setHidden(true);
   	
	egu.getColumn("diancxxb_id").setCenterHeader("�糧����");
	egu.getColumn("meikdq").setCenterHeader("ú�����");
	egu.getColumn("diqbm").setCenterHeader("ú���������");
	egu.getColumn("meikjc").setCenterHeader("ú����");
	egu.getColumn("meikqc").setCenterHeader("ú��ȫ��");
	egu.getColumn("meikbm").setCenterHeader("ú�����");
	egu.getColumn("shengf").setCenterHeader("ʡ��");
	egu.getColumn("chengs").setCenterHeader("����");
	
	
	
	//�趨�г�ʼ���
	
	egu.getColumn("diancxxb_id").setWidth(120);
	egu.getColumn("meikdq").setWidth(120);
	egu.getColumn("diqbm").setWidth(100);
	egu.getColumn("meikjc").setWidth(120);
	egu.getColumn("meikqc").setWidth(160);
	egu.getColumn("meikbm").setWidth(100);
	egu.getColumn("shengf").setWidth(80);
	egu.getColumn("chengs").setWidth(80);

	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(30);//���÷�ҳ
	
	
	
	//*****************************************����Ĭ��ֵ****************************
	//	�糧������
	int treejib2 = this.getDiancTreeJib();

	if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
		ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
		String mingc="";
		if(r.next()){
			mingc=r.getString("mingc");
		}
		egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		
	}		
	
	
	//�������ڵ�Ĭ��ֵ,
	

	
	//*************************������*****************************************88
	//����ʡ�ݵ�������
	ComboBox cb_shengf=new ComboBox();
	egu.getColumn("shengf").setEditor(cb_shengf);
	cb_shengf.setEditable(true);
	egu.getColumn("shengf").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String ShengfbSql="select id,quanc from shengfb order by quanc";
	egu.getColumn("shengf").setComboEditor(egu.gridId, new IDropDownModel(ShengfbSql));
	//���ó���������
	ComboBox cb_chengs=new ComboBox();
	egu.getColumn("chengs").setEditor(cb_chengs);
	cb_chengs.setEditable(true);
	egu.getColumn("chengs").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String ChengsbSql="select j.id,j.mingc from chengsb j order by id  ";
	egu.getColumn("chengs").setComboEditor(egu.gridId, new IDropDownModel(ChengsbSql));
	
	//********************������************************************************
	
	//������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");// ���÷ָ���

		
		egu.addTbarText("ú��λ����:");
		TextField tf=new TextField();
		tf.setWidth(100);
		tf.setId("TIAOJ");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
		
		String Gongysstr="function (){"+
		//"if(!gridDiv_sm.hasSelection()){Ext.MessageBox.alert('��ʾ','���ȵ��Ҫ���ع�����λ����������У�');return; };"+
   		" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
		"url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Gongyscx_tc&tiaoj='+TIAOJ.getValue();\n"+
   	    "var rewin =  window.showModalDialog(url,'newWin','dialogWidth=700px;dialogHeight=580px;');\n" +
  
   	   "if   (rewin != null){gridDiv_sm.getSelected().set('GONGHDWBM',rewin.bianh);}" +
   	    "" +
   	    "}";
	
		egu.addToolbarItem("{"+new GridButton("��ѯ",""+Gongysstr+"").getScript()+"}");
		egu.addTbarText("-");
			
//////////////////////////////////////////////////
		
		
		
		
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
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			
			this.setTreeid(null);
		
			setTbmsg(null);
		
		}
		
			getSelectData();
		
		
	}

	

//	 �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//�õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}

}
