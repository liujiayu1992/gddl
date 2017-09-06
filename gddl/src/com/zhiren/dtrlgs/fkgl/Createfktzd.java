package com.zhiren.dtrlgs.fkgl;

import org.apache.tapestry.html.BasePage;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Money;
import com.zhiren.common.MainGlobal;
//import com.zhiren.common.Sequence;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
//import com.zhiren.rmis.hesgl.changfhs.Jiesxgbean;
//import com.zhiren.rmis.report.kuangfjs.Kuangfjsd;

import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.dtrlgs.pubclass.BalanceLiuc;

public class Createfktzd extends BasePage {

	private static long _editTableRow = 0;//保存后的fukjsb_id

	public long getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(long _value) {
		_editTableRow = _value;
	}
	
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		
			_msg =MainGlobal.getExtMessageBox(_value, false);
		
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private boolean checkbh() {
        // TODO 自动生成方法存根
	    JDBCcon con = new JDBCcon();
	    String sql = "";
	    try{
	        sql="select fukdbh from fuktzb where fukdbh='"+((Createfktzdbean)getEditValues().get(0)).getFuktzdbh()+"'";
	        ResultSet rs=con.getResultSet(sql);
	        if(rs.next()){
	            return false;
	        }
	        rs.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally{
	        con.Close();
	    }
        return true;
    }
	private void Save(IRequestCycle cycle) {

		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = ((Visit)getPage().getVisit());
	    String sql = "";
	    int result=-1;
	    String liucm = "";//流程名
	    long liucksid = -1;//流程开始ID
	    boolean isTongguo=false;
	    String TableName="fuktzb";
	     long renyxxb_id=visit.getRenyID();
	    try{
    		if(((Createfktzdbean)getEditValues().get(0)).getShijfk()==0){
	    		liucm = "零付款审核";
	    		
	    	}else{
	    		liucm = "付款单审核";
	    	}
    		long liucb_id=MainGlobal.getTableId("liucb", "mingc", liucm);
    	
    		
    		
	    	/*sql = "select liucdzb.liuczthjid as liucksid "
	    		+ "  from liucb,liucztb,liucdzb,leibztb "
	    		+ " where liucztb.liucb_id=liucb.id and liucdzb.liucztqqid=liucztb.id " 
	    		+ "   and liucztb.leibztb_id=leibztb.id and leibztb.mingc='发起' and liucb.mingc='"+liucm+"' ";
	    	ResultSet rs = con.getResultSet(sql);
	    	if(rs.next()){
	    		liucksid = rs.getLong("liucksid");
	    	}
	    
	    	rs.close();*/
    		liucksid=0;
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((Createfktzdbean)getEditValues().get(0)).getFuktzdbh().equals("")){
	        	if(checkbh()){
//	        		long id = Sequence.getDataID("fuktzb");
	        		String id=MainGlobal.getNewID(visit.getDiancxxb_id());
	        		sql = "insert into fuktzb "
	        			+ " (id, fukdbh, riq, gongysb_id,tianzdw, fukdlx, fapje, kouyf, kouhkf, hexyfk, shijfk, fapbhs, xiangmmc, xiangmbh, liucztb_id, liucgzid) "
	        			+ " values ("+id+",'"
	        			+ ((Createfktzdbean)getEditValues().get(0)).getFuktzdbh()+"',"
	        			+ "to_date('"+FormatDate(new Date())+"','yyyy-mm-dd'),"
	        			+ ((Createfktzdbean)getEditValues().get(0)).getGongysb_id()+",'"
	        			+ ((Createfktzdbean)getEditValues().get(0)).getTianzdw()+"','"
	        			+ ((Createfktzdbean)getEditValues().get(0)).getFuksy()+"',"
	        			+ ((Createfktzdbean)getEditValues().get(0)).getFapje()+","
	        			+ ((Createfktzdbean)getEditValues().get(0)).getKouyf()+","
	        			+ ((Createfktzdbean)getEditValues().get(0)).getKouhkf()+","
	        			+ ((Createfktzdbean)getEditValues().get(0)).getHexyfk()+","
	        			+ ((Createfktzdbean)getEditValues().get(0)).getShijfk()+",'"
	        			+ ((Createfktzdbean)getEditValues().get(0)).getFapbhs()+"','"
	        			+ ((Createfktzdbean)getEditValues().get(0)).getXiangmmc()+"','"
	        			+ ((Createfktzdbean)getEditValues().get(0)).getXiangmbh()+"',"+liucksid+",0)";
	        		
	        		result = con.getInsert(sql);
	        		if(result<0){
	        			setMsg("保存失败！");
	        			con.rollBack();
	        			return;
	        		}
	        		String upmksql = "update kuangfjsmkb set fuktzb_id="+id+" where id in ("+visit.getString1()+")";
//	        		String upyfsql = "update kuangfjsyf set fuktzb_id="+id+" where id in ("+visit.getString1()+")";
	        		
	        		result = con.getUpdate(upmksql);
	        		if(result<0){
	        			setMsg("保存失败！");
	        			con.rollBack();
	        			return;
	        		}
	        		
//	        		result = con.getUpdate(upyfsql);
	        		if(result<0){
	        			setMsg("保存失败！");
	        			con.rollBack();
	        			return;
	        		}
	        		
	        		if(!FenpYfk()){//更新yufkb余额和yufklsb信息
	        			setMsg("保存失败！");
	        			con.rollBack();
	        			return;
	        		}
	        		con.commit();
	        		
	        		//自动提交流程
	        		BalanceLiuc.tij(TableName,new Long( id).longValue(), renyxxb_id, liucb_id, "", isTongguo);
//	        		long TableID=new Long(id).longValue();
//	        		BalanceLiuc.tij(TableName, TableID, renyxxb_id, liucksid, "", isTongguo);
	        		//自动提交流程
	        		setMsg("保存成功！");
	        		this.setEditTableRow(new Long(id).longValue());
		        }else{
		        	setMsg("结算编号重复，请核对！");
		        	return;
		        }
	        }
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	con.Close();
	    }
	}
	private boolean FenpYfk(){//分配预付款
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int result = -1;
		Visit visit = ((Visit)getPage().getVisit());
		int y=0;
		List listJiesd = new ArrayList();
		List listYufk = new ArrayList();
		try{
			String sql = "(select mk.id,mk.hansmk as fapje from kuangfjsmkb mk where mk.id in ("+visit.getString1()
					   + ")) --union (select yf.id,yf.yunzfhj as fapje from kuangfjsyf yf where yf.id in ("+visit.getString1()+"))";
			ResultSet rs = con.getResultSet(sql);
			while(rs.next()){
				listJiesd.add(new Createfktzdbean(rs.getLong("id"),rs.getDouble("fapje"), rs.getDouble("fapje")));
			}
			for(int j=0;j<visit.getList3().size();j++){//将使用的预付款对应的ID和金额存放到List里
				if(((Shengcfkdbean) visit.getList3().get(j)).getSeldiv()){
					listYufk.add(new Createfktzdbean(((Shengcfkdbean)visit.getList3().get(j)).getYufkiddiv(),new Double( ((Shengcfkdbean)visit.getList3().get(j)).getYufkyediv()).doubleValue(), ((Shengcfkdbean)visit.getList3().get(j)).getHexyfkdiv()));
				}
			}
			StringBuffer upyfksql = new StringBuffer();//更新yufkb表中的余额
			int yfk = 0;
			upyfksql.append("begin  \n ");
			for(int j=0;j<listYufk.size();j++){
				yfk++;
				upyfksql.append("update yufkb set yue="+(((Createfktzdbean)listYufk.get(j)).getOldvalue()-((Createfktzdbean)listYufk.get(j)).getDbvalue())+" where id="+((Createfktzdbean)listYufk.get(j)).getId()+"; \n");
			}
			upyfksql.append(" end; ");
			
			StringBuffer istlssql = new StringBuffer();//insert yufklsb sql
			int yfkls = 0;
			istlssql.append("begin  \n ");
			for(int i=0;i<listJiesd.size();i++){
				double Jiesdye = ((Createfktzdbean)listJiesd.get(i)).getDbvalue();
				if(Jiesdye>0){
					for(int j=0;j<listYufk.size();j++){
						double yufkye = ((Createfktzdbean)listYufk.get(j)).getDbvalue();
						if(yufkye>0){
							if(Jiesdye>yufkye){//结算金额>预付款余额
								yfkls++;
//								long lsbid = Sequence.getDataID("yufklsb");
								String lsbid=MainGlobal.getNewID(visit.getDiancxxb_id());
								
								istlssql.append("insert into yufklsb (id, yufkb_id, jine, jiesb_id, shiysj, beiz) values ( \n");
								istlssql.append(" "+lsbid+","+((Createfktzdbean)listYufk.get(j)).getId()+","+yufkye+","
												+((Createfktzdbean)listJiesd.get(i)).getId()+",to_date('"+FormatDate(new Date())+"','yyyy-mm-dd'),'');\n");
								
								listYufk.set(j, new Createfktzdbean(((Createfktzdbean)listYufk.get(j)).getId(), ((Createfktzdbean)listYufk.get(j)).getOldvalue(), 0));
								listJiesd.set(i, new Createfktzdbean(((Createfktzdbean)listJiesd.get(i)).getId(), ((Createfktzdbean)listJiesd.get(i)).getOldvalue(), (Jiesdye-yufkye)));
								Jiesdye = Jiesdye-yufkye;
							
							}else{//预付款余额>结算金额
								yfkls++;
//								long lsbid = Sequence.getDataID("yufklsb");
								String lsbid=MainGlobal.getNewID(visit.getDiancxxb_id());
								istlssql.append("insert into yufklsb (id, yufkb_id, jine, jiesb_id, shiysj, beiz) values ( \n");
								istlssql.append(" "+lsbid+","+((Createfktzdbean)listYufk.get(j)).getId()+","+Jiesdye+","
												+((Createfktzdbean)listJiesd.get(i)).getId()+",to_date('"+FormatDate(new Date())+"','yyyy-mm-dd'),'');\n");
								
								listYufk.set(j, new Createfktzdbean(((Createfktzdbean)listYufk.get(j)).getId(), ((Createfktzdbean)listYufk.get(j)).getOldvalue(), (yufkye-Jiesdye)));
								listJiesd.set(i, new Createfktzdbean(((Createfktzdbean)listJiesd.get(i)).getId(), ((Createfktzdbean)listJiesd.get(i)).getOldvalue(), 0));
								
								continue;
							}
						}
					}
				}
			}
			istlssql.append(" end; ");
			if(yfkls>0){
				result = con.getInsert(istlssql.toString());
				if(result<0){
					con.rollBack();
					return false;
				}
			}
			if(yfk>0){
				result = con.getUpdate(upyfksql.toString());
				if(result<0){
					con.rollBack();
					return false;
				}
			}
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return true;
	}
	private String Fuktzdbh(){
		
//		结算编号
		JDBCcon con=new JDBCcon();
		String strJsbh="";
		try{
	        String sYear ="";
	        String sMonth="";
	        java.util.Date datCur = new java.util.Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-01");
	        String dat = formatter.format(datCur);
	        sYear = dat.substring(2, 4);
	        sMonth = dat.substring(5, 7);
	        String sDate = "FK"+sYear + sMonth;
	        int intXh=1;
	        int intBh=0;
	        String sql2="select max(fukdbh) as bianh from (select fukdbh from fuktzb where fukdbh like '"+sDate+"%')";
	        ResultSet rsSl=con.getResultSet(sql2);
	        if (rsSl.next()){
	            strJsbh=rsSl.getString("bianh");               
	        }
	        if(strJsbh==null){
	            strJsbh=sDate+"0000";
	        }
	        intBh=Integer.parseInt(strJsbh.trim().substring(strJsbh.trim().length()-4,strJsbh.trim().length()));
	        intBh=intBh+1;
	        if(intBh<10000 && intBh>=1000){
	            strJsbh=sDate+String.valueOf(intBh);
	        }else if (intBh<1000 && intBh>=100){
	            strJsbh=sDate+"0"+String.valueOf(intBh);
	        }else if(intBh>=10 && intBh<100){
	            strJsbh=sDate+"00"+String.valueOf(intBh);
	        }else{
	            strJsbh=sDate+"000"+String.valueOf(intBh);
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
	
	private void Retruns(IRequestCycle cycle) {
		((Visit) getPage().getVisit()).setInt1(0);
		 cycle.activate(((Visit)getPage().getVisit()).getString5());
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
			Save(cycle);
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			Retruns(cycle);
		}
		
	}
	
	private Createfktzdbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Createfktzdbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Createfktzdbean EditValue) {
		_EditValue = EditValue;
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}

	public List getSelectData() {

		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon con = new JDBCcon();
		
		String mFuktzdbh=Fuktzdbh();
//		String mFuktzdbh = "";
//		String mtianzdw="";
		long mgongysb_id = -1;
		String mshoukdw = "";
		String mbianm = "";
		String mdiz = "";
		String mkaihyh = "";
		String mzhangh = "";
		
		String mfapbhs = visit.getString3();
		double mfapje = visit.getDouble2();
		double mhexyfk = visit.getDouble1();
		double mshijfk = mfapje-mhexyfk; 
		String mShijfkdx = getDXMoney(mshijfk);
		
		double mkouyf = 0;//扣运费
		double mkouhkf = 0;//扣回空费
		String mshengcfkdrq = FormatDate(new Date());
		
		String mxiangmmc = "";
		String mxiangmbh = "";
		String mfuksy = "实际付款";
		String mtianzdw = getTianzdw(visit.getDiancxxb_id());
		
		try {
			String sql="select distinct js.bianm bianh,gy.id,js.shoukdw,gy.bianm,gy.danwdz diz,js.kaihyh,js.zhangh from kuangfjsmkb js,gongysb gy "
					  +" where js.shoukdw=gy.quanc and js.shoukdw='"+visit.getString4()+"' and js.id in ("+visit.getString1()+")";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
//				mFuktzdbh=Fuktzdbh();
//				mFuktzdbh=rs.getString("bianm");
				mgongysb_id = rs.getLong("id");
				mshoukdw=rs.getString("shoukdw");
				mbianm=rs.getString("bianm");
				mdiz=rs.getString("diz");
				mkaihyh=rs.getString("kaihyh");
				mzhangh=rs.getString("zhangh");
				
			}/*else{
				sql = "select distinct js.bianh,gy.id,js.shoukdw,gy.bianm,gy.diz,js.kaihyh,js.zhangh from kuangfjsyf js,gongysb gy "
					  +" where js.shoukdw=gy.quanc and js.shoukdw='"+visit.getString4()+"'";
				rs = con.getResultSet(sql);
				if(rs.next()){
					mFuktzdbh=rs.getString("bianh");
					mgongysb_id = rs.getLong("id");
					mshoukdw=rs.getString("shoukdw");
					mbianm=rs.getString("bianm");
					mdiz=rs.getString("diz");
					mkaihyh=rs.getString("kaihyh");
					mzhangh=rs.getString("zhangh");
				}
			}*/
			_editvalues.add(new Createfktzdbean(mfuksy,mFuktzdbh,mtianzdw,mshengcfkdrq,mgongysb_id,mshoukdw,mbianm,mdiz,mkaihyh,mzhangh,mfapbhs,mfapje,mhexyfk,mshijfk,mShijfkdx,mkouyf,mkouhkf,mxiangmmc,mxiangmbh));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Createfktzdbean());
		}
		_editTableRow = -1;
		((Visit) getPage().getVisit()).setList1(_editvalues);
		
		return ((Visit) getPage().getVisit()).getList1();
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();
		}
//		getSelectData();
	}
	
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
	
	public String getTianzdw(long diancxxb_id) {
		String Tianzdw="";
		JDBCcon con=new JDBCcon();
		try{
			String sql="select mingc from diancxxb where id="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Tianzdw=rs.getString("mingc");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Tianzdw;
	}
	
	private int chaxunzt1 = 0;// 查询状态
	private int zhuangt =1;

//**********************打印界面DIV中的数据处理 结束**************************// 
    
	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

//	public String getPageHome() {
//		if (((Visit) getPage().getVisit()).getMissSession()) {
//			return "window.opener=null;self.close();window.parent.close();open('"
//					+ getpageLinks() + "','');";
//		} else {
//			return "";
//		}
//	}
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