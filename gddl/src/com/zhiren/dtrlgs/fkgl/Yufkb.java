package com.zhiren.dtrlgs.fkgl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
//import com.zhiren.common.Sequence;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.dtrlgs.pubclass.BalanceLiuc;
import com.zhiren.main.Visit;
//import com.zhiren.rmis.hesgl.fukdgl.Createfktzdbean;

public class Yufkb extends BasePage {
	private String msg = "";
	private boolean returnId = false;
	private IPropertySelectionModel saveModel=null;
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
        private String TJID;
        
	public String getTJID() {
			return TJID;
		}

		public void setTJID(String tjid) {
			TJID = tjid;
		}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		
//		getYinhxx(getGongysValue().getId());
		
		Save1(getChange(), visit);
	}
	
	private String mkaihyh;
	private String mzhangh;
	public String getKaihyh(){
		return this.mkaihyh;
	}
	public void setKaihyh(String value){
		this.mkaihyh = value;
	}
	
	public String getZhangh(){
		return this.mzhangh;
	}
	public void setZhangh(String value){
		this.mzhangh = value;
	}
	
	public void getYinhxx(long lnggongysb_id){
		
		JDBCcon cn = new JDBCcon();
		String sql = "select kaihyh,zhangh from gongysb where id="+lnggongysb_id;
		ResultSet rs = cn.getResultSet(sql);
		try {
			while(rs.next()){
				setKaihyh(rs.getString("kaihyh"));
				setZhangh(rs.getString("zhangh"));
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
	}
	
	public void Save1(String strchange,Visit visit) {
		String tableName="yufkb";
		returnId=true;
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
//		long gongysb_id = getGongysValue().getId();
		String strkaihyh = getKaihyh();
		String strzhangh = getZhangh();
		String strbianh = "";
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while(mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql3 = new StringBuffer();//页面上没有的字段
			StringBuffer sql4 = new StringBuffer();//页面上没有的字段的内容
			StringBuffer sql5 = new StringBuffer();
			StringBuffer sql6 = new StringBuffer();
			StringBuffer sql7 = new StringBuffer();
			StringBuffer sql8 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				sql3.append(",diancxxb_id");//可变
				sql4.append(",").append(visit.getDiancxxb_id());
//				sql5.append(",yue");//可变
//				sql6.append(",").append("0");
				sql7.append(",shoukdwb_id");//可变
				sql8.append(",").append("0");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					
//					if(mdrsl.getColumnNames()[i].equals("GONGYB_ID")){
//						sql2.append(",").append(gongysb_id);
//						
//					}else if (mdrsl.getColumnNames()[i].equals("KAIHYH")){
//						
//						sql2.append(",'").append(strkaihyh).append("'");
//					}else if (mdrsl.getColumnNames()[i].equals("ZHUANGH")){
//						
//						sql2.append(",'").append(strzhangh).append("'");
//					}else 
					if (mdrsl.getColumnNames()[i].equals("JINE") || mdrsl.getColumnNames()[i].equals("YUE")){
						
						sql2.append(",").append(Double.parseDouble(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)))*10000);
					}else{
						
						sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
					}
					
					if(mdrsl.getColumnNames()[i].equals("BIANH")){
						if(strbianh.equals("")){
							strbianh = getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i));
						}else{
							strbianh = strbianh+","+getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))+"";
						}
						
					}
				}
				sql.append(sql3).append(sql7).append(") values(").append(sql2).append(sql4).append(sql8).append(");\n");
//				sql.append(sql3).append(sql5).append(sql7).append(") values(").append(sql2).append(sql4).append(sql6).append(sql8).append(");\n");
			}else {
				sql.append("update ").append(visit.getExtGrid1().tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
//					if(mdrsl.getColumnNames()[i].equals("GONGYB_ID")){
//						
//						sql.append(getGongysValue().getId()).append(",");
//					}else 
					if (mdrsl.getColumnNames()[i].equals("JINE") || mdrsl.getColumnNames()[i].equals("YUE")){
						
						sql.append(Double.parseDouble(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)))*10000+",");
					}else{
						sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
					}
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		}
		sql.append("end;");
		int result = con.getUpdate(sql.toString());
		if(result<0){
			System.out.println("/*******************保存失败！*********************/");
			con.rollBack();
		}else{
			con.commit();
			SaveYufktzd(strbianh);
		}
		con.Close();
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if("string".equals(gc.datatype)) {
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}else {
				return "'"+value+"'";
			}
			
		}else if("date".equals(gc.datatype)) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else if("float".equals(gc.datatype)){
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}
			else {
				return value==null||"".equals(value)?"null":value;
			}
//			return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
	}
		
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	private boolean _shenhButtonChick = false;

public void 	ShenButton(IRequestCycle cycle) {
	_shenhButtonChick = true;
}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
			
		}
		if (_DeleteChick) {
			_DeleteChick = false;
		}
		if(_shenhButtonChick){
			_shenhButtonChick=false;
			try {
				tijlcButtonFunc();
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				this.setMsg(e.getMessage());
				e.printStackTrace();
			}
			getSelectData();
		}
	}

	public void tijlcButtonFunc()throws Exception{
            String changtxt="";
            changtxt=this.getTJID();
            String[] ids=changtxt.split(";"); 
		//提交流程
		String TableName="fuktzb";
		long liucb_id=MainGlobal.getTableId("liucb", "mingc", "预付款审核");
		Visit visit=(Visit)this.getPage().getVisit();
		long renyxxb_id=visit.getRenyID();
		for(int i=0;i<ids.length;i++)
		BalanceLiuc.tij(TableName,new Long(ids[i]).longValue(), renyxxb_id, liucb_id, "", false);
		
//		long TableID=new Long(id).longValue();
//		BalanceLiuc.tij(TableName, TableID, renyxxb_id, liucksid, "", isTongguo);
		//提交流程
	}
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql = "select distinct y.id,case when ls.id>0 then 1 else 0 end as 历史ID,y.riq,y.bianh,g.mingc as gongysb_id ,round(y.jine/10000,6) as jine,round(y.yue/10000,6) as yue,y.kaihyh,y.zhangh ,"
				   + "decode(leib,1,'煤款',2,'运费',3,'杂费',4,'其他')as leib,y.beiz,tz.id fuktzb_id from yufkb y,yufklsb ls, gongysb g ,liucztb l,fuktzb tz where y.riq>=to_date('"+getRiqi()
        		   + "','yyyy-mm-dd')and y.riq<=to_date('"+getRiq2()+"','yyyy-mm-dd')and g.id=y.gongysb_id and ls.yufkb_id(+)=y.id and y.fuktzb_id=tz.id and l.id=tz.liucztb_id and (l.leibztb_id=0 or tz.liucztb_id=0)";	//and y.gongyb_id="+getGongysValue().getId();
		
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yufkb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("历史ID").setHidden(true);
		egu.getColumn("历史ID").setUpdate(false);
		egu.getColumn("riq").setHeader("日期");		
		egu.getColumn("bianh").setHeader("编号");
		
		egu.getColumn("jine").setHeader("金额(万元)");
		NumberField number=new NumberField();
		number.setDecimalPrecision(6);
		egu.getColumn("jine").setEditor(number);
		egu.getColumn("yue").setHeader("余额");
		
		egu.getColumn("kaihyh").setHeader("开户银行");		
		egu.getColumn("zhangh").setHeader("帐号");
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("beiz").setHeader("备注");
		
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("kaihyh").setHidden(true);		
		egu.getColumn("zhangh").setHidden(true);
		egu.getColumn("fuktzb_id").setHidden(true);
		
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,new IDropDownModel("select gys.id,gys.mingc from gongysb gys  order by gys.mingc"));
		egu.getColumn("gongysb_id").setReturnId(true); 
//		egu.getColumn("gongyb_id").setEditor(null);
		
		egu.getColumn("yue").setEditor(null);
		egu.getColumn("历史ID").setEditor(null);
		
		egu.getColumn("riq").setDefaultValue(DateUtil.FormatDate(new Date()));
		
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "请选择"));
		l.add(new IDropDownBean(1, "煤款"));
		l.add(new IDropDownBean(2, "运费"));
		l.add(new IDropDownBean(3, "杂费"));
		l.add(new IDropDownBean(4, "其他"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("leib").setDefaultValue("请选择");
		egu.getColumn("leib").setReturnId(true);

		egu.getColumn("历史ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yue").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
		egu.setWidth(1000);
		
		egu.addTbarText("起始日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("截止日期:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		// 供货单位
//		egu.addTbarText(Locale.gongysb_id_fahb);
//		ComboBox comb4 = new ComboBox();
//		comb4.setTransform("GongysDropDown");
//		comb4.setId("Gongys");
//		comb4.setEditable(false);
//		comb4.setLazyRender(true);// 动态绑定
//		comb4.setWidth(130);
//		comb4.setReadOnly(true);
//		egu.addToolbarItem(comb4.getScript());
//		egu.addOtherScript("gongyb_id.on('select',function(){document.forms[0].submit();});");

//		---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
//		sb.append("Gongys.on('select',function(){document.forms[0].submit();}); \n");
		
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			//设置余额
			sb.append("if(e.field == 'JINE'){e.record.set('YUE',parseFloat(e.value));}");
		sb.append("});");
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('历史ID')!=0){e.cancel=true;}");//当历史ID不为0时,这一行不允许编辑
//		sb.append(" if(e.field=='YUE'){ e.cancel=true;}");//余额列不允许编辑
//		sb.append(" if(e.field=='历史ID'){ e.cancel=true;}");//历史ID列不允许编辑
		sb.append("});");
		
		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
		
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","");
//		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(Gongys.getRawValue()=='请选择'){alert('请选择供应商'); return;}");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
	    egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	    
	    egu.addToolbarItem("{"
				+ new GridButton(
						"提交审核",
						"function(){ Ext.MessageBox.confirm('警告', '确定将提交吗？', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
								+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
								+ " 	for(var i=0;i<rec.length;i++){ "
								+ " 		if(i==0){"
								+ " 			document.getElementById('TJID').value=rec[i].get('FUKTZB_ID')+';';"
								+ " 		}else{ "
								+ "       	document.getElementById('TJID').value+=rec[i].get('FUKTZB_ID')+';';}}"
								+ " document.getElementById('ShenButton').click();"+MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";} else{	alert('请选择一张结算单!');}}")
						.getScript() + "})}} ");
		setExtGrid(egu);
		
		con.Close();
	}
	
//供货单位

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

			((Visit) getPage().getVisit()).setDropDownBean10(value);
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
		
		String  sql ="select gys.id,gys.jianc from gongysb gys  order by gys.jianc ";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
//			getGongysValue();
//			setGongysValue(null);
//			getGongysModels();
			
			getSelectData();
		}
		getSelectData();
	}

	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
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
			String sql="     select quanc from diancxxb where   jib=2 and id="+diancxxb_id;
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
	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}
	public void SaveYufktzd(String yufkbhs) {

		if(yufkbhs.length()<=0){
			return;
		}
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="";
		ResultSet rs;
		
		String liucm = "预付款审核";
	    long liucksid = 0;//流程开始ID
	   String id="";
	    StringBuffer sb = new StringBuffer();
	    StringBuffer sbUpdate = new StringBuffer();
	    try{
//	    	sql = "select liucdzb.liucztqqid as liucksid "
//	    		+ "  from liucb,liucztb,liucdzb,leibztb "
//	    		+ " where liucztb.liucb_id=liucb.id and liucdzb.liucztqqid=liucztb.id " 
//	    		+ "   and liucztb.leibztb_id=leibztb.id and leibztb.mingc='开始' and liucb.mingc='"+liucm+"' ";
//	    	rs = con.getResultSet(sql);
//	    	if(rs.next()){
//	    		liucksid = rs.getLong("liucksid");
//	    	}
//	    	rs.close();
		
			sql="select yf.id as yufkb_id,gy.id,gy.quanc,gy.bianm,gy.DANWDZ,yf.kaihyh,yf.zhangh,yf.jine from yufkb yf,gongysb gy where gy.id=yf.gongysb_id and bianh in ("+yufkbhs+")";
			rs=con.getResultSet(sql);
			
			sb.append("begin \n");
			sbUpdate.append("begin \n");
			int hasdata = 0;
			if(rs.next()){ 
				
				String mFuktzdbh=Fuktzdbh();
				if(checkbh(mFuktzdbh)){
					hasdata++;
					long lngYufkb_id = rs.getLong("yufkb_id");
					
					long mgongysb_id = rs.getLong("id");
	//				String mshoukdw=rs.getString("quanc");
	//				String mbianm=rs.getString("bianm");
	//				String mdiz=rs.getString("diz");
	//				String mkaihyh=rs.getString("kaihyh");
	//				String mzhangh=rs.getString("zhuangh");
					double myufkje=rs.getDouble("jine");
					
					String mfapbhs = "";
					double mfapje = myufkje;
					double mhexyfk = 0;
					double mshijfk = Math.floor(mfapje-mhexyfk); 
					
					double mkouyf = 0;//扣运费
					double mkouhkf = 0;//扣回空费
					
					String mxiangmmc = "";
					String mxiangmbh = "";
					String mfuksy = "预付款";
					String mtianzdw = getTianzdw(visit.getDiancxxb_id());
	
//					long id = Sequence.getDataID("fuktzb");
					 id=MainGlobal.getNewID(visit.getDiancxxb_id());
					sb.append("insert into fuktzb \n");
					sb.append(" (id, fukdbh, riq, gongysb_id, tianzdw, fukdlx, fapje, kouyf, kouhkf, hexyfk, shijfk, fapbhs, xiangmmc, xiangmbh, liucztb_id, liucgzid) \n");
					sb.append(" values ("+id+",'");
					sb.append( mFuktzdbh+"',to_date('"+FormatDate(new Date())+"','yyyy-mm-dd'),"+mgongysb_id+",'"+mtianzdw+"','"+mfuksy+"',");
	        		sb.append(mfapje+","+mkouyf+","+mkouhkf+","+mhexyfk+","+mshijfk+",'"+mfapbhs+"','"+mxiangmmc+"','"+mxiangmbh+"',"+liucksid+",0); \n");
					
	        		sbUpdate.append("update yufkb set fuktzb_id="+id+" where id="+lngYufkb_id+"; \n");
				}
			}
			sb.append("end; ");
			sbUpdate.append("end; ");
			
			if(hasdata>0){
				con.getInsert(sb.toString());
				con.getUpdate(sbUpdate.toString());
			}
			
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	private boolean checkbh(String bianh) {
        // TODO 自动生成方法存根
	    JDBCcon con = new JDBCcon();
	    String sql = "";
	    try{
	        sql="select fukdbh from fuktzb where fukdbh='"+bianh+"'";
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
	
}