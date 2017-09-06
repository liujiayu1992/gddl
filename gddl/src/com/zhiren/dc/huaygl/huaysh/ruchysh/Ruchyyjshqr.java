package com.zhiren.dc.huaygl.huaysh.ruchysh;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ruchyyjshqr extends BasePage implements PageValidateListener {
	private String msg = "";
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		// TODO Auto-generated method stub
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

	private void Save() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		String strsql = "begin \n";
		while (mdrsl.next()) {
			strsql += "update zhillsb set shenhzt=5 where id =" + mdrsl.getString("id") + ";\n";
			strsql +="insert into HUAYYJSHJLB (id,meikdwmc,daohrq,ches,shenhsj,zhillsb_id,jingz,shenhry," +
					"caozbz,zhilb_id,qnet_ar,std) values (XL_HUAYYJSHJLB_ID.Nextval,'"+mdrsl.getString("meikdw")+"',to_date('"+mdrsl.getString("daohrq")+"','yyyy-mm-dd')," +
					""+mdrsl.getString("ches")+",sysdate,"+mdrsl.getString("id")+","+mdrsl.getDouble("shul")+",'"+visit.getRenymc()+"'," +
				   "'һ�����ȷ��',"+mdrsl.getString("zhilb_id")+","+mdrsl.getDouble("qnet_ar")+","+mdrsl.getDouble("std")+");\n";
		}
		strsql += " end;";
		con.getUpdate(strsql);
		mdrsl.close();
		con.Close();
	}

	private void Huit() {
		String tableName = "zhillsb";
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update ").append(tableName).append(" set shenhzt=4 where id =").append(mdrsl.getString("id"))
			.append(";\n");
			sql.append("insert into HUAYYJSHJLB (id,meikdwmc,daohrq,ches,shenhsj,zhillsb_id,jingz,shenhry," +
					"caozbz,zhilb_id,qnet_ar,std) values (XL_HUAYYJSHJLB_ID.Nextval,'"+mdrsl.getString("meikdw")+"',to_date('"+mdrsl.getString("daohrq")+"','yyyy-mm-dd')," +
					""+mdrsl.getString("ches")+",sysdate,"+mdrsl.getString("id")+","+mdrsl.getDouble("shul")+",'"+visit.getRenymc()+"'," +
				   "'һ�����ȷ�ϻ���',"+mdrsl.getString("zhilb_id")+","+mdrsl.getDouble("qnet_ar")+","+mdrsl.getDouble("std")+");\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Huit();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
	
	
	private void getSelectData(){
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
					str = "and dc.fuid="+ getTreeid() ;
				}else{
					str = "and dc.id = " + getTreeid() ;
				}
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
		}
		
			String sql = "select   l.id,l.zhilb_id,f.daohrq,\n"
				+ "                m.mingc meikdw,\n"
				+ "                cz.mingc faz,\n"
				+ "                p.mingc as pinz,\n"
				+ "                l.huaysj,\n" 
				+"				   l.huaylb,\n"
				+ "                zm.bianm as huaybh,\n"
				+ "                f.ches as ches,\n"
				+ "                f.jingz as shul,\n"
				+ "                l.had,\n"
				+ "                l.qnet_ar,\n"
				+ "                l.aar,\n"
				+ "                l.ad,\n"
				+ "                l.vdaf,\n"
				+ "                l.mt,\n"
				+ "                l.stad,\n"
				+ "                l.aad,\n"
				+ "                l.mad,\n"
				+ "                round_new(100*(l.mt-l.mad)/(100-l.mad),2) as mf,\n"
				+ "                l.qbad,\n"
				+ "                l.vad,\n"
				+ "                l.fcad,\n"
				+ "                l.std,\n"
				+ "                l.qgrad,\n"
				+"                 l.qgrd,\n "
				+ "                l.hdaf,\n"
				+ "                l.qgrad_daf,\n"
				+ "                l.sdaf,\n"
				+ "                l.t1,\n"
				+ "                l.t2,\n"
				+ "                l.t3,\n"
				+ "                l.t4,\n"
				+ "                l.huayy,\n"
				+ "                l.lury,\n"
				+ "                l.beiz\n"
				+ "  from zhilb z,\n"
				+ "       zhillsb l,\n"
				+ "       caiyb c,\n"
				+ "       (select *\n"
				+ "          from zhuanmb\n"
				+ "         where zhuanmlb_id =\n"
				+ "       (select id\n"
				+ "          from zhuanmlb\n"
				+ "         where jib = (select nvl(max(jib), 0) from zhuanmlb))) zm,\n"
				+ "       (select sum(laimsl) as jingz,sum(f.ches) as ches, meikxxb_id, zhilb_id, pinzb_id,faz_id,max(f.daohrq) as daohrq\n"
				+ "          from fahb f, diancxxb dc\n"
				+ "				where f.diancxxb_id = dc.id \n"
				+ str + "\n"
				+ "         group by pinzb_id, zhilb_id, meikxxb_id,faz_id) f,\n"
				+ "       meikxxb m,\n" + "       pinzb p, chezxxb cz\n"
				+ " where f.zhilb_id = z.id(+)\n"
				+ "   and c.zhilb_id = f.zhilb_id\n"
				+ "   and f.meikxxb_id = m.id\n"
				+ "   and f.zhilb_id = l.zhilb_id\n"
				+ "   and f.pinzb_id = p.id\n"
				+ "   and f.faz_id=cz.id\n"
				+ "   and zm.zhillsb_id = l.id\n"
				+ "   and l.shenhzt = 9\n"
				+ " order by l.zhilb_id,l.id";
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,"Ruchyyjshqr");
		// //���ñ��������ڱ���
		egu.setTableName("zhillsb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// /������ʾ������
		
		
		egu.getColumn("zhilb_id").setHeader("zhilb_id");
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("zhilb_id").setHidden(true);
		
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("daohrq").setHidden(true);
		egu.getColumn("daohrq").setWidth(70);
		

		egu.getColumn("meikdw").setHeader("ú��λ");
		egu.getColumn("meikdw").setEditor(null);
		egu.getColumn("meikdw").setWidth(60);
		
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("faz").setWidth(60);
		
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setWidth(50);
		
		egu.getColumn("shul").setHeader("����(��)");
		egu.getColumn("shul").setEditor(null);
		egu.getColumn("shul").setHidden(true);
		egu.getColumn("shul").setWidth(70);
		
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaybh").setWidth(80);
		
		egu.getColumn("huaylb").setHeader("�������");
		egu.getColumn("huaylb").setEditor(null);
		egu.getColumn("huaylb").setWidth(80);
		
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("huaysj").setWidth(80);
		
		egu.getColumn("had").setHeader("�����������<br>Had(%)");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("had").setWidth(80);
		
		egu.getColumn("qnet_ar").setHeader("�յ���<br>��λ����<br>Qnet,ar(Mj/kg)");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("qnet_ar").setWidth(80);
		
		egu.getColumn("aar").setHeader("�յ���<br>�ҷ�<br>Aar(%)");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("shul").setHidden(true);
		
		egu.getColumn("ad").setHeader("�����<br>�ҷ�<br>Ad(%)");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("ad").setWidth(80);
		
		egu.getColumn("vdaf").setHeader("�����޻һ�<br>�ӷ���<br>Vdaf(%)");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("vdaf").setWidth(80);
		
		egu.getColumn("mt").setHeader("ȫˮ��<br>Mt(%)");
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("mt").setWidth(70);
		
		egu.getColumn("stad").setHeader("���������<br>ȫ��<br>St,ad(%)");
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("stad").setWidth(80);
		
		egu.getColumn("aad").setHeader("���������<br>�ҷ�<br>Aad(%)");
		egu.getColumn("aad").setEditor(null);
		egu.getColumn("aad").setWidth(80);
		
		egu.getColumn("mad").setHeader("���������<br>ˮ��<br>Mad(%)");
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("mad").setWidth(80);
		
		egu.getColumn("mf").setHeader("��ˮ<br>Mf(%)");
		egu.getColumn("mf").setEditor(null);
		egu.getColumn("mf").setHidden(true);
		
		egu.getColumn("qbad").setHeader("���������<br>��Ͳ��ֵ<br>Qb,ad(Mj/kg)");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("qbad").setWidth(80);
		
		egu.getColumn("vad").setHeader("���������<br>�ӷ���<br>Vad(%)");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("vad").setWidth(80);
		
		egu.getColumn("fcad").setHeader("�̶�̼<br>FCad(%)");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("fcad").setWidth(80);
		
		egu.getColumn("std").setHeader("�����<br>ȫ��<br>St,d(%)");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("std").setWidth(80);
		
		egu.getColumn("qgrad").setHeader("���������<br>��λ��ֵ<br>Qgr,ad(Mj/kg)");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("qgrad").setWidth(80);
		
		egu.getColumn("qgrd").setHeader("�����<br>��λ��ֵ<br>Qgr,d(Mj/kg)");
		egu.getColumn("qgrd").setEditor(null);
		egu.getColumn("qgrd").setWidth(80);
		
		egu.getColumn("hdaf").setHeader("�����޻һ�<br>��<br>Hdaf(%)");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("hdaf").setWidth(80);
		
		egu.getColumn("qgrad_daf").setHeader("�����޻һ�<br>��λ��ֵ<br>Qgr,daf(Mj/kg)");
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("qgrad_daf").setEditor(null);
		
		
		egu.getColumn("sdaf").setHeader("�����޻һ�<br>ȫ��<br>Sdaf(%)");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("sdaf").setWidth(80);
		
		egu.getColumn("t1").setHeader("T1(��)");
		egu.getColumn("t1").setEditor(null);
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t1").setHidden(true);
		
		egu.getColumn("t2").setHeader("T2(��)");
		egu.getColumn("t2").setEditor(null);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t2").setHidden(true);
		
		egu.getColumn("t3").setHeader("T3(��)");
		egu.getColumn("t3").setEditor(null);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t3").setHidden(true);
		
		egu.getColumn("t4").setHeader("T4(��)");
		egu.getColumn("t4").setEditor(null);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("t4").setHidden(true);
		
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("huayy").setWidth(80);
		
		egu.getColumn("lury").setHeader("����¼��Ա");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lury").setWidth(80);
		
		egu.getColumn("beiz").setHeader("���鱸ע");
		egu.getColumn("beiz").setEditor(null);
		egu.getColumn("beiz").setWidth(80);
		
		egu.addPaging(0);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		 
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);

		egu.addToolbarButton("ȷ��", GridButton.ButtonType_SubmitSel, "SaveButton",null,SysConstant.Btn_Icon_SelSubmit);
		
		egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel, "HuitButton",null,SysConstant.Btn_Icon_Return);
		
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid("" + visit.getDiancxxb_id());
		}
		getSelectData();
	}
}