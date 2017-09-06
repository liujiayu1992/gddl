package com.zhiren.dc.jiek;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shul extends BasePage implements PageValidateListener {
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
//	绑定日期
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

	private boolean _ShenhClick = false;
	public void ShangcButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}
	
	private void Shenh() {//上传
	//1，验证数量全部审核，质量全部二级审核；新增：限制表条件
	//2,远程验证供应商编码必须存在，煤矿、新增：限制表条件
	//3，车站、品种远程不存在就构造基础信息插入sql语句//修改为煤矿也要按供应商处理因为编码错误煤矿会乱掉新增：限制表条件
	//4,构造fahbtmp插入语句新增：限制表条件
	//5，执行
	//6，提示执行结果
	//7,//插入相应质量、写入服务器状态，写入fahb的状态，说明已经上传成功
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		try {
		
		String sql="select fahb.hedbz,zhilb.shenhzt\n" +
		"from fahb,zhilb\n" + 
		"where fahb.zhilb_id=zhilb.id(+)\n" + 
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"' and (hedbz<>3 or shenhzt<>1)" +
		" and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq)";
		ResultSet rs=con.getResultSet(sql);
		if(rs.next()){
			setMsg("数据不完整，填补审核后上报！");
			return;
		}
	//2

		sql="select substr(gongysb.shangjgsbm,1,6)shangjgsbm\n" +
		"from fahb,gongysb\n" + 
		"where fahb.gongysb_id=gongysb.id\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"' and fahb.shangc=0 and not exists (\n" + 
		"select * from gongysb@gangkjy Ygongysb where Ygongysb.bianm=substr(gongysb.shangjgsbm,1,6)\n" + 
		")";
		ResultSet rs1=con.getResultSet(sql);
		
			if(rs1.next()){
				setMsg(rs1.getString("shangjgsbm")+"供应商编码不能识别！");
				return;
			}
	//2

		sql="select  meikxxb.shangjgsbm\n" +
		"from fahb,meikxxb\n" + 
		"where fahb.meikxxb_id=meikxxb.id\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"' and fahb.shangc=0 and not exists (\n" + 
		"select * from meikxxb@gangkjy Ymeikxxb where Ymeikxxb.bianm=meikxxb.shangjgsbm\n" + 
		")";
		ResultSet rs2=con.getResultSet(sql);
		if(rs2.next()){
			setMsg(rs2.getString("shangjgsbm")+"煤矿编码不能识别！");
			return;
		}
	//3车站

		sql="insert into chezxxb@gangkjy(id,xuh,bianm,mingc,quanc,lujxxb_id,leib)\n" + 
		"\n" + 
		"select    xl_xul_id.nextval@gangkjy  ,1,mingc,mingc,mingc,2,'车站'\n" + 
		"from(\n" + 
		"select  fz.mingc,fz.id\n" + 
		"from fahb,chezxxb fz\n" + 
		"where fahb.faz_id=fz.id\n" + 
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'and fahb.shangc=0\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		"union\n" + 
		"select  dz.mingc,dz.id\n" + 
		"from fahb,chezxxb dz\n" + 
		"where fahb.daoz_id=dz.id\n" + 
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'and fahb.shangc=0\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		")cz\n" + 
		" where not exists (\n" + 
		"select * from chezxxb@gangkjy Ychezxxb where Ychezxxb.mingc=cz.mingc\n" + 
		")";
		con.getInsert(sql);
	//3品种

		sql="insert into pinzb@gangkjy(id,xuh,bianm,mingc,leib)\n" + 
		"select xl_xul_id.nextval@gangkjy,1,bianm,mingc,'煤' from(select distinct  1,pz.mingc bianm,pz.mingc\n" + 
		"from fahb,pinzb pz\n" + 
		"where fahb.pinzb_id=pz.id\n" + 
		"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'and fahb.shangc=0\n" + 
//		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		" and not exists (\n" + 
		"select * from pinzb@gangkjy Ypinzb where Ypinzb.mingc=pz.mingc\n" + 
		"))";
		con.getInsert(sql);
	//4fahbtmp

		sql="insert into fahbtmp@gangkjy\n" +
		"  (id, yuanid, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, zhilb_id,\n" + 
		"  jiesb_id, yunsfsb_id, chec, maoz, piz, jingz, biaoz, yingd, yingk, yuns, yunsl, koud, kous, kouz, koum, zongkd,\n" + 
		"   sanfsl, ches, tiaozbz, yansbhb_id, lie_id, yuandz_id, yuanshdwb_id, kuangfzlb_id, liucb_id, liucztb_id, hedbz ,\n" + 
		" lieid, laimsl, laimzl, laimkc, guohsj,\n" + 
		" gujm, gujy, heth, hetj, jiekscsj, shangccz, xiugzt, danjc,leix_id)\n" + 
		"\n" + 
		"  select  xl_xul_id.nextval@gangkjy,xl_xul_id.nextval@gangkjy,fahb.diancxxb_id,\n" + 
		"  (select id from gongysb@gangkjy ygongysb where ygongysb.bianm=substr(gongysb.shangjgsbm,1,6))gongysb_id,\n" + 
		"  (select id from meikxxb@gangkjy ymeikxxb where ymeikxxb.bianm=meikxxb.shangjgsbm)meikxxb_id,\n" + 
		"  (select id from pinzb@gangkjy ypinzb where ypinzb.mingc=pinzb.mingc)pinzb_id,\n" + 
		"  (select id from chezxxb@gangkjy ychezxxb where ychezxxb.mingc=fz.mingc)fz_id,\n" + 
		"  (select id from chezxxb@gangkjy ychezxxb where ychezxxb.mingc=dz.mingc)dz_id,\n" + 
		"   (select id from jihkjb@gangkjy yjihkjb where yjihkjb.bianm=jihkjb.bianm)jihkj_id,\n" + 
		"  fahb.fahrq,fahb.daohrq,zhilb_id,jiesb_id,yunsfsb_id, chec, maoz, piz, jingz, biaoz, yingd, yingk, yuns,\n" + 
		"   yunsl, koud, kous, kouz, koum, zongkd, sanfsl, ches,\n" + 
		"   tiaozbz, yansbhb_id, lie_id, yuandz_id, yuanshdwb_id, kuangfzlb_id, liucb_id, liucztb_id, hedbz,\n" + 
		"    fahb_id, laimsl, laimzl, laimkc, daohrq,\n" + 
		"    meij,yunf,heth,hetjg,sysdate,0,0,0,3\n" + 
		"  from fahb,gongysb,chezxxb fz,chezxxb dz,jihkjb,meikxxb,pinzb,guslsb\n" + 
		"  where fahb.gongysb_id=gongysb.id and fahb.faz_id=fz.id and fahb.daoz_id=dz.id and fahb.jihkjb_id=jihkjb.id\n" + 
		"  and fahb.meikxxb_id=meikxxb.id and fahb.pinzb_id=pinzb.id and fahb.id=guslsb.fahb_id and guslsb.leix=2\n" + 
		"   and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"' and fahb.shangc=0\n" + 
		"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
		"";
		int flag=con.getInsert(sql);
		if(flag!=-1){//如果正确
			//要先删除质量，否则二级审核回退后无法上传
			sql="delete from zhilb@gangkjy  Yzhilb where  Yzhilb.id in(\n" +
			"select zhilb.id "+
			"from zhilb,fahb\n" + 
			"where fahb.zhilb_id=zhilb.id\n" + 
			"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"and fahb.shangc=0"+
			"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
			")";
			con.getDelete(sql);
			//插入相应质量
			sql="insert into zhilb@gangkjy(id,caiyb_id,huaybh,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,had,vad,std,qgrad,qgrad_daf,shenhzt,liucztb_id)\n" +
			"select zhilb.id,0,huaybh,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,had,vad,std,qgrad,qgrad_daf,shenhzt,zhilb.liucztb_id\n" + 
			"from zhilb,fahb\n" + 
			"where fahb.zhilb_id=zhilb.id\n" + 
			"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"and fahb.shangc=0"+
			"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) ";
			con.getInsert(sql);
			
			//20091125新增质量临时表处理
			sql="delete from zhillsb@gangkjy  Yzhilb where  Yzhilb.zhilb_id in(\n" +
			"select zhilb.id "+
			"from zhilb,fahb\n" + 
			"where fahb.zhilb_id=zhilb.id\n" + 
			"and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"and fahb.shangc=0"+
			"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) "+
			")";
			con.getDelete(sql);
			

			sql="insert into zhillsb@gangkjy(id,zhilb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,lury,shenhzt,huaylb\n" +
			",bumb_id,har,qgrd)\n" + 
			"select zhillsb.id,fahb.zhilb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,lury,shenhzt,huaylb\n" + 
			",bumb_id,har,qgrd\n" + 
			"      from zhillsb,fahb\n" + 
			"      where fahb.zhilb_id=zhillsb.zhilb_id\n" + 
			"      and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"      and fahb.shangc=0\n" + 
			"        and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq)" ;
			con.getInsert(sql);
			//服务器日志

			sql="select *\n" +
			"from jiekscjl@gangkjy "+
			"where  to_char(jiekscjl.riq,'yyyy-mm-dd')='"+getRiq()+"' and diancxxb_id= "+visit.getDiancxxb_id()+" and leix='数量'";
			ResultSet rs6=con.getResultSet(sql);
			if(rs6.next()){//如果已经存在日志更新否则插入
				sql="update jiekscjl@gangkjy\n" +
				"set zhuangt=1\n" + 
				"where  to_char(jiekscjl.riq,'yyyy-mm-dd')='"+getRiq()+"' and diancxxb_id= "+visit.getDiancxxb_id()+" and leix='数量'";
				con.getUpdate(sql);
			}else{
				sql="insert into jiekscjl@gangkjy\n" +
				"   (id, riq, diancxxb_id, shangcsj, gengxsj, caozy, zhuangt, leix)\n" + 
				" values\n" + 
				"   (xl_xul_id.nextval@gangkjy,to_date('"+getRiq()+"','yyyy-mm-dd'), "+visit.getDiancxxb_id()+", sysdate, sysdate, '"+visit.getRenymc()+"', 1, '数量')\n" ; 
				con.getInsert(sql);
			}
			//客户端日志
			sql="update fahb\n" +
			"set fahb.shangc=1\n" + 
			"where   to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'\n" + 
			"and fahb.shangc=0"+
			"  and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq) ";
			con.getInsert(sql);
			setMsg("上传数据成功！");
		}else{
			setMsg("插入数量时发生错误！");
			return;
		}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	
	private boolean _QuxshClick = false;
	public void QuxButton(IRequestCycle cycle) {
		_QuxshClick = true;
	}
	
	private void Quxsh() {//取消
		//1,按行取消，fahb.id与fahbtmp.lieid对应
		//2,判断fahbtmp.xiugzt的状态,如果0,直接删除,否则置成-1作废
		//3,写服务端日志,当天工作未完;写本地日志,fahb置成未上传状态
		//4,提示取消成功
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		JDBCcon con = new JDBCcon();
	try{
			if(rsl.next()){//选择一行
				String id=rsl.getString("ID");
				String sql="select xiugzt\n" +
				"from fahbtmp@gangkjy\n" + 
				"where xiugzt>=0 and fahbtmp.lieid="+id;
				ResultSet rs=con.getResultSet(sql);
				if(rs.next()){//服务器端有该数据
					if(rs.getInt("xiugzt")==0){//没有经过处理
						sql="delete from fahbtmp@gangkjy where xiugzt=0 and lieid="+id;
						con.getDelete(sql);
					}else{//已经使用
						sql="update fahbtmp@gangkjy\n" +
						"set xiugzt=-1\n" + 
						"where xiugzt>0 and lieid="+id;
						con.getUpdate(sql);
					}
					//3,写日志
					sql="update jiekscjl@gangkjy\n" +
					"set zhuangt=0\n" + 
					"where  to_char(jiekscjl.riq,'yyyy-mm-dd')='"+getRiq()+"' and diancxxb_id= "+visit.getDiancxxb_id()+" and leix='数量'";
					con.getUpdate(sql);
					//3,未上传状态
					sql="update fahb\n" +
					"set shangc=0\n" + 
					"where id="+id;
					con.getUpdate(sql);
					setMsg("成功取消数据！");
				}else{
					setMsg("服务器端无此数据，程序错误，请与厂商联系！");
					return;
				}
			}
		}catch(Exception e){
			setMsg("取消时出现未知异常，请与厂商联系！");
			return;
		}finally{
			con.Close();
		}
	}
	
	public void submit(IRequestCycle cycle) {
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
		}
		if (_QuxshClick) {
			_QuxshClick = false;
			Quxsh();
		}
	}
	
	private void initGrid() {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select fahb.id,gongysb.mingc,meikxxb.mingc mkmc,pinzb.mingc pinz,jihkjb.mingc jhkj,yunsfsb.mingc ysfs,fz.mingc fzmc,dz.mingc dzmc,fahb.ches,fahb.laimsl\n" +
		"from fahb,gongysb,meikxxb,chezxxb fz,chezxxb dz,jihkjb,pinzb,yunsfsb\n" + 
		"where fahb.gongysb_id=gongysb.id and fahb.meikxxb_id=meikxxb.id\n" + 
		"and fahb.faz_id=fz.id and fahb.daoz_id=dz.id and fahb.jihkjb_id=jihkjb.id\n" + 
		" and fahb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=fahb.fahrq and jiesrq>=fahb.fahrq)"+
		"and fahb.pinzb_id=pinzb.id and fahb.shangc="+getLeixSelectValue().getId()+" and fahb.yunsfsb_id =yunsfsb.id and to_char(fahb.daohrq,'yyyy-mm-dd')='"+getRiq()+"'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHeader("标识");
		egu.getColumn("id").setWidth(150);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("mingc").setHeader("供应商名称");
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("mingc").setEditor(null);
		
		egu.getColumn("mkmc").setHeader("煤矿名称");
		egu.getColumn("mkmc").setWidth(150);
		egu.getColumn("mkmc").setEditor(null);
		
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(70);
		egu.getColumn("pinz").setEditor(null);
		
		egu.getColumn("jhkj").setHeader("计划口径");
		egu.getColumn("jhkj").setWidth(70);
		egu.getColumn("jhkj").setEditor(null);
		
		egu.getColumn("ysfs").setHeader("运输方式");
		egu.getColumn("ysfs").setWidth(70);
		egu.getColumn("ysfs").setEditor(null);
		
		egu.getColumn("fzmc").setHeader("发站");
		egu.getColumn("fzmc").setWidth(70);
		egu.getColumn("fzmc").setEditor(null);
		
		egu.getColumn("dzmc").setHeader("到站");
		egu.getColumn("dzmc").setWidth(70);
		egu.getColumn("dzmc").setEditor(null);
		
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("ches").setEditor(null);
		
		egu.getColumn("laimsl").setHeader("来煤量");
		egu.getColumn("laimsl").setWidth(100);
		egu.getColumn("laimsl").setEditor(null);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		//设置每页显示行数
		egu.addPaging(25);
		
		egu.addTbarText("到货日期：");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("状态:");
		ComboBox comb2=new ComboBox();
		comb2.setId("zhuangt");
		comb2.setWidth(100);
	
		comb2.setTransform("zhuangtSelect");
		comb2.setLazyRender(true);//动态绑定weizSelect
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("zhuangt.on('select',function(){document.forms[0].submit()});");
		if(getLeixSelectValue().getId()==0){//为提交
			egu.addToolbarButton("上传",GridButton.ButtonType_SaveAll, "ShangcButton", null);
		}else{
			egu.addToolbarButton("取消",GridButton.ButtonType_SubmitSel, "QuxButton", null);
		}
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
//	 类型
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "未上传"));
		list.add(new IDropDownBean(1,"已上传"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));
		return;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if(getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			((Visit) getPage().getVisit())
			.setProSelectionModel3(null);
		}
		initGrid();
	} 
}