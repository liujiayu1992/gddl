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

	// ������
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

//	��������ѡ��
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
	}
	
//	�����������ȡֵ
	public String getChangb() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString2(changb);
	}
	
	// ҳ��仯��¼
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
            //Ϊ  "ˢ��"  ��ť��Ӵ������
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
//		���ºϲ���ĸ����㵥���
		
		JDBCcon con = new JDBCcon();
		StringBuffer sb=new StringBuffer("begin ");;
		
		if(Jiesdcz.checkbh("jiesb","jiesyfb",this.getNewjiesbh(),
				((Visit) this.getPage().getVisit()).getLong1(), 
				((Visit) this.getPage().getVisit()).getLong2())){
//			������ظ���false ���ظ���true �ظ�
			sb.append(" update jiesb set bianm='").append(this.getNewjiesbh())
				.append("'").append(" where id=")
				.append(((Visit) this.getPage().getVisit()).getLong1()).append(";\n");
			
			sb.append(" update jiesyfb set bianm='").append(this.getNewjiesbh())
				.append("'").append(" where id=")
				.append(((Visit) this.getPage().getVisit()).getLong2()).append(";\n");
			
//			���½������ƿ�
			this.setNewjiesbh("");
			
		}else{
			
			this.setMsg("�������ظ�!");
		}
		
		sb.append("end;");
		
		if(sb.length()>13){
			
			if(con.getUpdate(sb.toString())>=0){
				
//				���½������ƿ�
				this.setNewjiesbh("");
				this.setMsg("�����Ÿ��³ɹ�!");
			}else{
				
				this.setMsg("�����Ÿ���ʧ��!");
			}
		}
		sb=null;
		con.Close();
	}
	
	private void Merger(){
//		�ϲ����㵥
//		�߼���1���жϽ��㵥����
//				�������Ʊ��Ҫ��jiesb��jiesyfb�ж�Ҫ���������Ϣ��������������
//			 	�����ú�Ҫ��jiesb�м��������Ϣ��������jiesb��fuid
//				������˷ѣ�Ҫ��jiesyfb�м��������Ϣ��������jiesyfb��fuid
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
    	
//    	ȡ��Ҫ�������id
    	str_mkid=str_mkid.substring(0,str_mkid.lastIndexOf(","));
    	str_yfid=str_yfid.substring(0,str_yfid.lastIndexOf(","));
    	
    	str_Jisbh=Jiesdcz.getJiesbh(String.valueOf(diancxxb_id), "");
    	
    	if(str_jieslx.equals(Locale.liangpjs_feiylbb)){
//    		��Ʊ����
//    		�õ�����jiesb��sql
    		sb.append(getInsertJiesbSql(str_mkid,diancxxb_id,str_gongy_id,str_hetb_id,str_Jisbh));
    		sb.append(getInsertJiesyfbSql(str_yfid,diancxxb_id,str_gongy_id,str_hetb_id,str_Jisbh));
    		
    	}else if(str_jieslx.equals(Locale.meikjs_feiylbb)){
    		
//    		ú�����
    		sb.append(getInsertJiesbSql(str_mkid,diancxxb_id,str_gongy_id,str_hetb_id,str_Jisbh));
    		
    	}else if(str_jieslx.equals(Locale.guotyf_feiylbb)){
    		
//    		�˷ѽ���
    		sb.append(getInsertJiesyfbSql(str_yfid,diancxxb_id,str_gongy_id,str_hetb_id,str_Jisbh));
    	}
    	
    	sb.append("end;");
    	
    	if(sb.length()>13){
    		
    		if(con.getUpdate(sb.toString())>=0){
//    			����ϲ��ɹ��ͽ��½����Ŵ�������У���getselectdate�����н��д�ӡ��
    			this.setNewjiesbh(str_Jisbh);
    		}else{
    			
    			this.setMsg("�ϲ�ʧ�ܣ�");
    		}
    	}else{
    		
    		this.setMsg("�ϲ�δ�ɹ���");
    	}
    	
    	con.Close();
	}
	
	private StringBuffer getInsertJiesyfbSql(String str_yfid, long diancxxb_id, 
			String gongys_id, String hetb_id, String jiesbh) {
		// TODO �Զ����ɷ������
//		�����˷ѽ���insert���
		JDBCcon con=new JDBCcon();
		StringBuffer sb=new StringBuffer("");
		try{
			String hansdjblxsw="2";
			long lngDiancjsmkb_id=0;
//			�ӽ���������ȡ����˰���۱���С��λ
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
//					����jieszbsjb��insert���
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
			this.setMsg("SQL����");
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
		// TODO �Զ����ɷ������
		JDBCcon con=new JDBCcon();
		StringBuffer sb=new StringBuffer("");
		try{
			String hansdjblxsw="2";
//			�ӽ���������ȡ����˰���۱���С��λ
			String yunfhsdjblxsw="2";	//�ӽ���������ȡ���˷Ѻ�˰���۱���С��λ
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
//				����jieszbsjb��insert���
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
			this.setMsg("SQL����");
			s.printStackTrace();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return sb;
	}

	private void Split(){
//		��ֽ��㵥
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
//    		��Ʊ����
    		sb.append(" delete from jiesb where id="+str_mkid+"; \n");
    		sb.append(" update jiesb set fuid=0,yufkje=0 where fuid="+str_mkid+"; \n");
    		sb.append(" delete from jiesyfb where id="+str_yfid+"; \n");
    		sb.append(" update jiesyfb set fuid=0 where fuid="+str_yfid+"; \n");
    		
    	}else if(str_jieslx.equals(Locale.meikjs_feiylbb)){
    		
//    		ú�����
    		sb.append(" delete from jiesb where id="+str_mkid+"; \n");
    		sb.append(" update jiesb set fuid=0,yufkje=0 where fuid="+str_mkid+"; \n");
    		
    	}else if(str_jieslx.equals(Locale.guotyf_feiylbb)){
    		
//    		�˷ѽ���
    		sb.append(" delete from jiesyfb where id="+str_yfid+"; \n");
    		sb.append(" update jiesyfb set fuid=0 where fuid="+str_yfid+"; \n");
    	}
    	
    	sb.append("end;");
    	
    	if(sb.length()>13){
    		
    		if(con.getUpdate(sb.toString())>=0){
//    			�����ֳɹ��ͽ��½������ƿա�
    			this.setNewjiesbh("");
    			this.setMsg("��ֳɹ���");
    		}else {
    			
    			this.setMsg("���ʧ�ܣ�");
    		}
    	}else{
    		
    		this.setMsg("���δ�ɹ���");
    	}
    	
    	con.Close();
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
//		 ********************������************************************************
        String diancxxb_id_cdt_Mk="";//�糧������ú�
        String diancxxb_id_cdt_Yf="";//�糧�������˷ѣ�
        String gongys_cdt_Mk="";//��Ӧ��������ú�
        String gongys_cdt_Yf="";//��Ӧ���������˷ѣ�
        String jieslx_cdt_Mk="";//��������������ú�
        String jieslx_cdt_Yf="";//���������������˷ѣ�
        
        long diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
        
		if(((Visit) getPage().getVisit()).isFencb()){
//			�ֳ���
			if(this.getFencbValue().getId()==((Visit) getPage().getVisit()).getDiancxxb_id()){
//				�ܳ�
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
        		 "select mkid,yfid,decode(jiesrq,null,'�ϼ�',jiesrq) as jiesrq,gongysmc,gongys_id,meikdwmc,jiesdbh,hetbh,hetb_id, \n" +
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
        		 "       decode(GetZijsdid(j.id),null,'���ʽ��㵥','��ʽ��㵥') as leib\n" + 
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
        		 "       decode(GetZijsdid(jy.id),null,'���ʽ��㵥','��ʽ��㵥') as leib\n" + 
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
		egu.setWidth(Locale.Grid_DefaultWidth);// ����ҳ��Ŀ��,������������ʱ��ʾ������
		egu.setHeight("bodyHeight");
		egu.getColumn("mkid").setHidden(true);
		egu.getColumn("yfid").setHidden(true);
		egu.getColumn("jiesrq").setHeader("��������");
		egu.getColumn("gongysmc").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongys_id").setHidden(true);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("jiesdbh").setHeader("������");
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("hetb_id").setHidden(true);
		egu.getColumn("jiesdw").setHeader("���㵥λ");
		egu.getColumn("shoukdw").setHeader("�տλ");
		egu.getColumn("jiessl").setHeader("��������(��)");
		egu.getColumn("meikje").setHeader("ú����(Ԫ)");
		egu.getColumn("yunfje").setHeader("�˷ѽ��(Ԫ)");
		egu.getColumn("jieslx").setHeader("��������");
		egu.getColumn("leib").setHeader("���");
		
		// �趨�г�ʼ���
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

		egu.setGridType(ExtGridUtil.Gridstyle_Read);// �趨grid���Ա༭
		egu.addPaging(0);// ���÷�ҳ
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(3, new GridColumn(GridColumn.ColType_Check));

//		Toolbar����
		egu.addTbarText("��������");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// ���÷ָ���
		
		// ������
		egu.addTbarText(Locale.gongysb_id_fahb);
		String condition="fuid=0 and ruzrq is null and liucztb_id=0 and jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd') and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') ";
		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys_jies, diancxxb_id, getTreeid(),condition);
		setTree(etu);
		
		egu.addTbarTreeBtn("gongysTree");
		

		egu.addTbarText("-");// ���÷ָ���
	
		//���ձ�Ÿ��ݲ�ͬ�û���������
		//
		egu.addTbarText("��������:");
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("JieslxDropDown");
		comb4.setId("Jieslx");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(80);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Jieslx.on('select',function(){document.forms[0].submit();});");
		
		// �趨�������������Զ�ˢ��
		egu.addTbarText("-");// ���÷ָ���
		egu.addToolbarItem("{"+new GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}",SysConstant.Btn_Icon_Refurbish).getScript()+"}");
		egu.addTbarText("-");
		
		String Checkht="��";
		
		Checkht=MainGlobal.getXitxx_item("����ϲ�", "�Ƿ����ֺ�ͬ", String.valueOf(diancxxb_id), "��");
		
//		�ϲ�ԭ��1���Ѻϲ����Ľ��㵥���ܺϲ�
//				 2����ͬ��Ӧ�̵Ľ��㵥���ܺϲ�
//				 3����ͬ���͵Ľ��㵥���ܺϲ�
//				 4����ͬ�糧�Ĳ��ܺϲ���һ�����ƣ�
//				 5��ͬһ���տλ
//				 6��ͬһ����ͬ��(����ͨ����ϵͳ��Ϣ������ȡ��)
		String ButtonFc=
					"var rec= gridDiv_grid.getSelectionModel().getSelections();\n" +
					"   var tmpleix='',tmpleib='',tmpgys='',tmpjiesdw='',tmpshoukdw='',tmphetbh='';\n" + 
					"   var j=0;\n" + 
					"   if(rec.length<2){\n" + 
					"\n" + 
					"     Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ����������Ҫ�ϲ��ļ�¼!');\n" + 
					"     return;\n" + 
					"   }\n" + 
					"\n" + 
					"   for(var i=0;i<rec.length;i++){\n" + 
					"\n" + 
					"     if(rec[i].get('LEIB')!=''&&rec[i].get('JIESRQ')!='�ϼ�'){\n" + 
					"\n" + 
					"       j++;\n" + 
					"\n" + 
					"       if(rec[i].get('LEIB')!='���ʽ��㵥'){\n" + 
					"\n" + 
					"         Ext.MessageBox.alert('��ʾ��Ϣ','��<'+(i+1)+'>�в��ǵ��ʽ��㵥,���ܽ��кϲ�����!');\n" + 
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
					"             Ext.MessageBox.alert('��ʾ��Ϣ','��ͬ��Ӧ�̵Ľ��㵥���ܺϲ�!');\n" + 
					"             return;\n" + 
					"           }\n" + 
					"\n" + 
					"           if(tmpleix!=rec[i].get('JIESLX')){\n" + 
					"\n" + 
					"             Ext.MessageBox.alert('��ʾ��Ϣ','��ͬ���͵Ľ��㵥���ܺϲ�!');\n" + 
					"             return;\n" + 
					"           }\n" + 
					"        if(tmpjiesdw!=rec[i].get('JIESDW')){\n" + 
					"\n" + 
					"              Ext.MessageBox.alert('��ʾ��Ϣ','��ͬ���㵥λ�Ľ��㵥���ܺϲ�!');\n" + 
					"             return;\n" + 
					"        }\n" + 
					"\n" + 
					"        if(tmpshoukdw!=rec[i].get('SHOUKDW')){\n" + 
					"\n" + 
					"              Ext.MessageBox.alert('��ʾ��Ϣ','��ͬ�տλ���㵥���ܺϲ�!');\n" + 
					"             return;\n" + 
					"        }\n" + 
					"\n"; 
					if(Checkht.equals("��")){
						
						ButtonFc+="	if(tmphetbh!=rec[i].get('HETBH')){\n" + 
						"\n" + 
						"              Ext.MessageBox.alert('��ʾ��Ϣ','��ͬ��ͬ�Ľ��㵥���ܺϲ�!');\n" + 
						"             return;\n" + 
						"        	}";
					}
			ButtonFc+="       }\n" + 
					"       }\n" + 
					"     }\n" + 
					"     if(j<2){\n" + 
					"\n" + 
					"       Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ����������Ҫ�ϲ��ļ�¼!');\n" + 
					"     	return;\n" + 
					"     }";



		
        egu.addToolbarButton("�ϲ�",GridButton.ButtonType_SubmitSel_condition,"MergerButton",ButtonFc);
        
        egu.addOtherScript("function gridDiv_save(rec){	\n" +
				"	\tif(rec.get('JIESRQ')=='�ϼ�'){	\n" +
				"	\treturn 'continue';	\n" +
				"	\t}}");
        
        ButtonFc=
	        	"var rec = gridDiv_grid.getSelectionModel().getSelections();\n" +
	        	"for(var i=0;i<rec.length;i++){\n" + 
	        	"\n" + 
	        	"  if(rec[i].get('LEIB')=='���ʽ��㵥'){\n" + 
	        	"\n" + 
	        	"    Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�����Ϊ<��ʽ��㵥>�ļ�¼���в��!');\n" + 
	        	"    return;\n" + 
	        	"  }\n" + 
	        	"}";

        
        egu.addToolbarButton("���",GridButton.ButtonType_SubmitSel_condition,"SplitButton",ButtonFc);
        
		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
	/*	StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// sb.append("if (e.row==7){e.cancel=true;}");//�趨��8�в��ɱ༭,�����Ǵ�0��ʼ��
		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");// �趨��Ӧ���в��ɱ༭
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");// �趨�糧��Ϣ�в��ɱ༭
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
		
		
//		��ѡ��ȫѡ�����¼���ѡ����ĺϼ�ֵ
		egu.addOtherScript("gridDiv_grid.on('click',function(){		\n"
						+ "		reCountToolbarNum(this);			\n"
						+ "});		\n");
		
		
//		���¼���ѡ����ĺϼ�ֵ
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
					"            title:'�ϲ����㵥�±��',\n" + 
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
					"                  text     : 'ȷ��',\n" + 
					"                  handler  : function(){\n" + 
					" 					 document.getElementById('NEW_JIESBH').value = field.getValue();\n" +
					"				   	 document.getElementById('UpdateButton').click();\n" + 
					"                    win.hide();\n" + 
					"                  }\n" + 
					"              },{\n" + 
					"                  text     : 'ȡ��',\n" + 
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
		
		
		// ---------------ҳ��js�������--------------------------
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			
			visit.setString1("");	//Treeid
			visit.setString2("");	//���e
			visit.setString3("");	//riq1
			visit.setString4("");	//riq2
			visit.setLong1(0);		//�ϲ���jiesb_id
			visit.setLong2(0);		//�ϲ���jiesyfb_id
			
			((Visit) getPage().getVisit()).setLong1(0);		//�ϲ����diancjsmkb_id

			setJieslxValue(null);	//3
			setJieslxModel(null);
			getJieslxModels();		//3
			
			setFencbValue(null);	//5
			setFencbModel(null);
			getFencbModels();		//5
			
			visit.setboolean1(true);	//����change
			visit.setboolean2(false);	//������λchange����������,���䵥λ
		}
		getSelectData();
	}

     //��������
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
				.setProSelectionModel3(new IDropDownModel(sql,"ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
	// �õ���½�û����ڵ糧���߷ֹ�˾������
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}
	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����d
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

//����
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

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧

	public String getWunScript(){
		
		return "";
	}
}