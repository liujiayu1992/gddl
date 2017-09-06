package com.zhiren.dc.rulgl.meihyb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.LovComboBox;
import com.zhiren.common.ext.form.SelectCombo;
import com.zhiren.dc.diaoygl.AutoCreateShouhcrb;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterFac_dt;


public class Meihypp extends BasePage implements PageValidateListener {
//	�ͻ��˵���Ϣ��
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
//	ҳ���ʼ��(ÿ��ˢ�¶�ִ��)
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	// ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	//����
	public String _miaos;

	public String getMiaos() {
		return _miaos;
	}

	public void setMiaos(String miaos) {
		_miaos = miaos;
	}
//	 ���ڿؼ�
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {

			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private void Save() {
		

		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
	
	

		// ��������
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult+ "Yundlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		if (rsl.getRows() == 0) {
			return;
		}
		
		sb.append("begin\n");
		
		while (rsl.next()) {
			
			if(rsl.getString("shenhzt").equals("3")){
				setMsg("��¯���������,����ȡ�����,�ٱ��棡");
				return;
			}
			
			if (!"".equals(rsl.getString("huaybh"))) {
				 long rulmzlbtmp_id=(getExtGrid().getColumn("huaybh").combo).getBeanId(rsl.getString("huaybh"));
				sb.append("update  rulmzlb  set qnet_ar=").append("(select max(qnet_ar) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("aar=").append("(select max(aar) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("ad=").append("(select max(ad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("vdaf=").append("(select max(vdaf) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("mt=").append("(select max(mt) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("stad=").append("(select max(stad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("aad=").append("(select max(aad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("mad=").append("(select max(mad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("qbad=").append("(select max(qbad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("had=").append("(select max(had) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("vad=").append("(select max(vad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("fcad=").append("(select max(fcad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("std=").append("(select max(std) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("qgrad=").append("(select max(qgrad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("hdaf=").append("(select max(hdaf) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("sdaf=").append("(select max(sdaf) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("var=").append("(select max(var) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("huayy=").append("(select max(huayy) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("lury=").append("'"+visit.getRenymc()+"'").append(",\n");
				sb.append("shenhzt=").append("1").append(",\n");
				sb.append("bianm=").append("'"+rsl.getString("huaybh")+"'").append(",\n");
				sb.append("har=").append("(select max(har) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("qgrd=").append("(select max(qgrd) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("qgrad_daf=").append("(select max(qgrad_daf) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append("\n");
				sb.append("where id=").append(""+rsl.getString("rulmzlb_id")).append(";\n");
				sb.append("update rulmzlbtmp set ispip=1 where id="+rulmzlbtmp_id+";\n");
				sb.append("update meihyb set rulmzlbtmp_id="+rulmzlbtmp_id+" where id="+rsl.getString("id")+";\n");
					
				
			} 
		}
		sb.append("end;");

		if(con.getInsert(sb.toString())>=0){
			
			setMsg("����ɹ�");
		}else{
		setMsg("����ʧ��");
	    }
		
		con.Close();

	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		
	
	
		getSelectData();
	}
	

	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String rulrq = DateUtil.FormatOracleDate(this.getRiqi());	//��¯���� 
		
		String diancxxb_id="";
		if(getDiancTreeJib()==1){
			diancxxb_id = "";
		}else if(getDiancTreeJib()==2){
			diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
		}else if(getDiancTreeJib()==3){
			if(visit.isFencb()){
				diancxxb_id = " and (m.diancxxb_id="+this.getTreeid()+" or fuid="+this.getTreeid()+") ";
			}else{
				diancxxb_id = "and (d.id = " + this.getTreeid() + ")\n";
			}
		}
		
		String chaxun = "select m.id,m.diancxxb_id, m.rulrq,decode(t.ispip,1,'��ƥ��','δƥ��') as ispip,"
			    + "  m.rulmzlb_id, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,r.shenhzt,\n"
				+ "  m.fadhy,m.gongrhy,m.qity,m.feiscy,t.huaybh\n"
				+ "  from meihyb m, diancxxb d, rulmzlb r, rulbzb rb, jizfzb j ,rulmzlbtmp t\n"
				+ " where m.diancxxb_id = d.id(+)\n"
				+ "   and m.rulmzlb_id = r.id(+)\n"
				+ "   and m.rulbzb_id = rb.id(+)\n"
				+ "   and m.jizfzb_id = j.id(+)\n"
				+"    and m.rulmzlbtmp_id=t.id(+)"
				+ "   and m.rulrq = "+ rulrq + "\n"
				+""+diancxxb_id+"\n"
		        +"  order by m.rulrq,rb.xuh,j.xuh";
		//}
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meihyb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("��λ");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setHeader("��������");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("ispip").setHeader("�Ƿ�ƥ��");
		egu.getColumn("ispip").setEditor(null);
		
		egu.getColumn("rulmzlb_id").setHidden(true);
		egu.getColumn("rulmzlb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("��¯����");
		egu.getColumn("jizfzb_id").setHeader("��¯����");
		egu.getColumn("fadhy").setHeader("�������(t)");
		egu.getColumn("fadhy").setEditor(null);
		egu.getColumn("gongrhy").setHeader("���Ⱥ���(t)");
		egu.getColumn("gongrhy").setEditor(null);
		egu.getColumn("qity").setHeader("������(t)");
		egu.getColumn("qity").setEditor(null);
		egu.getColumn("feiscy").setHeader("��������(t)");
		egu.getColumn("feiscy").setEditor(null);
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setWidth(180);
		egu.getColumn("shenhzt").setHeader("��¯�������״̬");
		egu.getColumn("shenhzt").setWidth(50);
		egu.getColumn("shenhzt").setEditor(null);
		egu.getColumn("shenhzt").setHidden(true);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(25);// ���÷�ҳ
		// *****************************************����Ĭ��ֵ****************************
		

		// ������������¯����
		
			egu.getColumn("rulbzb_id").setEditor(null);
			egu.getColumn("jizfzb_id").setEditor(null);
		

		
		ComboBox huaybh = new ComboBox();
		egu.getColumn("huaybh").setEditor(huaybh);
		huaybh.setEditable(true);
		String huaybhSql = "select id, huaybh from rulmzlbtmp  where rulrq>=date'"+this.getRiqi()+"'-3 and  rulrq<=date'"+this.getRiqi()+"'+3  order by huaybh";
		egu.getColumn("huaybh").setComboEditor(egu.gridId,new IDropDownModel(huaybhSql));
		egu.getColumn("huaybh").setReturnId(true);
		

		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//		 �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		// ************************************************************
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		
		//���水ť
	
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		
		
		
		

		
	
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
			visit.setString3("");
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			setJizfzbModel(null);
			setJizfzbModels();
			setRulbzbModel(null);
			setRulbzbModels();
			setRiqi(null);
			setMsg("");
		}

		getSelectData();
	}

	


	public IPropertySelectionModel getRulbzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setRulbzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setRulbzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setRulbzbModels() {
		String sql = "select r.id,r.mingc from rulbzb r";
		setRulbzbModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getJizfzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setJizfzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setJizfzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setJizfzbModels() {
		Visit visit = (Visit) getPage().getVisit();
		String sql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")";
		setJizfzbModel(new IDropDownModel(sql));
	}
	
//	 �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}


	boolean treechange = false;

	private String treeid;

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

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}

}