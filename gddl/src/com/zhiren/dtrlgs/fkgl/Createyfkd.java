package com.zhiren.dtrlgs.fkgl;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.IDropDownModel;
//import com.zhiren.common.IDropDownSelectionModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Money;
import com.zhiren.common.MainGlobal;
//import com.zhiren.common.Sequence;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
//import com.zhiren.rmis.hesgl.changfhs.Jiesxgbean;
//import com.zhiren.rmis.hesgl.fukdgl.Createfktzdbean;
//import com.zhiren.rmis.report.kuangfjs.Kuangfjsd;

import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;

public class Createyfkd extends BasePage {

	private static int _editTableRow = -1;//编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value,false);;
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
	        sql="select fukdbh from fuktzb where fukdbh='"+((Createyfkdbean)getEditValues().get(0)).getFuktzdbh()+"'";
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

		if(((Createyfkdbean)getEditValues().get(0)).getGongysb_id()==-1){
			setMsg("不能保存空表单！");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = ((Visit)getPage().getVisit());
	    String sql = "";
	    int result=-1;
	    String liucm = "预付款审核";
	    long liucksid = -1;//流程开始ID
	    try{
	    	sql = "select liucdzb.liucztqqid as liucksid "
	    		+ "  from liucb,liucztb,liucdzb,leibztb "
	    		+ " where liucztb.liucb_id=liucb.id and liucdzb.liucztqqid=liucztb.id " 
	    		+ "   and liucztb.leibztb_id=leibztb.id and leibztb.mingc='开始' and liucb.mingc='"+liucm+"' ";
	    	ResultSet rs = con.getResultSet(sql);
	    	if(rs.next()){
	    		liucksid = rs.getLong("liucksid");
	    	}
	    	rs.close();
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((Createyfkdbean)getEditValues().get(0)).getFuktzdbh().equals("")){
	        	if(checkbh()){
//	        		long id = Sequence.getDataID("fuktzb");
	        		String id=MainGlobal.getNewID(visit.getDiancxxb_id());
	        		sql = "insert into fuktzb "
	        			+ " (id, fukdbh, riq, gongysb_id, tianzdw, fukdlx, fapje, kouyf, kouhkf, hexyfk, shijfk, fapbhs, xiangmmc, xiangmbh, liucztb_id, liucgzid) "
	        			+ " values ("+id+",'"
	        			+ ((Createyfkdbean)getEditValues().get(0)).getFuktzdbh()+"',"
	        			+ "to_date('"+FormatDate(new Date())+"','yyyy-mm-dd'),"
	        			+ ((Createyfkdbean)getEditValues().get(0)).getGongysb_id()+",'"
	        			+ ((Createyfkdbean)getEditValues().get(0)).getTianzdw()+"','"
	        			+ ((Createyfkdbean)getEditValues().get(0)).getFuksy()+"',"
	        			+ ((Createyfkdbean)getEditValues().get(0)).getFapje()+","
	        			+ ((Createyfkdbean)getEditValues().get(0)).getKouyf()+","
	        			+ ((Createyfkdbean)getEditValues().get(0)).getKouhkf()+","
	        			+ ((Createyfkdbean)getEditValues().get(0)).getHexyfk()+","
	        			+ ((Createyfkdbean)getEditValues().get(0)).getShijfk()+",'"
	        			+ ((Createyfkdbean)getEditValues().get(0)).getFapbhs()+"','"
	        			+ ((Createyfkdbean)getEditValues().get(0)).getXiangmmc()+"','"
	        			+ ((Createyfkdbean)getEditValues().get(0)).getXiangmbh()+"',"+liucksid+",0)";
	        		
	        		result = con.getInsert(sql);
	        		if(result<0){
	        			setMsg("保存失败！");
	        			con.rollBack();
	        			return;
	        		}
	        		String upmksql = "update yufkb set fuktzb_id="+id+" where id="+getYufkbhValue().getId()+"";
	        		
	        		result = con.getUpdate(upmksql);
	        		if(result<0){
	        			setMsg("保存失败！");
	        			con.rollBack();
	        			return;
	        		}
	        		con.commit();
	        		setMsg("保存成功！");
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
	        String sDate = sYear + sMonth;
	        int intXh=1;
	        int intBh=0;
	        String sql2="select max(fukdbh) as bianh from (select fukdbh from fuktzb where fukdbh like 'YFK"+sDate+"%')";
	        ResultSet rsSl=con.getResultSet(sql2);
	        if (rsSl.next()){
	            strJsbh=rsSl.getString("bianh");               
	        }
	        if(strJsbh==null){
	            strJsbh="YFK"+sDate+"0000";
	        }
	        intBh=Integer.parseInt(strJsbh.trim().substring(strJsbh.trim().length()-4,strJsbh.trim().length()));
	        intBh=intBh+1;
	        if(intBh<10000 && intBh>=1000){
	            strJsbh="YFK"+sDate+String.valueOf(intBh);
	        }else if (intBh<1000 && intBh>=100){
	            strJsbh="YFK"+sDate+"0"+String.valueOf(intBh);
	        }else if(intBh>=10 && intBh<100){
	            strJsbh="YFK"+sDate+"00"+String.valueOf(intBh);
	        }else{
	            strJsbh="YFK"+sDate+"000"+String.valueOf(intBh);
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
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

//	private boolean _RetrunsChick = false;
//
//	public void RetrunsButton(IRequestCycle cycle) {
//		_RetrunsChick = true;
//	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save(cycle);
			setYufkbhValue(null);
			setYufkbhModel(null);
		}
	}
	
	private Createyfkdbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Createyfkdbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Createyfkdbean EditValue) {
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
		if(getEditValues()!=null){
			getEditValues().clear();
		}
		
		String sql="";
		ResultSet rs;
		try {
			String mFuktzdbh=Fuktzdbh();
			long mgongysb_id = -1;
			String mshoukdw="";
			String mbianm="";
			String mdiz="";
			String mkaihyh="";
			String mzhangh="";
			double myufkje=0;
			
			String mfapbhs = "";
			double mfapje = 0;
			double mhexyfk = 0;
			double mshijfk = 0; 
			String mShijfkdx = getDXMoney(mshijfk);
			
			double mkouyf = 0;//扣运费
			double mkouhkf = 0;//扣回空费
			String mshengcfkdrq = FormatDate(new Date());
			String mxiangmmc = "";
			String mxiangmbh = "";
			String mfuksy = "预付款";
			String mtianzdw = getTianzdw(visit.getDiancxxb_id());
			
			sql="select gy.id,gy.quanc,gy.bianm,gy.danwdz,yf.kaihyh,yf.zhangh,yf.jine from yufkb yf,gongysb gy where gy.id=yf.gongysb_id and yf.bianh='"+getYufkbhValue().getValue()+"'";
			rs=con.getResultSet(sql);
			
			if(rs.next()){
				mgongysb_id = rs.getLong("id");
				mshoukdw=rs.getString("quanc");
				mbianm=rs.getString("bianm");
				mdiz=rs.getString("danwdz");
				mkaihyh=rs.getString("kaihyh");
				mzhangh=rs.getString("zhangh");
				myufkje=rs.getDouble("jine");
				mfapje = myufkje;
				mhexyfk = 0;
				mshijfk = Math.floor(mfapje-mhexyfk); 
				mShijfkdx = getDXMoney(mshijfk);
			}
			_editvalues.add(new Createyfkdbean(mfuksy,mFuktzdbh,mtianzdw,mshengcfkdrq,mgongysb_id,mshoukdw,mbianm,mdiz,mkaihyh,mzhangh,mfapbhs,mfapje,mhexyfk,mshijfk,mShijfkdx,mkouyf,mkouhkf,mxiangmmc,mxiangmbh));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Createyfkdbean());
		}
		_editTableRow = -1;
		((Visit) getPage().getVisit()).setList1(_editvalues);
		
		return ((Visit) getPage().getVisit()).getList1();
	}
	
	private boolean gongyschange=false;
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {
			gongyschange = true;
			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}else{
			gongyschange = false;
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String  sql ="select gys.id,gys.mingc from gongysb gys  order by gys.mingc ";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
	
	private boolean yufkbhChange = false; 
	public IDropDownBean getYufkbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getYufkbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setYufkbhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean9() != value) {
			yufkbhChange = true;
			((Visit) getPage().getVisit()).setDropDownBean9(value);
		}else{
			yufkbhChange = false;
		}
	}

	public void setYufkbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getYufkbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {

			getYufkbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public IPropertySelectionModel getYufkbhModels() {
		String  sql = "";
		if(getGongysValue()!=null){
			if(getGongysValue().getId()!=-1){
				sql ="select yf.id,yf.bianh from yufkb yf where yf.fuktzb_id=0 and yf.gongysb_id="+getGongysValue().getId()+" order by yf.bianh ";
			}else{
				sql ="select yf.id,yf.bianh from yufkb yf where yf.fuktzb_id=0 order by yf.bianh ";
			}
		}else{
			sql ="select yf.id,yf.bianh from yufkb yf where yf.fuktzb_id=0 order by yf.bianh ";
		}
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			visit.setDropDownBean9(null);
			visit.setProSelectionModel9(null);
		}
		if(yufkbhChange){
			yufkbhChange = false;
		}
		if(gongyschange){
			gongyschange = false;
			visit.setDropDownBean9(null);
			visit.setProSelectionModel9(null);
		}
		getSelectData();
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
			String sql="select quanc from diancxxb where jib=2 and id="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Tianzdw=rs.getString("quanc");
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