package com.zhiren.dc.jilgl.gongl.daoy;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ����:tzf
 * ʱ��:2009-07-15
 * ����:�ᵹ��  ��Ϣ�޸�
 */
public class Qicdyxg extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false );
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _CancelChick = false;

	public void CancelButton(IRequestCycle cycle) {
		_CancelChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if(_DeleteChick){
			_DeleteChick=false;
			delete();
			this.getSelectData();
		}
		if(_CancelChick){
			_CancelChick=false;
			huit();
			this.getSelectData();
		}
	}
	
	private void delete(){
		
		JDBCcon  con=new JDBCcon();
		
		String sql=" delete from qicdyb where  id in ("+this.getParameters()+")";
		int flag=con.getDelete(sql);
		if(flag>=0){
			this.setMsg("���ݲ����ɹ�!");
		}else{
			this.setMsg("���ݲ���ʧ��!");
		}
		con.Close();
	}

	private void huit(){
		JDBCcon  con=new JDBCcon();
		String sql=" update qicdyb set piz=0,qingcsj='' where id in("+this.getParameters()+")";
		
		int flag=con.getUpdate(sql);
		if(flag>=0){
			this.setMsg("���ݲ����ɹ�!");
		}else{
			this.setMsg("���ݲ���ʧ��!");
		}
	}
	
	private String Parameters;//��¼��ĿID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		//-----------------------------
		String jingz_sql=" select zhi  from xitxxb where mingc='�ᵹ�����������ֶν�ȡ����λ��' and leib='����' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl_jz=con.getResultSetList(jingz_sql);
		
		String baol_cu="-1";
		String jingz_cons=" (q.maoz-q.piz) as jingz,";//�����ֶ� �õ��� ���㹫ʽ
		if(rsl_jz.next()){//�ж�ϵͳ����  �ֶ� ģʽ   
			baol_cu=rsl_jz.getString("zhi");
			jingz_cons=" trunc((q.maoz-q.piz),"+baol_cu+") as jingz, ";
		}
		//--------------------------------------
		
		String shij=this.getRiq();
		String ora_str="to_date('"+shij+"','yyyy-MM-dd')";
		String sql = 
			"select q.id, q.diancxxb_id, q.cheph,q.qingcsj, m1.mingc yuanmc_id,\n" +
			"m2.mingc xiemc_id, q.maoz,q.piz,"+jingz_cons+" nvl('"+visit.getRenymc()+"','') qingcjjy,q.qingch\n" + 
			"from qicdyb q,meicb m1,meicb m2\n" + 
			"where q.yuanmc_id = m1.id(+) and q.xiemc_id = m2.id(+)\n" + 
			"and q.diancxxb_id = "+this.getTreeid()+" and to_date(to_char(q.qingcsj,'yyyy-MM-dd'),'yyyy-MM-dd')="+ora_str+" order by q.qingcsj asc";

		
//		System.out.println(sql);
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("qicdyb");
		
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("cheph").setHeader("����");
		egu.getColumn("cheph").setEditor(null);
		
		egu.getColumn("qingcsj").setHeader("�ᳵʱ��");
		egu.getColumn("qingcsj").setEditor(null);
		
		egu.getColumn("yuanmc_id").setHeader("ԭú��");
		egu.getColumn("yuanmc_id").setEditor(null);
		
		egu.getColumn("xiemc_id").setHeader("ú��");
		egu.getColumn("xiemc_id").setEditor(null);
		
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setEditor(null);
		
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setEditor(null);
		
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("jingz").setEditor(null);
		
		egu.getColumn("qingcjjy").setHeader("�ᳵԱ");
		egu.getColumn("qingcjjy").setEditor(null);
		
		egu.getColumn("qingch").setHeader("�ᳵ���");
		egu.getColumn("qingch").setEditor(null);
		
		
		egu.addTbarText("�ᳵʱ��:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());
		
		
		
		
		egu.addTbarText("-");
		egu.addTbarText("���복�ţ�");
		 TextField theKey=new TextField();
		 theKey.setId("theKey");
		 theKey.setListeners("change:function(thi,newva,oldva){ sta=new Array;}\n");
		 theKey.setListeners("specialkey:function(field,e){SearCH(field,e);}");
		 egu.addToolbarItem(theKey.getScript());
		GridButton chazhao=new GridButton("��ģ��������/������һ��","function(){SearCH(null,null);}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu.addTbarBtn(chazhao);
		egu.addTbarText("-");
		
		
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		
		GridButton cx=new GridButton("��ѯ","function(){ document.getElementById('RefurbishButton').click();}");
		cx.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(cx);
		
		egu.addTbarText("-");
		
		String str2=
			"   var recs = gridDiv_sm.getSelections(); \n"
	        +"  if(recs!=null && recs.length>0 ){  gridDiv_history='';\n"
	        +"  for(var i=0;i<recs.length;i++){ var rec=recs[i];\n"
	        +"      gridDiv_history += rec.get('ID'); if(i!=recs.length-1) gridDiv_history+=',';\n"
	        +"		}\n"  
	        +"  	document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"      "
	        +"  }else{\n"
	        +"  	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����¼!'); \n"
	        +"  	return;"
	        +"  }"
	        +"";
		
		
		
		GridButton huit=new GridButton("����","function(){ "+str2+" document.getElementById('CancelButton').click();}");
		huit.setIcon(SysConstant.Btn_Icon_Cancel);
		egu.addTbarBtn(huit);
		
		egu.addTbarText("-");
		
		GridButton del=new GridButton("ɾ��","function(){ "+str2+" document.getElementById('DeleteButton').click();}");
		del.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(del);
		
		
		String chaz="function SearCH(field,e){\n"+
		
		"   if(e!=null && e.getCharCode()!=e.ENTER){return;}\n"+
        "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('��ʾ','������ֵ');return;}\n"+
     "       var len=gridDiv_data.length;\n"+
     "       var count=1;\n"+
//     "       if(len%"+egu.getPagSize()+"!=0){\n"+
//     "        count=parseInt(len/"+egu.getPagSize()+")+1;\n"+
//     "        }else{\n"+
//     "          count=len/"+egu.getPagSize()+";\n"+
//     "        }\n"+
     "        for(var i=0;i<count;i++){\n"+
//     "           gridDiv_ds.load({params:{start:i*"+egu.getPagSize()+", limit:"+egu.getPagSize()+"}});\n"+
     "           var rec=gridDiv_ds.getRange();\n"+
     "           for(var j=0;j<rec.length;j++){\n"+
     "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"+
     "                 var nw=[rec[j]]\n"+
     "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"+
     "                      gridDiv_sm.selectRecords(nw);\n"+
     "                      sta[sta.length]=rec[j].get('ID').toString();\n"+
     "						 theKey.focus(true,true);"+
     "                       return;\n"+
     "                  }\n"+
     "                \n"+
     "               }\n"+
     "           }\n"+
     "        }\n"+
     "        if(sta.length==0){\n"+
     "          Ext.MessageBox.alert('��ʾ','��Ҫ�ҵĳ��Ų�����');\n"+
     "        }else{\n"+
     "           sta=new Array;\n"+
     "           Ext.MessageBox.alert('��ʾ','�����Ѿ�����β');\n"+
     "         }\n"+
     "      }\n";
		
		egu.addOtherScript(chaz);
		
		setExtGrid(egu);
		con.Close();
	}
	
//	�糧��
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
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
		return getTree().getWindowTreeScript();
	}
	
	
//	������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	


	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiq(DateUtil.FormatDate(new Date()));
			
			
			
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			
			getSelectData();
		}
	}
	
	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		setMsg("");
	}

}
