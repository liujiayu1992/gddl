package com.zhiren.jt.zdt.yansgl.shulb;
/* 
* ʱ�䣺2009-06-2
* ���ߣ� ll
* �޸����ݣ� ע��save()�����У��ж�Yundlr002��Yundlr003��Yundlr004����ķ�����
*/

/*
 * ���ߣ����
 * ʱ�䣺2012-02-01
 * ����������ϵͳ�Ƿ������������������÷�ʽ���м����ϵͳ����
 * 		�������ԭ��Ϊ���û�ֻ¼��Ʊ�أ�ϵͳ������Ʊ�ص�ǧ��֮���������𣬲���Ʊ�ظ��Ƶ�ë�����Ҿ���ΪƱ�ؼ�����
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shullr extends BasePage implements PageValidateListener {
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
//	private String Riq;
//
//	public String getRiq() {
//		return Riq;
//	}
//
//	public void setRiq(String riq) {
//		this.Riq = riq;
//	}
	
	private boolean _EndChange=false;
	public Date getRiq() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setRiq(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_EndChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_EndChange=true;
		}
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
		
		StringBuffer sb = new StringBuffer();
		StringBuffer sbdl = new StringBuffer();
		
		//ɾ������
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		if (delrsl.getRows()!=0) {
			sbdl.append("begin\n");
			while (delrsl.next()) {
				sbdl.append(" delete from ").append("fahb").append(
					" where id =").append(delrsl.getString("id")).append(
					";	\n");
				sbdl.append(" delete from ").append("zhilb").append(
					" where id =").append(delrsl.getString("zhilb_id")).append(
					";	\n");
			}
			sbdl.append("end;");
			con.getDelete(sbdl.toString());
			con.commit();
		}
		
		
		//��������
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult+ "Yundlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		if(rsl.getRows()==0){
			return;
		}
		double biaoz = 0.0;
		int ches = 0;
		ches = rsl.getRows();
		String riq1 = DateUtil.FormatDate(getRiq());
		long diancxxb_id = 0;
		
		sb.append("begin\n");
		while (rsl.next()) {
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
						.getBeanId(rsl.getString("diancxxb_id"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			String zhilbid=MainGlobal.getNewID(visit.getDiancxxb_id());//�õ�zhilb_id
			String huaybh=rsl.getString("huaybh");
			biaoz += rsl.getDouble("biaoz");
			
			if("0".equals(rsl.getString("id"))){
				
				sb.append("insert into zhilb (id,huaybh,caiyb_id) values (\n").append(zhilbid).append(",'").append(huaybh).append("',0);\n");
				
				sb.append("insert into fahb\n").append("(id,diancxxb_id,chec,biaoz,gongysb_id,meikxxb_id,faz_id,pinzb_id,\n");
				sb.append("jihkjb_id,fahrq,daohrq,yunsfsb_id,maoz,piz,jingz,yingd,yingk,\n");
				sb.append("yuns,yunsl,koud,kous,kouz,koum,zongkd,sanfsl,ches,daoz_id,yuandz_id,yuanshdwb_id,beiz,zhilb_id,jiancl) \n");
				sb.append("values (getnewid(").append(diancxxb_id).append("),").append(diancxxb_id).append(",'");
				sb.append(rsl.getString("chec")).append("',").append(rsl.getString("biaoz")).append(",").append(
							((IDropDownModel) getGongysModel()).getBeanId(rsl.getString("gongysb_id"))).append(",");
				sb.append(((IDropDownModel) getMeikModel()).getBeanId(rsl.getString("meikxxb_id"))).append(",");
				sb.append(((IDropDownModel) getChezModel()).getBeanId(rsl.getString("faz_id"))).append(",").append(
							(getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
				sb.append(",").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
				sb.append(",").append(DateUtil.FormatOracleDate(riq1)).append(",").append(DateUtil.FormatOracleDate(riq1));
				sb.append(",").append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(rsl.getString("yunsfsb_id")));
				sb.append(",").append(rsl.getString("maoz")).append(",").append(rsl.getString("piz"));
				sb.append(",").append(rsl.getString("jingz")).append(",").append(rsl.getString("yingd"));
				sb.append(",").append(rsl.getString("yingk")).append(",").append(rsl.getString("yuns"));
				sb.append(",").append(rsl.getString("yunsl")).append(",").append(rsl.getString("koud"));
				sb.append(",").append(rsl.getString("kous")).append(",").append(rsl.getString("kouz"));
				sb.append(",").append(rsl.getString("koum")).append(",").append(rsl.getString("zongkd"));
				sb.append(",").append(rsl.getString("sanfsl")).append(",").append(rsl.getString("ches"));
				sb.append(",").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id")));
				sb.append(",").append((getExtGrid().getColumn("yuandz_id").combo).getBeanId(rsl.getString("yuandz_id")));
				sb.append(",").append((getExtGrid().getColumn("yuanshdwb_id").combo).getBeanId(rsl.getString("yuanshdwb_id")));
				sb.append(",'").append(rsl.getString("beiz")).append("',").append(zhilbid).append(",").append(rsl.getString("jiancl")).append(");\n");
				
			}else{
				sb.append("update zhilb set huaybh='").append(huaybh).append("' where id=").append(rsl.getString("zhilb_id")).append(";\n");
				sb.append("update fahb set chec='").append(rsl.getString("chec")).append("',biaoz=").append(rsl.getString("biaoz"));
				sb.append(",gongysb_id=").append(((IDropDownModel) getGongysModel()).getBeanId(rsl.getString("gongysb_id")));
				sb.append(",meikxxb_id=").append(((IDropDownModel) getMeikModel()).getBeanId(rsl.getString("meikxxb_id")));
				sb.append(",faz_id=").append(((IDropDownModel) getChezModel()).getBeanId(rsl.getString("faz_id")));
				sb.append(",pinzb_id=").append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
				sb.append(",jihkjb_id=").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
				sb.append(",yunsfsb_id=").append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(rsl.getString("yunsfsb_id")));
				sb.append(",fahrq=to_date('").append(rsl.getString("fahrq")).append("','yyyy-mm-dd')");
				sb.append(",yuandz_id=").append((getExtGrid().getColumn("yuandz_id").combo).getBeanId(rsl.getString("yuandz_id")));
				sb.append(",yuanshdwb_id=").append((getExtGrid().getColumn("yuanshdwb_id").combo).getBeanId(rsl.getString("yuanshdwb_id")));
				sb.append(",maoz=").append(rsl.getString("maoz")).append(",piz=").append(rsl.getString("piz"));
				sb.append(",jingz=").append(rsl.getString("jingz")).append(",yingd=").append(rsl.getString("yingd"));
				sb.append(",yingk=").append(rsl.getString("yingk")).append(",yuns=").append(rsl.getString("yuns"));
				sb.append(",yunsl=").append(rsl.getString("yunsl")).append(",koud=").append(rsl.getString("koud"));
				sb.append(",kous=").append(rsl.getString("kous")).append(",kouz=").append(rsl.getString("kouz"));
				sb.append(",koum=").append(rsl.getString("koum")).append(",zongkd=").append(rsl.getString("zongkd"));
				sb.append(",sanfsl=").append(rsl.getString("sanfsl")).append(",ches=").append(rsl.getString("ches"));
				sb.append(",daoz_id=").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id")));
				sb.append(",beiz='").append(rsl.getString("beiz")).append("',jiancl=").append(rsl.getString("jiancl")).append(" where id=").append(rsl.getString("id")).append(";\n");
				
			}
		}
		sb.append("end;");
		int flag = con.getInsert(sb.toString());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			setMsg(ErrorMessage.Yundlr001);
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

		Date Yesterday = getYesterday(DateUtil.getDate(DateUtil.FormatDate(getRiq())));// �õ��¸���ǰ���ڵ�ǰһ��
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

		copyData.append("select f.id,d.mingc as diancxxb_id,g.mingc as gongysb_id, mk.mingc meikxxb_id,\n")
				.append(" (select cz.mingc from chezxxb cz where f.faz_id=cz.id) as faz_id,\n")
				.append(" pz.mingc as pinzb_id,jh.mingc as jihkjb_id,\n")
				.append(" ys.mingc as yunsfsb_id,f.chec,f.biaoz,f.maoz,f.piz,f.jingz,f.yingd,f.yingk,\n")
				.append(" f.yuns,f.yunsl,f.koud,f.kous,f.kouz,f.koum,f.zongkd,f.sanfsl,f.ches,\n")
				.append(" to_char(f.fahrq,'yyyy-mm-dd') as fahrq,to_char(f.daohrq,'yyyy-mm-dd') as daohrq,\n")
				.append(" (select cz.mingc from chezxxb cz where f.daoz_id=cz.id) as daoz_id,\n")
				.append(" (select cz.mingc from chezxxb cz where f.yuandz_id=cz.id) as yuandz_id,d.mingc as yuanshdwb_id,f.beiz\n")
				.append(" from fahb f,diancxxb d,gongysb g,meikxxb mk,pinzb pz,jihkjb jh,yunsfsb ys \n")
				.append(" where f.diancxxb_id=d.id and f.gongysb_id=g.id and f.meikxxb_id=mk.id and f.pinzb_id=pz.id\n")
				.append(" and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id\n")
				.append(str).append("\n").append(" and to_char(f.daohrq,'yyyy-mm-dd')='").append(riq)
				.append("'");

		// System.out.println("����ͬ�ڵ�����:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(copyData.toString());
		while (rslcopy.next()) {

			long diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
					.getBeanId(rslcopy.getString("diancxxb_id"));
			// String fahrq=rslcopy.getString("fahrq");
			// String daohrq=rslcopy.getString("daohrq");
			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id());
			
			String zhilbid = MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id());

			StringBuffer sb = new StringBuffer();
				
					sb.append("begin\n");	
					sb.append("insert into zhilb\n");
					sb.append("(id,huaybh,caiyb_id) values (");
					sb.append(zhilbid).append(",").append("'0',0);\n");
					sb.append("insert into fahb\n").append("(id,diancxxb_id,chec,biaoz,gongysb_id,meikxxb_id,faz_id,pinzb_id,")
					  .append("jihkjb_id,fahrq,daohrq,yunsfsb_id,maoz,piz,jingz,yingd,yingk,")
					  .append("yuns,yunsl,koud,kous,kouz,koum,zongkd,sanfsl,ches,daoz_id,yuandz_id,beiz,zhilb_id) ")
					// .append("(id, diancxxb_id, gongysb_id, meikxxb_id,
					// pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq,
					// yunsfsb_id, chec,cheph, biaoz, chebb_id, yuandz_id,
					// yuanshdwb_id, yuanmkdw, daozch, lury, beiz,caiyrq)\n")
					  .append("values (")
					  .append(_id).append(",")
					  .append(diancxxb_id).append(",'")
					  .append(rslcopy.getString("chec")).append("',")
					  .append(rslcopy.getString("biaoz")).append(",")
					  .append(((IDropDownModel) getGongysModel()).getBeanId(rslcopy.getString("gongysb_id"))).append(",")
					  .append(((IDropDownModel) getMeikModel()).getBeanId(rslcopy.getString("meikxxb_id"))).append(",")
					  .append(((IDropDownModel) getChezModel()).getBeanId(rslcopy.getString("faz_id"))).append(",")
					  .append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rslcopy.getString("pinzb_id"))).append(",")
					  .append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rslcopy.getString("jihkjb_id"))).append(",")
					  .append(DateUtil.FormatOracleDate(getRiq())).append(",")
					  .append(DateUtil.FormatOracleDate(getRiq())).append(",")
					  .append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(rslcopy.getString("yunsfsb_id")))
					  .append(",").append(rslcopy.getString("maoz")).append(",")
					  .append(rslcopy.getString("piz")).append(",").append(rslcopy.getString("jingz")).append(",")
					  .append(rslcopy.getString("yingd")).append(",").append(rslcopy.getString("yingk")).append(",")
					  .append(rslcopy.getString("yuns")).append(",").append(rslcopy.getString("yunsl")).append(",")
					  .append(rslcopy.getString("koud")).append(",").append(rslcopy.getString("kous")).append(",")
					  .append("0").append(",").append(rslcopy.getString("koum")).append(",")
					  .append(rslcopy.getString("zongkd")).append(",").append(rslcopy.getString("sanfsl")).append(",")
					  .append(rslcopy.getString("ches")).append(",").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rslcopy.getString("daoz_id")))
					  .append(",").append((getExtGrid().getColumn("yuandz_id").combo).getBeanId(rslcopy.getString("yuandz_id")))
					  .append(",'").append(rslcopy.getString("beiz")).append("',").append(zhilbid)
					  .append(");\n");
					sb.append("end;");
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

		String riq1 = DateUtil.FormatDate(getRiq());// �鿴
		String daoz_str="0";

		String daoz="select c.mingc as daoz_id from fahb f,chezxxb c,diancxxb d where f.daoz_id=c.id and f.diancxxb_id=d.id and f.id in\n" +
				"(select max(id) from fahb f where daohrq>=to_date('"+DateUtil.FormatDate(DateUtil.getFirstDayOfYear(getRiq()))+"','yyyy-mm-dd')\n" + 
				"and daohrq<=to_date('"+riq1+"','yyyy-mm-dd') )";//+str;

		ResultSetList rsl_daoz = con.getResultSetList(daoz);
		if(rsl_daoz !=null){
			if(rsl_daoz.next()){
				daoz_str=rsl_daoz.getString("daoz_id");
			}
		}

		if (rsl == null) {
			sb.append("select f.id,f.zhilb_id,d.mingc as diancxxb_id,g.mingc as gongysb_id, mk.mingc meikxxb_id,\n")
			  	.append(" (select cz.mingc from chezxxb cz where f.faz_id=cz.id) as faz_id,\n")
				.append(" pz.mingc as pinzb_id,jh.mingc as jihkjb_id,\n")
				.append(" ys.mingc as yunsfsb_id,nvl(z.huaybh,'') as huaybh,f.chec,f.ches,f.biaoz,f.maoz,f.piz,f.jingz,f.yingd,(f.yingd-f.yingk) as kuid,f.yingk as yingk,\n")//f.yingk
				.append(" f.yuns,f.yunsl,f.koud,f.kous,f.kouz,f.koum,f.zongkd,f.sanfsl,f.fahrq,f.jiancl,f.daohrq,\n")
				.append(" (select cz.mingc from chezxxb cz where f.daoz_id=cz.id) as daoz_id,\n")
				.append(" (select cz.mingc from chezxxb cz where f.yuandz_id=cz.id) as yuandz_id,(select dc.mingc from diancxxb dc where f.yuanshdwb_id=dc.id) as yuanshdwb_id,f.beiz\n")
				.append(" from fahb f,zhilb z,diancxxb d,gongysb g,meikxxb mk,pinzb pz,jihkjb jh,yunsfsb ys \n")
				.append(" where f.zhilb_id=z.id(+) and f.diancxxb_id=d.id and f.gongysb_id=g.id and f.meikxxb_id=mk.id and f.pinzb_id=pz.id\n")
				.append(" and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id\n")
				.append(str).append("\n").append(" and to_char(f.daohrq,'yyyy-mm-dd')='").append(riq1).append("'");

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
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("zhilb_id").setDefaultValue("0");
		egu.getColumn("zhilb_id").editor = null;
		// *****************************************����Ĭ��ֵ****************************
		// �糧������
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from diancxxb where fuid="+ getTreeid() + " order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from diancxxb where id="+ getTreeid() + " order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+ getTreeid() + " order by mingc");
			String mingc = "";
			if (r.next()) {
				mingc = r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);

		}

		// ���õ糧Ĭ�ϵ�վ
		egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());

		egu.getColumn("chec").setHeader("����");
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("chec").editor
				.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 4); }");
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
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
		egu.getColumn("gongysb_id").setWidth(150);
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
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setWidth(60);
		egu.getColumn("huaybh").setDefaultValue("0");
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").setDefaultValue("0");
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setWidth(65);
		egu.getColumn("piz").setDefaultValue("0");
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(65);
		egu.getColumn("jingz").setDefaultValue("0");
		
		egu.getColumn("yingd").setHeader("ӯ��");
		egu.getColumn("yingd").setWidth(65);
		egu.getColumn("yingd").setDefaultValue("0");
		egu.getColumn("kuid").setHeader("����");
		egu.getColumn("kuid").setWidth(65);
		egu.getColumn("kuid").setUpdate(true);
		egu.getColumn("kuid").setDefaultValue("0");
		((NumberField)egu.getColumn("kuid").editor).setDecimalPrecision(3);
		egu.getColumn("yingk").setHeader("ӯ��");//����ӯ�֣��۶�//����
		egu.getColumn("yingk").editor.setMinValue("-10000");
		egu.getColumn("yingk").setWidth(65);
		egu.getColumn("yingk").setHidden(true);
		egu.getColumn("yingk").setDefaultValue("0");
		
		egu.getColumn("yuns").setHeader("����");
		egu.getColumn("yuns").setWidth(65);
		egu.getColumn("yuns").setDefaultValue("0");
		egu.getColumn("yunsl").setHeader("������");//Ĭ����0.012
		egu.getColumn("yunsl").setWidth(65);
		egu.getColumn("yunsl").setDefaultValue("0.012");
		egu.getColumn("koud").setHeader("�۶�");
		egu.getColumn("koud").setWidth(65);
		egu.getColumn("koud").setDefaultValue("0");
		egu.getColumn("kous").setHeader("����");//����
		egu.getColumn("kous").setWidth(65);
		egu.getColumn("kous").setDefaultValue("0");
		egu.getColumn("kous").setHidden(true);
		egu.getColumn("kouz").setHeader("����");//����
		egu.getColumn("kouz").setWidth(65);
		egu.getColumn("kouz").setDefaultValue("0");
		egu.getColumn("kouz").setHidden(true);
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
		egu.getColumn("ches").setDefaultValue("0");
		
		egu.getColumn("jiancl").setHeader("�����");
		egu.getColumn("jiancl").setWidth(65);
		egu.getColumn("jiancl").setDefaultValue("0");
		
		egu.getColumn("daoz_id").setHeader("��վ");
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("daoz_id").setDefaultValue(daoz_str);
		egu.getColumn("yuandz_id").setHeader("ԭ��վ");
		egu.getColumn("yuandz_id").setWidth(65);
		egu.getColumn("yuandz_id").setEditor(null);
		egu.getColumn("yuandz_id").setDefaultValue(daoz_str);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(90);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		
		egu.getColumn("jingz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getRiq()));
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
		String riq = DateUtil.FormatDate(this.getRiq());
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
		ComboBox cb_gongys = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongys);
		cb_gongys.setEditable(true);
		//sql���ȥ����Ҫ* ����Ϊ����Ӧ��Ӵ�����Ҫ
		String GongysSql = "select id,mingc from gongysb where mingc not like '%*%' order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		//ú��
		ComboBox cb_meik= new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cb_meik);
		cb_meik.setEditable(true);
//		sql���ȥ����Ҫ* ����Ϊ����Ӧ��Ӵ�����Ҫ
		String MeikSql = "select id,mingc from meikxxb where quanc not like '%*%'order by mingc";
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
		String pinzSql = "select id,mingc from pinzb where leib='ú' order by mingc";
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
		egu.getColumn("yuandz_id").editor.setAllowBlank(true);
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
		
		
		//ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		 .append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('RIQ').value+'���ڵ�����,���Ժ�'",true))
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
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		// egu.addToolbarButton(GridButton.ButtonType_Copy, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		//egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton",MainGlobal.getExtMessageShow("���ڱ�������,���Ժ�...", "������...", 200));
		
		egu.addTbarText("<font color=\"#FF0000\">������¼����Ϣ</font>");
		
		
		StringBuffer sb1 = new StringBuffer();
		
//		ϵͳ�Ƿ������������������÷�ʽ���м���
		if(MainGlobal.getXitxx_item("����", "���������������", getTreeid(), "��").equals("��")){
			sb1.append("gridDiv_grid.on('afteredit',function(e){");
			sb1.append("if(e.field=='BIAOZ'){\n" +
					"e.record.set('MAOZ',e.record.get('BIAOZ'));\n" +
					"e.record.set('YUNS',parseFloat(e.record.get('BIAOZ')==''?0:Math.round((e.record.get('BIAOZ'))*0.005)));\n" +
					"e.record.set('JINGZ',eval(e.record.get('BIAOZ'))-eval(e.record.get('YUNS')));}\n");//
			sb1.append("if(e.field=='YUNS'){\n" +
					"e.record.set('JINGZ',parseFloat(e.record.get('BIAOZ')==''?0:e.record.get('BIAOZ'))-parseFloat(e.record.get('YUNS')==''?0:e.record.get('YUNS')) );}\n");//
			sb1.append("});\n");

		}else{
			//�趨ĳһ�в��ܱ༭
			sb1.append("gridDiv_grid.on('afteredit',function(e){");
			sb1.append("if(e.field=='MAOZ' || e.field=='PIZ'){e.record.set('JINGZ',parseFloat(e.record.get('MAOZ')==''?0:e.record.get('MAOZ'))-parseFloat(e.record.get('MAOZ')==''?0:e.record.get('PIZ')))}");//
			sb1.append("if(e.field=='YINGD' || e.field=='KUID'){e.record.set('YINGK',parseFloat(e.record.get('YINGD')==''?0:e.record.get('YINGD'))-parseFloat(e.record.get('KUID')==''?0:e.record.get('KUID')))}");//
			sb1.append("});");
		}
		
		egu.addOtherScript(sb1.toString());
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
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
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