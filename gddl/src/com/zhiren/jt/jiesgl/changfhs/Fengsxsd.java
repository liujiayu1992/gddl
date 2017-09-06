package com.zhiren.jt.jiesgl.changfhs;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.Money;
import com.zhiren.common.MainGlobal;
import com.zhiren.jt.jiesgl.changfhs.Balancebean;
import com.zhiren.jt.jiesgl.kuangfhs.Kuangfjsbean;
import com.zhiren.jt.jiesgl.report.kuangfhs.Kuangfjsd;
import com.zhiren.main.Visit;

import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.hesgl.jiesd.Dcbalancebean;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

public class Fengsxsd extends BasePage {

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) { 
		_editTableRow = _value;
	}
 
	private String tijldkf = "true";

	public void setTijldkf(String _tijldkf) {
		tijldkf = _tijldkf;
	}

	public String getTijldkf() {
		return tijldkf;
	}
//	合计大写
	public void setHejdxh(String value) {

		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getHejdxh() {

		return ((Visit) getPage().getVisit()).getString4();
	}

	// 条件下拉框开始

	private static Date _JiesrqsmallValue = new Date();

	private boolean _Jiesrqsmallchange = false;

	public Date getJiesrqsmall() {
		return _JiesrqsmallValue;
	}

	public void setJiesrqsmall(Date _value) {
		if (FormatDate(_JiesrqsmallValue).equals(FormatDate(_value))) {
			_Jiesrqsmallchange = false;
		} else {

			_JiesrqsmallValue = _value;
			_Jiesrqsmallchange = true;
		}

	}

	private static Date _JiesrqbigValue = new Date();

	private boolean _Jiesrqbigchange = false;

	public Date getJiesrqbig() {
		return _JiesrqbigValue;
	}

	public void setJiesrqbig(Date _value) {
		if (FormatDate(_JiesrqbigValue).equals(FormatDate(_value))) {
			_Jiesrqbigchange = false;
		} else {

			_JiesrqbigValue = _value;
			_Jiesrqbigchange = true;
		}

	}

	// 厂方结算编号
	public void setIJiesbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getIJiesbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getIJiesbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public IPropertySelectionModel getIJiesbhModels() {

		String sql = "";
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {
			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

				sql = " select bianm from "
						+ " (select distinct bianm from diancjsmkb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct bianm from diancjsyfb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by bianm";

			} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) { 

				if (getDiancmcValue().getId() == -1) {

					sql = " select bianm from "
							+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="
							+ ((Visit) getPage().getVisit()).getDiancxxb_id()
							+ " and liucztb_id=0)"
							+ " union"
							+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="
							+ ((Visit) getPage().getVisit()).getDiancxxb_id()
							+ " and liucztb_id=0) order by bianm";
				} else {

					sql = " select bianm from "
							+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.id="
							+ getDiancmcValue().getId()
							+ " and liucztb_id=0)"
							+ " union"
							+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.id="
							+ getDiancmcValue().getId()
							+ " and liucztb_id=0) order by bianm";
				}

			} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

				if (getGongsValue().getId() == -1) {

					if (getDiancmcValue().getId() == -1) {

						sql = "select bianm from "
								+ " (select distinct bianm from diancjsmkb where liucztb_id=0)"
								+ " union"
								+ " (select distinct bianm from diancjsyfb where liucztb_id=0) order by bianm";
					} else {

						sql = " select bianm from "
								+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.id="
								+ getDiancmcValue().getId()
								+ " and liucztb_id=0)"
								+ " union"
								+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.id="
								+ getDiancmcValue().getId()
								+ " and liucztb_id=0) order by bianm";
					}
				} else {

					if (getDiancmcValue().getId() == -1) {

						sql = " select bianm from "
								+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="
								+ getGongsValue().getId()
								+ " and liucztb_id=0)"
								+ " union"
								+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="
								+ getGongsValue().getId()
								+ " and liucztb_id=0) order by bianm";
					} else {

						sql = " select bianm from "
								+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.id="
								+ getDiancmcValue().getId()
								+ " and liucztb_id=0)"
								+ " union"
								+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.id="
								+ getDiancmcValue().getId()
								+ " and liucztb_id=0) order by bianm";
					}
				}
			}

			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("bianm")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public IDropDownBean getJiesbhValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {

			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getIJiesbhModel()
							.getOption(0));
		}

		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setJiesbhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean6() != value) {

			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean6(value);
		}
	}

	// 结算编号 end

	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value, false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private boolean checkbh() {
		// TODO 自动生成方法存根
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql = "";
		String jiesmkid = "";
		String jiesyfbid = "";
		
		String[] raw1=visit.getString3().split(";");
		for(int i=0;i<raw1.length;i++){
			String[] raw2=raw1[i].split(","); //分别获取 jiesmkbid jiesyfbid
			
			jiesmkid=raw2[0];
			jiesyfbid=raw2[1];
		}
		
		String id = ""; 
		if(jiesmkid!=null && !jiesmkid.equals("")){		
			if(jiesyfbid!=null && !jiesyfbid.equals("")){///俩票结算
				id = " and id not in ("+jiesmkid+","+jiesyfbid+")\n";
			}else{
				id = " and id not in ("+jiesmkid+")\n";
			}
		}else{
			id = " and id not in ("+jiesyfbid+")\n";
		}
				
		try {
			sql = "select jiesbh from ((select id,bianm as jiesbh from kuangfjsmkb)union(select id,bianm as jiesbh from kuangfjsyfb)) where jiesbh='"
					+ ((Kuangfjsbean) getEditValues().get(0)).getJiesbh() + "'"
					+ id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				return false;
			}

			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return true;
	}
	
	private long Save(IRequestCycle cycle) {
		// 存储煤款表
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql = "";
		long Id = 0;
		String[] raw1=visit.getString3().split(",");//分别获取 kuangfjsmkbid kuangfjsyfbid xiaosht caight
		String kuangfjsmkbid = raw1[0];
		String kuangfjsyfbid = raw1[1];
		String xs_htid = raw1[2];
		String cg_htid = raw1[3];
		String leix = "分公司销售单";;
		String hetid = xs_htid;
		String TableName = "diancjsmkb";
		Id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
		String sql_update = " update kuangfjsmkb set diancjsmkb_id = "+Id+" where id = "+kuangfjsmkbid+";\n";
		String colom = "";
		String zhi = "";
		if(visit.getString5().equals("采购")){
			TableName = "jiesb";
			hetid = cg_htid;
			leix = "电厂采购单";
			sql_update = "";
			colom = ",kuangfjsmkb_id";
			zhi = ","+kuangfjsmkbid;
		}
	
		try {
			
			sql=" begin \n";
			sql += " insert into "+TableName+"(id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid, ranlbmjbrq, beiz"+colom+")"
				+ " values "
				+ "("
				+ Id
				+ ", "
				+ getProperId(getIDiancmcModel(),
						((Kuangfjsbean) getEditValues().get(0))
								.getTianzdw())
				+ ", '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getJiesbh()
				+ "', "
				+ getProperId(getIFahdwModel(),
						((Kuangfjsbean) getEditValues().get(0)).getFahdw())
				+ ",'"
				+ ((Kuangfjsbean) getEditValues().get(0)).getFahdw()
				+ "','"
				+ ((Kuangfjsbean) getEditValues().get(0)).getFaz()
				+ "', to_date('"
				+ this.FormatDate(((Kuangfjsbean) getEditValues().get(0))
						.getFahksrq())
				+ "','yyyy-MM-dd'), to_date('"
				+ this.FormatDate(((Kuangfjsbean) getEditValues().get(0))
						.getFahjzrq())
				+ "','yyyy-MM-dd'),"
				+ "'"
				+ ((Kuangfjsbean) getEditValues().get(0)).getPinz()
				+ "', '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getDaibcc()
				+ "', '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getYuanshr()
				+ "', '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getXianshr()
				+ "', to_date('"
				+ this.FormatDate(((Kuangfjsbean) getEditValues().get(0))
						.getKaisysrq())
				+ "','yyyy-MM-dd'),to_date('"
				+ this.FormatDate(((Kuangfjsbean) getEditValues().get(0))
						.getJiezysrq())
				+ "','yyyy-MM-dd'),"
				+ "'"
				+ ((Kuangfjsbean) getEditValues().get(0)).getYansbh()
				+ "', '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getShoukdw()
				+ "', '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getKaihyh()
				+ "', '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getZhangh()
				+ "', '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getFapbh()
				+ "', '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getFukfs()
				+ "', '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getDuifdd()
				+ "', "
				+ ((Kuangfjsbean) getEditValues().get(0)).getChes()
				+ ", "
				+ " "
				+ ((Kuangfjsbean) getEditValues().get(0)).getJiessl()
				+ ", "
				+ ((Kuangfjsbean) getEditValues().get(0)).getJingz()
				+ ", "
				+ ((Kuangfjsbean) getEditValues().get(0)).getShulzjbz()
				+ ", "
				+ ((Kuangfjsbean) getEditValues().get(0)).getBukyqjk()
				+ ", "
				+ ((Kuangfjsbean) getEditValues().get(0)).getJiasje()
				+ ", "
				+ ((Kuangfjsbean) getEditValues().get(0)).getJiakhj()
				+ ", "
				+ ((Kuangfjsbean) getEditValues().get(0)).getJiakje()
				+ ","
				+ ((Kuangfjsbean) getEditValues().get(0)).getJiaksk()
				+ ", "
				+ ((Kuangfjsbean) getEditValues().get(0)).getJiaksl()
				+ ", "
				+ ((Kuangfjsbean) getEditValues().get(0)).getBuhsdj()
				+ ","
				+ " "
				+ ((Kuangfjsbean) getEditValues().get(0)).getJieslx()
				+ ", to_date('"+this.FormatDate(new Date())+"','yyyy-MM-dd'), null, "
				+ hetid//((Kuangfjsbean) getEditValues().get(0)).getJiesbh()
				+ ", 0, 0, sysdate, '"
				+ ((Kuangfjsbean) getEditValues().get(0)).getBeiz()
				+ "'"+zhi+");\n";
			
			sql += sql_update;
			sql += " end; \n";
			
			if (con.getUpdate(sql) >= 0) {
				setMsg(leix+"生成成功！");
				visit.setInt2(1);
				((Kuangfjsbean) getEditValues().get(0)).setId(Id);
				return Id;
			} else {
				setMsg(leix+"生成失败！");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			
			con.Close();
		}
		return 0;
	}


	private long getKuangfjsbId(long id) {
		// TODO 自动生成方法存根
		JDBCcon con = new JDBCcon();
		try {

			String sql = "select kuangfjsb_id from kuangfjsmkb where id=" + id
					+ "";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				return rs.getLong("kuangfjsb_id");
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return 0;
	}


	private long getDiancjsId(String TableName, String bianm) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		long Id=0;
		try{
			
			String sql="select id from "+TableName+" where bianm='"+bianm+"'";
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				Id=rs.getLong("id");
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return Id;
	}

	private long SaveJszbsjb(long Mkid, String mingc, String hetbz,
			double gongf, double changf, double jies, double yingk,
			double zhejbz, double zhejje, int zhuangt) {
		// 保存结算单中指标数据
		JDBCcon con = new JDBCcon();
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;
		try {

			Id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));

			sql = " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt) "
					+ " values ("
					+ Id
					+ ", "
					+ Mkid
					+ ", "
					+ getProperId(getIZhibbmModel(), mingc)
					+ ", '"
					+ hetbz
					+ "', "
					+ gongf
					+ ", "
					+ changf
					+ ", "
					+ jies
					+ ", "
					+ yingk
					+ ", "
					+ zhejbz
					+ ", "
					+ zhejje
					+ ","
					+ zhuangt
					+ ")";

			if (con.getInsert(sql) == 1) {

				return Id;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return 0;
	}


	private String Jiesbh() {

		// 结算编号
		JDBCcon con = new JDBCcon();
		String strJsbh = "";
		try {
			String sYear = "";
			String sMonth = "";
			java.util.Date datCur = new java.util.Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-01");
			String dat = formatter.format(datCur);
			sYear = dat.substring(2, 4);
			sMonth = dat.substring(5, 7);
			String sDate = sYear + sMonth;
			int intXh = 1;
			int intBh = 0;
			String sql2 = "select max(bianm) as jiesbh from ((select bianm from kuangfjsmkb where bianm like '%"
					+ sDate
					+ "%')union(select bianm from kuangfjsyfb where bianm like '%"
					+ sDate + "%'))";
			ResultSet rsSl = con.getResultSet(sql2);
			if (rsSl.next()) {
				strJsbh = rsSl.getString("jiesbh");
			}
			if (strJsbh == null) {
				strJsbh = sDate + "0000";
			}
			intBh = Integer.parseInt(strJsbh.trim().substring(
					strJsbh.trim().length() - 4, strJsbh.trim().length()));
			intBh = intBh + 1;
			if (intBh < 10000 && intBh >= 1000) {
				strJsbh = sDate + String.valueOf(intBh);
			} else if (intBh < 1000 && intBh >= 100) {
				strJsbh = sDate + "0" + String.valueOf(intBh);
			} else if (intBh >= 10 && intBh < 100) {
				strJsbh = sDate + "00" + String.valueOf(intBh);
			} else {
				strJsbh = sDate + "000" + String.valueOf(intBh);
			}
			rsSl.close();

			return strJsbh;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return strJsbh;
	}

	private String Diqbm(String Meikdqmc) {

		JDBCcon con = new JDBCcon();
		String mdiqbm = "";
		try {
			String sql = "select meikbm from ((select meikbm,meikdwmc as meikdqqc from meikxxb)union(select meikdqbm as meikbm,meikdqmc as meikdqqc from meikdqb)) where meikdqqc='"
					+ Meikdqmc + "'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				mdiqbm = rs.getString("meikbm");
			} else {
				sql = "select diqbm from diancjsmkb  where id = "
						+ ((Kuangfjsbean) getEditValues().get(0))
								.getDiancjsmkb_id();
				ResultSet rs2 = con.getResultSet(sql);
				if (rs2.next()) {
					mdiqbm = rs2.getString("diqbm");
				}
				rs2.close();
			}

			if (mdiqbm.length() > 6) {

				mdiqbm = mdiqbm.substring(0, 6);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return mdiqbm;
	}

	private long Diancjsmkb_id(String meikjsdbh) {

		JDBCcon con = new JDBCcon();
		long id = 0;
		try {
			String sql = "select id from kuangfjsmkb where bianh='" + meikjsdbh
					+ "'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				id = rs.getLong("id");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return id;
	}

	private boolean CheckMeikjsdbh(String meikjsdbh) {
		if (!meikjsdbh.equals("")) {
			JDBCcon con = new JDBCcon();
			try {
				String sql = "select id from diancjsmkb where bianh='"
						+ meikjsdbh + "'";
				ResultSet rs = con.getResultSet(sql);
				if (rs.next()) {
					return true;
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
			return false;

		} else {

			return true;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {
		_QuedChick = true;
	}
	
	private boolean _CaigChick = false;

	public void CaigButton(IRequestCycle cycle) {
		_CaigChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save(cycle);
//			getSelectData();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Fanh(cycle);
			
		}
		if(_QuedChick){
			_QuedChick=false;
			Submit();
			((Visit) getPage().getVisit()).setInt3(1);
			getIJiesbhModels();
			getSelectData();
		}
		if(_CaigChick) {
			_CaigChick = false;
			Caig();
			getSelectData();
		}
	}
	
	private void Fanh(IRequestCycle cycle){
		cycle.activate("Jiajjs");
	}
	
	public String _liucmc;
	
	public void setLiucmc(String _value) {
		_liucmc = _value;
	}
	
	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}
	
	private void Submit(){
		JDBCcon con =new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		long Liuc_id=MainGlobal.getProperId(getILiucmcModel(), getLiucmc()); 
		String TableName1 = "diancjsmkb";
		String TableName2 = "diancjsyfb";
		if(visit.getString5().equals("采购")){
			TableName1 = "jiesb";
			TableName2 = "jiesyfb";
		}
//		String bianh=((Kuangfjsbean) getEditValues().get(0)).getJiesbh();
//		
//		String sql="select * from diancjsmkb where bianm='"+bianh+"'";
//		
//		if (!con.getHasIt(sql)){//没值
//			setMsg("请先保存后再提交流程！");
//			return;
//		}
		
		if(Liuc_id>-1){
//			
			if(((Kuangfjsbean) getEditValues().get(0)).getJieslx()==Locale.liangpjs_feiylbb_id){//两票结算
				
				Liuc.tij(TableName1, ((Kuangfjsbean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				
				Jiesdcz.Zijsdlccl(TableName1,((Kuangfjsbean) getEditValues().get(0)).getId(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
				
				Liuc.tij(TableName2, ((Kuangfjsbean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				
				Jiesdcz.Zijsdlccl(TableName2,((Kuangfjsbean) getEditValues().get(0)).getYid(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
			
			}else if(((Kuangfjsbean) getEditValues().get(0)).getJieslx()==Locale.meikjs_feiylbb_id){//煤款结算
				
				Liuc.tij(TableName1, ((Kuangfjsbean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				Jiesdcz.Zijsdlccl(TableName1,((Kuangfjsbean) getEditValues().get(0)).getId(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
			}else {//运费结算
				
				Liuc.tij(TableName2, ((Kuangfjsbean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				Jiesdcz.Zijsdlccl(TableName2,((Kuangfjsbean) getEditValues().get(0)).getYid(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
			}
		}
	}
	
	private void Caig(){
		Visit visit = (Visit) getPage().getVisit();
		visit.setString5("采购");
	}
	
	private void Tijld() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int result = -1;
		String sql = "update kuangfjsmkb set shenhjb=2,zhuangt=0 where bianh='"
				+ ((Kuangfjsbean) getEditValues().get(0)).getJiesbh() + "'";
		result = con.getUpdate(sql);
		if (result < 0) {
			setMsg("提交失败!");
			con.rollBack();
			return;
		}
		con.commit();
		con.Close();
		setMsg("提交完成!");
		getEditValues().clear();
	}

	private Kuangfjsbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Kuangfjsbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Kuangfjsbean EditValue) {
		_EditValue = EditValue;
	}

	public String getDXMoney(double _Money) {
		Money money = new Money();
		return money.NumToRMBStr(_Money);
	}
	
	//显示null的字段用""显示
	private String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
	}

	public List getSelectData() {
		List _editvalues = new ArrayList();
		if(getEditValues()!=null){
			
			getEditValues().clear();
		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		try {

			long mid = 0;
			long myid = 0;
			long mdiancxxb_id=0;
			long mgongysb_id=0;
			String mtianzdw ="";
			String mjiesbh = "";
			String mfahdw = "";
			String mmeikdw="";
			String mfaz = "";
			String mshoukdw = "";
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date myansksrq = new Date();
			Date myansjzrq = new Date();
			String mfahrq="";
			String mkaihyh = "";
			String mpinz = "";
			String myuanshr = mtianzdw;
			String mzhangh = "";
			String mhetsl = "";// 合同数量
			double mgongfsl = 0;
			long mches = 0;
			String mxianshr = myuanshr;
			String mfapbh = "";
			String mdaibcc = "";
			String myansbh = "";
			String mduifdd = "";
			String mfukfs = "";
			double shulzjbz = 0;		//含税单价
			double mshulzjbz = 0;
			double myanssl = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			
			String mQnetar_ht = "";		// 合同热量
			double mQnetar_kf = 0;		// 供方热量
			double mQnetar_cf = 0;		// 厂方热量
			double mQnetar_js = 0;		// 厂方结算
			double mQnetar_yk = 0;		// 厂方盈亏
			double mQnetar_zdj = 0;		// 折单价
			double mQnetar_zje = 0;		// 这金额
			
			String mStd_ht="";			//合同硫分
			double mStd_kf=0;			//供方热量
			double mStd_cf=0;			//厂方热量
			double mStd_js=0;			//结算热量
			double mStd_yk = 0;			// 厂方盈亏
			double mStd_zdj = 0;		// 折单价
			double mStd_zje = 0;		// 这金额
			
			String mAd_ht="";			//合同硫分
			double mAd_kf=0;			//供方热量
			double mAd_cf=0;			//厂方热量
			double mAd_js=0;			//结算热量
			double mAd_yk = 0;			// 厂方盈亏
			double mAd_zdj = 0;			// 折单价
			double mAd_zje = 0;			// 这金额
			
			String mVdaf_ht="";			//合同硫分
			double mVdaf_kf=0;			//供方热量
			double mVdaf_cf=0;			//厂方热量
			double mVdaf_js=0;			//结算热量
			double mVdaf_yk = 0;		// 厂方盈亏
			double mVdaf_zdj = 0;		// 折单价
			double mVdaf_zje = 0;		// 这金额
			
			String mMt_ht="";			//合同硫分
			double mMt_kf=0;			//供方热量
			double mMt_cf=0;			//厂方热量
			double mMt_js=0;			//结算热量
			double mMt_yk = 0;			// 厂方盈亏
			double mMt_zdj = 0;			// 折单价
			double mMt_zje = 0;			// 这金额
			
			String mQgrad_ht="";		//合同硫分
			double mQgrad_kf=0;			//供方热量
			double mQgrad_cf=0;			//厂方热量
			double mQgrad_js=0;			//结算热量
			double mQgrad_yk = 0;		// 厂方盈亏
			double mQgrad_zdj = 0;		// 折单价
			double mQgrad_zje = 0;		// 这金额
			
			String mQbad_ht="";			//合同硫分
			double mQbad_kf=0;			//供方热量
			double mQbad_cf=0;			//厂方热量
			double mQbad_js=0;			//结算热量
			double mQbad_yk = 0;		// 厂方盈亏
			double mQbad_zdj = 0;		// 折单价
			double mQbad_zje = 0;		// 这金额
			
			String mHad_ht="";			//合同硫分
			double mHad_kf=0;			//供方热量
			double mHad_cf=0;			//厂方热量
			double mHad_js=0;			//结算热量
			double mHad_yk = 0;			// 厂方盈亏
			double mHad_zdj = 0;		// 折单价
			double mHad_zje = 0;		// 这金额
			
			String mStad_ht="";			//合同硫分
			double mStad_kf=0;			//供方热量
			double mStad_cf=0;			//厂方热量
			double mStad_js=0;			//结算热量
			double mStad_yk = 0;		// 厂方盈亏
			double mStad_zdj = 0;		// 折单价
			double mStad_zje = 0;		// 这金额
			
			String mStar_ht="";			//合同硫分
			double mStar_kf=0;			//供方热量
			double mStar_cf=0;			//厂方热量
			double mStar_js=0;			//结算热量
			double mStar_yk = 0;		// 厂方盈亏
			double mStar_zdj = 0;		// 折单价
			double mStar_zje = 0;		// 这金额
			
			String mMad_ht="";			//合同硫分
			double mMad_kf=0;			//供方热量
			double mMad_cf=0;			//厂方热量
			double mMad_js=0;			//结算热量
			double mMad_yk = 0;			// 厂方盈亏
			double mMad_zdj = 0;		// 折单价
			double mMad_zje = 0;		// 这金额
			
			String mAar_ht="";			//合同硫分
			double mAar_kf=0;			//供方热量
			double mAar_cf=0;			//厂方热量
			double mAar_js=0;			//结算热量
			double mAar_yk = 0;			// 厂方盈亏
			double mAar_zdj = 0;		// 折单价
			double mAar_zje = 0;		// 这金额
			
			String mAad_ht="";			//合同硫分
			double mAad_kf=0;			//供方热量
			double mAad_cf=0;			//厂方热量
			double mAad_js=0;			//结算热量
			double mAad_yk = 0;			// 厂方盈亏
			double mAad_zdj = 0;		// 折单价
			double mAad_zje = 0;		// 这金额
			
			String mVad_ht="";			//合同硫分
			double mVad_kf=0;			//供方热量
			double mVad_cf=0;			//厂方热量
			double mVad_js=0;			//结算热量
			double mVad_yk = 0;			// 厂方盈亏
			double mVad_zdj = 0;		// 折单价
			double mVad_zje = 0;		// 这金额
			
			String mT2_ht="";			//合同硫分
			double mT2_kf=0;			//供方热量
			double mT2_cf=0;			//厂方热量
			double mT2_js=0;			//结算热量
			double mT2_yk = 0;			// 厂方盈亏
			double mT2_zdj = 0;			// 折单价
			double mT2_zje = 0;			// 这金额
			
			String mYunju_ht="";		//合同运距
			double mYunju_kf=0;			//供方热量
			double mYunju_cf=0;			//厂方热量
			double mYunju_js=0;			//结算热量
			double mYunju_yk = 0;		// 厂方盈亏
			double mYunju_zdj = 0;		// 折单价
			double mYunju_zje = 0;		// 这金额
			
			long mhetb_id=0; 
			double mkoud_js=0;
			double mjiessl = 0;
			double myunfjsl = 0;
			double mbuhsdj = 0;
			double mhansdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.13;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mtielzf = 0;
			double mkuangqyf=0;
			double mkuangqzf=0;
			double mkuangqsk=0;
			double mkuangqjk=0;
			double mbukyqyzf = 0;
			double mjiskc = 0;
			double mbuhsyf = 0;
			double myunfsl = 0.07;
			double myunfsk = 0;
			double myunzfhj = 0;
			double mhej = 0;
			String mdaxhj = "";
			String mmeikhjdx = "";
			String myunzfhjdx = "";
			String mbeiz = "";
			String mranlbmjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mranlbmjbrq = new Date();
			double mkuidjf = 0;
			double mjingz = 0;
			Date mjiesrq = new Date();
			String mchangcwjbr = "";
			Date mchangcwjbrq = null;
			Date mruzrq = null;
			String mjieszxjbr = "";
			Date mjieszxjbrq = null;
			String mgongsrlbjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mgongsrlbjbrq = new Date();
			double mhetjg = 0;
			long mjieslx = 3;
			double myuns = 0;
			String mjiesslblxsw="0";
			String myunsfs = "";
			String mdiancjsbs = "";
			String mstrJieszb="";
			boolean blnHasMeik=false;
			Money mn=new Money();
			Jiesdcz jsdcz=new Jiesdcz();
			double mjiesslcy=0;
			long myunsfsb_id=0;
			double myingd=0;
			double mkuid=0;
			double myunju=0;		//运距
			double mfengsjj=0;		//分公司加价
			double mjiajqdj=0;		//加价前单价
			int mjijlx=0;			//基价类型
			String mMjtokcalxsclfs="";	//兆焦转大卡小数处理方式
			
//			进行单批次结算时，要将每一个批次的结算情况保存起来，存入danpcjsmxb中，此时就产生了id
//			结算时要判断有无这个id，如果有就一定要用这个id
			long mMeikjsb_id=0;
			long mYunfjsb_id=0;
			long mJihkjb_id=0;
			
			String jiesmkid = "";
			String jiesyfbid = "";
			String xiaoshtID = "";
			String caightID = "";
			double mchaokdl=0;   	//超亏吨量
			String mchaokdlx="";	//超亏吨类型
			
			String sql_jj = ""; 	//查询销售合同的分公司加价
			String hetbh = "";		//传递的销售合同编号	
			double fengsjj = 0;		//分公司加价
			double daoczhj = 0;		//到厂综合价
			
			double jiasje = 0;
			double jiakhj = 0;
			double jiaksk = 0;
			double jiakje = 0;
			double buhsdj = 0;
			
			String bianmlx = "";
			
			String[] raw1=visit.getString3().split(";");
			for(int i=0;i<raw1.length;i++){
				String[] raw2=raw1[i].split(","); //分别获取 kuangfjsmkbid kuangfjsyfbid xiaosht caight
				
				jiesmkid=raw2[0];
				jiesyfbid=raw2[1];
				xiaoshtID = raw2[2];
				caightID = raw2[3];
//				if(raw2.length>=2){
//					
//				}
			}
			
			if(visit.getString5().equals("销售")||visit.getString5().equals("销售采购")){
				sql_jj = " select min(jg.fengsjj) as fengsjj from hetb h,hetjgb jg where jg.hetb_id = h.id and h.leib=1 and h.id = '"+xiaoshtID+"'\n";
				bianmlx = "-XS";
			}else if(visit.getString5().equals("采购")){
				sql_jj = " select min(jg.fengsjj) as fengsjj from hetb h,hetjgb jg where jg.hetb_id = h.id and h.leib=0 and h.id = '"+caightID+"'\n";
				bianmlx = "-CG";
			}
			
			String sql="select * from kuangfjsmkb where id='"+jiesmkid+"'";
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				mid = rs.getLong("id");
				mtianzdw =rs.getString("xianshr");
				mjiesbh = rs.getString("bianm")+bianmlx;
				mdiancjsbs=mjiesbh.substring(0,mjiesbh.indexOf("-")+1);
				mfahdw = rs.getString("gongysmc");
				mfaz =rs.getString("faz");
				mshoukdw = rs.getString("shoukdw");
				mfahksrq = rs.getDate("fahksrq");
				mfahjzrq = rs.getDate("fahjzrq");
				myansksrq = rs.getDate("yansksrq");
				myansjzrq =rs.getDate("yansjzrq");
				mkaihyh =rs.getString("kaihyh");
				mpinz = rs.getString("meiz");
				myuanshr = rs.getString("yuanshr");
				mzhangh = rs.getString("zhangh");
				mches = rs.getInt("ches");
				mxianshr =rs.getString("xianshr");
				mdaibcc = rs.getString("daibch");
				myansbh =rs.getString("yansbh");
				mduifdd = rs.getString("duifdd");
				mfukfs = rs.getString("fukfs");
				mjiessl = rs.getDouble("jiessl");
				mbuhsdj = rs.getDouble("buhsdj");
				mjiakje = rs.getDouble("meikje");
				mbukyqjk = rs.getDouble("bukmk");
				mjiaksl = rs.getDouble("shuil");
				mjiasje = rs.getDouble("hansmk");
//				mjiasje = rs.getDouble("hansdj")*rs.getDouble("jiessl")+rs.getDouble("bukmk");//含税煤款（两位小数）=含税单价×结算数量+补扣款
				mjiakhj = rs.getDouble("buhsmk");
//				mjiakhj = mjiasje/(1+mjiaksl);												  //不含税煤款（两位小数）=含税煤款/（1+税率）
				mjiaksk = rs.getDouble("shuik");
//				mjiaksk = mjiasje - mjiakhj;												  //税款（两位小数）=含税煤款-不含税煤款
				mjiakje = rs.getDouble("meikje");
//				mjiakje = rs.getDouble("hansdj")*rs.getDouble("jiessl")/(1+mjiaksl);		  //金额（两位小数）=含税单价×结算数量/（1+税率）
				mbuhsdj = rs.getDouble("buhsdj");
//				mbuhsdj = rs.getDouble("hansdj")/(1+mjiaksl);								  //不含税单价（七位小数）=含税单价/（1+税率）
				mhansdj = rs.getDouble("hansdj");
				mjieslx = rs.getInt("jieslx");
				mjiesslcy=rs.getDouble("jiesslcy");
				mdiancxxb_id=rs.getLong("diancxxb_id");
				mgongysb_id=rs.getLong("gongysb_id");
				mkoud_js=rs.getDouble("koud");
				myunsfsb_id=rs.getLong("yunsfsb_id");
				myingd=rs.getDouble("yingd");
				mkuid=rs.getDouble("kuid");
				myunju=rs.getDouble("yunj");
				mhetb_id=rs.getLong("HETB_ID");
				blnHasMeik=true;
				mfapbh=rs.getString("fapbh");
				mfengsjj=rs.getDouble("fengsjj");
				mjiajqdj=rs.getDouble("jiajqdj");
				mjijlx=rs.getInt("jijlx");
				mchaokdl=Math.abs(rs.getDouble("chaokdl"));		//超亏吨量
				mchaokdlx=Jiesdcz.nvlStr(rs.getString("chaokdlx"));		//超亏吨类型
				
				
				ResultSet rs_xsjj=con.getResultSet(sql_jj);
				if(rs_xsjj.next()){
					  fengsjj = rs_xsjj.getDouble("fengsjj");
				}  
				
				sql = "select jieszbsjb.*,zhibb.bianm from jieszbsjb,kuangfjsmkb,zhibb "
					+ " where jieszbsjb.jiesdid=kuangfjsmkb.id and zhibb.id=jieszbsjb.zhibb_id"
					+ " and kuangfjsmkb.id='"
					+ jiesmkid
					+ "' and jieszbsjb.zhuangt=1 ";
				
				ResultSet rs2=con.getResultSet(sql);
					
				while(rs2.next()){
					
					if(rs2.getString("bianm").equals(Locale.jiessl_zhibb)){
						
						mhetsl = rs2.getString("hetbz");
						mgongfsl =rs2.getDouble("gongf") ;
						mshulzjbz =rs2.getDouble("zhejbz");
						myanssl = rs2.getDouble("changf");
						myingksl = rs2.getDouble("yingk");
						mshulzjje=rs2.getDouble("zhejje");
						
					}else if(rs2.getString("bianm").equals(Locale.Qnetar_zhibb)){
						
						mQnetar_ht = rs2.getString("hetbz");// 合同热量
						mQnetar_kf = rs2.getDouble("gongf");// 供方热量
						mQnetar_cf = rs2.getDouble("changf");
						mQnetar_js = rs2.getDouble("jies");// 结算热量
						mQnetar_yk = rs2.getDouble("yingk");
						mQnetar_zdj = rs2.getDouble("zhejbz");
						mQnetar_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Qnetar(kcal/kg)","Qnetar_ht","Qnetar_kf","Qnetar_cf","Qnetar_js","Qnetar_yk","Qnetar_zdj","Qnetar_zje",
								mQnetar_ht,MainGlobal.Mjkg_to_kcalkg(mQnetar_kf,0),MainGlobal.Mjkg_to_kcalkg(mQnetar_cf,0),
								MainGlobal.Mjkg_to_kcalkg(mQnetar_js,0),mQnetar_yk,mQnetar_zdj,mQnetar_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Std_zhibb)){
						
						mStd_ht = rs2.getString("hetbz");	// 合同硫分
						mStd_kf = rs2.getDouble("gongf");
						mStd_cf =rs2.getDouble("changf");
						mStd_js = rs2.getDouble("jies");	// 结算硫分
						mStd_yk = rs2.getDouble("yingk");
						mStd_zdj = rs2.getDouble("zhejbz");
						mStd_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Std(%)","Std_ht","Std_kf","Std_cf","Std_js","Std_yk","Std_zdj","Std_zje",
								mStd_ht,mStd_kf,mStd_cf,mStd_js,mStd_yk,mStd_zdj,mStd_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Star_zhibb)){
						
						mStar_ht = rs2.getString("hetbz");	// 合同挥发分
						mStar_kf = rs2.getDouble("gongf");
						mStar_cf = rs2.getDouble("changf");
						mStar_js = rs2.getDouble("jies");		// 结算挥发分
						mStar_yk = rs2.getDouble("yingk");
						mStar_zdj = rs2.getDouble("zhejbz");
						mStar_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Star(%)","Star_ht","Star_kf","Star_cf","Star_js","Star_yk","Star_zdj","Star_zje",
								mStar_ht,mStar_kf,mStar_cf,mStar_js,mStar_yk,mStar_zdj,mStar_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Ad_zhibb)){
						
						mAd_ht = rs2.getString("hetbz");	// 合同挥发分
						mAd_kf = rs2.getDouble("gongf");
						mAd_cf = rs2.getDouble("changf");
						mAd_js = rs2.getDouble("jies");		// 结算挥发分
						mAd_yk = rs2.getDouble("yingk");
						mAd_zdj = rs2.getDouble("zhejbz");
						mAd_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Ad(%)","Ad_ht","Ad_kf","Ad_cf","Ad_js","Ad_yk","Ad_zdj","Ad_zje",
								mAd_ht,mAd_kf,mAd_cf,mAd_js,mAd_yk,mAd_zdj,mAd_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Vdaf_zhibb)){
						
						mVdaf_ht = rs2.getString("hetbz");	// 合同挥发分
						mVdaf_kf = rs2.getDouble("gongf");
						mVdaf_cf = rs2.getDouble("changf");
						mVdaf_js = rs2.getDouble("jies");		// 结算挥发分
						mVdaf_yk = rs2.getDouble("yingk");
						mVdaf_zdj = rs2.getDouble("zhejbz");
						mVdaf_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Vdaf(%)","Vdaf_ht","Vdaf_kf","Vdaf_cf","Vdaf_js","Vdaf_yk","Vdaf_zdj","Vdaf_zje",
								mVdaf_ht,mVdaf_kf,mVdaf_cf,mVdaf_js,mVdaf_yk,mVdaf_zdj,mVdaf_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Mt_zhibb)){
						
						mMt_ht = rs2.getString("hetbz");	// 合同挥发分
						mMt_kf = rs2.getDouble("gongf");
						mMt_cf = rs2.getDouble("changf");
						mMt_js = rs2.getDouble("jies");		// 结算挥发分
						mMt_yk = rs2.getDouble("yingk");
						mMt_zdj = rs2.getDouble("zhejbz");
						mMt_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Mt(%)","Mt_ht","Mt_kf","Mt_cf","Mt_js","Mt_yk","Mt_zdj","Mt_zje",
								mMt_ht,mMt_kf,mMt_cf,mMt_js,mMt_yk,mMt_zdj,mMt_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Qgrad_zhibb)){
						
						mQgrad_ht = rs2.getString("hetbz");		// 合同挥发分
						mQgrad_kf = rs2.getDouble("gongf");
						mQgrad_cf = rs2.getDouble("changf");
						mQgrad_js = rs2.getDouble("jies");		// 结算挥发分
						mQgrad_yk = rs2.getDouble("yingk");
						mQgrad_zdj = rs2.getDouble("zhejbz");
						mQgrad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Qgrad(kcal/kg)","Qgrad_ht","Qgrad_kf","Qgrad_cf","Qgrad_js","Qgrad_yk","Qgrad_zdj","Qgrad_zje",
								mQgrad_ht,MainGlobal.Mjkg_to_kcalkg(mQgrad_kf,0),MainGlobal.Mjkg_to_kcalkg(mQgrad_cf,0),
								MainGlobal.Mjkg_to_kcalkg(mQgrad_js,0),mQgrad_yk,mQgrad_zdj,mQgrad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Qbad_zhibb)){
						
						mQbad_ht = rs2.getString("hetbz");		// 合同挥发分
						mQbad_kf = rs2.getDouble("gongf");
						mQbad_cf = rs2.getDouble("changf");
						mQbad_js = rs2.getDouble("jies");		// 结算挥发分
						mQbad_yk = rs2.getDouble("yingk");
						mQbad_zdj = rs2.getDouble("zhejbz");
						mQbad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Qbad(kcal/kg)","Qbad_ht","Qbad_kf","Qbad_cf","Qbad_js","Qbad_yk","Qbad_zdj","Qbad_zje",
								mQbad_ht,MainGlobal.Mjkg_to_kcalkg(mQbad_kf,0),MainGlobal.Mjkg_to_kcalkg(mQbad_cf,0),
								MainGlobal.Mjkg_to_kcalkg(mQbad_js,0),mQbad_yk,mQbad_zdj,mQbad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Had_zhibb)){
						
						mHad_ht = rs2.getString("hetbz");		// 合同挥发分
						mHad_kf = rs2.getDouble("gongf");
						mHad_cf = rs2.getDouble("changf");
						mHad_js = rs2.getDouble("jies");		// 结算挥发分
						mHad_yk = rs2.getDouble("yingk");
						mHad_zdj = rs2.getDouble("zhejbz");
						mHad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Had(%)","Had_ht","Had_kf","Had_cf","Had_js","Had_yk","Had_zdj","Had_zje",
								mHad_ht,mHad_kf,mHad_cf,mHad_js,mHad_yk,mHad_zdj,mHad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Stad_zhibb)){
						
						mStad_ht = rs2.getString("hetbz");		// 合同挥发分
						mStad_kf = rs2.getDouble("gongf");
						mStad_cf = rs2.getDouble("changf");
						mStad_js = rs2.getDouble("jies");		// 结算挥发分
						mStad_yk = rs2.getDouble("yingk");
						mStad_zdj = rs2.getDouble("zhejbz");
						mStad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Stad(%)","Stad_ht","Stad_kf","Stad_cf","Stad_js","Stad_yk","Stad_zdj","Stad_zje",
								mStad_ht,mStad_kf,mStad_cf,mStad_js,mStad_yk,mStad_zdj,mStad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Mad_zhibb)){
						
						mMad_ht = rs2.getString("hetbz");	// 合同挥发分
						mMad_kf = rs2.getDouble("gongf");
						mMad_cf = rs2.getDouble("changf");
						mMad_js = rs2.getDouble("jies");		// 结算挥发分
						mMad_yk = rs2.getDouble("yingk");
						mMad_zdj = rs2.getDouble("zhejbz");
						mMad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Mad(%)","Mad_ht","Mad_kf","Mad_cf","Mad_js","Mad_yk","Mad_zdj","Mad_zje",
								mMad_ht,mMad_kf,mMad_cf,mMad_js,mMad_yk,mMad_zdj,mMad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Aar_zhibb)){
						
						mAar_ht = rs2.getString("hetbz");	// 合同挥发分
						mAar_kf = rs2.getDouble("gongf");
						mAar_cf = rs2.getDouble("changf");
						mAar_js = rs2.getDouble("jies");		// 结算挥发分
						mAar_yk = rs2.getDouble("yingk");
						mAar_zdj = rs2.getDouble("zhejbz");
						mAar_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Aar(%)","Aar_ht","Aar_kf","Aar_cf","Aar_js","Aar_yk","Aar_zdj","Aar_zje",
								mAar_ht,mAar_kf,mAar_cf,mAar_js,mAar_yk,mAar_zdj,mAar_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Aad_zhibb)){
						
						mAad_ht = rs2.getString("hetbz");	// 合同挥发分
						mAad_kf = rs2.getDouble("gongf");
						mAad_cf = rs2.getDouble("changf");
						mAad_js = rs2.getDouble("jies");		// 结算挥发分
						mAad_yk = rs2.getDouble("yingk");
						mAad_zdj = rs2.getDouble("zhejbz");
						mAad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Aad(%)","Aad_ht","Aad_kf","Aad_cf","Aad_js","Aad_yk","Aad_zdj","Aad_zje",
								mAad_ht,mAad_kf,mAad_cf,mAad_js,mAad_yk,mAad_zdj,mAad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Vad_zhibb)){
						
						mVad_ht = rs2.getString("hetbz");	// 合同挥发分
						mVad_kf = rs2.getDouble("gongf");
						mVad_cf = rs2.getDouble("changf");
						mVad_js = rs2.getDouble("jies");		// 结算挥发分
						mVad_yk = rs2.getDouble("yingk");
						mVad_zdj = rs2.getDouble("zhejbz");
						mVad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Vad(%)","Vad_ht","Vad_kf","Vad_cf","Vad_js","Vad_yk","Vad_zdj","Vad_zje",
								mVad_ht,mVad_kf,mVad_cf,mVad_js,mVad_yk,mVad_zdj,mVad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.T2_zhibb)){
						
						mT2_ht = rs2.getString("hetbz");	// 合同挥发分
						mT2_kf = rs2.getDouble("gongf");
						mT2_cf = rs2.getDouble("changf");
						mT2_js = rs2.getDouble("jies");		// 结算挥发分
						mT2_yk = rs2.getDouble("yingk");
						mT2_zdj = rs2.getDouble("zhejbz");
						mT2_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("T2(%)","T2_ht","T2_kf","T2_cf","T2_js","T2_yk","T2_zdj","T2_zje",
								mT2_ht,mT2_kf,mT2_cf,mT2_js,mT2_yk,mT2_zdj,mT2_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Yunju_zhibb)){
						
						mYunju_ht = rs2.getString("hetbz");	// 合同挥发分
						mYunju_kf = rs2.getDouble("gongf");
						mYunju_cf = rs2.getDouble("changf");
						mYunju_js = rs2.getDouble("jies");		// 结算挥发分
						mYunju_yk = rs2.getDouble("yingk");
						mYunju_zdj = rs2.getDouble("zhejbz");
						mYunju_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("运距(Km)","Yunju_ht","Yunju_kf","Yunju_cf","Yunju_js","Yunju_yk","Yunju_zdj","Yunju_zje",
								mYunju_ht,mYunju_kf,mYunju_cf,mYunju_js,mYunju_yk,mYunju_zdj,mYunju_zje);
					
					}
					
					this.setJieszb(mstrJieszb);
				}
				
				rs2.close();
				mbeiz = rs.getString("beiz");
//				double mdanjc = 0;
//				待定
				mranlbmjbr = rs.getString("ranlbmjbr");
				mranlbmjbrq =rs.getDate("ranlbmjbrq");
//				mkuidjf = 0;
				mjingz = rs.getDouble("guohl");
				mjiesrq =rs.getDate("jiesrq");
				mruzrq =rs.getDate("ruzrq");
				
				mjiesslblxsw=Jiesdcz.getJiessz_item(mdiancxxb_id, mgongysb_id, mhetb_id, Locale.jiesslblxsw_jies, mjiesslblxsw);
				if(mjiesslblxsw.equals("0")){
					
					myuns=Math.round((mjiessl-mjingz));
				}else{
					
					String strblxs="";
					for(int i=0;i<Integer.parseInt(mjiesslblxsw);i++){
						
						if(i==0){
							
							strblxs="10";
						}else{
							
							strblxs+="0";
						}
					}
					myuns=Math.round((mjiessl-mjingz)*Double.parseDouble(strblxs))/Double.parseDouble(strblxs);
				}
				
			}
//			煤款
;
//			 1, 两票结算;
//			 2, 煤款结算
//			 3, 国铁运费
//			 4, 地铁运费
			if(blnHasMeik&&mjieslx==1){
//				两票		运费
				
				sql="select * from kuangfjsyfb where id='"+jiesyfbid+"'";
				rs=con.getResultSet(sql);
				if(rs.next()){
					myid=rs.getLong("id");
					mtielyf=rs.getDouble("guotyf");
					mtielzf=rs.getDouble("guotzf");
					mbukyqyzf = rs.getDouble("bukyf");
					mjiskc=rs.getDouble("jiskc");
					mbuhsyf=rs.getDouble("buhsyf");
					myunfsl=rs.getDouble("shuil");
					myunfsk=rs.getDouble("shuik");
					myunzfhj=rs.getDouble("hansyf");
					myunfjsl=rs.getDouble("jiessl");
					mkuangqyf=rs.getDouble("kuangqyf");
					mkuangqzf=rs.getDouble("kuangqzf");
					daoczhj = mhansdj + (myunzfhj-mbukyqyzf)/myunfjsl;  //到厂综合价=煤款含税单价+（含税运费-补扣以前运费）/运费结算数量）
					shulzjbz = daoczhj + fengsjj;						//当分公司采购单结算类型为两票结算时  含税单价=先求出到厂综合价（保留两位小数）+分公司加价
					jiasje = shulzjbz*mjiessl+mbukyqjk;					//含税煤款（两位小数）=含税单价×结算数量+补扣款
					jiakhj = jiasje/(1+mjiaksl);						//不含税煤款（两位小数）=含税煤款/（1+税率）
					jiaksk = jiasje - jiakhj;							//税款（两位小数）=含税煤款-不含税煤款
					jiakje = shulzjbz*mjiessl/(1+mjiaksl);		  		//金额（两位小数）=含税单价×结算数量/（1+税率）
					buhsdj = shulzjbz/(1+mjiaksl);						//不含税单价（七位小数）=含税单价/（1+税率）
				}
				
			}else if(mjieslx!=2){
				
				sql=" select * from kuangfjsyfb where id='"+jiesyfbid+"'";
				
					rs=con.getResultSet(sql);
					if(rs.next()){
						myid=rs.getLong("id");
						mtianzdw =rs.getString("xianshr");
						mjiesbh = rs.getString("bianm");
						mdiancjsbs=mjiesbh.substring(0,mjiesbh.indexOf("-")+1);
						mdiancxxb_id=rs.getLong("diancxxb_id");
						mfahdw = rs.getString("gongysmc");
						mfaz =rs.getString("faz");
						mshoukdw = rs.getString("shoukdw");
						mfahksrq = rs.getDate("fahksrq");
						mfahjzrq = rs.getDate("fahjzrq");
						myansksrq = rs.getDate("yansksrq");
						myansjzrq =rs.getDate("yansjzrq");
						mkaihyh =rs.getString("kaihyh");
						mpinz = rs.getString("meiz");
						myuanshr = rs.getString("yuanshr");
						mgongfsl=rs.getDouble("gongfsl");
						myanssl=rs.getDouble("yanssl");
						myingksl=rs.getDouble("yingk");
						mzhangh = rs.getString("zhangh");
						mches = rs.getInt("ches");
						mxianshr =rs.getString("xianshr");
						mfapbh = rs.getString("fapbh");
						mdaibcc = rs.getString("daibch");
						myansbh =rs.getString("yansbh");
						mduifdd = rs.getString("duifdd");
						mfukfs = rs.getString("fukfs");
						mjiessl = rs.getDouble("jiessl");
						mtielyf=rs.getDouble("guotyf");
						mtielzf=rs.getDouble("guotzf");
						mbukyqyzf = rs.getDouble("bukyf");
						mjiskc=rs.getDouble("jiskc");
						mbuhsyf=rs.getDouble("buhsyf");
						myunfsl=rs.getDouble("shuil");
						myunfsk=rs.getDouble("shuik");
						myunzfhj=rs.getDouble("hansyf");
						mkuangqyf=rs.getDouble("kuangqyf");
						mkuangqzf=rs.getDouble("kuangqzf");
						myunsfsb_id=rs.getLong("yunsfsb_id");
						myingd=rs.getDouble("yingd");
						mkuid=rs.getDouble("kuid");
						myunju=rs.getDouble("yunj");
						mjingz=rs.getDouble("guohl");
						mjiesslcy=rs.getDouble("jiesslcy");
						shulzjbz=mhansdj + fengsjj;							//当分公司采购单结算类型为煤款结算时  含税单价=煤款含税单价+分公司加价
						jiasje = shulzjbz*mjiessl+mbukyqjk;					//含税煤款（两位小数）=含税单价×结算数量+补扣款
						jiakhj = jiasje/(1+mjiaksl);						//不含税煤款（两位小数）=含税煤款/（1+税率）
						jiaksk = jiasje - jiakhj;							//税款（两位小数）=含税煤款-不含税煤款
						jiakje = shulzjbz*mjiessl/(1+mjiaksl);		  		//金额（两位小数）=含税单价×结算数量/（1+税率）
						buhsdj = shulzjbz/(1+mjiaksl);						//不含税单价（七位小数）=含税单价/（1+税率）
					}
			}
			
			if(((Visit) getPage().getVisit()).getDouble2()>0
					||((Visit) getPage().getVisit()).getDouble3()>0){
						
					
					mkuangqyf=((Visit) getPage().getVisit()).getDouble2();
					mkuangqzf=((Visit) getPage().getVisit()).getDouble3();
					mkuangqsk=((Visit) getPage().getVisit()).getDouble4();
					mkuangqjk=((Visit) getPage().getVisit()).getDouble5();
					myunzfhj=Math.round((mtielyf+mtielzf+mkuangqyf+mkuangqzf+mbukyqyzf)*100)/100;
					myunfsk=(double)Math.round(((double)Math.round((mtielyf+mbukyqyzf)*myunfsl*100)/100+((Visit) getPage().getVisit()).getDouble4())*100)/100;
					mbuhsyf=(double)Math.round(((double)Math.round((myunzfhj-myunfsk)*100)/100+((Visit) getPage().getVisit()).getDouble5())*100)/100;
			}	
			
			mmeikhjdx=getDXMoney(mjiasje);
			myunzfhjdx=getDXMoney(myunzfhj);
			mhej=mjiasje+myunzfhj;
			mdaxhj=getDXMoney(mhej);
			
//			设置超/亏吨的显示	
			if(!mchaokdlx.equals("")){
//				说明存在超亏吨
				this.setHejdxh(jsdcz.SetHejdxh(mchaokdlx,mchaokdl,mhej,mdaxhj));
			}else{
				
				this.setHejdxh(jsdcz.SetHejdxh("",0,mhej,mdaxhj));
			}
			
			_editvalues.add(new Kuangfjsbean(mid, myid, mtianzdw, mjiesbh,
					mfahdw, mmeikdw,mfaz, myunsfsb_id, mshoukdw, mfahksrq, mfahjzrq, myansksrq,
					myansjzrq, mkaihyh, mpinz,nvlStr(myuanshr) , mzhangh, mhetsl,
					mgongfsl, mches, mxianshr,nvlStr(mfapbh), mdaibcc,nvlStr(myansbh) ,
					mduifdd,nvlStr(mfukfs) , shulzjbz, myanssl, myingksl,  myingd, mkuid, mshulzjje,
					mjiessl, mjiesslcy,myunfjsl,buhsdj, jiakje,
					mbukyqjk, jiakhj, mjiaksl, jiaksk, jiasje, mtielyf,mtielzf,
					mkuangqyf,mkuangqzf, mkuangqsk, mkuangqjk,mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
					myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj,nvlStr(mbeiz) ,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq,
					mfahrq, mchangcwjbr, mchangcwjbrq, mruzrq,
					mjieszxjbr, mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq,
					mhetjg, mjieslx,myuns,mkoud_js,
					myunsfs, mdiancjsbs,mhetb_id,myunju,mMeikjsb_id,
					mYunfjsb_id,mJihkjb_id,mfengsjj,mjiajqdj,mjijlx,
					mMjtokcalxsclfs,mchaokdl,mchaokdlx));
			
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Kuangfjsbean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}
	
	
	public void setJieszb(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getJieszb(){
		
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	
	public void setYinhxx(){
		JDBCcon con = new JDBCcon();
		String sql = "";
		String mshoukdw = "";
		String mkaihyh = "";
		String mzhangh = "";
		sql = "select distinct h.gongfkhyh,h.gongfzh from hetb h where h.gongfdwmc='" + getShoukdwValue().getValue() + "'\n";
		ResultSet rs = con.getResultSet(sql);	
		try {
			if(rs.next()){
				mshoukdw = getShoukdwValue().getValue();
				mkaihyh = rs.getString("gongfkhyh");
				mzhangh = rs.getString("gongfzh");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			con.Close();
		}
		
		String js = "document.getElementById('SHOUKDW').value = '"+mshoukdw+"';\n" +
					"document.getElementById('KAIHYH').value = '"+mkaihyh+"';\n" +
					"document.getElementById('ZHANGH').value = '"+mzhangh+"';\n";
		setYinhxx(js);
	}
	
	private String _hetxx;


	public void setHetxx(String _value) {
		_hetxx = _value;
	}

	public String getHetxx() {
		if (_hetxx == null) {
			_hetxx = "";
		}
		return _hetxx;
	}
	
	private String _yinhxx;


	public void setYinhxx(String _value) {
		_yinhxx = _value;
	}

	public String getYinhxx() {
		if (_yinhxx == null) {
			_yinhxx = "";
		}
		return _yinhxx;
	}
	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean1(false);// 公用
			visit.setboolean2(false);// 改变电厂结算编号用
			visit.setboolean3(false);// 公用
			visit.setString4("");	//合计大写
			((Visit) getPage().getVisit()).setDropDownBean8(null);
			
			if (visit.getRenyjb() == 1) {// 集团

				setGongsValue(null);// drop1
				setIGongsModel(null);
				getIGongsModels();
			}
			
			visit.setString5("");
			String[] raw1=visit.getString3().split(",");
			String xiaosht = raw1[2];
			String caight = raw1[3];
			if(!xiaosht.equals("-1")){
				if(!caight.equals("-1")){
					visit.setString5("销售采购");
				}else{
					visit.setString5("销售");
				}
			}else{
				visit.setString5("采购");
			}
			
			visit.setInt2(0);//保存后变为1
			visit.setInt3(0);//提交流程后变为1

			setFahdwValue(null);// drop2
			setIFahdwModel(null);
			getIFahdwModels();

			setFazValue(null);// drop3
			setIFazModel(null);
			getIFazModels();

			setRanlpzValue(null);// drop4
			setIRanlpzModel(null);
			getIRanlpzModels();

			setShoukdwValue(null);// drop5
			setIShoukdwModel(null);
			getIShoukdwModels();

			setJiesbhValue(null);// drop6(厂方)
			setIJiesbhModel(null);
			getIJiesbhModels();

			setDiancmcValue(null);// drop7
			setIDiancmcModel(null);//
			getIDiancmcModels();

//			setHetbhValue(null);// drop8
//			setIHetbhModel(null);//
//			getIHetbhModels();

			setZhibbmValue(null);// drop9
			setIZhibbmModel(null);//
			getIZhibbmModels();

			setKuangfjsmkbhValue(null);// drop10
			setIKuangfjsmkbhModel(null);
			getIKuangfjsmkbhModels();

			setLiucmcValue(null);// drop11
			setILiucmcModel(null);
			getILiucmcModels();
			
			getSelectData();
		}else if(((Visit) getPage().getVisit()).getboolean3()==true){
			setYinhxx();
		}
		

		if (visit.getboolean2()) {
			
		}
		getToolbars();
		setTijldkf("true");
	}
	
	private void getToolbars() {
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		//返回
		ToolbarButton quxbt = new ToolbarButton(null, "返回",
				"function(){ document.Form0.ReturnButton.click();}");
		quxbt.setId("fanhbt");
		tb1.addItem(quxbt);
		tb1.addText(new ToolbarText("-"));
		
		//保存
		if(visit.getInt2()==0){
			ToolbarButton save = new ToolbarButton(null, "保存","function(){ document.Form0.SaveButton.click();}");
			save.setId("baocbt");
			tb1.addItem(save);
			tb1.addText(new ToolbarText("-"));
		}
		
		if(visit.getInt2()==1&&visit.getInt3()==0){
//			提交进入流程
			ToolbarButton submitbt=new ToolbarButton(null,"提交进入流程","function(){  \n"
					+ " if(!win){	\n" 
					+ "	\tvar form = new Ext.form.FormPanel({	\n" 
					+ " \tbaseCls: 'x-plain',	\n" 		
					+ " \tlabelAlign:'right',	\n" 
					+ " \tdefaultType: 'textfield',	\n"
					+ " \titems: [{		\n"
					+ " \txtype:'fieldset',	\n"
					+ " \ttitle:'请选择流程名称',	\n"
					+ " \tautoHeight:false,	\n"
					+ " \theight:220,	\n"
					+ " \titems:[	\n"
	        		+ " \tlcmccb=new Ext.form.ComboBox({	\n" 
					+ " \twidth:150,	\n"
					+ " \tid:'lcmccb',	\n"
					+ " \tselectOnFocus:true,	\n"
					+ "	\ttransform:'LiucmcDropDown',	\n"						
					+ " \tlazyRender:true,	\n"	
					+ " \tfieldLabel:'流程名称',		\n" 
					+ " \ttriggerAction:'all',	\n"
					+ " \ttypeAhead:true,	\n"	
					+ " \tforceSelection:true,	\n"
					+ " \teditable:false	\n"					
					+ " \t})	\n"
					+ " \t]		\n"
					+ " \t}]	\n"		
					+ " \t});	\n"
					+ " \twin = new Ext.Window({	\n"
					+ " \tel:'hello-win',	\n"
					+ " \tlayout:'fit',	\n"
					+ " \twidth:500,	\n"	
					+ " \theight:300,	\n"
					+ " \tcloseAction:'hide',	\n"
					+ " \tplain: true,	\n"
					+ " \ttitle:'流程',	\n"
					+ " \titems: [form],	\n"
					+ " \tbuttons: [{	\n"
					+ " \ttext:'确定',	\n"
					+ " \thandler:function(){	\n"  
					+ " \twin.hide();	\n"
					+ " \tif(lcmccb.getRawValue()=='请选择'){		\n" 
					+ "	\t	alert('请选择流程名称！');		\n"
					+ " \t}else{" 
					+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
					+ " \t\t document.all.item('QuedButton').click();	\n"
					+ " \t}	\n"
					+ " \t}	\n"   
					+ " \t},{	\n"
					+ " \ttext: '取消',	\n"
					+ " \thandler: function(){	\n"
					+ " \twin.hide();	\n"
					+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE').value='';	\n"	
					+ " \t}		\n"
					+ " \t}]	\n"
					+ " \t});}	\n" 
					+ " \twin.show(this);	\n"

					+ " \tif(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value!=''){	\n"	
//					+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"	
					+ " \t}	\n"	
					+ " \t}");
			submitbt.setId("submitbt");
			tb1.addItem(submitbt);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		if(visit.getInt2()==1&&visit.getString5().equals("销售采购")){
//			电厂采购单
			ToolbarButton cgbt = new ToolbarButton(null, "电厂采购单","function(){ document.Form0.CaigButton.click();}");
			cgbt.setId("caigbt");
			tb1.addItem(cgbt);
		}

		
		setToolbar(tb1);
	}
	
//	 工具条_begin

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private void Tianzdwtb() {

		String tianzdw = getDiancmcValue().getValue();
		if (!tianzdw.equals("请选择")) {

			((Kuangfjsbean) getEditValues().get(0)).setYuanshr(tianzdw);
			((Kuangfjsbean) getEditValues().get(0)).setXianshr(tianzdw);
		}

	}

	// 收款单位

	public IDropDownBean getShoukdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {

			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getIShoukdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setShoukdwValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setboolean3(false);
		if (((Visit) getPage().getVisit()).getDropDownBean5() != value) {
			((Visit) getPage().getVisit()).setboolean3(true);
			((Visit) getPage().getVisit()).setDropDownBean5(value);
		}
	}

	public void setIShoukdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getIShoukdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getIShoukdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIShoukdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			String sql = "select distinct gongfdwmc from hetb h where leib=2 order by gongfdwmc";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("gongfdwmc")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	// 填制单位、电厂名称

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getIDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setDiancmcValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean7() != value) {
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean7(value);
		}
	}

	public void setIDiancmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

			getIDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public IPropertySelectionModel getIDiancmcModels() {

		String sql = "select id,quanc from diancxxb";
		String where = "";

		if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

			where = " where id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " order by quanc";
		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			where = " where id in (select id from diancxxb where fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ") order by quanc";
		} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			where = " where jib=3 order by quanc ";
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(sql + where, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}


	// 指标表编码Model

	public IDropDownBean getZhibbmValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getIZhibbmModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setZhibbmValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean9(value);
	}

	public void setIZhibbmModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getIZhibbmModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getIZhibbmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public IPropertySelectionModel getIZhibbmModels() {
		String sql = "select id,mingc from zhibb where leib=1 order by bianm";
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	//

	// 关联矿方结算的编号
	public IDropDownBean getKuangfjsmkbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getIKuangfjsmkbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setKuangfjsmkbhValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean10(value);
	}

	public void setIKuangfjsmkbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getIKuangfjsmkbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getIKuangfjsmkbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getIKuangfjsmkbhModels() {

		String sql = "";

		if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

			sql = "select id,bianm from kuangfjsmkb where diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " order by bianm";

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			sql = "select id,bianm from kuangfjsmkb where diancxxb_id in (select id from diancxxb where fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ") order by bianm";
		} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			sql = "select id,bianm from kuangfjsmkb  order by bianm";
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	//

	// 流程名称
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean11() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getILiucmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean11();
	}

	public void setLiucmcValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean11() != value) {

			((Visit) getPage().getVisit()).setDropDownBean11(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {

			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	public IPropertySelectionModel getILiucmcModels() {

		String sql = "select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='结算' order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel11(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	// 所属公司
	public IDropDownBean getGongsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIGongsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setGongsValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean1(value);
	}

	public void setIGongsModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIGongsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getIGongsModels();
		}

		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIGongsModels() {

		String sql = "select id,mingc from diancxxb where jib=2 order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "全部"));

		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	// end

	// 发货单位
	public IDropDownBean getFahdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getIFahdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setFahdwValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setIFahdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIFahdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIFahdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIFahdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		String sql = "";
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
//			if (((Visit) getPage().getVisit()).getRenyjb() == 3) {
//
//				sql = " select gongysmc from "
//						+ " (select distinct gongysmc from diancjsmkb where diancxxb_id="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ " and liucztb_id=0)"
//						+ " union"
//						+ " (select distinct gongysmc from diancjsyfb where diancxxb_id="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ " and liucztb_id=0) order by gongysmc";
//
//			} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {
//
//				sql = " select gongysmc from "
//						+ " (select distinct gongysmc from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ " and liucztb_id=0)"
//						+ " union"
//						+ " (select distinct gongysmc from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ " and liucztb_id=0) order by gongysmc";
//
//			} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {
//
//				sql = "select gongysmc from "
//						+ " (select distinct gongysmc from diancjsmkb where liucztb_id=0)"
//						+ " union"
//						+ " (select distinct gongysmc from diancjsyfb where liucztb_id=0) order by gongysmc";
//			}
			sql = " select quanc from gongysb order by quanc";

			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("quanc")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	// end

	// 发站
	public IDropDownBean getFazValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIFazModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setFazValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setIFazModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIFazModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getIFazModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getIFazModels() {

		String sql = "";
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {
			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

				sql = " select faz from "
						+ " (select distinct faz from diancjsmkb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct faz from diancjsyfb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by faz";
			} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

				sql = " select faz from "
						+ " (select distinct faz from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct faz from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by faz";

			} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

				sql = "select faz from "
						+ " (select distinct faz from diancjsmkb where liucztb_id=0)"
						+ " union"
						+ " (select distinct faz from diancjsyfb where liucztb_id=0) order by faz";
			}

			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("faz")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	// end

	// 品种
	public IDropDownBean getRanlpzValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getIRanlpzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setRanlpzValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean4(value);
	}

	public void setIRanlpzModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getIRanlpzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getIRanlpzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getIRanlpzModels() {

		String sql = "";
		List List = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

				sql = " select meiz from "
						+ " (select distinct meiz from diancjsmkb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct meiz from diancjsyfb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by meiz";
			} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

				sql = " select meiz from "
						+ " (select distinct meiz from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct meiz from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by meiz";

			} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

				sql = "select meiz from "
						+ " (select distinct meiz from diancjsmkb where liucztb_id=0)"
						+ " union"
						+ " (select distinct meiz from diancjsyfb where liucztb_id=0) order by meiz";
			}
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("meiz")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	// end
	// **********************打印界面DIV中的数据处理 开始**************************//
	// 判断页面是否是第一次调用
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}

	private String RT_HET = "jies";

	private String mstrReportName = "jies";

	public String getPrintTable() {
		if (mstrReportName.equals(RT_HET)) {
			chaxunzt1 = 1;// 查询状态
			zhuangt = 2;
			return getPrintData();
		} else {
			return "无此报表";
		}
	}

	public boolean getRaw() {
		return true;
	}

	// 格式化
	public String format(double dblValue, String strFormat) {
		DecimalFormat df = new DecimalFormat(strFormat);
		return formatq(df.format(dblValue));

	}

	public String formatq(String strValue) {// 加千位分隔符
		String strtmp = "", xiaostmp = "", tmp = "";
		int i = 3;
		if (strValue.lastIndexOf(".") == -1) {

			strtmp = strValue;
			if (strValue.equals("")) {

				xiaostmp = "";
			} else {

				xiaostmp = ".00";
			}

		} else {

			strtmp = strValue.substring(0, strValue.lastIndexOf("."));

			if (strValue.substring(strValue.lastIndexOf(".")).length() == 2) {

				xiaostmp = strValue.substring(strValue.lastIndexOf(".")) + "0";
			} else {

				xiaostmp = strValue.substring(strValue.lastIndexOf("."));
			}

		}
		tmp = strtmp;

		while (i < tmp.length()) {
			strtmp = strtmp.substring(0, strtmp.length() - (i + (i - 3) / 3))
					+ ","
					+ strtmp.substring(strtmp.length() - (i + (i - 3) / 3),
							strtmp.length());
			i = i + 3;
		}

		return strtmp + xiaostmp;
	}

	public String getTianzdw(long diancxxb_id) {
		String Tianzdw = "";
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select quanc from gongsxxb where id=" + diancxxb_id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				Tianzdw = rs.getString("quanc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return Tianzdw;
	}

	private int chaxunzt1 = 0;// 查询状态

	private int zhuangt = 1;

	public String getPrintData() {
		Visit visit = (Visit) getPage().getVisit();

		if (getJiesbhValue().getValue() == "") {
			return "";
		} else {
			visit.setInt1(2);// 是第一次显示
			chaxunzt1 = 0;
			zhuangt = 1;
			Kuangfjsd jsd = new Kuangfjsd();
			return jsd.getKuangfjsd(((Kuangfjsbean) getEditValues().get(0))
					.getJiesbh(), 0);
		}

	}

	// **********************打印界面DIV中的数据处理 结束**************************//

	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			int value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}

	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
}