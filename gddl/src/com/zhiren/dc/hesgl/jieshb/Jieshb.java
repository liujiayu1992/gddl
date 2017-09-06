package com.zhiren.dc.hesgl.jieshb;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.dc.jilgl.Jilcz;

public class Jieshb extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String _newjiesbh="";
	
	public String getNewjiesbh() {
		return _newjiesbh;
	}

	public void setNewjiesbh(String _newjiesbh) {
		this._newjiesbh = _newjiesbh;
	}

	// 绑定日期
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString3() == null 
				|| ((Visit) this.getPage().getVisit()).getString3().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString3(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setRiq1(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString3() != null 
				&& !((Visit) this.getPage().getVisit()).getString3().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString3(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString4() == null 
				|| ((Visit) this.getPage().getVisit()).getString4().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString4(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString4() != null 
				&& !((Visit) this.getPage().getVisit()).getString4().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString4(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}

//	日期类型选择
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
	}
	
//	厂别别下拉框取值
	public String getChangb() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString2(changb);
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
    private void Refurbish() {
            //为  "刷新"  按钮添加处理程序
    	getSelectData();
    }
//
	private boolean _MergerChick = false;

	public void MergerButton(IRequestCycle cycle) {
		_MergerChick = true;
	}
	
	private boolean _UpdateChick = false;

	public void UpdateButton(IRequestCycle cycle) {
		_UpdateChick = true;
	}
	
	private boolean _SplitChick = false;
	
	public void SplitButton(IRequestCycle cycle) {
		_SplitChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
		
		if (_MergerChick) {
			_MergerChick = false;
			Merger();
		}
		
		if(_SplitChick){
			_SplitChick = false;
			Split();
		}
		
		if(_UpdateChick){
			_UpdateChick= false;
			Update();
		}
	}
	
	private void Update(){
//		更新合并后的父结算单编号
		
		JDBCcon con = new JDBCcon();
		StringBuffer sb=new StringBuffer("begin ");;
		
		if(Jiesdcz.checkbh("jiesb","jiesyfb",this.getNewjiesbh(),
				((Visit) this.getPage().getVisit()).getLong1(), 
				((Visit) this.getPage().getVisit()).getLong2())){
//			检查编号重复，false 不重复，true 重复
			sb.append(" update jiesb set bianm='").append(this.getNewjiesbh())
				.append("'").append(" where id=")
				.append(((Visit) this.getPage().getVisit()).getLong1()).append(";\n");
			
			sb.append(" update jiesyfb set bianm='").append(this.getNewjiesbh())
				.append("'").append(" where id=")
				.append(((Visit) this.getPage().getVisit()).getLong2()).append(";\n");
			
//			将新结算编号制空
			this.setNewjiesbh("");
			
		}else{
			
			this.setMsg("结算编号重复!");
		}
		
		sb.append("end;");
		
		if(sb.length()>13){
			
			if(con.getUpdate(sb.toString())>=0){
				
//				将新结算编号制空
				this.setNewjiesbh("");
				this.setMsg("结算编号更新成功!");
			}else{
				
				this.setMsg("结算编号更新失败!");
			}
		}
		sb=null;
		con.Close();
	}
	
	private void Merger(){
//		合并结算单
//		逻辑：1、判断结算单类型
//				如果是两票：要在jiesb和jiesyfb中都要加入汇总信息，更新这两个表
//			 	如果是煤款：要在jiesb中加入汇总信息，并更新jiesb的fuid
//				如果是运费：要在jiesyfb中加入汇总信息，并更新jiesyfb的fuid
		ExtGridUtil egu=this.getExtGrid();
    	ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
    	String str_jieslx="";
    	String str_gongy_id="";
    	String str_hetb_id="";
    	String str_mkid="";
    	String str_yfid="";
    	String str_Jisbh="";
    	long diancxxb_id=0;
    	JDBCcon con=new JDBCcon();
    	
    	if(((Visit) this.getPage().getVisit()).isFencb()){
    		
    		diancxxb_id=this.getFencbValue().getId();
    	}else{
    		
    		diancxxb_id=((Visit) this.getPage().getVisit()).getDiancxxb_id();
    	}
    	
    	while(mdrsl.next()){
    		
    		str_jieslx=mdrsl.getString("JIESLX");
    		str_gongy_id=mdrsl.getString("GONGYS_ID");
    		str_hetb_id=mdrsl.getString("HETB_ID");
    		str_mkid+=mdrsl.getString("MKID")+",";
    		str_yfid+=mdrsl.getString("YFID")+",";
    	}
    	
    	StringBuffer sb=new StringBuffer("begin	\n");
    	
//    	取到要操作表的id
    	str_mkid=str_mkid.substring(0,str_mkid.lastIndexOf(","));
    	str_yfid=str_yfid.substring(0,str_yfid.lastIndexOf(","));
    	
    	str_Jisbh=Jiesdcz.getJiesbh(String.valueOf(diancxxb_id), "");
    	
    	if(str_jieslx.equals(Locale.liangpjs_feiylbb)){
//    		两票结算
//    		得到插入jiesb的sql
    		sb.append(getInsertJiesbSql(str_mkid,diancxxb_id,str_gongy_id,str_hetb_id,str_Jisbh));
    		sb.append(getInsertJiesyfbSql(str_yfid,diancxxb_id,str_gongy_id,str_hetb_id,str_Jisbh));
    		
    	}else if(str_jieslx.equals(Locale.meikjs_feiylbb)){
    		
//    		煤款结算
    		sb.append(getInsertJiesbSql(str_mkid,diancxxb_id,str_gongy_id,str_hetb_id,str_Jisbh));
    		
    	}else if(str_jieslx.equals(Locale.guotyf_feiylbb)){
    		
//    		运费结算
    		sb.append(getInsertJiesyfbSql(str_yfid,diancxxb_id,str_gongy_id,str_hetb_id,str_Jisbh));
    	}
    	
    	sb.append("end;");
    	
    	if(sb.length()>13){
    		
    		if(con.getUpdate(sb.toString())>=0){
//    			如果合并成功就将新结算编号存入变量中，在getselectdate方法中进行打印。
    			this.setNewjiesbh(str_Jisbh);
    		}else{
    			
    			this.setMsg("合并失败！");
    		}
    	}else{
    		
    		this.setMsg("合并未成功！");
    	}
    	
    	con.Close();
	}
	
	private StringBuffer getInsertJiesyfbSql(String str_yfid, long diancxxb_id, 
			String gongys_id, String hetb_id, String jiesbh) {
		// TODO 自动生成方法存根
//		生成运费结算insert语句
		JDBCcon con=new JDBCcon();
		StringBuffer sb=new StringBuffer("");
		try{
			String hansdjblxsw="2";
			long lngDiancjsmkb_id=0;
//			从结算设置中取出含税单价保留小数位
			String JiesszArray[][]=null;
        	JiesszArray=Jiesdcz.getJiessz_items(diancxxb_id,Long.parseLong(gongys_id),Long.parseLong(hetb_id));
			
        	if(JiesszArray!=null){
        		
        		for(int i=0;i<JiesszArray.length;i++){
            		
            		if(JiesszArray[i][0].equals(Locale.yunfhsdjblxsw_jies)){
    					
            			hansdjblxsw=JiesszArray[i][1];
    				}
            	}
        	}
        	
        	if(((Visit) getPage().getVisit()).getLong1()>0){
        		
        		lngDiancjsmkb_id=((Visit) getPage().getVisit()).getLong1();
        	}
        	
        	String str_Jiesyfb_id="0";
        		
			String sql=
				"select max(jy.gongysmc) as gongysmc,max(jy.yunsfsb_id) as yunsfsb_id,max(jy.yunj) as yunj,\n" +
				"       sum(jy.yingd) as yingd,sum(jy.kuid) as kuid,max(faz) as faz,to_char(min(jy.fahksrq),'yyyy-MM-dd') as fahksrq,\n" + 
				"       to_char(max(jy.fahjzrq),'yyyy-MM-dd') as fahjzrq,max(jy.meiz) as meiz,max(jy.daibch) as daibch,max(jy.yuanshr) as yuanshr,\n" + 
				"       max(jy.xianshr) as xianshr,to_char(min(jy.yansksrq),'yyyy-MM-dd') as yansksrq,to_char(max(jy.yansjzrq),'yyyy-MM-dd') as yansjzrq,\n" + 
				"       max(jy.yansbh) as yansbh,max(jy.shoukdw) as shoukdw,max(jy.kaihyh) as kaihyh,max(jy.zhangh) as zhangh,\n" + 
				"       max(jy.fapbh) as fapbh,max(jy.fukfs) as fukfs,max(jy.duifdd) as duifdd,sum(ches) as ches,sum(jy.jiessl) as jiessl,\n" + 
				"       sum(jy.guohl) as guohl,sum(jy.yuns) as yuns,sum(jy.koud) as koud,sum(jy.jiesslcy) as jiesslcy,\n" + 
				"       sum(jy.guotyf) as guotyf,sum(jy.guotzf) as guotzf,sum(jy.kuangqyf) as kuangqyf,sum(jy.kuangqzf) as kuangqzf,\n" + 
				"       sum(jy.jiskc) as jiskc,round_new(sum(decode(jy.jiessl,0,0,jy.jiessl*jy.hansdj))/sum(decode(jy.jiessl,0,1,jy.jiessl)),"+hansdjblxsw+") as hansdj,\n"+
				"		sum(jy.bukyf) as bukyf,sum(jy.hansyf) as hansyf,\n" + 
				"       sum(jy.buhsyf) as buhsyf,sum(jy.shuik) as shuik,\n" + 
				"       round_new(sum(decode(jy.jiessl,0,0,jy.jiessl*jy.shuil))/sum(decode(jy.jiessl,0,1,jy.jiessl)),5) as shuil,\n" + 
				"       round_new(sum(decode(jy.jiessl,0,0,jy.jiessl*jy.buhsdj))/sum(decode(jy.jiessl,0,1,jy.jiessl)),2) as buhsdj,\n" + 
				"       max(jy.jieslx) as jieslx,to_char(sysdate,'yyyy-MM-dd') as jiesrq,null as ruzrq,\n" + 
				"       max(jy.hetb_id) as hetb_id,max(jy.liucztb_id) as liucztb_id,max(jy.liucgzid) as liucgzid,\n" + 
				"       max(jy.ranlbmjbr) as ranlbmjbr,to_char(max(ranlbmjbrq),'yyyy-MM-dd') as ranlbmjbrq,\n" + 
				"       max(beiz) as beiz,sum(jy.guotyfjf) as guotyfjf,sum(jy.guotzfjf) as guotzfjf,sum(jy.gongfsl) as gongfsl,\n" + 
				"       sum(jy.yanssl) as yanssl,sum(jy.yingk) as yingk,max(jy.gongysb_id) as gongysb_id,sum(ditzf) as ditzf,\n" + 
				"       max(meikxxb_id) as meikxxb_id,sum(jy.dityf) as dityf,max(jy.meikdwmc) as meikdwmc,sum(nvl(KUIDJFYF,0)) as KUIDJFYF,sum(nvl(KUIDJFZF,0)) as KUIDJFZF \n" + 
				"       from jiesyfb jy where jy.id in ("+str_yfid+")";
			
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				str_Jiesyfb_id=MainGlobal.getNewID(diancxxb_id);
				((Visit) getPage().getVisit()).setLong2(Long.parseLong(str_Jiesyfb_id));
				
				sb.append("insert into jiesyfb(id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, \n"
						+"meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, \n"
						+"fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, guotyf, guotzf, kuangqyf, kuangqzf, \n"
						+"jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, hetb_id, \n"
						+"liucztb_id, liucgzid, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, beiz, guotyfjf, guotzfjf, gongfsl, \n"
						+"yanssl, yingk, diancxxb_id, bianm, gongysb_id, ditzf, meikxxb_id, dityf, meikdwmc, fuid, kuidjfyf, kuidjfzf)\n")
				  .append(" values \n")	
				  .append("("+str_Jiesyfb_id+",'"+rs.getString("gongysmc")+"',"+rs.getString("yunsfsb_id")+",'"+rs.getString("yunj")+"',"
						  +rs.getString("yingd")+","+rs.getString("kuid")+",'"+rs.getString("faz")+"',to_date('"+rs.getString("fahksrq")+"','yyyy-MM-dd'),"
						  +"to_date('"+rs.getString("fahjzrq")+"','yyyy-MM-dd'),'"+rs.getString("meiz")+"','"+rs.getString("daibch")+"',"
						  +"'"+rs.getString("yuanshr")+"','"+rs.getString("xianshr")+"',to_date('"+rs.getString("yansksrq")+"','yyyy-MM-dd'),"
						  +"to_date('"+rs.getString("yansjzrq")+"','yyyy-MM-dd'),'"+rs.getString("yansbh")+"','"+rs.getString("shoukdw")+"',"
						  +"'"+rs.getString("kaihyh")+"','"+rs.getString("zhangh")+"','"+rs.getString("fapbh")+"','"+rs.getString("fukfs")+"',"
						  +"'"+rs.getString("duifdd")+"',"+rs.getString("ches")+","+rs.getString("jiessl")+","+rs.getString("guohl")+","
						  +""+rs.getString("yuns")+","+rs.getString("koud")+","+rs.getString("jiesslcy")+","+rs.getString("guotyf")+","
						  +rs.getString("guotzf")+","+rs.getString("kuangqyf")+","+rs.getString("kuangqzf")+","+rs.getString("jiskc")+","
						  +rs.getString("hansdj")+","+rs.getString("bukyf")+","+rs.getString("hansyf")+","+rs.getString("buhsyf")+","
						  +rs.getString("shuik")+","+rs.getString("shuil")+","
						  +rs.getString("buhsdj")+","+rs.getString("jieslx")+",to_date('"+rs.getString("jiesrq")+"','yyyy-MM-dd'),"
						  +rs.getString("hetb_id")+","+rs.getString("liucztb_id")+","+rs.getString("liucgzid")+","+lngDiancjsmkb_id+",'"
						  +rs.getString("ranlbmjbr")+"',to_date('"+rs.getString("ranlbmjbrq")+"','yyyy-MM-dd'),'"+rs.getString("beiz")+"',"
						  +rs.getDouble("guotyfjf")+","+rs.getDouble("guotzfjf")+","+rs.getString("gongfsl")+","+rs.getString("yanssl")+","
						  +rs.getString("yingk")+","+diancxxb_id+",'"+jiesbh+"',"+rs.getString("gongysb_id")+","
						  +rs.getString("ditzf")+","+rs.getString("meikxxb_id")+","+rs.getString("dityf")+",'"+rs.getString("meikdwmc")+"',"
						  +"0,"+rs.getDouble("KUIDJFYF")+","+rs.getDouble("KUIDJFZF")+");\n");
				
				
				if(sb.length()>0){
//					构造jieszbsjb的insert语句
					sql="select zhibb_id,\n" +
						"       min(hetbz) as hetbz,\n" + 
						"       decode(zbb.bianm,\n" + 
						"              '"+Locale.jiessl_zhibb+"',\n" + 
						"              sum(gongf),\n" + 
						"              '"+Locale.Shul_zhibb+"',\n" + 
						"              sum(gongf),\n" + 
						"              '"+Locale.Qnetar_zhibb+"',\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.gongf)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        0),\n" + 
						"              '"+Locale.Mt_zhibb+"',\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.gongf)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        1),\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.gongf)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        2)) as gongf,\n" + 
						"       decode(zbb.bianm,\n" + 
						"              '"+Locale.jiessl_zhibb+"',\n" + 
						"              sum(changf),\n" + 
						"              '"+Locale.Shul_zhibb+"',\n" + 
						"              sum(changf),\n" + 
						"              '"+Locale.Qnetar_zhibb+"',\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.changf)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        0),\n" + 
						"              '"+Locale.Mt_zhibb+"',\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.changf)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        1),\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.changf)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        2)) as changf,\n" + 
						"\n" + 
						"       decode(zbb.bianm,\n" + 
						"              '"+Locale.jiessl_zhibb+"',\n" + 
						"              sum(jies),\n" + 
						"              '"+Locale.Shul_zhibb+"',\n" + 
						"              sum(jies),\n" + 
						"              '"+Locale.Qnetar_zhibb+"',\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.jies)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        0),\n" + 
						"              '"+Locale.Mt_zhibb+"',\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.jies)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        1),\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.jies)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        2)) as jies,\n" + 
						"       decode(zbb.bianm,\n" + 
						"              '"+Locale.jiessl_zhibb+"',\n" + 
						"              sum(yingk),\n" + 
						"              '"+Locale.Qnetar_zhibb+"',\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.yingk)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        0),\n" + 
						"              '"+Locale.Mt_zhibb+"',\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.yingk)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        1),\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.yingk)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        2)\n" + 
						"                        ) as yingk,\n" + 
						"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.zhejbz)) /\n" + 
						"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
						"                        2) as zhejbz,\n" + 
						"       sum(zhejje) as zhejje,\n" + 
						"       1 as zhuangt,\n" + 
						"       0 as yansbhb_id\n" + 
						"  from jiesyfb j, jieszbsjb zb,zhibb zbb\n" + 
						" where j.id = zb.jiesdid\n" + 
						"   and zb.zhibb_id = zbb.id\n" + 
						"   and j.id in ("+str_yfid+")\n" + 
						" group by zhibb_id,zbb.bianm";

					rs=con.getResultSet(sql);
					while(rs.next()){
						
						sb.append(" insert into jieszbsjb(id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id) \n")
							.append(" values	\n")
							.append("(getnewid("+diancxxb_id+"), "+str_Jiesyfb_id+", "+rs.getLong("zhibb_id")+", '"+rs.getString("hetbz")+"', "+rs.getDouble("gongf")+", ")
							.append(""+rs.getDouble("changf")+", "+rs.getDouble("jies")+", "+rs.getDouble("yingk")+", "+rs.getDouble("zhejbz")+", "+rs.getDouble("zhejje")+", ")
							.append(""+rs.getLong("zhuangt")+", "+rs.getLong("yansbhb_id")+");	\n");	
					}
					
					sb.append("update jiesyfb set fuid=").append(str_Jiesyfb_id).append(" where id in (").append(str_yfid).append("); \n");
				}
			}
			rs.close();
		}catch(SQLException s){
			this.setMsg("SQL错误");
			s.printStackTrace();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return sb;
	}

	private StringBuffer getInsertJiesbSql(String str_mkid,long diancxxb_id,
			String gongys_id,String hetb_id,String jiesbh) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		StringBuffer sb=new StringBuffer("");
		try{
			String hansdjblxsw="2";
//			从结算设置中取出含税单价保留小数位
			String yunfhsdjblxsw="2";	//从结算设置中取出运费含税单价保留小数位
			String JiesszArray[][]=null;
        	JiesszArray=Jiesdcz.getJiessz_items(diancxxb_id,Long.parseLong(gongys_id),Long.parseLong(hetb_id));
			
        	if(JiesszArray!=null){
        		
        		for(int i=0;i<JiesszArray.length;i++){
            		
            		if(JiesszArray[i][0].equals(Locale.meikhsdjblxsw_jies)){
    					
            			hansdjblxsw=JiesszArray[i][1];
    				}else if(JiesszArray[i][0].equals(Locale.yunfhsdjblxsw_jies)){
    					
    					yunfhsdjblxsw=JiesszArray[i][1];
    				}
            	}
        	}
        	
        	String str_Jiesb_id="0";
        		
			String sql=
				"select max(diancxxb_id) as diancxxb_id,'' as bianm,max(gongysb_id) as gongysb_id,\n" +
				"       max(gongysmc) as gongysmc,max(yunsfsb_id) as yunsfsb_id,max(yunj) as yunj,\n" + 
				"       sum(yingd) as yingd,sum(kuid) as kuid,max(faz) as faz,\n" + 
				"       to_char(min(fahksrq),'yyyy-MM-dd') as fahksrq,\n" + 
				"       to_char(max(fahjzrq),'yyyy-MM-dd') as fahjzrq,\n" + 
				"       max(meiz) as meiz,max(daibch) as daibch,max(yuanshr) as yuanshr,\n" + 
				"       max(xianshr) as xianshr,\n" + 
				"       to_char(min(yansksrq),'yyyy-MM-dd') as yansksrq,\n" + 
				"       to_char(max(yansjzrq),'yyyy-MM-dd') as yansjzrq,\n" + 
				"       max(yansbh) as yansbh,max(shoukdw) as shoukdw,max(kaihyh) as kaihyh,\n" + 
				"       max(zhangh) as zhangh,'' as fapbh,max(fukfs) as fukfs,max(duifdd) as duifdd,\n" + 
				"       sum(ches) as ches,sum(jiessl) as jiessl,sum(guohl) as guohl,sum(yuns) as yuns,\n" + 
				"       sum(koud) as koud,sum(jiesslcy) as jiesslcy,\n" + 
				"       round_new(decode(sum(jiessl),0,0,sum(hansmk)/sum(jiessl)),"+hansdjblxsw+") as hansdj,\n" + 
				"       sum(bukmk) as bukmk,sum(hansmk) as hansmk,sum(buhsmk) as buhsmk,sum(meikje) as meikje,\n" + 
				"       sum(shuik) as shuik,\n" + 
				"       round_new(sum(decode(hansmk,0,0,hansmk*shuil))/sum(decode(hansmk,0,1,hansmk)),5) as shuil,\n" + 
				"       round_new(decode(sum(jiessl),0,0,sum(buhsmk)/sum(jiessl)),7) as buhsdj,\n" + 

				"		round_new(sum(decode(jiessl,0,0,fengsjj*jiessl))/sum(decode(jiessl,0,1,jiessl)),"+hansdjblxsw+") as fengsjj,\n" +
				"       round_new(sum(decode(jiessl,0,0,jiajqdj*jiessl))/sum(decode(jiessl,0,1,jiessl)),"+hansdjblxsw+") as jiajqdj,\n" + 
				"       max(jijlx) as jijlx,sum(nvl(yufkje,0)) as yufkje,sum(nvl(kuidjfyf,0)) as kuidjfyf,\n" + 
				"       sum(nvl(kuidjfzf,0)) as kuidjfzf,sum(nvl(chaokdl,0)) as chaokdl,max(nvl(chaokdlx,'')) as chaokdlx,\n" +
				
				"       max(jieslx) as jieslx,to_char(sysdate,'yyyy-MM-dd') as jiesrq,\n" + 
				"       max(hetb_id) as hetb_id,max(liucztb_id) as liucztb_id,max(liucgzid) as liucgzid,\n" + 
				"       max(ranlbmjbr) as ranlbmjbr,to_char(max(ranlbmjbrq),'yyyy-MM-dd') as ranlbmjbrq,\n" + 
				"       max(beiz) as beiz,\n" + 
				"       max(jihkjb_id) as jihkjb_id,max(meikxxb_id) as meikxxb_id,max(meikdwmc) as meikdwmc,\n" + 
				"       round_new(sum(decode(jiessl,0,0,jiesrl*jiessl))/sum(decode(jiessl,0,1,jiessl)),0) as jiesrl,\n" + 
				"       round_new(sum(decode(jiessl,0,0,jieslf*jiessl))/sum(decode(jiessl,0,1,jiessl)),2) as jieslf,\n" + 
				"       round_new(sum(decode(jiessl,0,0,jiessl*getjiesdzb('jiesb',jiesb.id,'Qnetar','changf')))\n" + 
				"       /sum(decode(jiessl,0,1,jiessl)),0) as jiesrcrl, \n" +
				"       round_new(sum(decode(yunfjsl,0,0,yunfjsl*Yunfhsdj))\n" + 
				"       /sum(decode(yunfjsl,0,1,yunfjsl)),"+yunfhsdjblxsw+") as Yunfhsdj,sum(nvl(hansyf,0)) as hansyf,sum(nvl(buhsyf,0)) as  buhsyf,\n" +
				"		sum(nvl(yunfjsl,0)) as yunfjsl \n"	+
				"       from jiesb \n" + 
				"       where jiesb.id in ("+str_mkid+")";
			
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				str_Jiesb_id=MainGlobal.getNewID(diancxxb_id);
				((Visit) getPage().getVisit()).setLong1(Long.parseLong(str_Jiesb_id));
				
				sb.append("insert into jiesb(id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh,")
				  .append(" fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, hetb_id, liucztb_id, liucgzid, ranlbmjbr, ranlbmjbrq, beiz,")
				  .append(" jihkjb_id, meikxxb_id, meikdwmc, jiesrl, jieslf, jiesrcrl, fuid, fengsjj, jiajqdj, jijlx, yufkje, kuidjfyf, kuidjfzf, chaokdl, chaokdlx, Yunfhsdj, hansyf, buhsyf, yunfjsl)\n")
				  .append(" values \n")	
				  .append("("+str_Jiesb_id+", "+rs.getLong("diancxxb_id")+", '"+jiesbh+"', "+rs.getLong("gongysb_id")+", '"+rs.getString("gongysmc")+"', "+rs.getLong("yunsfsb_id")+", '"+rs.getString("yunj")+"', "+rs.getDouble("yingd")+", "+rs.getDouble("kuid")+", '"+rs.getString("faz")+"', to_date('"+rs.getString("fahksrq")+"','yyyy-MM-dd'), to_date('"+rs.getString("fahjzrq")+"','yyyy-MM-dd'), '"+rs.getString("meiz")+"', '"+rs.getString("daibch")+"', '"+rs.getString("yuanshr")+"', '"+rs.getString("xianshr")+"',")
				  .append("to_date('"+rs.getString("yansksrq")+"','yyyy-MM-dd'), to_date('"+rs.getString("yansjzrq")+"','yyyy-MM-dd'), '"+rs.getString("yansbh")+"', '"+rs.getString("shoukdw")+"', '"+rs.getString("kaihyh")+"', '"+rs.getString("zhangh")+"', '"+rs.getString("fapbh")+"', '"+rs.getString("fukfs")+"', '"+Jiesdcz.nvlStr(rs.getString("duifdd"))+"', "+rs.getDouble("ches")+", "+rs.getDouble("jiessl")+", "+rs.getDouble("guohl")+", "+rs.getDouble("yuns")+", "+rs.getDouble("koud")+", "+rs.getDouble("jiesslcy")+", "+rs.getDouble("hansdj")+", "+rs.getDouble("bukmk")+",")
				  .append(""+rs.getDouble("hansmk")+", "+rs.getDouble("buhsmk")+", "+rs.getDouble("meikje")+", "+rs.getDouble("shuik")+", "+rs.getDouble("shuil")+", "+rs.getDouble("buhsdj")+", "+rs.getLong("jieslx")+", to_date('"+rs.getString("jiesrq")+"','yyyy-MM-dd'), "+rs.getString("hetb_id")+", "+rs.getLong("liucztb_id")+", "+rs.getLong("liucgzid")+", '"+rs.getString("ranlbmjbr")+"', to_date('"+rs.getString("ranlbmjbrq")+"','yyyy-MM-dd'), '"+rs.getString("beiz")+"', "+rs.getLong("jihkjb_id")+", "+rs.getLong("meikxxb_id")+", '"+rs.getString("meikdwmc")+"', "+rs.getString("jiesrl")+",")
				  .append(""+rs.getDouble("jieslf")+", "+rs.getDouble("jiesrcrl")+", 0, "+rs.getDouble("fengsjj")+", "+rs.getDouble("jiajqdj")+", "+rs.getDouble("jijlx")+", "+rs.getDouble("yufkje")+", "+rs.getDouble("kuidjfyf")+", "+rs.getDouble("kuidjfzf")+", "+rs.getDouble("chaokdl")+", '"+Jiesdcz.nvlStr(rs.getString("chaokdlx"))+"', "+rs.getString("Yunfhsdj")+", "+rs.getString("hansyf")+", "+rs.getString("buhsyf")+", "+rs.getString("yunfjsl")+"); \n");
			}
			
			if(sb.length()>0){
//				构造jieszbsjb的insert语句
				sql=
					"select zhibb_id,\n" +
					"       min(hetbz) as hetbz,\n" + 
					"       decode(zbb.bianm,\n" + 
					"              '"+Locale.jiessl_zhibb+"',\n" + 
					"              sum(gongf),\n" + 
					"              '"+Locale.Shul_zhibb+"',\n" + 
					"              sum(gongf),\n" + 
					"              '"+Locale.Qnetar_zhibb+"',\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.gongf)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        0),\n" + 
					"              '"+Locale.Mt_zhibb+"',\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.gongf)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        1),\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.gongf)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        2)) as gongf,\n" + 
					"       decode(zbb.bianm,\n" + 
					"              '"+Locale.jiessl_zhibb+"',\n" + 
					"              sum(changf),\n" + 
					"              '"+Locale.Shul_zhibb+"',\n" + 
					"              sum(changf),\n" + 
					"              '"+Locale.Qnetar_zhibb+"',\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.changf)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        0),\n" + 
					"              '"+Locale.Mt_zhibb+"',\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.changf)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        1),\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.changf)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        2)) as changf,\n" + 
					"\n" + 
					"       decode(zbb.bianm,\n" + 
					"              '"+Locale.jiessl_zhibb+"',\n" + 
					"              sum(jies),\n" + 
					"              '"+Locale.Shul_zhibb+"',\n" + 
					"              sum(jies),\n" + 
					"              '"+Locale.Qnetar_zhibb+"',\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.jies)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        0),\n" + 
					"              '"+Locale.Mt_zhibb+"',\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.jies)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        1),\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.jies)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        2)) as jies,\n" + 
					"       decode(zbb.bianm,\n" + 
					"              '"+Locale.jiessl_zhibb+"',\n" + 
					"              sum(yingk),\n" + 
					"              '"+Locale.Qnetar_zhibb+"',\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.yingk)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        0),\n" + 
					"              '"+Locale.Mt_zhibb+"',\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.yingk)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        1),\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.yingk)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        2)\n" + 
					"                        ) as yingk,\n" + 
					"              round_new(sum(decode(j.jiessl, 0, 0, j.jiessl * zb.zhejbz)) /\n" + 
					"                        sum(decode(j.jiessl, 0, 0, jiessl)),\n" + 
					"                        2) as zhejbz,\n" + 
					"       sum(zhejje) as zhejje,\n" + 
					"       1 as zhuangt,\n" + 
					"       0 as yansbhb_id\n" + 
					"  from jiesb j, jieszbsjb zb,zhibb zbb\n" + 
					" where j.id = zb.jiesdid\n" + 
					"   and zb.zhibb_id = zbb.id\n" + 
					"   and j.id in ("+str_mkid+")\n" + 
					" group by zhibb_id,zbb.bianm";

				rs=con.getResultSet(sql);
				while(rs.next()){
					
					sb.append(" insert into jieszbsjb(id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id) \n")
						.append(" values	\n")
						.append("(getnewid("+diancxxb_id+"), "+str_Jiesb_id+", "+rs.getLong("zhibb_id")+", '"+rs.getString("hetbz")+"', "+rs.getDouble("gongf")+", ")
						.append(""+rs.getDouble("changf")+", "+rs.getDouble("jies")+", "+rs.getDouble("yingk")+", "+rs.getDouble("zhejbz")+", "+rs.getDouble("zhejje")+", ")
						.append(""+rs.getLong("zhuangt")+", "+rs.getLong("yansbhb_id")+");	\n");	
				}
				
				sb.append("update jiesb set fuid=").append(str_Jiesb_id)
					.append(" where id in (").append(str_mkid).append(");\n");
			}
			
			rs.close();
		}catch(SQLException s){
			this.setMsg("SQL错误");
			s.printStackTrace();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return sb;
	}

	private void Split(){
//		拆分结算单
		JDBCcon con=new JDBCcon();
		String str_mkid="";
		String str_yfid="";
		String str_jieslx="";
		ExtGridUtil egu=this.getExtGrid();
    	ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
    	while(mdrsl.next()){
    		
    		str_jieslx=mdrsl.getString("JIESLX");
    		str_mkid+=mdrsl.getString("MKID")+",";
    		str_yfid+=mdrsl.getString("YFID")+",";
    	}
    	
    	str_mkid=str_mkid.substring(0,str_mkid.lastIndexOf(","));
    	str_yfid=str_yfid.substring(0,str_yfid.lastIndexOf(","));
    	
    	StringBuffer sb=new StringBuffer("begin \n");
    	
    	if(str_jieslx.equals(Locale.liangpjs_feiylbb)){
//    		两票结算
    		sb.append(" delete from jiesb where id="+str_mkid+"; \n");
    		sb.append(" update jiesb set fuid=0,yufkje=0 where fuid="+str_mkid+"; \n");
    		sb.append(" delete from jiesyfb where id="+str_yfid+"; \n");
    		sb.append(" update jiesyfb set fuid=0 where fuid="+str_yfid+"; \n");
    		
    	}else if(str_jieslx.equals(Locale.meikjs_feiylbb)){
    		
//    		煤款结算
    		sb.append(" delete from jiesb where id="+str_mkid+"; \n");
    		sb.append(" update jiesb set fuid=0,yufkje=0 where fuid="+str_mkid+"; \n");
    		
    	}else if(str_jieslx.equals(Locale.guotyf_feiylbb)){
    		
//    		运费结算
    		sb.append(" delete from jiesyfb where id="+str_yfid+"; \n");
    		sb.append(" update jiesyfb set fuid=0 where fuid="+str_yfid+"; \n");
    	}
    	
    	sb.append("end;");
    	
    	if(sb.length()>13){
    		
    		if(con.getUpdate(sb.toString())>=0){
//    			如果拆分成功就将新结算编号制空。
    			this.setNewjiesbh("");
    			this.setMsg("拆分成功！");
    		}else {
    			
    			this.setMsg("拆分失败！");
    		}
    	}else{
    		
    		this.setMsg("拆分未成功！");
    	}
    	
    	con.Close();
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
//		 ********************工具栏************************************************
        String diancxxb_id_cdt_Mk="";//电厂条件（煤款）
        String diancxxb_id_cdt_Yf="";//电厂条件（运费）
        String gongys_cdt_Mk="";//供应商条件（煤款）
        String gongys_cdt_Yf="";//供应商条件（运费）
        String jieslx_cdt_Mk="";//结算类型条件（煤款）
        String jieslx_cdt_Yf="";//结算类型条件（运费）
        
        long diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
        
		if(((Visit) getPage().getVisit()).isFencb()){
//			分厂别
			if(this.getFencbValue().getId()==((Visit) getPage().getVisit()).getDiancxxb_id()){
//				总厂
				diancxxb_id_cdt_Mk=" and j.diancxxb_id in (select id from diancxxb where fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+") \n";
				diancxxb_id_cdt_Yf=" and jy.diancxxb_id in (select id from diancxxb where fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+") \n";
			}else{
				
				diancxxb_id_cdt_Mk=" and j.diancxxb_id="+this.getFencbValue().getId()+"\n";
				diancxxb_id_cdt_Yf=" and jy.diancxxb_id="+this.getFencbValue().getId()+"\n";
				diancxxb_id=this.getFencbValue().getId();
			}
		}
		
		if(!(getTreeid()==null||getTreeid().equals("0"))){
			
			gongys_cdt_Mk=" and (j.meikxxb_id="+getTreeid()+" or j.gongysb_id="+getTreeid()+")	\n";
			gongys_cdt_Yf=" and (jy.meikxxb_id="+getTreeid()+" or jy.gongysb_id="+getTreeid()+")	\n";
		}
		
		if(this.getJieslxValue().getId()>-1){
			
			jieslx_cdt_Mk=" and j.jieslx="+this.getJieslxValue().getId();
			jieslx_cdt_Yf=" and jy.jieslx="+this.getJieslxValue().getId();
		}
		
        String sql = 
        		 "select mkid,yfid,decode(jiesrq,null,'合计',jiesrq) as jiesrq,gongysmc,gongys_id,meikdwmc,jiesdbh,hetbh,hetb_id, \n" +
        		 "		jiesdw,shoukdw,decode(mkid,null,'',sum(jiessl)) as jiessl, \n"	+
        		 " 		decode(mkid,null,'',sum(meikje)) as meikje,decode(mkid,null,'',sum(yunfje)) as yunfje, \n" +
        		 "		jieslx,leib \n" +
        		 " 		from \n" +
        		 "(select\n" + 
        		 "       nvl(j.id,0) as mkid,nvl(jy.id,0) as yfid,to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq,\n" + 
        		 "       gys.mingc as gongysmc,gys.id as gongys_id,mk.mingc as meikdwmc,j.bianm as jiesdbh,\n" + 
        		 " 		 ht.hetbh as hetbh,j.hetb_id,dc.mingc as jiesdw,j.shoukdw,max(j.jiessl) as jiessl,\n" + 
        		 "       max(j.hansmk) as meikje,\n" + 
        		 "       decode(nvl(j.jieslx,0),1,max(nvl(jy.hansyf,0)),0) as yunfje,\n" + 
        		 "       decode(nvl(j.jieslx,0),1,'"+Locale.liangpjs_feiylbb+"',2,'"+Locale.meikjs_feiylbb+"',3,'"+Locale.guotyf_feiylbb+"') as jieslx,\n" + 
        		 "       decode(GetZijsdid(j.id),null,'单笔结算单','多笔结算单') as leib\n" + 
        		 "       from jiesb j,jiesyfb jy,gongysb gys,meikxxb mk,hetb ht,diancxxb dc \n" + 
        		 "       where j.id=jy.diancjsmkb_id(+) and j.fuid=0 and j.ruzrq is null and j.liucztb_id=0 \n" + 
        		 " 			and j.jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n" +
        		 " 			and j.jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')\n"+
        		 "			and j.gongysb_id=gys.id and j.meikxxb_id=mk.id 	\n"+
        		 "			and j.hetb_id=ht.id and j.diancxxb_id=dc.id	\n"+
        	     "  		and (jy.diancxxb_id=dc.id or jy.diancxxb_id is null)	\n"+
        	     "			and (jy.hetb_id=ht.id or jy.hetb_id is null)	\n"+
        		 " 			and (jy.gongysb_id=gys.id or jy.gongysb_id is null)	\n"+
        		 " 			and (jy.meikxxb_id=mk.id or  jy.meikxxb_id is null)	\n"+
        		 diancxxb_id_cdt_Mk+
        		 gongys_cdt_Mk+
        		 jieslx_cdt_Mk+
        		 "       group by j.jiesrq,gys.mingc,gys.id,mk.mingc,j.bianm,j.hetb_id,ht.hetbh,dc.mingc,j.shoukdw,j.jieslx,j.id,jy.id \n" + 
        		 "union\n" + 
        		 "select\n" + 
        		 "       0 as mkid,nvl(jy.id,0) as yfid,to_char(jy.jiesrq,'yyyy-MM-dd') as jiesrq,\n" + 
        		 "       gys.mingc as gongysmc,gys.id as gongys_id,mk.mingc as meikdwmc,jy.bianm as jiesdbh,\n" +
        		 "		 ht.hetbh as hetbh,jy.hetb_id,dc.mingc as jiesdw,jy.shoukdw,max(jy.jiessl) as jiessl,\n" + 
        		 "       0 as meikje,sum(nvl(jy.hansyf,0)) as yunfje,\n" + 
        		 "       decode(nvl(jy.jieslx,0),1,'"+Locale.liangpjs_feiylbb+"',2,'"+Locale.meikjs_feiylbb+"',3,'"+Locale.guotyf_feiylbb+"') as jieslx,\n" + 
        		 "       decode(GetZijsdid(jy.id),null,'单笔结算单','多笔结算单') as leib\n" + 
        		 "       from jiesyfb jy,gongysb gys,meikxxb mk,hetys ht,diancxxb dc \n" + 
        		 "       where jy.fuid=0 and jy.ruzrq is null and jy.liucztb_id=0 \n" + 
        		 " 			and jy.jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n" +
        		 " 			and jy.jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')\n"+
        		 "			and jy.gongysb_id=gys.id and jy.meikxxb_id=mk.id 	\n"+
        		 "			and jy.hetb_id=ht.id and jy.diancxxb_id=dc.id  \n"+
        		 diancxxb_id_cdt_Yf+
        		 gongys_cdt_Yf+
        		 jieslx_cdt_Yf+
        		 "   		group by jy.jiesrq,gys.mingc,gys.id,mk.mingc,jy.bianm,ht.hetbh,jy.hetb_id,dc.mingc,jy.shoukdw,jy.jieslx,jy.id)\n" + 
        		 "	group by rollup(mkid,yfid,jiesrq,gongysmc,gongys_id,meikdwmc,jiesdbh,hetbh,hetb_id,jiesdw,shoukdw,jieslx,leib)\n" +
        		 "	having not (grouping(mkid)=0 and grouping(leib)=1)\n" +
        		 "	order by jiesrq,gongysmc,meikdwmc";

         
        ResultSetList rsl = con.getResultSetList(sql);  
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);// 设置页面的宽度,当超过这个宽度时显示滚动条
		egu.setHeight("bodyHeight");
		egu.getColumn("mkid").setHidden(true);
		egu.getColumn("yfid").setHidden(true);
		egu.getColumn("jiesrq").setHeader("结算日期");
		egu.getColumn("gongysmc").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongys_id").setHidden(true);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("jiesdbh").setHeader("结算编号");
		egu.getColumn("hetbh").setHeader("合同编号");
		egu.getColumn("hetb_id").setHidden(true);
		egu.getColumn("jiesdw").setHeader("结算单位");
		egu.getColumn("shoukdw").setHeader("收款单位");
		egu.getColumn("jiessl").setHeader("结算数量(吨)");
		egu.getColumn("meikje").setHeader("煤款金额(元)");
		egu.getColumn("yunfje").setHeader("运费金额(元)");
		egu.getColumn("jieslx").setHeader("结算类型");
		egu.getColumn("leib").setHeader("类别");
		
		// 设定列初始宽度
//		egu.getColumn("id").setWidth(80);
		egu.getColumn("jiesrq").setWidth(90);
		egu.getColumn("gongysmc").setWidth(100);
		egu.getColumn("meikdwmc").setWidth(100);
		egu.getColumn("jiesdbh").setWidth(180);
		egu.getColumn("jiessl").setWidth(90);
		egu.getColumn("meikje").setWidth(100);
		egu.getColumn("yunfje").setWidth(100);
		egu.getColumn("jieslx").setWidth(80);
		egu.getColumn("leib").setWidth(80);

		egu.setGridType(ExtGridUtil.Gridstyle_Read);// 设定grid可以编辑
		egu.addPaging(0);// 设置分页
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(3, new GridColumn(GridColumn.ColType_Check));

//		Toolbar设置
		egu.addTbarText("结算日期");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// 设置分隔符
		
		// 设置树
		egu.addTbarText(Locale.gongysb_id_fahb);
		String condition="fuid=0 and ruzrq is null and liucztb_id=0 and jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd') and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') ";
		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys_jies, diancxxb_id, getTreeid(),condition);
		setTree(etu);
		
		egu.addTbarTreeBtn("gongysTree");
		

		egu.addTbarText("-");// 设置分隔符
	
		//验收编号根据不同用户可以隐藏
		//
		egu.addTbarText("结算类型:");
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("JieslxDropDown");
		comb4.setId("Jieslx");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(80);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Jieslx.on('select',function(){document.forms[0].submit();});");
		
		// 设定工具栏下拉框自动刷新
		egu.addTbarText("-");// 设置分隔符
		egu.addToolbarItem("{"+new GridButton("刷新","function(){document.getElementById('RefurbishButton').click();}",SysConstant.Btn_Icon_Refurbish).getScript()+"}");
		egu.addTbarText("-");
		
		String Checkht="是";
		
		Checkht=MainGlobal.getXitxx_item("结算合并", "是否区分合同", String.valueOf(diancxxb_id), "是");
		
//		合并原则：1、已合并过的结算单不能合并
//				 2、不同供应商的结算单不能合并
//				 3、不同类型的结算单不能合并
//				 4、不同电厂的不能合并（一厂多制）
//				 5、同一个收款单位
//				 6、同一个合同号(可以通过在系统信息中设置取消)
		String ButtonFc=
					"var rec= gridDiv_grid.getSelectionModel().getSelections();\n" +
					"   var tmpleix='',tmpleib='',tmpgys='',tmpjiesdw='',tmpshoukdw='',tmphetbh='';\n" + 
					"   var j=0;\n" + 
					"   if(rec.length<2){\n" + 
					"\n" + 
					"     Ext.MessageBox.alert('提示信息','请选择至少两条要合并的记录!');\n" + 
					"     return;\n" + 
					"   }\n" + 
					"\n" + 
					"   for(var i=0;i<rec.length;i++){\n" + 
					"\n" + 
					"     if(rec[i].get('LEIB')!=''&&rec[i].get('JIESRQ')!='合计'){\n" + 
					"\n" + 
					"       j++;\n" + 
					"\n" + 
					"       if(rec[i].get('LEIB')!='单笔结算单'){\n" + 
					"\n" + 
					"         Ext.MessageBox.alert('提示信息','第<'+(i+1)+'>行不是单笔结算单,不能进行合并操作!');\n" + 
					"         return;\n" + 
					"       }\n" + 
					"\n" + 
					"         if(tmpleix==''&&tmpgys==''){\n" + 
					"\n" + 
					"           tmpleix=rec[i].get('JIESLX');\n" + 
					"           tmpgys=rec[i].get('GONGYSMC');\n" + 
					"           tmpjiesdw=rec[i].get('JIESDW');\n" + 
					"           tmpshoukdw=rec[i].get('SHOUKDW');\n" + 
					"           tmphetbh=rec[i].get('HETBH');\n" + 
					"         }else{\n" + 
					"\n" + 
					"           if(tmpgys!=rec[i].get('GONGYSMC')){\n" + 
					"\n" + 
					"             Ext.MessageBox.alert('提示信息','不同供应商的结算单不能合并!');\n" + 
					"             return;\n" + 
					"           }\n" + 
					"\n" + 
					"           if(tmpleix!=rec[i].get('JIESLX')){\n" + 
					"\n" + 
					"             Ext.MessageBox.alert('提示信息','不同类型的结算单不能合并!');\n" + 
					"             return;\n" + 
					"           }\n" + 
					"        if(tmpjiesdw!=rec[i].get('JIESDW')){\n" + 
					"\n" + 
					"              Ext.MessageBox.alert('提示信息','不同结算单位的结算单不能合并!');\n" + 
					"             return;\n" + 
					"        }\n" + 
					"\n" + 
					"        if(tmpshoukdw!=rec[i].get('SHOUKDW')){\n" + 
					"\n" + 
					"              Ext.MessageBox.alert('提示信息','不同收款单位结算单不能合并!');\n" + 
					"             return;\n" + 
					"        }\n" + 
					"\n"; 
					if(Checkht.equals("是")){
						
						ButtonFc+="	if(tmphetbh!=rec[i].get('HETBH')){\n" + 
						"\n" + 
						"              Ext.MessageBox.alert('提示信息','不同合同的结算单不能合并!');\n" + 
						"             return;\n" + 
						"        	}";
					}
			ButtonFc+="       }\n" + 
					"       }\n" + 
					"     }\n" + 
					"     if(j<2){\n" + 
					"\n" + 
					"       Ext.MessageBox.alert('提示信息','请选择至少两条要合并的记录!');\n" + 
					"     	return;\n" + 
					"     }";



		
        egu.addToolbarButton("合并",GridButton.ButtonType_SubmitSel_condition,"MergerButton",ButtonFc);
        
        egu.addOtherScript("function gridDiv_save(rec){	\n" +
				"	\tif(rec.get('JIESRQ')=='合计'){	\n" +
				"	\treturn 'continue';	\n" +
				"	\t}}");
        
        ButtonFc=
	        	"var rec = gridDiv_grid.getSelectionModel().getSelections();\n" +
	        	"for(var i=0;i<rec.length;i++){\n" + 
	        	"\n" + 
	        	"  if(rec[i].get('LEIB')=='单笔结算单'){\n" + 
	        	"\n" + 
	        	"    Ext.MessageBox.alert('提示信息','请选择类别为<多笔结算单>的记录进行拆分!');\n" + 
	        	"    return;\n" + 
	        	"  }\n" + 
	        	"}";

        
        egu.addToolbarButton("拆分",GridButton.ButtonType_SubmitSel_condition,"SplitButton",ButtonFc);
        
		// ---------------页面js的计算开始------------------------------------------
	/*	StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// sb.append("if (e.row==7){e.cancel=true;}");//设定第8列不可编辑,索引是从0开始的
		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");// 设定供应商列不可编辑
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");// 设定电厂信息列不可编辑
		sb.append("});");
		
		egu.addOtherScript(sb.toString());*/
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(own,row,e){ \n"
				+ " \tvar jiessl=0,meikje=0,yunfje=0;	\n"
				+ "	var rec = gridDiv_grid.getSelectionModel().getSelections();	\n"	
				+ " for(var i=0;i<rec.length;i++){ \n"
				+ " \tif(''!=rec[i].get('MKID')){ \n"
				+ "		\tjiessl+=eval(rec[i].get('JIESSL'));	\n"		
				+ "		\tmeikje+=eval(rec[i].get('MEIKJE'));	\n"
				+ "		\tyunfje+=eval(rec[i].get('YUNFJE'));	\n"		
				+ " \t}	\n"
				+ " }	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JIESSL',jiessl);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MEIKJE',meikje);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNFJE',yunfje);	\n"
				+ " });		\n");
		
		
//		当选择全选后重新计算选中项的合计值
		egu.addOtherScript("gridDiv_grid.on('click',function(){		\n"
						+ "		reCountToolbarNum(this);			\n"
						+ "});		\n");
		
		
//		重新计算选中项的合计值
		egu.addOtherScript(" function reCountToolbarNum(obj){	\n "
 				+ " \tvar rec;	\n"
 				+ " \tvar jiessl=0,meikje=0,yunfje=0;	\n"
 				+ " \trec = obj.getSelectionModel().getSelections();				\n"		
 				+ " \tfor(var i=0;i<rec.length;i++){								\n" 
 				+ " 	\tif(''!=rec[i].get('MKID')){								\n"
 				+ " 		\tjiessl+=eval(rec[i].get('JIESSL'));					\n"
				+ " 		\tmeikje+=eval(rec[i].get('MEIKJE'));					\n" 
				+ " 		\tyunfje+=eval(rec[i].get('YUNFJE'));					\n"
				+ " 	\t}															\n"
				+ " \t}																\n"
				+ " if(gridDiv_ds.getCount()>0){									\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JIESSL',jiessl);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MEIKJE',meikje);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNFJE',yunfje);	\n"
				+ "	}\n"
				+ " \t} \n");
		
		if(!this.getNewjiesbh().equals("")){
			
			egu.addOtherScript(
					"var win;\n" +
					"   if(!win){\n" + 
					"        win = new Ext.Window({\n" + 
					"            //applyTo     : 'hello-win',\n" + 
					"            title:'合并结算单新编号',\n" + 
					"            modal:true,\n" + 
					"            layout      : 'fit',\n" + 
					"            width       : 230,\n" + 
					"            height      : 95,\n" + 
					"            closeAction :'hide',\n" + 
					"            plain       : true,\n" + 
					"            items       : field = new Ext.form.Field({\n" + 
					"        		value:'"+getNewjiesbh()+"'\n" + 
					"            }),\n" + 
					"\n" + 
					"            buttons: [{\n" + 
					"                  text     : '确定',\n" + 
					"                  handler  : function(){\n" + 
					" 					 document.getElementById('NEW_JIESBH').value = field.getValue();\n" +
					"				   	 document.getElementById('UpdateButton').click();\n" + 
					"                    win.hide();\n" + 
					"                  }\n" + 
					"              },{\n" + 
					"                  text     : '取消',\n" + 
					"                  handler  : function(){\n" + 
					"						document.getElementById('NEW_JIESBH').value='';\n" +
					"						field.setValue('');\n" +
					"                    	win.hide();\n" + 
					"                  }\n" + 
					"            }]\n" + 
					"        });\n" + 
					"    }\n" + 
					"    win.show();");
		}
		
		
		// ---------------页面js计算结束--------------------------
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
			
			visit.setString1("");	//Treeid
			visit.setString2("");	//厂別
			visit.setString3("");	//riq1
			visit.setString4("");	//riq2
			visit.setLong1(0);		//合并后jiesb_id
			visit.setLong2(0);		//合并后jiesyfb_id
			
			((Visit) getPage().getVisit()).setLong1(0);		//合并后的diancjsmkb_id

			setJieslxValue(null);	//3
			setJieslxModel(null);
			getJieslxModels();		//3
			
			setFencbValue(null);	//5
			setFencbModel(null);
			getFencbModels();		//5
			
			visit.setboolean1(true);	//日期change
			visit.setboolean2(false);	//发货单位change、结算类型,运输单位
		}
		getSelectData();
	}

     //结算类型
	public boolean _Jieslxchange = false;
	public IDropDownBean getJieslxValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getJieslxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setJieslxValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getJieslxModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getJieslxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setJieslxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getJieslxModels() {
		
		String sql ="select id, mingc from feiylbb where leib<2 order by id ";
			
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql,"全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
	// 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}
	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称d
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

//厂别
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {
		
			String sql ="select id,mingc from diancxxb where id="+((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " union \n"
					+ " select id,mingc from diancxxb where fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	private String treeid;
	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {

		if(((Visit) getPage().getVisit()).getString1()==null
				||((Visit) getPage().getVisit()).getString1().equals("")){
			
			((Visit) getPage().getVisit()).setString1("0");
		}
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString1().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString1(treeid);
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂

	public String getWunScript(){
		
		return "";
	}
}