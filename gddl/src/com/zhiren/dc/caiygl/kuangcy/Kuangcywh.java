package com.zhiren.dc.caiygl.kuangcy;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*作者:王总兵
 *日期:2010-4-28 11:12:12
 *描述:矿采样编号自动按钮X加年月日顺序号自动生成 
 */
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
public class Kuangcywh extends BasePage implements PageValidateListener {
	private String msg = "";
	private String CustomSetKey = "Kuangcywh";
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

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		
		
		StringBuffer sbsqlDel = new StringBuffer("begin\n");
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsqlDel.append("delete from kuangcyb where id = ").append(delrsl.getString("id")).append(";\n");
			String IsHaveQnet_ar="select * from kuangcyb where id="+delrsl.getString("id")+" and  (shenhzt=3 or shenhzt=5)";
			if(con.getHasIt(IsHaveQnet_ar)){//化验值已经提交,不允许删除
				this.setMsg("矿采样编号:"+delrsl.getString("huaybh")+" 已经提交,不允许删除!");
				con.Close();
				return;
				
			}
		}
		sbsqlDel.append("end;");
		if(sbsqlDel.length()>15){
			con.getUpdate(sbsqlDel.toString());
		}
		
		delrsl.close();
		
		StringBuffer sbsql = new StringBuffer("begin\n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into kuangcyb(id, meikxxb_id,meikdwmc, cheh,caiysj, huaybh, caiyy,  shenqdw, pizr,ruc_rez,ruc_lif,beiz2) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id")))
				.append(",'"+mdrsl.getString("meikdwmc")+"'")
				.append(",'").append(mdrsl.getString("cheh")).append("'")
				.append(", to_date('").append(mdrsl.getString("caiysj")).append("', 'yyyy-mm-dd'), '")
				.append(mdrsl.getString("huaybh")).append("', '").append(mdrsl.getString("caiyy")).append("', '")
				.append(mdrsl.getString("shenqdw")).append("', '").append(mdrsl.getString("pizr")).append("', ")
				.append(mdrsl.getLong("ruc_rez")).append(", ").append(mdrsl.getDouble("ruc_lif")).append(", '")
				.append(mdrsl.getString("beiz2")).append("');\n");
			} else {
				sbsql.append("update kuangcyb set ")
				.append("meikxxb_id = ").append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id")))
				.append(",meikdwmc='").append(mdrsl.getString("meikdwmc"))
				.append("', caiysj = ").append(" to_date('").append(mdrsl.getString("caiysj")).append("', 'yyyy-mm-dd'),huaybh='")
				.append(mdrsl.getString("huaybh")).append("', caiyy = '").append(mdrsl.getString("caiyy"))
				.append("', shenqdw = '").append(mdrsl.getString("shenqdw")).append("', pizr = '").append(mdrsl.getString("pizr"))
				.append("', ruc_rez = ").append(mdrsl.getLong("ruc_rez"))
				.append(", ruc_lif = ").append(mdrsl.getDouble("ruc_lif")).append(", beiz2 = '").append(mdrsl.getString("beiz2"))
				.append("', cheh = '").append(mdrsl.getString("cheh"))
				.append("' where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		if(sbsql.length()>15){
			con.getUpdate(sbsql.toString());
		}
		
		mdrsl.close();
		con.Close();
		
		
		
		
		
		
		
		
	}
	
	// 日期控件
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

  
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;;
		}
		
		
		getSelectData();
	}

	// 复制昨日数据

	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String sql = 
			"select kc.id,m.mingc as meikxxb_id,kc.meikdwmc,kc.cheh,kc.caiysj,kc.huaybh,kc.caiyy,\n" +
			"kc.shenqdw,kc.pizr,round_new(kc.qnet_ar*1000/4.1816,0) as qnet_ar,kc.stad,kc.ruc_rez,kc.ruc_lif,kc.beiz2\n"+
			"from  kuangcyb kc,meikxxb m\n" + 
			"where kc.meikxxb_id=m.id(+)\n" + 
			"and kc.caiysj=to_date('"+this.getRiqi()+"','yyyy-mm-dd')\n"+
			"order by kc.huaybh";


		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey);
		egu.setTableName("kuangcyb");
		egu.setWidth("bodyWidth");

	
		egu.getColumn("meikxxb_id").setHeader("煤矿(选择)");
        // 煤矿下拉框
		ComboBox meik = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(meik);
		meik.setEditable(true);
		sql = "select id, mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("meikxxb_id").editor.setAllowBlank(true);
		
		egu.getColumn("meikdwmc").setHeader("煤矿(手写)");
		egu.getColumn("caiysj").setHeader("采样时间");
		egu.getColumn("caiysj").setDefaultValue(getRiqi());
		egu.getColumn("cheh").setHeader("车号");
		egu.getColumn("cheh").setWidth(80);
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").editor.setAllowBlank(false);
		egu.getColumn("huaybh").setEditor(null);
		
		egu.getColumn("caiyy").setHeader("采样员");
		ComboBox caiyy = new ComboBox();
		egu.getColumn("caiyy").setEditor(caiyy);
		meik.setEditable(true);
		sql = "select r.id,r.quanc from renyxxb r where r.bum='验煤员' or r.bum='采样员' order by quanc";
		egu.getColumn("caiyy").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("caiyy").setWidth(80);
		
		
		egu.getColumn("pizr").setHeader("批准人");
		ComboBox pizr = new ComboBox();
		egu.getColumn("pizr").setEditor(pizr);
		meik.setEditable(true);
		sql = "select r.id,r.quanc from renyxxb r where r.bum='煤场管理员' order by quanc";
		egu.getColumn("pizr").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("pizr").setWidth(80);
		
		
		egu.getColumn("shenqdw").setHeader("申请单位");
		egu.getColumn("shenqdw").setWidth(100);
		
		

		
		
		egu.getColumn("qnet_ar").setHeader("化验热值");
		egu.getColumn("qnet_ar").setWidth(80);
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("qnet_ar").setHidden(true);
		egu.getColumn("stad").setHeader("化验硫分");
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("stad").setHidden(true);
		egu.getColumn("ruc_rez").setHeader("预测热值(大卡)");
		egu.getColumn("ruc_rez").setWidth(90);
		egu.getColumn("ruc_lif").setHeader("入厂硫分");
		egu.getColumn("ruc_lif").setWidth(80);
		egu.getColumn("ruc_lif").setHidden(true);
		egu.getColumn("beiz2").setHeader("备注");
		egu.getColumn("beiz2").setWidth(120);
		
		
		

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

        // 日期
		egu.addTbarText("采样时间:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		
		// 电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		boolean KuangcyBm = MainGlobal.getXitxx_item("采样", "矿采样编码是否以字母C+年份+月份+三位顺序号作为编码规则", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"是").equals("是");
		String sPwHandler="";
		 String newBianh="";
		 if(KuangcyBm){//阳城电厂编码
			  newBianh=GetBianh();
				 sPwHandler = "function(){"
					+"var zhi3;"
					+"if (gridDiv_ds.getCount()!=0) {"
					+"	 var h1=gridDiv_ds.getAt(gridDiv_ds.getCount()-1);"
					+"	 var zhi=h1.get('HUAYBH').substring(2,h1.get('HUAYBH').length);"
					+"   var zhi2=parseInt(zhi)+1;"
					+"   zhi3='XK'+zhi2;"
					+"}else{"
					+"   zhi3='"+newBianh+"';      "
					+"        "
					+"             "
					+"}"
					+"  var plant = new gridDiv_plant({ID: '0',MEIKXXB_ID: '',MEIKDWMC:'',CHEH:'',CAIYSJ: '"+this.getRiqi()+"',HUAYBH: '',CAIYY: '',SHENQDW: '',PIZR: '',QNET_AR: '',STAD: '',RUC_REZ: '',RUC_LIF: '',BEIZ2: ''});"
					+"  gridDiv_ds.insert(gridDiv_ds.getCount(),plant);"
					+"	gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('HUAYBH',zhi3);"
					+"	 "
					
				
					+"}";
		 }else{//宣威发电编码
			 
			      newBianh=GetBianh_new();
				 sPwHandler = "function(){"
					+"var zhi3;"
					+"if (gridDiv_ds.getCount()!=0) {"
					+"	 var h1=gridDiv_ds.getAt(gridDiv_ds.getCount()-1);"
					+"	 var zhi=h1.get('HUAYBH').substring(1,h1.get('HUAYBH').length);"
					+"   if(zhi.substring(0,1)=='0'){"
					+"       var zhi2=parseInt(zhi.substring(1,zhi.length))+1;   "
					+"         zhi3='X0'+zhi2;   "
					+"    }else{       "
					+"       var zhi2=parseInt(zhi)+1;  "
					+"       zhi3='X'+zhi2;"
					+"     }"
					+"}else{"
					+"   zhi3='"+newBianh+"';      "
					+"        "
					+"             "
					+"}"
					+"  var plant = new gridDiv_plant({ID: '0',MEIKXXB_ID: '',MEIKDWMC:'',CHEH:'',CAIYSJ: '"+this.getRiqi()+"',HUAYBH: '',CAIYY: '',SHENQDW: '',PIZR: '',QNET_AR: '',STAD: '',RUC_REZ: '',RUC_LIF: '',BEIZ2: ''});"
					+"  gridDiv_ds.insert(gridDiv_ds.getCount(),plant);"
					+"	gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('HUAYBH',zhi3);"
					+"	 "
					
				
					+"}";
			 
		 }
       
		egu.addTbarBtn(new GridButton("添加",sPwHandler));
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton");
		setExtGrid(egu);
		con.Close();
	}
	
	//得到当月最大的编号(阳城电厂用)
	   private String GetBianh() {
	    	JDBCcon con = new JDBCcon();
	    	ResultSetList rsl = null;
	    	String bianh = "";
	    	String newBianh="";
	    	String sql = "select max(huaybh) as bianh\n" +
	    		"  from kuangcyb k\n" + 
	    		" where k.caiysj> = First_day(to_date('"+this.getRiqi()+"','yyyy-mm-dd'))\n" + 
	    		" and k.caiysj<=last_day(to_date('"+this.getRiqi()+"','yyyy-mm-dd'))";

	    	rsl = con.getResultSetList(sql);
	    	if (rsl.next()) {
	    		if (rsl.getString("bianh") == null || rsl.getString("bianh").equals("")) {
	    			newBianh = "XK"+getRiqi().substring(0, 4) + getRiqi().substring(5, 7) + "001";
	    		} else {
	    			bianh = Long.parseLong(rsl.getString("bianh").substring(2,rsl.getString("bianh").length())) + 1 + "";
	    			newBianh="XK"+bianh;
	    		}
	    	}
	    	
	    	rsl.close();
	    	con.Close();
	    	return newBianh;
	    }
	   
		//得到当月最大的编号 X080120，字母X+月份+3位顺序号(宣威电厂用)
	   private String GetBianh_new() {
	    	JDBCcon con = new JDBCcon();
	    	ResultSetList rsl = null;
	    	String bianh = "";
	    	String newBianh="";
	    	String sql = "select max(huaybh) as bianh\n" +
    		"  from kuangcyb k\n" + 
    		" where k.caiysj> = First_day(to_date('"+this.getRiqi()+"','yyyy-mm-dd'))\n" + 
    		" and k.caiysj<=last_day(to_date('"+this.getRiqi()+"','yyyy-mm-dd'))";


	    	rsl = con.getResultSetList(sql);
	    	if (rsl.next()) {
	    		if (rsl.getString("bianh") == null || rsl.getString("bianh").equals("")) {
	    			newBianh = 'X'+ getRiqi().substring(5, 7) + "001";
	    		} else {
	    			bianh = Long.parseLong(rsl.getString("bianh").substring(1,rsl.getString("bianh").length())) + 1 + "";
	    			if(bianh.length()==4){//1到9月加0
	    				newBianh="X0"+bianh;
	    			}else{
	    				newBianh="X"+bianh;
	    			}
	    			
	    			
	    			
	    		}
	    	}
	    	
	    	rsl.close();
	    	con.Close();
	    	return newBianh;
	    }
	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getRiqi();
		cnDate = cnDate.substring(0, 4) + "年" + cnDate.substring(5, 7) + "月" + cnDate.substring(8, 10);
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CopyButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
			
		}
		getSelectData();
	}

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

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
}
