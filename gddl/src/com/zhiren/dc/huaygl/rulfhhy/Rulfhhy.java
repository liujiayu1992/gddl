package com.zhiren.dc.huaygl.rulfhhy;

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
/*2009-11-27
*����ƽ
*����������¯�ɻһ���ҳ��¼����Ա��Ϊ������
*/

/*2009-11-17
*����ƽ
*��������ҳ�湤������Ӳ���ʱ��͵糧����
*1������¯�ɻ�ҳ��������ɰ�ť����xitxxb���������Ƿ���ʾ��
*2����xitxxb��������ҳ���Ƿ���޸ģ�����ʱ����״̬Ϊ1������xitxxb��������ʾ״̬Ϊ1�����ݣ���
*˵��������ʱ������ѡ�糧��rulbzb��fenxxmb��jizb�е����ݹ������ɣ����������ð���ͻ���ʱ
*һ��������Ҫ���÷ֳ������ݡ�
*��xitxxb���е����ã�insert into xitxxb (ID, XUH, DIANCXXB_ID, MINGC, ZHI, DANW, LEIB, ZHUANGT, BEIZ)
*values (20118886001, 1, 229, '��ʾ��¯�ɻһ���״̬Ϊ1��ֵ', '��', '1', '��¯�ɻһ���', 1, 'ʹ��');
*insert into xitxxb (ID, XUH, DIANCXXB_ID, MINGC, ZHI, DANW, LEIB, ZHUANGT, BEIZ)
*values (20118886002, 1, 229, '�ɻһ����Ƿ���ʾ���ɰ�ť', '��', '1', '��¯�ɻһ���', 1, 'ʹ��');
*/

public class Rulfhhy extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
//	boolean riqichange = false;
//
//	private String riqi;
//
//	public String getRiqi() {
//		if (riqi == null || riqi.equals("")) {
//			riqi = DateUtil.FormatDate(new Date());
//		}
//		return riqi;
//	}
//
//	public void setRiqi(String riqi) {
//
//		if (this.riqi != null && !this.riqi.equals(riqi)) {
//			this.riqi = riqi;
//			riqichange = true;
//		}
//
//	}

//	boolean riq2change = false;
//
//	private String riq2;
//
//	public String getRiq2() {
//		if (riq2 == null || riq2.equals("")) {
//			riq2 = DateUtil.FormatDate(new Date());
//		}
//		return riq2;
//	}
//
//	public void setRiq2(String riq2) {
//
//		if (this.riq2 != null && !this.riq2.equals(riq2)) {
//			this.riq2 = riq2;
//			riq2change = true;
//		}
//
//	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	public void Save1(String strchange, Visit visit) {
		String tableName = "rulfhhyb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();

			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");

				for (int i = 1; i < mdrsl.getColumnCount(); i++) {

					sql.append(",").append(mdrsl.getColumnNames()[i]);
					if (mdrsl.getColumnNames()[i].equals("JIZB_ID")) {
						sql2.append(",").append(
								(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]).combo)
										.getBeanId(mdrsl.getString("jizb_id")));
					} else if (mdrsl.getColumnNames()[i].equals("FENXXMB_ID")) {
						sql2.append(",").append(
								(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]).combo)
										.getBeanId(mdrsl
												.getString("fenxxmb_id")));
					} else if (mdrsl.getColumnNames()[i].equals("SHENHZT")) {
						sql2.append(",").append(1);

					}else {
						sql2.append(",").append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i)));
					}

				}

				sql.append(") values(").append(sql2).append(");\n");
			}else{
				sql.append("update rulfhhyb set caiyrq=to_date('" +mdrsl.getString("caiyrq")+"','yyyy-mm-dd')" +" \n"+
						",fenxrq=to_date('"+mdrsl.getString("fenxrq")+"','yyyy-mm-dd')" +" \n"+
						",caiyly='" +mdrsl.getString("caiyly")+"' \n"+
						",jizb_id=" +(getExtGrid().getColumn("jizb_id").combo).getBeanId(mdrsl.getString("jizb_id"))+" \n"+
						",fenxxmb_id=" +(getExtGrid().getColumn("fenxxmb_id").combo).getBeanId(mdrsl.getString("fenxxmb_id"))+" \n"+ 
						",zhi=" +mdrsl.getString("zhi")+" \n"+
						",lury='" +mdrsl.getString("lury")+"' \n"+
						",beiz='" +mdrsl.getString("beiz")+"' where id = " +mdrsl.getString("id")+
						";\n");
			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
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
		} else if ("float".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;

		}
		if (_DeleteChick) {
			_DeleteChick = false;
		}
		if (_CreateClick) {
			_CreateClick = false;
			Create();
			getSelectData();
		}

	}
	private void Create(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String diancxxb_id = getTreeid();
		String DELsql = "delete from rulfhhyb where caiyrq =to_date('"+getRiqi() + "','yyyy-mm-dd') and diancxxb_id =" + diancxxb_id;
		con.getDelete(DELsql);
		String caiylySql = "select id,mingc from rulbzb b where b.diancxxb_id="
			+ diancxxb_id + "order by xuh";
		String xmSql = "select id,mingc from fenxxmb  where " + "xuh=0  ";
		String jizSql = "select id,jizbh from jizb j where j.diancxxb_id="
			+ diancxxb_id + "order by jizbh";
		
		ResultSetList bzrsl = con.getResultSetList(caiylySql);
		ResultSetList xmrsl = con.getResultSetList(xmSql);
		ResultSetList jzrsl = con.getResultSetList(jizSql);		
		
		StringBuffer insSQL=new StringBuffer();
		insSQL.append("begin \n");
		xmrsl.beforefirst();
		while(xmrsl.next()){
			jzrsl.beforefirst();
			xmrsl.getRow();
			while(jzrsl.next()){
				bzrsl.beforefirst();
				jzrsl.getRow();
				while(bzrsl.next()){
					bzrsl.getRow();
					insSQL.append("insert into rulfhhyb (id,caiyrq,fenxrq,caiyly,jizb_id,fenxxmb_id,zhi,lury,shenhry,beiz,shenhzt,diancxxb_id) values(getnewid("+diancxxb_id+
							"),to_date('"+getRiqi()+
							"','yyyy-mm-dd'),to_date('"+getRiqi()+
							"','yyyy-mm-dd'),'"+bzrsl.getString("mingc")+
							"',"+jzrsl.getString("id")+
							","+xmrsl.getString("id")+
							",0,'"+visit.getRenymc()+
							"','','',1,"+diancxxb_id+");\n"
							);
				}
			}
			
		}
		insSQL.append("end;");
		if(con.getInsert(insSQL.toString())>-1){
			setMsg("�������ݳɹ���");
		}else{
			setMsg("�������ݳ���");
		}		
		xmrsl.close();
		jzrsl.close();
		bzrsl.close();
		con.Close();

		
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql ="";
		String isShow1SQL = "select zhi from xitxxb where mingc ='��ʾ��¯�ɻһ���״̬Ϊ1��ֵ' and zhuangt = 1 and diancxxb_id = "+
	 	visit.getDiancxxb_id();
		ResultSetList isShow1RS = con.getResultSetList(isShow1SQL);
		if(isShow1RS.next()){
			if(isShow1RS.getString("zhi").equals("��")){
				sql = "select r.id,r.diancxxb_id,r.caiyrq,r.fenxrq ,r.caiyly,j.jizbh as jizb_id,f.mingc as fenxxmb_id,"
					+ " r.zhi,r.lury,r.shenhry,r.shenhzt,"
					+ " r.beiz" 
					+ " from rulfhhyb r, jizb j,fenxxmb f where r.jizb_id=j.id and r.fenxxmb_id=f.id "
					+ " and r.caiyrq=to_date('"+getRiqi()+"','yyyy-mm-dd') "
					+ " and r.diancxxb_id="+getTreeid()
					+ " and r.shenhzt=1 order by (select decode(bz.xuh,null,0,bz.xuh) as xuh from rulbzb bz where mingc=r.caiyly and diancxxb_id="+getTreeid()+"),jizb_id";
			}
		}else{		
			sql = "select r.id,r.diancxxb_id,r.caiyrq,r.fenxrq ,r.caiyly,j.jizbh as jizb_id,f.mingc as fenxxmb_id,"
				+ " r.zhi,r.lury,r.shenhry,r.shenhzt,"
				+ " r.beiz"
				+ " from rulfhhyb r, jizb j,fenxxmb f where r.jizb_id=j.id and r.fenxxmb_id=f.id "
				+ " and r.caiyrq=to_date('"+getRiqi()+"','yyyy-mm-dd') "
				+ " and r.diancxxb_id="+getTreeid()
				+ " and r.shenhzt=0 ";				
		}

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("�糧");
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("caiyrq").setHeader("��������");
		egu.getColumn("caiyrq").setDefaultValue(getRiqi());
		egu.getColumn("fenxrq").setHeader("��������");
		egu.getColumn("fenxrq").setDefaultValue(getRiqi());
		egu.getColumn("jizb_id").setHeader("������");
		egu.getColumn("fenxxmb_id").setHeader("������Ŀ");
		egu.getColumn("caiyly").setHeader("������Դ");		
		egu.getColumn("zhi").setHeader("��ֵ");
		egu.getColumn("lury").setHeader("¼����Ա");
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("shenhry").setHidden(true);
		egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("beiz").setHeader("��ע");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(1000);
		//����
		ComboBox jiz = new ComboBox();
		egu.getColumn("jizb_id").setEditor(jiz);
		jiz.setEditable(true);
		String jizSql = "select id,jizbh from jizb j where j.diancxxb_id="
				+ getTreeid() + "order by jizbh";
		egu.getColumn("jizb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jizSql));
		/*lzp
		 * 2009-10-22
		 * ������¯Ҫ����¯�ɻ�¯������Feihdzhyҳ��Ѳ�����Դ���������ΪΪ������Ϣ�������򣬱����á�
		 */
		//������Դ
		String Xitsql = "";
		Xitsql = "select zhi from xitxxb where mingc = '������ԴΪ��¯����������'";
		ResultSetList Xitrsl = con.getResultSetList(Xitsql);
		if (Xitrsl.next()) {
			if (Xitrsl.getString("zhi").equals("��")) {
				ComboBox caiyly = new ComboBox();
				egu.getColumn("caiyly").setEditor(caiyly);
				jiz.setEditable(true);
				String caiylySql = "select id,mingc from rulbzb b where b.diancxxb_id="
						+ getTreeid() + "order by xuh";
				egu.getColumn("caiyly").setComboEditor(egu.gridId,
						new IDropDownModel(caiylySql));
				egu.getColumn("caiyly").returnId=false;
			}
		}
//		������Ŀ		
		ComboBox xm = new ComboBox();
		egu.getColumn("fenxxmb_id").setEditor(xm);
		xm.setEditable(true);
		String xmSql = "select id,mingc from fenxxmb  where " + "xuh=0  ";
		egu.getColumn("fenxxmb_id").setComboEditor(egu.gridId,
				new IDropDownModel(xmSql));
//		¼����Ա
		ComboBox lurry = new ComboBox();
		egu.getColumn("lury").setEditor(lurry);
		xm.setEditable(true);
		String lurrySql = "select id,quanc from renyxxb where bum='��¯' and diancxxb_id="+getTreeid();
		egu.getColumn("lury").setComboEditor(egu.gridId,
				new IDropDownModel(lurrySql));
		egu.getColumn("lury").returnId=false;
		/*  ���ù�����  */
		//��������
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		//�糧tree
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, "");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		// ���ɰ�ť
		String shengc = "select zhi from xitxxb where mingc ='�ɻһ����Ƿ���ʾ���ɰ�ť' and zhuangt = 1 and diancxxb_id = "+
	 	visit.getDiancxxb_id();
		ResultSetList shengcRS = con.getResultSetList(shengc);
		if(shengcRS.next()){
			if(shengcRS.getString("zhi").equals("��")){
				GridButton gbc = new GridButton("����",
						getBtnHandlerScript("CreateButton"));
				
				if (false) {
					gbc
							.setHandler("function (){"
									+ MainGlobal.getExtMessageBox(
											ErrorMessage.DataLocked_Yueslb, false)
									+ "return;}");
				}
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
			}			
		}

		setExtGrid(egu);

		con.Close();
	}
	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����").append(getRiqi()).append("���Ѵ����ݣ��Ƿ������");
		} else {
			btnsb.append("�Ƿ�ɾ��").append(getRiqi()).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			visit.isFencb();
		}
		getSelectData();
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