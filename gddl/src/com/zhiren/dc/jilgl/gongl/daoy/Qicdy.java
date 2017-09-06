package com.zhiren.dc.jilgl.gongl.daoy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-07-28
 * 修改内容:增加保存历史皮重功能，系统取得当天该车号的皮重作为该此条记录
 * 的皮重，并且对 净重 截取后几位小数存放到 qitz 字段。
 */
/*
 * 作者:tzf
 * 时间:2009-06-27
 * 修改内容:增加  净重字段的小数位的  处理,多余  小数位值  存放到  qitz  字段
 */
/*
 * 2009-05-13
 * 王磊
 * 处理车号头为空时不能自动关联上一车信息的问题，
 * 增加扣水扣杂处理,增加系统参数控制检斤小票的模式
 */
/*
 * 2009-05-11
 * 王磊
 * 修改车号头选择可以为空
 */
public class Qicdy extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	private String jingzXt="";
	
	public String getJingzXt(){
		return jingzXt;
	}
	public void setJingzXt(String jingzXt){
		this.jingzXt=jingzXt;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		this.setJingzXt("");
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		int flag = getExtGrid().Save(getChange(), visit);
		if(flag == -1){
			setMsg("毛重保存失败");
		}else{
			setMsg(ErrorMessage.SaveSuccessMessage);	
		}
	}
	private boolean _SavePizChick = false;
	public void SavePizButton(IRequestCycle cycle) {
		_SavePizChick = true;
	}
	
	private void SavePiz() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
		String sql = "begin\n";
		while(rsl.next()){
			
			String qitzStr="";
			if(this.getJingFlag()){
				qitzStr=" ,qitz="+rsl.getString("qitz")+" ";
			}
			
			sql += "update qicdyb set cheph = '"+rsl.getString("cheph")+
			"',piz = " + rsl.getDouble("piz")
			+",qingcjjy='" + rsl.getString("qingcjjy") + "',qingch='" +
			rsl.getString("qingch") + "',qingcsj=sysdate "+qitzStr+" where id =" +
			rsl.getString("id") + ";\n";
		}
		
		if(rsl.getRows()>0){
			rsl.close();
			sql += "end;\n";
			int flag = con.getUpdate(sql);
			if(flag==-1){
				setMsg("皮重保存失败!");
			}else{
				setMsg(ErrorMessage.SaveSuccessMessage);
			}
		}
		con.Close();
		
	}
	private boolean _AutoSaveChick = false;
	public void AutoSaveButton(IRequestCycle cycle) {
		_AutoSaveChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
		if (_SavePizChick) {
			_SavePizChick = false;
			SavePiz();
			init();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
		if (_AutoSaveChick) {
			_AutoSaveChick = false;
			AutoSave();
			init();
		}
	}
	

	private float getQitz(String maoz,String piz,String zhi){
		
		float mz=Float.valueOf(maoz).floatValue();
		float pz=Float.valueOf(piz).floatValue();
		int zi=Integer.valueOf(zhi).intValue();
		
		if(zhi==null){
			return 0;
		}else{
			String s=mz-pz+"";
			
			if(s.indexOf(".")==-1){//整数，直接返回
				return 0;
			}
			
			if(s.substring(s.indexOf(".")+1).length()>zi){
				
				String tem="";
				for(int i=0;i<zi;i++){
					tem+="0";
				}
				
			  return Float.valueOf("0."+tem+s.substring(s.indexOf(".")+zi+1)).floatValue();
			}else{
				return 0;
			}
		}
		
	}
	private void AutoSave() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl=null;
		
		String jingz_sql=" select zhi  from xitxxb where mingc='搬倒车数量净重字段截取保留位数' and leib='数量' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		
		rsl=con.getResultSetList(jingz_sql);
		String zhi=null;
		
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		
		String sql="";
		String msg="";
		String[] res=this.getChange().split(";");
		
		con.setAutoCommit(false);
		for(int i=0;i<res.length;i++){
			String[] rec=res[i].split(",");
			String id=rec[0];
			String cheph=rec[1];
			String maoz=rec[2];
			String piz=null;
			String qingch=null;
			
			String tem=" select piz,qingch from qicdyb where cheph='"+cheph+"' and to_char(qingcsj,'yyyy-MM-dd')='"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"' order by qingcsj asc ";
			
			 rsl=con.getResultSetList(tem);
			
			if(rsl.next()){
				piz = rsl.getString("piz");
				qingch=rsl.getString("qingch");
			}
			
			
			
			if(piz!=null){//今天有已经检斤信息
				
		     sql=" update qicdyb set cheph='"+cheph+"', piz="+piz+",qingcsj=sysdate," +
		     		"qingcjjy='"+visit.getRenymc()+"',\n" +
					"qingch='" +qingch+"',"+
					"qitz="+getQitz(maoz,piz,zhi)+" where id="+id+"";
		     
		     con.getUpdate(sql);
				
			}else{
				msg+="车皮 "+cheph+" 今天无检斤信息<br>";
			}
		}
	
		con.commit();
		this.setMsg(msg);
		
		con.Close();
	}
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择一行数据进行查看！");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		if(rsl.getRows()!=1) {
			setMsg("请选择一车进行打印！");
			return;
		}
		if(rsl.next()) {
			setChepid(rsl.getString("id"));
			this.setDiancxxb_id(rsl.getString("dc_id"));
		}
		cycle.activate("Qicjjd");
	}
	public void setChepid(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
	private void setDiancxxb_id(String id){
		
		((Visit)this.getPage().getVisit()).setString13(id);
		
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 
			"select 0 id, "+visit.getDiancxxb_id()+" diancxxb_id, '' cheph,\n" +
			"nvl(max(m1.mingc),'') yuanmc_id, nvl(max(m2.mingc),'') xiemc_id,\n" + 
			"0 maoz,'' zhongch, nvl('"+visit.getRenymc()+"','') zhongcjjy, '' beiz\n" + 
			"from (select * from qicdyb order by id desc )q,meicb m1,meicb m2\n" + 
			"where q.yuanmc_id = m1.id(+) and q.xiemc_id = m2.id(+)\n" + 
			"and rownum = 1 and q.diancxxb_id = " + visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.tableName = "qicdyb";
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight(88);
		egu.addPaging(0);

		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		
		egu.getColumn("yuanmc_id").setHeader("原煤场");
		egu.getColumn("yuanmc_id").setWidth(80);
		egu.getColumn("xiemc_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("xiemc_id").setWidth(80);
//		if(visit.isFencb()) {
//			ComboBox dc= new ComboBox();
//			egu.getColumn("diancxxb_id").setEditor(dc);
//			dc.setEditable(true);
//			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
//			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
//			egu.getColumn("diancxxb_id").setWidth(70);
//		}else {
//			egu.getColumn("diancxxb_id").setHidden(true);
//			egu.getColumn("diancxxb_id").setEditor(null);
//		}
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("maoz").editor.setAllowBlank(false);
		sql = "select * from shuzhlfwb where leib ='数量' and mingc = '汽车衡毛重' and diancxxb_id = " + visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			egu.getColumn("maoz").editor.setMinValue(rsl.getString("xiax"));
			egu.getColumn("maoz").editor.setMaxValue(rsl.getString("shangx"));
		}
		rsl.close();
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongch").setHeader(Locale.zhongchh_chepb);
		egu.getColumn("zhongch").setWidth(70);
		egu.getColumn("zhongch").setEditor(null);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(100);
//		原煤场
		ComboBox cmc = new ComboBox();
		egu.getColumn("yuanmc_id").setEditor(cmc);
		cmc.setEditable(true);
		String cmcSql = "select id, mingc from meicb";
		egu.getColumn("yuanmc_id").setComboEditor(egu.gridId,new IDropDownModel(cmcSql));
//		卸煤场
		ComboBox cxmc = new ComboBox();
		egu.getColumn("xiemc_id").setEditor(cxmc);
		cmc.setEditable(true);
		egu.getColumn("xiemc_id").setComboEditor(egu.gridId,new IDropDownModel(cmcSql));
//		输入栏中的保存按钮
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		单击行选择保存数据
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(){Mode='nosel';DataIndex='MAOZ';});");
//		毛重不可编辑
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){ "
				+"if(e.field == 'MAOZ'){e.cancel=true;}});\n");
		setExtGrid(egu);

		
		//-----------------------------
		String jingz_sql=" select zhi  from xitxxb where mingc='搬倒车数量净重字段截取保留位数' and leib='数量' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl_jz=con.getResultSetList(jingz_sql);
		
		String baol_cu="-1";
		boolean jingz_flag=false;
		String jingz_cons=" (q.maoz-q.piz) as jingz, q.qitz as qitz, ";//净重字段 用到的 计算公式
		if(rsl_jz.next()){//判断系统净重  字段 模式   
			baol_cu=rsl_jz.getString("zhi");
			jingz_flag=true;
			jingz_cons=" trunc((q.maoz-q.piz),"+baol_cu+") as jingz, q.qitz as qitz, ";
		}
		this.setJingzXt(" jingzXt="+baol_cu+";");
		
		this.setJingFlag(jingz_flag);
		//--------------------------------------
		
		sql = 
			"select q.id, q.diancxxb_id, q.cheph, m1.mingc yuanmc_id,\n" +
			"m2.mingc xiemc_id, q.maoz, q.zhongch, q.piz,"+jingz_cons+" nvl('"+visit.getRenymc()+"','') qingcjjy,q.qingch, q.beiz\n" + 
			"from qicdyb q,meicb m1,meicb m2\n" + 
			"where q.yuanmc_id = m1.id(+) and q.xiemc_id = m2.id(+)\n" + 
			"and q.diancxxb_id = "+visit.getDiancxxb_id()+" and q.qingcsj is null";

		rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl);
		//设置页面宽度
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		egu1.setHeight("bodyHeight-206");
		egu1.addPaging(12);
		egu1.getColumn("diancxxb_id").setHidden(true);
		egu1.getColumn("diancxxb_id").setEditor(null);
		egu1.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu1.getColumn("cheph").setWidth(60);
		egu1.getColumn("yuanmc_id").setHeader("原煤场");
		egu1.getColumn("yuanmc_id").setWidth(80);
		egu1.getColumn("yuanmc_id").setEditor(null);
		egu1.getColumn("xiemc_id").setHeader(Locale.meicb_id_chepb);
		egu1.getColumn("xiemc_id").setWidth(80);
		egu1.getColumn("xiemc_id").setEditor(null);
		egu1.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu1.getColumn("maoz").setWidth(50);
		egu1.getColumn("maoz").setEditor(null);
		egu1.getColumn("piz").setHeader(Locale.piz_chepb);
		egu1.getColumn("piz").setWidth(50);
		egu1.getColumn("piz").editor.setAllowBlank(false);
		sql = "select * from shuzhlfwb where leib ='数量' and mingc = '汽车衡皮重' and diancxxb_id = " + visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			egu1.getColumn("piz").editor.setMinValue(rsl.getString("xiax"));
			egu1.getColumn("piz").editor.setMaxValue(rsl.getString("shangx"));
		}
		rsl.close();
		egu1.getColumn("zhongch").setHeader(Locale.zhongchh_chepb);
		egu1.getColumn("zhongch").setWidth(70);
		egu1.getColumn("zhongch").setEditor(null);
		egu1.getColumn("qingcjjy").setHeader(Locale.qingcjjy_chepb);
		egu1.getColumn("qingcjjy").setWidth(80);
		egu1.getColumn("qingcjjy").setEditor(null);
		egu1.getColumn("qingch").setHeader(Locale.qingchh_chepb);
		egu1.getColumn("qingch").setWidth(70);
		egu1.getColumn("qingch").setEditor(null);
		egu1.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu1.getColumn("beiz").setWidth(100);
		egu1.getColumn("beiz").setEditor(null);
		
		
		if(jingz_flag){
			egu1.getColumn("jingz").setHeader("净重");
			egu1.getColumn("jingz").update=false;
			egu1.getColumn("jingz").setWidth(50);
			
			egu1.getColumn("qitz").setHidden(true);
			
			
			
			egu1.addOtherScript("gridDivPiz_grid.addListener('afteredit',function(e){ if(e.field=='PIZ'){ var rec=e.record;" 
					+" if(jingzXt<0){ if(DataIndex=='PIZ'){ var jin_va=(  parseFloat(rec.get('MAOZ'))-parseFloat(rec.get('PIZ')) )+'';"
					+" rec.set('JINGZ',jin_va);} \n"
					+" }else{ \n"
					+" if(DataIndex=='PIZ'){ var jin_va=(  parseFloat(rec.get('MAOZ'))-parseFloat(rec.get('PIZ'))  )+'';"
					+" var  koud_va=\"0\";"
					+"  var s=\"0.\";\n"
					+" var km=\"\";\n"
					+" jin_va=jin_va.substring(0,jin_va.lastIndexOf(\".\"))+jin_va.substr(jin_va.lastIndexOf(\".\"),6);\n"
					+" if(jin_va.lastIndexOf(\".\")>=0){ \n"
//					+" alert(jin_va.substring(jin_va.lastIndexOf(\".\")+1));"
					
					+" koud_va=\"0.\";"
					+" if( parseFloat(rec.get('MAOZ')) <  parseFloat(rec.get('PIZ'))){ \n"
					+" koud_va=\"-0.\"; \n"
					+" s=\"-0.\";\n"
					+" km=\"-\";\n"
					+" } \n"
					
					+" if(jin_va.substring(jin_va.lastIndexOf(\".\")+1).length>jingzXt){"
					
					+" for(var i=0;i<jingzXt;i++){ \n"
					+" koud_va+=\"0\"; \n"
					+" if(i!=jingzXt-1) \n"
					+" s+=\"0\";\n"
					+" } \n"
					
					+" var num_tem=Math.ceil(parseFloat(\"0.\"+jin_va.substr(jin_va.lastIndexOf(\".\")+1+jingzXt+1,2))); \n"
					+" var k_t=parseFloat(jin_va.substr(jin_va.lastIndexOf(\".\")+1+jingzXt,1))+num_tem; \n"
					
//					+" alert(num_tem); alert(k_t);"
					+" if(k_t>9){ s+=\"1\"; koud_va=\"0\"; rec.set('QITZ',\"0\");} \n"
					+" else{koud_va+=k_t;rec.set('QITZ',koud_va); } \n"
//					+" koud_va+=k_t;"
					
					//+" koud_va+=jin_va.substr(jin_va.lastIndexOf(\".\")+1+jingzXt,4); \n"
					+" rec.set('QITZ',parseFloat(koud_va)); \n"
					+" } \n"
					+" else{ s=\"0\"; } \n"
					
//					+" alert(parseFloat(jin_va.substring(0,jin_va.lastIndexOf(\".\")))+'---'+km+'---'+parseFloat(km+\"0\"+jin_va.substr(jin_va.lastIndexOf(\".\"),jingzXt+1))+'---'+parseFloat(s));"
					
					
					//+"  jin_va=jin_va.substring(0,jin_va.lastIndexOf(\".\"))+jin_va.substr(jin_va.lastIndexOf(\".\"),jingzXt+1); \n"
					+"  jin_va=parseFloat(jin_va.substring(0,jin_va.lastIndexOf(\".\")))+parseFloat(km+\"0\"+jin_va.substr(jin_va.lastIndexOf(\".\"),jingzXt+1))+parseFloat(s)+\"\"; \n"
					
//					+" alert(jin_va);"
					
					+"  if( jin_va.lastIndexOf(\".\")!=-1 && jin_va.substring(jin_va.lastIndexOf(\".\")).length>jingzXt+1){ var kl=\"0.\";for(var i=0;i<jingzXt;i++){kl+=\"0\";} jin_va=parseFloat(jin_va.substring(0,jin_va.lastIndexOf(\".\")+jingzXt+1))+Math.ceil(parseFloat(kl+jin_va.substr(jin_va.lastIndexOf(\".\")+jingzXt+1,2))); } \n"
					+"  } \n"
					+" else{rec.set('QITZ',\"0\"); } \n"
					+" rec.set('JINGZ',jin_va);  if(koud_va=='0.' || koud_va=='-0.'){koud_va=\"0\";} rec.set('QITZ',koud_va);  } \n"
					+" } \n"
					+"}});");
			
			
			
		}
		
//		输入车号可以查到模糊对应的信息。-----------------------------------------------------------
		
		egu1.addTbarText("输入车号：");
		 TextField theKey=new TextField();
		 theKey.setId("theKey");
		 System.out.println(theKey);
		 theKey.setListeners("change:function(thi,newva,oldva){ sta='';}\n");
		 egu1.addToolbarItem(theKey.getScript());
//	  这是ext中的第二个egu，其中带有gridDiv字样的变量都比第一个多Piz字样，gridDiv----gridDivPiz.
		GridButton chazhao=new GridButton("（模糊）查找/查找下一个","function(){\n"+
	               "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('提示','请输入值');return;}\n"+
	                "       var len=gridDivPiz_data.length;\n"+
	                "       var count;\n"+
	                "       if(len%"+egu1.getPagSize()+"!=0){\n"+
	                "        count=parseInt(len/"+egu1.getPagSize()+")+1;\n"+
	                "        }else{\n"+
	                "          count=len/"+egu1.getPagSize()+";\n"+
	                "        }\n"+
	                "        for(var i=0;i<count;i++){\n"+
	                "           gridDivPiz_ds.load({params:{start:i*"+egu1.getPagSize()+", limit:"+egu1.getPagSize()+"}});\n"+
	                "           var rec=gridDivPiz_ds.getRange();\n "+
	                "           for(var j=0;j<rec.length;j++){\n "+
	                "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"+
	                "                 var nw=[rec[j]]\n"+
	                "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"+
	                "                      gridDivPiz_sm.selectRecords(nw);\n"+
	                "                      sta+=rec[j].get('ID').toString()+';';\n"+
	                "                       return;\n"+
	                "                  }\n"+
	                "                \n"+
	                "               }\n"+
	                "           }\n"+
	                "        }\n"+
	                "        if(sta==''){\n"+
	                "          Ext.MessageBox.alert('提示','你要找的车号不存在');\n"+
	                "        }else{\n"+
	                "           sta='';\n"+
	                "           Ext.MessageBox.alert('提示','查找已经到结尾');\n"+
	                "         }\n"+
	                "      }\n");
	chazhao.setIcon(SysConstant.Btn_Icon_Search);
	egu1.addTbarBtn(chazhao);
	egu1.addTbarText("-");
		
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu1.addTbarBtn(refurbish);

		egu1.addToolbarButton(GridButton.ButtonType_Save, "SavePizButton");
		
		ResultSetList aut=con.getResultSetList(" select * from xitxxb  where mingc='搬倒机保存历史皮重' and zhi='是' and leib='数量' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id());
		
		if(aut.next()){
			GridButton AutoSaveButton=new GridButton("保存历史皮重","function(){" +
					"var res=gridDivPiz_sm.getSelections();" +
					"if(res==null || res.length==0){Ext.Msg.alert('提示信息','请选择记录操作!');return;}" +
					"var s='';" +
					"for(var i=0;i<res.length;i++){" +
					"s+=res[i].get('ID')+','+res[i].get('CHEPH')+','+res[i].get('MAOZ');" +
					"if(i!=res.length-1) s+=';';" +
					"}" +
					"document.all.CHANGE.value=s;" +
					"" +
					"document.all.AutoSaveButton.click();}");
			AutoSaveButton.setIcon(SysConstant.Btn_Icon_Save);
			egu1.addTbarBtn(AutoSaveButton);
		}
//		egu1.addToolbarButton("保存历史皮重",GridButton.ButtonType_Save, "AutoSaveButton", null, SysConstant.Btn_Icon_Save);

		
		//		打印按钮
		GridButton gbp = new GridButton("打印","function (){"+"document.all.ShowButton.click();"+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu1.addTbarBtn(gbp);
		egu1.addOtherScript("gridDivPiz_grid.on('rowclick',function(){Mode='sel';DataIndex='PIZ';});");
		egu1.addOtherScript("gridDivPiz_grid.on('beforeedit',function(e){ "
				+"if(e.field == 'PIZ'){e.cancel=true;}});\n");
		setPizGrid(egu1);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	
	public String getGridScriptPiz() {
		if (getExtGrid() == null) {
			return "";
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
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
	
	private void setJianjdmodel(){
		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc='汽车衡检斤单模式' and zhuangt = 1 and diancxxb_id =" + ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String model = "";
		if(rsl.next()){
			model = rsl.getString("zhi");
		}
		rsl.close();
		((Visit) this.getPage().getVisit()).setString15(model);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			visit.setboolean5(false);
			init();
		}
	} 
	
	private void setJingFlag(boolean t){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setboolean5(t);
	}
	
	private boolean getJingFlag(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getboolean5();
	}
	
	private void init() {
		setJianjdmodel();
		setExtGrid(null);
		setPizGrid(null);
		getSelectData();
	}
}