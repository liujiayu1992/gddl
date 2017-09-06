package com.zhiren.dc.hesgl.changnzf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
/**
 * @author 刘雨
 * 2010-01-27
 * 描述：装车结算选择页面
 */
public class Zhuangcjs extends BasePage implements PageValidateListener {
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

	// 长别下拉框取值

	public String getChangb() {

		if (((Visit) this.getPage().getVisit()).getString3().equals("")) {

			((Visit) this.getPage().getVisit())
					.setString3(((IDropDownBean) getChangbSelectModel()
							.getOption(0)).getValue());
		}

		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}

	// 主结算单位下拉框
	public String getMainjsdw() {

		if (((Visit) this.getPage().getVisit()).getString13().equals("")) {

			((Visit) this.getPage().getVisit())
					.setString13(((IDropDownBean) getChangbSelectModel()
							.getOption(0)).getValue());
		}

		return ((Visit) this.getPage().getVisit()).getString13();
	}

	public void setMainjsdw(String value) {

		((Visit) this.getPage().getVisit()).setString13(value);
	}

	// 验收编号下拉框取值

	public String getYansbh() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setYansbh(String yansbh) {
		((Visit) this.getPage().getVisit()).setString4(yansbh);
	}

	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString5(rbvalue);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		getSelectData();
	}

	private boolean _SbChick = false;

	public void SbButton(IRequestCycle cycle) {
		_SbChick = true;
	}

	private boolean _RbChick = false;

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}
	
	private boolean _DJChick = false;

	public void DJButton(IRequestCycle cycle) {
		_DJChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_SbChick) {
			_SbChick = false;
			getSelectData();
		}

		if (_RbChick) {
			_RbChick = false;
			gotoZafjs(cycle);
		}
		
		if (_DJChick) {
			_DJChick = false;
			cycle.activate("Zhuangcdjwh");
		}
	}
	
	Calendar temp1; // 两个临时变量用于比较起始日期和截止日期
	Calendar temp2;
	//功能：跳转结算
	//逻辑：将结算页面需要的参数保存到visit的变量中，跳转到装车结算（Zcjs）页面
	private void gotoZafjs(IRequestCycle cycle) {
		
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = this.getExtGrid();
		ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
		ResultSetList mdrsl1 = egu.getModifyResultSet(this.getChange());
		Visit visit = (Visit)this.getPage().getVisit();
		
		//结算编号
		SimpleDateFormat sdfbm = new SimpleDateFormat("yyyyMMdd");
		String bmsql = "select max(bianm) as bianm from zafjsb where bianm like '"+ sdfbm.format(new Date()).substring(0, 6) +"___'";
		ResultSetList bmrsl = con.getResultSetList(bmsql);
		String bianm = "";
		while(bmrsl.next()) {
			if (bmrsl.getString("bianm").equals("")) {
				bianm = sdfbm.format(new Date()).substring(0, 6) + "001";
			} else {
				bianm = (Integer.parseInt(bmrsl.getString("bianm"))+1)+"";
			}
		}
		visit.setString12(bianm);
		
		//itemID;fahbID
		StringBuffer sbsql = new StringBuffer();
		while(mdrsl1.next()) {
			sbsql.append(mdrsl1.getString("id")).append(";").append(mdrsl1.getString("fahid")).append(",");
		}
		visit.setString11(sbsql.substring(0, sbsql.length()-1));

		int sumChes = 0;//车数
		double sumKoud = 0.00;//扣吨
		double zhi = 0.00;//税率
		double sumShul = 0.0; // 数量总和
		double sumDanj = 0.0; // 单价总和
		double sumFeiyhj = 0.0; // 费用合计总和
		double sumBuhszf = 0.0; // 不含税杂费总和
		int mark = 0; // 第一次循环比较起始日期、截止日期、备注的标记
		String gys = "";
		String shoukdw = ""; //收款单位
		String beiz = "";//备注存煤矿单位
		StringBuffer strsb = new StringBuffer();
		ResultSetList rsl_skdw = null;//判断收款单位
		String sql_skdw="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		while(mdrsl.next()) {
//			获取选择记录的供货单位字段的所有供应商
			gys = gys+"'"+mdrsl.getString("fahdw")+"',";
			
			//车数
			sumChes = sumChes + mdrsl.getInt("ches");
			
			//扣吨
			sumKoud = sumKoud + mdrsl.getDouble("koud");
			
//			获取选择记录的数量字段的总和
			sumShul = sumShul + Double.parseDouble(mdrsl.getString("duns"));
			
//			获取选择记录的单价字段的值（取最大的）
			if(sumDanj<Double.parseDouble(mdrsl.getString("danj"))){
				sumDanj = Double.parseDouble(mdrsl.getString("danj"));
			}
			
//			获取选择记录的不含税杂费的总和(不含税杂费=费用合计*(1 - 税率))
//			sumBuhszf = sumBuhszf +  Double.parseDouble(mdrsl.getString("feiyhj")) * (1.0 - Double.parseDouble(mdrsl.getString("shuil")));
//			visit.setDouble4(Math.round(sumBuhszf * 100)/100.0);
			
//			获取收款单位名称
			sql_skdw = "select mingc from shoukdw where mingc='"+mdrsl.getString("zhuangcdw")+"'\n";
			rsl_skdw = con.getResultSetList(sql_skdw);
			if(rsl_skdw.next()){
				shoukdw = mdrsl.getString("zhuangcdw");
			} else {
				setMsg("装车单位"+mdrsl.getString("zhuangcdw")+"在收款单位表中不存在，请先配置收款单位！");
			}
			
//			获取收款单位ID
			visit.setString13(mdrsl.getString("shoukdw_id"));
			
//			获取所选记录的起始日期和截止日期，如果选择多条记录那么获取最小的日期和最大的日期
			try {
				
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				c1.setTime(sdf.parse(mdrsl.getString("fahrq")));
				c2.setTime(sdf.parse(mdrsl.getString("fahrq")));
				if (mark == 0) { // 第一次循环不比较起始作用范围和截止作用范围，只将它们放到变量里以供下次循环比较
					temp1 = c1;
					temp2 = c2;
				} else {
					if (!temp1.before(c1)) {
						temp1 = c1;
					}
					if (!temp2.after(c2)) {
						temp2 = c2;
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
//			煤矿信息（放在备注里）
			if (mark == 0) {//第一次循环
				strsb.append(mdrsl.getString("meikxxb_id"));
			} else {
				strsb.append(",").append(mdrsl.getString("meikxxb_id"));
			}
			
//			if (mdrsl.getRows() == 1) {
//				visit.setString10(mdrsl.getString("beiz"));
//			} else {
//				strsb.append(mdrsl.getString("beiz")).append(";");
//				visit.setString10(strsb.substring(0, strsb.length() - 1));
//			}
			
			if(zhi<mdrsl.getDouble("shuil")){
				zhi=mdrsl.getDouble("shuil");
			}
			
			mark ++;
		}
		
//		获取选择记录的费用合计字段的总和
		sumFeiyhj = CustomMaths.mul(sumShul,sumDanj);

		
//		获取供应商名称，如果选择的记录的供应商相同则在结算单显示，否则不显示。
		String sql = "select distinct mingc from gongysb where mingc in(" + gys.substring(0, gys.length()-1)  + ")";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() == 1) {
			if(rsl.next()){
				visit.setString1(rsl.getString("mingc"));
			}
		} else {
			visit.setString1("");
		}
		
		visit.setDouble2(CustomMaths.Round_new(sumShul, 2));//结算数量
		visit.setDouble3(CustomMaths.Round_new(sumDanj,2));//单价
		visit.setDouble4(CustomMaths.Round_new(CustomMaths.mul(sumFeiyhj, 1-zhi), 2));//不含税杂费
		visit.setDouble5(CustomMaths.Round_new(CustomMaths.mul(sumFeiyhj, zhi),2));//杂费税款
		visit.setDouble6(CustomMaths.Round_new(sumFeiyhj, 2));//费用合计
		visit.setDouble7(CustomMaths.Round_new(sumKoud, 2));//扣吨
		visit.setInt2(sumChes);//车数
		
		visit.setString7(shoukdw);//收款单位
		visit.setString8(sdf.format(temp1.getTime()));//起始日期
		visit.setString9(sdf.format(temp2.getTime()));//截止日期
		visit.setDouble16(CustomMaths.Round_new(zhi, 2));//税率
		
		sql = "select mingc from meikxxb where id in (" +strsb.toString()+")";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			beiz=beiz+rsl.getString("mingc")+",";
		}
		visit.setString10(beiz.substring(0,beiz.length()-1));
		
		rsl.close();
		rsl_skdw.close();
		con.Close();
		
////		获取选择记录的单价并保存在Visit中，如果选择的是多条记录那么单价=费用合计/结算数量
//		visit.setDouble3(Math.round(sumFeiyhj/sumShul*100)/100.0);
		
//		获取杂费税款并保存在Visit中，杂费税款=费用合计-不含税杂费
//		visit.setDouble5(Math.round((sumFeiyhj - (Math.round(sumBuhszf * 100)/100.0)) * 100) / 100.0);
		
		cycle.activate("Zcjs");
	}

	public String rb1() {
		if (getRbvalue() == null || getRbvalue().equals("")) {

			return "fh.fahrq";

		} else {
			if (getRbvalue().equals("fahrq")) {
				return "fh.fahrq";
			} else
				return "fh.daohrq";
		}

	}

	public String rb2() {
		if (getRbvalue() == null || getRbvalue().equals("")) {

			return Locale.fahrq_fahb + ":";// 发货日期

		} else {
			if (getRbvalue().equals("fahrq")) {
				return Locale.fahrq_fahb + ":";
			} else
				return Locale.daohrq_id_fahb + ":";// 到货日期
		}

	}

	public String riq1() {
		String riqTiaoq = this.getQisriqi();
		if (riqTiaoq == null || riqTiaoq.equals("")) {
			riqTiaoq = DateUtil.FormatDate(new Date());

		}
		return riqTiaoq;
	}

	public String riq2() {
		String riqTiaoj = this.getJiezriqi();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());

		}
		return riqTiaoj;
	}

	// private String tree = "";

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		String str = "";
		String tiaoj_fahrq = "";
		String tiaoj_daohrq = "";
		String tiaoj_changb = "";
		String tiaoj_returnvalue_changb = "";

		if (getRbvalue() == null || getRbvalue().equals("fahrq")
				|| getRbvalue().equals("")) {

			tiaoj_fahrq = "checked:true, \n";

		} else if (getRbvalue().equals("daohrq")) {

			tiaoj_daohrq = "checked:true, \n";
		}

		tiaoj_changb = ", \n" + "{  \n" + "	xtype:'textfield', \n"
				+ "	fieldLabel:'厂别', \n" + "	width:0  \n" + "}, \n"
				+ "Changb=new Ext.zr.select.Selectcombo({ \n"
				+ "	multiSelect:true,\n" + "	width:150, \n"
				+ "	transform:'ChangbDropDown', \n" + "	lazyRender:true, \n"
				+ "	triggerAction:'all', \n" + "	typeAhead:true, \n"
				+ "	forceSelection:true \n" + "}),	\n" + "{	\n"
				+ "	xtype:'textfield', \n" + "	fieldLabel:'主结算单位', \n"
				+ "	width:0  \n" + "}, \n"
				+ "Mainjsdw=new Ext.form.ComboBox({	\n" + "	width:150,	\n"
				+ "	selectOnFocus:true,	\n"
				+ "	transform:'MainjsdwDropDown',	\n" + "	lazyRender:true, \n"
				+ "	triggerAction:'all', \n" + "	forceSelection:true \n"
				+ "})	\n";

		tiaoj_returnvalue_changb = "	document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue();	\n"
				+ "	document.getElementById('TEXT_MAINJSDW_VALUE').value=Mainjsdw.getRawValue();	\n";

		str = "var form = new Ext.form.FormPanel({ " + "baseCls: 'x-plain', \n"
				+ "labelAlign:'right', \n" + "defaultType: 'radio',\n"
				+ "items: [ \n" + "{ \n" + "    	xtype:'textfield', \n"
				+ "    	fieldLabel:'日期选择',\n"

				+ "    	width:0 \n" + "    },	\n" + " { \n" + "		boxLabel:'"
				+ Locale.fahrq_fahb + "', \n" + "     anchor:'95%', \n";

		str += tiaoj_fahrq;

		str += "     Value:'fahrq', \n"
				+ "		id:'fahrq',\n"
				+ "		name:'test',\n"
				+ "		listeners:{ \n"
				+ "				'focus':function(r,c){\n"
				+ "					document.getElementById('rbvalue').value=r.Value;\n"
				+ "				},\n"
				+ "				'check':function(r,c){ \n"
				+ "					document.getElementById('rbvalue').value=r.Value;\n"
				+ "					\tif(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){	\n"
				+ " 				\t	document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;} \n"
				+ "				}\n" + "		} \n"

				+ "	},\n" + "	{  \n"

				+ "		boxLabel:'" + Locale.daohrq_id_fahb + "',\n";
		str += tiaoj_daohrq;

		str += "		Value:'daohrq', \n"
				+ "     anchor:'95%',	\n"
				+ "		id:'daohrq',\n"
				+ "		name:'test',\n"
				+ "		listeners:{ \n"
				+ "				'focus':function(r,c){ \n"
				+ "					document.getElementById('rbvalue').value=r.Value;\n"
				+ "				}, \n"
				+ "				'check':function(r,c){\n"
				+ "					document.getElementById('rbvalue').value=r.Value;\n"
				+ "					\tif(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){	\n"
				+ " 				\t	document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;} \n"
				+ "				}\n"

				+ "		}	\n"
				+ "	}		\n"

				+ tiaoj_changb
				+ "]\n"
				+ " });\n"

				+ " win = new Ext.Window({\n"
				+ " el:'hello-win',\n"
				+ " layout:'fit',\n"
				+ " width:500,\n"
				+ " height:300,\n"
				+ " closeAction:'hide',\n"
				+ " plain: true,\n"
				+ " title:'条件',\n"
				+ " items: [form],\n"

				+ "buttons: [{\n"
				+ "    	text:'确定',\n"
				+ "   	handler:function(){  \n"
				+ "   	win.hide();\n"
				+ tiaoj_returnvalue_changb
				+ "		document.getElementById('TEXT_RADIO_RQSELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
				+ " 	document.forms[0].submit(); \n"
				+ "  }   \n"
				+ "},{\n"
				+ "   text: '取消',\n"
				+ "   handler: function(){\n"
				+ "       	win.hide();\n"
				+ "			document.getElementById('TEXT_MAINJSDW_VALUE').value='';	\n"
				+ "			document.getElementById('TEXT_CHANGB_VALUE').value='';	\n"
				+ "			document.getElementById('TEXT_YANSBH_VALUE').value='';	\n"
				+ "   }\n" + "}]\n" + " });";

		String chaxun = "select it.id,fh.id as fahid,decode(grouping(it.mingc),1,'合计',it.mingc) as zhuangcdw,\n"
				+ "       fh.fahrq,\n"
				+ "       fh.daohrq,\n"
				+ "       fh.gongysb_id,\n"
				+ "       gys.mingc as fahdw,\n"
				+ "       fh.meikxxb_id,\n"
				+ "       m.mingc as meikdw,\n"
				+ "       sum(c.ches) ches,\n"
				+ "       sum(c.maoz - c.piz - c.zongkd) duns,\n"
				+ "       nvl(GetDanj(it.id,fh.id),0) as danj,\n"
				+ "       nvl(GetShuil_zc(it.id,fh.id),0) as shuil,\n"
				+ "       sum(c.zongkd) as koud\n"
				+ "  from fahb fh,gongysb gys,meikxxb m,\n"
				+ "       (select fahb_id,\n"
				+ "               zhuangcdw_item_id item_id,\n"
				+ "               count(id) ches,\n"
				+ "               sum(maoz) maoz,\n"
				+ "               sum(piz) piz,\n"
				+ "               sum(zongkd) zongkd\n"
				+ "          from chepb\n"
				+ "         group by fahb_id, zhuangcdw_item_id) c,\n"
				+ "       item it,\n"
				+ "       zhuangcjsglb gl\n"
				+ " where fh.id = c.fahb_id\n"
				+ "   and it.id = c.item_id\n"
//				+ "   and it.id = jg.zhuangcdw_item_id(+)\n"
				+ "   and c.fahb_id = gl.fahb_id(+)\n"
				+ "   and c.item_id = gl.zhuangcdwb_item_id(+)\n"
				+ "   and c.fahb_id not in (select fahb_id from zhuangcjsglb)\n"
				+ "   and fh.gongysb_id = gys.id\n"
				+ "   and fh.meikxxb_id = m.id\n"
				+ getWhere()
				+ " group by rollup(fh.id,\n"
				+ "          c.item_id,\n"
				+ "          it.id,\n"
				+ "          it.mingc,\n"
				+ "          fh.fahrq,\n"
				+ "          fh.daohrq,\n"
				+ "          fh.gongysb_id,\n"
				+ "          gys.mingc,\n"
				+ "          fh.meikxxb_id,\n"
				+ "          m.mingc)\n"
				+ " having not (grouping(fh.id)=0 and grouping(m.mingc)=1)"
				+ " order by it.mingc,fh.fahrq, fh.daohrq, fh.gongysb_id,gys.mingc,fh.meikxxb_id,m.mingc";

		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("fahid").setHidden(true);
		egu.getColumn("zhuangcdw").setHeader("装车单位");
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("fahdw").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikdw").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("duns").setHeader("吨数(毛-皮-扣)");
		egu.getColumn("danj").setHeader("单价");

		egu.getColumn("zhuangcdw").setWidth(100);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("fahdw").setWidth(100);
		egu.getColumn("meikdw").setWidth(100);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("duns").setWidth(80);
		egu.getColumn("danj").setWidth(80);
		egu.getColumn("gongysb_id").setHidden(true);
		egu.getColumn("meikxxb_id").setHidden(true);
		egu.getColumn("shuil").setHidden(true);
		egu.getColumn("koud").setHidden(true);

		egu.addPaging(0); // 设置分页
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		egu.addTbarText(rb2());
		DateField df1 = new DateField();
		df1.setValue(this.getQisriqi());
		df1.Binding("QISRIQI", "");// forms[0] 与html页中的id绑定,并自动刷新
		df1.setId("fahrq");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");

		egu.addTbarText("至:");
		DateField df2 = new DateField();
		df2.setValue(this.getJiezriqi());
		df2.Binding("JIEZRIQI", "");// forms[0] 与html页中的id绑定,并自动刷新
		df2.setId("fahrq2");
		egu.addToolbarItem(df2.getScript());
		egu.addTbarText("-");
		// /
		egu.setDefaultsortable(false);
		egu.addTbarText("装车单位:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("FazDropDown");
		comb1.setId("faz");
		// comb1.setEditable(true);
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(80);
		egu.addToolbarItem(comb1.getScript());
		// 设置树
		egu.addTbarText(Locale.gongysb_id_fahb);
		String condition = " and " + rb1() + ">=to_date('" + this.getQisriqi()
				+ "','yyyy-MM-dd') and " + rb1() + "<=to_date('"
				+ this.getJiezriqi() + "','yyyy-MM-dd')";
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(), condition);
		etu.setWidth(60);
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");
//		egu
//				.addOtherScript("gongysTree.on('select',function(){document.forms[0].submit();});");

		// egu.addOtherScript("faz.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		egu.addToolbarItem("{"+ new GridButton("刷新",
								"function(){if(faz.getValue()==-1){\n" +
								"		Ext.MessageBox.alert('提示信息','请先选择装车单位！');\n" +
								"		return;\n" +
								"} else {\n" +
								"	document.getElementById('RefurbishButton').click();}\n" +
								"}\n ")
								.getScript() + "}");
		egu.addToolbarItem("{"+ new GridButton(
								"条件",
								"function(){ if(!win){ "
										+ str
										+ "}"
										+ " win.show(this);	\n"
										+ " \tif(document.getElementById('TEXT_CHANGB_VALUE').value!=''){	\n"
										+ "		\tChangb.setRawValue(document.getElementById('TEXT_CHANGB_VALUE').value);	\n"
										+ "	\t}	\n"
										+ " \tif(document.getElementById('TEXT_MAINJSDW_VALUE').value!=''){	\n"
										+ "		\tMainjsdw.setRawValue(document.getElementById('TEXT_MAINJSDW_VALUE').value);	\n"
										+ "	\t}	\n" + "}").getScript() + "}");
		egu.addToolbarButton("结算", GridButton.ButtonType_SubmitSel,
				"RbButton");
		egu.addToolbarItem("{"+new GridButton("单价设置","function(){ document.getElementById('DJButton').click();" +
			"}").getScript()+"}");
		egu
				.addOtherScript("gridDiv_grid.on('rowclick',function(own,row,e){ \n"
						+ " \tvar ches=0,duns=0;	\n"
						+ "	var rec = gridDiv_grid.getSelectionModel().getSelections();	\n"
						+ " for(var i=0;i<rec.length;i++){ \n"
						+ " \tif(''!=rec[i].get('ID')){ \n"
						+ "		\tches+=eval(rec[i].get('CHES'));"
						+ "		\tduns+=eval(rec[i].get('DUNS'));"
						+ " \t}	\n"
						+ " }	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('DUNS',Round_new(duns,2));	\n"
						+ " });	");

		egu.addOtherScript("function gridDiv_save(rec){	\n"
				+ "	\tif(rec.get('ZHUANGCDW')=='合计'){	\n"
				+ "	\treturn 'continue';	\n" + "	\t}}");
		setExtGrid(egu);
		con.Close();
	}

	private String getWhere() {
		// TODO 自动生成方法存根
		// 条件设
		String where = "";

		// 日期设置
		where += " and " + rb1() + ">= to_date('" + riq1()
				+ "', 'yyyy-mm-dd') and " + rb1() + "<= to_date('" + riq2()
				+ "', 'yyyy-mm-dd')";

		// 供应商
		if (getFazSelectValue().getId() > -1) {

			where += " and it.id=" + getFazSelectValue().getId() + "	\n";
		}
		if (!getTreeid().equals("0")) {

			where += " and (m.id = " + getTreeid() + " or gys.id = "
					+ getTreeid() + ")";
		}
		// where += TreeID();
		// 测试时注掉，正式环境要用
		if (((Visit) getPage().getVisit()).isFencb()) {

			where += " and fh.diancxxb_id in ("
					+ MainGlobal.getProperIds(this.getChangbSelectModel(), this
							.getChangb()) + ")";
		} else {
			where += " and fh.diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		return where;
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

	// /////发站下拉框

	public IDropDownBean getFazSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getFazSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setFazSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setFazSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getFazSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getFazSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getFazSelectModels() {
		String sql = "select id, mingc\n"
			+ "  from item\n"
			+ " where itemsortid in (select id from itemsort where bianm = 'ZHUANGCDW')\n"
			+ " order by mingc";	
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
	}

	// /场别
	public IDropDownBean getChangbSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {

			if (getChangbSelectModel().getOptionCount() > 0) {

				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getChangbSelectModel()
								.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setChangbSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}
	}

	public void setChangbSelectModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getChangbSelectModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getChangbSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getChangbSelectModels() {
		String sql = "";
		JDBCcon con = new JDBCcon();

		sql = "select id,mingc from diancxxb d where d.fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + " \n "
				+ "order by xuh,mingc";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() == 0) {

			sql = "select id,mingc from diancxxb where id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		rsl.close();
		con.Close();

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql));
	}

	// 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// 得到电厂的默认到站
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setInt1(-1);
			visit.setList1(null);

			visit.setString2(""); // Treeid
			visit.setString3(""); // 长别下拉框取值
			visit.setString4(""); // 验收编号下拉框取值
			visit.setString5(""); // Radio日期选择取值
			
			visit.setString1(""); // 供应商名称
			visit.setDouble2(0);//结算数量
			visit.setDouble3(0);//单价
			visit.setDouble4(0);//不含税杂费
			visit.setDouble5(0);//杂费税款
			visit.setDouble6(0);//费用合计
			visit.setString7("");//收款单位
			visit.setString8("");//起始日期
			visit.setString9("");//截止日期
			visit.setString10(""); // 验收编号
			visit.setString11("");//itemID;fahbID
			visit.setString12(""); // bianm
			visit.setString13(""); // Mainjsdw（主结算单位）
			visit.setString14(""); // riq1
			visit.setString15(""); // riq2
			visit.setString16(""); // 税率

			// 页面传值初始化开始
//			visit.setLong1(0); // Diancxxb_id
//			visit.setLong2(0); // Feiylbb_id(已作废)
//			// visit.setLong3(0); //Meikxxb_id
//			visit.setLong4(0); // Faz_id
//			visit.setLong5(0); // Daoz_id
//			visit.setString8(""); // 验收编号
//			visit.setString9(""); // Fahrq
			
			// 页面传值初始化结束
			setFazSelectValue(null);
			setFazSelectModel(null);
			setChangbSelectValue(null);
			setChangbSelectModel(null);
			getFazSelectModel();
			getChangbSelectModel();
		}
		getSelectData();
	}

	public String getTreeid() {

		if (((Visit) getPage().getVisit()).getString2().equals("")) {

			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {

		if (!((Visit) getPage().getVisit()).getString2().equals(treeid)) {

			((Visit) getPage().getVisit()).setString2(treeid);
//			getFazSelectModels();
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

	public int getEditTableRow() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}

	public void setEditTableRow(int value) {

		((Visit) this.getPage().getVisit()).setInt1(value);
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

	public String getQisriqi() {
		if (((Visit) this.getPage().getVisit()).getString14() == null
				|| ((Visit) this.getPage().getVisit()).getString14().equals("")) {

			((Visit) this.getPage().getVisit()).setString14(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString14();
	}

	public void setQisriqi(String qisriqi) {

		if (((Visit) this.getPage().getVisit()).getString14() != null
				&& !((Visit) this.getPage().getVisit()).getString14().equals(
						qisriqi)) {

			((Visit) this.getPage().getVisit()).setString14(qisriqi);
		}

	}

	public String getJiezriqi() {
		if (((Visit) this.getPage().getVisit()).getString15() == null
				|| ((Visit) this.getPage().getVisit()).getString15().equals("")) {

			((Visit) this.getPage().getVisit()).setString15(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString15();
	}

	public void setJiezriqi(String jiezriqi) {

		if (((Visit) this.getPage().getVisit()).getString15() != null
				&& !((Visit) this.getPage().getVisit()).getString15().equals(
						jiezriqi)) {

			((Visit) this.getPage().getVisit()).setString15(jiezriqi);
		}

	}

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根

	}
}
