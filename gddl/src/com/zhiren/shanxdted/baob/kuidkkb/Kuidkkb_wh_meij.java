package com.zhiren.shanxdted.baob.kuidkkb;

import java.util.Date;

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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Kuidkkb_wh_meij extends BasePage implements PageValidateListener {

//	日期
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
			((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay)));
		}
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq(String riq) {
		if (((Visit) this.getPage().getVisit()).getString1() != null && !((Visit) this.getPage().getVisit()).getString1().equals(riq)) {
			((Visit) this.getPage().getVisit()).setString1(riq);
		}
	}
	//新添加的日期字段
	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString3() == null || ((Visit) this.getPage().getVisit()).getString3().equals("")) {
			((Visit) this.getPage().getVisit()).setString3(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setRiq2(String riq2) {
		if (((Visit) this.getPage().getVisit()).getString3() != null && !((Visit) this.getPage().getVisit()).getString3().equals(riq2)) {
			((Visit) this.getPage().getVisit()).setString3(riq2);
		}
	}
	//结束
	protected void initialize() {
		msg = "";
	}

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public void Save() {
		String strchange = getChange();
		JDBCcon con = new JDBCcon();
		String strId="";
		
		StringBuffer sql = new StringBuffer();
		ResultSetList rssb=getExtGrid().getDeleteResultSet(getChange());
		while(rssb.next()){
			String id=rssb.getString("id");
			if(id.equals("")||id==null){
				id="0";
			}
			sql.append("delete kuidkkb_wh where id ="+id+";\n");
			
		}
		rssb.close();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if ("".equals(mdrsl.getString("ID"))) {
//				新增id
				strId=MainGlobal.getNewID(con, getChangbValue().getId());
				sql.append("insert into kuidkkb_wh(ID,DIANCXXB_ID,MEIKXXB_ID,YUNSDWB_ID,DAOHRQ,fahrq,HETMJ,MEIJ,\n" +
						"  HETRZ,KUANGFRZ,chukrz,RUCRZ,JIESRZ,KUID,KUIDJE,beiz,stad,fahb_id,yunsyxcb) values(\n" +
							strId + ",\n" +
							"(select max(id) from diancxxb where mingc ='"+mdrsl.getString("diancxxb_id")+"')" + ",\n" +
							"(select max(id) from meikxxb where mingc ='" + mdrsl.getString("meikxxb_id") + "')" + ",\n" +
							"'"+ mdrsl.getString("yunsdwb_id2")+ "' ,\n" +
							"to_date('"+mdrsl.getString("daohrq")+"','yyyy-mm-dd') ,\n" +
							"to_date('"+mdrsl.getString("fahrq")+"','yyyy-mm-dd') ,\n" +
							""+mdrsl.getDouble("hetmj")+" ,"+mdrsl.getDouble("meij")+"  ,\n" +
							""+mdrsl.getDouble("hetrz")+" ,"+mdrsl.getDouble("kuangfrz")+" ,"+mdrsl.getDouble("chukrz")+","+mdrsl.getDouble("rucrz")+" ,\n" +
							""+mdrsl.getDouble("jiesrz")+" ,\n" +
							""+mdrsl.getDouble("kuid")+" ,"+mdrsl.getDouble("kuidje")+" ,\n" +
							"'"+mdrsl.getString("beiz")+"'," +
							""+mdrsl.getDouble("stad")+","+mdrsl.getString("fid")+","+mdrsl.getDouble("yunsyxcb")+");\n");
				
			} else {
				sql.append("update kuidkkb_wh set " +
						   "	hetmj="+mdrsl.getDouble("hetmj")+" ,\n" +
						   "	meij="+mdrsl.getDouble("meij")+" ,\n" +
						   "	hetrz="+mdrsl.getDouble("hetrz")+" ,\n" +
						   "	kuangfrz="+mdrsl.getDouble("kuangfrz")+" ,\n" +
						   "	chukrz="+mdrsl.getDouble("chukrz")+" ,\n" +
						   "	rucrz="+mdrsl.getDouble("rucrz")+" ,\n" +
						   "	stad="+mdrsl.getDouble("stad")+" ,\n" +
						   "	jiesrz="+mdrsl.getDouble("jiesrz")+" ,\n" +
						   "	kuid="+mdrsl.getDouble("kuid")+" ,\n" +
						   "	kuidje="+mdrsl.getDouble("kuidje")+" ,\n" +
						   "	yunsyxcb="+mdrsl.getDouble("yunsyxcb")+" ,\n" +
						   "	beiz='"+mdrsl.getString("beiz")+"' \n" +
						   " where id=" + mdrsl.getString("id") + ";\n");
				
			}
		}
		mdrsl.close();
		if(sql.length()>0){
			if(con.getUpdate("begin\n" + sql.toString() + "end;") >= 0){
//				删除没有关联发货的kuidkkb_wh
				con.getDelete("delete kuidkkb_wh where daohrq=date'"+this.getRiq()+"' and fahb_id not in (select id from fahb f where f.daohrq=date'"+this.getRiq()+"')");
			
				setMsg("保存成功！");
			}else {
				setMsg("保存失败！");
			}
		}else {
			setMsg("保存失败！");
		}
		con.Close();
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _CopyClick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		} else if(_RefreshChick){
			_RefreshChick=false;
			getSelectData();
		} else if (_CopyClick) {
			_CopyClick = false;
			 Copysykj();//锁定数据
			 getSelectData();
		}
	}
	
	
	public void Copysykj(){
		
		
		JDBCcon con = new JDBCcon();
		String updatesql="update kuidkkb_wh w set w.zhuangt=1 where daohrq=date'"+this.getRiq()+"'";
		int falg=con.getUpdate(updatesql);
		if(falg!=-1){
		    	setMsg("数据锁定成功！");
	     }else{
		    	setMsg("数据锁定失败！");
		 }
		con.Close();
		
	}

	public void deleteFahb_null_date(JDBCcon con ,String riq){
		//这个方法的作用是删除发货表中没有和车皮表关联的空发货的数据.
		String sql="delete fahb f where f.daohrq=date'"+riq+"' and f.id not in (select distinct fahb_id from chepb c)";
		con.getDelete(sql);
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
//		 日期条件
		String Riq = this.getRiq();
		
		deleteFahb_null_date(con,Riq);
		
		String dianctiaoj="";
		if(this.getChangbValue().getId()==300){
			dianctiaoj="  ";
		}else {
			dianctiaoj=" and f.diancxxb_id="+this.getChangbValue().getId();
		}
		
		
		
		String sql =
			"select a.id,a.fid,a.diancxxb_id,a.meikxxb_id,a.yunsdwb_id,a.yunsdwb_id2,decode(a.id,null,'','已保存') as isSave,a.fahrq,a.daohrq,\n" +
			"a.biaoz,a.jingz,a.hetmj,a.meijlx,a.jiesrzlx,a.meij,\n" + 
		    "a.hetrz,a.kuangfrz,a.chukrz,a.rucrz as rucrz,a.stad,a.jiesrz,(a.biaoz-a.jingz) as kuid,\n" + 
			"decode(a.id,null,decode(a.jiesl,0,0,round((a.biaoz-a.jingz)*a.meij/1.17/10000,2)),a.kuidje) as kuidje,\n" + 
			"--运损影响成本\n"+
			"decode(a.id,null,\n" +
			"  decode(kuidkk_meik_yunsl(a.meikxxb_id2,to_date('"+Riq+"','yyyy-mm-dd'),'yunsl'),0,0,\n" +
					"round((a.biaoz-a.jingz-a.biaoz*kuidkk_meik_yunsl(a.meikxxb_id2,to_date('"+Riq+"','yyyy-mm-dd'),'yunsl'))*a.meij/1.17/10000,3)),\n" +
			"  a.yunsyxcb) as yunsyxcb,\n"+
			"a.beiz\n"+
			"from\n" + 
			"(select kk.id, fh.id as fid,d.mingc as diancxxb_id,mk.mingc as meikxxb_id,fh.meikxxb_id as meikxxb_id2,ys.mingc as yunsdwb_id,fh.yunsdwb_id as yunsdwb_id2,fh.daohrq,fh.fahrq,\n" + 
			"decode(kk.id,null, kuidkk(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),'hetmj'),kk.hetmj) as hetmj,\n" + 
			"kuidkk_meij_leix(mk.id,to_date('"+Riq+"','yyyy-mm-dd')) as meijlx,\n" + 
			"kuidkk_jiesrz_Leix(mk.id,to_date('"+Riq+"','yyyy-mm-dd')) as jiesrzlx,\n" + 
			"kuidkk(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),'jiesl') as jiesl,\n" + 
			"decode(kk.id,null,kuidkk_meij(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),\n" + 
			"decode(kk.id,null,round(zl.qnet_ar*1000/4.1816,0),kk.rucrz),\n" + 
			"decode(kk.id,null,round(kf.qnet_ar*1000/4.1816,0),kk.kuangfrz),\n" + 
			"decode(kk.id,null,kuidkk(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),'hetrz'),kk.hetrz),fh.biaoz,fh.jingz),kk.meij) as meij,\n" + 
			"fh.biaoz,fh.jingz,kk.kuidje,kk.kuik,kk.kuikje,kk.kuidkkhjje,\n" + 
			"decode(kk.id,null,kuidkk(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),'hetrz'),kk.hetrz) as hetrz,\n" + 
			"decode(kk.id,null,round(kf.qnet_ar*1000/4.1816,0),kk.kuangfrz) as kuangfrz,\n" + 
			"decode(kk.id,null,round(kf.qnet_ar_ck*1000/4.1816,0),kk.chukrz) as chukrz,\n" + 
			"decode(kk.id,null,round(zl.qnet_ar*1000/4.1816,0),kk.rucrz)  as rucrz,\n" + 
			"decode(kk.id,null,zl.stad,kk.stad)  as stad,\n" + 
			"decode(kk.id,null,kuidkk_jiesrz(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),decode(kk.id,null,round(zl.qnet_ar*1000/4.1816,0),kk.rucrz),\n" + 
			"  decode(kk.id,null,round(kf.qnet_ar*1000/4.1816,0),kk.kuangfrz),\n" + 
			"  decode(kk.id,null,kuidkk(mk.id,to_date('"+Riq+"','yyyy-mm-dd'),'hetrz'),kk.hetrz),fh.biaoz,fh.jingz,'jiesrz'),kk.jiesrz) as jiesrz,\n" + 
			"kk.yunsyxcb,kk.beiz\n"+
			"from\n" + 
			"( select * from ( select f.* ,\n" + 
			" nvl((select (select ys.id  from yunsdwb ys where ys.id=c.yunsdwb_id )\n" + 
			"  from chepb c where c.fahb_id=f.id and rownum=1),0)  yunsdwb_id\n" + 
			"  from fahb f where f.daohrq>=to_date('"+Riq+"','yyyy-mm-dd')\n" + 
			" and  f.daohrq<=to_date('"+Riq+"','yyyy-mm-dd') "+dianctiaoj+"\n" + 
			" ))fh,zhilb zl,meikxxb mk,kuangfzlb kf,vwdianc d,kuidkkb_wh kk,yunsdwb ys\n" + 
			"where fh.zhilb_id=zl.id(+)\n" + 
			"and fh.kuangfzlb_id=kf.id(+)\n" + 
			"and fh.diancxxb_id=d.id\n" + 
			"and fh.meikxxb_id=mk.id\n" + 
			"and fh.yunsdwb_id=ys.id(+)\n" + 
			"and fh.id=kk.fahb_id(+)\n" + 
			"order by fh.yunsfsb_id,mk.mingc,ys.mingc) a";




		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("kuidkkb_wh");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		
		boolean IsShowButton=true;
		if(isNotEdit(con)){
			//数据超过设定的日期条件,不允许进行编辑,但是特殊用户可以编辑
			if(IsTeShuYongHu(con,visit.getRenymc())){
				//如果是特殊用户,超过系统设置的条件后也能进行编辑
				egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			}else{
				egu.setGridType(ExtGridUtil.Gridstyle_Read);//设定grid不可以编辑
				//数据不能编辑是隐藏删除和保存按钮
				IsShowButton=false;
				this.setMsg("页面数据已经锁定,不允许修改,如有修改,请通知系统管理员!");
			}
			
		}else{
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
		}
		
		
		//设置多选框
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// /设置显示列名称
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("fid").setHeader("fid");
		egu.getColumn("fid").setEditor(null);
		egu.getColumn("fid").setHidden(true);
		
		egu.getColumn("diancxxb_id").setHeader("电厂");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("diancxxb_id").setEditor(null);
		
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("meikxxb_id").setEditor(null);
		
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setWidth(120);
		egu.getColumn("yunsdwb_id").setEditor(null);
		
		egu.getColumn("yunsdwb_id2").setHeader("运输单位id");
		egu.getColumn("yunsdwb_id2").setHidden(true);
		egu.getColumn("yunsdwb_id2").setEditor(null);
		
		egu.getColumn("isSave").setHeader("是否保存");
		egu.getColumn("isSave").setEditor(null);
		egu.getColumn("isSave").setWidth(70);
		
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("daohrq").setWidth(90);
		
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("fahrq").setWidth(90);
		
		egu.getColumn("hetmj").setHeader("合同煤价");
		egu.getColumn("hetmj").setWidth(60);
		egu.getColumn("hetmj").setEditor(null);
		
		egu.getColumn("meijlx").setHeader("煤价类型");
		egu.getColumn("meijlx").setWidth(60);
		egu.getColumn("meijlx").setEditor(null);
		egu.getColumn("meijlx").setHidden(true);
		
		egu.getColumn("jiesrzlx").setHeader("结算热值类型");
		egu.getColumn("jiesrzlx").setWidth(60);
		egu.getColumn("jiesrzlx").setEditor(null);
		egu.getColumn("jiesrzlx").setHidden(true);
		
		egu.getColumn("meij").setHeader("煤价");
		egu.getColumn("meij").setWidth(60);
		egu.getColumn("meij").setEditor(null);
		
		
		egu.getColumn("biaoz").setHeader("矿发量");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setEditor(null);
		
		egu.getColumn("jingz").setHeader("入厂量");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(60);  
		
		
		egu.getColumn("hetrz").setHeader("合同热值");
		egu.getColumn("hetrz").setWidth(60);
		egu.getColumn("hetrz").setEditor(null);
		
		egu.getColumn("kuangfrz").setHeader("矿发热值");
		egu.getColumn("kuangfrz").setWidth(60);
		egu.getColumn("kuangfrz").setEditor(null);
		
		egu.getColumn("chukrz").setHeader("出矿热值");
		egu.getColumn("chukrz").setWidth(60);
		egu.getColumn("chukrz").setEditor(null);
		
		egu.getColumn("rucrz").setHeader("入厂热值");
		egu.getColumn("rucrz").setWidth(60);
		egu.getColumn("rucrz").setEditor(null);
		
		egu.getColumn("stad").setHeader("入厂硫分");
		egu.getColumn("stad").setWidth(60);
		egu.getColumn("stad").setEditor(null);
		
		egu.getColumn("jiesrz").setHeader("结算热值");
		egu.getColumn("jiesrz").setWidth(60);
		egu.getColumn("jiesrz").setEditor(null);
		
	
		
		egu.getColumn("kuid").setHeader("亏吨");
		egu.getColumn("kuid").setWidth(60);
		egu.getColumn("kuid").setEditor(null);
		
		egu.getColumn("kuidje").setHeader("亏吨金额");
		egu.getColumn("kuidje").setWidth(70);
		((NumberField)egu.getColumn("kuidje").editor).setDecimalPrecision(2);
		//egu.getColumn("kuidje").setEditor(null);
		
		
		egu.getColumn("yunsyxcb").setHeader("运损影响成本");
		egu.getColumn("yunsyxcb").setWidth(60);
		egu.getColumn("yunsyxcb").setEditor(null);
		
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(60);
		
		
		egu.addPaging(1000);
		
		

		// ********************工具栏************************************************

		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		//df.Binding("RIQ", "");
		df.Binding("RIQ", "forms[0]");
		egu.addToolbarItem(df.getScript());
	
		egu.addTbarText("-");
		// 设置树
		//egu.addTbarText(Locale.gongysb_id_fahb);
		String condition=" and daohrq=to_date('"+Riq+"','yyyy-MM-dd') \n " ;		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		setTree(etu);
		//egu.addTbarTreeBtn("gongysTree");
		
//		分厂别
		egu.addTbarText("-");
		egu.addTbarText("单位:");
		ComboBox comb = new ComboBox();
		comb.setTransform("ChangbDropDown");
		comb.setId("Changb");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(100);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Changb.on('select',function(){document.forms[0].submit();});");
		
		//页面js计算开始
		
		egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){ " +
					" if( e.field=='MEIJ' ||  e.field=='KUANGFRZ'|| e.field=='JIESRZ'){ " +
					"     if(e.field=='KUANGFRZ'||e.field=='JIESRZ'){\n"+
					"        //当各种热值变化时,根据热值类型,重新计算结算结算热值        \n"+
					"        if(e.record.get('JIESRZLX')==0){                                                  \n"+
					"              e.record.set('JIESRZ',eval(e.record.get('RUCRZ')||0));  \n                                               "+
					"        }else if(e.record.get('JIESRZLX')==1){\n"+
					"              e.record.set('JIESRZ',eval(e.record.get('KUANGFRZ')||0));  \n                                    "+
					"        }else if(e.record.get('JIESRZLX')==2){\n"+
					"               e.record.set('JIESRZ',Round((eval(e.record.get('KUANGFRZ')||0)*eval(e.record.get('BIAOZ')||0)+eval(e.record.get('RUCRZ')||0)*eval(e.record.get('JINGZ')||0))/(eval(e.record.get('BIAOZ')||0)+eval(e.record.get('JINGZ')||0)),0));    \n                                    "+
					"        }else if (e.record.get('JIESRZLX')==3){\n    "+
					"             e.record.set('JIESRZ',eval(e.record.get('HETRZ')||0));                        \n"+
					"        }"+
					"       \n"+
					"        //计算煤价的7种方式,这5种方式来自meijfsb表\n"+
					"        if(e.record.get('MEIJLX')==1){                                                     \n"+
					"              e.record.set('MEIJ',eval(e.record.get('HETMJ')||0));                         \n"+
					"        }else if (e.record.get('MEIJLX')==2){                                              \n"+
					"              e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-eval(e.record.get('JIESRZ')||0))*0.1,2));     \n"+
					"        }else if (e.record.get('MEIJLX')==3){                                               \n"+
					"               e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-eval(e.record.get('JIESRZ')||0))*0.08,2));     \n"+
					"        }else if (e.record.get('MEIJLX')==4){                                               \n"+
					"               e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-100-eval(e.record.get('JIESRZ')||0))*0.1,2));     \n"+
					"        }else if (e.record.get('MEIJLX')==5){                                               \n"+
					"               e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-100-eval(e.record.get('JIESRZ')||0))*0.08,2));     \n"+
					"        }else if (e.record.get('MEIJLX')==6){                                               \n"+
					"               e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-eval(e.record.get('JIESRZ')||0))*0.05,2));     \n"+
					"        }else if (e.record.get('MEIJLX')==7){                                               \n"+
					"               e.record.set('MEIJ',Round(eval(e.record.get('HETMJ')||0)-(eval(e.record.get('HETRZ')||0)-100-eval(e.record.get('JIESRZ')||0))*0.05,2));     \n"+
					"        }\n"+
					"     }\n"+
					
				
				
					"    //亏吨金额 " +"\n"+
					"      e.record.set('KUIDJE',Round(eval(e.record.get('KUID')||0)*eval(e.record.get('MEIJ')||0)/1.17/10000,2)); " +
					
					
					" }  " +
					
					
				
				" } );");
		
		
		//页面js计算结束
		
		
		

//		设置Toolbar按钮	
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("刷新",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		
		
		
		
		
		
		
		if(getShujzt(con)){
			egu.addTbarText("-");
			egu.addTbarText("当日数据已经锁定,不允许修改和删除!");
		}else{
			
			if(IsShowButton){
				egu.addTbarText("-");
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
				
				egu.addTbarText("-");
				egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
				
				egu.addTbarText("-");
				egu.addTbarText("-");
				egu.addTbarText("->");
				
				egu.addTbarText("-");
				GridButton gbt = new GridButton("锁定数据","function(){document.getElementById('CopyButton').click();}");
				gbt.setIcon(SysConstant.Btn_Icon_SelSubmit);
				egu.addTbarBtn(gbt);
			}
			
		}
		
		
		
		
		setExtGrid(egu);
		con.Close();
	}
	
	
	public boolean isNotEdit(JDBCcon cn){
		//厂里要增加限制,当然日期前7天的数据可以修改,超过7天就不允许再进行修改
		boolean booIsNotEdit=false;
		long tians=0;
		String sql="select to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date('"+this.getRiq()+"','yyyy-mm-dd') as riqicha from dual";
		ResultSetList rsl =cn.getResultSetList(sql);
		if(rsl.next()){
			tians=rsl.getLong("riqicha");
		}
		String xitszrq=MainGlobal.getXitxx_item("调运", "亏吨亏卡维护页面可修改前几天的数据", "0", "7");
		if(tians>Long.parseLong(xitszrq)){
			//当然日期天数超过系统限制的可编辑天数
			booIsNotEdit= true;
		}
		
		return booIsNotEdit;
		
	}
	
	

	public boolean IsTeShuYongHu(JDBCcon cn,String username){
		//厂里要增加限制,当然日期前7天的数据可以修改,超过7天就不允许再进行修改
		boolean booIsTeShuYongHu=false;
		
		String sql="select quanc from renyxxb where beiz='矿方化验管理员'";
		ResultSetList rsl =cn.getResultSetList(sql);
		while(rsl.next()){
			if(rsl.getString("quanc").equals(username)){
				booIsTeShuYongHu=true;
				break;
			}
		}
		return booIsTeShuYongHu;
		
	}
	
	public boolean getShujzt(JDBCcon con){
		boolean isShenh=false;
		String sql="select zhuangt from kuidkkb_wh where daohrq=date'"+this.getRiq()+"' order by zhuangt desc ";
		ResultSetList rs2=con.getResultSetList(sql);
		if(rs2.next()){
			if(rs2.getInt("zhuangt")==1){
				isShenh=true;
			}
		}
		return isShenh;
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
			init();
		}
		getSelectData();
	}
	
	private void init() {
		
		((Visit) getPage().getVisit()).setString1("");			//riq1
		((Visit) getPage().getVisit()).setString3("");			//riq2
		((Visit) getPage().getVisit()).setString2("");			//Treeid
		((Visit) getPage().getVisit()).setboolean1(false);		//厂别
		this.setChangbModel(null);		//IPropertySelectionModel1
		this.setChangbValue(null);		//IDropDownBean1
		this.getChangbModels();
		
		this.setTree(null);
		this.setTreeid("0");
	}
	
//	厂别
	public IDropDownBean getChangbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getChangbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}
	public void setChangbValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}
	public IPropertySelectionModel getChangbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getChangbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	public IPropertySelectionModel getChangbModels() {
		String sql ="select id,mingc from diancxxb d order by id";
		((Visit) getPage().getVisit())
		.setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	厂别_End
	
	public String getTreeid() {
		if(((Visit) getPage().getVisit()).getString2()==null||((Visit) getPage().getVisit()).getString2().equals("")){
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			((Visit) getPage().getVisit()).setString2(treeid);
			((Visit) getPage().getVisit()).setboolean2(true);
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
}
