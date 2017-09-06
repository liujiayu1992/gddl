package com.zhiren.shanxdted.huocbl;

import java.sql.ResultSet;
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
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-10-26
 * 修改内容:调整页面初始化时间为当前时间的前一天
 */
public class Huocbl extends BasePage implements PageValidateListener {
	
	private static int c_yuns = 0;
	private static int c_yingd = 1;
	private static int c_kuid = 2;
	
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
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long fahb_id =0;
		long chepb_id =0;
		long zhilb_id = 0;
		long lie_id=0;
		long caiyb_id = 0;
		long zhillsb_id = 0;
		long yangpdhb_id = 0;
		long zhuanmb_id=0;
		long gongysb_id = 0;
		
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if(rsl == null) {
			setMsg(ErrorMessage.NullResult);
			return;
		}
	
		while (rsl.next()) {
			ResultSetList rss = con.getResultSetList("SELECT yunslb.yunsl FROM yunslb,meikxxb WHERE  yunslb.meikxxb_id=meikxxb.id AND meikxxb.mingc='" + rsl.getString("mk")+ "'");
			double yunsl = 0;
			if (rss.next()) {
				yunsl = rss.getDouble("yunsl");
			}
			double[] data = new double[3];
			double Jingz = rsl.getDouble("maoz") - rsl.getDouble("piz");
			countYuns(Jingz,rsl.getDouble("biaoz"),yunsl,data);
			
			if (rsl.getString("id").equals("0")) {
				fahb_id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				zhilb_id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				lie_id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				gongysb_id = getGongysbID(rsl.getString("mk"), con);
				//fahb
				sb.append(
						"INSERT INTO FAHB\n" +
						"  (ID,\n" + 
						"   YUANID,\n" + 
						"   DIANCXXB_ID,\n" + 
						"   GONGYSB_ID,\n" + 
						"   MEIKXXB_ID,\n" + 
						"   PINZB_ID,\n" + 
						"   FAZ_ID,\n" + 
						"   DAOZ_ID,\n" + 
						"   JIHKJB_ID,\n" + 
						"   FAHRQ,\n" + 
						"   DAOHRQ,\n" + 
						"   ZHILB_ID,\n" + 
						"   YUNSFSB_ID,\n" + 
						"   CHEC,\n" + 
						"   MAOZ,\n" + 
						"   PIZ,\n" + 
						"   JINGZ,\n" + 
						"   BIAOZ,\n" + 
						"   YINGD,\n" + 
						"   YINGK,\n" + 
						"   YUNS,\n" + 
						"   YUNSL,\n" +
						"   CHES,\n" + 
						"   LIE_ID,LIUCZTB_ID,HEDBZ)\n" + 
						"VALUES\n" + 
						"  (" + fahb_id + "," +
						fahb_id + "," +
						this.getTreeid() + "," +
						gongysb_id + "," +
						this.getExtGrid().getColumn("mk").combo.getBeanId(rsl.getString("mk")) + "," +
						"30036641," +
						this.getExtGrid().getColumn("dz").combo.getBeanId(rsl.getString("dz")) + "," +
						this.getExtGrid().getColumn("dz").combo.getBeanId(rsl.getString("dz")) + "," +
						this.getExtGrid().getColumn("jk").combo.getBeanId(rsl.getString("jk")) + "," +
						"to_date('" + rsl.getString("daohrq")+ "','yyyy-mm-dd')," +
						"to_date('" + rsl.getString("daohrq")+ "','yyyy-mm-dd')," +
						zhilb_id + "," +
						"1,1," +
						rsl.getString("maoz") + "," +
						rsl.getString("piz") + "," +
						Jingz + "," +
						rsl.getString("biaoz") + "," +
						data[c_yingd] + "," +
						(data[c_yingd]-data[c_kuid])+ "," +
						data[c_yuns] + "," +
						yunsl + ",\n" +
						rsl.getDouble("ches") + "," +
						lie_id + ",1,3" +
						");\n");
				
				//chepb
				chepb_id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				sb.append(
						"\n" +
						"INSERT INTO CHEPB\n" + 
						"  (ID,\n" + 
						"   XUH,\n" + 
						"   CHEPH,\n" + 
						"   YUANMZ,\n" + 
						"   MAOZ,\n" + 
						"   PIZ,\n" + 
						"   BIAOZ,\n" + 
						"   YINGD,\n" + 
						"   YINGK,\n" + 
						"   YUNS,\n" + 
						"   FAHB_ID,\n" + 
						"   CHEBB_ID,\n" + 
						"   YUANMKDW,\n" + 
						"   YUNSDWB_ID,\n" + 
						"   QINGCSJ,\n" + 
						"   QINGCHH,\n" + 
						"   QINGCJJY,\n" + 
						"   ZHONGCSJ,\n" + 
						"   ZHONGCHH,\n" + 
						"   ZHONGCJJY,\n" + 
						"   HEDBZ,\n" + 
						"   LURSJ,\n" + 
						"   LURY)\n" + 
						"VALUES\n" + 
						"\n" + 
						"  (" + chepb_id + "," +
						"1,'火车'," +
						rsl.getString("MAOZ") + "," +
						rsl.getString("MAOZ") + "," +
						rsl.getString("PIZ") + "," +
						rsl.getString("BIAOZ") + "," +
						data[c_yingd] + "," +
						(data[c_yingd] - data[c_kuid]) + "," +
						data[c_yuns] + "," +
						fahb_id + "," +
						"1,'" + rsl.getString("mk") + "',1," +
						"to_date('" + rsl.getString("daohrq") + "','yyyy-mm-dd'),'',''," +
						"to_date('" + rsl.getString("daohrq") + "','yyyy-mm-dd'),'',''," +
						"3," +
						"to_date('" +rsl.getString("lursj") + "','yyyy-mm-dd')," +
						"'" +rsl.getString("lury") + "'" +
						");\n");
				//caiyb
				caiyb_id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				zhillsb_id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				sb.append(
						"INSERT INTO CAIYB (ID, ZHILB_ID, CUNYWZB_ID, BIANM, CAIYRQ, XUH)\n" +
						"VALUES (" + 
						caiyb_id + "," +
						zhilb_id + ",0," +
						"'" + rsl.getString("hybh") + "'," +
						"to_date('" + rsl.getString("daohrq") + "','yyyy-mm-dd'),1" +
						");\n"
					);
				//yangpdhp
				yangpdhb_id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				ResultSetList rsBum = con.getResultSetList("SELECT * FROM bumb");
				String bum_id = "0";
				if (rsBum.next()) {
					bum_id = rsBum.getString("id");
				}
				sb.append(
						"INSERT INTO YANGPDHB\n" +
						"  (ID, CAIYB_ID, Zhilblsb_Id, LURRY, BIANH, LEIB, BUMB_ID, LEIBB_ID)\n" + 
						"VALUES\n" + 
						"  (" +
						yangpdhb_id + "," +
						caiyb_id + "," +
						zhillsb_id + "," +
						"'" + rsl.getString("lury") + "'," +
						"'" + rsl.getString("hybh") + "'," +
						"'正常样'," + bum_id + ",301624" +
						");\n"
				);
				//zhillsb
				sb.append(
						"INSERT INTO ZHILLSB (ID, ZHILB_ID, HUAYLBB_ID, HUAYLB, BUMB_ID,shenhzt)\n" +
						"VALUES (" +
						zhillsb_id + "," +
						zhilb_id + "," +
						"301624,'正常样'," + bum_id + ",0" +
						");\n"
				);
				//zhuanmb
				String ss = "SELECT * FROM zhuanmlb ";
				ResultSetList rs = con.getResultSetList(ss);
				while (rs.next()) {
					zhuanmb_id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
					sb.append(
							"INSERT INTO ZHUANMB (ID, ZHILLSB_ID, BIANM, ZHUANMLB_ID)\n" +
							"VALUES (" +
							zhuanmb_id + "," +
							zhillsb_id + "," +
							"'" + rsl.getString("hybh") + "'," +
							rs.getString("id") +
							");\n"
					);
				}
			} else {
				sb.append(
						"UPDATE FAHB\n" +
						"   SET MEIKXXB_ID =" + this.getExtGrid().getColumn("mk").combo.getBeanId(rsl.getString("mk")) +",\n" + 
						"       FAZ_ID     =" + this.getExtGrid().getColumn("dz").combo.getBeanId(rsl.getString("dz")) + ",\n" + 
						"       DAOZ_ID    =" + this.getExtGrid().getColumn("dz").combo.getBeanId(rsl.getString("dz")) + ",\n" + 
						"       JIHKJB_ID  =" + this.getExtGrid().getColumn("jk").combo.getBeanId(rsl.getString("jk")) + ",\n" + 
						"       MAOZ       =" + rsl.getString("maoz") + ",\n" + 
						"       PIZ       =" + rsl.getString("piz") + ",\n" +
						"       JINGZ      =" + Jingz + ",\n" +  
						"       BIAOZ      =" + rsl.getString("BIAOZ") + ",\n" + 
						"       CHES      =" + rsl.getString("CHES") + ",\n" + 
						"       YINGD       =" + data[c_yingd] + ",\n" +
						"       YINGK       =" + (data[c_yingd]-data[c_kuid]) + ",\n" +
						"       YUNSL       =" + yunsl + ",\n" +
						"       YUNS       =" + data[c_yuns] + "\n" + 
						" WHERE ID =" + rsl.getString("id") + ";\n"
				);
				
				sb.append(
						"UPDATE CHEPB\n" +
						"SET YUANMZ =" + rsl.getString("maoz") +" , MAOZ =" + rsl.getString("maoz") 
						+ ",piz=" + rsl.getString("piz") + ", yingd=" + data[c_yingd] + ", yingk=" + (data[c_yingd]-data[c_kuid])
						+ ", BIAOZ =" + rsl.getString("BIAOZ") + ", YUNS =" + data[c_yuns] + "\n" + 
						"WHERE FAHB_ID =" + rsl.getString("id") + ";\n"
				);
			}
		}	
		
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		if(drsl == null) {
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (drsl.next()) {
			sb.append(
					"DELETE FROM yangpdhb WHERE caiyb_id IN (SELECT ID FROM caiyb WHERE zhilb_id IN (SELECT zhilb_id FROM fahb WHERE ID=" + drsl.getString("id")+ "));\n" +
					"DELETE FROM caiyb WHERE zhilb_id IN (SELECT zhilb_id FROM fahb WHERE ID=" + drsl.getString("id") + ");\n" + 
					"DELETE FROM zhuanmb WHERE zhillsb_id IN (SELECT ID FROM zhillsb WHERE zhilb_id IN (SELECT zhilb_id FROM fahb WHERE ID="+ drsl.getString("id") +") );\n" + 
					"DELETE FROM zhillsb WHERE zhilb_id IN (SELECT zhilb_id FROM fahb WHERE ID="+ drsl.getString("id") +");\n" + 
					"DELETE FROM chepb WHERE fahb_id=" + drsl.getString("id") + ";\n" + 
					"DELETE FROM fahb WHERE ID=" + drsl.getString("id") +";\n"
					);
		}
		sb.append("end;");
		if (sb.length()>13) {
			int flag = con.getInsert(sb.toString());
			if (flag==-1) {
				con.rollBack();
				this.setMsg("保存失败！");
				return;
			}
		}
		con.commit();
	}
	
	private void countYuns(double Jingz,double Biaoz,double Yunsl,double[] data) {
		double Yuns =0;
		double Yingk = 0;
		double Yingd =0 ;
		double Kuid =0;
		Yuns =  CustomMaths.mul(Biaoz, Yunsl);
		Yingk = CustomMaths.sub(Jingz, Biaoz);

//			    如果盈亏大于零
	    if (Yingk >= 0) {
			Yuns = 0;
			Yunsl = 0;
			Yingd = Yingk;
			Kuid = 0;
	    }else 
			if (Math.abs(Yingk) <= Yuns) {
			    Yuns = Math.abs(Yingk);
			    Yingd = 0;
			    Kuid = 0;
//					    Yunsl = CustomMaths.div(Yuns, Biaoz, 4);
			}
			else {
//					    Yunsl = Yunsl;
			    Yingd = 0;
			    Kuid = CustomMaths.sub(Math.abs(Yingk),Yuns);
			}
	    data[c_yuns] = Yuns;
	    data[c_yingd] = Yingd;
	    data[c_kuid] = Kuid;
	}
	
	private long getGongysbID(String meikdw, JDBCcon con) {
		long gongyID = 0;
		String SQL = 
			"SELECT G.ID\n" +
			"  FROM GONGYSB G, GONGYSMKGLB GL, MEIKXXB M\n" + 
			" WHERE G.ID = GL.GONGYSB_ID\n" + 
			"   AND GL.MEIKXXB_ID = M.ID\n" + 
			"   AND M.MINGC = '" + meikdw + "'";
		ResultSetList rsl = con.getResultSetList(SQL);
		if (rsl.next()) {
			gongyID =  rsl.getLong("id");
		}

		return gongyID;
	}
	
	private boolean _RefreshChick = false;
	private boolean _SaveChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
//			getSelectData();
		}
		getSelectData();
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		
		sb.append(
				"SELECT F.ID,\n" +
				"       ZM.BIANM AS HYBH,\n" + 
				"       M.MINGC AS MK,\n" + 
				"       CZ.MINGC AS DZ,\n" + 
				"       J.MINGC AS JK,\n" + 
				"       F.CHES,\n" + 
				"       F.maoz,\n" + 
				"       F.piz,\n" + 
				"       F.BIAOZ,\n" + 
				"       F.JINGZ,\n" + 				
				"       F.yingk,\n" + 
				"       F.yuns,\n" + 				
				"       F.DAOHRQ,\n" + 
				"       F.JIESB_ID,\n" + 
				"       C.LURY,\n" + 
				"       C.LURSJ\n" + 
				"  FROM FAHB    F,\n" + 
				"       CHEPB   C,\n" + 
				"       ZHILLSB Z,\n" + 
				"       ZHUANMB ZM,\n" + 
				"       MEIKXXB M,\n" + 
				"       CHEZXXB CZ,\n" + 
				"       JIHKJB  J\n" + 
				" WHERE F.DAOHRQ = TO_DATE('" + this.getRiq() + "', 'yyyy-mm-dd')\n" + 
				"   AND C.FAHB_ID = F.ID\n" + 
				"   AND F.ZHILB_ID = Z.ZHILB_ID\n" + 
				"   AND Z.ID = ZM.ZHILLSB_ID\n" + 
				"   AND F.MEIKXXB_ID = M.ID\n" + 
				"   AND F.FAZ_ID = CZ.ID\n" + 
				"   AND F.JIHKJB_ID = J.ID\n" + 
				"   AND F.YUNSFSB_ID = 1\n" + 
				"   AND ZM.ZHUANMLB_ID = 100663\n" + 
				"   AND F.DIANCXXB_ID = " + this.getTreeid());

		ResultSetList rsl = con.getResultSetList(sb.toString());
		
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误sb:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setDefaultsortable(false);
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("hybh").setHeader("化验编号");
		egu.getColumn("hybh").setEditor(null);
		egu.getColumn("mk").setHeader("煤矿名称");
		egu.getColumn("dz").setHeader("到站");
		egu.getColumn("jk").setHeader("计划口径");
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("yingk").setHeader("盈亏");
		egu.getColumn("yingk").setEditor(null);
		egu.getColumn("yuns").setHeader("运损");
		egu.getColumn("yingk").setEditor(null);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setDefaultValue(this.getRiq());
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("jiesb_id").setHeader("jies");
		egu.getColumn("jiesb_id").setHidden(true);
		egu.getColumn("jiesb_id").setEditor(null);
		egu.getColumn("lury").setHeader("录入人员");
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("lursj").setEditor(null);
		
		//设置口径下拉框
		ComboBox c3 = new ComboBox();
		egu.getColumn("jk").setEditor(c3);
		c3.setEditable(true);
		String jihkjsb = SysConstant.SQL_Kouj;
		egu.getColumn("jk").setComboEditor(egu.gridId, new IDropDownModel(jihkjsb));
		
		//车站
		ComboBox c4 = new ComboBox();
		egu.getColumn("dz").setEditor(c4);
		c4.setEditable(true);
		String dzsb = "SELECT ID,mingc FROM chezxxb ";
		egu.getColumn("dz").setComboEditor(egu.gridId, new IDropDownModel(dzsb));
		
		//煤矿单位
		ComboBox c5 = new ComboBox();
		egu.getColumn("mk").setEditor(c5);
		c5.setEditable(true);
		String mksb = "SELECT ID,mingc FROM meikxxb WHERE beiz LIKE '%火车%' ";
		egu.getColumn("mk").setComboEditor(egu.gridId, new IDropDownModel(mksb));
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		String insertF = 
			"function() {\n" +
			"  var plant;\n" + 
			"  if (gridDiv_ds.getCount()==0) {\n" + 
			"    plant = new gridDiv_plant({ID: '0',HYBH: 'H01',MK: '',DZ: '',JK: '',JINGZ: '',BIAOZ: '',DAOHRQ: '" + this.getRiq() + "',LURY: '" + visit.getRenymc() + "',LURSJ: '" + DateUtil.FormatDate(new Date()) + "'});\n" + 
			"  } else {\n" + 
			"    var lastHybh=gridDiv_ds.getAt(gridDiv_ds.getCount()-1).get('HYBH');\n" + 
			"    lastHybh = Number(lastHybh.substring(1))+1;\n" + 
			"    if (lastHybh <10) {\n" + 
			"      lastHybh ='H0' + lastHybh;\n" + 
			"    } else {\n" + 
			"      lastHybh ='H' + lastHybh;\n" + 
			"    }\n" + 
			"    plant = new gridDiv_plant({ID: '0',HYBH: lastHybh,MK: '',DZ: '',JK: '',JINGZ: '',BIAOZ: '',DAOHRQ: '"+ this.getRiq() + "',LURY: '" + visit.getRenymc() + "',LURSJ: '" + DateUtil.FormatDate(new Date()) + "'});\n" + 
			"\n" + 
			"  }\n" + 
			"  gridDiv_ds.insert(gridDiv_ds.getCount(),plant);\n" + 
			"}";
		GridButton gbtI = new GridButton("添加",insertF);
		gbtI.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(gbtI);
		egu.addToolbarButton("删除", GridButton.ButtonType_Delete, null);
		egu.addToolbarButton("保存", GridButton.ButtonType_Save, "SaveButton");
		
		egu.addOtherScript("gridDiv_grid.addListener('afteredit',function(e){" +
				"" +
				"if(e.field=='MAOZ' || e.field=='PIZ'){\n" +
				" var mv=e.record.get('MAOZ');\n" +
				" var pv=e.record.get('PIZ');\n" +
				" if(mv==null || mv=='') mv=0;\n" +
				" if(pv==null || pv=='') pv=0;\n" +
				" e.record.set('JINGZ',(mv-pv));\n" +
				"\n}" +
				"if (e.field=='CHES') {\n" +
				" var chesv=eval(e.record.get('CHES')||0);\n" +
				" e.record.set('PIZ',(chesv*24));\n" +
				"\n}" +
				"" +
				"\n});");

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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public String getTreeScript1() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	
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

	//电厂树
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
//			setRiq(DateUtil.FormatDate(new Date()));
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
			getSelectData();
		}
	}
}
