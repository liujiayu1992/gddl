package com.zhiren.dtrlgs.faygl.shulgl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dtrlgs.faygl.faygs.FaycbBean;
import com.zhiren.dtrlgs.faygl.faygs.FayzgInfo;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Fayxxwh extends BasePage implements PageValidateListener{
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _QumChick = false;
	public void QumButton(IRequestCycle cycle) {
		_QumChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			setOriRiq(getRiq());
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}else if (_QumChick) {
			_QumChick = false;
			((Visit) getPage().getVisit()).setString11(getChange());
			((Visit) getPage().getVisit()).setString2(((Visit) getPage().getVisit()).getActivePageName());
			cycle.activate("Qumxx");
		}
	}
	public void save() {
		StringBuffer sSql = new StringBuffer("begin \n");
		long id = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		ResultSetList dlrsl = getExtGrid().getDeleteResultSet(getChange());
		if (dlrsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fayxxwh.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (dlrsl.next()) {
			id = dlrsl.getInt("id");
			sSql.append("delete from fayslb where id=" + id+";\n");
		}
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		if(mdrsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fayxxwh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
//		List fayslb_idList=new ArrayList();
		while (mdrsl.next()) {
			id = mdrsl.getInt("id");
			String fahdd=getExtGrid().getValueSql(getExtGrid().getColumn("fahdd"),mdrsl.getString("fahdd"));
			String gongys=getExtGrid().getValueSql(getExtGrid().getColumn("GONGYSMC"),mdrsl.getString("GONGYSMC"));
			String meikdw=getExtGrid().getValueSql(getExtGrid().getColumn("MEIKDW"),mdrsl.getString("MEIKDW"));
			String kehmc=getExtGrid().getValueSql(getExtGrid().getColumn("KEHMC"),mdrsl.getString("KEHMC"));
			String shouhdw=getExtGrid().getValueSql(getExtGrid().getColumn("SHOUHDW"),mdrsl.getString("SHOUHDW"));
			String faz=getExtGrid().getValueSql(getExtGrid().getColumn("FAZ"),mdrsl.getString("FAZ"));
			String daoz=getExtGrid().getValueSql(getExtGrid().getColumn("DAOZ"),mdrsl.getString("DAOZ"));
			String yunsfs=getExtGrid().getValueSql(getExtGrid().getColumn("YUNSFS"),mdrsl.getString("YUNSFS"));
			String chuanm=getExtGrid().getValueSql(getExtGrid().getColumn("CHUANM"),mdrsl.getString("CHUANM"));
			long hetb_id=getExtGrid().getColumn("hetb_id").combo.getBeanId(mdrsl.getString("HETB_ID"));
			String chec=mdrsl.getString("CHEC");
			StringBuffer sb1=new StringBuffer();
			StringBuffer sb2=new StringBuffer();
			StringBuffer sbChk=new StringBuffer();

			String chukkssj=mdrsl.getString("CHUKKSSJ");
			if(chukkssj.equals("")||chukkssj==null){
				chukkssj="";
			}else{
				sbChk.append(",CHUKKSSJ");
				chukkssj+=" "+mdrsl.getString("CHUKKSSJ_HR")+":"+mdrsl.getString("CHUKKSSJ_MI")+":00";
				sb1.append(chukkssj);
			}
			
			String chukjssj=mdrsl.getString("CHUKJSSJ");
			if(chukjssj.equals("")||chukjssj==null){
				chukjssj="";
			}else{
				sbChk.append(",CHUKJSSJ");
				chukjssj+=" "+mdrsl.getString("CHUKJSSJ_HR")+":"+mdrsl.getString("CHUKJSSJ_MI")+":00";
				sb2.append(chukjssj);
			}
			getExtGrid().getColumn("FAHRQ").datatype="String";
			String fahrq=getExtGrid().getValueSql(getExtGrid().getColumn("FAHRQ"),mdrsl.getString("FAHRQ"));
			
			String pinz=getExtGrid().getValueSql(getExtGrid().getColumn("PINZ"),mdrsl.getString("PINZ"));
			String meil=mdrsl.getString("MEIL");
			String lurry=mdrsl.getString("LURRY");
			
			if (id == 0) {
				   String fayslb_Newid=  MainGlobal.getNewID(visit.getDiancxxb_id());
//				   fayslb_idList.add(new FaycbBean(new Long(fahdd).longValue(),new Long(fayslb_Newid).longValue()));   //与收煤有关
				sSql.append("insert into fayslb (id, diancxxb_id,gongysb_id,meikxxb_id," +
						"shr_diancxxb_id,faz_id,daoz_id,yunsfsb_id, chec,luncxxb_id," +
						"pinzb_id,meil,lurry,lursj,fahrq,keh_diancxxb_id ")
					.append(sbChk)
					.append(",hetb_id")
					.append(",neibxs) values(")
					.append(fayslb_Newid + ",")
					.append(fahdd+","+gongys+",")
					.append(meikdw+","+shouhdw+",")
					.append(faz+","+daoz+",")
					.append(yunsfs+",'"+chec+"',"+chuanm+",")
					.append(pinz+","+meil+","+lurry+", sysdate,")
					.append("to_date('"+fahrq+"','yyyy-mm-dd'),"+kehmc+"");
				if(!chukkssj.equals("")){
					sSql.append(", to_date('"+sb1.toString()+"','yyyy-mm-dd hh24:mi:ss')");
				}
				if(!chukjssj.equals("")){
					sSql.append(", to_date('"+sb2.toString()+"','yyyy-mm-dd hh24:mi:ss')");
				}
				 sSql.append(","+hetb_id);
					sSql.append(",(select neibxs from diancxxb where id="+kehmc+"));\n");
					
			} else {
				sSql.append("update fayslb set diancxxb_id=").append(fahdd)
					.append(",gongysb_id=").append(gongys)
					.append(", meikxxb_id=").append(meikdw)
					.append(",keh_diancxxb_id=").append(kehmc)
					.append(",shr_diancxxb_id=").append(shouhdw)
					.append(",faz_id=").append(faz)
					.append(",daoz_id=").append(daoz)
					.append(",yunsfsb_id=").append(yunsfs)
					.append(",chec='").append(chec+"'")
					.append(",luncxxb_id=").append(chuanm)
					.append(",pinzb_id=").append(pinz)
					.append(",meil=").append(meil)
					.append(",fahrq=to_date('").append(fahrq).append("','yyyy-mm-dd')\n");
				if(!chukkssj.equals("")){
					sSql.append(",CHUKKSSJ= to_date('"+sb1.toString()+"','yyyy-mm-dd hh24:mi:ss')\n");
				}
				if(!chukjssj.equals("")){
					sSql.append(",CHUKJSSJ= to_date('"+sb2.toString()+"','yyyy-mm-dd hh24:mi:ss')\n");
				}
				sSql.append(",hetb_id="+hetb_id);
				sSql.append(",neibxs=(select neibxs from diancxxb where id="+kehmc+")");
				sSql.append(" where id="+id).append(";\n");
			}
		}
		dlrsl.close();
		mdrsl.close();
		sSql.append("end;");
		if(con.getUpdate(sSql.toString())>=0){
//			FayzgInfo.CountChengb(fayslb_idList,true);
			setMsg("保存成功！");
		}else {
			
			setMsg("保存失败！");
		}
		con.Close();
	}

	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String FyxxSql=	"select f.id, d.mingc as fahdd ,g.mingc as gongysmc,\n" +
			"m.mingc as meikdw,k.mingc as kehmc,s.mingc as shouhdw,fz.mingc as faz,dz.mingc as daoz,\n" + 
			"ys.mingc as yunsfs, f.chec as chec,lc.mingc as CHUANM,\n" + 
			"pz.mingc as pinz,\n" + 
			" (select hetbh from hetb where id=f.hetb_id) hetb_id,\n "+
			"f.meil as meil,\n" + 
			"f.fahrq as fahrq,\n" + 
			"f.chukkssj as chukkssj,\n" + 
			"to_char(f.chukkssj,'hh24')as chukkssj_hr,\n" + 
			"to_char(f.chukkssj,'mi')as chukkssj_mi,\n" + 
			"f.chukjssj as chukjssj,\n" + 
			"to_char(f.chukjssj,'hh24')as chukjssj_hr,\n" + 
			"to_char(f.chukjssj,'mi')as chukjssj_mi,\n" + 
//			
			"f.lurry as lurry,\n" + 
			"f.lursj as lursj\n" + 
			"from fayslb f,(select id,mingc from diancxxb where cangkb_id<>1) d,\n" + 
			"(select id,mingc from gongysb) g, (select id,mingc from meikxxb) m,(select id,mingc from diancxxb where cangkb_id=1) k,\n" + 
			"(select id,mingc from diancxxb where cangkb_id=1) s,(select id,mingc from chezxxb) fz,\n" + 
			"chezxxb dz ,yunsfsb ys,luncxxb lc,vwpinz pz\n" + 
			"where d.id=f.diancxxb_id\n" + 
			"and g.id=f.gongysb_id\n" + 
			"and m.id(+)=f.meikxxb_id\n" + 
			"and s.id=f.shr_diancxxb_id and k.id=f.keh_diancxxb_id \n" + 
			"and fz.id=f.faz_id\n" + 
			"and dz.id=f.daoz_id\n" + 
			"and ys.id=f.yunsfsb_id\n" + 
			"and lc.id(+)=f.luncxxb_id\n" + 
			"and pz.id=f.pinzb_id and f.fahrq>=to_date('"+getRiq()+"','yyyy-mm-dd') and f.fahrq<to_date('"+getRiq()+"','yyyy-mm-dd')+1 \n" +
			" and f.yewlxb_id<>3 \n";


		ResultSetList rsl = con.getResultSetList(FyxxSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + FyxxSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setTableName("fayslb");

		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
				
//		发货地点
		egu.getColumn("fahdd").setHeader("发货地点");
		egu.getColumn("fahdd").setWidth(90);
		ComboBox fahd = new ComboBox();
		fahd.setEditable(true);
//		
		egu.getColumn("fahdd").setEditor(fahd);
//		fahd.setEditable(true);
		String fahdSql="";
		if(1==getJib()){
			fahdSql = "select id,mingc from diancxxb where cangkb_id<>1 order by xuh";
			egu.getColumn("fahdd").setComboEditor(egu.gridId,
					new IDropDownModel(fahdSql));
		}else if(2==getJib()){
			fahdSql = "select id,mingc from diancxxb where cangkb_id<>1 and fuid=" + visit.getDiancxxb_id() + " order by xuh";
			egu.getColumn("fahdd").setComboEditor(egu.gridId,
					new IDropDownModel(fahdSql));
		}else{
			fahdSql = "select id,mingc from diancxxb where cangkb_id<>1 and id=" + visit.getDiancxxb_id() + " order by xuh";
			fahd.setEditable(false);
			egu.getColumn("fahdd").setComboEditor(egu.gridId,
					new IDropDownModel(fahdSql));
			egu.getColumn("fahdd").setEditor(null);
			egu.getColumn("fahdd").setDefaultValue(visit.getDiancmc());
		}
		egu.getColumn("fahdd").setReturnId(true);

//		供应商名称
		egu.getColumn("gongysmc").setHeader("供应商名称");
		egu.getColumn("gongysmc").setWidth(100);
		ComboBox gongys = new ComboBox();
		gongys.setEditable(true);
		egu.getColumn("gongysmc").setEditor(gongys);
//		if(getJib()==3){
			egu.getColumn("gongysmc").setDefaultValue("塔山");
//		}
		gongys.setEditable(true);
		String gongysSql = "select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysmc").setComboEditor(egu.gridId,
				new IDropDownModel(gongysSql));
		egu.getColumn("gongysmc").setReturnId(true);
		
//		煤矿单位
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("meikdw").setWidth(80);
		ComboBox meikdw = new ComboBox();
		egu.getColumn("meikdw").setEditor(meikdw);
//		if(getJib()==3){
			egu.getColumn("meikdw").setDefaultValue("同煤塔山");
//		}
		meikdw.setEditable(true);
		String meikdwSql = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikdw").setComboEditor(egu.gridId,
				new IDropDownModel(meikdwSql));
		egu.getColumn("meikdw").setReturnId(true);
		egu.getColumn("meikdw").editor.setAllowBlank(true);
		
//		收货单位
		egu.getColumn("kehmc").setHeader("客户名称");
		egu.getColumn("kehmc").setWidth(90);
		ComboBox kehmc = new ComboBox();
		egu.getColumn("kehmc").setEditor(kehmc);
		egu.getColumn("kehmc").setDefaultValue("开滦");
		kehmc.setEditable(true);
		String kehmcSql = "select id,mingc from diancxxb where cangkb_id=1 and jib=3 order by mingc";
		egu.getColumn("kehmc").setComboEditor(egu.gridId,
				new IDropDownModel(kehmcSql));
		egu.getColumn("kehmc").setReturnId(true);
		
//		收货单位
		egu.getColumn("shouhdw").setHeader("流向");
		egu.getColumn("shouhdw").setWidth(90);
		ComboBox shouhdw = new ComboBox();
		shouhdw.setEditable(true);
		egu.getColumn("shouhdw").setEditor(shouhdw);
		shouhdw.setEditable(true);
		String shouhdwSql = "select id,mingc from diancxxb where cangkb_id=1 and jib=3 order by mingc";
		egu.getColumn("shouhdw").setComboEditor(egu.gridId,
				new IDropDownModel(shouhdwSql));
		egu.getColumn("shouhdw").setReturnId(true);
		
//		发站
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("faz").setWidth(60);
		ComboBox faz = new ComboBox();
		egu.getColumn("faz").setEditor(faz);
		faz.setEditable(true);
		String fazSql = "select id,mingc from chezxxb order by xuh";
		egu.getColumn("faz").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		egu.getColumn("faz").setReturnId(true);
		
		
//		到站
		egu.getColumn("daoz").setHeader("到站");
		egu.getColumn("daoz").setWidth(60);
		ComboBox daoz = new ComboBox();
		egu.getColumn("daoz").setEditor(daoz);
		daoz.setEditable(true);
		String daozSql = "select id,mingc from chezxxb order by xuh";
		egu.getColumn("daoz").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		egu.getColumn("daoz").setReturnId(true);
		
//		运输方式
		egu.getColumn("yunsfs").setHeader("运输方式");
		egu.getColumn("yunsfs").setWidth(60);
		ComboBox yunsfs = new ComboBox();
		egu.getColumn("yunsfs").setEditor(yunsfs);
		yunsfs.setEditable(true);
		String yunsfsSql = "select id,mingc from yunsfsb order by id";
		egu.getColumn("yunsfs").setComboEditor(egu.gridId,
				new IDropDownModel(yunsfsSql));
		egu.getColumn("yunsfs").setReturnId(true);
		
		egu.getColumn("chec").setHeader("车次/航次");
		egu.getColumn("chec").setWidth(80);

//		船名
		egu.getColumn("CHUANM").setHeader("船名");
		egu.getColumn("CHUANM").setWidth(80);
		ComboBox chuanm = new ComboBox();
		egu.getColumn("CHUANM").setEditor(chuanm);
		chuanm.setEditable(true);
		String chuanmSql = "select id,mingc from luncxxb order by id";
		egu.getColumn("CHUANM").setComboEditor(egu.gridId,
				new IDropDownModel(chuanmSql));
		egu.getColumn("CHUANM").setReturnId(true);
		egu.getColumn("CHUANM").editor.setAllowBlank(true);
		
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(60);
		ComboBox pinz = new ComboBox();
		egu.getColumn("pinz").setEditor(pinz);
		pinz.setEditable(true);
		String pz = "select id,mingc from vwpinz  where leib='煤' order by mingc";
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(pz));
		egu.getColumn("pinz").setReturnId(true);
		
		egu.getColumn("hetb_id").setHeader("合同");
		egu.getColumn("hetb_id").setWidth(120);
		ComboBox hetb_combox=new ComboBox();
		egu.getColumn("hetb_id").setEditor(hetb_combox);
		hetb_combox.setEditable(true);
		String ht = " select id,hetbh from hetb where leib<>2 and qisrq<=to_date('"+getRiq()+"','yyyy-mm-dd') and guoqrq>=to_date('"+getRiq()+"','yyyy-mm-dd')";//销售合同
		egu.getColumn("hetb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ht));
		egu.getColumn("hetb_id").setReturnId(true);
		
		egu.getColumn("meil").setHeader("煤量");
		egu.getColumn("meil").setWidth(60);
		egu.getColumn("meil").setEditor(null);
		egu.getColumn("meil").setDefaultValue("0");
		egu.getColumn("meil").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setWidth(80);
		
//		出库开始时间
		egu.getColumn("chukkssj").setHeader("出库开始时间");
		egu.getColumn("chukkssj").setWidth(80);
		
		egu.getColumn("chukkssj_hr").setHeader("时");
		egu.getColumn("chukkssj_hr").setWidth(30);
		List lsShi = new ArrayList();
		for(int i=0;i<24;i++){
			if(i<10){
				lsShi.add(new IDropDownBean(i, "0"+i));
			}else{
				lsShi.add(new IDropDownBean(i, ""+i));
			}
		}
		ComboBox Shi = new ComboBox();
		egu.getColumn("chukkssj_hr").setEditor(Shi);
		Shi.setEditable(true);
		egu.getColumn("chukkssj_hr").setComboEditor(egu.gridId,
				new IDropDownModel(lsShi));
		egu.getColumn("chukkssj_hr").setDefaultValue("00");
		

		egu.getColumn("chukkssj_mi").setHeader("分");
		egu.getColumn("chukkssj_mi").setWidth(30);
		List lsFen = new ArrayList();
		for(int i=0;i<60;i++){
			if(i<10){
				lsFen.add(new IDropDownBean(i, "0"+i));
			}else{
				lsFen.add(new IDropDownBean(i, ""+i));
			}
		}
		ComboBox fen = new ComboBox();
		egu.getColumn("chukkssj_mi").setEditor(fen);
		fen.setEditable(true);
		egu.getColumn("chukkssj_mi").setComboEditor(egu.gridId,
				new IDropDownModel(lsFen));
		egu.getColumn("chukkssj_mi").setDefaultValue("00");
		
		
//		出库结束时间
		egu.getColumn("chukjssj").setHeader("出库结束时间");
		egu.getColumn("chukjssj").setWidth(80);
		
		egu.getColumn("chukjssj_hr").setHeader("时");
		egu.getColumn("chukjssj_hr").setWidth(30);
		ComboBox CShi = new ComboBox();
		egu.getColumn("chukjssj_hr").setEditor(CShi);
		CShi.setEditable(true);
		egu.getColumn("chukjssj_hr").setComboEditor(egu.gridId,
				new IDropDownModel(lsShi));
		egu.getColumn("chukjssj_hr").setDefaultValue("00");
		

		egu.getColumn("chukjssj_mi").setHeader("分");
		egu.getColumn("chukjssj_mi").setWidth(30);
		ComboBox Cfen = new ComboBox();
		egu.getColumn("chukjssj_mi").setEditor(Cfen);
		Cfen.setEditable(true);
		egu.getColumn("chukjssj_mi").setComboEditor(egu.gridId,
				new IDropDownModel(lsFen));
		egu.getColumn("chukjssj_mi").setDefaultValue("00");
		
		
		egu.getColumn("lurry").setHeader("录入人员");
		egu.getColumn("lurry").setWidth(60);
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("lurry").setDefaultValue(visit.getRenyID()+"");
		egu.getColumn("lurry").setHidden(true);
		
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setWidth(60);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("lursj").setHidden(true);
	
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
	
		egu.addTbarText("日期:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","forms[0]");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		
		egu.addTbarText("-");
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		String DelCondition="var rec=gridDiv_sm.getSelected();if(rec.get('MEIL')!='0'){Ext.MessageBox.alert('提示信息','已取煤数据不能删除!');return;}\n";
		IGridButton delete = new IGridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "",DelCondition);
		egu.addTbarBtn(delete);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton qum = new GridButton("取煤", "Qum");
		qum.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(qum);
		
		StringBuffer sbJingz = new StringBuffer();
		sbJingz.append("gridDiv_grid.on('beforeedit',function(e){\n")
				.append("if(e.field=='CHUANM'){if(e.record.get('YUNSFS')!='海运'){e.cancel=true;}")
				.append("}else{e.cancel=false;}" +
						"if(e.record.get('MEIL')>0){e.cancel=true;}});\n");
		sbJingz.append("gridDiv_grid.on('afteredit',function(e){ ");
		sbJingz.append("if(e.field == 'CHUKJSSJ')" +
				"{e.record.set('FAHRQ',e.record.get('CHUKJSSJ'));}" +
				"if(e.field == 'YUNSFS'||e.field=='CHUANM')" +
					"{if(e.record.get('YUNSFS')!='海运'){e.record.set('CHUANM','');}}");
		sbJingz.append("});\n");

		egu.addOtherScript(sbJingz.toString());
		
		setExtGrid(egu);
		con.Close();
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
		}
		visit.setString2("");
		visit.setString11("");
		init();
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		
	}
	
	private void init() {
	  
		setOriRiq(getRiq());
		getSelectData();
	}
	
	
	private	class IGridButton extends GridButton{
		public IGridButton(int btnType,String parentId,List columns,String tapestryBtnId,String condition){
			super( btnType, parentId, columns, tapestryBtnId,condition);
		}
		public String getDeleteScript() {
			StringBuffer record = new StringBuffer();
			record.append("function() {\n");
			record.append( super.condition+"\n");
			record.append("for(i=0;i<"+parentId+"_sm.getSelections().length;i++){\n");
			record.append("	record = "+parentId+"_sm.getSelections()[i];\n");
			
			StringBuffer sb = new StringBuffer();
			//sb.append(b);
			sb.append(parentId).append("_history += '<result>' ")
			.append("+ '<sign>D</sign>' ");
			
			for(int c=0;c<columns.size();c++) {
				if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
					GridColumn gc = ((GridColumn)columns.get(c));
					if(gc.update) {
						if("date".equals(gc.datatype)) {
							sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("record.get('")
							.append(gc.dataIndex).append("'))?").append("record.get('")
							.append(gc.dataIndex).append("'):").append("record.get('")
							.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
							.append("+ '</").append(gc.dataIndex).append(">'\n");
						}else {
							sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("record.get('").append(gc.dataIndex).append("')");
							if(!gc.datatype.equals(GridColumn.DataType_Float)) {
								sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
							}
							sb.append("+ '</").append(gc.dataIndex).append(">'\n");
						}
					}
				}
			}
			sb.append(" + '</result>' ;");
			record.append(sb);
			
			record.append("	"+parentId+"_ds.remove("+parentId+"_sm.getSelections()[i--]);}}");
			return record.toString();
		}
	}
	
	private int getJib(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rs=con.getResultSetList("select jib from diancxxb where id= " + visit.getDiancxxb_id());
		rs.next();
		return rs.getInt("jib");
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

}