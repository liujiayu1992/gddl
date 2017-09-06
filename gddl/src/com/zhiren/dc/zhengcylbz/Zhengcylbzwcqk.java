package com.zhiren.dc.zhengcylbz;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhengcylbzwcqk extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO 自动生成方法存根
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
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01" + "-01");
		String sql = "select id from ZHENGCYLBZBHZB where diancxxb_id = " + getTreeid() + "and riq = " + CurrODate;
		ResultSetList rsl = con.getResultSetList(sql);
		if(!rsl.next()) {
			setMsg("请先录入标准！");
			return;
		}
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sb = new StringBuffer("begin \n");
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		while(mdrsl.next()){
			sql1 = "update zhengcylbzbwcqkb set biaoz = " + mdrsl.getDouble("bmdjbz") + ", shijwc = " + mdrsl.getDouble("bmdjwc")
					+ " ,def = " + mdrsl.getDouble("bmdjdf") +" where id = " + mdrsl.getLong("bhsbmdj_id") + ";";
			sql2 = "update zhengcylbzbwcqkb set biaoz = " + mdrsl.getDouble("mzbz") + ", shijwc = " + mdrsl.getDouble("mzwc")
					+ ", def = " + mdrsl.getDouble("mzdf") + " where id = " + mdrsl.getLong("mz_id") + ";";
			sql3 = "update zhengcylbzbwcqkb set biaoz = " + mdrsl.getDouble("rzcbz") + ", shijwc = " +mdrsl.getDouble("rzcwc")
					+ ", def = " + mdrsl.getDouble("rzcdf") + " where id= " +mdrsl.getLong("rzc_id") + ";";
			sb.append(sql1).append(sql2).append(sql3);
		}
		sb.append("end;");
		if(sb.length() < 13) {
			con.Close();
		} else {
			con.getUpdate(sb.toString());
		}
		mdrsl.close();
		con.Close();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	// 生成按钮
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	private boolean _DeleteClick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;
		}

		if (_DeleteClick) {
			_DeleteClick = false;
			delete();
		}

		if (_CreateClick) {
			_CreateClick = false;
			create();
		}
		getSelectData();
	}

	public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
	}

	public String getYuef() {
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		return StrMonth;
	}
	
//标煤单价
	public String getBiaomdj(String diancxxb_id, String date) {
		String sql = "select hz.id, bz.zhengcylzb_item_id, bz.biaoz, Round_new(wanc,5) as wanc, Round_new((10+(bz.biaoz-bmdj.wanc)/bz.biaoz*100)*15,5) as def from "
			+ "zhengcylbzb bz, zhengcylbzbhzb hz, item i, (select diancxxb_id, decode(sum(jiesl), 0, 0, sum(biaomdj * jiesl) / sum(jiesl)) as wanc\n"
			+ "from yuetjkjb y, yuejsbmdj j where y.id = j.yuetjkjb_id and j.fenx = '累计' and diancxxb_id = "+diancxxb_id+" and riq = "
			+ date
			+ "\n"
			+ "group by diancxxb_id,riq) bmdj\n"
			+ "where hz.diancxxb_id = bz.diancxxb_id(+) and hz.diancxxb_id = bmdj.diancxxb_id(+) and bz.zhengcylzb_item_id = i.id and i.bianm = 'bhsbmdj' and bz.diancxxb_id \n" +
			"= hz.diancxxb_id and hz.diancxxb_id = "+ diancxxb_id + " and bz.riq = hz.riq and to_char(bz.riq,'yyyy') = " + getNianf();
		return sql;
	}
	
	public String getMeiz(String diancxxb_id, String date) {
		String sql = "select hz.id, bz.zhengcylzb_item_id, bz.biaoz, Round_new(mz.wanc,5) as wanc, Round_new((10-(bz.biaoz-mz.wanc)/bz.biaoz*100)*15,5) as def from\n"
			+ "zhengcylbzb bz, zhengcylbzbhzb hz, item i,(select diancxxb_id, decode(sum(jincsl), 0, 0, sum(changffrl * jincsl) / sum(jincsl)) as wanc\n"
			+ "from diaor04bb  where fenx = '累计' and diancxxb_id = "+diancxxb_id+" and riq = "
			+ date
			+ " group by diancxxb_id,riq) mz\n"
			+ "where hz.diancxxb_id = bz.diancxxb_id(+) and hz.diancxxb_id = mz.diancxxb_id(+) and bz.zhengcylzb_item_id = i.id and i.bianm = 'mz' and bz.diancxxb_id = \n"
			+ " hz.diancxxb_id and hz.diancxxb_id = "+ diancxxb_id + " and bz.riq = hz.riq and to_char(bz.riq,'yyyy') = " + getNianf();
		return sql;
	}
	
	public String getRezc(String diancxxb_id, String date) {
		String sql = "select hz.id, bz.zhengcylzb_item_id, bz.biaoz, Round_new((mz.wanc-zonghm)*1000,5) as wanc, Round_new((10-((mz.wanc-zonghm)*1000-502)/100)*10,5) as def from\n"
			+ "zhengcylbzb bz, zhengcylbzbhzb hz, item i,(select diancxxb_id, decode(sum(jincsl), 0, 0, sum(changffrl * jincsl) / sum(jincsl)) as wanc\n"
			+ "from diaor04bb  where fenx = '累计' and diancxxb_id = "+diancxxb_id+" and riq = "
			+ date
			+ " group by diancxxb_id,riq) mz,\n"
			+ "(select diancxxb_id, zonghm from diaor01bb where fenx = '累计' and diancxxb_id = "+diancxxb_id+" and riq = "
			+ date
			+ ") zhc\n"
			+ "where hz.diancxxb_id = bz.diancxxb_id(+) and hz.diancxxb_id = mz.diancxxb_id(+) and hz.diancxxb_id = zhc.diancxxb_id(+) and bz.zhengcylzb_item_id = i.id and i.bianm = 'rzc' \n" +
			"and bz.diancxxb_id = hz.diancxxb_id and hz.diancxxb_id = "+ diancxxb_id + " and bz.riq = hz.riq and to_char(bz.riq,'yyyy') = " + getNianf();
		return sql;
	}
	
//	1、从TreeId里得到标准，赋值给变量。如果TreeId对应的标准表没有数据提示，不生成
//	2、计算TreeId下面各厂的计划、标准、得分。如果下属电厂没有标准，默认执行Treeid标准
	public void create() {
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef()
				+ "-01");
		String Date = DateUtil.FormatOracleDate(getNianf() + "-01" + "-01");
		
		double Biaomdj_bz = 0;
        double Meiz_bz = 0;
        double Rezc_bz = 0;
        long Biaomdj_hzid = 0;
        long Meiz_hzid = 0;
        long Rezc_hzid = 0;
        long Biaomdj_item_id = 0;
        long Meiz_item_id = 0;
        long Rezc_item_id = 0;
        double biaomdj_wc = 0;
        double biaomdj_df = 0;
		double meiz_wc = 0;
		double meiz_df = 0;
		double rezc_wc = 0;
		double rezc_df = 0;
		double Array1[][] = null;
		double Array2[][] = null;
		double Array3[][] = null;
		ResultSetList rsl = null;
		ResultSetList rsls = null;
		StringBuffer sb = new StringBuffer();
		int i = 0;
		
		long id = 0;
		String sql = "select hz.id from zhengcylbzbhzb hz, zhengcylbzb bz where bz.diancxxb_id = hz.diancxxb_id and hz.diancxxb_id = "+ getTreeid() 
			+ " and bz.riq = hz.riq and to_char(hz.riq,'yyyy') = " + getNianf();
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			id = rsl.getLong("id");
		} else {
			setMsg("请先录入标准！");
			return;
		}
		
		String sqls = "select id from diancxxb where fuid = "+getTreeid();
		String sqlD = "";
		sql = "select id from zhengcylbzbhzb where riq = " + Date;
		rsl = con.getResultSetList(sqls);
		rsls = con.getResultSetList(sql);
		sb.append("begin\n");
		if (rsl.next()) {
			while(rsls.next()) {
				sqlD = "delete from zhengcylbzbwcqkb where zhengcylbzbhzb_id = " + rsls.getLong("id")+";";
				sb.append(sqlD);
			}
			sb.append("end;");
			if(sb.length() < 13) {
				con.Close();
			} else {
				con.getDelete(sb.toString());
			}
			sb.setLength(0);
		} else {
			sqlD = "delete from zhengcylbzbwcqkb where zhengcylbzbhzb_id = "+ id;
			con.getDelete(sqlD);
		}
		
		rsl = con.getResultSetList(getBiaomdj(getTreeid(), CurrODate));
    	if (rsl.next()) {
    		Biaomdj_item_id = rsl.getLong("zhengcylzb_item_id");
    	}
    	rsl = con.getResultSetList(getMeiz(getTreeid(), CurrODate)); 
		if (rsl.next()) {
			Meiz_item_id = rsl.getLong("zhengcylzb_item_id");
    	}
		rsl = con.getResultSetList(getRezc(getTreeid(), CurrODate)); 
		if (rsl.next()) {
			Rezc_item_id = rsl.getLong("zhengcylzb_item_id");
    	}
		
		sql = "select id from diancxxb where id not in (select diancxxb_id from zhengcylbzbhzb where to_char(riq,'yyyy') = "+getNianf()+") " +
				"and (id = "+getTreeid()+" or fuid = "+getTreeid()+")";
		rsl = con.getResultSetList(sql);
		sb.append("begin\n");
		String sqlq = "";
		String sqlS = "";
		while(rsl.next()) {
			sql = "insert into zhengcylbzbhzb(id, diancxxb_id, riq) values(getNewID("+getTreeid()+"), "+rsl.getLong("id")+", "+Date+");";
			sqlS = "insert into ZHENGCYLBZB(id, diancxxb_id, riq, zhengcylzb_item_id, danwb_id, biaoz, gongs, beiz)\n"
				+ "values(getNewID("+ getTreeid() + "), "+ rsl.getLong("id") + ", "+ Date+ ", "+ Biaomdj_item_id + " , 9, 0, '', '');";
			sqlD = "insert into ZHENGCYLBZB(id, diancxxb_id, riq, zhengcylzb_item_id, danwb_id, biaoz, gongs, beiz)\n"
				+ "values(getNewID("+ getTreeid() + "), "+ rsl.getLong("id") + ", "+ Date+ ", "+ Meiz_item_id + " , 6, 0, '', '');";
			sqlq = "insert into ZHENGCYLBZB(id, diancxxb_id, riq, zhengcylzb_item_id, danwb_id, biaoz, gongs, beiz)\n"
				+ "values(getNewID("+ getTreeid() + "), "+ rsl.getLong("id") + ", "+ Date+ ", "+ Rezc_item_id + " , 27, 0, '', '');";
			sb.append(sql).append(sqlS).append(sqlD).append(sqlq);
		}
		sb.append("end;");
		if(sb.length() < 13) {
			con.Close();
		} else {
			con.getInsert(sb.toString());
		}
		
//		取分公司的三个标准
        rsls = con.getResultSetList(sqls);
        
        Array1 = new double[rsls.getRows()][4];
        Array2 = new double[rsls.getRows()][4];
        Array3 = new double[rsls.getRows()][4];
        if(rsls.getRows() > 0) {
        	rsl = con.getResultSetList(getBiaomdj(getTreeid(), CurrODate));
        	if (rsl.next()) {
        		Biaomdj_bz = rsl.getDouble("biaoz");
        		Biaomdj_hzid = rsl.getLong("id");
        	}   		
    		while (rsls.next()) {
            	rsl = con.getResultSetList(getBiaomdj(rsls.getString("id"), CurrODate));
        		if(rsl.next()) { 
        			Array1[i][0] = rsl.getDouble("id");
        			Array1[i][1] = rsl.getDouble("biaoz")==0?Biaomdj_bz:rsl.getDouble("biaoz");
        			Array1[i][2] = rsl.getDouble("wanc");
        			Array1[i][3] = rsl.getDouble("def");
        			
        		}
        		biaomdj_wc += Array1[i][2];
        		biaomdj_df += Array1[i][3];
        		i++;
    		}
    		biaomdj_wc = biaomdj_wc / rsls.getRows();
    		biaomdj_df = biaomdj_df / rsls.getRows();
    		
    		i = 0;
    		rsl = con.getResultSetList(getMeiz(getTreeid(), CurrODate));
    		rsls = con.getResultSetList(sqls);  
    		if (rsl.next()) {
    			Meiz_bz = rsl.getDouble("biaoz");
    			Meiz_hzid = rsl.getLong("id");
        	}   		
    		while (rsls.next()) {
            	rsl = con.getResultSetList(getMeiz(rsls.getString("id"), CurrODate));
        		if(rsl.next()) {  
        			Array2[i][0] = rsl.getDouble("id");
        			Array2[i][1] = rsl.getDouble("biaoz")==0?Meiz_bz:rsl.getDouble("biaoz");
        			Array2[i][2] = rsl.getDouble("wanc");
        			Array2[i][3] = rsl.getDouble("def");
        			
        		}
        		meiz_wc += Array2[i][2];
        		meiz_df += Array2[i][3];
        		i++;
    		}
    		meiz_wc = meiz_wc / rsls.getRows();
    		meiz_df = meiz_df / rsls.getRows();
    		
    		i = 0;
    		rsl = con.getResultSetList(getRezc(getTreeid(), CurrODate));
    		rsls = con.getResultSetList(sqls);  
    		if (rsl.next()) {
    			Rezc_bz = rsl.getDouble("biaoz");
    			Rezc_hzid = rsl.getLong("id");
        	}   		
    		while (rsls.next()) {
            	rsl = con.getResultSetList(getRezc(rsls.getString("id"), CurrODate));
        		if(rsl.next()) { 
        			Array3[i][0] = rsl.getDouble("id");
        			Array3[i][1] = rsl.getDouble("biaoz")==0?Rezc_bz:rsl.getDouble("biaoz");
        			Array3[i][2] = rsl.getDouble("wanc");
        			Array3[i][3] = rsl.getDouble("def");
        			
        		}
        		rezc_wc += Array3[i][2];
        		rezc_df += Array3[i][3];
        		i++;
    		}
    		rezc_wc = rezc_wc / rsls.getRows();
    		rezc_df = rezc_df / rsls.getRows();
        }
        
        rsls = con.getResultSetList(sqls);
        sb.setLength(0);
		sb.append("begin\n");
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		if(rsls.getRows()>0) {
			for(i = 0; i < rsls.getRows(); i++) {
				sql1 = "insert into zhengcylbzbwcqkb(id, zhengcylbzbhzb_id, zhengcylzb_item_id, biaoz, shijwc, def)\n"
					+ "values(getNewID(" + getTreeid() + "), " + Array1[i][0] + ", "+ Biaomdj_item_id + ","+ Array1[i][1] + "," + Array1[i][2] + ","
					+ Array1[i][3] + ");";
				sql2 = "insert into zhengcylbzbwcqkb(id, zhengcylbzbhzb_id, zhengcylzb_item_id, biaoz, shijwc, def)\n"
					+ "values(getNewID(" + getTreeid() + "), " + Array2[i][0] + ", "+ Meiz_item_id + ","+ Array2[i][1] + "," + Array2[i][2] + ","
					+ Array2[i][3] + ");";
				sql3 = "insert into zhengcylbzbwcqkb(id, zhengcylbzbhzb_id, zhengcylzb_item_id, biaoz, shijwc, def)\n"
					+ "values(getNewID(" + getTreeid() + "), " + Array3[i][0] + ", "+ Rezc_item_id + ","+ Array3[i][1] + "," + Array3[i][2] + ","
					+ Array3[i][3] + ");";
				sb.append(sql1).append(sql2).append(sql3);
			}
			sql1 = "insert into zhengcylbzbwcqkb(id, zhengcylbzbhzb_id, zhengcylzb_item_id, biaoz, shijwc, def)\n"
				+ "values(getNewID(" + getTreeid() + "), " + Biaomdj_hzid + ", "+ Biaomdj_item_id + ","+ Biaomdj_bz + "," + biaomdj_wc + ","
				+ biaomdj_df + ");";
			sql2 = "insert into zhengcylbzbwcqkb(id, zhengcylbzbhzb_id, zhengcylzb_item_id, biaoz, shijwc, def)\n"
				+ "values(getNewID(" + getTreeid() + "), " + Meiz_hzid + ", "+ Meiz_item_id + ","+ Meiz_bz + "," + meiz_wc + ","
				+ meiz_df + ");";
			sql3 = "insert into zhengcylbzbwcqkb(id, zhengcylbzbhzb_id, zhengcylzb_item_id, biaoz, shijwc, def)\n"
				+ "values(getNewID(" + getTreeid() + "), " + Rezc_hzid + ", "+ Rezc_item_id + ","+ Rezc_bz + "," + rezc_wc + ","
				+ rezc_df + ");";
			sb.append(sql1).append(sql2).append(sql3);
		} else {
			sql1 = "insert into zhengcylbzbwcqkb(id, zhengcylbzbhzb_id, zhengcylzb_item_id, biaoz, shijwc, def)\n"
				+ "values(getNewID(" + getTreeid() + "), " + Array1[i][0] + ", "+ Biaomdj_item_id + ","+ Array1[i][1] + "," + Array1[i][2] + ","
				+ Array1[i][3] + ");";
			sql2 = "insert into zhengcylbzbwcqkb(id, zhengcylbzbhzb_id, zhengcylzb_item_id, biaoz, shijwc, def)\n"
				+ "values(getNewID(" + getTreeid() + "), " + Array2[i][0] + ", "+ Meiz_item_id + ","+ Array2[i][1] + "," + Array2[i][2] + ","
				+ Array2[i][3] + ");";
			sql3 = "insert into zhengcylbzbwcqkb(id, zhengcylbzbhzb_id, zhengcylzb_item_id, biaoz, shijwc, def)\n"
				+ "values(getNewID(" + getTreeid() + "), " + Array3[i][0] + ", "+ Rezc_item_id + ","+ Array3[i][1] + "," + Array3[i][2] + ","
				+ Array3[i][3] + ");";
			sb.append(sql1).append(sql2).append(sql3);
		}
		sb.append("end;");
		if(sb.length() < 13) {
			con.Close();
		} else {
			con.getInsert(sb.toString());
		}
		rsls.close();
		rsl.close();
		con.Close();
	}

	public void delete() {
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil
				.FormatOracleDate(getNianf() + "-01" + "-01");
		String sqlD = "";
		long id = 0;
		String sqls = "select wc.* from zhengcylbzbwcqkb wc, zhengcylbzbhzb hz where wc.zhengcylbzbhzb_id = hz.id \n"
				+ "and hz.diancxxb_id = "
				+ getTreeid()
				+ " and hz.riq = "
				+ CurrODate;
		ResultSetList rsl = con.getResultSetList(sqls);
		if (rsl.next()) {
			id = rsl.getLong("zhengcylbzbhzb_id");
		}
		sqls = "select id from diancxxb where fuid = "+getTreeid();
		sqlD = "";
		String sql = "select id from zhengcylbzbhzb where riq = " + CurrODate;
		rsl = con.getResultSetList(sqls);
		ResultSetList rsls = con.getResultSetList(sql);
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		if (rsl.next()) {
			while(rsls.next()) {
				sqlD = "delete from zhengcylbzbwcqkb where zhengcylbzbhzb_id = " + rsls.getLong("id")+";";
				sb.append(sqlD);
			}
			sb.append("end;");
			if(sb.length() < 13) {
				con.Close();
			} else {
				con.getDelete(sb.toString());
			}
			sb.setLength(0); 
		}else {
			sqlD = "delete from zhengcylbzbwcqkb where zhengcylbzbhzb_id = "+ id;
			con.getDelete(sqlD);
		}
		rsl.close();
		rsls.close();
		con.Close();
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String riq = DateUtil.FormatOracleDate(getNianf() + "-01" + "-01");
		String sql = "select bhsbmdj_id, d.mingc, nvl(a.biaoz,0) as bmdjbz, Round_new(nvl(a.shijwc,0),3) as bmdjwc, Round_new(nvl(a.def,0),3) as bmdjdf, mz_id, nvl(b.biaoz,0) as mzbz, Round_new(nvl(b.shijwc,0),3) " +
			"as mzwc, Round_new(nvl(b.def,0),3) as mzdf, rzc_id, nvl(c.biaoz,0) as rzcbz, Round_new(nvl(c.shijwc,0),3) as rzcwc, Round_new(nvl(c.def,0),3) as rzcdf\n" +
			"from\n" + 
			"(select wc.id as bhsbmdj_id, d.*, wc.biaoz, wc.shijwc, wc.def from zhengcylbzbwcqkb wc, zhengcylbzbhzb hz, item i, diancxxb d where wc.zhengcylbzbhzb_id = hz.id and\n" + 
			"wc.zhengcylzb_item_id = i.id and i.bianm = 'bhsbmdj' and hz.diancxxb_id = d.id and (d.id = "+getTreeid()+" or d.fuid = "+getTreeid()+") and hz.riq = "+riq+") a,\n" + 
			"(select wc.id as mz_id, d.*, wc.biaoz, wc.shijwc, wc.def from zhengcylbzbwcqkb wc, zhengcylbzbhzb hz, item i, diancxxb d where wc.zhengcylbzbhzb_id = hz.id and\n" + 
			"wc.zhengcylzb_item_id = i.id and i.bianm = 'mz' and hz.diancxxb_id = d.id and (d.id = "+getTreeid()+" or d.fuid = "+getTreeid()+") and hz.riq = "+riq+") b,\n" + 
			"(select wc.id as rzc_id, d.*,wc.biaoz, wc.shijwc, wc.def from zhengcylbzbwcqkb wc, zhengcylbzbhzb hz, item i, diancxxb d where wc.zhengcylbzbhzb_id = hz.id and\n" + 
			"wc.zhengcylzb_item_id = i.id and i.bianm = 'rzc' and hz.diancxxb_id = d.id and (d.id = "+getTreeid()+" or d.fuid = "+getTreeid()+") and hz.riq = "+riq+") c,\n" + 
			"(select * from diancxxb order by jib) d\n" + 
			"where d.id=a.id(+) and d.id=b.id(+) and d.id=c.id(+) and (d.id = "+getTreeid()+" or d.fuid = "+getTreeid()+")";
        
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight - 150");
		// 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("zhengcylbzbwcqkb");
		egu.getColumn("bhsbmdj_id").setHeader("ID");
		egu.getColumn("bhsbmdj_id").setHidden(true);
		egu.getColumn("bhsbmdj_id").editor = null;
		egu.getColumn("mingc").setHeader("单位名称");
		egu.getColumn("mingc").editor = null;
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("bmdjbz").setHeader("计划标准");
		egu.getColumn("bmdjbz").editor = null;
		egu.getColumn("bmdjbz").setWidth(74);
//		((NumberField)(egu.getColumn("bmdjbz").editor)).setDecimalPrecision(3);
		egu.getColumn("bmdjbz").setFixed(true);
		egu.getColumn("bmdjwc").setHeader("实际完成");
		egu.getColumn("bmdjwc").setWidth(74);
		egu.getColumn("bmdjwc").setFixed(true);
		((NumberField)(egu.getColumn("bmdjwc").editor)).setDecimalPrecision(3);
		egu.getColumn("bmdjdf").setHeader("得分");
		egu.getColumn("bmdjdf").editor = null;
		egu.getColumn("bmdjdf").setWidth(74);
		egu.getColumn("bmdjdf").setFixed(true);
//		((NumberField)(egu.getColumn("bmdjdf").editor)).setDecimalPrecision(3);
		egu.getColumn("mz_id").setHeader("ID");
		egu.getColumn("mz_id").setHidden(true);
		egu.getColumn("mz_id").editor = null;
		egu.getColumn("mzbz").setHeader("计划标准");
		egu.getColumn("mzbz").editor = null;
		egu.getColumn("mzbz").setWidth(74);
//		((NumberField)(egu.getColumn("mzbz").editor)).setDecimalPrecision(3);
		egu.getColumn("mzwc").setHeader("实际完成");
		egu.getColumn("mzwc").setWidth(74);
		egu.getColumn("mzwc").setFixed(true);
		((NumberField)(egu.getColumn("mzwc").editor)).setDecimalPrecision(3);
		egu.getColumn("mzdf").setHeader("得分");
		egu.getColumn("mzdf").editor = null;
		egu.getColumn("mzdf").setWidth(74);
		egu.getColumn("mzdf").setFixed(true);
//		((NumberField)(egu.getColumn("mzdf").editor)).setDecimalPrecision(3);
		egu.getColumn("rzc_id").setHeader("ID");
		egu.getColumn("rzc_id").setHidden(true);
		egu.getColumn("rzc_id").editor = null;
		egu.getColumn("rzcbz").setHeader("计划标准");
		egu.getColumn("rzcbz").editor = null;
		egu.getColumn("rzcbz").setWidth(74);
		egu.getColumn("rzcbz").setFixed(true);
//		((NumberField)(egu.getColumn("rzcbz").editor)).setDecimalPrecision(3);
		egu.getColumn("rzcwc").setHeader("实际完成");
		egu.getColumn("rzcwc").setWidth(74);
		egu.getColumn("rzcwc").setFixed(true);
		((NumberField)(egu.getColumn("rzcwc").editor)).setDecimalPrecision(3);
		egu.getColumn("rzcdf").setHeader("得分");
		egu.getColumn("rzcdf").editor = null;
		egu.getColumn("rzcdf").setWidth(74);
		egu.getColumn("rzcdf").setFixed(true);
//		((NumberField)(egu.getColumn("rzcdf").editor)).setDecimalPrecision(3);

		// 年份ComBox
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(false);
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");

		// 月份ComBox
		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setEditable(false);
		comb2.setWidth(60);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");

		// 电厂树
		egu.addTbarText("电厂树:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		// 设定工具栏下拉框自动刷新
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");

		egu
				.addToolbarItem("{"
						+ new GridButton("刷新",
								"function(){document.getElementById('RefurbishButton').click();}")
								.getScript() + "}");

		// 生成按钮
		GridButton CreateButton = new GridButton("生成",
				getBtnHandlerScript("CreateButton"));
		CreateButton.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(CreateButton);

		GridButton DeleteButton = new GridButton("删除",
				getBtnHandlerScript("DeleteButton"));
		DeleteButton.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(DeleteButton);

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		StringBuffer sb = new StringBuffer();
		String Headers =

		          "		   [\n"
				+ "         {header:'<table><tr><td width=2 align=center></td></tr></table>', align:'center',rowspan:3},\n"
				+ "         {header:'ID', align:'center',rowspan:3},\n" 
				+ "		    {header:'<table><tr><td width=143 align=center style=border:0>单位名称</td></tr></table>', align:'center',rowspan:3},\n"
				+ "         {header:'争创一流指标', colspan:12,rowspan:1}\n"
				+ "        ],\n"
				+ "		   [\n"
//				+ "         {header:'<table><tr><td width=8 align=center></td></tr></table>', align:'center',rowspan:2},\n"
				+ "         {header:'不含税标煤单价（元/吨）', colspan:3},\n"
				+ "			{header:'ID2', align:'center',rowspan:2},"
				+ "         {header:'煤质（mj/kg）', colspan:3},\n"
				+ "			{header:'ID3', align:'center',rowspan:2},"	
				+ "         {header:'热值差（kj/kg）', colspan:3}\n"
				+ "        ],\n"
				+ "        [\n"
				+ "	        {header:'<table><tr><td width=60 align=center style=border:0>计划标准</td></tr></table>'},\n"
				+ "         {header:'<table><tr><td width=60 align=center style=border:0>实际完成</td></tr></table>'},\n"
				+ "         {header:'<table><tr><td width=60 align=center style=border:0>得分</td></tr></table>'},\n"
				+ "	        {header:'<table><tr><td width=60 align=center style=border:0>计划标准</td></tr></table>'},\n"
				+ "         {header:'<table><tr><td width=60 align=center style=border:0>实际完成</td></tr></table>'},\n"
				+ "         {header:'<table><tr><td width=60 align=center style=border:0>得分</td></tr></table>'},\n"
				+ "         {header:'<table><tr><td width=60 align=center style=border:0>计划标准</td></tr></table>'},\n"
				+ "         {header:'<table><tr><td width=60 align=center style=border:0>实际完成</td></tr></table>'},\n"
				+ "         {header:'<table><tr><td width=60 align=center style=border:0>得分</td></tr></table>'}\n"
				+ "        ]";

		sb.append(Headers);
		egu.setHeaders(sb.toString());
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
		
		// 设置第一行不可编辑
		String show = "gridDiv_grid.on('beforeedit',function(e){ \n"+	
					  "		if(e.row == 0){	\n"+
					  "			e.cancel=true; \n"+ //		
					  "		} \n"+
					  "});";
		egu.addOtherScript(show);
		
//		String round = "function Round(a_Num , a_Bit){ "+
//		               "	return(Format(Math.round(a_Num * Math.pow (10 , a_Bit)) / Math.pow(10 , a_Bit),a_Bit)); "+
//	                   "}";
//		egu.addOtherScript(round);
		
		String yemjs = "gridDiv_grid.on('afteredit',function(e) { \n" +
				       "	var rec = e.record; \n" +
				       "	if(e.field=='BMDJWC') { \n" +
				       "		var bmdjdf_newValue = (10 + (eval(rec.get('BMDJBZ') || 0) - eval(rec.get('BMDJWC') || 0)) / eval(rec.get('BMDJBZ') || 0) * 100) * 15; \n" +
				       "	    if(eval(rec.get('BMDJBZ') || 0) == 0) { \n" +
				       "			rec.set('BMDJDF',0); \n" +
				       "		} else { \n" +
				       "			rec.set('BMDJDF',Round_new(bmdjdf_newValue,3)); \n" +
				       "		} \n" +
				       "	} \n" +
				       "	if(e.field=='MZWC') { \n" +
				       "		var mzdf_newValue = (10 - (eval(rec.get('MZBZ') || 0) - eval(rec.get('MZWC') || 0)) / eval(rec.get('MZBZ') || 0) * 100) * 15; \n" +
				       "	    if(eval(rec.get('MZBZ') || 0) == 0) { \n" +
				       "			rec.set('MZDF',0); \n" +
				       "		} else { \n" +
				       "			rec.set('MZDF',Round_new(mzdf_newValue,3)); \n" +
				       "		} \n" +
				       "	} \n" +
				       "	if(e.field=='RZCWC') { \n" +
				       "		var rzcdf_newValue = (10 - (eval(rec.get('RZCWC') || 0) - 502) / 100) * 10; \n" +
				       "		rec.set('RZCDF',Round_new(rzcdf_newValue,3)); \n" +
				       "	} \n" +
				       
				       "	var t1 = 0; \n"+
				       "	var t2 = 0; \n"+
				       "	var t3 = 0; \n"+
				       "	var t4 = 0; \n"+
				       "	var recd = null; \n"+
				   	   "	for(var i = 1; i < gridDiv_ds.getCount(); i++) { \n"+
				  	   "		recd = gridDiv_ds.getAt(i); \n"+			
				   	   "		t1 = t1 + eval(recd.get(e.field)||0); \n"+
				   	   "		t2 = t2 + eval(recd.get('BMDJDF')||0); \n"+
				   	   "		t3 = t3 + eval(recd.get('MZDF')||0); \n"+
				   	   "		t4 = t4 + eval(recd.get('RZCDF')||0); \n"+
				       "	} \n"+				   
				       "	if(e.row>0) { \n"+				   						   		
				   	   "		gridDiv_ds.getAt(0).set(e.field,Round_new(t1/eval(gridDiv_ds.getCount()-1),3)); \n"+
				   	   "		if(e.field == 'BMDJWC') { \n"+
					   "			gridDiv_ds.getAt(0).set('BMDJDF',Round_new(t2/eval(gridDiv_ds.getCount()-1),3)); \n"+
					   "		} \n"+									    			
					   "		if(e.field == 'MZWC') { \n"+
					   "			gridDiv_ds.getAt(0).set('MZDF',Round_new(t3/eval(gridDiv_ds.getCount()-1),3)); \n"+
					   "		} \n"+
					   "		if(e.field == 'RZCWC') { \n"+
					   "			gridDiv_ds.getAt(0).set('RZCDF',Round_new(t4/eval(gridDiv_ds.getCount()-1),3)); \n"+
					   "		} \n"+
				       "	} \n"+				       
				       "})";
		egu.addOtherScript(yemjs);
		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "年";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton") || btnName.endsWith("CopyButton")) {
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

			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);

			visit.setDefaultTree(null);
			setTreeid(null);
			getSelectData();
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

	// 年份
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean2((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	// 月份
	public boolean Changeyuef = false;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean3((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean3() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(listYuef));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}
}
