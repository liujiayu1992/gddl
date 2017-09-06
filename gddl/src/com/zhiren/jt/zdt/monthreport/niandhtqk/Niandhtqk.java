package com.zhiren.jt.zdt.monthreport.niandhtqk;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-11-30
 * 描述：合同供方改为只取煤矿地区（为计算月报中的到货率）
 */
/*
 * 作者：赵胜男
 * 时间：2012-04-20
 * 描述：标题字体居中
 *      运输方式调整至发站前
 *      标题单位补充完成并括号
 */
/*
 * 作者：夏峥
 * 时间：2012-07-12
 * 描述：修改复制上年计划的功能
 * 		调整界面显示内容
 * 		增加煤矿单位字段
 */

public  class Niandhtqk extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag =0;
		ResultSetList rsl;
		rsl=visit.getExtGrid1().getModifyResultSet(getChange());

		double htl=0;
		String hangid="";
		String strDate="";
		String strYear="";
		String strYearEnd="";
		//保存更新或新增
		strYear=getNianfValue().getValue() +"-01-01";
		strYearEnd=getNianfValue().getValue() +"-12-01";
		
		while (rsl.next()){
			double rez=rsl.getDouble("rez");
			double huiff=rsl.getDouble("huiff");
			double liuf=rsl.getDouble("liuf");
			double chebjg=rsl.getDouble("chebjg");
			double yunf=rsl.getDouble("yunf");
			double zaf=rsl.getDouble("zaf");
			
			long diancxxb_id=(getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
			long gongysb_id=(getExtGrid().getColumn("gongysb_id").combo).getBeanId(rsl.getString("gongysb_id"));
			long meikxxb_id=(getExtGrid().getColumn("meikxxb_id").combo).getBeanId(rsl.getString("meikxxb_id"));
			long jihkjb_id=(getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id"));
			long pinzb_id=(getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id"));
			long faz_id=(getExtGrid().getColumn("faz_id").combo).getBeanId(rsl.getString("faz_id"));
			long daoz_id=(getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id"));
			long yunsfs=(getExtGrid().getColumn("yunsfs").combo).getBeanId(rsl.getString("yunsfs"));
			
			
			if(!"0".equals(rsl.getString("id"))){				
				String cc ="delete from (select id from niandhtqkb " +
				"where (riq>=to_date('"+strYear +"','yyyy-mm-dd') and riq<=to_date('"+strYearEnd +"','yyyy-mm-dd'))" +
				"and HANGID="+rsl.getString("HANGID")+" \n"+
				"and DIANCXXB_ID = "+diancxxb_id+")";
				
				flag=con.getDelete(cc);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"	+ cc);
					setMsg(ErrorMessage.DeleteDatabaseFail);
					return;
				}
		   }
			hangid=MainGlobal.getNewID(100);//行ID
			for (int i=1;i<=12;i++){
				htl=rsl.getDouble("Y"+i);
				strDate=getNianfValue().getValue() +"-"+ i +"-01";
				String bb="insert into niandhtqkb(id,diancxxb_id,gongysb_id,meikxxb_id,jihkjb_id,pinzb_id,faz_id,daoz_id,beiz,hej,riq,yunsfsb_id,REZ,HUIFF,LIUF,CHEBJG,YUNF,ZAF,hangid) values(" +
				"getNewId("+diancxxb_id+"),"+
				diancxxb_id+","+
				gongysb_id+","+
				meikxxb_id+","+
				jihkjb_id+","+
				pinzb_id+","+
				faz_id+","+
				daoz_id+","+
				"'"+rsl.getString("beiz")+"',"+
				htl+"," +
				"to_date('"+strDate+"','yyyy-mm-dd'),"+
				yunsfs+","+rez+","+huiff+","+liuf+","+chebjg+","+yunf+","+zaf+","+hangid+")";

				flag=con.getInsert(bb);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"	+ bb);
					setMsg(ErrorMessage.InsertDatabaseFail);
					return;
				}
			}
		}
		//删除数据
		rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			String cc ="delete from (select id from niandhtqkb " +
			"where (riq>=to_date('"+strYear +"','yyyy-mm-dd') and riq<=to_date('"+strYearEnd +"','yyyy-mm-dd'))" +
			"and HANGID="+rsl.getString("HANGID")+" \n"+
			"and DIANCXXB_ID = "+this.getTreeid()+")";
			
			flag=con.getDelete(cc);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"	+ cc);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
		}
		if(flag!=-1){
			con.commit();
			con.Close();
			setMsg(ErrorMessage.SaveSuccessMessage);
		}else{
		setMsg("保存失败！");
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		if(_CopyButton){
			_CopyButton=false;
			CoypLastYearData();
		}
	}
	
	public void CoypLastYearData(){
		JDBCcon con = new JDBCcon();
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long Lstyear;
		Lstyear=intyear-1;
//		复制上一年计划
		String copyData =
			"INSERT INTO NIANDHTQKB\n" +
			"(ID,DIANCXXB_ID,GONGYSB_ID,MEIKXXB_ID,JIHKJB_ID,PINZB_ID,FAZ_ID,DAOZ_ID,BEIZ,HEJ,RIQ,YUNSFSB_ID," +
			"REZ,HUIFF,LIUF,CHEBJG,YUNF,ZAF,HANGID)\n" + 
			"(SELECT GETNEWID(100) ID,DIANCXXB_ID,GONGYSB_ID,MEIKXXB_ID,JIHKJB_ID,PINZB_ID,FAZ_ID,DAOZ_ID,BEIZ,HEJ," +
			"ADD_MONTHS(RIQ, 12) RIQ,YUNSFSB_ID,\n" + 
			"REZ,HUIFF,LIUF,CHEBJG,YUNF,ZAF,HANGID FROM NIANDHTQKB WHERE DIANCXXB_ID = "+this.getTreeid()+" AND TO_CHAR(RIQ, 'yyyy') = '"+Lstyear+"')";
		con.getInsert(copyData);
		
//		更新行ID
		String upd_Hangid="SELECT DISTINCT HANGID FROM NIANDHTQKB WHERE DIANCXXB_ID = "+this.getTreeid()+" AND TO_CHAR(RIQ, 'yyyy') = '"+intyear+"'";
		ResultSetList rsl=con.getResultSetList(upd_Hangid);
		String upd="";
		while (rsl.next()){
			String hangid=MainGlobal.getNewID(100);//行ID
			upd+="UPDATE NIANDHTQKB SET HANGID="+hangid+" WHERE HANGID="+rsl.getString("HANGID")+" AND DIANCXXB_ID = "+this.getTreeid()+" AND TO_CHAR(RIQ, 'yyyy') = '"+intyear+"';\n";
			
		}
		if(upd.length()>10){
			con.getUpdate("begin \n"+upd+"end;");
		}
		con.Close();
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
//		 工具栏的年份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
	
		String strdiancTreeID = "";
		
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strdiancTreeID="";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strdiancTreeID = " and dc.fuid= " +this.getTreeid();
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancTreeID=" and dc.id= " +this.getTreeid();
		}
		
		String chaxun = "select min(n.id) as id,\n"
				+ "       dc.mingc as diancxxb_id,\n"
				+ "       g.mingc as gongysb_id,\n"
				+ "       mk.mingc as meikxxb_id,\n"
				+ "       j.mingc as jihkjb_id,\n"
				+ "       p.mingc as pinzb_id,\n"
				+"        y.mingc as yunsfs,\n"
				+ "       c.mingc as faz_id,\n"
				+ "       c1.mingc as daoz_id,\n"
				+ "       max(round_new(rez,2)) as rez,\n "
				+ "       max(round_new(huiff,2)) as huiff,\n"
				+ "       max(round_new(liuf,2)) as liuf,\n"
				+ "       max(round_new(chebjg,2)) as chebjg,\n" 
				+ "       max(round_new(yunf,2)) as yunf,\n"
				+ "       max(round_new(zaf,2)) as zaf,\n"
				+ "       round_new(max(round_new(chebjg,2))+max(round_new(yunf,2))+max(round_new(zaf,2)),2) as daocj,"
				+ "       decode(max(rez),0,0,round_new((max(chebjg)/1.17+max(yunf)*(1-0.07)+max(zaf))*29.271/max(rez),2)) as biaomdj,"
				+ "       sum(decode(to_char(riq,'mm'),'01',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'02',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'03',nvl( n.hej,0),0)+\n"
				+ "       decode(to_char(riq,'mm'),'04',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'05',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'06',nvl( n.hej,0),0)+\n"
				+ "       decode(to_char(riq,'mm'),'07',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'08',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'09',nvl( n.hej,0),0)+\n"
				+ "       decode(to_char(riq,'mm'),'10',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'11',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'12',nvl( n.hej,0),0)) as quann,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '01', nvl(n.hej, 0), 0)) as y1,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '02', nvl(n.hej, 0), 0)) as y2,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '03', nvl(n.hej, 0), 0)) as y3,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '04', nvl(n.hej, 0), 0)) as y4,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '05', nvl(n.hej, 0), 0)) as y5,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '06', nvl(n.hej, 0), 0)) as y6,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '07', nvl(n.hej, 0), 0)) as y7,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '08', nvl(n.hej, 0), 0)) as y8,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '09', nvl(n.hej, 0), 0)) as y9,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '10', nvl(n.hej, 0), 0)) as y10,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '11', nvl(n.hej, 0), 0)) as y11,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '12', nvl(n.hej, 0), 0)) as y12,\n"
				+ "       max(n.beiz) as beiz, n.hangid\n"
				+ "  from niandhtqkb n,diancxxb dc,gongysb g,jihkjb j,chezxxb c,chezxxb c1,yunsfsb y,PINZB p,MEIKXXB mk\n"
				+ " where n.diancxxb_id = dc.id(+)\n"
				+"    and n.yunsfsb_id=y.id(+)\n"
				+ "   and n.gongysb_id = g.id(+)\n"
				+ "   and n.jihkjb_id = j.id(+)\n"
				+ "   and n.faz_id = c.id(+)\n"
				+ "   and n.meikxxb_id = mk.id(+)\n"
				+ "   and n.daoz_id = c1.id(+) and n.pinzb_id=p.id(+)  "+strdiancTreeID +"\n"
				+ "   and to_char(n.riq, 'yyyy') = '"+intyear+"'\n"
				+ "   group by (dc.mingc,g.mingc,mk.mingc, j.mingc, p.mingc, c.mingc,c1.mingc,y.mingc,n.hangid)"
		        + "   order by dc.mingc,g.mingc";

		ResultSetList rsl = con.getResultSetList(chaxun);
//		是否显示复制按钮
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("niandhtqkb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.getColumn("gongysb_id").setCenterHeader("供应商");
		
		egu.getColumn("MEIKXXB_ID").setCenterHeader("煤矿单位");
		
		egu.getColumn("diancxxb_id").setCenterHeader("单位");
		
		egu.getColumn("quann").setCenterHeader("全年合计<br>(万吨)");
		
		egu.getColumn("jihkjb_id").setCenterHeader("计划口径");
		
		egu.getColumn("pinzb_id").setCenterHeader("品种");
		
		egu.getColumn("yunsfs").setCenterHeader("运输方式");
		
		egu.getColumn("faz_id").setCenterHeader("发站");
		
		egu.getColumn("daoz_id").setCenterHeader("到站");
		
		egu.getColumn("rez").setCenterHeader("热值<br>(MJ/Kg)");
		//egu.getColumn("rez").setHidden(true);
		((NumberField)egu.getColumn("rez").editor).setDecimalPrecision(2);
		
		egu.getColumn("huiff").setCenterHeader("挥发分<br>(%)");
		//egu.getColumn("huiff").setHidden(true);
		((NumberField)egu.getColumn("huiff").editor).setDecimalPrecision(2);
		
		egu.getColumn("liuf").setCenterHeader("硫分<br>(%)");
		//egu.getColumn("liuf").setHidden(true);
		((NumberField)egu.getColumn("liuf").editor).setDecimalPrecision(2);
		
		egu.getColumn("chebjg").setCenterHeader("车板价<br>(元/吨)");
		((NumberField)egu.getColumn("chebjg").editor).setDecimalPrecision(2);
		
		egu.getColumn("daocj").setCenterHeader("到厂价<br>(元/吨)");
		
		egu.getColumn("biaomdj").setCenterHeader("标煤单价<br>(元/吨)");
		
		egu.getColumn("yunf").setCenterHeader("运费<br>(元/吨)");
		((NumberField)egu.getColumn("yunf").editor).setDecimalPrecision(2);
		
		egu.getColumn("zaf").setCenterHeader("杂费<br>(元/吨)");
		((NumberField)egu.getColumn("zaf").editor).setDecimalPrecision(2);
		
		egu.getColumn("y1").setCenterHeader("一月<br>(万吨)");
		
		egu.getColumn("y2").setCenterHeader("二月<br>(万吨)");
		
		egu.getColumn("y3").setCenterHeader("三月<br>(万吨)");
		
		egu.getColumn("y4").setCenterHeader("四月<br>(万吨)");
		
		egu.getColumn("y5").setCenterHeader("五月<br>(万吨)");
		
		egu.getColumn("y6").setCenterHeader("六月<br>(万吨)");
		
		egu.getColumn("y7").setCenterHeader("七月<br>(万吨)");
		
		egu.getColumn("y8").setCenterHeader("八月<br>(万吨)");
		
		egu.getColumn("y9").setCenterHeader("九月<br>(万吨)");
		
		egu.getColumn("y10").setCenterHeader("十月<br>(万吨)");
		
		egu.getColumn("y11").setCenterHeader("十一月<br>(万吨)");
		
		egu.getColumn("y12").setCenterHeader("十二月<br>(万吨)");
		
		egu.getColumn("beiz").setCenterHeader("备注");
		
		egu.getColumn("hangid").setHidden(true);
		
		egu.getColumn("hangid").setEditor(null);
		egu.getColumn("quann").setEditor(null);
		egu.getColumn("daocj").setEditor(null);
		egu.getColumn("biaomdj").setEditor(null);
		
		//循环设定列的宽度,并设定小数位数
		for( int i=1;i<=12;i++){
			egu.getColumn("y"+i).setWidth(45);
			egu.getColumn("y"+i).setDefaultValue("0");
			((NumberField)egu.getColumn("y"+i).editor).setDecimalPrecision(4);
		}
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("gongysb_id").setWidth(130);
		egu.getColumn("meikxxb_id").setWidth(130);
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("yunsfs").setWidth(60);
		egu.getColumn("faz_id").setWidth(60);
		egu.getColumn("daoz_id").setWidth(60);
		egu.getColumn("quann").setWidth(65);
		egu.getColumn("beiz").setWidth(65);
		egu.getColumn("rez").setWidth(45);
		egu.getColumn("huiff").setWidth(45);
		egu.getColumn("liuf").setWidth(45);
		egu.getColumn("chebjg").setWidth(45);
		egu.getColumn("yunf").setWidth(45);
		egu.getColumn("zaf").setWidth(45);
		egu.getColumn("daocj").setWidth(45);
		egu.getColumn("biaomdj").setWidth(55);
		
//		设定不可编辑列的颜色
		egu.getColumn("quann").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("daocj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("biaomdj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(23);// 设置分页

		// *****************************************设置默认值****************************
		
//		电厂下拉框
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib2 == 3) {// 选电厂只刷新出该电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			
		}		
		
		// *************************下拉框*****************************************88
		// 设置供应商的下拉框
		ComboBox cb_gongys=new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongys);
		cb_gongys.setEditable(true);
		String GongysSql = "select id, mingc from gongysb where leix = 1 order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,new IDropDownModel(GongysSql));
		
//		 设置煤矿的下拉框
		ComboBox cb_meik=new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cb_meik);
		cb_meik.setEditable(true);
		String cb_meikSql = "select id, mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(cb_meikSql));
		
//		设置计划口径下拉框
		ComboBox cb_jihkj=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(cb_jihkj);
		cb_jihkj.setEditable(true);
		egu.getColumn("jihkjb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
		String jihkjSql="select j.id,j.mingc from jihkjb j order by id  ";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjSql));
		egu.getColumn("jihkjb_id").setDefaultValue("重点订货");
		
//		设置品种下拉框
		ComboBox cb_pinz=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(cb_pinz);
		cb_pinz.setEditable(true);
		egu.getColumn("pinzb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
		String pinzSql="select id,mingc from pinzb order by id ";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
//		egu.getColumn("pinzb_id").setDefaultValue("原煤");
		
//      运输方式下拉框
		ComboBox cb_yunsfs=new ComboBox();
		egu.getColumn("yunsfs").setEditor(cb_yunsfs);
		cb_yunsfs.setEditable(true);
		egu.getColumn("yunsfs").editor.setAllowBlank(true);//设置下拉框是否允许为空
		String yunsfsSql="select j.id,j.mingc from yunsfsb j order by id  ";
		egu.getColumn("yunsfs").setComboEditor(egu.gridId, new IDropDownModel(yunsfsSql));
		egu.getColumn("yunsfs").setDefaultValue("铁路");
//		设置发站下拉框
		ComboBox cb_faz=new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String fazSql="select id ,mingc from chezxxb c where c.leib='车站' order by c.mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(fazSql));
		//设置到站下拉框
		ComboBox cb_daoz=new ComboBox();
		egu.getColumn("daoz_id").setEditor(cb_daoz);
		cb_daoz.setEditable(true);
	
		String daozSql="select id,mingc from chezxxb c where c.leib='车站' order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(daozSql));
		
		

		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		// 设置树
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		

//		 设定工具栏下拉框自动刷新
//		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		if (showBtn) {
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("复制上年数据", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}
		
		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			sb.append("e.record.set('DAOCJ',Round(parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))+parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')),2));\n"
					+ "if (e.record.get('REZ')!=0){e.record.set('BIAOMDJ',Round(Round((parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))/1.17+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))*(1-0.07)+parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')))*29.271/parseFloat(e.record.get('REZ')==''?0:e.record.get('REZ')),2),2));}\n"
					+ "\n");
			sb.append("if(!(e.field == 'DIANCXXB_ID'||e.field == 'GONGYSB_ID'||e.field == 'JIHKJB_ID'||e.field == 'FAZ_ID'||e.field == 'DAOZ_ID')){e.record.set('QUANN',parseFloat(e.record.get('Y1')==''?0:e.record.get('Y1'))+parseFloat(e.record.get('Y2')==''?0:e.record.get('Y2'))+parseFloat(e.record.get('Y3')==''?0:e.record.get('Y3'))+parseFloat(e.record.get('Y4')==''?0:e.record.get('Y4'))" +
					" +parseFloat(e.record.get('Y5')==''?0:e.record.get('Y5'))+parseFloat(e.record.get('Y6')==''?0:e.record.get('Y6'))+parseFloat(e.record.get('Y7')==''?0:e.record.get('Y7'))+parseFloat(e.record.get('Y8')==''?0:e.record.get('Y8'))+parseFloat(e.record.get('Y9')==''?0:e.record.get('Y9'))" +
					"  +parseFloat(e.record.get('Y10')==''?0:e.record.get('Y10'))+parseFloat(e.record.get('Y11')==''?0:e.record.get('Y11'))+parseFloat(e.record.get('Y12')==''?0:e.record.get('Y12')) )};");
			
		sb.append("});");
		
		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
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
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=200 scrollamount=2>" + getTbmsg()+ "</marquee>'");
		}
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			setTbmsg(null);
		}
			getSelectData();
	}
	
//	 年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//得到电厂的默认到站
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
//	private String treeid;
	
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
		return getTree().getWindowTreeScript();
	}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
	
}