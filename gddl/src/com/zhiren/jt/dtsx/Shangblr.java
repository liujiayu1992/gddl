package com.zhiren.jt.dtsx;

import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
//import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author huochaoyuan
 * �����ֹ�˾�ձ�������
 *�½����չ����������(Shangblr )
 *�˹��ֿ�¼����ú��Ϣ����ԭ��¼����棬������Ϣ��Ҫ¼�롰�ⱨ������(fahb.biaoz)��
 *���ⱨ������(fahb.chec)������(fahb.jingz)������(fahb.ches)��
 *��ע(fahb.beiz)Ĭ�Ϻ�̨���롰�˹��ϱ�����fahb���ݽ�������
 *Ϊ�����桱��ť����޸�ʱ�޿��ƣ�
 */
public class Shangblr extends BasePage implements PageValidateListener {
	// �����û���ʾ
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

	// ������
	private String Riq;

	public String getRiq() {
		return Riq;
	}

	public void setRiq(String riq) {
		this.Riq = riq;
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
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Yundlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		StringBuffer sb = new StringBuffer();
		// ���뷢����
		sb.append("begin\n");
		double biaoz = 0.0;
		int ches = 0;
		ches = rsl.getRows();
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sb.append("delete from ").append("fahb").append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		while (rsl.next()) {
			long diancxxb_id = 0;
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
						.getBeanId(rsl.getString("diancxxb_id"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			if(rsl.getString("meikxxb_id").startsWith("*")){
				con.commit();
				con.Close();
				setMsg("ú��λѡ��������������д!");
				return;
			}
			biaoz += rsl.getDouble("biaoz");
			if ("0".equals(rsl.getString("ID"))) {
			sb
					.append("insert into fahb\n")
					.append(
							"(id,diancxxb_id,chec,biaoz,gongysb_id,meikxxb_id,faz_id,pinzb_id,")
					.append(
							"jihkjb_id,fahrq,daohrq,yunsfsb_id,maoz,piz,jingz,yingd,yingk,")
					.append(
							"yuns,yunsl,koud,kous,kouz,koum,zongkd,sanfsl,ches,daoz_id,yuandz_id,beiz) ")
					// .append("(id, diancxxb_id, gongysb_id, meikxxb_id,
					// pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq,
					// yunsfsb_id, chec,cheph, biaoz, chebb_id, yuandz_id,
					// yuanshdwb_id, yuanmkdw, daozch, lury, beiz,caiyrq)\n")
					.append("values (getnewid(").append(diancxxb_id).append(
							"),").append(diancxxb_id).append(",").append(
							rsl.getString("chec")).append(",").append(
							rsl.getString("biaoz")).append(",").append(
							((IDropDownModel) getGongysModel()).getBeanId(rsl
									.getString("gongysb_id"))).append(",")
					.append(
							((IDropDownModel) getMeikModel()).getBeanId(rsl
									.getString("meikxxb_id"))).append(",")
					.append(
							((IDropDownModel) getChezModel()).getBeanId(rsl
									.getString("faz_id"))).append(",").append(
							(getExtGrid().getColumn("pinzb_id").combo)
									.getBeanId(rsl.getString("pinzb_id")))
					.append(",").append(
							(getExtGrid().getColumn("jihkjb_id").combo)
									.getBeanId(rsl.getString("jihkjb_id")))
					.append(",").append(
							DateUtil.FormatOracleDate(rsl.getString("fahrq")))
					.append(",").append(
							DateUtil.FormatOracleDate(getRiq()))
					.append(",").append(
							(getExtGrid().getColumn("yunsfsb_id").combo)
									.getBeanId(rsl.getString("yunsfsb_id")))
					.append(",").append(rsl.getString("maoz")).append(",")
					.append(rsl.getString("piz")).append(",").append(
							rsl.getString("jingz")).append(",").append(
							rsl.getString("yingd")).append(",").append(
							rsl.getString("yingk")).append(",").append(
							rsl.getString("yuns")).append(",").append(
							rsl.getString("yunsl")).append(",").append(
							rsl.getString("koud")).append(",").append(
							rsl.getString("kous")).append(",").append(
							rsl.getString("kouz")).append(",").append(
							rsl.getString("koum")).append(",").append(
							rsl.getString("zongkd")).append(",").append(
							rsl.getString("sanfsl")).append(",").append(
							rsl.getString("ches")).append(",").append(
							(getExtGrid().getColumn("daoz_id").combo)
									.getBeanId(rsl.getString("daoz_id")))
					.append(",").append(
							(getExtGrid().getColumn("yuandz_id").combo)
									.getBeanId(rsl.getString("yuandz_id")))
					.append(",'").append(rsl.getString("beiz")).append("'")
					.append(");\n");
		}else {sb.append("update ").append(" fahb ").append(" set ");
		for (int i = 1; i < rsl.getColumnCount(); i++) {
			sb.append(rsl.getColumnNames()[i]).append(" = ");
			sb.append(
					getExtGrid().getValueSql(getExtGrid().getColumn(rsl.getColumnNames()[i]),
							rsl.getString(i))).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" where id =").append(rsl.getString("ID")).append(
				";\n");}
			}
		sb.append("end;");
		int flag = con.getUpdate(sb.toString());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			setMsg(ErrorMessage.Yundlr001);
			return;
		}
		flag = Jilcz.Updatezlid(con, visit.getDiancxxb_id(),
				SysConstant.YUNSFS_HUOY, null);
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Yundlr002);
			setMsg(ErrorMessage.Yundlr002);
			return;
		}
		flag = Jilcz.INSorUpfahb(con, visit.getDiancxxb_id());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Yundlr003);
			setMsg(ErrorMessage.Yundlr003);
			return;
		}
		flag = Jilcz.InsChepb(con, visit.getDiancxxb_id(),
				SysConstant.YUNSFS_HUOY, SysConstant.HEDBZ_TJ);
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Yundlr004);
			setMsg(ErrorMessage.Yundlr004);
			return;
		}
		sb.delete(0, sb.length());
		con.commit();
		con.Close();
		setMsg("�������� " + ches + " ������Ϣ,����Ʊ�� " + biaoz + " �֡�");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData(null);
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYueData();
		}
	}

	public static Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public static Date getDate(String strDate) {
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1,
					Integer.parseInt(date[2]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return ca.getTime();
	}

	// ����ͬ��
	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		Date Yesterday = getYesterday(DateUtil.getDate(getRiq()));// �õ��¸���ǰ���ڵ�ǰһ��
		String riq = DateUtil.FormatDate(Yesterday);// �õ���ǰ����

		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
					+ ")";

		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and d.id = " + getTreeid() + "";

		}
		StringBuffer copyData = new StringBuffer();

		copyData
				.append(
						"select f.id,d.mingc as diancxxb_id,g.mingc as gongysb_id, mk.mingc meikxxb_id,\n")
				.append(
						" (select cz.mingc from chezxxb cz where f.faz_id=cz.id) as faz_id,\n")
				.append(" pz.mingc as pinzb_id,jh.mingc as jihkjb_id,\n")
				.append(
						" ys.mingc as yunsfsb_id,f.chec,f.biaoz,f.maoz,f.piz,f.jingz,f.yingd,f.yingk,\n")
				.append(
						" f.yuns,f.yunsl,f.koud,f.kous,f.kouz,f.koum,f.zongkd,f.sanfsl,f.ches,\n")
				.append(
						" to_char(f.fahrq,'yyyy-mm-dd') as fahrq,to_char(f.daohrq,'yyyy-mm-dd') as daohrq,\n")
				.append(
						" (select cz.mingc from chezxxb cz where f.daoz_id=cz.id) as daoz_id,\n")
				.append(
						" (select cz.mingc from chezxxb cz where f.yuandz_id=cz.id) as yuandz_id,d.mingc as yuanshdwb_id,f.beiz\n")
				.append(
						" from fahb f,diancxxb d,gongysb g,meikxxb mk,pinzb pz,jihkjb jh,yunsfsb ys \n")
				.append(
						" where f.diancxxb_id=d.id and f.gongysb_id=g.id and f.meikxxb_id=mk.id and f.pinzb_id=pz.id\n")
				.append(" and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id\n")
				.append(str).append("\n").append(
						" and to_char(f.daohrq,'yyyy-mm-dd')='").append(riq)
				.append("'");

		// System.out.println("����ͬ�ڵ�����:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(copyData.toString());
		while (rslcopy.next()) {

			long diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
					.getBeanId(rslcopy.getString("diancxxb_id"));
			// String fahrq=rslcopy.getString("fahrq");
			// String daohrq=rslcopy.getString("daohrq");

			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit())
					.getDiancxxb_id());

			StringBuffer sb = new StringBuffer();
			sb
					.append("insert into fahb\n")
					.append(
							"(id,diancxxb_id,chec,biaoz,gongysb_id,meikxxb_id,faz_id,pinzb_id,")
					.append(
							"jihkjb_id,fahrq,daohrq,yunsfsb_id,maoz,piz,jingz,yingd,yingk,")
					.append(
							"yuns,yunsl,koud,kous,kouz,koum,zongkd,sanfsl,ches,daoz_id,yuandz_id,beiz) ")
					// .append("(id, diancxxb_id, gongysb_id, meikxxb_id,
					// pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq,
					// yunsfsb_id, chec,cheph, biaoz, chebb_id, yuandz_id,
					// yuanshdwb_id, yuanmkdw, daozch, lury, beiz,caiyrq)\n")
					.append("values (")
					.append(_id)
					.append(",")
					.append(diancxxb_id)
					.append(",")
					.append(rslcopy.getString("chec"))
					.append(",")
					.append(rslcopy.getString("biaoz"))
					.append(",")
					.append(
							((IDropDownModel) getGongysModel())
									.getBeanId(rslcopy.getString("gongysb_id")))
					.append(",")
					.append(
							((IDropDownModel) getMeikModel()).getBeanId(rslcopy
									.getString("meikxxb_id")))
					.append(",")
					.append(
							((IDropDownModel) getChezModel()).getBeanId(rslcopy
									.getString("faz_id")))
					.append(",")
					.append(
							(getExtGrid().getColumn("pinzb_id").combo)
									.getBeanId(rslcopy.getString("pinzb_id")))
					.append(",")
					.append(
							(getExtGrid().getColumn("jihkjb_id").combo)
									.getBeanId(rslcopy.getString("jihkjb_id")))
					.append(",")
					.append(DateUtil.FormatOracleDate(getRiq()))
					.append(",")
					.append(DateUtil.FormatOracleDate(getRiq()))
					.append(",")
					.append(
							(getExtGrid().getColumn("yunsfsb_id").combo)
									.getBeanId(rslcopy.getString("yunsfsb_id")))
					.append(",").append(rslcopy.getString("maoz")).append(",")
					.append(rslcopy.getString("piz")).append(",").append(
							rslcopy.getString("jingz")).append(",").append(
							rslcopy.getString("yingd")).append(",").append(
							rslcopy.getString("yingk")).append(",").append(
							rslcopy.getString("yuns")).append(",").append(
							rslcopy.getString("yunsl")).append(",").append(
							rslcopy.getString("koud")).append(",").append(
							rslcopy.getString("kous")).append(",").append(
							rslcopy.getString("kouz")).append(",").append(
							rslcopy.getString("koum")).append(",").append(
							rslcopy.getString("zongkd")).append(",").append(
							rslcopy.getString("sanfsl")).append(",").append(
							rslcopy.getString("ches")).append(",").append(
							(getExtGrid().getColumn("daoz_id").combo)
									.getBeanId(rslcopy.getString("daoz_id")))
					.append(",").append(
							(getExtGrid().getColumn("yuandz_id").combo)
									.getBeanId(rslcopy.getString("yuandz_id")))
					.append(",'").append(rslcopy.getString("beiz")).append("'")
					.append(")\n");
			con.getInsert(sb.toString());

		}
		getSelectData(null);
		con.Close();
	}

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();

		// ----------�糧��--------------
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
					+ ")";

		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and d.id = " + getTreeid() + "";

		}

		String riq1 = getRiq();// �鿴

		if (rsl == null) {
			sb
					.append(
							"select f.id,d.mingc as diancxxb_id,g.mingc as gongysb_id, mk.mingc meikxxb_id,\n")
					.append(
							" (select cz.mingc from chezxxb cz where f.faz_id=cz.id) as faz_id,\n")
					.append(" pz.mingc as pinzb_id,jh.mingc as jihkjb_id,\n")
					.append(
							" ys.mingc as yunsfsb_id,f.chec,f.biaoz,f.maoz,f.piz,f.jingz,f.yingd,f.kouz,round((f.yingd-f.koud),2) as yingk,\n")//f.yingk
					.append(
							" f.yuns,f.yunsl,f.koud,f.kous,f.koum,f.zongkd,f.sanfsl,f.ches,f.fahrq,f.daohrq,\n")
					.append(
							" (select cz.mingc from chezxxb cz where f.daoz_id=cz.id) as daoz_id,\n")
					.append(
							" (select cz.mingc from chezxxb cz where f.yuandz_id=cz.id) as yuandz_id,d.mingc as yuanshdwb_id,f.beiz\n")
					.append(
							" from fahb f,diancxxb d,gongysb g,meikxxb mk,pinzb pz,jihkjb jh,yunsfsb ys \n")
					.append(
							" where f.diancxxb_id=d.id and f.gongysb_id=g.id and f.meikxxb_id=mk.id and f.pinzb_id=pz.id\n")
					.append(" and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id\n")
					.append(str).append("\n").append(
							" and to_char(f.daohrq,'yyyy-mm-dd')='").append(
							riq1).append("'");

			rsl = con.getResultSetList(sb.toString());
		}

		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("fahb");
		// ����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// ����ҳ��߶�
		// egu.setHeight(570);
		// ����ÿҳ��ʾ����
		egu.addPaging(25);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		// *****************************************����Ĭ��ֵ****************************
		// �糧������
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu
					.getColumn("diancxxb_id")
					.setComboEditor(
							egu.gridId,
							new IDropDownModel(
									"select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where fuid="
									+ getTreeid() + " order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where id="
									+ getTreeid() + " order by mingc"));
			ResultSetList r = con
					.getResultSetList("select id,mingc from diancxxb where id="
							+ getTreeid() + " order by mingc");
			String mingc = "";
			if (r.next()) {
				mingc = r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);

		}

		// ���õ糧Ĭ�ϵ�վ
		egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());

		egu.getColumn("chec").setHeader("�ⱨ����");
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("chec").setDefaultValue("0");
		egu.getColumn("chec").editor
				.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 4); }");
		egu.getColumn("biaoz").setHeader("�ⱨ��");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setDefaultValue("0");
		StringBuffer lins = new StringBuffer();
		lins
				.append("specialkey:function(own,e){ \n")
				.append("if(row+1 == gridDiv_grid.getStore().getCount()){ \n")
				.append("Ext.MessageBox.alert('��ʾ��Ϣ','�ѵ�������ĩβ��');return; \n")
				.append("} \n")
				.append("row = row+1; \n")
				.append(
						"last = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("gridDiv_grid.getSelectionModel().selectRow(row); \n")
				.append(
						"cur = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("copylastrec(last,cur); \n").append(
						"gridDiv_grid.startEditing(row , 3); }");

		egu.getColumn("biaoz").editor.setListeners(lins.toString());
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader("��������");//����
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").setDefaultValue("0");
		egu.getColumn("maoz").setHidden(true);
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setWidth(65);
		egu.getColumn("piz").setDefaultValue("0");
		egu.getColumn("piz").setHidden(true);
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("jingz").setWidth(65);
		egu.getColumn("yingd").setHeader("ӯ��");
		egu.getColumn("yingd").setWidth(65);
		egu.getColumn("yingd").setDefaultValue("0");
		egu.getColumn("yingd").setHidden(true);
		egu.getColumn("yingk").setHeader("ӯ��");//����ӯ�֣��۶�
		egu.getColumn("yingk").setWidth(65);
		egu.getColumn("yingk").setDefaultValue("0");
		egu.getColumn("yingk").setHidden(true);
		egu.getColumn("yuns").setHeader("����");
		egu.getColumn("yuns").setWidth(65);
		egu.getColumn("yuns").setDefaultValue("0");
		egu.getColumn("yuns").setHidden(true);
		egu.getColumn("yunsl").setHeader("������");//Ĭ����0.012
		egu.getColumn("yunsl").setWidth(65);
		egu.getColumn("yunsl").setDefaultValue("0.012");
		egu.getColumn("yunsl").setHidden(true);
		egu.getColumn("koud").setHeader("�۶�");
		egu.getColumn("koud").setWidth(65);
		egu.getColumn("koud").setDefaultValue("0");
		egu.getColumn("koud").setHidden(true);
		egu.getColumn("kous").setHeader("����");//����
		egu.getColumn("kous").setWidth(65);
		egu.getColumn("kous").setDefaultValue("0");
		egu.getColumn("kous").setHidden(true);
		egu.getColumn("kouz").setHeader("����");//����
		egu.getColumn("kouz").setWidth(65);
		egu.getColumn("kouz").setHidden(true);
		egu.getColumn("kouz").setDefaultValue("0");
		egu.getColumn("koum").setHeader("��ë");//����
		egu.getColumn("koum").setWidth(65);
		egu.getColumn("koum").setHidden(true);
		egu.getColumn("koum").setDefaultValue("0");
		egu.getColumn("zongkd").setHeader("�ܿ���");//����
		egu.getColumn("zongkd").setWidth(65);
		egu.getColumn("zongkd").setHidden(true);
		egu.getColumn("zongkd").setDefaultValue("0");
		egu.getColumn("sanfsl").setHeader("������");//����
		egu.getColumn("sanfsl").setWidth(65);
		egu.getColumn("sanfsl").setHidden(true);
		egu.getColumn("sanfsl").setDefaultValue("0");
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(65);
		egu.getColumn("daoz_id").setHeader("��վ");
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("daoz_id").setDefaultValue("��ʱ");
		egu.getColumn("daoz_id").setHidden(true);
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("yuandz_id").setHeader("ԭ��վ");
		egu.getColumn("yuandz_id").setWidth(65);
		egu.getColumn("yuandz_id").setDefaultValue("��ʱ");
		egu.getColumn("yuandz_id").setHidden(true);
		egu.getColumn("yuandz_id").setEditor(null);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(90);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setDefaultValue("�˹��ϱ�");
		egu.getColumn("beiz").setHidden(true);

		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		// ���÷������ں͵������ڵ�Ĭ��ֵ
		String riq = DateUtil.FormatDate(new Date());
		egu.getColumn("fahrq").setDefaultValue(riq);
		egu.getColumn("daohrq").setDefaultValue(riq);
		egu.getColumn("daohrq").setHidden(true);
		// ���õ�վ������
		ComboBox c4 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c4);
		c4.setEditable(true);
		String daozSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
//		 ���ù�Ӧ�̵�������
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		String GongysSql = "select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		//ú��
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		String MeikSql = 

			"select decode(m.id, null, 999999,m.id),\n" +
			"       decode(m.id,\n" + 
			"              null,\n" + 
			"              '***'||m.gmc||'***',\n" + 
			"              m.mingc)\n" + 
			"  from (select grouping(m.id),\n" + 
			"               m.id as id,\n" + 
			"               gys.mingc as gmc,\n" + 
			"               max(m.mingc) as mingc\n" + 
			"          from meikxxb m, gongysmkglb g, gongysb gys\n" + 
			"         where m.id = g.meikxxb_id\n" + 
			"           and g.gongysb_id = gys.id\n" + 
			"         group by gys.mingc, rollup(m.id)\n" + 
			"         order by gys.mingc, grouping(m.id) desc) m";


		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(MeikSql));
		egu.getColumn("meikxxb_id").setReturnId(true);
		
		// ���÷�վ������
		ComboBox cb_faz = new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String fazSql = "select id ,mingc from chezxxb c where c.leib='��վ' order by c.mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		egu.getColumn("faz_id").setReturnId(true);
		// ����Ʒ��������
		ComboBox c5 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c5);
		c5.setEditable(true);
		String pinzSql = "select id,mingc from pinzb order by mingc";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		// ���ÿھ�������
		ComboBox c6 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c6);
		c6.setEditable(true);
		String jihkjSql = "select id,mingc from jihkjb";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjSql));
		// �������䷽ʽ������
		ComboBox c10 = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(c10);
		c10.setEditable(true);
		String yunsfsbSql = "select id,mingc from yunsfsb";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsfsbSql));
		// ����ԭ��վ������
		ComboBox c8 = new ComboBox();
		egu.getColumn("yuandz_id").setEditor(c8);
		c8.setEditable(true);
		String YuandzSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("yuandz_id").setComboEditor(egu.gridId,
				new IDropDownModel(YuandzSql));
		// egu.getColumn("yuandz_id").setDefaultValue(visit.getDaoz());
		// ����ԭ�ջ���λ������
		ComboBox c9 = new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(c9);
		c9.setEditable(true);// ���ÿ�����
		String Sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql));
		egu.getColumn("yuanshdwb_id").setDefaultValue(
				"" + ((Visit) getPage().getVisit()).getDiancmc());
		// ����ҳ�水ť

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		// egu.addToolbarButton(GridButton.ButtonType_Copy, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		boolean tag=true;
		if(visit.isDCUser()){
			try {
				ResultSet kzsql=con.getResultSet(
				"select decode(beiz,null,-1,(to_date(substr(beiz,1,10),'yyyy-mm-dd')-to_date('"+riq1+"','yyyy-mm-dd'))) as riq from diancxxb where id="+visit.getDiancxxb_id());
				int a;
				if(kzsql.next()){
					a=Integer.parseInt(kzsql.getString("riq"));
					if(a>=0){
						tag=false;
					}
				}
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
		}
		if(tag){
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		// .append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('RIQ').value+'����'������,���Ժ�'",true))
				.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		if (showBtn) {
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("����ǰ������", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
			// egu.addToolbarItem("{"+new
			// GridButton("����ǰ������","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
		}
		/*egu
				.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
						+ "row = irow; \n"
						+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
						+ "gongysTree_window.show();}});\n");
		// egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){alert();if(e.Field=='MEIKXXB_ID'){e.record.set('YUANMKDW',e.value)}});\n");
		egu.setDefaultsortable(false);*/
		setExtGrid(egu);
		/*DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz,
				"gongysTree", "" + visit.getDiancxxb_id(), null, null, null);

		visit.setDefaultTree(dt);*/
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

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setList1(null);
			// visit.setDefaultTree(null);
			this.setTreeid(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			setTbmsg(null);
		}
		getSelectData(null);
	}

	// �õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	// private String treeid;
	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public String getTreedcScript() {
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