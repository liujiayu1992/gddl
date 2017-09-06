package com.zhiren.jt.zdt.xitgl.meikxx;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meikxx extends BasePage implements PageValidateListener {
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


	private void Save() {


		//--------------------------------
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql_delete = new StringBuffer("begin \n");
		long gongysb_id=this.getMeikdqmcValue().getId();
		while (drsl.next()) {
			//ɾ���±�ú���۱�
			sql_delete.append("delete from " ).append(" gongysmkglb").append(" where gongysb_id=").append(gongysb_id)
			          .append(" and meikxxb_id=").append(drsl.getLong("id")).append(";\n");
			sql_delete.append("delete from ").append("meikxxb").append(" where id =").append(drsl.getLong("id")).append(";\n");
			}
		sql_delete.append("end;");
		//System.out.println(sql_delete.length());
		if(sql_delete.length()>11){
			con.getUpdate(sql_delete.toString());
		}
		
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		StringBuffer sql = new StringBuffer();
		while (rsl.next()) {
			sql.delete(0, sql.length());
			sql.append("begin \n");
			long id = 0;
			
			
			String ID_1=rsl.getString("ID");

			if ("0".equals(ID_1)||"".equals(ID_1)) {
				id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				
				
						
				//��ú����Ϣ�����������
				sql.append("insert into meikxxb("
								+ "id,xuh,bianm,mingc,quanc,piny,shengfb_id,leib,jihkjb_id,leix,beiz,danwdz,chengsb_id)values("
								+ id+ ","
								+rsl.getString("xuh")+",'"
								+rsl.getString("bianm")+"','"
								+rsl.getString("mingc")+"','"
								+rsl.getString("quanc")+"','"
								+rsl.getString("piny")+"',"
								+"getShengfbId('"+rsl.getString("shengfb_id")+"'),'"
								+rsl.getString("leib")+"',"
								+"getJihkjbId('"+rsl.getString("jihkjb_id")+"'),'"
								+rsl.getString("leix")+"','"
								+rsl.getString("beiz")+"','"
								+rsl.getString("danwdz")+"',"
								+"getChengsbId('"+rsl.getString("chengsb_id")+"')"
								+ ");\n");
				sql.append("insert into gongysmkglb (id,gongysb_id,meikxxb_id) values(" +
							+id+","+gongysb_id+","+id+");\n");
				
			} else {
				
				
				//�޸�ú����Ϣ������
				 sql.append("update meikxxb set xuh="
				 + rsl.getString("xuh")+",bianm='"
				 + rsl.getString("bianm")+"',mingc='"
				 + rsl.getString("mingc")+"',quanc='"
				 + rsl.getString("quanc")+"',piny='"
				 + rsl.getString("piny")+"',shengfb_id="
				 +"getShengfbId('"+rsl.getString("shengfb_id")+"'),leib='"
				 + rsl.getString("leib")+"',jihkjb_id="
				 +"getJihkjbId('"+rsl.getString("jihkjb_id")+"'), leix='"
				 + rsl.getString("leix")+"',beiz='"
				 + rsl.getString("beiz")+"',danwdz='"
				 + rsl.getString("danwdz")+"',chengsb_id="
				 +"getChengsbId('"+rsl.getString("chengsb_id")+"')"
				 + " where id=" + rsl.getLong("id")+";\n");
			
			}
			sql.append("end;");
			con.getUpdate(sql.toString());
		

			
		}
		
		con.Close();
		setMsg("����ɹ�!");
	}


	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		//-----------------------------------
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
		String gongysmc=this.getMeikdqmcValue().getValue();

		String chaxun = "select m.id,m.xuh,m.bianm,m.mingc,m.quanc, m.piny,sf.quanc as shengfb_id,\n"
				+ "       cs.mingc as chengsb_id,j.mingc as jihkjb_id,m.leib,\n"
				+ "       m.leix,\n"
				+ "       m.danwdz,\n"
				+ "       m.beiz\n"
				+ "  from meikxxb m, shengfb sf, chengsb cs, jihkjb j,gongysmkglb gl\n"
				+ " where m.shengfb_id = sf.id(+)\n"
				+ "   and m.chengsb_id = cs.id(+)\n"
				+ "   and m.jihkjb_id = j.id(+)\n"
				+ "   and m.id=gl.meikxxb_id(+)\n" 
				+ "   and gl.gongysb_id=getGongysId('"+gongysmc+"')" 
				+"    order by m.xuh     ";


		
		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
	
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("meikxxb");
   	
	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);
	
	egu.getColumn("xuh").setCenterHeader("�����");
	egu.getColumn("bianm").setCenterHeader("����");
	egu.getColumn("mingc").setCenterHeader("���");
	egu.getColumn("quanc").setCenterHeader("ȫ��");
	egu.getColumn("piny").setCenterHeader("ƴ��");
	egu.getColumn("shengfb_id").setCenterHeader("ʡ��");
	egu.getColumn("chengsb_id").setCenterHeader("������");
	egu.getColumn("jihkjb_id").setCenterHeader("�ƻ��ھ�");
	egu.getColumn("leib").setCenterHeader("���");
	egu.getColumn("leix").setCenterHeader("����");
	egu.getColumn("danwdz").setCenterHeader("��λ��ַ");
	egu.getColumn("beiz").setCenterHeader("��ע");
	//���û�Ϊ�糧����ʱ,ú����벻�ܱ༭
	if(((Visit) getPage().getVisit()).isDCUser()){
		egu.getColumn("bianm").setEditor(null);
	}
	//�趨�г�ʼ���
	egu.getColumn("xuh").setWidth(60);
	egu.getColumn("bianm").setWidth(90);
	egu.getColumn("mingc").setWidth(120);
	egu.getColumn("quanc").setWidth(150);
	egu.getColumn("piny").setWidth(80);
	egu.getColumn("shengfb_id").setWidth(80);
	egu.getColumn("chengsb_id").setWidth(80);
	egu.getColumn("jihkjb_id").setWidth(80);
	egu.getColumn("leib").setWidth(70);
	egu.getColumn("leix").setWidth(60);
	egu.getColumn("danwdz").setWidth(100);
	egu.getColumn("beiz").setWidth(100);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(22);//���÷�ҳ
	egu.setWidth(1000);//����ҳ��Ŀ��,������������ʱ��ʾ������
	
	
	
	//*****************************************����Ĭ��ֵ****************************
	
	
	
	
	//�������ڵ�Ĭ��ֵ,
	//egu.getColumn("riq").setDefaultValue(intyear+"-"+StrMonth+"-01");
	
	
	
	
	//*************************������*****************************************88
	//����ʡ�ݵ�������
	ComboBox cb_shengf=new ComboBox();
	egu.getColumn("shengfb_id").setEditor(cb_shengf);
	cb_shengf.setEditable(true);
	String ShengfSql="select id,quanc from shengfb order by mingc";
	egu.getColumn("shengfb_id").setComboEditor(egu.gridId, new IDropDownModel(ShengfSql));
	//���ó���������
	ComboBox cb_chengs=new ComboBox();
	egu.getColumn("chengsb_id").setEditor(cb_chengs);
	cb_chengs.setEditable(true);
	egu.getColumn("chengsb_id").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String jihkjSql="select c.id,c.mingc from chengsb c order by mingc  ";
	egu.getColumn("chengsb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjSql));
	
	//�ƻ��ھ�������
	ComboBox cb_jhkj=new ComboBox();
	egu.getColumn("jihkjb_id").setEditor(cb_jhkj);
	cb_jhkj.setEditable(true);
	egu.getColumn("jihkjb_id").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String cb_pinzStr="select j.id,j.mingc from jihkjb j order by id  ";
	egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(cb_pinzStr));
	egu.getColumn("jihkjb_id").setDefaultValue("�ص㶩��");
	
//	���������
	List leib = new ArrayList();
	leib.add(new IDropDownBean(0, "ͳ���"));
	leib.add(new IDropDownBean(1, "�ط���"));
	egu.getColumn("leib").setEditor(new ComboBox());
	egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(leib));
	egu.getColumn("leib").setReturnId(false);
	egu.getColumn("leib").setDefaultValue("ͳ���");
	
//	����������
	List leix = new ArrayList();
	leix.add(new IDropDownBean(0, "ú"));
	leix.add(new IDropDownBean(1, "��"));
	leix.add(new IDropDownBean(2,"��"));
	egu.getColumn("leix").setEditor(new ComboBox());
	egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(leix));
	egu.getColumn("leix").setReturnId(false);
	egu.getColumn("leix").setDefaultValue("ú");
	
	//********************������************************************************
		
		//������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");// ���÷ָ���
	
		egu.addTbarText("��Ӧ��:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MeikmcDropDown");
		comb3.setId("gongys");
		comb3.setEditable(true);
		comb3.setLazyRender(true);// ��̬��
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());

		// �趨�������������Զ�ˢ��
		egu.addOtherScript("gongys.on('select',function(){document.forms[0].submit();});");
		
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",MainGlobal.getExtMessageShow("���ڱ���,���Ժ�...", "������...", 200));
		egu.addTbarText("-");
		//�û��ǵ糧ʱ,����ʾ��ѯ����
		if(!((Visit) getPage().getVisit()).isDCUser()){
			egu.addTbarText("��ѯú�����:");
			TextField tf=new TextField();
			tf.setWidth(130);
			tf.setId("TIAOJ");
			egu.addToolbarItem(tf.getScript());
			//�ύ
			
			String strs=
	       		" var url = 'http://'+document.location.host+document.location.pathname;"+
	            "var end = url.indexOf(';');"+
				"url = url.substring(0,end);"+
	       	    "url = url + '?service=page/' + 'meikbm_cx&tiaoj='+TIAOJ.getValue();"+
	       	    " window.open(url,'newWin');";
			egu.addToolbarItem("{"+new GridButton("��ѯ","function (){"+strs+"}").getScript()+"}");
		}
		
		

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
			this.setMeikdqmcValue(null);
			this.getIMeikdqmcModel();
			this.setTreeid(null);
		
			setTbmsg(null);
		}
		
			getSelectData();
		
		
	}

//	 ��Ӧ������
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {
			String sql = "";
			int treejib2 = this.getDiancTreeJib();
			//��Ӧ���������ڵ�ֵ�͵糧���й���
			if(treejib2==3){
				sql = 	"select g.id, g.mingc\n" +
				"  from gongysb g, gongysdcglb gl, diancxxb dc\n" + 
				" where gl.gongysb_id = g.id\n" + 
				"   and gl.diancxxb_id = dc.id\n" + 
				"   and dc.id = "+getTreeid()+"";
				
			}else if(treejib2==2){
				sql = 	"select g.id, g.mingc\n" +
				"  from gongysb g, gongysdcglb gl, diancxxb dc\n" + 
				" where gl.gongysb_id = g.id\n" + 
				"   and gl.diancxxb_id = dc.id\n" + 
				"   and dc.fuid = "+getTreeid()+" or dc.id="+getTreeid()+"";
			}else if(treejib2==1){
				sql=
					"select g.id, g.mingc\n" +
					"  from gongysb g, gongysdcglb gl, diancxxb dc\n" + 
					" where gl.gongysb_id = g.id\n" + 
					"   and gl.diancxxb_id = dc.id\n";
				
			}
		
			_IMeikdqmcModel = new IDropDownModel(sql,"��ѡ��");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
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
