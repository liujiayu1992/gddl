package com.zhiren.shanxdted.huifext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-10-20
 * 修改内容:根据系统参数过滤掉 不需要的急剧信息，注  电厂下新增加的需要过滤掉的机组 xuh序号要设置最大。
 */
/*
 * 作者:tzf
 * 时间:2009-10-19
 * 修改内容:当飞灰和可燃物为0时，数据都清零，否则按百分制录入，调整无法录入小数的问题。
 */
/*
 * 作者:tzf
 * 时间:2009-09-18
 * 内容:完成入炉飞灰可燃物维护
 */
public class Huifext extends BasePage implements PageValidateListener {
	private final static String list_huif="灰分";
	private final static String list_kerw="可燃物";
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg(null);
	}
	

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer sql = new StringBuffer(" ");

		String strchange=this.getChange();
		ResultSetList delrsl = this.getExtGrid().getDeleteResultSet(strchange);
		int sqlex=0;
		while (delrsl.next()) {
			sqlex++;
			
			if(!(this.getExtGrid().mokmc==null)&&!this.getExtGrid().mokmc.equals("")){
				String id = delrsl.getString("id");
				//删除时增加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,this.getExtGrid().mokmc,
						this.getExtGrid().tableName,id);
			}
			sql.append("delete from ").append(this.getExtGrid().tableName).append(" where rulbzb_id in ")
					.append(" (select id from rulbzb where diancxxb_id="+this.getTreeid()+" and  mingc='"+delrsl.getString("mingc")+"')")
					.append(" and riq="+DateUtil.FormatOracleDate(this.getRiqi()))
					.append(" and diancxxb_id="+this.getTreeid()).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = this.getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			sqlex+=2;
			
			
			String banz=getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[0]),
					mdrsl.getString("mingc"));
			
			sql.append("delete from ").append(this.getExtGrid().tableName).append(" where rulbzb_id =")
			.append(banz).append(" and riq="+DateUtil.FormatOracleDate(this.getRiqi()))
			.append(" and xiangm=").append(getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[1]),mdrsl.getString(1)))
			.append(" and diancxxb_id="+this.getTreeid()).append(";\n");
			

			//下表从0开始
			for (int i = 2; i < mdrsl.getColumnCount()-3; i++) {
				
				sql.append("insert into ").append(this.getExtGrid().tableName).append("(id,RULBZB_ID,JIZFZB_ID," +
						"beiz,DIANCXXB_ID,zhi,FENXY,LURY,RIQ,xiangm) " +"");
				
				String jiz_id=(i-1)+"";
				
				StringBuffer sql2 = new StringBuffer();
				sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
				sql2.append(",").append(
						getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[0]),
								mdrsl.getString(0)))
					.append(", ( select tt.id from ( select t.id,rownum rm from (select * from jizfzb order by xuh) t where t.mingc not like '%皮带%' and t.diancxxb_id="+this.getTreeid()+" ) tt where tt.rm="+jiz_id+")," +
							" "+getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[mdrsl.getColumnCount()-1]),
									mdrsl.getString(mdrsl.getColumnCount()-1))+","+this.getTreeid()+","+getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
							mdrsl.getString(i))+","+
							getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[mdrsl.getColumnCount()-3]),
									mdrsl.getString(mdrsl.getColumnCount()-3))
							+",'"+visit.getRenymc()+"',"+DateUtil.FormatOracleDate(this.getRiqi())+","+
									getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[1]),
											mdrsl.getString(1)));
				
				sql.append("  values (").append(sql2).append(");\n");
			}
			
			
			
			
		}
		mdrsl.close();
		
		String str=sql.toString();
		if(sqlex>1){
			str=" begin \n"+str+" end ;";
		}else{
			str=str.substring(0,str.lastIndexOf(";"));
			
		}
		
//		System.out.println(str);
		int flag = con.getUpdate(str);
		
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
		}
		con.Close();
		
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "0" : value;
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _RefreshChick=false;
	
	public void RefreshButton(IRequestCycle cycle){
		_RefreshChick=true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
		if(_RefreshChick){
			_RefreshChick=false;
		}
		getSelectData();
	}
	
	private StringBuffer getJizfzbsql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append("  select j.id,j.mingc from jizfzb  j where j.mingc not like '%皮带%' \n");
		bf.append("  and j.diancxxb_id="+this.getTreeid()+"  \n");
		bf.append(" and mingc not in  \n");
		bf.append(" (select zhi from xitxxb where mingc='灰分机组过滤项' and leib='入炉' and zhuangt=1 and diancxxb_id="+this.getTreeid()+")");
		bf.append(" order by xuh asc \n");
	
		return bf;
	}
	
	private StringBuffer getRulsbbsql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append("  select * from rulsbb r \n");
		bf.append("  where r.diancxxb_id="+this.getTreeid()+" order by xuh asc \n");
	
		return bf;
	}

	private StringBuffer getBancSql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append("  select id,mingc,xuh, rownum+2 linem from (select id,mingc,xuh from rulbzb r \n");
		bf.append("  where r.diancxxb_id="+this.getTreeid()+" union select 0 id,'平均值' mingc,99999 xuh from dual  order by xuh  asc  )  order by xuh  asc \n");
	
		return bf;
	}
	
	private int jizcount=0;

	

	public void getSelectData() {
		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		ResultSetList rs = null;
        rs=con.getResultSetList(getJizfzbsql().toString());
        int count_jiz=rs.getRows();//总共的机组数目
        this.jizcount=count_jiz;
        
        
        String sql=" select decode(grouping(b.mingc),0,b.mingc,'平均值') mingc,b.ca,";
        String where=" where 1=1 ";
        String sqlView="";
        
        
        StringBuffer shead=new StringBuffer();
        shead.append(" [  {header:'<table><tr><td width=8 align=center></td></tr></table>', align:'center',rowspan:2},\n");
//        shead.append("  {header:'<table><tr><td  align=center style=border:0>炉号</td></tr></table>', align:'center'},\n ");
        shead.append("   {header:'炉号', colspan:2},\n");
        
        
        StringBuffer xhead=new StringBuffer();
        xhead.append(" [ {header:'<table><tr><td width=80 align=center style=border:0>班次</td></tr></table>', align:'center'},\n ");
        xhead.append("  {header:'<table><tr><td width=80 align=center style=border:0>项目</td></tr></table>', align:'center'},\n ");
        
        
        
        int flag_c=1; 
        while(rs.next()){
        	shead.append(" {header:'"+rs.getString("mingc")+"', align:'center',rowspan:2}, \n");
        	
        	sqlView+=" \n (select * from rulfhb where diancxxb_id="+this.getTreeid()+" and riq="+DateUtil.FormatOracleDate(this.getRiqi())+" and jizfzb_id="+rs.getString("id")+") f"+flag_c+", ";
        	where+=" and f"+flag_c+".rulbzb_id(+)=b.id "+" and f"+flag_c+".xiangm(+)=b.ca \n";
        	sql+=" round_new(avg(f"+flag_c+".zhi),2) as f"+flag_c+",";
        	flag_c++;
        }
        
        sqlView+=" \n (select max(fenxy) fenxy,max(lury) lury,max(beiz) beiz,rulbzb_id from rulfhb  where diancxxb_id="+this.getTreeid()+"and riq="+DateUtil.FormatOracleDate(this.getRiqi())+"  group by rulbzb_id ) reny, ";
        sqlView+=" \n (select * from ( select * from rulbzb where diancxxb_id="+this.getTreeid()+") d,(select '灰分' ca from dual union select '可燃物' ca from dual ) xm ) b ";
        
        where+=" and reny.rulbzb_id(+)=b.id ";
        
        sql+=" decode(grouping(b.mingc),0,max(reny.fenxy),'')  fenxy,decode(grouping(b.mingc),0,max(reny.lury),'') lury,decode(grouping(b.mingc),0,max(reny.beiz),'') beiz from "+sqlView+where+"  group by grouping sets((b.ca),(b.mingc,b.ca,b.xuh) )   order by b.xuh asc,b.ca asc ";
        
        
        shead.append(" {header:'化验员', align:'center',rowspan:2}, \n");
        shead.append(" {header:'录入员', align:'center',rowspan:2}, \n");
        shead.append(" {header:'备注', align:'center',rowspan:2} \n");
        shead.append(" ],\n");
        
        xhead=new StringBuffer(xhead.toString().substring(0,xhead.toString().lastIndexOf(",")));
        xhead.append(" ] \n ");
        
        
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("RULFHB");
		egu.addPaging(25);
		
		List list=egu.gridColumns;
		for(int i=0;i<list.size();i++){
			GridColumn gc=(GridColumn)list.get(i);
			gc.setWidth(100);
			gc.setFixed(true);
			
			if ("float".equals(gc.datatype)){
				NumberField nf=new NumberField();
				nf.setDecimalPrecision(3);
				gc.setEditor(nf);
			}
		}
		
		
		ComboBox banz=new ComboBox();
		egu.getColumn("MINGC").setEditor(banz);
		egu.getColumn("MINGC").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from rulbzb where diancxxb_id="+this.getTreeid()+" order by xuh asc "));
		egu.getColumn("MINGC").setReturnId(true);
		egu.getColumn("MINGC").setHeader("班次");
		
		
		ComboBox xm=new ComboBox();
		egu.getColumn("ca").setEditor(xm);
		List list_xm=new ArrayList();
		list_xm.add(new IDropDownBean("1","灰分"));
		list_xm.add(new IDropDownBean("2","可燃物"));
		egu.getColumn("ca").setComboEditor(egu.gridId, new IDropDownModel(list_xm));
		egu.getColumn("ca").setReturnId(false);
		egu.getColumn("ca").setHeader("项目");
		
		
		
		ComboBox fenxy=new ComboBox();
		egu.getColumn("fenxy").setEditor(fenxy);
		egu.getColumn("fenxy").setComboEditor(egu.gridId, new IDropDownModel(" select id,quanc from renyxxb where zhiw='入炉化验员' "));
		egu.getColumn("fenxy").setReturnId(false);
		egu.getColumn("fenxy").editor.allowBlank=true;
		
		
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("lury").setEditor(null);
		
		
		egu.getColumn("beiz").setWidth(120);
		
		
		StringBuffer sb = new StringBuffer();
		String Headers = shead.toString()+xhead.toString();
		sb.append(Headers);
		egu.setHeaders(sb.toString());
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
		
		
//		设置树
		egu.addTbarText("电厂:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");
		
		egu.addTbarText("采样日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		
		
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		GridButton insert=new GridButton("添加","function(){\n" +
//				" var plant = new gridDiv_plant({ID: '0',RULBZB_ID: '',JIZFZB_ID: '',XIANGM:'"+list_huif+"',ZHI: '0',DIANCXXB_ID: '"+this.getTreeid()+"', " +
//				" FENXY: '',LURY: '"+visit.getRenymc()+"',RIQ: '"+DateUtil.FormatDate(new Date())+"',BEIZ: ''});\n" +
//				" gridDiv_ds.insert(gridDiv_ds.getCount(),plant); \n"+
//				
//				"  plant = new gridDiv_plant({ID: '0',RULBZB_ID: '',JIZFZB_ID: '',XIANGM:'"+list_kerw+"',ZHI: '0',DIANCXXB_ID: '"+this.getTreeid()+"', " +
//				" FENXY: '',LURY: '"+visit.getRenymc()+"',RIQ: '"+DateUtil.FormatDate(new Date())+"',BEIZ: ''});\n" +
//				" gridDiv_ds.insert(gridDiv_ds.getCount(),plant); \n"+
//				
//				"}");
//		insert.setIcon(SysConstant.Btn_Icon_Insert);
//		egu.addTbarBtn(insert);
		
		
		
		
		
//		GridButton dele=new GridButton("删除",getDeleteScript());
//		dele.setIcon(SysConstant.Btn_Icon_Delete);
//		egu.addTbarBtn(dele);
		
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){\n" +
				"if(e.record.get('MINGC')=='平均值'){e.cancel=true;}\n" +
				"" +
				"} \n);");
		
		egu.addOtherScript(" gridDiv_grid.addListener('afteredit',function(e){\n" +
				
				" if(e.field=='CA') return;\n"+
				
				" if(e.field=='MINGC'  || e.field=='FENXY' || e.field=='LURY' || e.field=='BEIZ'){\n" +
				" if((e.row+1)%2==0){ gridDiv_ds.getAt(e.row-1).set(e.field,e.value); } else{ gridDiv_ds.getAt(e.row+1).set(e.field,e.value); } \n" +
				" return; } \n"+
				
				" var _value=e.value; if(_value==null || _value=='') _value=0; \n"+
				
				" if(_value==0) _value=100;\n"+
				
				" if((e.row+1)%2==0){ " +
				" gridDiv_ds.getAt(e.row-1).set(e.field,Round_new((100-_value),3) );  }\n " +//可燃物
				" else { gridDiv_ds.getAt(e.row+1).set(e.field,Round_new((100-_value),3) );  } \n" +
				"\n });");
		
		
		setExtGrid(egu);
		con.Close();
	}


	private  String getDeleteScript() {
		StringBuffer record = new StringBuffer();
		String parentId=this.getExtGrid().gridId;
		List columns=this.getExtGrid().getGridColumns();
		record.append("function() {\n");
		
		record.append(" if("+parentId+"_sm.getSelections().length<=0) return;\n");
		record.append(" var array=new Array(); ");
		record.append(" " +
				"if("+parentId+"_sm.getSelections()[0].get('XIANGM')=='"+list_kerw+"') " +"	"+"array.push("+parentId+"_ds.getAt(row_line-1) );\n" +
				"if("+parentId+"_sm.getSelections()[0].get('XIANGM')=='"+list_huif+"') " +"	"+"array.push("+parentId+"_ds.getAt(row_line+1) );\n" +
				"");
		record.append(" array.push("+parentId+"_sm.getSelections()[0]);\n");
		
		
		record.append("for(i=0;i<array.length;i++){\n");
		record.append("	record = "+"array[i];\n");
		
		StringBuffer sb = new StringBuffer();
		//sb.append(b);
		sb.append(parentId).append("_history += '<result>' ")
		.append("+ '<sign>D</sign>' ");
		
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(gc.update) {
					if("date".equals(gc.datatype)) {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("record.get('")
						.append(gc.dataIndex).append("'))?").append("record.get('")
						.append(gc.dataIndex).append("'):").append("record.get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					}else {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("record.get('").append(gc.dataIndex).append("')");
						if(!gc.datatype.equals(GridColumn.DataType_Float)) {
							sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
						}
						sb.append("+ '</").append(gc.dataIndex).append(">'\n");
					}
				}
			}
		}
		sb.append(" + '</result>' ;");
		record.append(sb);
		
//		record.append(" " +
//		"if("+parentId+"_sm.getSelections()[i].get('XIANGM')=='"+list_kerw+"') " +"	"+parentId+"_ds.remove("+parentId+"_ds.getAt(row_line-1) );\n" +
//		"if("+parentId+"_sm.getSelections()[i].get('XIANGM')=='"+list_huif+"') " +"	"+parentId+"_ds.remove("+parentId+"_ds.getAt(row_line+1) );\n" +
//		"");
		record.append("	"+parentId+"_ds.remove(record);}}");
		return record.toString();
	}
	
	
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
//			riqi=DateUtil.FormatDate(new Date());
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	
	
//	-----电厂tree
	private String treeid;
	private boolean diancFlag=false;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		if(((Visit) getPage().getVisit()).getString2()!=null && !((Visit) getPage().getVisit()).getString2().equals(treeid)){
			diancFlag=true;
		}
		((Visit) getPage().getVisit()).setString2(treeid);
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
	
	//--------------------------------
	
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
			visit.setList1(null);
			this.setTreeid(visit.getDiancxxb_id()+"");
			getSelectData();
		}
	}
}
