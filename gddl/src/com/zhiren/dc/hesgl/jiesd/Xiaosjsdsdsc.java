package com.zhiren.dc.hesgl.jiesd;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
 
public class Xiaosjsdsdsc extends BasePage {

	private static int _editTableRow = -1;//编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	
	private String _msg;

	protected void initialize() {
		super.initialize();
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value,false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	// 煤款含税单价保留小数位
	public int getMeikhsdjblxsw() {

		return ((Visit) getPage().getVisit()).getInt2();
	}
	
	public void setMeikhsdjblxsw(int value){
		
		((Visit) getPage().getVisit()).setInt2(value);
	}
	
	// 运费含税单价保留小数位
	public int getYunfhsdjblxsw() {

		return ((Visit) getPage().getVisit()).getInt3();
	}
	
	public void setYunfhsdjblxsw(int value){
		
		((Visit) getPage().getVisit()).setInt3(value);
	}
	
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

	private void Save() {
		Visit visit = (Visit) getPage().getVisit();
	    JDBCcon con = new JDBCcon();
	    ResultSetList rsl = null;
	    String sql = "";
	    String tablename = "kuangfjsmkb";
	    String mingc = "煤款税率";
	    
	    sql = "select diancxxb_id, gongysb_id, liucztb_id, liucgzid, liucgzbid, jiesfrl, " +
		  "       meikxxb_id, jiesrl, jieslf, jiesrcrl, ruzry, yufkje, hansyf, shuik" +
		  "  from " + tablename + 
		  " where bianm = '" + getJiesbhValue().getValue() + "'";
	    
		if ("矿区运费".equals(getJieslxValue().getValue())) {
			tablename = "kuangfjsyfb";
			mingc = "运费税率";
			
			sql = 
				"select diancxxb_id,\n" +
				"       gongysb_id,\n" + 
				"       liucztb_id,\n" + 
				"       liucgzid,\n" + 
				"       meikxxb_id,\n" + 
				"       ruzry,\n" + 
				"       hansyf,\n" + 
				"       shuik\n" + 
				"  from kuangfjsyfb\n" + 
				" where bianm = '" + getJiesbhValue().getValue() + "'";
		}
		
		rsl = con.getResultSetList(sql);
		
		long diancxxb_id = 0;
		long gongysb_id = 0;
		long liucztb_id = 0;
		long liucgzid = 0;
		long liucgzbid = 0;
		double jiesfrl = 0;
		long meikxxb_id = 0;
		double jiesrl = 0;
		double jieslf = 0;
		double jiesrcrl = 0;
		String ruzry = "";
		double yufkje = 0;
		double hansyf = 0;
		double shuik = 0;
		
		if (rsl.next()) {
			diancxxb_id = rsl.getLong("diancxxb_id");
			gongysb_id = rsl.getLong("gongysb_id");
			liucztb_id = rsl.getLong("liucztb_id");
			liucgzid = rsl.getLong("liucgzid");
			liucgzbid = rsl.getLong("liucgzbid");
			jiesfrl = rsl.getDouble("jiesfrl");
			meikxxb_id = rsl.getLong("meikxxb_id");
			jiesrl = rsl.getDouble("jiesrl");
			jieslf = rsl.getDouble("jieslf");
			jiesrcrl = rsl.getDouble("jiesrcrl");
			ruzry = rsl.getString("ruzry");
			yufkje = rsl.getDouble("yufkje");
			hansyf = rsl.getDouble("hansyf");
			shuik = rsl.getDouble("shuik");
		}
		
		rsl.close();
		
		sql = "select zhi from xitxxb where mingc = '" + mingc + "'";
		rsl = con.getResultSetList(sql);
		
		double shuil = 0;
		
		if (rsl.next()) {
			shuil = Double.parseDouble("0" + rsl.getString("zhi"));
		}
		
		rsl.close();
		
		if (getEditValues() != null && !getEditValues().isEmpty()
				&& !((Dcbalancebean) getEditValues().get(0)).getJiesbh().equals("")) {
			
			String diancjsmkb_id = MainGlobal.getNewID(visit.getDiancxxb_id());
			String update = "";
			String insertJieszbsjb = "";
			String insertDanpcjsmxb = "";
			
			double Jiasje = ((Dcbalancebean) getEditValues().get(0)).getJiasje();		// 9行合计
			double Bukyqjk = ((Dcbalancebean) getEditValues().get(0)).getBukyqjk();		// 9行补扣以前价款
			double Jiakhj = ((Dcbalancebean) getEditValues().get(0)).getJiakhj() ;		// 9行价款合计
			double Jiaksk = shuik;		// 9行税款
			double Jiakje = ((Dcbalancebean) getEditValues().get(0)).getJiakje();		// 9行金额
			
			if ("矿区运费".equals(getJieslxValue().getValue())) {
				Jiasje = visit.getDouble21();
				Bukyqjk = visit.getDouble20();
				Jiakhj = visit.getDouble19();
				Jiaksk = visit.getDouble18();
				Jiakje = visit.getDouble17();
			}
			
			sql = "insert into diancjsmkb values (" + diancjsmkb_id 
					+ ", " + diancxxb_id 
					+ ", '" + ((Dcbalancebean) getEditValues().get(0)).getJiesbh().replaceAll("CG", "XS") 
					+ "', " + getProperId(getIFahdwModels(), ((Dcbalancebean)getEditValues().get(0)).getFahdw()) 
					+ ", '" + ((Dcbalancebean) getEditValues().get(0)).getFahdw() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getFaz() 
					+ "', to_date('" + FormatDate(((Dcbalancebean) getEditValues().get(0)).getFahksrq()) 
					+ "', 'yyyy-mm-dd'), to_date('" + FormatDate(((Dcbalancebean) getEditValues().get(0)).getFahjzrq()) 
					+ "', 'yyyy-mm-dd'), '" + ((Dcbalancebean) getEditValues().get(0)).getPinz() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getDaibcc() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getYuanshr() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getXianshr() 
					+ "', to_date('" + FormatDate(((Dcbalancebean) getEditValues().get(0)).getYansksrq()) 
					+ "', 'yyyy-mm-dd'), to_date('" + FormatDate(((Dcbalancebean) getEditValues().get(0)).getYansjzrq()) 
					+ "', 'yyyy-mm-dd'), '" + ((Dcbalancebean) getEditValues().get(0)).getYansbh() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getShoukdw() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getKaihyh() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getZhangh() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getFapbh() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getFukfs() 
					+ "', '" + ((Dcbalancebean) getEditValues().get(0)).getDuifdd() 
					+ "', " + ((Dcbalancebean) getEditValues().get(0)).getChes() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getJiessl() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getJingz() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getShulzjbz()//hansdj
					+ ", " + Bukyqjk 
					+ ", " + Jiasje
					+ ", " + Jiakhj
					+ ", " + Jiakje 
					+ ", " + Jiaksk 
					+ ", " + shuil 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getBuhsdj() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getJieslx() 
					+ ", to_date('" + FormatDate(((Dcbalancebean) getEditValues().get(0)).getJiesrq()) 
					+ "', 'yyyy-mm-dd'), to_date('" + FormatDate(((Dcbalancebean) getEditValues().get(0)).getRuzrq()) 
					+ "', 'yyyy-mm-dd'), " + ((Dcbalancebean) getEditValues().get(0)).getHetb_id() 
					+ ", " + liucztb_id 
					+ ", " + liucgzid 
					+ ", 0, '" + ((Dcbalancebean) getEditValues().get(0)).getRanlbmjbr() 
					+ "', to_date('" + FormatDate(((Dcbalancebean) getEditValues().get(0)).getRanlbmjbrq()) 
					+ "', 'yyyy-mm-dd'), '" + ((Dcbalancebean) getEditValues().get(0)).getBeiz() 
					+ "', " + liucgzbid 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getYunsfsb_id() 
					+ ", '" + ((Dcbalancebean) getEditValues().get(0)).getYunju() 
					+ "', " + ((Dcbalancebean) getEditValues().get(0)).getYingd() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getKuid() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getYuns() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getKoud_js() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getJiesslcy() 
					+ ", " + jiesfrl 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getJihkjb_id() 
					+ ", " + meikxxb_id 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getHetjg() 
					+ ", '" + ((Dcbalancebean) getEditValues().get(0)).getMeikdw() 
					+ "', ''" ///zhiljq 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getQiyfhj() 
					+ ", " + jiesrl 
					+ ", " + jieslf 
					+ ", " + jiesrcrl 
					+ ", '" + ruzry 
					+ "', " + ((Dcbalancebean) getEditValues().get(0)).getFengsjj() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getJiajqdj() 
					+ ", 0, " + ((Dcbalancebean) getEditValues().get(0)).getJijlx() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getKuidjfyf() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getKuidjfzf() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getChaokdl() 
					+ ", '" + ((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid() 
					+ "', " + yufkje 
					+ ", " + hansyf 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getYunfhsdj() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getBuhsyf() 
					+ ", " + ((Dcbalancebean) getEditValues().get(0)).getYunfjsl() + ");";
					
			update = "update " + tablename + " set diancjsmkb_id = " + diancjsmkb_id + " where bianm = '"
							+ ((Dcbalancebean) getEditValues().get(0)).getJiesbh() + "';";
			
			insertJieszbsjb = "insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, " +
							  "		jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id)" + 
							  "(select getNewID(" + visit.getDiancxxb_id() + "), " + diancjsmkb_id + ", zhibb_id, hetbz, " +
							  "		gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id" +
							  "		from jieszbsjb where jiesdid = (select id from " + 
							  tablename + " where bianm = '" + getJiesbhValue().getValue() + "'));";
			
			insertDanpcjsmxb = "insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, " +
					           "	jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, \n" +
					           " 	ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, " +
					           "	jiashj, biaomdj, buhsbmdj, leib, hetj, qnetar, std, stad, star, vdaf, mt, " +
					           "	mad, aad, ad, aar, vad, zongje, meikxxb_id, faz_id, chaokdl, jiajqdj, " +
					           "	lie_id, zhekfs) \n" +
					           "(select getNewID(" + visit.getDiancxxb_id() + "), xuh, " + diancjsmkb_id + ", zhibb_id, hetbz, " +
					           "	gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, " +
					           "	kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, " +
					           "	jiashj, biaomdj, buhsbmdj, leib, hetj, qnetar, std, stad, star, vdaf, mt, " +
					           "	mad, aad, ad, aar, vad, zongje, meikxxb_id, faz_id, chaokdl, jiajqdj, lie_id, " +
					           "	zhekfs from danpcjsmxb where jiesdid = (select id from " + tablename + " where " +
					           "bianm = '" + getJiesbhValue().getValue() + "'));";
			
			StringBuffer buffer = new StringBuffer("begin \n");
			buffer.append(sql).append(update).append(insertJieszbsjb).append(insertDanpcjsmxb).append("\n end;");
			
			if (buffer.length() < 13) {
				con.Close();
			} else {
				con.getUpdate(buffer.toString());
				con.Close();
				setMsg("结算单更新成功！");
			}
			
		} else {

			setMsg("该结算编号已被另一张结算单使用，请核对！");
		}
	}
	
	private void Retruns(IRequestCycle cycle) {
		cycle.activate("Caigddcl");
	}

	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _CreateChick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		
		if (_RefreshChick) {
			_RefreshChick = false;
//			getIJiesbhModels();
			getSelectData();
		}
		
		if (_CreateChick) {
			_CreateChick = false;
			create();
		}
		
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
			getIJiesbhModels();
			getSelectData();
		}
		
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getIJiesbhModels();
			getSelectData();
		}
		
		if (_RetrunsChick) {
			_RetrunsChick = false;
			Retruns(cycle);
		}
	}
		
	private void Delete() {
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer strbf = new StringBuffer();
			if (getEditValues() != null && !getEditValues().isEmpty()) {
				
				String deleteDiancjsmkb = "delete from diancjsmkb where bianm = '"
											+ getJiesbhValue().getValue().replaceAll("CG", "XS") + "';";
				
				String deleteJieszbsjb = "delete from jieszbsjb where jiesdid = (select id from "
											+ " diancjsmkb where bianm = '"
											+ getJiesbhValue().getValue().replaceAll("CG", "XS") + "');";
				
				String deleteDanpcjsmxb = "delete from danpcjsmxb where jiesdid = (select id from "
											+ " diancjsmkb where bianm = '"
											+ getJiesbhValue().getValue().replaceAll("CG", "XS") + "');";
				
				strbf.append("begin		\n");
				strbf.append(deleteDiancjsmkb).append(deleteJieszbsjb).append(deleteDanpcjsmxb);
				strbf.append("end;	\n");

				if (con.getDelete(strbf.toString()) >= 0) {

					setMsg("编号："
							+ ((Dcbalancebean) getEditValues().get(0))
									.getJiesbh().replaceAll("CG", "XS") + "结算单已删除！");
				} else {

					setMsg("编号："
							+ ((Dcbalancebean) getEditValues().get(0))
									.getJiesbh().replaceAll("CG", "XS") + "结算单删除失败！");
				}

			} else {

				setMsg("请选择要删除的结算单！");
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
			getIJiesbhModels();
			getEditValues().clear();
			this.setJieszb("");
			List _editvalues = new ArrayList();
			_editvalues.add(new Dcbalancebean());
			setEditValues(_editvalues);
		}
	}
	
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}
	
	private Dcbalancebean _EditValue;
	
	public Dcbalancebean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Dcbalancebean EditValue) {
		_EditValue = EditValue;
	}
	
	public String getDXMoney(double _Money ) {
		Money money = new Money();
		return money.NumToRMBStr(_Money);
	}
	
	public String getTitle() {
		
		return Locale.jiesd_title;
	}
	
	public String getJiesslcyText() {
		
		return Locale.jiesslcy_title;
	}
	
	public void create() {
		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		if (getEditValues() != null) {
			
			getEditValues().clear();
		}
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		ResultSetList rsl1 = null;
		String tablename = "kuangfjsmkb";
		
		if ("矿区运费".equals(getJieslxValue().getValue())) {
			tablename = "kuangfjsyfb";
		}
		
		String sql = "select sum(fengsjj) as fengsjj " +
				     "  from hetjgb jg, hetb ht " +
				     " where jg.hetb_id = ht.id " +
				     "   and ht.hetbh = '" + getHetbhValue().getValue() + "'";
		rsl = con.getResultSetList(sql);
		double fengsjj = 0;
		if (rsl.next()) {
			fengsjj = rsl.getDouble("fengsjj");
		}
		rsl.close();
		
		sql = "select * from " + tablename + " where bianm = '" + getJiesbhValue().getValue() + "'";
		rsl = con.getResultSetList(sql);
		
		long mkid = 0;
		long yfid = 0;
		double bukmk = 0;
		double hansmk = 0;
		double buhsmk = 0;
		double meikje = 0;
		long jihkjb_id = 0;
		double hetj = 0;
		String zhiljq = "";
		double qiyf = 0;
		double jiesrl = 0;
		double jieslf = 0;
		double jiesrcrl = 0;
		double kffengsjj = 0;
		double jiajqdj = 0;
		int jijlx = 0;
		double yufkje = 0;
		double yunfhsdj = 0;
		double yunfjsl = 0;
		
		double meiksk = 0;
		double yunfsk = 0;
		
		
		String mmeikhjdx = "";
		String myunzfhjdx = "";
		
		if (rsl.next()) {
			
			mkid = rsl.getLong("id");
			
			bukmk = rsl.getDouble("bukmk");
			hansmk = rsl.getDouble("hansmk");
			buhsmk = rsl.getDouble("buhsmk");
			meikje = rsl.getDouble("meikje");
			jihkjb_id = rsl.getLong("jihkjb_id");
			hetj = rsl.getDouble("hetj");
			zhiljq = rsl.getString("zhiljq");
			qiyf = rsl.getDouble("qiyf");
			jiesrl = rsl.getDouble("jiesrl");
			jieslf = rsl.getDouble("jieslf");
			jiesrcrl = rsl.getDouble("jiesrcrl");
			kffengsjj = rsl.getDouble("fengsjj");
			jiajqdj = rsl.getDouble("jiajqdj");
			jijlx = rsl.getInt("jijlx");
			yufkje = rsl.getDouble("yufkje");
			yunfhsdj = rsl.getDouble("yunfhsdj");
			yunfjsl = rsl.getDouble("yunfjsl");
			
			meiksk = rsl.getDouble("shuik");
			
			mmeikhjdx = getDXMoney(hansmk);
			
			if ("kuangfjsyfb".equals(tablename)) {
				
				yfid = rsl.getLong("id");
				
				bukmk = rsl.getDouble("bukyf");
				hansmk = rsl.getDouble("hansyf");
				buhsmk = rsl.getDouble("buhsyf");
				
				yunfsk = rsl.getDouble("shuik");
				
				myunzfhjdx = getDXMoney(hansmk);
			}
			
			sql = "select jieszbsjb.*, zhibb.bianm from jieszbsjb, "
					+ tablename
					+ " js, zhibb "
					+ " where jieszbsjb.jiesdid = js.id and zhibb.id = jieszbsjb.zhibb_id "
					+ " and js.bianm = '" + getJiesbhValue().getValue()
					+ "' and jieszbsjb.zhuangt = 1 order by jieszbsjb.id";
			rsl1 = con.getResultSetList(sql);
			
			String mhetsl = "";
			double mgongfsl = 0;
			double mshulzjbz = 0;
			double myanssl = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			
			String mShulzb_ht = "";
			double mShulzb_yk = 0;
			double mShulzb_zdj = 0;
			double mShulzb_zje = 0;
			
			String mstrJieszb = "";
			Jiesdcz jsdcz = new Jiesdcz();
			
			String mQnetar_ht = "";
			double mQnetar_kf = 0;
			double mQnetar_cf = 0;
			double mQnetar_js = 0;
			double mQnetar_yk = 0;
			double mQnetar_zdj = 0;
			double mQnetar_zje = 0;
			
			String mStd_ht = "";
			double mStd_kf = 0;
			double mStd_cf = 0;
			double mStd_js = 0;
			double mStd_yk = 0;
			double mStd_zdj = 0;
			double mStd_zje = 0;
			
			String mStar_ht = "";
			double mStar_kf = 0;
			double mStar_cf = 0;
			double mStar_js = 0;
			double mStar_yk = 0;
			double mStar_zdj = 0;
			double mStar_zje = 0;
			
			String mAd_ht = "";
			double mAd_kf = 0;
			double mAd_cf = 0;
			double mAd_js = 0;
			double mAd_yk = 0;
			double mAd_zdj = 0;
			double mAd_zje = 0;
			
			String mVdaf_ht = "";
			double mVdaf_kf = 0;
			double mVdaf_cf = 0;
			double mVdaf_js = 0;
			double mVdaf_yk = 0;
			double mVdaf_zdj = 0;
			double mVdaf_zje = 0;
			
			String mMt_ht = "";
			double mMt_kf = 0;
			double mMt_cf = 0;
			double mMt_js = 0;
			double mMt_yk = 0;
			double mMt_zdj = 0;
			double mMt_zje = 0;
			
			String mQgrad_ht = "";
			double mQgrad_kf = 0;
			double mQgrad_cf = 0;
			double mQgrad_js = 0;
			double mQgrad_yk = 0;
			double mQgrad_zdj = 0;
			double mQgrad_zje = 0;
			
			String mQbad_ht = "";
			double mQbad_kf = 0;
			double mQbad_cf = 0;
			double mQbad_js = 0;
			double mQbad_yk = 0;
			double mQbad_zdj = 0;
			double mQbad_zje = 0;
			
			String mHad_ht = "";
			double mHad_kf = 0;
			double mHad_cf = 0;
			double mHad_js = 0;
			double mHad_yk = 0;
			double mHad_zdj = 0;
			double mHad_zje = 0;
			
			String mStad_ht = "";
			double mStad_kf = 0;
			double mStad_cf = 0;
			double mStad_js = 0;
			double mStad_yk = 0;
			double mStad_zdj = 0;
			double mStad_zje = 0;
			
			String mMad_ht = "";
			double mMad_kf = 0;
			double mMad_cf = 0;
			double mMad_js = 0;
			double mMad_yk = 0;
			double mMad_zdj = 0;
			double mMad_zje = 0;
			
			String mAar_ht = "";
			double mAar_kf = 0;
			double mAar_cf = 0;
			double mAar_js = 0;
			double mAar_yk = 0;
			double mAar_zdj = 0;
			double mAar_zje = 0;
			
			String mAad_ht = "";
			double mAad_kf = 0;
			double mAad_cf = 0;
			double mAad_js = 0;
			double mAad_yk = 0;
			double mAad_zdj = 0;
			double mAad_zje = 0;
			
			String mVad_ht = "";
			double mVad_kf = 0;
			double mVad_cf = 0;
			double mVad_js = 0;
			double mVad_yk = 0;
			double mVad_zdj = 0;
			double mVad_zje = 0;
			
			String mT2_ht = "";
			double mT2_kf = 0;
			double mT2_cf = 0;
			double mT2_js = 0;
			double mT2_yk = 0;
			double mT2_zdj = 0;
			double mT2_zje = 0;
			
			String mYunju_ht = "";
			double mYunju_kf = 0;
			double mYunju_cf = 0;
			double mYunju_js = 0;
			double mYunju_yk = 0;
			double mYunju_zdj = 0;
			double mYunju_zje = 0;
			
			if (rsl1.next()) {
				
				if (rsl1.getString("bianm").equals(Locale.jiessl_zhibb)) {

					mhetsl = rsl1.getString("hetbz");
					mgongfsl = rsl1.getDouble("gongf");
					mshulzjbz = rsl1.getDouble("zhejbz");
					myanssl = rsl1.getDouble("changf");
					myingksl = rsl1.getDouble("yingk");
					mshulzjje = rsl1.getDouble("zhejje");

				} else if (rsl1.getString("bianm").equals(Locale.Shul_zhibb)) {

					mShulzb_ht = rsl1.getString("hetbz");
					mShulzb_yk = rsl1.getDouble("yingk");
					mShulzb_zdj = rsl1.getDouble("zhejbz");
					mShulzb_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("数量(吨)", "Shulzb_ht",
							"Shulzb_kf", "Shulzb_cf", "Shulzb_js",
							"Shulzb_yk", "Shulzb_zdj", "Shulzb_zje",
							mShulzb_ht, mgongfsl, myanssl, rsl.getDouble("jiessl"),
							mShulzb_yk, mShulzb_zdj, mShulzb_zje);

				} else if (rsl1.getString("bianm").equals(
						Locale.Qnetar_zhibb)) {

					mQnetar_ht = rsl1.getString("hetbz");// 合同热量
					mQnetar_kf = rsl1.getDouble("gongf");// 供方热量
					mQnetar_cf = rsl1.getDouble("changf");
					mQnetar_js = rsl1.getDouble("jies");// 结算热量
					mQnetar_yk = rsl1.getDouble("yingk");
					mQnetar_zdj = rsl1.getDouble("zhejbz");
					mQnetar_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Qnetar(kcal/kg)",
							"Qnetar_ht", "Qnetar_kf", "Qnetar_cf",
							"Qnetar_js", "Qnetar_yk", "Qnetar_zdj",
							"Qnetar_zje", mQnetar_ht, MainGlobal
									.Mjkg_to_kcalkg(mQnetar_kf, 0),
							MainGlobal.Mjkg_to_kcalkg(mQnetar_cf, 0),
							MainGlobal.Mjkg_to_kcalkg(mQnetar_js, 0),
							mQnetar_yk, mQnetar_zdj, mQnetar_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Std_zhibb)) {

					mStd_ht = rsl1.getString("hetbz"); // 合同硫分
					mStd_kf = rsl1.getDouble("gongf");
					mStd_cf = rsl1.getDouble("changf");
					mStd_js = rsl1.getDouble("jies"); // 结算硫分
					mStd_yk = rsl1.getDouble("yingk");
					mStd_zdj = rsl1.getDouble("zhejbz");
					mStd_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Std(%)", "Std_ht",
							"Std_kf", "Std_cf", "Std_js", "Std_yk",
							"Std_zdj", "Std_zje", mStd_ht, mStd_kf,
							mStd_cf, mStd_js, mStd_yk, mStd_zdj, mStd_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Star_zhibb)) {

					mStar_ht = rsl1.getString("hetbz"); // 合同挥发分
					mStar_kf = rsl1.getDouble("gongf");
					mStar_cf = rsl1.getDouble("changf");
					mStar_js = rsl1.getDouble("jies"); // 结算挥发分
					mStar_yk = rsl1.getDouble("yingk");
					mStar_zdj = rsl1.getDouble("zhejbz");
					mStar_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Star(%)", "Star_ht",
							"Star_kf", "Star_cf", "Star_js", "Star_yk",
							"Star_zdj", "Star_zje", mStar_ht, mStar_kf,
							mStar_cf, mStar_js, mStar_yk, mStar_zdj,
							mStar_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Ad_zhibb)) {

					mAd_ht = rsl1.getString("hetbz"); // 合同挥发分
					mAd_kf = rsl1.getDouble("gongf");
					mAd_cf = rsl1.getDouble("changf");
					mAd_js = rsl1.getDouble("jies"); // 结算挥发分
					mAd_yk = rsl1.getDouble("yingk");
					mAd_zdj = rsl1.getDouble("zhejbz");
					mAd_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Ad(%)", "Ad_ht",
							"Ad_kf", "Ad_cf", "Ad_js", "Ad_yk", "Ad_zdj",
							"Ad_zje", mAd_ht, mAd_kf, mAd_cf, mAd_js,
							mAd_yk, mAd_zdj, mAd_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Vdaf_zhibb)) {

					mVdaf_ht = rsl1.getString("hetbz"); // 合同挥发分
					mVdaf_kf = rsl1.getDouble("gongf");
					mVdaf_cf = rsl1.getDouble("changf");
					mVdaf_js = rsl1.getDouble("jies"); // 结算挥发分
					mVdaf_yk = rsl1.getDouble("yingk");
					mVdaf_zdj = rsl1.getDouble("zhejbz");
					mVdaf_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Vdaf(%)", "Vdaf_ht",
							"Vdaf_kf", "Vdaf_cf", "Vdaf_js", "Vdaf_yk",
							"Vdaf_zdj", "Vdaf_zje", mVdaf_ht, mVdaf_kf,
							mVdaf_cf, mVdaf_js, mVdaf_yk, mVdaf_zdj,
							mVdaf_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Mt_zhibb)) {

					mMt_ht = rsl1.getString("hetbz"); // 合同挥发分
					mMt_kf = rsl1.getDouble("gongf");
					mMt_cf = rsl1.getDouble("changf");
					mMt_js = rsl1.getDouble("jies"); // 结算挥发分
					mMt_yk = rsl1.getDouble("yingk");
					mMt_zdj = rsl1.getDouble("zhejbz");
					mMt_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Mt(%)", "Mt_ht",
							"Mt_kf", "Mt_cf", "Mt_js", "Mt_yk", "Mt_zdj",
							"Mt_zje", mMt_ht, mMt_kf, mMt_cf, mMt_js,
							mMt_yk, mMt_zdj, mMt_zje);

				} else if (rsl1.getString("bianm")
						.equals(Locale.Qgrad_zhibb)) {

					mQgrad_ht = rsl1.getString("hetbz"); // 合同挥发分
					mQgrad_kf = rsl1.getDouble("gongf");
					mQgrad_cf = rsl1.getDouble("changf");
					mQgrad_js = rsl1.getDouble("jies"); // 结算挥发分
					mQgrad_yk = rsl1.getDouble("yingk");
					mQgrad_zdj = rsl1.getDouble("zhejbz");
					mQgrad_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Qgrad(kcal/kg)",
							"Qgrad_ht", "Qgrad_kf", "Qgrad_cf", "Qgrad_js",
							"Qgrad_yk", "Qgrad_zdj", "Qgrad_zje",
							mQgrad_ht, MainGlobal.Mjkg_to_kcalkg(mQgrad_kf,
									0), MainGlobal.Mjkg_to_kcalkg(
									mQgrad_cf, 0), MainGlobal
									.Mjkg_to_kcalkg(mQgrad_js, 0),
							mQgrad_yk, mQgrad_zdj, mQgrad_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Qbad_zhibb)) {

					mQbad_ht = rsl1.getString("hetbz"); // 合同挥发分
					mQbad_kf = rsl1.getDouble("gongf");
					mQbad_cf = rsl1.getDouble("changf");
					mQbad_js = rsl1.getDouble("jies"); // 结算挥发分
					mQbad_yk = rsl1.getDouble("yingk");
					mQbad_zdj = rsl1.getDouble("zhejbz");
					mQbad_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Qbad(kcal/kg)",
							"Qbad_ht", "Qbad_kf", "Qbad_cf", "Qbad_js",
							"Qbad_yk", "Qbad_zdj", "Qbad_zje", mQbad_ht,
							MainGlobal.Mjkg_to_kcalkg(mQbad_kf, 0),
							MainGlobal.Mjkg_to_kcalkg(mQbad_cf, 0),
							MainGlobal.Mjkg_to_kcalkg(mQbad_js, 0),
							mQbad_yk, mQbad_zdj, mQbad_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Had_zhibb)) {

					mHad_ht = rsl1.getString("hetbz"); // 合同挥发分
					mHad_kf = rsl1.getDouble("gongf");
					mHad_cf = rsl1.getDouble("changf");
					mHad_js = rsl1.getDouble("jies"); // 结算挥发分
					mHad_yk = rsl1.getDouble("yingk");
					mHad_zdj = rsl1.getDouble("zhejbz");
					mHad_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Had(%)", "Had_ht",
							"Had_kf", "Had_cf", "Had_js", "Had_yk",
							"Had_zdj", "Had_zje", mHad_ht, mHad_kf,
							mHad_cf, mHad_js, mHad_yk, mHad_zdj, mHad_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Stad_zhibb)) {

					mStad_ht = rsl1.getString("hetbz"); // 合同挥发分
					mStad_kf = rsl1.getDouble("gongf");
					mStad_cf = rsl1.getDouble("changf");
					mStad_js = rsl1.getDouble("jies"); // 结算挥发分
					mStad_yk = rsl1.getDouble("yingk");
					mStad_zdj = rsl1.getDouble("zhejbz");
					mStad_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Stad(%)", "Stad_ht",
							"Stad_kf", "Stad_cf", "Stad_js", "Stad_yk",
							"Stad_zdj", "Stad_zje", mStad_ht, mStad_kf,
							mStad_cf, mStad_js, mStad_yk, mStad_zdj,
							mStad_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Mad_zhibb)) {

					mMad_ht = rsl1.getString("hetbz"); // 合同挥发分
					mMad_kf = rsl1.getDouble("gongf");
					mMad_cf = rsl1.getDouble("changf");
					mMad_js = rsl1.getDouble("jies"); // 结算挥发分
					mMad_yk = rsl1.getDouble("yingk");
					mMad_zdj = rsl1.getDouble("zhejbz");
					mMad_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Mad(%)", "Mad_ht",
							"Mad_kf", "Mad_cf", "Mad_js", "Mad_yk",
							"Mad_zdj", "Mad_zje", mMad_ht, mMad_kf,
							mMad_cf, mMad_js, mMad_yk, mMad_zdj, mMad_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Aar_zhibb)) {

					mAar_ht = rsl1.getString("hetbz"); // 合同挥发分
					mAar_kf = rsl1.getDouble("gongf");
					mAar_cf = rsl1.getDouble("changf");
					mAar_js = rsl1.getDouble("jies"); // 结算挥发分
					mAar_yk = rsl1.getDouble("yingk");
					mAar_zdj = rsl1.getDouble("zhejbz");
					mAar_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Aar(%)", "Aar_ht",
							"Aar_kf", "Aar_cf", "Aar_js", "Aar_yk",
							"Aar_zdj", "Aar_zje", mAar_ht, mAar_kf,
							mAar_cf, mAar_js, mAar_yk, mAar_zdj, mAar_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Aad_zhibb)) {

					mAad_ht = rsl1.getString("hetbz"); // 合同挥发分
					mAad_kf = rsl1.getDouble("gongf");
					mAad_cf = rsl1.getDouble("changf");
					mAad_js = rsl1.getDouble("jies"); // 结算挥发分
					mAad_yk = rsl1.getDouble("yingk");
					mAad_zdj = rsl1.getDouble("zhejbz");
					mAad_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Aad(%)", "Aad_ht",
							"Aad_kf", "Aad_cf", "Aad_js", "Aad_yk",
							"Aad_zdj", "Aad_zje", mAad_ht, mAad_kf,
							mAad_cf, mAad_js, mAad_yk, mAad_zdj, mAad_zje);

				} else if (rsl1.getString("bianm").equals(Locale.Vad_zhibb)) {

					mVad_ht = rsl1.getString("hetbz"); // 合同挥发分
					mVad_kf = rsl1.getDouble("gongf");
					mVad_cf = rsl1.getDouble("changf");
					mVad_js = rsl1.getDouble("jies"); // 结算挥发分
					mVad_yk = rsl1.getDouble("yingk");
					mVad_zdj = rsl1.getDouble("zhejbz");
					mVad_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("Vad(%)", "Vad_ht",
							"Vad_kf", "Vad_cf", "Vad_js", "Vad_yk",
							"Vad_zdj", "Vad_zje", mVad_ht, mVad_kf,
							mVad_cf, mVad_js, mVad_yk, mVad_zdj, mVad_zje);

				} else if (rsl1.getString("bianm").equals(Locale.T2_zhibb)) {

					mT2_ht = rsl1.getString("hetbz"); // 合同挥发分
					mT2_kf = rsl1.getDouble("gongf");
					mT2_cf = rsl1.getDouble("changf");
					mT2_js = rsl1.getDouble("jies"); // 结算挥发分
					mT2_yk = rsl1.getDouble("yingk");
					mT2_zdj = rsl1.getDouble("zhejbz");
					mT2_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("T2(%)", "T2_ht",
							"T2_kf", "T2_cf", "T2_js", "T2_yk", "T2_zdj",
							"T2_zje", mT2_ht, mT2_kf, mT2_cf, mT2_js,
							mT2_yk, mT2_zdj, mT2_zje);

				} else if (rsl1.getString("bianm")
						.equals(Locale.Yunju_zhibb)) {

					mYunju_ht = rsl1.getString("hetbz"); // 合同挥发分
					mYunju_kf = rsl1.getDouble("gongf");
					mYunju_cf = rsl1.getDouble("changf");
					mYunju_js = rsl1.getDouble("jies"); // 结算挥发分
					mYunju_yk = rsl1.getDouble("yingk");
					mYunju_zdj = rsl1.getDouble("zhejbz");
					mYunju_zje = rsl1.getDouble("zhejje");

					mstrJieszb += jsdcz.SetJieszb("运距(Km)", "Yunju_ht",
							"Yunju_kf", "Yunju_cf", "Yunju_js", "Yunju_yk",
							"Yunju_zdj", "Yunju_zje", mYunju_ht, mYunju_kf,
							mYunju_cf, mYunju_js, mYunju_yk, mYunju_zdj,
							mYunju_zje);

				}
				
				this.setJieszb(mstrJieszb);
			}
			
			double mhej = hansmk;
			String mdaxhj = getDXMoney(mhej);

			// 设置超/亏吨的显示
			if (!rsl.getString("chaokdlx").equals("")) {
				// 说明存在超亏吨
				this.setHejdxh(jsdcz.SetHejdxh(rsl.getString("chaokdlx"), rsl.getDouble("chaokdl"), mhej,
						mdaxhj));
			} else {

				this.setHejdxh(jsdcz.SetHejdxh("", 0, mhej, mdaxhj));
			}
			
			sql = "select zhi from xitxxb where mingc = '煤款税率'";
			ResultSetList r = con.getResultSetList(sql);
			double meiksl = 0;
			if (r.next()) {
				meiksl = Double.parseDouble("0" + r.getString("zhi"));
			}
			r.close();
			
			sql = "select zhi from xitxxb where mingc = '运费税率'";
			r = con.getResultSetList(sql);
			double yunfsl = 0;
			if (r.next()) {
				yunfsl = Double.parseDouble("0" + r.getString("zhi"));
			}
			r.close();
			
			if ("煤款".equals(getJieslxValue().getValue())) {
				
				double jiasje = (mshulzjbz + fengsjj) * rsl.getDouble("jiessl") + bukmk;
				double jiakhj = CustomMaths.Round_new(jiasje / (1 + meiksl), 2);
				meiksk = jiasje - jiakhj;
				double jiakje = CustomMaths.Round_new((mshulzjbz + fengsjj) * rsl.getDouble("jiessl") / (1 + meiksl), 2);
				double buhsdj = CustomMaths.Round_new((mshulzjbz + fengsjj) / (1+ meiksl), 7);
				
				_editvalues.add(new Dcbalancebean(mkid, yfid, rsl.getString("xianshr"),
						rsl.getString("bianm"), rsl.getString("gongysmc"), "", rsl
								.getString("faz"), rsl.getLong("yunsfsb_id"), rsl
								.getString("shoukdw"), rsl.getDate("fahksrq"),
						rsl.getDate("fahjzrq"), rsl.getDate("yansksrq"), rsl
								.getDate("yansjzrq"), rsl.getString("kaihyh"),
						rsl.getString("meiz"), rsl.getString("yuanshr"), rsl
								.getString("zhangh"), mhetsl, mgongfsl, rsl
								.getLong("ches"), rsl.getString("xianshr"), Jiesdcz
								.nvlStr(rsl.getString("fapbh")), rsl
								.getString("daibch"), Jiesdcz.nvlStr(rsl
								.getString("yansbh")), rsl.getString("duifdd"), rsl
								.getString("fukfs"), mshulzjbz + fengsjj, myanssl, myingksl,
						rsl.getDouble("yingd"), rsl.getDouble("kuid"), mshulzjje,
						rsl.getDouble("jiessl"), rsl.getDouble("jiesslcy"),
						yunfjsl, buhsdj, jiakje, bukmk, jiakhj,
						meiksl, meiksk, mhej, 0.0, 0.0, 0.0, 0.0, //mhej
						0, 0, 0, 0, 0, yunfsl, 0, 0, mhej,
						mmeikhjdx, myunzfhjdx, mdaxhj, rsl.getString("beiz"), 
						rsl.getString("ranlbmjbr"), rsl.getDate("ranlbmjbrq"),
						rsl.getDouble("kuidjfyf"), rsl.getDouble("guohl"), 
						rsl.getDate("jiesrq"), "", "", null, rsl.getDate("ruzrq"), 
						"", null, "", null, hetj, rsl.getLong("jieslx"), rsl.getDouble("yuns"), 
						rsl.getDouble("koud"), "", rsl.getString("bianm").substring(0,
							rsl.getString("bianm").indexOf("-") + 1),
						rsl.getLong("hetb_id"), rsl.getString("yunj"), 0, 0, jihkjb_id,
						kffengsjj, rsl.getDouble("jiajqdj"), jijlx, "", rsl
								.getDouble("kuidjfyf"), rsl.getDouble("kuidjfzf"),
						rsl.getDouble("chaokdl"), rsl.getString("chaokdlx"),
						yunfhsdj, rsl.getDouble("yunfhsdj"), rsl
								.getDouble("hansyf"), rsl.getDouble("buhsyf"),
						yunfjsl,0,0,0));
			} else {
				
				double yunzfhj = rsl.getDouble("guotyf") + rsl.getDouble("guotzf") + 
								rsl.getDouble("kuangqyf") + rsl.getDouble("kuangqzf");
				yunfsk = CustomMaths.Round_new(yunzfhj * yunfsl, 2);
				double buhsyf = yunzfhj - yunfsk;
				
				_editvalues.add(new Dcbalancebean(mkid, yfid, rsl.getString("xianshr"),
						rsl.getString("bianm"), rsl.getString("gongysmc"), "", rsl
								.getString("faz"), rsl.getLong("yunsfsb_id"), rsl
								.getString("shoukdw"), rsl.getDate("fahksrq"),
						rsl.getDate("fahjzrq"), rsl.getDate("yansksrq"), rsl
								.getDate("yansjzrq"), rsl.getString("kaihyh"),
						rsl.getString("meiz"), rsl.getString("yuanshr"), rsl
								.getString("zhangh"), mhetsl, rsl.getDouble("gongfsl"), rsl
								.getLong("ches"), rsl.getString("xianshr"), Jiesdcz
								.nvlStr(rsl.getString("fapbh")), rsl
								.getString("daibch"), Jiesdcz.nvlStr(rsl
								.getString("yansbh")), rsl.getString("duifdd"), rsl
								.getString("fukfs"), rsl.getDouble("hansdj") + fengsjj, rsl.getDouble("yanssl"), rsl.getDouble("yingk"),
						rsl.getDouble("yingd"), rsl.getDouble("kuid"), mshulzjje,
						rsl.getDouble("jiessl"), rsl.getDouble("jiesslcy"),
						yunfjsl, rsl.getDouble("buhsdj"), 0.00, 0.00, 0.00,
						meiksl, 0.00, 0.00, rsl.getDouble("guotyf"), rsl.getDouble("guotzf"), rsl.getDouble("kuangqyf"), rsl.getDouble("kuangqzf"), 
						0, 0, rsl.getDouble("guotyf") - rsl.getDouble("bukyf"), rsl.getDouble("jiskc"), buhsyf, 
						yunfsl, yunfsk, mhej, mhej,//mhej
						mmeikhjdx, myunzfhjdx, mdaxhj, rsl.getString("beiz"), 
						rsl.getString("ranlbmjbr"), rsl.getDate("ranlbmjbrq"),
						rsl.getDouble("kuidjfyf"), rsl.getDouble("guohl"), 
						rsl.getDate("jiesrq"), "", "", null, rsl.getDate("ruzrq"), 
						"", null, "", null, hetj, rsl.getLong("jieslx"), rsl.getDouble("yuns"), 
						rsl.getDouble("koud"), "", rsl.getString("bianm").substring(0,
							rsl.getString("bianm").indexOf("-") + 1),
						rsl.getLong("hetb_id"), rsl.getString("yunj"), 0, 0, jihkjb_id,
						kffengsjj, rsl.getDouble("jiajqdj"), jijlx, "", rsl
								.getDouble("kuidjfyf"), rsl.getDouble("kuidjfzf"),
						rsl.getDouble("chaokdl"), rsl.getString("chaokdlx"),
						yunfhsdj, rsl.getDouble("yunfhsdj"), rsl
								.getDouble("hansyf"), rsl.getDouble("buhsyf"),
						yunfjsl,0,0,0));
				
				// 9行合计 Jiasje
				visit.setDouble21((mshulzjbz + fengsjj)
								* rsl.getDouble("jiessl")
								+ ((rsl.getDouble("guotyf") - rsl.getDouble("bukyf"))
										* (1 - yunfsl) * (1 + meiksl)));
				// 9行补扣以前价款 Bukyqjk
				visit.setDouble20((rsl.getDouble("guotyf") - rsl.getDouble("bukyf"))
						* (1 - yunfsl) * (1 + meiksl));
				// 9行价款合计 Jiakhj
				visit.setDouble19(((mshulzjbz + fengsjj)
								* rsl.getDouble("jiessl") + ((rsl
								.getDouble("guotyf") - rsl.getDouble("bukyf"))
								* (1 - yunfsl) * (1 + meiksl)))
								* (1 + meiksl));
				// 9行税款 Jiaksk
				visit.setDouble18(((mshulzjbz + fengsjj)
								* rsl.getDouble("jiessl") + ((rsl
								.getDouble("guotyf") - rsl.getDouble("bukyf"))
								* (1 - yunfsl) * (1 + meiksl)))
								* meiksl / (1 + meiksl));
				// 9行金额 Jiakje
				visit.setDouble17((mshulzjbz + fengsjj)
						* rsl.getDouble("jiessl") / (1 + meiksl));
			}
		}
		
		if (_editvalues == null) {
			_editvalues.add(new Dcbalancebean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		
		rsl.close();
		con.Close();
	}
	
	public List getSelectData() {
		List _editvalues = new ArrayList();
		if (getEditValues() != null) {
			
			getEditValues().clear();
		}
		
		JDBCcon con = new JDBCcon();
		try {

			long mid = 0;
			long myid = 0;
			long mdiancxxb_id = 0;
			long mgongysb_id = 0;
			String mtianzdw = "";
			String mjiesbh = "";
			String mfahdw = "";
			String mmeikdw = "";
			String mfaz = "";
			String mshoukdw = "";
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date myansksrq = new Date();
			Date myansjzrq = new Date();
			String mfahrq = "";
			String mkaihyh = "";
			String mpinz = "";
			String myuanshr = mtianzdw;
			String mzhangh = "";
			String mhetsl = "";			// 合同数量
			double mgongfsl = 0;
			long mches = 0;
			String mxianshr = myuanshr;
			String mfapbh = "";
			String mdaibcc = "";
			String myansbh = "";
			String mduifdd = "";
			String mfukfs = "";
			double mshulzjbz = 0;
			double myanssl = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			double myunfhsdj = 0;		// 运费含税单价
			
			String mShulzb_ht = "";		// 合同数量指标
			double mShulzb_yk = 0;		// 数量指标盈亏
			double mShulzb_zdj = 0;		// 数量指标折单价
			double mShulzb_zje = 0;		// 数量指标折金额
			 
			String mQnetar_ht = "";		// 合同热量
			double mQnetar_kf = 0;		// 供方热量
			double mQnetar_cf = 0;		// 厂方热量
			double mQnetar_js = 0;		// 厂方结算
			double mQnetar_yk = 0;		// 厂方盈亏
			double mQnetar_zdj = 0;		// 折单价
			double mQnetar_zje = 0;		// 这金额
			
			String mStd_ht = "";		// 合同硫分
			double mStd_kf = 0;			// 供方热量
			double mStd_cf = 0;			// 厂方热量
			double mStd_js = 0;			// 结算热量
			double mStd_yk = 0;			// 厂方盈亏
			double mStd_zdj = 0;		// 折单价
			double mStd_zje = 0;		// 这金额
			
			String mAd_ht = "";			// 合同硫分
			double mAd_kf = 0;			// 供方热量
			double mAd_cf = 0;			// 厂方热量
			double mAd_js = 0;			// 结算热量
			double mAd_yk = 0;			// 厂方盈亏
			double mAd_zdj = 0;			// 折单价
			double mAd_zje = 0;			// 这金额
			
			String mVdaf_ht = "";		// 合同硫分
			double mVdaf_kf = 0;		// 供方热量
			double mVdaf_cf = 0;		// 厂方热量
			double mVdaf_js = 0;		// 结算热量
			double mVdaf_yk = 0;		// 厂方盈亏
			double mVdaf_zdj = 0;		// 折单价
			double mVdaf_zje = 0;		// 这金额
			
			String mMt_ht = "";			// 合同硫分
			double mMt_kf = 0;			// 供方热量
			double mMt_cf = 0;			// 厂方热量
			double mMt_js = 0;			// 结算热量
			double mMt_yk = 0;			// 厂方盈亏
			double mMt_zdj = 0;			// 折单价
			double mMt_zje = 0;			// 这金额
			
			String mQgrad_ht = "";		// 合同硫分
			double mQgrad_kf = 0;		// 供方热量
			double mQgrad_cf = 0;		// 厂方热量
			double mQgrad_js = 0;		// 结算热量
			double mQgrad_yk = 0;		// 厂方盈亏
			double mQgrad_zdj = 0;		// 折单价
			double mQgrad_zje = 0;		// 这金额
			
			String mQbad_ht = "";		// 合同硫分
			double mQbad_kf = 0;		// 供方热量
			double mQbad_cf = 0;		// 厂方热量
			double mQbad_js = 0;		// 结算热量
			double mQbad_yk = 0;		// 厂方盈亏
			double mQbad_zdj = 0;		// 折单价
			double mQbad_zje = 0;		// 这金额
			
			String mHad_ht = "";		// 合同硫分
			double mHad_kf = 0;			// 供方热量
			double mHad_cf = 0;			// 厂方热量
			double mHad_js = 0;			// 结算热量
			double mHad_yk = 0;			// 厂方盈亏
			double mHad_zdj = 0;		// 折单价
			double mHad_zje = 0;		// 这金额
			
			String mStad_ht = "";		// 合同硫分
			double mStad_kf = 0;		// 供方热量
			double mStad_cf = 0;		// 厂方热量
			double mStad_js = 0;		// 结算热量
			double mStad_yk = 0;		// 厂方盈亏
			double mStad_zdj = 0;		// 折单价
			double mStad_zje = 0;		// 这金额
			
			String mStar_ht = ""; 		// 合同硫分
			double mStar_kf = 0; 		// 供方热量
			double mStar_cf = 0; 		// 厂方热量
			double mStar_js = 0; 		// 结算热量
			double mStar_yk = 0; 		// 厂方盈亏
			double mStar_zdj = 0; 		// 折单价
			double mStar_zje = 0; 		// 这金额
			
			String mMad_ht = ""; 		// 合同硫分
			double mMad_kf = 0; 		// 供方热量
			double mMad_cf = 0; 		// 厂方热量
			double mMad_js = 0; 		// 结算热量
			double mMad_yk = 0; 		// 厂方盈亏
			double mMad_zdj = 0; 		// 折单价
			double mMad_zje = 0; 		// 这金额
			
			String mAar_ht = ""; 		// 合同硫分
			double mAar_kf = 0; 		// 供方热量
			double mAar_cf = 0; 		// 厂方热量
			double mAar_js = 0;	 		// 结算热量
			double mAar_yk = 0; 		// 厂方盈亏
			double mAar_zdj = 0; 		// 折单价
			double mAar_zje = 0; 		// 这金额
			
			String mAad_ht = ""; 		// 合同硫分
			double mAad_kf = 0; 		// 供方热量
			double mAad_cf = 0; 		// 厂方热量
			double mAad_js = 0;	 		// 结算热量
			double mAad_yk = 0; 		// 厂方盈亏
			double mAad_zdj = 0; 		// 折单价
			double mAad_zje = 0; 		// 这金额
			
			String mVad_ht = ""; 		// 合同硫分
			double mVad_kf = 0; 		// 供方热量
			double mVad_cf = 0; 		// 厂方热量
			double mVad_js = 0; 		// 结算热量
			double mVad_yk = 0; 		// 厂方盈亏
			double mVad_zdj = 0; 		// 折单价
			double mVad_zje = 0; 		// 这金额
			
			String mT2_ht = ""; 		// 合同硫分
			double mT2_kf = 0; 			// 供方热量
			double mT2_cf = 0; 			// 厂方热量
			double mT2_js = 0; 			// 结算热量
			double mT2_yk = 0; 			// 厂方盈亏
			double mT2_zdj = 0; 		// 折单价
			double mT2_zje = 0; 		// 这金额
			
			String mYunju_ht = ""; 		// 合同运距
			double mYunju_kf = 0; 		// 供方热量
			double mYunju_cf = 0; 		// 厂方热量
			double mYunju_js = 0; 		// 结算热量
			double mYunju_yk = 0; 		// 厂方盈亏
			double mYunju_zdj = 0; 		// 折单价
			double mYunju_zje = 0; 		// 这金额
			
			long mhetb_id = 0;
			double mkoud_js = 0;
			double mjiessl = 0;
			double myunfjsl = 0;
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.13;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mtielzf = 0;
			double mkuangqyf = 0;
			double mkuangqzf = 0;
			double mkuangqsk = 0;
			double mkuangqjk = 0;
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
			String mjiesslblxsw = "0";
			String myunsfs = "";
			String mdiancjsbs = "";
			String mstrJieszb = "";
			boolean blnHasMeik = false;
			Money mn = new Money();
			Jiesdcz jsdcz = new Jiesdcz();
			double mjiesslcy = 0;
			long myunsfsb_id = 0;
			double myingd = 0;
			double mkuid = 0;
			String myunju = ""; 			// 运距
			double mfengsjj = 0; 			// 分公司加价
			double mjiajqdj = 0; 			// 加价前单价
			int mjijlx = 0; 				// 基价类型
			String mMjtokcalxsclfs = ""; 	// 兆焦转大卡小数处理方式
			String meikslfdcjs = ""; 		// 煤款数量分多次结算(是、否)
			double mkuidjfyf_je = 0; 		// 亏吨拒付运费金额
			double mkuidjfzf_je = 0; 		// 亏顿拒付杂费金额
			double mchaokdl = 0; 			// 超亏吨量
			String mchaokdlx = ""; 			// 超亏吨类型
			
			
			// 进行单批次结算时，要将每一个批次的结算情况保存起来，存入danpcjsmxb中，此时就产生了id
			// 结算时要判断有无这个id，如果有就一定要用这个id
			long mMeikjsb_id = 0;
			long mYunfjsb_id = 0;
			long mJihkjb_id = 0;
			
			// 煤款结算表中的运费关键信息
			double mYunfjsdj_mk = 0; 	// 运费结算单价(jiesb)
			double mYunzfhj_mk = 0; 	// 运杂费合计（jiesb）
			double mBuhsyf_mk = 0; 		// 不含税运费（jiesb）
			double mYunfjsl_mk = 0; 	// 运费结算数量(jiesb)
			// 煤款结算表中的运费关键信息_End
			
			String sql = "select * from diancjsmkb where bianm = '"
					+ getJiesbhValue().getValue().replaceAll("CG", "XS") + "' and fuid = 0";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				
				mid = rs.getLong("id");
				mtianzdw = rs.getString("xianshr");
				mjiesbh = rs.getString("bianm");
				mdiancjsbs = mjiesbh.substring(0, mjiesbh.indexOf("-") + 1);
				mfahdw = rs.getString("gongysmc");
				mfaz = rs.getString("faz");
				mshoukdw = rs.getString("shoukdw");
				mfahksrq = rs.getDate("fahksrq");
				mfahjzrq = rs.getDate("fahjzrq");
				myansksrq = rs.getDate("yansksrq");
				myansjzrq = rs.getDate("yansjzrq");
				mkaihyh = rs.getString("kaihyh");
				mpinz = rs.getString("meiz");
				myuanshr = rs.getString("yuanshr");
				mzhangh = rs.getString("zhangh");
				mches = rs.getInt("ches");
				mxianshr = rs.getString("xianshr");
				mdaibcc = rs.getString("daibch");
				myansbh = rs.getString("yansbh");
				mduifdd = rs.getString("duifdd");
				mfukfs = rs.getString("fukfs");
				mjiessl = rs.getDouble("jiessl");
				mbuhsdj = rs.getDouble("buhsdj");
				mjiakje = rs.getDouble("meikje");
				mbukyqjk = rs.getDouble("bukmk");
				mjiakhj = rs.getDouble("buhsmk");
				mjiaksl = rs.getDouble("shuil");
				mjiaksk = rs.getDouble("shuik");
				mjiasje = rs.getDouble("hansmk");
				mjieslx = rs.getInt("jieslx");
				mjiesslcy = rs.getDouble("jiesslcy");
				mdiancxxb_id = rs.getLong("diancxxb_id");
				mgongysb_id = rs.getLong("gongysb_id");
				mkoud_js = rs.getDouble("koud");
				myunsfsb_id = rs.getLong("yunsfsb_id");
				myingd = rs.getDouble("yingd");
				mkuid = rs.getDouble("kuid");
				myunju = rs.getString("yunj");
				mhetb_id = rs.getLong("HETB_ID");
				blnHasMeik = true;
				mfapbh = rs.getString("fapbh");
				mfengsjj = rs.getDouble("fengsjj");
				mjiajqdj = rs.getDouble("jiajqdj");
				mjijlx = rs.getInt("jijlx");
				mkuidjfyf_je = rs.getDouble("kuidjfyf"); 					// 亏吨拒付运费金额
				mkuidjfzf_je = rs.getDouble("kuidjfzf"); 					// 亏顿拒付杂费金额
				mchaokdl = Math.abs(rs.getDouble("chaokdl")); 				// 超亏吨量
				mchaokdlx = Jiesdcz.nvlStr(rs.getString("chaokdlx")); 		// 超亏吨类型
				mYunfjsdj_mk = rs.getDouble("Yunfhsdj");
				mYunzfhj_mk = rs.getDouble("hansyf");
				mBuhsyf_mk = rs.getDouble("buhsyf");
				mYunfjsl_mk = rs.getDouble("yunfjsl");
				
				// 得到煤款单价小数位，和运费单价小数位
				String str_meikhsdjblxsw = "";
				str_meikhsdjblxsw = Jiesdcz.getJiessz_item(rs
						.getLong("diancxxb_id"), rs.getLong("gongysb_id"), rs
						.getLong("hetb_id"), Locale.meikhsdjblxsw_jies, String.valueOf(getMeikhsdjblxsw()));
				
				if (str_meikhsdjblxsw != null && !str_meikhsdjblxsw.equals("")
						&& str_meikhsdjblxsw.matches("\\d+")) {

					this.setMeikhsdjblxsw(Integer.parseInt(str_meikhsdjblxsw));
				}
				
				sql = "select jieszbsjb.*, zhibb.bianm" +
					  "  from jieszbsjb, diancjsmkb js, zhibb" +
					  " where jieszbsjb.jiesdid = js.id " +
					  "   and zhibb.id = jieszbsjb.zhibb_id " + 
					  "   and js.bianm = '" + getJiesbhValue().getValue().replaceAll("CG", "XS") + 
					  "'  and jieszbsjb.zhuangt = 1 " +
					  " order by jieszbsjb.id";

				ResultSet rs2 = con.getResultSet(sql);
					
				while (rs2.next()) {
					
					if (rs2.getString("bianm").equals(Locale.jiessl_zhibb)) {

						mhetsl = rs2.getString("hetbz");
						mgongfsl = rs2.getDouble("gongf");
						mshulzjbz = rs2.getDouble("zhejbz");
						myanssl = rs2.getDouble("changf");
						myingksl = rs2.getDouble("yingk");
						mshulzjje = rs2.getDouble("zhejje");

					} else if (rs2.getString("bianm").equals(Locale.Shul_zhibb)) {

						mShulzb_ht = rs2.getString("hetbz");
						mShulzb_yk = rs2.getDouble("yingk");
						mShulzb_zdj = rs2.getDouble("zhejbz");
						mShulzb_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("数量(吨)", "Shulzb_ht",
								"Shulzb_kf", "Shulzb_cf", "Shulzb_js",
								"Shulzb_yk", "Shulzb_zdj", "Shulzb_zje",
								mShulzb_ht, mgongfsl, myanssl, mjiessl,
								mShulzb_yk, mShulzb_zdj, mShulzb_zje);

					} else if (rs2.getString("bianm").equals(
							Locale.Qnetar_zhibb)) {

						mQnetar_ht = rs2.getString("hetbz");// 合同热量
						mQnetar_kf = rs2.getDouble("gongf");// 供方热量
						mQnetar_cf = rs2.getDouble("changf");
						mQnetar_js = rs2.getDouble("jies");// 结算热量
						mQnetar_yk = rs2.getDouble("yingk");
						mQnetar_zdj = rs2.getDouble("zhejbz");
						mQnetar_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Qnetar(kcal/kg)",
								"Qnetar_ht", "Qnetar_kf", "Qnetar_cf",
								"Qnetar_js", "Qnetar_yk", "Qnetar_zdj",
								"Qnetar_zje", mQnetar_ht, MainGlobal
										.Mjkg_to_kcalkg(mQnetar_kf, 0),
								MainGlobal.Mjkg_to_kcalkg(mQnetar_cf, 0),
								MainGlobal.Mjkg_to_kcalkg(mQnetar_js, 0),
								mQnetar_yk, mQnetar_zdj, mQnetar_zje);

					} else if (rs2.getString("bianm").equals(Locale.Std_zhibb)) {

						mStd_ht = rs2.getString("hetbz"); // 合同硫分
						mStd_kf = rs2.getDouble("gongf");
						mStd_cf = rs2.getDouble("changf");
						mStd_js = rs2.getDouble("jies"); // 结算硫分
						mStd_yk = rs2.getDouble("yingk");
						mStd_zdj = rs2.getDouble("zhejbz");
						mStd_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Std(%)", "Std_ht",
								"Std_kf", "Std_cf", "Std_js", "Std_yk",
								"Std_zdj", "Std_zje", mStd_ht, mStd_kf,
								mStd_cf, mStd_js, mStd_yk, mStd_zdj, mStd_zje);

					} else if (rs2.getString("bianm").equals(Locale.Star_zhibb)) {

						mStar_ht = rs2.getString("hetbz"); // 合同挥发分
						mStar_kf = rs2.getDouble("gongf");
						mStar_cf = rs2.getDouble("changf");
						mStar_js = rs2.getDouble("jies"); // 结算挥发分
						mStar_yk = rs2.getDouble("yingk");
						mStar_zdj = rs2.getDouble("zhejbz");
						mStar_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Star(%)", "Star_ht",
								"Star_kf", "Star_cf", "Star_js", "Star_yk",
								"Star_zdj", "Star_zje", mStar_ht, mStar_kf,
								mStar_cf, mStar_js, mStar_yk, mStar_zdj,
								mStar_zje);

					} else if (rs2.getString("bianm").equals(Locale.Ad_zhibb)) {

						mAd_ht = rs2.getString("hetbz"); // 合同挥发分
						mAd_kf = rs2.getDouble("gongf");
						mAd_cf = rs2.getDouble("changf");
						mAd_js = rs2.getDouble("jies"); // 结算挥发分
						mAd_yk = rs2.getDouble("yingk");
						mAd_zdj = rs2.getDouble("zhejbz");
						mAd_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Ad(%)", "Ad_ht",
								"Ad_kf", "Ad_cf", "Ad_js", "Ad_yk", "Ad_zdj",
								"Ad_zje", mAd_ht, mAd_kf, mAd_cf, mAd_js,
								mAd_yk, mAd_zdj, mAd_zje);

					} else if (rs2.getString("bianm").equals(Locale.Vdaf_zhibb)) {

						mVdaf_ht = rs2.getString("hetbz"); // 合同挥发分
						mVdaf_kf = rs2.getDouble("gongf");
						mVdaf_cf = rs2.getDouble("changf");
						mVdaf_js = rs2.getDouble("jies"); // 结算挥发分
						mVdaf_yk = rs2.getDouble("yingk");
						mVdaf_zdj = rs2.getDouble("zhejbz");
						mVdaf_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Vdaf(%)", "Vdaf_ht",
								"Vdaf_kf", "Vdaf_cf", "Vdaf_js", "Vdaf_yk",
								"Vdaf_zdj", "Vdaf_zje", mVdaf_ht, mVdaf_kf,
								mVdaf_cf, mVdaf_js, mVdaf_yk, mVdaf_zdj,
								mVdaf_zje);

					} else if (rs2.getString("bianm").equals(Locale.Mt_zhibb)) {

						mMt_ht = rs2.getString("hetbz"); // 合同挥发分
						mMt_kf = rs2.getDouble("gongf");
						mMt_cf = rs2.getDouble("changf");
						mMt_js = rs2.getDouble("jies"); // 结算挥发分
						mMt_yk = rs2.getDouble("yingk");
						mMt_zdj = rs2.getDouble("zhejbz");
						mMt_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Mt(%)", "Mt_ht",
								"Mt_kf", "Mt_cf", "Mt_js", "Mt_yk", "Mt_zdj",
								"Mt_zje", mMt_ht, mMt_kf, mMt_cf, mMt_js,
								mMt_yk, mMt_zdj, mMt_zje);

					} else if (rs2.getString("bianm")
							.equals(Locale.Qgrad_zhibb)) {

						mQgrad_ht = rs2.getString("hetbz"); // 合同挥发分
						mQgrad_kf = rs2.getDouble("gongf");
						mQgrad_cf = rs2.getDouble("changf");
						mQgrad_js = rs2.getDouble("jies"); // 结算挥发分
						mQgrad_yk = rs2.getDouble("yingk");
						mQgrad_zdj = rs2.getDouble("zhejbz");
						mQgrad_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Qgrad(kcal/kg)",
								"Qgrad_ht", "Qgrad_kf", "Qgrad_cf", "Qgrad_js",
								"Qgrad_yk", "Qgrad_zdj", "Qgrad_zje",
								mQgrad_ht, MainGlobal.Mjkg_to_kcalkg(mQgrad_kf,
										0), MainGlobal.Mjkg_to_kcalkg(
										mQgrad_cf, 0), MainGlobal
										.Mjkg_to_kcalkg(mQgrad_js, 0),
								mQgrad_yk, mQgrad_zdj, mQgrad_zje);

					} else if (rs2.getString("bianm").equals(Locale.Qbad_zhibb)) {

						mQbad_ht = rs2.getString("hetbz"); // 合同挥发分
						mQbad_kf = rs2.getDouble("gongf");
						mQbad_cf = rs2.getDouble("changf");
						mQbad_js = rs2.getDouble("jies"); // 结算挥发分
						mQbad_yk = rs2.getDouble("yingk");
						mQbad_zdj = rs2.getDouble("zhejbz");
						mQbad_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Qbad(kcal/kg)",
								"Qbad_ht", "Qbad_kf", "Qbad_cf", "Qbad_js",
								"Qbad_yk", "Qbad_zdj", "Qbad_zje", mQbad_ht,
								MainGlobal.Mjkg_to_kcalkg(mQbad_kf, 0),
								MainGlobal.Mjkg_to_kcalkg(mQbad_cf, 0),
								MainGlobal.Mjkg_to_kcalkg(mQbad_js, 0),
								mQbad_yk, mQbad_zdj, mQbad_zje);

					} else if (rs2.getString("bianm").equals(Locale.Had_zhibb)) {

						mHad_ht = rs2.getString("hetbz"); // 合同挥发分
						mHad_kf = rs2.getDouble("gongf");
						mHad_cf = rs2.getDouble("changf");
						mHad_js = rs2.getDouble("jies"); // 结算挥发分
						mHad_yk = rs2.getDouble("yingk");
						mHad_zdj = rs2.getDouble("zhejbz");
						mHad_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Had(%)", "Had_ht",
								"Had_kf", "Had_cf", "Had_js", "Had_yk",
								"Had_zdj", "Had_zje", mHad_ht, mHad_kf,
								mHad_cf, mHad_js, mHad_yk, mHad_zdj, mHad_zje);

					} else if (rs2.getString("bianm").equals(Locale.Stad_zhibb)) {

						mStad_ht = rs2.getString("hetbz"); // 合同挥发分
						mStad_kf = rs2.getDouble("gongf");
						mStad_cf = rs2.getDouble("changf");
						mStad_js = rs2.getDouble("jies"); // 结算挥发分
						mStad_yk = rs2.getDouble("yingk");
						mStad_zdj = rs2.getDouble("zhejbz");
						mStad_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Stad(%)", "Stad_ht",
								"Stad_kf", "Stad_cf", "Stad_js", "Stad_yk",
								"Stad_zdj", "Stad_zje", mStad_ht, mStad_kf,
								mStad_cf, mStad_js, mStad_yk, mStad_zdj,
								mStad_zje);

					} else if (rs2.getString("bianm").equals(Locale.Mad_zhibb)) {

						mMad_ht = rs2.getString("hetbz"); // 合同挥发分
						mMad_kf = rs2.getDouble("gongf");
						mMad_cf = rs2.getDouble("changf");
						mMad_js = rs2.getDouble("jies"); // 结算挥发分
						mMad_yk = rs2.getDouble("yingk");
						mMad_zdj = rs2.getDouble("zhejbz");
						mMad_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Mad(%)", "Mad_ht",
								"Mad_kf", "Mad_cf", "Mad_js", "Mad_yk",
								"Mad_zdj", "Mad_zje", mMad_ht, mMad_kf,
								mMad_cf, mMad_js, mMad_yk, mMad_zdj, mMad_zje);

					} else if (rs2.getString("bianm").equals(Locale.Aar_zhibb)) {

						mAar_ht = rs2.getString("hetbz"); // 合同挥发分
						mAar_kf = rs2.getDouble("gongf");
						mAar_cf = rs2.getDouble("changf");
						mAar_js = rs2.getDouble("jies"); // 结算挥发分
						mAar_yk = rs2.getDouble("yingk");
						mAar_zdj = rs2.getDouble("zhejbz");
						mAar_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Aar(%)", "Aar_ht",
								"Aar_kf", "Aar_cf", "Aar_js", "Aar_yk",
								"Aar_zdj", "Aar_zje", mAar_ht, mAar_kf,
								mAar_cf, mAar_js, mAar_yk, mAar_zdj, mAar_zje);

					} else if (rs2.getString("bianm").equals(Locale.Aad_zhibb)) {

						mAad_ht = rs2.getString("hetbz"); // 合同挥发分
						mAad_kf = rs2.getDouble("gongf");
						mAad_cf = rs2.getDouble("changf");
						mAad_js = rs2.getDouble("jies"); // 结算挥发分
						mAad_yk = rs2.getDouble("yingk");
						mAad_zdj = rs2.getDouble("zhejbz");
						mAad_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Aad(%)", "Aad_ht",
								"Aad_kf", "Aad_cf", "Aad_js", "Aad_yk",
								"Aad_zdj", "Aad_zje", mAad_ht, mAad_kf,
								mAad_cf, mAad_js, mAad_yk, mAad_zdj, mAad_zje);

					} else if (rs2.getString("bianm").equals(Locale.Vad_zhibb)) {

						mVad_ht = rs2.getString("hetbz"); // 合同挥发分
						mVad_kf = rs2.getDouble("gongf");
						mVad_cf = rs2.getDouble("changf");
						mVad_js = rs2.getDouble("jies"); // 结算挥发分
						mVad_yk = rs2.getDouble("yingk");
						mVad_zdj = rs2.getDouble("zhejbz");
						mVad_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("Vad(%)", "Vad_ht",
								"Vad_kf", "Vad_cf", "Vad_js", "Vad_yk",
								"Vad_zdj", "Vad_zje", mVad_ht, mVad_kf,
								mVad_cf, mVad_js, mVad_yk, mVad_zdj, mVad_zje);

					} else if (rs2.getString("bianm").equals(Locale.T2_zhibb)) {

						mT2_ht = rs2.getString("hetbz"); // 合同挥发分
						mT2_kf = rs2.getDouble("gongf");
						mT2_cf = rs2.getDouble("changf");
						mT2_js = rs2.getDouble("jies"); // 结算挥发分
						mT2_yk = rs2.getDouble("yingk");
						mT2_zdj = rs2.getDouble("zhejbz");
						mT2_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("T2(%)", "T2_ht",
								"T2_kf", "T2_cf", "T2_js", "T2_yk", "T2_zdj",
								"T2_zje", mT2_ht, mT2_kf, mT2_cf, mT2_js,
								mT2_yk, mT2_zdj, mT2_zje);

					} else if (rs2.getString("bianm")
							.equals(Locale.Yunju_zhibb)) {

						mYunju_ht = rs2.getString("hetbz"); // 合同挥发分
						mYunju_kf = rs2.getDouble("gongf");
						mYunju_cf = rs2.getDouble("changf");
						mYunju_js = rs2.getDouble("jies"); // 结算挥发分
						mYunju_yk = rs2.getDouble("yingk");
						mYunju_zdj = rs2.getDouble("zhejbz");
						mYunju_zje = rs2.getDouble("zhejje");

						mstrJieszb += jsdcz.SetJieszb("运距(Km)", "Yunju_ht",
								"Yunju_kf", "Yunju_cf", "Yunju_js", "Yunju_yk",
								"Yunju_zdj", "Yunju_zje", mYunju_ht, mYunju_kf,
								mYunju_cf, mYunju_js, mYunju_yk, mYunju_zdj,
								mYunju_zje);

					}
					
					this.setJieszb(mstrJieszb);
				}
				
				rs2.close();
				mbeiz = rs.getString("beiz");
				// double mdanjc = 0;
				// 待定
				mranlbmjbr = rs.getString("ranlbmjbr");
				mranlbmjbrq = rs.getDate("ranlbmjbrq");
				// mkuidjf = 0;
				mjingz = rs.getDouble("guohl");
				mjiesrq = rs.getDate("jiesrq");
				mruzrq = rs.getDate("ruzrq");

				mjiesslblxsw = Jiesdcz.getJiessz_item(mdiancxxb_id,
						mgongysb_id, mhetb_id, Locale.jiesslblxsw_jies,
						mjiesslblxsw);
				if (mjiesslblxsw.equals("0")) {

					myuns = Math.round((mjiessl - mjingz));
				} else {

					String strblxs = "";
					for (int i = 0; i < Integer.parseInt(mjiesslblxsw); i++) {

						if (i == 0) {

							strblxs = "10";
						} else {

							strblxs += "0";
						}
					}
					myuns = Math.round((mjiessl - mjingz)
							* Double.parseDouble(strblxs))
							/ Double.parseDouble(strblxs);
				}
				
			}
			
//			if ("".equals(getJiesbhValue().getValue())) {
//
//				sql = " select * from " + table_yf + " where bianm = '"
//						+ getJiesbhValue().getValue() + "' and fuid = 0";
//
//				rs = con.getResultSet(sql);
//				if (rs.next()) {
//					myid = rs.getLong("id");
//					myunfhsdj = rs.getDouble("hansdj");
//					mtianzdw = rs.getString("xianshr");
//					mjiesbh = rs.getString("bianm");
//					mdiancjsbs = mjiesbh.substring(0, mjiesbh.indexOf("-") + 1);
//					mdiancxxb_id = rs.getLong("diancxxb_id");
//					mfahdw = rs.getString("gongysmc");
//					mfaz = rs.getString("faz");
//					mshoukdw = rs.getString("shoukdw");
//					mfahksrq = rs.getDate("fahksrq");
//					mfahjzrq = rs.getDate("fahjzrq");
//					myansksrq = rs.getDate("yansksrq");
//					myansjzrq = rs.getDate("yansjzrq");
//					mkaihyh = rs.getString("kaihyh");
//					mpinz = rs.getString("meiz");
//					myuanshr = rs.getString("yuanshr");
//					mgongfsl = rs.getDouble("gongfsl");
//					myanssl = rs.getDouble("yanssl");
//					myingksl = rs.getDouble("yingk");
//					mzhangh = rs.getString("zhangh");
//					mches = rs.getInt("ches");
//					mxianshr = rs.getString("xianshr");
//					mfapbh = rs.getString("fapbh");
//					mdaibcc = rs.getString("daibch");
//					myansbh = rs.getString("yansbh");
//					mduifdd = rs.getString("duifdd");
//					mfukfs = rs.getString("fukfs");
//					myunfjsl = rs.getDouble("jiessl");
//					mjiessl = rs.getDouble("jiessl");
//					mtielyf = rs.getDouble("guotyf");
//					mtielzf = rs.getDouble("guotzf");
//					mbukyqyzf = rs.getDouble("bukyf");
//					mjiskc = rs.getDouble("jiskc");
//					mbuhsyf = rs.getDouble("buhsyf");
//					myunfsl = rs.getDouble("shuil");
//					myunfsk = rs.getDouble("shuik");
//					myunzfhj = rs.getDouble("hansyf");
//					mkuangqyf = rs.getDouble("kuangqyf");
//					mkuangqzf = rs.getDouble("kuangqzf");
//					myunsfsb_id = rs.getLong("yunsfsb_id");
//					myingd = rs.getDouble("yingd");
//					mkuid = rs.getDouble("kuid");
//					myunju = rs.getString("yunj");
//					mjingz = rs.getDouble("guohl");
//					mjiesslcy = rs.getDouble("jiesslcy");
//					mshulzjbz = rs.getDouble("hansdj");
//					mkuidjfyf_je = rs.getDouble("kuidjfyf");
//					mkuidjfzf_je = rs.getDouble("kuidjfzf");
//
//					// 得到运费单价小数位
//					String str_yunfhsdjblxsw = "";
//					str_yunfhsdjblxsw = Jiesdcz.getJiessz_item(rs
//							.getLong("diancxxb_id"), rs.getLong("gongysb_id"),
//							rs.getLong("hetb_id"), Locale.yunfhsdjblxsw_jies,
//							String.valueOf(getYunfhsdjblxsw()));
//
//					if (str_yunfhsdjblxsw != null
//							&& !str_yunfhsdjblxsw.equals("")
//							&& str_yunfhsdjblxsw.matches("\\d+")) {
//
//						this.setYunfhsdjblxsw(Integer
//								.parseInt(str_yunfhsdjblxsw));
//					}
//				}
//			}
			
			if (((Visit) getPage().getVisit()).getDouble2() > 0
					|| ((Visit) getPage().getVisit()).getDouble3() > 0) {

				mkuangqyf = ((Visit) getPage().getVisit()).getDouble2();
				mkuangqzf = ((Visit) getPage().getVisit()).getDouble3();
				mkuangqsk = ((Visit) getPage().getVisit()).getDouble4();
				mkuangqjk = ((Visit) getPage().getVisit()).getDouble5();
				myunzfhj = Math.round((mtielyf + mtielzf + mkuangqyf
						+ mkuangqzf + mbukyqyzf) * 100) / 100;
				myunfsk = (double) Math
						.round(((double) Math.round((mtielyf + mbukyqyzf)
								* myunfsl * 100) / 100 + ((Visit) getPage()
								.getVisit()).getDouble4()) * 100) / 100;
				mbuhsyf = (double) Math
						.round(((double) Math.round((myunzfhj - myunfsk) * 100) / 100 + ((Visit) getPage()
								.getVisit()).getDouble5()) * 100) / 100;
			}
			
			mmeikhjdx = getDXMoney(mjiasje);
			myunzfhjdx = getDXMoney(myunzfhj);
			mhej = mjiasje + myunzfhj;
			mdaxhj = getDXMoney(mhej);

			// 设置超/亏吨的显示
			if (!mchaokdlx.equals("")) {
				// 说明存在超亏吨
				this.setHejdxh(jsdcz.SetHejdxh(mchaokdlx, mchaokdl, mhej,
						mdaxhj));
			} else {

				this.setHejdxh(jsdcz.SetHejdxh("", 0, mhej, mdaxhj));
			}
			
			_editvalues.add(new Dcbalancebean(mid, myid, mtianzdw, mjiesbh,
					mfahdw, mmeikdw, mfaz, myunsfsb_id, mshoukdw, mfahksrq,
					mfahjzrq, myansksrq, myansjzrq, mkaihyh, mpinz, myuanshr,
					mzhangh, mhetsl, mgongfsl, mches, mxianshr, Jiesdcz
							.nvlStr(mfapbh), mdaibcc, Jiesdcz.nvlStr(myansbh),
					mduifdd, mfukfs, mshulzjbz, myanssl, myingksl, myingd,
					mkuid, mshulzjje, mjiessl, mjiesslcy, myunfjsl, mbuhsdj,
					mjiakje, mbukyqjk, mjiakhj, mjiaksl, mjiaksk, mjiasje,
					//mtielyf, mtielzf, mkuangqyf, mkuangqzf,
					0, 0, 0, 0, 
					mkuangqsk, mkuangqjk, 0,//mbukyqyzf, 
					mjiskc, 
					//mbuhsyf, myunfsl, myunfsk, myunzfhj, 
					0, 0, 0, 0,
					mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq, mfahrq,
					mchangcwjbr, mchangcwjbrq, mruzrq, mjieszxjbr, mjieszxjbrq,
					mgongsrlbjbr, mgongsrlbjbrq, mhetjg, mjieslx, myuns,
					mkoud_js, myunsfs, mdiancjsbs, mhetb_id, myunju,
					mMeikjsb_id, mYunfjsb_id, mJihkjb_id, mfengsjj, mjiajqdj,
					mjijlx, mMjtokcalxsclfs, mkuidjfyf_je, mkuidjfzf_je,
					mchaokdl, mchaokdlx, myunfhsdj, mYunfjsdj_mk, mYunzfhj_mk,
					mBuhsyf_mk, mYunfjsl_mk
					//, meikslfdcjs
					,0,0,0));
			// 超吨亏吨标识符
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Dcbalancebean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}	
	
	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		// 结算类型
		tb1.addText(new ToolbarText("结算类型:"));
		ComboBox JieslxDropDown = new ComboBox();
		JieslxDropDown.setId("JIESLXDropDown");
		JieslxDropDown.setWidth(80);
		JieslxDropDown.setLazyRender(true);
		JieslxDropDown.setTransform("JIESLXDropDown");
		JieslxDropDown.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(JieslxDropDown);
		tb1.addText(new ToolbarText("-"));
		
		// 采购结算单编号
		tb1.addText(new ToolbarText("采购结算单编号:"));
		ComboBox JiesbhDropDown = new ComboBox();
		JiesbhDropDown.setId("JIESBHDropDown");
		JiesbhDropDown.setWidth(200);
		JiesbhDropDown.setLazyRender(true);
		JiesbhDropDown.setTransform("JIESBHDropDown");
		JiesbhDropDown.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(JiesbhDropDown);
		tb1.addText(new ToolbarText("-"));
		
		// 销售合同编号
		tb1.addText(new ToolbarText("合同编号:"));
		ComboBox HetbhDropDown = new ComboBox();
		HetbhDropDown.setId("HETBHDropDown");
		HetbhDropDown.setWidth(200);
		HetbhDropDown.setLazyRender(true);
		HetbhDropDown.setTransform("HETBHDropDown");
		tb1.addField(HetbhDropDown);
		tb1.addText(new ToolbarText("-"));
		
		// 刷新
		ToolbarButton shuaxbt = new ToolbarButton(null, "刷新",
				"function(){ document.Form0.RefreshButton.click();}");
		shuaxbt.setId("Shuaxbt");
		tb1.addItem(shuaxbt);
		tb1.addText(new ToolbarText("-"));
		
		// 生成
		ToolbarButton createbt = new ToolbarButton(null, "生成",
				"function(){ document.Form0.CreateButton.click(); }");
		createbt.setId("createbt");
		tb1.addItem(createbt);
		tb1.addText(new ToolbarText("-"));
		
		// 删除
		ToolbarButton deletebt = new ToolbarButton(null, "删除",
				"function(){ document.Form0.DeleteButton.click(); }");
		deletebt.setId("deletebt");
		tb1.addItem(deletebt);
		tb1.addText(new ToolbarText("-"));
		
		// 保存
		ToolbarButton savebt = new ToolbarButton(null, "保存",
				"function(){ document.Form0.SaveButton.click(); }");
		savebt.setId("savebt");
		tb1.addItem(savebt);
		tb1.addText(new ToolbarText("-"));
		
		// 返回
		ToolbarButton returnbt = new ToolbarButton(null, "返回",
				"function(){ document.Form0.RetrunsButton.click(); }");
		savebt.setId("returnbt");
		tb1.addItem(returnbt);
		
		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(0); // 状态 判断是修改合同号还是提交进入流程
			_JiesrqsmallValue = new Date();
			_JiesrqbigValue = new Date();
			visit.setboolean2(false);// 分公司
			visit.setboolean3(false);// 电厂
			visit.setString2(""); // 结算指标页面显示
			((Visit) getPage().getVisit()).setString11(""); // 用于Kuangqzf返回时，跳转到正确的界面(DCBalance,Jiesdxg)

			((Visit) getPage().getVisit()).setString2(""); // 指标显示
			((Visit) getPage().getVisit()).setString3(""); // 合计大写行显示内容（为实现动态设置“超扣吨”显示用）
			((Visit) getPage().getVisit()).setString4(""); // 结算单位（用来保存页面设置的结算单位，可能是
															// 电厂id 或 分公司id），
			// 在结算编号下拉框和getselectdate()、save()、Submit()、Delete()
			// 方法中进行判断时使用

			((Visit) getPage().getVisit()).setString5(""); // 用于处理
															// 填写增扣款后是否重算结算单价
			((Visit) getPage().getVisit()).setInt2(2); // 煤款含税单价保留小数位
			((Visit) getPage().getVisit()).setInt3(2); // 运费含税单价保留小数位

			if (visit.getRenyjb() < 3) {

				visit.setboolean3(true);
			}	
			
			setJieslxValue(null);
			setIJieslxModel(null);
			setJiesbhValue(null);
			setIJiesbhModel(null);
			setHetbhValue(null);
			setIHetbhModel(null);
			
			visit.setboolean1(false);//共用(目前是电厂数)
			visit.setboolean5(false);//合同编号显示
			visit.setboolean6(false);//流程名称显示
			
			if (cycle.getRequestContext().getParameters("jsdwid") != null) {

				((Visit) this.getPage().getVisit()).setString4(cycle
						.getRequestContext().getParameters("jsdwid")[0].trim());
			}
			
			getIShoukdwModels();
			getIJiesbhModels();
			getSelectData();
			getToolbars();
		}
		
		if (visit.getboolean1()) {
			visit.setboolean1(false);
			setJiesbhValue(null);
			setIJiesbhModel(null);
			getIJiesbhModels();
		}
		
		if (visit.getboolean8()) {
			getIJiesbhModels();
			visit.setboolean8(false);
		}
		
		if (visit.getboolean7()) {
			getIHetbhModels();
			visit.setboolean7(false);
		}
		
	}
	
	public void setJieszb(String value) {

		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getJieszb() {

		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setHejdxh(String value) {

		((Visit) getPage().getVisit()).setString3(value);
	}

	public String getHejdxh() {

		return ((Visit) getPage().getVisit()).getString3();
	}

	// 工具条_begin
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	// 工具条_end
		
	// 收款单位 DropDownBean9
	// 收款单位 ProSelectionModel9
    public IDropDownBean getShoukdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {

			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getIShoukdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setShoukdwValue(IDropDownBean value) {
		if (((Visit) getPage().getVisit()).getDropDownBean9() != value) {

			((Visit) getPage().getVisit()).setDropDownBean9(value);
		}
	}

	public void setIShoukdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}
    
    public IPropertySelectionModel getIShoukdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {

			getIShoukdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
    
    public IPropertySelectionModel getIShoukdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			String sql = "select shoukdw from (	\n"
					+ " select distinct gongfdwmc as shoukdw from hetb h	\n"
					+ " union	\n"
					+ " select distinct quanc as shoukdw from shoukdw	\n"
					+ " union	\n"
					+ " select gongfdwmc as shoukdw from hetys \n"
					+ " ) order by shoukdw";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("shoukdw")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
    
	// 结算类型 DropDownBean8
	// 结算类型 ProSelectionModel8
    public IDropDownBean getJieslxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {

			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getIJieslxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setJieslxValue(IDropDownBean value) {
		if (((Visit) getPage().getVisit()).getDropDownBean8() != value
				&& ((Visit) getPage().getVisit()).getDropDownBean8() != null) {
			((Visit) getPage().getVisit()).setboolean8(true);
		}
		
		if (((Visit) getPage().getVisit()).getDropDownBean8() != value) {
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setIJieslxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}
    
    public IPropertySelectionModel getIJieslxModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {

			getIJieslxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
    
    public IPropertySelectionModel getIJieslxModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			String sql = "select jieslx from (	\n"
					+ " select '煤款' as jieslx from dual	\n"
					+ " union	\n"
					+ " select '矿区运费' as jieslx from dual	\n"
					+ " ) order by jieslx";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("jieslx")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel8(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
    
    // 采购结算单编号 DropDownBean6
	// 采购结算单编号 ProSelectionModel6
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
		String tablename = "kuangfjsmkb";
		
		if ("矿区运费".equals(getJieslxValue().getValue())) {
			tablename = "kuangfjsyfb";
		}
		
		try {
			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));

			sql = "select bianm from " + tablename + " where diancjsmkb_id is null order by bianm";

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
		if (((Visit) getPage().getVisit()).getDropDownBean6() != value
				&& ((Visit) getPage().getVisit()).getDropDownBean6() != null) {
			((Visit) getPage().getVisit()).setboolean7(true);
		}
		
		if (((Visit) getPage().getVisit()).getDropDownBean6() != value) {
			((Visit) getPage().getVisit()).setDropDownBean6(value);
		}
	}
	
    // 销售合同编号 DropDownBean7
	// 销售合同编号 ProSelectionModel7
	public void setIHetbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getIHetbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getIHetbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}
    
    public IPropertySelectionModel getIHetbhModels() {
    	
		String sql = "";
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		String tablename = "kuangfjsmkb";
		
		if ("矿区运费".equals(getJieslxValue().getValue())) {
			tablename = "kuangfjsyfb";
		}
		
		sql = "select diancxxb_id, gongysb_id, to_char(fahksrq, 'yyyy-mm-dd') as fahksrq, to_char(fahjzrq, 'yyyy-mm-dd') as fahjzrq from "
				+ tablename + " where bianm = '" + getJiesbhValue().getValue()
				+ "'";
		ResultSetList rsl = con.getResultSetList(sql);
		long diancxxb_id = 0;
		long gongysb_id = 0;
		String fahksrq = "";
		String fahjzrq = "";
		if (rsl.next()) {
			diancxxb_id = rsl.getLong("diancxxb_id");
			gongysb_id = rsl.getLong("gongysb_id");
			fahksrq = rsl.getString("fahksrq");
			fahjzrq = rsl.getString("fahjzrq");
		}
		rsl.close();
		
		try {
			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));

			sql = "select hetbh from hetb where leib = 1 and diancxxb_id = "
					+ diancxxb_id + " and gongysb_id = " + gongysb_id
					+ " and qisrq <= to_date('" + fahksrq
					+ "', 'yyyy-mm-dd') and guoqrq >= to_date('" + fahjzrq
					+ "', 'yyyy-mm-dd') order by hetbh";

			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("hetbh")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}
    
    public IDropDownBean getHetbhValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {

			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getIHetbhModel()
							.getOption(0));
		}

		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setHetbhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean7() != value) {

			((Visit) getPage().getVisit()).setDropDownBean7(value);
		}
	}
	
//***************************************************************************//
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
	
	public IPropertySelectionModel getIFahdwModels() {

		String sql = "select id, quanc from gongysb";
		
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
    // ********************************************************************************//
    
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