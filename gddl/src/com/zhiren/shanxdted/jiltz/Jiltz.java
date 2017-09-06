package com.zhiren.shanxdted.jiltz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2009-11-20
 * 内容:修改 已经过皮时  才取净重  要不为0
 */
/*
 * 作者:tzf
 * 时间:2009-07-06
 * 内容:数量台帐
 */
public class Jiltz extends BasePage implements PageValidateListener {
	private static final int RPTTYPE_TZ_ALL = 1;
	private static final int RPTTYPE_TZ_HUOY = 2;
	private static final int RPTTYPE_TZ_QIY = 3;

	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ_DATED";// baobpzb中对应的关键字
//	界面用户提示
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
	
//	绑定日期
	private String briq;

	public String getBRiq() {
		if (briq==null && "".equals(briq)){
			briq = DateUtil.FormatDate(new Date());
		}
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		if (eriq==null && "".equals(eriq)){
			eriq = DateUtil.FormatDate(new Date());
		}
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}

//	获取供应商
	public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }
	public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc from vwgongysmk where diancxxb_id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
    	
    	sql+="  union select 0 id, '全部' mingc from dual ";
//        setGongysDropDownModel(new IDropDownModel(sql,"全部")) ;
    	setGongysDropDownModel(new IDropDownModel(sql)) ;
        return ;
    }
    
//  获得选择的树节点的对应的电厂名称   
    private String getMingc(String id){ 
		JDBCcon con=new JDBCcon();
		String mingc=null;
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
			
		}
		rsl.close();
		return mingc;
	}
    
//  判断电厂Tree中所选电厂时候还有子电厂   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    
//	获取相关的SQL
    
    public StringBuffer  getBaseSqlStr(){
    	Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String gongys = "";
		String meik="";
		String fahgl="";
		String diancid = "" ;
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			ResultSetList rsl = con.getResultSetList("select lx from vwgongysmk where id="+getTreeid());
			if(rsl == null) {
				return null;
			}
			if(rsl.next()) {
				if(rsl.getInt("lx")==1) {
					gongys = " where g.id ="+getTreeid();
					fahgl=" and f.gongysb_id="+this.getTreeid();
				}else {
					meik = " and m.id ="+getTreeid();
					fahgl=" and f.meikxxb_id="+this.getTreeid();
				}
			}
		}
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " where d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " where d.id ="+ getTreeid_dc();
			}
			
		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and y.id="+this.getMeikid();
		}
		
		con.Close();
		String riq ="";
		riq = "  f.daohrq>="+DateUtil.FormatOracleDate(getBRiq())
		+" and f.daohrq<="+DateUtil.FormatOracleDate(getERiq());
		
		
		String sql1 = "";
		switch(visit.getInt1()) {
		case RPTTYPE_TZ_ALL:
			break;
		case RPTTYPE_TZ_HUOY:
			sql1+=" and f.yunsfsb_id = "+SysConstant.YUNSFS_HUOY+" \n";
			break;
		case RPTTYPE_TZ_QIY:
			sql1+=" and f.yunsfsb_id = "+SysConstant.YUNSFS_QIY+" \n";
			break;
		default : break;
		}
		
		
		StringBuffer sb = new StringBuffer();
		
		
		sb.append(" select decode(grouping(fx.dc),1,'总计',fx.dc) dc,\n");
		sb.append(" decode(grouping(fx.dc)+grouping(fx.gys),1,'合计',fx.gys) gys,\n");
		sb.append(" decode(grouping(fx.dc)+grouping(fx.gys)+grouping(fx.mk),1,'小计',fx.mk) mk,\n");
		sb.append(" cx.ysdw,\n");
		sb.append(" decode(grouping(fx.dc)+grouping(fx.gys)+grouping(fx.mk)+grouping(cx.ysdw)+grouping(fx.daohrq),1,'小计',to_char(fx.daohrq,'yyyy-MM-dd')) daohrq ,\n");
		sb.append(" fx.pinz,fx.faz,\n");
		
		sb.append(" sum(round_new(cx.biaoz,"+visit.getShuldec()+")) biaoz,\n");
		sb.append(" sum(round_new(cx.jingz,"+visit.getShuldec()+")) jingz,\n");
		sb.append(" sum(round_new(cx.yuns,"+visit.getShuldec()+"))  yuns,\n");
		
		sb.append(" sum(round_new(cx.yingk,"+visit.getShuldec()+")) yingk,\n");
		sb.append(" sum(round_new(cx.zongkd,"+visit.getShuldec()+")) zongkd,\n");
//		sb.append(" sum(cx.ches) ches\n");
		sb.append(" sum(fx.ches) ches\n");
		
		sb.append("  from \n");
		sb.append(" (\n");
		sb.append(" select fh.id ,gh.mingc gys,m.mingc mk,c.mingc faz,p.mingc pinz,dh.mingc dc,fh.daohrq,fh.ches  from \n");
		
		sb.append(" (select f.id,f.diancxxb_id ,f.gongysb_id,f.meikxxb_id,f.faz_id,f.pinzb_id,f.daohrq,f.ches  from    fahb  f where \n");
		sb.append( riq+" "+sql1+fahgl+"  ) fh,\n");
		sb.append(" \n");
		
		sb.append(" (select g.id,g.mingc  from  gongysb g "+gongys+")  gh,\n");
		sb.append(" (select  d.id,d.mingc from  vwdianc d "+diancid+" ) dh,\n");
		sb.append(" pinzb p, chezxxb c,meikxxb m\n");
		
		sb.append(" where fh.diancxxb_id=dh.id and fh.gongysb_id=gh.id and fh.meikxxb_id=m.id and fh.faz_id=c.id  and fh.pinzb_id=p.id"+meik+" \n");
		sb.append(" ) fx,\n");
		
		sb.append(" (select cp.fahb_id,sum(cp.maoz) maoz,sum(cp.piz) piz,sum( decode(cp.QINGCSJ,null,0,cp.biaoz) ) biaoz,sum(cp.yuns) yuns," +
				"sum(cp.yingk) yingk,sum(cp.zongkd) zongkd,sum( decode(cp.QINGCSJ,null,0,cp.maoz-cp.piz-cp.zongkd) ) jingz, " +
				"count(*) ches,y.mingc ysdw\n");
		sb.append(" from chepb cp,yunsdwb y  where  cp.yunsdwb_id=y.id(+) "+yunsdw+" group by cp.fahb_id,y.mingc ) cx where fx.id=cx.fahb_id \n");
		
		sb.append(" group by grouping sets((),(fx.dc),(fx.dc,fx.gys),\n");
		sb.append(" (fx.dc,fx.gys,fx.mk,cx.ysdw),(fx.dc,fx.gys,fx.mk,cx.ysdw,fx.daohrq,fx.pinz,fx.faz))\n");

		sb.append(" order by grouping(fx.dc) desc,fx.dc desc,grouping(fx.gys) desc,fx.gys asc,grouping(fx.mk) desc,cx.ysdw,grouping(fx.daohrq) desc\n");
		sb.append(" \n");
		sb.append(" \n");
		sb.append(" \n");
		return sb;
    }
	public StringBuffer getBaseSql() {
		

    	Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String gongys = "";
		String meik="";
		String fahgl="";
		String diancid = "" ;
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			ResultSetList rsl = con.getResultSetList("select lx from vwgongysmk where id="+getTreeid());
			if(rsl == null) {
				return null;
			}
			if(rsl.next()) {
				if(rsl.getInt("lx")==1) {
					gongys = " where g.id ="+getTreeid();
					fahgl=" and f.gongysb_id="+this.getTreeid();
				}else {
					meik = " and m.id ="+getTreeid();
					fahgl=" and f.meikxxb_id="+this.getTreeid();
				}
			}
		}
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " where d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " where d.id ="+ getTreeid_dc();
			}
			
		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and y.id="+this.getMeikid();
		}
		
		con.Close();
		String riq ="";
		riq = "  f.daohrq>="+DateUtil.FormatOracleDate(getBRiq())
		+" and f.daohrq<="+DateUtil.FormatOracleDate(getERiq());
		
		
		String sql1 = "";
		switch(visit.getInt1()) {
		case RPTTYPE_TZ_ALL:
			break;
		case RPTTYPE_TZ_HUOY:
			sql1+=" and f.yunsfsb_id = "+SysConstant.YUNSFS_HUOY+" \n";
			break;
		case RPTTYPE_TZ_QIY:
			sql1+=" and f.yunsfsb_id = "+SysConstant.YUNSFS_QIY+" \n";
			break;
		default : break;
		}
		
		
		StringBuffer sb = new StringBuffer();
		
		
		sb.append(" select decode(grouping(fx.dc),1,'总计',fx.dc) dc,\n");
		sb.append(" decode(count( distinct fx.gys),1,max(fx.gys),'') gys, \n");
		sb.append(" decode(count( distinct fx.mk),1,max(fx.mk),'') mk, \n");
		sb.append(" decode(count( distinct cx.ysdw),1,max(cx.ysdw),'') ysdw, \n");
		sb.append(" decode(count( distinct fx.daohrq),1, max(to_char(fx.daohrq,'yyyy-MM-dd')),'') daohrq, \n");
		sb.append("decode(count( distinct fx.pinz),1,max(fx.pinz),'') pinz, decode(count( distinct fx.faz),1,max(fx.faz),'') faz,\n");
		
		sb.append(" sum(round_new(cx.biaoz,"+visit.getShuldec()+")) biaoz,\n");
		sb.append(" sum(round_new(cx.jingz,"+visit.getShuldec()+")) jingz,\n");
		sb.append(" sum(round_new(cx.yuns,"+visit.getShuldec()+"))  yuns,\n");
		
		sb.append(" sum(round_new(cx.yingk,"+visit.getShuldec()+")) yingk,\n");
		sb.append(" sum(round_new(cx.zongkd,"+visit.getShuldec()+")) zongkd,\n");
//		sb.append(" sum(cx.ches) ches\n");
		sb.append(" sum(fx.ches) ches\n");
		
		sb.append("  from \n");
		sb.append(" (\n");
		sb.append(" select fh.id ,gh.mingc gys,m.mingc mk,c.mingc faz,p.mingc pinz,dh.mingc dc,fh.daohrq,fh.ches  from \n");
		
		sb.append(" (select f.id,f.diancxxb_id ,f.gongysb_id,f.meikxxb_id,f.faz_id,f.pinzb_id,f.daohrq,f.ches  from    fahb  f where \n");
		sb.append( riq+" "+sql1+fahgl+"  ) fh,\n");
		sb.append(" \n");
		
		sb.append(" (select g.id,g.mingc  from  gongysb g "+gongys+")  gh,\n");
		sb.append(" (select  d.id,d.mingc from  vwdianc d "+diancid+" ) dh,\n");
		sb.append(" pinzb p, chezxxb c,meikxxb m\n");
		
		sb.append(" where fh.diancxxb_id=dh.id and fh.gongysb_id=gh.id and fh.meikxxb_id=m.id and fh.faz_id=c.id  and fh.pinzb_id=p.id"+meik+" \n");
		sb.append(" ) fx,\n");
		
		sb.append(" (select cp.fahb_id,sum(cp.maoz) maoz,sum(cp.piz) piz,sum( decode(cp.QINGCSJ,null,0,cp.biaoz) ) biaoz,sum(cp.yuns) yuns,sum(cp.yingk) yingk," +
				"sum(cp.zongkd) zongkd,sum( decode(cp.QINGCSJ,null,0,cp.maoz-cp.piz-cp.zongkd) ) jingz, count(*) ches,y.mingc ysdw\n");
		sb.append(" from chepb cp,yunsdwb y  where  cp.yunsdwb_id=y.id(+) "+yunsdw+"  group by cp.fahb_id,y.mingc  ) cx where fx.id=cx.fahb_id \n");
		
		sb.append(" group by grouping sets((),(fx.dc))\n");

		sb.append(" order by grouping(fx.dc) desc,fx.dc desc\n");
		sb.append(" \n");
		sb.append(" \n");
		sb.append(" \n");
		return sb;
    
	}
	
//	获取表表标题
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb;
		switch(visit.getInt1()) {
		case RPTTYPE_TZ_ALL:
			sb=Locale.RptTitle_Jiltz_All;
			break;
		case RPTTYPE_TZ_HUOY:
			sb=Locale.RptTitle_Jiltz_Huoy;
			break;
		case RPTTYPE_TZ_QIY:
			sb=Locale.RptTitle_Jiltz_Qiy;
			break;
		default : sb=Locale.RptTitle_Jiltz_All;break;
		}
		return sb;
	}
	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("到货日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
//		
//		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
//				,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"",getTreeid(),getTreeid());
		
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		
		
//运输单位--------------------
		
		DefaultTree mktree=new DefaultTree();
		mktree.setTree_window(this.getDTsql().toString(), "meikTree", visit.getDiancxxb_id()+"", "", this.getMeikid(), this.getMeikid());
		this.setTree(mktree);
		
		TextField tfmk=new TextField();
		tfmk.setId("meikTree_text");
		tfmk.setWidth(100);
		tfmk.setValue(((IDropDownModel)this.getMeikModel()).getBeanValue(Long
				.parseLong(this.getMeikid() == null || "".equals(this.getMeikid()) ? "-1"
						: this.getMeikid())));
		
		

		ToolbarButton tb4 = new ToolbarButton(null, null,
				"function(){meikTree_window.show();}");
		tb4.setIcon("ext/resources/images/list-items.gif");
		tb4.setCls("x-btn-icon");
		tb4.setMinWidth(20);
		
		tb1.addText(new ToolbarText("运输单位"));
		tb1.addField(tfmk);
		tb1.addItem(tb4);
		
		tb1.addText(new ToolbarText("-"));
	//-------------------------
		
		
		
//		电厂Tree
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("格式:"));
		ComboBox ges = new ComboBox();
		ges.setTransform("GesSelect");
		ges.setWidth(60);
//		ges.setListeners("select:function(){document.forms[0].submit();}");
		tb1.addField(ges);
		

		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){ShowJD();document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
//		tb1.addFill();
//		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		
		setToolbar(tb1);
	}
	
	

	
	
//	格式下拉框
	public IDropDownBean getGesValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getGesModel().getOptionCount()>0) {
				setGesValue((IDropDownBean)getGesModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setGesValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getGesModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setGesModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setGesModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public void setGesModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		
		List list=new ArrayList();
		list.add(new IDropDownBean("1","明细"));
		list.add(new IDropDownBean("2","简报"));
		setGesModel(new IDropDownModel(list));
	}
	//------------煤矿----------

	private String meikid = "";
		public String getMeikid() {
			if (meikid.equals("")) {

				meikid = "0";
			}
			return meikid;
		}
		public void setMeikid(String meikid) {
			if(meikid!=null) {
				if(!meikid.equals(this.meikid)) {
					((TextField)getToolbar().getItem("meikTree_text")).setValue
					(((IDropDownModel)this.getMeikModel()).getBeanValue(Long.parseLong(meikid)));
					this.getTree().getTree().setSelectedNodeid(meikid);
				}
			}
			this.meikid = meikid;
		}
	
		
		
		
		//获得运输单位 树形结构sql
		private StringBuffer getDTsql(){
			
			Visit visit=(Visit)this.getPage().getVisit();
			StringBuffer bf=new StringBuffer();
			
			String s=" union  select id ,mingc,1 jib from yunsdwb \n ";
			switch(visit.getInt1()) {
			case RPTTYPE_TZ_ALL:
				break;
			case RPTTYPE_TZ_HUOY:
				s=" ";
				break;
			case RPTTYPE_TZ_QIY:
				break;
			default : break;
			}
			
			bf.append(" select distinct * from ( \n");
			bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
			bf.append(s);
			bf.append(" ) \n");
			bf.append(" order by id,mingc\n");
			
			return bf;
		}
		
		
		DefaultTree mktr;
		
		public DefaultTree getTree() {
			return mktr;
		}
		public void setTree(DefaultTree etu) {
			mktr=etu;
		}

		public String getTreeScriptMK() {
			return this.getTree().getScript();
		}
		
		


		public IPropertySelectionModel getMeikModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
				getMeikModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel3();
		}

		public void setMeikModel(IPropertySelectionModel _value) {
			((Visit) getPage().getVisit()).setProSelectionModel3(_value);
		}

		public void getMeikModels() {
			String sql = "select 0 id,'全部' mingc from dual union select id,mingc  from yunsdwb ";
			setMeikModel(new IDropDownModel(sql));
		}
		
		//-------------------------------------------------
		
		
		
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		
		
		String sql="";
		if(this.getGesValue().getStrId().equals("1")){//明细
			sql=this.getBaseSqlStr().toString();
		}else{//简报
			sql=this.getBaseSql().toString();
		}
//		System.out.println(sql);
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
//		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+v.getInt1()+"' order by xuh");
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+BAOBPZB_GUANJZ+v.getInt1()+"' order by xuh");
        ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='"+BAOBPZB_GUANJZ+v.getInt1()+"'");
        	String Htitle=getRptTitle() ;
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
        	 ArrHeader = new String[][] {{Locale.diancxxb_id_fahb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb,Locale.yunsdw_id_chepb, Locale.daohrq_id_fahb, 
    			Locale.pinzb_id_fahb, Locale.faz_id_fahb,Locale.biaoz_fahb, Locale.jingz_fahb,
    			Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb, Locale.ches_fahb} };
    
    		 ArrWidth = new int[] {100, 100, 120, 120, 70, 70, 50, 50, 50, 50, 50, 50, 50 };
    
    		rt.setTitle(getRptTitle(), ArrWidth);
    		rt.title.fontSize=10;
    		rt.title.setRowHeight(2, 50);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
    		rt.setDefaultTitle(1, 3, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
    				Table.ALIGN_LEFT);
    		rt.setDefaultTitle(4, 4, getBRiq() + " 至 " + getERiq(),
    				Table.ALIGN_CENTER);
    		rt.setDefaultTitle(9, 4, "单位：吨、车", Table.ALIGN_RIGHT);
    
    		strFormat = new String[] { "", "", "", "", "", "", "",
    				"", "", "" };
    		rt.setTitle(getRptTitle(), ArrWidth);
        }
		
//		ResultSet rs = con.getResultSet(getBaseSql(),
//				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//		if(rs == null) {
//			setMsg("数据获取失败！请检查您的网络状况！错误代码 JLTZ-001");
//			return "";
//		}
//		Report rt = new Report();
//
//		String[][] ArrHeader = new String[][] {{ Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.daohrq_id_fahb, 
//			Locale.pinzb_id_fahb, Locale.faz_id_fahb, Locale.laimsl_fahb, Locale.jingz_fahb,
//			Locale.biaoz_fahb, Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb, Locale.ches_fahb} };
//
//		int[] ArrWidth = new int[] { 100, 120, 70, 70, 70, 50, 50, 50, 50, 50, 50, 50 };
//
	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, getBRiq() + " 至 " + getERiq(),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(9, 4, "单位：吨、车", Table.ALIGN_RIGHT);
//
//		String[] arrFormat = new String[] { "", "", "", "", "", "", "",
//				"", "", "" };
//
		rt.setBody(new Table(rs, 1, 0, 3));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(4, Table.ALIGN_CENTER);
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),getBaseSqlStr().toString(),rt,getRptTitle(),""+BAOBPZB_GUANJZ+v.getInt1());
     	return rt.getAllPagesHtml();// ph;
	}
//	工具栏使用的方法
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------电厂Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			
			visit.setInt1(Integer.parseInt(reportType));
			visit.setActivePageName("");
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			if(reportType == null) {
				visit.setInt1(RPTTYPE_TZ_ALL);
			}
			
//			this.setGongysDropDownModel(null);
//			this.setMeikModel(null);
			this.meikid="";
			this.treeid="";
			this.getGongysDropDownModels();
			this.getMeikModels();
			
			
//			this.setGesValue(null);
			this.setGesModels();
			
		
			
//			visit.setDefaultTree(null);
//			setDiancmcModel(null);
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getSelectData();
		}
	}
	
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
//	页面登陆验证
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
}
