package com.zhiren.pub.riz;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rizcx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	//����
	private String row;
	public String getRow(){
		return row;
	}
	public void setRow(String Row){
		row = Row;
	}
	//��־����
	private String File;
	public String getFile(){
		return File;
	}
	public void setFile(String File){
		File = File;
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	 ������
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}

	}
	
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //Ϊ  "��ѯ"  ��ť��Ӵ������
    	getSelectData();
    }
    
    private boolean _RiznrChick = false;
    public void RiznrButton(IRequestCycle cycle) {
    	_RiznrChick = true;
    }

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
		if(_RiznrChick){
			_RiznrChick = false;
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

//		�糧Treeˢ������
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
			try {
				ResultSet rsss=con.getResultSet("select id from diancxxb where fuid="+getTreeid());
				if(rsss.next()){
					str = "and (dc.fuid = "+ getTreeid() + "or dc.id = "+ getTreeid() + ")" ;
				}else{
					str = "and dc.id = " + getTreeid() ;
				}
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			
		}
		
		String sql = 
			"select r.id,\n" +
			"       diancxxb_id,\n" + 
			"       to_char(caozsj,'yyyy-MM-dd') as caozsj,\n" + 
			"       caozy,\n" + 
			"       caoz,\n" + 
			"       ip,\n" + 
			"       mokmc,\n" + 
			"       biaom,\n" + 
			"       biaoid,\n" + 
			"       leix,\n" + 
			"       r.beiz,\n" + 
			"		GetNeir(r.id) as neir\n" +
			"from rizb r,diancxxb dc \n" + 
			"where caozsj >= to_date('"+getRiq1()+"', 'yyyy-mm-dd')	\n" + 
            "	and caozsj < to_date('"+getRiq2()+"', 'yyyy-mm-dd') + 1 \n" +
			str + "\n" +
			"   and r.diancxxb_id = dc.id \n" +
			"order by caozsj";
		
		

		ResultSetList rsl = con.getResultSetList(sql);
		row=rsl.getRows()+"";
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rizb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("caozsj").setHeader("����ʱ��");
		egu.getColumn("caozy").setHeader("����Ա");
		egu.getColumn("caoz").setHeader("����");
		egu.getColumn("ip").setHeader("IP");
		egu.getColumn("mokmc").setHeader("ģ������");
		egu.getColumn("biaom").setHeader("����");
		egu.getColumn("biaoid").setHeader("��ID");
		egu.getColumn("leix").setHeader("����");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("neir").setHidden(true);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(25);
	
//**********************************************����ģ��*****************************************************************
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		
		egu.addToolbarItem("{"+new GridButton("��ѯ","function(){\n" +
													 "	var i = document.getElementById('ROW').value;\n" +
													 "	if(i>1000){\n" +
													 "		Ext.Msg.alert('��ʾ��Ϣ','��¼�����࣬�������趨��ѯ������');\n" +
													 "		gridDiv_grid.store.removeAll();\n" +
													 "	}else{\n" +
													 "		document.getElementById('RefurbishButton').click();\n" +
													 "	}\n" +
													 "}" ).getScript()+"}");
		egu.addTbarText("-");
		String str2=
			"   var rec = gridDiv_sm.getSelected(); \n"
	        +"  if(rec==null){\n"
	        +"  	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����¼!'); \n"
	        +"  	return;\n"
	        +"  }else{\n"
	        +"  	sqms.setValue(rec.get('NEIR'));\n"
	        +"		Rpt_window.show();\n"
	        +"  }\n";
	        //+"  document.getElementById('RiznrButton').click(); \n";
        egu.addToolbarItem("{"+new GridButton("��־����","function(own,row,col,e){"+str2+"}").getScript()+"}");
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
			visit.setList1(null);
			visit.setString5("");	//riq1
			visit.setString6("");	//riq2
			visit.setboolean1(true);	//����change
			
		}
		getSelectData();
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
}
