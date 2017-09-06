package com.zhiren.zidy.duibcx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Table;
/*
 * 作者：王磊
 * 时间：2009-08-27 02：32
 * 描述：增加对比期的设置处理
 */
/*
 * 作者：王磊
 * 时间：2009-08-18 11：43
 * 描述：处理JDBCcon未关闭的问题,电厂默认选择的问题
 */
/**
 * @author Rock
 * @since 2009-04-18
 * @version v2.0
 */
public class DuibcxModify extends BasePage implements PageValidateListener{
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg("");
	}
	/**
	 *	定义各种资源 
	 */
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String FieldChange;
	
	public String getFieldChange() {
		return FieldChange;
	}
	public void setFieldChange(String value) {
		FieldChange = value;
	}
//	标题
	private String title;
	public String getTitle(){
		return title;
	}
	public void setTitle(String value){
		title = value;
	}
//	宽度
	private int width;
	public int getWidth(){
		return width;
	}
	public void setWidth(int wd){
		width = wd;
	}
//	供应商
	private String Gongys;
	public String getGongys(){
		return Gongys;
	}
	public void setGongys(String gys){
		Gongys = gys;
	}
//	电厂
	private String dianc;
	public String getDianc(){
		return dianc;
	}
	public void setDianc(String dc){
		dianc = dc;
	}
//	数据源ID
	private String DataSrcId;
	public String getDataSrcId(){
		return DataSrcId;
	}
	public void setDataSrcId(String dsid){
		DataSrcId = dsid;
	}
//	数据列ID
	private String ColId;
	public String getColId(){
		return ColId;
	}
	public void setColId(String clid){
		ColId = clid;
	}
//	数据源列下拉列表
	public IDropDownBean getDataSrcValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getDataSrcModel().getOptionCount()>0) {
				setDataSrcValue((IDropDownBean)getDataSrcModel().getOption(0));
				setDataSrcId(((IDropDownBean)getDataSrcModel().getOption(0)).getStrId());
			}
		}else{
			if(getDataSrcId()!=null || !"".equals(getDataSrcId())){
				for(int i = 0; i< getDataSrcModel().getOptionCount(); i++){
					if(getDataSrcId().equals(((IDropDownBean)getDataSrcModel().getOption(i)).getStrId())){
						setDataSrcValue(((IDropDownBean)getDataSrcModel().getOption(i)));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setDataSrcValue(IDropDownBean value) {
		if(value==null)return;
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	public IPropertySelectionModel getDataSrcModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setDataSrcModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setDataSrcModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setDataSrcModels() {
		String sql = "select j.id,j.z_title_cn from zidyjcsjy j where j.z_remark = '综合对比分析'";
		setDataSrcModel(new IDropDownModel(sql));
	}
//	数据源列
	public IDropDownBean getdsColumnValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getdsColumnModel().getOptionCount()>0) {
				setdsColumnValue((IDropDownBean)getdsColumnModel().getOption(0));
				setColId(((IDropDownBean)getdsColumnModel().getOption(0)).getStrId());
			}
		}else{
			if(getColId() != null && !"".equals(getColId())){
				for(int i=0;i<getdsColumnModel().getOptionCount();i++){
					if(getColId().equals(((IDropDownBean)getdsColumnModel().getOption(i)).getStrId())){
						setdsColumnValue((IDropDownBean)getdsColumnModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setdsColumnValue(IDropDownBean value) {
		if(value==null)return;
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	public IPropertySelectionModel getdsColumnModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setdsColumnModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setdsColumnModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}
	public void setdsColumnModels() {
		String sql = "select id,z_column_cn  from zidyjcsjyms where zidyjcsjy_id = " + (getDataSrcId()==null?"-1":getDataSrcId()) + " and z_isDataCol = 1";
		setdsColumnModel(new IDropDownModel(sql));
	}
//	电厂下拉列表
	public IDropDownBean getDiancValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getDiancModel().getOptionCount()>0) {
				setDiancValue((IDropDownBean)getDiancModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setDiancValue(IDropDownBean value) {
//		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	public IPropertySelectionModel getDiancModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setDiancModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setDiancModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void setDiancModels() {
		long dcid = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		String sql = 
			"select -1 id,'' mingc from dual union select id,mingc from vwdianc where fuid = "+dcid+" or id = "+dcid;
		setDiancModel(new IDropDownModel(sql));
	}
//	供应商下拉列表
	public IDropDownBean getGongysValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getGongysModel().getOptionCount()>0) {
				setGongysValue((IDropDownBean)getGongysModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setGongysValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(_value);
	}

	public void setGongysModels() {
		long dcid = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		String sql = 
			"select -1 id,'' mingc from dual union select g.id,g.mingc from vwgongys g,gongysdcglb l,vwdianc d\n" +
			"where l.gongysb_id = g.id and l.diancxxb_id = d.id\n" + 
			"and (d.fuid = "+dcid+" or d.id = "+dcid+")\n" + 
			"and g.id = g.dqid";
		setGongysModel(new IDropDownModel(sql));
	}
	
	private void setDefaultFommValue(){
		if(getColId()!=null && !"".equals(getColId())){
			String sql = "select * from zidyjcsjyms where id = " + getColId();
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con.getResultSetList(sql);
			if(rsl.next()){
				setTitle(rsl.getString("z_column_cn"));
				setWidth(rsl.getInt("z_width"));
			}
			rsl.close();
			con.Close();
		}
	}

	//根据本列序号确定列标识
	public String getCurrentLiebs(int Xuhao) {
		char[] zim = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z' };
		if (Xuhao <= 26) {
			return String.valueOf(zim[Xuhao - 1]);
		}
		StringBuffer sb = new StringBuffer();
		int firstYus = Xuhao % 26;
		int yus = firstYus;
		while (Xuhao >= 26) {
			if (yus > 0) {
				sb.append(String.valueOf(zim[yus - 1]));
			} else if (firstYus == 0) {
				sb.append(String.valueOf(zim[25]));
			} else {
				sb.append(String.valueOf(zim[0]));
			}
			Xuhao = Xuhao / 26;
			yus = Xuhao % 26;
		}
		if (firstYus > 0) {
			sb.append(String.valueOf(zim[Xuhao - 1]));
		}
		char[] reverses = sb.toString().toCharArray();
		char[] reverses2 = new char[reverses.length];
		for (int i = 0, j = reverses.length - 1; i < reverses.length; i++, j--) {
			reverses2[j] = reverses[i];
		}
		return new String(reverses2);
	}
	
	
	//根据上列的列标识确定本列标识,如果上列标识为空和null，则返回“A”，如果字符中有空格就过滤掉
	public String getCurrentLiebs(String previousBiaoshi) {
		if (previousBiaoshi == "" || previousBiaoshi == null) {
			return "A";
		}
		previousBiaoshi = previousBiaoshi.toUpperCase();
		previousBiaoshi = previousBiaoshi.trim();
		char[] pTochar = previousBiaoshi.toCharArray();
		pTochar[pTochar.length - 1]++;
		char[] pTochar1 = new char[pTochar.length + 1];
		for (int i = pTochar.length - 1; i >= 0; i--) {
			pTochar1[i + 1] = pTochar[i];
		}
		pTochar1[0] = ' ';
		for (int i = pTochar1.length - 1; i > 0; i--) {
			if (pTochar1[i] > 'Z') {
				pTochar1[i] = 'A';
				if (pTochar1[i - 1] == ' ') {
					pTochar1[i - 1] = 'A';
				} else {
					pTochar1[i - 1]++;
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= pTochar1.length - 1; i++) {
			if (pTochar1[i] != ' ') {
				sb.append(pTochar1[i]);
			}
		}
		return sb.toString();
	}
	

	
	
	
	private boolean _RefurbishDataClick = false;
	public void RefurbishDataSourceButton(IRequestCycle cycle) {
		_RefurbishDataClick = true;
	}
	private void RefurbishDataSource(){
		if("1".equals(getChange())){
//			setColId(null);
			Visit v = ((Visit)this.getPage().getVisit());
			v.setDropDownBean2(null);
			setdsColumnModels();
			getdsColumnValue();
			setChange("");
		}
		setDefaultFommValue();
	}
	
	private boolean _ReturnClick = false;
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}
	private void Return(IRequestCycle cycle){
		cycle.activate("DuibcxIndex");
	}
	
	
	private boolean _InsertFormulaClick = false;
	public void InsertFormulaButton(IRequestCycle cycle) {
		_InsertFormulaClick = true;
	}
	private boolean _InsertColClick = false;
	public void InsertColButton(IRequestCycle cycle) {
		_InsertColClick = true;
	}
	private void InsertCol(boolean formula){
		JDBCcon con = new JDBCcon();
		Visit v = ((Visit)this.getPage().getVisit());
		long diancxxb_id = v.getDiancxxb_id();
		String changes = getChange();
		String[] change = changes.split(";");
		HashMap colSignmap = new HashMap();
		String ReportId = v.getString1();
		String colHead = getTitle();
		int colWidth = getWidth();
		for(int i = 0; i < change.length; i++){
			String colSubHead = change[i];
			String colId = MainGlobal.getNewID(diancxxb_id);
			
			String colType = DuibcxOperation.Duibcx_ColType_Formula;
			String colNum = "1";
			String colSign = "A";
			String paramPointBegin = "0";
			String paramPointEnd = "0";
			String sql = "select nvl(max(colnum)+1,1) xuh from duibcxsjlb where zidyfa_id = " + ReportId;  
			ResultSetList rsl = con.getResultSetList(sql);
			if(rsl.next()){
				colNum = rsl.getString("xuh");
			}
			rsl.close();
			colSign = getCurrentLiebs(Integer.parseInt(colNum));
			colSignmap.put(colSubHead, colSign);
			String colFormula = null;
			if(formula){
				colFormula = getFieldChange();
			}else{
				if("环比".equals(colSubHead)){
					colFormula = "(" + colSignmap.get("本期") + "-" + colSignmap.get("上期") + ")/" + colSignmap.get("上期") + "*100";
				}else if("同比".equals(colSubHead)){
					colFormula = "(" + colSignmap.get("本期") + "-" + colSignmap.get("同期") + ")/" + colSignmap.get("同期") + "*100";
				}else if("累比".equals(colSubHead)){
					colFormula = "(" + colSignmap.get("累计") + "-" + colSignmap.get("同期累计") + ")/" + colSignmap.get("同期累计") + "*100";
				}else{
					colType = DuibcxOperation.Duibcx_ColType_DataSource;
					if("本月".equals(colSubHead)){
						
					}else if("上期".equals(colSubHead)){
						paramPointBegin = "-1";
						paramPointEnd = "-1";
					}else if("同期".equals(colSubHead)){
						paramPointBegin = "-12";
						paramPointEnd = "-12";
					}else if("累计".equals(colSubHead)){
						paramPointBegin = "1-起始月份";
						paramPointEnd = "0";
					}else if("同期累计".equals(colSubHead)){
						paramPointBegin = "-起始月份 -11";
						paramPointEnd = "-12";
					}else if("对比期".equals(colSubHead)){
						String[] fc = getFieldChange().split(";");
						paramPointBegin = fc[0];
						paramPointEnd = fc[1];
					}
				}
			}
//			总列
			sql = "insert into duibcxsjlb(id,zidyfa_id,colnum,colsign,coltype) values(" + colId
			+ "," + ReportId + "," + colNum + ",'" + colSign + "'," + colType + ")";
			con.getInsert(sql);
//			列标题
			InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColHead,colHead);
//			副标题
			InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColSubHead,colSubHead);
//			列宽
			InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColWidth,String.valueOf(colWidth));
			if(colType.equals(DuibcxOperation.Duibcx_ColType_DataSource)){
//				数据源
				InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_DataSource,getDataSrcId());
//				数据源列
				InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_DataSourceCol,getColId());
				sql = "select * from zidyjcsjyms where id = " + getColId();
				rsl = con.getResultSetList(sql);
				if(rsl.next()){
					InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColWord,rsl.getString("z_column"));
					InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColFormat,rsl.getString("z_format"));
					InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColOperational,rsl.getString("z_operational"));
					InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColWeighted,rsl.getString("z_weighted"));
					InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColAlign,rsl.getString("z_align"));
				}
				rsl.close();
				sql = "select z_column from zidyjcsjyms where zidyjcsjy_id =" + getDataSrcId() + " and z_datatypes='date' and z_isDataCol = 0";
				rsl = con.getResultSetList(sql);
				if(rsl.next()){
//					参数列
					InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_SourceParamCol,rsl.getString("z_column"));
					InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ParamPointBegin,paramPointBegin);
					InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ParamPointEnd,paramPointEnd);
				}
				rsl.close();
			}else{
//				公式列
				InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColFormat,"0.00");
				InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColAlign,"2");
				InsertConfiguration(con,diancxxb_id,colId,SysConstant.CustomAttribute_ColFormula,colFormula);
			}
		}
		con.Close();
		setMsg("添加成功");
	}
	
	public void InsertConfiguration(JDBCcon con,long diancxxb_id,String colId,String configurationType,String value){
		String sql = "insert into duibcxsjlpzb(id,duibcxsjlb_id,z_code,z_value) values(getnewid("
			+ diancxxb_id + ")," + colId + ",'"+configurationType+"','" + value + "')";
		con.getInsert(sql);
	}
	
	//private void Insert
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		JDBCcon con = new JDBCcon();
//		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		String sql = "";
		if (rsl == null) {
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rsl.next()){
			sql = "delete from duibcxsjlpzb where duibcxsjlb_id = " + rsl.getString("id");
			con.getDelete(sql);
			sql = "delete from duibcxsjlb where id = " + rsl.getString("id");
			con.getDelete(sql);
		}
		rsl.close();
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rsl.next()){
			sql = "update duibcxsjlpzb set z_value = '" + rsl.getString("SubHead") 
			+ "' where duibcxsjlb_id = " + rsl.getString("id") + " and z_code = '"
			+SysConstant.CustomAttribute_ColSubHead + "'";
			con.getUpdate(sql);
			sql = "update duibcxsjlpzb set z_value = '" + rsl.getString("ColWidth") 
			+ "' where duibcxsjlb_id = " + rsl.getString("id") + " and z_code = '"
			+SysConstant.CustomAttribute_ColWidth + "'";
			con.getUpdate(sql);
			sql = "update duibcxsjlpzb set z_value = '" + 
			getExtGrid().getColumn("ColAlign").combo.getBeanStrId(rsl.getString("ColAlign")) 
			+ "' where duibcxsjlb_id = " + rsl.getString("id") + " and z_code = '"
			+SysConstant.CustomAttribute_ColAlign + "'";
			con.getUpdate(sql);
			sql = "update duibcxsjlpzb set z_value = '" + rsl.getString("ColFormat") 
			+ "' where duibcxsjlb_id = " + rsl.getString("id") + " and z_code = '"
			+SysConstant.CustomAttribute_ColFormat + "'";
			con.getUpdate(sql);
		}
		rsl.close();
		con.Close();
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishDataClick) {
			_RefurbishDataClick = false;
			RefurbishDataSource();
		}
		if (_InsertColClick) {
			_InsertColClick = false;
			InsertCol(false);
			initGrid();
		}
		if (_InsertFormulaClick) {
			_InsertFormulaClick = false;
			InsertCol(true);
			initGrid();
		}
		
		if (_ReturnClick) {
			_ReturnClick = false;
			Return(cycle);
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			initGrid();
		}
	}
	
	public void initItems(){
		Visit v = ((Visit)this.getPage().getVisit());
//		初始化数据源
		v.setDropDownBean1(null);
		setDataSrcModel(null);
//		初始化数据列
		v.setDropDownBean2(null);
		setdsColumnModel(null);
//		初始化电厂
		setDiancValue(null);
		setDiancModel(null);
//		初始化供应商
		setGongysValue(null);
		setGongysModel(null);
//		初始化GRID 这里需要更改 应该把数据传到JS中即可 现在是把整个grid又构造了一遍
		
		initGrid();
	}
	
	public void initGrid(){
		JDBCcon con = new JDBCcon();
		Visit v = ((Visit)this.getPage().getVisit());
		String sql = 
			"select d.id,d.colsign,\n" +
			"(select p.z_value from duibcxsjlpzb p where p.duibcxsjlb_id = d.id and p.z_code = '"+SysConstant.CustomAttribute_ColHead+"') MainHead,\n" + 
			"(select p.z_value from duibcxsjlpzb p where p.duibcxsjlb_id = d.id and p.z_code = '"+SysConstant.CustomAttribute_ColSubHead+"') SubHead,\n" + 
			"(select p.z_value from duibcxsjlpzb p where p.duibcxsjlb_id = d.id and p.z_code = '"+SysConstant.CustomAttribute_ColWidth+"') ColWidth,\n" + 
			"decode((select p.z_value from duibcxsjlpzb p where p.duibcxsjlb_id = d.id and p.z_code = '"
			+SysConstant.CustomAttribute_ColAlign+"'),"+Table.ALIGN_LEFT+",'左',"+Table.ALIGN_CENTER+",'中','右') ColAlign,\n" +
			"(select p.z_value from duibcxsjlpzb p where p.duibcxsjlb_id = d.id and p.z_code = '"+SysConstant.CustomAttribute_ColFormat+"') ColFormat\n" +
			"from duibcxsjlb d where d.zidyfa_id= "+v.getString1()+" order by colnum";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		设置GRID可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.getColumn("colsign").setHeader("列标识");
		egu.getColumn("colsign").setWidth(80);
		egu.getColumn("colsign").setEditor(null);
		egu.getColumn("MainHead").setHeader("主标题");
		egu.getColumn("MainHead").setWidth(80);
		egu.getColumn("MainHead").setEditor(null);
		egu.getColumn("SubHead").setHeader("副标题");
		egu.getColumn("SubHead").setWidth(80);
		//egu.getColumn("SubHead").setEditor(null);
		egu.getColumn("ColWidth").setHeader("列宽");
		egu.getColumn("ColWidth").setWidth(80);
		egu.getColumn("ColAlign").setHeader("对齐");
		egu.getColumn("ColAlign").setWidth(80);
		egu.getColumn("ColFormat").setHeader("格式");
		egu.getColumn("ColFormat").setWidth(80);
		//egu.getColumn("ColWidth").setEditor(null);
		List ls = new ArrayList();
		ls.add(new IDropDownBean(Table.ALIGN_LEFT, "左"));
		ls.add(new IDropDownBean(Table.ALIGN_CENTER, "中"));
		ls.add(new IDropDownBean(Table.ALIGN_RIGHT, "右"));
		ComboBox c = new ComboBox();
		egu.getColumn("ColAlign").setEditor(c);
		c.setEditable(true);
		egu.getColumn("ColAlign").setComboEditor(egu.gridId,
				new IDropDownModel(ls));
		egu.getColumn("ColAlign").setReturnId(true);
		
		egu.setEL(false);
		egu.setWidth("bodyWidth-380");
		egu.setHeight("bodyHeight-160");
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getDataScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
//	 ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
//		cycle.
	    Visit v = (Visit) getPage().getVisit();
		if (!v.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if(!v.getActivePageName().toString().equals("DuibcxIndex")){
				throw new PageRedirectException("ErrorPage");
			}
			v.setActivePageName(getPageName().toString());
			initItems();
			setDataSrcModels();
			getDataSrcValue();
			setdsColumnModels();
			getdsColumnValue();
			setDefaultFommValue();
		}
	}
	
	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
}