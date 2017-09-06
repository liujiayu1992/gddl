/*
 * 作者:zuoh
 * 时间:2011-05-06
 * 描述:JS中不知如何控制，新作一个打印检斤单，黔东专用，增加品种；
 */
/*
 * 作者:zuoh
 * 时间:2011-07-11
 * 描述: 1.增加查看选择条件：品种下拉框；
 *           2.增加汇总行；
 */
package com.zhiren.dc.jilgl.qichjjddy;

import com.zhiren.common.*;
import com.zhiren.main.Visit;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Qichjjddy_lh extends BasePage {
	String Jianmsj2 = "";
	String Jianpsj2 = "";
	String Danw2 = "";
	String Yunsdw2 = "";
	String Ranlpz2 = "";
	String Kuangfl2 = "";
	String Maoz2 = "";
	String Piz2 = "";
	String Koud2 = "";
	String Jingz2 = "";
	String Cheh2 = "";
	String Meikdw2 = "";
	String X_ID="";
	public static final String Display_Format = "请输入正确的日期格式（yyyy-MM-dd）";

	public static final DateFormat DATE_FORMAT_Time = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private int _editTableRow = -1; // 编辑框中选中的行
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	private Date _jianjsj;

	public Date getJianjsj() {
		if (_jianjsj == null) {
			_jianjsj = new Date();
		}
		return _jianjsj;
	}

	public void setJianjsj(Date value) {
		_jianjsj = value;
	}

	private Date _jianjsj1;

	public Date getJianjsj1() {
		if (_jianjsj1 == null) {
			_jianjsj1 = new Date();
		}
		return _jianjsj1;
	}

	public void setJianjsj1(Date value) {
		_jianjsj1 = value;
	}

	// 获取随车检斤单各项值的方法（开始）
	private String _title = "";

	public String getjianjdTitle() {
		return _title;
	}

	public void setjianjdTitle(String value) {
		_title = value;
	}

	private String _danw = "";

	public String getDanw() {
		return _danw;
	}

	public void setDanw(String value) {
		_danw = value;
	}

	private String _shij = "";

	public String getShij() {
		return _shij;
	}

	public void setShij(String value) {
		_shij = value;
	}

	private String _chex = "";

	public String getChex() {
		return _chex;
	}

	public void setChex(String value) {
		_chex = value;
	}

	// 矿别
	private String _kuangb = "";

	public String getKuangb() {
		return _kuangb;
	}

	public void setKuangb(String value) {
		_kuangb = value;
	}

	//
	private String _chengydw = "";

	public String getChengydw() {
		return _chengydw;
	}

	public void setChengydw(String value) {
		_chengydw = value;
	}

	private String _cheh = "";

	public String getCheh() {
		return _cheh;
	}

	public void setCheh(String value) {
		_cheh = value;
	}

	private String _pinz = "";

	public String getPinz() {
		return _pinz;
	}

	public void setPinz(String value) {
		_pinz = value;
	}

	private String _shouhr = "";

	public String getShouhr() {
		return _shouhr;
	}

	public void setShouhr(String value) {
		_shouhr = value;
	}

	private String _jianmsj;

	public String getJianmsj() {
		return _jianmsj;
	}

	public void setJianmsj(String value) {
		_jianmsj = value;
	}

	private String _jianpsj;

	public String getJianpsj() {
		return _jianpsj;
	}

	public void setJianpsj(String value) {
		_jianpsj = value;
	}

	private double _Maoz;

	public double getMaoz() {
		return _Maoz;
	}

	public void setMaoz(double value) {
		_Maoz = value;
	}

	private double _Piz;

	public double getPiz() {
		return _Piz;
	}

	public void setPiz(double value) {
		_Piz = value;
	}

	private double _Koud;

	public double getKoud() {
		return _Koud;
	}

	public void setKoud(double value) {
		_Koud = value;
	}

	private double _Yuns;

	public double getYuns() {
		return _Yuns;
	}

	public void setYuns(double value) {
		_Yuns = value;
	}

	private double _Jingz;

	public double getJingz() {
		return _Jingz;
	}

	public void setJingz(double value) {
		_Jingz = value;
	}

	private double _Fahl;

	public double getFahl() {
		return _Fahl;
	}

	public void setFahl(double value) {
		_Fahl = value;
	}

	private String _Beiz;

	public String getBeiz() {
		return _Beiz;
	}

	public void setBeiz(String value) {
		_Beiz = value;
	}

	private String _jianjy;

	public String getJianjy() {
		return _jianjy;
	}

	public void setJianjy(String value) {
		_jianjy = value;
	}

	private String _Jianpry;

	public String getJianpry() {
		return _Jianpry;
	}

	public void setJianpry(String Jianpry) {
		_Jianpry = Jianpry;
	}

	private String _meigy;

	public String getMeigy() {
		return _meigy;
	}

	public void setMeigy(String value) {
		_meigy = value;
	}

	// （结束）

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
			_RefurbishChick = false;
			Refurbish();
		}
	}

	private void Refurbish() {
		getSelectData1();
	}

	private void Save() {
		List list = ((Visit) getPage().getVisit()).getList1();
//		List list =null;
				JDBCcon JDBCcon = new JDBCcon();
		for (int i = 0; i < list.size(); i++) {
			if (((Qichjjddybean) list.get(i)).getXuanz() == true) {
				//给标准检斤单赋值
				this.setShij(DATE_FORMAT_Time.format(new Date()));
				this.setKuangb(((Qichjjddybean) list.get(i)).getMeikdwmc());
				this.setPinz(((Qichjjddybean) list.get(i)).getPinz());
				this.setShouhr(((Qichjjddybean) list.get(i)).getShouhr());
				this.setCheh(((Qichjjddybean) list.get(i)).getCheph());
				this.setChengydw(((Qichjjddybean) list.get(i)).getChengydw());
				this.setJianmsj(((Qichjjddybean) list.get(i)).getJianmsj());
				this.setJianpsj(((Qichjjddybean) list.get(i)).getJianpsj());
				this.setMaoz(((Qichjjddybean) list.get(i)).getMaoz());
				this.setPiz(((Qichjjddybean) list.get(i)).getPiz());
				this.setJingz(((Qichjjddybean) list.get(i)).getJingz());
				this.setYuns(((Qichjjddybean) list.get(i)).getYuns());
				this.setKoud(((Qichjjddybean) list.get(i)).getKoud());
				this.setFahl(((Qichjjddybean) list.get(i)).getFahl());
				this.setBeiz(((Qichjjddybean) list.get(i)).getBeiz());
				this.setJianjy(((Qichjjddybean) list.get(i)).getJianjy());
				this.setMeigy(((Qichjjddybean) list.get(i)).getMeigy());
				this.setJianpry(((Qichjjddybean) list.get(i)).getJIANPY());
				try {
					String Sql = "select q.xuh from qichjjbtmp q where q.id = "
							+ ((Qichjjddybean) list.get(i)).getId();
					ResultSet danwrs = JDBCcon.getResultSet(Sql);
					if (danwrs.next()) {
						if (danwrs.getInt("xuh") < 10) {
							this.setChex("00" + danwrs.getInt("xuh"));
						} else if (danwrs.getInt("xuh") >= 10
								&& danwrs.getInt("xuh") < 100) {
							this.setChex("0" + danwrs.getInt("xuh"));
						} else {
							this.setChex("" + danwrs.getInt("xuh"));
						}

					}
					danwrs.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					JDBCcon.Close();
				}
				//套打检斤单
				Qichjjddybean jjd = ((Qichjjddybean) list.get(i));
				setX_ID(((Qichjjddybean) list.get(i)).getId()+"");
				setJianmsj2(jjd.getJianmsj());
				setJianpsj2(jjd.getJianpsj());
				setDanw2(jjd.getMeikdqmc());
				setYunsdw2(jjd.getChengydw());
				setRanlpz2(jjd.getPinz());
				setKuangfl2(jjd.getFahl()+"");
				setMaoz2(jjd.getMaoz()+"");
				setPiz2(jjd.getPiz()+"");
				setKoud2(jjd.getKoud()+"");
				setJingz2(jjd.getJingz()+"");
				setCheh2(jjd.getCheph());
				setMeikdw2(jjd.getMeikdwmc());
			}
		}
	}
	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(
				this.getPageName())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			setRanlpzb_id(null);
			setIRanlpzb_idModel(null);
			visit.setActivePageName(getPageName());
//			visit.setEditValues(null);
			visit.setList1(null);
//			visit.setListData1(null);
			String sql;
			JDBCcon con = new JDBCcon();
			// 获取检斤单标题
			sql = "select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单标题'";
			ResultSet rs3 = con.getResultSet(sql);
			try {
				while (rs3.next()) {
					this.setjianjdTitle(rs3.getString("zhi"));
				}
				rs3.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 获取检斤单单位
			sql = "select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单单位'";
			ResultSet rs4 = con.getResultSet(sql);
			try {
				while (rs4.next()) {
					this.setDanw(rs4.getString("zhi"));
				}
				rs4.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String biaod="否";
			sql = "select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单显示表格' and shifsy=1";
			ResultSet rs = con.getResultSet(sql);
			try {
				while (rs.next()) {
					biaod = rs.getString("zhi");
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(biaod.equals("是")){
				setReportType(0);
			}else{
				setReportType(1);
			}
			getSelectData1();
		}
		
		
	}

	// private String getProperValue(IPropertySelectionModel _selectModel,
	// long value) {
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

	private int EditTableRow1 = -1; // 编辑框中选中的行

	public int getEditTableRow1() {
		return EditTableRow1;
	}

	public void setEditTableRow1(int _value) {
		EditTableRow1 = _value;
	}

	private int ReportType = 0; // 编辑框中选中的行

	public int getReportType() {
		return ReportType;
	}

	public void setReportType(int _value) {
		ReportType = _value;
	}

	public List getEditValues1() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues1(List value) {
		((Visit) getPage().getVisit()).setList1(value);
	}

	private Qichjjddybean _EditValue1;

	public Qichjjddybean getEditValue1() {
		return _EditValue1;
	}

	public void setEditValue1(Qichjjddybean EditValue) {
		_EditValue1 = EditValue;
	}

	public void getSelectData1() {
		List _editvalues = getEditValues1();
		if(_editvalues==null){
			_editvalues=new ArrayList();
		}else {
			_editvalues.clear();
		}

		JDBCcon JDBCcon = new JDBCcon();
		try {
			SysInfo si = new SysInfo();
			String useShortName = "n";
			useShortName = si.getBasicValue("打印汽车衡随车检斤单矿别使用单位全称", "n");

			String sql = "select *\n"
					+ "  from (select q.id,\n"
					+ "               q.cheph,\n"
					+ "               nvl(d.meikdqmc, '盘煤') meikdqmc,\n"
					+ "               nvl(decode('n', '"
					+ useShortName
					+ "', m.mingc, m.quanc), '盘煤') meikdwmc,\n"
					+ "               r.mingc pinz,\n"
					+ "               j.mingc,\n"
					+ "               q.fahl,\n"
					+ "               q.maoz,\n"
					+ "               nvl(q.kouz,0)+nvl(q.koupz,0)+nvl(q.piz,0) as piz,\n"
					+ "               q.maoz - q.piz as jingz,\n"
					+ "               q.koud,\n"
					+ "               q.yuns,\n"
					+ "               mc.meicmc as meic,\n"
					+ "               to_char(q.jianmsj, 'yyyy-mm-dd hh24:mi:ss') as jianmsj,\n"
					+ "               to_char(q.jianpsj, 'yyyy-mm-dd hh24:mi:ss') as jianpsj,\n"
					+ "               q.chengydw,\n"
					+ "               q.meigy,\n"
					+ "               q.jianjy || '   ' || q.jianpy as jianjy,\n"
					+ "               JIANPY,\n"
					+ "               q.shouhr,\n"
					+ "               q.beiz\n"
					+ "          from qichjjbtmp q,\n"
					+ "               meikdqb    d,\n"
					+ "               meikxxb    m,\n"
					+ "               pinzb    r,\n"
					+ "               jihkjb     j,\n"
					+ "               meicfqb    mc\n"
					+ "         where q.meikdqb_id = d.id(+)\n"
					+ "           and q.meikxxb_id = m.id(+)\n"
					+ "           and q.ranlpzb_id = r.id(+)\n"
					+ "           and q.jihkjb_id = j.id(+)\n"
					+ "           and q.meicb_id = mc.id\n"
					+ "           and q.maoz <> 0\n"
					+ "           and q.piz <> 0\n"

					+(getRanlpzb_id().getId()==-1?"": "           and q.ranlpzb_id = " + getRanlpzb_id().getId())

					+ "\n"
					+ "           and to_date(to_char(q.jianmsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') >=\n"
					+ "               to_date('"
					+ FormatDate(getJianjsj())
					+ "', 'yyyy-mm-dd')\n"
					+ "           and to_date(to_char(q.jianmsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') <=\n"
					+ "               to_date('"
					+ FormatDate(getJianjsj1())
					+ "', 'yyyy-mm-dd')\n"
					+ "        union\n"
					+ "        select 0 id,\n"
					+ "               '总计' cheph,\n"
					+ "               '' meikdqmc,\n"
					+ "               '' meikdwmc,\n"
					+ "               '' pinz,\n"
					+ "               '' mingc,\n"
					+ "               sum(q.fahl),\n"
					+ "               sum(q.maoz),\n"
					+ "               sum(q.piz),\n"
					+ "               sum(q.maoz - q.piz) as jingz,\n"
					+ "               sum(q.koud),\n"
					+ "               sum(q.yuns),\n"
					+ "               '' as meic,\n"
					+ "               '' as jianmsj,\n"
					+ "               '' as jianpsj,\n"
					+ "               '' chengydw,\n"
					+ "               '' meigy,\n"
					+ "               '' as jianjy,\n"
					+ "               '' JIANPY,\n"
					+ "               '' shouhr,\n"
					+ "               '' beiz\n"
					+ "          from qichjjbtmp q,\n"
					+ "               meikdqb    d,\n"
					+ "               meikxxb    m,\n"
					+ "               pinzb    r,\n"
					+ "               jihkjb     j,\n"
					+ "               meicfqb    mc\n"
					+ "         where q.meikdqb_id = d.id(+)\n"
					+ "           and q.meikxxb_id = m.id(+)\n"
					+ "           and q.ranlpzb_id = r.id(+)\n"
					+ "           and q.jihkjb_id = j.id(+)\n"
					+ "           and q.meicb_id = mc.id\n"
					+ "           and q.maoz <> 0\n"
					+ "           and q.piz <> 0\n"
					+ (getRanlpzb_id().getId()==-1?"":"           and q.ranlpzb_id = "+ getRanlpzb_id().getId())
					+ "\n"
					+ "           and to_date(to_char(q.jianmsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') >=\n"
					+ "               to_date('"
					+ FormatDate(getJianjsj())
					+ "', 'yyyy-mm-dd')\n"
					+ "           and to_date(to_char(q.jianmsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') <=\n"
					+ "               to_date('" + FormatDate(getJianjsj1())
					+ "', 'yyyy-mm-dd')) t\n" + " order by t.jianmsj\n" + "";
			ResultSet rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				long mid = rs.getLong("id");
				String mcheph = rs.getString("CHEPH");
				String mmeikdqmc = rs.getString("MEIKDQMC");
				String mmeikdwmc = rs.getString("MEIKDWMC");
				String mpinz = rs.getString("PINZ");
				String mjihkj = rs.getString("MINGC");
				double mfahl = rs.getDouble("FAHL");
				double mmaoz = rs.getDouble("MAOZ");
				double mpiz = rs.getDouble("PIZ");
				double mjingz = rs.getDouble("JINGZ");
				double mkoud = rs.getDouble("KOUD");
				double myuns = rs.getDouble("YUNS");
				String mmeic = rs.getString("MEIC");
				String jianmsj = rs.getString("JIANMSJ");
				String jianpsj = rs.getString("JIANPSJ");
				String mchengydw = rs.getString("CHENGYDW");
				String mmeigy = rs.getString("MEIGY");
				String mjianjy = rs.getString("JIANJY");
				String mshouhr = rs.getString("SHOUHR");
				String mbeiz = rs.getString("BEIZ");
				String jianpry = rs.getString("JIANPY");

				Qichjjddybean jj = new Qichjjddybean(false, mid, mcheph,
						mmeikdqmc, mmeikdwmc, mpinz, mjihkj, mfahl, mmaoz,
						mpiz, mjingz, mkoud, myuns, mmeic, jianmsj, jianpsj,
						mchengydw, mmeigy, mjianjy, mshouhr, mbeiz);
				jj.setJIANPY(jianpry);
				_editvalues.add(jj);
				setEditValues1(_editvalues);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		EditTableRow1 = -1;
		return;
	}

	private boolean ranlpzb_idChange;

	public IDropDownBean getRanlpzb_id() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			if (getIRanlpzb_idModel() != null) {
				if (getIRanlpzb_idModel().getOptionCount() > 1) {
					((Visit) getPage().getVisit())
							.setDropDownBean3((IDropDownBean) getIRanlpzb_idModel()
									.getOption(1));
				} else {
					((Visit) getPage().getVisit())
							.setDropDownBean3((IDropDownBean) getIRanlpzb_idModel()
									.getOption(0));
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setRanlpzb_id(IDropDownBean value) {
		long id = -2;
		if (getRanlpzb_id() != null) {
			id = getRanlpzb_id().getId();
		}
		if (value != null) {
			if (value.getId() != id) {
				ranlpzb_idChange = true;
			} else {
				ranlpzb_idChange = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getIRanlpzb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			setIRanlpzb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setIRanlpzb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public void setIRanlpzb_idModels() {

		String sql = "select id,pinz from ranlpzb order by id";
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(-1, "请选择"));
		setIRanlpzb_idModel(new IDropDownModel(dropdownlist, sql));
	}
	public static String Formatdate(String format, Date date) {
		SimpleDateFormat formatter;
		String StrDate;
		try {
			formatter = new SimpleDateFormat(format);
			StrDate = formatter.format(date);
		} catch (IllegalArgumentException IAE) {
			StrDate = "1900-01-01";
			System.out.println("输入格式化字符串格式不正确！");
			// IAE.printStackTrace();
		} catch (NullPointerException NPE) {
			StrDate = "1900-01-01";
			System.out.println("格式化字符串或日期为空!");
			// NPE.printStackTrace();
		} catch (Exception E) {
			StrDate = "1900-01-01";
			System.out.println("未知异常！");
			E.printStackTrace();
		}
		return StrDate;
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return this.Formatdate("yyyy-MM-dd", new Date());
		}
		return this.Formatdate("yyyy-MM-dd", _date);
	}

	public String getCheh2() {
		return Cheh2;
	}

	public void setCheh2(String cheh2) {
		Cheh2 = cheh2;
	}

	public String getDanw2() {
		return Danw2;
	}

	public void setDanw2(String danw2) {
		Danw2 = danw2;
	}

	public String getX_ID() {
		return X_ID;
	}

	public void setX_ID(String chepbtmp_id) {
		X_ID = chepbtmp_id;
	}
	
	public String getJianmsj2() {
		return Jianmsj2;
	}

	public void setJianmsj2(String jianmsj2) {
		Jianmsj2 = jianmsj2;
	}

	public String getJianpsj2() {
		return Jianpsj2;
	}

	public void setJianpsj2(String jianpsj2) {
		Jianpsj2 = jianpsj2;
	}

	public String getJingz2() {
		return Jingz2;
	}

	public void setJingz2(String jingz2) {
		Jingz2 = jingz2;
	}

	public String getKoud2() {
		return Koud2;
	}

	public void setKoud2(String koud2) {
		Koud2 = koud2;
	}

	public String getKuangfl2() {
		return Kuangfl2;
	}

	public void setKuangfl2(String kuangfl2) {
		Kuangfl2 = kuangfl2;
	}

	public String getMaoz2() {
		return Maoz2;
	}

	public void setMaoz2(String maoz2) {
		Maoz2 = maoz2;
	}

	public String getMeikdw2() {
		return Meikdw2;
	}

	public void setMeikdw2(String meikdw2) {
		Meikdw2 = meikdw2;
	}

	public String getPiz2() {
		return Piz2;
	}

	public void setPiz2(String piz2) {
		Piz2 = piz2;
	}

	public String getRanlpz2() {
		return Ranlpz2;
	}

	public void setRanlpz2(String ranlpz2) {
		Ranlpz2 = ranlpz2;
	}

	public String getYunsdw2() {
		return Yunsdw2;
	}

	public void setYunsdw2(String yunsdw2) {
		Yunsdw2 = yunsdw2;
	}

}
