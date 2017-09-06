package com.zhiren.jt.gongys;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author 王刚
 * 
 */
public class GongysChak extends BasePage implements PageValidateListener {

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	boolean zt = true;

	public boolean getZt() {
		return zt;
	}

	public void setZt(boolean zt) {
		this.zt = zt;
	}

	private long m_mid;//

	private String m_fuid;//

	private int m_xuh;//

	private String m_mingc;//

	private String m_quanc;//

	private String m_piny;//

	private String m_bianm;//

	private String m_danwdz;//

	private String m_faddbr;//

	private String m_weitdlr;//

	private String m_kaihyh;//

	private String m_zhangh;//

	private String m_dianh;//

	private String m_shuih;//

	private String m_youzbm;//

	private String m_chuanz;//

	private String m_meitly;//

	private String m_meiz;//

	private String m_chubnl;//

	private String m_kaicnl;//

	private String m_kaicnx;//

	private String m_shengcnl;//

	private String m_gongynl;//

	private String m_liux;//

	private String m_yunsfs;//

	private String m_shiccgl;//

	private String m_zhongdht;//

	private String m_yunsnl;//

	private String m_heznx;//

	private String m_rongqgx;//

	private String m_xiny;//

	private String m_gongsxz;//

	private String m_kegywfmz;//

	private String m_kegywfmzzb;//

	private String m_shengfb_id;//

	private String m_shifss;//

	private String m_shangsdz;//

	private String m_zicbfb;//

	private String m_shoumbfb;//

	private String m_qitbfb;//

	private String m_beiz;//

	private double m_quansf;

	private double m_huif;

	private double m_huiff;

	private double m_liuf;

	private double m_farl;

	private int m_lid;

	private double m_haskmxs;

	private String m_huird;

	private double m_tan;

	private double m_qing;

	private double m_yang;

	private double m_dan;

	public long getMId() {
		return m_mid;
	}

	public void setMId(long mid) {
		this.m_mid = mid;
	}

	public String getFuid() {
		return m_fuid;
	}

	public void setFuid(String fuid) {
		this.m_fuid = fuid;
	}

	public int getXuh() {
		return m_xuh;
	}

	public void setXuh(int xuh) {
		this.m_xuh = xuh;
	}

	public String getMingc() {
		return m_mingc;
	}

	public void setMingc(String mingc) {
		this.m_mingc = mingc;
	}

	public String getQuanc() {
		return m_quanc;
	}

	public void setQuanc(String quanc) {
		this.m_quanc = quanc;
	}

	public String getPiny() {
		return m_piny;
	}

	public void setPiny(String piny) {
		this.m_piny = piny;
	}

	public String getBianm() {
		return m_bianm;
	}

	public void setBianm(String bianm) {
		this.m_bianm = bianm;
	}

	public String getDanwdz() {
		return m_danwdz;
	}

	public void setDanwdz(String danwdz) {
		this.m_danwdz = danwdz;
	}

	public String getFaddbr() {
		return m_faddbr;
	}

	public void setFaddbr(String faddbr) {
		this.m_faddbr = faddbr;
	}

	public String getWeitdlr() {
		return m_weitdlr;
	}

	public void setWeitdlr(String weitdlr) {
		this.m_weitdlr = weitdlr;
	}

	public String getKaihyh() {
		return m_kaihyh;
	}

	public void setKaihyh(String kaihyh) {
		this.m_kaihyh = kaihyh;
	}

	public String getZhangh() {
		return m_zhangh;
	}

	public void setZhangh(String zhangh) {
		this.m_zhangh = zhangh;
	}

	public String getDianh() {
		return m_dianh;
	}

	public void setDianh(String dianh) {
		this.m_dianh = dianh;
	}

	public String getShuih() {
		return m_shuih;
	}

	public void setShuih(String shuih) {
		this.m_shuih = shuih;
	}

	public String getYouzbm() {
		return m_youzbm;
	}

	public void setYouzbm(String youzbm) {
		this.m_youzbm = youzbm;
	}

	public String getChuanz() {
		return m_chuanz;
	}

	public void setChuanz(String chuanz) {
		this.m_chuanz = chuanz;
	}

	public String getMeitly() {
		return m_meitly;
	}

	public void setMeitly(String meitly) {
		this.m_meitly = meitly;
	}

	public String getMeiz() {
		return m_meiz;
	}

	public void setMeiz(String meiz) {
		this.m_meiz = meiz;
	}

	public String getChubnl() {
		return m_chubnl;
	}

	public void setChubnl(String chubnl) {
		this.m_chubnl = chubnl;
	}

	public String getKaicnl() {
		return m_kaicnl;
	}

	public void setKaicnl(String kaicnl) {
		this.m_kaicnl = kaicnl;
	}

	public String getKaicnx() {
		return m_kaicnx;
	}

	public void setKaicnx(String kaicnx) {
		this.m_kaicnx = kaicnx;
	}

	public String getShengcnl() {
		return m_shengcnl;
	}

	public void setShengcnl(String shengcnl) {
		this.m_shengcnl = shengcnl;
	}

	public String getGongynl() {
		return m_gongynl;
	}

	public void setGongynl(String gongynl) {
		this.m_gongynl = gongynl;
	}

	public String getLiux() {
		return m_liux;
	}

	public void setLiux(String liux) {
		this.m_liux = liux;
	}

	public String getYunsfs() {
		return m_yunsfs;
	}

	public void setYunsfs(String yunsfs) {
		this.m_yunsfs = yunsfs;
	}

	public String getShiccgl() {
		return m_shiccgl;
	}

	public void setShiccgl(String shiccgl) {
		this.m_shiccgl = shiccgl;
	}

	public String getZhongdht() {
		return m_zhongdht;
	}

	public void setZhongdht(String zhongdht) {
		this.m_zhongdht = zhongdht;
	}

	public String getYunsnl() {
		return m_yunsnl;
	}

	public void setYunsnl(String yunsnl) {
		this.m_yunsnl = yunsnl;
	}

	public String getHeznx() {
		return m_heznx;
	}

	public void setHeznx(String heznx) {
		this.m_heznx = heznx;
	}

	public String getRongqgx() {
		return m_rongqgx;
	}

	public void setRongqgx(String rongqgx) {
		this.m_rongqgx = rongqgx;
	}

	public String getXiny() {
		return m_xiny;
	}

	public void setXiny(String xiny) {
		this.m_xiny = xiny;
	}

	public String getGongsxz() {
		return m_gongsxz;
	}

	public void setGongsxz(String gongsxz) {
		this.m_gongsxz = gongsxz;
	}

	public String getKegywfmz() {
		return m_kegywfmz;
	}

	public void setKegywfmz(String kegywfmz) {
		this.m_kegywfmz = kegywfmz;
	}

	public String getKegywfmzzb() {
		return m_kegywfmzzb;
	}

	public void setKegywfmzzb(String kegywfmzzb) {
		this.m_kegywfmzzb = kegywfmzzb;
	}

	public String getShengfb_id() {
		return m_shengfb_id;
	}

	public void setShengfb_id(String shengfb_id) {
		this.m_shengfb_id = shengfb_id;
	}

	public String getShifss() {
		return m_shifss;
	}

	public void setShifss(String shifss) {
		this.m_shifss = shifss;
	}

	public String getShangsdz() {
		return m_shangsdz;
	}

	public void setShangsdz(String shangsdz) {
		this.m_shangsdz = shangsdz;
	}

	public String getZicbfb() {
		return m_zicbfb;
	}

	public void setZicbfb(String zicbfb) {
		this.m_zicbfb = zicbfb;
	}

	public String getShoumbfb() {
		return m_shoumbfb;
	}

	public void setShoumbfb(String shoumbfb) {
		this.m_shoumbfb = shoumbfb;
	}

	public String getQitbfb() {
		return m_qitbfb;
	}

	public void setQitbfb(String qitbfb) {
		this.m_qitbfb = qitbfb;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	public double getHuif() {
		return m_huif;
	}

	public void setHuif(double huif) {
		this.m_huif = huif;
	}

	public double getHuiff() {
		return m_huiff;
	}

	public void setHuiff(double huiff) {
		this.m_huiff = huiff;
	}

	public String getHuird() {
		return m_huird;
	}

	public void setHuird(String huird) {
		this.m_huird = huird;
	}

	public int getLid() {
		return m_lid;
	}

	public void setLid(int lid) {
		this.m_lid = lid;
	}

	public double getLiuf() {
		return m_liuf;
	}

	public void setLiuf(double liuf) {
		this.m_liuf = liuf;
	}

	public double getQing() {
		return m_qing;
	}

	public void setQing(double qing) {
		this.m_qing = qing;
	}

	public double getQuansf() {
		return m_quansf;
	}

	public void setQuansf(double quansf) {
		this.m_quansf = quansf;
	}

	public double getTan() {
		return m_tan;
	}

	public void setTan(double tan) {
		this.m_tan = tan;
	}

	public double getYang() {
		return m_yang;
	}

	public void setYang(double yang) {
		this.m_yang = yang;
	}

	public double getDan() {
		return m_dan;
	}

	public void setDan(double dan) {
		this.m_dan = dan;
	}

	public double getFarl() {
		return m_farl;
	}

	public void setFarl(double farl) {
		this.m_farl = farl;
	}

	public double getHaskmxs() {
		return m_haskmxs;
	}

	public void setHaskmxs(double haskmxs) {
		this.m_haskmxs = haskmxs;
	}

	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	public static boolean isNull(String value) {
		if (value == null) {
			return true;
		} else if (value.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private GongysChakbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public GongysChakbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(GongysChakbean EditValue) {
		_EditValue = EditValue;
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getEditValues();
		// ((Chezxxbean) _list.get(i)).getXXX();
		getSelectData();
		// setMsg();
	}

	// private void Insert() {
	// // 为 "添加" 按钮添加处理程序
	// // List _list = ((Visit) getPage().getVisit()).getEditValues();
	// // ((Chezxxbean) _list.get(i)).getXXX();
	// JDBCcon con = new JDBCcon();
	// try {
	// long mid = -1;
	// String str = "select decode(max(xuh), null, 1, 0, 1, max(xuh) + 1) as xuh
	// from chezxxb";
	// ResultSet rs = con.getResultSet(str);
	// int mxuh = 0;
	// while (rs.next()) {
	// mxuh = rs.getInt("xuh");
	// }
	// rs.close();
	// String mbianm = "";
	// String mmingc = "";
	// String mquanc = "";
	// String mpiny = "";
	// String mlujxxb_id = "";
	// String mleib = "";
	// String mbeiz = "";
	// setMid(mid);
	// setXuh(mxuh);
	// setBianm(mbianm);
	// setMingc(mmingc);
	// setQuanc(mquanc);
	// setPiny(mpiny);
	// setLujxxb_id(mlujxxb_id);
	// setLeib(mleib);
	// setBeiz(mbeiz);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// con.Close();
	// }
	// }

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		JDBCcon con = new JDBCcon();
		long _id = 0;
		_id = ((Visit) getPage().getVisit()).getLong1();
		con.getDelete("delete chezxxb where id = " + _id + " and "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id());
		con.getDelete("delete diancdzb where chezxxb_id = " + _id
				+ " and diancxxb_id = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id());
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		// ResultSet rs;
		// long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			long _id = getMId();
			int xuh = getXuh();
			String mingc = getMingc();
			String quanc = getQuanc();
			String piny = getPiny();
			String bianm = getBianm();
			String danwdz = getDanwdz();
			String faddbr = getFaddbr();
			String weitdlr = getWeitdlr();
			String kaihyh = getKaihyh();
			String zhangh = getZhangh();
			String dianh = getDianh();
			String shuih = getShuih();
			String youzbm = getYouzbm();
			String chuanz = getChuanz();
			String meitly = getMeitly();
			String meiz = getMeiz();
			String chubnl = getChubnl();
			String kaicnl = getKaicnl();
			String kaicnx = getKaicnx();
			String shengcnl = getShengcnl();
			String gongynl = getGongynl();
			String liux = getLiux();
			String yunsfs = getYunsfs();
			String shiccgl = getShiccgl();
			String zhongdht = getZhongdht();
			String yunsnl = getYunsnl();
			String heznx = getHeznx();
			String rongqgx = getRongqgx();
			String xiny = getXiny();
			String gongsxz = getGongsxz();
			String kegywfmz = getKegywfmz();
			String kegywfmzzb = getKegywfmzzb();
			String shengfb_id = getShengfb_id();
			String shifss = getShifss();
			String shangsdz = getShangsdz();
			String zicbfb = getZicbfb();
			String shoumbfb = getShoumbfb();
			String qitbfb = getQitbfb();
			String beiz = getBeiz();
			double huif = getHuif();
			double huiff = getHuiff();
			String huird = getHuird();
			double lid = getLid();
			double liuf = getLiuf();
			double qing = getQing();
			double quansf = getQuansf();
			double tan = getTan();
			double yang = getYang();
			double dan = getDan();
			double farl = getFarl();
			double haskmxs = getHaskmxs();
			String sql = "update gongysb\n" + "   set FUID       =,\n"
					+ "       XUH        =" + xuh + ",\n"
					+ "       MINGC      = '" + mingc + "',\n"
					+ "       QUANC      = '" + quanc + "',\n"
					+ "       PINY       = '" + piny + "',\n"
					+ "       BIANM      = '" + bianm + "',\n"
					+ "       DANWDZ     = '" + danwdz + "',\n"
					+ "       FADDBR     = '" + faddbr + "',\n"
					+ "       WEITDLR    = '" + weitdlr + "',\n"
					+ "       KAIHYH     = '" + kaihyh + "',\n"
					+ "       ZHANGH     = '" + zhangh + "',\n"
					+ "       DIANH      = '" + dianh + "',\n"
					+ "       SHUIH      = '" + shuih + "',\n"
					+ "       YOUZBM     = '" + youzbm + "',\n"
					+ "       CHUANZ     = '" + chuanz + "',\n"
					+ "       MEITLY     = '" + meitly + "',\n"
					+ "       MEIZ       = '" + meiz + "',\n"
					+ "       CHUBNL     = " + chubnl + ",\n"
					+ "       KAICNL     =" + kaicnl + ",\n"
					+ "       KAICNX     =" + kaicnx + ",\n"
					+ "       SHENGCNL   =" + shengcnl + ",\n"
					+ "       GONGYNL    =" + gongynl + ",\n"
					+ "       LIUX       = '" + liux + "',\n"
					+ "       YUNSFS     = '" + yunsfs + "',\n"
					+ "       SHICCGL    =" + shiccgl + ",\n"
					+ "       ZHONGDHT   =" + zhongdht + ",\n"
					+ "       YUNSNL     =" + yunsnl + ",\n"
					+ "       HEZNX      =" + heznx + ",\n"
					+ "       RONGQGX    =" + rongqgx + ",\n"
					+ "       XINY       =" + xiny + ",\n"
					+ "       GONGSXZ    = '" + gongsxz + "',\n"
					+ "       KEGYWFMZ   = '" + kegywfmz + "',\n"
					+ "       KEGYWFMZZB = '" + kegywfmzzb + "',\n"
					+ "       SHENGFB_ID =" + shengfb_id + ",\n"
					+ "       SHIFSS     =" + shifss + ",\n"
					+ "       SHANGSDZ   = '" + shangsdz + "',\n"
					+ "       ZICBFB     =" + zicbfb + ",\n"
					+ "       SHOUMBFB   =" + shoumbfb + ",\n"
					+ "       QITBFB     =" + qitbfb + ",\n"
					+ "       BEIZ       = '" + beiz + "'\n" + " where id ="
					+ _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + huif
					+ "' where mingc= '灰分' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + huird
					+ "' where mingc= '灰熔点' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + lid
					+ "' where mingc= '粒度' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + liuf
					+ "' where mingc= '硫分' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + huiff
					+ "' where mingc= '挥发分' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + qing
					+ "' where mingc= '氢' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + quansf
					+ "' where mingc= '全水分' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + tan
					+ "' where mingc= '碳' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + yang
					+ "' where mingc= '氧' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + dan
					+ "' where mingc= '氮' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + farl
					+ "' where mingc= '发热量' and gongysb_id = " + _id;
			con.getUpdate(sql);
			sql = "update gongysmzb set zhi = '" + haskmxs
					+ "' where mingc= '哈氏可磨系数' and gongysb_id = " + _id;
			con.getUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	public String getArrayScript() {
		StringBuffer array = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		boolean xianszt = visit.getboolean1();
		array.append("var xianszt = " + xianszt + ";\n");
		return array.toString();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ReturnsChick = false;

	public void ReturnsButton(IRequestCycle cycle) {
		_ReturnsChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
			Skipping(cycle);
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			if (getZt() == true) {
				Skipping(cycle);
			}
		}
		if (_ReturnsChick) {
			_ReturnsChick = false;
			Skipping(cycle);
		}
	}

	private void Skipping(IRequestCycle cycle) {
		cycle.activate("Gongys");
	}

	public List getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			getLujxxb_idModels();
			getLeibModels();
			// long _id = visit.getLong1();
			String sql = "select * from gongysb where id = " + getMId();
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				setXuh(rs.getInt("XUH"));
				setMingc(rs.getString("MINGC"));
				setQuanc(rs.getString("QUANC"));
				setPiny(rs.getString("PINY"));
				setBianm(rs.getString("BIANM"));
				setDanwdz(rs.getString("DANWDZ"));
				setFaddbr(rs.getString("FADDBR"));
				setWeitdlr(rs.getString("WEITDLR"));
				setKaihyh(rs.getString("KAIHYH"));
				setZhangh(rs.getString("ZHANGH"));
				setDianh(rs.getString("DIANH"));
				setShuih(rs.getString("SHUIH"));
				setYouzbm(rs.getString("YOUZBM"));
				setChuanz(rs.getString("CHUANZ"));
				setMeitly(rs.getString("MEITLY"));
				setMeiz(rs.getString("MEIZ"));
				setChubnl(rs.getString("CHUBNL"));
				setKaicnl(rs.getString("KAICNL"));
				setKaicnx(rs.getString("KAICNX"));
				setShengcnl(rs.getString("SHENGCNL"));
				setGongynl(rs.getString("GONGYNL"));
				setLiux(rs.getString("LIUX"));
				setYunsfs(rs.getString("YUNSFS"));
				setShiccgl(rs.getString("SHICCGL"));
				setZhongdht(rs.getString("ZHONGDHT"));
				setYunsnl(rs.getString("YUNSNL"));
				setHeznx(rs.getString("HEZNX"));
				setRongqgx(rs.getString("RONGQGX"));
				setXiny(rs.getString("XINY"));
				setGongsxz(rs.getString("GONGSXZ"));
				setKegywfmz(rs.getString("KEGYWFMZ"));
				setKegywfmzzb(rs.getString("KEGYWFMZZB"));
				setShengfb_id(rs.getString("SHENGFB_ID"));
				setShifss(rs.getString("SHIFSS"));
				setShangsdz(rs.getString("SHANGSDZ"));
				setZicbfb(rs.getString("ZICBFB"));
				setShoumbfb(rs.getString("SHOUMBFB"));
				setQitbfb(rs.getString("QITBFB"));
				setBeiz(rs.getString("BEIZ"));
				String SQL = "select (select zhi\n"
						+ "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '灰分') as huif,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '灰熔点') as huird,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '粒度') as lid,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '硫分') as liuf,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '挥发分') as huiff,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '氢') as qing,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '全水分') as quansf,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '碳') as tan,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '氧') as yang,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '氮') as dan,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '发热量') as farl,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + getMId() + "\n"
						+ "           and mingc = '哈氏可磨系数') as haskmxs\n"
						+ "  from gongysmzb z, gongysb g\n"
						+ " where z.gongysb_id = g.id\n" + "   and g.id = "
						+ getMId() + "";
				ResultSet r = con.getResultSet(SQL);
				if (rs.next()) {
					setHuif(r.getDouble("huif"));
					setHuiff(r.getDouble("huiff"));
					setHuird(r.getString("huird"));
					setLid(r.getInt("lid"));
					setLiuf(r.getDouble("liuf"));
					setQing(r.getDouble("qing"));
					setQuansf(r.getDouble("quansf"));
					setTan(r.getDouble("tan"));
					setYang(r.getDouble("yang"));
					setDan(r.getDouble("dan"));
					setFarl(r.getDouble("farl"));
					setHaskmxs(r.getDouble("haskmxs"));
				} else {
					setHuif(0.00);
					setHuiff(0.00);
					setHuird("");
					setLid(0);
					setLiuf(0.00);
					setQing(0.00);
					setQuansf(0.00);
					setTan(0.00);
					setYang(0.00);
					setDan(0.00);
					setFarl(0.00);
					setHaskmxs(0.00);
				}
				r.close();
				String SQL1 = "select xx.*\n"
						+ "  from gongysmkglb gl, meikxxb xx\n"
						+ " where gl.meikxxb_id = xx.id\n"
						+ "   and gl.gongysb_id = " + getMId() + "\n"
						+ " order by xx.xuh, mingc";
				r = con.getResultSet(SQL1);
				while (r.next()) {
					int xuh = r.getInt("xuh");
					long id = r.getLong("id");
					String bianm = r.getString("bianm");
					String mingc = r.getString("mingc");
					String quanc = r.getString("quanc");
					String piny = r.getString("piny");
					String shengfb_id = r.getString("shengfb_id");
					String leib = r.getString("leib");
					String jihkjb_id = r.getString("jihkjb_id");
					String leix = r.getString("leix");
					String beiz = r.getString("beiz");
					String danwdz = r.getString("danwdz");
					GongysChakbean bean = new GongysChakbean(id, xuh, piny,
							bianm, quanc, mingc, shengfb_id, leib, leix,
							jihkjb_id, beiz, danwdz);
					_editvalues.add(bean);
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new GongysChakbean());
		}
		_editTableRow = -1;
		((Visit) getPage().getVisit()).setList1(_editvalues);
		return ((Visit) getPage().getVisit()).getList1();
	}

	public IDropDownBean getLujxxb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getLujxxb_idModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getLujxxb_idModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong3()) {
					((Visit) getPage().getVisit()).setDropDownBean1(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setLujxxb_idValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getLujxxb_idModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getLujxxb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getLujxxb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setLujxxb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getLujxxb_idModels() {
		String sql = "select id, mingc from lujxxb order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, null));
	}

	public IDropDownBean getLeibValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getLeibModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getLeibModel().getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong2()) {
					((Visit) getPage().getVisit()).setDropDownBean2(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setLeibValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getLeibModel().getOption(
							0));
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getLeibModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getLeibModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setLeibModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getLeibModels() {
		String sql = "select z.*\n"
				+ "  from (select 0 as id, decode(1,1,'不可编辑') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 1 as id, decode(1,1,'可以编辑') as mingc from dual) z\n"
				+ " order by id, mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, null));
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
		String id = (String) cycle.getRequestContext().getRequest().getParameter("ID");
//				.getAttribute("id");
		StringBuffer sql = new StringBuffer();
		String s[] = id.split(",");
		sql.append("select * from gongysb where id=" + s[0]);
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			visit.setList1(null);
			visit.getboolean1();
			visit.getLong1();
			setMId(Long.parseLong(s[0]));

			getSelectData();
		}
	}

	// private String getProperValue(IPropertySelectionModel _selectModel,
	// int value) {
	// int OprionCount;
	// OprionCount = _selectModel.getOptionCount();
	// for (int i = 0; i < OprionCount; i++) {
	// if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
	// return ((IDropDownBean) _selectModel.getOption(i)).getValue();
	// }
	// }
	// return null;
	// }
	//
	// private long getProperId(IPropertySelectionModel _selectModel, String
	// value) {
	// int OprionCount;
	// OprionCount = _selectModel.getOptionCount();
	//
	// for (int i = 0; i < OprionCount; i++) {
	// if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
	// value)) {
	// return ((IDropDownBean) _selectModel.getOption(i)).getId();
	// }
	// }
	// return -1;
	// }

}
