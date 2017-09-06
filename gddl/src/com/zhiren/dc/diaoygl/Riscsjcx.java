package com.zhiren.dc.diaoygl;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:赵胜男
 * 日期:2013-03-26
 * 修改内容:将发电量，供电量和上网电量的单位变更为万千瓦时，并在列加入单位。
 * 			
 */
/*
 * 作者：夏峥
 * 时间：2013-07-29
 * 描述：调整宁东特殊需求
 */
public class Riscsjcx extends BasePage {
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
		return getHetltj();
	}
	//  
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");

		buffer.append(
			"select decode(grouping(riq),1,decode(grouping(jizbh),1,'全厂','机组'),to_char(riq,'yyyy-mm-dd'))riq,jizbh,\n" +
			"ROUND(sum(r.fadl)/10000,4)fadl,round(sum(r.gongdl)/10000,4)gongdl,sum(r.gongrl)gongrl,round(sum(shangwdl)/10000,4)shangwdl,\n" + 
			"round(sum(r.fadfhl)/count(*),2)fadfhl\n" + 
			"from riscsjb r,jizb j\n" + 
			"where r.jizb_id=j.id\n" + 
			 " and r.fadl<>0 and to_char(r.riq,'yyyy-mm-dd')>='"+getRiq1()+"' and to_char(r.riq,'yyyy-mm-dd')<='"+getRiq2()+"' "+
			"group by cube(r.riq,j.jizbh)"+
			" order by r.riq");

		String NDSQL=MainGlobal.getXitxx_item("生产", "日生产数据宁东专用", "0", "否");
		if(NDSQL.equals("是")){
			buffer.delete(0, buffer.length());
			buffer.append("SELECT RIQ, JIZBH, FADL, GONGDL, GONGRL, SHANGWDL, FDFHL\n" + 
						"  FROM (SELECT DECODE(GROUPING(RIQ),\n" + 
						"                      1,\n" + 
						"                      DECODE(GROUPING(JIZBH), 1, '全厂', '机组'),\n" + 
						"                      TO_CHAR(RIQ, 'yyyy-mm-dd')) RIQ,\n" + 
						"               JIZBH,\n" + 
						"               ROUND(SUM(R.FADL) / 10000, 4) FADL,\n" + 
						"               ROUND(SUM(R.GONGDL) / 10000, 4) GONGDL,\n" + 
						"               SUM(R.GONGRL) GONGRL,\n" + 
						"               ROUND(SUM(SHANGWDL) / 10000, 4) SHANGWDL,\n" + 
						"               ROUND((SUM(R.FADL / 10000) /\n" + 
						"                     (SUM(J.JIZURL * 24 / 10) * COUNT(*))) * 100,\n" + 
						"                     2) FDFHL\n" + 
						"          FROM RISCSJB R, JIZB J\n" + 
						"         WHERE R.JIZB_ID = J.ID\n" + 
						"           AND R.FADL <> 0\n" + 
						"           AND R.RIQ >= to_date('"+getRiq1()+"','yyyy-mm-dd')\n" + 
						"           AND R.RIQ <= to_date('"+getRiq2()+"','yyyy-mm-dd')\n" + 
						"         GROUP BY CUBE(R.RIQ, J.JIZBH)\n" + 
						"        HAVING GROUPING(RIQ) = 0\n" + 
						"         ORDER BY R.RIQ)\n" + 
						"UNION ALL\n" + 
						"SELECT DECODE(GROUPING(RIQ), 1, '全厂', RIQ) RIQ,\n" + 
						"       JIZBH,\n" + 
						"       SUM(FADL) FADL,\n" + 
						"       SUM(GONGDL) GONGDL,\n" + 
						"       SUM(GONGRL) GONGRL,\n" + 
						"       SUM(SHANGWDL) SHANGWDL,\n" + 
						"       DECODE(SUM(JZRL * 24 * TS),0,0,ROUND(SUM(FADL) / SUM(JZRL * 24 * TS) * 100, 2)) FDFHL\n" + 
						"  FROM (SELECT DECODE(GROUPING(RIQ),\n" + 
						"                      1,\n" + 
						"                      DECODE(GROUPING(JIZBH), 1, '全厂', '机组'),\n" + 
						"                      TO_CHAR(RIQ, 'yyyy-mm-dd')) RIQ,\n" + 
						"               JIZBH,\n" + 
						"               GROUPING(RIQ) A,\n" + 
						"               GROUPING(J.JIZBH) B,\n" + 
						"               ROUND(SUM(R.FADL) / 10000, 4) FADL,\n" + 
						"               ROUND(SUM(R.GONGDL) / 10000, 4) GONGDL,\n" + 
						"               SUM(R.GONGRL) GONGRL,\n" + 
						"               ROUND(SUM(SHANGWDL) / 10000, 4) SHANGWDL,\n" + 
						"               COUNT(*) TS,\n" + 
						"               DECODE(COUNT(*), 0, 0, SUM(JIZURL) / COUNT(*)) / 10 JZRL\n" + 
						"          FROM RISCSJB R, JIZB J\n" + 
						"         WHERE R.JIZB_ID = J.ID\n" + 
						"           AND R.FADL <> 0\n" + 
						"           AND R.RIQ  >= to_date('"+getRiq1()+"','yyyy-mm-dd')\n" + 
						"           AND R.RIQ <=to_date('"+getRiq2()+"','yyyy-mm-dd')\n" + 
						"         GROUP BY CUBE(R.RIQ, J.JIZBH)\n" + 
						"        HAVING GROUPING(RIQ) = 1 AND GROUPING(J.JIZBH) = 0\n" + 
						"         ORDER BY RIQ)\n" + 
						" GROUP BY ROLLUP((RIQ, JIZBH))\n");
		}
					
		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][7];
		ArrWidth = new int[] { 100, 100, 100,100,100,120,100};
		ArrHeader[0] = new String[] { "日期", "机组号", "发电量(万千瓦时)","供电量(万千瓦时)","供热量(吉焦)","上网电量(万千瓦时)","发电负荷率(%)"};
		rt.setBody(new Table(rs, 1, 0, 2));
		//
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("发 电 量 查 询", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 8, "单位:(吨)" ,Table.ALIGN_RIGHT);
//		rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
		rt.body.setPageRows(21);
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 15, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

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
	boolean riqichange = false;
	private String riq1;
	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setRiq1(String riq) {
		if (this.riq1 != null && !this.riq1.equals(riq)) {
			this.riq1 = riq;
			riqichange = true;
		}
	}
	
	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq) {
		if (this.riq2!= null && !this.riq2.equals(riq)) {
			this.riq2 = riq;
			riqichange = true;
		}
	}
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("入炉日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("RIQ1");
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("RIQ2");
		tb1.addField(df1);
		ToolbarButton tb = new ToolbarButton(null, "刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
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
			setTreeid(null);
		}
		getToolbars();
		blnIsBegin = true;

	}
//	//单位
//    public IDropDownBean getDiancmcValue() {
//    	if( ((Visit) getPage().getVisit()).getDropDownBean1()==null){
//    		 ((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getDanwSelectModel().getOption(0));
//    	}
//        return  ((Visit) getPage().getVisit()).getDropDownBean1();
//    }
//
//    public void setDanwSelectValue(IDropDownBean Value) {
//    	((Visit) getPage().getVisit()).setDropDownBean1(Value);
//    }
//
//
//    public void setDanwSelectModel(IPropertySelectionModel value) {
//    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
//    }
//
//    public IPropertySelectionModel getDanwSelectModel() {
//        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
//            getDanwSelectModels();
//        }
//        return ((Visit) getPage().getVisit()).getProSelectionModel1();
//    }
//
//    public void getDanwSelectModels() {
//    	 String sql = 
//         	"select id,mingc,jib\n" +
//         	"from(\n" + 
//         	" select id,mingc,0 as jib\n" + 
//         	" from diancxxb\n" + 
//         	" where id="+ ((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
//         	" union\n" + 
//         	" select *\n" + 
//         	" from(\n" + 
//         	" select id,mingc,level as jib\n" + 
//         	"  from diancxxb\n" + 
//         	" start with fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
//         	" connect by fuid=prior id\n" + 
//         	" order SIBLINGS by  xuh)\n" + 
//         	" )\n" + 
//         	" order by jib";
//         List dropdownlist = new ArrayList();
// 		JDBCcon con = new JDBCcon();
// 		try {
// 			ResultSet rs = con.getResultSet(sql);
// 			while (rs.next()) {
// 				int id = rs.getInt("id");
// 				String mc = rs.getString("mingc");
// 				int jib=rs.getInt("jib");
// 				String nbsp=String.valueOf((char)0xA0);
// 				for(int i=0;i<jib;i++){
// 					mc=nbsp+nbsp+nbsp+nbsp+mc;
// 				}
// 				dropdownlist.add(new IDropDownBean(id, mc));
// 			}
// 			rs.close();
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 		} finally {
// 			con.Close();
// 		}
//         ((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(dropdownlist)) ;
//         return ;
//    }
    // 供应商
	
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
//    	}
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
    //年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(getIDropDownBean(getNianfModel(),new Date().getYear()+1900));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setNianfValue(IDropDownBean Value) {
//		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean3()!=null){
//			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean3().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData2(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData2(false);
//	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean3(Value);
//		}
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
    private IDropDownBean getIDropDownBean(IPropertySelectionModel model,long id) {
        int OprionCount;
        OprionCount = model.getOptionCount();
        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) model.getOption(i)).getId() == id) {
                return (IDropDownBean) model.getOption(i);
            }
        }
        return null;
    }
    //类型
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean4()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean4().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
//    	}
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
        list.add(new IDropDownBean(1,"分厂分矿"));
        list.add(new IDropDownBean(2,"分矿分厂"));
        list.add(new IDropDownBean(3,"分厂"));
        list.add(new IDropDownBean(4,"分矿"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }
    //ext 
    //tree
//    public String getTreeid() {
//		String treeid=((Visit) getPage().getVisit()).getString2();
//		if(treeid==null||treeid.equals("")){
//			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
//		}
//		return ((Visit) getPage().getVisit()).getString2();
//	}
//	public void setTreeid(String treeid) {
//		((Visit) getPage().getVisit()).setString2(treeid);
//	}
//
//	public ExtTreeUtil getTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//	public void setTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//	public String getTreeHtml() {
//		return getTree().getWindowTreeHtml(this);
//	}
//	public String getTreeScript() {
//		return getTree().getWindowTreeScript();
//	}
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
