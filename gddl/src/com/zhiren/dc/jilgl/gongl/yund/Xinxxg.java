package com.zhiren.dc.jilgl.gongl.yund;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xinxxg extends BasePage implements PageValidateListener {
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
	//绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
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
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		long diancxxb_id ;
		List fhlist = new ArrayList();
		ResultSetList rsld = getExtGrid().getDeleteResultSet(getChange());
		if(rsld == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Xinxxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
//		删除车皮
		while(rsld.next()) {
			String fahbid = rsld.getString("fahbid");
			Jilcz.addFahid(fhlist,fahbid);
			String id = rsld.getString("id");
			String sql = "delete from chepb where id ="+id;
			int flag = con.getDelete(sql);
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Xinxxg001);
				setMsg(ErrorMessage.Xinxxg001);
				return;
			}
		}
//		修改车皮
		rsld = getExtGrid().getModifyResultSet(getChange());
		if(rsld == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Xinxxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
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
			sb.append("update chepb set biaoz=").append(rsld.getDouble("biaoz"));
			sb.append(",cheph='").append(rsld.getString("cheph"));
			sb.append("',daozch='").append(rsld.getString("daozch"));
			sb.append("',beiz ='").append(rsld.getString("beiz"));
			Visit v=(Visit)this.getPage().getVisit();
			sb.append("',xiecfsb_id=").append("(select id from xiecfsb where mingc='"+rsld.getString("xiecfsb_id")+"' "+Jilcz.filterDcid(v,"xiecfsb")+")");
			sb.append(" where id=").append(chepid);
			int flag = con.getUpdate(sb.toString());
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Xinxxg002);
				setMsg(ErrorMessage.Xinxxg002);
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
			sb.append("and f.yuandz_id = yz.id and f.faz_id = fz.id and f.daoz_id = dz.id and f.diancxxb_id = ").append(diancxxb_id).append("\n");
			sb.append("and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.pinzb_id=p.id\n");
			sb.append("and f.jihkjb_id=j.id and f.yuanshdwb_id=d.id and f.yunsfsb_id ="+SysConstant.YUNSFS_QIY+"\n");
			ResultSetList r = con.getResultSetList(sb.toString());
			if (r == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
				setMsg(ErrorMessage.NullResult);
				return;
			}
			if(r.next()) {
				if(!yuanfhid.equals(r.getString("fahbid"))) {
					String sql = "update chepb set fahb_id ="+r.getString("fahbid")+" where id="+chepid;
					flag = con.getUpdate(sql);
					if(flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.Xinxxg003);
						setMsg(ErrorMessage.Xinxxg003);
						return;
					}
					Jilcz.addFahid(fhlist,r.getString("fahbid"));
				}
			}else {
//				新增发货
				String newFhid = MainGlobal.getNewID(diancxxb_id);
				long meikxxb_id = ((IDropDownModel)getMeikModel())
				.getBeanId(rsld.getString("meikxxb_id"));
				long pinzb_id = getExtGrid().getColumn("pinzb_id").combo.getBeanId(rsld.getString("pinzb_id"));
				double yunsl = Jilcz.getYunsl(diancxxb_id, pinzb_id, SysConstant.YUNSFS_QIY, meikxxb_id);
				sb.delete(0, sb.length());
				sb.append("insert into fahb (id,");
				sb.append("yuanid, diancxxb_id, gongysb_id, meikxxb_id,");
				sb.append("pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq,");
				sb.append("daohrq, zhilb_id, yunsfsb_id,");
				sb.append("chec,yuandz_id, yuanshdwb_id, yunsl) values(");
				sb.append(newFhid).append(",").append(newFhid);
				sb.append(",").append(diancxxb_id).append(",");
				sb.append(((IDropDownModel)getGongysModel())
						.getBeanId(rsld.getString("gongysb_id"))).append(",");
				sb.append(meikxxb_id).append(",");
				sb.append(pinzb_id).append(",1,1,");
				sb.append(getExtGrid().getColumn("jihkjb_id").combo.getBeanId(rsld.getString("jihkjb_id"))).append(",");
				sb.append("to_date('").append(rsld.getString("fahrq")).append("','yyyy-mm-dd'),");
				sb.append("to_date('").append(rsld.getString("daohrq")).append("','yyyy-mm-dd'),");
				sb.append(rsld.getInt("zhilb_id")).append(",");
				sb.append(SysConstant.YUNSFS_QIY).append(",'");
				sb.append(rsld.getString("chec")).append("',1,");
				sb.append(getExtGrid().getColumn("yuanshdwb_id").combo.getBeanId(rsld.getString("yuanshdwb_id"))).append(",");
				sb.append(yunsl).append(")");
				
				flag = con.getInsert(sb.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "引发错误SQL:" + sb);
					setMsg(ErrorMessage.Xinxxg004);
					return;
				}
				String sql = "update chepb set fahb_id ="+newFhid+" where id="+chepid;
				flag = con.getUpdate(sql);
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Xinxxg005);
					setMsg(ErrorMessage.Xinxxg005);
					return;
				}
				Jilcz.addFahid(fhlist,newFhid);
			}
			flag = Jilcz.CountChepbYuns(con,chepid,SysConstant.HEDBZ_YJJ);
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Xinxxg006);
				setMsg(ErrorMessage.Xinxxg006);
				return;
			}
			Jilcz.addFahid(fhlist,yuanfhid);
		}
		for(int i=0; i< fhlist.size() ;i++) {
			int flag = Jilcz.updateFahb(con,(String)fhlist.get(i));
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Xinxxg007);
				setMsg(ErrorMessage.Xinxxg007);
				return;
			}
			flag = Jilcz.updateLieid(con,(String)fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Xinxxg008);
				setMsg(ErrorMessage.Xinxxg008);
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
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sqlbf = new StringBuffer();
		String guohsj = getGuohsjValue()==null?DateUtil.FormatDateTime(new Date()):getGuohsjValue().getValue();
		sqlbf.append("select c.id,f.id fahbid,d.mingc  diancxxb_id,f.zhilb_id, cheph, c.biaoz, g.mingc as gongysb_id, m.mingc as meikxxb_id,\n");
		sqlbf.append("(select mingc from chezxxb fc where fc.id = f.faz_id) faz_id, \n");
		sqlbf.append("p.mingc as pinzb_id, j.mingc as jihkjb_id, fahrq,\n");
		sqlbf.append("daohrq, chec,cb.mingc as chebb_id,\n");
		sqlbf .append("(select mingc from xiecfsb xi where xi.id=c.xiecfsb_id) xiecfsb_id,");
		sqlbf.append("(select mingc from chezxxb dc where dc.id = f.daoz_id) daoz_id,\n");
		sqlbf.append("(select mingc from chezxxb yc where yc.id = f.yuandz_id) yuandz_id,\n");
		sqlbf.append("vy.mingc yuanshdwb_id,yuanmkdw,daozch\n"); 
		sqlbf.append(",c.beiz\n");
		sqlbf.append("from chepb c,fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,chebb cb,diancxxb d,vwyuanshdw vy \n");
		sqlbf.append("where lursj=to_date('").append(guohsj).append("','yyyy-mm-dd hh24:mi:ss')\n");
		sqlbf.append("and c.fahb_id=f.id and f.gongysb_id=g.id and f.meikxxb_id=m.id \n");
		sqlbf.append("and f.pinzb_id=p.id and f.jihkjb_id=j.id and c.chebb_id=cb.id \n");
		sqlbf.append("and f.diancxxb_id=d.id and f.yuanshdwb_id = vy.id and f.yunsfsb_id = 2\n");
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
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(90);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("faz_id").setHidden(true);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("chebb_id").setEditor(null);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("daoz_id").setHidden(true);
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("yuandz_id").setHidden(true);
		egu.getColumn("yuandz_id").setEditor(null);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(90);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yuanmkdw").setWidth(90);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("xiecfsb_id").setHeader("卸车方式");
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		
		egu.addTbarText("录入时间：");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(150);
		comb1.setTransform("Guohsj");
		comb1.setId("Guohsj");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		egu.addToolbarItem(comb1.getScript());
		//设置供应商下拉框
		/*ComboBox c8= new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c8);
		c8.setEditable(true);
		String gysSql="select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(gysSql));
		//设置煤矿单位下拉框
		ComboBox c9= new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c9);
		c9.setEditable(true);
		String mkSql="select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(mkSql));
	    *///设置默认到站
		egu.getColumn("daoz_id").setDefaultValue(visit.getDaoz());
        //设置发货日期和到货日期的默认值
		egu.getColumn("fahrq").setDefaultValue(this.getRiq());
		egu.getColumn("daohrq").setDefaultValue(this.getRiq());
		//设置品种下拉框
		ComboBox c2=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c2);
		c2.setEditable(true);
		String pinzSql=SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
		//设置口径下拉框
		ComboBox c3=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c3);
		c3.setEditable(true);
		String jihkjSql=SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
		//设置原收货单位下拉框
		ComboBox c7=new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(c7);
		c7.setEditable(true);//设置可输入
		String Sql="select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId, new IDropDownModel(Sql));
		egu.getColumn("yuanshdwb_id").setDefaultValue(""+((Visit) getPage().getVisit()).getDiancmc());
		//设置卸车方式下拉菜单
		ComboBox c11=new ComboBox();
		   Visit v=(Visit)this.getPage().getVisit();
		   String XiecfsSql="select id,mingc from xiecfsb where "+Jilcz.filterDcid(v,null).substring(4)+" order by id";
		   c11.setEditable(true);
		   egu.getColumn("xiecfsb_id").setEditor(c11);
		   egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,new IDropDownModel(XiecfsSql));
		//设置原煤矿单位下拉框
		ComboBox c10= new ComboBox();
		egu.getColumn("yuanmkdw").setEditor(c10);
		c10.setEditable(true);
		String mkSql1="select id,mingc from meikxxb order by mingc";
		egu.getColumn("yuanmkdw").setComboEditor(egu.gridId, new IDropDownModel(mkSql1));
		egu.addOtherScript("Guohsj.on('select',function(){document.forms[0].submit();});");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript("gridDiv_ds.on('add',function(own,rec,i){rec[0].set('Guohsj',guohrq.getValue().dateFormat('Y-m-d') + ' '+ HOUR.getRawValue()+':'+MIN.getRawValue()+':00');});\n");
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+"if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"gongysTree_window.show();}});");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_kj,"gongysTree"
				,""+visit.getDiancxxb_id(),null,null,null);
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
		.append("rec.set('MEIKXXB_ID', cks.parentNode.text);rec.set('JIHKJB_ID', cks.text); \n")
		.append("gongysTree_window.hide(); \n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
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
	
	public IDropDownBean getGuohsjValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getGuohsjModel().getOptionCount() > 0) {
				setGuohsjValue((IDropDownBean) getGuohsjModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}
	public void setGuohsjValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(value);
	}
	public IPropertySelectionModel getGuohsjModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setGuohsjModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setGuohsjModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}
	public void setGuohsjModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select rownum,lursj from (select distinct to_char(lursj,'yyyy-mm-dd hh24:mi:ss') as lursj \n");
		sb.append(" from chepb c,fahb f where c.hedbz<3 and f.yunsfsb_id = ").append(SysConstant.YUNSFS_QIY);
		sb.append(" and c.fahb_id=f.id order by lursj desc)");
		setGuohsjModel(new IDropDownModel(sb.toString()));
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
			setGuohsjValue(null);
			setGuohsjModel(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setTbmsg(null);
		}
		getSelectData();
	} 
}