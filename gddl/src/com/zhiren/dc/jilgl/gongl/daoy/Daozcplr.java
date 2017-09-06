package com.zhiren.dc.jilgl.gongl.daoy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
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

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:zl
 * 时间:2009-09-23
 * 修改内容:增加倒装车皮录入功能
 */

public class Daozcplr extends BasePage implements PageValidateListener {
	//导入数据缓冲容器
	private List daordata=null;
	// 界面用户提示
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

	// 绑定日期
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
		Visit visit = (Visit) this.getPage().getVisit();
		Save(getChange(), visit);
	}
	
	public void Save(String strchange,Visit visit) {
		String sql="";
		JDBCcon con = new JDBCcon();
		ResultSetList delrsl = visit.getExtGrid1().  getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql="delete from daozcpb where id ="+delrsl.getInt("ID");
			con.getUpdate(sql);
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Daozcplr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		
		// 插入车皮临时表
		
		
		while (rsl.next()) {
//			String diancxxb_id = getExtGrid().getColumn("diancxxb_id")
//			.combo.getBeanStrId(rsl.getString("diancxxb_id"));
			double Jingz = 0.0;
			double Biaoz = 0.0;
			double Yunsl = 0.0;
			double Yingd = 0.0;
			double Yingk = 0.0;
			Jingz = rsl.getDouble("maoz")-rsl.getDouble("piz");
			Biaoz = rsl.getDouble("biaoz");
			if(Jingz-Biaoz>0){
				Yingk=Jingz-Biaoz;
				Yingd=Jingz-Biaoz;
			}else{
				Yingk=Jingz-Biaoz;
			}
			
			
			String diancxxb_id="";
			TreeNode tn=this.getTree().getRootNode();
			if(tn.getChildNodes()==null || tn.getChildNodes().size()==0 || tn.getText().equals(rsl.getString("diancxxb_id"))){
				diancxxb_id=this.getTreeid();
			}else{
				for(int i=0;i<tn.getChildNodes().size();i++){
					TreeNode node=(TreeNode)tn.getChildNodes().get(i);
					if(node.getText().equals(rsl.getString("diancxxb_id"))){
						diancxxb_id=node.getId();
					}
				}
			}
			if(rsl.getInt("id")==0){
				sb.append("insert into daozcpb\n");
				sb.append("(id,fahb_id,diancxxb_id,piaojh,gongysmc,meikdwmc,pinz,faz,daoz,jihkj,fahrq,daohrq,hetb_id,zhilb_id,caiybh,jiesb_id,yunsfs,");
				sb.append("chec,cheph,maoz,piz,biaoz,yingd,yingk,yuns,yunsl,koud,kous,kouz,zongkd,sanfsl,ches,jianjfs,qingcsj,qingchh,qingcjjy,");
				sb.append("zhongcsj,zhongchh,zhongcjjy,meicb_id,daozch,lury,beiz,caiyrq,lursj,yunsdw,xiecfs,yuanmz,yuanpz,kuangfzlzb_id,koum)");
				sb.append(" values (getnewid(").append(diancxxb_id).append("),0,").append(diancxxb_id).append(",'").append(rsl.getString("piaojh"));
				sb.append("',").append(((IDropDownModel) getGongysModel()).getBeanId(rsl.getString("gongysb_id"))).append(",");
				sb.append(((IDropDownModel) getMeikModel()).getBeanId(rsl.getString("meikxxb_id"))).append(",");
				sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
				sb.append(",").append((getExtGrid().getColumn("faz_id").combo).getBeanId(rsl.getString("faz_id")));
				sb.append(",").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id")));
				sb.append(",").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
				sb.append(",").append(DateUtil.FormatOracleDate(rsl.getString("fahrq")));
				sb.append(",").append(DateUtil.FormatOracleDate(rsl.getString("daohrq")));
				sb.append(",0,0,0,0,'公路','").append(rsl.getString("chec")).append("','").append(rsl.getString("cheph"));
				sb.append("',").append(rsl.getDouble("maoz")).append(",").append(rsl.getDouble("piz"));
				sb.append(",").append(rsl.getDouble("biaoz")).append(",").append(Yingd).append(",").append(Yingk).append(",").append(rsl.getDouble("yuns")).append(",").append(rsl.getDouble("yunsl"));
				sb.append(",0,0,0,0,0,0,'过衡',").append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",'A','").append(rsl.getString("lury"));
				sb.append("',").append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",'A','").append(rsl.getString("lury"));
				sb.append("',").append((getExtGrid().getColumn("meicb_id").combo).getBeanId(rsl.getString("meicb_id")));
				sb.append(",0,'").append(rsl.getString("lury")).append("','").append(rsl.getString("beiz"));
				sb.append("',").append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",");
				sb.append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",").append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id")));
				sb.append(",").append((getExtGrid().getColumn("xiecfsb_id").combo).getBeanId(rsl.getString("xiecfsb_id")));
				sb.append(",").append(rsl.getDouble("maoz")).append(",").append(rsl.getDouble("piz"));
				sb.append(",0,0); \n");
			}else{
				sb.append("update daozcpb set id=").append(rsl.getString("ID"));
				sb.append(",fahb_id=0,diancxxb_id=").append(diancxxb_id).append(",piaojh='").append(rsl.getString("piaojh"));
				sb.append("',gongysmc=").append(((IDropDownModel) getGongysModel()).getBeanId(rsl.getString("gongysb_id"))).append(",meikdwmc=");
				sb.append(((IDropDownModel) getMeikModel()).getBeanId(rsl.getString("meikxxb_id"))).append(",pinz=");
				sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
				sb.append(",faz=").append((getExtGrid().getColumn("faz_id").combo).getBeanId(rsl.getString("faz_id")));
				sb.append(",daoz=").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id")));
				sb.append(",jihkj=").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
				sb.append(",fahrq=").append(DateUtil.FormatOracleDate(rsl.getString("fahrq")));
				sb.append(",daohrq=").append(DateUtil.FormatOracleDate(rsl.getString("daohrq")));
				sb.append(",hetb_id=0,zhilb_id=0,caiybh=0,jiesb_id=0,yunsfs='公路',chec='").append(rsl.getString("chec")).append("',cheph='").append(rsl.getString("cheph"));
				sb.append("',maoz=").append(rsl.getDouble("maoz")).append(",piz=").append(rsl.getDouble("piz"));
				sb.append(",biaoz=").append(rsl.getDouble("biaoz")).append(",yingd=").append(Yingd).append(",yingk=").append(Yingk).append(",yuns=").append(rsl.getDouble("yuns")).append(",yunsl=").append(rsl.getDouble("yunsl"));
				sb.append(",koud=0,kous=0,kouz=0,zongkd=0,sanfsl=0,ches=0,jianjfs='过衡',qingcsj=").append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",qingchh='A',qingcjjy='").append(rsl.getString("lury"));
				sb.append("',zhongcsj=").append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",zhongchh='A',zhongcjjy='").append(rsl.getString("lury"));
				sb.append("',meicb_id=").append((getExtGrid().getColumn("meicb_id").combo).getBeanId(rsl.getString("meicb_id")));
				sb.append(",daozch=0,lury='").append(rsl.getString("lury")).append("',beiz='").append(rsl.getString("beiz"));
				sb.append("',caiyrq=").append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",lursj=");
				sb.append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",yunsdw=").append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id")));
				sb.append(",xiecfs=").append((getExtGrid().getColumn("xiecfsb_id").combo).getBeanId(rsl.getString("xiecfsb_id")));
				sb.append(",yuanmz=").append(rsl.getDouble("maoz")).append(",yuanpz=").append(rsl.getDouble("piz"));
				sb.append(",kuangfzlzb_id=0,koum=0 where id=").append(rsl.getInt("id")).append(";\n");
			}
			sb.append("end;");
			con.getResultSet(sb.toString());
		}
		
		con.Close();
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
    private boolean _DaorClick=false;
    public void DaorButton(IRequestCycle cycle){
    	_DaorClick=true;
    }
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if(_DaorClick){
			_DaorClick=false;
		}
		if(_RefreshChick){
			_RefreshChick = false;
		}
		getSelectData();
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="";
		String b="";
		if(getKuangbValue().getId()==-1){
			b = "";
		}else{
			b = " and d.meikdwmc="+getKuangbValue().getId();
		}
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
		ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		StringBuffer sb = new StringBuffer();
		sb.append("select d.id,dc.mingc diancxxb_id,g.mingc gongysb_id,m.mingc meikxxb_id,(select mingc from chezxxb where id=d.faz) faz_id, \n");
		sb.append("(select mingc from chezxxb where id=d.daoz) daoz_id, \n");
		sb.append("d.piaojh,cheph,maoz,piz,biaoz,p.mingc pinzb_id,j.mingc jihkjb_id,d.fahrq,d.daohrq,yingd,yingk,d.chec,mc.mingc as meicb_id,lury,caiyrq,x.mingc xiecfsb_id,y.mingc yunsdwb_id,d.beiz \n");
		sb.append("from daozcpb d,diancxxb dc,pinzb p,gongysb g,meikxxb m,chezxxb c,jihkjb j,xiecfsb x,meicb mc,yunsdwb y \n");
		sb.append("where d.diancxxb_id=dc.id and d.pinz=p.id and d.gongysmc=g.id and d.meikdwmc=m.id and d.yunsdw=y.id(+) \n");
		sb.append("and d.jihkj=j.id(+) and d.xiecfs=x.id(+) and d.faz=c.id(+) and d.meicb_id=mc.id(+) and daohrq=to_date('").append(getRiq()).append("','yyyy-mm-dd')").append(b);
	
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.addPaging(0);
		ComboBox dc = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		dc.setEditable(true);
		sql = 
			"select id, mingc from (\n" +
			"select id, mingc, level,CONNECT_BY_ISLEAF leaf\n" + 
			"  from diancxxb\n" + 
			" start with id = "+getTreeid()+"\n" + 
			"connect by fuid = prior id)" ;
		IDropDownModel dcmd = new IDropDownModel(sql);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,dcmd);
		if(dcmd.getOptionCount()>1){
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else{
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
			//egu.getColumn("diancxxb_id").setDefaultValue(dcmd.getBeanValue(getTreeid()));
			
			String dc_value="";
			TreeNode tn=this.getTree().getRootNode();
			if(tn.getChildNodes()==null || tn.getChildNodes().size()==0 || tn.getId().equals(this.getTreeid())){
				dc_value=tn.getText();
			}else{
				for(int i=0;i<tn.getChildNodes().size();i++){
					TreeNode node=(TreeNode)tn.getChildNodes().get(i);
					if((node.getId()+"").equals(this.getTreeid())){
						dc_value=node.getText();
					}
				}
			}
			
			egu.getColumn("diancxxb_id").setDefaultValue(dc_value);
		}
		
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("piaojh").setHeader("票据号");
//		egu.getColumn("piaojh").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("pinzb_id").setDefaultValue("原煤");
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("jihkjb_id").setDefaultValue("市场采购");
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("caiyrq").setHeader(Locale.caiyrq_caiyb);
		egu.getColumn("caiyrq").setWidth(70);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("xiecfsb_id").setHeader(Locale.xiecfs_chepb);
		egu.getColumn("xiecfsb_id").setWidth(75);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
			egu.getColumn("piaojh").editor
			.setListeners("specialkey:function(own,e){if(gridDiv_grid.getStore().getAt(row).get('BULSJ')!=0) gridDiv_grid.startEditing(row , 8); }");
			egu.getColumn("cheph").editor
			.setListeners("specialkey:function(own,e){if(gridDiv_grid.getStore().getAt(row).get('BULSJ')!=0) gridDiv_grid.startEditing(row , 9); }");
			egu.getColumn("maoz").editor
			.setListeners("specialkey:function(own,e){if(gridDiv_grid.getStore().getAt(row).get('BULSJ')!=0) gridDiv_grid.startEditing(row , 10); }");
			egu.getColumn("piz").editor
			.setListeners("specialkey:function(own,e){if(gridDiv_grid.getStore().getAt(row).get('BULSJ')!=0) gridDiv_grid.startEditing(row , 11); }");
		
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(65);
		
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
		
		
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(65);
		egu.getColumn("yingd").setHeader(Locale.yingd_chepb);
		egu.getColumn("yingd").setWidth(65);
		egu.getColumn("yingd").setEditor(null);
		egu.getColumn("yingk").setHeader(Locale.yingk_chepb);
		egu.getColumn("yingk").setWidth(65);
		egu.getColumn("yingk").setEditor(null);
//		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
//		egu.getColumn("yuns").setEditor(null);
//		egu.getColumn("yuns").setWidth(65);
//		egu.getColumn("yunsl").setHeader(Locale.yunsl_fahb);
//		egu.getColumn("yunsl").setWidth(65);
		egu.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("meicb_id").setWidth(65);
		egu.getColumn("lury").setHeader(Locale.lury_chepb);
		egu.getColumn("lury").setWidth(65);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdwb_id").setWidth(65);
		
		StringBuffer lins = new StringBuffer();
		lins
				.append("specialkey:function(own,e){ \n")
				.append("if(row+1 == gridDiv_grid.getStore().getCount()){ \n")
				.append("Ext.MessageBox.alert('提示信息','已到达数据末尾！');return; \n")
				.append("} \n")
				.append("row = row+1; \n")
				.append(
						"last = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("gridDiv_grid.getSelectionModel().selectRow(row); \n")
				.append(
						"cur = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("copylastrec(last,cur); \n")
				.append("if(gridDiv_grid.getStore().getAt(row).get('BULSJ')==0){\n")
				.append("   gridDiv_grid.startEditing(row , 11);\n")
				.append("	   return;\n")
				.append("	}\n") 
				.append(
						"gridDiv_grid.startEditing(row , 7); }");
		egu.getColumn("biaoz").editor.setListeners(lins.toString());
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.addTbarText("过衡时间:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());
//		 矿别下拉框
		egu.addTbarText("矿别:");
		ComboBox kuangb = new ComboBox();
		kuangb.setTransform("KuangbSelect");
		kuangb.setWidth(150);
		kuangb.setListeners("select:function(own,rec,index){Ext.getDom('KuangbSelect').selectedIndex=index}");
		egu.addToolbarItem(kuangb.getScript());
		// 卸车方式表下拉框
		ComboBox c8 = new ComboBox();
		egu.getColumn("xiecfsb_id").setEditor(c8);
		c8.setEditable(true);
		egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_xiecfs));

		// 设置发货日期和到货日期的默认值
		egu.getColumn("fahrq").setDefaultValue(this.getRiq());
		egu.getColumn("daohrq").setDefaultValue(this.getRiq());
		egu.getColumn("caiyrq").setDefaultValue(this.getRiq());
		// 设置发站下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("faz_id").setEditor(c1);
		c1.setEditable(true);
		String fazSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
//		 设置到站下拉框
		ComboBox c4 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c4);
		c4.setEditable(true);
		String daozSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		egu.getColumn("daoz_id").setDefaultValue(visit.getDaoz()==null?"":visit.getDaoz());
		// 设置品种下拉框
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c2);
		c2.setEditable(true);
		String pinzSql = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		// 设置口径下拉框
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c3);
		c3.setEditable(true);
		String jihkjSql = SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjSql));
		// 设置运输单位下拉框
		ComboBox comb = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(comb);
		comb.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsdwSql));
		egu.getColumn("yunsdwb_id").setDefaultValue("请选择");
		egu.getColumn("yunsdwb_id").editor.setAllowBlank(true);

//		 设置煤场下拉框
		ComboBox c5 = new ComboBox();
		egu.getColumn("meicb_id").setEditor(c5);
		c5.setEditable(true);
		String meicSql = SysConstant.SQL_Meic;
		egu.getColumn("meicb_id").setComboEditor(egu.gridId,
				new IDropDownModel(meicSql));
		egu.getColumn("meicb_id").editor.setAllowBlank(true);
//		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
//						.getVisit()).getDiancxxb_id(), getTreeid());
//		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符

		// 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// 设置每页显示行数
//		egu.addPaging(25);
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Inserts, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		查系统信息表关于此功能的是否显示
	
		egu.addOtherScript("gridDiv_ds.on('add',function(own,rec,i){for(i=0;i<rec.length;i++){rec[i].set('GUOHSJ',guohrq.getValue().dateFormat('Y-m-d') + ' '+ HOUR.getRawValue()+':'+MIN.getRawValue()+':00');" +
						"rec[i].set('FAHRQ',guohrq.getValue().dateFormat('Y-m-d'));" +
						"rec[i].set('DAOHRQ',guohrq.getValue().dateFormat('Y-m-d'));" +
						"rec[i].set('CAIYRQ',guohrq.getValue().dateFormat('Y-m-d'));}});\n");
			egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){ if(e.field=='MAOZ' || e.field=='PIZ' || e.field=='KOUD'  || e.field=='KOUS' || e.field=='KOUZ'){ var jingz_va=eval(e.record.get('MAOZ')||0)-eval(e.record.get('PIZ')||0)-eval(e.record.get('KOUD')||0)-eval(e.record.get('KOUS')||0)-eval(e.record.get('KOUZ')||0);e.record.set('JINGZ',jingz_va);  }   } );");
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
						+ "row = irow; \n"
						+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
						+ "gongysTree_window.show();}});\n");
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(bob){if(bob.record.get('BULSJ')==0&&bob.column==3){bob.cancel=true;}});\n");
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz_kj,
				"gongysTree", "" + visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() { \n")
				.append(
						"var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
				.append("if(cks==null){gongysTree_window.hide();return;} \n")
				.append(
						"rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("if(cks.getDepth() == 4){ \n")
				.append(
						"rec.set('GONGYSB_ID', cks.parentNode.parentNode.parentNode.text);\n")
				.append(
						"rec.set('MEIKXXB_ID', cks.parentNode.parentNode.text);\n")
				.append(
						"rec.set('YUANMKDW', cks.parentNode.parentNode.text);\n")
				.append(
						"rec.set('FAZ_ID', cks.parentNode.text);rec.set('JIHKJB_ID', cks.text);\n")
				.append("}else if(cks.getDepth() == 3){\n")
				.append(
						"rec.set('GONGYSB_ID', cks.parentNode.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.parentNode.text);\n")
				.append("rec.set('YUANMKDW', cks.parentNode.text);\n").append(
						"rec.set('FAZ_ID', cks.text);\n").append(
						"}else if(cks.getDepth() == 2){\n").append(
						"rec.set('GONGYSB_ID', cks.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.text);\n").append(
						"rec.set('YUANMKDW', cks.text);\n").append(
						"}else if(cks.getDepth() == 1){\n").append(
						"rec.set('GONGYSB_ID', cks.text); }\n").append(
						"gongysTree_window.hide();\n").append("return;")
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public String getTreeScript1() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

//	煤矿下拉框
	public IDropDownBean getKuangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getKuangbModel().getOptionCount()>0) {
				setKuangbValue((IDropDownBean)getKuangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setKuangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getKuangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setKuangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setKuangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void setKuangbModels() {
		String cheh="";
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select distinct m.id,m.mingc from fahb f,meikxxb m where f.meikxxb_id=m.id ");
		sb.append("order by m.id \n");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"请选择"));
		setKuangbModel(new IDropDownModel(list,sb));
	}
	
	// 设置小时下拉框
	public IDropDownBean getHourValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getHourModel().getOptionCount(); i++) {
				Object obj = getHourModel().getOption(i);
				if (DateUtil.getHour(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setHourValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHourValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getHourModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			getHourModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHourModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getHourModels() {
		List listHour = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				listHour.add(new IDropDownBean(i, "0" + i));
			else
				listHour.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setHourModel(new IDropDownModel(listHour));
	}

	// 设置分钟下拉框
	public IDropDownBean getMinValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getMinModel().getOptionCount(); i++) {
				Object obj = getMinModel().getOption(i);
				if (DateUtil.getMinutes(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMinValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setMinValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getMinModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			getMinModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMinModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getMinModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMinModel(new IDropDownModel(listMin));
	}

	// 设置秒下拉框
	public IDropDownBean getSecValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getSecModel().getOptionCount(); i++) {
				Object obj = getSecModel().getOption(i);
				if (DateUtil.getSeconds(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setSecValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setSecValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getSecModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getSecModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setSecModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getSecModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setSecModel(new IDropDownModel(listMin));
	}
//设置文件名
	public IDropDownBean getWenjmSelectValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean7() == null) {
				Object obj = getMinModel().getOption(0);
			    setWenjmSelectValue((IDropDownBean) obj);
				
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean7();
	}

	public void setWenjmSelectValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean7(Value);
	}

	public IPropertySelectionModel getWenjmSelectModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel7() == null) {
			getWenjmSelectModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel7();
	}

	public void setWenjmSelectModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel7(_value);
	}

	public void getWenjmSelectModels() {
	
		setWenjmSelectModel(new IDropDownModel(this.getWenjm()));
	}
//	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel6() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel6();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel6(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
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
	//得到本厂下的所有文件名
private synchronized List getWenjm(){
	Visit visit = (Visit) this.getPage().getVisit();
	File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwj");
	String filenames[];
	if(filepath.isDirectory()){
	 filenames=	filepath.list();
     ArrayList filenamesList=new ArrayList();

     if(filenames.length<=0){
         ArrayList filenamesListerror=new ArrayList();
		  filenamesListerror.add(new IDropDownBean(0,"本厂没有数据"));
		  return filenamesListerror;
	 }
	  for(int i=0;i<filenames.length;i++){
		IDropDownBean filenamebean=new IDropDownBean(i+1,filenames[i]);
		filenamesList.add(filenamebean);
	  }
	  return filenamesList;
	}else{
		WriteLog.writeErrorLog(ErrorMessage.Hengdxg006);
		setMsg("文件导入出现问题");
		  ArrayList filenamesListerror=new ArrayList();
		  filenamesListerror.add(new IDropDownBean(0,"本厂没有数据,或保存路径有误"));
		  return filenamesListerror;
	}
//	File oldfilename = new File(filepath+"\\"+getHengdSelectValue().getValue()) ;
//	boolean success = oldfilename.delete();
}
//备份操作
private boolean datacopy(String wenjm){
	Visit visit=(Visit)this.getPage().getVisit();
	try{
	File file = new File(visit.getXitwjjwz() + "/shul/jianjwj/"+wenjm);
	File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwjbak/"+wenjm.substring(0, 4)+"/"+wenjm.substring(4, 6));
	File bakfile = new File(filepath+"/",wenjm);
	if(bakfile.exists()) {
		bakfile.delete();
	}
	if (!filepath.exists()) {
		filepath.mkdirs();
	}
	if(!file.renameTo(bakfile)) {
		
		return false;
	}
	}catch(Exception e){
		return false;
	}
	return true;
}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiq(DateUtil.FormatDate(new Date()));
			setHourValue(null);
			setHourModel(null);
			setMinValue(null);
			setMinModel(null);
			setSecValue(null);
			setSecModel(null);
			this.setWenjmSelectModel(null);
			this.setWenjmSelectValue(null);
			visit.setDefaultTree(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			getSelectData();
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
	}
	private class Guohxx {

		
		public String _maoz;

		public String _piz;

		public String _sud;

		public String _cheph;

		public Guohxx( String maoz, String piz, String sud, String cheph) {

			_maoz = maoz;
			_piz = piz;
			_sud = sud;
			_cheph = cheph;
		}
	}
}
