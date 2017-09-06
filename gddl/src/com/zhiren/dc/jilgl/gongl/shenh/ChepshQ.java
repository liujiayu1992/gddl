package com.zhiren.dc.jilgl.gongl.shenh;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class ChepshQ extends BasePage implements PageValidateListener {
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
	public String getFahids() {
		return ((Visit) this.getPage().getVisit()).getString1();
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
			setMsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		List fhlist = new ArrayList();
//		修改车皮
		ResultSetList rsld = getExtGrid().getModifyResultSet(getChange());
		if(rsld == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ChepshQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rsld.next()) {
			String chepid = rsld.getString("id");
			StringBuffer sb = new StringBuffer();
			sb.append("update chepb set biaoz=").append(rsld.getDouble("biaoz"));
			sb.append(",daozch='").append(rsld.getString("daozch"));
			sb.append("',beiz ='").append(rsld.getString("beiz"));
			sb.append("' where id=").append(chepid);
			int flag = con.getUpdate(sb.toString());
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.ChepshQ001);
				setMsg(ErrorMessage.ChepshQ001);
				return;
			}
			String yuanfhid = rsld.getString("fahbid");
			sb.delete(0, sb.length());
			sb.append("select f.id fahbid\n");
			sb.append("from fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,vwyuanshdw d,\n");
			sb.append("(select id from chezxxb where mingc='").append(rsld.getString("faz_id")).append("') fz,\n");
			sb.append("(select id from chezxxb where mingc='").append(rsld.getString("daoz_id")).append("') dz,\n");;
			sb.append("(select id from chezxxb where mingc='").append(rsld.getString("yuandz_id")).append("') yz\n");;
			sb.append("where f.fahrq = to_date('").append(rsld.getString("fahrq")).append("','yyyy-mm-dd')\n");
			sb.append("and f.daohrq = to_date('").append(rsld.getString("daohrq")).append("','yyyy-mm-dd')\n");
			sb.append("and f.chec ='").append(rsld.getString("chec")).append("' and g.mingc='").append(rsld.getString("gongysb_id")).append("'\n");
			sb.append("and m.mingc ='").append(rsld.getString("meikxxb_id")).append("' and p.mingc = '").append(rsld.getString("pinzb_id")).append("'\n");
			sb.append("and j.mingc = '").append(rsld.getString("jihkjb_id")).append("' and d.mingc = '").append(rsld.getString("yuanshdwb_id")).append("'\n");
			sb.append("and f.yuandz_id = yz.id and f.faz_id = fz.id and f.daoz_id = dz.id\n");
			sb.append("and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.pinzb_id=p.id\n");
			sb.append("and f.jihkjb_id=j.id and f.yuanshdwb_id=d.id and f.yunsfsb_id = 1\n");
			ResultSetList r = con.getResultSetList(sb.toString());
			///////////
			if(r.next()) {
				if(!yuanfhid.equals(r.getString("fahbid"))) {
					String sql = "update chepb set fahb_id ="+r.getString("fahbid")+" where id="+chepid;
					flag = con.getUpdate(sql);
					if(flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.ChepshQ002);
						setMsg(ErrorMessage.ChepshQ002);
						return;
					}
					Jilcz.addFahid(fhlist,r.getString("fahbid"));
				}
			}else {
//				新增发货
				String newFhid = MainGlobal.getNewID(visit.getDiancxxb_id());
				
				long meikxxb_id = getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rsld.getString("meikxxb_id"));
				long pinzb_id = getExtGrid().getColumn("pinzb_id").combo.getBeanId(rsld.getString("pinzb_id"));
				long diancxxb_id = rsld.getInt("diancxxb_id");
				double yunsl = Jilcz.getYunsl(diancxxb_id, pinzb_id, SysConstant.YUNSFS_QIY, meikxxb_id);
				sb.delete(0, sb.length());
				sb.append("insert into fahb (id,");
				sb.append("yuanid, diancxxb_id, gongysb_id, meikxxb_id,");
				sb.append("pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq,");
				sb.append("daohrq, zhilb_id, yunsfsb_id,");
				sb.append("chec,yuandz_id, yuanshdwb_id, yunsl) values(");
				sb.append(newFhid).append(",").append(newFhid);
				sb.append(",").append(diancxxb_id).append(",");
				sb.append(getExtGrid().getColumn("gongysb_id").combo.getBeanId(rsld.getString("gongysb_id"))).append(",");
				sb.append(meikxxb_id).append(",");
				sb.append(pinzb_id).append(",");
				sb.append(getExtGrid().getColumn("faz_id").combo.getBeanId(rsld.getString("faz_id"))).append(",");
				sb.append(getExtGrid().getColumn("daoz_id").combo.getBeanId(rsld.getString("daoz_id"))).append(",");
				sb.append(getExtGrid().getColumn("jihkjb_id").combo.getBeanId(rsld.getString("jihkjb_id"))).append(",");
				sb.append("to_date('").append(rsld.getString("fahrq")).append("','yyyy-mm-dd'),");
				sb.append("to_date('").append(rsld.getString("daohrq")).append("','yyyy-mm-dd'),");
				sb.append(rsld.getInt("zhilb_id")).append(",");
				sb.append("1,");
				sb.append("'").append(rsld.getString("chec")).append("',");
				sb.append(getExtGrid().getColumn("daoz_id").combo.getBeanId(rsld.getString("yuandz_id"))).append(",");
				sb.append(getExtGrid().getColumn("yuanshdwb_id").combo.getBeanId(rsld.getString("yuanshdwb_id"))).append(",");
				sb.append(yunsl).append(")");
				
				flag = con.getInsert(sb.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+sb);
					setMsg(ErrorMessage.ChepshQ003);
					return;
				}
				String sql = "update chepb set fahb_id ="+newFhid+" where id="+chepid;
				flag = con.getUpdate(sql);
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.ChepshQ004);
					setMsg(ErrorMessage.ChepshQ004);
					return;
				}
				Jilcz.addFahid(fhlist,newFhid);
			
			}
			flag = Jilcz.CountChepbYuns(con, chepid, SysConstant.HEDBZ_YJJ);
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.ChepshQ005);
				setMsg(ErrorMessage.ChepshQ005);
				return;
			}
			Jilcz.addFahid(fhlist,yuanfhid);
		}
		for(int i=0; i< fhlist.size() ;i++) {
			int flag = Jilcz.updateFahb(con,(String)fhlist.get(i));
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.ChepshQ006);
				setMsg(ErrorMessage.ChepshQ006);
				return;
			}
			flag = Jilcz.updateLieid(con, (String)fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.ChepshQ007);
				setMsg(ErrorMessage.ChepshQ007);
				return;
			}
		}
		con.commit();
		con.Close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist);
		setMsg("保存成功");
	}

	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _FanhClick = false;
	public void FanhButton(IRequestCycle cycle){
		_FanhClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_FanhClick){
			_FanhClick = false;
			Fanh(cycle);
		}
	}
//返回方法
	private void Fanh(IRequestCycle cycle){
		cycle.activate("ShujshQ");
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		StringBuffer sqlbf = new StringBuffer();
		sqlbf.append("select c.id,f.id fahbid,f.diancxxb_id,f.zhilb_id, g.mingc as gongysb_id, m.mingc as meikxxb_id,\n");
		sqlbf.append("(select mingc from chezxxb fc where fc.id = f.faz_id) faz_id, \n");
		sqlbf.append("p.mingc as pinzb_id, fahrq, c.cheph, c.maoz, c.piz, c.yuns, c.biaoz, c.zongkd, j.mingc as jihkjb_id, \n");
		sqlbf.append("daohrq, chec,cb.mingc as chebb_id,\n");
		sqlbf.append("(select mingc from chezxxb dc where dc.id = f.daoz_id) daoz_id,\n");
		sqlbf.append("(select mingc from chezxxb yc where yc.id = f.yuandz_id) yuandz_id,\n");
		sqlbf.append("d.mingc as yuanshdwb_id,yuanmkdw,daozch,c.beiz\n");
		sqlbf.append("from chepb c,fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,chebb cb,vwyuanshdw d\n");
		sqlbf.append("where f.id in (").append(getFahids()).append(")\n");
		sqlbf.append("and c.fahb_id=f.id and f.gongysb_id=g.id and f.meikxxb_id=m.id \n");
		sqlbf.append("and f.pinzb_id=p.id and f.jihkjb_id=j.id and c.chebb_id=cb.id \n");
		sqlbf.append("and f.yuanshdwb_id=d.id and f.yunsfsb_id = 2\n");
		ResultSetList rsl = con.getResultSetList(sqlbf.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sqlbf);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(25);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("fahbid").setHidden(true);
		egu.getColumn("fahbid").editor = null;
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("zhilb_id").editor = null;
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(110);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(80);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(55);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(55);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(77);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(77);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(45);
		egu.getColumn("maoz").editor = null;
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(45);
		egu.getColumn("piz").editor = null;
		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(45);
		egu.getColumn("yuns").editor = null;
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(45);
		egu.getColumn("zongkd").setHeader(Locale.zongkd_chepb);
		egu.getColumn("zongkd").setWidth(45);
		egu.getColumn("zongkd").editor = null;
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(50);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(40);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz_id").setWidth(65);
		egu.getColumn("yuandz_id").setEditor(null);
		egu.getColumn("yuandz_id").setHidden(true);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(90);
		egu.getColumn("yuanshdwb_id").setEditor(null);
		egu.getColumn("yuanshdwb_id").setHidden(true);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yuanmkdw").setWidth(90);
		egu.getColumn("yuanmkdw").setEditor(null);
		egu.getColumn("yuanmkdw").setHidden(true);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("daozch").setWidth(60);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(70);
		
		//设置供应商下拉框
		ComboBox c1= new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c1);
		c1.setEditable(true);
		String gysSql="select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(gysSql));
		
		//设置煤矿单位下拉框
		ComboBox c2= new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c2);
		c2.setEditable(true);
		String mkSql="select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(mkSql));
		
		//设置发站下拉框
		ComboBox c3= new ComboBox();
		egu.getColumn("faz_id").setEditor(c3);
		c3.setEditable(true);
		String FazSql="select id,mingc from chezxxb order by mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(FazSql));
		
		//设置品种下拉框
		ComboBox c4=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c4);
		c4.setEditable(true);
		String pinzSql="select id,mingc from pinzb order by mingc";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
		
		//设置口径下拉框
		ComboBox c5=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c5);
		c5.setEditable(true);
		String jihkjSql="select id,mingc from jihkjb";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
		
		//设置车别下拉框
		List ls= new ArrayList();
		ls.add(new IDropDownBean(1,"路车"));
		ls.add(new IDropDownBean(2,"自备车"));
		ComboBox c6=new ComboBox();
		egu.getColumn("chebb_id").setEditor(c6);
		c6.setEditable(true);
		egu.getColumn("chebb_id").setComboEditor(egu.gridId, new IDropDownModel(ls));
		egu.getColumn("chebb_id").setDefaultValue("路车");
		
		//设置到站下拉框
		ComboBox c7=new ComboBox();
		egu.getColumn("daoz_id").setEditor(c7);
		c1.setEditable(true);
		String daozSql="select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(daozSql));
		
		//设置原收货单位下拉框
		ComboBox c8=new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(c8);
		c8.setEditable(true);//设置可输入
		String Sql="select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId, new IDropDownModel(Sql));
		
		//设置按钮
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton btnreturn = new GridButton("返回",
		"function (){document.getElementById('FanhButton').click()}");
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);
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
			setTbmsg(null);
			getSelectData();
		}
	} 
}