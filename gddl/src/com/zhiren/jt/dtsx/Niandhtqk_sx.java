package com.zhiren.jt.dtsx;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * @author huo1
 *2010-03-02针对大唐陕西分公司调运部提出的调运日报制作的按煤矿地区或者煤矿单位录入年计划的页面
 *数据保存至原有数据库表niandhtqkb,代码基本复制于Niandhtqk.java
 */
public  class Niandhtqk_sx extends BasePage implements PageValidateListener {
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
		int r=0;
		while (rsl.next()){
			
			
			double rez=rsl.getDouble("rez");
			double huiff=rsl.getDouble("huiff");
			double liuf=rsl.getDouble("liuf");
			double chebjg=rsl.getDouble("chebjg");
			double yunf=rsl.getDouble("yunf");
			double zaf=rsl.getDouble("zaf");
			
			strYear=getNianfValue().getValue() +"-01-01";
			strYearEnd=getNianfValue().getValue() +"-12-01";
			if(!"0".equals(rsl.getString("id"))){
				
				String bb="select diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,faz_id,daoz_id,yunsfsb_id from niandhtqkb where id="+rsl.getLong("id")+"";
//				System.out.println(bb);
				ResultSetList dele = con.getResultSetList(bb);
				long diancxxb_id=0;
				long gongysb_id=0;
				long jihkjb_id=0;
				long pinzb_id=0;
				long faz_id=0;
				long daoz_id=0;
				long yunsfsb_id=0;
				if(dele.next()){
					diancxxb_id = dele.getLong("diancxxb_id");
					gongysb_id = dele.getLong("gongysb_id");
					jihkjb_id = dele.getLong("jihkjb_id");
					pinzb_id = dele.getLong("pinzb_id");
					faz_id = dele.getLong("faz_id");
					daoz_id = dele.getLong("daoz_id");
					yunsfsb_id=dele.getLong("yunsfsb_id");
				}
				
				String aa="delete from niandhtqkb " +
				"where (riq>=to_date('"+strYear +"','yyyy-mm-dd') and riq<=to_date('"+strYearEnd +"','yyyy-mm-dd'))" +
				" and gongysb_id="+gongysb_id+" " +
				" and diancxxb_id="+diancxxb_id+""+
				" and jihkjb_id="+jihkjb_id+"";
				if(pinzb_id==0 ){
					aa+=" and pinzb_id is null";
				}else{
					aa+=" and pinzb_id="+pinzb_id+"";
				}
			
				
				aa+=" and faz_id="+faz_id+""+
				" and yunsfsb_id="+yunsfsb_id+""+
				" and daoz_id="+daoz_id+"";
//				System.out.println(aa);
				flag=con.getDelete(aa);
				
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
							+ aa);
					setMsg(ErrorMessage.DeleteDatabaseFail);
					return;
				}
		   }
			hangid=MainGlobal.getNewID(100);//行ID
			for (int i=1;i<=12;i++){
				htl=rsl.getDouble("Y"+i);
				
				//if (htl!=0){
					strDate=getNianfValue().getValue() +"-"+ i +"-01";
					String bb="insert into niandhtqkb(id,diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,faz_id,daoz_id,beiz,hej,riq,yunsfsb_id,REZ,HUIFF,LIUF,CHEBJG,YUNF,ZAF,hangid) values(" +
					"getNewId(getDiancId('"+ rsl.getString("diancxxb_id")+"')),"+
					"getDiancId('"+ rsl.getString("diancxxb_id")+"'),"+
					"getGongysId_sx('"+rsl.getString("gongysb_id")+"'),"+
					"getJihkjbId('"+rsl.getString("jihkjb_id")+"'),"+
					"getPinzbId('"+rsl.getString("pinzb_id")+"'),"+
					"getChezxxbId('"+rsl.getString("faz_id")+"'),"+
					"getChezxxbId('"+rsl.getString("daoz_id")+"'),"+
					"'"+rsl.getString("beiz")+"',"+
					htl+"," +
					"to_date('"+strDate+"','yyyy-mm-dd'),"+
					"getYunsfsbId('"+rsl.getString("yunsfs")+"'),"+rez+","+huiff+","+liuf+","+chebjg+","+yunf+","+zaf+","+hangid+")";
//					System.out.println(bb);
					flag=con.getInsert(bb);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
								+ bb);
						setMsg(ErrorMessage.InsertDatabaseFail);
						return;
					}
				//}
			}
		}
		//删除数据
		rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			//System.out.println("保存时进入删除");
			strYear=getNianfValue().getValue() +"-01-01";
			strYearEnd=getNianfValue().getValue() +"-12-01";
			String cc ="delete from niandhtqkb " +
			"where (riq>=to_date('"+strYear +"','yyyy-mm-dd') and riq<=to_date('"+strYearEnd +"','yyyy-mm-dd'))" +
			"and gongysb_id=getGongysId('"+rsl.getString("gongysb_id")+"') " +
			"and diancxxb_id=getDiancId('"+ rsl.getString("diancxxb_id")+"')" +
			"and jihkjb_id=getJihkjbId('"+ rsl.getString("jihkjb_id")+"')";
			if(rsl.getString("pinzb_id").equals("")){
				cc+=" and pinzb_id is null";
			}else{
				cc+=" and pinzb_id=getPinzbId('"+ rsl.getString("pinzb_id")+"')";
			}
//			"and pinzb_id=getPinzbId('"+ rsl.getString("pinzb_id")+"')"+
			cc+=" and faz_id=getChezxxbId('"+ rsl.getString("faz_id")+"')"+
			"and daoz_id=getChezxxbId('"+ rsl.getString("daoz_id")+"')"+
			"and yunsfsb_id=getYunsfsbId('"+rsl.getString("yunsfs")+"')";
//			System.out.println(cc);
			flag=con.getDelete(cc);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
						+ cc);
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
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_CopyButton){
			_CopyButton=false;
			CoypLastYearData();
		}
	}
	
	public void CoypLastYearData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		intyear=intyear-1;
		//供应商条件
		
		
		//
		String strdiancTreeID = "";
		
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strdiancTreeID="";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strdiancTreeID = " and d.fuid= " +this.getTreeid();
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancTreeID=" and d.id= " +this.getTreeid();
			
		}
		
		String copyData = "select n.*\n"
				+ "  from niancgjhb n, diancxxb d,gongysb g\n"
				+ " where n.diancxxb_id = d.id(+)\n"
				+ "   and n.gongysb_id=g.id(+)\n"
				+ "   "+strdiancTreeID+"\n"
				
				+ "   and to_char(n.riq, 'yyyy') = '"+intyear+"'";

		//System.out.println("复制去年的数据:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(copyData);
		while(rslcopy.next()){
			
			long gongysb_id=rslcopy.getLong("gongysb_id");
			long diancxxb_id=rslcopy.getLong("diancxxb_id");
			long hej=rslcopy.getLong("hej");
			Date riq=rslcopy.getDate("riq");
			int year=DateUtil.getYear(riq);
			int yue=DateUtil.getMonth(riq);
			int day=DateUtil.getDay(riq);
			
			String strriq=year+1+"-"+yue+"-"+day;
			String _id = MainGlobal.getNewID(((Visit) getPage()
					.getVisit()).getDiancxxb_id());
			con.getInsert("insert into niancgjhb(id,gongysb_id,diancxxb_id,hej,riq) values(" +
					_id+","+gongysb_id +","+ diancxxb_id+","+hej+","+"to_date('"+strriq+"','yyyy-mm-dd'))");
					
		}
		
		con.Close();
		
		
	}
	
	


	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份下拉框
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
			strdiancTreeID = " and (dc.fuid= " +this.getTreeid()+" or dc.id=" +this.getTreeid()+")";
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancTreeID=" and dc.id= " +this.getTreeid();
			
		}
		String chaxun = "select min(n.id) as id,\n"
				+ "       dc.mingc as diancxxb_id,\n"
				+ "       g.mingc as gongysb_id,\n"
				+ "       j.mingc as jihkjb_id,\n"
				+ "       p.mingc as pinzb_id,\n"
				+ "       c.mingc as faz_id,\n"
				+"        y.mingc as yunsfs,\n"
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
				+ "       max(n.beiz) as beiz\n"
				+ "  from niandhtqkb n,diancxxb dc,"+
"(select id,mingc from (\n" +
"select id,'***'||mingc||'***' as mingc from gongysb where leix=0\n" + 
"union\n" + 
"select id,mingc as mmingc from meikxxb\n" + 
")) g,jihkjb j,chezxxb c,chezxxb c1,yunsfsb y,PINZB p\n"
				+ " where n.diancxxb_id = dc.id(+)\n"
				+"    and n.yunsfsb_id=y.id(+)\n"
				+ "   and n.gongysb_id = g.id(+)\n"
				+ "   and n.jihkjb_id = j.id(+)\n"
				+ "   and n.faz_id = c.id(+)\n"
				+ "   and n.daoz_id = c1.id(+) and n.pinzb_id=p.id(+)  "+strdiancTreeID +"\n"
				+ "   and to_char(n.riq, 'yyyy') = '"+intyear+"'\n"
				+ "   group by (dc.mingc,g.mingc, j.mingc, p.mingc, c.mingc,c1.mingc,y.mingc)"
		        + "   order by dc.mingc,g.mingc";

	
		
//		System.out.println(chaxun);
		//System.out.println("----------------------------------------");
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("niandhtqkb");
		egu.setWidth("bodyWidth");
		egu.getColumn("gongysb_id").setHeader("合同供方");
		egu.getColumn("diancxxb_id").setHeader("合同需方");
		egu.getColumn("quann").setHeader("全年合计<br>(万吨)");
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("yunsfs").setHeader("运输方式");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("daoz_id").setHeader("到站");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("rez").setHeader("热值<br>(MJ/Kg)");
		((NumberField)egu.getColumn("rez").editor).setDecimalPrecision(2);
		egu.getColumn("huiff").setHeader("挥发分<br>%");
		((NumberField)egu.getColumn("huiff").editor).setDecimalPrecision(2);
		egu.getColumn("liuf").setHeader("硫分<br>%");
		((NumberField)egu.getColumn("liuf").editor).setDecimalPrecision(2);
		egu.getColumn("chebjg").setHeader("车板价<br>(元/吨)");
		((NumberField)egu.getColumn("chebjg").editor).setDecimalPrecision(2);
		egu.getColumn("daocj").setHeader("到厂价");
		egu.getColumn("biaomdj").setHeader("标煤单价");
		egu.getColumn("yunf").setHeader("运费<br>(元/吨)");
		((NumberField)egu.getColumn("yunf").editor).setDecimalPrecision(2);
		egu.getColumn("zaf").setHeader("杂费<br>(元/吨)");
		((NumberField)egu.getColumn("zaf").editor).setDecimalPrecision(2);
		egu.getColumn("y1").setHeader("一月<br>(万吨)");
		egu.getColumn("y2").setHeader("二月<br>(万吨)");
		egu.getColumn("y3").setHeader("三月<br>(万吨)");
		egu.getColumn("y4").setHeader("四月<br>(万吨)");
		egu.getColumn("y5").setHeader("五月<br>(万吨)");
		egu.getColumn("y6").setHeader("六月<br>(万吨)");
		egu.getColumn("y7").setHeader("七月<br>(万吨)");
		egu.getColumn("y8").setHeader("八月<br>(万吨)");
		egu.getColumn("y9").setHeader("九月<br>(万吨)");
		egu.getColumn("y10").setHeader("十月<br>(万吨)");
		egu.getColumn("y11").setHeader("十一月<br>(万吨)");
		egu.getColumn("y12").setHeader("十二月<br>(万吨)");
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
		egu.getColumn("gongysb_id").setWidth(200);
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

		if (treejib2 == 1) {// 选集团时刷新出所有的电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib2 == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" or id="+getTreeid()+" order by id"));
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
		String GongysSql = 
			"select id,mingc from (\n" +
			"select m.id as id,m.mingc as mingc,m.meikdq_id as id2,1 as xuh1,g.xuh as xuh2\n" + 
			"from\n" + 
			"meikxxb m,gongysb g\n" + 
			"where m.meikdq_id=g.id\n" + 
			"union\n" + 
			"select id as id,'***'||mingc||'***' as mingc,id as id2,0 as xuh1,xuh as xuh2\n" + 
			"from gongysb\n" + 
			"where leix=0)\n" + 
			"order by xuh2,id2 ,xuh1";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,new IDropDownModel(GongysSql));
		
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
		egu.getColumn("pinzb_id").setDefaultValue("原煤");
		
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
		

		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		egu.addTbarText("->");
//		egu.addTbarText("<font color=\"#EE0000\">单位:万吨</font>");
		
		
		String BgColor="";
		String sql_color="select zhi from xitxxb where mingc ='总计小计行颜色' and zhuangt=1 ";
		
		rsl = con.getResultSetList(sql_color);
		
		if (rsl.next()){
			BgColor=rsl.getString("zhi");
		}
		rsl.close();
		
		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			sb.append("e.record.set('DAOCJ',Round(parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))+parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')),2));\n"
					+ "if (e.record.get('REZ')!=0){e.record.set('BIAOMDJ',Round(eval(Round((parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))/1.17+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))*(1-0.07)+eval(e.record.get('ZAF')||0))*29.271/parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')),2)||0),2));}\n"
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
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			setTbmsg(null);
			getSelectData();
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
	
	
	private String treeid;
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