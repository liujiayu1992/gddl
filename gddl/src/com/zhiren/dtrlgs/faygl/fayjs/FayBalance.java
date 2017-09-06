package com.zhiren.dtrlgs.faygl.fayjs;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.DateUtil;
import com.zhiren.common.Money;
import com.zhiren.main.Visit;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.chengbgl.Chengbcl;

public class FayBalance extends BasePage {
	// List1
	// dropdwon1,2

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	private String _msg;

	public String _liucmc;

	protected void initialize() {
		_msg = "";
		_liucmc = "";
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

	public void setLiucmc(String _value) {
		_liucmc = _value;
	}

	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}

	public String getTitle() {

		return "燃 料 销 售 结 算 单";
	}

	public String getJiesslcyText() {

		return Locale.jiesslcy_title;
	}
    
	private String getDiancjsbs(long diancxxb_id) {

		JDBCcon con = new JDBCcon();
		String sql = "", diancjsbs = "";

		try {

			sql = "select JIESBDCBS from diancxxb where id=" + diancxxb_id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				diancjsbs = rs.getString("JIESBDCBS") + "-";
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return diancjsbs;
	}

	private long SaveDiancjsmkb(JDBCcon con) {
		// 存储煤款表
		String sql = "";
		long Id = 0;

		try {

			if (((Dcbalancebean) getEditValues().get(0)).getMeikjsb_id() > 0) {
//				如果danpcjsmxb的jiesbid大于0说明danpcjsmxb表中有信息要和jiesb相关联，
//					故结算表的id应取danpcjsmxb的jiesbid。
				Id = ((Dcbalancebean) getEditValues().get(0)).getMeikjsb_id();
			} else {

				Id = Long
						.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getLong1()));
			}

			sql = "insert into diancjsmkb (id, diancxxb_id, bianm, gongysb_id, gongysmc,  \n" +
					"faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, \n" +
					"zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl,/* yuns, koud,*/ hansdj, bukmk, hansmk, buhsmk, \n" +
					"meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, LIUCGZID, beiz, RANLBMJBR, RANLBMJBRQ ,\n" +
					"jihkjb_id,yewlxb_id) "
					+ " values ("
					+ Id
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong1()
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesbh()
					+ "',"
					+ MainGlobal
							.getTableId("gongysb", "quanc",
									((Dcbalancebean) getEditValues().get(0))
											.getFahdw())
					+ ", '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFahdw()
					+ "','"
//					+ ((Dcbalancebean) getEditValues().get(0)).getYunsfsb_id()
//					+ ","
//					+ ((Dcbalancebean) getEditValues().get(0)).getYunju()
//					+ ","
//					+ ((Dcbalancebean) getEditValues().get(0)).getYingd()
//					+ ","
//					+ ((Dcbalancebean) getEditValues().get(0)).getKuid()
//					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getFaz()
					+ "', to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean) getEditValues().get(0)).getPinz()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDaibcc()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getYuanshr()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getXianshr()
					+ "', to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean) getEditValues().get(0)).getYansbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getShoukdw()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDuifdd()
					+ "',"
					+ ((Dcbalancebean) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiessl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJingz()
//					+ ", "
//					+ ((Dcbalancebean) getEditValues().get(0)).getYuns()
//					+ ", "
//					+ ((Dcbalancebean) getEditValues().get(0)).getKoud_js()
					+ ", "
//					+ ((Dcbalancebean) getEditValues().get(0)).getJiesslcy()
//					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getShulzjbz()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getBukyqjk()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiasje()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiakhj()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiakje()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getJiaksk()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getJiaksl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getBuhsdj()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJieslx()
					+ ", to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getJiesrq())
					+ "','yyyy-MM-dd'),null,"
					+ ((Dcbalancebean) getEditValues().get(0)).getHetb_id()
					+ ",0,0,'"
					+ ((Dcbalancebean) getEditValues().get(0)).getBeiz()
					+ "','"
					+ ((Dcbalancebean) getEditValues().get(0)).getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getRanlbmjbrq()) + "','yyyy-MM-dd'),"
					+ ((Dcbalancebean) getEditValues().get(0)).getJihkjb_id()
					+","
					+((Visit)this.getPage().getVisit()).getLong10()//业务类型表id
					+ ")";

			if (con.getInsert(sql) >= 0) {

//				if (UpdateJiesb(Id,con)) {
//
//					((Dcbalancebean) getEditValues().get(0)).setId(Id);

					if (UpdateFayslb_Jiesbid(Id,con)) {
						// 更新发货表中jiesb_id
//						if(UpdateJiesyfb_Diancjsmkbid(Id,con)){
//							如果先结算了运费，再结算煤款是要和jiesyfb进行关联
							return Id;
//						}
					}
//				}
			} 

		} catch (Exception e) {

			e.printStackTrace();
		}
		return 0;
	}
	
//	private  boolean UpdateJiesyfb_Diancjsmkbid(long meikjsb_id,JDBCcon con){
////		如果先结算了运费，再结算煤款是要和jiesyfb进行关联
//		boolean Flag=false;
//		Fayjsdcz Jscz = new Fayjsdcz();
//		String sql="update jiesyfb set diancjsmkb_id="+meikjsb_id+" where id in("+
//				"select distinct dj.yunfjsb_id\n" +
//				"       from fahb f,chepb c,danjcpb dj,yunfdjb yd\n" + 
//				"       where f.id=c.fahb_id\n" + 
//				"             and c.id=dj.chepb_id\n" + 
//				"             and dj.yunfdjb_id=yd.id\n" + 
//				"             and yd.feiylbb_id="+Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
//						.getLong2())+"\n" + 
//				"             and f.jiesb_id in ("+meikjsb_id+"))";
//		
//		if(con.getUpdate(sql)>=0){
//			
//			Flag=true;
//		}
//		return Flag;
//	}

//	private boolean UpdateJiesb(long Jiesb_Id,JDBCcon con) {
//
//		String sql;
//		try {
//			sql = "update jiesb set jiesrl="
//					+ ((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_js()
//					+ "," + " jieslf="
//					+ ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_js()
//					+ "," + " jiesrcrl="
//					+ ((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_cf()
//					+ "," + " meikxxb_id="
//					+ MainGlobal.getTableId("meikxxb", "quanc", ((Dcbalancebean) getEditValues().get(0)).getMeikdw())
//					+ ","+ " meikdwmc='"
//					+ ((Dcbalancebean) getEditValues().get(0)).getMeikdw()
//					+ "',hetj="
//					+ ((Dcbalancebean) getEditValues().get(0)).getHetjg();
////					+ ",fengsjj="
////					+((Dcbalancebean) getEditValues().get(0)).getFengsjj()
////					+",jiajqdj="
////					+((Dcbalancebean) getEditValues().get(0)).getJiajqdj()
////					+",jijlx="
////					+((Dcbalancebean) getEditValues().get(0)).getJijlx();
//			
//			if(((Dcbalancebean) getEditValues().get(0)).getJieslx()==Locale.meikjs_feiylbb_id){
//				
//				sql+= ",kuidjfyf="
//					+((Dcbalancebean) getEditValues().get(0)).getKuidjfyf()
//					+ ",kuidjfzf="
//					+((Dcbalancebean) getEditValues().get(0)).getKuidjfzf();	
//			}
//			
//			if(((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid().equals("CD")){
////				保存"超吨"
//				sql+=",chaokdl="+((Dcbalancebean) getEditValues().get(0)).getChaokdl()
//					+", chaokdlx='"+((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid()+"'";
//			}else if(((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid().equals("KD")){
////				保存"亏吨"
//				sql+=",chaokdl="+(-((Dcbalancebean) getEditValues().get(0)).getChaokdl())
//					+", chaokdlx='"+((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid()+"'";
//			}
//			
//			sql+=" where id=" + Jiesb_Id;
//			if (con.getUpdate(sql) >= 0) {
//
//				return true;
//			}
//		
//		} catch (Exception e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//
//		return false;
//	}

	private boolean UpdateFayslb_Jiesbid(long JiesbId,JDBCcon con) {
		// 更新发货表jiesb_id
		boolean Flag = false;
		String sql = "update fayslb set XIAOSJSB_ID=" + JiesbId
				+ " where id in ("
				+ ((Visit) getPage().getVisit()).getString13() + ")";
		if (con.getUpdate(sql) >= 0) {

			Flag = true;
		} 
		return Flag;
	}

//	private boolean UpdateDanjcpb_Jiesyfbid(long JiesyfbId, JDBCcon con) {
//		// 单据车皮表yunfjsb_id
//		// 如果在结算选择页面中选择“需要核对货票”时说明，yunfdjb和danjcpb中已经有记录
//		// 如果在结算选择页面中选择“不需要核对货票”时说明，yunfdjb和danjcpb中都没有记录，要插入新纪录
//		boolean Flag = false;
//		boolean Hedbz = false; // true 已核对、false 未核对
//		Fayjsdcz Jscz = new Fayjsdcz();
//		String yunsdw_contion="";
//		
////		if(((Visit) getPage().getVisit()).getLong9()>-1){
//			if(((Visit) getPage().getVisit()).getLong9()>-1){
//			yunsdw_contion=" and c.yunsdwb_id="+((Visit) getPage().getVisit()).getLong9();
//		}
//		
//		try {
////			StringBuffer sbcpid = new StringBuffer();
////			String sql = "select c.id from fahb f,chepb c		\n"
////					+ " where f.id=c.fahb_id and f.id in ("
////					+ ((Visit) getPage().getVisit()).getString13() + ")	\n"
////					+ yunsdw_contion;
////
////			ResultSet rs = con.getResultSet(sql);
////			while (rs.next()) {
////
////				sbcpid.append(rs.getLong("id")).append(",");
////			}
////			sbcpid.deleteCharAt(sbcpid.length() - 1);
//			String sql ="";
//			ResultSet rs=null;
//			// 判断yunfdjb和danjcpb中是否存在记录，如果存在Hedbz=true直接更新，如果不存在Hedbz=false;先将数据插入二表，再更新状态
//			sql = "select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
//					+ " and danjcpb.chepb_id in ("
//					+ sbcpid.toString()
//					+ ") and yunfdjb.feiylbb_id="
//					+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
//							.getLong2()) + "";
//
//			rs = con.getResultSet(sql);
//			if (rs.next()) {
//
//				Hedbz = true;
//			}
//
//			if (!Hedbz) {
//
//				String Chep[] = sbcpid.toString().split(",");
//				StringBuffer sb = new StringBuffer("begin		\n");
//				String Yunfdjb_id = "0";
//
//				for (int i = 0; i < Chep.length; i++) {
//
//					Yunfdjb_id = MainGlobal.getNewID(((Visit) getPage()
//							.getVisit()).getLong1());
//					sb
//							.append(
//									"insert into yunfdjb(id, danjbh, feiyb_id, biaoz, zongje, caozy, caozsj, beiz, feiylbb_id, ches)	\n")
//							.append(" values		\n")
//							.append(
//									" ("
//											+ Yunfdjb_id
//											+ ", '', 0, 0, 0, '"
//											+ ((Visit) getPage().getVisit())
//													.getRenymc()
//											+ "', sysdate, ")
//							.append(
//									"'不需核对货票或汽运费结算', "
//											+ Jscz
//													.Feiylbb_transition(((Visit) getPage()
//															.getVisit())
//															.getLong2())
//											+ ", 1);	\n");
//
//					sb
//							.append(
//									"insert into danjcpb(yunfdjb_id, chepb_id, yunfjsb_id, yansbhb_id, jifzl, id)		\n")
//							.append(" values		\n").append(
//									"("
//											+ Yunfdjb_id
//											+ ", "
//											+ Chep[i]
//											+ ", 0, 0, 0, getnewid("
//											+ ((Visit) getPage().getVisit())
//													.getLong1() + "));	\n");
//				}
//				sb.append("end;");
//				con.getInsert(sb.toString());
//			}
//			rs.close();
//
//			sql = "update danjcpb set yunfjsb_id="
//					+ JiesyfbId
//					+ " where chepb_id in ("
//					+ sbcpid.toString()
//					+ ") 	\n"
//					+ " and danjcpb.yunfdjb_id in	\n"
//					+ " (select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id	\n"
//					+ "	and danjcpb.chepb_id in ("
//					+ sbcpid.toString()
//					+ ") and yunfdjb.feiylbb_id="
//					+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
//							.getLong2()) + ") ";
//			if (con.getUpdate(sql) > 0) {
//
//				Flag = true;
//			} 
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//
//		return Flag;
//	}

	private long SaveDiancjsyfb(long Meikid,JDBCcon con) {
		// 存储运费表
		String sql = "";
		long Id = 0;
		try {

			if (((Dcbalancebean) getEditValues().get(0)).getYunfjsb_id() > 0) {

				Id = ((Dcbalancebean) getEditValues().get(0)).getYunfjsb_id();
			} else {

				Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
						.getVisit()).getLong1()));
			}

			sql = " insert into diancjsyfb  (id,diancxxb_id, bianm, gongysb_id, gongysmc, meikxxb_id, "
					+ " /*meikdwmc,*/faz, fahksrq, fahjzrq, meiz, daibch, "
					+ " yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, "
					+ " fukfs, duifdd, ches, gongfsl, yanssl, jiessl, yingk, guohl,/* yuns, koud,*/  "
					+ " guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, "
					+ " shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, LIUCGZbID, beiz, "
					+ " diancjsmkb_id, RANLBMJBR, RANLBMJBRQ, kuidjfyf,kuidjfzf,yewlxb_id)"
					+ " values("
					+ Id
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong1()
					+ ", '"
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesbh()
					+ "', "
					+ MainGlobal
							.getTableId("gongysb", "quanc",
									((Dcbalancebean) getEditValues().get(0))
											.getFahdw())
					+ ", '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFahdw()
					+ "',"
					+ MainGlobal
							.getTableId("meikxxb", "quanc",
									((Dcbalancebean) getEditValues().get(0))
											.getMeikdw())
											
					+ ",'"
//					+ ((Dcbalancebean) getEditValues().get(0)).getMeikdw()
//					+ "','"
//					+ ((Dcbalancebean) getEditValues().get(0)).getYunsfsb_id()
//					+ ","
//					+ ((Dcbalancebean) getEditValues().get(0)).getYunju()
//					+ ","
//					+ ((Dcbalancebean) getEditValues().get(0)).getYingd()
//					+ ","
//					+ ((Dcbalancebean) getEditValues().get(0)).getKuid()
//					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getFaz()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean) getEditValues().get(0)).getPinz()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDaibcc()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getYuanshr()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getXianshr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansksrq())
					+ "','yyyy-MM-dd'), to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansjzrq())
					+ "','yyyy-MM-dd'), '"
					+ ((Dcbalancebean) getEditValues().get(0)).getYansbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getShoukdw()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDuifdd()
					+ "', "
					+ ((Dcbalancebean) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getGongfsl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYanssl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfjsl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYingksl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJingz()
					+ ","
					//+ ((Dcbalancebean) getEditValues().get(0)).getYuns()
				//	+ ","
//					+ ((Dcbalancebean) getEditValues().get(0)).getKoud_js()
//					+ ","
//					+ ((Dcbalancebean) getEditValues().get(0)).getJiesslcy()
//					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getTielyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getTielzf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuangqyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuangqzf()
					+ ","
					+ (double) Math
							.round((((Dcbalancebean) getEditValues().get(0))
									.getTielzf() + ((Dcbalancebean) getEditValues()
									.get(0)).getKuangqzf()) * 100)
					/ 100
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getShulzjbz()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getBukyqyzf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYunzfhj()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getBuhsyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfsk()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfsl()
					+ ", 0, "
//					+ ((Visit) getPage().getVisit()).getLong2()
					+((Dcbalancebean) getEditValues().get(0)).getJieslx()
					+ ", to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getJiesrq())
					+ "','yyyy-MM-dd'), null,"
					+ ((Dcbalancebean) getEditValues().get(0)).getHetb_id()
					+ ", 0, 0, '"
					+ ((Dcbalancebean) getEditValues().get(0)).getBeiz()
					+ "',"
					+ GetMeikjsb_id(Meikid)
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getRanlbmjbrq()) + "','yyyy-MM-dd')" 
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuidjfyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuidjfzf()
					+","
					+((Visit)this.getPage().getVisit()).getLong10()//业务类型表id
					+ ")";

			if (con.getInsert(sql) >= 0) {

				((Dcbalancebean) getEditValues().get(0)).setYid(Id);
//				if (UpdateDanjcpb_Jiesyfbid(Id, con)) {

					return Id;
//				}
			} 

		} catch (Exception e) {

			e.printStackTrace();
		} 
		return 0;
	}
	
	private long GetMeikjsb_id(long meikjsb_id) throws SQLException{
//		结算运费时要和结算表进行绑定，即更新jiesyfb.diancjsmkb_id为jiesb.id
		long Jiesb_id=meikjsb_id;
		if(meikjsb_id>0){
//			如果是两票结算，meikjsb_id>0
			
		}else{
			
			JDBCcon con=new JDBCcon();
			String sql="select XIAOSJSB_ID from fayslb where id in ("+((Visit) getPage().getVisit()).getString13()+")";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Jiesb_id=rs.getLong("XIAOSJSB_ID");
			}
			rs.close();
			con.Close();
		}
		
		return Jiesb_id;
	}

//	private boolean UpdateJieszfb(long id, JDBCcon con) {
//		// TODO 自动生成方法存根
//		// 更新杂费表中jiesyfb_id字段
//		// 当保存jieszf时把该次操作的session存入表中
//		// 保存jieszf表时更新session相同的记录
//		// JDBCcon con=new JDBCcon();
//		String sql = "update jieszfb set jiesyfb_id=" + id
//				+ " where serversession='"
//				+ ((Visit) getPage().getVisit()).getSession() + "'";
//		if (con.getUpdate(sql) >= 0) {
//
//			return true;
//		}
//		// con.Close();
//		return false;
//	}

	private String SaveJszbsjb(long Mkid, String mingc, String hetbz,
			double gongf, double changf, double jies, double yingk,
			double zhejbz, double zhejje, int zhuangt) {
		// 保存结算单中指标数据
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;
		try {

			Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getLong1()));

			sql = " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id) "
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
					+ ","
					+ MainGlobal.getTableId("yansbhb", "bianm",
							((Visit) getPage().getVisit()).getString4())
					+ ");	\n";

		} catch (Exception e) {

			e.printStackTrace();
		} 
		return sql;
	}

	private boolean Save() {
		// 重要说明：所有结算表未填写 合同id、流程状态id、流程跟踪id、矿方结算id
		String msg = "";
		long Mkid = 0;// 煤款id
		long Yfid = 0;// 运费id
		boolean Flag = false;
		JDBCcon con =new JDBCcon();
		con.setAutoCommit(false);
		try {

			if (Fayjsdcz.CheckHetshzt(((Dcbalancebean) getEditValues().get(0))
					.getHetb_id())) {
				// 判断合同的审核状态

				if (getEditValues() != null
						&& !getEditValues().isEmpty()
						&& !((Dcbalancebean) getEditValues().get(0))
								.getJiesbh().equals("")) {
					if (Fayjsdcz.checkbh(
							((Dcbalancebean) getEditValues().get(0))
									.getJiesbh(), 0, 0)) {

						if (((Visit) getPage().getVisit()).getLong2() == Locale.liangpjs_feiylbb_id) {// 两票结算

							if (((Dcbalancebean) getEditValues().get(0))
									.getId() == 0) {
								// 电厂煤矿表
								Mkid = SaveDiancjsmkb(con);

								if (Mkid > 0) {
									// 操作电厂结算运费表
									Yfid = SaveDiancjsyfb(Mkid,con);

									if (Yfid > 0) {
										// 要和基础信息指标模块定一下zhibb内容

										if (SaveZhib(Mkid,con)) {

											Flag = true;
										}
									}
								}
							}

						} else if (((Visit) getPage().getVisit()).getLong2() == 2) {
							// 单结算煤款
							if (((Dcbalancebean) getEditValues().get(0))
									.getId() == 0) {
								// 保存煤款表
								Mkid = SaveDiancjsmkb(con);

								if (Mkid > 0) {

									if (this.SaveZhib(Mkid,con)) {

										Flag = true;
									}
								}
							}
						} else {
							// 单结算运费
							if (checkXitszDjyf()) {
								// 读系统信息表的设置，看公司系统能不能单结算运费
								Yfid = SaveDiancjsyfb(0,con);

								if (Yfid > 0) {

									Flag = true;
								}

							} else {

								msg = "请选择与该运费对应的煤款结算单";
							}
						}
					} else {

						msg = "结算单编号重复";
					}
				} else {

					msg = "不能保存空结算单";
				}

			} else {

				msg = "合同未审核不能保存！";
			}

			if (Flag) {

				setMsg("结算操作成功！");
				con.commit();
//				Chengbcl.CountCb_js(Mkid, Yfid);

			} else {

				setMsg(msg + " 结算操作失败！");
				con.rollBack();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return Flag;
	}

	private boolean SaveZhib(long Mkid,JDBCcon con) {
		// 保存结算单指标
		boolean Flag = false;

		StringBuffer sbsql = new StringBuffer(" begin \n");

		sbsql.append(this.SaveJszbsjb(Mkid, Locale.jiessl_zhibb,
				((Dcbalancebean) getEditValues().get(0)).getHetsl(),
				((Dcbalancebean) getEditValues().get(0)).getGongfsl(),
				((Dcbalancebean) getEditValues().get(0)).getYanssl(),
				((Dcbalancebean) getEditValues().get(0)).getJiessl(),
				((Dcbalancebean) getEditValues().get(0)).getYingksl(),
				((Dcbalancebean) getEditValues().get(0)).getShulzjbz(),
				((Dcbalancebean) getEditValues().get(0)).getShulzjje(), 1));

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_ht()
				.equals("")) {

			sbsql
					.append(this
							.SaveJszbsjb(
									Mkid,
									Locale.Qnetar_zhibb,
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_ht(),
													"-", 0),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_kf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_cf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_js(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQnetar_yk(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQnetar_zdj(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQnetar_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStd_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Std_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAd_ht().equals("")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Ad_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_ht().equals(
				"")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Vdaf_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getMt_ht().equals("")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Mt_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQgrad_ht().equals(
				"")) {

			sbsql
					.append(this
							.SaveJszbsjb(
									Mkid,
									Locale.Qgrad_zhibb,
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_ht(),
													"-", 0),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_kf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_cf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_js(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQgrad_yk(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQgrad_zdj(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQgrad_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_ht().equals(
				"")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Qbad_zhibb, MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_ht(), "-", 0), MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_kf(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()), MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_cf(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()), MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_js(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
					((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getHad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Had_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStad_ht().equals(
				"")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Stad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getMad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Mad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAar_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Aar_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Aad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getVad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Vad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getT2_ht().equals("")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.T2_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getYunju_ht().equals(
				"")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Yunju_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_zje(), 1));
		}
		
		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStar_ht().equals(
		"")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Star_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_js(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_zje(), 1));
		}

		sbsql.append(" end; ");

		if (con.getInsert(sbsql.toString()) >= 0) {

			Flag = true;
		} 
		return Flag;
	}

	private boolean checkXitszDjyf() {
		// TODO 自动生成方法存根
		// 检查系统设置中的"可单独结算运费"设置
		JDBCcon con = new JDBCcon();
		try {
			String zhi = "";

			String sql = "select zhi from xitxxb where mingc='可单独结算运费'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				zhi = rs.getString("zhi");
			}

			if (zhi.trim().equals("是")) {

				return true;
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return false;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {

		_QuedChick = true;
	}

	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {

		_ReturnChick = true;
	}

	private boolean _KuangqzfChick = false;

	public void KuangqzfButton(IRequestCycle cycle) {

		_KuangqzfChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_QuedChick) {
			_QuedChick = false;
			Submit();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Qux(cycle);
		}
		if (_KuangqzfChick) {
			_KuangqzfChick = false;
			Kuangqzf(cycle);
		}
	}

	private void Kuangqzf(IRequestCycle cycle) {

		((Visit) getPage().getVisit()).getLong1(); // 电厂信息表id
		((Visit) getPage().getVisit()).getLong3(); // 供应商表id
		((Visit) getPage().getVisit()).getLong4(); // 煤矿信息表id
		((Visit) getPage().getVisit()).getLong5(); // 发站id
		((Visit) getPage().getVisit()).getLong6(); // 到站id
		((Visit) getPage().getVisit()).getDouble1(); // 总票重
		((Visit) getPage().getVisit())
				.setLong7(((Dcbalancebean) getEditValues().get(0)).getYid()); // 结算运费表id
		((Visit) getPage().getVisit()).setSession(cycle.getRequestContext()
				.getSession());
		((Visit) getPage().getVisit()).setString11("DCBalance");
		cycle.activate("Kuangqzf");
	}

	private void Qux(IRequestCycle cycle) {
		// TODO 自动生成方法存根

		cycle.activate("Fayjsxz");
	}

	private void Submit() {
		// TODO 自动生成方法存根

		if (((Dcbalancebean) getEditValues().get(0)).getId() == 0
				&& ((Dcbalancebean) getEditValues().get(0)).getYid() == 0) {// 没保存先做保存

			if (Save() && this.getProperId(this.getILiucmcModel(), getLiucmc()) > -1) {
				// 流程方法
				if (((Visit) getPage().getVisit()).getLong2() == 1) {// 两票结算

					Liuc.tij("jiesb", ((Dcbalancebean) getEditValues().get(0))
							.getId(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getProperId(this.getILiucmcModel(), getLiucmc()));
					Liuc.tij("jiesyfb",
							((Dcbalancebean) getEditValues().get(0)).getYid(),
							((Visit) getPage().getVisit()).getRenyID(), "",
							getProperId(this.getILiucmcModel(), getLiucmc()));

					Fayjsdcz.SubmitGsDiancjsmkb(((Dcbalancebean) getEditValues()
							.get(0)).getId());
					Fayjsdcz.SubmitGsDiancjsyfb(((Dcbalancebean) getEditValues()
							.get(0)).getYid());

				} else if (((Visit) getPage().getVisit()).getLong2() == 0) {// 煤款结算

					Liuc.tij("jiesb", ((Dcbalancebean) getEditValues().get(0))
							.getId(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getProperId(this.getILiucmcModel(), getLiucmc()));

					Fayjsdcz.SubmitGsDiancjsmkb(((Dcbalancebean) getEditValues()
							.get(0)).getId());

				} else if (((Visit) getPage().getVisit()).getLong2() == 2) {// 运费结算

					Liuc.tij("jiesyfb",
							((Dcbalancebean) getEditValues().get(0)).getYid(),
							((Visit) getPage().getVisit()).getRenyID(), "",
							getProperId(this.getILiucmcModel(), getLiucmc()));

					Fayjsdcz.SubmitGsDiancjsyfb(((Dcbalancebean) getEditValues()
							.get(0)).getYid());
				}

			} else if (getProperId(this.getILiucmcModel(), getLiucmc()) == -1) {

				setMsg("请选择流程名称！");
			}

		} else {// 已经保存

			if (this.getProperId(this.getILiucmcModel(), getLiucmc()) > -1) {
				// 流程方法
				if (((Visit) getPage().getVisit()).getLong2() == 1) {// 两票结算

					Liuc.tij("jiesb", ((Dcbalancebean) getEditValues().get(0))
							.getId(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getProperId(this.getILiucmcModel(), getLiucmc()));
					Liuc.tij("jiesyfb",
							((Dcbalancebean) getEditValues().get(0)).getYid(),
							((Visit) getPage().getVisit()).getRenyID(), "",
							getProperId(this.getILiucmcModel(), getLiucmc()));

				} else if (((Visit) getPage().getVisit()).getLong2() == 0) {// 煤款结算

					Liuc.tij("jiesb", ((Dcbalancebean) getEditValues().get(0))
							.getId(), ((Visit) getPage().getVisit())
							.getRenyID(), "", getProperId(this.getILiucmcModel(), getLiucmc()));

				} else if (((Visit) getPage().getVisit()).getLong2() == 2) {// 运费结算

					Liuc.tij("jiesyfb",
							((Dcbalancebean) getEditValues().get(0)).getYid(),
							((Visit) getPage().getVisit()).getRenyID(), "",
							getProperId(this.getILiucmcModel(), getLiucmc()));
				}

			} else if (getProperId(this.getILiucmcModel(), getLiucmc()) == -1) {

				setMsg("请选择流程名称！");
			}
		}
	}

	private Dcbalancebean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public List getJieszbValues() {
		return ((Visit) getPage().getVisit()).getList2();
	}

	public void setJieszbValues(List editList) {
		((Visit) getPage().getVisit()).setList2(editList);
	}

	public Dcbalancebean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Dcbalancebean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		Visit visit = new Visit();
		List _editvalues = new ArrayList();
		List _Jieszbvalues = new ArrayList();

		if (getEditValues() != null) {

			getEditValues().clear();
		}
		if (getJieszbValues() != null) {

			getJieszbValues().clear();
		}

		JDBCcon con = new JDBCcon();
		try {

			long mid = 0;
			long myid = 0;
			String mtianzdw = MainGlobal.getTableCol("diancxxb", "quanc", "id",
					String.valueOf(((Visit) getPage().getVisit()).getLong1()));
			String mjiesbh = "";
			String mfahdw = "";
			String mmeikdw="";
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
			String mhetsl = "";// 合同数量
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
			String mMjtokcalxsclfs=""; 	//兆焦转大卡小数处理方式

			String mQnetar_ht = ""; // 合同热量
			double mQnetar_kf = 0; // 供方热量
			double mQnetar_cf = 0; // 厂方热量
			double mQnetar_js = 0; // 厂方结算
			double mQnetar_yk = 0; // 厂方盈亏
			double mQnetar_zdj = 0; // 折单价
			double mQnetar_zje = 0; // 这金额

			String mStd_ht = ""; // 合同硫分
			double mStd_kf = 0; // 供方热量
			double mStd_cf = 0; // 厂方热量
			double mStd_js = 0; // 结算热量
			double mStd_yk = 0; // 厂方盈亏
			double mStd_zdj = 0; // 折单价
			double mStd_zje = 0; // 这金额

			String mAd_ht = ""; // 合同硫分
			double mAd_kf = 0; // 供方热量
			double mAd_cf = 0; // 厂方热量
			double mAd_js = 0; // 结算热量
			double mAd_yk = 0; // 厂方盈亏
			double mAd_zdj = 0; // 折单价
			double mAd_zje = 0; // 这金额

			String mVdaf_ht = ""; // 合同硫分
			double mVdaf_kf = 0; // 供方热量
			double mVdaf_cf = 0; // 厂方热量
			double mVdaf_js = 0; // 结算热量
			double mVdaf_yk = 0; // 厂方盈亏
			double mVdaf_zdj = 0; // 折单价
			double mVdaf_zje = 0; // 这金额

			String mMt_ht = ""; // 合同硫分
			double mMt_kf = 0; // 供方热量
			double mMt_cf = 0; // 厂方热量
			double mMt_js = 0; // 结算热量
			double mMt_yk = 0; // 厂方盈亏
			double mMt_zdj = 0; // 折单价
			double mMt_zje = 0; // 这金额

			String mQgrad_ht = ""; // 合同硫分
			double mQgrad_kf = 0; // 供方热量
			double mQgrad_cf = 0; // 厂方热量
			double mQgrad_js = 0; // 结算热量
			double mQgrad_yk = 0; // 厂方盈亏
			double mQgrad_zdj = 0; // 折单价
			double mQgrad_zje = 0; // 这金额

			String mQbad_ht = ""; // 合同硫分
			double mQbad_kf = 0; // 供方热量
			double mQbad_cf = 0; // 厂方热量
			double mQbad_js = 0; // 结算热量
			double mQbad_yk = 0; // 厂方盈亏
			double mQbad_zdj = 0; // 折单价
			double mQbad_zje = 0; // 这金额

			String mHad_ht = ""; // 合同硫分
			double mHad_kf = 0; // 供方热量
			double mHad_cf = 0; // 厂方热量
			double mHad_js = 0; // 结算热量
			double mHad_yk = 0; // 厂方盈亏
			double mHad_zdj = 0; // 折单价
			double mHad_zje = 0; // 这金额

			String mStad_ht = ""; // 合同硫分
			double mStad_kf = 0; // 供方热量
			double mStad_cf = 0; // 厂方热量
			double mStad_js = 0; // 结算热量
			double mStad_yk = 0; // 厂方盈亏
			double mStad_zdj = 0; // 折单价
			double mStad_zje = 0; // 这金额
			
			String mStar_ht = ""; // 合同硫分
			double mStar_kf = 0; // 供方热量
			double mStar_cf = 0; // 厂方热量
			double mStar_js = 0; // 结算热量
			double mStar_yk = 0; // 厂方盈亏
			double mStar_zdj = 0; // 折单价
			double mStar_zje = 0; // 这金额

			String mMad_ht = ""; // 合同硫分
			double mMad_kf = 0; // 供方热量
			double mMad_cf = 0; // 厂方热量
			double mMad_js = 0; // 结算热量
			double mMad_yk = 0; // 厂方盈亏
			double mMad_zdj = 0; // 折单价
			double mMad_zje = 0; // 这金额

			String mAar_ht = ""; // 合同硫分
			double mAar_kf = 0; // 供方热量
			double mAar_cf = 0; // 厂方热量
			double mAar_js = 0; // 结算热量
			double mAar_yk = 0; // 厂方盈亏
			double mAar_zdj = 0; // 折单价
			double mAar_zje = 0; // 这金额

			String mAad_ht = ""; // 合同硫分
			double mAad_kf = 0; // 供方热量
			double mAad_cf = 0; // 厂方热量
			double mAad_js = 0; // 结算热量
			double mAad_yk = 0; // 厂方盈亏
			double mAad_zdj = 0; // 折单价
			double mAad_zje = 0; // 这金额

			String mVad_ht = ""; // 合同硫分
			double mVad_kf = 0; // 供方热量
			double mVad_cf = 0; // 厂方热量
			double mVad_js = 0; // 结算热量
			double mVad_yk = 0; // 厂方盈亏
			double mVad_zdj = 0; // 折单价
			double mVad_zje = 0; // 这金额

			String mT2_ht = ""; // 合同硫分
			double mT2_kf = 0; // 供方热量
			double mT2_cf = 0; // 厂方热量
			double mT2_js = 0; // 结算热量
			double mT2_yk = 0; // 厂方盈亏
			double mT2_zdj = 0; // 折单价
			double mT2_zje = 0; // 这金额

			String mYunju_ht = ""; // 合同运距
			double mYunju_kf = 0; // 供方运距
			double mYunju_cf = 0; // 厂方运距
			double mYunju_js = 0; // 结算运距
			double mYunju_yk = 0; // 厂方盈亏
			double mYunju_zdj = 0; // 折单价
			double mYunju_zje = 0; // 这金额

			long mhetb_id = 0;
			double mjiessl = 0;
			double myunfjsl = 0;
			double mkoud_js = 0; // 结算时的扣吨
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.17;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mtielzf = 0;
			double mkuangqyf = 0;
			double mkuangqzf = 0;
			double mkuangqsk = 0;
			double mkuangqjk = 0;
//			double mjiesslcy = 0;
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
			long mjieslx = ((Visit) getPage().getVisit()).getLong2();
			double myuns = 0;
			String myunsfs = "";
//			long myunsfsb_id = 0;
			String mdiancjsbs = "";
			mdiancjsbs = getDiancjsbs(((Visit) getPage().getVisit()).getLong1());
			String mstrJieszb = "";
			String mstrHejdxh= "";
			String mErroMessage = "";
			double myingd = 0;
			double mkuid = 0;
			double myunju = 0; // 运距

			// 进行单批次结算时，要将每一个批次的结算情况保存起来，存入danpcjsmxb中，此时就产生了id
			// 结算时要判断有无这个id，如果有就一定要用这个id
			long mMeikjsb_id = 0;
			long mYunfjsb_id = 0;
			long mJihkjb_id = 0;
			
			double mfengsjj=0;	//分公司加价
			double mjiajqdj=0;	//加价前单价
			int mjijlx=0;		//基价类型（0、含税，1、不含税）
			
			double mkuidjfyf_je=0;	//亏吨拒付运费金额
			double mkuidjfzf_je=0;	//亏吨拒付杂费金额
			double mchaokdl=0;		//超/亏吨量
			String mChaodorKuid="";	//超/亏吨标识（CD,KD,""）

			// mkuangqyf=((Visit) getPage().getVisit()).getDouble2(); //矿区运费
			// mkuangqzf=((Visit) getPage().getVisit()).getDouble3(); //矿区杂费
			// mkuangqsk=((Visit) getPage().getVisit()).getDouble4(); //矿区税款
			// mkuangqjk=((Visit) getPage().getVisit()).getDouble5(); //矿区价款

			// ((Visit) getPage().getVisit()).getLong1() //电厂信息表id
			// ((Visit) getPage().getVisit()).getString1() //列Id
			// ((Visit) getPage().getVisit()).getLong2() //结算类型
			// ((Visit) getPage().getVisit()).getString2() //是否需要结算指标调整（是、否）
			// ((Visit) getPage().getVisit()).getString4() //验收编号
			// ((Visit) getPage().getVisit()).getLong3() //发货单位表id
			// ((Visit) getPage().getVisit()).getLong8() //合同表id
			// ((Visit) getPage().getVisit()).getLong9() //运输单位id			

			Balances bls = new Balances();
			Balances_variable bsv = new Balances_variable(); // Balances变量
			// bsv.setKuangqyf(mkuangqyf);
			// bsv.setKuangqzf(mkuangqzf);
			// bsv.setKuangqsk(mkuangqsk);
			// bsv.setKuangqjk(mkuangqjk);
//			bls.setBsv(bsv);
               bls=(Balances)((Visit) getPage().getVisit()).getObject1();
               bsv=bls.getBsv();
//			bls.getBalanceData(((Visit) getPage().getVisit()).getString1(),
//					((Visit) getPage().getVisit()).getLong1(),
//					((Visit) getPage().getVisit()).getLong2(),
//					((Visit) getPage().getVisit()).getLong3(),
//					((Visit) getPage().getVisit()).getLong8(),
//					((Visit) getPage().getVisit()).getString2(),
//					((Visit) getPage().getVisit()).getString4(), 
//					Double.parseDouble(((Visit) getPage().getVisit()).getString7()),
//					((Visit) getPage().getVisit()).getLong9(),
//					Double.parseDouble(((Visit) getPage().getVisit()).getString12())
//					);

			bsv = bls.getBsv();
			mxianshr=bsv.getXianshr();//现收货人
			mfapbh=bsv.getFapbh();//发票编号
			mErroMessage = bsv.getErroInfo();
			mjiesbh = bsv.getJiesbh();
			mfahdw = bsv.getFahdw();
			mmeikdw = bsv.getMeikdw();
			mfaz = bsv.getFaz();
			mshoukdw = bsv.getShoukdw();
			mfahksrq = bsv.getFahksrq();
			mfahjzrq = bsv.getFahjzrq();
			mfahrq = bsv.getFahrq();
			myansksrq = bsv.getYansksrq();
			myansjzrq = bsv.getYansjsrq();
			mkaihyh = bsv.getKaihyh();
			mpinz = bsv.getRanlpz();
			mzhangh = bsv.getZhangH();
			mhetsl = String.valueOf(bsv.getHetml());
			myuanshr=bsv.getYuanshr();
			mgongfsl = bsv.getGongfsl();
			mches = bsv.getChes();
			myansbh = bsv.getYansbh();
			mshulzjbz = bsv.getHansmj();
			myanssl = bsv.getYanssl();
			myingksl = bsv.getYingksl();
			mshulzjje = bsv.getShul_zje();
			mhetb_id = bsv.getHetb_Id();
			mdaibcc = bsv.getDaibcc();
			mkoud_js = bsv.getKoud_js();
//			myunsfsb_id = bsv.getYunsfsb_id();
			myunju = bsv.getYunju_js();
//			加价和不含税单价处理
			mfengsjj=bsv.getFengsjj();
			mjiajqdj=bsv.getJiajqdj();
			mjijlx=bsv.getJijlx();
			mMjtokcalxsclfs=bsv.getMj_to_kcal_xsclfs();//兆焦转大卡小数处理方式

			// 指标_Begin
			mQnetar_ht = bsv.getQnetar_ht();
			mQnetar_kf = bsv.getQnetar_kf();
			mQnetar_cf = bsv.getQnetar_cf();
			mQnetar_js = bsv.getQnetar_js();
			mQnetar_yk = bsv.getQnetar_yk();
			mQnetar_zdj = bsv.getQnetar_zdj();
			mQnetar_zje = bsv.getQnetar_zje();

			mAd_ht = bsv.getAd_ht();
			mAd_kf = bsv.getAd_kf();
			mAd_cf = bsv.getAd_cf();
			mAd_js = bsv.getAd_js();
			mAd_yk = bsv.getAd_yk();
			mAd_zdj = bsv.getAd_zdj();
			mAd_zje = bsv.getAd_zje();

			mStd_ht = bsv.getStd_ht();
			mStd_kf = bsv.getStd_kf();
			mStd_cf = bsv.getStd_cf();
			mStd_js = bsv.getStd_js();
			mStd_yk = bsv.getStd_yk();
			mStd_zdj = bsv.getStd_zdj();
			mStd_zje = bsv.getStd_zje();

			mVdaf_ht = bsv.getVdaf_ht();
			mVdaf_kf = bsv.getVdaf_kf();
			mVdaf_cf = bsv.getVdaf_cf();
			mVdaf_js = bsv.getVdaf_js();
			mVdaf_yk = bsv.getVdaf_yk();
			mVdaf_zdj = bsv.getVdaf_zdj();
			mVdaf_zje = bsv.getVdaf_zje();

			mMt_ht = bsv.getMt_ht();
			mMt_kf = bsv.getMt_kf();
			mMt_cf = bsv.getMt_cf();
			mMt_js = bsv.getMt_js();
			mMt_yk = bsv.getMt_yk();
			mMt_zdj = bsv.getMt_zdj();
			mMt_zje = bsv.getMt_zje();

			mQgrad_ht = bsv.getQgrad_ht();
			mQgrad_kf = bsv.getQgrad_kf();
			mQgrad_cf = bsv.getQgrad_cf();
			mQgrad_js = bsv.getQgrad_js();
			mQgrad_yk = bsv.getQgrad_yk();
			mQgrad_zdj = bsv.getQgrad_zdj();
			mQgrad_zje = bsv.getQgrad_zje();

			mQbad_ht = bsv.getQbad_ht();
			mQbad_kf = bsv.getQbad_kf();
			mQbad_cf = bsv.getQbad_cf();
			mQbad_js = bsv.getQbad_js();
			mQbad_yk = bsv.getQbad_yk();
			mQbad_zdj = bsv.getQbad_zdj();
			mQbad_zje = bsv.getQbad_zje();

			mHad_ht = bsv.getHad_ht();
			mHad_kf = bsv.getHad_kf();
			mHad_cf = bsv.getHad_cf();
			mHad_js = bsv.getHad_js();
			mHad_yk = bsv.getHad_yk();
			mHad_zdj = bsv.getHad_zdj();
			mHad_zje = bsv.getHad_zje();

			mStad_ht = bsv.getStad_ht();
			mStad_kf = bsv.getStad_kf();
			mStad_cf = bsv.getStad_cf();
			mStad_js = bsv.getStad_js();
			mStad_yk = bsv.getStad_yk();
			mStad_zdj = bsv.getStad_zdj();
			mStad_zje = bsv.getStad_zje();
			
			mStar_ht = bsv.getStar_ht();
			mStar_kf = bsv.getStar_kf();
			mStar_cf = bsv.getStar_cf();
			mStar_js = bsv.getStar_js();
			mStar_yk = bsv.getStar_yk();
			mStar_zdj = bsv.getStar_zdj();
			mStar_zje = bsv.getStar_zje();

			mMad_ht = bsv.getMad_ht();
			mMad_kf = bsv.getMad_kf();
			mMad_cf = bsv.getMad_cf();
			mMad_js = bsv.getMad_js();
			mMad_yk = bsv.getMad_yk();
			mMad_zdj = bsv.getMad_zdj();
			mMad_zje = bsv.getMad_zje();

			mAar_ht = bsv.getAar_ht();
			mAar_kf = bsv.getAar_kf();
			mAar_cf = bsv.getAar_cf();
			mAar_js = bsv.getAar_js();
			mAar_yk = bsv.getAar_yk();
			mAar_zdj = bsv.getAar_zdj();
			mAar_zje = bsv.getAar_zje();

			mAad_ht = bsv.getAad_ht();
			mAad_kf = bsv.getAad_kf();
			mAad_cf = bsv.getAad_cf();
			mAad_js = bsv.getAad_js();
			mAad_yk = bsv.getAad_yk();
			mAad_zdj = bsv.getAad_zdj();
			mAad_zje = bsv.getAad_zje();

			mVad_ht = bsv.getVad_ht();
			mVad_kf = bsv.getVad_kf();
			mVad_cf = bsv.getVad_cf();
			mVad_js = bsv.getVad_js();
			mVad_yk = bsv.getVad_yk();
			mVad_zdj = bsv.getVad_zdj();
			mVad_zje = bsv.getVad_zje();

			mT2_ht = bsv.getT2_ht();
			mT2_kf = bsv.getT2_kf();
			mT2_cf = bsv.getT2_cf();
			mT2_js = bsv.getT2_js();
			mT2_yk = bsv.getT2_yk();
			mT2_zdj = bsv.getT2_zdj();
			mT2_zje = bsv.getT2_zje();

			mYunju_ht = bsv.getYunju_ht(); // 合同运距
			mYunju_kf = bsv.getYunju_kf(); // 供方运距
			mYunju_cf = bsv.getYunju_cf(); // 厂方运距
			mYunju_js = bsv.getYunju_js(); // 结算运距
			mYunju_yk = bsv.getYunju_yk(); // 运距盈亏
			mYunju_zdj = bsv.getYunju_zdj(); // 运距折单价
			mYunju_zje = bsv.getYunju_zje(); // 折金额

			// 指标_End

			mjiessl = bsv.getJiessl();
			myunfjsl = bsv.getYunfjsl();
			mbuhsdj = bsv.getBuhsmj();
			mjiakje = bsv.getJine();
			mjiakhj = bsv.getJiakhj();
			mjiaksl = bsv.getMeiksl();
			mjiaksk = bsv.getJiaksk();
			mjiasje = bsv.getJiashj();
			mtielyf = bsv.getTielyf();
			mtielzf = bsv.getTielzf();
//			矿区运费、杂费
			mkuangqyf=bsv.getKuangqyf();
			mkuangqzf=bsv.getKuangqzf();
			
//			mjiesslcy = bsv.getJiesslcy();
			mbuhsyf = bsv.getBuhsyf();
			myunfsl = bsv.getYunfsl();
			myunfsk = bsv.getYunfsk();
			myunzfhj = bsv.getYunzfhj();
			mhej = bsv.getHej();
			Money mn = new Money();
			mdaxhj = mn.NumToRMBStr(mhej);
			mmeikhjdx = mn.NumToRMBStr(bsv.getJiashj());
			myunzfhjdx = mn.NumToRMBStr(bsv.getYunzfhj());
			mbeiz = bsv.getBeiz();
			mjingz = bsv.getJingz();
			mhetjg = bsv.getHetmdj();
			myuns = bsv.getYuns();
			myunsfs = bsv.getYunsfs();
			myingd = bsv.getYingd();
			mkuid = bsv.getKuid(); 
			mJihkjb_id = bsv.getJihkjb_id();
			mMeikjsb_id = bsv.getMeikjsb_id();
			mYunfjsb_id = bsv.getYunfjsb_id();
			
			mkuidjfyf_je = bsv.getKuidjfyf_je();
			mkuidjfzf_je = bsv.getKuidjfzf_je();
			mchaokdl = Math.abs(bsv.getChaokdl());	//超/亏吨量都以正数显示
			mChaodorKuid = bsv.getChaodOrKuid();	//超/亏吨标识

			((Visit) getPage().getVisit()).setLong4(bsv.getMeikxxb_Id()); // 煤矿信息表id
			((Visit) getPage().getVisit()).setLong5(bsv.getFaz_Id()); // 发站id
			((Visit) getPage().getVisit()).setLong6(bsv.getDaoz_Id()); // 到站id
			((Visit) getPage().getVisit()).setDouble1(bsv.getGongfsl()); // 供方数量

			Fayjsdcz Jsdcz = new Fayjsdcz();

			if (!mQnetar_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Qnetar(kcal/kg)", "Qnetar_ht",
						"Qnetar_kf", "Qnetar_cf", "Qnetar_js", "Qnetar_yk",
						"Qnetar_zdj", "Qnetar_zje", MainGlobal.Mjkg_to_kcalkg(
								mQnetar_ht, "-", 0), MainGlobal.Mjkg_to_kcalkg(
								mQnetar_kf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
								mQnetar_cf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
								mQnetar_js, 0,mMjtokcalxsclfs), mQnetar_yk, mQnetar_zdj,
						mQnetar_zje);
			}

			if (!mAd_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Ad(%)", "Ad_ht", "Ad_kf",
						"Ad_cf", "Ad_js", "Ad_yk", "Ad_zdj", "Ad_zje", mAd_ht,
						mAd_kf, mAd_cf, mAd_js, mAd_yk, mAd_zdj, mAd_zje);
			}

			if (!mStd_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Std(%)", "Std_ht", "Std_kf",
						"Std_cf", "Std_js", "Std_yk", "Std_zdj", "Std_zje",
						mStd_ht, mStd_kf, mStd_cf, mStd_js, mStd_yk, mStd_zdj,
						mStd_zje);
			}

			if (!mVdaf_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Vdaf(%)", "Vdaf_ht", "Vdaf_kf",
						"Vdaf_cf", "Vdaf_js", "Vdaf_yk", "Vdaf_zdj",
						"Vdaf_zje", mVdaf_ht, mVdaf_kf, mVdaf_cf, mVdaf_js,
						mVdaf_yk, mVdaf_zdj, mVdaf_zje);
			}

			if (!mMt_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Mt(%)", "Mt_ht", "Mt_kf",
						"Mt_cf", "Mt_js", "Mt_yk", "Mt_zdj", "Mt_zje", mMt_ht,
						mMt_kf, mMt_cf, mMt_js, mMt_yk, mMt_zdj, mMt_zje);
			}

			if (!mQgrad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Qgrad(kcal/kg)", "Qgrad_ht",
						"Qgrad_kf", "Qgrad_cf", "Qgrad_js", "Qgrad_yk",
						"Qgrad_zdj", "Qgrad_zje", MainGlobal.Mjkg_to_kcalkg(
								mQgrad_ht, "-", 0), MainGlobal.Mjkg_to_kcalkg(
								mQgrad_kf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
								mQgrad_cf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
								mQgrad_js, 0,mMjtokcalxsclfs), mQgrad_yk, mQgrad_zdj,
						mQgrad_zje);
			}

			if (!mQbad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Qbad(kcal/kg)", "Qbad_ht",
						"Qbad_kf", "Qbad_cf", "Qbad_js", "Qbad_yk", "Qbad_zdj",
						"Qbad_zje",
						MainGlobal.Mjkg_to_kcalkg(mQbad_ht, "-", 0), MainGlobal
								.Mjkg_to_kcalkg(mQbad_kf, 0,mMjtokcalxsclfs), MainGlobal
								.Mjkg_to_kcalkg(mQbad_cf, 0,mMjtokcalxsclfs), MainGlobal
								.Mjkg_to_kcalkg(mQbad_js, 0,mMjtokcalxsclfs), mQbad_yk,
						mQbad_zdj, mQbad_zje);
			}

			if (!mHad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Had(%)", "Had_ht", "Had_kf",
						"Had_cf", "Had_js", "Had_yk", "Had_zdj", "Had_zje",
						mHad_ht, mHad_kf, mHad_cf, mHad_js, mHad_yk, mHad_zdj,
						mHad_zje);
			}

			if (!mStad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Stad(%)", "Stad_ht", "Stad_kf",
						"Stad_cf", "Stad_js", "Stad_yk", "Stad_zdj",
						"Stad_zje", mStad_ht, mStad_kf, mStad_cf, mStad_js,
						mStad_yk, mStad_zdj, mStad_zje);
			}
			
			if (!mStar_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Star(%)", "Star_ht", "Star_kf",
						"Star_cf", "Star_js", "Star_yk", "Star_zdj",
						"Star_zje", mStar_ht, mStar_kf, mStar_cf, mStar_js,
						mStar_yk, mStar_zdj, mStar_zje);
			}

			if (!mMad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Mad(%)", "Mad_ht", "Mad_kf",
						"Mad_cf", "Mad_js", "Mad_yk", "Mad_zdj", "Mad_zje",
						mMad_ht, mMad_kf, mMad_cf, mMad_js, mMad_yk, mMad_zdj,
						mMad_zje);
			}

			if (!mAar_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Aar(%)", "Aar_ht", "Aar_kf",
						"Aar_cf", "Aar_js", "Aar_yk", "Aar_zdj", "Aar_zje",
						mAar_ht, mAar_kf, mAar_cf, mAar_js, mAar_yk, mAar_zdj,
						mAar_zje);
			}

			if (!mAad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Aad(%)", "Aad_ht", "Aad_kf",
						"Aad_cf", "Aad_js", "Aad_yk", "Aad_zdj", "Aad_zje",
						mAad_ht, mAad_kf, mAad_cf, mAad_js, mAad_yk, mAad_zdj,
						mAad_zje);
			}

			if (!mVad_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("Vad(%)", "Vad_ht", "Vad_kf",
						"Vad_cf", "Vad_js", "Vad_yk", "Vad_zdj", "Vad_zje",
						mVad_ht, mVad_kf, mVad_cf, mVad_js, mVad_yk, mVad_zdj,
						mVad_zje);
			}

			if (!mT2_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("T2(%)", "T2_ht", "T2_kf",
						"T2_cf", "T2_js", "T2_yk", "T2_zdj", "T2_zje", mT2_ht,
						mT2_kf, mT2_cf, mT2_js, mT2_yk, mT2_zdj, mT2_zje);
			}

			if (!mYunju_ht.equals("")) {

				mstrJieszb += Jsdcz.SetJieszb("运距(Km)", "Yunju_ht", "Yunju_kf",
						"Yunju_cf", "Yunju_js", "Yunju_yk", "Yunju_zdj",
						"Yunju_zje", mYunju_ht, mYunju_kf, mYunju_cf,
						mYunju_js, mYunju_yk, mYunju_zdj, mYunju_zje);
			}

			setJieszb(mstrJieszb);
			
			mstrHejdxh=mn.NumToRMBStr(mhej);//转换大小写
//			设置超/亏吨的显示	
			mstrHejdxh=Jsdcz.SetHejdxh(bsv.getChaodOrKuid(),mchaokdl,mhej,mstrHejdxh);
			
			this.setHejdxh(mstrHejdxh);
			

			if (!mErroMessage.equals("")) {

				this.setMsg(mErroMessage);
			} else if ((((Visit) getPage().getVisit()).getLong2() == Locale.liangpjs_feiylbb_id || ((Visit) getPage()
					.getVisit()).getLong2() == Locale.meikjs_feiylbb_id)
					&& mshulzjbz == 0) {
				this.setMsg("合同设置有误请检查");
			}

			_editvalues.add(new Dcbalancebean(mid, myid, mtianzdw, mjiesbh,
					mfahdw, mmeikdw,mfaz, 
//					myunsfsb_id,
					mshoukdw, mfahksrq, mfahjzrq, myansksrq,
					myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh, mhetsl,
					mgongfsl, mches, mxianshr, mfapbh, mdaibcc, myansbh,
					mduifdd, mfukfs, mshulzjbz, myanssl, myingksl, 
//					mkuid,
					mshulzjje,
					mjiessl, 
//					mjiesslcy,
					myunfjsl,mbuhsdj, mjiakje,
					mbukyqjk, mjiakhj, mjiaksl, mjiaksk, mjiasje, mtielyf,mtielzf,
					mkuangqyf,mkuangqzf, mkuangqsk, mkuangqjk,mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
					myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq,
					mfahrq, mchangcwjbr, mchangcwjbrq, mruzrq,
					mjieszxjbr, mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq,
					mhetjg, mjieslx,myuns,mkoud_js,
					myunsfs, mdiancjsbs,mhetb_id,
//					myunju,
					mMeikjsb_id,
					mYunfjsb_id,mJihkjb_id,
//					mfengsjj,
//					mjiajqdj,
//					mjijlx,
					mMjtokcalxsclfs,
					mkuidjfyf_je,
					mkuidjfzf_je,mchaokdl,mChaodorKuid));

			_Jieszbvalues.add(new Dcbalancezbbean(mQnetar_ht, mStd_ht, mAd_ht,
					mVdaf_ht, mMt_ht, mQgrad_ht, mQbad_ht, mHad_ht, mStad_ht,
					mMad_ht, mAar_ht, mAad_ht, mVad_ht, mT2_ht, mYunju_ht,mStar_ht,
					mQnetar_kf, mStd_kf, mAd_kf, mVdaf_kf, mMt_kf, mQgrad_kf,
					mQbad_kf, mHad_kf, mStad_kf, mMad_kf, mAar_kf, mAad_kf,
					mVad_kf, mT2_kf, mYunju_kf, mStar_kf,mQnetar_cf, mStd_cf, mAd_cf,
					mVdaf_cf, mMt_cf, mQgrad_cf, mQbad_cf, mHad_cf, mStad_cf,
					mMad_cf, mAar_cf, mAad_cf, mVad_cf, mT2_cf, mYunju_cf,mStar_cf,
					mQnetar_js, mStd_js, mAd_js, mVdaf_js, mMt_js, mQgrad_js,
					mQbad_js, mHad_js, mStad_js, mMad_js, mAar_js, mAad_js,
					mVad_js, mT2_js, mYunju_js, mStar_js,mQnetar_yk, mStd_yk, mAd_yk,
					mVdaf_yk, mMt_yk, mQgrad_yk, mQbad_yk, mHad_yk, mStad_yk,
					mMad_yk, mAar_yk, mAad_yk, mVad_yk, mT2_yk, mYunju_yk,mStar_yk,
					mQnetar_zdj, mStd_zdj, mAd_zdj, mVdaf_zdj, mMt_zdj,
					mQgrad_zdj, mQbad_zdj, mHad_zdj, mStad_zdj, mMad_zdj,
					mAar_zdj, mAad_zdj, mVad_zdj, mT2_zdj, mYunju_zdj,mStar_zdj,
					mQnetar_zje, mStd_zje, mAd_zje, mVdaf_zje, mMt_zje,
					mQgrad_zje, mQbad_zje, mHad_zje, mStad_zje, mMad_zje,
					mAar_zje, mAad_zje, mVad_zje, mT2_zje, mYunju_zje,
					mStar_zje));

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
		this.setJieszbValues(_Jieszbvalues);
		return getEditValues();
	}

	public void setJieszb(String value) {

		((Visit) getPage().getVisit()).setString8(value);
	}

	public String getJieszb() {

		return ((Visit) getPage().getVisit()).getString8();
	}
	
	public void setHejdxh(String value){
		
		((Visit) getPage().getVisit()).setString9(value);
	}
	
	public String getHejdxh(){
		
		return ((Visit) getPage().getVisit()).getString9();
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		// 返回
		ToolbarButton quxbt = new ToolbarButton(null, "返回",
				"function(){ document.Form0.ReturnButton.click();}");
		quxbt.setId("fanhbt");
		tb1.addItem(quxbt);
		tb1.addText(new ToolbarText("-"));
		//		
		if (((Dcbalancebean) getEditValues().get(0)).getId() == 0
				|| ((Dcbalancebean) getEditValues().get(0)).getYid() == 0) {

			// 保存
			ToolbarButton savebt = new ToolbarButton(null, "保存",
					"function(){ document.Form0.SaveButton.click(); }");
			savebt.setId("savebt");
			tb1.addItem(savebt);

			// if(((Visit) getPage().getVisit()).getLong2()==1||((Visit)
			// getPage().getVisit()).getLong2()==3){
			//				
			// tb1.addText(new ToolbarText("-"));
			//				
			// ToolbarButton kuangqzfbt=new
			// ToolbarButton(null,"矿区杂费录入","function(){
			// document.Form0.KuangqzfButton.click(); }");
			// kuangqzfbt.setId("kuangqzfbt");
			// tb1.addItem(kuangqzfbt);
			// }

		} else if (((Dcbalancebean) getEditValues().get(0)).getId() > 0
				|| ((Dcbalancebean) getEditValues().get(0)).getYid() > 0) {

			// 提交进入流程
			ToolbarButton submitbt = new ToolbarButton(
					null,
					"提交进入流程",
					"function(){  \n"
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
							+ " \tselectOnFocus:true,	\n"
							+ "	\ttransform:'LIUCMCSelect',	\n"
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
//							+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"
							+ " \t}	\n" + " \t}");
			submitbt.setId("submitbt");
			tb1.addItem(submitbt);
		}

		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);

			// ((Visit) getPage().getVisit()).getLong1() //电厂信息表id
			// ((Visit) getPage().getVisit()).getString1() //fahb_Id
			// ((Visit) getPage().getVisit()).getLong2() //结算类型
			// ((Visit) getPage().getVisit()).getString2() //是否需要结算指标调整（是、否）
			// ((Visit) getPage().getVisit()).getString4() //验收编号
			// ((Visit) getPage().getVisit()).getLong3() //发货单位表id
			// visit.setLong4(0); //煤矿信息表id
			// visit.setLong5(0); //发站
			// visit.setLong6(0); //到站
			// visit.setLong7(0); //结算运费表id
			// visit.setLong8(0); //合同表id
			// visit.setLong9(0); //运输单位id
			// visit.setDouble1(0); //总票重
//			visit.setString12("");	//上次结算量
//			visit.setString13(string); //发货id

			((Visit) getPage().getVisit()).setDropDownBean1(null); // zhibb
			((Visit) getPage().getVisit()).setProSelectionModel1(null);

			((Visit) getPage().getVisit()).setDropDownBean3(null); // 供应商
			((Visit) getPage().getVisit()).setProSelectionModel3(null);

			((Visit) getPage().getVisit()).setDropDownBean4(null); // 车站
			((Visit) getPage().getVisit()).setProSelectionModel4(null);

			((Visit) getPage().getVisit()).setDropDownBean6(null); // 品种
			((Visit) getPage().getVisit()).setProSelectionModel6(null);

			((Visit) getPage().getVisit()).setDropDownBean7(null); // 收款单位
			((Visit) getPage().getVisit()).setProSelectionModel7(null);

			((Visit) getPage().getVisit()).setString8(""); // 结算指标页面显示值
			((Visit) getPage().getVisit()).setString9(""); // 合计大写行显示内容（为实现动态设置“超扣吨”显示用）

			((Visit) getPage().getVisit()).setLong4(0); // 煤矿信息表id
			((Visit) getPage().getVisit()).setLong5(0); // 发站
			((Visit) getPage().getVisit()).setLong6(0); // 到站
			((Visit) getPage().getVisit()).setLong7(0); // 结算运费表id
			((Visit) getPage().getVisit()).setDouble1(0); // 总票重
			((Visit) getPage().getVisit()).setString11(""); // 用于Kuangqzf返回时，跳转到正确的界面(DCBalance,Jiesdxg)

			setLiucmcValue(null);// 10
			setILiucmcModel(null);// 10
			getILiucmcModels();
			setZhanghValue(null);
			setZhanghModel(null);
			setKaihyhValue(null);
			setKaihyhModel(null);

			getIZhibbmModels();
			getIShoukdwModels();
			((Visit) getPage().getVisit()).setboolean1(false); // 合同号
			((Visit) getPage().getVisit()).setboolean2(false); // 填制单位
			getSelectData();
		}
		getToolbars();
	}

	// 指标表编码Model

	public IDropDownBean getZhibbmValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIZhibbmModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setZhibbmValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean1(value);
	}

	public void setIZhibbmModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIZhibbmModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIZhibbmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIZhibbmModels() {
		String sql = "select id,bianm from zhibb where leib=1 order by bianm";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	// 指标表编码Model_end

	// 收款单位
	public IDropDownBean getShoukdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getIShoukdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setShoukdwValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setIShoukdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getIShoukdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

			getIShoukdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public IPropertySelectionModel getIShoukdwModels() {
		String sql="select id, quanc from gongysb order by quanc";
		((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	// 收款单位_end

//  开户银行
	private IDropDownBean KaihyhValue;

	public IDropDownBean getKaihyhValue() {
		if (KaihyhValue == null) {
			KaihyhValue = (IDropDownBean) getKaihyhModel().getOption(0);
		}
		return KaihyhValue;
	}

	public void setKaihyhValue(IDropDownBean Value) {
			KaihyhValue = Value;
	}

	private IPropertySelectionModel KaihyhModel;

	public void setKaihyhModel(IPropertySelectionModel value) {
		KaihyhModel = value;
	}

	public IPropertySelectionModel getKaihyhModel() {
		if (KaihyhModel == null) {
			getKaihyhModels();
		}
		return KaihyhModel;
	}

	public IPropertySelectionModel getKaihyhModels() {
		String sql="select id, kaihyh from gongysb order by quanc";
		
		KaihyhModel = new IDropDownModel(sql);
		return KaihyhModel;
	}
//    银行账号
	private IDropDownBean ZhanghValue;

	public IDropDownBean getZhanghValue() {
		if (ZhanghValue == null) {
			ZhanghValue = (IDropDownBean) getZhanghModel().getOption(0);
		}
		return ZhanghValue;
	}

	public void setZhanghValue(IDropDownBean Value) {
			ZhanghValue = Value;
	}

	private IPropertySelectionModel ZhanghModel;

	public void setZhanghModel(IPropertySelectionModel value) {
		ZhanghModel = value;
	}

	public IPropertySelectionModel getZhanghModel() {
		if (ZhanghModel == null) {
			getZhanghModels();
		}
		return ZhanghModel;
	}

	public IPropertySelectionModel getZhanghModels() {
		String sql="select id, Zhangh from gongysb order by quanc";
		
		ZhanghModel = new IDropDownModel(sql);
		return ZhanghModel;
	}
	
	// 流程名称
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getILiucmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setLiucmcValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getILiucmcModels() {

		String sql = "select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='结算' order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	// 电厂名称_end

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
