package com.zhiren.shanxdted.xidext;

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
 * 时间:2009-10-21
 * 修改内容:调整表头显示的宽度
 */
/*
 * 作者:tzf
 * 时间:2009-10-20
 * 修改内容:根据系统参数过滤掉 不需要的急剧信息，注  电厂下新增加的需要过滤掉的机组 xuh序号要设置最大。
 */
/*
 * 作者:tzf
 * 时间:2009-09-18
 * 内容:实现入炉细度
 */
public class Xidext extends BasePage implements PageValidateListener {
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
			.append(" and diancxxb_id="+this.getTreeid()).append(";\n");
			

			//下表从0开始
			for (int i = 1; i < mdrsl.getColumnCount()-2; i++) {
				
				sql.append("insert into ").append(this.getExtGrid().tableName).append("(id,RULBZB_ID,JIZFZB_ID," +
						"RULSBB_ID,DIANCXXB_ID,XIDZ,FENXY,LURY,RIQ) " +"");
//						"getnewid("+this.getTreeid()+"),"+banz+",");
				
				String jiz_id=(i+1)/this.shebcount+"";
				String sheb_id=((i%this.shebcount==0)?this.shebcount:(i%this.shebcount))+"";
				
				StringBuffer sql2 = new StringBuffer();
				sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
				sql2.append(",").append(
						getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[0]),
								mdrsl.getString(0)))
					.append(", ( select tt.id from ( select t.id,rownum rm from (select * from jizfzb order by xuh) t where t.mingc not like '%皮带%' and t.diancxxb_id="+this.getTreeid()+" ) tt where tt.rm="+jiz_id+")," +
							" ( select tt.id from ( select t.id,rownum rm from (select * from rulsbb order by xuh) t where t.diancxxb_id="+this.getTreeid()+" ) tt where tt.rm="+sheb_id+") ,"+this.getTreeid()+","+getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
							mdrsl.getString(i))+","+
							getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[mdrsl.getColumnCount()-2]),
									mdrsl.getString(mdrsl.getColumnCount()-2))
							+",'"+visit.getRenymc()+"',"+DateUtil.FormatOracleDate(this.getRiqi()));
				
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

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
		getSelectData();
	}
	

	private StringBuffer getJizfzbsql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append("  select j.id,j.mingc from jizfzb  j where j.mingc not like '%皮带%' \n");
		bf.append("  and j.diancxxb_id="+this.getTreeid()+"  \n");
		bf.append(" and mingc not in  \n");
		bf.append(" (select zhi from xitxxb where mingc='细度机组过滤项' and leib='入炉' and zhuangt=1 and diancxxb_id="+this.getTreeid()+")");
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
	private int shebcount=0;

	public void getSelectData() {
		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		ResultSetList rstmp =null;
		ResultSetList rs = null;
		
		
	      
        rs=con.getResultSetList(getJizfzbsql().toString());
        rstmp=con.getResultSetList(getRulsbbsql().toString());
        int count_jiz=rs.getRows();//总共的机组数目
        int count_sheb=rstmp.getRows();//总共的设备数
        
        this.jizcount=count_jiz;
        this.shebcount=count_sheb;
        
        
        String sql=" select nvl(b.mingc,'平均值') mingc,\n";
        String sqlView="";
        String where=" where 1=1 ";
        
        StringBuffer shead=new StringBuffer();
        shead.append(" [  {header:'<table><tr><td width=8 align=center></td></tr></table>', align:'center',rowspan:2},\n");
//        shead.append(" {header:'炉号', align:'center'},\n");
        shead.append("  {header:'<table><tr><td  align=center style=border:0>炉号</td></tr></table>', align:'center'},\n ");
        
        
        StringBuffer xhead=new StringBuffer();
        xhead.append(" [ {header:'<table><tr><td width=80 align=center style=border:0>班次/设备</td></tr></table>', align:'center'},\n ");
//        xhead.append("  {header:'<table><tr><td width=80 align=center style=border:0>设备号</td></tr></table>', align:'center'},\n ");
        
       int flag_c=1; 
       while(rs.next()){
    	   
    	   shead.append(" {header:'"+rs.getString("mingc")+"', align:'center',colspan:"+count_sheb+"},\n \n");
    	   
    	   rstmp.beforefirst();
    	   while(rstmp.next()){
    		   
    		   xhead.append("  {header:'<table><tr><td width=40 align=center style=border:0>"+rstmp.getString("mingc")+"</td></tr></table>', align:'center'},\n ");
    		   
    		   
    		   sqlView+=" \n ( select * from xidxxb where diancxxb_id="+this.getTreeid()+" and riq="+DateUtil.FormatOracleDate(this.getRiqi())+"" +
    		   		" and jizfzb_id="+rs.getString("id")+" and rulsbb_id="+rstmp.getString("id")+" ) sb"+flag_c+",";
    		   
    		   sql+=" round_new(avg(sb"+flag_c+".xidz),2)  as jzsb"+flag_c+",";
    		   where+=" and sb"+flag_c+".rulbzb_id(+)=b.id ";
    		   flag_c++;
    	   }
    	   
    	   
       }
       
       	sqlView+=" \n ( select max(fenxy) fenxy,max(lury) lury, rulbzb_id from xidxxb where diancxxb_id="+this.getTreeid()+" and riq="+DateUtil.FormatOracleDate(this.getRiqi())+" group by rulbzb_id " +
  		"  )  reny "+",";
       
       	sqlView+=" \n rulbzb b ";
       	sql+=" decode(grouping(b.mingc),0,max(reny.fenxy),1,'') fenxy,decode(grouping(b.mingc),0,max(reny.lury),1,'') lury \n";
       	
        sql+=" from  "+sqlView+" \n "+where+" and reny.rulbzb_id(+)=b.id  and b.diancxxb_id="+this.getTreeid()+" group by grouping sets((),(b.mingc,b.xuh) ) " +
//        		" having grouping(b.mingc)=0 " +
        		"  order by b.xuh ";
        
        
        shead.append(" {header:'分析员', align:'center',rowspan:2}, \n");
        shead.append(" {header:'录入员', align:'center',rowspan:2} \n");
        
        shead.append(" ],\n");
        
        xhead=new StringBuffer(xhead.toString().substring(0,xhead.toString().lastIndexOf(",")));
        xhead.append(" ] \n ");
       
//        System.out.println(sql);
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("xidxxb");

		egu.addPaging(25);
		
		List list=egu.gridColumns;
		
		for(int i=0;i<list.size();i++){
			GridColumn gc=(GridColumn)list.get(i);
//			gc.setWidth(100);
			
			if(gc.dataIndex.equalsIgnoreCase("MINGC")){
				gc.setWidth(120);
//				System.out.println("MINGC");
			}else{
				gc.setWidth(60);
			}
			
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
		
		
		ComboBox fenxy=new ComboBox();
		egu.getColumn("fenxy").setEditor(fenxy);
		egu.getColumn("fenxy").setComboEditor(egu.gridId, new IDropDownModel(" select id,quanc from renyxxb where zhiw='入炉化验员' "));
		egu.getColumn("fenxy").setReturnId(false);
		egu.getColumn("fenxy").editor.allowBlank=true;
		
		
		
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("lury").setEditor(null);
		
		
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
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		

		egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){\n" +
				"if(e.record.get('MINGC')=='平均值'){e.cancel=true;}\n" +
				"" +
				"} \n);");
		setExtGrid(egu);
		con.Close();
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
