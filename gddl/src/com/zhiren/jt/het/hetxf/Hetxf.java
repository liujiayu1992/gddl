package com.zhiren.jt.het.hetxf;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.webservice.InterCom_dt;
/*
 * 作者：王磊
 * 时间：2009-12-21
 * 描述：重写下发方法
 */
public class Hetxf extends BasePage  {
	private String riq1;
	private String riq2;
	public String getRiq1() {
		if(riq1==null||riq1.equals("")){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), 0, 1);
			riq1=DateUtil.FormatDate(stra.getTime());
		}
		return riq1;
	}
	public void setRiq1(String riq1) {
		this.riq1 = riq1;
	}
	public String getRiq2() {
		if(riq2==null||riq2.equals("")){
			riq2=DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}
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
	private boolean RefurbishButton;
	public void RefurbishButton(IRequestCycle cycle) {
		RefurbishButton = true;
	}
	public void xiafButton(IRequestCycle cycle) {
		((Visit) getPage().getVisit()).setboolean2(true);
	}
	public void shancButton(IRequestCycle cycle) {
		((Visit) getPage().getVisit()).setboolean3(true);
	}
	public void chongxxfButton(IRequestCycle cycle) {
		((Visit) getPage().getVisit()).setboolean4(true);
	}
	public void submit(IRequestCycle cycle) {
		if(RefurbishButton){
			Refurbish();
			RefurbishButton=false;
		}
		if(((Visit) getPage().getVisit()).getboolean2()){
			Issued(false);
			((Visit) getPage().getVisit()).setboolean2(false);
		}
		if(((Visit) getPage().getVisit()).getboolean3()){
			shanc();
			((Visit) getPage().getVisit()).setboolean3(false);
		}
		if(((Visit) getPage().getVisit()).getboolean4()){
			Issued(true);
			((Visit) getPage().getVisit()).setboolean4(false);
		}
	}
	
//	判断接口账户表是否配置成功
	private String isSetSuccess(JDBCcon con, String strcontractid){
		String strSetSuccess = null;
		String sql = "select a.hetbh,d.quanc,nvl(z.id,0) zt from\n" +
			"(select distinct s.diancxxb_id,h.hetbh from hetb h, hetslb s\n" + 
			"where h.id = s.hetb_id and h.id = "+strcontractid+") a,diancxxb d, dianczhb z\n" + 
			"where a.diancxxb_id = d.id and a.diancxxb_id = z.diancxxb_id(+)";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			if("0".equals(rs.getString("id"))){
				strSetSuccess = "合同:" + rs.getString("hetbh") + ",下发至" + rs.getString("quanc") +" 未进行配置，请检查。";
			}
		}
		rs.close();
		return strSetSuccess;
	}
	
	private void AddMainContract(JDBCcon con, List lisSql, String strcontractid, boolean anew){
		if(anew){
			lisSql.add("delete from hetb where id="+strcontractid);
		}
		String sql="select hetb.ID,hetb.FUID,DIANCXXB_ID,HETBH,to_char(QIANDRQ,'yyyy-mm-dd')QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
		"GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,gongysb.bianm HETGYSBID,\n" + 
		" gs.bianm GONGYSB_ID,to_char(QISRQ,'yyyy-mm-dd')QISRQ,to_char(GUOQRQ,'yyyy-mm-dd')GUOQRQ,HETB_MB_ID,j.bianm JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,nvl(HETJJFSB_ID,0)HETJJFSB_ID,MEIKMCS,XIAF\n" + 
		"from hetb,gongysb,gongysb gs,jihkjb j\n" + 
		"where hetb.jihkjb_id=j.id and hetb.HETGYSBID=gongysb.id and hetb.GONGYSB_ID=gs.id and hetb.id="+strcontractid;
		ResultSetList rs=con.getResultSetList(sql);
		while(rs.next()){
			sql="insert into hetb(ID,FUID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
			"GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,\n" + 
			"GONGYSB_ID,QISRQ,GUOQRQ,HETB_MB_ID,JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,HETJJFSB_ID,MEIKMCS,XIAF\n" + 
			")values(\n" + 
			 rs.getString("ID")+","+
			 rs.getString("FUID")+","+
			 rs.getString("DIANCXXB_ID")+",'"+
			 rs.getString("HETBH")+"',to_date('"+
			 rs.getString("QIANDRQ")+"','yyyy-mm-dd'),'"+
			 rs.getString("QIANDDD")+"','"+
			 rs.getString("GONGFDWMC")+"','"+
			 rs.getString("GONGFDWDZ")+"','"+
			 rs.getString("GONGFDH")+"','"+
			 rs.getString("GONGFFDDBR")+"','"+
			 rs.getString("GONGFWTDLR")+"','"+
			 rs.getString("GONGFDBGH")+"','"+
			 rs.getString("GONGFKHYH")+"','"+
			 rs.getString("GONGFZH")+"','"+
			 rs.getString("GONGFYZBM")+"','"+
			 rs.getString("GONGFSH")+"','"+
			 rs.getString("XUFDWMC")+"','"+
			 rs.getString("XUFDWDZ")+"','"+
			 rs.getString("XUFFDDBR")+"','"+
			 rs.getString("XUFWTDLR")+"','"+
			 rs.getString("XUFDH")+"','"+
			 rs.getString("XUFDBGH")+"','"+
			 rs.getString("XUFKHYH")+"','"+
			 rs.getString("XUFZH")+"','"+
			 rs.getString("XUFYZBM")+"','"+
			 rs.getString("XUFSH")+"',(select id from gongysb where bianm='"+
			 rs.getString("HETGYSBID")+"' or shangjgsbm='"+rs.getString("HETGYSBID")+"'),(select id from gongysb where bianm='"+
			 rs.getString("GONGYSB_ID")+"' or shangjgsbm='"+rs.getString("GONGYSB_ID")+"'),to_date('"+
			 rs.getString("QISRQ")+"','yyyy-mm-dd'),to_date('"+
			 rs.getString("GUOQRQ")+"','yyyy-mm-dd'),"+
			 rs.getString("HETB_MB_ID")+",(select id from jihkjb where bianm='"+
			 rs.getString("JIHKJB_ID")+"'),"+
			 rs.getString("LIUCZTB_ID")+","+
			 rs.getString("LIUCGZID")+","+
			 rs.getString("LEIB")+","+
			 rs.getString("HETJJFSB_ID")+",'"+
			 rs.getString("MEIKMCS")+"',"+
			 rs.getString("XIAF")+
			")";
			lisSql.add(sql);
		}
		rs.close();
	}
	
	private void AddQuantityContract(JDBCcon con, List lisSql, List lisDc, String strcontractid, boolean anew){
		if(anew){
			lisSql.add("delete from hetslb where hetb_id="+strcontractid);
		}
		String sql="select hetslb.ID,pinzb.mingc PINZB_ID,YUNSFSB_ID,fz.mingc FAZ_ID,dz.mingc DAOZ_ID,DIANCXXB_ID,to_char(RIQ,'yyyy-mm-dd')riq,HETL,HETB_ID,hetslb.ZHUANGT\n" +
		"from hetslb,pinzb,chezxxb fz,chezxxb dz\n" + 
		"where hetslb.faz_id=fz.id and hetslb.daoz_id=dz.id and hetslb.pinzb_id=pinzb.id and hetslb.hetb_id="+strcontractid+" order by hetslb.diancxxb_id";
		ResultSetList rs=con.getResultSetList(sql);
		
		if(rs.getRows()>0){
//			合同有数量
			while(rs.next()){
				//下发多个电厂
				MainGlobal.addStr2ListNorepeat(lisDc, rs.getString("DIANCXXB_ID"));
				sql="insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID,ZHUANGT)values(\n" + 
				 rs.getString("ID")+",(select id from pinzb where mingc='"+
				 rs.getString("PINZB_ID")+"'),"+
				 rs.getString("YUNSFSB_ID")+",(select id from chezxxb where mingc='"+
				 rs.getString("FAZ_ID")+"'),(select id from chezxxb where mingc='"+
				 rs.getString("DAOZ_ID")+"'),"+
				 rs.getString("DIANCXXB_ID")+",to_date('"+
				 rs.getString("RIQ")+"','yyyy-mm-dd'),"+
				 rs.getString("HETL")+","+
				 rs.getString("HETB_ID")+","+
				 rs.getString("ZHUANGT")+
				")";
				lisSql.add(sql);
			}
		}else{
//			找主合同id
			
			sql = "select distinct diancxxb_id from hetslb where hetb_id in (select fuid from hetb where id="+strcontractid+")";
			rs = con.getResultSetList(sql);
			while(rs.next()){
				
				MainGlobal.addStr2ListNorepeat(lisDc, rs.getString("DIANCXXB_ID"));
			}
		}
		rs.close();
	}
	
	private void AddQualityContract(JDBCcon con, List lisSql, String strcontractid, boolean anew){
		if(anew){
			lisSql.add("delete hetzlb where hetb_id="+strcontractid);
		}
		String sql="select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID\n" +
		"from hetzlb\n" + 
		"where hetzlb.hetb_id="+strcontractid;
		ResultSetList rs=con.getResultSetList(sql);
		while(rs.next()){
			sql="insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values(\n" + 
			rs.getString("ID")+",'"+
			rs.getString("ZHIBB_ID")+"','"+
			rs.getString("TIAOJB_ID")+"','"+
			rs.getString("SHANGX")+"','"+
			rs.getString("XIAX")+"','"+
			rs.getString("DANWB_ID")+"',"+
			rs.getString("HETB_ID")+
			")";
			lisSql.add(sql);
		}	
		rs.close();
	}
	
	private void AddCostContract(JDBCcon con, List lisSql, String strcontractid, boolean anew){
		if(anew){
			lisSql.add("delete hetjgb where hetb_id="+strcontractid);
		}
		String sql="select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
		"YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,fengsjj\n" + 
		"from hetjgb\n" + 
		"where hetjgb.hetb_id="+strcontractid;
		ResultSetList rs=con.getResultSetList(sql);
		while(rs.next()){
			sql = "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" + 
			"YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,FENGSJJ)values(\n" + 
			rs.getString("ID")+",'"+
			rs.getString("ZHIBB_ID")+"','"+
			rs.getString("TIAOJB_ID")+"','"+
			rs.getString("SHANGX")+"','"+
			rs.getString("XIAX")+"','"+
			rs.getString("DANWB_ID")+"','"+
			rs.getString("JIJ")+"','"+
			rs.getString("JIJDWID")+"','"+
			rs.getString("HETJSFSB_ID")+"','"+
			rs.getString("HETJSXSB_ID")+"','"+
			rs.getString("YUNJ")+"','"+
			rs.getString("YUNJDW_ID")+"','"+
			rs.getString("YINGDKF")+"','"+
			rs.getString("YUNSFSB_ID")+"','"+
			rs.getString("ZUIGMJ")+"','"+
			rs.getString("HETB_ID")+"','"+
			rs.getString("HETJJFSB_ID")+"','"+
			rs.getString("FENGSJJ")+
			"')";
			lisSql.add(sql);
		}	
		rs.close();
	}
	
	private void AddPricesInDeContract(JDBCcon con, List lisSql, String strcontractid, boolean anew){
		if(anew){
			lisSql.add("delete hetzkkb where hetb_id="+strcontractid);
		}
		String sql="select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" +
		"JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID\n" + 
		"from hetzkkb\n" + 
		"where hetzkkb.hetb_id="+strcontractid;
		ResultSetList rs=con.getResultSetList(sql);
		while(rs.next()){
			sql = "insert into hetzkkb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" + 
			"JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID)values(\n" + 
			rs.getString("ID")+",'"+
			rs.getString("ZHIBB_ID")+"','"+
			rs.getString("TIAOJB_ID")+"','"+
			rs.getString("SHANGX")+"','"+
			rs.getString("XIAX")+"','"+
			rs.getString("DANWB_ID")+"','"+
			rs.getString("JIS")+"','"+
			rs.getString("JISDWID")+"','"+
			rs.getString("KOUJ")+"','"+
			rs.getString("KOUJDW")+"','"+
			rs.getString("ZENGFJ")+"','"+
			rs.getString("ZENGFJDW")+"','"+
			rs.getString("XIAOSCL")+"','"+
			rs.getString("JIZZKJ")+"','"+
			rs.getString("JIZZB")+"','"+
			rs.getString("CANZXM")+"','"+
			rs.getString("CANZXMDW")+"','"+
			rs.getString("CANZSX")+"','"+
			rs.getString("CANZXX")+"','"+
			rs.getString("HETJSXSB_ID")+"','"+
			rs.getString("YUNSFSB_ID")+"','"+
			rs.getString("BEIZ")+"',"+
			rs.getString("HETB_ID")+
			")";
			lisSql.add(sql);
		}	
		rs.close();
	}
	
	private void AddContentContract(JDBCcon con, List lisSql, String strcontractid, boolean anew){
		if(anew){
			lisSql.add("delete hetwzb where hetb_id="+strcontractid);
		}
		String sql = "select ID,WENZNR,HETB_ID\n" +
		"from hetwzb\n" + 
		"where hetwzb.hetb_id="+strcontractid;
		ResultSetList rs=con.getResultSetList(sql);
		while(rs.next()){
			sql = "insert into hetwzb(ID,WENZNR,HETB_ID)values(\n" + 
			rs.getString("ID")+",'"+
			rs.getString("WENZNR")+"',"+
			rs.getString("HETB_ID")+
			")";
			lisSql.add(sql);
		}
		rs.close();
	}
	//把选择的合同插入相应的电厂，如果成功则更新xiaf状态
    private void Issued(boolean anew){//增加，如果是已经下发过的要更新主合同、增加删除分合同，这样避免在建立约束的情况下，无法用删增方法进行修改合同。
    	/*  取得选中的要下发的合同信息 */
    	ResultSetList Rs=getExtGrid().getModifyResultSet(getChange());
    	JDBCcon con = new JDBCcon();
    	InterCom_dt xiaf=new InterCom_dt();
    	String strErr="";//失败的合同编号
    	while(Rs.next()){
    		String strcontractid = Rs.getString("id");
    		String strcontractcode = Rs.getString("hetbh");
    		String strError = isSetSuccess(con,strcontractid);
    		if(strError != null){
    			strErr += strError + "\n";
    			return;
    		}
    		List lisSql = new ArrayList();
    		List lisDcid = new ArrayList();
    		/* 写入合同主体信息的SQL */
    		AddMainContract(con, lisSql, strcontractid, anew);
    		/* 写入合同数量信息的SQL 并且确定下发电厂  */
    		AddQuantityContract(con, lisSql, lisDcid, strcontractid, anew);
    		/* 写入合同质量信息的SQL */
    		AddQualityContract(con, lisSql, strcontractid, anew);
    		/* 写入合同价格信息的SQL */
    		AddCostContract(con, lisSql, strcontractid, anew);
    		/* 写入合同增扣款信息的SQL */
    		AddPricesInDeContract(con, lisSql, strcontractid, anew);
    		/* 写入合同内容信息的SQL */
    		AddContentContract(con, lisSql, strcontractid, anew);
    		
    		String[] StrArrSql = new String[lisSql.size()];
    		for(int i = 0; i< lisSql.size(); i++){
    			StrArrSql[i] = (String)lisSql.get(i);
    		}
    		for(int i = 0; i< lisDcid.size(); i++){
    			String[] resul = xiaf.sqlExe((String)lisDcid.get(i), StrArrSql, true);
				if(!resul[0].equals("true")){
					strErr += strcontractcode+":"+resul[0].replaceAll("\n", "")+";";
					return;
				}
    		}
    		con.getUpdate("update hetb set xiaf=1 where id="+strcontractid);
    	}
    	Rs.close();
    	con.Close();
		Refurbish();
	}
    
	private void shanc(){
		Visit visit = (Visit) getPage().getVisit();
		ResultSetList result=visit.getExtGrid1().getModifyResultSet(getChange());
		JDBCcon con=new JDBCcon();
		InterCom_dt xiaf=new InterCom_dt();
		String strSuc="";
		String strErr="";
		while(result.next()){//逐个处理每个合同
			String hetb_id=result.getString("id");
			String sql=" select max(diancxxb_id)diancxxb_id from hetslb where hetb_id= "+hetb_id;
			String diancxxb_id="";
			ResultSetList rs1=con.getResultSetList(sql);
			if(rs1.next()){
				diancxxb_id=rs1.getString("diancxxb_id");
			}
			String hetbh=result.getString("hetbh");
			String[] resul=null; 
			String[] sqls=new String[6]; 
			sqls[0]="delete from hetb where id="+hetb_id;
			sqls[1]="delete from hetslb where hetb_id="+hetb_id;
			sqls[2]="delete hetzlb where hetb_id="+hetb_id;
			sqls[3]="delete hetjgb where hetb_id="+hetb_id;
			sqls[4]="delete hetzkkb where hetb_id="+hetb_id;
			sqls[5]="delete hetwzb where hetb_id="+hetb_id;
			resul=xiaf.sqlExe(diancxxb_id, sqls, true);
			if(resul[0].equals("true")){
				strSuc+=hetbh+":删除成功;";
				con.getUpdate("update hetb set xiaf=0 where id="+hetb_id);
			}else{
				strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
			}
		}
		setMsg(strSuc+strErr);
		Refurbish();
	}
	private void Refurbish(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con= new JDBCcon();
		String leib="";
		boolean isContrl= MainGlobal.getXitxx_item("合同","合同下发是否过滤采购合同",String.valueOf(visit.getDiancxxb_id()), "否").equals("是");
		if(isContrl){
			leib=" and h.leib !=2 \n ";
		}
		String sql=
			"select h.id,h.hetbh,h.gongfdwmc,h.xufdwmc,j.mingc ,to_char(h.qisrq,'yyyy-mm-dd')riq1,to_char(h.guoqrq,'yyyy-mm-dd')riq2,sum(s.hetl)hetl \n" +
			"from hetb h,hetslb s,jihkjb j,diancxxb d,hetb_mb\n" + 
			"where h.id=s.hetb_id(+)  and h.jihkjb_id=j.id\n" +
			" and h.hetb_mb_id=hetb_mb.id and hetb_mb.xiafmb=1"+leib+
			" and h.diancxxb_id=d.id and to_char(h.qisrq,'YYYY-MM-DD')>='"+getRiq1()+"'and to_char(h.qisrq,'YYYY-MM-DD')<='"+getRiq2()+"' "+
			" and h.diancxxb_id in(" +
			        	"select id from diancxxb\n" +
			        	"start with id=" +visit.getDiancxxb_id()+
			        	"connect by  fuid= prior id) and h.xiaf="+getzhuangtSelectValue().getId()+" and h.liucztb_id=1"+//and d.jib=3
			"group by h.diancxxb_id, h.id, h.hetbh,h.gongfdwmc,h.xufdwmc,j.mingc ,to_char(h.qisrq,'yyyy-mm-dd'),to_char(h.guoqrq,'yyyy-mm-dd') order by h.diancxxb_id";

	    ResultSetList rsl = con.getResultSetList(sql);
		visit.setExtGrid1(new ExtGridUtil("gridDiv", rsl));
		ExtGridUtil egu = visit.getExtGrid1();
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(1, new GridColumn(GridColumn.ColType_Check));
//		设置每页显示行数
		egu.addPaging(25);
		//设置页面宽高与body适应
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("hetbh").setHeader("合同编号");
		egu.getColumn("hetbh").setWidth(150);
		egu.getColumn("hetbh").setEditor(null);
		
		egu.getColumn("gongfdwmc").setHeader("供方");
		egu.getColumn("gongfdwmc").setWidth(300);
		egu.getColumn("gongfdwmc").setEditor(null);
		
		egu.getColumn("xufdwmc").setHeader("需方");
		egu.getColumn("xufdwmc").setWidth(200);
		egu.getColumn("xufdwmc").setEditor(null);
		
		egu.getColumn("mingc").setHeader("计划口径");
		egu.getColumn("mingc").setWidth(70);
		egu.getColumn("mingc").setEditor(null);
		
		egu.getColumn("riq1").setHeader("起始日期");
		egu.getColumn("riq1").setWidth(70);
		egu.getColumn("riq1").setEditor(null);
		
		egu.getColumn("riq2").setHeader("过期日期");
		egu.getColumn("riq2").setWidth(70);
		egu.getColumn("riq2").setEditor(null);
		
		egu.getColumn("hetl").setHeader("合同量");
		egu.getColumn("hetl").setWidth(70);
		egu.getColumn("hetl").setEditor(null);
		
		String str=
       		" var url = 'http://'+document.location.host+document.location.pathname;"+
            "var end = url.indexOf(';');"+
			"url = url.substring(0,end);"+
       	    "url = url + '?service=page/' + 'Shenhrz&hetb_id='+record.data['ID'];";
		egu.getColumn("hetbh").setRenderer(
				"function(value,p,record){" +str+
				"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>\"+value+\"</a>\"}"
		);	
		//
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
//		GridButton xiafBut = new GridButton("下发","function (){document.getElementById('xiafButton').click();}");
		
		egu.addTbarText("起始日期") ;
		DateField riq1 = new DateField();
		riq1.setValue(this.getRiq1());
		riq1.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		riq1.setId("riq1");
		riq1.emptyText="起始日期";
		egu.addToolbarItem(riq1.getScript());
		DateField riq2 = new DateField();
		riq2.setValue(this.getRiq2());
		riq2.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		riq2.setId("riq2");
		riq2.emptyText="过期日期";
		egu.addToolbarItem(riq2.getScript());
		egu.addTbarText("状态:");
		ComboBox comb2=new ComboBox();
		comb2.setId("zhuangt");
		comb2.setWidth(100);
		comb2.setTransform("zhuangtSelect");
		comb2.setLazyRender(true);//动态绑定weizSelect//cb.setListeners("select:function(){document.Form0.submit();}");
		comb2.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarBtn(refurbish);
		//如果是已下发
		if(getzhuangtSelectValue().getId()==1){
			egu.addToolbarButton("重新下发",GridButton.ButtonType_SubmitSel, "chongxxfButton");
			egu.addToolbarButton("删除",GridButton.ButtonType_SubmitSel, "shancButton");
		}else{//否则
			egu.addToolbarButton("下发",GridButton.ButtonType_SubmitSel, "xiafButton");
		}
		setExtGrid(egu);
		con.Close();
	}
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setboolean1(false);
			((Visit) getPage().getVisit()).setboolean2(false);
			((Visit) getPage().getVisit()).setboolean3(false);
			((Visit) getPage().getVisit()).setboolean4(false);
			Refurbish();
		}
		if(((Visit) getPage().getVisit()).getboolean1()){
			Refurbish();
		}
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
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	 //状态
    public IDropDownBean getzhuangtSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getzhuangtSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
 
    public void setzhuangtSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }
    public void setzhuangtSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getzhuangtSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getweizSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getweizSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(0,"未下发"));
        list.add(new IDropDownBean(1,"已下发"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
	
}
