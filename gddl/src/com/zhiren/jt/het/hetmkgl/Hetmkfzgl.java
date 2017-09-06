package com.zhiren.jt.het.hetmkgl;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;

public class Hetmkfzgl extends BasePage implements PageValidateListener {
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	protected void initialize() {
		msg = "";
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

//		2009-09-22 zsj改，
		JDBCcon con = new JDBCcon();
		ResultSetList drsl =getExtGrid().getDeleteResultSet(getChange());
		String TableName="hetmkfzglb";
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql_Insert = new StringBuffer("");	//insert
		StringBuffer sql_Update = new StringBuffer("");	//Update
		while (drsl.next()) {
			//删除
			sql.append("delete from " ).append(TableName).append(" where id=")
				.append(drsl.getString("ID")).append(";\n");
			
//			级联操作，删除
//			sql.append(Zicjlcz("D",getTreeid(),TableName,drsl.getString("ID")));
		}
		drsl.close();
//		添加、修改
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			
			if ("0".equals(mdrsl.getString("ID"))) {
				
				sql_Insert.append("insert into ").append(TableName)
					.append("(id, hetb_id, meikfzb_id, diancxxb_id, qisrq, jiezrq,zhuangt,beiz")
				    .append(") values(").append("getnewid(").append(visit.getDiancxxb_id()).append(")")
					.append(",").append(getExtGrid().getValueSql(getExtGrid().getColumn("HETBID"),mdrsl.getString("HETBID"))).append(",")
					.append(getExtGrid().getValueSql(getExtGrid().getColumn("MEIKFZB_ID"),mdrsl.getString("MEIKFZB_ID"))).append(",")
					.append(getExtGrid().getValueSql(getExtGrid().getColumn("DIANCXXB_ID"),mdrsl.getString("DIANCXXB_ID"))).append(",")
					.append("to_date('").append(mdrsl.getString("QISRQ")).append("','yyyy-MM-dd')").append(",")
					.append("to_date('").append(mdrsl.getString("JIEZRQ")).append("','yyyy-MM-dd')").append(",")
					.append(getExtGrid().getValueSql(getExtGrid().getColumn("ZHUANGT"),mdrsl.getString("ZHUANGT"))).append(",'")
					.append(mdrsl.getString("BEIZ")).append("');\n");
				
//				级联操作，插入
//				sql_Insert.append(Zicjlcz("I",getTreeid(),TableName,sql_Insert.toString()));
				
			} else {
				sql_Update.append(" update hetmkfzglb ").append("set").append(" QISRQ=to_date('").append(mdrsl.getString("QISRQ")).append("','yyyy-MM-dd')")
						.append(",JIEZRQ=to_date('").append(mdrsl.getString("JIEZRQ")).append("','yyyy-MM-dd')")
						.append(", MEIKFZB_ID=").append(getExtGrid().getValueSql(getExtGrid().getColumn("MEIKFZB_ID"),mdrsl.getString("MEIKFZB_ID")))
						.append(", DIANCXXB_ID = ").append(getExtGrid().getValueSql(getExtGrid().getColumn("DIANCXXB_ID"),mdrsl.getString("DIANCXXB_ID")))
						.append(", zhuangt = ").append(getExtGrid().getValueSql(getExtGrid().getColumn("ZHUANGT"),mdrsl.getString("ZHUANGT")))
						.append(",xiafzt=0,beiz = '").append(mdrsl.getString("BEIZ"))
						.append("' where id=").append(mdrsl.getString("ID")).append(";\n");
				
			}
		}
		mdrsl.close();
		sql.append(sql_Insert);
		sql.append(sql_Update);
		sql.append("end;");
		if(sql.length()>13){
			
			if(con.getUpdate(sql.toString())>=0){
				
				setMsg("保存成功！");
			}else{
				
				setMsg("保存失败！");
			}
		}
		con.Close();
	
	}
	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getBeginRiq() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date()));
		}
		return riqi;
	}

	public void setBeginRiq(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getEndRiq() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setEndRiq(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _ZengjfzChick = false;

	public void ZengjfzButton(IRequestCycle cycle) {
		_ZengjfzChick = true;
	}
	
	private boolean _XiafChick = false;

	public void XiafButton(IRequestCycle cycle) {
		_XiafChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_XiafChick) {
			_XiafChick = false;
			Xiaf();
//			getSelectData();
		}
		if (_ZengjfzChick) {
			_ZengjfzChick = false;
			GotoZengjfz(cycle);	
		}
	}

	 private void Xiaf(){//增加，如果是已经下发过的要更新主合同、增加删除分合同，这样避免在建立约束的情况下，无法用删增方法进行修改合同。
//	    	Visit visit = (Visit) getPage().getVisit();
			InterCom_dt xiaf=new InterCom_dt();
			ResultSetList result=getExtGrid().getModifyResultSet(getChange());

			JDBCcon con=new JDBCcon();
			try{
			while(result.next()){//下发每一个合同
				String fanga_id=result.getString("id");
				String diancxxb_id="";
				String[] resul=null; 
				String sql="select h.id as fanga_id,to_char(h.qisrq,'yyyy-mm-dd') as qisrq, to_char(h.jiezrq,'yyyy-mm-dd') as jiezrq,h.diancxxb_id, m.bianm as meikxxb_id, h.zhuangt from hetmkfzglb h, meikfzmxb mz, meikxxb m \n"+
                      " where h.meikfzb_id=mz.meikfzb_id and mz.meikxxb_id=m.id and h.id="+fanga_id;
				ResultSetList rs1=con.getResultSetList(sql);
	            int len= rs1.getResultSetlist().size()+1;
				String[] sqls=new String[len];
				String[] diancxxb_ids=new String[20];//
				int kk=0;
				int j=0;
				sqls[j]="delete from jinmsqpfb where fanga_id="+fanga_id;
				j++;
//				boolean flag=false;
				while(rs1.next()){
					if(!diancxxb_id.equals(rs1.getString("DIANCXXB_ID"))){
						diancxxb_ids[kk++]=rs1.getString("DIANCXXB_ID");
						diancxxb_id=rs1.getString("DIANCXXB_ID");
					}
					sqls[j]="insert into jinmsqpfb(ID,fanga_id,qisrq,jiezrq,meikxxb_id,zhuangt\n" +
					")values( getnewid("+diancxxb_id+"),\n" + 
					 rs1.getString("fanga_id")+",to_date('"+
					 rs1.getString("qisrq")+"','yyyy-mm-dd'),to_date('"+
					 rs1.getString("jiezrq")+"','yyyy-mm-dd'),(select id from meikxxb where shangjgsbm="+
					 rs1.getString("meikxxb_id")+"),"+
					 rs1.getString("zhuangt")+
					")";
					j++;
				}
				
				for (int k=0;k<kk;k++){
					resul=xiaf.sqlExe(diancxxb_ids[0], sqls, true);//这里做文章
					if(!resul[0].equals("true")){
						setMsg("下发失败！");
						return;
					}
				}
				con.getUpdate("update hetmkfzglb set xiafzt=1 where id="+fanga_id);
		    }
			
			}finally{
				con.Close();
				setMsg("下发成功！");
			}
			getSelectData();
		}
	
	private void GotoZengjfz(IRequestCycle cycle) {
		((Visit) getPage().getVisit()).setLong10(getGongysValue().getId());
     	cycle.activate("Meikfz");
	}
	
	public void getSelectData() {
//		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "select decode(h.id,null,0,h.id) as id,\n" +
		"       ht.id as hetbid,\n" + 
		"       g.id as gongysb_id,\n"+
		"       ht.hetbh as hetb_id,\n" + 
		"       decode(h.qisrq,null,ht.qisrq,h.qisrq) as qisrq,\n" + 
		"       decode(h.jiezrq,null,ht.guoqrq,h.jiezrq) as jiezrq,\n" + 
		"       m.mingc as meikfzb_id,\n" + 
		"       d.mingc as diancxxb_id,\n" + 
		"       decode(h.xiafzt,1,'已下发','未下发') as xiafzt,\n"+
	    "       decode(h.zhuangt,0,'未启用','启用') as zhuangt,\n"+
		"       h.beiz\n" + 
		"  from hetmkfzglb h, meikfzb m, hetb ht, diancxxb d,gongysb g\n" + 
		" where h.meikfzb_id = m.id(+)\n" + 
		"    and h.hetb_id(+) = ht.id\n" + 
		"   and h.diancxxb_id = d.id(+)\n" + 
		"   and ht.gongysb_id=g.id \n"+
		"   and ht.qisrq>=to_date('"+this.getBeginRiq()+"','yyyy-mm-dd') \n " +
		"   and ht.guoqrq<=to_date('"+this.getEndRiq()+"','yyyy-mm-dd') \n " +
		"   and g.id="+getGongysValue().getId()+"\n"+
		"   order by hetb_id";



	      ResultSetList rsl = con.getResultSetList(str);

	      
	      ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
	      
	      egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		  egu.getGridColumns().add(1, new GridColumn(GridColumn.ColType_Check));
	      egu.getColumn("id").setHeader("id");
	      egu.getColumn("id").setHidden(true);
	      egu.getColumn("hetbid").setHidden(true);
	      egu.getColumn("hetbid").setHeader("合同表id");
	      egu.getColumn("gongysb_id").setHidden(true);
	      egu.getColumn("gongysb_id").setHeader("供应商表id");
	      egu.getColumn("hetb_id").setHeader("合同编号");
	      egu.getColumn("hetb_id").setEditor(null);
	      egu.getColumn("hetb_id").setWidth(150);
	      egu.getColumn("qisrq").setHeader("起始日期");
	      egu.getColumn("jiezrq").setHeader("截止日期");
	      egu.getColumn("meikfzb_id").setHeader("煤矿分组");
	      egu.getColumn("diancxxb_id").setHeader("电厂名称");
	      egu.getColumn("xiafzt").setHeader("下发状态");
	      egu.getColumn("xiafzt").setEditor(null);
	      egu.getColumn("zhuangt").setHeader("是否启用");
	      egu.getColumn("beiz").setHeader("备注");

	      egu.addTbarText("合同起始日期:");
			DateField dStart = new DateField();
			dStart.Binding("RIQ1","forms[0]");
			dStart.setValue(getBeginRiq());
			egu.addToolbarItem(dStart.getScript());
			
			egu.addTbarText("至");
			
			DateField dEnd = new DateField();
			dEnd.Binding("RIQ2","forms[0]");
			dEnd.setValue(getEndRiq());
			egu.addToolbarItem(dEnd.getScript());
			egu.addTbarText("-");
			
			egu.addTbarText(Locale.gongysb_id_fahb);
			ComboBox comb4 = new ComboBox();
			comb4.setTransform("GongysDropDown");
			comb4.setId("Gongys");
			comb4.setEditable(true);
			comb4.setLazyRender(true);// 动态绑定
			comb4.setWidth(130);
			comb4.setListWidth(200);
//			comb4.setReadOnly(true);
			egu.addToolbarItem(comb4.getScript());
			egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");
			
			String sql ="select id,mingc from meikfzb order by mingc ";
			egu.getColumn("meikfzb_id").setEditor(new ComboBox());
			egu.getColumn("meikfzb_id").setComboEditor(egu.gridId,
					new IDropDownModel(sql));
			egu.getColumn("meikfzb_id").setReturnId(true);
//			egu.getColumn("zhi").setWidth(150);
			
			String dcsql ="select id,mingc from diancxxb where jib=3 order by mingc ";
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel(dcsql));
			egu.getColumn("diancxxb_id").setReturnId(true);
			
			List l2 = new ArrayList();
			l2.add(new IDropDownBean(1, "启用"));
			l2.add(new IDropDownBean(0, "未启用"));
			egu.getColumn("zhuangt").setEditor(new ComboBox());
			egu.getColumn("zhuangt").setComboEditor(egu.gridId,
					new IDropDownModel(l2));
			egu.getColumn("zhuangt").setReturnId(true);
			egu.getColumn("zhuangt").setDefaultValue(String.valueOf(((IDropDownBean)l2.get(0)).getValue()));
			
			
			egu.getColumn("diancxxb_id").setWidth(150);
			
			egu.addToolbarButton("刷新",GridButton.ButtonType_Refresh,"RefreshButton");
//			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton("保存",GridButton.ButtonType_Save, "SaveButton");
			String str2="document.getElementById('ZengjfzButton').click(); \n";
	        egu.addToolbarItem("{"+new GridButton("增加煤矿分组","function(){"+str2+"}").getScript()+"}");
	        String str1=
				"   var rec = gridDiv_sm.getSelected(); \n"
				+"  if(rec!=null){\n"
		        + " if(rec.get('ID')==0){ \n"
			    + " 	Ext.MessageBox.alert('提示信息','在下发之前请先保存!'); \n"
			    + "  	return;"
		        +"   }"
		        +"  }else{\n"
		        +"  Ext.MessageBox.alert('提示信息','请选中一条数据进行下发!'); \n"
		        +"  return;"
		        +"  }"
		      ;
	        
//	        egu.addToolbarButton("下发",GridButton.ButtonType_SubmitSel, "xiafButton");
//	        egu.addToolbarItem("{"+new GridButton("下发","function(){"+str1+"}").getScript()+"}");
	        egu.addToolbarButton("下发",GridButton.ButtonType_SubmitSel_condition,"XiafButton",str1);
	        egu.setGridType(ExtGridUtil.Gridstyle_Edit);
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
	
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String  sql ="select gys.id,gys.piny||' '||gys.mingc from gongysdcglb glb,diancxxb dc,gongysb gys,hetb h\n" +
			"where glb.diancxxb_id=dc.id and glb.gongysb_id=gys.id and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" \n"+
			" and gys.leix=1 and h.gongysb_id=gys.id and h.qisrq>=to_date('"+this.getBeginRiq()+"','yyyy-mm-dd') \n " +
			" and h.guoqrq<=to_date('"+this.getEndRiq()+"','yyyy-mm-dd') and h.liucztb_id=1 and h.leib=2 order by gys.mingc";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
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
			setGongysModel(null);
		    setGongysValue(null);
		    getGongysModels();
		    visit.setLong10(0);
		    riqi = null;
			riqichange = false;
			riq2 = null;
			riq2change = false;
			getSelectData();
		}
		if(riqichange || riq2change){
			riqichange = false;
			riq2change = false;
			setGongysModel(null);
		    setGongysValue(null);
		    getGongysModels();
			getSelectData();
		}
	}
}
