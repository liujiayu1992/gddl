package com.zhiren.jingjfx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.zidy.Aotcr;
import com.zhiren.zidy.ZidyParam;

/*
 * ���ߣ����
 * ʱ�䣺2011-06-14
 * ��������������
 * 		 ��������ͨ���Զ��巽��ID�;��÷����ۺϱ���ID�õ����Ӧ�ı���
 *		 �����̶���ʽ����̬���ݵı����б�
 *		���ַ���������ݣ�ÿ�����ֵ���������2�������ַ�
 *		�����·��������е�ֵ���̶ܹ�������
 *		����ѡ������������Ϊ����ʱ��������쳣������(����������������ʱ�����ü�����صı����ѯ��Ϣ)
 */

/*
 * ���ߣ����
 * ʱ�䣺2011-06-16
 * ���÷�Χ���������ȼ����Ϣϵͳ
 * ������JJFXDXFXSJB���������ֶ�DIANCXXB_ID
 * 		��JJFXDXFXSJB��jjfxdxfxszb�е����ݷֿ���ѯ�����ڲ�ѯJJFXDXFXSJB��ʱ���ӵ糧��Ϣ��ʶ�����ƣ�
 * 		����ʵ�ֶԲ�ͬ�糧�Ĳ�ѯ����
 */

public class Zonghfx extends BasePage implements PageValidateListener{
	
	private String beginRiq=""; //SQL���õ��Ŀ�ʼ����
	private String endRiq="";//��������
	
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
		_CurrentPage= _value;
	}
	
	private int _AllPages = 1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public String getId() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	public void setId(String id) {
		((Visit)this.getPage().getVisit()).setString3(id);
	}
	
	public boolean getRaw() {
		return true;
	}
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString1();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString1(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString1(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
//	����������
    private boolean leixChange=false;
    public IDropDownBean getLeixValue() {
    	if(((Visit)this.getPage().getVisit()).getDropDownBean5()==null){
    		if (getLeixModel().getOptionCount()>0) {
				setLeixValue((IDropDownBean)getLeixModel().getOption(0));
			}
    	}
    	return ((Visit)this.getPage().getVisit()).getDropDownBean5();
    }
    public void setLeixValue(IDropDownBean v) {
    	if(v!=null&&((Visit)this.getPage().getVisit()).getDropDownBean5()!=null){
    		if(((Visit)this.getPage().getVisit()).getDropDownBean5().getValue().equals(v.getValue())){
    			leixChange=false;
    		}else{
    			leixChange=true;
    		}
    	}
    	((Visit)this.getPage().getVisit()).setDropDownBean5(v);
    }
    public IPropertySelectionModel getLeixModel(){
    	if (((Visit)this.getPage().getVisit()).getProSelectionModel5() == null) {
	        getLeixModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel5();
    }
    public void setLeixModel(IPropertySelectionModel _value){
    	((Visit)this.getPage().getVisit()).setProSelectionModel5(_value);
    }
    public void getLeixModels() {
    	List list=new ArrayList();
    	list.add(new IDropDownBean("0","�¶�"));
    	list.add(new IDropDownBean("1","����"));
    	setLeixModel(new IDropDownModel(list));
    }
	
	private String html = "";
//	���������
    public IDropDownBean getNianfValue() {
        if (((Visit)this.getPage().getVisit()).getDropDownBean1() == null) {
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj).getId()) {
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
	    	int Month=DateUtil.getMonth(new Date());
		        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
		            Object obj = getYuefModel().getOption(i);
		            if (Month == ((IDropDownBean) obj).getId()) {
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
    		setYuefModel(new IDropDownModel("select mvalue, mlabel from yuefb "));
    }
    
//	����������
	public IDropDownBean getJidValue() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean6() == null) {
	    	int Month=DateUtil.getMonth(new Date());
	    		int Season=1;
	    		if(Month<=3){
	    			Season=1;
	    		}else if(Month>3 &&Month<=6){
	    			Season=2;
	    		}else if(Month>6 &&Month<=9){
	    			Season=3;
	    		}else if(Month>9 &&Month<=12){
	    			Season=4;
	    		}
		        for (int i = 0; i < getJidModel().getOptionCount(); i++) {
		            Object obj = getJidModel().getOption(i);
		            if (Season == ((IDropDownBean) obj).getId()) {
		            	setJidValue((IDropDownBean) obj);
		                break;
		            }
		        }
	    }
	    return ((Visit)this.getPage().getVisit()).getDropDownBean6();
	}
	
	public void setJidValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean6(Value);
	}
	
	public IPropertySelectionModel getJidModel() {
	    if (((Visit)this.getPage().getVisit()).getProSelectionModel6() == null) {
	        getJidModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel6();
	}
	
	public void setJidModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel6(_value);
    }

    public void getJidModels() {
    		setJidModel(new IDropDownModel("select mvalue, mlabel from yuefb where mvalue<5"));
    }

//	�������������
	public IDropDownBean getZonghfxValue() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean3() == null) {
            if(getZonghfxModel().getOptionCount() > 0) {
	        	Object obj = getZonghfxModel().getOption(0);
	            setZonghfxValue((IDropDownBean) obj);
            }
	    }
	    return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setZonghfxValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(Value);
	}
	
	public IPropertySelectionModel getZonghfxModel() {
	    if (((Visit)this.getPage().getVisit()).getProSelectionModel3() == null) {
	        getZonghfxModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setZonghfxModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(_value);
    }

    public void getZonghfxModels() {
        setZonghfxModel(new IDropDownModel("select id,mingc from jjfxzhbg"));
        		//"where diancxxb_id = "+((Visit) getPage().getVisit()).getDiancxxb_id()));
    }
    
    private void formatDate(){
		if(getLeixValue().getValue().equals("����")){
			int yuef=(int)getYuefValue().getId();
			switch(yuef){
			case 1:
				beginRiq="to_date('"+getNianfValue().getId()+"-01-01','yyyy-mm-dd')";
				endRiq="to_date('"+getNianfValue().getId()+"-03-31','yyyy-mm-dd')";
				break;
			case 2:
				beginRiq="to_date('"+getNianfValue().getId()+"-04-01','yyyy-mm-dd')";
				endRiq="to_date('"+getNianfValue().getId()+"-06-30','yyyy-mm-dd')";
				break;
			case 3:
				beginRiq="to_date('"+getNianfValue().getId()+"-07-01','yyyy-mm-dd')";
				endRiq="to_date('"+getNianfValue().getId()+"-09-30','yyyy-mm-dd')";
				break;
			case 4:
				beginRiq="to_date('"+getNianfValue().getId()+"-10-01','yyyy-mm-dd')";
				endRiq="to_date('"+getNianfValue().getId()+"-12-31','yyyy-mm-dd')";
				break;
			}
    	}else{
    		beginRiq="to_date('"+getNianfValue().getId()+"-"+getYuefValue().getId()+"-01','yyyy-mm-dd')";
    		String end=getNianfValue().getId()+"-"+getYuefValue().getId()+"-01";
    		endRiq=DateUtil.FormatOracleDate(DateUtil.getLastDayOfMonth(end));//(DateUtil.getDate(end))
        }
	}
    
	private boolean _RefurbishClick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}
	
	private void Refurbish() {
		formatDate();
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strOracleDate = "";
		if(getLeixValue().getValue().equals("�¶�")){
			strOracleDate="to_date('"+getNianfValue().getId()+"-"+getYuefValue().getId()+"-01','yyyy-mm-dd')";
		}else{
			strOracleDate="to_date('"+getNianfValue().getId()+"-"+getJidValue().getId()+"-01','yyyy-mm-dd')";
		}
		StringBuffer sb = new StringBuffer();
		StringBuffer sbhtml = new StringBuffer();
		String zonghfx_id="";
		if(getZonghfxValue()==null){
			zonghfx_id="-1";
		}else{
			zonghfx_id=getZonghfxValue().getStrId();
		}
		sb.append("select * from jjfxzhbgszb s, jjfxzhbg b where s.jjfxzhbg_id = b.id ")
		.append(" and b.id = ").append(zonghfx_id)
		//.append(" and b.diancxxb_id =").append(visit.getDiancxxb_id())
		.append(" \n order by xuh");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			sb.delete(0, sb.length());
			sb.append("select z.id zid,z.jjfxdxmk_id,z.gongsb_id,z.zidyfa_id from jjfxdxfxszb z\n")
			.append("where jjfxdxmk_id =").append(rsl.getString("jjfxdxmk_id"));
			ResultSetList rs = con.getResultSetList(sb.toString());
			while(rs.next()) {
				Aotcr a = getActor(rs.getDouble("zidyfa_id"));
				
				if(a == null) {
					continue;
				}
//				д�뱨����Ŀ��Ŀ����
				sbhtml.append("<center><table width=600 cellPadding=2 cellSpacing=0 ");
				sbhtml.append("style=\"font-family:����;border-color:#000000;border-style:solid;border-width:0px 0px 0px 0px;\">");
				sbhtml.append("<tr height=14>");
				sbhtml.append("<td  width=600 style=\' font-size:16pt;border-width:0px;\'>");
				sbhtml.append(getBiaot(con,rs.getDouble("zidyfa_id"),zonghfx_id));
				sbhtml.append("</td>");
				sbhtml.append("</tr>");
				sbhtml.append("</table></center>");
				
//				д�뱨��������Ϣ
				sbhtml.append(a.getHtml());
				
//				��ò�д�����ַ����������
				sb.delete(0, sb.length());
				String zid=rs.getString("zid");
				sb.append("SELECT J.ID JID, RIQ, WENZFX, J.DIANCXXB_ID\n" );
				sb.append("  FROM JJFXDXFXSJB J\n" );
				sb.append(" WHERE J.JJFXDXFXSZB_ID = "+zid+"\n" );
				sb.append("   AND J.DIANCXXB_ID = "+getTreeid()+"\n" );
				sb.append("   AND J.RIQ = "+strOracleDate);
				ResultSetList rsl_Wz=con.getResultSetList(sb.toString());
				if(rsl_Wz.next()){
					sbhtml.append("<center><span><table><tr><td align=left width=600>");
					String wenzfx = "&nbsp;&nbsp;&nbsp;&nbsp;";
					wenzfx+=rsl_Wz.getString("wenzfx");
					wenzfx = wenzfx.replaceAll("\r\n", "<br>&nbsp;&nbsp;&nbsp;&nbsp;");
					wenzfx = wenzfx.replaceAll("\n", "<br>");
					wenzfx = wenzfx.replaceAll("\r", "<br>&nbsp;&nbsp;&nbsp;&nbsp;&");
					wenzfx = wenzfx.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
					sbhtml.append(wenzfx);
					sbhtml.append("</td></tr></table></span></center>");
				}
				rsl_Wz.close();
			}
			rs.close();
		}
		rsl.close();
		html = sbhtml.toString();
		con.Close();
	}
	
	
//	����Int�ͱ������Ȳ��������ת��Ϊdouble��
	private Aotcr getActor(double faid) {
		if(faid == 0) {
			return null;
		}
		Visit visit = (Visit) getPage().getVisit();
		Aotcr a = new Aotcr(String.valueOf(faid));
		a.setIPage(this);
		ZidyParam zdc = new ZidyParam();
		zdc.setId(null);
		zdc.setName("�糧ID");
		zdc.setSvalue(getTreeid());
		a.getParaMeters().add(zdc);
		
		List plist = a.getParaMeters();
		for(int i=0;i<plist.size();i++) {
			ZidyParam p = (ZidyParam)plist.get(i);
			if("���".equals(p.getName())) {
				p.setSvalue(getNianfValue().getStrId());
			}else if("�·�".equals(p.getName())) {
				p.setSvalue(getYuefValue().getStrId());
			}else if("��ʼ����".equals(p.getName())) {
				p.setSvalue(beginRiq);
			}else if("��������".equals(p.getName())) {
				p.setSvalue(endRiq);
			}
		}
		a.setData();
		return a;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			Refurbish();
		}
	}

	public String getPrintTable() {
		return html;
	}

	public void initToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "form[0]", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);	
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("����:"));
		ComboBox cbn3 = new ComboBox();
		cbn3.setWidth(60);
		cbn3.setTransform("LeixDropDown");
		cbn3.setId("LeixDropDown");//���Զ�ˢ�°�
		cbn3.setLazyRender(true);//��̬��
		cbn3.setEditable(false);
		cbn3.setListeners("change:function(){document.forms[0].submit();}");
		tb1.addField(cbn3);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox cbn = new ComboBox();
		cbn.setWidth(60);
		cbn.setTransform("NianfDropDown");
		cbn.setId("NianfDropDown");//���Զ�ˢ�°�
		cbn.setLazyRender(true);//��̬��
		cbn.setEditable(false);
		cbn.setValue(getNianfValue().getValue());
		tb1.addField(cbn);
		
		if(getLeixValue().getValue().equals("�¶�")){
			tb1.addText(new ToolbarText("�·�:"));
			ComboBox cby = new ComboBox();
			cby.setWidth(60);
			cby.setTransform("YuefDropDown");
			cby.setId("YuefDropDown");//���Զ�ˢ�°�
			cby.setLazyRender(true);//��̬��
			cby.setEditable(false);
			cby.setValue(getYuefValue().getValue());
			tb1.addField(cby);
		}else{
			tb1.addText(new ToolbarText("����:"));
			ComboBox cby = new ComboBox();
			cby.setWidth(60);
			cby.setTransform("JidDropDown");
			cby.setId("JidDropDown");//���Զ�ˢ�°�
			cby.setLazyRender(true);//��̬��
			cby.setEditable(false);
			cby.setValue(getJidValue().getValue());
			tb1.addField(cby);
		}

		
		tb1.addText(new ToolbarText("�ۺϷ���ģ��:"));
		ComboBox cbm = new ComboBox();
		cbm.setWidth(200);
		cbm.setTransform("ZonghfxDropDown");
		cbm.setId("ZonghfxDropDown");//���Զ�ˢ�°�
		cbm.setLazyRender(true);//��̬��
		cbm.setEditable(false);
		
		tb1.addField(cbm);
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){"+MainGlobal.getExtMessageShow("���ڽ��б����ѯ", "���ڴ������Ժ�...", 2000)+";document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		
		tb1.setWidth("bodyWidth");
		setToolbar(tb1);
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
	
//	 ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setId("");
			setNianfValue(null);
			setYuefValue(null);
			setZonghfxValue(null);
			getNianfModels();
			getYuefModels();
			getZonghfxModels();
			
			getLeixModels();
			setLeixModel(null);
			setLeixValue(null);
//			��ʼ������������
			getJidModels();
			setJidModel(null);
			setJidValue(null);

			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			visit.setString1(null);
			
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			
		}
		if(leixChange){
//			getYuefModels();
			setYuefModel(null);
			setNianfModel(null);
			setJidModel(null);
			setJidValue(null);
			setYuefValue(null);
			setNianfValue(null);
		}
		initToolBars();
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
    		html = "";
		}
	}
	
//	ͨ���Զ��巽��ID�;��÷����ۺϱ���ID�õ����Ӧ�ı���
	private String getBiaot(JDBCcon con,double DesignID,String JJFXZHBG_ID){
		String bt_sql=
			"SELECT Z.ZIDYFA_ID, S.BIAOT JJFXDXMK_ID\n" +
			"  FROM JJFXZHBGSZB S, JJFXDXFXSZB Z\n" + 
			" WHERE S.JJFXDXMK_ID = Z.JJFXDXMK_ID\n" + 
			"   AND Z.ZIDYFA_ID = "+DesignID+"\n" +
			"	AND S.JJFXZHBG_ID ="+JJFXZHBG_ID;
		ResultSetList bt_rs = con.getResultSetList(bt_sql);
	//	�������
		String biaot="";
		while(bt_rs.next()){
			biaot=bt_rs.getString("JJFXDXMK_ID");
		}
		bt_rs.close();
		return biaot;
	}
	
}