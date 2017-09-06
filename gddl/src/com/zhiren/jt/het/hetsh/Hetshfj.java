package com.zhiren.jt.het.hetsh;

import java.sql.ResultSet;
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
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;

	public class Hetshfj extends BasePage {
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean5()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)+ "';";
		} else {
			return "";
		}
	}
	public boolean isQuanxkz(){
		return((Visit) getPage().getVisit()).getboolean4();
	}
	public void setQuanxkz(boolean value){
		((Visit) getPage().getVisit()).setboolean4(value);
	}
	private int _editTableRow = -1;// 编辑框中选中的行
	public int getEditTableRow() {
		return _editTableRow;
	}
	private String _msg;
	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getXiaox(){
		if(((Visit) getPage().getVisit()).getString1()==null){
			((Visit) getPage().getVisit()).setString1("");
		}
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setXiaox(String xiaox){
		((Visit) getPage().getVisit()).setString1(xiaox);
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	private boolean TijButton = false;
	public void TijButton(IRequestCycle cycle) {
		TijButton = true;
	}
	private boolean HuitButton = false;
	public void HuitButton(IRequestCycle cycle) {
		HuitButton = true;
	}
	private boolean chakwbButton = false;
	public void chakwbButton(IRequestCycle cycle) {
		chakwbButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (TijButton) {
			TijButton = false;
			tij();
		}
		if (HuitButton) {
			HuitButton = false;
			huit();
		}
		if (chakwbButton) {
			chakwbButton = false;
			chakwb();
		}
	}
	private Hetshbean _EditValue;
	public List getEditValues() {
		if(((Visit) getPage().getVisit()).getList5()==null){
			((Visit) getPage().getVisit()).setList5(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList5();
	}
	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList5(editList);
	}
	public Hetshbean getEditValue() {
		return _EditValue;
	}
	public void setEditValue(Hetshbean EditValue) {
		_EditValue = EditValue;
	}
	

	private boolean tijsh;//是否添加  提交进入流程审核功能
	
	public boolean isTijsh(){
		
		return tijsh;
	}
	
	public void setTijsh(){
		
		tijsh=false;
		
		String sql=" select * from xitxxb  where mingc='合同模板提交审核' and leib='合同模板' and zhi='是' and zhuangt=1 ";
	
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			tijsh=true;
		}
		
	}
	
	public void getSelectData() {
		List list=getEditValues();
		String sql="";
		String xufCondi="";
		list.clear();
		JDBCcon con=new JDBCcon();
		String tableName1="hetb_mb";
		String tableName="hetb";
		String leib="合同";
		String leib1="合同模板";
		long renyxxb_id=((Visit) getPage().getVisit()).getRenyID();
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),this.getTreeid());
		if(Long.parseLong(getTreeid())!=-1&&MainGlobal.getXitxx_item("合同", "显示需方", "0", "是").equals("是")){//){
			xufCondi=" and hetb.diancxxb_id "+
				"in (select id\n" +
				"from(\n" + 
				"select id from diancxxb\n" + 
				"start with (fuid="+Long.parseLong(getTreeid())+" or shangjgsid="+Long.parseLong(getTreeid())+") \n" + 
				"connect by fuid=prior id\n" + 
				")\n" + 
				"union\n" + 
				"select id\n" + 
				"from diancxxb\n" + 
				"where id="+Long.parseLong(getTreeid())+")";
		}
		
		this.setTijsh();
		
//		获得文件保存路径
		String Imagelj=MainGlobal.getXitxx_item("合同", "合同附件路径", "0", "D:\\\\zhiren\\\\het");
		Imagelj=Imagelj + "\\\\" + this.getTreeid() + "\\\\";
		Imagelj=Imagelj.replaceAll("\\\\", "\\\\\\\\");
		
		((Visit) getPage().getVisit()).setExtTree1(etu);
		if(getweizSelectValue().getId()==1){//自己的任务
			 sql=
			"select rownum num, hetxx.* from (select ''Shend,hetb.id,hetbh,gongfdwmc,xufdwmc,qiandrq,\n" +
			"decode(hetb.leib,0,'"+Locale.dianccg_hetlx+"',1,'"+Locale.quyxs_hetlx+"',2,'"+Locale.quycg_hetlx+"')leib,liucztb.id liucztb_id,\n" + 
			"leibztb.mingc zhuangt,sum(hetslb.hetl)hetl,\n" +
//			"nvl('查看','') as chak,\n" +
			"nvl('<a href=# onclick=window.open(''"+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"&filename='||GETHETFILENAME(hetb.id)||''')>查看附件</a>','') as chak, \n"+
			"liucztb.liucb_id,''HISTRYYJ,hetb.gongysb_id,nvl('合同','') as leix\n" + 
			"from hetb,hetslb,liucztb,leibztb,liuclbb\n" + 
			"where hetslb.hetb_id(+)=hetb.id and liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id\n" + 
			"\n" + 
			"and hetb.liucztb_id=liucztb.id and hetb.id in ("+Liuc.getWodrws(tableName, renyxxb_id, leib)+")\n" + //我的任务
			"\n" + 
			"and to_char(hetb.qiandrq,'YYYY')="+getNianfValue().getId()+"\n" + 
			xufCondi+
			"group by  hetb.id,hetbh,gongfdwmc,xufdwmc,qiandrq,leib,leibztb.mingc,liucztb.id,liucztb.liucb_id,hetb.gongysb_id";
			 
			 if(this.isTijsh()){
				 sql+=" union \n"
					 +"select ''Shend,hetb_mb.id,hetb_mb.mingc hetbh,gongfdwmc,xufdwmc,qiandrq,\n" +
						"'' leib,liucztb.id liucztb_id,\n" + 
						"leibztb.mingc zhuangt,sum(hetslb.hetl)hetl,\n" +
//						"nvl('查看','') as chak,\n" +
						"nvl('<a href=# onclick=window.open(''"+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"&filename='||GETHETFILENAME(hetb_mb.id)||''')>查看附件</a>','') as chak, \n"+
						"liucztb.liucb_id,''HISTRYYJ,hetb_mb.gongysb_id,nvl('模板','') as leix\n" + 
						"from hetb_mb,hetslb,liucztb,leibztb,liuclbb\n" + 
						"where hetslb.hetb_id(+)=hetb_mb.id and liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id\n" + 
						"\n" + 
						"and hetb_mb.liucztb_id=liucztb.id and hetb_mb.id in ("+Liuc.getWodrws(tableName1, renyxxb_id, leib1)+")\n" + //我的任务
						"\n" + 
						"and to_char(hetb_mb.qiandrq,'YYYY')="+getNianfValue().getId()+"\n" + 
						xufCondi+
						"group by  hetb_mb.id,hetb_mb.mingc,gongfdwmc,xufdwmc,qiandrq,leibztb.mingc,liucztb.id,liucztb.liucb_id,hetb_mb.gongysb_id ";
			 }
			 
			 sql+="  order by hetbh) hetxx";
 			 ResultSetList rs=con.getResultSetList(sql);
			 while(rs.next()){
				 rs.setString("Shend", String.valueOf(Liuc.getShendId(rs.getLong("liucb_id"),rs.getLong("liucztb_id"))));
			 }
			 rs.beforefirst();
				
			 String str1=
		       		" var url = 'http://'+document.location.host+document.location.pathname;"+
		            "var end = url.indexOf(';');"+
					"url = url.substring(0,end);"+
		       	    "url = url + '?service=page/' + 'GongysReport&gongysb_id='+record.data['GONGYSB_ID'];";
			 ExtGridUtil egu = new ExtGridUtil("SelectFrmDiv", rs);
			 	egu.getColumn("num").setHidden(true);
				egu.getColumn("id").setHidden(true);
				egu.getColumn("gongysb_id").setHidden(true);
				
				egu.getColumn("liucb_id").setHidden(true);
				egu.getColumn("liucztb_id").setHidden(true);
				egu.getColumn("Shend").setHidden(true);
				egu.getColumn("HISTRYYJ").setHidden(true);
				egu.getColumn("hetbh").setHeader("合同编号");
				
				egu.getColumn("gongfdwmc").setHeader("供方单位名称");
				egu.getColumn("gongfdwmc").setRenderer("function(value,p,record){"+str1+
						"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>\"+value+\"</a>\"}"
				);
				egu.getColumn("xufdwmc").setHeader("需方单位名称");
				egu.getColumn("qiandrq").setHeader("签订日期");
				egu.getColumn("leib").setHeader("类别");
				egu.getColumn("zhuangt").setHeader("状态");
				egu.getColumn("hetl").setHeader("合同量");
				egu.getColumn("chak").setHeader("查看");
				
				egu.getColumn("leix").setHeader("类型");
				
				if(!this.isTijsh()){
					egu.getColumn("leix").setHidden(true);
				}

				egu.addToolbarItem("{"+new GridButton("提交","function(){document.getElementById('tijButton').click();}").getScript()+"}");
				egu.addTbarText("-");
				egu.addToolbarItem("{"+new GridButton("回退","function(){document.getElementById('huitButton').click();}").getScript()+"}");
				egu.addTbarText("-");
				egu.addToolbarItem("{"+new GridButton("审核意见","function(){ " +
						"if(SelectFrmDiv_sm.hasSelection()){" +
						" if(weiz.getRawValue()=='流程中'){ " +
						" document.getElementById('DivMy_opinion').className = 'x-hidden';}" +
						"	window_panel.show(); " +
						"  rec = SelectFrmDiv_grid.getSelectionModel().getSelections(); " +
						" for(var i=0;i<rec.length;i++){ " +
						" 	var strtmp=rec[i].get('HISTRYYJ').replace(/_/g,'\\n');" +
						" document.getElementById('Histry_opinion').value=strtmp;}"+
						" }else{ "+
						" 	alert('请选择一个合同!');} "+
						"}").getScript()+"}");

				egu.addTbarText("-");
				egu.addTbarText("签订年份:");
				//egu.addToolbarItem("cbo_NianfDropDown");
				ComboBox comb1=new ComboBox();
				comb1.setId("nianf");
				comb1.setWidth(100);
				comb1.setTransform("NianfDropDown");
				comb1.setLazyRender(true);//动态绑定
				egu.addToolbarItem(comb1.getScript());
				//
				egu.addTbarText("-");
				egu.addTbarText("状态:");
				ComboBox comb2=new ComboBox();
				comb2.setId("weiz");
				comb2.setWidth(100);
				comb2.setTransform("weizSelect");
				comb2.setLazyRender(true);//动态绑定weizSelect
				egu.addToolbarItem(comb2.getScript());
				egu.addOtherScript("nianf.on('select',function(){document.forms[0].submit();});weiz.on('select',function(){document.forms[0].submit()});");
				egu.addOtherScript("SelectFrmDiv_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=record.get('NUM') - 1;});");

				if(MainGlobal.getXitxx_item("合同", "显示需方", "0", "是").equals("是")){//
					egu.addTbarText("-");
					egu.addTbarText("单位:");
					egu.addTbarTreeBtn("diancTree");
				}
			
				//意见
				List tmp= new ArrayList();
				
				for(int i=0;i<rs.getRows();i++){
					String strtmp="";
					tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,1)));
					for(int j=0;j<tmp.size();j++){
						strtmp+=((Yijbean)tmp.get(j)).getXitts()+"_"+(((Yijbean)tmp.get(j)).getYij()==null?"":((Yijbean)tmp.get(j)).getYij());
					}
					egu.setDataValue(i, 12, strtmp);
				}
			((Visit) this.getPage().getVisit()).setExtGrid1(egu);
		}else{//流程中的任务（不包括自己的）
				 sql=
				"select ''Shend,hetb.id,hetbh,gongfdwmc,xufdwmc,qiandrq,\n" +
				"decode(hetb.leib,0,'"+Locale.dianccg_hetlx+"',1,'"+Locale.quyxs_hetlx+"',2,'"+Locale.quycg_hetlx+"')leib,liucztb.id liucztb_id,\n" + 
				"leibztb.mingc zhuangt,sum(hetslb.hetl)hetl,\n" +
//				"nvl('查看','') as chak,\n" +
				"nvl('<a href=# onclick=window.open(''"+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"&filename='||GETHETFILENAME(hetb.id)||''')>查看附件</a>','') as chak, \n"+
				"liucztb.liucb_id,''HISTRYYJ,hetb.gongysb_id,nvl('合同','') as leix\n" + 
				"from hetb,hetslb,liucztb,leibztb,liuclbb\n" + 
				"where hetslb.hetb_id=hetb.id and liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id\n" + 
				"\n" + 
				"and hetb.liucztb_id=liucztb.id and hetb.id in ("+Liuc.getLiuczs(tableName, renyxxb_id, leib)+")\n" + //我的任务
				"\n" + 
				"and to_char(hetb.qiandrq,'YYYY')="+getNianfValue().getId()+"\n" + 
				xufCondi+
				"group by  hetb.id,hetbh,gongfdwmc,xufdwmc,qiandrq,leib,leibztb.mingc,liucztb.id,liucztb.liucb_id,hetb.gongysb_id";
				 
				 if(this.isTijsh()){
					 
					 sql+=" union \n"
						 +"select ''Shend,hetb_mb.id,hetb_mb.mingc  hetbh,gongfdwmc,xufdwmc,qiandrq,\n" +
							"'' leib,liucztb.id liucztb_id,\n" + 
							"leibztb.mingc zhuangt,sum(hetslb.hetl)hetl,\n" +
//							"nvl('查看','') as chak,\n" +
							"nvl('<a href=# onclick=window.open(''"+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"&filename='||GETHETFILENAME(hetb_mb.id)||''')>查看附件</a>','') as chak, \n"+
							"liucztb.liucb_id,''HISTRYYJ,hetb_mb.gongysb_id,nvl('模板','') as leix\n" + 
							"from hetb_mb,hetslb,liucztb,leibztb,liuclbb\n" + 
							"where hetslb.hetb_id=hetb_mb.id and liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id\n" + 
							"\n" + 
							"and hetb_mb.liucztb_id=liucztb.id and hetb_mb.id in ("+Liuc.getLiuczsMB(tableName1, renyxxb_id, leib1)+")\n" + //我的任务
							"\n" + 
							"and to_char(hetb_mb.qiandrq,'YYYY')="+getNianfValue().getId()+"\n" + 
							"and hetb_mb.diancxxb_id in (select id\n" + 
							" from(\n" + 
							" select id from diancxxb\n" + 
							" start with fuid="+getTreeid()+"\n" + 
							" connect by fuid=prior id\n" + 
							" )\n" + 
							" union\n" + 
							" select id\n" + 
							" from diancxxb\n" + 
							" where id="+getTreeid()+")\n" + 
							"group by  hetb_mb.id,hetb_mb.mingc,gongfdwmc,xufdwmc,qiandrq,leibztb.mingc,liucztb.id,liucztb.liucb_id,hetb_mb.gongysb_id";
				 }
				 ResultSetList rs=con.getResultSetList(sql);
				 while(rs.next()){
					 rs.setString("Shend", String.valueOf(Liuc.getShendId(rs.getLong("liucb_id"),rs.getLong("liucztb_id"))));
				 }
				 rs.beforefirst();

				 ExtGridUtil egu = new ExtGridUtil("SelectFrmDiv", rs);
				
				String str1=" var url = 'http://'+document.location.host+document.location.pathname;"+
		            "var end = url.indexOf(';');"+
					"url = url.substring(0,end);"+
		       	    "url = url + '?service=page/' + 'GongysReport&gongysb_id='+record.data['GONGYSB_ID'];";

				egu.getColumn("id").setHidden(true);
				egu.getColumn("gongysb_id").setHidden(true);
				egu.getColumn("liucb_id").setHidden(true);
				egu.getColumn("liucztb_id").setHidden(true);
				egu.getColumn("Shend").setHidden(true);
				egu.getColumn("HISTRYYJ").setHidden(true);
				egu.getColumn("hetbh").setHeader("合同编号");
				egu.getColumn("gongfdwmc").setHeader("供方单位名称");
				egu.getColumn("gongfdwmc").setRenderer("function(value,p,record){"+str1+
						"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>\"+value+\"</a>\"}"
				);
				egu.getColumn("xufdwmc").setHeader("需方单位名称");
				egu.getColumn("qiandrq").setHeader("签订日期");
				egu.getColumn("leib").setHeader("类别");
				egu.getColumn("zhuangt").setHeader("状态");
				egu.getColumn("hetl").setHeader("合同量");
				egu.getColumn("chak").setHeader("查看");
				egu.getColumn("leix").setHeader("类型");
//				
				if(!this.isTijsh()){
					egu.getColumn("leix").setHidden(true);
				}

				egu.addToolbarItem("{"+new GridButton("审核意见","function(){ " +
						"if(SelectFrmDiv_sm.hasSelection()){" +
						" if(weiz.getRawValue()=='流程中'){ " +
						" document.getElementById('DivMy_opinion').className = 'x-hidden';}" +
						"	window_panel.show(); " +
						"  rec = SelectFrmDiv_grid.getSelectionModel().getSelections(); " +
						" for(var i=0;i<rec.length;i++){ " +
						" 	var strtmp=rec[i].get('HISTRYYJ').replace(/_/g,'\\n');" +
						" document.getElementById('Histry_opinion').value=strtmp;}"+
						" }else{ "+
						" 	alert('请选择一个合同!');} "+
						"}").getScript()+"}");	
				
				egu.addTbarText("-");
				egu.addTbarText("签订年份:");

				ComboBox comb1=new ComboBox();
				comb1.setId("nianf");
				comb1.setWidth(100);
				comb1.setTransform("NianfDropDown");
				comb1.setLazyRender(true);//动态绑定
				egu.addToolbarItem(comb1.getScript());
				//
				egu.addTbarText("-");
				egu.addTbarText("状态:");
				ComboBox comb2=new ComboBox();
				comb2.setId("weiz");
				comb2.setWidth(100);
				comb2.setTransform("weizSelect");
				comb2.setLazyRender(true);//动态绑定weizSelect
				egu.addToolbarItem(comb2.getScript());
				egu.addOtherScript("nianf.on('select',function(){document.forms[0].submit();});weiz.on('select',function(){document.forms[0].submit()});");
				
				if(MainGlobal.getXitxx_item("合同", "显示需方", "0", "是").equals("是")){//
					egu.addTbarText("-");
					egu.addTbarText("单位:");
					egu.addTbarTreeBtn("diancTree");
				}
				egu.pagsize=1000;
				((Visit) this.getPage().getVisit()).setExtGrid1(egu);
//				意见
				List tmp= new ArrayList();
				
				for(int i=0;i<rs.getRows();i++){
					String strtmp="";
					tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,1)));
					for(int j=0;j<tmp.size();j++){
						strtmp+=((Yijbean)tmp.get(j)).getXitts()+"_"+(((Yijbean)tmp.get(i)).getYij()==null?"":((Yijbean)tmp.get(i)).getYij());
					}

					egu.setDataValue(i, 12, strtmp);
				}
		}
		setXiaox("");
	}
	
	/**1, 根据合同状态动作表找出下一个状态，进行更新
	 * 2,在更新合同状态的同时书写日志
	 */
	private void tij(){
//		List list=getEditValues();
		if(getEditTableRow()!=-1){
			ExtGridUtil ExtGrid1=((Visit) this.getPage().getVisit()).getExtGrid1();
			String tableName="hetb";
			String tableName1="hetb_mb";
			long renyxxb_id=((Visit) getPage().getVisit()).getRenyID();
			
			if(ExtGrid1.griddata[getEditTableRow()][15]!=null && ExtGrid1.griddata[getEditTableRow()][15].equals("合同")){
				Liuc.tij(tableName,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][2]) , renyxxb_id, getXiaox());
			}else if(ExtGrid1.griddata[getEditTableRow()][15]!=null && ExtGrid1.griddata[getEditTableRow()][15].equals("模板")){
				Liuc.tij(tableName1,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][2]) , renyxxb_id, getXiaox());
			}
			setEditTableRow(-1);
		}
	}
	/**1, 根据合同状态动作表找出下一个状态，进行更新
	 * 2,在更新合同状态的同时书写日志
	 */
	private void huit(){
//		List list=getEditValues();
		if(getEditTableRow()!=-1){
			ExtGridUtil ExtGrid1=((Visit) this.getPage().getVisit()).getExtGrid1();
			String tableName="hetb";
			String tableName1="hetb_mb";
//			String leib="合同";
			long renyxxb_id=((Visit) getPage().getVisit()).getRenyID();
			
			if(ExtGrid1.griddata[getEditTableRow()][15]!=null && ExtGrid1.griddata[getEditTableRow()][15].equals("合同")){
				Liuc.huit(tableName,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][2]), renyxxb_id, getXiaox());
			}else if(ExtGrid1.griddata[getEditTableRow()][15]!=null && ExtGrid1.griddata[getEditTableRow()][15].equals("模板")){
				Liuc.huit(tableName1,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][2]), renyxxb_id, getXiaox());
			}
			setEditTableRow(-1);
		}
	}
	private void chakwb(){
		
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean5(true);
			return;
		} else {
			visit.setboolean5(false);
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setDropDownBean1(null);
			((Visit) getPage().getVisit()).setProSelectionModel1(null);
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setDropDownBean3(null);
			((Visit) getPage().getVisit()).setProSelectionModel3(null);
			((Visit) getPage().getVisit()).setDropDownBean4(null);
			((Visit) getPage().getVisit()).setProSelectionModel4(null);
			setXiaox(null);
			setTreeid("");
			visit.setboolean4(true);
			visit.setString2("");
		}
		getSelectData();
	}

	//单位
	public IDropDownBean getdanwSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean1()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getdanwSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean1();
    }
 
    public void setdanwSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean1()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean1().getId()){
	    		((Visit) getPage().getVisit()).setboolean3(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean3(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean1(Value);
    	}
    }
    public void setdanwSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public IPropertySelectionModel getdanwSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getdanwSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void getdanwSelectModels() {
        String sql = 
        	"select id,mingc,jib\n" +
        	"from(\n" + 
        	" select id,mingc,0 as jib\n" + 
        	" from diancxxb\n" + 
        	" where id="+ ((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" union\n" + 
        	" select *\n" + 
        	" from(\n" + 
        	" select id,mingc,level as jib\n" + 
        	"  from diancxxb\n" + 
        	" start with fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" connect by fuid=prior id\n" + 
        	" order SIBLINGS by  xuh)\n" + 
        	" )\n" + 
        	" order by jib";
        List dropdownlist = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String mc = rs.getString("mingc");
				int jib=rs.getInt("jib");
				String nbsp=String.valueOf((char)0xA0);
				for(int i=0;i<jib;i++){
					mc=nbsp+nbsp+nbsp+nbsp+mc;
				}
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
        ((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(dropdownlist)) ;
        return ;
    }
    //位置
    public IDropDownBean getweizSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getweizSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
 
    public void setweizSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }
    public void setweizSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getweizSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getweizSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getweizSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"我的任务"));
        list.add(new IDropDownBean(2,"流程中"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
    
    //我的意见下拉框
    public IDropDownBean getwodyjSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getwodyjSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
 
    public void setwodyjSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean4()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean4().getId()){
	    		((Visit) getPage().getVisit()).setboolean6(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean6(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
    	}
    }
    public void setwodyjSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getwodyjSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getwodyjSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public void getwodyjSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"同意"));
        list.add(new IDropDownBean(2,"不同意"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }
    
    //年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
//			((Visit) getPage().getVisit()).setDropDownBean3(getIDropDownBean(getNianfModel(),new Date().getYear()+1900));
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setNianfValue(IDropDownBean Value) {
		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean3().getId()){
	    		((Visit) getPage().getVisit()).setboolean2(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean2(false);
	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
    public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date())+2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		 ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listNianf)) ;
	}

    //ext代码
	public String getGridHtml() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1().getHtml();
	}
	public String getGridScript() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1().getGridScript();
	}
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
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
//	我的意见
	public void setMy_opinion(String value){
		
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getMy_opinion(){
		
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	//历史意见
	public void setHistry_opinion(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getHistry_opinion(){
		
		return ((Visit) getPage().getVisit()).getString2();
	}
}