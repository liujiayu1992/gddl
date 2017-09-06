package com.zhiren.dc.huaygl.zhiltz;
/*
 * 作者：zl
 * 时间：2009-10-27
 * 描述：增加邯郸质量台帐个性需求
 */
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
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

public class Zhiltz_hd extends BasePage {
	private static final int RPTTYPE_TZ_ALL = 1;
	private static final int RPTTYPE_TZ_HUOY = 2;
	private static final int RPTTYPE_TZ_QIY = 3;
	
	private static final String BAOBPZB_GUANJZ = "ZHILTZ_HD";// baobpzb中对应的关键字
	private static final String REPORTNAME_HUAYBGD_ZHILLSB="Zhiltz_zhillsb"; 
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
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
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
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
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
    

//	品种下拉框
	public IDropDownBean getPinzValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getPinzModel().getOptionCount()>0) {
				setPinzValue((IDropDownBean)getPinzModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setPinzValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getPinzModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setPinzModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setPinzModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void setPinzModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,mingc from pinzb order by id");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"全部"));
		setPinzModel(new IDropDownModel(list,sb));
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
	public StringBuffer getBaseSql() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String gongys = "";
		String diancid = "" ;
		String pinz = "";
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			ResultSetList rsl = con.getResultSetList("select lx from vwgongysmk where id="+getTreeid());
			if(rsl == null) {
				return null;
			}
			if(rsl.next()) {
				if(rsl.getInt("lx")==1) {
					gongys = " and g.id ="+getTreeid();
				}else {
					gongys = " and m.id ="+getTreeid();
				}
			}
		}
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and f.diancxxb_id ="+ getTreeid_dc();
			}
			
		}
		con.Close();
		String riq ="";
		riq = " and f.daohrq>="+DateUtil.FormatOracleDate(getBRiq())
		+" and f.daohrq<="+DateUtil.FormatOracleDate(getERiq());
		
		if(getPinzValue().getId()!=-1){
			pinz = " and f.pinzb_id="+getPinzValue().getId();
		}
		
		
		StringBuffer sb = new StringBuffer();
		String sql1 = "";
		String sql2 = "";
		sql1 = "select dcmc,fhdw,mkdw,pz,daohrq,fz,jingz,ches,mt,mad,aad,ad,aar,vad,vdaf,qbad*1000,farl*1000,round_new(farl*1000/4.1816,0) as qbar,\n"
			+" sdaf,stad,std,star, hdaf,had, fcad,qgrd*1000\n"
			+" from (select decode(grouping(d.fgsmc)+grouping(d.mingc)+grouping(g.mingc)+grouping(m.mingc),4,'总计',\n" ;
		if(hasDianc(getTreeid_dc())){
			sql1 += "3,decode(grouping(d.mingc)+grouping(m.mingc)+grouping(d.fgsmc),3,'"+ getMingc(getTreeid_dc()) + "',d.mingc),\n";
		}
		sql1 +=	
			"d.mingc) as dcmc,\n" + 
			"decode(grouping(g.mingc)+grouping(m.mingc)-grouping(d.mingc),2,'合计',g.mingc) as fhdw,\n" + 
			"decode(grouping(m.mingc)-grouping(g.mingc)-grouping(d.mingc),1,'小计',m.mingc) as mkdw,\n"
			
		
	
		+ "               decode(grouping(g.mingc) + grouping(m.mingc) +\n"
		+ "                      grouping(f.daohrq),\n"
		+ "                      1,\n"
		+ "                      '小计',\n"
		+ "                      to_char(f.daohrq, 'yyyy-mm-dd')) daohrq,\n"
		+ "               p.mingc pz,\n"
		+ "               c.mingc fz,\n"
		+ "               sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) jingz,\n"
		+ "               sum(f.biaoz) biaoz,\n"
		+ "               sum(f.yuns) yuns,\n"
		+ "               sum(f.yingk) yingk,\n"
		+ "               sum(f.zongkd) zongkd,\n"
		+ "               sum(f.ches) ches,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.mt,"
		+ v.getMtdec()
		+ ") * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), "
		+ v.getMtdec()
		+ ")) as mt,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.mad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as mad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.aad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as aad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.ad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as ad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.aar * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as aar,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.vad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as vad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.vdaf * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as vdaf,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.qbad,"
		+ v.getFarldec()
		+ ") * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), "
		+ v.getFarldec()
		+ ")) as qbad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.qnet_ar,"
		+ v.getFarldec()
		+ ") * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) /\n"
		+ "                                          sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ "))\n"
		+ "                                           * 1000 / 4.1816,\n"
		+ "                                0)) as qbar,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.qnet_ar,"
		+ v.getFarldec()
		+ ") * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), "
		+ v.getFarldec()
		+ ")) as farl,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.sdaf * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as sdaf,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.stad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as stad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.std * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as std,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as star,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.hdaf * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as hdaf,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.had * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as had,\n"
		+

		"               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.fcad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as fcad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.qgrd * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as qgrd,\n"
		+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
		+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
//		+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z\n"
		+ "          from fahb f, gongysb g, meikxxb m,vwdianc d, pinzb p, chezxxb c, zhilb z\n"
		+ "         where f.gongysb_id = g.id(+)\n"
		+ diancid + gongys
		+ pinz +

		"           and f.meikxxb_id = m.id\n"
		+ "           and f.pinzb_id = p.id\n"
		+ "           and f.faz_id = c.id\n"
		+ "           and f.diancxxb_id = d.id\n"
		+ "           and f.zhilb_id = z.id\n"
		+ riq
		+ "  group by grouping sets ('1',(g.mingc),(d.mingc),\n"
		+ "(g.mingc,d.mingc),(g.mingc, d.mingc,m.mingc),(g.mingc, d.mingc,m.mingc,p.mingc), (d.fgsmc, g.mingc, d.mingc,m.mingc, p.mingc,f.daohrq,c.mingc))\n"
		+ "order by '1' desc, grouping(d.mingc) desc,d.mingc, g.mingc,grouping(m.mingc) desc, m.mingc,grouping(p.mingc) desc, p.mingc,grouping(f.daohrq) desc,f.daohrq) ";

		
//		"to_char(f.daohrq,'yyyy-mm-dd') as daohrq,p.mingc pz,c.mingc fz,\n" + 
//		"sum(f.ches) ches\n" + 
//		"from fahb f, vwdianc d, gongysb g, meikxxb m, pinzb p, chezxxb c\n" +
//		"where f.gongysb_id = g.id and f.meikxxb_id = m.id and f.pinzb_id = p.id\n" +
//		"and f.faz_id = c.id \n" +
//		"and f.diancxxb_id = d.id \n"
		
//		sb.append(sql1)
//		.append(pinz).append(" \n")
//		.append(gongys).append(" \n")
//		.append(diancid).append(" \n")
//		.append(riq).append(" \n");
		switch(v.getInt1()) {
		case RPTTYPE_TZ_ALL:
			break;
		case RPTTYPE_TZ_HUOY:
			sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY).append(" \n");
			break;
		case RPTTYPE_TZ_QIY:
			sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_QIY).append(" \n");
			break;
		default : break;
		}
		
//		sql2 = 
//			"group by grouping sets ('1'," ;
//		if(hasDianc(getTreeid_dc())){
//			sql2+="(g.mingc),";
//		}
//		sql2+=	 
//			"(d.mingc),\n" +
//			"(g.mingc,d.mingc),(d.fgsmc,g.mingc,d.mingc,m.mingc,f.daohrq,p.mingc,c.mingc))\n" + 
//			"order by '1' desc,grouping(d.mingc) desc,d.mingc,grouping(m.mingc)-grouping(d.fgsmc),g.mingc desc,grouping(m.mingc) desc,f.daohrq \n";

		sb.append(sql1);
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
//		if(visit.isFencb()) {
//			tb1.addText(new ToolbarText("厂别:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("ChangbSelect");
//			changbcb.setWidth(130);
//			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
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
				,""+visit.getDiancxxb_id(),"forms[0]",getTreeid(),getTreeid());
		
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
		
		tb1.addText(new ToolbarText("品种:"));
		ComboBox pinz = new ComboBox();
		pinz.setTransform("PinzSelect");
		pinz.setWidth(80);
		pinz.setListeners("select:function(own,rec,index){Ext.getDom('PinzSelect').selectedIndex=index}");
		tb1.addField(pinz);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	private String getJiltz(){

		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(getBaseSql().toString());
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
        	ArrHeader = new String[1][25];

    		ArrHeader[0] = new String[] { "厂别","发货单位", "煤矿单位",  "品种","到货日期", "发站",
    				"检质数<br>量(吨)", "车数", "全水<br>分<br>(%)Mt",
    				"空气<br>干燥<br>基水<br>分<br>(%)Mad",
    				"空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
    				"收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
    				"干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
    				"收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
    				"收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
    				"干燥<br>无灰<br>基硫<br>(%)<br>Sdaf",
    				"空气<br>干燥<br>基硫<br>(%)<br>St,ad", "干燥<br>基全<br>硫(%)<br>St,d",
    				"收到<br>基全<br>硫(%)<br>St,ar", "干燥<br>无灰<br>基氢<br>(%)<br>Hdaf",
    				"空气<br>干燥<br>基氢<br>(%)<br>Had",

    				"固定<br>碳<br>(%)<br>Fcad", "干基<br>高位<br>热<br>(J/g)<br>Qgrd" };
    		ArrWidth = new int[23];

    		ArrWidth = new int[] { 85,85, 100,50, 90,  50, 40, 50, 40, 40, 40, 40, 40,
    				40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 };

    		rt.setTitle("煤  质  检  验  台  帐", ArrWidth);
    		rt.title.setRowHeight(2, 40);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

    		rt.setDefaultTitle(1, 5, "卸煤日期:" + getERiq() + "至" + getBRiq(),
    				Table.ALIGN_LEFT);
    		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

    		strFormat = new String[] { "", "", "", "", "", "", "", "0.0",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };

    		rt.setBody(new Table(rs, 1, 0, 3));
    		rt.body.setWidth(ArrWidth);
    		rt.body.setHeaderData(ArrHeader);
    		rt.body.setPageRows(25);
    		rt.body.mergeFixedCols();
    		rt.body.setColFormat(strFormat);
    		for (int i = 1; i <= 23; i++) {
    			rt.body.setColAlign(i, Table.ALIGN_CENTER);
    		}

    		rt.createDefautlFooter(ArrWidth);

    		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
    				Table.ALIGN_LEFT);
    		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
    		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
    		rt.setDefautlFooter(14, 4, "制表:", Table.ALIGN_LEFT);
    		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

    		// 设置页数
    		_CurrentPage = 1;
    		_AllPages = rt.body.getPages();
    		if (_AllPages == 0) {
    			_CurrentPage = 0;
    		}
    		con.Close();
    		rt.body.setRowHeight(21);
//		RPTInit.getInsertSql(v.getDiancxxb_id(),getBaseSql().toString(),rt,getRptTitle(),""+BAOBPZB_GUANJZ+v.getInt1());
        }
     	return rt.getAllPagesHtml();// ph;
	
	}
	
	
	public StringBuffer getBaseSql_pz() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String gongys = "";
		String diancid = "" ;
		String pinz = "";
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			ResultSetList rsl = con.getResultSetList("select lx from vwgongysmk where id="+getTreeid());
			if(rsl == null) {
				return null;
			}
			if(rsl.next()) {
				if(rsl.getInt("lx")==1) {
					gongys = " and g.id ="+getTreeid();
				}else {
					gongys = " and m.id ="+getTreeid();
				}
			}
		}
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and f.diancxxb_id ="+ getTreeid_dc();
			}
			
		}
		con.Close();
		String riq ="";
		riq = " and f.daohrq>="+DateUtil.FormatOracleDate(getBRiq())
		+" and f.daohrq<="+DateUtil.FormatOracleDate(getERiq());
		
		if(getPinzValue().getId()!=-1){
			pinz = " and f.pinzb_id="+getPinzValue().getId();
		}
		
		StringBuffer sb = new StringBuffer();
		String sql1 = "";
		String sql2 = "";
		sql1 = "select dcmc,fhdw,mkdw,pz,daohrq,fz,jingz,ches,mt,mad,aad,ad,aar,vad,vdaf,qbad*1000,farl*1000,round_new(farl*1000/4.1816,0) as qbar,\n"
			+" sdaf,stad,std,star, hdaf,had, fcad,qgrd*1000\n"
			+" from (select decode(grouping(d.fgsmc)+grouping(d.mingc)+grouping(g.mingc)+grouping(m.mingc),4,'总计',\n" ;
		if(hasDianc(getTreeid_dc())){
			sql1 += "3,decode(grouping(d.mingc)+grouping(m.mingc)+grouping(d.fgsmc),3,'"+ getMingc(getTreeid_dc()) + "',d.mingc),\n";
		}
		sql1 +=	
			"d.mingc) as dcmc,\n" + 
			"decode(grouping(g.mingc)+grouping(m.mingc)-grouping(d.mingc),2,'合计',g.mingc) as fhdw,\n" + 
			"decode(grouping(m.mingc)-grouping(g.mingc)-grouping(d.mingc),1,'小计',m.mingc) as mkdw,\n"
			
		
	
		+ "               decode(grouping(g.mingc) + grouping(m.mingc) +\n"
		+ "                      grouping(f.daohrq),\n"
		+ "                      1,\n"
		+ "                      '小计',\n"
		+ "                      to_char(f.daohrq, 'yyyy-mm-dd')) daohrq,\n"
		+ "               p.mingc pz,\n"
		+ "               c.mingc fz,\n"
		+ "               sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) jingz,\n"
		+ "               sum(f.biaoz) biaoz,\n"
		+ "               sum(f.yuns) yuns,\n"
		+ "               sum(f.yingk) yingk,\n"
		+ "               sum(f.zongkd) zongkd,\n"
		+ "               sum(f.ches) ches,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.mt,"
		+ v.getMtdec()
		+ ") * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), "
		+ v.getMtdec()
		+ ")) as mt,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.mad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as mad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.aad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as aad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.ad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as ad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.aar * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as aar,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.vad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as vad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.vdaf * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as vdaf,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.qbad,"
		+ v.getFarldec()
		+ ") * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), "
		+ v.getFarldec()
		+ ")) as qbad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.qnet_ar,"
		+ v.getFarldec()
		+ ") * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) /\n"
		+ "                                          sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ "))\n"
		+ "                                           * 1000 / 4.1816,\n"
		+ "                                0)) as qbar,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.qnet_ar,"
		+ v.getFarldec()
		+ ") * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), "
		+ v.getFarldec()
		+ ")) as farl,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.sdaf * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as sdaf,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.stad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as stad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.std * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as std,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as star,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.hdaf * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as hdaf,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.had * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as had,\n"
		+

		"               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.fcad * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as fcad,\n"
		+ "               decode(sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")),\n"
		+ "                      0,\n"
		+ "                      0,\n"
		+ "                      round_new(sum(z.qgrd * round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")) / sum(round_new(f.laimsl,"
		+ v.getShuldec()
		+ ")), 2)) as qgrd,\n"
		+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
		+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
//		+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z\n"
		+ "          from fahb f, gongysb g, meikxxb m,vwdianc d, pinzb p, chezxxb c, zhilb z\n"
		+ "         where f.gongysb_id = g.id(+)\n"
		+ diancid + gongys
		+ pinz +

		"           and f.meikxxb_id = m.id\n"
		+ "           and f.pinzb_id = p.id\n"
		+ "           and f.faz_id = c.id\n"
		+ "           and f.diancxxb_id = d.id\n"
		+ "           and f.zhilb_id = z.id\n"
		+ riq
		+ "  group by grouping sets ('1',(g.mingc),(d.mingc),\n"
		+ "(g.mingc,d.mingc),(g.mingc, d.mingc,m.mingc),(g.mingc, d.mingc,m.mingc,p.mingc), (d.fgsmc, g.mingc, d.mingc,m.mingc, p.mingc,f.daohrq,c.mingc))\n"
		+ "order by '1' desc, grouping(d.mingc) desc,d.mingc, g.mingc,grouping(m.mingc) desc, m.mingc,grouping(p.mingc) desc, p.mingc,grouping(f.daohrq) desc,f.daohrq) ";

		
//		"to_char(f.daohrq,'yyyy-mm-dd') as daohrq,p.mingc pz,c.mingc fz,\n" + 
//		"sum(f.ches) ches\n" + 
//		"from fahb f, vwdianc d, gongysb g, meikxxb m, pinzb p, chezxxb c\n" +
//		"where f.gongysb_id = g.id and f.meikxxb_id = m.id and f.pinzb_id = p.id\n" +
//		"and f.faz_id = c.id \n" +
//		"and f.diancxxb_id = d.id \n"
		
//		sb.append(sql1)
//		.append(pinz).append(" \n")
//		.append(gongys).append(" \n")
//		.append(diancid).append(" \n")
//		.append(riq).append(" \n");
		switch(v.getInt1()) {
		case RPTTYPE_TZ_ALL:
			break;
		case RPTTYPE_TZ_HUOY:
			sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY).append(" \n");
			break;
		case RPTTYPE_TZ_QIY:
			sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_QIY).append(" \n");
			break;
		default : break;
		}
		
//		sql2 = 
//			"group by grouping sets ('1'," ;
//		if(hasDianc(getTreeid_dc())){
//			sql2+="(g.mingc),";
//		}
//		sql2+=	 
//			"(d.mingc),\n" +
//			"(g.mingc,d.mingc),(d.fgsmc,g.mingc,d.mingc,m.mingc,f.daohrq,p.mingc,c.mingc))\n" + 
//			"order by '1' desc,grouping(d.mingc) desc,d.mingc,grouping(m.mingc)-grouping(d.fgsmc),g.mingc desc,grouping(m.mingc) desc,f.daohrq \n";

		sb.append(sql1);
		return sb;
	}
	
	
	private String getJiltz_pz(){

		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(getBaseSql_pz().toString());
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
        	ArrHeader = new String[1][25];

    		ArrHeader[0] = new String[] { "厂别","发货单位", "煤矿单位",  "品种","到货日期", "发站",
    				"检质数<br>量(吨)", "车数", "全水<br>分<br>(%)Mt",
    				"空气<br>干燥<br>基水<br>分<br>(%)Mad",
    				"空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
    				"收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
    				"干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
    				"收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
    				"收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
    				"干燥<br>无灰<br>基硫<br>(%)<br>Sdaf",
    				"空气<br>干燥<br>基硫<br>(%)<br>St,ad", "干燥<br>基全<br>硫(%)<br>St,d",
    				"收到<br>基全<br>硫(%)<br>St,ar", "干燥<br>无灰<br>基氢<br>(%)<br>Hdaf",
    				"空气<br>干燥<br>基氢<br>(%)<br>Had",

    				"固定<br>碳<br>(%)<br>Fcad", "干基<br>高位<br>热<br>(J/g)<br>Qgrd" };
    		ArrWidth = new int[23];

    		ArrWidth = new int[] { 85,85, 100,50, 90,  50, 40, 50, 40, 40, 40, 40, 40,
    				40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 };

    		rt.setTitle("煤  质  检  验  台  帐", ArrWidth);
    		rt.title.setRowHeight(2, 40);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

    		rt.setDefaultTitle(1, 5, "卸煤日期:" + getERiq() + "至" + getBRiq(),
    				Table.ALIGN_LEFT);
    		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

    		strFormat = new String[] { "","", "", "", "", "", "", "", "0.0",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };

    		rt.setBody(new Table(rs, 1, 0, 3));
    		rt.body.setWidth(ArrWidth);
    		rt.body.setHeaderData(ArrHeader);
    		rt.body.setPageRows(25);
    		rt.body.mergeFixedCols();
    		rt.body.setColFormat(strFormat);
    		for (int i = 1; i <= 23; i++) {
    			rt.body.setColAlign(i, Table.ALIGN_CENTER);
    		}

    		rt.createDefautlFooter(ArrWidth);

    		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
    				Table.ALIGN_LEFT);
    		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
    		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
    		rt.setDefautlFooter(14, 4, "制表:", Table.ALIGN_LEFT);
    		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

    		// 设置页数
    		_CurrentPage = 1;
    		_AllPages = rt.body.getPages();
    		if (_AllPages == 0) {
    			_CurrentPage = 0;
    		}
    		con.Close();
    		rt.body.setRowHeight(21);
//		RPTInit.getInsertSql(v.getDiancxxb_id(),getBaseSql().toString(),rt,getRptTitle(),""+BAOBPZB_GUANJZ+v.getInt1());
        }
     	return rt.getAllPagesHtml();// ph;
	
	}
	
	public String getPrintTable(){
		Visit visit= (Visit) getPage().getVisit();
		
		if(visit.getString13()==null || visit.getString13().equals("")){
			return this.getJiltz();
		}else if(visit.getString13().equals("jiltzpz")){
			return this.getJiltz_pz();
		}
		return "";
		
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
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setInt1(Integer.parseInt(reportType));
		}
		
		String reportOpe=cycle.getRequestContext().getParameter("lr");
		if(reportOpe!=null){
			visit.setString13(reportOpe);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			getGongysDropDownModels();
			if(reportType == null) {
				visit.setInt1(RPTTYPE_TZ_ALL);
			}
			setChangbValue(null);
			setChangbModel(null);
			setPinzValue(null);
			setPinzModel(null);
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