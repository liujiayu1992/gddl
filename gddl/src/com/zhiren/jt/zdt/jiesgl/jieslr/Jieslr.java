package com.zhiren.jt.zdt.jiesgl.jieslr;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;

import com.zhiren.main.Visit;
import org.apache.tapestry.form.IPropertySelectionModel;


import com.zhiren.common.Locale;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
 
public class Jieslr extends BasePage {

	private static int _editTableRow = -1;//编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	private String _msg;

	
	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private static Date _JiesrqValue = new Date();
	private boolean _Jiesrqchange = false;
	public Date getJiesrq() {
		return _JiesrqValue;
	}

	public void setJiesrq(Date _value) {
		if (FormatDate(_JiesrqValue).equals(FormatDate(_value))) {
			_Jiesrqchange = false;
		} else {
			_JiesrqValue = _value;
			_Jiesrqchange = true;
		}
	}
	private static Date _ChengyrqValue = new Date();
	private boolean _Chengyrqchange = false;
	public Date getChengyrq() {
		return _ChengyrqValue;
	}

	public void setChengyrq(Date _value) {
		if (FormatDate(_ChengyrqValue).equals(FormatDate(_value))) {
			_Chengyrqchange = false;
		} else {
			_ChengyrqValue = _value;
			_Chengyrqchange = true;
		}
	}
	private static Date _DaocrqValue = new Date();
	private boolean _Daocrqchange = false;
	public Date getDaocrq() {
		return _DaocrqValue;
	}

	public void setDaocrq(Date _value) {
		if (FormatDate(_DaocrqValue).equals(FormatDate(_value))) {
			_Daocrqchange = false;
		} else {
			_DaocrqValue = _value;
			_Daocrqchange = true;
		}
	}

	private long SaveJiesb() {
		// 存储煤款表
		
//		System.out.print("SaveJiesb()1111");
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)getPage().getVisit();
		String sql = "";
		long Id = 0;
		double hansmk=((Jieslrbean) getEditValues().get(0)).getMeij()*((Jieslrbean) getEditValues().get(0)).getShijjsl();
		try {
			Id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
			sql = 
				"insert into jiesb\n" +
				"  (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr,\n" + 
				"   xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches,\n" + 
				"   jiessl, guohl, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq,\n" + 
				"   ruzrq, hetb_id, liucztb_id, liucgzbid, ranlbmjbr, ranlbmjbrq, beiz, meikxxb_id, meikdwmc,\n" + 
				"   yunsfsb_id, yunj, yingd, kuid, yuns, koud, jiesslcy, hetj,jihkjb_id)"+
				"values("
					+ Id
					+ ", "
					+ visit.getDiancxxb_id()
					+ ",'"
					+ ((Jieslrbean) getEditValues().get(0)).getJiesbh()
					+ "',(select max(id) from gongysb where mingc='"
					+ getGonghdwValue().getValue()
					+"'),'"
					+ getGonghdwValue().getValue()
					+ "','"
					+ getFazValue().getValue()
					+ "', to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getChengyrq())
					+ "','yyyy-MM-dd'), to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getChengyrq())
					+ "','yyyy-MM-dd'),'"
					+ getMeitpzValue().getValue()
					+ "','','','',to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getDaocrq())
					+ "','yyyy-MM-dd'),to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getDaocrq())
					+ "','yyyy-MM-dd'),'','"
					+ getJiesdwValue().getValue()
					+ "','','','','','',"
					+ ((Jieslrbean) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Jieslrbean) getEditValues().get(0)).getShijjsl()
					+ ", "
					+ ((Jieslrbean) getEditValues().get(0)).getJingz()
					+ ", "
					+ //((Jieslrbean) getEditValues().get(0)).getHansdj()
					  ((Jieslrbean) getEditValues().get(0)).getMeij()
					+ ", "
					+ ((Jieslrbean) getEditValues().get(0)).getJiakmk()
					+ ","
					+ //hansmk
					  ((Jieslrbean) getEditValues().get(0)).getJiashj()
					+", "  
					+ ((Jieslrbean) getEditValues().get(0)).getBuhsmk()
					+ ", 0"
//					 ((Jieslrbean) getEditValues().get(0)).getJiashj()
					+ ","
					+ ((Jieslrbean) getEditValues().get(0)).getShuij()
					+ ","
					+ ((Jieslrbean) getEditValues().get(0)).getShuil()
					+ ", "
					+ ((Jieslrbean) getEditValues().get(0)).getBuhsdj()
					+ ",1, to_date('"
					+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())
					+ "','yyyy-MM-dd'), to_date('"
					+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())
					+ "','yyyy-MM-dd'),(select max(id) from hetb where hetbh='"
					+ getHetbhValue().getValue()
					+ "'),0,0,'"
					+ ((Jieslrbean) getEditValues().get(0)).getJingbr()
					+"',to_date('"
					+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Jieslrbean) getEditValues().get(0)).getBeiz()
					+ "',0,'',(select id from yunsfsb where mingc='"
					+ getYunsfsValue().getValue()
					+ "'),"
					+ ((Jieslrbean) getEditValues().get(0)).getYunsjl()
					+","
					+ ((Jieslrbean) getEditValues().get(0)).getYingd()
					+","
					+ ((Jieslrbean) getEditValues().get(0)).getKuid()
					+","
					+ ((Jieslrbean) getEditValues().get(0)).getYuns()
					+","
					+ ((Jieslrbean) getEditValues().get(0)).getKoud()
					+","
					+ ((Jieslrbean) getEditValues().get(0)).getChayl()
					+","
					+ ((Jieslrbean) getEditValues().get(0)).getJiashj()
					+",(select id from jihkjb where mingc='"+getJihkjValue().getValue()+"'))";

			if (con.getInsert(sql) == 1) {
				
				((Jieslrbean) getEditValues().get(0)).setId(Id);
				return Id;
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return 0;
	}
	

	private long Savejiesyfb(long jiesbid) {
		// 存储运费表
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql = "";
		long Id = 0;
		double jiskc=((Jieslrbean) getEditValues().get(0)).getHej()+((Jieslrbean) getEditValues().get(0)).getQityzf();
		double hansdj=((Jieslrbean) getEditValues().get(0)).getYunfhj()/((Jieslrbean) getEditValues().get(0)).getShijjsl();
		double dityf=0;
		if(((Jieslrbean) getEditValues().get(0)).getHansyf_k()==0){
			dityf=((Jieslrbean) getEditValues().get(0)).getHansyf_q();
		}else{
			dityf=((Jieslrbean) getEditValues().get(0)).getHansyf_k();
		}
		
//		long Jiesdid=SaveJiesb(Id);
		try {

			Id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
			sql ="insert into jiesyfb(id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch,\n" +
							"yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl,\n" + 
							"guohl, guotyf, dityf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq,\n" + 
							"hetb_id, liucztb_id, liucgzbid, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, beiz, meikxxb_id, meikdwmc, yunsfsb_id,\n" + 
							"yunj, yingd, kuid, yuns, koud, jiesslcy, guotzf, ditzf,guotyfjf,guotzfjf,qiyf)\n" + 
							" values("
							+ Id
							+ ", "
							+ visit.getDiancxxb_id()
							+ ",'"
							+ ((Jieslrbean) getEditValues().get(0)).getJiesbh()
							+ "',(select max(id) from gongysb where mingc='"
							+ getGonghdwValue().getValue()
							+"'),'"
							+ getGonghdwValue().getValue()
							+ "','"
							+ getFazValue().getValue()
							+ "', to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getChengyrq())
							+ "','yyyy-MM-dd'),to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getChengyrq())
							+ "','yyyy-MM-dd'),'"
							+ getMeitpzValue().getValue()
							+ "','','','',to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getDaocrq())
							+ "','yyyy-MM-dd'),to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getDaocrq())
							+ "','yyyy-MM-dd'),'','"
							+ getJiesdwValue().getValue()
							+ "','','','','','',"
							+ ((Jieslrbean) getEditValues().get(0)).getChes()
							+ ", "
							+ ((Jieslrbean) getEditValues().get(0)).getShijjsl()
							+ ", "
							+ ((Jieslrbean) getEditValues().get(0)).getJingz()
							+ ", "
							+ //((Jieslrbean) getEditValues().get(0)).getHansyf_q()
							  ((Jieslrbean) getEditValues().get(0)).getYingfyf()
							+ ", "
							+ ((Jieslrbean) getEditValues().get(0)).getHansyf_k()
							+ ","
							+ jiskc
							+","
							+ //((Jieslrbean) getEditValues().get(0)).getHansdj()
							  hansdj
							+ ","
							+ ((Jieslrbean) getEditValues().get(0)).getQityzf()
							+", "
							+ ((Jieslrbean) getEditValues().get(0)).getYunfhj()
							+ ","
							+ ((Jieslrbean) getEditValues().get(0)).getYunf()
							+ ","
							+ ((Jieslrbean) getEditValues().get(0)).getYunfsj()
							+ ", "
							+ ((Jieslrbean) getEditValues().get(0)).getYunfsl()
							+ ","
							+ ((Jieslrbean) getEditValues().get(0)).getBuhsdj()
							+",1, to_date('"
							+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())
							+ "','yyyy-MM-dd'), to_date('"
							+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())
							+ "','yyyy-MM-dd'),(select max(id) from hetb where hetbh='"
							+ getHetbhValue().getValue()
							+ "'),0,0,"
							+ jiesbid
							+",'"
							+ ((Jieslrbean) getEditValues().get(0)).getJingbr()
							+"',to_date('"
							+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())
							+"','yyyy-MM-dd'),'"
							+ ((Jieslrbean) getEditValues().get(0)).getBeiz()
							+ "',0,'',(select id from yunsfsb where mingc='"
							+ getYunsfsValue().getValue()
							+ "'),"
							+ ((Jieslrbean) getEditValues().get(0)).getYunsjl()
							+","
							+ ((Jieslrbean) getEditValues().get(0)).getYingd()
							+","
							+ ((Jieslrbean) getEditValues().get(0)).getKuid()
							+","
							+ ((Jieslrbean) getEditValues().get(0)).getYuns()
							+","
							+ ((Jieslrbean) getEditValues().get(0)).getKoud()
							+","
							+ ((Jieslrbean) getEditValues().get(0)).getChayl()
							+","
							+ ((Jieslrbean) getEditValues().get(0)).getYingfyf_b()
							+",0,"
							+ ((Jieslrbean) getEditValues().get(0)).getYingkyf()
							+","
							+ ((Jieslrbean) getEditValues().get(0)).getYingkyf_b()
							+","
							+((Jieslrbean) getEditValues().get(0)).getHansyf_q()
							+")";

			if (con.getInsert(sql) == 1) {
				
				((Jieslrbean) getEditValues().get(0)).setYid(Id);
				return Id;
			}
				

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return 0;
	}
	public List getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		if(getEditValues()!=null){
			
			getEditValues().clear();
		}
		JDBCcon JDBCcon = new JDBCcon();
		try {
			long mid = 0;
			long myid=0;
			long mhetid=0;
			String mjiesbh = "";
			String mhetbh="";
			String mjiesdw="";
			String mfaz = "";
			String mmeitpz="";
			String mgongysdw="";
			String myunsjl="0";
			String myunsfs="";
			String mjihkj = "";
			Date mjiesrq = new Date();
			Date mchengyrq = new Date();
			Date mdaocrq = new Date();
			double mmeij = 0;
			double myingfyf = 0;
			long mches = 0;
			double mkuid = 0;
			double mjiakmk = 0;
			double myingkyf = 0;
			double mpiaoz = 0;
			double myuns = 0;
			double mhansdj = 0;
			double mhej = 0;
			double mjingz = 0;
			double mkoud = 0;
			double mbuhsdj = 0;
			double myingfyf_b = 0;
			double myingd = 0;
			double mchayl = 0;
			double mshuil = 0.13;
			double myingkyf_b = 0;
			double mshijjsl = 0;
			double mbuhsmk = 0;
			double mhej_b = 0;
			double mshuij = 0;
//			double mkuangyf = 0;
			String mrez_ht = "0";
			double mrez_ys = 0;
			double mrez_gf = 0;
			double mrez_js = 0;
			double mjiashj = 0;
//			double mqiyf = 0;
			String mhuif_ht = "0";
			double mhuif_ys = 0;
			double mhuif_gf = 0;
			double mhuif_js = 0;
			double myunfsl = 0.07;
			double mqityzf = 0;
			String mhuiff_ht = "0";
			double mhuiff_ys = 0;
			double mhuiff_gf = 0;
			double mhuiff_js = 0;
			double myunf = 0;
//			double mguot = 0;
			String mshuif_ht = "0";
			double mshuif_ys = 0;
			double mshuif_gf = 0;
			double mshuif_js = 0;
			double myunfsj = 0;
//			double mkuangy = 0;
			String mliuf_ht = "0";
			double mliuf_ys = 0;
			double mliuf_gf = 0;
			double mliuf_js = 0;
			double myunfhj = 0;
//			double mqiy = 0;
			String mbeiz = "";
			double mshifzje=0;
			String mshifzjedx="";
			double mhansyf_k=0;
			double mshuij_k=0;
			double mhansyf_q=0;
			double mshuij_q=0;
			String mjingbr= visit.getDiancmc();
			_editvalues.add(new Jieslrbean(mid,myid,mhetid, mjiesbh,mhetbh,mjiesdw,
					mfaz,mmeitpz,mgongysdw,myunsjl,myunsfs,mjihkj,
					mjiesrq,mchengyrq,mdaocrq,mmeij,myingfyf,mches,
					mkuid, mjiakmk, myingkyf, mpiaoz, myuns, mhansdj,
					mhej, mjingz, mkoud, mbuhsdj, myingfyf_b, myingd,
					mchayl, mshuil, myingkyf_b, mshijjsl, mbuhsmk, mhej_b,
					mshuij, //mkuangyf,
					mrez_ht, mrez_ys, mrez_gf, mrez_js,
					mjiashj,// mqiyf,
					mhuif_ht, mhuif_ys, mhuif_gf, mhuif_js,
					myunfsl, mqityzf, mhuiff_ht, mhuiff_ys, mhuiff_gf,
					mhuiff_js, myunf, //mguot, 
					mshuif_ht, mshuif_ys,
					mshuif_gf, mshuif_js, myunfsj, //mkuangy,
					mliuf_ht, mliuf_ys, mliuf_gf, mliuf_js, myunfhj, //mqiy,
					 mbeiz,mjingbr,mshifzje,mshifzjedx,mhansyf_k,mshuij_k,mhansyf_q,mshuij_q));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
			
		}
		if (_editvalues == null) {
			_editvalues.add(new Jieslrbean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}
	
	private long SaveJszbsjb(long jiesbid, String bianm, String hetbz,
			double gongf, double changf, double jies, double yingk,
			double zhejbz, double zhejje, int zhuangt) {
		// 保存结算单中指标数据
		JDBCcon con = new JDBCcon();
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;
		try {

			Id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));

			sql = "insert into jieszbsjb\n" +
						"  (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id)\n" + 
					"values("

					+ Id
					+ ", "
					+ jiesbid
					+ ", (select id from zhibb where bianm='"
					+ bianm
					+ "'), '"
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
					+ ",0)";

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
	private boolean checkbh(String jiesbh) {
		// TODO 自动生成方法存根
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
			sql = " select jiesbh from ((select bianm as jiesbh from jiesb) union (select bianm as jiesbh from jiesyfb)) where jiesbh='" + jiesbh + "'";
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
	private boolean Save() {
		// 重要说明：所有结算表未填写 合同id、流程状态id、流程跟踪id、矿方结算id
		JDBCcon con = new JDBCcon();
		String msg="";
		long Jiesbid = 0;// 结算表id
		long Jiesyfbid = 0;// 结算运费表id
		long Zbbid = 0;// 指标表id
		boolean Flag = false;
		try {
			if (getEditValues() != null
					&& !getEditValues().isEmpty()
					&& !((Jieslrbean) getEditValues().get(0)).getJiesbh()
							.equals("")) {
				if (checkbh(((Jieslrbean) getEditValues().get(0)).getJiesbh())) {

						if (((Jieslrbean) getEditValues().get(0)).getId() == 0) {
							// 电厂煤矿表
							Jiesbid = SaveJiesb();

							if (Jiesbid > 0) {
								// 操作电厂结算运费表
								Jiesyfbid = Savejiesyfb(Jiesbid);

								if (Jiesyfbid > 0) {
									// 要和基础信息指标模块定一下zhibb内容
									Zbbid = this.SaveJszbsjb(Jiesbid,
											"数量",
											"0",
											((Jieslrbean) getEditValues()
													.get(0)).getPiaoz(),
											((Jieslrbean) getEditValues()
													.get(0)).getJingz()
											+((Jieslrbean) getEditValues()
													.get(0)).getYuns(),
											((Jieslrbean) getEditValues()
													.get(0)).getShijjsl(),
											0,
											0,
											0,
											1);
											if (Zbbid > 0) {
										Zbbid = this.SaveJszbsjb(Jiesbid,
//												"收到基低位热值",
												"收到基低位热值Qnetar(MJ/Kg)",
												((Jieslrbean) getEditValues()
														.get(0)).getRez_ht(),
												((Jieslrbean) getEditValues()
														.get(0)).getRez_gf(),
												((Jieslrbean) getEditValues()
														.get(0)).getRez_ys(),
												((Jieslrbean) getEditValues()
														.get(0)).getRez_js(),
												0,
												0,
												0,
												1);
													if (Zbbid > 0) {

													Zbbid = this
															.SaveJszbsjb(
																	Jiesbid,
//																	"干燥基灰分",
																	"干燥基灰分Ad(%)",
																	((Jieslrbean) getEditValues()
																			.get(0)).getHuif_ht(),
																	((Jieslrbean) getEditValues()
																			.get(0)).getHuif_gf(),
																	((Jieslrbean) getEditValues()
																			.get(0)).getHuif_ys(),
																	((Jieslrbean) getEditValues()
																			.get(0)).getHuif_js(),
																	0,
																	0,
																	0,
																	1);
											if (Zbbid > 0) {

												Zbbid = this
														.SaveJszbsjb(
																Jiesbid,
//																"干燥无灰基挥发分",
																"干燥无灰基挥发分Vdaf(%)",
																((Jieslrbean) getEditValues()
																		.get(0)).getHuiff_ht(),
																((Jieslrbean) getEditValues()
																		.get(0)).getHuiff_gf(),
																((Jieslrbean) getEditValues()
																		.get(0)).getHuiff_ys(),
																((Jieslrbean) getEditValues()
																		.get(0)).getHuiff_js(),
																0,
																0,
																0,
																1);
													if (Zbbid > 0) {

														Zbbid = this
																.SaveJszbsjb(
																		Jiesbid,
//																		"全水分",
																		"全水分Mt(%)",
																		((Jieslrbean) getEditValues()
																				.get(0)).getShuif_ht(),
																		((Jieslrbean) getEditValues()
																				.get(0)).getShuif_gf(),
																		((Jieslrbean) getEditValues()
																				.get(0)).getShuif_ys(),
																		((Jieslrbean) getEditValues()
																				.get(0)).getShuif_js(),
																		0,
																		0,
																		0,
																		1);
															if (Zbbid > 0) {

																Zbbid = this.SaveJszbsjb(
																		Jiesbid,
//																		"干燥基全硫",
																		"干燥基全硫Std(%)",
																		((Jieslrbean) getEditValues()
																				.get(0)).getLiuf_ht(),
																		((Jieslrbean) getEditValues()
																				.get(0)).getLiuf_gf(),
																		((Jieslrbean) getEditValues()
																				.get(0)).getLiuf_ys(),
																		((Jieslrbean) getEditValues()
																				.get(0)).getLiuf_js(),
																		0,
																		0,
																		0,
																		1);
														if(Zbbid > 0){
															
															Flag = true;
														}

													}
												}
											}
										}
									}
								}
							}
						}	
				} else {

					msg="结算单编号重复";
				}
			} else {

				msg="不能保存空结算单";
			}

			if (Flag) {

				((Jieslrbean) getEditValues().get(0)).setId(Jiesbid+Jiesyfbid);
				setMsg("结算操作成功！");
			} else {

				setMsg(msg+" 结算操作失败！");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return Flag;
	}
	private void Retruns() {

		getSelectData();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			Retruns();
		}
		if(_ReturnChick){
			_ReturnChick=false;
			
			((Visit) getPage().getVisit()).getPagePreferences().equals("Jiesdtb");
				
				cycle.activate("Jiesdtb");

		}

	}
	
	
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	
	private Jieslrbean _EditValue;
	public Jieslrbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Jieslrbean EditValue) {
		_EditValue = EditValue;
	}
	
	
	
	public String getTitle(){
		
		return Locale.jiesd_title;
	}


	
	private void getToolbars(){

		Toolbar tb1 = new Toolbar("tbdiv");
		
		ToolbarButton fanhbt=new ToolbarButton(null,"返回","function (){ document.getElementById('ReturnButton').click();}");
		fanhbt.setId("fanhbt");
		tb1.addItem(fanhbt);
		tb1.addText(new ToolbarText("-"));
//		刷新
		ToolbarButton shuaxbt=new ToolbarButton(null,"刷新","function(){ document.Form0.RetrunsButton.click();}");
		shuaxbt.setId("Shuaxbt");
		tb1.addItem(shuaxbt);
		tb1.addText(new ToolbarText("-"));
//		保存
		ToolbarButton savebt=new ToolbarButton(null,"保存","function(){ document.Form0.SaveButton.click(); }");
		savebt.setId("savebt");
		tb1.addItem(savebt);
		tb1.addText(new ToolbarText("-"));
		
		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(0);
			_JiesrqValue = new Date();
			_ChengyrqValue = new Date();
			_DaocrqValue = new Date();

			setHetbhValue(null);
			getIHetbhModels();
			setJiesdwValue(null);
			getIJiesdwModels();
			setJihkjValue(null);
			getIJihkjModels();
			setFazValue(null);
			getIFazModels();
			setGonghdwValue(null);
			getIGonghdwModels();
			setMeitpzValue(null);
			getIMeitpzModels();
			setYunsfsValue(null);
			getIYunsfsModels();
			

			
			getSelectData();
		}
		getToolbars();
		
	}

	
	public void setJieszb(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getJieszb(){
		
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	//工具条_begin
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	//工具条_end
	
	public boolean isGongsdp() {
		return ((Visit) getPage().getVisit()).getboolean2();
	}

	public void setGongsdp(boolean gongsdp) {
		((Visit) getPage().getVisit()).setboolean2(gongsdp);
	}
	
	public boolean isDiancdp() {
		return ((Visit) getPage().getVisit()).getboolean3();
	}

	public void setDiancdp(boolean diancdp) {
		((Visit) getPage().getVisit()).setboolean3(diancdp);
	}
//	********************合同编码********************************************************
	 public IDropDownBean getHetbhValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean14()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean14((IDropDownBean)getIHetbhModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean14();
	    }
	    
	    public void setHetbhValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean14()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean14(value);
	        }
	    }
	    
	    public void setIHetbhModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel14(value);
		}
	    
	    public IPropertySelectionModel getIHetbhModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel14()==null){
	        
	    		getIHetbhModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel14();
	    }
	    
	    public IPropertySelectionModel getIHetbhModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = " select ht.id, ht.hetbh from hetb ht,diancxxb dc where dc.id = ht.diancxxb_id and dc.id ="+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by ht.id";
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
					.setProSelectionModel14(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel14();
		}
	//****************************************************************************
	//********************结算单位（收款单位）********************************************************
	 public IDropDownBean getJiesdwValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getIJiesdwModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean10();
	    }
	    
	    public void setJiesdwValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean10()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean10(value);
	        }
	    }
	    
	    public void setIJiesdwModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel10(value);
		}
	    
	    public IPropertySelectionModel getIJiesdwModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel10()==null){
	        
	    		getIJiesdwModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel10();
	    }
	    
	    public IPropertySelectionModel getIJiesdwModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select id, quanc from shoukdw order by id";
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
					.setProSelectionModel10(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel10();
		}
	//****************************************************************************
	//********************发站********************************************************
	 public IDropDownBean getFazValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean11()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean11((IDropDownBean)getIFazModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean11();
	    }
	    
	    public void setFazValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean11()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean11(value);
	        }
	    }
	    
	    public void setIFazModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel11(value);
		}
	    
	    public IPropertySelectionModel getIFazModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel11()==null){
	        
	    		getIFazModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel11();
	    }
	    
	    public IPropertySelectionModel getIFazModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select id, mingc from chezxxb";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {

					List.add(new IDropDownBean(i++, rs.getString("mingc")));
				}
				rs.close();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {

				con.Close();
			}
			((Visit) getPage().getVisit())
					.setProSelectionModel11(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel11();
		}
	//****************************************************************************
	//********************煤炭品种********************************************************
	 public IDropDownBean getMeitpzValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean12()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean12((IDropDownBean)getIMeitpzModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean12();
	    }
	    
	    public void setMeitpzValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean12()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean12(value);
	        }
	    }
	    
	    public void setIMeitpzModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel12(value);
		}
	    
	    public IPropertySelectionModel getIMeitpzModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel12()==null){
	        
	    		getIMeitpzModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel12();
	    }
	    
	    public IPropertySelectionModel getIMeitpzModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select id, mingc from pinzb order by id";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {

					List.add(new IDropDownBean(i++, rs.getString("mingc")));
				}
				rs.close();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {

				con.Close();
			}
			((Visit) getPage().getVisit())
					.setProSelectionModel12(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel12();
		}
	//****************************************************************************
	//********************供货单位********************************************************
	 public IDropDownBean getGonghdwValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getIGonghdwModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean2();
	    }
	    
	    public void setGonghdwValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean2()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean2(value);
	        }
	    }
	    
	    public void setIGonghdwModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel2(value);
		}
	    
	    public IPropertySelectionModel getIGonghdwModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel2()==null){
	        
	    		getIGonghdwModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel2();
	    }
	    
	    public IPropertySelectionModel getIGonghdwModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select id, mingc from gongysb order by id";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {
					List.add(new IDropDownBean(i++, rs.getString("mingc")));
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

	//****************************************************************************
//	  ********************运输方式********************************************************
	 public IDropDownBean getYunsfsValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean3()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getIYunsfsModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean3();
	    }
	    
	    public void setYunsfsValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean3()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean3(value);
	        }
	    }
	    
	    public void setIYunsfsModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel3(value);
		}
	    
	    public IPropertySelectionModel getIYunsfsModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel3()==null){
	        
	    		getIYunsfsModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel3();
	    }
	    
	    public IPropertySelectionModel getIYunsfsModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select id,mingc from yunsfsb order by id";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {

					List.add(new IDropDownBean(i++, rs.getString("mingc")));
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
	//****************************************************************************
	//		  ********************计划口径********************************************************
		 public IDropDownBean getJihkjValue() {
		    	if(((Visit)getPage().getVisit()).getDropDownBean4()==null){
		    		
		    		((Visit)getPage().getVisit()).setDropDownBean4((IDropDownBean)getIJihkjModel().getOption(0));
		    	}
		       return ((Visit)getPage().getVisit()).getDropDownBean4();
		    }
		    
		    public void setJihkjValue(IDropDownBean value)
		    {
		    	if(((Visit)getPage().getVisit()).getDropDownBean4()!=value){
		    		
		    		((Visit)getPage().getVisit()).setDropDownBean4(value);
		        }
		    }
		    
		    public void setIJihkjModel(IPropertySelectionModel value){
	
		    	((Visit)getPage().getVisit()).setProSelectionModel4(value);
			}
		    
		    public IPropertySelectionModel getIJihkjModel(){
		            
		    	if(((Visit)getPage().getVisit()).getProSelectionModel4()==null){
		        
		    		getIJihkjModels();
		        }
		        return ((Visit)getPage().getVisit()).getProSelectionModel4();
		    }
		    
		    public IPropertySelectionModel getIJihkjModels() {
	
				JDBCcon con = new JDBCcon();
				List List = new ArrayList();
				try {
	
					int i = -1;
					List.add(new IDropDownBean(i++, "请选择"));
					String sql = "select id, mingc from jihkjb order by id";
					ResultSet rs = con.getResultSet(sql);
					while (rs.next()) {
	
						List.add(new IDropDownBean(i++, rs.getString("mingc")));
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
		//****************************************************************************
    
//   结算编号 DropDownBean6
//   结算编号 ProSelectionModel6
    
    public void setIJiesbhModel(IPropertySelectionModel value){
        
        ((Visit)getPage().getVisit()).setProSelectionModel6(value);
    }
    
    public IPropertySelectionModel getIJiesbhModel(){
    	if(((Visit)getPage().getVisit()).getProSelectionModel6()==null){
    		getIJiesbhModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel6();
    }
    
    public IPropertySelectionModel getIJiesbhModels(){
    	
    	String sql = "";
    	JDBCcon con=new JDBCcon();
    	List List=new ArrayList();
    	try{
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));
    		if(((Visit)getPage().getVisit()).getRenyjb()==3){
    			
    			sql=" select bianm from "
    				+ " (select distinct bianm from jiesb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct bianm from jiesyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by bianm";
    		}
    		
    		ResultSet rs=con.getResultSet(sql);
    		while(rs.next()){
    			
    			List.add(new IDropDownBean(i++, rs.getString("bianm")));
    		}
    		rs.close();
    	}catch(Exception e){
    		
    		e.printStackTrace();
    	}finally{
    		
    		con.Close();
    	}
    	
    	((Visit)getPage().getVisit()).setProSelectionModel6(new IDropDownModel(List));
		return ((Visit)getPage().getVisit()).getProSelectionModel6();
    }
    
    public IDropDownBean getJiesbhValue() {
    	
    	if(((Visit)getPage().getVisit()).getDropDownBean6()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean6((IDropDownBean)getIJiesbhModel().getOption(0));
    	}
    	
		return ((Visit)getPage().getVisit()).getDropDownBean6();
	}
	
    public void setJiesbhValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean6()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean6(value);
		}
	}
//    end
    
	
//	流程名称 DropDownBean8
//  流程名称 ProSelectionModel8
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getILiucmcModels() {
		
		String sql="";
		sql="select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='结算' order by mingc";

		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
//   流程名称 end
	
//***************************************************************************//
	public boolean getRaw() {
		return true;
	}

//	格式化
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	
	public String formatq(String strValue){//加千位分隔符
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			
			strtmp=strValue;
			if(strValue.equals("")){
				
				xiaostmp="";
			}else{
				
				xiaostmp=".00";
			}
			
		}else {
			
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
			
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		
		return strtmp+xiaostmp;
	}
	
public IPropertySelectionModel getIFahdwModels(){
    	
		JDBCcon con=new JDBCcon();
		List List=new ArrayList();
    	String sql="";
    	try{
    		
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));
    		if(((Visit)getPage().getVisit()).getRenyjb()==3){
        		
        		sql=" select gongysmc from "
    				+ " (select distinct gongysmc from jiesb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysmc from jiesyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        	
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
        		
        		sql=" select gongysmc from "
    				+ " (select distinct gongysmc from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysmc from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        		
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
        		
        		sql="select gongysmc from "
    				+ " (select distinct gongysmc from diancjsmkb where liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysmc from diancjsyfb where liucztb_id=0) order by gongysmc";
        	}
    		
    		ResultSet rs=con.getResultSet(sql);
    		while(rs.next()){	
    			
    			List.add(new IDropDownBean(i++, rs.getString("gongysmc")));
    		}
    		rs.close();
    	}catch(Exception e){
    		
    		e.printStackTrace();
    	}finally{
    		
    		con.Close();
    	}
    	((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(List));
        return ((Visit)getPage().getVisit()).getProSelectionModel2();
    }
	

    //********************************************************************************//
    
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
