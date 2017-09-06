package com.zhiren.jingjfx;

import java.io.IOException;
import java.sql.SQLException;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.zidy.Aotcr;
import com.zhiren.zidy.ZidyCss;
import com.zhiren.zidy.ZidyDataSource;
import com.zhiren.zidy.ZidyParam;
/*
 * �޸��ˣ���衻�
 * �޸�ʱ�䣺2010��11��8��
 * �޸����ݣ� �ڹ����� ��ӵ糧�����糧����������ԴSQL��ĵ糧��ȡֵ��Χ.
 * 
 */
/*
 * �޸��ˣ���衻�
 * �޸�ʱ�䣺2010��12��10��
 * �޸����ݣ� �����¶ȡ�����ѡ���������Դ��������ʼ���ڡ��������ڡ�
 * 
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-06-16
 * ���÷�Χ���������ȼ����Ϣϵͳ
 * ������JJFXDXFXSJB���������ֶ�DIANCXXB_ID
 * 		��JJFXDXFXSJB��jjfxdxfxszb�е����ݷֿ���ѯ�����ڲ�ѯJJFXDXFXSJB��ʱ���ӵ糧��Ϣ��ʶ�����ơ�
 * 		���ڱ���ʱ����DIANCXXB_IDΪ��ѡ�糧������Ӧ�ĵ糧��ʶ
 */
public class Danxfx extends BasePage implements PageValidateListener{
	
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
	
	public String getGongs() {
		return ((Visit)this.getPage().getVisit()).getString2();
	}
	public void setGongs(String gongs) {
		((Visit)this.getPage().getVisit()).setString2(gongs);
	}
	
	public boolean getRaw() {
		return true;
	}
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	public Aotcr getAotcr() {
		return ((Visit)this.getPage().getVisit()).getAotcr();
	}
	public void setAotcr(Aotcr aotcr) {
		((Visit)this.getPage().getVisit()).setAotcr(aotcr);
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

	

//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	/*public int getDiancTreeJib() {
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
	}*/
	/*private String getDiancxxbIds(JDBCcon con){
		int jib=getDiancTreeJib();
		StringBuffer diancs=new StringBuffer();
		String sql="";
		if (jib==1) {
			sql="select id from vwdianc v where v.rlgsid="+getTreeid();
		}else if(jib==2){
			sql="select id from vwdianc v where v.fgsid="+getTreeid();
		}else if(jib==3){
			sql="select * from vwdianc v where v.id="+getTreeid()+" or v.fuid="+getTreeid();
		}
		ResultSetList list=con.getResultSetList(sql);
		diancs.append("(");
		while(list.next()){
			diancs.append(list.getInt("id")+",");
		}
		String rs=diancs.substring(0, diancs.length()-1);
		rs+=")";
		return rs;
	}*/
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
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj).getId()) {
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
    	if(getLeixValue().getValue().equals("����")){
    		setYuefModel(new IDropDownModel("select mlabel,mvalue from yuefb where mvalue<5"));
    	}else{
    		setYuefModel(new IDropDownModel("select mlabel,mvalue from yuefb "));
        }
    }
//����������
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
//	�������������
	public IDropDownBean getDanxfxValue() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean3() == null) {
            if(getDanxfxModel().getOptionCount() > 0) {
	        	Object obj = getDanxfxModel().getOption(0);
	            setDanxfxValue((IDropDownBean) obj);
            }
	    }
	    return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setDanxfxValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(Value);
	}
	
	public IPropertySelectionModel getDanxfxModel() {
	    if (((Visit)this.getPage().getVisit()).getProSelectionModel3() == null) {
	        getDanxfxModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setDanxfxModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(_value);
    }

    public void getDanxfxModels() {
        setDanxfxModel(new IDropDownModel(" select ID,MINGC from JJFXDXMK "));
    }
    
	private boolean _RefurbishClick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
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
	
	private void Refurbish() {
		formatDate();
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strOracleDate = getLeixValue().getValue().equals("�¶�")?"to_date('"+getNianfValue().getId()+"-"+getYuefValue().getId()+"-01','yyyy-mm-dd')":"to_date('"+getNianfValue().getId()+"-"+getYuefValue().getId()+"-02','yyyy-mm-dd')";
		StringBuffer sb = new StringBuffer();
		sb.append("select z.id zid,z.jjfxdxmk_id,z.gongsb_id,z.zidyfa_id from jjfxdxfxszb z\n")
		.append("where jjfxdxmk_id =").append(getDanxfxValue().getId());
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()) {
			if(rsl.getLong("zidyfa_id") != 0) {
				setAotcr(new Aotcr(rsl.getString("zidyfa_id")));
				getAotcr().setIPage(this);
				ZidyParam zdc = new ZidyParam();
				zdc.setId(null);
				zdc.setName("�糧ID");
				zdc.setSvalue(getTreeid());
				getAotcr().getParaMeters().add(zdc);
				
				List plist = getAotcr().getParaMeters();
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
				getAotcr().setData();
			}
			sb.delete(0, sb.length());
			String zid=rsl.getString("zid");
			sb.append("SELECT J.ID JID, RIQ, WENZFX, J.DIANCXXB_ID\n" );
			sb.append("  FROM JJFXDXFXSJB J\n" );
			sb.append(" WHERE J.JJFXDXFXSZB_ID = "+zid+"\n" );
			sb.append("   AND J.DIANCXXB_ID = "+getTreeid()+"\n" );
			sb.append("   AND J.RIQ = "+strOracleDate);
			rsl=con.getResultSetList(sb.toString());
			if(rsl.next()){
				setId(rsl.getString("jid"));
				setGongs(rsl.getString("wenzfx"));
			}else {
				sb.delete(0, sb.length());
				String id = MainGlobal.getNewID(visit.getDiancxxb_id());
				sb.append("insert into jjfxdxfxsjb(id,jjfxdxfxszb_id,riq,wenzfx,diancxxb_id) values(")
				.append(id).append(",").append(zid).append(",").append(strOracleDate)
				.append(",'',"+getTreeid()+")");
				con.getInsert(sb.toString());
				setId(id);
				setGongs("");
			}
		}
		con.Close();
	}
	
	private boolean _CreateChick = false;
    public void CreateButton(IRequestCycle cycle) {
    	_CreateChick = true;
    }
    
    private void Create() {
    	setGongs("");
    	String gongs = "";
    	Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strOracleDate = getLeixValue().getValue().equals("�¶�")?"to_date('"+getNianfValue().getId()+"-"+getYuefValue().getId()+"-01','yyyy-mm-dd')":"to_date('"+getNianfValue().getId()+"-"+getYuefValue().getId()+"-02','yyyy-mm-dd')";
		StringBuffer sb = new StringBuffer();
		sb.append("select z.id zid,z.jjfxdxmk_id,z.gongsb_id,z.zidyfa_id,j.id jid,riq,wenzfx from jjfxdxfxszb z,jjfxdxfxsjb j \n")
		.append("where z.id = j.jjfxdxfxszb_id(+) and j.riq(+) = ").append(strOracleDate).append(" \n")
//		.append(" and diancxxb_id =").append(visit.getDiancxxb_id()).append(" \n")
		.append(" and jjfxdxmk_id =").append(getDanxfxValue().getId());
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()) {
			DataBassUtil dbu = new DataBassUtil();
			try {
				gongs = dbu.getClob("Gongsb", "gongs", rsl.getLong("gongsb_id"));
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			} catch (IOException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
		}
    	List data = getAotcr().getDataSources();
    	String wenz = "";
    	for(int i = 0; i<  data.size() ; i++) {
    		ZidyDataSource zds = (ZidyDataSource)data.get(i);
    		rsl = zds.getDataSource();
    		rsl.beforefirst();
    		while(rsl.next()) {
    			try {
    				Interpreter bsh = new Interpreter();
	    			for(int j=0; j< rsl.getColumnNames().length ; j++) {
						bsh.set(rsl.getColumnNames()[j], rsl.getString(rsl.getColumnNames()[j]));
	    			}
	    			bsh.eval(gongs);
	    			wenz = getGongs() + String.valueOf(bsh.get("���ַ���"));
    			} catch (EvalError e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
    			setGongs(wenz);
    		}
    	}
    }

	private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    private void Save() {
    	if(!"".equals(getId()) ){
    		JDBCcon con = new JDBCcon();
        	StringBuffer sb = new StringBuffer();
        	sb.append("update jjfxdxfxsjb set wenzfx='")
        	.append(getGongs()).append("' where id =").append(getId());
        	con.getUpdate(sb.toString());
        	con.Close();
    	}
    }
	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			Refurbish();
		}
		if (_CreateChick) {
			_CreateChick = false;
			Create();
		}
		if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
	}

	public String getPrintTable() {
		if(getAotcr() == null) {
			return "�����������ȷ";
		}
		return getAotcr().getHtml();
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
		
		tb1.addField(cbn);
		if(getLeixValue().getValue().equals("�¶�")){
			tb1.addText(new ToolbarText("�·�:"));
		}else{
			tb1.addText(new ToolbarText("����:"));
		}
		
		ComboBox cby = new ComboBox();
		cby.setWidth(60);
		cby.setTransform("YuefDropDown");
		cby.setId("YuefDropDown");//���Զ�ˢ�°�
		cby.setLazyRender(true);//��̬��
		cby.setEditable(false);
		
		tb1.addField(cby);
		
		tb1.addText(new ToolbarText("ģ��:"));
		ComboBox cbm = new ComboBox();
		cbm.setWidth(200);
		cbm.setTransform("DanxfxDropDown");
		cbm.setId("DanxfxDropDown");//���Զ�ˢ�°�
		cbm.setLazyRender(true);//��̬��
		cbm.setEditable(false);
		
		tb1.addField(cbm);
		
		ToolbarButton rbtn = new ToolbarButton(null,"ˢ��","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(rbtn);
		
		ToolbarButton cbtn = new ToolbarButton(null,"����","function(){document.getElementById('CreateButton').click();}");
		cbtn.setIcon(SysConstant.Btn_Icon_Create);
		tb1.addItem(cbtn);
		
		ToolbarButton sbtn = new ToolbarButton(null,"����","function(){Ext.getDom('Gongs').value=extGS.getValue();document.getElementById('SaveButton').click();}");
		sbtn.setIcon(SysConstant.Btn_Icon_Save);
		tb1.addItem(sbtn);
		
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
	
	public String getAotcrCss() {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<getAotcr().getCss().size();i++) {
			sb.append(((ZidyCss)getAotcr().getCss().get(i)).getText()).append("\n");
		}
		return sb.toString();
	}
	
//	 ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setId("");
			setGongs("");
			setNianfValue(null);
			setYuefValue(null);
			setDanxfxValue(null);
			getNianfModels();
			getYuefModels();
			getDanxfxModels();
			
			setLeixModel(null);
			setLeixValue(null);
			
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			visit.setString1(null);

			setTreeid(String.valueOf(visit.getDiancxxb_id()));
		}
		if(leixChange){
			getYuefModels();
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
		}
	}
	
}