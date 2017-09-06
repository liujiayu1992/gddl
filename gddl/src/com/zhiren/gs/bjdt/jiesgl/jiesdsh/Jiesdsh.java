package com.zhiren.gs.bjdt.jiesgl.jiesdsh;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.webservice.dtgsinterface.DBconn;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Liuc;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;

public class Jiesdsh extends BasePage {
	// 流程状态的类别
	private static long WODRW = 1;// 刚刚提交未选择流程的任务

	private static long LIUCZ = 2;// 未审核任务

	private static long YISH = 3;// 已审核

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
	}

	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _SbChick = false;

	public void SbButton(IRequestCycle cycle) {
		_SbChick = true;
	}

	private boolean _PassChick = false;

	public void PassButton(IRequestCycle cycle) {
		_PassChick = true;
	}

	private boolean _RbChick = false;

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}

	private boolean _Huitui = false;

	public void Huitui(IRequestCycle cycle) {
		_Huitui = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SbChick) {
			_SbChick = false;
//			System.out.println(this.getLiucfsSelectValue().getValue());
//			if (this.getLiucfsSelectValue().getValue().equals("公司审核")) {
				Tij();
//			} else  {
//				Pass();
//			}
			getSelectData();
		}
		if (shuax) {
			shuax = false;
			// Pass();
			getSelectData();
		}
		if (_RbChick) {
			_RbChick = false;
			Huit();
			getSelectData();
		}
		if (_Huitui) {
			_Huitui = false;
			Huitui();
			getSelectData();
		}
		//审核通过
		if(_PassChick){
			_PassChick=false;
			Pass();
			getSelectData();
		}
	}

	private void Huit() {// 回退电厂,即本地删除
		// TODO 自动生成方法存根
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();

		if (!(this.getChange().equals("") || this.getChange() == null)) {

			String change[] = this.getChange().split(";");
			for (int i = 0; i < change.length; i++) {

				if (change[i] == null || "".equals(change[i])) {
					continue;
				}

				String record[] = change[i].split(",");
				if (record.length > 2) {
					// Liuc.huit(record[1], Long.parseLong(record[0]),
					// renyxxb_id, "");
					try {
						ILiuc.getDelete(Long.parseLong(record[0]));
					} catch (NumberFormatException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					} catch (Exception e) {
						// TODO 自动生成 catch 块
						this.setMsg(e.getMessage());
					}
				}
			}
		}
	}

	public void Tij() {

		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();

		if (!(this.getChange().equals("") || this.getChange() == null)) {

			String change[] = this.getChange().split(";");

			for (int i = 0; i < change.length; i++) {

				if (change[i] == null || "".equals(change[i])) {

					continue;
				}
				String record[] = change[i].split(",");
				if (record.length > 2) {

					// UpdatejsdZt(Long.parseLong(record[0]));

					try {
						if (getWeizSelectValue().getId() == WODRW) {
							ILiuc.tij(record[1], Long.parseLong(record[0]),
									renyxxb_id,this.getLiucfsSelectValue()
											.getId(),false);
						} else {
							ILiuc.tij(record[1], Long.parseLong(record[0]),
									renyxxb_id,false, "");
						}
					} catch (NumberFormatException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					} catch (Exception e) {
						//通知用户异常
						this.setMsg(e.getMessage());
					}
				}
			}
		}
	}
//
	public void Pass() {//直接审核通过

		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();

		if (!(this.getChange().equals("") || this.getChange() == null)) {

			String change[] = this.getChange().split(";");

			for (int i = 0; i < change.length; i++) {

				if (change[i] == null || "".equals(change[i])) {

					continue;
				}

				String record[] = change[i].split(",");

				if (record.length > 2) {

					// if(Passjiesd(Long.parseLong(record[0])))
					// {
					try {
						if (getWeizSelectValue().getId() == WODRW) {
							ILiuc.tij(record[1], Long.parseLong(record[0]),
									renyxxb_id,this.getLiucfsSelectValue()
											.getId(),true);
						} else {
							ILiuc.tij(record[1], Long.parseLong(record[0]),
									renyxxb_id,true, "");
						}
						
					} catch (NumberFormatException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					} catch (Exception e) {
						// TODO 自动生成 catch 块
						this.setMsg(e.getMessage());
					}
					// }
				}

			}

		}
	}

	// 被pass（）引用
//
//	public void getDelete(long diancjsmkb_id) {
//
//		JDBCcon con = new JDBCcon();
//		con.setAutoCommit(false);
//		//同步电厂
//		 String sqli="select bianm,diancxxb_id from diancjsmkb where id="+diancjsmkb_id+"\n";
//		ResultSetList sr =con.getResultSetList(sqli);
//		 
//		 if(!ILiuc.DiancUpdate(sr.getLong("diancxxb_id"),sr.getString("bianm"), 1,-1)){ 
//			 this.setMsg("同步电厂时，数据库出现错误，请联系管理员");
//			 return;
//		 }
//	    int  result1 =-1;
//		String sql = "call deleteDiancJsd(" + diancjsmkb_id + ")";
//		System.out.println(sql);
//		result1 = con.getUpdate(sql);
//
//		if (result1>=0) {
//			con.commit();
//			System.out.println("结算单ID=" + diancjsmkb_id + "  操作成功！"
//					+ DateUtil.FormatDateTime(new Date()));
//			setMsg("回退电厂操作成功！");
//		} else {
//			con.rollBack();
//			System.out.println("结算单ID=" + diancjsmkb_id + "  操作失败！"
//					+ DateUtil.FormatDateTime(new Date()));
//			setMsg("回退电厂操作失败！");
//		}
//	
//		con.Close();
//	}

	// private void UpdatejsdZt(long jiesdid){
	//			
	// JDBCcon con = new JDBCcon();
	// con.setAutoCommit(false);
	// String sql = "";
	// int result = -1;
	// try{
	// sql="update diancjsmkb set shenhjb=11,zhuangt=1,zhijzxjbr='"+((Visit)
	// getPage().getVisit()).getRenymc()+"',zhijzxjbrq="+OraDate(new Date())+"
	// where id="+jiesdid;
	// result = con.getUpdate(sql);
	// if(result<0){
	// con.rollBack();
	// System.out.println("结算单ID="+jiesdid+"
	// 状态更新失败！"+DateUtil.FormatDateTime(new Date()));
	// setMsg("结算单提交领导失败！");
	// return;
	// }
	// result = -1;
	// sql="update diancjsyf set shenhjb=11,shenhzt=1,zhijzxjbr='"+((Visit)
	// getPage().getVisit()).getRenymc()+"',zhijzxjbrq="+OraDate(new Date())+"
	// where id="+jiesdid;
	// result = con.getUpdate(sql);
	// if(result<0){
	// con.rollBack();
	// System.out.println("结算单ID="+jiesdid+"
	// 状态更新失败！"+DateUtil.FormatDateTime(new Date()));
	// setMsg("运费结算单提交领导失败！");
	// return;
	// }
	// con.commit();
	// System.out.println("结算单ID="+jiesdid+" 操作成功！"+DateUtil.FormatDateTime(new
	// Date()));
	// setMsg("提交领导操作成功！");
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// con.Close();
	// }
	// }

	// private boolean Passjiesd(long jiesdid){
	//			
	// JDBCcon con = new JDBCcon();
	// ResultSet rs;
	// boolean bln = false;
	// try{
	// String sql2 = "select js.diancxxb_id,js.bianh,js.fapbh,d.diancssgszt from
	// diancjsmkb js,diancxxb d where js.diancxxb_id=d.id and js.id="+jiesdid;
	// rs = con.getResultSet(sql2);
	// if(rs.next()){
	//					
	// bln = UpdateJsdzt(con, rs, jiesdid);
	//			
	// }else{//运费结算单
	// sql2 = "select js.diancxxb_id,js.bianh,js.fapbh,d.diancssgszt from
	// diancjsyf js,diancxxb d where js.diancxxb_id=d.id and js.id="+jiesdid;
	// rs = con.getResultSet(sql2);
	// if(rs.next()){
	// bln = UpdateJsdzt(con, rs, jiesdid);
	// }
	// }
	//						
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// con.Close();
	// }
	// if(bln){
	// System.out.println("结算单ID="+jiesdid+" 操作成功！"+DateUtil.FormatDateTime(new
	// Date()));
	// return true;
	// }else{
	// System.out.println("结算单ID="+jiesdid+" 操作失败！"+DateUtil.FormatDateTime(new
	// Date()));
	// return false;
	// }
	// }

	// private boolean UpdateJsdzt(JDBCcon con, ResultSet rs,long jiesdID){
	// String sql = "";
	// int result = -1;
//	 final int dcshenhjb = 4;
//	 final int dczhuangt = 1;
	// try {
	// // if(rs.getInt("diancssgszt")==1){//属于公司下属电厂
	// if(!
//	 DiancUpdate(rs.getLong("diancxxb_id"),rs.getString("bianh"),dcshenhjb,dczhuangt)){
	//					
	// con.rollBack();
	// System.out.println("结算单"+rs.getString("bianh")+"更新失败！"+DateUtil.FormatDateTime(new
	// Date()));
	// setMsg("网络或连接数据库故障，结算单审核失败！");
	// return false;
	// }else{
	// sql="update diancjsmkb set shenhjb=15,zhuangt=1,jieszxjbr='"+((Visit)
	// getPage().getVisit()).getRenymc()+"',gongsshrq="+OraDate(new Date())+"
	// where id="+jiesdID;
	// result = con.getUpdate(sql);
	// if(result<0){
	// con.rollBack();
	// System.out.println("结算单"+rs.getString("bianh")+"状态更新失败！"+DateUtil.FormatDateTime(new
	// Date()));
	// setMsg("结算单状态更新失败！");
	// return false;
	// }
	// result = -1;
	// sql="update diancjsyf set shenhjb=15,shenhzt=1,jieszxjbr='"+((Visit)
	// getPage().getVisit()).getRenymc()+"',gongsshrq="+OraDate(new Date())+"
	// where id="+jiesdID;
	// result = con.getUpdate(sql);
	// if(result<0){
	// con.rollBack();
	// System.out.println("结算单"+rs.getString("bianh")+"状态更新失败！"+DateUtil.FormatDateTime(new
	// Date()));
	// setMsg("结算单状态更新失败！");
	// return false;
	// }
	// }
	// // }
	// con.commit();
	// setMsg("结算单审核成功！");
	// } catch (SQLException e) {
	// // TODO 自动生成 catch 块
	// e.printStackTrace();
	// return false;
	// }finally{
	// }
	// return true;
	// }
//审核通过 4,1    删除了 1,-1
//	 private boolean DiancUpdate(long diancID,String Jiesdh,int shenhjb,int zhuangt){
//				
//	 JDBCcon con = new JDBCcon();
//	 ResultSet rs;
//	 String sql = "";
//	 String mhostname = "";
//	 String msid = "";
//	 String mduank = "";
//	 String myonghm = "";
//	 String mmim = "";
//	 String mjiesb = "";
//	 String mjiesbhzd = "";
//	 String mpinysy = "";
//	 String jiesb[]={};
////	滨河，联营     唐山
//	 if(diancID==82 || diancID==102){
//		 diancID = 211;
//	 }
//	 sql = "select pz.*,dc.pinysy from diancxtpz pz,diancxxb dc where pz.jitdcid=dc.id and pz.jitdcid="+diancID;
//			
//	 try{
//	 rs = con.getResultSet(sql);
//	 if(rs.next()){
//	 mhostname = rs.getString("ip");
//	 msid = rs.getString("sid");
//	 mduank = rs.getString("duank");
//	 myonghm = rs.getString("yonghm");
//	 mmim = rs.getString("mim");
//	 mjiesb = rs.getString("jiesb");
//	 jiesb = mjiesb.split(",");
//	 mjiesbhzd = rs.getString("jiesbhzd");
//	 mpinysy = rs.getString("pinysy");
//	 }
//	 String bianh = Jiesdh.replaceAll(mpinysy, "");
//	 if(diancID==26){
//	 bianh = bianh.replaceAll("BH", "");
//	 bianh = bianh.replaceAll("LY", "");
//	 }
//	 DBconn dbcn=new DBconn(mhostname,mduank,msid);
//	 dbcn.setAutoCommit(false);
//	 dbcn.setUserName(myonghm);
//	 dbcn.setPassword(mmim);
//	 for(int t=0;t<jiesb.length;t++){
//		 int result=-1;
//		
//	//	 String dcsql1 = "update "+jiesb[t]+" set shenhjb="+shenhjb+",shenhzt="+zhuangt+" where "+mjiesbhzd+"='"+bianh+"'";
//	//	 result = dbcn.getUpdate(dcsql1);
//					    	
//		 if(result<0){
//			 dbcn.rollBack();
//			 dbcn.close();
//			 return false;
//		 }
//	 }
//	 dbcn.commit();
//	 dbcn.close();
//	 }catch(Exception e){
//	 e.printStackTrace();
//	 }finally{
//	 con.Close();
//	 }
//	 return true;
//	 }

	// 查询数据并形成界面
	public void getSelectData() {

		String sql = "";
		JDBCcon con = new JDBCcon();
		String leix = "公司结算";
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		String strWeizhitj = "";
		String liucztb_id="";
		if (getWeizSelectValue().getId() == WODRW) {// 得到刚刚从电厂上传的，还未选择流程的条目
			strWeizhitj = ILiuc.getWeixz("diancjsmkb", renyxxb_id, leix);
		} else if (getWeizSelectValue().getId() == LIUCZ) {// 得到未审核的任务
			strWeizhitj = ILiuc.getWeish("diancjsmkb", renyxxb_id, leix);
		} else {// 得到已审核的任务
			liucztb_id = ILiuc.getYish("diancjsmkb", renyxxb_id, leix);
		}

		sql = "select * \n"
				+ "  from ("
				+ "  select js.id,'diancjsmkb' as tabname,di.id as diancid,di.mingc as dianmc,\n"
				+ "				  getHtmlAlert('"
				+ MainGlobal.getHomeContext(this)
				+ "','Showjsd','jiesdbh',js.bianm,js.bianm) as bianh,"
				+ "               --gy.jianc as gongysmc, \n"
				+ "               js.gongysmc as fahdw,nvl(he.hetbh,js.hetbh) hetbh,jiessl,guohl,nvl(jiesrl,0) jiesrl,nvl(changfrl,0) changfrl,nvl(jieslf,0) jiesliuf,nvl(changflf,0) changflf,\n"
				+ "				  hansmk,meikje,shuik,"
				+ "				  buhsdj danj,js.jiesrq, \n"
				+ "				  nvl(jiesrl,0)-nvl(changfrl,0) as rezc,(nvl(jiessl,0)-nvl(guohl,0))-round((nvl(guohl,0)*0.02)) as shulc,	\n"
				+ "               lz.mingc zhuangt,zt.liucb_id,zt.id as liucztb_id\n"
				+ "          from diancjsmkb js , \n"
				+ "		(\n"
				+ "select nvl(rel.jiesdid,quanl.jiesdid) jiesdid,nvl( changfrl,0) changfrl,nvl(jiesrl,0) jiesrl,nvl(changflf,0) changflf,nvl(jieslf,0) jieslf from\n"
				+ "(select ji.jiesdid  jiesdid, ji.changf changfrl,ji.jies jiesrl from  jieszbsjb ji  left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='收到基低位热值') rel\n"
				+ "full outer join\n"
				+ "(select ji.jiesdid jiesdid, ji.changf changflf,ji.jies jieslf from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='干燥基全硫') quanl\n"
				+ "on (rel.jiesdid=quanl.jiesdid)\n"
				+ "      )chengf \n"
				+ "  , gongysb gon \n"
				+ "  , hetb he \n"
				+ "  , liucztb zt \n"
				+ "  , leibztb lz \n"
				+ "  , liuclbb ll \n"
				+ "  , diancxxb di \n";
				if(getWeizSelectValue().getId() == YISH)
				{sql+= "  where js.liucztb_id in(" + liucztb_id + ")  and chengf.jiesdid(+)=js.id and gon.id(+)=js.gongysb_id\n" ;}
				else{
					sql+= "  where js.id in(" + strWeizhitj + ")  and chengf.jiesdid(+)=js.id and gon.id(+)=js.gongysb_id\n" ;
				}
				sql+="and he.id(+)=js.hetb_id and zt.id(+)=js.liucztb_id and lz.id(+)=zt.leibztb_id and ll.id(+)=lz.liuclbb_id\n" +
						"and js.diancxxb_id=di.id(+)\n";
		
		         if(this.getWeizSelectValue().getId()==YISH){
		        	 sql+="and jiesrq>=to_date('"+this.getStartdate()+"','yyyy-mm-dd') and jiesrq<=to_date('"+this.getEnddate()+"','yyyy-mm-dd')\n";
		         }
				
				sql+="  )\n";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);// 设定记录集对应的表单

		egu.setGridType(ExtGridUtil.Gridstyle_Read);

		egu.getColumn("id").setHidden(true);
		egu.getColumn("tabname").setHidden(true);
		egu.getColumn("diancid").setHidden(true);
		egu.getColumn("dianmc").setHeader("电厂名称");
		egu.getColumn("bianh").setHeader("结算单编码");
		egu.getColumn("jiesrq").setHeader("结算日期");
		egu.getColumn("fahdw").setHeader("发货单位");
		egu.getColumn("hetbh").setHeader("合同编号");
		egu.getColumn("danj").setHeader("单价");
		egu.getColumn("jiessl").setHeader("结算数量");
		egu.getColumn("guohl").setHeader("过衡数量");
		egu.getColumn("changfrl").setHeader("厂方热值");
		egu.getColumn("jiesrl").setHeader("结算热值");
		egu.getColumn("changflf").setHeader("厂方硫分");
		egu.getColumn("jiesliuf").setHeader("结算硫分");
		egu.getColumn("hansmk").setHeader("价税金额");
		egu.getColumn("meikje").setHeader("价款金额");
		egu.getColumn("shuik").setHeader("税款");
		egu.getColumn("rezc").setHeader("热值差");
		egu.getColumn("shulc").setHeader("数量差");
		egu.getColumn("zhuangt").setHeader("状态");

		egu.getColumn("liucztb_id").setHidden(true);
		egu.getColumn("liucb_id").setHidden(true);
		egu.getColumn("rezc").setHidden(true);
		egu.getColumn("shulc").setHidden(true);

		egu.getColumn("id").setWidth(10);
		egu.getColumn("dianmc").setWidth(60);
		egu.getColumn("bianh").setWidth(100);
		// egu.getColumn("jieszb").setWidth(60);
		egu.getColumn("jiesrq").setWidth(70);
		// egu.getColumn("gongysmc").setWidth(90);
		egu.getColumn("fahdw").setWidth(100);
		egu.getColumn("hetbh").setWidth(90);
		egu.getColumn("danj").setWidth(75);
		egu.getColumn("jiessl").setWidth(60);
		egu.getColumn("guohl").setWidth(60);
		egu.getColumn("changfrl").setWidth(60);
		egu.getColumn("jiesrl").setWidth(60);
		egu.getColumn("changflf").setWidth(60);
		egu.getColumn("jiesliuf").setWidth(60);
		egu.getColumn("meikje").setWidth(90);
		egu.getColumn("meikje").setWidth(85);
		egu.getColumn("shuik").setWidth(80);
		egu.getColumn("rezc").setWidth(50);
		egu.getColumn("shulc").setWidth(50);
		egu.getColumn("zhuangt").setWidth(100);

		egu.setWidth(1000);

		egu
				.getColumn("jiessl")
				.setRenderer(
						"function(value,metadata,rec){if(rec.data['SHULC']>0) {metadata.css='tdTextext';} return value;} ");
		egu
				.getColumn("jiesrl")
				.setRenderer(
						"function(value,metadata,rec){if(rec.data['REZC']>200){metadata.css='tdTextext';} return value;}");

		// List tmp= new ArrayList();
		// for(int i=0;i<rsl.getRows();i++){
		// String strtmp="";
		// tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,0)));
		// for(int j=0;j<tmp.size();j++){
		// strtmp+=((Yijbean)tmp.get(j)).getXitts()+"\\n
		// "+(((Yijbean)tmp.get(j)).getYij()==null?"":((Yijbean)tmp.get(j)).getYij())+"\\n
		// ";
		// }
		// egu.setDataValue(i, 13, "付款编号 "+egu.getDataValue(i,1)+":\\n
		// "+strtmp);
		// }
		egu.getCheckPlugins();
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(22);

		egu.addTbarText("流程状态");
		ComboBox WeizSelect = new ComboBox();
		WeizSelect.setId("Weizx");
		WeizSelect.setWidth(150);
		WeizSelect.setLazyRender(true);
		WeizSelect.setTransform("WeizSelectx");
		egu.addToolbarItem(WeizSelect.getScript());
		egu
				.addOtherScript("Weizx.on('select',function(){Showwait(); document.forms[0].submit();});");
		egu
				.addOtherScript("gridDiv_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");

		
		if(this.getWeizSelectValue().getId()==YISH){
			 DateField startdf=new DateField();
			 startdf.setValue(this.getStartdate());
			 startdf.Binding("startdate", "");
			 egu.addTbarText("日期：");
			 egu.addToolbarItem(startdf.getScript());
			 egu.addTbarText("至");
			 DateField enddf=new DateField();
			 enddf.setValue(this.getEnddate());
			 enddf.Binding("enddate", "");
			 egu.addToolbarItem(enddf.getScript());
			 
				this.setYishzt(true);
	  String condition=MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+"document.getElementById('ShuaxinButton').click();";
		egu.addToolbarItem("{"+new GridButton("刷新","function(){"+condition+"}",SysConstant.Btn_Icon_Refurbish).getScript()+"}");				  
////			 GridButton gb=new GridButton("刷新","");
//			 gb.setTapestryBtnId("ShuaxinButton");
////			 gb.setButton(GridButton.ButtonType_Refresh);
//					
//			 gb.condition=MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";document.getElementById('ShuaxinButton').click();";
//			 egu.addTbarBtn(gb);
		}
		if (this.getWeizSelectValue().getId() == WODRW) {
			egu.addTbarText("流程方式：");
			ComboBox liucfs = new ComboBox();
			liucfs.setId("liucfs");
			liucfs.setWidth(80);
			liucfs.setLazyRender(true);
			liucfs.setTransform("liucfs");
			liucfs.setListeners("'change':function(){document.getElementById('liucfs').value=liucfs.getValue();}");
			egu.addToolbarItem(liucfs.getScript());
		}

		egu.addTbarText("-");
		egu.addToolbarItem("{"
				+ new GridButton(
						"回退电厂",
						"function(){ Ext.MessageBox.confirm('警告', '确定将结算单回退电厂吗？', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
								+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
								+ " 	for(var i=0;i<rec.length;i++){ "
								+ " 		if(i==0){"
								+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
								+ " 		}else{ "
								+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
								+ " document.getElementById('YiSRbButton').click();"+MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";} else{	alert('请选择一张结算单!');}}")
						.getScript() + "})}} ");

		// if(! ((Visit) getPage().getVisit()).getUid().equals("bfg") && !
		// ((Visit) getPage().getVisit()).getUid().equals("zzg")){
		if (this.getWeizSelectValue().getId() != YISH) {
			egu.addTbarText("-");
			String tishi="确定将结算提交吗？";
			if(this.getWeizSelectValue().getId()==WODRW){
				tishi="确定将结算按流程 '+liucfs.getRawValue()+' 提交吗？";
			}
			if(!ILiuc.isLastLeader(renyxxb_id)){
			egu
					.addToolbarItem("{"
							+ new GridButton(
									"提交领导",
									"function(){ Ext.MessageBox.confirm('警告', '"+tishi+"', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){  "
											+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
											+ " 	for(var i=0;i<rec.length;i++){ "
											+ " 		if(i==0){"
											+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
											+ " 		}else{ "
											+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
											+ " document.getElementById('SbButton').click();"+MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";}else{	alert('请选择一张结算单!');}}")
									.getScript() + "})}}");

			 }

			 egu.addTbarText("-");
			 egu.addToolbarItem("{"
			 + new GridButton(
			 "审核通过",
			 "function(){ Ext.MessageBox.confirm('警告', '"+tishi+"', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
			 + " rec = gridDiv_grid.getSelectionModel().getSelections(); "
			 + " for(var i=0;i<rec.length;i++){ "
			 + " if(i==0){"
			 + "document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
			 + " }else{ "
			 + "document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
			 + " document.getElementById('PassButton').click();"+MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";}else{alert('请选择一张结算单!');}}").getScript()+"})}}");
							
			egu.addTbarText("-");
		}

		// egu.addToolbarItem("{"
		// + new GridButton("审核意见",
		// "function(){ "
		// + "if(gridDiv_sm.hasSelection()){ "
		// + " if(Weizx.getRawValue()=='流程中'){ "
		// + " document.getElementById('DivMy_opinion').className =
		// 'x-hidden';}"
		// + " window_panel.show(); "
		// + " rec = gridDiv_grid.getSelectionModel().getSelections(); "
		// + " document.getElementById('My_opinion').value='';"
		// + " document.getElementById('Histry_opinion').value='';"
		// + " var strmyp=''; "
		// + " for(var i=0;i<rec.length;i++){ "
		// + " if(strmyp.substring(rec[i].get('YIJ'))>-1){ "
		// + " if(strmyp==''){ strmyp=rec[i].get('YIJ');}else{
		// strmyp+=','+rec[i].get('YIJ');}}"
		// + " var strtmp=rec[i].get('HISTRYYJ');"
		// + " document.getElementById('Histry_opinion').value+=strtmp+'\\n';}
		// document.getElementById('My_opinion').value=strmyp;"
		// + " }else{ "
		// + " alert('请选择一张付款单!');} " + "}")
		// .getScript() + "}");
		// egu.addTbarText("-");
		// //往Toolbar绑定combobox
		// egu.addTbarText("审批类型:");
		// ComboBox JieslxDropDown = new ComboBox();
		// JieslxDropDown.setId("JieslxDrop");
		// JieslxDropDown.setWidth(100);
		// JieslxDropDown.setLazyRender(true);
		// JieslxDropDown.setTransform("JieslxDropDown");
		// egu.addToolbarItem(JieslxDropDown.getScript());
		// //树
		// egu.addTbarText("-");
		// if(getJieslxValue().getId()!=3){
		// egu.addTbarText("单位名称:");
		// ExtTreeUtil etu = new
		// ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)getPage().getVisit()).getDiancxxbId(),getTreeid());
		//			
		// setTree(etu);
		// egu.addTbarTreeBtn("diancTree");
		// }
		// 绑定combobox完成
		// 假如选择的是 位置的值是已审核，工具栏选择这些按钮
		if (this.getWeizSelectValue().getId() == LIUCZ) {
			this.setYishzt(true);
			
			egu
					.addToolbarItem("{"
							+ new GridButton(
									"回退",
									"function(){ Ext.MessageBox.confirm('警告', '确定将结算单回退吗？', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
											+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
											+ " 	for(var i=0;i<rec.length;i++){ "
											+ " 		if(i==0){"
											+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
											+ " 		}else{ "
											+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
											+ " document.getElementById('Huitui').click();"+MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";} else{	alert('请选择一张结算单!');}}")
									.getScript() + "})}} ");

			// egu.addToolbarButton("回退", GridButton.ButtonType_SubmitSel,
			// "Huitui","", SysConstant.Btn_Icon_Return);
			egu.addTbarText("-");

		}
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

	// 位置下拉菜单--流程状态
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		
		if(ILiuc.isFirstRenY(v.getRenyID())){
		list.add(new IDropDownBean(1, "未选择流程的任务"));
		}else{
		list.add(new IDropDownBean(2, "待审核的任务"));
		}
		list.add(new IDropDownBean(3, "已审核的任务(包括其他人)"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
	}

	// 选择流程方式
	public IDropDownBean getLiucfsSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getLiucfsSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setLiucfsSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean6(true);
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}

	public void setLiucfsSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getLiucfsSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getLiucfsSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getLiucfsSelectModels() {
	Visit v=(Visit)this.getPage().getVisit();	
	ArrayList al=new ArrayList();
	al=ILiuc.getIcanliuc(v.getRenyID());
	if(al.size()<1){
		JDBCcon jcon=new JDBCcon();
		ResultSetList rsl=jcon.getResultSetList("select * from liucb where mingc='公司结算'");
		ArrayList al2=new ArrayList();
		al2.add(new IDropDownBean(rsl.getLong("id"),rsl.getString("mingc")));
		al=al2;
		jcon.Close();
	}
	v
	.setProSelectionModel3(new IDropDownModel(al));
	}

	public boolean isQuanxkz() {
		return ((Visit) getPage().getVisit()).getboolean4();
	}

	public void setQuanxkz(boolean value) {
		((Visit) getPage().getVisit()).setboolean4(value);
	}

	public boolean isYishzt() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setYishzt(boolean value) {
		((Visit) getPage().getVisit()).setboolean5(value);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(-1);
			setExtGrid(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			getWeizSelectModel();
			setLiucfsSelectValue(null);
			setLiucfsSelectModel(null);
			getLiucfsSelectModel();
			visit.setboolean4(true);// 我的任务、流程中
			visit.setboolean1(false);// 位置
			visit.setboolean2(false);// 付款类型
			visit.setboolean3(false);// 单位
			visit.setboolean5(false);// 已审核
			getSelectData();
		}

		if (((Visit) getPage().getVisit()).getboolean1()
				|| ((Visit) getPage().getVisit()).getboolean2()
				|| ((Visit) getPage().getVisit()).getboolean3()) {// 如果合同位置改变
			// 1, 位置2, 付款类型3, 单位
			if (((Visit) getPage().getVisit()).getboolean1() == true) {
				if (getWeizSelectValue().getId() == 1) {
					visit.setboolean4(true);
				} else {
					visit.setboolean4(false);
				}
			}
			visit.setboolean1(false);
			visit.setboolean2(false);
			visit.setboolean3(false);
			getSelectData();
		}
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

	public int getEditTableRow() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}

	public void setEditTableRow(int value) {

		((Visit) this.getPage().getVisit()).setInt1(value);
	}

	// 持久化电厂树
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

	public String getWunScript() {

		return "for(var i=0;i<rec.length;i++){"

		+ "    rec[i].set('YIJ',document.getElementById('My_opinion').value);"
				+ " }";
	}

	// 当选择已审核时显
	// 刷新动作
	private boolean shuax = false;

	public void ShuaxinButton(IRequestCycle cycle) {
		shuax = true;

	}

	// 回退动作
	public void Huitui() {// 及一步一步的回退
	// 回退电厂
		// TODO 自动生成方法存根
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		if (!(this.getChange().equals("") || this.getChange() == null)) {

			String change[] = this.getChange().split(";");
			for (int i = 0; i < change.length; i++) {
				if (change[i] == null || "".equals(change[i])) {
					continue;
				}

				String record[] = change[i].split(",");
				if (record.length > 2) {

					ILiuc.huit(record[1], Long.parseLong(record[0]),
							renyxxb_id, "");
					// getDelete(Long.parseLong(record[0]));
				}
			}
		}

	}

	// 开始日期设值
	private String startdate;

	public void setStartdate(String startdate) {

		this.startdate = startdate;
	}

	public String getStartdate() {
		if (startdate == null || startdate.equals("")) {
			Date today = new Date();
			today.setDate(today.getDate());
			this.setStartdate(DateUtil.FormatDate(today));
		}
		return this.startdate;
	}

	// 结束日期设值
	private String enddate;

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getEnddate() {
		if (enddate == null || enddate.equals("")) {
			this.setEnddate(DateUtil.FormatDate(new Date()));
		}
		return this.enddate;
	}
	// 得到已审核的id串 类似 "2332,2323,23234,34334"
	// private String getYish(){
	// String ids="";
	// JDBCcon con=new JDBCcon();
	// String sql="select distinct id from diancjsmkb where liucztb_id=1 and
	// jiesrq>=to_date('"+this.getStartdate()+"','yyyy-mm-dd') and
	// jiesrq<=to_date('"+this.getEnddate()+"','yyyy-mm-dd')";
	//			
	// ResultSet rs=con.getResultSet(sql);
	// try{
	// while(rs.next()){
	// if(!ids.equals("")){
	// ids+=",";
	// }
	// ids+=rs.getString(1);
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// con.Close();
	// }
	// if(ids.equals("")){
	// ids="-1";
	// }
	// return ids;
	// }

}
