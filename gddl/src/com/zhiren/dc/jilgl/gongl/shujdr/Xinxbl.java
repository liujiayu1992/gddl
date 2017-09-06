package com.zhiren.dc.jilgl.gongl.shujdr;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ���ΰ
 * ʱ�䣺2009-03-26
 * �޸����ݣ�
 * 		���ӳ���ģ����ѯ
 */
/*
 * ���ߣ���ΰ
 * ʱ�䣺2009-04-01
 * �޸����ݣ�
 * 		�޸�SQL���
 * 			biao=maoz-piz-koud
 */
/*
 * ���ߣ���ΰ
 * ʱ�䣺2009-04-17
 * �޸����ݣ�
 * 		�޸�SQL���
 * 			��ӱ�ע��beiz��
 */
/* ���ߣ����ش�
* ʱ�䣺2009-10-31
* �޸����ݣ�
* 		����������Ϣ���޸ģ����ӱ��水ť��
* 
*/
/* �޸��ˣ�ww
 * �޸�ʱ�䣺2009-11-17
 * �޸����ݣ��޸�SQL��䣬��Ӧ�̡�ú�����䵥λ��Ʒ�֡�װ����Ϣ������
 *         ��Ӳ������ñ��水ť��Ĭ����ʾ
 */
/* �޸��ˣ�ww
 * �޸�ʱ�䣺2009-11-18
 * �޸����ݣ���Ӽƻ��������
 *         
 */

public class Xinxbl extends BasePage implements PageValidateListener {
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
	
//	��ͷ��	
	private String cheth="";
	public String getCheth() {
		return cheth;
	}
	public void setCheth(String cheth) {		
		this.cheth = cheth;
	}

//	 ������
	public String getRiqi() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setRiqi(String riqi) {
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

	public String getRiq2() {
		return ((Visit)this.getPage().getVisit()).getString4();
	}

	public void setRiq2(String riq2) {
		((Visit)this.getPage().getVisit()).setString4(riq2);
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
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl=getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Xinxlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		StringBuffer SQL = new StringBuffer();
		//���복Ƥ��ʱ��
		SQL.append("begin\n");
		double biaoz = 0.0;
		long diancxxb_id = v.getDiancxxb_id();
		String sql = "begin\n";
		while(rsl.next()){
			
			if (rsl.getDouble("piz") <= 0) {
				setMsg("{" + rsl.getString("cheh") + "}�޻�Ƥ�����ݣ�");
				return;
			}
			
			biaoz += rsl.getDouble("biaoz");
			SQL.append("insert into cheplsb\n"); 
            SQL.append("(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, yunsdwb_id, yunsfsb_id,zhongcsj,maoz,qingcsj,piz, chec,cheph, biaoz, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, daozch, lury, beiz,caiyrq,koud)\n");
            SQL.append("values (getnewid(").append(diancxxb_id).append("),").append(diancxxb_id);
            SQL.append(",").append(((IDropDownModel)getGongysModel())
					.getBeanId(rsl.getString("gongysb_id")));
            SQL.append(",").append(((IDropDownModel)getMeikModel())
					.getBeanId(rsl.getString("meikxxb_id")));
            SQL.append(",").append((v.getExtGrid1().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
            SQL.append(",1,1,").append((v.getExtGrid1().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
            SQL.append(",to_date('").append(rsl.getString("fahrq")).append("','yyyy-mm-dd'),")
            .append("to_date('2050-12-31','yyyy-mm-dd'),").append((getExtGrid().getColumn("yunsdwb_id").combo)
					.getBeanId(rsl.getString("yunsdwb_id"))).append(",")
            .append(SysConstant.YUNSFS_QIY).append(",to_date('").append(rsl.getString("maozsj"))
            .append("','yyyy-mm-dd hh24:mi:ss'),").append(rsl.getDouble("maoz")).append(",to_date('")
            .append(rsl.getString("pizsj")).append("','yyyy-mm-dd hh24:mi:ss'),").append(rsl.getDouble("piz"));
            SQL.append(",'").append(rsl.getString("chec"));
            SQL.append("','").append(rsl.getString("cheh")).append("'");
            SQL.append(",").append((rsl.getDouble("biaoz")-rsl.getDouble("koud")));
            SQL.append(","+SysConstant.CHEB_QC+",1,").append(diancxxb_id);
            SQL.append(",'").append(rsl.getString("yuanmkdw"));
            SQL.append("','").append(rsl.getString("daozch"));
            SQL.append("','").append(v.getRenymc());
            SQL.append("','").append(rsl.getString("beiz")).append("'");
            SQL.append(",to_date('").append(rsl.getString("caiyrq")).append("','yyyy-mm-dd')")
            .append(",").append(rsl.getString("koud"))
            .append(");\n");
            sql += "update qichzcb set zhuangt = 1 where id =" + rsl.getString("id") + ";\n";
		}
		if(rsl.getRows() > 0){
			sql +="end;";
			con.getUpdate(sql);
		}
		SQL.append("end;");
		int flag = con.getInsert(SQL.toString());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+SQL);
			setMsg(ErrorMessage.Xinxlr001);
			return;
		}
		flag = Jilcz.Updatezlid(con, v.getDiancxxb_id(), SysConstant.YUNSFS_QIY, null);
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Xinxlr002);
			setMsg(ErrorMessage.Xinxlr002);
			return;
		}
		flag = Jilcz.INSorUpfahb(con, v.getDiancxxb_id());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Xinxlr003);
			setMsg(ErrorMessage.Xinxlr003);
			return;
		}
		flag = Jilcz.InsChepb(con, v.getDiancxxb_id(), null, SysConstant.HEDBZ_YJJ);
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Xinxlr004);
			setMsg(ErrorMessage.Xinxlr004);
			return;
		}
		SQL.delete(0, SQL.length());
		SQL.append("select distinct fahb_id from cheplsb");
		rsl = con.getResultSetList(SQL.toString());
		if (rsl == null) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Xinxlr005);
			setMsg(ErrorMessage.Xinxlr005);
			return;
		}
		while (rsl.next()) {
			flag = Jilcz.updateFahb(con, rsl.getString("fahb_id"));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Xinxlr006);
				setMsg(ErrorMessage.Xinxlr006);
				return;
			}
			flag = Jilcz.updateLieid(con, rsl.getString("fahb_id"));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Xinxlr006);
				setMsg(ErrorMessage.Xinxlr006);
				return;
			}
		}
		con.commit();  
		con.Close();
		setMsg("�������� "+rsl.getRows()+" ������Ϣ,����Ʊ�� "+biaoz+" �֡�");
	}
	
	
	public void Save1(){
		

		if(getChange()==null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		long diancxxb_id ;
		int yunsfsb_id =2;
		List fhlist = new ArrayList();
		ResultSetList rsld = getExtGrid().getDeleteResultSet(getChange());
		if(rsld == null) {	
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Jianjxg.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}

		while(rsld.next()) {
			String fahbid = rsld.getString("fahbid");
			Jilcz.addFahid(fhlist,fahbid);
			String id = rsld.getString("id");
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Jianjxg,
					"chepb",id);

		}
//		�޸ĳ�Ƥ
		rsld = getExtGrid().getModifyResultSet(getChange());
		if(rsld == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Jianjxg.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rsld.next()) {
			if(visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsld.getString("diancxxb_id"));
			}else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			String chepid = rsld.getString("id");
			StringBuffer sb = new StringBuffer();
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Jianjxg,
					"chepb",chepid);
			sb.append("update qichzcb set ").append("koud=").append(rsld.getDouble("koud")).append(",");
			sb.append("gongysb_id=").append(
						((IDropDownModel) getGongysModel()).getBeanId(rsld
								.getString("gongysb_id"))).append(",");
			sb.append("meikxxb_id=").append(
					((IDropDownModel) getMeikModel()).getBeanId(rsld
							.getString("meikxxb_id"))).append(",");
			sb.append("jihkjb_id=").append(
					getExtGrid().getColumn("jihkjb_id").combo
					.getBeanId(rsld.getString("jihkjb_id"))).append(",");
			
			sb.append("pinzb_id=").append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsld.getString("pinzb_id"))).append(",");
			sb.append("yunsdwb_id=").append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsld.getString("yunsdwb_id"))).append(",");
			sb.append("cheh='").append(rsld.getString("cheh")).append("',");
			sb.append("zhuangcdw_item_id=").append(
					getExtGrid().getColumn("zhuangcdw_item_id").combo
					.getBeanId(rsld.getString("zhuangcdw_item_id"))).append(",");
			sb.append("beiz ='").append(rsld.getString("beiz"));
			sb.append("' where id=").append(chepid);
			int flag = con.getUpdate(sb.toString());
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail+"SQL:"+sb);
				setMsg(ErrorMessage.Jianjxg002);
				return;
			}

			
		

		con.commit();
		con.Close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist);
		setMsg("����ɹ�");
	
		}
		
		
	}
 
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _Save1Chick=false;
	
	public void Save1Button(IRequestCycle cycle){
		
		_Save1Chick=true;
	}
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_Save1Chick){
			_Save1Chick=false;
			Save1();
			getSelectData();
		}
		
	}
	
	/* �޸��ˣ�ww
	 * �޸�ʱ�䣺2009-09-03
	 * �޸����ݣ�Ĭ�Ϸ������ں͵�������Ϊ�س�ʱ��
	 */
	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
//		sb.append("select id,cheh,maoz,to_char(maozsj,'yyyy-mm-dd hh24:mi:ss') maozsj,")
//		.append("piz,to_char(pizsj,'yyyy-mm-dd hh24:mi:ss') pizsj, '' gongysb_id,'' meikxxb_id, ")
//		.append("'' jihkjb_id,'' yunsdwb_id,0 koud, \n")
//		.append("'1' chec, (select mingc from pinzb where rownum=1) pinzb_id, ")
//		.append("(select mingc from meicb where rownum=1) meicb_id, maoz-piz biaoz, sysdate fahrq, sysdate caiyrq,  \n")
//		.append("'' yuanmkdw, '' daozch, beiz from qichzcb where diancxxb_id =")
//		.append(v.getDiancxxb_id()).append(" and zhuangt = 0 and maozsj >= ")
//		.append(DateUtil.FormatOracleDate(getRiqi())).append(" and maozsj <")
//		.append(DateUtil.FormatOracleDate(getRiq2())).append(" + 1");
	
//		sb.append("select id,cheh,maoz,to_char(maozsj,'yyyy-mm-dd hh24:mi:ss') maozsj,")
//		.append("piz,to_char(pizsj,'yyyy-mm-dd hh24:mi:ss') pizsj, gongysb.mingc as gongysb_id,meikxxb.mingc as meikxxb_id, ")
//		.append("jihkjb.mingc as jihkjb_id,yunsdwb.mingc as yunsdwb_id, koud as koud, \n")
//		.append("'1' chec, (select mingc from pinzb where rownum=1) pinzb_id, ")
//		.append("(select mingc from meicb where rownum=1) meicb_id, maoz-piz biaoz, maozsj fahrq, maozsj caiyrq,  \n")
//		.append("'' yuanmkdw, '' daozch, beiz from qichzcb,gongysb ,meikxxb,jihakjb,yunsdwb  where diancxxb_id =")
//		.append(v.getDiancxxb_id()).append(" and zhuangt = 0 and maozsj >= ")
//		.append(DateUtil.FormatOracleDate(getRiqi())).append(" and maozsj <")
//		.append(DateUtil.FormatOracleDate(getRiq2())).append(" + 1");
		
		/* �޸��ˣ�cbd
		 * �޸�ʱ�䣺2009-10-31
		 * �޸����ݣ��޸�SQL��䣬��ʾ����������Ϣ��װ����Ϣ
		 */

		/* �޸��ˣ�ww
		 * �޸�ʱ�䣺2009-11-17
		 * �޸����ݣ��޸�SQL��䣬װ����Ϣ������
		 */
		
		sb.append("select distinct qichzcb.id as ID,\n" +
		"       cheh,\n" + 
		"       maoz,\n" + 
		"       to_char(maozsj, 'yyyy-mm-dd hh24:mi:ss') maozsj,\n" + 
		"       piz,\n" + 
		"       to_char(pizsj, 'yyyy-mm-dd hh24:mi:ss') pizsj,\n" + 
		"       gongysb.mingc as gongysb_id,\n" + 
		"       meikxxb.mingc as meikxxb_id,\n" + 
		"       jihkjb.mingc as jihkjb_id,\n" + 
		"       yunsdwb.mingc as yunsdwb_id,\n" + 
		"       koud as koud,\n" + 
		"       '1' chec,\n" + 
		"       pinzb.mingc as pinzb_id,\n" + 
		"       (select mingc from meicb where rownum = 1) meicb_id,\n" + 
		"       maoz - piz biaoz,\n" + 
		"       maozsj fahrq,\n" + 
		"       maozsj caiyrq,\n" + 
		"       '' yuanmkdw,\n" + 
		"       '' daozch," +
		"       nvl(item.mingc,'') as zhuangcdw_item_id,\n" + 
		"       qichzcb.piaojh,\n" +
		"       qichzcb.beiz\n" + 
		"  from qichzcb, gongysb, meikxxb, jihkjb, yunsdwb,pinzb,meicb,item\n" + 
		" where qichzcb.diancxxb_id = \n" + v.getDiancxxb_id()+
		"   and qichzcb.zhuangt = 0\n" + 
		"   and qichzcb.gongysb_id = gongysb.id(+)\n" + 
		"   and qichzcb.meikxxb_id = meikxxb.id(+)\n" + 
		"   and qichzcb.jihkjb_id = jihkjb.id(+)\n" + 
		"   and qichzcb.yunsdwb_id=yunsdwb.id(+)"+
		"   and qichzcb.pinzb_id=pinzb.id(+)"+
		"   and qichzcb.zhuangcdw_item_id=item.id(+)"+
		"   and qichzcb.maozsj>= "+DateUtil.FormatOracleDate(getRiqi())+
		"   and qichzcb.maozsj< "+DateUtil.FormatOracleDate(getRiq2())+"+1");

		
		
		String strValue = getGuopztValue().getStrId();
		if(strValue.equals(Integer.toString(0))){
			
		}else if(strValue.equals(Integer.toString(1))){
			sb.append(" and piz=0");
		}else if(strValue.equals(Integer.toString(2))){
			sb.append(" and piz<>0");
		}
//		��ѯ����
		if(getCheth()!=null && !"".equals(getCheth())){
			sb.append("and cheh like '%" + getCheth() + "%'");
		}
			
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("cheplsb");
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		//����ÿҳ��ʾ����
		egu.addPaging(25);
//		��ʱ���жϷֳ�������
//		if(v.isFencb()) {
//			ComboBox dc= new ComboBox();
//			egu.getColumn("diancxxb_id").setEditor(dc);
//			dc.setEditable(true);
//			String dcSql="select id,mingc from diancxxb where fuid="+v.getDiancxxb_id();
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
//			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
//			egu.getColumn("diancxxb_id").setWidth(70);
//		}else {
//			egu.getColumn("diancxxb_id").setHidden(true);
//			egu.getColumn("diancxxb_id").editor = null;
//		}
		
		egu.getColumn("cheh").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheh").setWidth(100);
//      ���ſɱ༭		
//		egu.getColumn("cheh").setEditor(null);
		egu.getColumn("maoz").setHeader(Locale.maoz_fahb);
		egu.getColumn("maoz").setWidth(65);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("maozsj").setHeader(Locale.zhongcsj_chepb);
		egu.getColumn("maozsj").setWidth(110);
		egu.getColumn("maozsj").setEditor(null);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("pizsj").setHeader(Locale.qingcsj_chepb);
		egu.getColumn("pizsj").setWidth(110);
		egu.getColumn("pizsj").setEditor(null);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(110);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("jihkjb_id").setEditor(null);		
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdwb_id").setWidth(80);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setWidth(40);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(50);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(40);
		egu.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("meicb_id").setWidth(80);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("caiyrq").setHeader(Locale.caiyrq_caiyb);
		egu.getColumn("caiyrq").setWidth(70);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yuanmkdw").setWidth(90);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("daozch").setWidth(70);
		egu.getColumn("zhuangcdw_item_id").setHeader("װ����Ϣ");
		egu.getColumn("zhuangcdw_item_id").setWidth(70);
		egu.getColumn("piaojh").setHeader("�ƻ�����");
		egu.getColumn("piaojh").setEditor(null);
		egu.getColumn("piaojh").setWidth(70);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(80);
		
		
		//�۶ֱ���3λС��
		((NumberField) egu.getColumn("koud").editor).setDecimalPrecision(3);
		
		//���÷������ں͵������ڵ�Ĭ��ֵ
		egu.getColumn("fahrq").setDefaultValue(getRiqi());
		egu.getColumn("caiyrq").setDefaultValue(getRiqi());
		//����Ʒ��������
		ComboBox c5=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c5);
		c5.setEditable(true);
		String pinzSql=SysConstant.SQL_Pinz_mei;
		
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,new IDropDownModel(pinzSql));
		//���ÿھ�������
		ComboBox c6=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c6);
		c6.setEditable(true);
		String jihkjSql=SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
//		���䵥λ
		ComboBox cysdw = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(cysdw);
		cysdw.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="+ v.getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,new IDropDownModel(yunsdwSql));
//		����ԭ�ջ���λ������
//		ComboBox c9=new ComboBox();
//		egu.getColumn("yuanshdwb_id").setEditor(c9);
//		c9.setEditable(true);//���ÿ�����
//		String Sql="select id,mingc from diancxxb order by mingc";
//		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId, new IDropDownModel(Sql));
//		egu.getColumn("yuanshdwb_id").setDefaultValue(""+((Visit) getPage().getVisit()).getDiancmc());
//		ú��
		ComboBox cmc = new ComboBox();
		egu.getColumn("meicb_id").setEditor(cmc);
		cmc.setEditable(true);
		String cmcSql = SysConstant.SQL_Meic;
		egu.getColumn("meicb_id").setComboEditor(egu.gridId,new IDropDownModel(cmcSql));
//      װ����Ϣ
		ComboBox zcxx = new ComboBox();
		egu.getColumn("zhuangcdw_item_id").setEditor(zcxx);
		cmc.setEditable(true);
		String zcxxSql = "select id,mingc from item ";
		egu.getColumn("zhuangcdw_item_id").setComboEditor(egu.gridId,new IDropDownModel(zcxxSql));
		
		

		
		egu.addTbarText("��ë����:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());		
		egu.addTbarText("-");
		
		// ��Ƥ״̬
		egu.addTbarText("��Ƥ״̬");		
		ComboBox comb4 = new ComboBox();		
		comb4.setTransform("GuopztDropDown");
		comb4.setId("Guopzt");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Guopzt.on('select',function(){document.getElementById('RefreshButton').click();});");		
		egu.addTbarText("-");
		
		egu.addTbarText("���ţ�");
		TextField tf = new TextField();
		tf.setId("cheth");
		tf.setWidth(100);
		tf.setValue(getCheth());
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��",
				"function(){document.getElementById('CHETH').value=cheth.getValue();document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		
		//egu.addToolbarButton(GridButton.ButtonType_Inserts, null);
		//egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		String sqlSave = "select * from xitxxb where mingc = '��Ϣ��¼��ʾ���水ť' and leib = '����' and zhi ='��' and zhuangt =1";
		boolean blSave = con.getHasIt(sqlSave);
//		egu.addToolbarButton(GridButton.ButtonType_SubmitSel, "SaveButton");

		if (!blSave) {
			egu.addToolbarButton(GridButton.ButtonType_SubmitSel, "SaveButton");
			egu.addToolbarButton(GridButton.ButtonType_Save, "Save1Button");
		}
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+"row = irow; \n"
				+"if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"gongysTree_window.show();}});");
		
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_kj,"gongysTree"
				,""+v.getDiancxxb_id(),null,null,null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler.append("function() { \n")
		.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();return;} \n")
		.append("if(cks.getDepth() < 3){ \n")
		.append("Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���Ӧ�ļƻ��ھ���');")
		.append("return; } \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.text); \n")
		.append("rec.set('MEIKXXB_ID', cks.parentNode.text); \n")
		.append("rec.set('YUANMKDW', cks.parentNode.text); rec.set('JIHKJB_ID', cks.text); \n")
		.append("gongysTree_window.hide(); \n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
		bbar.addItem(btn);
		v.setDefaultTree(dt);
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
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public String getTreeScript() {
//		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
	
//	��Ƥ״̬

	public IDropDownBean getGuopztValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGuopztModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGuopztValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGuopztModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGuopztModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGuopztModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGuopztModels() {
		
//		String  sql ="select pizzt,decode(pizzt,0,'δ��Ƥ',1,'�ѹ�Ƥ') from (select decode(piz,0,0,piz,1)as pizzt from qichzcb) group by pizzt;";		  
		
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "ȫ��"));
		l.add(new IDropDownBean(1, "δ��Ƥ"));
		l.add(new IDropDownBean(2, "�ѹ�Ƥ"));

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(l));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	
	
	
//��Ӧ��
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setGongysModel(null);
			setGongysModels();
			((Visit) getPage().getVisit()).setDropDownBean10(null);
			((Visit) getPage().getVisit()).setProSelectionModel10(null);
			setMeikModel(null);
			setMeikModels();
			setRiqi(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			setTbmsg(null);
			setCheth(null);
			getSelectData();
		}
	}
}