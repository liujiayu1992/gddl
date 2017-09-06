package com.zhiren.dtrlgs.faygl.fayjs;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dtrlgs.faygl.fayjs.JiesdlrBean;
 
public class Shengckfjsd extends BasePage {

	private static int _editTableRow = -1;//编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	private String _msg;

	protected void initialize() {
		super.initialize();
		_msg = "";
//		_liucmc="";
	}

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value,false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	public boolean isHetbhDp() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setHetbhDp(boolean editable) {
		((Visit) getPage().getVisit()).setboolean5(editable);
	}
	
//	public boolean isLiucmcDp() {
//		return ((Visit) getPage().getVisit()).getboolean6();
//	}
//
//	public void setLiucmcDp(boolean editable) {
//		((Visit) getPage().getVisit()).setboolean6(editable);
//	}
	
//	int1 状态 判断是修改合同号还是提交进入流程	1、修改合同，2、提交进入流程
	public void setZhuangt(int value) {
		((Visit) getPage().getVisit()).setInt1(value);
	}
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}	
	
	private static Date _JiesrqsmallValue = new Date();
	private boolean _Jiesrqsmallchange = false;
	public Date getJiesrqsmall() {
		return _JiesrqsmallValue;
	}

	public void setJiesrqsmall(Date _value) {
		if (FormatDate(_JiesrqsmallValue).equals(FormatDate(_value))) {
			_Jiesrqsmallchange = false;
		} else {

			_JiesrqsmallValue = _value;
			_Jiesrqsmallchange = true;
		}

	}
	
	private static Date _JiesrqbigValue = new Date();
	private boolean _Jiesrqbigchange = false;
	public Date getJiesrqbig() {
		return _JiesrqbigValue;
	}

	public void setJiesrqbig(Date _value) {
		if (FormatDate(_JiesrqbigValue).equals(FormatDate(_value))) {
			_Jiesrqbigchange = false;
			
		} else {

			_JiesrqbigValue = _value;
			_Jiesrqbigchange = true;
		}

	}
	
private String Jiesbh(){
		
//		结算编号
		JDBCcon con=new JDBCcon();
		String strJsbh="";
		try{
	        String sYear ="";
	        String sMonth="";
	        java.util.Date datCur = new java.util.Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-01");
	        String dat = formatter.format(datCur);
	        sYear = dat.substring(0, 4);
	        sMonth = dat.substring(5, 7);
	        String sDate = sYear + sMonth;
	        int intXh=1;
	        int intBh=0;
	        String sql2="select max(bianm) as jiesbh from (select bianm from kuangfjsmkb where bianm like 'CDT-JS-DTRL-CG-"+sDate+"-%')";
//	        String sql2="select max(bianh) as jiesbh from ((select bianh from diancjsmkb where bianh like '"+sDate+"%')union(select bianh from kuangfjsyf where bianh like '"+sDate+"%'))";
	        ResultSet rsSl=con.getResultSet(sql2);
	        if (rsSl.next()){
	            strJsbh=rsSl.getString("jiesbh");               
	        }
	        if(strJsbh==null){
	            strJsbh="CDT-JS-DTRL-CG-"+sDate+"-0000";
	        }
	        intBh=Integer.parseInt(strJsbh.trim().substring(strJsbh.trim().length()-4,strJsbh.trim().length()));
	        intBh=intBh+1;
	        if(intBh<10000 && intBh>=1000){
	            strJsbh="CDT-JS-DTRL-CG-"+sDate+"-"+String.valueOf(intBh);
	        }else if (intBh<1000 && intBh>=100){
	            strJsbh="CDT-JS-DTRL-CG-"+sDate+"-0"+String.valueOf(intBh);
	        }else if(intBh>=10 && intBh<100){
	            strJsbh="CDT-JS-DTRL-CG-"+sDate+"-00"+String.valueOf(intBh);
	        }else{
	            strJsbh="CDT-JS-DTRL-CG-"+sDate+"-000"+String.valueOf(intBh);
	        }
	        rsSl.close();
	        
	        return strJsbh;
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return strJsbh;
	}

	private void Save() {

		int flag = 0;
		Visit visit = (Visit) getPage().getVisit();
	    JDBCcon con = new JDBCcon();
//	    con.setAutoCommit(false);
//	    con.rollBack();
//	    con.commit();
	    String jiesbh=((JiesdlrBean)getEditValues().get(0)).getJiesbh();
	    String newID=MainGlobal.getNewID(visit.getDiancxxb_id());
	    long gongysb_id=getProperId(getIFahdwModels(),((JiesdlrBean)getEditValues().get(0)).getFahdw());
	    String gongysmc=((JiesdlrBean)getEditValues().get(0)).getFahdw();
	    String faz=((JiesdlrBean)getEditValues().get(0)).getFaz();
	    String fahksrq=this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getFahksrq());
	    String fahjzrq=this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getFahjzrq());
	    String meiz=((JiesdlrBean)getEditValues().get(0)).getPinz();
	    String daibch=((JiesdlrBean)getEditValues().get(0)).getDaibcc();
	    String yuanshr=((JiesdlrBean)getEditValues().get(0)).getYuanshr();
	    String xianshr=((JiesdlrBean)getEditValues().get(0)).getXianshr();
	    String yansksrq=this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getYansksrq());
	    String yansjzrq=this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getYansjzrq());
	    String shoukdw=((JiesdlrBean)getEditValues().get(0)).getShoukdw();
	    String kaihyh=((JiesdlrBean)getEditValues().get(0)).getKaihyh();
	    String yansbh=((JiesdlrBean)getEditValues().get(0)).getYansbh();
	    String zhangh=((JiesdlrBean)getEditValues().get(0)).getZhangh();
	    String fapbh=((JiesdlrBean)getEditValues().get(0)).getFapbh();
	    String fukfs=((JiesdlrBean)getEditValues().get(0)).getFukfs();
	    String duifdd ="";//((JiesdlrBean)getEditValues().get(0)).getDuifdd();
	    double ches =((JiesdlrBean)getEditValues().get(0)).getChes();
	    double jiessl = ((JiesdlrBean)getEditValues().get(0)).getJiessl();
	    double guohl = ((JiesdlrBean)getEditValues().get(0)).getJingz();
	    double hansdj = ((JiesdlrBean)getEditValues().get(0)).getShulzjbz();
	    double bukmk = ((JiesdlrBean)getEditValues().get(0)).getBukyqjk();
	    double hansmk = ((JiesdlrBean)getEditValues().get(0)).getJiasje();
	    double buhsmk = ((JiesdlrBean)getEditValues().get(0)).getJiakhj();
	    double shuik = ((JiesdlrBean)getEditValues().get(0)).getJiaksk();
	    double shuil = ((JiesdlrBean)getEditValues().get(0)).getJiaksl();
	    double buhsdj = ((JiesdlrBean)getEditValues().get(0)).getBuhsdj();
	    String beiz = ((JiesdlrBean)getEditValues().get(0)).getBeiz();
	    double meikje = ((JiesdlrBean)getEditValues().get(0)).getJiakje();
	    String ranlbmjbr=((JiesdlrBean)getEditValues().get(0)).getRanlbmjbr();
	    String ranlbmjbrq=this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getRanlbmjbrq());
	    long diancid=getProperId(getXianshrModels(),((JiesdlrBean)getEditValues().get(0)).getXianshr());
	    long jieslx=getJieslxValue().getId();
	    String danjc=getDanjc();

	    StringBuffer sql = new StringBuffer();
	    try{
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((JiesdlrBean)getEditValues().get(0)).getJiesbh().equals("")){
	        	if(Fayjsdcz.checkbh_Shih(jiesbh,((JiesdlrBean)getEditValues().get(0)).getId(),((JiesdlrBean)getEditValues().get(0)).getYid())){
	        				sql.append("begin \n");
	        				String mk="insert into kuangfjsmkb (id,bianm,gongysb_id,gongysmc,faz,fahksrq,fahjzrq," +
	        						"meiz,daibch,yuanshr,xianshr,yansksrq,yansjzrq,shoukdw,kaihyh,yansbh,zhangh,fapbh,fukfs," +
	        						"duifdd,ches,jiessl,guohl,hansdj,bukmk,hansmk,buhsmk,shuik,shuil,buhsdj,beiz,meikje," +
	        						"ranlbmjbr,ranlbmjbrq,jieslx,diancxxb_id,danjc,DIANCJSMKB_ID,YEWLXB_ID,jiesrl,jieslf,jiesrq)values("+newID+",'"+jiesbh+"',"+gongysb_id+",'"+gongysmc+"','"+faz+"',"
	        						+"to_date('"+fahksrq+"','yyyy-MM-dd'),to_date('"+fahjzrq+"','yyyy-MM-dd'),'"+meiz+"','"+daibch+"',"
	        						+"'"+yuanshr+"','"+xianshr+"',to_date('"+yansksrq+"','yyyy-MM-dd'),to_date('"+yansjzrq+"','yyyy-MM-dd'),'"
	        						+shoukdw+"','"+kaihyh+"','"+yansbh+"','"+zhangh+"','"+fapbh+"','"+fukfs+"','"+duifdd+"',"
	        						+ches+","+jiessl+","+guohl+","+
	        						hansdj+","+bukmk+","+hansmk+","+buhsmk+","+shuik+","+shuil+","+buhsdj+",'"+beiz+"',"+meikje+",'"+ranlbmjbr+
	        						"',to_date('"+ranlbmjbrq+"','yyyy-MM-dd'),"+jieslx+","+diancid+","+danjc+
	        						",(select id from diancjsmkb where bianm='"+getJiesbhValue().getValue()+"'),"+getShujlyValue().getId()+"," +
	        						((JiesdlrBean)getEditValues().get(0)).getQnetar_js()+","+((JiesdlrBean)getEditValues().get(0)).getStd_js()+"," +
    								"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getJiesrq())+"','yyyy-MM-dd')); \n";
			        		
			        		String yf="insert into kuangfjsyfb (id,bianm,gongysb_id, gongysmc,faz,fahksrq,fahjzrq,meiz,daibch,yuanshr,xianshr,yansksrq,yansjzrq," +
			        				"yansbh,shoukdw,kaihyh,zhangh,fapbh,fukfs,duifdd,ches,jiessl,guohl,guotyf,guotzf,kuangqyf,kuangqzf,jiskc," +
			        				"bukyf,hansyf,buhsyf,shuik,shuil,jiesrq,beiz,jieslx,diancxxb_id,hansdj,danjc,KUANGFJSMKB_ID,YEWLXB_ID)values("+newID+",'"+jiesbh+"',"+gongysb_id+",'"+gongysmc+"','"+faz+"',"
	        						+"to_date('"+fahksrq+"','yyyy-MM-dd'),to_date('"+fahjzrq+"','yyyy-MM-dd'),'"+meiz+"','"+daibch+"',"
	        						+"'"+yuanshr+"','"+xianshr+"',to_date('"+yansksrq+"','yyyy-MM-dd'),to_date('"+yansjzrq+"','yyyy-MM-dd'),'"+
	        						yansbh+"','"+shoukdw+"','"+kaihyh+"','"+zhangh+"','"+fapbh+"','"+fukfs+"','"+duifdd+"',"
	        						+ches+","+jiessl+","+guohl+","
	        						+((JiesdlrBean)getEditValues().get(0)).getTielyf()+","+
	        						((JiesdlrBean)getEditValues().get(0)).getTielzf()+","+((JiesdlrBean)getEditValues().get(0)).getKuangqzf()+
	        						","+((JiesdlrBean)getEditValues().get(0)).getKuangqzf()+","
	        						+((double)Math.round((((JiesdlrBean)getEditValues().get(0)).getTielzf()+((JiesdlrBean)getEditValues().get(0)).getKuangqzf())*100)/100)
	        						+","+((JiesdlrBean)getEditValues().get(0)).getBukyqyzf()+","
	        						+((JiesdlrBean)getEditValues().get(0)).getYunzfhj()+","
	        						+((JiesdlrBean)getEditValues().get(0)).getBuhsyf()+","
	        						+((JiesdlrBean)getEditValues().get(0)).getYunfsk()+","
	        						+((JiesdlrBean)getEditValues().get(0)).getYunfsl()+","
	        						+"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getJiesrq())+"','yyyy-MM-dd'),"
	        						+"'"+beiz+"',"
	        						+jieslx+","+diancid+","+hansdj+","+danjc+","+newID+
	        						","+getShujlyValue().getId()+"); \n";
			        		
			        		String sqlzl="";
//			        		long dancID=((JiesdlrBean)getEditValues().get(0)).getId();
			        		sqlzl+= " insert into jieszbsjb (YANSBHB_ID,id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt) "
			    				+ " values (0,"+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))+","+newID+"," +
			    				"(select id from zhibb where leib=1 and bianm='结算数量'),'"+
			    				nvlStrNum(((JiesdlrBean)getEditValues().get(0)).getHetsl())+"',"+
			    				((JiesdlrBean)getEditValues().get(0)).getGongfsl()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getYanssl()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getJiessl()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getYingksl()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getShulzjbz()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getShulzjje()+",1);\n";
			        		
			        		sqlzl+= " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt) "
			    				+ " values ("+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))+","+newID+","+
			    				"(select id from zhibb where leib=1 and bianm='Qnetar')," +
			    				"'"+nvlStrNum(((JiesdlrBean)getEditValues().get(0)).getQnetar_ht())+"',"+
			    				((JiesdlrBean)getEditValues().get(0)).getQnetar_kf()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getQnetar_cf()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getQnetar_js()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getQnetar_yk()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getQnetar_zdj()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getQnetar_zje()+",1);\n";
			        		
			        		sqlzl += " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt) "
			    				+ " values ("+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))+","+newID+","+
			    				"(select id from zhibb where leib=1 and bianm='Std')," +
			    				"'"+nvlStrNum(((JiesdlrBean)getEditValues().get(0)).getStd_ht())+"',"+
			    				((JiesdlrBean)getEditValues().get(0)).getStd_kf()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getStd_cf()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getStd_js()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getStd_yk()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getStd_zdj()+","+
			    				((JiesdlrBean)getEditValues().get(0)).getStd_zje()+",1);\n";
			        		
			        		if(getJieslxValue().getValue().equals("两票结算")){
			        			sql.append(mk+yf+sqlzl);
			        			sql.append("update diancjsmkb set KUANGFJSMKB_ID="+newID+", danjc="+danjc+" where id="+((JiesdlrBean)getEditValues().get(0)).getId()+";\n");
			        			if(getShujlyValue().getStrId().equals("3")){ //3=直达
			        				sql.append("update fahb set jiesb_id="+newID+" where id in(select f.fahb_id from fahbtmp f where f.fayslb_id in(select id from fayslb fy where fy.xiaosjsb_id="+((JiesdlrBean)getEditValues().get(0)).getId()+"));\n");
			        				sql.append("update kuangfjsmkb set hetb_id= (select max(xiaoshetb_id) from fahbtmp " +
			        						"where fayslb_id=nvl((select max(id) from fayslb fy where fy.xiaosjsb_id="+((JiesdlrBean)getEditValues().get(0)).getId()+"),0)) where id="+newID+";\n");
			        				sql.append("update kuangfjsyfb set hetb_id=(select max(xiaoshetb_id) from fahbtmp " +
			        						"where fayslb_id=nvl((select max(id) from fayslb fy where fy.xiaosjsb_id="+((JiesdlrBean)getEditValues().get(0)).getId()+"),0))  where id="+newID+";\n");
			        			}
			        		}  		
			        		else if(getJieslxValue().getValue().equals("煤款结算")){
			        			sql.append(mk+sqlzl);
			        			sql.append("update diancjsmkb set KUANGFJSMKB_ID="+newID+", danjc="+danjc+" where id="+((JiesdlrBean)getEditValues().get(0)).getId()+";\n");
			        			if(getShujlyValue().getStrId().equals("3")){ //3=直达
			        				sql.append("update fahb set jiesb_id="+newID+" where id in(select f.fahb_id from fahbtmp f " +
			        						"where f.fayslb_id in(select id from fayslb fy where fy.xiaosjsb_id="+((JiesdlrBean)getEditValues().get(0)).getId()+"));\n");
			        				sql.append("update kuangfjsmkb set hetb_id= (select max(xiaoshetb_id) from fahbtmp " +
			        						"where fayslb_id=nvl((select max(id) from fayslb fy where fy.xiaosjsb_id="+((JiesdlrBean)getEditValues().get(0)).getId()+"),0)) where id="+newID+";\n");
			        			}
			        		}  		
			        		else{sql.append(yf+sqlzl);}  
			        		
			        		sql.append("end;");

			        		flag = con.getUpdate(sql.toString());
							if (flag == -1) {
								con.rollBack();
								con.Close();
								setMsg("结算单保存更新失败！");
								return;
							}
			        		setMsg("结算单保存更新成功！");
	        	}else{
	        		
	        		setMsg("该结算编号已被另一张结算单使用，请核对！");
	        	}
	        }
	        
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	con.Close();
	    }
	}
	

	public static String nvlStrNum(String strValue){
		if (strValue==null || strValue.equals("") || strValue.equals("null") ) {
			return "0.0";
		}
		return strValue;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}
	
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			setKUANGFJSBH(null);
			setJiesbhValue(null);
			setIJiesbhModel(null);
//			getIJiesbhModels();
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			setMsg("");
		}
	}
	
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	
	private JiesdlrBean _EditValue;
	public JiesdlrBean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(JiesdlrBean EditValue) {
		_EditValue = EditValue;
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}
	
	public String getTitle(){
		
		return "燃 料 采 购 结 算 单";
	}
	
	public String getJiesslcyText(){
		
		return Locale.jiesslcy_title;
	}
	
	public List getSelectData() {
		List _editvalues = new ArrayList();
		if(getEditValues()!=null){
			
			getEditValues().clear();
		}
		
		JDBCcon con = new JDBCcon();
		try {

			long mid = 0;
			long myid = 0;
			long mdiancxxb_id=0;
			long mgongysb_id=0;
			String mtianzdw ="";
			String mjiesbh = "";
			String mfahdw = "";
			String mmeikdw="";
			String mfaz = "";
			String mshoukdw = "";
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date myansksrq = new Date();
			Date myansjzrq = new Date();
			String mfahrq="";
			String mkaihyh = "";
			String mpinz = "";
			String myuanshr = "";
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
			
			String mQnetar_ht = "";		// 合同热量
			double mQnetar_kf = 0;		// 供方热量
			double mQnetar_cf = 0;		// 厂方热量
			double mQnetar_js = 0;		// 厂方结算
			double mQnetar_yk = 0;		// 厂方盈亏
			double mQnetar_zdj = 0;		// 折单价
			double mQnetar_zje = 0;		// 这金额
			
			String mStd_ht="";			//合同硫分
			double mStd_kf=0;			//供方热量
			double mStd_cf=0;			//厂方热量
			double mStd_js=0;			//结算热量
			double mStd_yk = 0;			// 厂方盈亏
			double mStd_zdj = 0;		// 折单价
			double mStd_zje = 0;		// 这金额
			
			String mAd_ht="";			//合同硫分
			double mAd_kf=0;			//供方热量
			double mAd_cf=0;			//厂方热量
			double mAd_js=0;			//结算热量
			double mAd_yk = 0;			// 厂方盈亏
			double mAd_zdj = 0;			// 折单价
			double mAd_zje = 0;			// 这金额
			
			String mVdaf_ht="";			//合同硫分
			double mVdaf_kf=0;			//供方热量
			double mVdaf_cf=0;			//厂方热量
			double mVdaf_js=0;			//结算热量
			double mVdaf_yk = 0;		// 厂方盈亏
			double mVdaf_zdj = 0;		// 折单价
			double mVdaf_zje = 0;		// 这金额
			
			String mMt_ht="";			//合同硫分
			double mMt_kf=0;			//供方热量
			double mMt_cf=0;			//厂方热量
			double mMt_js=0;			//结算热量
			double mMt_yk = 0;			// 厂方盈亏
			double mMt_zdj = 0;			// 折单价
			double mMt_zje = 0;			// 这金额
			
			String mQgrad_ht="";		//合同硫分
			double mQgrad_kf=0;			//供方热量
			double mQgrad_cf=0;			//厂方热量
			double mQgrad_js=0;			//结算热量
			double mQgrad_yk = 0;		// 厂方盈亏
			double mQgrad_zdj = 0;		// 折单价
			double mQgrad_zje = 0;		// 这金额
			
			String mQbad_ht="";			//合同硫分
			double mQbad_kf=0;			//供方热量
			double mQbad_cf=0;			//厂方热量
			double mQbad_js=0;			//结算热量
			double mQbad_yk = 0;		// 厂方盈亏
			double mQbad_zdj = 0;		// 折单价
			double mQbad_zje = 0;		// 这金额
			
			String mHad_ht="";			//合同硫分
			double mHad_kf=0;			//供方热量
			double mHad_cf=0;			//厂方热量
			double mHad_js=0;			//结算热量
			double mHad_yk = 0;			// 厂方盈亏
			double mHad_zdj = 0;		// 折单价
			double mHad_zje = 0;		// 这金额
			
			String mStad_ht= "";			//合同硫分
			double mStad_kf=0;			//供方热量
			double mStad_cf=0;			//厂方热量
			double mStad_js=0;			//结算热量
			double mStad_yk = 0;		// 厂方盈亏
			double mStad_zdj = 0;		// 折单价
			double mStad_zje = 0;		// 这金额
			
			String mStar_ht="";			//合同硫分
			double mStar_kf=0;			//供方热量
			double mStar_cf=0;			//厂方热量
			double mStar_js=0;			//结算热量
			double mStar_yk = 0;		// 厂方盈亏
			double mStar_zdj = 0;		// 折单价
			double mStar_zje = 0;		// 这金额
			
			String mMad_ht="";			//合同硫分
			double mMad_kf=0;			//供方热量
			double mMad_cf=0;			//厂方热量
			double mMad_js=0;			//结算热量
			double mMad_yk = 0;			// 厂方盈亏
			double mMad_zdj = 0;		// 折单价
			double mMad_zje = 0;		// 这金额
			
			String mAar_ht="";			//合同硫分
			double mAar_kf=0;			//供方热量
			double mAar_cf=0;			//厂方热量
			double mAar_js=0;			//结算热量
			double mAar_yk = 0;			// 厂方盈亏
			double mAar_zdj = 0;		// 折单价
			double mAar_zje = 0;		// 这金额
			
			String mAad_ht="";			//合同硫分
			double mAad_kf=0;			//供方热量
			double mAad_cf=0;			//厂方热量
			double mAad_js=0;			//结算热量
			double mAad_yk = 0;			// 厂方盈亏
			double mAad_zdj = 0;		// 折单价
			double mAad_zje = 0;		// 这金额
			
			String mVad_ht="";			//合同硫分
			double mVad_kf=0;			//供方热量
			double mVad_cf=0;			//厂方热量
			double mVad_js=0;			//结算热量
			double mVad_yk = 0;			// 厂方盈亏
			double mVad_zdj = 0;		// 折单价
			double mVad_zje = 0;		// 这金额
			
			String mT2_ht="";			//合同硫分
			double mT2_kf=0;			//供方热量
			double mT2_cf=0;			//厂方热量
			double mT2_js=0;			//结算热量
			double mT2_yk = 0;			// 厂方盈亏
			double mT2_zdj = 0;			// 折单价
			double mT2_zje = 0;			// 这金额
			
			String mYunju_ht="";		//合同运距
			double mYunju_kf=0;			//供方热量
			double mYunju_cf=0;			//厂方热量
			double mYunju_js=0;			//结算热量
			double mYunju_yk = 0;		// 厂方盈亏
			double mYunju_zdj = 0;		// 折单价
			double mYunju_zje = 0;		// 这金额
			
			long mhetb_id=0; 
			double mkoud_js=0;
			double mjiessl = 0;
			double myunfjsl = 0;
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.13;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mtielzf = 0;
			double mkuangqyf=0;
			double mkuangqzf=0;
			double mkuangqsk=0;
			double mkuangqjk=0;
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
			long mjieslx = 3;
			double myuns = 0;
			String mjiesslblxsw="0";
			String myunsfs = "";
			String mdiancjsbs = "";
			String mstrJieszb="";
			boolean blnHasMeik=false;
			Money mn=new Money();
			Fayjsdcz jsdcz=new Fayjsdcz();
//			double mjiesslcy=0;
//			long myunsfsb_id=0;
//			double myingd=0;
//			double mkuid=0;
//			double myunju=0;		//运距
//			double mfengsjj=0;		//分公司加价
//			double mjiajqdj=0;		//加价前单价
//			int mjijlx=0;			//基价类型
			String mMjtokcalxsclfs="";	//兆焦转大卡小数处理方式
			double mkuidjfyf_je=0;	//亏吨拒付运费金额
			double mkuidjfzf_je=0;	//亏顿拒付杂费金额
			double mchaokdl=0;   	//超亏吨量
			String mchaokdlx="";	//超亏吨类型
			
//			进行单批次结算时，要将每一个批次的结算情况保存起来，存入danpcjsmxb中，此时就产生了id
//			结算时要判断有无这个id，如果有就一定要用这个id
			long mMeikjsb_id=0;
			long mYunfjsb_id=0;
			long mJihkjb_id=0;
			String Xianszt="";

			String sql="select quanc from diancxxb where id=199";
			ResultSet rs0=con.getResultSet(sql);
			while(rs0.next()){
				mtianzdw =rs0.getString("quanc");
			}
			
			sql="select * from diancjsmkb where bianm='"+getJiesbhValue().getValue()+"' ";//and fuid=0";
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				mid = rs.getLong("id");
//				mtianzdw =rs.getString("xianshr");
				mjiesbh = rs.getString("bianm");
				mdiancjsbs=mjiesbh.substring(0,mjiesbh.indexOf("-")+1);
				mfahdw = rs.getString("gongysmc");
				mfaz =rs.getString("faz");
				mshoukdw = rs.getString("shoukdw");
				mfahksrq = rs.getDate("fahksrq");
				mfahjzrq = rs.getDate("fahjzrq");
				myansksrq = rs.getDate("yansksrq");
				myansjzrq =rs.getDate("yansjzrq");
				mkaihyh =rs.getString("kaihyh");
				mpinz = rs.getString("meiz");
				myuanshr = rs.getString("yuanshr");
				mzhangh = rs.getString("zhangh");
				mches = rs.getInt("ches");
				mxianshr =rs.getString("xianshr");
				mdaibcc = rs.getString("daibch");
				myansbh =rs.getString("yansbh");
				mduifdd = rs.getString("duifdd");
				mfukfs = rs.getString("fukfs");
				mjiessl = rs.getDouble("jiessl");
				mbuhsdj = rs.getDouble("buhsdj");
				mjiakje = rs.getDouble("meikje");
				mbukyqjk = rs.getDouble("bukmk");
				mjiakhj = rs.getDouble("buhsmk");
				mjiaksl = rs.getDouble("shuil");
				mjiaksk = rs.getDouble("shuik");
				mjiasje = rs.getDouble("hansmk");
				mjieslx = rs.getInt("jieslx");
//				mjiesslcy=rs.getDouble("jiesslcy");
				mdiancxxb_id=rs.getLong("diancxxb_id");
				mgongysb_id=rs.getLong("gongysb_id");
				mkoud_js=0.00;
				mshulzjbz=rs.getDouble("hansdj");//含税单价从煤款表中取
//				myunsfsb_id=rs.getLong("yunsfsb_id");
//				myingd=rs.getDouble("yingd");//
//				mkuid=rs.getDouble("kuid");//
//				myunju=rs.getDouble("yunj");//
				mhetb_id=rs.getLong("HETB_ID");
				blnHasMeik=true;
				mfapbh=rs.getString("fapbh");
//				mfengsjj=rs.getDouble("fengsjj");//
//				mjiajqdj=rs.getDouble("jiajqdj");//
//				mjijlx=rs.getInt("jijlx");//
//				mkuidjfyf_je=rs.getDouble("kuidjfyf");	//亏吨拒付运费金额 //
//				mkuidjfzf_je=rs.getDouble("kuidjfzf");	//亏顿拒付杂费金额 //
				mchaokdl=Math.abs(rs.getDouble("chaokdl"));		//超亏吨量 //
				mchaokdlx=Fayjsdcz.nvlStr(rs.getString("chaokdlx"));		//超亏吨类型 //
				
				sql="select jieszbsjb.*,zhibb.bianm from jieszbsjb,diancjsmkb,zhibb "
					 + " where jieszbsjb.jiesdid="+mid+" and zhibb.id=jieszbsjb.zhibb_id and zhibb.leib=1"
					 + " and diancjsmkb.bianm='"+getJiesbhValue().getValue()+"' and jieszbsjb.zhuangt=1 order by jieszbsjb.id";
				
				ResultSet rs2=con.getResultSet(sql);
					
				while(rs2.next()){
					
					if(rs2.getString("bianm").equals(Locale.jiessl_zhibb)){
						
						mhetsl = rs2.getString("hetbz");
						mgongfsl =rs2.getDouble("gongf") ;
						mshulzjbz =rs2.getDouble("zhejbz");
						myanssl = rs2.getDouble("changf");
						myingksl = rs2.getDouble("yingk");
						mshulzjje=rs2.getDouble("zhejje");
						
					}else if(rs2.getString("bianm").equals(Locale.Qnetar_zhibb)){
						Xianszt+="qnet,";
						mQnetar_ht = rs2.getString("hetbz");// 合同热量
						mQnetar_kf = rs2.getDouble("gongf");// 供方热量
						mQnetar_cf = rs2.getDouble("changf");
						mQnetar_js = rs2.getDouble("jies");// 结算热量
						mQnetar_yk = rs2.getDouble("yingk");
						mQnetar_zdj = rs2.getDouble("zhejbz");
						mQnetar_zje = rs2.getDouble("zhejje");
						
					}else if(rs2.getString("bianm").equals(Locale.Std_zhibb)){
						Xianszt+="std,";
						mStd_ht = rs2.getString("hetbz");	// 合同硫分
						mStd_kf = rs2.getDouble("gongf");
						mStd_cf =rs2.getDouble("changf");
						mStd_js = rs2.getDouble("jies");	// 结算硫分
						mStd_yk = rs2.getDouble("yingk");
						mStd_zdj = rs2.getDouble("zhejbz");
						mStd_zje = rs2.getDouble("zhejje");
						
					}else if(rs2.getString("bianm").equals(Locale.Star_zhibb)){
						
						mStar_ht = rs2.getString("hetbz");	// 合同挥发分
						mStar_kf = rs2.getDouble("gongf");
						mStar_cf = rs2.getDouble("changf");
						mStar_js = rs2.getDouble("jies");		// 结算挥发分
						mStar_yk = rs2.getDouble("yingk");
						mStar_zdj = rs2.getDouble("zhejbz");
						mStar_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Star(%)","Star_ht","Star_kf","Star_cf","Star_js","Star_yk","Star_zdj","Star_zje",
								mStar_ht,mStar_kf,mStar_cf,mStar_js,mStar_yk,mStar_zdj,mStar_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Ad_zhibb)){
						Xianszt+="ad,";
						mAd_ht = rs2.getString("hetbz");	// 合同挥发分
						mAd_kf = rs2.getDouble("gongf");
						mAd_cf = rs2.getDouble("changf");
						mAd_js = rs2.getDouble("jies");		// 结算挥发分
						mAd_yk = rs2.getDouble("yingk");
						mAd_zdj = rs2.getDouble("zhejbz");
						mAd_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Ad(%)","Ad_ht","Ad_kf","Ad_cf","Ad_js","Ad_yk","Ad_zdj","Ad_zje",
								mAd_ht,mAd_kf,mAd_cf,mAd_js,mAd_yk,mAd_zdj,mAd_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Vdaf_zhibb)){
						Xianszt+="vdaf,";
						mVdaf_ht = rs2.getString("hetbz");	// 合同挥发分
						mVdaf_kf = rs2.getDouble("gongf");
						mVdaf_cf = rs2.getDouble("changf");
						mVdaf_js = rs2.getDouble("jies");		// 结算挥发分
						mVdaf_yk = rs2.getDouble("yingk");
						mVdaf_zdj = rs2.getDouble("zhejbz");
						mVdaf_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Vdaf(%)","Vdaf_ht","Vdaf_kf","Vdaf_cf","Vdaf_js","Vdaf_yk","Vdaf_zdj","Vdaf_zje",
								mVdaf_ht,mVdaf_kf,mVdaf_cf,mVdaf_js,mVdaf_yk,mVdaf_zdj,mVdaf_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Mt_zhibb)){
						Xianszt+="mt,";
						mMt_ht = rs2.getString("hetbz");	// 合同挥发分
						mMt_kf = rs2.getDouble("gongf");
						mMt_cf = rs2.getDouble("changf");
						mMt_js = rs2.getDouble("jies");		// 结算挥发分
						mMt_yk = rs2.getDouble("yingk");
						mMt_zdj = rs2.getDouble("zhejbz");
						mMt_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Mt(%)","Mt_ht","Mt_kf","Mt_cf","Mt_js","Mt_yk","Mt_zdj","Mt_zje",
								mMt_ht,mMt_kf,mMt_cf,mMt_js,mMt_yk,mMt_zdj,mMt_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Qgrad_zhibb)){
						
						mQgrad_ht = rs2.getString("hetbz");		// 合同挥发分
						mQgrad_kf = rs2.getDouble("gongf");
						mQgrad_cf = rs2.getDouble("changf");
						mQgrad_js = rs2.getDouble("jies");		// 结算挥发分
						mQgrad_yk = rs2.getDouble("yingk");
						mQgrad_zdj = rs2.getDouble("zhejbz");
						mQgrad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Qgrad(kcal/kg)","Qgrad_ht","Qgrad_kf","Qgrad_cf","Qgrad_js","Qgrad_yk","Qgrad_zdj","Qgrad_zje",
								mQgrad_ht,MainGlobal.Mjkg_to_kcalkg(mQgrad_kf,0),MainGlobal.Mjkg_to_kcalkg(mQgrad_cf,0),
								MainGlobal.Mjkg_to_kcalkg(mQgrad_js,0),mQgrad_yk,mQgrad_zdj,mQgrad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Qbad_zhibb)){
						
						mQbad_ht = rs2.getString("hetbz");		// 合同挥发分
						mQbad_kf = rs2.getDouble("gongf");
						mQbad_cf = rs2.getDouble("changf");
						mQbad_js = rs2.getDouble("jies");		// 结算挥发分
						mQbad_yk = rs2.getDouble("yingk");
						mQbad_zdj = rs2.getDouble("zhejbz");
						mQbad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Qbad(kcal/kg)","Qbad_ht","Qbad_kf","Qbad_cf","Qbad_js","Qbad_yk","Qbad_zdj","Qbad_zje",
								mQbad_ht,MainGlobal.Mjkg_to_kcalkg(mQbad_kf,0),MainGlobal.Mjkg_to_kcalkg(mQbad_cf,0),
								MainGlobal.Mjkg_to_kcalkg(mQbad_js,0),mQbad_yk,mQbad_zdj,mQbad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Had_zhibb)){
						
						mHad_ht = rs2.getString("hetbz");		// 合同挥发分
						mHad_kf = rs2.getDouble("gongf");
						mHad_cf = rs2.getDouble("changf");
						mHad_js = rs2.getDouble("jies");		// 结算挥发分
						mHad_yk = rs2.getDouble("yingk");
						mHad_zdj = rs2.getDouble("zhejbz");
						mHad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Had(%)","Had_ht","Had_kf","Had_cf","Had_js","Had_yk","Had_zdj","Had_zje",
								mHad_ht,mHad_kf,mHad_cf,mHad_js,mHad_yk,mHad_zdj,mHad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Stad_zhibb)){
						
						mStad_ht = rs2.getString("hetbz");		// 合同挥发分
						mStad_kf = rs2.getDouble("gongf");
						mStad_cf = rs2.getDouble("changf");
						mStad_js = rs2.getDouble("jies");		// 结算挥发分
						mStad_yk = rs2.getDouble("yingk");
						mStad_zdj = rs2.getDouble("zhejbz");
						mStad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Stad(%)","Stad_ht","Stad_kf","Stad_cf","Stad_js","Stad_yk","Stad_zdj","Stad_zje",
								mStad_ht,mStad_kf,mStad_cf,mStad_js,mStad_yk,mStad_zdj,mStad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Mad_zhibb)){
						
						mMad_ht = rs2.getString("hetbz");	// 合同挥发分
						mMad_kf = rs2.getDouble("gongf");
						mMad_cf = rs2.getDouble("changf");
						mMad_js = rs2.getDouble("jies");		// 结算挥发分
						mMad_yk = rs2.getDouble("yingk");
						mMad_zdj = rs2.getDouble("zhejbz");
						mMad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Mad(%)","Mad_ht","Mad_kf","Mad_cf","Mad_js","Mad_yk","Mad_zdj","Mad_zje",
								mMad_ht,mMad_kf,mMad_cf,mMad_js,mMad_yk,mMad_zdj,mMad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Aar_zhibb)){
						
						mAar_ht = rs2.getString("hetbz");	// 合同挥发分
						mAar_kf = rs2.getDouble("gongf");
						mAar_cf = rs2.getDouble("changf");
						mAar_js = rs2.getDouble("jies");		// 结算挥发分
						mAar_yk = rs2.getDouble("yingk");
						mAar_zdj = rs2.getDouble("zhejbz");
						mAar_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Aar(%)","Aar_ht","Aar_kf","Aar_cf","Aar_js","Aar_yk","Aar_zdj","Aar_zje",
								mAar_ht,mAar_kf,mAar_cf,mAar_js,mAar_yk,mAar_zdj,mAar_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Aad_zhibb)){
						
						mAad_ht = rs2.getString("hetbz");	// 合同挥发分
						mAad_kf = rs2.getDouble("gongf");
						mAad_cf = rs2.getDouble("changf");
						mAad_js = rs2.getDouble("jies");		// 结算挥发分
						mAad_yk = rs2.getDouble("yingk");
						mAad_zdj = rs2.getDouble("zhejbz");
						mAad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Aad(%)","Aad_ht","Aad_kf","Aad_cf","Aad_js","Aad_yk","Aad_zdj","Aad_zje",
								mAad_ht,mAad_kf,mAad_cf,mAad_js,mAad_yk,mAad_zdj,mAad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Vad_zhibb)){
						
						mVad_ht = rs2.getString("hetbz");	// 合同挥发分
						mVad_kf = rs2.getDouble("gongf");
						mVad_cf = rs2.getDouble("changf");
						mVad_js = rs2.getDouble("jies");		// 结算挥发分
						mVad_yk = rs2.getDouble("yingk");
						mVad_zdj = rs2.getDouble("zhejbz");
						mVad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("Vad(%)","Vad_ht","Vad_kf","Vad_cf","Vad_js","Vad_yk","Vad_zdj","Vad_zje",
								mVad_ht,mVad_kf,mVad_cf,mVad_js,mVad_yk,mVad_zdj,mVad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.T2_zhibb)){
						
						mT2_ht = rs2.getString("hetbz");	// 合同挥发分
						mT2_kf = rs2.getDouble("gongf");
						mT2_cf = rs2.getDouble("changf");
						mT2_js = rs2.getDouble("jies");		// 结算挥发分
						mT2_yk = rs2.getDouble("yingk");
						mT2_zdj = rs2.getDouble("zhejbz");
						mT2_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("T2(%)","T2_ht","T2_kf","T2_cf","T2_js","T2_yk","T2_zdj","T2_zje",
								mT2_ht,mT2_kf,mT2_cf,mT2_js,mT2_yk,mT2_zdj,mT2_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Yunju_zhibb)){
						
						mYunju_ht = rs2.getString("hetbz");	// 合同挥发分
						mYunju_kf = rs2.getDouble("gongf");
						mYunju_cf = rs2.getDouble("changf");
						mYunju_js = rs2.getDouble("jies");		// 结算挥发分
						mYunju_yk = rs2.getDouble("yingk");
						mYunju_zdj = rs2.getDouble("zhejbz");
						mYunju_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=SetJieszb("运距(Km)","Yunju_ht","Yunju_kf","Yunju_cf","Yunju_js","Yunju_yk","Yunju_zdj","Yunju_zje",
								mYunju_ht,mYunju_kf,mYunju_cf,mYunju_js,mYunju_yk,mYunju_zdj,mYunju_zje);
					
					}
					
					this.setJieszb(mstrJieszb);
				}
				
				rs2.close();
				mbeiz = rs.getString("beiz");
//				double mdanjc = 0;
//				待定
				mranlbmjbr = rs.getString("ranlbmjbr");//燃料部门经办人
				mranlbmjbrq =rs.getDate("ranlbmjbrq");//燃料部门经办日期
//				mkuidjf = 0;
				mjingz = rs.getDouble("guohl");
				mjiesrq =rs.getDate("jiesrq");
				mruzrq =rs.getDate("ruzrq");
				
				mjiesslblxsw=Fayjsdcz.getJiessz_item(mdiancxxb_id, mgongysb_id, mhetb_id, Locale.jiesslblxsw_jies, mjiesslblxsw);
				if(mjiesslblxsw.equals("0")){
					
					myuns=Math.round((mjiessl-mjingz));
				}else{
					
					String strblxs="";
					for(int i=0;i<Integer.parseInt(mjiesslblxsw);i++){
						
						if(i==0){
							
							strblxs="10";
						}else{
							
							strblxs+="0";
						}
					}
					myuns=Math.round((mjiessl-mjingz)*Double.parseDouble(strblxs))/Double.parseDouble(strblxs);
				}
				
			}
//			煤款
;
//			 1, 两票结算;
//			 2, 煤款结算
//			 3, 国铁运费
//			 4, 地铁运费
			if(blnHasMeik&&mjieslx==1){
//				两票		运费
				sql="select * from diancjsyfb where bianm='"+getJiesbhValue().getValue()+"' ";//and fuid=0";
				rs=con.getResultSet(sql);
				if(rs.next()){
					myid=rs.getLong("id");
					mtielyf=rs.getDouble("guotyf");
					mtielzf=rs.getDouble("guotzf");
					mbukyqyzf = rs.getDouble("bukyf");
					mjiskc=rs.getDouble("jiskc");
					mbuhsyf=rs.getDouble("buhsyf");
					myunfsl=rs.getDouble("shuil");
					myunfsk=rs.getDouble("shuik");
					myunzfhj=rs.getDouble("hansyf");
					myunfjsl=rs.getDouble("jiessl");
					mkuangqyf=rs.getDouble("kuangqyf");
					mkuangqzf=rs.getDouble("kuangqzf");
					mkuidjfyf_je=rs.getDouble("kuidjfyf");
					mkuidjfzf_je=rs.getDouble("kuidjfzf");
				}
				
			}else if(mjieslx!=2){
				
				sql=" select * from diancjsyfb where bianm='"+getJiesbhValue().getValue()+"'";
				
					rs=con.getResultSet(sql);
					if(rs.next()){
						myid=rs.getLong("id");
//						mtianzdw =rs.getString("xianshr");
						mjiesbh = rs.getString("bianm");
						mdiancjsbs=mjiesbh.substring(0,mjiesbh.indexOf("-")+1);
						mdiancxxb_id=rs.getLong("diancxxb_id");
						mfahdw = rs.getString("gongysmc");
						mfaz =rs.getString("faz");
						mshoukdw = rs.getString("shoukdw");
						mfahksrq = rs.getDate("fahksrq");
						mfahjzrq = rs.getDate("fahjzrq");
						myansksrq = rs.getDate("yansksrq");
						myansjzrq =rs.getDate("yansjzrq");
						mkaihyh =rs.getString("kaihyh");
						mpinz = rs.getString("meiz");
						myuanshr = rs.getString("yuanshr");
						mgongfsl=rs.getDouble("gongfsl");
						myanssl=rs.getDouble("yanssl");
						myingksl=rs.getDouble("yingk");
						mzhangh = rs.getString("zhangh");
						mches = rs.getInt("ches");
						mxianshr =rs.getString("xianshr");
						mfapbh = rs.getString("fapbh");
						mdaibcc = rs.getString("daibch");
						myansbh =rs.getString("yansbh");
						mduifdd = rs.getString("duifdd");
						mfukfs = rs.getString("fukfs");
						mjiessl = rs.getDouble("jiessl");
						mtielyf=rs.getDouble("guotyf");
						mtielzf=rs.getDouble("guotzf");
						mbukyqyzf = rs.getDouble("bukyf");
						mjiskc=rs.getDouble("jiskc");
						mbuhsyf=rs.getDouble("buhsyf");
						myunfsl=rs.getDouble("shuil");
						myunfsk=rs.getDouble("shuik");
						myunzfhj=rs.getDouble("hansyf");
						mkuangqyf=rs.getDouble("kuangqyf");
						mkuangqzf=rs.getDouble("kuangqzf");
//						myunsfsb_id=rs.getLong("yunsfsb_id");
//						myingd=rs.getDouble("yingd");
//						mkuid=rs.getDouble("kuid");
//						myunju=rs.getDouble("yunj");
						mjingz=rs.getDouble("guohl");
//						mjiesslcy=rs.getDouble("jiesslcy");
						mshulzjbz=rs.getDouble("hansdj");
						mkuidjfyf_je=rs.getDouble("kuidjfyf");
						mkuidjfzf_je=rs.getDouble("kuidjfzf");
					}
			}
			
				if(((Visit) getPage().getVisit()).getDouble2()>0
					||((Visit) getPage().getVisit()).getDouble3()>0){
						
					
					mkuangqyf=((Visit) getPage().getVisit()).getDouble2();
					mkuangqzf=((Visit) getPage().getVisit()).getDouble3();
					mkuangqsk=((Visit) getPage().getVisit()).getDouble4();
					mkuangqjk=((Visit) getPage().getVisit()).getDouble5();
					myunzfhj=Math.round((mtielyf+mtielzf+mkuangqyf+mkuangqzf+mbukyqyzf)*100)/100;
					myunfsk=(double)Math.round(((double)Math.round((mtielyf+mbukyqyzf)*myunfsl*100)/100+((Visit) getPage().getVisit()).getDouble4())*100)/100;
					mbuhsyf=(double)Math.round(((double)Math.round((myunzfhj-myunfsk)*100)/100+((Visit) getPage().getVisit()).getDouble5())*100)/100;
				}
			
			mmeikhjdx=getDXMoney(mjiasje);
			myunzfhjdx=getDXMoney(myunzfhj);
			mhej=mjiasje+myunzfhj;
			mdaxhj=getDXMoney(mhej);
			
//			设置超/亏吨的显示	
			if(!mchaokdlx.equals("")){
//				说明存在超亏吨
				this.setHejdxh(jsdcz.SetHejdxh(mchaokdlx,mchaokdl,mhej,mdaxhj));
			}else{
				
				this.setHejdxh(jsdcz.SetHejdxh("",0,mhej,mdaxhj));
			}
			
			_editvalues.add(new JiesdlrBean(
					mid, myid, mtianzdw, mjiesbh,
					mfahdw, mmeikdw,mfaz, 
					mshoukdw, mfahksrq, mfahjzrq, myansksrq,
					myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh, mhetsl,
					mgongfsl, mches, mxianshr, mfapbh, mdaibcc, myansbh,
					mduifdd, mfukfs, mshulzjbz, myanssl, myingksl, 
					mshulzjje,
					mjiessl, 
					myunfjsl,mbuhsdj, mjiakje,
					mbukyqjk, mjiakhj, mjiaksl, mjiaksk, mjiasje, mtielyf,mtielzf,
					mkuangqyf,mkuangqzf, mkuangqsk, mkuangqjk,mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
					myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq,
					mfahrq, mchangcwjbr, mchangcwjbrq, mruzrq,
					mjieszxjbr, mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq,
					mhetjg, mjieslx,myuns,mkoud_js,
					myunsfs, mdiancjsbs,mhetb_id,
					mMeikjsb_id,
					mYunfjsb_id,mJihkjb_id,
					mMjtokcalxsclfs,
					mkuidjfyf_je,
					mkuidjfzf_je,
					mchaokdl,mchaokdlx,
					mQnetar_ht ,	// 合同热量
					mQnetar_kf,		// 供方热量
					mQnetar_cf ,	// 厂方热量
					mQnetar_js ,		// 厂方结算
					mQnetar_yk ,	// 厂方盈亏
					mQnetar_zdj,		// 折单价
					mQnetar_zje,	// 折价金额
					mStd_ht,		//合同硫分
					mStd_kf,			//供方热量
					mStd_cf,			//厂方热量
					mStd_js,			//结算热量
					mStd_yk ,			// 厂方盈亏
					mStd_zdj,		// 折单价
					mStd_zje 		// 折价金额
					));//超吨亏吨标识符
			
			if(Xianszt.length()>1){
				setXuanz(Xianszt);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new JiesdlrBean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}	
	
	
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
//		结算类型
		tb1.addText(new ToolbarText("结算类型:"));
		ComboBox JieslxDropDown = new ComboBox();
		JieslxDropDown.setId("JieslxDrop");
		JieslxDropDown.setWidth(80);
		JieslxDropDown.setLazyRender(true);
		JieslxDropDown.setTransform("JieslxDropDown");
		tb1.addField(JieslxDropDown);
		tb1.addText(new ToolbarText("-"));
		
//		数据来源
		tb1.addText(new ToolbarText("业务类型:"));
		ComboBox ShujlyDropDown = new ComboBox();
		ShujlyDropDown.setId("ShujlyDrop");
		ShujlyDropDown.setWidth(80);
		ShujlyDropDown.setLazyRender(true);
		ShujlyDropDown.setTransform("ShujlyDropDown");
		ShujlyDropDown.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(ShujlyDropDown);
		tb1.addText(new ToolbarText("-"));
		
//		结算编号
		tb1.addText(new ToolbarText("结算单编号:"));
		ComboBox JiesbhDropDown = new ComboBox();
		JiesbhDropDown.setId("JiesbhDrop");
		JiesbhDropDown.setWidth(190);
		JiesbhDropDown.setLazyRender(true);
		JiesbhDropDown.setTransform("JIESBHDropDown");
		tb1.addField(JiesbhDropDown);
		tb1.addText(new ToolbarText("-"));
		
//		刷新
		ToolbarButton shuaxbt=new ToolbarButton(null,"刷新","function(){ document.Form0.RetrunsButton.click();}");
		shuaxbt.setId("Shuaxbt");
		tb1.addItem(shuaxbt);
//		保存
		ToolbarButton savebt=new ToolbarButton(null,"保存","function(){ document.Form0.SaveButton.click(); }");
		savebt.setId("savebt");
		savebt.setDisabled(true);
		tb1.addItem(savebt);
		
//		取消
//		ToolbarButton Backbt=new ToolbarButton(null,"取消","function(){ document.Form0.BackButton.click(); }");
//		Backbt.setId("Backbt");
//		tb1.addItem(Backbt);
//		tb1.addText(new ToolbarText("-"));
//		生成
		ToolbarButton Shengcbt=new ToolbarButton(null,"生成采购结算单","function(){HtSButton()}");
		Shengcbt.setId("Shengcbt");
		Shengcbt.setMinWidth(100);
		tb1.addItem(Shengcbt);
		tb1.addText(new ToolbarText("-"));
////		读取单价差
//		JDBCcon con = new JDBCcon();
//		String djc="select nvl(danjc,0) as danjc from diancjsmkb where bianm='"+getJiesbhValue().getValue()+"'";	
//		int zhi=0;
//		if(con.getHasIt(djc)){
//		ResultSet rs = con.getResultSet(djc);
//		try {
//			while (rs.next()) {
//				zhi=rs.getInt("danjc");
//			}
//			rs.close();
//		} catch (SQLException e) {	e.printStackTrace();}
//
//		}
//		con.Close();
//		if(zhi==0){zhi=4;}
		int zhi=4;
		tb1.addText(new ToolbarText("单价差"));
		NumberField tx=new NumberField();
		tx.setId("DANJC");
		tx.setMaxLength(5);
		tx.setDecimalPrecision(2);
		tx.setValue(zhi+"");
		tx.setWidth(40);
		tx.id="DANJC";
		tb1.addField(tx);
		
		tb1.addText(new ToolbarText("元"));

		
		setToolbar(tb1);
	}
	
	public String Xuanz;
	public void setXuanz(String value){
		Xuanz=value;
	}
	public String getXuanz(){
		if(Xuanz==null){
			Xuanz="";
		}
		return Xuanz;
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(0);
			_JiesrqsmallValue = new Date();
			_JiesrqbigValue = new Date();
			visit.setboolean2(false);//分公司
			visit.setboolean3(false);//电厂
			visit.setString2("");	 //结算指标页面显示
			((Visit) getPage().getVisit()).setString11("");	//用于Kuangqzf返回时，跳转到正确的界面(DCBalance,Jiesdxg)
			
			((Visit) getPage().getVisit()).setString2("");	//指标显示
			((Visit) getPage().getVisit()).setString3("");	//合计大写行显示内容（为实现动态设置“超扣吨”显示用）
			
			if(visit.getRenyjb()<3){
				
				visit.setboolean3(true);
			}	

//			setLiucmcValue(null);
//			setILiucmcModel(null);
//			getILiucmcModels();
			setKUANGFJSBH(null);
			setJieslxValue(null);
			setJieslxModel(null);
			setShujlyValue(null);
			setShujlyModel(null);
			setGonghValue(null);
			setGonghModel(null);
			setXianshrValue(null);
			setXianshrModel(null);
			setYuanshrValue(null);
			setYuanshrModel(null);
			setShoukdwValue(null);
			setIShoukdwModel(null);
			getIShoukdwModels();
			setJiesbhValue(null);
			visit.setProSelectionModel6(null);//清空visit中的选择模式
			visit.setDropDownBean6(null);
			setIJiesbhModel(null);
			setZhanghValue(null);
			setZhanghModel(null);
			setKaihyhValue(null);
			setKaihyhModel(null);
			visit.setboolean5(false);//合同编号显示
			visit.setboolean6(false);//流程名称显示
		}
		getToolbars();
			if(falg1){
				setJiesbhValue(null);
				setIJiesbhModel(null);
				falg1=false;
			}
			setXuanz("");
			getSelectData();
	}


	public String KUANGFJSBH;
	public void setKUANGFJSBH(String value){
		KUANGFJSBH=value;
	}
	public String getKUANGFJSBH(){
		if(KUANGFJSBH==null){
			KUANGFJSBH=Jiesbh();
		}
		return KUANGFJSBH;
	}
	
	
	private String danjc = "";
	public String getDanjc(){
		return danjc;
	}
	public void setDanjc(String value){
		this.danjc = value;
	}
	
	public void setJieszb(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getJieszb(){
		
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	public void setHejdxh(String value) {

		((Visit) getPage().getVisit()).setString3(value);
	}

	public String getHejdxh() {

		return ((Visit) getPage().getVisit()).getString3();
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
	
	
    public IDropDownBean getShoukdwValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean9()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean9((IDropDownBean)getIShoukdwModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean9();
    }
    
    public void setShoukdwValue(IDropDownBean value)
    {
    		((Visit)getPage().getVisit()).setDropDownBean9(value);
    }
    
    public void setIShoukdwModel(IPropertySelectionModel value){

    	((Visit)getPage().getVisit()).setProSelectionModel9(value);
	}
    
    public IPropertySelectionModel getIShoukdwModel(){
            
    	if(((Visit)getPage().getVisit()).getProSelectionModel9()==null){
        
    		getIShoukdwModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel9();
    }
    
    public IPropertySelectionModel getIShoukdwModels() {
    	String sql="select id, quanc from gongysb order by quanc";
		((Visit) getPage().getVisit()).setProSelectionModel9(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
    
	// 结算类型下拉框
	private boolean falg1 = false;

	private IDropDownBean JieslxValue;

	public IDropDownBean getJieslxValue() {
		if (JieslxValue == null) {
			JieslxValue = (IDropDownBean) getJieslxModel().getOption(0);
		}
		return JieslxValue;
	}

	public void setJieslxValue(IDropDownBean Value) {
		if (!(JieslxValue == Value)) {
			JieslxValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel JieslxModel;

	public void setJieslxModel(IPropertySelectionModel value) {
		JieslxModel = value;
	}

	public IPropertySelectionModel getJieslxModel() {
		if (JieslxModel == null) {
			getJieslxModels();
		}
		return JieslxModel;
	}

	public IPropertySelectionModel getJieslxModels() {
		String sql="select id,mingc from feiylbb where mingc<>'矿区运费' order by id";
		
		JieslxModel = new IDropDownModel(sql);
		return JieslxModel;
	}
	
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
	
//	业务类型
	private IDropDownBean ShujlyValue;

	public IDropDownBean getShujlyValue() {
		if (ShujlyValue == null) {
			ShujlyValue = (IDropDownBean) getShujlyModel().getOption(0);
		}
		return ShujlyValue;
	}

	public void setShujlyValue(IDropDownBean Value) {
		if (!(ShujlyValue == Value)) {
			ShujlyValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel ShujlyModel;

	public void setShujlyModel(IPropertySelectionModel value) {
		ShujlyModel = value;
	}

	public IPropertySelectionModel getShujlyModel() {
		if (ShujlyModel == null) {
			getShujlyModels();
		}
		return ShujlyModel;
	}

	public IPropertySelectionModel getShujlyModels() {
		String sql="select id, mingc from yewlxb";
		ShujlyModel = new IDropDownModel(sql);
		return ShujlyModel;
	}
    
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
    	Visit visit = (Visit) this.getPage().getVisit();
    	String sql = "";
    	JDBCcon con=new JDBCcon();
    	List List=new ArrayList();
    	
    	String strDiancID="";
    	
		if(visit.isJTUser()){
			strDiancID = "";
		}else if(visit.isGSUser()){
			strDiancID = " and (dc.id="+visit.getDiancxxb_id()+" or dc.fuid="+visit.getDiancxxb_id()+")";
		}else{
			strDiancID = " and dc.id="+visit.getDiancxxb_id();
		}
    	String strTiaoj="  and k.gongsqrzt=1 ";
    	
    	try{
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));
    			sql=
//    				" select bianm from "
//    				+ " (" +
				"select distinct k.bianm from diancjsmkb k, diancxxb dc where k. diancxxb_id=dc.id and k.kuangfjsmkb_id=0 and liucztb_id=0 "+strDiancID+strTiaoj+" and k.yewlxb_id="+getShujlyValue().getStrId()+" order by bianm";
//				+" )"
//    				+ " union"
//    				+ " (select distinct k1.bianm from diancjsyfb k1, diancxxb dc where k1.diancxxb_id=dc.id and liucztb_id=0 "+strDiancID+" and k1.yewlxb_id="+getShujlyValue().getStrId()+") order by bianm";
    		
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
	
//	供货人 DropDownBean5
//  供货人 ProSelectionModel5
	public IDropDownBean getGonghValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getGonghModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setGonghValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean5()!=value){
			((Visit) getPage().getVisit()).setDropDownBean5(value);
		}
	}

	public void setGonghModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getGonghModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getIFahdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
	
	public IPropertySelectionModel getIFahdwModels(){
    	String sql="select id, quanc as gongysmc from gongysb order by quanc";
    		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql));
    		return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }
	
//  现收货人 DropDownBean7
//  现收货人 ProSelectionModel7
   
   public void setXianshrModel(IPropertySelectionModel value){
       ((Visit)getPage().getVisit()).setProSelectionModel7(value);
   }
   
   public IPropertySelectionModel getXianshrModel(){
   	if(((Visit)getPage().getVisit()).getProSelectionModel7()==null){
   		getXianshrModels();
       }
       return ((Visit)getPage().getVisit()).getProSelectionModel7();
   }
   
   public IPropertySelectionModel getXianshrModels(){
//   	Visit visit = (Visit) this.getPage().getVisit();
	   String sql="select id,quanc from diancxxb where (cangkb_id=1 and jib=3) or jib=2 order by quanc";
   	((Visit)getPage().getVisit()).setProSelectionModel7(new IDropDownModel(sql));
		return ((Visit)getPage().getVisit()).getProSelectionModel7();
   }
   
   public IDropDownBean getXianshrValue() {
   	
   	if(((Visit)getPage().getVisit()).getDropDownBean7()==null){
   		((Visit)getPage().getVisit()).setDropDownBean7((IDropDownBean)getXianshrModel().getOption(0));
   	}
		return ((Visit)getPage().getVisit()).getDropDownBean7();
	}
	
   public void setXianshrValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean7()!=value){
			((Visit) getPage().getVisit()).setDropDownBean7(value);
		}
	}
//   end
	
//	原收货人 DropDownBean8
//  原收货人 ProSelectionModel8
	public IDropDownBean getYuanshrValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getYuanshrModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setYuanshrValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setYuanshrModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getYuanshrModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getYuanshrModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getYuanshrModels() {
		String sql="select id,quanc from diancxxb where (cangkb_id=1 and jib=3) or jib=2 order by quanc";
		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
    //********************************************************************************//
	
	public String SetJieszb(String Zhibmc, String Zhib_ht, String Zhib_kf, String Zhib_cf, 
			String Zhib_js, String Zhib_yk, String Zhib_zdj, String Zhib_zje, 
			String Zhib_ht_value, double Zhib_kf_value, double Zhib_cf_value, 
			double Zhib_js_value, double Zhib_yk_value, double Zhib_zdj_value, 
			double Zhib_zje_value){
		
		//初始化结算指标
		StringBuffer Stbf=new StringBuffer();
		
		Stbf.append("<tr>");
		Stbf.append("	<td class='Jsdtdborder'>"+Zhibmc+"</td>");
		Stbf.append("	<td class='Jsdtdborder'><input readonly='true' class='noeditinput'  style='width:100%' name='"+Zhib_ht+"'   	id='"+Zhib_ht+"'	value="+Zhib_ht_value+"  type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input readonly='true' class='noeditinput'  style='width:100%' name='"+Zhib_kf+"' 		id='"+Zhib_kf+"' 	value="+Zhib_kf_value+" 	type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input readonly='true' class='noeditinput'  style='width:100%' name='"+Zhib_cf+"'  	id='"+Zhib_cf+"'  	value="+Zhib_cf_value+"	type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input readonly='true' class='noeditinput'  style='width:100%' name='"+Zhib_js+"'  	id='"+Zhib_js+"'  	value="+Zhib_js_value+"	type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input readonly='true' class='noeditinput'  style='width:100%' name='"+Zhib_yk+"' 		id='"+Zhib_yk+"' 	value="+Zhib_yk_value+"	type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input readonly='true' class='noeditinput'  style='width:100%' name='"+Zhib_zdj+"' 	id='"+Zhib_zdj+"' 	value="+Zhib_zdj_value+" type='text' /></td>");
		Stbf.append("	<td colspan='2' class='Jsdtdborder'><input readonly='true' class='noeditinput' style='width:100%' name='"+Zhib_zje+"' id='"+Zhib_zje+"' value="+Zhib_zje_value+"  type='text' /></td>");
		Stbf.append("</tr>");
		
		return	Stbf.toString();    
	}
    
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