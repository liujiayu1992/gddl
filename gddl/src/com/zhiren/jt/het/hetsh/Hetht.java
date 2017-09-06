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
	import com.zhiren.common.MainGlobal;
	import com.zhiren.common.ResultSetList;
	import com.zhiren.common.ext.ExtGridUtil;
	import com.zhiren.common.ext.ExtTreeUtil;
	import com.zhiren.common.ext.GridButton;
	import com.zhiren.common.ext.form.ComboBox;
	
/*
 * ���ߣ����
 * ʱ�䣺2013-3-20
 * ������ˢ����������ͬ�������ع��ܣ����ʹ�ò���MainGlobal.getXitxx_item("��ͬ", "�Ƿ�鿴���Ӱ��ͬ����", "0", "��")
 * 		���ú�ͬ�и������Զ�����������ݣ�������ʾϵͳ��ͬ���ݡ�
 */
	
import com.zhiren.main.Visit;
	public class Hetht extends BasePage {
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
	private int _editTableRow = -1;// �༭����ѡ�е���
	public int getEditTableRow() {
		return _editTableRow;
	}
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
	public void submit(IRequestCycle cycle) {
		if (TijButton) {
			TijButton = false;
			tij();
			getSelectData();
		}
		if (HuitButton) {
			HuitButton = false;
			huit();
			getSelectData();
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
	public void getSelectData() {
		List list=getEditValues();
		String sql="";
		list.clear();
		JDBCcon con=new JDBCcon();
//		����ļ�����·��
		String Imagelj=MainGlobal.getXitxx_item("��ͬ", "��ͬ����·��", "0", "D:\\\\zhiren\\\\het");
		Imagelj=Imagelj + "\\\\" + this.getTreeid() + "\\\\";
		Imagelj=Imagelj.replaceAll("\\\\", "\\\\\\\\");
		String chak="";
		if(MainGlobal.getXitxx_item("��ͬ", "�Ƿ�鿴���Ӱ��ͬ����", "0", "��").equals("��")){
			chak="decode(GETHETFILENAME(hetb.id),null,null,'<a href=# onclick=window.open(''"+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"&filename='||GETHETFILENAME(hetb.id)||''')>�鿴����</a>') as chak \n";
		}else{
			chak="nvl('�鿴','') chak ";
		}
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),this.getTreeid());
		((Visit) getPage().getVisit()).setExtTree1(etu);
		 sql=
				"select id, hetbh,gongfdwmc,xufdwmc,qiandrq,\n" +
				"leib,hetl,chak from " +
				"(select hetb.id,hetbh,gongfdwmc,xufdwmc,qiandrq,\n" +
				"decode(hetb.leib,0,'�糧��ͬ','�����ͬ')leib,sum(hetslb.hetl)hetl,\n" +
				chak+
				"from hetb,hetslb\n" + 
				"where hetslb.hetb_id=hetb.id \n" + 
				"\n" + 
				"and hetb.liucztb_id=" +getweizSelectValue().getId() //�ҵ�����
				+ 
				"and to_char(hetb.qiandrq,'YYYY')="+getNianfValue().getId()+"\n" + 
				"and hetb.diancxxb_id in (select id\n" + 
				" from(\n" + 
				" select id from diancxxb\n" + 
				" start with fuid="+getTreeid()+"\n" + 
				" connect by fuid=prior id\n" + 
				" )\n" + 
				" union\n" + 
				" select id\n" + 
				" from diancxxb\n" + 
				" where id="+getTreeid()+")\n" + 
				"group by  hetb.id,hetbh,gongfdwmc,xufdwmc,qiandrq,leib)";
	 			 ResultSetList rs=con.getResultSetList(sql);
				 ExtGridUtil egu = new ExtGridUtil("SelectFrmDiv", rs);
					egu.getColumn("id").setHidden(true);
					egu.getColumn("hetbh").setHeader("��ͬ���");
					egu.getColumn("hetbh").setWidth(150);
					egu.getColumn("gongfdwmc").setHeader("������λ����");
					egu.getColumn("gongfdwmc").setWidth(200);
					egu.getColumn("xufdwmc").setHeader("�跽��λ����");
					egu.getColumn("xufdwmc").setWidth(200);
					egu.getColumn("qiandrq").setHeader("ǩ������");
					egu.getColumn("leib").setHeader("���");
					egu.getColumn("hetl").setHeader("��ͬ��");
					egu.getColumn("chak").setHeader("����");	
					String str=
		       		" var url = 'http://'+document.location.host+document.location.pathname;"+
		            "var end = url.indexOf(';');"+
					"url = url.substring(0,end);"+
		       	    "url = url + '?service=page/' + 'Shenhrz&hetb_id='+record.data['ID'];";
					
					if(MainGlobal.getXitxx_item("��ͬ", "�Ƿ�鿴���Ӱ��ͬ����", "0", "��").equals("��")){
						egu.getColumn("chak").setRenderer(
								"function(value,p,record){" +str+
								"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>�鿴</a>\"}"
						);
					}
					//
					egu.addTbarText("ǩ�����:");
					//egu.addToolbarItem("cbo_NianfDropDown");
					ComboBox comb1=new ComboBox();
					comb1.setId("nianf");
					comb1.setWidth(100);
					comb1.setTransform("NianfDropDown");
					comb1.setLazyRender(true);//��̬��
					
					egu.addToolbarItem(comb1.getScript());
					//
					egu.addTbarText("״̬:");
					ComboBox comb2=new ComboBox();
					comb2.setId("weiz");
					comb2.setWidth(100);
					comb2.setTransform("weizSelect");
					comb2.setLazyRender(true);//��̬��weizSelect
					egu.addToolbarItem(comb2.getScript());
					egu.addOtherScript("nianf.on('select',function(){document.forms[0].submit();});weiz.on('select',function(){document.forms[0].submit()});");
					egu.addOtherScript("SelectFrmDiv_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");
					egu.addTbarTreeBtn("diancTree");
					egu.addToolbarItem("{"+new GridButton("�ύ","function(){document.getElementById('tijButton').click();}").getScript()+"}");
					egu.addToolbarItem("{"+new GridButton("����","function(){document.getElementById('huitButton').click();}").getScript()+"}");
					egu.pagsize=0;
					((Visit) this.getPage().getVisit()).setExtGrid1(egu);
		}
	private void tij(){
		if(getEditTableRow()!=-1){
			//����liucztb_id
			String sql="";
			JDBCcon con=new JDBCcon();
			sql="update hetb\n" +
			"set  hetb.liucztb_id=1\n" + 
			"where hetb.id="+((Visit) getPage().getVisit()).getExtGrid1().griddata[getEditTableRow()][0];
			con.getUpdate(sql);
			
		}
	}
	private void huit(){
		if(getEditTableRow()!=-1){
//			����liucztb_id
			String sql="";
			JDBCcon con=new JDBCcon();
			sql="update hetb\n" +
			"set  hetb.liucztb_id=0\n" + 
			"where hetb.id="+((Visit) getPage().getVisit()).getExtGrid1().griddata[getEditTableRow()][0];
			con.getUpdate(sql);
		}
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setDropDownBean1(null);
			((Visit) getPage().getVisit()).setProSelectionModel1(null);
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setDropDownBean3(null);
			((Visit) getPage().getVisit()).setProSelectionModel3(null);
			((Visit) getPage().getVisit()).setString2("");
			visit.setboolean4(true);
			setTreeid(null);
			
		}
		getSelectData();
	}
	//��λ
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
    //λ��
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
        list.add(new IDropDownBean(0,"����"));
        list.add(new IDropDownBean(1,"����"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
    //���
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
//    private IDropDownBean getIDropDownBean(IPropertySelectionModel model,long id) {
//        int OprionCount;
//        OprionCount = model.getOptionCount();
//        for (int i = 0; i < OprionCount; i++) {
//            if (((IDropDownBean) model.getOption(i)).getId() == id) {
//                return (IDropDownBean) model.getOption(i);
//            }
//        }
//        return null;
//    }
    //ext����
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
}
