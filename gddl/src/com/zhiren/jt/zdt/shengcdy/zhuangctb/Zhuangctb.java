package com.zhiren.jt.zdt.shengcdy.zhuangctb;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-30
 * �������޸Ŀ�Ԥ��������λ���������Ϊú�����
 */
public class Zhuangctb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
//	���ڿؼ�
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else {
			return value;
		}
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
//		����ģ�����ƣ���visit.getExtGrid1().Save(getChange(), visit)���ã������޸Ļ�ɾ������ʱ�����־
		visit.getExtGrid1().setMokmc(SysConstant.RizOpMokm_Kuangfyb);
		int flag = visit.getExtGrid1().Save(getChange(), visit);
		if(flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
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
		String riqTiaoj=this.getRiqi();
		
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			if(visit.isFencb()){
				str = " and (dc.id = " + getTreeid() + " or dc.fuid="+getTreeid()+")";
			}else{
				str = "and dc.id = " + getTreeid() + "";
			}
			
		}
		ResultSetList rsl = con
				.getResultSetList("  select zc.id as id,dc.mingc as diancxxb_id, zc.riq as riq,gy.mingc as gongysb_id,cz.mingc as chezxxb_id,zc.zuorzc,zc.jinrzc,zc.jinrcr,zc.mingrqc,zc.mingrsd,zc.beiz " +
						"				from zhuangcyb zc,gongysb gy,chezxxb cz,diancxxb dc where zc.gongysb_id=gy.id and zc.diancxxb_id=dc.id and zc.chezxxb_id=cz.id" +
						" and riq =  to_date('"+riqTiaoj+"','yyyy-mm-dd') "+str);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("zhuangcyb");
		// /������ʾ������
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("�糧����");
				
		IDropDownModel dc;
		if (treejib == 1) {
			dc =  new IDropDownModel("select id,mingc from diancxxb where jib=3 order by id,mingc");
		} else if (treejib == 2) {
			dc =  new IDropDownModel("select id,mingc from diancxxb where fuid=" + getTreeid() + " order by id,mingc");
		} else {
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			if(visit.isFencb()){
				dc = new IDropDownModel("select id,mingc from diancxxb where id = "+getTreeid()+" or fuid="+getTreeid()+" order by id,mingc");
			}else{
				dc = new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by id,mingc");
			}
		}
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,dc);
		egu.getColumn("diancxxb_id").setReturnId(true);
		egu.getColumn("diancxxb_id").setDefaultValue(dc.getBeanValue(getTreeid()));

		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		
		egu.getColumn("gongysb_id").setHeader("������λ����");
		ComboBox gonghmc =new ComboBox();
		gonghmc.setEditable(true);
		egu.getColumn("gongysb_id").setEditor(gonghmc);
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from gongysb where leix = 1 order by xuh"));
		
		egu.getColumn("chezxxb_id").setHeader("��վ����");
		ComboBox fazmc =new ComboBox();
		fazmc.setEditable(true);
		egu.getColumn("chezxxb_id").setEditor(fazmc);
		egu.getColumn("chezxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb"));
		
		egu.getColumn("zuorzc").setHeader("����װ��");
		egu.getColumn("zuorzc").setDefaultValue("0");
		egu.getColumn("jinrzc").setHeader("����װ��");
		egu.getColumn("jinrzc").setDefaultValue("0");
		egu.getColumn("jinrcr").setHeader("���ճ���");
		egu.getColumn("jinrcr").setDefaultValue("0");
		egu.getColumn("mingrqc").setHeader("�����복");
		egu.getColumn("mingrqc").setDefaultValue("0");
		egu.getColumn("mingrsd").setHeader("����ʵ��");
		egu.getColumn("mingrsd").setDefaultValue("0");
		egu.getColumn("beiz").setHeader("��ע");
//		// //�����п��
		egu.getColumn("diancxxb_id").setWidth(120);
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("chezxxb_id").setWidth(90);
		egu.getColumn("zuorzc").setWidth(90);
		egu.getColumn("jinrzc").setWidth(90);
		egu.getColumn("jinrcr").setWidth(90);
		egu.getColumn("mingrqc").setWidth(90);
		egu.getColumn("mingrsd").setWidth(90);
		egu.getColumn("beiz").setWidth(90);
		
//		// //���õ�ǰ���Ƿ�༭
//		egu.getColumn("piny").setEditor(null);
		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		// /���ð�ť
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		/*
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Bumreport&lx=rezc';" +
   	    " window.open(url,'newWin');";
   	   
	egu.addToolbarItem("{"+new GridButton("��ӡ","function (){"+str+"}").getScript()+"}");
	 */
		setExtGrid(egu);
		con.Close();
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
		if (treeid.equals("")) {

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
			visit.setList1(null);
			getSelectData();
		}
	}
}
