package com.zhiren.dc.gdxw.jianj;

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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


/*作者:王总兵
 * 
 *日期:2011-1-8 19:35:46
 *描述:宣威电厂厂内倒短回皮以后发现错误的修改页面
 * 
 */
public class Changnbd_xiug extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String riq; // 保存日期
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
    
	
//	煤矿单位下拉框_开始
	public IDropDownBean getMeikdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getMeikdwModel().getOptionCount() > 0) {
				setMeikdwValue((IDropDownBean) getMeikdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setMeikdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getMeikdwModels() {
		String sql = "select mk.id, mk.mingc from meikxxb mk order by mk.mingc";
		setMeikdwModel(new IDropDownModel(sql, "请选择"));
	}
//	煤矿单位下拉框_结束
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			
		}
		this.getSelectData();
	}
	
	public void save() {

	    Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from changnbdb where id = ").append(delrsl.getString("id")).append(";\n");
		}
	
		
		
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if(mdrsl.next()) {
			
			IDropDownModel idm = new IDropDownModel(SysConstant.SQL_Meic);
			IDropDownModel ysdw = new IDropDownModel(SysConstant.SQL_Yunsdw);
			
			
			String id=mdrsl.getString("id");
			String cheph=mdrsl.getString("cheph");
			String piz=mdrsl.getString("piz");
			long come_meicb_id=idm.getBeanId(mdrsl.getString("come_meicb_id"));
			long go_meicb_id=idm.getBeanId(mdrsl.getString("go_meicb_id"));
			long yunsdwb_id=ysdw.getBeanId(mdrsl.getString("yunsdwb_id"));
			String pinz=mdrsl.getString("pinz");
			String meigy=mdrsl.getString("meigy");
			
			
			sbsql.append("update changnbdb set cheph='"+cheph+"',piz="+piz+",come_meicb_id="+come_meicb_id+",go_meicb_id="+go_meicb_id+"," +
					"yunsdwb_id="+yunsdwb_id+",pinz='"+pinz+"',meigy='"+meigy+"',beiz=to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||'"+visit.getRenymc()+"修改数据' where id="+id+";\n");
		}

		
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		delrsl.close();
		mdrsl.close();
		con.Close();
		this.setMsg("恭喜你修改成功!");
	
	}
	
	public void getSelectData() {
		

		Visit visit = (Visit) getPage().getVisit();
		String tiaoj="";
		String meikmc=this.getMeikdwValue().getValue();
		//System.out.println(meikmc);
		if(meikmc.equals("请选择")){
			tiaoj="";
		}else{
			tiaoj= "and c.meikdwmc='"+meikmc+"'";
		}
			
		JDBCcon con = new JDBCcon();
		String sql =
			"select rownum as xuh,a.* from (\n" +
			"select bd.id,bd.cheph,bd.maoz,bd.piz ,(bd.maoz-bd.piz) as jingz, meigy,mc1.mingc as\n" + 
			" come_meicb_id,mc2.mingc as go_meicb_id,\n" + 
			"to_char(bd.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,\n" + 
			"to_char(bd.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,\n" + 
			"ys.mingc as yunsdwb_id,bd.pinz,bd.zhongcjjy,bd.qingcjjy\n" + 
			"from changnbdb bd,meicb mc1,meicb mc2,yunsdwb ys\n" + 
			"where bd.come_meicb_id=mc1.id(+)\n" + 
			"and bd.go_meicb_id=mc2.id(+)\n" + 
			"and bd.yunsdwb_id=ys.id(+)\n" + 
			"and bd.qingcsj is not null\n" + 
			"and bd.daohrq=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"order by bd.qingcsj )a order by xuh desc";


		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		设置该grid不进行分页
		egu.addPaging(0);
		
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setEditor(null);
		egu.getColumn("xuh").setWidth(50);
		
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("cheph").setWidth(80);
		
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("maoz").setWidth(60);
		
		egu.getColumn("piz").setHeader("皮重");
		//egu.getColumn("piz").setEditor(null);
		egu.getColumn("piz").setWidth(60);
		
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(60);
		
		egu.getColumn("meigy").setHeader("煤管员");
		egu.getColumn("meigy").setWidth(60);
		
		egu.getColumn("come_meicb_id").setHeader("煤炭来源");
		egu.getColumn("come_meicb_id").setWidth(100);
		
		egu.getColumn("go_meicb_id").setHeader("卸煤地点");
		egu.getColumn("go_meicb_id").setWidth(100);
		
		egu.getColumn("zhongcsj").setHeader("重衡时间");
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").setWidth(120);
		
		egu.getColumn("qingcsj").setHeader("轻车时间");
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("qingcsj").setWidth(120);
		
		
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setWidth(100);
		
		
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(60);
		
		egu.getColumn("zhongcjjy").setHeader("重衡检斤员");
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongcjjy").setWidth(80);
		
		egu.getColumn("qingcjjy").setHeader("轻衡检斤员");
		egu.getColumn("qingcjjy").setEditor(null);
		egu.getColumn("qingcjjy").setWidth(80);
		
		
	
//      煤炭来源
		ComboBox cmk= new ComboBox();
		egu.getColumn("come_meicb_id").setEditor(cmk);
		cmk.setEditable(true);
		String mkSql="select id,mingc from meicb order by xuh";
		egu.getColumn("come_meicb_id").setComboEditor(egu.gridId, new
		IDropDownModel(mkSql));
		
		
//		卸煤地点
		ComboBox cmc= new ComboBox();
		egu.getColumn("go_meicb_id").setEditor(cmc); 
		cmc.setEditable(true);
		String mcSql="select id,mingc from meicb order by xuh";
		egu.getColumn("go_meicb_id").setComboEditor(egu.gridId, new IDropDownModel(mcSql));
		
		
//      验煤员对应验煤章下拉框
		ComboBox ymz= new ComboBox();
		egu.getColumn("meigy").setEditor(ymz);
		ymz.setEditable(true);
		String ymy = "select id ,zhiw from renyxxb r where r.bum='验煤员' order by r.zhiw";
		egu.getColumn("meigy").setComboEditor(egu.gridId,new IDropDownModel(ymy));

		
//      运输单位下拉框
		ComboBox ysdw= new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(ysdw);
		ysdw.setEditable(true);
		String ysdwStr = "select id,mingc from yunsdwb ";
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,new IDropDownModel(ysdwStr));

//      pinz下拉框
		ComboBox pinz= new ComboBox();
		egu.getColumn("pinz").setEditor(pinz);
		pinz.setEditable(true);
		String pinzStr = "select id,mingc from pinzb  p where  p.leib='煤'order by xuh ";
		egu.getColumn("pinz").setComboEditor(egu.gridId,new IDropDownModel(pinzStr));
		
		
		egu.addTbarText("倒短日期：");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
		df.Binding("Riq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		
	/*	
		egu.addTbarText("煤矿单位：");
		ComboBox comb = new ComboBox();
		comb.setWidth(120);
		comb.setListWidth(150);
		comb.setTransform("Meikdw");
		comb.setId("Meikdw");
		comb.setLazyRender(true);
		comb.setEditable(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Meikdw.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		*/
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		

	
	
		setExtGrid(egu);
		rsl.close();
		con.Close();
	
	}
	
	/**
	 * 如果在页面上取到的值为Null或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setProSelectionModel3(null); // 煤矿单位下拉框
			visit.setDropDownBean3(null);
			
			
		}
		getSelectData();
	}
}