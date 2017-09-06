package com.zhiren.dc.jilgl.jicxx;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2013-03-04
 * ���÷�Χ�������������������λ
 * ������ʹ���º���getNewYSDWId��ȡ���䵥λ��ID
 */
public class Yunsdw extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
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
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
	}
	
	/**
	 * ��������ʱ�ֱ��ж����ƺ�ȫ���Ƿ������ݿ��е������ظ���
     * �ظ��������棬������ʱ��ȫ�Ʊ��浽bianm�ֶ��У���ȫ����Ϊ���䵥λ�ı��롣
	 * @author yinjm
	 */
	public void save() {
		
		JDBCcon con = new JDBCcon();
		String message = "";	// ���ڱ����ظ���������ʾ��Ϣ
		StringBuffer sqlsb = new StringBuffer("begin\n"); 
		ResultSetList mdrsl = ((Visit)this.getPage().getVisit()).getExtGrid1().getModifyResultSet(getChange());
		ResultSetList rsl = new ResultSetList();
		
		while(mdrsl.next()) {
//			�����ݿ��в�ѯ�Ƿ�����Ҫ��������ơ�ȫ����ͬ�ļ�¼��
			String sql = "select y.mingc, y.quanc from yunsdwb y where (y.mingc = '"+ mdrsl.getString("mingc") +"' or y.quanc = '"
				+ mdrsl.getString("quanc") +"') and y.id <> " + mdrsl.getString("id") + " and y.diancxxb_id = " + getTreeid();
			rsl = con.getResultSetList(sql);
			
			if (rsl.getRows() > 0) {
				while(rsl.next()) {
					if (rsl.getString("mingc").equals(mdrsl.getString("mingc"))) {
						message += "���䵥λ���� \""+ mdrsl.getString("mingc") +"\" �Ѿ����ڣ����������룡<br>";
					}  
					if (rsl.getString("quanc").equals(mdrsl.getString("quanc"))) {
						message += "���䵥λȫ�� \""+ mdrsl.getString("quanc") +"\" �Ѿ����ڣ����������룡<br>";
					}
				}
				continue;
			}
			
			if (mdrsl.getString("id").equals("0")) {
				
				sqlsb.append("insert into yunsdwb(id, diancxxb_id, mingc, beiz, quanc, danwdz, youzbm, shuih, faddbr, " +
					"weitdlr, kaihyh, zhangh, dianh, chuanz, bianm) values(getNewYSDWId("+ getTreeid() +"), ").append(getTreeid())
					.append(", '").append(mdrsl.getString("mingc")).append("', '").append(mdrsl.getString("beiz")).append("', '")
					.append(mdrsl.getString("quanc")).append("', '").append(mdrsl.getString("danwdz")).append("', '")
					.append(mdrsl.getString("youzbm")).append("', '").append(mdrsl.getString("shuih")).append("', '")
					.append(mdrsl.getString("faddbr")).append("', '").append(mdrsl.getString("weitdlr")).append("', '")
					.append(mdrsl.getString("kaihyh")).append("', '").append(mdrsl.getString("zhangh")).append("', '")
					.append(mdrsl.getString("dianh")).append("', '").append(mdrsl.getString("chuanz")).append("', '")
					.append(mdrsl.getString("quanc")).append("'); \n");
			} else {
				sqlsb.append("update yunsdwb set mingc = '").append(mdrsl.getString("mingc"))
					.append("', beiz = '").append(mdrsl.getString("beiz")).append("', quanc = '").append(mdrsl.getString("quanc"))
					.append("', danwdz = '").append(mdrsl.getString("danwdz")).append("', youzbm = '").append(mdrsl.getString("youzbm"))
					.append("', shuih = '").append(mdrsl.getString("shuih")).append("', faddbr = '").append(mdrsl.getString("faddbr"))
					.append("', weitdlr = '").append(mdrsl.getString("weitdlr")).append("', kaihyh = '").append(mdrsl.getString("kaihyh"))
					.append("', zhangh = '").append(mdrsl.getString("zhangh")).append("', dianh = '").append(mdrsl.getString("dianh"))
					.append("', chuanz = '").append(mdrsl.getString("chuanz")).append("', bianm = '").append(mdrsl.getString("quanc"))
					.append("' where id = ").append(mdrsl.getString("id")).append("; \n");
			}
		}
		sqlsb.append("end;");
		if (!message.equals("")) {
			setMsg(message);
		}
		if (!sqlsb.toString().equals("begin\nend;")) {
			con.getUpdate(sqlsb.toString());
		}
		rsl.close();
		mdrsl.close();
		con.Close();
	}

	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String sql="select id,diancxxb_id,bianm,mingc,quanc,danwdz,youzbm,shuih,faddbr,weitdlr,kaihyh,zhangh,dianh,chuanz,beiz \n"
			+ " from yunsdwb where diancxxb_id = " +getTreeid()+ "\n"
			+ " order by mingc \n";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�����Ƿ���Ա༭
		egu.setTableName("yunsdwb");
//		if(visit.isFencb()) {
//			ComboBox dc= new ComboBox();
//			egu.getColumn("diancxxb_id").setEditor(dc);
//			dc.setEditable(true);
//			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
//			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
//			egu.getColumn("diancxxb_id").returnId=true;
//			egu.getColumn("diancxxb_id").setWidth(70);
//		}else {
//			egu.getColumn("diancxxb_id").setHidden(true);
//			egu.getColumn("diancxxb_id").editor = null;
//			egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
//		}
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setDefaultValue(""+getTreeid());
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setHidden(true);
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("danwdz").setHeader("��λ��ַ");
		egu.getColumn("youzbm").setHeader("��������");
		egu.getColumn("shuih").setHeader("˰��");
		egu.getColumn("faddbr").setHeader("����������");
		egu.getColumn("weitdlr").setHeader("ί�д�����");
		egu.getColumn("kaihyh").setHeader("��������");
		egu.getColumn("zhangh").setHeader("�˺�");
		egu.getColumn("dianh").setHeader("�绰");
		egu.getColumn("chuanz").setHeader("����");
		egu.getColumn("beiz").setHeader("��ע");
		
		//����Grid����
		egu.addPaging(25);
		
		egu.getColumn("bianm").setWidth(100);
		egu.getColumn("quanc").setWidth(130);
		egu.getColumn("beiz").setWidth(150);
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		String str = 
			"var url = 'http://'+document.location.host+document.location.pathname;\n" +
			"var end = url.indexOf(';');\n" + 
			"url = url.substring(0,end);\n" + 
			"url = url + '?service=page/YunsdwReport&lx="+ getTreeid() +"';\n" + 
			"window.open(url,'newWin');";
		egu.addToolbarItem("{" + new GridButton("��ӡ", "function (){" + str + "}", SysConstant.Btn_Icon_Print).getScript() + "}");
		
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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
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
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid==null||treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
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
			setTreeid(null);
		}
		init();
	}
	private void init() {
		getSelectData();
	}
}
