package com.zhiren.dc.jilgl.jih;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;

public abstract class Jihcy extends BasePage {
//	进行页面提示信息的设置
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
    
    public String getCheph(){
    	Visit v = (Visit) getPage().getVisit();
    	return v.getString1();
    }
    public void setCheph(String s){
    	Visit v = (Visit) getPage().getVisit();
    	v.setString1(s);
    }
    public String getChed(){
    	Visit v = (Visit) getPage().getVisit();
    	return v.getString2();
    }
    public void setChed(String s){
    	Visit v = (Visit) getPage().getVisit();
    	v.setString2(s);
    }
    public String getZhuangt(){
    	Visit v = (Visit) getPage().getVisit();
    	return v.getString3();
    }
    public void setZhuangt(String s){
    	Visit v = (Visit) getPage().getVisit();
    	v.setString3(s);
    }
    public String getXuh(){
    	Visit v = (Visit) getPage().getVisit();
    	return v.getString4();
    }
    public void setXuh(String s){
    	Visit v = (Visit) getPage().getVisit();
    	v.setString4(s);
    }
    public String getCurXuh(){
    	Visit v = (Visit) getPage().getVisit();
    	return v.getString5();
    }
    public void setCurXuh(String s){
    	Visit v = (Visit) getPage().getVisit();
    	v.setString5(s);
    }
    public String getMeikbm(){
    	Visit v = (Visit) getPage().getVisit();
    	return v.getString6();
    }
    public void setMeikbm(String s){
    	Visit v = (Visit) getPage().getVisit();
    	v.setString6(s);
    }
    public String getMeikmc(){
    	Visit v = (Visit) getPage().getVisit();
    	return v.getString7();
    }
    public void setMeikmc(String s){
    	Visit v = (Visit) getPage().getVisit();
    	v.setString7(s);
    }
    public double getKoud(){
    	Visit v = (Visit) getPage().getVisit();
    	return v.getDouble1();
    }
    public void setKoud(double s){
    	Visit v = (Visit) getPage().getVisit();
    	v.setDouble1(s);
    }
    public String getCuny(){
    	Visit v = (Visit) getPage().getVisit();
    	return v.getString8();
    }
    public void setCuny(String s){
    	Visit v = (Visit) getPage().getVisit();
    	v.setString8(s);
    }
    
    public IPropertySelectionModel getCunywzModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setCunywzModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setCunywzModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setCunywzModels() {
		String sql = "select id,mingc from cunywzb order by mingc";
		setCunywzModel(new IDropDownModel(sql));
	}
	
	public IDropDownBean getCunywz(){
		if(((Visit) this.getPage().getVisit()).getDropDownBean1()==null){
			getCunywzModel();
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}
	public void setCunywz(IDropDownBean cywz){
		((Visit) this.getPage().getVisit()).setDropDownBean1(cywz);
	}
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			setCheph("");
			setChed("");
			setZhuangt("");
			setXuh("");
			setCurXuh("");
			setMeikbm("");
			setMeikmc("");
			setKoud(0.0);
			setCuny("2桶");
		}
	}

//	按钮事件处理
    private boolean _ChehChick = false;
    public void ChehButton(IRequestCycle cycle) {
        _ChehChick = true;
    }
    
    private void setChepxx(){
    	Visit v = (Visit) getPage().getVisit();
    	JDBCcon con = new JDBCcon();
    	if(getCheph() == null || "".equals(getCheph())){
    		setMsg("没有该车号对应的信息！");
    		con.Close();
    		return;
    	}
    	String sql = 
    		"select y.mingc from chelxxb c,yunsdwb y\n" +
    		"where c.yunsdwb_id = y.id and c.diancxxb_id = "+ v.getDiancxxb_id() +
    		"\n and c.cheph = '"+getCheph()+"'";
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl.getRows() == 0){
    		setMsg("没有该车号对应的信息！");
    		rsl.close();
    		con.Close();
    		return;
    	}
    	if(rsl.next()){
    		setChed(rsl.getString("mingc"));
    	}
    	rsl.close();
    	sql = "select jincxh from Jihcpb where diancxxb_id ="+
    	v.getDiancxxb_id() + " and cheph ='"+getCheph() + "' and riq = "
    	+DateUtil.FormatOracleDate(new Date()) + " and jihk_ID = 0";
    	rsl = con.getResultSetList(sql);
    	if(rsl.getRows() == 0){
    		setMsg("没有采集进厂序号！");
    		rsl.close();
    		con.Close();
    		return;
    	}else if(rsl.getRows() > 1 ){
    		setMsg("进厂序号重复采集！");
    		rsl.close();
    		con.Close();
    		return;
    	}
    	if(rsl.next()){
    		setXuh(rsl.getString("jincxh"));
    	}
    	rsl.close();
    	sql = "select nvl(max(caiyxh),0) caiyxh from jihcpb where riq = "
    		+ DateUtil.FormatOracleDate(new Date()) + " and diancxxb_id ="
    		+ v.getDiancxxb_id() ;
    	rsl = con.getResultSetList(sql);
    	if(rsl.next()){
    		setCurXuh(rsl.getString("caiyxh"));
    	}
    	rsl.close();
    	sql = "select zhuangt from (select * from jihcpb where cheph = '"+getCheph()+"' and jihk_id != 0"
    		+ "order by jincsj desc) where rownum = 1";
    	rsl = con.getResultSetList(sql);
    	if(rsl.next()){
    		setZhuangt(rsl.getString("zhuangt"));
    	}else{
    		setZhuangt("正常");
    	}
    	rsl.close();
    	con.Close();
    }
    
    private boolean _MeikChick = false;
    public void MeikButton(IRequestCycle cycle) {
        _MeikChick = true;
    }
    private void setMeikxx(){
    	if("0000000000123".equals(getMeikbm())){
    		setMeikmc("测试煤矿");
//    		查找设置存样位置
    	}
    }
    
	private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
    	if (_ChehChick) {
    		_ChehChick = false;
    		setChepxx();
        }
    	if (_MeikChick) {
    		_MeikChick = false;
    		setMeikxx();
        }
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    }
//	保存分组的改动
	private void Save() {
		Visit v = (Visit)this.getPage().getVisit();
    	JDBCcon con = new JDBCcon();
    	String jihk_id = null;
    	String sql = "select * from jihkbmb where tiaoxm = '"+ getMeikbm() + "'";
    	ResultSetList rs = con.getResultSetList(sql);
    	if(rs.getRows() == 0){
    		setMsg("煤矿编码错误！");
    		rs.close();
    		con.Close();
    		return;
    	}
    	if(rs.next()){
    		jihk_id = rs.getString("Jihkxxb_id");
    	}
    	double koud = getKoud();
    	long caiyxh = Long.parseLong(getCurXuh())+1;
    	long cunywz =  ((IDropDownModel)getCunywzModel()).getBeanId(getCuny());
		sql = "update jihcpb set zhuangt = '"+getZhuangt() 
		+ "',jihk_id="+jihk_id+",cunywzb_id="+cunywz+",koud="+koud+",caiyxh="+caiyxh
		+ " where cheph='"+getCheph()+"' and jihk_id =0";
		con.getUpdate(sql);
    	con.Close();
	}
}
