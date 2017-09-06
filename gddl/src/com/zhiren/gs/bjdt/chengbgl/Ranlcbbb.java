package com.zhiren.gs.bjdt.chengbgl;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Ranlcbbb extends BasePage {
	public boolean getRaw() {
		return true;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}
	
	private boolean reportShowZero(){
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}
	
	private int _CurrentPage = -1;
	
	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	
	public int getAllPages() {
		return _AllPages;
	}
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	***************设置消息框******************//
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private String mstrReportName="";
	
	public String getTianzdwQuanc(){
		return getTianzdwQuanc(getDiancxxbId());
	}
	
	public long getDiancxxbId(){
		
		return ((Visit)getPage().getVisit()).getDiancxxb_id();
	}
	
	public boolean isJTUser(){
		return ((Visit)getPage().getVisit()).isJTUser();
	}
	//得到单位全称
	public String getTianzdwQuanc(long gongsxxbID){
		String _TianzdwQuanc="";  
		JDBCcon cn = new JDBCcon();
		
		try {
			ResultSet rs=cn.getResultSet(" select quanc from diancxxb where id="+gongsxxbID);
			while (rs.next()){
				_TianzdwQuanc=rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return _TianzdwQuanc;
	}
	private boolean blnIsBegin = false;
	private String leix="";
	public String getPrintTable(){
		
			return getYueslb();
		
		
	}
	
	private String getDiancCondition(){
		JDBCcon cn = new JDBCcon();
		String diancxxb_id=getTreeid();
		String condition ="";
		ResultSet rs=cn.getResultSet("select jib,id,fuid from diancxxb where id=" +diancxxb_id);
		try {
			if (rs.next()){
				if( rs.getLong("jib")==SysConstant.JIB_JT){
					condition="";
				}else if(rs.getLong("jib")==SysConstant.JIB_GS){
					condition=" and dc.fuid=" +diancxxb_id;
				}else {
					condition=" and dc.id=" +diancxxb_id;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return condition;
	}
	
	private String getGongysCondition(){
		if (getMeikdqmcValue().getId()==-1){
			return "";
		}else{
			return " and dq.id=" +getMeikdqmcValue().getId();
		}
	}
	/*
	 * 修改月数量基础表表头为Locale中变量
	 * 修改时间：2008-12-05
	 * 修改人：王磊
	 */
	private String getYueslb(){
		Visit visit = (Visit) getPage().getVisit();
		long dianc_id=new Long (this.getTreeid()).longValue();
	
		String _Danwqc=getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		
		StringBuffer sbsql = new StringBuffer();
		StringBuffer sbsql2 =new StringBuffer();
		long lngDiancId=getDiancxxbId();//电厂信息表id
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		
		String strLeib   =this.getLeibValue().getValue();
		sbsql.append(" select id from diancxxb where (id="+dianc_id+" or fuid="+dianc_id+")and id not in (\n")
             .append(" select dianc.id from  ranlcbyfxb xb FULL OUTER JOIN RANLCBNCJCYSB sb on(xb.diancxxb_id=sb.diancxxb_id )  left join diancxxb dianc on(xb.diancxxb_id=dianc.id)\n")
             .append(" where  (dianc.id="+dianc_id+" or dianc.fuid="+dianc_id+")\n") 
             .append(" and to_date('"+strDate+"','yyyy-mm-dd')=xb.riq and xb.fenx='"+strLeib+"')  "+"\n ");
		 ResultSet rs=cn.getResultSet(sbsql.toString());
		 sbsql2.append("select decode(leix,'  燃料小计','&nbsp;&nbsp;燃料小计','     合同煤','&nbsp;&nbsp;&nbsp;&nbsp;合同煤','     自购煤','&nbsp;&nbsp;&nbsp;&nbsp;自购煤','  燃油','&nbsp;&nbsp;燃油',leix) leix,duns,danj,jine,caigsl,yunj,danwfrl,caigje,caigdj,\n")
				.append(" caigkj,caigyzf,changnfy,caigbml,caigbmdj,rulsl,ruldj,rulje,\n")
				.append(" rulcs,rulrl,rulbml,rulmzbmdj,rulyzbmdj,rulzhbmdj,rezc,qithysl,qithydj,qithyje,\n")
				.append("  qimjysl,qimdj,qimje from(\n");
		   sbsql2.append("select decode(dianc.id,1,-1,di.xuh) changxuh,decode(dianc.id,1,'月报',di.kouj) kouj ,decode(dianc.id,1,1,di.shujsbzt) shujsbzt,nvl(vw.xuh,nvl(di.xuh,-1)) xuh,xb.leix,nvl(sb.duns,0) duns,nvl(sb.danj,0) danj,nvl(sb.jine,0) jine,xb.caigsl,xb.yunj,xb.danwfrl,xb.caigje,xb.caigdj,")
				  .append(" xb.caigkj,xb.caigyzf,xb.changnfy,xb.caigbml,xb.caigbmdj,xb.rulsl,xb.ruldj,xb.rulje,\n")
				  .append(" xb.rulcs,xb.rulrl,xb.rulbml,xb.rulmzbmdj,xb.rulyzbmdj,xb.rulzhbmdj,xb.rezc,xb.qithysl,xb.qithydj,xb.qithyje,\n")
				  .append("  xb.qimjysl,xb.qimdj,xb.qimje \n")
				  .append("  from  ranlcbyfxb xb  left JOIN RANLCBNCJCYSB sb on(xb.diancxxb_id=sb.diancxxb_id ) left join dianckjpxb di on (xb.diancxxb_id=di.diancxxb_id) left join diancxxb dianc on(xb.diancxxb_id=dianc.id)\n")
				  .append("         left join  vwchengbfxlx vw on(xb.leix=vw.fenx)\n")
				   .append(" where  (dianc.id="+dianc_id+" or dianc.fuid="+dianc_id+")\n") 
				    .append("                       and to_date('"+strDate+"','yyyy-mm-dd')=xb.riq and xb.fenx='"+strLeib+"' and ((sb.leix is null)or(xb.leix=sb.leix))\n");
		
		
		 try {
			while(rs.next()){
				long id= rs.getLong("id");
				sbsql2.append(" union\n")
 .append(" select   decode(fx.id,1,-1,di.xuh) changxuh,decode(fx.id,1,'月报',di.kouj) kouj,decode(fx.id,1,1,di.shujsbzt) shujsbzt,fx.xuh,fx.fenx leix,nvl(sb.duns,0) duns, nvl(sb.danj,0) danj,nvl(sb.jine,0) jine,0 as caigsl,0 as yunj,0 as danwfrl,0 as caigje,0 as caigdj,\n")
 .append(" 0 as caigkj,0 as caigyzf,0 as changnfy,0 as caigbml,0 as caigbmdj,0 as rulsl,0 as ruldj,0 as rulje,\n")
 .append(" 0 as rulcs,0 as rulrl,0 as rulbml,0 as rulmzbmdj,0 as rulyzbmdj,0 as rulzhbmdj,0 as rezc,0 as qithysl,0 as qithydj,0 as qithyje,\n")
 .append(" 0 as qimjysl,0 as qimdj,0 as qimje \n")
 .append(" from (select id, xuh, mingc as fenx from diancxxb where id="+id+" union select "+id+" as id,xuh,fenx from vwchengbfxlx) fx left join dianckjpxb di on(di.diancxxb_id=fx.id) left join RANLCBNCJCYSB sb on(fx.id=sb.diancxxb_id) where ((fx.fenx=sb.leix)or(sb.leix is null))\n");
			 }
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		 sbsql2.append(")where kouj='月报' and shujsbzt=1 order by changxuh,xuh\n");
		 ResultSet rs1=cn.getResultSet(sbsql2.toString());
		Report rt=new Report();
		//定义表头数据
		 String ArrHeader[][]=new String[3][30];
		 ArrHeader[0]=new String[] {"单位","年初结存情况","年初结存情况","年初结存情况","采购情况（不含税）","采购情况（不含税）","采购情况（不含税）","采购情况（不含税）","采购情况（不含税）","采购情况（不含税）","采购情况（不含税）","采购情况（不含税）","采购情况（不含税）","采购情况（不含税）","入炉情况","入炉情况","入炉情况","入炉情况","入炉情况","入炉情况","入炉情况","入炉情况","入炉情况","热值差（千焦/千克）","其他耗用","其他耗用","其他耗用","期末结余","期末结余","期末结余"};
		 ArrHeader[1]=new String[] {"单位","数量（吨）","单价（元/吨）","金额（万元）","数量（吨）","运距（公里）","单位发热量（千焦/千克）","采购金额（万元）","采购价格单价（元/吨）","采购价格单价（元/吨）","采购价格单价（元/吨）","采购价格单价（元/吨）","标煤量（吨）","标煤单价（元/吨）","数量（吨）","单价（元/吨）","金额（万元）","贮损（吨）","热值（千焦/千克）","标煤量（吨）","煤折标煤单价（元/吨）","油折标煤单价（元/吨）","综合标煤单价（元/吨）","热值差（千焦/千克）","数量（吨）","单价（元/吨）","金额（万元）","数量（吨）","单价（元/吨）","金额（万元）"};
		 ArrHeader[2]=new String[] {"单位","数量（吨）","单价（元/吨）","金额（万元）","数量（吨）","运距（公里）","单位发热量（千焦/千克）","采购金额（万元）","合计","矿价","运杂费","厂内费用","标煤量（吨）","标煤单价（元/吨）","数量（吨）","单价（元/吨）","金额（万元）","贮损（吨）","热值（千焦/千克）","标煤量（吨）","煤折标煤单价（元/吨）","油折标煤单价（元/吨）","综合标煤单价（元/吨）","热值差（千焦/千克）","数量（吨）","单价（元/吨）","金额（万元）","数量（吨）","单价（元/吨）","金额（万元）"};
//			列宽
		 int ArrWidth[]=new int[] {90,43,43,43,43,43,43,43,43,43,43,30,43,43,43,43,43,43,43,43,43,43,43,43,43,43,43,43,43,43};
		 int r=0;
		 for(int i=0;i<ArrWidth.length;i++){
			 r=r+ArrWidth[i];
		 }
		 //设置页标题
		rt.setTitle("中国大唐集团公司2008年6月份燃料成本完成情况表",ArrWidth);
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月";
		rt.setDefaultTitle(14,2,strMonth,Table.ALIGN_CENTER);
		//数据
		rt.setBody(new Table(rs1,ArrHeader.length,0,2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(30);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.ShowZero=false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(4);
		rt.body.setColAlign(2,Table.ALIGN_RIGHT);
		
		//页脚
		//rt.createDefautlFooter(ArrWidth);
		//rt.setDefautlFooter(2,1,"批准:",Table.ALIGN_LEFT);
		//rt.setDefautlFooter(6,1,"制表:",Table.ALIGN_LEFT);
		//rt.setDefautlFooter(10,1,"审核:",Table.ALIGN_LEFT);
		//rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();	
	}

	private boolean _QueryClick = false;
	
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	private boolean _CreateChick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
		}
		if(_CreateChick){
			_CreateChick=false;
			Create();
		}
		if(_DeleteChick){
			_DeleteChick=false;
			Delete();
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString1("");
			visit.setDefaultTree(null);
			getDiancmcModels();
			getMeikdqmcModels();
			getSelectData();
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName =visit.getString1();
        }else{
        	if(visit.getString1().equals("")) {
        		mstrReportName =visit.getString1();
            }
        }
		//mstrReportName="diaor04bb";
	
		if(getYuefValue()!=null){
			String yuef = getYuefValue().getValue();
			if(yuef.length()==1){
				leix=leix+"0"+yuef;
			}else{
				leix=leix+yuef;
			}
			if(! leix.substring(0,1).equals("2")){
				//getDiancmcModels(); 
			}
		}
		setUserName(visit.getRenymc());
		setYuebmc(mstrReportName);
		blnIsBegin = true;
		
		
	}
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		
		tb1.addText(new ToolbarText("-"));

		
		tb1.addText(new ToolbarText("类别:"));
		  ComboBox leib=new ComboBox();
		       leib.setTransform("leibDropDown");
		       leib.setWidth(60);
	    tb1.addField(leib);
		  
		tb1.addText(new ToolbarText("-"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf=new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2=new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		
		tb1.addText(new ToolbarText("单位："));
		tb1.addField(tf);
		
		tb1.addItem(tb2);
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);
		setToolbar(tb1);

	}
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit)this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	
	
	private String m_yuebmc;
	public void setYuebmc(String yuebmc){
		m_yuebmc=yuebmc;
	}
	public String getYuebmc(){
		return m_yuebmc;
	}
	
	private void Create() {
		// 为 "刷新" 按钮添加处理程序
	}
	
	private void Delete() {
		JDBCcon con=new JDBCcon();
		String sql="";
		String where="where tongjrq=to_date('"+getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-1','yyyy-MM-dd')";
		if(getDiancmcValue().getId()!=-1){
			where = where+" and diancxxb_id="+getDiancmcValue().getId();
		}
		 if(leix.substring(0, 1).equals("6")){
			 sql="delete from diaor16bb "+where;
		 }else{
			 sql="delete from diaor0"+leix.substring(0, 1)+"bb "+where;
		 }
		 con.getDelete(sql);
		 con.Close();
		 setMsg("删除成功!");
	}

//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean10()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel10(_value);
		}
		
//		煤矿地区
		private boolean _meikdqmc = false;
	    public IPropertySelectionModel getMeikdqmcModel() {
	    	if(((Visit)getPage().getVisit()).getProSelectionModel3() == null){
	    		getMeikdqmcModels();
	    	}
	    	return ((Visit)getPage().getVisit()).getProSelectionModel3();
	    }

	    public IDropDownBean getMeikdqmcValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean3() == null){
				((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getMeikdqmcModel().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean3();
	    }
	    
	    public void setMeikdqmcValue(IDropDownBean Value){
	    	if (Value==null){
	    		((Visit)getPage().getVisit()).setDropDownBean3(Value);
				return;
			}
	    	if (((Visit)getPage().getVisit()).getDropDownBean3().getId()==Value.getId()) {
	    		_meikdqmc = false;
			}else{
				_meikdqmc = true;
			}
	    	((Visit)getPage().getVisit()).setDropDownBean3(Value);
	    }
	    
	    public IPropertySelectionModel getMeikdqmcModels(){
	        long  lngDiancxxbID= ((Visit) getPage().getVisit()).getDiancxxb_id();
	        String sql="";
	        
	        if (((Visit) getPage().getVisit()).isDCUser()){
	        	sql="select distinct gys.id,gys.mingc from diaor16bb d,gongysb gys where d.gongysb_id=gys.id and diancxxb_id=" + lngDiancxxbID;
	        }else if(((Visit) getPage().getVisit()).isJTUser()){
	        	sql="select distinct gys.id,gys.mingc from diaor16bb d ,gongysb gys where d.gongysb_id=gys.id  ";
	        }else {
	        	sql="select distinct gys.id,gys.mingc from diaor16bb d,diancxxb dc,gongysb gys where d.gongysb_id=gys.id and d.diancxxb_id=dc.id and dc.fuid=" + lngDiancxxbID;	        	
	        }
	        ((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"全部"));
	        return ((Visit)getPage().getVisit()).getProSelectionModel3();
	    }
	    
	    public void setMeikdqmcModel(IPropertySelectionModel _value) {
	    	((Visit)getPage().getVisit()).setProSelectionModel3(_value);
	    }
		
	 // 年份下拉框
    private static IPropertySelectionModel _NianfModel;
    public IPropertySelectionModel getNianfModel() {
        if (_NianfModel == null) {
            getNianfModels();
        }
        return _NianfModel;
    }

	private IDropDownBean _NianfValue;
	
    public IDropDownBean getNianfValue() {
        if (_NianfValue == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

// 月份下拉框
	private static IPropertySelectionModel _YuefModel;
	
	public IPropertySelectionModel getYuefModel() {
	    if (_YuefModel == null) {
	        getYuefModels();
	    }
	    return _YuefModel;
	}
	
	private IDropDownBean _YuefValue;
	
	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }
 
//类别下拉框	
    private IDropDownBean leibValue;
    public void setLeibValue(IDropDownBean leibValue){
    	if(this.leibValue!=leibValue){
    	this.leibValue=leibValue;
    	}
    }
    public IDropDownBean getLeibValue(){
    	 if(leibValue==null){
    		 //如果为空设置默认
    		  for (int i = 0; i < getLeibModel().getOptionCount(); i++) {
  	            Object obj = getLeibModel().getOption(i);
  	            if ("当月".equals(((IDropDownBean) obj).getValue())) {
  	                leibValue = (IDropDownBean) obj;
  	                break;
  	            }
  	        }
    	 }
    	return leibValue;
    }
    private IPropertySelectionModel leibModel;
    public void setLeibModel(IPropertySelectionModel leibModel){
    	this.leibModel=leibModel;
    }
    public  IPropertySelectionModel getLeibModel(){
    	if(leibModel==null){
    	 List listLeib = new ArrayList();
    	 listLeib.add(new IDropDownBean(1,"当月"));
    	 listLeib.add(new IDropDownBean(2,"累计"));
          leibModel= new IDropDownModel(listLeib);
    	}
          return leibModel;
    }
//煤矿地区_id	
	private IPropertySelectionModel _IMeikdqIdModel;
	public IPropertySelectionModel getIMeikdqIdModel() {
		if (_IMeikdqIdModel == null) {
			getIMeikdqIdModels();
		}
		return _IMeikdqIdModel;
	}
	
	public IPropertySelectionModel getIMeikdqIdModels() {
		
		String sql="";
		
		sql = "select id,meikdqbm from meikdqb order by meikdqmc";
		
		_IMeikdqIdModel = new IDropDownModel(sql);
		return _IMeikdqIdModel;
	}
//	
	//地区名称
	public boolean _diqumcchange = false;
	private IDropDownBean _DiqumcValue;

	public IDropDownBean getDiqumcValue() {
		if(_DiqumcValue==null){
			_DiqumcValue=(IDropDownBean)getIDiqumcModels().getOption(0);
		}
		return _DiqumcValue;
	}

	public void setDiqumcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiqumcValue != null) {
			id = _DiqumcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diqumcchange = true;
			} else {
				_diqumcchange = false;
			}
		}
		_DiqumcValue = Value;
	}

	private IPropertySelectionModel _IDiqumcModel;

	public void setIDiqumcModel(IPropertySelectionModel value) {
		_IDiqumcModel = value;
	}

	public IPropertySelectionModel getIDiqumcModel() {
		if (_IDiqumcModel == null) {
			getIDiqumcModels();
		}
		return _IDiqumcModel;
	}

	public IPropertySelectionModel getIDiqumcModels() {
		String sql="";
		
		sql = "select mk.meikdqbm,mk.meikdqmc from meikdqb mk order by meikdqmc";
//		System.out.println(sql);
		
		_IDiqumcModel = new IDropDownModel(sql);
		return _IDiqumcModel;
	}
	

	
	public String getcontext() {
			return "";
//		return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
}
