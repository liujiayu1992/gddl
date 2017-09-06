package com.zhiren.jt.zdt.xitgl.gonghdw;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class GongysChakext extends BasePage implements PageValidateListener {

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
	
	public boolean isXianszt(){
		return((Visit) getPage().getVisit()).getboolean4();
	}
	public void setXianszt(boolean value){
		((Visit) getPage().getVisit()).setboolean4(value);
	}

	private long m_mid = -1;//

	private String m_fuid = "大供应商";//

	private int m_xuh;//

	private String m_mingc = "";//

	private String m_quanc = "";//

	private String m_piny = "";//

	private String m_bianm = "";//

	private String m_danwdz = "";//

	private String m_faddbr = "";//

	private String m_weitdlr = "";//

	private String m_kaihyh = "";//

	private String m_zhangh = "";//

	private String m_dianh = "";//

	private String m_shuih = "";//

	private String m_youzbm = "";//

	private String m_chuanz = "";//

	private String m_meitly = "";//

	private String m_meiz = "";//

	private String m_chubnl = "";//

	private String m_kaicnl = "";//

	private String m_kaicnx = "";//

	private String m_shengcnl = "";//

	private String m_gongynl = "";//

	private String m_liux = "";//

	private String m_yunsfs = "";//

	private String m_shiccgl = "";//

	private String m_zhongdht = "";//

	private String m_yunsnl = "";//

	private String m_heznx = "";//

	private String m_rongqgx = "";//

	private String m_xiny = "";//

	private String m_gongsxz = "";//

	private String m_kegywfmz = "";//

	private String m_kegywfmzzb = "";//

	private String m_shengfb_id = "";//

	private String m_shifss = "";//

	private String m_shangsdz = "";//

	private String m_zicbfb = "";//

	private String m_shoumbfb = "";//

	private String m_qitbfb = "";//

	private String m_beiz = "";//

	private double m_quansf = 0.00;

	private double m_huif = 0.00;

	private double m_huiff = 0.00;

	private double m_liuf = 0.00;

	private double m_farl = 0.00;

	private int m_lid = 3;

	private double m_haskmxs = 0;

	private double m_huird = 0;

	private double m_tan = 0;

	private double m_qing = 0;

	private double m_yang = 0;

	private double m_dan = 0;
	
	private String m_shenqr="";//申请人
	
	private double m_zhuangt=0;
	
	private String m_chengsb_id="";
	

	public long getMId() {
		return m_mid;
	}

	public List gridColumns;

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

	public double getHuird() {
		return m_huird;
	}

	public void setHuird(double huird) {
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
	
	public String getShenqr() {
		return m_shenqr;
	}

	public void setShenqr(String shenqr) {
		this.m_shenqr = shenqr;
	}

	public double getZhuangt() {
		return m_zhuangt;
	}

	public void setZhuangt(double zhuangt) {
		this.m_zhuangt = zhuangt;
	}
	public String getChengsb_id() {
		return m_chengsb_id;
	}

	public void setChengsb_id(String chengsb_id) {
		this.m_chengsb_id = chengsb_id;
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

	int flag = -1;

	private void Save() {
		JDBCcon con = new JDBCcon();
		// ResultSet rs;
		long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			long _id = ((Visit) getPage().getVisit()).getLong9();
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
			if (getChubnl().trim().equals("") || getChubnl() == null) {
				setChubnl("0");
			}
			if (getKaicnl().trim().equals("") || getKaicnl() == null) {
				setKaicnl("0");
			}
			if (getKaicnx().trim().equals("") || getKaicnx() == null) {
				setKaicnx("0");
			}
			if (getShengcnl().trim().equals("") || getShengcnl() == null) {
				setShengcnl("0");
			}
			if (getGongynl().trim().equals("") || getGongynl() == null) {
				setGongynl("0");
			}
			if (getShiccgl().trim().equals("") || getShiccgl() == null) {
				setShiccgl("0");
			}
			if (getZhongdht().trim().equals("") || getZhongdht() == null) {
				setZhongdht("0");
			}
			if (getYunsnl().trim().trim().equals("") || getYunsnl() == null) {
				setYunsnl("0");
			}
			if (getHeznx().trim().equals("") || getHeznx() == null) {
				setHeznx("0");
			}
			int chubnl = Integer.parseInt(getChubnl());
			int kaicnl = Integer.parseInt(getKaicnl());
			int kaicnx = Integer.parseInt(getKaicnx());
			int shengcnl = Integer.parseInt(getShengcnl());
			int gongynl = Integer.parseInt(getGongynl());
			String liux = getLiux();
			String yunsfs = getYunsfs();
			int shiccgl = Integer.parseInt(getShiccgl());
			int zhongdht = Integer.parseInt(getZhongdht());
			int yunsnl = Integer.parseInt(getYunsnl());
			int heznx = Integer.parseInt(getHeznx());
			long rongqgx = getRongqgxValue().getId();
			long xiny = getXinyValue().getId();
			String gongsxz = getGongsxz();
			String kegywfmz = getKegywfmz();
			String kegywfmzzb = getKegywfmzzb();
			long shengfb_id = getShengfb_idValue().getId();
			long chengsb_id=this.getChengs_idValue().getId();
			long shifss = getShifssValue().getId();
			String shangsdz = getShangsdz();
			if (getZicbfb().trim().equals("") || getZicbfb() == null) {

				setZicbfb("0");
			}
			if (getShoumbfb().trim().equals("") || getShoumbfb() == null) {
				setShoumbfb("0");
			}
			if (getQitbfb().trim().equals("") || getQitbfb() == null) {
				setQitbfb("0");
			}
			int zicbfb = Integer.parseInt(getZicbfb());
			int shoumbfb = Integer.parseInt(getShoumbfb());
			int qitbfb = Integer.parseInt(getQitbfb());
			String beiz = getBeiz();
			double huif = getHuif();
			double huiff = getHuiff();
			double huird = getHuird();
			double lid = getLid();
			double liuf = getLiuf();
			double qing = getQing();
			double quansf = getQuansf();
			double tan = getTan();
			double yang = getYang();
			double dan = getDan();
			double farl = getFarl();
			double haskmxs = getHaskmxs();
			long fuid = getFUIDValue().getId();
			long leib = getFuid_idValue().getId();
			String shenqr=((Visit) getPage().getVisit()).getDiancmc()+"-"+((Visit) getPage().getVisit()).getRenymc();

			if (_id == -1) {
				_id = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
				String SQL = "select * from gonghdwdmsqb where mingc = '" + mingc
						+ "'";
				ResultSet rs = con.getResultSet(SQL);
				if (rs.next()) {
					setMsg("简称存在重复数据！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				} else {
					rs.close();
				}
				if (mingc == null || mingc.trim().equals("")) {
					setMsg("简称不能为空！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				}
				if (quanc == null || quanc.trim().equals("")) {
					setMsg("全称不能为空！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				}

				SQL = "select * from gonghdwdmsqb where quanc = '" + quanc + "'";
				rs = con.getResultSet(SQL);
				if (rs.next()) {
					setMsg("全称存在重复数据！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				} else {
					rs.close();
				}
				if (leib == -1) {
					setMsg("请选择供应商类别！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				}else{
					if(leib==0){
						fuid=0;
					}else{
						if(fuid==-1){
							setMsg("请选择隶属关系！");
							rs.close();
							con.rollBack();
							con.Close();
							return;
						}
					}
				}
				if (shifss == -1) {
					setMsg("请选择上市状态！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				}
				if (shengfb_id == -1) {
					setMsg("请选择省份！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				}
				if (chengsb_id == -1) {
					setMsg("请选择供货单位所属城市！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				}
				if (xiny == -1) {
					setMsg("请选择供应商信誉！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				}
				if (rongqgx == -1) {
					setMsg("请选择融洽关系！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				}
				String sql = "insert into gonghdwdmsqb (ID,	FUID,XUH,MINGC,QUANC,PINY,BIANM	,DANWDZ,FADDBR," +
						"WEITDLR	,KAIHYH	,ZHANGH	,DIANH,	SHUIH,YOUZBM,CHUANZ	,MEITLY," +
						"MEIZ,	CHUBNL,	KAICNL,	KAICNX,	SHENGCNL,GONGYNL,LIUX,YUNSFS,SHICCGL," +
						"ZHONGDHT,YUNSNL,HEZNX,	RONGQGX,XINY,GONGSXZ,KEGYWFMZ," +
						"KEGYWFMZZB,SHENGFB_ID,SHIFSS,SHANGSDZ,	ZICBFB," +
						"SHOUMBFB,QITBFB,BEIZ,SHENQR,ZHUANGT,CHENGSB_ID) values (" + _id + "," + fuid
						+ "," + xuh + " ,'" + mingc + "','" + quanc + "','"
						+ piny + "','" + bianm + " ','" + danwdz + " ','"
						+ faddbr + " ','" + weitdlr + " ','" + kaihyh + " ','"
						+ zhangh + " ','" + dianh + " ','" + shuih + " ','"
						+ youzbm + " ','" + chuanz + " ','" + meitly + " ','"
						+ meiz + " '," + chubnl + " ," + kaicnl + " ," + kaicnx
						+ " ," + shengcnl + " ," + gongynl + " ,'" + liux
						+ " ',' " + yunsfs + " '," + shiccgl + " ," + zhongdht
						+ " ," + yunsnl + " ," + heznx + " ," + rongqgx + " ,"
						+ xiny + ",'" + gongsxz + " ','" + kegywfmz + " ','"
						+ kegywfmzzb + " '," + shengfb_id + "," + shifss
						+ " ,'" + shangsdz + " '," + zicbfb + " ," + shoumbfb
						+ " ," + qitbfb + " ,'" + beiz + " ','" + shenqr + " ',0,'" + chengsb_id + " ')";
				flag = con.getInsert(sql);
				if (flag == -1) {
					return;
				} else {
					con.getInsert("insert into gongysdcglb (id,diancxxb_id,gongysb_id) values("
							+ Long.parseLong(MainGlobal.getNewID(diancxxb_id))
							+ "," + diancxxb_id + "," + _id + ")");
					
					//向gongysmzb里插入数据开始
					StringBuffer sb = new StringBuffer();
					sb.append("begin \n");
					for(int i=1;i<=12;i++){
						String mingc1="";
						double zhi=0;
						 switch(i){
							case 1:
								 mingc1="灰分";
								 zhi=huif;
								break;
							case 2:
								mingc1="灰熔点";
								zhi=huird;
								break;
							case 3:
								mingc1="粒度";
								zhi=lid;
								break;
							case 4:
								mingc1="硫分";
								zhi=liuf;
								break;
							case 5:
								mingc1="挥发分";
								zhi=huiff;
								break;
							case 6:
								mingc1="氢";
								zhi=qing;
								break;
							case 7:
								mingc1="全水分";
								zhi=quansf;
								break;
							case 8:
								mingc1="碳";
								zhi=tan;
								break;
							case 9:
								mingc1="氧";
								zhi=yang;
								break;
							case 10:
								mingc1="氮";
								zhi=dan;
								break;
							case 11:
								mingc1="发热量";
								zhi=farl;
								break;
							case 12:
								mingc1="哈氏可磨系数";
								zhi=haskmxs;
								break;
							default: break;
						}
						 
					   sb.append("insert into gongysmzb (id,gongysb_id,mingc,zhi) values(")
					   	.append(Long.parseLong(MainGlobal.getNewID(diancxxb_id))).append(",")
					   	.append(_id).append(",'")
					   	.append(mingc1).append("',")
					   	.append(zhi).append("")
					   	.append(");\n");
					   
					
					}
					sb.append("end;");
					con.getUpdate(sb.toString());		
//					向gongysmzb里插入数据结束
					
					setMId(_id);
					//保存煤矿信息
					//KSave();	
				
					
				}

			} else {
				String sql = "update gonghdwdmsqb\n" + "   set FUID = " + fuid
						+ ",\n" + "       XUH        =" + xuh + ",\n"
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
						+ "       BEIZ       = '" + beiz + "',\n"
						+ "       chengsb_id = " + chengsb_id + "\n"
						+ " where id =" + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + huif
						+ "' where mingc= '灰分' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + huird
						+ "' where mingc= '灰熔点' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + lid
						+ "' where mingc= '粒度' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + liuf
						+ "' where mingc= '硫分' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + huiff
						+ "' where mingc= '挥发分' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + qing
						+ "' where mingc= '氢' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + quansf
						+ "' where mingc= '全水分' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + tan
						+ "' where mingc= '碳' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + yang
						+ "' where mingc= '氧' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + dan
						+ "' where mingc= '氮' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + farl
						+ "' where mingc= '发热量' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				sql = "update gongysmzb set zhi = '" + haskmxs
						+ "' where mingc= '哈氏可磨系数' and gongysb_id = " + _id;
				flag = con.getUpdate(sql);
				
				
			}
			if (flag == -1) {
				System.out.println("数据存储错误!");
				setMsg("保存失败！");
				con.rollBack();
				con.Close();
				return;
			} else {
				con.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void KSave() {
		Visit visit = (Visit) this.getPage().getVisit();
		//Save(getChange(), visit);
		MK_Save();
	}

	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else {
			return value;
		}
	}

	public int getDataColumnCount() {
		int count = 0;
		for (int c = 0; c < getExtGrid().getGridColumns().size(); c++) {
			if (((GridColumn) getExtGrid().getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
				count++;
			}
		}
		return count;
	}

	

	public String getArrayScript() {
		StringBuffer array = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		boolean xianszt = visit.getboolean1();
		
		array.append("var xianszt = " + xianszt + ";\n");
		array.append("var num = "+isXianszt()+";\n");
		return array.toString();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// private boolean _DeleteChick = false;
	//
	// public void DeleteButton(IRequestCycle cycle) {
	// _DeleteChick = true;
	// }

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _KSaveChick = false;

	public void KSaveButton(IRequestCycle cycle) {
		_KSaveChick = true;
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
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			if (((Visit) getPage().getVisit()).getLong9()==0) {//当第一次保存时,返回跳转前的页面,原因:否则煤矿得到gongysb的id为0
				Skipping(cycle);
			} else {
				
			}
		}
		if (_ReturnsChick) {
			_ReturnsChick = false;
			Skipping(cycle);
		}
		if (_KSaveChick) {
			_KSaveChick = false;
			KSave();
			MeikSelectDate(); 
			
		}
	}

	private void Skipping(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
			cycle.activate("Gonghdwdmsq");
	
		
	}
	
	private void getToolbars(){
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb = new Toolbar("tbdiv");
		//egu.addToolbarItem("{"+new GridButton("添加","function(){document.getElementById('InsertButton').click();}").getScript()+"}");
		ToolbarButton but = new ToolbarButton(null,"返回","function(){document.getElementById('ReturnsButton').click();}");
		ToolbarButton but1 = new ToolbarButton(null,"保存","function(){document.getElementById('SaveButton').click();}");
		tb.addItem(but);
		tb.addText(new ToolbarText("-"));
		if(visit.getboolean1()== false){
			tb.addItem(but1);
		}
	
		
	
		
		
		
		
		setToolbar(tb);
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		// List _editvalues = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			getFuid_idModels();
			getShengfb_idModels();
			getXinyModels();
			getShifssModels();
			// long _id = visit.getLong1();
			String sql = "select * from gonghdwdmsqb where id = " + visit.getLong9();
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				visit.setLong1(rs.getLong("FUID"));
				visit.setLong3(rs.getLong("XINY"));
				visit.setLong4(rs.getLong("SHIFSS"));
				visit.setLong2(rs.getLong("SHENGFB_ID"));
				visit.setLong5(rs.getLong("RONGQGX"));
				visit.setLong7(rs.getLong("chengsb_id"));
				setXuh(rs.getInt("XUH"));
				setFuid(getProperValue(getFuid_idModel(), rs.getInt("FUID")));
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
				setRongqgx(getProperValue(getRongqgxModel(), rs.getInt("RONGQGX")));
				setXiny(getProperValue(getXinyModel(), rs.getInt("XINY")));
				setGongsxz(rs.getString("GONGSXZ"));
				setKegywfmz(rs.getString("KEGYWFMZ"));
				setKegywfmzzb(rs.getString("KEGYWFMZZB"));
				setShengfb_id(getProperValue(getShengfb_idModel(), rs.getInt("SHENGFB_ID")));
				setChengsb_id(getProperValue(getChengs_idModel(),rs.getInt("CHENGSB_ID")));
				setShifss(getProperValue(getShifssModel(), rs.getInt("SHIFSS")));
				setShangsdz(rs.getString("SHANGSDZ"));
				setZicbfb(rs.getString("ZICBFB"));
				setShoumbfb(rs.getString("SHOUMBFB"));
				setQitbfb(rs.getString("QITBFB"));
				setBeiz(rs.getString("BEIZ"));
				String SQL = "select (select zhi\n"
						+ "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '灰分') as huif,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '灰熔点') as huird,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '粒度') as lid,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '硫分') as liuf,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '挥发分') as huiff,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '氢') as qing,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '全水分') as quansf,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '碳') as tan,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '氧') as yang,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '氮') as dan,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '发热量') as farl,\n"
						+ "       (select zhi\n" + "          from gongysmzb\n"
						+ "         where gongysb_id = " + visit.getLong9() + "\n"
						+ "           and mingc = '哈氏可磨系数') as haskmxs\n"
						+ "  from gongysmzb z, gonghdwdmsqb g\n"
						+ " where z.gongysb_id = g.id\n" + "   and g.id = "
						+ visit.getLong9()+ "";
				ResultSet r = con.getResultSet(SQL);
				if (r.next()) {
					setHuif(r.getDouble("huif"));
					setHuiff(r.getDouble("huiff"));
					setHuird(r.getDouble("huird"));
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
					setHuird(0);
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
			} else {
				setXuh(0);
				setFuid("");
				setMingc("");
				setQuanc("");
				setPiny("");
				setBianm("");
				setDanwdz("");
				setFaddbr("");
				setWeitdlr("");
				setKaihyh("");
				setZhangh("");
				setDianh("");
				setShuih("");
				setYouzbm("");
				setChuanz("");
				setMeitly("");
				setMeiz("");
				setChubnl("");
				setKaicnl("");
				setKaicnx("");
				setShengcnl("");
				setGongynl("");
				setLiux("");
				setYunsfs("");
				setShiccgl("");
				setZhongdht("");
				setYunsnl("");
				setHeznx("");
				setRongqgx("");
				setXiny("");
				setGongsxz("");
				setKegywfmz("");
				setKegywfmzzb("");
				setShengfb_id("");
				setShifss("");
				setShangsdz("");
				setZicbfb("");
				setShoumbfb("");
				setQitbfb("");
				setBeiz("");
			}
			this.MeikSelectDate();//调用煤矿单位的刷新操作
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
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

	// /////////////////////////////////////////////////////////////////////////////
	public IDropDownBean getFuid_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getFuid_idModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getFuid_idModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong6()) {
					if(idb.getId()==0){
						setXianszt(false);
					}else{
						setXianszt(true);
					}
					((Visit) getPage().getVisit()).setDropDownBean1(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setFuid_idValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getFuid_idModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getFuid_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getFuid_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setFuid_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getFuid_idModels() {
		String sql = "select f.*\n"
				+ "  from (select 0 as id, decode(1, 1, '大供货单位') as fuid\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 1 as id, decode(1, 1, '小供货单位') as fuid from dual) f\n"
				+ " order by id";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, null));
	}

	// ///////////////////////////////////////////////////////////////////////
	public IDropDownBean getShengfb_idValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getShengfb_idModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getShengfb_idModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong2()) {
					((Visit) getPage().getVisit()).setDropDownBean2(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setShengfb_idValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getShengfb_idModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getShengfb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getShengfb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setShengfb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getShengfb_idModels() {
		String sql = "select id,quanc from shengfb order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, null));
	}

	// /////////////////////////////////////////////////////////////////////////////
	//城市下拉框
//	 ///////////////////////////////////////////////////////////////////////
	public IDropDownBean getChengs_idValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			for (int i = 0; i < getChengs_idModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getChengs_idModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong7()) {
					((Visit) getPage().getVisit()).setDropDownBean7(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setChengs_idValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getChengs_idModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public IPropertySelectionModel getChengs_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getChengs_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public void setChengs_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public void getChengs_idModels() {
		String sql = "select id,mingc from chengsb order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(sql, null));
	}

	// /////////////////////////////////////////////////////////////////////////////
	public IDropDownBean getXinyValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getXinyModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getXinyModel().getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong3()) {
					((Visit) getPage().getVisit()).setDropDownBean3(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setXinyValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getXinyModel().getOption(
							0));
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getXinyModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getXinyModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setXinyModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getXinyModels() {
		String sql = "select f.*\n"
				+ "  from (select 1 as id, decode(1, 1, '好') as fuid from dual\n"
				+ "        union\n"
				+ "        select 2 as id, decode(1, 1, '中') as fuid from dual\n"
				+ "        union\n"
				+ "        select 3 as id, decode(1, 1, '差') as fuid from dual) f\n"
				+ " order by id";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, null));
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public IDropDownBean getShifssValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			for (int i = 0; i < getShifssModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getShifssModel().getOption(
						i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong4()) {
					((Visit) getPage().getVisit()).setDropDownBean4(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setShifssValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getShifssModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getShifssModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getShifssModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setShifssModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getShifssModels() {
		String sql = "select f.*\n"
				+ "  from (select 1 as id, decode(1, 1, '是') as fuid from dual\n"
				+ "        union\n"
				+ "        select 0 as id, decode(1, 1, '否') as fuid from dual) f\n"
				+ " order by id";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, null));
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public IDropDownBean getRongqgxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			for (int i = 0; i < getRongqgxModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getRongqgxModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong5()) {
					((Visit) getPage().getVisit()).setDropDownBean5(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setRongqgxValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getRongqgxModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getRongqgxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getRongqgxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setRongqgxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public void getRongqgxModels() {
		String sql = "select f.*\n"
				+ "  from (select 1 as id, decode(1, 1, '好') as fuid from dual\n"
				+ "        union\n"
				+ "        select 2 as id, decode(1, 1, '中') as fuid from dual\n"
				+ "        union\n"
				+ "        select 3 as id, decode(1, 1, '差') as fuid from dual) f\n"
				+ " order by id";
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, null));
	}

	// ///////////////////////////////////////////////////////////////////////////////////
	public IDropDownBean getFUIDValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			for (int i = 0; i < getFUIDModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getFUIDModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong1()) {
					((Visit) getPage().getVisit()).setDropDownBean6(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setFUIDValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getFUIDModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public IPropertySelectionModel getFUIDModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getFUIDModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setFUIDModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public void getFUIDModels() {
		String sql = "select id,mingc from gongysb where fuid = 0 order by xuh, mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(sql, null));
	}
	/////////////////////////////////////////////////////////////////////////////////
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
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String _id = "-1";
		String zhuangt = "";
		_id = (String) cycle.getRequestContext().getParameter("id");
		zhuangt = (String) cycle.getRequestContext().getParameter("zhuangt");
		if (_id == null || _id.equals("")) {
			_id = "-1";
		}
		if (zhuangt == null || zhuangt.equals("")) {
			zhuangt = "0";
		}
		visit.setboolean1(false);
		visit.setboolean2(false);
		
		if (zhuangt.equals("1")) {
			visit.setboolean1(true);
			visit.setboolean2(true);
		}else if (zhuangt.equals("2")) {
			visit.setboolean1(false);
			visit.setboolean2(true);
		}else {
			visit.setboolean1(false);
		}
		
		if (visit.getboolean2() == false) {
			_id = "-1";
			zhuangt = "0";
		}
		if (zhuangt.equals("1") || zhuangt.equals("2")) {
			String sql = "select fuid,xiny,shifss,shengfb_id,rongqgx,chengsb_id from gonghdwdmsqb where id="
					+ Long.parseLong(_id);
			JDBCcon con = new JDBCcon();
			ResultSet rs = con.getResultSet(sql);
			try {
				if (rs.next()) {
					visit.setLong1(rs.getLong("FUID"));
					if(visit.getLong1()==0){
						visit.setLong6(0);
					}else{
						visit.setLong6(1);
					}
					visit.setLong3(rs.getLong("XINY"));
					visit.setLong4(rs.getLong("SHIFSS"));
					visit.setLong2(rs.getLong("SHENGFB_ID"));
					visit.setLong5(rs.getLong("RONGQGX"));
					visit.setLong7(rs.getLong("chengsb_id"));
				} else {
					visit.setLong1(0);
					visit.setLong6(0);
					visit.setLong2(0);
					visit.setLong3(1);
					visit.setLong4(0);
					visit.setLong5(1);
					visit.setLong7(0);
				}
				rs.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			setMId(Long.parseLong(_id));
		} else {
			setMId(-1);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setDropDownBean4(null);
			visit.setDropDownBean5(null);
			visit.setDropDownBean6(null);
			visit.setDropDownBean7(null);
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
			visit.setProSelectionModel6(null);
			visit.setProSelectionModel7(null);
			visit.setList1(null);
			visit.setLong9(Long.parseLong(_id));
			getSelectData();
		}
		getToolbars();
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

	public void MK_Save(){
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql_delete = new StringBuffer("begin \n");

		while (drsl.next()) {
			//删除供应商煤矿关联表,(暂时没有判断diancxxb_id),
			sql_delete.append("delete from ").append(" gongysmkglb ").append(" where meikxxb_id=").append(drsl.getLong("id")).append(";\n");
			//删除矿站关联表(暂时没有判断diancxxb_id)
			sql_delete.append("delete from ").append(" kuangzglb ").append(" where meikxxb_id=").append(drsl.getLong("id")).append(";\n");
			//煤矿信息表不能够删除,这个矿这个电厂不用了,但是有可能其它电厂还在用着.
			sql_delete.append("delete from ").append("meikxxb").append(" where id =").append(drsl.getLong("id")).append(";\n");
			}
		sql_delete.append("end;");
		
		if(sql_delete.length()>11){
			con.getUpdate(sql_delete.toString());
		
		}
		StringBuffer sql=new StringBuffer();
		sql.append("begin \n");
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (rsl.next()) {
		
		
			long meik_id = 0;

			if ("0".equals(rsl.getString("ID"))) {
				meik_id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				sql.append("insert into meikxxb("
								+ "ID,XUH,BIANM,MINGC,QUANC,PINY,SHENGFB_ID,LEIB,JIHKJB_ID,LEIX,BEIZ,DANWDZ,CHENGSB_ID)values("
								+ meik_id+ ","
								+ rsl.getLong("xuh")+",'"
								+ rsl.getString("bianm")+"','"
								+ rsl.getString("mingc")+"','"
								+ rsl.getString("quanc")+"','"
								+ rsl.getString("piny")+"',"
								+"getShengfbId('"+rsl.getString("shengfb_id")+"'),"+"'"
								+ rsl.getString("leib")+"',"
								+"getJihkjbId('"+rsl.getString("jihkjb_id")+"'),'"
								+ rsl.getString("leix") + "','"
								+ rsl.getString("beiz") + "','"
								+ rsl.getString("danwdz") + "',0"
								+ ");\n");
				sql.append("insert into gongysmkglb(id,gongysb_id,meikxxb_id) values(")
				   .append(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()))
				   .append(",").append(visit.getLong9()).append(",")
				   .append(meik_id).append(");\n");
				
				sql.append("insert into kuangzglb (id,meikxxb_id,chezxxb_id) values(")
					.append(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()))
					.append(",").append(meik_id).append(",")
					.append("getChezxxbId('").append(rsl.getString("faz")).append("'));\n");
				
				
			} else {
				 sql.append("update meikxxb set xuh="
				 + rsl.getLong("xuh")+",bianm='"
				 + rsl.getString("bianm")+"',mingc='"
				 + rsl.getString("mingc")+"',quanc='"
				 + rsl.getString("quanc")+"',piny='"
				 + rsl.getString("piny")+"',shengfb_id="
				 +"getShengfbId('"+rsl.getString("shengfb_id")+"'),"+"leib='"
				 + rsl.getString("leib")+"',"
				 +" jihkjb_id="+"getJihkjbId('"+rsl.getString("jihkjb_id")+"'),leix='"
				 + rsl.getString("leix")+"',beiz='"
				 + rsl.getString("beiz")+"',danwdz='"
				 + rsl.getString("danwdz")
				 + "' where id=" + rsl.getLong("id")+";\n");
				 //更新矿站关联表
				 sql.append("update kuangzglb set chezxxb_id=getChezxxbId('"
					+rsl.getString("faz")
					 + "') where meikxxb_id=" + rsl.getLong("id")+";\n");
				
				 
			}
			
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	
	}
	public void MeikSelectDate(){//煤矿添加删除,修改后刷新操作
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select xx.id,\n"
				+ "       xx.xuh,\n" + "       xx.bianm,\n"
				+ "       xx.mingc,\n" + "       xx.quanc,\n"
				+ "       xx.piny,\n" + "       sf.quanc as shengfb_id,\n"
				+ "       jh.mingc as jihkjb_id,\n"
				+ "       (select cz.quanc\n"
				+ "          from kuangzglb kgl, chezxxb cz\n"
				+ "         where kgl.meikxxb_id = xx.id\n"
				+ "           and cz.id = kgl.chezxxb_id\n"
				+ "           and cz.leib = '车站') as faz,\n"
				+ "       xx.leib,\n" + "       xx.leix,\n"
				+ "       xx.danwdz\n"

				+ "  from gongysmkglb gl, meikxxb xx, shengfb sf, jihkjb jh\n"
				+ " where gl.meikxxb_id = xx.id\n"
				+ "   and xx.jihkjb_id = jh.id\n"
				+ "   and sf.id = xx.shengfb_id\n" + "   and gl.gongysb_id = "
				+ visit.getLong9() + "\n" + " order by xx.xuh, xx.mingc");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("meikxxb");
		
		egu.setHeight(320);
		egu.frame = false;
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("bianm").setHeader("编码");

		egu.getColumn("bianm").setEditor(null);

		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setHidden(true);
		egu.getColumn("leix").setHeader("类型");
		egu.getColumn("danwdz").setHeader("单位地址");

		egu.getColumn("faz").setHeader("发站");

		egu.getColumn("xuh").setWidth(40);
		egu.getColumn("bianm").setWidth(100);
		egu.getColumn("mingc").setWidth(80);
		egu.getColumn("quanc").setWidth(100);
		egu.getColumn("piny").setWidth(50);
		egu.getColumn("faz").setWidth(80);
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("leib").setWidth(80);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("leix").setWidth(60);
		egu.getColumn("danwdz").setWidth(100);
		egu.addPaging(7);

		// 省份
		ComboBox cb_shengf = new ComboBox();
		egu.getColumn("shengfb_id").setEditor(cb_shengf);
		cb_shengf.setEditable(true);
		String ShengfSql = "select id,quanc from shengfb order by mingc";
		egu.getColumn("shengfb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ShengfSql));
		// 发站
		ComboBox cb_faz = new ComboBox();
		egu.getColumn("faz").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String faz = "select id,quanc from chezxxb where leib = '车站'";
		egu.getColumn("faz")
				.setComboEditor(egu.gridId, new IDropDownModel(faz));
		// 计划口径
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select id,mingc from jihkjb order by xuh,mingc"));
		egu.getColumn("jihkjb_id").setDefaultValue("市场采购");
		List l = new ArrayList();
		l.add(new IDropDownBean(1, "统配矿"));
		l.add(new IDropDownBean(2, "地方矿"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(l));
		egu.getColumn("leib").editor.selectOnFocus = false;
		egu.getColumn("leib").setReturnId(false);
		egu.getColumn("leib").setDefaultValue("统配矿");
		List h = new ArrayList();
		h.add(new IDropDownBean(1, "煤"));
		h.add(new IDropDownBean(2, "油"));
		h.add(new IDropDownBean(3, "气"));
		egu.getColumn("leix").setEditor(new ComboBox());
		egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(h));
		egu.getColumn("leix").editor.selectOnFocus = false;
		egu.getColumn("leix").setReturnId(false);
		egu.getColumn("leix").setDefaultValue("煤");
		if (visit.getboolean1()) {// 当从供货单位代码申请查看页面进来时,添加保存,删除按钮隐藏,并设定页面不可编辑
			egu.addToolbarItem("{" + new GridButton("", "").getScript() + "}");
			egu.setGridType(ExtGridUtil.Gridstyle_Read);
		} else {
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton(GridButton.ButtonType_Save, "KSaveButton");
		}

		setExtGrid(egu);
		con.Close();
	}

}
