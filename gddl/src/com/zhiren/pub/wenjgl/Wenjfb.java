package com.zhiren.pub.wenjgl;

/*
 * ���ߣ����
 * ʱ�䣺2013-01-25
 * ������ʹ��MDAS�����е�Դ���������BUG 1.1.2.1
 */

//ȱ���ǻ�û�н����ظ���������֤����
import java.io.File;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.pub.wenjgl.Xiewj;
public class Wenjfb extends BasePage  {
	private static final int  YOUXNX=2;//Ĭ����Ч����2
	private String youxLeix="������";
	public String getYouxLeix() {
		return youxLeix;
	}
	public void setYouxLeix(String youxLeix) {
		this.youxLeix = youxLeix;
	}
	private boolean wenjfb = false;
    public void wenjfb(IRequestCycle cycle) {
    	wenjfb = true;
    }
    private boolean _DelClick = false;
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
    private boolean shuaxButton = false;
	public void shuaxButton(IRequestCycle cycle) {
		shuaxButton = true;
	}
	private boolean shangcwjButton = false;
	public void shangcwjButton(IRequestCycle cycle) {
		shangcwjButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (wenjfb) {//�ļ�����
			wenjfb = false;
    		save();
        }
		if (shuaxButton) {//�ļ��б����ļ�����
			shuaxButton = false;
			if(youxLeix.equals("������")){
				LoadData();
			}else{
				LoadData1();
			}
		}
		if (_DelClick) {
			_DelClick = false;
			if(youxLeix.equals("������")){
				DelData();
			}else{
				DelData1();
			}
			getSelectData();
		}
		if (shangcwjButton) {
			shangcwjButton = false;
			shangcwj();
			LoadData();
		}
	}
	private void shangcwj(){
		JDBCcon con=new JDBCcon();
		String sql="select * from fabwjb where wenjb_id="+getChange();
		ResultSet rs=con.getResultSet(sql);
		String cuncm="";//ʵ�ʴ洢����(���)
		try{
		if(rs.next()){
			return;
		}
		//ɾ�������ļ����������ݿ⡢�ļ�
		sql="select mingc from fujb where wenjb_id='"+getChange()+"'";
		ResultSet rs1=con.getResultSet(sql);
		
			while(rs1.next()){
				cuncm=Xiewj.home+rs1.getString(1);
				File file=new File(cuncm);
				if(file.exists()){
					file.delete();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//����ɾ���������ݿ�
		 sql="delete from fujb where wenjb_id='"+getChange()+"'";
		con.getDelete(sql);
		//ɾ�����ļ�
		sql="delete from wenjb where id="+getChange();
		con.getDelete(sql);
		con.Close();
	}
	
	private void  DelData(){
		JDBCcon con=new JDBCcon();
		String[] chang=getChange().split(",");

		if (con.getHasIt("select * from fabwjb where wenjb_id =" + chang[0])) {
			setMsg("�ļ��ѷ������޷�ɾ��!");
			return;
		}else{
			String sql=	"delete from wenjb where id=" + chang[0];
			con.getDelete(sql);
			getSelectData();
		}
		con.Close();
	}
	
	private void  DelData1(){
		JDBCcon con=new JDBCcon();
		String[] chang=getChange().split(",");
		String sql="delete from fabwjb\n" + 
		           " where fabwjb.wenjb_id="+chang[0]+
		           "   and fabwjb.shij=to_date('"+chang[1]+"','yyyy-mm-dd HH24:MI:SS')";
		con.getDelete(sql);
		getSelectData();
		con.Close();
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {//��ʾ
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			this.setRiq1(DateUtil.FormatDate(visit.getMorkssj()));
			this.setRiq2(DateUtil.FormatDate(new Date())); 
		}
		if(youxLeix.equals("������")){// �ļ��б�
			LoadData();
		}else{//�ѷ����ļ�
			LoadData1();
		}
	}
	private void save(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String records=getChange();
		String sql="";
		String[]record=records.split(";");
		String strDateTime=DateUtil.FormatDateTime(new Date());
		if(getisCheck().equals("1")){
			setisCheck("0");
			for(int i=0;i<record.length;i++){
				String[] ids=record[i].split(",");//ids[0]:wenjb_id, ids[1]:diancxxb_id 
				sql="insert into fabwjb(id,renyxxb_id,wenjb_id,diancxxb_id,shij,youxq,zhid)values(\n" +
				MainGlobal.getNewID(visit.getDiancxxb_id())+","+
				visit.getRenyID()+","+
				ids[0]+","+
				ids[1]+",to_date('"+strDateTime+"','YYYY-MM-DD HH24:MI:SS'),to_date('"+getRiq()+
				"','YYYY-MM-DD'),1)";
				con.getInsert(sql);
			}
		}else{
			for(int i=0;i<record.length;i++){
				String[] ids=record[i].split(",");//ids[0]:wenjb_id, ids[1]:diancxxb_id 
				sql="insert into fabwjb(id,renyxxb_id,wenjb_id,diancxxb_id,shij,youxq,zhid)values(\n" +
				MainGlobal.getNewID(visit.getDiancxxb_id())+","+
				visit.getRenyID()+","+
				ids[0]+","+
				ids[1]+",to_date('"+strDateTime+"','YYYY-MM-DD HH24:MI:SS'),to_date('"+getRiq()+
				"','YYYY-MM-DD'),0)";
				con.getInsert(sql);
			}
		}
		
		con.Close();
		setMsg("�ļ������ɹ����������ѷ������в�ѯ����");
	}
	private void getSelectData() {
		JDBCcon con=new JDBCcon();
		StringBuffer data=new StringBuffer();
		int i=0;
		try{
			String sql="";
			sql=
				"select w.id,r.mingc reny,to_char(min(f.shij),'YYYY-MM-DD HH24:MI:SS')shij,to_char(min(f.youxq),'YYYY-MM-DD')youxsj,w.biaot ,getJiesdws(w.id,f.shij) jiesdws\n" +
				"from fabwjb f,renyxxb r,wenjb w\n" + 
				"where f.renyxxb_id=r.id and f.wenjb_id=w.id and to_char(f.shij,'YYYY-MM-DD')>='" + getRiq1()+"'and  to_char(f.shij,'YYYY-MM-DD')<='"+ getRiq2()+
				"' group by w.id,r.mingc ,w.biaot ,f.shij";
			ResultSet rs=con.getResultSet(sql);
			data.append(" var fabData = [\n");
			while(rs.next()){
				if(i==0){
					data.append("['"+rs.getString("id")+"','"+rs.getString("reny")+"','"+rs.getString("shij")+"','"+rs.getString("biaot")+"','"+rs.getString("jiesdws")+"','"+rs.getString("youxsj")+"']\n");
				}else{
					data.append(",['"+rs.getString("id")+"','"+rs.getString("reny")+"','"+rs.getString("shij")+"','"+rs.getString("biaot")+"','"+rs.getString("jiesdws")+"','"+rs.getString("youxsj")+"']\n");
				}
				i++;
			}
			data.append("]\n");
			setGridData(data.toString());

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	private void LoadData1(){//�ѷ����ļ��б�
		getSelectData();
		setQitJs(
				"function loadPanel1()\n" +
				"\t{\n" + 
				"\t\tvar store = new Ext.data.Store({\n" + 
				"\t\t\tproxy : new Ext.zr.data.PagingMemoryProxy(fabData),\n" + 
				"\t\t\tpruneModifiedRecords:true,\n" + 
				"\t\t\treader: new Ext.data.ArrayReader({}, [\n" + 
				"\t\t\t\t\t{name:'wenjb_id'},{name:'reny'},{name:'shij'},{name:'wenjm'},{name:'jiesdw'},{name:'youxsj'}])});\n" + 
				"        var cmGrid=new Ext.grid.ColumnModel([\n" + 
				"                  new Ext.grid.RowNumberer(),\n" + 
				"                  {header: \"��Ա\", width: 100, sortable: true, resizable: true,dataIndex:'reny'},\n" + 
				"                  {header: \"ʱ��\", width: 100, sortable: true, resizable: true,dataIndex:'shij'},\n" + 
				"                  {header: \"�ļ�����\", width: 100, sortable: true, resizable: true,dataIndex:'wenjm'},\n" + 
				"\t\t\t\t  {id:'aec',header: \"���յ�λ\", width: 100, sortable: true, resizable: true,dataIndex:'jiesdw'},\n" + 
				"                  {id:'aec',header: \"��Ч����\", width: 100, sortable: true, resizable: true,dataIndex:'youxsj'}\n" + 
				"                  ]);\n" + 
				"\t    one= new Ext.grid.GridPanel({\n" + 
				"                     frame:true,\n" + 
				"       \t\t \t     store: store,\n" + 
				"                \t cm:cmGrid,\n" + 
				"\t\t\t\t\t sm:singleSeMo=new Ext.grid.RowSelectionModel({singleSelect:true}),\n" + 
				"\t\t\t        stripeRows: false,\n" + 
				"\t\t\t        autoExpandColumn: 'aec',\n" + 
				"                    tbar:[\n" + 
					"			{text:'����'},\n" + 
					"                   new Ext.form.ComboBox({\n" + 
					"                     editable:false,\n" + 
					"\t\t             triggerAction: 'all',\n" + 
					"\t\t\t\t\t value:document.getElementById('youxLeix').value,\n" + 
					"                     listeners:{select:function(combo,rec,ind){\n" + 
					"                     \tdocument.getElementById('youxLeix').value=rec.get('text');document.Form0.submit();\n" + 
					"                     }}  ,\n" + 
					"\t\t             selectOnFocus:false,\n" + 
					"                     store:[['1','������'],['2','�ѷ�����']]}),\n" + 
				"                    {text:'�ļ���������'},\n" + 
				"                     new Ext.form.DateField({format:'Y-m-d',value:document.getElementById('riq1').value,listeners:{change :function(obj,newv,oldv){document.getElementById('riq1').value=newv.dateFormat('Y-m-d');}}}),\n" + 
				"                     new Ext.form.DateField({format:'Y-m-d',value:document.getElementById('riq2').value,listeners:{change :function(obj,newv,oldv){document.getElementById('riq2').value=newv.dateFormat('Y-m-d');}}}),\n" + 
				"                     {icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,text:'ˢ��',handler : function(){ document.getElementById(\"shuax\").click(); }},\n" + 
				"                    {icon:'imgs/btnicon/delete.gif',cls:'x-btn-text-icon',minWidth:75,text:'ɾ��',handler : function(){\n" + 
				"                    \tif(singleSeMo.getCount()==0){\n" + 
				"                    \t\tExt.MessageBox.minWidth=200;\n" + 
				"\t\t             \t\tExt.MessageBox.alert('ϵͳ��ʾ��','��ѡ��Ҫɾ���Ķ���!');\n" + 
				"\t\t             \t\treturn;\n" + 
				"                   \t\t }\n" + 
				"                    \tdocument.getElementById(\"CHANGE\").value=singleSeMo.getSelected().get('wenjb_id')+','+singleSeMo.getSelected().get('shij');\n" + 
				"                        document.getElementById(\"shanc\").click();\n" + 
				"\n" + 
				"                    }}]\n" + 
				"\t\t         })\n" + 
				"          store.load();\n" + 
				"\t    return  one;\n" + 
				"\t};\n" + 
				"    var port=new Ext.Viewport({\n" + 
				"    \t\t  layout:\"fit\",\n" + 
				"           items:[\n" + 
				"              loadPanel1()\n" + 
				"           ]\n" + 
				"    });");
	}
	private void LoadData(){
		// ȡ����
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		StringBuffer date=new StringBuffer();
		int i=0;
		try{
			String sql="";
			sql=
				"select id,biaot,neir,leix,to_char(shij,'YYYY-MM-DD HH24:MI')shij,reny,'�鿴'chak,'�޸�'xiug\n" +
				"from wenjb\n" + 
				"where diancxxb_id="+visit.getDiancxxb_id()+" and to_char(shij,'YYYY-MM-DD')>='" + getRiq1()+"'and  to_char(shij,'YYYY-MM-DD')<='"+ getRiq2()+"'";
			ResultSet rs=con.getResultSet(sql);
			date.append(" var TongZ = [\n");
			while(rs.next()){
				if(i==0){
					date.append("['"+rs.getString("id")+"','"+rs.getString("biaot")+"','"+rs.getString("shij")+"','"+rs.getString("reny")+"','"+rs.getString("leix")+"','�鿴','�޸�']\n");
				}else{
					date.append(",['"+rs.getString("id")+"','"+rs.getString("biaot")+"','"+rs.getString("shij")+"','"+rs.getString("reny")+"','"+rs.getString("leix")+"','�鿴','�޸�']\n");
				}
				i++;
			}
			i=0;
			date.append("]\n");
			setGridData(date.toString());
			//
			//������
			TreeNodes tns;
			if(visit.isFencb()){
				tns=new TreeNodes("diancxxb",visit.getDiancxxb_id(),"mingc",3,true);//����0:��1���ϼ�2�������ϼ�
			}else{
				tns=new TreeNodes("diancxxb",visit.getDiancxxb_id(),"mingc",2,true);//����0:��1���ϼ�2�������ϼ�
			}
			
			setTreeData(tns.getScript());
			//��������
			String qitJs=

				"var json='';\n" +
				"\tfunction loadPanel1()//�ļ��б�\n" + 
				"\t{\n" + 
				"\t\tvar store = new Ext.data.Store({\n" + 
				"\t\t\tproxy : new Ext.zr.data.PagingMemoryProxy(TongZ),\n" + 
				"\t\t\tpruneModifiedRecords:true,\n" + 
				"\t\t\treader: new Ext.data.ArrayReader({}, [\n" + 
				"\t\t\t\t\t{name:'ID'},{name:'wenjmc'},{name:'zuijbjsj'},{name:'zuijbjr'},{name:'leix'},{name:'chak'},{name:'xiug'}])});\n" + 
				"        var cmGrid=new Ext.grid.ColumnModel([\n" + 
				"                  new Ext.grid.CheckboxSelectionModel(),\n" +

				"                  {header: '�ļ�����', width:200, sortable: true, resizable: true,dataIndex:'wenjmc'},\n" + 
				"\t\t\t\t  {header: '����༭ʱ��', width: 100, sortable: true, resizable: true,dataIndex:'zuijbjsj'},\n" + 
				"\t\t\t\t  {header: '����༭��', width: 80, sortable: true, resizable: true,dataIndex:'zuijbjr'},\n" + 
				"\t\t\t\t  //{header: '������', width: 70, sortable: true, resizable: true,dataIndex:'fujmc'},\n" + 
				"                  {header: '����', width: 50, sortable: true, resizable: true,dataIndex:'leix'},\n" + 
				"                  {header: '�鿴', renderer:function(value,p,record){ var url1 = 'http://'+document.location.host+document.location.pathname;var end1 = url1.indexOf(';');url1 = url1.substring(0,end1);url1 = url1 + '?service=page/' + 'Wenjlr&wenjb_id='+record.data['ID']+'&flag=1';return \"<a href=# onclick=window.open('\"+url1+\"','newWin','toolbar=0,resizable=0,status=1,width=600,height=430,scrollbar=1,left=\"+(window.screen.width-600)/2+\",top=\"+(window.screen.height-430)/2+\"')>�鿴</a>\"},width: 50, sortable: true, resizable: true,dataIndex:'chak'},\n" + 
				"                  {header: '�޸�',renderer:function(value,p,record){ var url1 = 'http://'+document.location.host+document.location.pathname;var end1 = url1.indexOf(';');url1 = url1.substring(0,end1);url1 = url1 + '?service=page/' + 'Wenjlr&wenjb_id='+record.data['ID']+'&flag=2';return \"<a href=# onclick=window.open('\"+url1+\"','newWin'," +
				"'toolbar=0,resizable=0,status=1,width=600,height=430,scrollbar=1,left=\"+(window.screen.width-600)/2+\",top=\"+(window.screen.height-430)/2+\"')>�޸�</a>\"}, width: 50, sortable: true, resizable: true,dataIndex:'xiug'}\n" + 
				"                  ]);\n" + 
				"\t   var one = new Ext.grid.GridPanel({\n" + 
				"                     frame:true,\n" + 
				"       \t\t \t   store: store,\n" + 
				"                   cm:cmGrid,\n" + 
				"\t\t\t\t\tsm:checkMo=new Ext.grid.CheckboxSelectionModel(),\n" + 
				"\t\t\t        stripeRows: false,\n" + 
				"\t\t\t        height:350,\n" + 
				"\t\t\t        width:600,\n" + 
				"                    //����\n" + 
				"\t\t\t        tbar:[\n" + 
				"                    {text:'����'},\n" + 
				"                   new Ext.form.ComboBox({\n" + 
				"                     editable:false,\n" + 
				"\t\t             triggerAction: 'all',\n" + 
				"\t\t\t\t\t value:document.getElementById('youxLeix').value,\n" + 
				"                     listeners:{select:function(combo,rec,ind){\n" + 
				"                     \tdocument.getElementById('youxLeix').value=rec.get('text');document.Form0.submit();\n" + 
				"                     }}  ,\n" + 
				"\t\t             selectOnFocus:false,\n" + 
				"                     store:[['1','������'],['2','�ѷ�����']]}),\n" + 
				"                    {text:'�ļ��༭����'},\n" + 
				"                    new Ext.form.DateField({format:'Y-m-d',value:document.getElementById('riq1').value,listeners:{change :function(obj,newv,oldv){document.getElementById('riq1').value=newv.dateFormat('Y-m-d');}}}),\n" + 
				"                    new Ext.form.DateField({format:'Y-m-d',value:document.getElementById('riq2').value,listeners:{change :function(obj,newv,oldv){document.getElementById('riq2').value=newv.dateFormat('Y-m-d');}}}),\n" + 
				"                    {icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,text:'ˢ��',handler:function(){document.getElementById('shuax').click();}},\n" + 
				"                   '-',\n" + 
				"                    {text: '���ļ�', handler:  function(){var url1 = 'http://'+document.location.host+document.location.pathname;var end1 = url1.indexOf(';');url1 = url1.substring(0,end1);window.open(url1+'?service=page/Wenjlr','ddd','toolbar=0,resizable=0,status=1,width=600,height=430,scrollbar=1,left='+(window.screen.width-600)/2+',top='+(window.screen.height-430)/2);}},\n" + 
				//ɾ��
				"                    {icon:'imgs/btnicon/delete.gif',cls:'x-btn-text-icon',minWidth:75,text:'ɾ��',handler : function(){\n" + 
				"                    \tif(checkMo.getCount()!=1){\n" + 
				"                    \t\tExt.MessageBox.minWidth=200;\n" + 
				"\t\t             \t\tExt.MessageBox.alert('ϵͳ��ʾ��','��ѡ��һ��Ҫɾ���Ķ���!');\n" + 
				"\t\t             \t\treturn;\n" + 
				"                   \t\t }\n" + 
				"                    \tdocument.getElementById(\"CHANGE\").value=checkMo.getSelected().get('ID');\n" + 
				"                        document.getElementById(\"shanc\").click();\n" + 
				"\n" + 
				"                    }}," +
				
				"                    {text: '����', handler:  function(){diancTree_window.show()}}\n" + 
				"                   ]\n" + 
				"                   //\n" + 
				"\t\t})\n" + 
				"\t\tstore.load({params:{start:0, limit:25}});\n" + 
				"\t    return  one;\n" + 
				"\t};\n" + 
				"\tfunction loadPanel2()//���ն����б�\n" + 
				"\t{\n" + 
				"\t     panelTree=new Ext.tree.TreePanel({\n" + 
				"\t      height:362,\n" + 
				"\t\t  animate:true,\n" + 
				"\t\t  line:true,\n" + 
				"\t\t  rootVisible:true,\n" + 
				"\t\t  autoScroll :true\n" + 
				"\t     });\n" + 
				"\t\t node0.expanded = true;\n" + 
				"\t     panelTree.setRootNode(node0);\n" + 
				"\t\t return  panelTree;\n" + 
				"\t};\n" + 
				"    var diancTree_window =new Ext.Window({//��\n" + 
				"    header :false,\n" + 
				"    footer :true,\n" + 
				"\twidth:300,\n" + 
				"\theight:400,\n" + 
				"\tcloseAction:'hide',\n" + 
				"\tmodal:true,\n" + 
				"\titems:[loadPanel2(),\n" + 
				"\t\tnew Ext.Toolbar({\n" + 
				"\t\t\tlayout:'column',\n" + 
				"\t\t\titems:[\n" + 
				"\t\t\t\tnew Ext.form.Checkbox({boxLabel:'��Ҫ֪ͨ',checked:false,listeners:{'check':function(){document.getElementById('isCheck').value=1}}}),\n" + 
				"\t\t\t\tnew Ext.form.Label({html:'&nbsp&nbsp&nbsp��Ч������:'}),\n" + 
				"\t\t\t\t yxrq=new Ext.form.DateField({\n" + 
				"\t\t\t\tformat:'Y-m-d',value:'"+DateUtil.FormatDate(DateUtil.AddDate(new Date(), YOUXNX, DateUtil.AddType_intYear))+"'\n" + 
				"\t\t\t\t}),\n" + 
				"\t\t\t\tnew Ext.Toolbar.Button({\n" + 
				"\t\t\t\t\ttext:'ȷ��',\n" + 
				"\t\t\t\t\thandler:" +

				"function(){\n" +
				"\t                 var  arry1=panelTree.getChecked('id');//�糧\n" + 
				"\t                 var  arry2=checkMo.getSelections();//�ļ�checkMo.getSelections()[0].get('ID');\n" + 
				" \t\t\t\t         if(arry1.length==0){\n" + 
				"\t\t              \tExt.MessageBox.minWidth=200;\n" + 
				"\t\t               \tExt.MessageBox.alert('ϵͳ��ʾ��','��ѡ��Ҫ���յĵ�λ!');\n" + 
				"\t\t             \t   return;\n" + 
				"\t\t              }\n" + 
				"\t\t              if(arry2.length==0){\n" + 
				"\t\t              \tExt.MessageBox.minWidth=200;\n" + 
				"\t\t               \tExt.MessageBox.alert('ϵͳ��ʾ��','��ѡ��Ҫ�������ļ�!');\n" + 
				"\t\t              \treturn;\n" + 
				"\t\t              }\n" + 
				"\t              \t\t//���챾�ض���\n" + 
				"\t              \t\tjson=\"\";\n" + 
				"\t              \t\tfor(var i=0;i<arry1.length;i++){//�糧\n" + 
				"\t              \t\t\tfor(var j=0;j<arry2.length;j++){//�ļ�\n" + 
				"  \t              \t\t\tif(!(i==0&&j==0)){//������ǵ�һ��\n" + 
				"  \t              \t\t\t json+=';';\n" + 
				"  \t              \t\t\t}\n" + 
				"  \t              \t\t\tjson+=arry2[j].get('ID');\n" + 
				"  \t              \t\t\tjson+=',';\n" + 
				"  \t              \t\t\tjson+=arry1[i];\n" + 
				"\t              \t\t\t}\n" + 
				"\t              \t\t}\n" + 
				"                    document.getElementById(\"CHANGE\").value=json;document.getElementById('youxrq').value=yxrq.value;\n" + 
				"\t              \t\tdocument.getElementById(\"wenjfb\").click();\n" + 
				"\t               }"+

				"\t\t\t\t})\n" + 
				"\t\t\t]\n" + 
				"\t\t})\n" + 
				"    ]});\n" + 
				"    var port=new Ext.Viewport({//body\n" + 
				"\t   layout:'fit',\n" + 
				"       items:[\n" + 
				"          loadPanel1()\n" + 
				"       ]\n" + 
				"    });";
			setQitJs(qitJs);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	private String riq;
	private String isCheck;
	private String GridData;
	public String getGridData(){
		return GridData;
	}
	public void setGridData(String value){
		this.GridData=value;
	}
	private String TreeData;
	public String getTreeData(){
		return TreeData;
	}
	public void setTreeData(String value){
		this.TreeData=value;
	}
	private String QitJs;
	public String getQitJs(){
		return QitJs;
	}
	public void setQitJs(String value){
		this.QitJs=value;
	}
	public String getRiq() {
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	//�ж��Ƿ�Ϊ��Ҫ֪ͨ
	public String getisCheck() {
		if(isCheck==null||isCheck.equals("")){
			isCheck="0";
		}
		return isCheck;
	}
	public void setisCheck(String isCheck) {
		this.isCheck = isCheck;
	}
//  ҳ��仯��¼
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
    private String riq2;
	public String getRiq2() {
		if(riq2==null||riq2.equals("")){
			riq2=DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}
	
	private String riq1;
	public String getRiq1() {
		if(riq1==null||riq1.equals("")){
			riq1=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -15, DateUtil.AddType_intDay));
		}
		return riq1;
	}
	public void setRiq1(String riq1) {
		this.riq1 = riq1;
	}
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
}