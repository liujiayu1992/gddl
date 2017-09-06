package com.zhiren.dc.diaoygl;

import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Xiecwh extends BasePage implements PageValidateListener {
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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		StringBuffer sb=new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag=0;
		long id = 0;
		ResultSetList rssb=getExtGrid().getDeleteResultSet(getChange());
		if(rssb==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahxg.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rssb.next()){
			id=rssb.getLong("RID");
			//删除时新增日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Riscsj,
					"rucxcb",id+"");
			String sql="update  rucxcb set xiecjzsj=to_date('','YYYY-MM-DD hh24:mi:ss') where id="+id;
			flag=con.getDelete(sql);
			if(flag==-1){
				con.rollBack();
				con.Close();
				
			}
			con.commit();
		}
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Riscsjwh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			con.Close();
			return;
		}
		
		sb.append("begin ");
		String riq = "";
		while(rsl.next()) {
			id=rsl.getLong("RID");
			if(id==0){
				riq = rsl.getString("riq");
				sb.append("insert into rucxcb (id,gongysb_id,beiz,xieckssj,xiecjzsj,chepb_id,SHANGMBZB_ID)");//,gongdl,shangwdl,gongrl,fadfhl,to_char(guohsj,'YYYY-MM-DD hh24:mi:ss'),xiecjzsj
				sb.append("values (getnewid(").append(getTreeid()).append("),").append(rsl.getString("GID")).append(",'");
				sb.append(rsl.getString("BEIZ")).append("',").append("to_date('"+rsl.getString("GUOHSJ")+"','YYYY-MM-DD hh24:mi:ss')"+"").append(",");
				sb.append("to_date('"+rsl.getString("XIECJZSJ")+"','YYYY-MM-DD hh24:mi:ss')"+",0,0").append(");") ;
//				con.getInsert(sb.toString());
			}else{
				//更改时新增日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Riscsj,
						"RUCXCB",id+"");
				sb.append("update RUCXCB set beiz='").append("',").append("xiecjzsj=");
				sb.append("to_date('"+rsl.getString("XIECJZSJ")+"','YYYY-MM-DD hh24:mi:ss')"+"");
				sb.append(" where id=").append(id).append(";");
//				con.getUpdate(sb.toString());
			}
		}
		sb.append("end;");
		if(sb.length()>13){
			
			flag = con.getInsert(sb.toString());
		}
		if(flag==-1){
			con.rollBack();
			con.Close();
			
		}
		if (flag!=-1){//保存成功
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
//		AutoCreateShouhcrb.Create(con,visit.getDiancxxb_id(),DateUtil.getDate(riq));
		con.commit();
		con.Close();
		//setMsg("保存成功");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}


	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		
	}
	
	private String getFenc(JDBCcon con){
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		String s="";
		while(rsl.next()){
			s+=rsl.getString("id")+",";
		}
		if(s.equals("")){
			s=this.getTreeid()+",";
		}
		
		return s.substring(0,s.lastIndexOf(","));
	}
	
	
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon(); 
		String riqTiaoj=this.getRiqi();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
		Visit visit = ((Visit) getPage().getVisit());
		String riq = DateUtil.FormatDate(new Date());
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select r.id,r.diancxxb_id,r.riq,j.jizbh as jizb_id,r.fadl,r.gongdl,r.shangwdl,r.gongrl,r.fadfhl \n");
		sbsql.append("from riscsjb r,jizb j,diancxxb d \n");
		sbsql.append("where r.jizb_id=j.id and riq=to_date('"+riqTiaoj+"','yyyy-mm-dd') \n");
		sbsql.append("and r.diancxxb_id=d.id and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+") order by id");
		
		
		
		String sqlr=
			"select decode(nvl(gid,0),0,1, nvl(rid,0)) rid,gid ,to_char(guohsj,'YYYY-MM-DD hh24:mi:ss') guohsj,zhongchh,decode(nvl(gid,0),0,'总计',mingc) mingc,chec,zongcs,zibcs ,nvl(lucs,0) lucs,to_char(xiecjzsj,'YYYY-MM-DD hh24:mi:ss') xiecjzsj,\n" +
			"xcs,xzibc,xlucs,wxcs,wzibc,nvl(wlucs,0) wlucs\n" + 
			"  from(\n" + 
			"\n" + 
			"select a.rid,a.gid,a.guohsj,a.zhongchh,a.mingc,a.chec,a.ches as zongcs,a.zibcs ,a.lucs,a.xiecjzsj ,\n" + 
			"decode(a.xiecjzsj,'',0,a.ches) xcs,decode(a.xiecjzsj,'',0,a.zibcs ) xzibc,decode(a.xiecjzsj,'',0,a.lucs ) xlucs,\n" + 
			"decode(a.xiecjzsj,'',a.ches,0) wxcs,decode(a.xiecjzsj,'',a.zibcs,0 ) wzibc,decode(a.xiecjzsj,'',a.lucs,0 ) wlucs\n" + 
			",1 as xuh\n" + 
			" from\n" + 
			"(\n" + 
			"         select\n" + 
			"\n" + 
			"           rb.id rid,\n" + 
			"\n" + 
			"           gsb.id as gid,\n" + 
			"\n" + 
			"           gb.guohsj,\n" + 
			"\n" + 
			"           max(cb.zhongchh)as  zhongchh ,\n" + 
			"          fb.chec,\n" + 
			"          max(md.mingc) as mingc,\n" + 
			"\n" + 
			"          count(gsb.id) as ches,\n" + 
			"          count(decode(cb.chebb_id,1,1)) as lucs,\n" + 
			"          count(decode(cb.chebb_id,2,1)) as zibcs\n" + 
			"          ,rb.xiecjzsj\n" + 
			"      from\n" + 
			"            (select r.id,nvl(g.id,0) as guohb_id,nvl(r.gongysb_id,0) as gongysb_id,r.xieckssj,r.xiecjzsj\n" + 
			"                    from rucxcb r,guohb g\n" + 
			"                    where to_char(g.guohsj,'yyyymmdd') = '"+riqTiaoj.replaceAll("-", "")+"'\n" + 
			"                          and r.xieckssj(+) = g.guohsj\n" + 
			"                    )  rb,\n" + 
			"\n" + 
			"           chepb   cb,\n" + 
			"           guohb   gb,\n" + 
			"           fahb    fb,\n" + 
			"           gongysb gsb,\n" + 
			"           chebb   cbb,\n" + 
			"           yunsfsb ys,meikxxb md\n" + 
			"     where\n" + 
			"           (rb.gongysb_id = gsb.id or rb.gongysb_id=0) and fb.meikxxb_id=md.id\n" + 
			"       and\n" + 
			"       cb.guohb_id = gb.id\n" + 
			"       and cb.fahb_id = fb.id\n" + 
			"       and fb.gongysb_id = gsb.id\n" + 
			"       and cb.chebb_id = cbb.id\n" + 
			"       and fb.yunsfsb_id = ys.id\n" + 
			"       and ys.id = 1\n" + 
			"       and fb.diancxxb_id in (select id from diancxxb db where db.fuid= "+getTreeid()+")\n" + 
			"       and rb.guohb_id = gb.id\n" + 
			"      and to_char(gb.guohsj,'yyyymmdd') = '"+riqTiaoj.replaceAll("-", "")+"'\n" + 
			"      group by rollup( rb.id ,\n" + 
			"      gsb.id ,gb.guohsj,fb.chec\n" + 
			"\n" + 
			"      ,rb.xiecjzsj)\n" + 
			"      having  (\n" + 
			"      grouping(rb.id)+grouping(rb.xiecjzsj)\n" + 
			"      +grouping(gsb.id)+grouping(fb.chec)+grouping(gb.guohsj))=0\n" + 
			"\n" + 
			"      ) a\n" + 
			"       union\n" + 
			"       (\n" + 
			"       select 1 rid, 1 gid,to_date('','YYYY-MM-DD hh24:mi:ss') guohsj,'' zhongchh, '总计' mingc,'' chec,sum(b.ches) as zongcs,sum(b.zibcs) zibcs ,sum(b.lucs) lucs,to_date('','YYYY-MM-DD hh24:mi:ss') xiecjzsj ,\n" + 
			"sum(decode(b.xiecjzsj,'',0,b.ches)) xcs,sum(decode(b.xiecjzsj,'',0,b.zibcs )) xzibc,\n" + 
			"sum(decode(b.xiecjzsj,'',0,b.lucs )) xlucs,\n" + 
			"sum(decode(b.xiecjzsj,'',b.ches,0)) wxcs,\n" + 
			"sum(decode(b.xiecjzsj,'',b.zibcs,0 )) wzibc,sum(decode(b.xiecjzsj,'',b.lucs,0 )) wlucs\n" + 
			",2 as xuh\n" + 
			" from\n" + 
			"(\n" + 
			"         select\n" + 
			"\n" + 
			"           rb.id rid,\n" + 
			"\n" + 
			"           gsb.id as gid,\n" + 
			"\n" + 
			"           gb.guohsj,\n" + 
			"\n" + 
			"           max(cb.zhongchh)as  zhongchh ,\n" + 
			"          fb.chec,\n" + 
			"          max(gsb.mingc) as mingc,\n" + 
			"\n" + 
			"          count(gsb.id) as ches,\n" + 
			"          count(decode(cb.chebb_id,1,1)) as lucs,\n" + 
			"          count(decode(cb.chebb_id,2,1)) as zibcs\n" + 
			"          ,rb.xiecjzsj\n" + 
			"      from\n" + 
			"            (select r.id,nvl(g.id,0) as guohb_id,nvl(r.gongysb_id,0) as gongysb_id,r.xieckssj,r.xiecjzsj\n" + 
			"                    from rucxcb r,guohb g\n" + 
			"                    where to_char(g.guohsj,'yyyymmdd') = '"+riqTiaoj.replaceAll("-", "")+"'\n" + 
			"                          and r.xieckssj(+) = g.guohsj\n" + 
			"                    )  rb,\n" + 
			"\n" + 
			"           chepb   cb,\n" + 
			"           guohb   gb,\n" + 
			"           fahb    fb,\n" + 
			"           gongysb gsb,\n" + 
			"           chebb   cbb,\n" + 
			"           yunsfsb ys\n" + 
			"     where\n" + 
			"           (rb.gongysb_id = gsb.id or rb.gongysb_id=0)\n" + 
			"       and\n" + 
			"       cb.guohb_id = gb.id\n" + 
			"       and cb.fahb_id = fb.id\n" + 
			"       and fb.gongysb_id = gsb.id\n" + 
			"       and cb.chebb_id = cbb.id\n" + 
			"       and fb.yunsfsb_id = ys.id\n" + 
			"       and ys.id = 1\n" + 
			"      and fb.diancxxb_id in (select id from diancxxb db where db.fuid= "+getTreeid()+")\n" + 
			"       and rb.guohb_id = gb.id\n" + 
			"      and to_char(gb.guohsj,'yyyymmdd') = '"+riqTiaoj.replaceAll("-", "")+"'\n" + 
			"      group by rollup( rb.id ,\n" + 
			"      gsb.id ,gb.guohsj,fb.chec\n" + 
			"\n" + 
			"      ,rb.xiecjzsj)\n" + 
			"      having  (\n" + 
			"      grouping(rb.id)+grouping(rb.xiecjzsj)\n" + 
			"      +grouping(gsb.id)+grouping(fb.chec)+grouping(gb.guohsj))=0\n" + 
			"\n" + 
			"       ) b\n" + 
			"\n" + 
			"       )\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"  ) order by xuh,guohsj\n" + 
			"\n" + 
			"";



		ResultSetList rsl = con.getResultSetList(sqlr);
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sbsql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rucxcb");
		//rid,gid ,id ,guohsj,zhongchh,mingc,chec,zongcs,zibcs ,lucs,xiecjzsj,\n" +
//		"xcs,xzibc,xlucs,wxcs,wzibc,wlucs
		egu.getColumn("rid").setHeader("rid");
		egu.getColumn("rid").setWidth(70);
		egu.getColumn("rid").editor = null;
//		egu.getColumn("rid").setDefaultValue(visit.getDiancxxb_id()+"");
		egu.getColumn("rid").setHidden(true);
		egu.getColumn("gid").setHeader("gid");
		egu.getColumn("gid").setWidth(70);
//		egu.getColumn("rid").setDefaultValue(visit.getDiancxxb_id()+"");
		egu.getColumn("gid").setHidden(true);
		egu.getColumn("gid").editor = null;
		egu.getColumn("guohsj").setHeader("过衡日期");
		egu.getColumn("guohsj").setWidth(130);
		egu.getColumn("guohsj").editor = null;
//		egu.getColumn("riq").setDefaultValue(riqTiaoj);
//		egu.getColumn("riq").setHidden(true);
		egu.getColumn("zhongchh").setHeader("道号");
		egu.getColumn("zhongchh").setWidth(50);
		egu.getColumn("zhongchh").editor = null;
		egu.getColumn("mingc").setHeader("供应商单位名称");
		egu.getColumn("mingc").setWidth(120);
		egu.getColumn("mingc").editor = null;
		egu.getColumn("chec").setHeader("列数");
		egu.getColumn("chec").setWidth(50);
		egu.getColumn("chec").editor = null;
		egu.getColumn("zongcs").setHeader("车数");
		egu.getColumn("zongcs").setWidth(70);
		egu.getColumn("zongcs").editor = null;
		egu.getColumn("zibcs").setHeader("自备车");
		egu.getColumn("zibcs").setWidth(70);
		egu.getColumn("zibcs").editor = null;
		egu.getColumn("lucs").setHeader("路车");
		egu.getColumn("lucs").setWidth(50);
		egu.getColumn("lucs").editor = null;
		egu.getColumn("xiecjzsj").setHeader("卸完日期时间");
		egu.getColumn("xiecjzsj").setWidth(130);
		egu.getColumn("xcs").setHeader("卸车数");
		egu.getColumn("xcs").setWidth(50);
		egu.getColumn("xcs").editor = null;
		egu.getColumn("xzibc").setHeader("自备车");
		egu.getColumn("xzibc").setWidth(50);
		egu.getColumn("xzibc").editor = null;
		egu.getColumn("xlucs").setHeader("路车");
		egu.getColumn("xlucs").setWidth(50);
		egu.getColumn("xlucs").editor = null;
		
		egu.getColumn("wxcs").setHeader("未卸车数");
		egu.getColumn("wxcs").setWidth(50);
		egu.getColumn("wxcs").editor = null;
		egu.getColumn("wzibc").setHeader("自备车");
		egu.getColumn("wzibc").setWidth(50);
		egu.getColumn("wzibc").editor = null;
		egu.getColumn("wlucs").setHeader("路车");
		egu.getColumn("wlucs").setWidth(50);
		egu.getColumn("wlucs").editor = null;
		
		DatetimeField datetime = new DatetimeField();
		egu.getColumn("xiecjzsj").setEditor(datetime);
		egu.getColumn("xiecjzsj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
		
//		 设置树
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
//						.getVisit()).getDiancxxb_id(), getTreeid());
//		setTree(etu);
//		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.defaultsortable=false;
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
		if(getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

//	树
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

	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTbmsg(null);
			setRiqi(null);
			getRiqi();
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		getSelectData();
	}
}