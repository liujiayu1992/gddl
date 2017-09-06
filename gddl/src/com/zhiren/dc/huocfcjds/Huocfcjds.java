package com.zhiren.dc.huocfcjds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-06-08
 * 内容:实现火车翻车机数据上传都服务器相关表中
 */
public class Huocfcjds extends BasePage implements PageValidateListener {

	private static final String xit_mingc="火车皮重最大估值";//xitxxb中名称字段
	private static final String xit_leib="数量";//xitxxb中类别字段
	private static final int record_show=10;//页面最多显示记录数
	
	private int nextGroup;
	
	public int getNextGroup(){
		return nextGroup;
	}
	
	public void setNextGroup(int value){
		nextGroup=value;
	}
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Filename;
	
	public void setFilename(String filename){
		Filename=filename;
	}
	public String getFilename(){
		return Filename;
	}
	
	
	private String Pzljz;//皮重临界值
	
	public void setPzljz(String pzljz){
		this.Pzljz=pzljz;
	}
	
	public String getPzljz(){
		
		String sql=" select x.zhi from xitxxb x  where x.mingc='"+xit_mingc+"' and x.leib='"+xit_leib+"' and  x.zhuangt=1";
		
		String zhi="35";//默认为35吨
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		return " ljz="+zhi+";";
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
//		this.setFilename("cancel");
//		this.setNextGroup(1);
		this.Pzljz="";
		
	}
	
	private boolean _RefreshChick=false;
	
	public void RefreshButton(IRequestCycle cycle){
		_RefreshChick=true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _insertChick=false;
	
	public void InsertButton(IRequestCycle cycle){
		_insertChick=true;
	}
	
	private boolean _deleteChick=false;
	
	public void DeleteButton(IRequestCycle cycle){
		_deleteChick=true;
	}
	public void submit(IRequestCycle cycle) {

		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}else if(_insertChick){
			_insertChick=false;
		}else if(_deleteChick){//求下一组
			_deleteChick=false;
			this.setNextGroup(record_show+this.getNextGroup());
		}else if(_RefreshChick){
			_RefreshChick=false;
			this.setNextGroup(1);
		}
		
		this.getSelectData();
	}
	//获得文件名字
//	private String getFileName(){
//		String name="";
//		
//		Date date=new Date();
//		
//		name=DateUtil.Formatdate("yyyyMMddHHmmss", date);
//		return name;
//	}
//	

	//测试 保存成功与否   客户端文件是在暂存文件夹 中 还是正式文件 
	private boolean test(){
		Random rd=new Random();
		int a=rd.nextInt();
		System.out.println(a+"********");
		if(a%2==0){
			return true;
		}
		return false;
	}
	
	public void Save(){
		
		
		Visit visit=(Visit)this.getPage().getVisit(); 
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		String rq1=" to_date('"+this.getRiq()+" "+this.getXiaosValue().getValue()+":"+this.getFenValue().getValue()+":"+this.getMiaoValue().getValue()+"','yyyy-MM-dd HH24:mi:ss')";
		
		StringBuffer bf=new StringBuffer(" begin \n");
		String[] raw=this.getChange().split(";");
		boolean t=false;
		for(int i=0;i<raw.length;i++){
			
			String[] col=raw[i].split(",");
			
			if(col[0].equals("0")){//说明是新增加的   记录
				
				if(col[3].toUpperCase().equals("CHEPBTMP")){
					
					String sql=" select *  from CHEPBTMP c ,fahb f ,yunsfsb y\n"
							+" where c.zhongcsj>="+rq1+"\n"
							+" and c.fahb_id=f.id  and f.yunsfsb_id=y.id and y.mingc='铁路' \n"
							+" and (nvl(c.yuanmz,0)=0 or nvl(c.yuanpz,0)=0) order by c.zhongcsj asc";
					
					//取出  最接近 该时间的   fahb
					
					rsl=con.getResultSetList(sql);
					
					if(rsl.next()){
						t=true;
						String id= MainGlobal.getNewID(visit.getDiancxxb_id());
						
						String strlursj;
						
						strlursj = DateUtil.FormatOracleDateTime(new Date());
						
						
						bf.append(" insert into CHEPBTMP(ID,QICRJHB_ID,QINGCHH,QINGCJJY,ZHONGCSJ,ZHONGCHH,\n" +
								" ZHONGCJJY,MEICB_ID,DAOZCH,LURY,BEIZ,CAIYRQ,LURSJ,YUNSDW,DIANCXXB_ID,\n" +
								" PIAOJH,GONGYSMC,MEIKDWMC,PINZ,FAZ,DAOZ,JIHKJ,FAHRQ,DAOHRQ,HETB_ID,ZHILB_ID,CAIYBH,\n" +
								" JIESB_ID,YUNSFS,CHEC,CHEPH,MAOZ,PIZ,BIAOZ,YINGD,YINGK,YUNS,YUNSL,KOUD,KOUS,KOUZ,\n" +
								" SANFSL,CHES,JIANJFS,GUOHB_ID,FAHB_ID,FAHBTMP_ID,CHEBB_ID,YUANDZ,YUANSHDW,KUANGFZLB_ID,\n" +
								" YUANMKDW,YUNSDWB_ID,QINGCSJ,XIECFS,YUANMZ,YUANPZ)\n" +
								" values(" +id+",decode("+rsl.getString("QICRJHB_ID")+",'',0,"+rsl.getString("QICRJHB_ID")+"),'"+rsl.getString("QINGCHH")+"',\n"+
								"'" +rsl.getString("QINGCJJY")+"',to_date('"+DateUtil.FormatDate(rsl.getDate("ZHONGCSJ"))+"','yyyy-MM-dd HH24:mi:ss'),\n"+
								"'" +rsl.getString("ZHONGCHH")+"','"+rsl.getString("ZHONGCJJY")+"',"+rsl.getString("MEICB_ID")+",'"+rsl.getString("DAOZCH")+"',\n"+
								"'" +rsl.getString("LURY")+"','"+rsl.getString("BEIZ")+"',to_date('"+DateUtil.FormatDate(rsl.getDate("CAIYRQ"))+"','yyyy-MM-dd'),\n"+
								"" +strlursj+",'"+rsl.getString("YUNSDW")+"',\n"+
								"" +rsl.getString("DIANCXXB_ID")+",getLiush("+strlursj+"),'"+rsl.getString("GONGYSMC")+"',\n"+
								"'" +rsl.getString("MEIKDWMC")+"','"+rsl.getString("PINZ")+"','"+rsl.getString("FAZ")+"',"+"'"+rsl.getString("DAOZ")+"',"+"'"+rsl.getString("JIHKJ")+"',\n"+
								"to_date('" +DateUtil.FormatDate(rsl.getDate("FAHRQ"))+"','yyyy-MM-dd'),to_date('"+DateUtil.FormatDate(rsl.getDate("DAOHRQ"))+"','yyyy-MM-dd'),\n"+
								"" +rsl.getString("HETB_ID")+","+rsl.getString("ZHILB_ID")+",'"+rsl.getString("CAIYBH")+"',"+rsl.getString("JIESB_ID")+",\n"+
								" '"+rsl.getString("YUNSFS")+"','"+rsl.getString("CHEC")+"','"+col[4]+"',"+col[1]+",\n"+
								""+col[2]+","+rsl.getString("BIAOZ")+","+rsl.getString("YINGD")+","+rsl.getString("YINGK")+","+rsl.getString("YUNS")+",\n"+
								""+rsl.getString("YUNSL")+","+rsl.getString("KOUD")+","+rsl.getString("KOUS")+","+rsl.getString("KOUZ")+","+rsl.getString("SANFSL")+","+rsl.getString("CHES")+",\n"+
								"'" +rsl.getString("JIANJFS")+"',"+rsl.getString("GUOHB_ID")+","+rsl.getString("FAHB_ID")+","+rsl.getString("FAHBTMP_ID")+","+rsl.getString("CHEBB_ID")+",'"+rsl.getString("YUANDZ")+"','"+rsl.getString("YUANSHDW")+"',"+rsl.getString("KUANGFZLB_ID")+",\n"+
								"'" +rsl.getString("YUANMKDW")+"','" +rsl.getString("YUNSDWB_ID")+"',to_date('" +DateUtil.FormatDate(rsl.getDate("QINGCSJ"))+"','yyyy-MM-dd HH24:mi:ss')" +",\n"+
								"'"+rsl.getString("XIECFS")+"',"+col[1]+","+col[2]+
								");\n");
						bf.append(" update  fahb   set maoz=maoz+"+col[1]+" ,piz=piz+"+col[2]+" where id="+rsl.getString("fahb_id")+";\n");
						
					}else{
						this.setMsg("此未导入数据源 该时间没有数据，无法添加!");
					}
					
				
				}else{//chepb

					String sql=" select *  from CHEPB c ,fahb f ,yunsfsb y\n"
							+" where c.zhongcsj>="+rq1+"\n"
							+" and c.fahb_id=f.id  and f.yunsfsb_id=y.id and y.mingc='铁路' \n"
							+" and (nvl(c.yuanmz,0)=0 or nvl(c.yuanpz,0)=0) order by c.zhongcsj asc";
					
					//取出  最接近 该时间的   fahb
					
					rsl=con.getResultSetList(sql);
					
					if(rsl.next()){
						t=true;
						String id= MainGlobal.getNewID(visit.getDiancxxb_id());
						String strlursj;
					
						strlursj = DateUtil.FormatOracleDateTime(new Date());
						
						
						
						bf.append("insert into chepb(id,xuh,piaojh, cheph, yuanmz, yuanpz, maoz, piz, biaoz, ");
						bf.append("koud, kous, kouz, zongkd, sanfsl, ches, jianjfs, guohb_id, ");
						bf.append("fahb_id, chebb_id, yuanmkdw, yunsdwb_id, qingcsj, qingchh, qingcjjy, ");
						bf.append("zhongcsj, zhongchh, zhongcjjy, meicb_id, daozch, lursj, lury, beiz , hedbz,xiecfsb_id,BULSJ\n");
						bf.append(")");
						bf.append("(");
						bf.append("select "+id+",getChepxh(")
						.append(strlursj).append("),getLiush(").append(strlursj).append("), '"+col[4]+"',");
						bf.append(" "+col[1]+", "+col[2]+", "+col[1]+", "+col[2]+", biaoz, koud, kous, kouz,koud+kous+kouz,sanfsl, ");
						bf.append("ches, jianjfs, guohb_id, fahb_id, chebb_id, yuanmkdw, yunsdwb_id,");
						bf.append(" qingcsj, qingchh, qingcjjy, zhongcsj, zhongchh, zhongcjjy, meicb_id, daozch, ").append(strlursj).append(",lury, beiz, ").append("hedbz");
						bf.append(",xiecfsb_id").append(",BULSJ");
						bf.append(" from chepb where id="+rsl.getString("id")+");");
						
						bf.append(" update  fahb   set maoz=maoz+"+col[1]+" ,piz=piz+"+col[2]+" where id="+rsl.getString("fahb_id")+";\n");
					}else{
						this.setMsg("此已导入数据源 该时间没有数据，无法添加!");
					}
				}
			}else{//原有车皮的更改
				t=true;
				bf.append(" update "+col[3]+" set yuanmz="+col[1]+" , yuanpz="+col[2]+" where piaojh='"+col[0]+"';\n");
				
			//	bf.append(" update fahb set maoz=");
			}
			
			
		}
		
		bf.append(" end ;");
		
//		System.out.println(bf.toString());
		
		if(t){
			int flag=con.getUpdate(bf.toString());
			
			if(flag>=0){
				this.setFilename("true");
				this.setMsg("数据操作成功!");
				return;
			}
			
			this.setFilename("false");
			this.setMsg("数据操作失败,请导入未成功数据继续操作!");
		}
		
		
	}
	
	public void getSelectData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String tableName="";
		String sql = "";
		
		
//			String rq1=DateUtil.FormatOracleDate(this.getRiq());
//			String rq2=DateUtil.FormatOracleDate(this.getRiq1());
			String rq1=" to_date('"+this.getRiq()+" "+this.getXiaosValue().getValue()+":"+this.getFenValue().getValue()+":"+this.getMiaoValue().getValue()+"','yyyy-MM-dd HH24:mi:ss')";
			if(this.getDsValue().getStrId().equals("1")){//已导入
				tableName="chepb";
			}else{
				tableName="chepbtmp";
			}
			
			sql=" select rownum rw,c.cheph cheph,c.piaojh piaojh,c.yuanmz yuanmz,c.yuanpz yuanpz,'"+tableName+"'  tableName  from  "+tableName+" c ,fahb f ,yunsfsb y \n"
				+" where c.zhongcsj>="+rq1+"\n"
				+" and c.fahb_id=f.id  and f.yunsfsb_id=y.id and y.mingc='铁路' \n"
				+" and (nvl(c.yuanmz,0)=0 or nvl(c.yuanpz,0)=0) order  by c.zhongcsj,c.cheph";
			
			sql=" select cheph,piaojh,yuanmz,yuanpz,round_new(yuanmz-yuanpz,2) jingz,tableName from ( "+sql+" ) za where za.rw>="+this.getNextGroup()+" and za.rw<"+(this.getNextGroup()+record_show);
		
//		System.out.println(sql);
		
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName(tableName);
		egu.addPaging(-1);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		egu.getColumn("cheph").setHeader("车皮号");
		egu.getColumn("piaojh").setHeader("票据号");
		egu.getColumn("piaojh").setHidden(true);
		egu.getColumn("yuanmz").setHeader("毛重");
		egu.getColumn("yuanpz").setHeader("皮重");
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("tableName").setHidden(true);
		
		egu.getColumn("cheph").setDefaultValue("0");
		egu.getColumn("piaojh").setDefaultValue("0");
		egu.getColumn("yuanmz").setDefaultValue("0");
		egu.getColumn("yuanpz").setDefaultValue("0");
		egu.getColumn("jingz").setDefaultValue("0");
		egu.getColumn("tableName").setDefaultValue(tableName);
		
		
		
		egu.getColumn("piaojh").setEditor(null);
		egu.getColumn("yuanmz").setEditor(null);
		egu.getColumn("yuanpz").setEditor(null);
		
		NumberField nf=new NumberField();
//		nf.setId("nf");
		nf.setDecimalPrecision(2l);
		nf.setReadOnly(true);
		nf.setEmptyText("0");
		egu.getColumn("jingz").setEditor(nf);
		
		egu.getColumn("tableName").setEditor(null);
		
		
		egu.addTbarText("重车时间:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		
		egu.addTbarText("时:");
		
		ComboBox xs=new ComboBox();
		xs.setWidth(50);
		xs.setTransform("XS");
		xs.setId("XS");//和自动刷新绑定
		xs.setLazyRender(true);//动态绑定
		egu.addToolbarItem(xs.getScript());
		
		
		egu.addTbarText("分:");
		
		ComboBox fen=new ComboBox();
		fen.setWidth(50);
		fen.setTransform("FEN");
		fen.setId("FEN");//和自动刷新绑定
		fen.setLazyRender(true);//动态绑定
		egu.addToolbarItem(fen.getScript());
		
		
		egu.addTbarText("秒:");
		
		ComboBox sec=new ComboBox();
		sec.setWidth(50);
		sec.setTransform("SEC");
		sec.setId("SEC");//和自动刷新绑定
		sec.setLazyRender(true);//动态绑定
		egu.addToolbarItem(sec.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		
		
		egu.addTbarText("数据源:");
		ComboBox comb2=new ComboBox();
		comb2.setWidth(80);
		comb2.setTransform("DS");
		comb2.setId("DS");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符
		
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		
		
	
		
		
		StringBuffer str_de=new StringBuffer();
		str_de.append("function(){ document.all.DeleteButton.click();");
		str_de.append("}");
		GridButton de=new GridButton("下一组",str_de.toString());
		de.setIcon(SysConstant.Btn_Icon_Search);
		egu.addTbarBtn(de);
		
		egu.addTbarText("-");
		
		
		StringBuffer str_in=new StringBuffer();
		str_in.append("function(){ Daords();");
		str_in.append("}");
		GridButton in=new GridButton("导入",str_in.toString());
		in.setId("DRDS");
		in.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(in);
		
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Insert,null);
		
		egu.addTbarText("-");
		StringBuffer str_sa=new StringBuffer();
		str_sa.append("function(){  grid_save(grid.getStore()); ");
		str_sa.append("}");
		GridButton sa=new GridButton("保存",str_sa.toString());
		sa.setIcon(SysConstant.Btn_Icon_Save);
		egu.addTbarBtn(sa);
		
		egu.addOtherScript(" grid=gridDiv_grid;");
		egu.addOtherScript(" gridDiv_sm.addListener('rowselect',function(sml,rowIndex,re){row_line=rowIndex;});");
		
		//如果票据号  是0   说明是新添加的记录，车皮号允许改变，否则不允许用户改变 车皮号
		egu.addOtherScript(" gridDiv_grid.addListener('beforeedit',function(e){ if(e.record.get('PIAOJH')!='0'){e.cancel=true;}   }); ");
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
//***************************************************************************//
	
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	private String riq1;
	public void setRiq1(String value) {
		riq1 = value;
	}
	public String getRiq1() {
		if ("".equals(riq1) || riq1 == null) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setOriRiq1(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	public String getOriRiq1() {
		return ((Visit) getPage().getVisit()).getString2();
	}
	//-------------------------------------------------
	
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

	
	//取出暂存在本地指定路径下的文件数据
	private String initData;
	
	public String getInitData(){
		
		String s=" gridDiv_data=[ ";
		String filepath="D:/zhiren/huocfcjds/temp/huocfcj_temp";
		File file=new File(filepath);
//		File file = new File(visit.getXitwjjwz()+"/huocfcj/temp/huocfcj_temp");
		int flag=0;
		if(file.exists()){
			
			try {
				FileReader fr=new FileReader(file);
				BufferedReader br=new BufferedReader(fr);
				
				while(true){
					String readLine=br.readLine();
					if(readLine==null){//已经结束 退出
						
						if(flag==0){
							return "";
						}
						break;
					}
					flag++;
					String[] Str_temp=readLine.split(",");
					
					s+="[";
					for(int i=0;i<Str_temp.length;i++){
						s+="'"+Str_temp[i]+"'";
						if(i!=Str_temp.length-1){
							s+=",";
						}
					}
					s+="],";
					
				}
				
				s=s.substring(0,s.lastIndexOf(","))+"];";
				return s;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}else{
			return "";
		}
		
	}
	
	
	
	public void beginResponse(IMarkupWriter writer,IRequestCycle cycle){
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setRiq(null);
			this.setRiq1(null);
			this.setNextGroup(1);
			this.setFilename("cancel");
			getSelectData();
		}
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
	
	
	
	// 查询方式下拉框 （数据源）
	private boolean falg1 = false;

	private IDropDownBean DsValue;

	public IDropDownBean getDsValue() {
		if (DsValue == null) {
			DsValue = (IDropDownBean) getDsModel().getOption(0);
		}
		return DsValue;
	}

	public void setDsValue(IDropDownBean Value) {
		if (!(DsValue == Value)) {
			DsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel DsModel;

	public void setDsModel(IPropertySelectionModel value) {
		DsModel = value;
	}

	public IPropertySelectionModel getDsModel() {
		if (DsModel == null) {
			getDsModels();
		}
		return DsModel;
	}

	public IPropertySelectionModel getDsModels() {
		
		List list=new ArrayList();
		list.add(new IDropDownBean(1,"已导入"));
		list.add(new IDropDownBean(2,"未导入"));
		DsModel = new IDropDownModel(list);
		return DsModel;
	}
//小时--------------------
	
	private IDropDownBean XiaosValue;

	public IDropDownBean getXiaosValue() {
		if (XiaosValue == null) {
			XiaosValue = (IDropDownBean) getXiaosModel().getOption(0);
		}
		return XiaosValue;
	}

	public void setXiaosValue(IDropDownBean Value) {
		if (!(XiaosValue == Value)) {
			XiaosValue = Value;
		//	falg1 = true;
		}
	}

	private IPropertySelectionModel XiaosModel;

	public void setXiaosModel(IPropertySelectionModel value) {
		XiaosModel = value;
	}

	public IPropertySelectionModel getXiaosModel() {
		if (XiaosModel == null) {
			getXiaosModels();
		}
		return XiaosModel;
	}

	public IPropertySelectionModel getXiaosModels() {
		
		List list=new ArrayList();
		for(int i=0;i<24;i++){
			list.add(new IDropDownBean(i,i<10?("0"+i):(i+"")));
		}
		XiaosModel = new IDropDownModel(list);
		return XiaosModel;
	}
	
	//分------------------
	private IDropDownBean FenValue;

	public IDropDownBean getFenValue() {
		if (FenValue == null) {
			FenValue = (IDropDownBean) getFenModel().getOption(0);
		}
		return FenValue;
	}

	public void setFenValue(IDropDownBean Value) {
		if (!(FenValue == Value)) {
			FenValue = Value;
		//	falg1 = true;
		}
	}

	private IPropertySelectionModel FenModel;

	public void setFenModel(IPropertySelectionModel value) {
		FenModel = value;
	}

	public IPropertySelectionModel getFenModel() {
		if (FenModel == null) {
			getFenModels();
		}
		return FenModel;
	}

	public IPropertySelectionModel getFenModels() {
		
		List list=new ArrayList();
		for(int i=0;i<60;i++){
			list.add(new IDropDownBean(i,i<10?("0"+i):(i+"")));
		}
		FenModel = new IDropDownModel(list);
		return FenModel;
	}
	//秒-----
	private IDropDownBean MiaoValue;

	public IDropDownBean getMiaoValue() {
		if (MiaoValue == null) {
			MiaoValue = (IDropDownBean) getMiaoModel().getOption(0);
		}
		return MiaoValue;
	}

	public void setMiaoValue(IDropDownBean Value) {
		if (!(MiaoValue == Value)) {
			MiaoValue = Value;
		//	falg1 = true;
		}
	}

	private IPropertySelectionModel MiaoModel;

	public void setMiaoModel(IPropertySelectionModel value) {
		MiaoModel = value;
	}

	public IPropertySelectionModel getMiaoModel() {
		if (MiaoModel == null) {
			getMiaoModels();
		}
		return MiaoModel;
	}

	public IPropertySelectionModel getMiaoModels() {
		
		List list=new ArrayList();
		
		for(int i=0;i<60;i++){
			list.add(new IDropDownBean(i,i<10?("0"+i):(i+"")));
		}
		
		
		MiaoModel = new IDropDownModel(list);
		return MiaoModel;
	}
}
