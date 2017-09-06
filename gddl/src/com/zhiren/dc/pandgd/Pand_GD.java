package com.zhiren.dc.pandgd;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * 修改人： lichenji
 * 时间：2010年10月22日
 *内容：根据需求，把结束时间拆分开分成盘点年份和盘点月份，由这两个和盘点开始时间可以确定盘煤是盘前还是盘后
 */
public class Pand_GD extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		return riq;
	}
	
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
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
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid==null||treeid.equals("")) {

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
	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
//			setOriRiq(getRiq());
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
		}
		getSelectData();
	}
	public void save() {
		String sSql = "";
		String sMessage = "";
		String msg_insert="";
		String msg_update="";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		String copy_flag = MainGlobal.getXitxx_item("盘点", "复制上月盘点所有数据", String.valueOf(visit.getDiancxxb_id()), "否");
		String bianm = "";
		String id1 = "";
		if("是".equals(copy_flag)){
			ResultSetList rs = new ResultSetList();
			rs = con.getResultSetList("select id, bianm from pand_gd where diancxxb_id= "+getTreeid()+" order by riq desc");
			rs.next();
			bianm=rs.getString("bianm");	//得到上一个日期的编码
			id1=rs.getString("id");			//得到上一个日期的pand_gd的id即pand_gd_id
		}
		ResultSetList sql1 = new ResultSetList();
		sql1 = con.getResultSetList("select getnewid("+visit.getDiancxxb_id()+") id from dual");
		sql1.next();
		String newid=sql1.getString("id");
		
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		ResultSetList rs_bianm=new ResultSetList();
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandb.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//String zhuangt = rsl.getString("zhuangt");
			//if (zhuangt.equals("否")) {
				//sMessage = sMessage + "编 码：" + rsl.getString("bianm") + "</br>";
				//continue;
			//} else {
				//进行删除操作时添加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Pand,
						"pand_gd",id+"");
				sSql = "delete from pand_gd where id=" + id;
				flag = con.getDelete(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();				
				//}
			}

		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandb.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				
				String sqlTem="select p.bianm from pand_gd p where p.diancxxb_id="+getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id"))
							+" and p.bianm='"+rsl.getString("bianm")+"'";
				
				rs_bianm=con.getResultSetList(sqlTem);
				
				if(rs_bianm!=null && rs_bianm.next()){//说明编码重复
					
					msg_insert+="编 码：" + rsl.getString("bianm") + "</br>";
					continue;
				}
				sSql = "insert into pand_gd values(" + newid + ","
					+ getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id")) + ","+"'"
					+ rsl.getString("bianm") + "',"
					+ "to_date('" + getRiq() + "','yyyy-mm-dd'),"
					+ "to_date('"+rsl.getString("kaisrq")+"','yyyy-mm-dd hh24:mi')"+","+"to_date('"+rsl.getString("nianf")+"-"+rsl.getString("yuef")+"-01','yyyy-mm-dd')"+","
					+ "'"+rsl.getString("piz")+"',"
					+ "'"+rsl.getString("shenh")+"',"
					+ "'"+rsl.getString("bianz")+"',"
					+ "'"+rsl.getString("bianzbm")+"'"
					+ " );\n";
				if("是".equals(copy_flag)){
					sSql+=
						"insert into pandryb \n" +		//添加盘点人员信息
						"(select getnewid("+visit.getDiancxxb_id()+"),"+newid+",bum,canjry,zhiz\n" + 
						"from pandryb p,pand_gd d\n" + 
						"where p.pand_gd_id=d.id \n" + 
						"and d.bianm='"+bianm+"'\n" + 
						");\n" +
						"insert into meicdjmdcdb(\n" +	//添加密度测量信息
						"select getnewid("+visit.getDiancxxb_id()+"),"+newid+", p.meicb_id, p.tongb, p.zongz, p.piz, p.jingz, p.rongj, p.mid\n" + 
						"  from meicdjmdcdb p, meicb m, pand_gd b\n" + 
						"  where p.pand_gd_id = b.id\n" + 
						"    and m.id = p.meicb_id and b.bianm='"+bianm+"'\n" + 
						");\n" +
	
	
						"insert into meicdxcsb(\n" +	//添加体积测量信息
						" select getnewid("+visit.getDiancxxb_id()+"),"+newid+", p.shangk, p.xiak, p.gao, p.dingc, p.jic, p.tij,p.mid,p.shul,'',p.meicb_id\n" + 
						"   from meicdxcsb p, meicb m, pand_gd b\n" + 
						"  where p.pand_gd_id = b.id\n" + 
						"    and m.id = p.meicb_id and b.bianm='"+bianm+"'\n" + 
						");\n" +
	
						"insert into pandgdcmb\n" +		//添加煤仓存煤信息
						"  (select getnewid("+visit.getDiancxxb_id()+"),"+newid+", pa.id as meicclb_id, pb.shul as shul\n" + 
						"     from (select p.id, b.jizbh as jizbh, m.mingc as mingc\n" + 
						"             from jizb b, item m, meicclb p, pand_gd g\n" + 
						"            where p.jizb_id = b.id\n" + 
						"              and p.item_id = m.id\n" + 
						"              and g.bianm = '"+bianm+"'\n" + 
						"              and g.diancxxb_id = b.diancxxb_id\n" + 
						"            order by p.id) pa,\n" + 
						"          pandgdcmb pb\n" + 
						"    where pb.meicclb_id = pa.id);\n" +
	
						"insert into pandgdwzcmb(\n" +	//添加其他存煤信息
						" select getnewid("+visit.getDiancxxb_id()+"),"+newid+",\n" + 
						"        pandgdcmb_id,\n" + 
						"        cunml,beiz\n" + 
						"   from pandgdwzcmb, pand_gd\n" + 
						"  where  pand_gd_id = pand_gd.id\n" + 
						"    and pand_gd.bianm = '"+bianm+"'\n" + 
						");\n" +
	
						"insert into pandzmb(\n" +		//添加盘点账面信息
						"select  getnewid("+visit.getDiancxxb_id()+"),"+newid+",\n" + 
						"       YUNXCS,\n" + 
						"       JITCS,\n" + 
						"       YUNS,\n" + 
						"       DANGYLJRCSF,\n" + 
						"       DANGYLJRLSF,\n" + 
						"       SHUIFCTZ,\n" + 
						"       QICKC,\n" + 
						"       BENYLM,\n" + 
						"       BENYHM,\n" + 
						"       QITHM,\n" + 
						"       DIAOCL,\n" + 
						"       BENQKC,\n" + 
						"       JITSHHKC,\n" + 
						"       BENQYK\n" + 
						"  from pandzmb p, pand_gd\n" + 
						" where p.pand_gd_id = pand_gd.id\n" + 
						"   and pand_gd.bianm = '"+bianm+"');\n" +
	
						"insert into pandqhbhb(\n" +
						"select getnewid("+visit.getDiancxxb_id()+"),"+newid+",\n" + 
						"       dangrscll,\n" + 
						"       dangrschl,\n" + 
						"       panqhll,\n" + 
						"       panqhhl,\n" + 
						"       rucpjsf,\n" + 
						"       rulpjsf,\n" + 
						"       shuifctz\n" + 
						"  from pandqhbhb p, pand_gd\n" + 
						" where p.pand_gd_id = pand_gd.id\n" + 
						"   and pand_gd.bianm = '"+bianm+"' );\n" +
	
						"insert into pandsxb\n" +
						"  (select getnewid("+visit.getDiancxxb_id()+"),"+newid+",\n" + 
						"          b.pandff,\n" + 
						"          b.shiyyq,\n" + 
						"          b.meitcfqk,\n" + 
						"          b.midcd,\n" + 
						"          b.rulmjljsfctz,\n" + 
						"          b.yingkqkfx\n" + 
						"     from pandsxb b\n" + 
						"    where b.pand_gd_id = "+id1+");\n";
				}
	
				flag = con.getInsert("begin\n" + sSql + "\nend;");
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				
				String sqlTem="select p.bianm from pand_gd p where p.diancxxb_id="+getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id"))
				+" and p.bianm='"+rsl.getString("bianm")+"' and id <> " +id;
			
				rs_bianm=con.getResultSetList(sqlTem);
				
				if(rs_bianm!=null && rs_bianm.next()){//说明编码重复
					
					

					String sql_bianm="select p.bianm from pand_gd p where p.id="+id;
					
					ResultSetList rm=con.getResultSetList(sql_bianm);
					
					if(rm.next()){
						msg_update+="编 码：" + rm.getString("BIANM") + "</br>";
					}
					
					rm.close();
					continue;
				}
	
	
				//进行修改操作时添加日志
//				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
//						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Pand,
//						"pand_gd",id+"");
				sSql = "update pand_gd set "
					+ " diancxxb_id=" + getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id")) + ","					
					+ " bianm='" + rsl.getString("bianm") + "',"
					+ " kaisrq=to_date('"+rsl.getString("kaisrq")+"','yyyy-mm-dd hh24:mi'),"
					+ " jiesrq=to_date('"+rsl.getString("nianf")+"-"+rsl.getString("yuef")+"-01','yyyy-mm-dd')"+", "
					+ " piz='"+rsl.getString("piz")+"',"
					+ " shenh='"+rsl.getString("shenh")+"',"
					+ " bianz='"+rsl.getString("bianz")+"',"
					+ " bianzbm='"+rsl.getString("bianzbm")+"'"
					+ " where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			}
		}
		con.Close();
		
		String msg_alert="";
		if (!sMessage.equals("")) {
			sMessage = sMessage + "不能删除<br>";
			
			msg_alert+=sMessage;
			//setMsg(sMessage);
		}
		if(!msg_insert.equals("")){
			msg_insert+="不能添加,有重复记录<br>";
			msg_alert+=msg_insert;
		}
		if(!msg_update.equals("")){
			msg_update+="不能更改，有重复记录<br>";
			msg_alert+=msg_update;
		}
		
		if(!msg_alert.equals("")){
			this.setMsg(msg_alert);
		}
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		visit.getRenyID();
		JDBCcon con = new JDBCcon();
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			if(visit.isFencb()){//是否分厂别
			 str="and (dc.id = " + getTreeid() + " or dc.fuid = "
				+ getTreeid() + ")";
			}else{
				str = "and dc.id = " + getTreeid() + "";
			}
		
		}
		String sSql =
			"select p.id,dc.mingc as diancxxb_id,p.bianm,p.kaisrq,to_char(p.jiesrq,'yyyy') as nianf,to_char(p.jiesrq,'MM') as yuef,p.piz as piz,p.shenh as shenh,p.bianz as bianz,p.bianzbm as bianzbm  " +
			"  from pand_gd p,diancxxb dc " +
			" where p.diancxxb_id=dc.id and p.riq=to_date('"+getRiq()+"','yyyy-mm-dd') " + str;

//		System.out.println(sSql);
		ResultSetList rsl = con.getResultSetList(sSql);
		ResultSetList rslTemp=con.getResultSetList(sSql);
//		if (rsl == null) {
//			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
//			setMsg(ErrorMessage.NullResult);
//			return;
//		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setWidth(130);
		egu.getColumn("bianm").setWidth(130);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("kaisrq").setHeader("盘点时间");
		egu.getColumn("nianf").setHeader("盘点年份");
		egu.getColumn("yuef").setHeader("盘点月份");
		egu.getColumn("piz").setHeader("批准");
		egu.getColumn("shenh").setHeader("审核");
		egu.getColumn("bianz").setHeader("编制");
		egu.getColumn("bianzbm").setHeader("编制部门");
		IDropDownModel dc;
		if (treejib == 1) {
			dc =  new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc");
		} else if (treejib == 2) {
			dc =  new IDropDownModel("select id,mingc from diancxxb where fuid=" + getTreeid() + " order by mingc");
		} else {
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			if(visit.isFencb()){
				dc = new IDropDownModel("select id,mingc from diancxxb where id = "+getTreeid()+" or fuid="+getTreeid()+" order by mingc");
			}else{
				dc = new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");
			}
		}
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,dc);
		egu.getColumn("diancxxb_id").setReturnId(true);
		egu.getColumn("diancxxb_id").setDefaultValue(dc.getBeanValue(getTreeid()));
		//自动生成编码_Start
		
			//获得当前选中电厂的piny
			String sql_diancpy = "select piny from diancxxb where id = '"+this.getTreeid()+"'";
			String piny = "";
			ResultSetList rs = con.getResultSetList(sql_diancpy);
			while(rs.next()){
				piny = rs.getString("piny");
			}
			//获得当前的年份和月份
			long a=0;
			DateFormat df = new SimpleDateFormat("yyyyMM");
			try {
				 a=df.parse(getRiq()).getTime();
			} catch (ParseException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
//			String   nowdate=new   java.text.SimpleDateFormat( "yyyyMM ").format(java.util.Calendar.getInstance().getTime());
//			String bianm =piny+nowdate; 
			String[] nowdate=getRiq().split("-");
			egu.getColumn("bianm").setDefaultValue(piny+nowdate[0]+nowdate[1]+nowdate[2]);
			egu.getColumn("bianm").setEditor(null);
		
		//自动生成编码_end
			
			//设置开始日期控件
			DatetimeField dtf1 = new DatetimeField();
			dtf1.setFormat("Y-m-d H:i");
			dtf1.setMenu("new DatetimeMenu()");
			egu.getColumn("kaisrq").setRenderer(GridColumn.Renderer_DateTime);
			egu.getColumn("kaisrq").setEditor(dtf1);
			egu.getColumn("kaisrq").setDataType(GridColumn.DataType_DateTime);
			egu.modifyColumnContent(rslTemp, "kaisrq");
			//设置结束日期控件
			/*DatetimeField dtf2 = new DatetimeField();
			dtf2.setFormat("Y-m-d H:i");
			dtf2.setMenu("new DatetimeMenu()");
			egu.getColumn("jiesrq").setRenderer(GridColumn.Renderer_DateTime);
			egu.getColumn("jiesrq").setEditor(dtf2);
			egu.getColumn("jiesrq").setDataType(GridColumn.DataType_DateTime);
			egu.modifyColumnContent(rslTemp, "jiesrq");*/
			
			int nowNianf=Integer.parseInt(nowdate[0]);
			int nowYuef=Integer.parseInt(nowdate[1]);
			
			//设置盘点年份
			List nianfList=new ArrayList();
			IDropDownBean db1=new IDropDownBean();
			for (int i = nowNianf-2; i < nowNianf+3; i++) {
				nianfList.add(new IDropDownBean(i,String.valueOf(i)));
			}
			
			ComboBox c1 = new ComboBox();
			egu.getColumn("nianf").setEditor(c1);
			c1.setEditable(true);
			egu.getColumn("nianf").setComboEditor(egu.gridId,new IDropDownModel(nianfList));
			egu.getColumn("nianf").setDefaultValue(String.valueOf(nowNianf));
			
			//盘点月份
			List yuefList=new ArrayList();
			for (int i = 1; i < 13; i++) {
				yuefList.add(new IDropDownBean(i,String.valueOf(i)));
			}
			ComboBox c2 = new ComboBox();
			egu.getColumn("yuef").setEditor(c2);
			c2.setEditable(true);
			egu.getColumn("yuef").setComboEditor(egu.gridId,new IDropDownModel(yuefList));
			egu.getColumn("yuef").setDefaultValue(String.valueOf(nowYuef));
			
		egu.addTbarText("日期:");
		DateField dStart = new DateField();
		dStart.setValue(getRiq());
		dStart.Binding("RIQ","forms[0]");
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("-");
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//GridButton gbt1 = new GridButton("保存","function(){document.getElementById('SaveButton').click();}");
		//gbt1.setIcon(SysConstant.Btn_Icon_Save);
//		String script = "";
//		script = "\ngridDiv_grid.on('beforeedit', function(e) {\n" 
//				+ "\tif (e.record.get('ZHUANGT') == '否') {e.cancel = true;}"
//				+ "});";
//		egu.addOtherScript(script);
		setExtGrid(egu);
		con.Close();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			visit.setExtTree1(null);
			visit.setExtGrid1(null);
			setTreeid(null);
//			setOriRiq(getRiq());
			setRiq(DateUtil.FormatDate(new Date()));
		}
		init();
	}
	private void init() {
		getSelectData();
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
}
