package com.zhiren.dc.diaoygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2011-09-26
 * 适用范围：国电电力庄河电厂
 * 描述：基于国电电力通用版本(1.1.1.16)为庄河电厂的特殊需求而单独制作的节目
 */
/*
 * 作者：夏峥
 * 修改时间：2011-11-1
 * 适用范围：国电电力庄河电厂
 * 描述：对发货的来煤信息进行修约且保留2位小数
 */
/*
 * 作者：夏峥
 * 修改时间：2011-11-17
 * 适用范围：庄河电厂
 * 描述：修正日估价界面增加判断信息如果超过2日且有数据则界面不显示编辑相关按钮。
 * 		 修正日估价界面增加判断信息如果界面中除运价外，信息不全则不能进行保存。
 */
/*
 * 作者：夏峥
 * 修改时间：2011-11-18
 * 适用范围：庄河电厂
 * 描述：从系统信息表中提取系统设定的日报不可编辑日期，默认为2天
 */
/*
 * 作者：夏峥
 * 修改时间：2011-11-23
 * 适用范围：庄河电厂
 * 描述：用户点击生成按钮时同时生成收耗存日报和分矿日估价信息
 */
/*
 * 作者：夏峥
 * 修改时间：2011-12-20
 * 适用范围：庄河电厂
 * 描述：修正界面显示
 */
/*
 * 作者:夏峥
 * 日期:2013-09-26
 * 修改内容:煤价和运价将不从估收取，默认为0。
 * 			生成时增加煤价税，运价税
 */
/*
 * 作者:夏峥
 * 日期:2013-10-16
 * 修改内容:增加运输方式
 */
public abstract class Shouhcfkb_zh extends BasePage {
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
	}
//	绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//	页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	private void Save() {
		JDBCcon con=new JDBCcon();
		ResultSetList changList=getExtGrid().getModifyResultSet(getChange());
		String diancxxb_id = getTreeid();
		int rs=0;
		String sql="";
		while(changList.next()){
			String id=changList.getString("id");
			double rez=changList.getDouble("rez");
			double meij=changList.getDouble("meij");
			double yunj=changList.getDouble("yunj");
			double meijs=changList.getDouble("meijs");
			double yunjs=changList.getDouble("yunjs");
			double laimsl=changList.getDouble("laimsl");
			double fahb_id=0.0;
			
			if ("0".equals(changList.getString("id"))) {
				sql=
					"insert into shouhcfkb(id,diancxxb_id,fahb_id,rez,meij,meijs,yunj,yunjs,laimsl,riq,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id) values(\n" +
					"getnewid("+diancxxb_id+"),"+diancxxb_id+","+fahb_id+"," +
					rez+","+meij+","+meijs+","+yunj+","+yunjs+","+laimsl+"," +
					DateUtil.FormatOracleDate(getRiq())+"," +
					(getExtGrid().getColumn("meikmc").combo).getBeanId(changList.getString("meikmc"))+"," +
					(getExtGrid().getColumn("pinz").combo).getBeanId(changList.getString("pinz"))+"," +
					(getExtGrid().getColumn("gongysmc").combo).getBeanId(changList.getString("gongysmc")) +"," +
					(getExtGrid().getColumn("JIHKJB_ID").combo).getBeanId(changList.getString("JIHKJB_ID"))+"," +
					(getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(changList.getString("yunsfsb_id"))+
					");\n";
			}else{
				sql=
					"update shouhcfkb\n" +
					"   set rez = "+rez+", meij = "+meij+", yunj = "+yunj+", meijs = "+meijs+", yunjs = "+yunjs+",laimsl="+laimsl+",\n" + 
							"meikxxb_id = " +(getExtGrid().getColumn("meikmc").combo).getBeanId(changList.getString("meikmc"))+"," +
							"pinzb_id = " +(getExtGrid().getColumn("pinz").combo).getBeanId(changList.getString("pinz"))+"," +
							"gongysb_id = " +(getExtGrid().getColumn("gongysmc").combo).getBeanId(changList.getString("gongysmc"))+"," +
							"JIHKJB_ID = " +(getExtGrid().getColumn("JIHKJB_ID").combo).getBeanId(changList.getString("JIHKJB_ID"))+"," +
							"yunsfsb_id = " +(getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(changList.getString("yunsfsb_id"))+"," +
							"FAHB_ID="+fahb_id+
					" where id = "+id+";\n";
			}
		}
		if(sql.length()!=0){
			rs=con.getInsert("begin\n"+sql.toString()+"\nend;");
			if (rs==-1) {
				setMsg("保存失败！");
			}else{
				setMsg("保存成功！");
			}
		}
		con.Close();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + getTreeid();
		return con.getHasIt(sql);
	}
	private void CreateData() {
		long diancxxb_id = Long.parseLong(getTreeid());
		JDBCcon con = new JDBCcon();
//		生成时自动生成日收耗存和分矿数据
		AutoCreateDaily_Report_gd RP=new AutoCreateDaily_Report_gd();
		String rbb=RP.CreateRBB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		String fcb=RP.CreateFCB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		String Smsg="";
		if(rbb.length()>0){
			Smsg+=rbb+"<br>";
		}
		if(fcb.length()>0){
			Smsg+=fcb+"<br>";
		}
		if(Smsg.length()>0){
			setMsg(Smsg);
		}
		con.Close();
	}

	private String Fahxx;
	public void setFahxx(){
		String rslSQL=getFahxxSQL();
		JDBCcon con = new JDBCcon();
		ResultSetList list=con.getResultSetList(rslSQL.toString());
		String rsl="var fahxx=[ ";
		while(list.next()){
			String fahb_id=list.getString("FAHXX");
			String gongys=list.getString("GONGYS");
			String MEIK=list.getString("MEIK");
			String JIHKJ=list.getString("JIHKJ");
			String PINZ=list.getString("PINZ");
			String yunsfs=list.getString("yunsfsb_id");
			String LAIMSL=list.getString("LAIMSL");
			String REZ=list.getString("REZ");
			String MEIJ=list.getString("MEIJ");
			String YUNJ=list.getString("YUNJ");
			String MEIJS=list.getString("MEIJS");
			String YUNJS=list.getString("YUNJS");
			rsl+="[\""+fahb_id+"\",\""+gongys+"\",\""+MEIK+"\",\""+JIHKJ+"\",\""+PINZ+"\",\""+yunsfs+"\",\""+LAIMSL+"\",\""+REZ+"\",\""+MEIJ+"\",\""+MEIJS+"\",\""+YUNJ+"\",\""+YUNJS+"\"],";
		}
		rsl=rsl.substring(0, rsl.length()-1);
		rsl+="];";
		this.Fahxx=rsl;
	}
	
	public String getFahxx(){
		return this.Fahxx;
	}
	
	private String getFahxxSQL(){
		return 
		"SELECT G.MINGC || TO_CHAR(A.DAOHRQ, 'yyyy-mm-dd') || '日' || A.LAIMSL || '吨' FAHXX,\n" +
		"       G.MINGC GONGYS,\n" + 
		"       M.MINGC MEIK,\n" + 
		"       J.MINGC JIHKJ,\n" + 
		"       P.MINGC PINZ,\n" + 
		"       y.MINGC yunsfsb_id,\n" + 
		"       A.DAOHRQ,\n" + 
		"       A.LAIMSL,\n" + 
		"       ROUND_NEW(NVL(DECODE(NVL(A.REZ,0), 0, B.REZ, A.REZ), 0),2) REZ,\n" + 
//		"       ROUND_NEW(NVL(DECODE(A.MEIJ, 0, B.MEIJ, A.MEIJ), 0),2) MEIJ,\n" + 
//		"       ROUND_NEW(NVL(DECODE(A.YUNJ, 0, B.YUNJ, A.YUNJ), 0),2) YUNJ\n" + 
		"       ROUND_NEW(NVL(B.MEIJ, 0),2) MEIJ,\n" + 
		"       ROUND_NEW(NVL(B.MEIJS, 0),2) MEIJS,\n" + 
		"       ROUND_NEW(NVL(B.YUNJ, 0),2) YUNJ,\n" + 
		"       ROUND_NEW(NVL(B.YUNJS, 0),2) YUNJS\n" + 
		"  FROM (SELECT F.GONGYSB_ID,\n" + 
		"               F.MEIKXXB_ID,\n" + 
		"               F.DAOHRQ,\n" + 
		"               F.PINZB_ID,\n" + 
		"               F.JIHKJB_ID,\n" + 
		"               F.yunsfsb_id,\n" + 
		"               SUM(F.LAIMSL) LAIMSL,\n" + 
		"               DECODE(SUM(F.LAIMSL),\n" + 
		"                      0,\n" + 
		"                      0,\n" + 
		"                      SUM(F.LAIMSL * Z.QNET_AR) / SUM(F.LAIMSL)) REZ,\n" + 
		"               DECODE(SUM(F.LAIMSL),\n" + 
		"                      0,\n" + 
		"                      0,\n" + 
		"                      SUM(F.LAIMSL * (NVL(G.MEIJ, 0) + NVL(G.MEIS, 0))) /\n" + 
		"                      SUM(F.LAIMSL)) MEIJ,\n" + 
		"               DECODE(SUM(F.LAIMSL),\n" + 
		"                      0,\n" + 
		"                      0,\n" + 
		"                      SUM(F.LAIMSL * (NVL(G.YUNF, 0) + NVL(G.YUNFS, 0))) /\n" + 
		"                      SUM(F.LAIMSL)) YUNJ\n" + 
		"          FROM ( --取得估收信息\n" + 
		"                SELECT ID, FAHB_ID, MEIJ, MEIS, YUNF, YUNFS\n" + 
		"                  FROM GUSLSB\n" + 
		"                -- 得到同一类型的最大估收ID\n" + 
		"                 WHERE ID IN (SELECT MAX(G.ID)\n" + 
		"                                FROM GUSLSB G,\n" + 
		"                                     --取出同一发货最大类型的估收信息\n" + 
		"                                     (SELECT FAHB_ID, MAX(LEIX) LEIX\n" + 
		"                                        FROM GUSLSB\n" + 
		"                                       GROUP BY FAHB_ID) G2\n" + 
		"                               WHERE G.FAHB_ID = G2.FAHB_ID\n" + 
		"                                 AND G.LEIX = G2.LEIX\n" + 
		"                               GROUP BY G.FAHB_ID)) G,\n" + 
		"               FAHB F,\n" + 
		"               ZHILB Z\n" + 
		"         WHERE F.ZHILB_ID = Z.ID(+)\n" + 
		"           AND F.ID = G.FAHB_ID(+)\n" + 
		"           AND F.DAOHRQ BETWEEN "+DateUtil.FormatOracleDate(getRiq())+"- 10 AND\n" + 
		"               "+DateUtil.FormatOracleDate(getRiq())+"\n" + 
		"         GROUP BY F.GONGYSB_ID,\n" + 
		"                  F.MEIKXXB_ID,\n" + 
		"                  F.DAOHRQ,\n" + 
		"                  F.PINZB_ID,\n" + 
		"                  F.JIHKJB_ID,f.yunsfsb_id) A,\n" + 
		"       --取得日报中每个供应商、煤矿所对应的最后一次录入的数据\n" + 
		"       (SELECT MEIKXXB_ID, GONGYSB_ID, PINZB_ID, JIHKJB_ID,yunsfsb_id,REZ, MEIJ, YUNJ,MEIJS,YUNJS\n" + 
		"          FROM SHOUHCFKB\n" + 
		"         WHERE ID IN\n" + 
		"               (SELECT MAX(ID)\n" + 
		"                  FROM SHOUHCFKB\n" + 
		"                 WHERE RIQ BETWEEN "+DateUtil.FormatOracleDate(getRiq())+"- 10 AND\n" + 
		"                       "+DateUtil.FormatOracleDate(getRiq())+"\n" + 
		"                 GROUP BY MEIKXXB_ID, GONGYSB_ID, PINZB_ID, JIHKJB_ID)) B,\n" + 
		"       GONGYSB G,\n" + 
		"       MEIKXXB M,\n" + 
		"       JIHKJB J,yunsfsb y,\n" + 
		"       PINZB P\n" + 
		" WHERE A.GONGYSB_ID = B.GONGYSB_ID(+)\n" + 
		"   AND A.MEIKXXB_ID = B.MEIKXXB_ID(+)\n" + 
		"   AND A.PINZB_ID = B.PINZB_ID(+)\n" + 
		"   AND A.JIHKJB_ID = B.JIHKJB_ID(+)\n" + 
		"   AND A.yunsfsb_id = B.yunsfsb_id(+)\n" + 
		"   AND A.yunsfsb_id = y.id\n" + 
		"   AND A.GONGYSB_ID = G.ID\n" + 
		"   AND A.MEIKXXB_ID = M.ID\n" + 
		"   AND A.JIHKJB_ID = J.ID\n" + 
		"   AND A.PINZB_ID = P.ID\n" + 
		" ORDER BY A.DAOHRQ";
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private void DelData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("delete from shouhcfkb where diancxxb_id=")
		.append(diancxxb_id)
		.append(" and riq = ")
		.append(CurDate);
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
		con.Close();
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		} 
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
//		界面是否可编辑
		boolean isEditable=true;
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select s.id,\n" +
				"       g.mingc||to_char(f.daohrq,'yyyy-mm-dd')||'日'||f.laimsl||'吨' fahb_id,\n" + 
				"       g.mingc  gongysmc,\n" + 
				"       m.mingc  meikmc,\n" + 
				"       p.mingc  pinz,\n" + 
				"       j.mingc  JIHKJB_ID,\n" + 
				"       y.mingc  yunsfsb_id,\n" + 
				"       f.biaoz,\n" + 
				"       f.ches,\n" + 
				"       s.rez,\n" + 
				"       s.meij,\n" + 
				"       s.meijs,\n" + 
				"       s.yunj,\n" + 
				"       s.yunjs,\n" + 
				"       s.laimsl\n" + 
				"  from shouhcfkb s,\n" + 
				"       meikxxb m,\n" + 
				"       gongysb g,pinzb p,JIHKJB j,yunsfsb y,\n" + 
				"       (select diancxxb_id,\n" + 
				"               daohrq,\n" + 
				"               gongysb_id,pinzb_id,yunsfsb_id,\n" + 
				"               meikxxb_id,\n" + 
				"               nvl(sum(round_new(LAIMSL,0)), 0) LAIMSL,\n" +
				"				nvl(sum(round_new(biaoz,0)), 0) biaoz,\n" + 
				"               nvl(sum(ches), 0) ches\n" + 
				"          from fahb\n" + 
				"			where daohrq="+CurDate+"\n" +
				"         group by diancxxb_id, daohrq, gongysb_id, meikxxb_id,pinzb_id,yunsfsb_id) f\n" + 
				" where s.riq = f.daohrq(+) and s.JIHKJB_ID=j.ID(+)\n" + 
				"   and s.diancxxb_id = f.diancxxb_id(+)\n" + 
				"   and s.meikxxb_id = f.meikxxb_id(+)\n" + 
				"   and s.yunsfsb_id = f.yunsfsb_id(+)\n" + 
				"   and s.yunsfsb_id = y.id(+)\n" + 
				"   and s.pinzb_id = p.id(+)\n" + 
				"   and s.gongysb_id = f.gongysb_id(+)\n" + 
				"   and s.meikxxb_id = m.id(+)\n" + 
				"   and s.gongysb_id = g.id(+)\n" + 
				"   and s.riq = "+CurDate+"\n" + 
				"   and s.diancxxb_id in (select id from diancxxb where id="+getTreeid()+" or fuid="+getTreeid()+")");
		JDBCcon con = new JDBCcon();
//		判断所选日期内是否有数据
		if(con.getHasIt(sb.toString())){
			Date curdate=new Date();
			long t=(curdate.getTime()-DateUtil.getDate(getRiq()).getTime())/(3600*24*1000);
//			如果时间多于2天，那么界面显示编辑相关按钮将为关闭状态
//			从系统信息表中取得日报不可编辑天数信息，默认值为2吨。
			if(t>Long.parseLong(MainGlobal.getXitxx_item("收耗存日报", "日报不可编辑天数", "0", "2"))){
				isEditable=false;
			}
		}
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcfkb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("fahb_id").setHeader("发货信息");
		egu.getColumn("fahb_id").setWidth(200);
		ComboBox cb_fahxx = new ComboBox();
		egu.getColumn("fahb_id").setEditor(cb_fahxx);
		egu.getColumn("fahb_id").setDefaultValue("");
		cb_fahxx.setEditable(true);
		String cb_fahxxSql="SELECT rownum id,\n" +
							"       fahxx\n" + 
							"  FROM (\n" + 
							getFahxxSQL()+
							") F \n" + 
							"UNION\n" + 
							"SELECT 0 ID, DECODE(0, 0, '无发货信息') MINGC FROM DUAL ORDER BY ID";
		egu.getColumn("fahb_id").setReturnId(true);
		egu.getColumn("fahb_id").setComboEditor(egu.gridId, new IDropDownModel(cb_fahxxSql));
		egu.getColumn("fahb_id").setDefaultValue("无发货信息");
		egu.getColumn("fahb_id").setUpdate(false);
		egu.getColumn("fahb_id").editor.setAllowBlank(false);
		
		
		egu.getColumn("gongysmc").setHeader("供货单位");
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysmc").setEditor(cb_gongysb);
		egu.getColumn("gongysmc").setDefaultValue("");
		cb_gongysb.setEditable(true);
		String GongysSql="select id,mingc from gongysb  where leix =1 order by mingc";
		egu.getColumn("gongysmc").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
		
		egu.getColumn("meikmc").setHeader("煤矿单位");
		ComboBox c3 = new ComboBox();
		egu.getColumn("meikmc").setEditor(c3);
		c3.setEditable(true);
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikmc").setComboEditor(egu.gridId,new IDropDownModel(mksb));
				
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(80);
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinz").setEditor(c2);
		c2.setEditable(true);
		String pzsb = "select id,mingc from pinzb order by mingc";
		egu.getColumn("pinz").setComboEditor(egu.gridId,new IDropDownModel(pzsb));
		
		egu.getColumn("JIHKJB_ID").setHeader("统计口径");
		egu.getColumn("JIHKJB_ID").setWidth(80);
		ComboBox ct = new ComboBox();
		egu.getColumn("JIHKJB_ID").setEditor(ct);
		ct.setEditable(true);
		String kjsb = "select id,mingc from JIHKJB order by mingc";
		egu.getColumn("JIHKJB_ID").setComboEditor(egu.gridId,new IDropDownModel(kjsb));
		
		egu.getColumn("YUNSFSB_ID").setHeader("运输方式");
		egu.getColumn("YUNSFSB_ID").setWidth(80);
		ComboBox cy = new ComboBox();
		egu.getColumn("YUNSFSB_ID").setEditor(cy);
		cy.setEditable(true);
		String cysb = "select id,mingc from yunsfsb order by mingc";
		egu.getColumn("YUNSFSB_ID").setComboEditor(egu.gridId,new IDropDownModel(cysb));
		
		egu.getColumn("biaoz").setHeader("标重");
		egu.getColumn("biaoz").setWidth(80);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(40);
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("rez").setHeader("热值(MJ/kg)");
		((NumberField)egu.getColumn("rez").editor).setDecimalPrecision(2);
		egu.getColumn("rez").setWidth(90);
		
		
		egu.getColumn("meij").setHeader("煤价<br>(元/吨)");
		((NumberField)egu.getColumn("meij").editor).setDecimalPrecision(2);
		egu.getColumn("meij").setWidth(80);
		
		egu.getColumn("meijS").setHeader("煤价税<br>(元/吨)");
		egu.getColumn("meijS").setWidth(60);
		egu.getColumn("meijS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("meijS").setEditor(null);
		
		egu.getColumn("yunj").setHeader("运价(元/吨)");
		((NumberField)egu.getColumn("yunj").editor).setDecimalPrecision(2);
		egu.getColumn("yunj").setWidth(80);
		
		egu.getColumn("yunjs").setHeader("运价税<br>(元/吨)");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("yunjs").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunjs").setEditor(null);
		
		egu.getColumn("laimsl").setHeader("来煤数量");
		egu.getColumn("laimsl").setWidth(100);
		
		if("是".equals(MainGlobal.getXitxx_item("日报", "日估价维护标重是否隐藏", getTreeid(), "否"))){
			egu.getColumn("biaoz").setHidden(true);
		}
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('RIQ').value+'的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish); 
		egu.addTbarBtn(gbr);
		
		if(isEditable){
	//		添加按钮
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
	//		生成按钮
			if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("收耗存日报", "分厂别总厂显示生成按钮", diancxxb_id, "否").equals("否")){
				
			}else{
				GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
			}
	//		删除按钮
			GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
	//		保存按钮
			String Condition="\n var mrec=gridDiv_ds.getModifiedRecords();\n"+
							"for(i = 0; i< mrec.length; i++){\n"+
							"	if(mrec[i].get('FAHB_ID') == '无发货信息'){Ext.MessageBox.alert('提示信息','字段 发货信息 应为有效发货信息');return;}\n"+
							"	}\n"+
							"var rec = gridDiv_ds.getRange();\n" +
							"for(i = 0; i< rec.length; i++){if(rec[i].get('REZ') <=0 || rec[i].get('MEIJ') <=0)\n" +
							"{Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,数据未填全');return;}}";
			GridButton gbs = new GridButton(GridButton.ButtonType_Save_condition,"gridDiv",egu.getGridColumns(),"SaveButton",Condition);
			egu.addTbarBtn(gbs);
			
			String yunjslx = MainGlobal.getXitxx_item("结算", "运费税是否是增值税", diancxxb_id, "是");
			String meijs = MainGlobal.getXitxx_item("结算", "煤款税率", diancxxb_id, "0.17");
			String yunjsl = MainGlobal.getXitxx_item("结算", "运费税率", diancxxb_id, "0.07");		
			String yunjzzsl = MainGlobal.getXitxx_item("结算", "运费增值税率", diancxxb_id, "0.11");		
			
	//		grid 计算方法
			StringBuffer script=new StringBuffer();
			script.append(
				"gridDiv_grid.on('afteredit',function(e)\n"+
					"{\n"+
				"rec = e.record;\n"+
				"var fah=rec.get('FAHB_ID');\n"+
				"if(e.field=='FAHB_ID'& fah!='无发货信息')\n"+
				"{\n"+
				"	for (var i=0;i<fahxx.length;i++ ){\n"+
				"		if(fahxx[i][0]==fah){\n"+
				"			rec.set('GONGYSMC',fahxx[i][1]);\n"+
				"			rec.set('MEIKMC',fahxx[i][2]);\n"+
				"			rec.set('JIHKJB_ID',fahxx[i][3]);\n"+
				"			rec.set('PINZ',fahxx[i][4]);\n"+
				"			rec.set('YUNSFSB_ID',fahxx[i][5]);\n"+
				"			rec.set('REZ',fahxx[i][7]);\n"+
				"			rec.set('MEIJ',fahxx[i][8]);\n"+
				"			rec.set('MEIJS',fahxx[i][9]);\n"+
				"			rec.set('YUNJ',fahxx[i][10]);\n"+
				"			rec.set('YUNJS',fahxx[i][11]);\n"+
				"			}\n"+
				"	}	\n"+
				"}\n"+
				"  if(e.field=='MEIJ'){\n" + 
				"    var meijs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)-(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)/(1+" + meijs + ")),2);\n" + 
				"    gridDiv_ds.getAt(i).set('MEIJS',meijs);\n" + 
				"  }\n"+
				"  if(e.field=='YUNJ'){\n" + 
				"    var yunjs=0,i=0;\n" + 
				"    i=e.row;\n");
				if(yunjslx.equals("是")){
					script.append("if(gridDiv_ds.getAt(i).get('YUNSFSB_ID')=='铁路'){  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);}\n" ); 
					script.append(" else{  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)-(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)/(1+" + yunjzzsl + ")),2);}\n");
				}else{
					script.append("yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);\n" );
				}
				script.append("    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n");
				
				script.append("  if(e.field=='YUNSFSB_ID'){\n" + 
						"    var yunjs=0,i=0;\n" + 
						"    i=e.row;\n"); 
				if(yunjslx.equals("是")){
					script.append("if(gridDiv_ds.getAt(i).get('YUNSFSB_ID')=='铁路'){  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);}\n" ); 
					script.append(" else{  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)-(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)/(1+" + yunjzzsl + ")),2);}\n");
				}else{
					script.append("yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);\n" );
				}
				script.append("    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n");
				
				script.append("});\n");
			egu.addOtherScript(script.toString());
		}
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('RIQ').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将同时覆盖:日收耗存和日估价<br>")
			.append(cnDate).append("的已存数据，是否继续？");
		}else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
			setChangbModels();
			getSelectData();
			setTreeid(null);
		}
		setFahxx();
	}
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
}
