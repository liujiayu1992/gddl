package com.zhiren.dc.jilgl.gongl.jianj;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-10-12
 * 内容:汽车检斤录入，根据发货信息编码，过滤数据
 */
/**
 * huochaoyuan
 * 2010-01-16在使用后页面出现多项bug，根据电厂实际的使用情况进行了调整
 */
public class Qicjjbmlr extends BasePage implements PageValidateListener {
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
		setPTbmsg(null);
		
		this.setJingzXt("");
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private String jingzXt="";
	
	public String getJingzXt(){
		return jingzXt;
	}
	public void setJingzXt(String jingzXt){
		this.jingzXt=jingzXt;
	}
	private String ptbmsg;
	public String getPTbmsg() {
		return ptbmsg;
	}
	public void setPTbmsg(String tbmsg) {
		this.ptbmsg = tbmsg; 
	}
//grid1卸车方式的选项菜单
//	private String Grid1Html;
//	public void setGrid1Html(String Grid1Html){
//		this.Grid1Html=Grid1Html;
//	}
//	public String getGrid1Html(){
//		JDBCcon con=new JDBCcon();
//		Visit v=(Visit)this.getPage().getVisit();
//		ResultSet rs=con.getResultSet("select mingc from xiecfsb where "+Jilcz.filterDcid(v,null).substring(4)+" order by id");
//		StringBuffer sb=new StringBuffer();
//		sb.append("<select style='display:none' id='grid1select'>");
//		try {
//			while(rs.next()){
//			     sb.append("<option value='").append(rs.getString("mingc")).append("'>");
//			     sb.append(rs.getString("mingc"));
//			     sb.append("</option>");
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//		sb.append("</select>");
//		this.setGrid1Html(sb.toString());
//		return this.Grid1Html;
//	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public IDropDownModel getYunsdwModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = "select id,mingc from yunsdwb where diancxxb_id="
			+ visit.getDiancxxb_id();
		return new IDropDownModel(sql);
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	
	private boolean ZSuodzt(String cheph){
		boolean issuod=false;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps= null;
		ResultSet rs=null;
		StringBuffer sql= new StringBuffer();
			sql.append("select cl.islocked from chelxxb cl where cl.cheph='");
			sql.append(cheph).append("'");
		ps=con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();			
			while(rs.next()){
				if(rs.getString("islocked").equals("1")){
					issuod=true;			
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
		        ps.close();
		        con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		return issuod;
	}
	
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Qicjjlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
			if(ZSuodzt(rsl.getString("cheph"))){
				setMsg("该车已被锁定,操作结束!");
				return;
			}
			long diancxxb_id = 0;
			if(visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
			}else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			

			String fahxxbm=this.getBianmValue().getValue();
			ResultSetList bmxxrs=con.getResultSetList(" select  \n" +
					"  (select cz.id  from chezxxb cz where cz.id=bm.faz_id) faz_id, \n" +
					"  (select cz.id  from chezxxb cz where cz.id=bm.daoz_id) daoz_id,\n" +
					"  (select fs.id from yunsfsb fs where fs.id=bm.yunsfsb_id) yunsfsb_id" +
					"  from fahxxbmb bm where bm.bianm='"+fahxxbm+"'");
			
			String faz_id_rs="1";
			String daoz_id_rs="1";
			String yunsfsb_id_rs="1";
			if(bmxxrs.next()){
				faz_id_rs=bmxxrs.getString("faz_id");
				daoz_id_rs=bmxxrs.getString("daoz_id");
				yunsfsb_id_rs=bmxxrs.getString("yunsfsb_id");
			
			}
			
			
			sb.append("insert into cheplsb\n");
			sb.append("(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, cheph, maoz, biaoz, sanfsl, meicb_id, jihkjb_id, yunsdwb_id,\n");
			sb.append(" zhongcsj , zhongcjjy, zhongchh, faz_id, daoz_id, fahrq, daohrq, chec, yunsfsb_id, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, lury, beiz, caiyrq,xiecfsb_id)\n");
			sb.append("values (getnewid(").append(diancxxb_id).append("),").append(diancxxb_id).append(",");
//			sb.append(((IDropDownModel)getGongysModel()).getBeanId(rsl.getString("gongysb_id"))).append(",");
//			sb.append(((IDropDownModel)getMeikModel()).getBeanId(rsl.getString("meikxxb_id"))).append(",");
			sb.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(rsl.getString("gongysb_id"))).append(",");
			sb.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(rsl.getString("meikxxb_id"))).append(",");
			sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id"))).append(",");
			//将车头和车号连接起来
			sb.append("'").append(rsl.getString("qiccht_id")+rsl.getString("cheph")).append("',");
			sb.append(rsl.getString("maoz")).append(",");
			sb.append(rsl.getString("biaoz")).append(",");
			sb.append(rsl.getString("sanfsl")).append(",");
			sb.append((getExtGrid().getColumn("meicb_id").combo).getBeanId(rsl.getString("meicb_id"))).append(",");
			sb.append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id"))).append(",");
			sb.append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id"))).append(",");
			sb.append(DateUtil.FormatOracleDateTime(new Date())).append(",");
			sb.append("'").append(rsl.getString("zhongcjjy")).append("','").append(rsl.getString("zhongchh")).append("',");
			sb.append(""+faz_id_rs+","+daoz_id_rs+",").append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",");
			sb.append("to_date('2050-12-31','yyyy-mm-dd'),'").append(rsl.getString("chec")).append("',")
			.append(SysConstant.YUNSFS_QIY).append(",").append(SysConstant.CHEB_QC).append(",1,");
			sb.append(diancxxb_id).append(",'");
			sb.append(rsl.getString("meikxxb_id")).append("','").append(rsl.getString("zhongcjjy"));
			sb.append("','").append(rsl.getString("beiz")).append("',");
			sb.append("to_date('").append(DateUtil.FormatDate(new Date())).append("','yyyy-mm-dd'),")
			.append(
							getExtGrid().getColumn("xiecfsb_id").combo
							.getBeanId(rsl.getString("xiecfsb_id")))
			.append(")");
//		System.out.println(sb);
		}
		if(sb.length() == 0) {
			WriteLog.writeErrorLog(ErrorMessage.Qicjjlr001);
			setMsg(ErrorMessage.Qicjjlr001);
			con.rollBack();
			con.Close();
			return;
		}
		flag = con.getInsert(sb.toString());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+sb);
			setMsg(ErrorMessage.Qicjjlr002);
			return;
		}
		flag = Jilcz.Updatezlid(con, visit.getDiancxxb_id(), SysConstant.YUNSFS_QIY, null);
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Qicjjlr003);
			setMsg(ErrorMessage.Qicjjlr003);
			return;
		}
		flag = Jilcz.INSorUpfahb(con, visit.getDiancxxb_id());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Qicjjlr004);
			setMsg(ErrorMessage.Qicjjlr004);
			return;
		}
		flag = Jilcz.InsChepb(con, visit.getDiancxxb_id(), SysConstant.YUNSFS_QIY, SysConstant.HEDBZ_TJ);
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Qicjjlr005);
			setMsg(ErrorMessage.Qicjjlr005);
			return;
		}
		sb.delete(0, sb.length());
		sb.append("select distinct fahb_id from cheplsb");
		rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Qicjjlr006);
			setMsg(ErrorMessage.Qicjjlr006);
			return;
		}
		while (rsl.next()) {
			flag = Jilcz.updateLieid(con, rsl.getString("fahb_id"));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjjlr007);
				setMsg(ErrorMessage.Qicjjlr007);
				return;
			}
		}
		con.commit();  
		con.Close();
		setMsg("毛重保存成功");
	}
	private boolean _SavePizChick = false;
	public void SavePizButton(IRequestCycle cycle) {
		_SavePizChick = true;
	}
	
	private boolean QSuodzt(String id){
		boolean issuod=false;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps= null;
		ResultSet rs=null;
		StringBuffer sql= new StringBuffer();
			sql.append("select cl.islocked from chepb cp,chelxxb cl where cp.id=");
			sql.append(id);
			sql.append(" and cp.cheph=cl.cheph");
		ps=con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();			
			while(rs.next()){
				if(rs.getString("islocked").equals("1")){
					issuod=true;			
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
		        ps.close();
		        con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		return issuod;
	}
	
	private void SavePiz() {
		if(getChange()==null || "".equals(getChange())) {
			setPTbmsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Qincjjlr.SavePiz 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
			String id = rsl.getString("id");
			double maoz = rsl.getDouble("maoz");
			double piz = rsl.getDouble("piz");
			double biaoz = rsl.getDouble("biaoz");
			double koud = rsl.getDouble("koud");
			double kous = rsl.getDouble("kous");
			double kouz = rsl.getDouble("kouz");
			double zongkd = koud + kous + kouz ;
			String fahbid = rsl.getString("fahb_id");
			if(biaoz == 0.0) {
				biaoz = maoz - piz-zongkd;
			}
//huochaoyuan 2009-11-4  修改标重为空时同步净重数量 			
			sb.delete(0, sb.length());
			if(QSuodzt(id)){
				setMsg("该车已被锁定,操作结束!");
				return;
			}
//			更新车皮表将 皮重、票重、轻车时间、轻车衡号、轻车人员 更新入车皮表
			sb.append("update chepb set piz = ").append(piz).append(",biaoz = ").append(biaoz).append(",koud=")
			.append(koud).append(",kous=").append(kous).append(",kouz=").append(kouz).append(",zongkd=").append(zongkd);
			sb.append(",qingcsj=sysdate,qingchh='").append(rsl.getString("qingchh")).append("',").append("beiz='").append(rsl.getString("beiz")).append("',");
			sb.append("qingcjjy='").append(rsl.getString("qingcjjy")).append("'");
			sb.append(",xiecfsb_id=").append("(select id from xiecfsb where mingc='"+rsl.getString("xiecfsb_id")+"' "+Jilcz.filterDcid(visit,"xiecfsb")+")");
			sb.append(" where id =").append(id);
			flag = con.getUpdate(sb.toString());
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjjlr008);
				setMsg(ErrorMessage.Qicjjlr008);
				return;
			}
//			保存车辆信息表
			Jilcz.SaveChelxx(con,visit.getDiancxxb_id(),getYunsdwModel().getBeanId(rsl.getString("yunsdwb_id")),SysConstant.YUNSFS_QIY,
					rsl.getString("CHEPH"),maoz,piz);
//			根据单车id 调用jilcz 中CountChepbYuns 方法计算单车的运损盈亏
			flag = Jilcz.CountChepbYuns(con, id, SysConstant.HEDBZ_YJJ);
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjjlr009);
				setMsg(ErrorMessage.Qicjjlr009);
				return;
			}
//			根据车皮所在fahid 调用Jilcz 中 updateFahb 方法更新发货表
			flag = Jilcz.updateFahb(con, fahbid);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjjlr010);
				setMsg(ErrorMessage.Qicjjlr010);
				return;
			}
			flag = Jilcz.updateLieid(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjjlr011);
				setMsg(ErrorMessage.Qicjjlr011);
				return;
			}
			visit.setString1(id);
		}
		con.commit();
		con.Close();
		setPTbmsg("皮重保存成功");
	}
	
	private boolean _AutoSaveChick = false;
	public void AutoSaveButton(IRequestCycle cycle) {
		_AutoSaveChick = true;
	}
	
	private void AutoSave() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Kongcjjlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
			String id = rsl.getString("id");
			double maoz = rsl.getDouble("maoz");
			double piz = getAutoPiz(con,rsl.getString("cheph"));
			if(piz==0) {
				setMsg("保存失败！该车无已检斤信息！");
				return;
			}
			double biaoz = rsl.getDouble("biaoz");
			double koud = rsl.getDouble("koud");
			String fahbid = rsl.getString("fahb_id");
			if(biaoz == 0.0) {
				biaoz = maoz - piz;
			}
			sb.delete(0, sb.length());
//			更新车皮表将 皮重、票重、备注 更新入车皮表
			sb.append("update chepb set piz = ").append(piz).append(",biaoz = ").append(biaoz).append(",koud=").append(koud).append(",zongkd=").append(koud);
			sb.append(",qingcsj = ").append("to_date('").append(DateUtil.FormatDateTime(new Date())).append("','yyyy-mm-dd hh24:mi:ss')");
			sb.append(",qingcjjy = '").append(rsl.getString("qingcjjy")).append("',qingchh = '").append(rsl.getString("qingchh"));
			
			//kouz 也有可能改变
			sb.append(",kouz=").append(rsl.getString("kouz"));
			
			sb.append("',beiz = '皮重取自历史检皮记录' where id =").append(id);
			flag = con.getUpdate(sb.toString());
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail+"SQL:"+sb);
				setMsg(ErrorMessage.Kongcjjlr001);
				return;
			}
//			根据单车id 调用jilcz 中CountChepbYuns 方法计算单车的运损盈亏
			flag = Jilcz.CountChepbYuns(con, id, SysConstant.HEDBZ_YJJ );
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjjlr002);
				setMsg(ErrorMessage.Kongcjjlr002);
				return;
			}
//			根据车皮所在fahid 调用Jilcz 中 updateFahb 方法更新发货表
			flag = Jilcz.updateFahb(con, fahbid);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjjlr003);
				setMsg(ErrorMessage.Kongcjjlr003);
				return;
			}
			flag = Jilcz.updateLieid(con,fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjjlr004);
				setMsg(ErrorMessage.Kongcjjlr004);
				return;
			}
			visit.setString1(id);
		}
		con.commit();
		con.Close();
		setMsg("保存成功");
	}
	
	private double getAutoPiz(JDBCcon con,String cheph) {
		double piz = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select max(piz) piz from chelxxb where cheph='")
		.append(cheph).append("' and yunsfsb_id=").append(SysConstant.YUNSFS_QIY);
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()) {
			piz = rsl.getDouble("piz");
		}
		return piz;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
		if (_SavePizChick) {
			_SavePizChick = false;
			SavePiz();
			init();
		}
		if (_AutoSaveChick) {
			_AutoSaveChick = false;
			AutoSave();
			init();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
	}
	
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择一行数据进行查看！");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		if(rsl.getRows()!=1) {
			setMsg("请选择一车进行打印！");
			return;
		}
		if(rsl.next()) {
			setChepid(rsl.getString("id"));
			this.setDiancxxb_id(rsl.getString("dc_id"));
		}
		cycle.activate("Qicjjd");
	}
	public void setChepid(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
	private void setDiancxxb_id(String id){
		
		((Visit)this.getPage().getVisit()).setString13(id);
		
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String tempst="";
		
		String fahxxbm=this.getBianmValue().getValue();
//		System.out.println(fahxxbm);
		StringBuffer sb = new StringBuffer();

		sb.append("select id,\n");
		sb.append("qiccht_id,\n"); 
		sb.append("cheph,\n"); 
		sb.append("diancxxb_id,\n"); 
		sb.append("gongysb_id,\n"); 
		sb.append("meikxxb_id,\n"); 
		sb.append("pinzb_id,\n"); 
		sb.append("sysdate fahrq,\n"); 
		sb.append("maoz,\n"); 
		sb.append("0 biaoz,\n"); 
		sb.append("sanfsl,\n"); 
		sb.append("chec,\n"); 
		sb.append("xiecfsb_id,\n"); 
		sb.append("meicb_id,\n"); 
		sb.append("jihkjb_id,\n"); 
		sb.append("yunsdwb_id,\n"); 
		sb.append("zhongcjjy,\n"); 
		sb.append("zhongchh,\n"); 
		sb.append("beiz from (\n");

		sb.append("select 0 id,qi.mingc qiccht_id, '' cheph, d.mingc diancxxb_id,(g.piny||'-'|| g.mingc) gongysb_id, (m.piny||'-'||m.mingc) meikxxb_id, p.mingc pinzb_id,\n");
		sb.append("f.fahrq, 0 maoz, c.biaoz, c.sanfsl,f.chec,")
		  .append("(select mingc from xiecfsb xi where xi.id=c.xiecfsb_id) xiecfsb_id")
		   .append(", mc.mingc meicb_id, j.mingc jihkjb_id, \n");
		sb.append("y.mingc yunsdwb_id, nvl('").append(visit.getRenymc()).append("','') zhongcjjy, c.zhongchh, '' beiz\n");
		sb.append("from qiccht qi,chepb c, fahb f, gongysb g, meikxxb m, pinzb p, jihkjb j, meicb mc, yunsdwb y, diancxxb d,FAHXXBMB bm \n");
		sb.append("where c.cheph like '%'||qi.mingc(+)||'%'  and c.id =(select max(id) id from chepb where chebb_id="+SysConstant.CHEB_QC+") and  f.id = c.fahb_id and f.yunsfsb_id=").append(SysConstant.YUNSFS_QIY).append(" \n");
		sb.append("and f.gongysb_id = g.id and f.meikxxb_id = m.id and f.diancxxb_id = d.id\n");
		sb.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id and c.hedbz<").append(SysConstant.HEDBZ_YJJ).append(" \n");
		sb.append("and c.meicb_id = mc.id(+) and c.yunsdwb_id = y.id(+)\n");
		
		sb.append("and  bm.bianm='"+fahxxbm+"' \n");
		sb.append("and  f.gongysb_id=bm.gongysb_id \n");
		sb.append("and  f.meikxxb_id=bm.meikxxb_id \n");
		sb.append("and  (  (nvl(bm.yunsfsb_id,'0')!='0' and f.yunsfsb_id=bm.yunsfsb_id) or (nvl(bm.yunsfsb_id,'0')='0') ) \n");
		sb.append("and  (  (nvl(bm.pinzb_id,'0')!='0' and f.pinzb_id=bm.pinzb_id) or (nvl(bm.pinzb_id,'0')='0') ) \n");
		sb.append("and  (  (nvl(bm.jihkjb_id,'0')!='0' and f.jihkjb_id=bm.jihkjb_id) or (nvl(bm.jihkjb_id,'0')='0') ) \n");
		sb.append("and  (  (nvl(bm.faz_id,'0')!='0' and f.faz_id=bm.faz_id) or (nvl(bm.faz_id,'0')='0') ) \n");
		sb.append("and  (  (nvl(bm.daoz_id,'0')!='0' and f.daoz_id=bm.daoz_id) or (nvl(bm.daoz_id,'0')='0') ) \n");
		sb.append("and  (  (nvl(bm.meicb_id,'0')!='0' and c.meicb_id=bm.meicb_id) or (nvl(bm.meicb_id,'0')='0') ) \n");
		sb.append("and  (  (nvl(bm.yunsdwb_id,'0')!='0' and c.yunsdwb_id=bm.yunsdwb_id) or (nvl(bm.yunsdwb_id,'0')='0') ) \n");
		sb.append("order by c.id desc)\n");
		sb.append(" where rownum = 1");
		
//		下面注释这句话是判定 汽车
		ResultSetList rsl = con.getResultSetList(sb.toString());
		ExtGridUtil egu;
		if(!rsl.next()) {
			sb.delete(0, sb.length());
			String moren_sql=" select zhi  from xitxxb where mingc='检斤默认值' and leib='数量' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
			ResultSetList rsl_mor=con.getResultSetList(moren_sql);
			
			
			ResultSetList bmxxrs=con.getResultSetList(" select  \n" +
					" nvl((select g.piny||'-'||g.mingc  from gongysb g where g.id=bm.gongysb_id),'-') gongysb_id,\n" +
					" nvl((select m.piny||'-'||m.mingc  from meikxxb m where m.id=bm.meikxxb_id),'-') meikxxb_id,\n" +
					" (select p.mingc from pinzb p where p.id=bm.pinzb_id) pinzb_id,\n" +
					" (select j.mingc  from jihkjb j where j.id=bm.jihkjb_id) jihkjb_id,\n" +
					" (select y.mingc from yunsdwb y where y.id=bm.yunsdwb_id) yunsdwb_id,\n" +
					" (select mc.mingc from meicb mc where mc.id=bm.meicb_id) meicb_id \n" +
					"  from fahxxbmb bm where bm.bianm='"+fahxxbm+"'");
			
			String gongysb_id_rs="-";
			String meikxxb_id_rs="-";
			String pinzb_id_rs="";
			String jihkjb_id_rs="";
			String yunsdwb_id_rs="";
			String meicb_id_rs="";
			if(bmxxrs.next()){
				gongysb_id_rs=bmxxrs.getString("gongysb_id");
				meikxxb_id_rs=bmxxrs.getString("meikxxb_id");
				pinzb_id_rs=bmxxrs.getString("pinzb_id");
				jihkjb_id_rs=bmxxrs.getString("jihkjb_id");
				yunsdwb_id_rs=bmxxrs.getString("yunsdwb_id");
				meicb_id_rs=bmxxrs.getString("meicb_id");
			}
			
			String dcid_rs=visit.getDiancxxb_id()+"";
			bmxxrs=con.getResultSetList( "select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
			
			if(bmxxrs.next()){
				dcid_rs=bmxxrs.getString("mingc");
			}
			
			if(rsl_mor.next()){//判断系统净重  字段 模式  
				sb.append("select 0 id,'' qiccht_id,'' cheph, '11#12#机' diancxxb_id, '-' gongysb_id, '-' meikxxb_id, '' pinzb_id,\n");
				sb.append("sysdate fahrq, 0 maoz, 0 biaoz, 0 sanfsl,'1' chec,'' yunsdwb_id,'机械' xiecfsb_id, '' meicb_id, '市场采购' jihkjb_id,\n");
				sb.append("nvl('").append(visit.getRenymc()).append("','') zhongcjjy, '' zhongchh, '' beiz from dual\n");
			}else{
				sb.append("select 0 id,'' qiccht_id,'' cheph, '"+dcid_rs+"' diancxxb_id, nvl('"+gongysb_id_rs+"','') gongysb_id, nvl('"+meikxxb_id_rs+"','') meikxxb_id, nvl('"+pinzb_id_rs+"','') pinzb_id,\n");
				sb.append("sysdate fahrq, 0 maoz, 0 biaoz, 0 sanfsl,'' chec,nvl('"+yunsdwb_id_rs+"','') yunsdwb_id,'' xiecfsb_id, nvl('"+meicb_id_rs+"','') meicb_id, nvl('"+jihkjb_id_rs+"','') jihkjb_id,\n");
				sb.append("nvl('").append(visit.getRenymc()).append("','') zhongcjjy, '' zhongchh, '' beiz from dual\n");
			}
			rsl = con.getResultSetList(sb.toString());
//			获取默认车号头
			String tempsql="select mingc from qiccht where zhuangt=1 order by xuh";
			ResultSetList temprsl=con.getResultSetList(tempsql);
			
			if(temprsl.next()){
				tempst=temprsl.getString("mingc");	 
			}
			temprsl.close();
		}else {
			rsl.beforefirst();
		}

		egu = new ExtGridUtil("gridDiv", rsl,"Zhongcjjlr_cz");
		//egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight(88);
		egu.addPaging(0);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		
		egu.getColumn("qiccht_id").setHeader("车头");
		egu.getColumn("qiccht_id").setWidth(80);
		egu.getColumn("qiccht_id").setEditor(null);
		egu.getColumn("qiccht_id").setDefaultValue(tempst);
		
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("cheph").editor.allowBlank=false;
		if(visit.isFencb()) {
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setEditor(null);
		}
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("maoz").editor.setAllowBlank(false);
		String sql = "select * from shuzhlfwb where leib ='数量' and mingc = '汽车衡毛重' and diancxxb_id = " + visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			egu.getColumn("maoz").editor.setMinValue(rsl.getString("xiax"));
			egu.getColumn("maoz").editor.setMaxValue(rsl.getString("shangx"));
		}
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(50);
		((NumberField)egu.getColumn("biaoz").editor).setDecimalPrecision(3);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setWidth(50);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("chec").editor.allowBlank = false;
		egu.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("meicb_id").setWidth(80);
		egu.getColumn("meicb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdwb_id").setWidth(80);
		egu.getColumn("yunsdwb_id").setEditor(null);
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongchh").setHeader(Locale.zhongchh_chepb);
		egu.getColumn("zhongchh").setWidth(70);
		egu.getColumn("zhongchh").setEditor(null);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(100);
		
		
//		设置供应商下拉框
		ComboBox cgys= new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cgys);
		cgys.setEditable(true);
/**
 * huochaoyuan
 * 2009-11-29修改供应商下拉框，过滤煤矿地区信息；
 */		
		String gysSql="select -1 id,'-' mingc from dual union select id,(piny||'-'||mingc) mingc from gongysb where leix=1 order by mingc";
//end		
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(gysSql));
			
//		设置煤矿单位下拉框
		ComboBox cmk= new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cmk);
		cmk.setEditable(true);
		String mkSql=" select -1 id,'-' mingc from dual union select id,(piny||'-'||mingc) mingc from meikxxb where zhuangt=1 order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(mkSql));
		
//		设置车头下拉框
		ComboBox qic=new ComboBox();
		egu.getColumn("qiccht_id").setEditor(qic);
		qic.setEditable(true);
		String qiccSql="select id ,mingc  from qiccht where zhuangt=1 order by xuh ";
		/**
		* huochaoyuan2009-02-18修改上边取数的排列方法，原来的order by mingc,改为order by xuh,顺序可由用户控制；
		*/		
		egu.getColumn("qiccht_id").setComboEditor(egu.gridId, new IDropDownModel(qiccSql));
		egu.getColumn("qiccht_id").editor.allowBlank = true;
//		设置品种下拉框
		ComboBox cpz=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(cpz);
		cpz.setEditable(true);
		String pinzSql=SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
		
//		设置口径下拉框
		ComboBox ckj=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(ckj);
		ckj.setEditable(true);
		String jihkjSql=SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
		
//		运输单位
		ComboBox cysdw = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(cysdw);
		cysdw.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="+ visit.getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,new IDropDownModel(yunsdwSql));
//      卸车方式下拉框和表头的设置
		egu.getColumn("xiecfsb_id").setHeader("卸车方式");
		ComboBox c11=new ComboBox();
		   c11.setTransform("xiecfsSe");
		   Visit v=(Visit)this.getPage().getVisit();
		   String XiecfsSql="select id,mingc from xiecfsb where diancxxb_id ="+visit.getDiancxxb_id()+" order by id";
		   c11.setEditable(true);
		   egu.getColumn("xiecfsb_id").setEditor(c11);
		   egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,new IDropDownModel(XiecfsSql));
//		煤场
		ComboBox cmc = new ComboBox();
		egu.getColumn("meicb_id").setEditor(cmc);
		cmc.setEditable(true);
		String cmcSql = "select id, mingc from meicb";
		egu.getColumn("meicb_id").setComboEditor(egu.gridId,new IDropDownModel(cmcSql));
		
		
		egu.addTbarText("发货编码:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("BianmDropDown");
		comb3.setId("bianm");
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(130);
		comb3.setEditable(true);
		egu.addToolbarItem(comb3.getScript());
		
		egu.addTbarText("-");
		
		GridButton refur = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refur.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refur);
		
		egu.addTbarText("-");
		
//		输入栏中的保存按钮
		String condi=" for(var i=0;i<gridDiv_ds.getModifiedRecords().length;i++){\n" +
				"var rec=gridDiv_ds.getModifiedRecords()[i];\n" +
				"if(rec.get('GONGYSB_ID')=='-' ){Ext.Msg.alert('提示信息','字段 供货单位 不能为空');return;}\n" +
				"if(rec.get('MEIKXXB_ID')=='-' ){Ext.Msg.alert('提示信息','字段 煤矿单位 不能为空');return;}\n" +
				"}\n";
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",condi);
		
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(){Mode='nosel';DataIndex='MAOZ';});");
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){ "
				+"if(e.field == 'MAOZ'){e.cancel=true;}});\n");
		setExtGrid(egu);

		
		String jingz_sql=" select zhi  from xitxxb where mingc='数量净重字段截取保留位数' and leib='数量' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl_jz=con.getResultSetList(jingz_sql);
		
		String baol_cu="-1";
		boolean jingz_flag=false;
		String jingz_cons=" (c.maoz-c.piz-c.kous-c.koud-c.kouz) ";//净重字段 用到的 计算公式
		if(rsl_jz.next()){//判断系统净重  字段 模式   
			baol_cu=rsl_jz.getString("zhi");
			jingz_flag=true;
			jingz_cons=" trunc((c.maoz-c.piz-c.koud),"+baol_cu+") ";
		}
		this.setJingzXt(" jingzXt="+baol_cu+";");
		
		sb.delete(0, sb.length());
		sb.append("select c.id,c.fahb_id, c.cheph, d.mingc diancxxb_id, g.mingc gongysb_id, m.mingc meikxxb_id, p.mingc pinzb_id,\n");
		sb.append("f.fahrq, c.maoz,c.piz, c.biaoz,"+jingz_cons+" as jingz, c.sanfsl, f.chec,")
		  .append("(select mingc from xiecfsb xi where xi.id=c.xiecfsb_id) xiecfsb_id")
		  .append(", c.koud, c.kous, c.kouz, mc.mingc meicb_id, j.mingc jihkjb_id, \n");
		sb.append("y.mingc yunsdwb_id,nvl('").append(visit.getRenymc()).append("','') qingcjjy, c.qingchh, c.beiz\n");
		sb.append("from chepb c, fahb f, gongysb g, meikxxb m, pinzb p, jihkjb j, meicb mc, yunsdwb y, diancxxb d\n");
		sb.append("where f.id = c.fahb_id and c.piz = 0 and f.yunsfsb_id=2 \n");
		sb.append("and f.gongysb_id = g.id and f.meikxxb_id = m.id and f.diancxxb_id = d.id\n");
		sb.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id\n");
		sb.append("and c.meicb_id = mc.id(+) and c.yunsdwb_id = y.id(+) order by c.zhongcsj desc\n");
//		ResultSetList 
		ResultSetList rsl1 = con.getResultSetList(sb.toString());
		if (rsl1 == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl1);
		//设置页面宽度
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		egu1.setHeight("bodyHeight-206");
		egu1.addPaging(12);
		egu1.getColumn("id").setHidden(true);
		egu1.getColumn("id").editor=null;
		egu1.getColumn("fahb_id").setHidden(true);
		egu1.getColumn("fahb_id").editor=null;
		egu1.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu1.getColumn("cheph").setWidth(60);
		egu1.getColumn("diancxxb_id").setHidden(true);
		egu1.getColumn("diancxxb_id").setEditor(null);
		egu1.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu1.getColumn("gongysb_id").setWidth(100);
		egu1.getColumn("gongysb_id").setEditor(null);
		egu1.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu1.getColumn("meikxxb_id").setWidth(100);
		egu1.getColumn("meikxxb_id").setEditor(null);
		egu1.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu1.getColumn("pinzb_id").setWidth(60);
		egu1.getColumn("pinzb_id").setEditor(null);
		egu1.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu1.getColumn("fahrq").setWidth(70);
		egu1.getColumn("fahrq").setEditor(null);
		egu1.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu1.getColumn("maoz").setWidth(50);
		egu1.getColumn("maoz").setEditor(null);
		egu1.getColumn("piz").setHeader(Locale.piz_chepb);
		egu1.getColumn("piz").setWidth(50);
	//	egu1.getColumn("piz").setEditor(null);
		egu1.getColumn("piz").editor.setAllowBlank(false);
		sql = "select * from shuzhlfwb where leib ='数量' and mingc = '汽车衡皮重' and diancxxb_id = " + visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			egu1.getColumn("piz").editor.setMinValue(rsl.getString("xiax"));
			egu1.getColumn("piz").editor.setMaxValue(rsl.getString("shangx"));
		}
//		egu1.getColumn("piz").setEditor(null);
		egu1.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu1.getColumn("biaoz").setWidth(50);
		egu1.getColumn("biaoz").setEditor(null);
		
		egu1.getColumn("jingz").setHeader("净重");
		egu1.getColumn("jingz").setWidth(50);
		egu1.getColumn("jingz").setEditor(null);
		
		
		
		egu1.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu1.getColumn("sanfsl").setWidth(50);
		egu1.getColumn("sanfsl").setEditor(null);
		egu1.getColumn("chec").setHeader(Locale.chec_fahb);
		egu1.getColumn("chec").setWidth(60);
		egu1.getColumn("chec").setEditor(null);
		
		egu1.getColumn("xiecfsb_id").setHeader(Locale.xiecfs_chepb);
		egu1.getColumn("xiecfsb_id").setWidth(60);
		egu1.getColumn("xiecfsb_id").setEditor(null);
		
		egu1.getColumn("koud").setHeader(Locale.koud_chepb);
		egu1.getColumn("koud").setWidth(60);
		egu1.getColumn("kous").setHeader(Locale.kous_chepb);
		egu1.getColumn("kous").setWidth(60);
		egu1.getColumn("kouz").setHeader(Locale.kouz_chepb);
		egu1.getColumn("kouz").setWidth(60);
		

		if(jingz_flag){//净重 是截取的    kous  kouz不显示
			egu1.getColumn("kous").setHidden(false);
			egu1.getColumn("kouz").setHidden(false);
		}
		
		
		sql = "select * from xitxxb where mingc = '扣水扣杂是否显示' " +
		"and zhuangt=1 and diancxxb_id =" + visit.getDiancxxb_id() +
		" and beiz= '使用' and zhi='是'";
		if(!con.getHasIt(sql)){
			egu1.getColumn("kous").setHidden(true);
			egu1.getColumn("kouz").setHidden(true);
		}
		egu1.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu1.getColumn("meicb_id").setWidth(80);
		egu1.getColumn("meicb_id").setEditor(null);
		egu1.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu1.getColumn("jihkjb_id").setWidth(80);
		egu1.getColumn("jihkjb_id").setEditor(null);
		egu1.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu1.getColumn("yunsdwb_id").setWidth(80);
		egu1.getColumn("yunsdwb_id").setEditor(null);
		egu1.getColumn("qingcjjy").setHeader(Locale.qingcjjy_chepb);
		egu1.getColumn("qingcjjy").setWidth(80);
		egu1.getColumn("qingcjjy").setEditor(null);
		egu1.getColumn("qingchh").setHeader(Locale.qingchh_chepb);
		egu1.getColumn("qingchh").setWidth(70);
		egu1.getColumn("qingchh").setEditor(null);
		egu1.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu1.getColumn("beiz").setWidth(100);
		egu1.getColumn("beiz").setEditor(null);
		
//		输入车号可以查到模糊对应的信息。-----------------------------------------------------------
		
		egu1.addTbarText("输入车号：");
		 TextField theKey=new TextField();
		 theKey.setId("theKey");
		 theKey.setListeners("change:function(thi,newva,oldva){  sta='';},specialkey:function(thi,e){if (e.getKey()==13) {chaxun();}}\n");
		 egu1.addToolbarItem(theKey.getScript());
//	  这是ext中的第二个egu，其中带有gridDiv字样的变量都比第一个多Piz字样，gridDiv----gridDivPiz.
		GridButton chazhao=new GridButton("（模糊）查找/查找下一个","function(){chaxun();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu1.addTbarBtn(chazhao);
		egu1.addTbarText("-");
		
		 String otherscript =  "var sta=''; function chaxun(){\n"+
			"       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('提示','请输入值');return;}\n"+
	        "       var len=gridDivPiz_data.length;\n"+
	        "       var count;\n"+
	        "       if(len%"+egu1.getPagSize()+"!=0){\n"+
	        "        count=parseInt(len/"+egu1.getPagSize()+")+1;\n"+
	        "        }else{\n"+
	        "          count=len/"+egu1.getPagSize()+";\n"+
	        "        }\n"+
	        "        for(var i=0;i<count;i++){\n"+
	        "           gridDivPiz_ds.load({params:{start:i*"+egu1.getPagSize()+", limit:"+egu1.getPagSize()+"}});\n"+
	        "           var rec=gridDivPiz_ds.getRange();\n "+
	        "           for(var j=0;j<rec.length;j++){\n "+
	        "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"+
	        "                 var nw=[rec[j]]\n"+
	        "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"+
	        "                      gridDivPiz_sm.selectRecords(nw);\n"+
	        "                      sta+=rec[j].get('ID').toString()+';';\n"+
	        "                       return;\n"+
	        "                  }\n"+
	        "                \n"+
	        "               }\n"+
	        "           }\n"+
	        "        }\n"+
	        "        if(sta==''){\n"+
	        "          Ext.MessageBox.alert('提示','你要找的车号不存在');\n"+
	        "        }else{\n"+
	        "           sta='';\n"+
	        "           Ext.MessageBox.alert('提示','查找已经到结尾');\n"+
	        "         }\n"+
		    "}\n";
	
		egu1.addOtherScript(otherscript);
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu1.addTbarBtn(refurbish);

		egu1.addToolbarButton(GridButton.ButtonType_Save, "SavePizButton");
		egu1.addToolbarButton("保存历史皮重",GridButton.ButtonType_Save, "AutoSaveButton", null, SysConstant.Btn_Icon_Save);
//      卸车方式下拉框和表头的设置
//		egu1.getColumn("xiecfsb_id").setHeader("卸车方式");
//		egu1.getColumn("xiecfsb_id").setWidth(90);
//		ComboBox c10=new ComboBox();
//		   c10.setTransform("grid1select");
//		   //Visit vi=(Visit)this.getPage().getVisit();
//		   //XiecfsSql="select id,mingc from xiecfsb where "+Jilcz.filterDcid(vi,null).substring(4)+" order by id";
//		   c10.setEditable(true);
//		   egu1.getColumn("xiecfsb_id").setEditor(c10);
		   //egu1.getColumn("xiecfsb_id").setComboEditor(egu1.gridId,new IDropDownModel(XiecfsSql));
//		打印按钮
		GridButton gbp = new GridButton("打印","function (){"+"document.all.ShowButton.click();"+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu1.addTbarBtn(gbp);
		egu1.addOtherScript("gridDivPiz_grid.on('rowclick',function(){Mode='sel';DataIndex='PIZ';});");
		egu1.addOtherScript("gridDiv_grid.on('beforeedit',function(e){ "
				+"if(e.field == 'PIZ'){e.cancel=true;}});\n");
		
		boolean kougFlag=false;
		String kouggs="KOUD";
		rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='扣矸是百分比录入'  and leib='数量' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
		if(rsl.next()){
			kougFlag=true;
			kouggs=rsl.getString("zhi");
		}
		
		boolean kousFlag=false;
		String kousgs="KOUS";
		rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='扣水是百分比录入'  and leib='数量' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
		if(rsl.next()){
			kousFlag=true;
			kousgs=rsl.getString("zhi");
		}
		
		boolean kouzFlag=false;
		String kouzgs="KOUZ";
		rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='扣杂是百分比录入'  and leib='数量' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
		if(rsl.next()){
			kouzFlag=true;
			kouzgs=rsl.getString("zhi");
		}
		
		if(kougFlag || kousFlag ||  kouzFlag){
			egu1.addOtherScript("gridDivPiz_grid.addListener('afteredit',function(e){ " +
					
					" var rec=e.record;\n" +
					" var MAOZ=rec.get('MAOZ');\n" +
					" var PIZ=rec.get('PIZ');\n"+
					" var KOUD=rec.get('KOUD');\n" +
					" var KOUS=rec.get('KOUS');\n" +
					" var KOUZ=rec.get('KOUZ');"+
					
					" if(KOUD==null || KOUD==''){KOUD=0;}\n"+
					" if(KOUS==null || KOUS==''){KOUS=0;}\n"+
					" if(KOUZ==null || KOUZ==''){KOUZ=0;}\n"+
					
					"if( e.field=='KOUD' ){\n" +
						" rec.set('KOUD',Round_new("+kouggs+",2) );"+
					"} \n" +
					
					"if( e.field=='KOUS' ){\n" +
					" rec.set('KOUS',Round_new("+kousgs+",2));"+
					"} \n" +
					
					"if( e.field=='KOUZ' ){\n" +
					" rec.set('KOUZ',Round_new("+kouzgs+",2));"+
					"} \n" +
					"" +
					
					" var bs=rec.get('BEIZ');\n" +
					" if(bs==null ||  bs==''){\n" +
					" bs='0,0,0';\n"+
					" }\n"+
					" var bssp=bs.split(',');\n"+//  格式  koud ， kous ， kouz
					"if(e.field=='KOUD'){bs=KOUD+','+bssp[1]+','+bssp[2];}\n" +
					"if(e.field=='KOUS'){bs=bssp[0]+','+KOUS+','+bssp[2];}\n" +
					"if(e.field=='KOUZ'){bs=bssp[0]+','+bssp[1]+','+KOUZ;}\n" +
				
					"rec.set('BEIZ',bs);\n"+
					
					"rec.set('JINGZ',Round_new(rec.get('MAOZ')-rec.get('PIZ')-rec.get('KOUD')-rec.get('KOUS')-rec.get('KOUZ'),2));\n" +
					"rec.set('BIAOZ',Round_new(rec.get('MAOZ')-rec.get('PIZ')-rec.get('KOUD')-rec.get('KOUS')-rec.get('KOUZ'),2));\n" +
					"" +
					"\n});" );
			
			this.setJingzXt(" jingzXt="+"-1"+";");
			
		}else{
		
		egu1.addOtherScript("gridDivPiz_grid.addListener('afteredit',function(e){ if(e.field=='PIZ'||e.field=='KOUD'||e.field=='KOUS'||e.field=='KOUZ'){ var rec=e.record;" 
				+" if(jingzXt<0){ if(DataIndex=='PIZ'){ var jin_va=(  parseFloat(rec.get('MAOZ'))-parseFloat(rec.get('PIZ'))-parseFloat(rec.get('KOUD'))-parseFloat(rec.get('KOUS'))-parseFloat(rec.get('KOUZ')) )+'';"
				+" rec.set('JINGZ',jin_va);} \n"
				+" }else{ \n"
				+" if(DataIndex=='PIZ'){ var jin_va=(  parseFloat(rec.get('MAOZ'))-parseFloat(rec.get('PIZ'))-parseFloat(rec.get('KOUD')) )+'';"
				+" var  koud_va=\"0\";"
				+"  var s=\"0.\";\n"
				+" var km=\"\";\n"
				+" jin_va=jin_va.substring(0,jin_va.lastIndexOf(\".\"))+jin_va.substr(jin_va.lastIndexOf(\".\"),6);\n"
				+" if(jin_va.lastIndexOf(\".\")>=0){ \n"
//				+" alert(jin_va.substring(jin_va.lastIndexOf(\".\")+1));"
				
				+" koud_va=\"0.\";"
				+" if( parseFloat(rec.get('MAOZ')) <  parseFloat(rec.get('PIZ'))){ \n"
				+" koud_va=\"-0.\"; \n"
				+" s=\"-0.\";\n"
				+" km=\"-\";\n"
				+" } \n"
				
				+" if(jin_va.substring(jin_va.lastIndexOf(\".\")+1).length>jingzXt){"
				
				+" for(var i=0;i<jingzXt;i++){ \n"
				+" koud_va+=\"0\"; \n"
				+" if(i!=jingzXt-1) \n"
				+" s+=\"0\";\n"
				+" } \n"
				
				+" var num_tem=Math.ceil(parseFloat(\"0.\"+jin_va.substr(jin_va.lastIndexOf(\".\")+1+jingzXt+1,2))); \n"
				+" var k_t=parseFloat(jin_va.substr(jin_va.lastIndexOf(\".\")+1+jingzXt,1))+num_tem; \n"
				
//				+" alert(num_tem); alert(k_t);"
				+" if(k_t>9){ s+=\"1\"; koud_va=\"0\"; rec.set('KOUZ',\"0\");} \n"
				+" else{koud_va+=k_t;rec.set('KOUZ',koud_va); } \n"
//				+" koud_va+=k_t;"
				
				//+" koud_va+=jin_va.substr(jin_va.lastIndexOf(\".\")+1+jingzXt,4); \n"
				+" rec.set('KOUZ',parseFloat(koud_va)); \n"
				+" } \n"
				+" else{ s=\"0\"; } \n"
				
//				+" alert(parseFloat(jin_va.substring(0,jin_va.lastIndexOf(\".\")))+'---'+km+'---'+parseFloat(km+\"0\"+jin_va.substr(jin_va.lastIndexOf(\".\"),jingzXt+1))+'---'+parseFloat(s));"
				
				
				//+"  jin_va=jin_va.substring(0,jin_va.lastIndexOf(\".\"))+jin_va.substr(jin_va.lastIndexOf(\".\"),jingzXt+1); \n"
				+"  jin_va=parseFloat(jin_va.substring(0,jin_va.lastIndexOf(\".\")))+parseFloat(km+\"0\"+jin_va.substr(jin_va.lastIndexOf(\".\"),jingzXt+1))+parseFloat(s)+\"\"; \n"
				
//				+" alert(jin_va);"
				
				+"  if( jin_va.lastIndexOf(\".\")!=-1 && jin_va.substring(jin_va.lastIndexOf(\".\")).length>jingzXt+1){ var kl=\"0.\";for(var i=0;i<jingzXt;i++){kl+=\"0\";} jin_va=parseFloat(jin_va.substring(0,jin_va.lastIndexOf(\".\")+jingzXt+1))+Math.ceil(parseFloat(kl+jin_va.substr(jin_va.lastIndexOf(\".\")+jingzXt+1,2))); } \n"
				+"  } \n"
				+" else{rec.set('KOUZ',\"0\"); } \n"
				+" rec.set('JINGZ',jin_va);  if(koud_va=='0.' || koud_va=='-0.'){koud_va=\"0\";} rec.set('KOUZ',koud_va);  } \n"
				+" } \n"
				+"}});");
		
		}
		
		setPizGrid(egu1);
		con.Close();
	}

	
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
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
	
	public String getGridScriptPiz() {
		if (getExtGrid() == null) {
			return "";
		}
		if(getPTbmsg()!=null) {
			getPizGrid().addToolbarItem("'->'");
			getPizGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getPTbmsg()+"</marquee>'");
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public DefaultTree getDefaultTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}
	
	public void setDefaultTree(DefaultTree dftree1) {
		((Visit) this.getPage().getVisit()).setDefaultTree(dftree1);
	}
	
	public String getTreeScript() {
//		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return getDefaultTree().getScript();
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
		String sql = "select id,mingc from meikxxb  where zhuangt=1 order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}
	
	private void setJianjdmodel(){
		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc='汽车衡检斤单模式' and zhuangt = 1 and diancxxb_id =" + ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String model = "";
		if(rsl.next()){
			model = rsl.getString("zhi");
		}
		rsl.close();
		((Visit) this.getPage().getVisit()).setString15(model);
	}
	
	

	// 发货信息编码
	public boolean _Bianmchange = false;

	private IDropDownBean _BianmValue;

	public IDropDownBean getBianmValue() {
		if (_BianmValue == null) {
			_BianmValue = (IDropDownBean) getIBianmModels()
					.getOption(0);
		}
		return _BianmValue;
	}

	public void setBianmValue(IDropDownBean Value) {
		long id = -2;
		if (_BianmValue != null) {
			id = _BianmValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Bianmchange = true;
			} else {
				_Bianmchange = false;
			}
		}
		_BianmValue = Value;
	}

	private IPropertySelectionModel _IBianmModel;

	public void setIBianmModel(IPropertySelectionModel value) {
		_IBianmModel = value;
	}

	public IPropertySelectionModel getIBianmModel() {
		if (_IBianmModel == null) {
			getIBianmModels();
		}
		return _IBianmModel;
	}

	public IPropertySelectionModel getIBianmModels() {
		_IBianmModel=new IDropDownModel(" select -1 id,'请选择' bianm from dual union select id,bianm from FAHXXBMB  where YUNSFSB_ID="+SysConstant.YUNSFS_QIY+" and zhuangt=1 order by id asc");
		return _IBianmModel;
	}
	
	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			init();
		}
/**
 * huochaoyuan
 * 2009-12-16添加以下调用函数语句，原因是打印随车检斤单后，点击返回按钮，返回该页面，一些参数没有刷新
 */		
		getSelectData();
//end		
	} 
	
	private void init() {
		
		this.setBianmValue(null);
		this.setIBianmModel(null);
		
		
		setGongysModels();
		setMeikModels();
		setJianjdmodel();
		setExtGrid(null);
		setPizGrid(null);
		setDefaultTree(null);
		getSelectData();
	}
}
