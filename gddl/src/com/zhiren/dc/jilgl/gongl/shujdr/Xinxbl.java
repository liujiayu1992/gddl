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
 * 作者：王伟
 * 时间：2009-03-26
 * 修改内容：
 * 		增加车号模糊查询
 */
/*
 * 作者：王伟
 * 时间：2009-04-01
 * 修改内容：
 * 		修改SQL语句
 * 			biao=maoz-piz-koud
 */
/*
 * 作者：王伟
 * 时间：2009-04-17
 * 修改内容：
 * 		修改SQL语句
 * 			添加备注（beiz）
 */
/* 作者：车必达
* 时间：2009-10-31
* 修改内容：
* 		车辆基础信息可修改，增加保存按钮。
* 
*/
/* 修改人：ww
 * 修改时间：2009-11-17
 * 修改内容：修改SQL语句，供应商、煤矿、运输单位、品种、装车信息外连接
 *         添加参数配置保存按钮，默认显示
 */
/* 修改人：ww
 * 修改时间：2009-11-18
 * 修改内容：添加计划卡序号列
 *         
 */

public class Xinxbl extends BasePage implements PageValidateListener {
//	界面用户提示
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
	
//	车头号	
	private String cheth="";
	public String getCheth() {
		return cheth;
	}
	public void setCheth(String cheth) {		
		this.cheth = cheth;
	}

//	 绑定日期
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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl=getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Xinxlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		StringBuffer SQL = new StringBuffer();
		//插入车皮临时表
		SQL.append("begin\n");
		double biaoz = 0.0;
		long diancxxb_id = v.getDiancxxb_id();
		String sql = "begin\n";
		while(rsl.next()){
			
			if (rsl.getDouble("piz") <= 0) {
				setMsg("{" + rsl.getString("cheh") + "}无回皮数数据！");
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
		setMsg("您保存了 "+rsl.getRows()+" 车的信息,共计票重 "+biaoz+" 吨。");
	}
	
	
	public void Save1(){
		

		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
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
					+ "Jianjxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
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
//		修改车皮
		rsld = getExtGrid().getModifyResultSet(getChange());
		if(rsld == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Jianjxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
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
		setMsg("保存成功");
	
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
	
	/* 修改人：ww
	 * 修改时间：2009-09-03
	 * 修改内容：默认发货日期和到货日期为重车时间
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
		
		/* 修改人：cbd
		 * 修改时间：2009-10-31
		 * 修改内容：修改SQL语句，显示车辆基础信息和装车信息
		 */

		/* 修改人：ww
		 * 修改时间：2009-11-17
		 * 修改内容：修改SQL语句，装车信息外连接
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
//		查询车号
		if(getCheth()!=null && !"".equals(getCheth())){
			sb.append("and cheh like '%" + getCheth() + "%'");
		}
			
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("cheplsb");
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(25);
//		暂时不判断分厂别的情况
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
//      车号可编辑		
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
		egu.getColumn("zhuangcdw_item_id").setHeader("装车信息");
		egu.getColumn("zhuangcdw_item_id").setWidth(70);
		egu.getColumn("piaojh").setHeader("计划卡号");
		egu.getColumn("piaojh").setEditor(null);
		egu.getColumn("piaojh").setWidth(70);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(80);
		
		
		//扣吨保留3位小数
		((NumberField) egu.getColumn("koud").editor).setDecimalPrecision(3);
		
		//设置发货日期和到货日期的默认值
		egu.getColumn("fahrq").setDefaultValue(getRiqi());
		egu.getColumn("caiyrq").setDefaultValue(getRiqi());
		//设置品种下拉框
		ComboBox c5=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c5);
		c5.setEditable(true);
		String pinzSql=SysConstant.SQL_Pinz_mei;
		
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,new IDropDownModel(pinzSql));
		//设置口径下拉框
		ComboBox c6=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c6);
		c6.setEditable(true);
		String jihkjSql=SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
//		运输单位
		ComboBox cysdw = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(cysdw);
		cysdw.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="+ v.getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,new IDropDownModel(yunsdwSql));
//		设置原收货单位下拉框
//		ComboBox c9=new ComboBox();
//		egu.getColumn("yuanshdwb_id").setEditor(c9);
//		c9.setEditable(true);//设置可输入
//		String Sql="select id,mingc from diancxxb order by mingc";
//		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId, new IDropDownModel(Sql));
//		egu.getColumn("yuanshdwb_id").setDefaultValue(""+((Visit) getPage().getVisit()).getDiancmc());
//		煤场
		ComboBox cmc = new ComboBox();
		egu.getColumn("meicb_id").setEditor(cmc);
		cmc.setEditable(true);
		String cmcSql = SysConstant.SQL_Meic;
		egu.getColumn("meicb_id").setComboEditor(egu.gridId,new IDropDownModel(cmcSql));
//      装车信息
		ComboBox zcxx = new ComboBox();
		egu.getColumn("zhuangcdw_item_id").setEditor(zcxx);
		cmc.setEditable(true);
		String zcxxSql = "select id,mingc from item ";
		egu.getColumn("zhuangcdw_item_id").setComboEditor(egu.gridId,new IDropDownModel(zcxxSql));
		
		

		
		egu.addTbarText("检毛日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());		
		egu.addTbarText("-");
		
		// 过皮状态
		egu.addTbarText("过皮状态");		
		ComboBox comb4 = new ComboBox();		
		comb4.setTransform("GuopztDropDown");
		comb4.setId("Guopzt");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Guopzt.on('select',function(){document.getElementById('RefreshButton').click();});");		
		egu.addTbarText("-");
		
		egu.addTbarText("车号：");
		TextField tf = new TextField();
		tf.setId("cheth");
		tf.setWidth(100);
		tf.setValue(getCheth());
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新",
				"function(){document.getElementById('CHETH').value=cheth.getValue();document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		
		//egu.addToolbarButton(GridButton.ButtonType_Inserts, null);
		//egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		String sqlSave = "select * from xitxxb where mingc = '信息补录显示保存按钮' and leib = '数量' and zhi ='否' and zhuangt =1";
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
		.append("Ext.MessageBox.alert('提示信息','请选择对应的计划口径！');")
		.append("return; } \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.text); \n")
		.append("rec.set('MEIKXXB_ID', cks.parentNode.text); \n")
		.append("rec.set('YUANMKDW', cks.parentNode.text); rec.set('JIHKJB_ID', cks.text); \n")
		.append("gongysTree_window.hide(); \n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
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
	
//	过皮状态

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
		
//		String  sql ="select pizzt,decode(pizzt,0,'未过皮',1,'已过皮') from (select decode(piz,0,0,piz,1)as pizzt from qichzcb) group by pizzt;";		  
		
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "全部"));
		l.add(new IDropDownBean(1, "未过皮"));
		l.add(new IDropDownBean(2, "已过皮"));

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(l));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	
	
	
//供应商
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
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