package com.zhiren.jingjfx;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * ���ߣ�����
 * ʱ�䣺2009-10-20
 * �������޸ķ�������list�����صķ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-18
 * �������޸�ú���Ĵ�������õķ���
 */
/**
 * @author Rock
 * @since 2009-08-20
 * @version �е����v1.0
 * @discription �е���ʾ��÷������Ի�
 */
public class Jingjfxgxh extends BasePage implements PageValidateListener{
	
//	�����Ƿ���ʾ�жϱ���
	private boolean isShowReport = false;
//	�����û���ʾ
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
	
//	ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	��¼���ڵ��ѡ��
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
		setGongysDropDownModel(new IDropDownModel(sql, "ȫ��"));
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
	
//	���������
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

//	�·�������
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
//		�����Զ��巽��ID ����Aotcr
		Aotcr a = new Aotcr(AotcrId);
//		����page����ʾͼƬ
		a.setIPage(this);
//		����Ĭ�ϵĵ糧ID����
		ZidyParam zdc = new ZidyParam();
		zdc.setId(null);
		zdc.setName("�糧ID");
		zdc.setSvalue(String.valueOf(getTreeid()));
//		��Ĭ�ϵĵ糧ID���������Զ��屨����
		a.getParaMeters().add(zdc);
//		������ݡ��·�����������ֵ
		List plist = a.getParaMeters();
		for(int i=0;i<plist.size();i++) {
			ZidyParam p = (ZidyParam)plist.get(i);
			if("���".equals(p.getName())) {
				p.setSvalue(getNianf());
			}else if("�·�".equals(p.getName())) {
				p.setSvalue(getYuef());
			}
		}
//		��ʼ������
		a.setData();
		return a;
	}
	
	public void initItems(){
//		��ʼ����ർ����toolbar
		initNavigationToolbar();
//		��ʼ����ർ��������
		initNavigation();
//		��ʼ��������
		initToolBars();
	}
//	��ർ��������
	private String treeid;	//�糧��ID
	private String NavigationToolbarScript;	//������toolbar script
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

		tb.addText(new ToolbarText("�糧:"));
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
//		����������Ϣ
		List tnl = new ArrayList();
		tnl.add(new String[]{"0","�ۺϷ���","-1"});
		tnl.add(new String[]{"1","�ۺϷ���","0"});
		tnl.add(new String[]{AnalyOperation.analyId_Meitshcqk_fgs,"ú̿���Ĵ����","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Zhongdhtdhl_fgs,"�ص㶩����ͬ������","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Ranyhyqk_fgs,"ȼ�ͺ������","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Jicmfrlqk_fgs,"����ú���������","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Jicmfrllj_fgs,"����ú������������ۼƣ�","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Rucbmdjwcqk_fgs,"�볧��ú����������","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Rucbmdjwcqklj_fgs,"�볧��ú����������(�ۼ�)","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Rucbmdjwcdb_fgs,"�볧��ú�����������뼯��ͬ����Ƚ�","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Rucbmdjwcdblj_fgs,"�볧��ú����������(�ۼ�)�뼯��ͬ����Ƚ�","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Rulbmdj_fgs,"��¯��ú����������","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Rulbmdjlj_fgs,"��¯��ú����������(�ۼ�)","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Rucrlbmdjc_fgs,"�볧��¯��ú���۲�","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Rezcqk_fgs,"��ֵ�����","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Rezcqkck_fgs,"�󡢳���ֵ�����","1"});
		tnl.add(new String[]{AnalyOperation.analyId_Yunsqk_fgs,"����������","1"});
//		tnl.add(new String[]{AnalyOperation.analyId_Shuifctz_fgs,"ˮ�ֲ����","1"});
//		tnl.add(new String[]{AnalyOperation.analyId_Shouudlqk_fgs,"����Ӫ�����","1"});
		
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(tnl,false);
		
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
		tb1.addText(new ToolbarText("���:"));
		ComboBox cbn = new ComboBox();
		cbn.setWidth(60);
		cbn.setTransform("NianfDropDown");
		cbn.setListeners("change:function(own,nv,ov){document.getElementById('Nainf').value=nv}");
		cbn.setId("NianfDropDown");//���Զ�ˢ�°�
		cbn.setLazyRender(true);//��̬��
		cbn.setEditable(false);
		
		tb1.addField(cbn);
		
		tb1.addText(new ToolbarText("�·�:"));
		ComboBox cby = new ComboBox();
		cby.setWidth(60);
		cby.setTransform("YuefDropDown");
		cby.setListeners("change:function(own,nv,ov){document.getElementById('Yuef').value=nv}");
		cby.setId("YuefDropDown");//���Զ�ˢ�°�
		cby.setLazyRender(true);//��̬��
		cby.setEditable(false);
		
		tb1.addField(cby);
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('SearchButton').click();}");
		String url = "'http://"
			+ this.getRequestCycle().getRequestContext()
					.getServerName()
			+ ":"
			+ this.getRequestCycle().getRequestContext()
					.getServerPort() + ""
			+ this.getEngine().getContextPath() + "/app?service=page/JingjfxUpLoad&year='+document.getElementById('Nianf').value+"
			+"'&month='+document.getElementById('Yuef').value+"
			+"'&diancxxb_id='+document.getElementById('diancTree_id').value"
			;
		ToolbarButton wbtn = new ToolbarButton(null,"�ĵ��ϴ�","function(){window.open("+url+",'JingjfxUpLoad');}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		
		tb1.setWidth("bodyWidth");
		setToolbar(tb1);
	}

	public String getPrintTable() {
//		�����ʾ����
		if(isShowReport){
			isShowReport = false;
			String aotcrHtml = "";
			JDBCcon con = new JDBCcon();
			String diancxxb_id = getTreeid();
			if(getFieldChange()!=null && !"".equals(getFieldChange())){
//				ȡ�ò�������
				String[] result = getFieldChange().split(",");
				String nodeLevel = result[0];
				String nodeId = result[1];
				if(nodeLevel.equals("2")){
					if(nodeId.equals(AnalyOperation.analyId_Meitshcqk_fgs)){
						aotcrHtml = AnalyOperation.getMeitghcztqk_fgs(con,
								diancxxb_id, getNianf(), getYuef());
					}else if(nodeId.equals(AnalyOperation.analyId_Zhongdhtdhl_fgs)){
						aotcrHtml = AnalyOperation.getZhongdhtdhl_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Ranyhyqk_fgs)){
						aotcrHtml = AnalyOperation.getRanyhyqk_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Jicmfrlqk_fgs)){
						aotcrHtml = AnalyOperation.getJicmfrlqk_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Jicmfrllj_fgs)){
						aotcrHtml = AnalyOperation.getJicmfrllj_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Rucbmdjwcqk_fgs)){
						aotcrHtml = AnalyOperation.getRucbmdjwcqk_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Rucbmdjwcqklj_fgs)){
						aotcrHtml = AnalyOperation.getRucbmdjwcqklj_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Rucbmdjwcdb_fgs)){
						aotcrHtml = AnalyOperation.getRucbmdjwcdb_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Rucbmdjwcdblj_fgs)){
						aotcrHtml = AnalyOperation.getRucbmdjwcdblj_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Rulbmdj_fgs)){
						aotcrHtml = AnalyOperation.getRulbmdjwcqk_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Rulbmdjlj_fgs)){
						aotcrHtml = AnalyOperation.getRulbmdjwcqklj_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if(nodeId.equals(AnalyOperation.analyId_Rucrlbmdjc_fgs)){
						aotcrHtml = AnalyOperation.getRucrlbmdjc_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if (nodeId.equals(AnalyOperation.analyId_Rezcqk_fgs)){
						aotcrHtml = AnalyOperation.getRezcqk_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if (nodeId.equals(AnalyOperation.analyId_Rezcqkck_fgs)){
						aotcrHtml = AnalyOperation.getKuangcrzc_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if (nodeId.equals(AnalyOperation.analyId_Yunsqk_fgs)){
						aotcrHtml = AnalyOperation.getYunsqk_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if (nodeId.equals(AnalyOperation.analyId_Shuifctz_fgs)){
						aotcrHtml = AnalyOperation.getShuifctz_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}else if (nodeId.equals(AnalyOperation.analyId_Shouudlqk_fgs)){
						aotcrHtml = AnalyOperation.getShouudlqk_fgs(con, 
								diancxxb_id, getNianf(), getYuef(), this.getPage());
					}
				}else{
//					aotcrHtml = "<font size = 10 color=green>�ۺϱ���ģ�����ڿ�����...</font>";
					aotcrHtml += AnalyOperation.getMeitghcztqk_fgs(con,
							diancxxb_id, getNianf(), getYuef());
					aotcrHtml += AnalyOperation.getZhongdhtdhl_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getRanyhyqk_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getJicmfrlqk_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getJicmfrllj_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getRucbmdjwcqk_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getRucbmdjwcqklj_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getRucbmdjwcdb_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getRucbmdjwcdblj_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getRulbmdjwcqk_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getRulbmdjwcqklj_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getRucrlbmdjc_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getRezcqk_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getKuangcrzc_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
					aotcrHtml += AnalyOperation.getYunsqk_fgs(con, 
							diancxxb_id, getNianf(), getYuef(), this.getPage());
				}
			}else{
				setMsg("��ѡ��Ҫ��ѯ�ľ��÷�����Ŀ");
			}
			return aotcrHtml;
		}
		return "";
	}
	
	public String getAotcrCss() {
		
		return "";
	}
	
//	 ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit v = (Visit) getPage().getVisit();
		if (!v.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			v.setActivePageName(getPageName().toString());
			initItems();
			setTreeid(String.valueOf(v.getDiancxxb_id()));
			setGongysDropDownModel(null);
			Date last = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intMonth);
			setNianf(String.valueOf(DateUtil.getYear(last)));
			setYuef(String.valueOf(DateUtil.getMonth(last)));
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