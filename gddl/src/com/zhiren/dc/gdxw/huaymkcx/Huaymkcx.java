package com.zhiren.dc.gdxw.huaymkcx;

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
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2010-1-1
 * 内容:增加  化验值录入  煤矿查询
 */
public class Huaymkcx extends BasePage implements PageValidateListener {

	private String CustomSetKey = "Huaymkcx";
	
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


	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getKuangm() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setKuangm(String kuangm) {
		((Visit) this.getPage().getVisit()).setString1(kuangm);
	}

	public String getBianh() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setBianh(String bianh) {
		((Visit) this.getPage().getVisit()).setString2(bianh);
	}

	public String getShul() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setShul(String shul) {
		((Visit) this.getPage().getVisit()).setString3(shul);
	}

	public String getHuaysj() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setHuaysj(String huaysj) {
		((Visit) this.getPage().getVisit()).setString4(huaysj);
	}

	// 从数字合理范围表里取出 Mt, Mad，Aad,Vad,Std,Qbad的范围
	public ResultSetList getmaxmin(JDBCcon con, long diancxxb_id) {
		ResultSetList shuzhirsl;
		String sql = "select shangx,xiax,mingc from shuzhlfwb "
				+ "where leib = '质量' and beiz='化验录入'";
		shuzhirsl = con.getResultSetList(sql);
		return shuzhirsl;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		// 氢是否可编辑
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// 硫是否可编辑
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// 氢元素符号
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// 硫元素符号
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}
		// 数值合理范围
		ResultSetList szhlfw = getmaxmin(con, visit.getDiancxxb_id());
		// 转码类别中对应化验编码的ID号
		String zhuanmlbid = "";
		sql = "select id from zhuanmlb where mingc = '化验编码'";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			zhuanmlbid = rsl.getString("id");
		}
		rsl.close();
		if ("".equals(zhuanmlbid)) {
			WriteLog.writeErrorLog(ErrorMessage.ZhuanmlbNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.ZhuanmlbNotFound);
			return;
		}
		
//		电厂Tree刷新条件
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
			try {
				ResultSet rsss=con.getResultSet("select id from diancxxb where fuid="+getTreeid());
				if(rsss.next()){
					str = "and dc.fuid="+ getTreeid() ;
				}else{
					str = "and dc.id = " + getTreeid() ;
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			
		}
		
		//是否显示采样时间
		String riqTiaoj = this.getRiq();
		if (riqTiaoj == null || "".equals(riqTiaoj)) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		boolean isShowRq = false;
		String strTemp ="";
		isShowRq="是".equals(MainGlobal.getXitxx_item("化验", "化验录入显示采样日期", visit.getDiancxxb_id()+"", "否"));
		if (isShowRq) {
			strTemp=" AND (SELECT caiyrq FROM caiyb WHERE zhilb_id=l.zhilb_id)=to_date('" + riqTiaoj + "','yyyy-mm-dd')\n";
		} 
		
		String zmid=" ";
		if(this.getChange()!=null && !this.getChange().equals("") && !this.getChange().equals("-1")){
			zmid=" and z.id="+this.getChange()+" ";
		}

		// 查询数据SQL
		//说明:宣威发电的化验编号列表需要能够根据制样日期单独刷新出来,所以在这里sql单独写.其它电厂不适用
		if(visit.getDiancmc().equals("宣威发电")){
			sql = 
				"select l.id,\n" +
				"       to_char(z.bianm) as huaybh,\n" + 
				"       ( select mingc from meikxxb where id=fh.meikxxb_id) meik,\n"+
				"       nvl(l.huaysj,sysdate) huaysj,\n" + 
				"       l.mt,\n" + 
				"       l.mad,\n" + 
				"       l.aad,\n" + 
				"       l.vad,\n" + 
				"       l." + sign_S + "," + 
				"       l." + sign_H + ",\n" + 
				"       l.qbad,\n" + 
				"       l.t1,\n" + 
				"       l.t2,\n" + 
				"       l.t3,\n" + 
				"       l.t4,\n" + 
				"       huayy,\n" + 
				"       l.lury,\n" + 
				"       l.beiz,\n" + 
				"       l.huaylb\n" + 
				"from zhuanmb z, zhillsb l,\n" + 
				"       (select distinct f.zhilb_id,f.meikxxb_id \n" + 
				"        from fahb f,diancxxb dc\n" + 
				"        where f.diancxxb_id = dc.id\n" +
				               str+ ") fh ,zhiyryb zy\n" + 
				"where z.zhillsb_id = l.id and zhuanmlb_id = " + zhuanmlbid + zmid+"\n" + 
				"      and l.zhilb_id = fh.zhilb_id\n" + 
				//"      and (l.shenhzt = 0 or l.shenhzt = 2)\n" + 
				"      and l.zhilb_id=zy.zhilb_id\n" + 
				"      and zy.zhiyrq=to_date('"+this.getZhiyrq()+"','yyyy-mm-dd')\n" + 
				strTemp +
				"order by l.id, l.huaylb";
		}else{
			sql = 
				"select l.id,\n" +
				"       to_char(z.bianm) as huaybh,\n" + 
				"       nvl(l.huaysj,sysdate) huaysj,\n" + 
				"       l.mt,\n" + 
				"       l.mad,\n" + 
				"       l.aad,\n" + 
				"       l.vad,\n" + 
				"       l." + sign_S + "," + 
				"       l." + sign_H + ",\n" + 
				"       l.qbad,\n" + 
				"       l.t1,\n" + 
				"       l.t2,\n" + 
				"       l.t3,\n" + 
				"       l.t4,\n" + 
				"       huayy,\n" + 
				"       l.lury,\n" + 
				"       l.beiz,\n" + 
				"       l.huaylb\n" + 
				"from zhuanmb z, zhillsb l,\n" + 
				"       (select distinct f.zhilb_id\n" + 
				"        from fahb f,diancxxb dc\n" + 
				"        where f.diancxxb_id = dc.id\n" +
				               str+ ") fh\n" + 
				"where z.zhillsb_id = l.id and zhuanmlb_id = " + zhuanmlbid + "\n" + 
				"      and l.zhilb_id = fh.zhilb_id\n" + 
				"      and (l.shenhzt = 0 or l.shenhzt = 2)\n" + 
				strTemp +
				"order by l.id, l.huaylb";
		}
		


		rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, CustomSetKey);
		// // 
		
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		
		// //设置表名称用于保存
		egu.setTableName("zhillsb");
		// 设置页面宽度以便增加滚动条
		egu.setWidth("bodyWidth");
		// /设置显示列名称
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("meik").setHeader("煤矿");
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("mt").setHeader("全水分Mt(%)");
		egu.getColumn("mad").setHeader("水分Mad(%)");
		egu.getColumn("aad").setHeader("灰分Aad(%)");
		egu.getColumn("vad").setHeader("挥发分Vad(%)");
		egu.getColumn("qbad").setHeader("弹筒热量Qbad(Mj/kg)");
		egu.getColumn(sign_S).setHeader("硫分" + sign_S + "(%)");
		egu.getColumn(sign_H).setHeader("氢" + sign_H + "(%)");

		egu.getColumn("t1").setHeader("T1(℃)");
		egu.getColumn("t2").setHeader("T2(℃)");
		egu.getColumn("t3").setHeader("T3(℃)");
		egu.getColumn("t4").setHeader("T4(℃)");
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("lury").setHeader("化验录入员");
		egu.getColumn("beiz").setHeader("化验备注");
		egu.getColumn("huaylb").setHeader("化验类别");
		egu.getColumn("huaylb").setEditor(null);
		
		egu.getColumn("huaysj").setColtype(GridColumn.ColType_default);
		egu.getColumn("huaysj").editor.setAllowBlank(false);
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("mt").editor.setMaxValue("100");
		egu.getColumn("mt").editor.setMinValue("1");
		egu.getColumn("mt").setColtype(GridColumn.ColType_default);
		egu.getColumn("mt").editor.setAllowBlank(false);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("mad").editor.setMaxValue("100");
		egu.getColumn("mad").editor.setMinValue("0");
		egu.getColumn("mad").setColtype(GridColumn.ColType_default);
		egu.getColumn("mad").editor.setAllowBlank(false);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("aad").editor.setMaxValue("100");
		egu.getColumn("aad").editor.setMinValue("1");
		egu.getColumn("aad").setColtype(GridColumn.ColType_default);
		egu.getColumn("aad").editor.setAllowBlank(false);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("vad").editor.setMaxValue("100");
		egu.getColumn("vad").editor.setMinValue("1");
		egu.getColumn("vad").setColtype(GridColumn.ColType_default);
		egu.getColumn("vad").editor.setAllowBlank(false);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("qbad").editor.setMaxValue("100");
		egu.getColumn("qbad").editor.setMinValue("1");
		egu.getColumn("qbad").setColtype(GridColumn.ColType_default);
		egu.getColumn("qbad").editor.setAllowBlank(false);
		egu.getColumn(sign_S).setWidth(80);
		egu.getColumn(sign_S).editor.setMaxValue("100");
		egu.getColumn(sign_S).editor.setMinValue("0");
		egu.getColumn(sign_S).setColtype(GridColumn.ColType_default);
		egu.getColumn(sign_S).setHidden(!editable_S);
		if (editable_S) {
			egu.getColumn(sign_S).editor.setAllowBlank(false);
		}
		egu.getColumn(sign_H).setWidth(80);
		egu.getColumn(sign_H).editor.setMaxValue("100");
		egu.getColumn(sign_H).editor.setMinValue("0");
		egu.getColumn(sign_H).setColtype(GridColumn.ColType_default);
		egu.getColumn(sign_H).setHidden(!editable_H);
		if (editable_H) {
			egu.getColumn(sign_H).editor.setAllowBlank(false);
		}
		// 用用户设置的范围覆盖程序默认
		while (szhlfw.next()) {
			GridColumn gc = egu.getColumn(szhlfw.getString("mingc"));
			if (gc != null) {
				gc.editor.setMaxValue(szhlfw.getString("shangx"));
				gc.editor.setMinValue(szhlfw.getString("xiax"));
			}
		}
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t1").setDefaultValue("20");
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t2").setDefaultValue("20");
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t3").setDefaultValue("20");
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("t4").setDefaultValue("20");
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setWidth(80);
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(22);
		// Toolbar tb1 = new Toolbar("tbdiv");
		// egu.setGridSelModel(3);
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		
		//是否显示采样日期
		if (isShowRq) {
			egu.addTbarText("采样日期:");
			DateField df = new DateField();
			df.setValue(this.getRiq());
			df.Binding("Caiyrq", "");// 与html页中的id绑定,并自动刷新
			egu.addToolbarItem(df.getScript());
			egu.addTbarText("-");
		}
		if(visit.getDiancmc().equals("宣威发电")){
			egu.addTbarText("制样日期:");
			DateField zhiyrq = new DateField();
			zhiyrq.Binding("Zhiyrq", "forms[0]");
			zhiyrq.setValue(this.getZhiyrq());
			egu.addToolbarItem(zhiyrq.getScript());
			egu.addTbarText("-");
		}
		
		

		egu.addTbarText("化验编号:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("MeikmcDropDown");
		comb2.setId("gongys");
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(130);
		comb2.setEditable(true);
	
		
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");
		
		
		
		
		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);

//		验证是否隐藏 化验类别
		String sqltmp="select * from xitxxb where mingc='化验值录入显示样品类别' and zhi='隐藏'";
		ResultSetList rsl2 = con.getResultSetList(sqltmp);
		if(rsl2.next()){
			egu.getColumn("huaylb").setHidden(true);
		}
		
		egu.addOtherScript(" gongys.on('change',function(field,newValue,oldValue){document.all.CHANGE.value=newValue;}); ");
		
		if(this.getChange()==null|| this.getChange().equals("") ){
			egu.addOtherScript("gongys.setValue('-1');");
		}else{
			egu.addOtherScript("gongys.setValue('"+this.getChange()+"');");
		}
		
		
	
		setExtGrid(egu);
		rsl2.close();
		con.Close();
	}

	public String getfunction(String binder) {
		String handler = "function(){\n"
				+ "var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n"
				+ "for(i = 0; i< Mrcd.length; i++){\n"
				+ "gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HUAYBH update=\"true\">' + Mrcd[i].get('HUAYBH').replace('<','&lt;').replace('>','&gt;')+ '</HUAYBH>'+ '<HUAYSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('HUAYSJ'))?Mrcd[i].get('HUAYSJ'):Mrcd[i].get('HUAYSJ').dateFormat('Y-m-d'))+ '</HUAYSJ>'+ '<MT update=\"true\">' + Mrcd[i].get('MT')+ '</MT>'+ '<MAD update=\"true\">' + Mrcd[i].get('MAD')+ '</MAD>'+ '<AAD update=\"true\">' + Mrcd[i].get('AAD')+ '</AAD>'+ '<VAD update=\"true\">' + Mrcd[i].get('VAD')+ '</VAD>'+ '<STD update=\"true\">' + Mrcd[i].get('STD')+ '</STD>'+ '<QBAD update=\"true\">' + Mrcd[i].get('QBAD')+ '</QBAD>'+ '<T1 update=\"true\">' + Mrcd[i].get('T1')+ '</T1>'+ '<T2 update=\"true\">' + Mrcd[i].get('T2')+ '</T2>'+ '<T3 update=\"true\">' + Mrcd[i].get('T3')+ '</T3>'+ '<T4 update=\"true\">' + Mrcd[i].get('T4')+ '</T4>'+ '<HUAYY update=\"true)\">' + Mrcd[i].get('HUAYY').replace('<','&lt;').replace('>','&gt;')+ '</HUAYY>'+ '<LURY update=\"true\">' + Mrcd[i].get('LURY').replace('<','&lt;').replace('>','&gt;')+ '</LURY>'+ '<BEIZ update=\"true\">' + Mrcd[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+ '</BEIZ>'+ '<HUAYLB update=\"true\">' + Mrcd[i].get('HUAYLB').replace('<','&lt;').replace('>','&gt;')+ '</HUAYLB>' + '</result>' ; \n"
				+ "}\n"
				+ "if(gridDiv_history==''){ \n"
				+ "Ext.MessageBox.alert('提示信息','没有选择数据信息');\n"
				+ "}else{\n"
				+ "    var Cobj = document.getElementById('CHANGE');\n"
				+ "    Cobj.value = '<result>'+gridDiv_history+'</result>';\n"
				+ "    document.getElementById('"
				+ binder
				+ "').click();\n"
				+ "    Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"
				+ "}\n" + "}";
		return handler;
	}
	
	// 矿别名称
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			if(getIMeikdqmcModels().getOptionCount() >1){
				_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
			}else{
				_MeikdqmcValue = new IDropDownBean(-1,"无选项");
			}
			
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		try {

			String sql = "";
			String zhuanmlbid="";
			
			sql = "select id from zhuanmlb where mingc = '化验编码'";
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				zhuanmlbid = rsl.getString("id");
			}
			rsl.close();
		
			
//			电厂Tree刷新条件
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
				try {
					ResultSet rsss=con.getResultSet("select id from diancxxb where fuid="+getTreeid());
					if(rsss.next()){
						str = "and dc.fuid="+ getTreeid() ;
					}else{
						str = "and dc.id = " + getTreeid() ;
					}
				} catch (SQLException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
				
			}
			
			//是否显示采样时间
			String riqTiaoj = this.getRiq();
			if (riqTiaoj == null || "".equals(riqTiaoj)) {
				riqTiaoj = DateUtil.FormatDate(new Date());
			}
			boolean isShowRq = false;
			String strTemp ="";
			isShowRq="是".equals(MainGlobal.getXitxx_item("化验", "化验录入显示采样日期", visit.getDiancxxb_id()+"", "否"));
			if (isShowRq) {
				strTemp=" AND (SELECT caiyrq FROM caiyb WHERE zhilb_id=l.zhilb_id)=to_date('" + riqTiaoj + "','yyyy-mm-dd')\n";
			} 
			
			
			sql = 
				"select z.id,\n" +
				"       to_char(z.bianm) as huaybh\n" + 
				"from zhuanmb z, zhillsb l,\n" + 
				"       (select distinct f.zhilb_id \n" + 
				"        from fahb f,diancxxb dc\n" + 
				"        where f.diancxxb_id = dc.id\n" +
				               str+ ") fh ,zhiyryb zy\n" + 
				"where z.zhillsb_id = l.id and zhuanmlb_id = " + zhuanmlbid + "\n" + 
				"      and l.zhilb_id = fh.zhilb_id\n" + 
			//	"      and (l.shenhzt = 0 or l.shenhzt = 2)\n" + 
				"      and l.zhilb_id=zy.zhilb_id\n" + 
				"      and zy.zhiyrq=to_date('"+this.getZhiyrq()+"','yyyy-mm-dd')\n" + 
				strTemp +
				"order by l.id, l.huaylb";
			
			_IMeikdqmcModel = new IDropDownModel(sql,"全部");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}


	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ZuofChick = false;

	public void ZuofButton(IRequestCycle cycle) {
		_ZuofChick = true;
	}

	public void submit(IRequestCycle cycle) {

//		System.out.println(this.getChange());
		if (_RefurbishChick) {
			_RefurbishChick = false;

		}else{
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			
			this.setChange(null);
		}
		getSelectData();
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

	public String gridId;

	public int pagsize;

	public int getPagSize() {
		return pagsize;
	}

	public String getGridScriptLoad() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(gridId).append("_grid.render();");
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:0, limit:").append(getPagSize())
					.append("}});\n");
			gridScript.append(gridId).append(
					"_ds.on('datachanged',function(){ ").append(gridId).append(
					"_history = ''; });");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		return gridScript.toString();
	}

	public String getGridHtml() {
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

	public String getTbarScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");
		return gridScript.toString();
	}

	public String tbars;

	public String getTbar() {
		if (tbars == null) {
			tbars = "";
		}
		return tbars;
	}

	public void setTbar(String tbars) {
		this.tbars = tbars;
	}

	private String getToolbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length() - 1);
		tbarScript.append("]");
		return tbarScript.toString();
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
			this.setChange(null);
			setExtGrid(null);
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			visit.setString6(DateUtil.FormatDate(new Date()));
			getSelectData();
			
		}
		
	}
	
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
		}
	}
//	制样日期绑定,宣威电厂用
	
	public void setZhiyrq(String zhiyrq) {
		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(zhiyrq)) {
			
			((Visit) this.getPage().getVisit()).setString6(zhiyrq);
		}
	}
	public String getZhiyrq() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}
}
