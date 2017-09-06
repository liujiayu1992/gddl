package com.zhiren.dtrlgs.fkgl;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

//import com.zhiren.common.DBconn;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
//import com.zhiren.rmis.hesgl.changfhs.Caiwrzbean;
//import com.zhiren.rmis.hesgl.changfhs.Jiescwbean;

import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.dtrlgs.pubclass.BalanceLiuc;

public class Shengcfkd extends BasePage {
	private String _msg;

    protected void initialize() {
        _msg = "";
    }

    public void setMsg(String _value) {
        _msg = _value;
    }

    public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
    
    private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
//	格式化
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		return df.format(dblValue);
		 
	}

	private static int _editTableRow = -1;//编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}


	
	private void Refurbish() {
		//为 "刷新" 按钮添加处理程序
		//List _list =((Visit) getPage().getVisit()).getEditValues();
		//((Jiesselectbean) _list.get(i)).getXXX();
		setJiesbhValue(null);
		setJiesbhModel(null);
		getSelectData();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _QuerjsChick = false;

	public void QuerjsButton(IRequestCycle cycle) {
		_QuerjsChick = true;
	}
	
	private boolean _ChaxChick = false;

	public void ChaxButton(IRequestCycle cycle) {
		_ChaxChick = true;
	}

	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		
		if (_QuerjsChick) {
			_QuerjsChick = false;
			CreateFukd(cycle);
		}
		if (_ChaxChick) {
			_ChaxChick = false;
			Chax(cycle);
		}
	}

	private void CreateFukd(IRequestCycle cycle){
		
		String shoukdw = "";
		String fapbhs = "";
		double hexyfk = 0;
		double fukdfpje = 0;
		String strjsdid = "";
		String stryufkid = "";
		for(int i=0;i<getEditValues().size();i++){
			if(((Shengcfkdbean) getEditValues().get(i)).getSelect()){
				shoukdw = ((Shengcfkdbean)getEditValues().get(i)).getShoukdw();
				if(strjsdid.equals("")){
					strjsdid = ""+((Shengcfkdbean)getEditValues().get(i)).getId();
				}else{
					strjsdid = strjsdid+","+((Shengcfkdbean)getEditValues().get(i)).getId();
				}
				
				if(fapbhs.equals("")){
					fapbhs = ((Shengcfkdbean)getEditValues().get(i)).getFapbh();
				}else{
					fapbhs = fapbhs+","+((Shengcfkdbean)getEditValues().get(i)).getFapbh();
				}
			}
		}
		for(int j=0;j<getYufkEditValues().size();j++){
			if(((Shengcfkdbean) getYufkEditValues().get(j)).getSeldiv()){
				if(stryufkid.equals("")){
					stryufkid = ""+((Shengcfkdbean)getYufkEditValues().get(j)).getYufkiddiv();
				}else{
					stryufkid = stryufkid+","+((Shengcfkdbean)getYufkEditValues().get(j)).getYufkiddiv();
				}
			}
		}
		hexyfk = getHexyfk();
		fukdfpje = getFukdfpje();
		
		((Visit) getPage().getVisit()).setString1(strjsdid);
		((Visit) getPage().getVisit()).setString2(stryufkid);
		((Visit) getPage().getVisit()).setString3(fapbhs);
		((Visit) getPage().getVisit()).setString4(shoukdw);
		((Visit) getPage().getVisit()).setDouble1(hexyfk);
		((Visit) getPage().getVisit()).setDouble2(fukdfpje);
		((Visit) getPage().getVisit()).setList3(getYufkEditValues());
		((Visit) getPage().getVisit()).setString5("Shengcfkd_dtrl");
		cycle.activate("Createfktzd_dtrl");
	}
	private double fukdfpje;
	public double getFukdfpje(){
		return this.fukdfpje;
	}
	public void setFukdfpje(double fukdfpje){
		this.fukdfpje = fukdfpje;
	}
	
	private double hexyfk;
	public double getHexyfk(){
		return this.hexyfk;
	}
	public void setHexyfk(double hexyfk){
		this.hexyfk = hexyfk;
	}
	
	private void Chax(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		String strjiesbh=((Shengcfkdbean)getEditValues().get(getEditTableRow())).getJiesdbh();
		((Visit) getPage().getVisit()).setString6(strjiesbh);
		((Visit) getPage().getVisit()).setString7(getGongsmcValue().getValue());
		((Visit) getPage().getVisit()).setDiancmc(getDiancmcValue().getValue());
		((Visit) getPage().getVisit()).setString8(getFahdwValue().getValue());
		((Visit) getPage().getVisit()).setString9(getShoukdwValue().getValue());
		((Visit) getPage().getVisit()).setString10(getJieslxValue().getValue());
		((Visit)getPage().getVisit()).setString11("Shengcfkd_dtrl");
		cycle.activate("Balancekfcw_dtrl");
	}

	private static IPropertySelectionModel _DiancmcModel;
	
	public IPropertySelectionModel getDiancmcModel() {
		if (_DiancmcModel == null) {
			getDiancmcModels();
		}
		return _DiancmcModel;
	}

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
		for(int i=0;i<getDiancmcModels().getOptionCount();i++){
			if(((IDropDownBean)getDiancmcModels().getOption(i)).getStrId().equals("199")){
			_DiancmcValue=(IDropDownBean)getDiancmcModels().getOption(i);
			return _DiancmcValue;
			}
		}
		}
		return _DiancmcValue;
	}
	
	private boolean _DiancmcChange=false;
	public void setDiancmcValue(IDropDownBean Value) {
//		if(_DiancmcValue!=null){
			if (_DiancmcValue==Value) {
				_DiancmcChange = false;
			}else{
				_DiancmcChange = true;
			}
//		}else if(Value!=null){
//			_DiancmcChange = true;
//		}
		_DiancmcValue = Value;
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb order by mingc desc";
		_DiancmcModel = new IDropDownModel(sql);
		return _DiancmcModel;
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		_DiancmcModel = _value;
	}
	

	public Date getBeginjsrq() {
		
		return ((Visit) getPage().getVisit()).getDate1() ;
	}
   boolean  _Beginjsrqchange=false;
	public void setBeginjsrq(Date _value) {
		if(((Visit) getPage().getVisit()).getDate1()!=null&&!_value.toString().equals(((Visit) getPage().getVisit()).getDate1().toString())){
			_Beginjsrqchange=true;
		}else{
			_Beginjsrqchange=false;
		}
		((Visit) getPage().getVisit()).setDate1(_value);
	}


	public Date getEndjsrq() {
		
		return ((Visit) getPage().getVisit()).getDate2();
	}
     boolean _Endjsrqchange=false;
	public void setEndjsrq(Date _value) {
		if(((Visit) getPage().getVisit()).getDate2()!=null&&!_value.toString().equals(((Visit) getPage().getVisit()).getDate2().toString())){
			_Endjsrqchange=true;
		}else{
			_Endjsrqchange=false;
		}
		((Visit) getPage().getVisit()).setDate2(_value);
	}

	private Shengcfkdbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Shengcfkdbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Shengcfkdbean EditValue) {
		_EditValue = EditValue;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setList2(null);
			visit.setString11(null);
			visit.setString5(null);
			visit.setList3(null);
			visit.setDouble1(0);
			visit.setString2(null);
			visit.setString1(null);
			visit.setString3(null);
			visit.setString4(null);
			visit.setDouble2(0);
			setFukdfpje(0);
			setHexyfk(0);
			if(visit.getInt1()!=1){
				setBeginjsrq(getMonthFirstday(new Date()));
				setEndjsrq(new Date());
				getDiancmcModels();
				setFahdwValue(null);
				setFahdwModel(null);
				setShoukdwValue(null);
				setShoukdwModel(null);
				setJiesbhValue(null);
				setJiesbhModel(null);
	            getIGongsmcModels();
	            getJieslxModels();
	            getRuzztModels();
	        
			}else{
				getSelectData();
	           // visit.setTiaozzt(0);

				visit.setInt1(0);//	标志调整状态
			}
		}
		if (_Beginjsrqchange) {
			setFahdwModel(null);
			setFahdwValue(null);
			setShoukdwModel(null);
			setShoukdwValue(null);
		}
		if (_Endjsrqchange) {
			setFahdwModel(null);
			setFahdwValue(null);
			setShoukdwModel(null);
			setShoukdwValue(null);
		}
		if(_JiesbhChange){
        	getSelectData();
        }
		if (_DiancmcChange) {
			setJiesbhModel(null);
			setJiesbhValue(null);
			getSelectData();
		}
        if(_GongsmcChange){
        	setJiesbhModel(null);
			setJiesbhValue(null);
        }
        if(_ShoukdwChange){
        	setJiesbhModel(null);
			setJiesbhValue(null);
            getSelectData();
        }
       
        if(_JieslxChange){
        	setJiesbhModel(null);
			setJiesbhValue(null);
            getSelectData();
        }
        
	}
	
    private Shengcfkdbean YufkBean;
    public List getYufkEditValues(){
    	
        return ((Visit)getPage().getVisit()).getList2();
    }
    public void setYufkEditValues(List editList){
    	((Visit)getPage().getVisit()).setList2(editList);
    }
    public Shengcfkdbean getYufkEditValue(){
        return YufkBean;
    }
    public void setYufkEditValue(Shengcfkdbean EditValue){
    	YufkBean = EditValue;
    }
   
    private List getYufkxx(){
    	JDBCcon con = new JDBCcon();
    	List editlist = new ArrayList();
    	try{
    		if(getYufkEditValues()!=null){
    			getYufkEditValues().clear();
    		}
    		String gongysbId = "";
    		if(getShoukdwValue()!=null && getShoukdwValue().getId()!=-1){
    			gongysbId = " and gy.quanc='"+getShoukdwValue().getValue()+"'";
    		}
//    		String sql = "select yf.id,yf.bianh,to_char(yf.riq,'yyyy-mm-dd') as riq,to_char(yf.jine,'FM9999999990.09') jine,to_char(yf.yue,'FM9999999990.09') yue from yufkb yf,gongysb gy where yf.gongysb_id=gy.id and yf.yue>0 "+gongysbId;

    		  
    		String sql="  select yf.id,yf.bianh,to_char(yf.riq,'yyyy-mm-dd') as riq,to_char(yf.jine,'FM9999999990.09') jine,to_char(yf.yue,'FM9999999990.09') yue\n"+
    		   "from yufkb yf,gongysb gy,fuktzb f,liucztb l where yf.gongysb_id=gy.id and yf.yue>0 and yf.fuktzb_id=f.id and f.liucztb_id=l.id\n"+
    		   "and (l.leibztb_id=1 or f.liucztb_id=1)"+gongysbId;
    		ResultSet rs = con.getResultSet(sql);
    		while (rs.next()) {
    			long id = rs.getLong("id");
    			String bianh=rs.getString("bianh");
    			String riq=rs.getString("riq");
    			String yufkje=rs.getString("jine");
    			String yue=rs.getString("yue");
    			double hexyfk=0;
    			boolean seldiv = false;
    			editlist.add(new Shengcfkdbean(id,seldiv,bianh,riq,yufkje,yue,hexyfk));
    		}
    		rs.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		con.Close();
    	}
    	setYufkEditValues(editlist);
    	return getYufkEditValues();
    }
	
	public List getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		String strmk="";
		String stryf="";
		String str = "";

//		str=str+" and gongsxxb_id="+((Visit) getPage().getVisit()).getGongsxxbId();
		
//		if(getDiancmcValue()!=null && getDiancmcValue().getId()!=-1){//电厂名称
//			str=str+" and diancxxb_id="+getDiancmcValue().getId();
//		}
//		if(getFahdwValue()!=null && getFahdwValue().getId()!=-1){//发货单位
//			str=str+" and d.GONGYSMC='"+getFahdwValue().getValue()+"'";
//		}
		if(getShoukdwValue()!=null&&!getShoukdwValue().getValue().equals("请选择")){//收款单位
			if(getShoukdwValue().getValue().equals(" ")){
				str=str+" and d.shoukdw is null ";
			}else{
				str=str+" and d.shoukdw='"+getShoukdwValue().getValue()+"' ";
			}
		}else if(getJiesbhValue()==null || getJiesbhValue().getId()==-1){
			str=str+" and d.shoukdw is not null ";
		}else{
			str=str+"";
		}
		if(getJiesbhValue()!=null && getJiesbhValue().getId()!=-1){//结算编号
			str=str+" and d.bianm='"+getJiesbhValue().getValue()+"'";
		}
//		str = str +" and liuc.leibztb_id<>1   ";
		
		if(getEditValues()!=null && getEditValues().size()>0){
			getEditValues().clear();
		}
		str+=" and jiesrq>="+OraDate(getBeginjsrq())//开始日期
		   +" and jiesrq<"+OraDate(getEndjsrq())+"+1";//结束日期
		try {
			/*String sql = "select * from "
				+ " (select d.id,d.GONGYSMC fahdw,d.shoukdw,dc.jianc as diancmc,d.bianm,d.fapbh,1 as jieslx,to_char(d.jiesrq,'yyyy-MM-dd') as jiesrq,d.jiakhj,d.jiaksk,d.jiasje as meikhj,shulzjbz as hansdj,0 as yunfhj,d.jiessl,d.ches,d.yansrl,d.liuf,d.huiff,d.huif,d.shuif,to_char(d.ruzrq,'yyyy-mm-dd') as ruzrq " 
				+ " from kuangfjsmkb d,diacxxb dc,liucztb liuc where  d.diancxxb_id=dc.id and d.liucztb_id=liuc.id "+str+"  and fuktzb_id=0 and shoukdw is not null order by fahdw,shoukdw,diancmc )";
*///				if(getJieslxValue().getId()==1){
//					sql=" select * from (select y.id,y.fahdw,y.shoukdw,dc.jianc as diancmc,y.bianh,y.fapbh,2 as jieslx,to_char(jiesrq,'yyyy-MM-dd') as jiesrq,0 as jiakhj,0 as jiaksk,0 as meikhj,nvl(yunfhsdj,0) as hansdj,yunzfhj as yunfhj,gongfsl as jiessl,ches,0 as yansrl,0 as liuf,0 as huiff,0 as huif,0 as shuif,to_char(y.ruzrq,'yyyy-mm-dd') as ruzrq  " 
//						+ " from kuangfjsyf  y,diancxxb dc where y.diancxxb_id=dc.id "+str+" and shenhzt=1 and fuktzb_id=0 and jieslx=2 and shoukdw is not null order by fahdw,shoukdw,diancmc )";
//				}
//			sl.jiesdid, std.jies std,Qnetar.jies Qnetar,sl.jies sl
	        
			String sql = "	 select * from \n"+ 
			 "(select d.id,d.GONGYSMC fahdw,d.shoukdw,dc.mingc as diancmc,d.bianm,d.fapbh,1 as jieslx,\n"+ 
			" to_char(d.jiesrq,'yyyy-MM-dd') as jiesrq,/*round(round(d.hansdj*d.jiessl+d.bukmk,2)/1.17,2)*/ d.buhsmk  jiakhj,d.shuik jiaksk,d.hansmk as meikhj,d.hansdj   as hansdj,0 as yunfhj,sl.jies jiessl,\n"+ 
			 "d.ches,Qnetar.jies yansrl,std.jies liuf,0 huiff, 0 huif,0 shuif,to_char(d.ruzrq,'yyyy-mm-dd') as ruzrq  from kuangfjsmkb d,\n"+ 
			 "diancxxb dc,\n"+ 
//			  "      (select sl.jiesdid, std.jies std,Qnetar.jies Qnetar,sl.jies sl from \n"+ 
			  "      (select * from jieszbsjb where zhibb_id=3)  std,\n"+ 
			  "      (select * from jieszbsjb where zhibb_id=2)  Qnetar,\n"+ 
			   "     (select * from jieszbsjb where zhibb_id=1)  sl,\n"+ 
//			    "     where sl.jiesdid=Qnetar.Jiesdid and Qnetar.Jiesdid=std.jiesdid) zhib ,\n"+ 
			    "(select js.id,js.bianm from kuangfjsmkb js where   js.fuktzb_id=0 \n"+  
                  " /* union \n"+
                 " select ku.id,ku.bianm from kuangfjsmkb ku,fuktzb fu,liucztb liuc\n"+
               "  where fu.id=ku.fuktzb_id and fu.liucztb_id=liuc.id and liuc.leibztb_id=0*/) shaix\n"+
			 " where   std.jiesdid(+)=d.id and  Qnetar.jiesdid(+)=d.id and sl.jiesdid(+)=d.id  and  d.diancxxb_id=dc.id and  shaix.id=d.id\n"+
			 str+
			"     and shoukdw is not null and d.liucztb_id ="+BalanceLiuc.getLastStatus("收煤结算")+" order by bianm, fahdw,shoukdw,diancmc )\n";
			
			ResultSet rs = JDBCcon.getResultSet(sql);
			int hangh = 0;
			while (rs.next()) {
				long mid = rs.getLong("ID");
				String mdiancmc = rs.getString("DIANCMC");
				String mjiesdbh = rs.getString("BIANM");
				String mjieslx="";
                if(rs.getInt("JIESLX")==1){
                	mjieslx="煤款";
                }else{
                	mjieslx="运费";
                }
                String mfahdw = rs.getString("fahdw");
                String mshoukdw = rs.getString("shoukdw");
				String mfapbh = rs.getString("fapbh");
				String mjiakhj=formatq(format(rs.getDouble("jiakhj"),"0.00"));
				String mjiaksk=formatq(format(rs.getDouble("jiaksk"),"0.00"));
				String mmeikhj = formatq(format(rs.getDouble("MEIKHJ"),"0.00"));
				double mhansdj = rs.getDouble("HANSDJ");
				String myunfhj = formatq(format(rs.getDouble("YUNFHJ"),"0.00"));
				double mjiessl = rs.getDouble("JIESSL");
				int    mches = rs.getInt("CHES");
				
//				String myufkbh = "";
//				double mhexyfkje = 0;
				
				hangh++;
				_editvalues.add(new Shengcfkdbean(hangh,mid,mfahdw,mshoukdw,mdiancmc,mjiesdbh,mjieslx,mfapbh,
						mjiakhj,mjiaksk,mmeikhj,mhansdj,myunfhj, mjiessl, mches));
//				_editvalues.add(new Shengcfkdbean(hangh,mid,mfahdw,mshoukdw,mdiancmc,mjiesdbh,mjieslx,myufkbh,mfapbh,
//						mjiakhj,mjiaksk,mmeikhj,mhansdj,myunfhj, mjiessl, mches,mhexyfkje));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Shengcfkdbean());
		}
		_editTableRow = -1;
		((Visit) getPage().getVisit()).setList1(_editvalues);
		getYufkxx();
		return ((Visit) getPage().getVisit()).getList1();
	}


//	private static IPropertySelectionModel _JiesbhModel;

	private boolean _JiesbhChange=false;
	public IPropertySelectionModel getJiesbhModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel8() == null) {
			getJiesbhModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel8();
	}

//	private IDropDownBean _JiesbhValue;
	
	public IDropDownBean getJiesbhValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean8()==null){
			((Visit)getPage().getVisit()).setDropDownBean8((IDropDownBean)getJiesbhModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean8();
	}
	
	public void setJiesbhValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean8()==Value) {
	    	_JiesbhChange=false;
		}else{
			_JiesbhChange=true;
		}
		((Visit)getPage().getVisit()).setDropDownBean8(Value);
	}

	public IPropertySelectionModel getJiesbhModels() {
		StringBuffer w_sql = new StringBuffer();
//		w_sql.append(" where ");
		w_sql.append(" ");
		String beginriq="";
		String endriq="";
		String gongsid="";
		String diancid="";
		String fahdw="";
		String shoukdw="";
		String ruzzt="";
		beginriq="   jiesrq>="+OraDate(getBeginjsrq());//开始日期
		endriq=" and jiesrq<"+OraDate(getEndjsrq())+"+1";//结束日期
		w_sql.append(beginriq);
		w_sql.append(endriq);

//		if(getDiancmcValue()!=null && getDiancmcValue().getId()!=-1){//电厂名称
//			diancid=" and diancxxb_id="+getDiancmcValue().getId();
//			w_sql.append(diancid);
//		}
		if(getFahdwValue()!=null && getFahdwValue().getId()!=-1){//发货单位
			fahdw=" and GONGYSMC='"+getFahdwValue().getValue()+"'";
			w_sql.append(fahdw);
		}
		if(getShoukdwValue()!=null && getShoukdwValue().getId()!=-1){//收款单位
			if(getShoukdwValue().getValue().equals(" ")){
				shoukdw = " and shoukdw is null ";
			}else{
				shoukdw = " and shoukdw='"+getShoukdwValue().getValue()+"' ";
			}
			w_sql.append(shoukdw);
		}
//		ruzzt=" and liuc.leibztb_id<>1  ";
		w_sql.append(ruzzt);
//		gongsid = " and gongsxxb_id="+((Visit)getPage().getVisit()).getGongsxxbId();
		w_sql.append(gongsid);
//		if(getRuzztValue()!=null){
//			if(getRuzztValue().getId()==0){
//				ruzzt=" and shenhjb=5";
//				
//			}else if(getRuzztValue().getId()==1){
//				ruzzt=" and shenhjb>=2 and shenhjb<6 ";
//			
//			}else if(getRuzztValue().getId()==2){
//				ruzzt=" and shenhjb=6 ";
//			}
//			w_sql.append(ruzzt);
//		}
		String sql="";
//		if(getJieslxValue()!=null){
//			if(getJieslxValue().getId()==0){
//				sql = " select * from (select js.id,js.bianm from kuangfjsmkb js,liucztb liuc where  liuc.id=js.liucztb_id and js.fuktzb_id=0 " +  w_sql.toString()+" ) ";
//			}else{
//				sql	= " select * from (select yf.id,yf.bianm from kuangfjsyf yf where  fuktzb_id=0 " + w_sql.toString() + " and yf.jieslx<>0 ) ";    
//			}
//			sql = " select * from (select js.id,js.bianm from kuangfjsmkb js,liucztb liuc where  liuc.id=js.liucztb_id and js.fuktzb_id=0 " +  w_sql.toString()+" ) ";
//		}else{
//			sql = "select * from (select id,bianh from ("
//				+ " select js.id,js.bianm,js.gongsxxb_id,js.diancxxb_id,js.diqbm from kuangfjsmkb js where zhuangt=1 and fuktzb_id=0 " +  w_sql.toString()  
//				+ " union "
//				+ " select yf.id,yf.bianh,yf.gongsxxb_id,yf.diancxxb_id,yf.diqbm from kuangfjsyf yf where shenhzt=1 and fuktzb_id=0 " + w_sql.toString() + " and yf.jieslx<>0"    
//				+ " ) ) ";
//		}
//		sql = " select * from (select js.id,js.bianm from kuangfjsmkb js where   js.fuktzb_id=0 " +  w_sql.toString()+" ) ";
		sql="  select * from (select js.id,js.bianm from kuangfjsmkb js where   js.fuktzb_id=0 and  " +  w_sql.toString()+"\n"+ 
			 "    /* union \n"+
			  "    select ku.id,ku.bianm from kuangfjsmkb ku,fuktzb fu,liucztb liuc\n"+ 
			   "    where fu.id=ku.fuktzb_id and fu.liucztb_id=liuc.id and liuc.leibztb_id=0 " +  w_sql.toString()+"*/)\n";
		setJiesbhModel(new IDropDownModel(sql,"请选择"));
		return ((Visit)getPage().getVisit()).getProSelectionModel8();
	}

	public void setJiesbhModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel8(_value);
	}
	
//	结算类型
	
	private static IPropertySelectionModel _JieslxModel;
	private boolean _JieslxChange=false;
	
	public IPropertySelectionModel getJieslxModel() {
		if (_JieslxModel == null) {
			getJieslxModels();
		}
		return _JieslxModel;
	}

	private IDropDownBean _JieslxValue;
	
	public IDropDownBean getJieslxValue() {
		if(_JieslxValue==null){
			_JieslxValue=(IDropDownBean)getJieslxModel().getOption(0);
		}
		return _JieslxValue;
	}
	
	public void setJieslxValue(IDropDownBean Value) {
			if (_JieslxValue==Value) {
		    	
		    	_JieslxChange=false;
			}else{
			    _JieslxChange=true;
			}
		_JieslxValue = Value;
	}

	public IPropertySelectionModel getJieslxModels() {
		
		List arryJieslx=new ArrayList();
        arryJieslx.add(new IDropDownBean(0,"煤款"));
        arryJieslx.add(new IDropDownBean(1,"运费"));
		_JieslxModel = new IDropDownModel(arryJieslx);
		return _JieslxModel;
	}

	public void setJieslxModel(IPropertySelectionModel _value) {
	    _JieslxModel = _value;
	}
	
//	结算类型End
    
    private boolean _GongsmcChange=false;
    private IDropDownBean _GongsmcValue;

    public IDropDownBean getGongsmcValue() {
    	if(_GongsmcValue==null){
    		_GongsmcValue=(IDropDownBean)getIGongsmcModels().getOption(0);
    	}
        return _GongsmcValue;
    }

    public void setGongsmcValue(IDropDownBean Value) {
//    	if(_GongsmcValue!=null){
    		if(_GongsmcValue==Value){
                _GongsmcChange=false;
            }else{
            	_GongsmcChange=true;
            }
//    	}else if(Value!=null){
//    		_GongsmcChange=true;
//    	}
        _GongsmcValue = Value;
    }

    private static IPropertySelectionModel _IGongsmcModel;

    public void setIGongsmcModel(IPropertySelectionModel value) {
        _IGongsmcModel = value;
    }

    public IPropertySelectionModel getIGongsmcModel() {
        if (_IGongsmcModel == null) {
            getIGongsmcModels();
        }
        return _IGongsmcModel;
    }

    public IPropertySelectionModel getIGongsmcModels() {
        String sql="select id,mingc from diancxxb where jib=2  order by mingc ";
        _IGongsmcModel = new IDropDownModel(sql);
        return _IGongsmcModel;
    }
//	发货单位下拉框
//    private static IPropertySelectionModel _FahdwModel;

	private boolean _FahdwChange=false;
	public IPropertySelectionModel getFahdwModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel9() == null) {
			getFahdwModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel9();
	}

//	private IDropDownBean _FahdwValue;
	
	public IDropDownBean getFahdwValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean9()==null){
			((Visit)getPage().getVisit()).setDropDownBean9((IDropDownBean)getFahdwModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean9();
	}
	
	public void setFahdwValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean9()==Value) {
	    	_FahdwChange=false;
		}else{
			_FahdwChange=true;
		}
		((Visit)getPage().getVisit()).setDropDownBean9(Value);
	}

	public IPropertySelectionModel getFahdwModels() {
		StringBuffer w_sql = new StringBuffer();
		JDBCcon con = new JDBCcon();
		w_sql.append(" where ");
		String beginriq="";
		String endriq="";
		beginriq=" jiesrq>="+OraDate(getBeginjsrq());//开始日期
		endriq=" and jiesrq<="+OraDate(getEndjsrq());//结束日期
		w_sql.append(beginriq);
		w_sql.append(endriq);
//		if(getGongsmcValue()!=null && getGongsmcValue().getId()!=-1){//公司名称
//			gongsid=" and gongsxxb_id="+getGongsmcValue().getId();
//			w_sql.append(gongsid);
//		}
//		if(getDiancmcValue()!=null && getDiancmcValue().getId()!=-1){//电厂名称
//			diancid=" and diancxxb_id="+getDiancmcValue().getId();
//			w_sql.append(diancid);
//		}
		
		String sql="";
		
		sql = "select distinct fahdw from ("
			+ " select js.id,js.bianm,js.diancxxb_id,js.GONGYSMC fahdw from kuangfjsmkb js " +  w_sql.toString() +"    and js.fuktzb_id=0\n" 
			+ " /*union "
			+ " select yf.id,yf.bianh,yf.gongsxxb_id,yf.diancxxb_id,yf.fahdw from kuangfjsyf yf " + w_sql.toString() + " and shenhjb>=1 and shenhjb<6 and shenhzt=1 and yf.jieslx<>0*/"    
			+ " ) order by fahdw ";
		ResultSet rs = con.getResultSet(sql);
		List Fahdwlist = new ArrayList(); 
		try{
			int i = 0;
			Fahdwlist.add(new IDropDownBean(-1,"请选择"));
			while(rs.next()){
				Fahdwlist.add(new IDropDownBean(i++,rs.getString("fahdw")));
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		setFahdwModel(new IDropDownModel(Fahdwlist));
		return ((Visit)getPage().getVisit()).getProSelectionModel9();
	}

	public void setFahdwModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel9(_value);
	}
//	收款单位下拉框
//	private static IPropertySelectionModel _ShoukdwModel;

	private boolean _ShoukdwChange=false;
	public IPropertySelectionModel getShoukdwModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getShoukdwModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}

//	private IDropDownBean _ShoukdwValue;
	
	public IDropDownBean getShoukdwValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getShoukdwModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
	public void setShoukdwValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean10()==Value) {
	    	_ShoukdwChange=false;
		}else{
			_ShoukdwChange=true;
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getShoukdwModels() {
		
		List dropdownlist = new ArrayList();  
        JDBCcon con = new JDBCcon();
        int i=0;
        
		StringBuffer w_sql = new StringBuffer();
//		w_sql.append(" where ");
		String beginriq="";
		String endriq="";
		beginriq=" jiesrq>="+OraDate(getBeginjsrq());//开始日期
		endriq=" and jiesrq<"+OraDate(getEndjsrq())+"+1";//结束日期
		w_sql.append(beginriq);
		w_sql.append(endriq);
		
		String sql="";
		
//		sql = "select distinct 1 as id,shoukdw from ("
//			+ " select js.id,js.bianh,js.gongsxxb_id,js.diancxxb_id,js.shoukdw from kuangfjsmkb js where shenhjb>=1 and shenhjb<6 and zhuangt=1 and fuktzb_id=0 and shoukdw is not null " 
//			+ " union "
//			+ " select yf.id,yf.bianh,yf.gongsxxb_id,yf.diancxxb_id,yf.shoukdw from kuangfjsyf yf where shenhjb>=1 and shenhjb<6 and shenhzt=1 and fuktzb_id=0 and shoukdw is not null  and yf.jieslx<>0"    
//			+ " ) order by shoukdw ";
		
//		sql = "select distinct 1 as id,decode(shoukdw,null,' ',shoukdw) as shoukdw from ("
//			+ " select js.id,js.bianh,js.gongsxxb_id,js.diancxxb_id,js.shoukdw from kuangfjsmkb js" +  w_sql.toString() +" and shenhjb>=1 and shenhjb<6 and zhuangt=1" 
//			+ " union "
//			+ " select yf.id,yf.bianh,yf.gongsxxb_id,yf.diancxxb_id,yf.shoukdw from kuangfjsyf yf " + w_sql.toString() + " and shenhjb>=1 and shenhjb<6 and shenhzt=1 and yf.jieslx<>0"    
//			+ " ) order by shoukdw ";

	sql="	select distinct 1 as id,shoukdw from \n"+
		"( select  js.liucztb_id,js.shoukdw, js.id,js.diancxxb_id,js.GONGYSMC fahdw from kuangfjsmkb js\n"+
		 "  where fuktzb_id=0 and\n"+w_sql.toString()+
		 " )     order by shoukdw \n";
		try{
	        ResultSet rs = con.getResultSet(sql);
	        dropdownlist.add(new IDropDownBean(-1,"请选择"));
	        while (rs.next()) {
//	           int id = rs.getInt(1);
	           dropdownlist.add(new IDropDownBean(i,rs.getString(2)));
	           i++;
	        }
	        rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            con.Close();
        }
		setShoukdwModel(new IDropDownModel(dropdownlist));
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}

	public void setShoukdwModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel10(_value);
	}
	
//入账状态
	
	private static IPropertySelectionModel _RuzztModel;
	
	public IPropertySelectionModel getRuzztModel() {
		if (_RuzztModel == null) {
			getRuzztModels();
		}
		return _RuzztModel;
	}

	private IDropDownBean _RuzztValue;

	public IDropDownBean getRuzztValue() {
		if(_RuzztValue==null){
			_RuzztValue=((IDropDownBean)getRuzztModel().getOption(0));
		}
		return _RuzztValue;
	}
	
	private boolean _RuzztChange=false;
	public void setRuzztValue(IDropDownBean Value) {
		if (_RuzztValue==Value) {
			_RuzztChange = false;
		}else{
			_RuzztChange = true;
		}
		_RuzztValue = Value;
	}

	public IPropertySelectionModel getRuzztModels() {
		List listRuzzt=new ArrayList();
		listRuzzt.add(new IDropDownBean(0, "待入账"));
		listRuzzt.add(new IDropDownBean(1, "未入账"));
//		listRuzzt.add(new IDropDownBean(2, "已入账"));
		
		_RuzztModel = new IDropDownModel(listRuzzt);
		return _RuzztModel;
	}

	public void setRuzztModel(IPropertySelectionModel _value) {
		_RuzztModel = _value;
	}
	
//	格式化
	/*
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	*/
	
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
	
//	修改入账日期
	
	private Date _SelRuzrqValue=new Date();
	public Date getRuzrq() {
		if (_SelRuzrqValue == null) {
			_SelRuzrqValue=new Date();
		}
		return _SelRuzrqValue;
	}
	public void setRuzrq(Date selRuzrq) {
		_SelRuzrqValue = selRuzrq;
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
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
	/* （非 Javadoc）
     * @see org.apache.tapestry.AbstractPage#detach()
     */
    public void detach() {
        // TODO 自动生成方法存根
        super.detach();
    }
//    public String getPageHome() {
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