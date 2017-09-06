package com.zhiren.dc.jilgl.shujcl;

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

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.DateUtil;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;

public class Jihsjdr extends BasePage implements PageValidateListener {

	public static final String YUNSFS_QY = "QY";// ����

	public static final String YUNSFS_HY = "HY";// ����

	public static final String YUNSFS_All = "ALL";// ȫ��

	public String getYunsfs(){
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	public void setYunsfs(String yunsfs){
		((Visit)this.getPage().getVisit()).setString1(yunsfs);
	}

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private IDropDownModel getYunsfsModel(){
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null){
			setYunsfsModel(new IDropDownModel(SysConstant.SQL_yunsfs));
		}
		return (IDropDownModel)((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	private void setYunsfsModel(IDropDownModel value){
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}

	private String Parameters;// ��¼ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	private void save(Visit v,JDBCcon con,String changeid,int intysfs){
		StringBuffer sb = new StringBuffer(
				"insert into cheplsb\n" +
				"(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id,\n" + 
				"jihkjb_id, fahrq, daohrq, caiybh, yunsfsb_id, chec,cheph, maoz, piz,\n" + 
				"biaoz, koud, kous, kouz, sanfsl, jianjfs, chebb_id, yuandz_id,\n" + 
				"yuanshdwb_id,  yuanmkdw, zhongcjjy, daozch, lury, beiz,qingcsj,zhongcsj,\n" + 
				"yunsdwb_id,caiyrq,zhilb_id)\n" + 
				"(select getnewid("+v.getDiancxxb_id()+"),"+v.getDiancxxb_id()+",q.gongys_id,q.meikxxb_id,q.pinz_id\n" + 
				",(select id from chezxxb where mingc =c.faz) faz_id\n" + 
				",(select id from chezxxb where mingc =c.daoz) daoz_id\n" + 
				",q.kouj_id,c.fahrq,c.daohrq,c.caiybh, "+intysfs+",q.chec,c.cheph,c.maoz\n" + 
				",c.piz,c.biaoz, c.koud,c.kous,c.kouz,c.sanfsl,c.jianjfs,c.chebb_id\n" + 
				",(select nvl(max(id),0) from chezxxb where mingc =c.yuandz) yuandz_id\n" + 
				",(select nvl(max(id),"+v.getDiancxxb_id()+") from vwyuanshdw where mingc = c.yuanshdw) yuanshdw_id\n" + 
				",c.yuanmkdw,c.zhongcjjy,c.daozch,c.lury,c.beiz,c.qingcsj\n" + 
				",c.zhongcsj,q.yunsdwb_id,c.caiyrq,q.zhilb_id from chepbtmp c,qicrjhb q\n" + 
				"where c.qicrjhb_id = q.id and c.qicrjhb_id = "+changeid+" and c.yunsfs = '"+
				getYunsfsModel().getBeanValue(intysfs)+"')");
			int flag = con.getInsert(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog
						.writeErrorLog(ErrorMessage.InsertDatabaseFail + "sb:" + sb);
				setMsg(ErrorMessage.Jhdr001);
				return;
			}
			if(MainGlobal.getXitxx_item("����", "�ռƻ����ɲ���", String.valueOf(v.getDiancxxb_id()), "��").equals("��")){
				flag = Jilcz.Updatezlid(con, v.getDiancxxb_id(),
						intysfs, null);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Jhdr002);
					setMsg(ErrorMessage.Jhdr002);
					return;
				}
			}
			flag = Jilcz.INSorUpfahb(con, v.getDiancxxb_id());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr003);
				setMsg(ErrorMessage.Jhdr003);
				return;
			}
			flag = Jilcz.InsChepb(con, v.getDiancxxb_id(),intysfs, 9);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr004);
				setMsg(ErrorMessage.Jhdr004);
				return;
			}
			
	}
	private void Save() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String changeid = getChangeid();
		if (YUNSFS_HY.equals(getYunsfs())) {
			save(v,con,changeid,SysConstant.YUNSFS_HUOY);
		} else if (YUNSFS_QY.equals(getYunsfs())) {
			save(v,con,changeid,SysConstant.YUNSFS_QIY);
		} else {
			save(v,con,changeid,SysConstant.YUNSFS_HUOY);
			save(v,con,changeid,SysConstant.YUNSFS_QIY);
		}
		
		int flag = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select c.* from chepb c,fahb f where f.id = c.fahb_id and c.hedbz=9 and f.diancxxb_id="+v.getDiancxxb_id());
		ResultSetList rsl = con.getResultSetList(sb.toString());
		List fhlist = new ArrayList();
		while (rsl.next()) {
			flag = Jilcz.CountChepbYuns(con, rsl.getString("id"),
					SysConstant.HEDBZ_YJJ);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr005);
				setMsg(ErrorMessage.Jhdr005);
				return;
			}
			Jilcz.addFahid(fhlist, rsl.getString("fahb_id"));
		}
		rsl.close();
		for (int i = 0; i < fhlist.size(); i++) {
			flag = Jilcz.updateFahb(con, (String) fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr006);
				setMsg(ErrorMessage.Jhdr006);
				return;
			}
			flag = Jilcz.updateLieid(con, (String) fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr007);
				setMsg(ErrorMessage.Jhdr007);
				return;
			}
		}
		sb.delete(0, sb.length());
		sb.append("update chepb set hedbz = "+SysConstant.HEDBZ_YJJ+" where id in (")
		.append("select c.id from chepb c,fahb f where f.id = c.fahb_id and c.hedbz=9 and f.diancxxb_id="+v.getDiancxxb_id()+")");
		flag = con.getUpdate(sb.toString());
		if(flag == -1){
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Jhdr008);
			setMsg(ErrorMessage.Jhdr008);
			return;
		}
		
		sb.delete(0, sb.length());
		sb
				.append(
						"update chepbtmp set fahb_id = 1 where fahb_id = 0 and diancxxb_id=")
				.append(v.getDiancxxb_id()).append(
						"and qicrjhb_id = " + changeid);
		flag = con.getUpdate(sb.toString());
		if(flag == -1){
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Jhdr009);
			setMsg(ErrorMessage.Jhdr009);
			return;
		}
		con.commit();
		con.Close();
		Chengbjs.CountChengb(v.getDiancxxb_id(), fhlist);
		setMsg("����ɹ�");
	}

	private boolean _RefurbishChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ChakChick = false;

	public void ChakButton(IRequestCycle cycle) {
		_ChakChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_ChakChick) {
			_ChakChick = false;
			Update(cycle);
		}
	}

	public void getSelectData() {
		String riqTiaoj = this.getRiqi();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		StringBuffer sb1 = new StringBuffer();
		if (YUNSFS_HY.equals(getYunsfs())) {
			sb1.append(" and yunsfs = '").append(
					getYunsfsModel().getBeanValue(SysConstant.YUNSFS_HUOY)).append("' ");
		} else if (YUNSFS_QY.equals(getYunsfs())) {
			sb1.append(" and yunsfs = '").append(
					getYunsfsModel().getBeanValue(SysConstant.YUNSFS_QIY)).append("' ");
		}

		Visit visit = (Visit) getPage().getVisit();
		//visit.setString1(getChange());
		JDBCcon con = new JDBCcon();
		String sql = "select q.mingc as qicrjhb_id,c.diancxxb_id,gongysmc as gongysb_id,meikdwmc as meikxxb_id,faz as faz_id,daoz as daoz_id,pinz as pinzb_id,\n"
				+ "jihkj as jihkjb_id,fahrq,daohrq,caiybh,q.chec,c.qicrjhb_id as ID,\n"
				+ "count(c.id) as ches,sum(biaoz) as biaoz\n"
				+ "from chepbtmp c,qicrjhb q\n"
				+ "where\n"
				+ "daohrq = to_date('"
				+ riqTiaoj
				+ "','yyyy-mm-dd') and\n"
				+ "c.diancxxb_id = "
				+ visit.getDiancxxb_id()
				+ " and fahb_id = 0  "
				+ sb1.toString()
				+ " and q.id = c.qicrjhb_id\n"
				+ "group by c.diancxxb_id,gongysmc, meikdwmc,faz,daoz,pinz,jihkj,fahrq,daohrq,caiybh,q.chec,c.qicrjhb_id,q.mingc order by mingc";
		ResultSetList rs = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rs);

		egu.setTableName("chepbtmp");
		egu.getColumn("diancxxb_id").setHeader("�糧");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader("������λ");
		egu.getColumn("meikxxb_id").setHeader("ú��");
		egu.getColumn("faz_id").setHeader("��վ");
		egu.getColumn("daoz_id").setHeader("��վ");
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("caiybh").setHeader("�������");
		egu.getColumn("chec").setHeader("����");
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("biaoz").setHeader("����");
		egu.getColumn("ID").setHeader("����ID");
		egu.getColumn("ID").setHidden(true);
		egu.getColumn("qicrjhb_id").setHeader("�ռƻ�");

		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());

		// ���ù�Ӧ��������
		ComboBox c8 = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c8);
		c8.setEditable(true);
		String gyssb = "select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(gyssb));
		// ����ú��λ������
		ComboBox c9 = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c9);
		c9.setEditable(true);
		c9
				.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(mksb));
		// ���÷�վ������
		ComboBox c0 = new ComboBox();
		egu.getColumn("faz_id").setEditor(c0);
		c0.setEditable(true);
		String Fazsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(Fazsb));

		ComboBox c1 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c1);
		c1.setEditable(true);
		String Daozsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(Daozsb));

		// ����Ʒ��������
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c2);
		c2.setEditable(true);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		// ���ÿھ�������
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c3);
		c3.setEditable(true);
		String jihkjsb = SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjsb));

		//egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// ֻ�ܵ�ѡ����
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);

		//
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		GridButton bc = new GridButton("����", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id;"
				+ " document.getElementById('SaveButton').click();}}");
		egu.addTbarBtn(bc);
		GridButton gb = new GridButton("�鿴", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id;"
				+ " document.getElementById('ChakButton').click();}}");
		egu.addTbarBtn(gb);
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

	// ҳ���ж�����
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

	private void Update(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString10("Jihsjdr");
		visit.setLong1(Integer.parseInt(getChangeid()));
		cycle.activate("Jihsjdrmx");
	}

	public String getRiqi() {
		if(((Visit)this.getPage().getVisit()).getString2()== null){
			setRiqi(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1,
					DateUtil.AddType_intDay)));
		}
		return ((Visit)this.getPage().getVisit()).getString2();
	}

	public void setRiqi(String riqi) {
		((Visit)this.getPage().getVisit()).setString2(riqi);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if (reportType != null) {
			setYunsfs(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			if(!visit.getActivePageName().equalsIgnoreCase("Jihsjdrmx") 
					&& reportType == null){
				setYunsfs(YUNSFS_All);
				setRiqi(null);
			}
			visit.setActivePageName(this.getPageName().toString());
			setYunsfsModel(null);
			getSelectData();
		}

	}
}