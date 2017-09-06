package com.zhiren.jingjfx;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import bsh.EvalError;
import bsh.Interpreter;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.data.Node;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.zidy.Aotcr;
import com.zhiren.zidy.ZidyDataSource;
import com.zhiren.zidy.ZidyParam;

import org.apache.tapestry.contrib.palette.SortMode;
/*
 * 2009-05-19
 * 王磊
 * 修改选择的节点为空时 会发生null指针的错误
 */
public class JingjfxIndex extends BasePage implements PageValidateListener{
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	private boolean isShowReport = false;
	
	private int _CurrentPage = 1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = 1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String FieldChange;
	
	public String getFieldChange() {
		return FieldChange;
	}
	public void setFieldChange(String value) {
		FieldChange = value;
	}
	
	public boolean getRaw() {
		return true;
	}
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getGongysDropDownModels() {
		String sql = "select d.id,d.mingc from diancxxb d ";
		setGongysDropDownModel(new IDropDownModel(sql, "全部"));
		return;
	}
	private String nianf;
	public String getNianf(){
		return nianf;
	}
	public void setNianf(String nianf){
		this.nianf = nianf;
	}
	private String yuef;
	public String getYuef(){
		return yuef;
	}
	public void setYuef(String yuef){
		this.yuef = yuef;
	}
	
//	年份下拉框
    public IDropDownBean getNianfValue() {
        if (((Visit)this.getPage().getVisit()).getDropDownBean1() == null) {
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (getNianf().equals(((IDropDownBean) obj).getStrId())) {
                	setNianfValue((IDropDownBean) obj);
                    break;
                }
            }
        }
        return ((Visit)this.getPage().getVisit()).getDropDownBean1();
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	((Visit)this.getPage().getVisit()).setDropDownBean1(Value);
    }
    
    public IPropertySelectionModel getNianfModel() {
        if (((Visit)this.getPage().getVisit()).getProSelectionModel1() == null) {
            getNianfModels();
        }
        return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
    }
    
    public void setNianfModel(IPropertySelectionModel _value) {
    	((Visit)this.getPage().getVisit()).setProSelectionModel1(_value);
    }

    public void getNianfModels() {
        setNianfModel(new IDropDownModel("select ylabel,yvalue from nianfb where to_number(to_char(sysdate,'yyyy'))+1 >= yvalue"));
    }

//	月份下拉框
	public IDropDownBean getYuefValue() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean2() == null) {
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (getYuef().equals(((IDropDownBean) obj).getStrId())) {
	            	setYuefValue((IDropDownBean) obj);
	                break;
	            }
	        }
	    }
	    return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	
	public void setYuefValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(Value);
	}
	
	public IPropertySelectionModel getYuefModel() {
	    if (((Visit)this.getPage().getVisit()).getProSelectionModel2() == null) {
	        getYuefModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(_value);
    }

    public void getYuefModels() {
        setYuefModel(new IDropDownModel("select mlabel,mvalue from yuefb "));
    }
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString3(nav);
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private boolean _SearchClick = false;
	public void SearchButton(IRequestCycle cycle) {
		_SearchClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SearchClick) {
			_SearchClick = false;
			isShowReport = true;
		}
	}
	
	private Aotcr getAotcr(String AotcrId) {
//		根据自定义方案ID 构造Aotcr
		Aotcr a = new Aotcr(AotcrId);
//		设置page以显示图片
		a.setIPage(this);
//		设置默认的电厂ID参数
		ZidyParam zdc = new ZidyParam();
		zdc.setId(null);
		zdc.setName("电厂ID");
		zdc.setSvalue(String.valueOf(getTreeid()));
//		将默认的电厂ID参数加入自定义报表中
		a.getParaMeters().add(zdc);
//		解析年份、月份两个参数的值
		List plist = a.getParaMeters();
		for(int i=0;i<plist.size();i++) {
			ZidyParam p = (ZidyParam)plist.get(i);
			if("年份".equals(p.getName())) {
				p.setSvalue(getNianf());
			}else if("月份".equals(p.getName())) {
				p.setSvalue(getYuef());
			}
		}
//		初始化数据
		a.setData();
		return a;
	}
	
	public void initItems(){
//		初始化左侧导航栏toolbar
		initNavigationToolbar();
//		初始化左侧导航栏数据
		initNavigation();
//		初始化工具栏
		initToolBars();
	}
//	左侧导航栏处理
	private String treeid;	//电厂树ID
	private String NavigationToolbarScript;	//导航栏toolbar script
	private boolean TreeidChange = false;
	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	
	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				TreeidChange = true;
			}
		}
		this.treeid = treeid;
	}
	
	public String getNavigationScript() {
		return NavigationToolbarScript;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	public void initNavigationToolbar(){
		Toolbar tb = new Toolbar("NavigationTbdiv");
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getGongysDropDownModel())
				.getBeanValue(Long.parseLong(getTreeid() == null
						|| "".equals(getTreeid()) ? "-1" : getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb.addText(new ToolbarText("电厂:"));
		tb.addField(tf);
		tb.addItem(tb2);
		NavigationToolbarScript = tb.getScript();
		Visit visit = (Visit) this.getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "forms[0]", null,
				getTreeid());
		visit.setDefaultTree(dt);
	}

	public void initNavigation(){
		setNavigetion("");
//		导航栏树的查询SQL
		/*String sql = 
			"select id,mingc,jib,fuid from\n" +
			"(select 0 id,-1 fuid,nvl('综合分析','') mingc,1 jib from dual\n" + 
			"union\n" + 
			"select b.id,0 fuid,b.mingc,2 jib from jjfxzhbg b where b.diancxxb_id = "+getTreeid()+"\n" + 
			"union\n" + 
			"select s.id,s.jjfxzhbg_id,s.biaot,3 jib from jjfxzhbgszb s,jjfxzhbg b\n" + 
			"where s.jjfxzhbg_id = b.id and b.diancxxb_id = "+getTreeid()+")\n" + 
			"where jib = level\n" + 
			"connect by fuid = prior id";*/
		//去掉对电厂的限制
		String sql = 
			"select id,mingc,jib,fuid from\n" +
			"(select 0 id,-1 fuid,nvl('综合分析','') mingc,1 jib from dual\n" + 
			"union\n" + 
			"select b.id,0 fuid,b.mingc,2 jib from jjfxzhbg b \n" + 
			"union\n" + 
			"select s.id,s.jjfxzhbg_id,s.biaot,3 jib from jjfxzhbgszb s,jjfxzhbg b\n" + 
			"where s.jjfxzhbg_id = b.id )\n" + 
			"where jib = level\n" + 
			"connect by fuid = prior id";

		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql);
		
		if(getFieldChange() != null && !"".equals(getFieldChange())){
			Node tn = node.getNodeById(getFieldChange().split(",")[1]);
			if(tn!=null){
				((TreeNode)tn.getParentNode()).setExpanded(true);
			}
		}
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}
	
	public void initToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("年份:"));
		ComboBox cbn = new ComboBox();
		cbn.setWidth(60);
		cbn.setTransform("NianfDropDown");
		cbn.setListeners("change:function(own,nv,ov){document.getElementById('Nainf').value=nv}");
		cbn.setId("NianfDropDown");//和自动刷新绑定
		cbn.setLazyRender(true);//动态绑定
		cbn.setEditable(false);
		
		tb1.addField(cbn);
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox cby = new ComboBox();
		cby.setWidth(60);
		cby.setTransform("YuefDropDown");
		cby.setListeners("change:function(own,nv,ov){document.getElementById('Yuef').value=nv}");
		cby.setId("YuefDropDown");//和自动刷新绑定
		cby.setLazyRender(true);//动态绑定
		cby.setEditable(false);
		
		tb1.addField(cby);
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('SearchButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		
		tb1.setWidth("bodyWidth");
		setToolbar(tb1);
	}

	private String getWenzHtml(JDBCcon con, Aotcr a, String id){
		String html = "";
		html += "<center><span><table><tr><td align=left width=600>";
//		取得文字分析
		String wenz = getWenz(con,a,id);
//    	将文字中的特殊字符替换成html语言
    	wenz = wenz.replaceAll("\r\n", "<br>");
    	wenz = wenz.replaceAll("\n", "<br>");
    	wenz = wenz.replaceAll("\r", "<br>");
    	wenz = wenz.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		html += wenz;
    	html += "</td></tr></table></span></center>";
		return html;
	}
	private String getWenz(JDBCcon con, Aotcr a, String id){
//		查找对应公式
		String sql = "select z.id zid,z.jjfxdxmk_id,z.gongsb_id,z.zidyfa_id from jjfxdxfxszb z where z.id="+id;
		String gongs = "";
		ResultSetList rsl = con.getResultSetList(sql);
//		没有设置返回""
		if(rsl.getRows() == 0)return "";
		if(rsl.next()) {
//			没有设置公式返回""
			if(rsl.getLong("gongsb_id")==0){
				return "";
			}
			DataBassUtil dbu = new DataBassUtil();
			try {
				gongs = dbu.getClob("Gongsb", "gongs", rsl.getLong("gongsb_id"));
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		rsl.close();
		if("".equals(gongs))return "";
//		取得Aotcr的数据传入公式并生成分析文字
		List data = a.getDataSources();
    	String wenz = "";
    	for(int i = 0; i<  data.size() ; i++) {
    		ZidyDataSource zds = (ZidyDataSource)data.get(i);
    		rsl = zds.getDataSource();
    		rsl.beforefirst();
    		while(rsl.next()) {
    			try {
    				Interpreter bsh = new Interpreter();
    				bsh.set("年份", getNianf());
    				bsh.set("月份", getYuef());
	    			for(int j=0; j< rsl.getColumnNames().length ; j++) {
						bsh.set(rsl.getColumnNames()[j], rsl.getString(rsl.getColumnNames()[j]));
	    			}
	    			bsh.eval(gongs);
	    			wenz = wenz + String.valueOf(bsh.get("文字分析"));
    			} catch (EvalError e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
    		}
    		rsl.close();
    	}
    	return wenz;
	}
	
	public String getPrintTable() {
//		如果显示报表
		if(isShowReport){
			isShowReport = false;
			String aotcrHtml = "";
			
			if(getFieldChange()!=null && !"".equals(getFieldChange())){
//				取得参数配置
				String[] result = getFieldChange().split(",");
				String sqltmp = " and s.id =" + result[1];
//				"1".equals(result[0])是判断点击的是综合报告
				if("1".equals(result[0])){
					sqltmp = " and s.jjfxzhbg_id =" + result[1];
				}
//				根据设置取得自定义方案ID及经济分析单项设置ID
				/*String sql = 
					"select distinct z.zidyfa_id, z.id from jjfxzhbgszb s,jjfxdxfxszb z\n" +
					"where s.jjfxdxmk_id = z.jjfxdxmk_id and z.diancxxb_id= " + getTreeid() + sqltmp;*/
				//去掉电厂限制
				String sql = 
					"select distinct z.zidyfa_id, z.id from jjfxzhbgszb s,jjfxdxfxszb z\n" +
					"where s.jjfxdxmk_id = z.jjfxdxmk_id " + sqltmp;
				JDBCcon con = new JDBCcon();
				ResultSetList rs = con.getResultSetList(sql);
				while(rs.next()){
					if(rs.getInt("zidyfa_id")!=0){
//						构造Aotcr
						Aotcr a = getAotcr(rs.getString("zidyfa_id"));
//						取得方案的html
						aotcrHtml += a.getHtml();
//						取得方案对应的文字分析的HTML
						aotcrHtml += getWenzHtml(con, a, rs.getString("id"));
					}
				}
				rs.close();
				con.Close();
			}else{
				setMsg("请选择要查询的经济分析项目");
			}
			return aotcrHtml;
		}
		return "";
	}
	
	public String getAotcrCss() {
		
		return "";
	}
	
//	 ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit v = (Visit) getPage().getVisit();
		if (!v.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			v.setActivePageName(getPageName().toString());
			initItems();
			setTreeid(String.valueOf(v.getDiancxxb_id()));
			setGongysDropDownModel(null);
			setNianf(String.valueOf(DateUtil.getYear(new Date())));
			setYuef(String.valueOf(DateUtil.getMonth(new Date())));
			setNianfValue(null);
			setNianfModel(null);
			setYuefValue(null);
			setYuefModel(null);
		}
		initNavigationToolbar();
		initNavigation();
		if(TreeidChange){
			TreeidChange = false;
			((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
			.setSelectedNodeid(treeid);
		}
	}
	
	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
}