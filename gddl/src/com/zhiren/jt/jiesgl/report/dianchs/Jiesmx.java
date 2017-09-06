//chh 2008-03-17 修改查询语句与表格格式
package com.zhiren.jt.jiesgl.report.dianchs;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jiesmx extends BasePage {
	public boolean getRaw() {
		return true;
	}
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages; 
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		
		return getDianccx();
		
	}
	// 分厂、分矿统计报表
	private String getDianccx() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strDate =  getBeginriqDate()+" 至 "+getEndriqDate();
		try {
//		String gongysCondition=" select id from gongysb where fuid="+getGongysDropDownValue().getId();
		String diancCondition="";
		  StringBuffer wsql = new StringBuffer();
		  String ArrHeader[][]=new String[3][32];
		  int whith1=0,whith3=0;
		  String beginriq="";
		  String endriq="";
		  String diancid="";
		  String gongysmc="";
		  String tongjff="";
		  String tongjzd="";
		  String dianctjtab="";
		  String dianctjtj="";
		  beginriq=" and js.jiesrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')";//开始日期
		  endriq=" and js.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')";//结束日期
		  wsql.append(beginriq);
		  wsql.append(endriq);
		  
		  String strGongsID = "";
			String danwmc="";//汇总名称
			int jib=this.getDiancTreeJib();
		  if(jib==1){//选集团时刷新出所有的电厂
				strGongsID=" ";
				
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strGongsID = "  and dc.fuid=  " +this.getTreeid();
				
			}else if (jib==3){//选电厂只刷新出该电厂
				strGongsID=" and dc.id= " +this.getTreeid();
				 
			}else if (jib==-1){
				strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
			}
			danwmc=getTreeDiancmc(this.getTreeid());
		  
		  if(getGongysDropDownValue()!=null && getGongysDropDownValue().getId()!=-1){//公司名称
			  gongysmc=" and dq.mingc='"+getGongysDropDownValue().getValue()+"' ";
			  wsql.append(gongysmc);
		  }

//			统计方法				  
			  if(getLeixSelectValue()!=null){//统计方法
				  if(getLeixSelectValue().getId()==1){
					  tongjzd=" select decode(grouping(dc.mingc)+grouping(lx.mingc),2,'总计',1,lx.mingc||'合计',dc.mingc) as diancmc,decode(grouping(dc.mingc)+grouping(dq.mingc),1,'电厂小计',dq.mingc) as meikdqmc,decode(grouping(dq.mingc)+grouping(js.gongysmc),1,'地区小计',js.gongysmc) as fahdw,";
					  tongjff=" group by rollup(lx.mingc,dc.mingc,dq.mingc,(js.gongysmc,js.bianm,js.ruzrq,js.daibch)) \n "
						  	+ " order by grouping(lx.mingc) desc,max(lx.xuh),grouping(dc.mingc) desc,max(px.xuh),grouping(dq.mingc) desc,dq.mingc, grouping(js.gongysmc) desc,js.gongysmc) ";
					  dianctjtab=",dianclbb lx,dianckjpxb px";
					  dianctjtj=" and dc.dianclbb_id=lx.id and dc.id=px.diancxxb_id and px.kouj='月报' ";
					  
					  ArrHeader[0]=new String[] {"电厂名称","煤矿地区","发货单位","结算单编号","入账日期","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","国铁运杂费","国铁运杂费","国铁运杂费","国铁运杂费","国铁运杂费","结算金额","结算金额","结算金额","结算金额","结算金额"};
					  ArrHeader[1]=new String[] {"电厂名称","煤矿地区","发货单位","结算单编号","入账日期","车数","供方数量","验收数量","过衡量","运损","盈亏数量","盈亏金额","热量","亏卡金额","硫","硫折金额","结算数量"," 单价","价款合计","价款税款","价税合计","运费","计税扣除","不含税运费","税款","运杂费合计","亏吨拒费","结算总金额","综合价","标煤单价","标煤单价不含税"};
					  ArrHeader[2]=new String[] {"电厂名称","煤矿地区","发货单位","结算单编号","入账日期","(车)",    " (吨)",   "(吨) ",  " (吨)","(吨) "," (吨)",    "(元)","(Kcal/kg)","(元)","(%)", "(元)",   "(吨)", " (元/吨)",    "(元) ",   " (元)",    "(元) "," (元)","(元) ",			" (元)","(元) ",	   " (元)","(元) "," (元)","(元) "," (元)",         "(元) "};
					  whith1=80;
					  whith3=150;
					  
				  }else if(getLeixSelectValue().getId()==2){
					  dianctjtab="";
					  dianctjtj="";
					  tongjzd=" select decode(dq.mingc,null,'总计',dq.mingc) as meikdqqc,decode(grouping(js.gongysmc)+grouping(dq.mingc),1,'小计',2,'',js.gongysmc) as fahdw,dc.mingc as diancmc, ";
					  tongjff=" group by rollup(dq.mingc,(js.gongysmc,dc.mingc,js.bianm,js.ruzrq,js.daibch)) \n order by grouping(dq.mingc) desc,dq.mingc, grouping(js.gongysmc) desc,js.gongysmc,grouping(dc.mingc) desc,dc.mingc)";
					  ArrHeader[0]=new String[] {"煤矿地区","发货单位","电厂名称","结算单编号","入账日期","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","国铁运杂费","国铁运杂费","国铁运杂费","国铁运杂费","国铁运杂费","结算金额","结算金额","结算金额","结算金额","结算金额"};
					  ArrHeader[1]=new String[] {"煤矿地区","发货单位","电厂名称","结算单编号","入账日期","车数","供方数量","验收数量","过衡量","运损","盈亏数量","盈亏金额","热量","亏卡金额","硫","硫折金额","结算数量"," 单价","价款合计","价款税款","价税合计","运费","计税扣除","不含税运费","税款","运杂费合计","亏吨拒费","结算总金额","综合价","标煤单价","标煤单价不含税"};
					  ArrHeader[2]=new String[] {"煤矿地区","发货单位","电厂名称","结算单编号","入账日期","(车)",    " (吨)",   "(吨) ",  " (吨)","(吨) "," (吨)",    "(元)","(Kcal/kg)","(元)","(%)", "(元)",   "(吨)", " (元/吨)",    "(元) ",   " (元)",    "(元) "," (元)","(元) ",			" (元)","(元) ",	   " (元)","(元) "," (元)","(元) "," (元)",         "(元) "};
					  
					 whith1=150;
					 whith3=80;
				  }
			  }
			  
			 StringBuffer sbsql = new StringBuffer();
			 sbsql.append("select * from ( "+tongjzd+" \n");
			 sbsql.append("         decode(grouping(js.bianm),1,'',getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Jiesdcz','dianc_bianm',js.bianm,js.bianm)) as bianh,to_char(js.ruzrq,'yyyy-mm-dd') as ruzrq,\n");
			 sbsql.append("         sum(js.ches) as ches,sum(getjiesdzb('jiesb',js.id,'数量','gongf')) as gongfsl,\n");
			 sbsql.append("			sum(getjiesdzb('jiesb',js.id,'数量','changf')) as yanssl, sum(js.guohl) as guohl, sum(0) as yuns, \n");
//燃料费用
			 sbsql.append("         sum(getjiesdzb('jiesb',js.id,'数量','yingk')) as yingk,sum(getjiesdzb('jiesb',js.id,'数量','zhejje')) as shulzjje, \n");//加入结算指标表
			 sbsql.append("         decode(sum(js.jiessl),0,0,round(sum(js.jiessl*getjiesdzb('jiesb',js.id,'Qnetar','changf'))/sum(js.jiessl))) as yansrl,  \n");
			
			 sbsql.append("         sum(getjiesdzb('jiesb',js.id,'Qnetar','zhejje')) as relzjje,  \n");
			 
			 sbsql.append("         decode(sum(js.jiessl),0,0,round(sum(js.jiessl*getjiesdzb('jiesb',js.id,'Std','changf'))/sum(js.jiessl),2)) as liu, \n");
			 
			 sbsql.append("         sum(getjiesdzb('jiesb',js.id,'Std','zhejje')) as liuyxje,sum(js.jiessl) as jiessl,\n");
			 sbsql.append("         decode(sum(getjiesdzb('jiesb',js.id,'数量','jies')),0,0,round(sum(getjiesdzb('jiesb',js.id,'数量','jies')*getjiesdzb('jiesb',js.id,'数量','zhejbz'))/sum(getjiesdzb('jiesb',js.id,'数量','jies')),2)) as danj,  \n");
			 sbsql.append("         sum(js.buhsmk) as jiakhj, sum(js.shuik) as jiaksk,sum(js.hansmk) as jiashj, \n");
//国铁运杂费
			 sbsql.append("         sum(nvl(jyf.guotyf,0)) as yunf, sum(nvl(jyf.jiskc,0)) as jiskc,sum(nvl(jyf.buhsyf,0)) as buhsyf,sum(nvl(jyf.shuik,0)) as shuik, \n");
			 sbsql.append("         sum(nvl(jyf.hansyf,0)) as yunzfhj,0 as kundjf, \n");
			 
			sbsql.append("         sum(js.hansmk+nvl(jyf.hansyf,0)) as cfjieszje, \n");
			
			sbsql.append("         decode(sum(js.jiessl),0,0,round(sum(js.hansmk)/sum(js.jiessl),2))+nvl(decode(sum(getjiesdzb('jiesb',js.id,'数量','gongf')),0,0,round(sum(nvl(getjiesdzb('jiesb',js.id,'数量','gongf'),0))/sum(getjiesdzb('jiesb',js.id,'数量','gongf')),2)),0) as cfzonghj, \n" );
			
			sbsql.append("         (decode(decode(sum(js.jiessl),0,0,round(sum(js.jiessl*getjiesdzb('jiesb',js.id,'Qnetar','changf'))/sum(js.jiessl))),0,0,  \n");
			sbsql.append("			round((decode(sum(js.jiessl),0,0,round(sum(js.hansmk)/sum(js.jiessl),2))+nvl(decode(sum(getjiesdzb('jiesb',js.id,'数量','gongf')),0,0,round(sum(nvl(jyf.hansyf,0))/sum(getjiesdzb('jiesb',js.id,'数量','gongf')),2)),0))*max(7000)  \n");
			sbsql.append("			 /decode(sum(js.jiessl),0,0,round(sum(js.jiessl*getjiesdzb('jiesb',js.id,'数量','jies'))/sum(js.jiessl))),2))) as cfbiaomdj, \n");
			
			                
			sbsql.append("			decode(decode(sum(js.jiessl),0,0,round(sum(js.jiessl*getjiesdzb('jiesb',js.id,'Qnetar','jies'))/sum(js.jiessl))),0,0, \n");
			sbsql.append("  		round((decode(sum(js.jiessl),0,0,round(sum(js.hansmk)/sum(js.jiessl),2))+nvl(decode(sum(getjiesdzb('jiesb',js.id,'数量','gongf')),0,0,round(sum(nvl(jyf.hansyf,0))/sum(getjiesdzb('jiesb',js.id,'数量','gongf')),2)),0) \n");
			sbsql.append("   		-decode(sum(js.jiessl),0,0,round(sum(js.shuik)/sum(js.jiessl),2)) \n");
			sbsql.append("  		-nvl(decode(sum(getjiesdzb('jiesb',js.id,'数量','gongf')),0,0,round(sum(nvl(jyf.shuik,0))/sum(getjiesdzb('jiesb',js.id,'数量','gongf')),2)),0))*7000 \n");
			sbsql.append("   		/decode(sum(js.jiessl),0,0,round(sum(js.jiessl*getjiesdzb('jiesb',js.id,'Qnetar','jies'))/sum(js.jiessl))),2)) as cfbiaomdjbhs  \n");
		 
			sbsql.append("    		from jiesb js,jiesyfb jyf,diancxxb dc,gongysb dq"+dianctjtab+" \n");
			sbsql.append("    		where js.diancxxb_id = dc.id(+)  and js.gongysb_id=dq.id(+) and jyf.jiesb_id=js.id  "+dianctjtj+" \n");
			sbsql.append("			  "+strGongsID+gongysmc+" \n");
			sbsql.append(     wsql.toString()+" "+ tongjff + "  \n");

			 int ArrWidth[]=new int[] {whith1,120,whith3,65,100,65,50,60,60,60,45,60,65,55,65,40,65,60,55,75,75,80,70,70,70,65,75,55,80,50,50,50};
			 ResultSet rs = con.getResultSet(sbsql.toString());
			 Report rt = new Report();
			 
//		设置页标题
		rt.setTitle("结算统计台帐",ArrWidth);
	
			rt.setDefaultTitle(1,4,"填制单位:"+MainGlobal.getTableCol("diancxxb", "quanc", "id", String.valueOf(visit.getDiancxxb_id())),Table.ALIGN_LEFT);
	
		rt.setDefaultTitle(14,4,strDate,Table.ALIGN_CENTER);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(32-3,3,"打印日期:"+FormatDate(new Date()),Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,3,0,5));
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(3);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return "";
	}
	

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private void getToolbars(){
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		//日期
		tb1.addText(new ToolbarText("结算日期:"));
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("BeginTime","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("EndTime","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));

		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
		}
		getToolbars();
		blnIsBegin = true;

	}

	
	private String FormatDate(Date _date) {
		if (_date == null) {
//			return MainGlobal.Formatdate("yyyy年 MM月 dd日", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
	private String FormatDate2(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy年 MM月 dd日", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}
    // 供应商
	
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);

    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc\n" +
    	"from gongysb\n" + 
    	"where gongysb.fuid=0";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
    } 
	// 日期
    public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
				stra.setTime(new Date());
				stra.add(Calendar.DATE,-1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
   
    //类型
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);

    }
    public void setLeixSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getLeixSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getLeixSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getLeixSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"分厂"));
        list.add(new IDropDownBean(2,"分矿"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }

    private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
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
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
//		得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
		public int getDiancTreeJib() {
			JDBCcon con = new JDBCcon();
			int jib = -1;
			String DiancTreeJib = this.getTreeid();
			//System.out.println("jib:" + DiancTreeJib);
			if (DiancTreeJib == null || DiancTreeJib.equals("")) {
				DiancTreeJib = "0";
			}
			String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
			ResultSet rs = con.getResultSet(sqlJib.toString());
			
			try {
				while (rs.next()) {
					jib = rs.getInt("jib");
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}finally{
				con.Close();
			}

			return jib;
		}
//		得到电厂树下拉框的电厂名称或者分公司,集团的名称
		public String getTreeDiancmc(String diancmcId) {
			if(diancmcId==null||diancmcId.equals("")){
				diancmcId="1";
			}
			String IDropDownDiancmc = "";
			JDBCcon cn = new JDBCcon();
			
			String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
			ResultSet rs = cn.getResultSet(sql_diancmc);
			try {
				while (rs.next()) {
					IDropDownDiancmc = rs.getString("mingc");
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}finally{
				cn.Close();
			}

			return IDropDownDiancmc;

		}
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
