package com.zhiren.pub.kuaijtdsz;

//缺陷是还没有进行重复发布的验证机制
import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
public class Kuaijtdsz extends BasePage  {
	private boolean saveClick = false;
    public void wenjfb(IRequestCycle cycle) {
    	saveClick = true;
    }
    private boolean _DelClick = false;
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (saveClick) {
			saveClick = false;
    		save();
        }
			loadDate();
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
	}
	private void  DelData(){
		JDBCcon con=new JDBCcon();
		String sql=
		"delete\n" +
		"from quickstart\n" + 
		"where id ="+getChange();
		con.getDelete(sql);
		loadDate();
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {//显示
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			
		}
			loadDate();
	}
	private void save(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String records=getChange();
		try{
			//chh 2008-09-26 加入人员条件判断
			String Selsql="select ziyxxb_id from quickstart where ziyxxb_id = "+records +" and renyxxb_id="+visit.getRenyID();
			ResultSet rs=con.getResultSet(Selsql);
			if(rs.next()){
				
			}
			else{
				String Inssql="";
				Inssql="insert into quickstart(id,xuh,renyxxb_id,ziyxxb_id)values(\n" +
				MainGlobal.getNewID(visit.getDiancxxb_id())+",1,"+
				visit.getRenyID()+","+
				records+")";
				con.getInsert(Inssql);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}

		con.Close();
		loadDate();
	}
	
	
	
	private void loadDate(){
		// 取数据
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
 		StringBuffer date=new StringBuffer();
		int i=0;
		try{
			String sql="";
			// chh 2008-09-26 去掉级别的条件
			sql=
//				"select distinct(q.id),q.xuh,renyxxb_id,z.mingc as ziyxxb_id from quickstart q,ziyxxb z,\n" +
//				"(select zy.mingc mingc\n" + 
//				"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id " +
////				"and z.id = "+visit.getRenyID()+
//				")order by zy.jib,zy.xuh) a\n" + 
//				" where z.id = q.ziyxxb_id\n" + 
//				"and renyxxb_id = " + visit.getRenyID()+
//				"and z.mingc = a.mingc\n" + 
//				"order by q.id";

			"select distinct(q.id),q.xuh,renyxxb_id,z.mingc as ziyxxb_id from quickstart q,ziyxxb z \n" +
			" where z.id = q.ziyxxb_id\n" + 
			" and renyxxb_id = " + visit.getRenyID()+
			" order by q.id";
			
			ResultSet rs=con.getResultSet(sql);
			date.append(" var TongZ = [\n");
			while(rs.next()){
				if(i==0){
					date.append("['"+rs.getString("id")+"','"+rs.getString("ziyxxb_id")+"']");
				}else{
					date.append(",['"+rs.getString("id")+"','"+rs.getString("ziyxxb_id")+"']");
				}
				i++;
			}
			i=0;
			date.append("]\n");
			setGridData(date.toString());
			//
			//树数据
			
//			TreeNodes tns=new TreeNodes("ziyxxb",0,"mingc",2,false,""+visit.getRenyID());//类型0:树1：上级2：树和上级
			setTreeData(getTreeData());
			//其他数据
			String qitJs=

				"var json='';\n" +
				"\tfunction loadPanel1()//文件列表\n" + 
				"\t{\n" + 
				"\t\tvar store = new Ext.data.Store({\n" + 
				"\t\t\tproxy : new Ext.zr.data.PagingMemoryProxy(TongZ),\n" + 
				"\t\t\tpruneModifiedRecords:true,\n" + 
				"\t\t\treader: new Ext.data.ArrayReader({}, [\n" + 
				"\t\t\t\t\t{name:'ID'},{name:'kuaijzy'}])});\n" + 
				"        var cmGrid=new Ext.grid.ColumnModel([\n" + 
//				"                  new Ext.grid.CheckboxSelectionModel(),\n" + 
				"\t\t\t\t  new Ext.grid.RowNumberer(),{header: '快捷资源', width: 100, sortable: true, resizable: true,dataIndex:'kuaijzy'}\n" + 
				"                  ]);\n" + 
				"\t   var one = new Ext.grid.GridPanel({\n" + 
				"                     frame:true,\n" + 
				"       \t\t \t   store: store,\n" + 
				"                   cm:cmGrid,\n" + 
				"\t\t\t\t\t sm:singleSeMo=new Ext.grid.RowSelectionModel({singleSelect:true}),\n" + 
				"\t\t\t        stripeRows: false,\n" + 
				"\t\t\t        height:350,\n" + 
				"\t\t\t        width:600,\n" + 
				"                    //变量\n" + 
				"\t\t\t        tbar:[\n" + 
				"                    {text: '增加快捷资源', handler:  function(){diancTree_window.show()}},\n" +
				"                   '-',\n" + 
				"                    {icon:'imgs/btnicon/delete.gif',cls:'x-btn-text-icon',minWidth:75,text:'删除',handler : function(){\n" + 
				"                    \tif(singleSeMo.getCount()==0){\n" + 
				"                    \t\tExt.MessageBox.minWidth=200;\n" + 
				"\t\t             \t\tExt.MessageBox.alert('系统提示：','请选择要删除的对象!');\n" + 
				"\t\t             \t\treturn;\n" + 
				"                   \t\t }\n" + 
				"                    \tdocument.getElementById(\"CHANGE\").value=singleSeMo.getSelected().get('ID');\n" + 
				"                        document.getElementById(\"shanc\").click();\n" + 
				"                    }}]\n" + 
				"                   //\n" + 
				"\t\t})\n" + 
				"\t\tstore.load({params:{start:0, limit:25}});\n" + 
				"\t    return  one;\n" + 
				"\t};\n" + 
				"\tfunction loadPanel2()//接收对象列表\n" + 
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
				"    var diancTree_window =new Ext.Window({//树\n" + 
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
				"\t\t\t\tnew Ext.Toolbar.Button({\n" + 
				"\t\t\t\t\ttext:'确定',\n" + 
				"\t\t\t\t\thandler:" +
				"function(){\n" +
				"\t                 var  arry1=panelTree.getSelectionModel().getSelectedNode();//电厂\n" +
				" \t\t\t\t         if(arry1==('')){\n" + 
				"\t\t              \tExt.MessageBox.minWidth=200;\n" + 
				"\t\t               \tExt.MessageBox.alert('系统提示：','请选择要增加的资源');\n" + 
				"\t\t             \t   return;\n" + 
				"\t\t              }\n" + 
				"\t              \t\t//构造本地对象\n" + 
				"\t              \t\tjson=\"\";\n" + 
				"\t              \t\tfor(var i=0;i<arry1.length;i++){//电厂\n" + 
				"  \t              \t\t\tif(!(i==0)){//如果不是第一次\n" + 
				"  \t              \t\t\t json+=';';\n" + 
				"  \t              \t\t\t}\n" + 
				"  \t              \t\t\tjson+=',';\n" + 
				"  \t              \t\t\tjson+=arry1[i];\n" + 
				"\t              \t\t}\n" + 
				"                    document.getElementById(\"CHANGE\").value=arry1.id;\n" + 
				"\t              \t\tdocument.getElementById(\"wenjfb\").click();\n" + 
				"\t\t\t\t}})\n" + 
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
	
	private String GridData;
	public String getGridData(){
		return GridData;
	}
	public void setGridData(String value){
		this.GridData=value;
	}
	private String TreeData;
    public String getTreeData() {
    	Visit visit = (Visit) getPage().getVisit();
    	/*String sql = 

    		"select c.id as id,c.mingc as mingc,c.fuid as fuid,c.childs as childs,c.checked as checked from\n" +
    		"(select zy.id,mingc,zy.fuid,\n" + 
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) checked\n" + 
    		"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id and z.id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" ))\n" + 
    		"and nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) <>0\n" + 
    		"order by zy.jib,zy.xuh )a,\n" + 
    		"(select zy.id,mingc,zy.fuid,\n" + 
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) checked\n" + 
    		"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id and z.id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" ))\n" + 
    		"and nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) <>0\n" + 
    		"order by zy.jib,zy.xuh )b,\n" + 
    		"(select zy.id,mingc,zy.fuid,\n" + 
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) checked\n" + 
    		"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id and z.id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" ))\n" + 
    		"and nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) <>0\n" + 
    		"order by zy.jib,zy.xuh )c\n" + 
    		"where a.id = b.fuid and b.id = c.fuid\n" + 
    		"union\n" + 
    		"select b.id,b.mingc,b.fuid,b.childs,b.checked from\n" + 
    		"(select zy.id,mingc,zy.fuid,\n" + 
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) checked\n" + 
    		"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id and z.id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" ))\n" + 
    		"and nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) <>0\n" + 
    		"order by zy.jib,zy.xuh )a,\n" + 
    		"(select zy.id,mingc,zy.fuid,\n" + 
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) checked\n" + 
    		"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id and z.id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" ))\n" + 
    		"and nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) <>0\n" + 
    		"order by zy.jib,zy.xuh )b,\n" + 
    		"(select zy.id,mingc,zy.fuid,\n" + 
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) checked\n" + 
    		"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id and z.id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" ))\n" + 
    		"and nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) <>0\n" + 
    		"order by zy.jib,zy.xuh )c\n" + 
    		"where a.id = b.fuid and b.id = c.fuid\n" + 
    		"union\n" + 
    		"select a.id,a.mingc,a.fuid,a.childs,a.checked from\n" + 
    		"(select zy.id,mingc,zy.fuid,\n" + 
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) checked\n" + 
    		"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id and z.id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" ))\n" + 
    		"and nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) <>0\n" + 
    		"order by zy.jib,zy.xuh )a,\n" + 
    		"(select zy.id,mingc,zy.fuid,\n" + 
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) checked\n" + 
    		"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id and z.id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" ))\n" + 
    		"and nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) <>0\n" + 
    		"order by zy.jib,zy.xuh )b,\n" + 
    		"(select zy.id,mingc,zy.fuid,\n" + 
    		"(select count(z.id) from ziyxxb z where z.fuid = zy.id) childs,\n" + 
    		"nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) checked\n" + 
    		"from ziyxxb zy  where zy.leib = (select nvl(max(d.jib),1) jib from zuxxb z,diancxxb d where z.diancxxb_id = d.id and z.id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" ))\n" + 
    		"and nvl((select id from zuqxb where zuqxb.ziyxxb_id = zy.id and zuqxb.zuxxb_id = (select distinct z.id from zuqxb zq,ziyxxb zy,diancxxb dc,zuxxb z\n" + 
    		"where zq.ziyxxb_id = zy.id\n" + 
    		"and zq.zuxxb_id=z.id\n" + 
    		"and z.mingc = (select getrenyfz(id) zu from renyxxb where id = "+visit.getRenyID()+" and diancxxb_id = "+visit.getDiancxxb_id()+")\n" + 
    		"and dc.id ="+visit.getDiancxxb_id()+" )),0) <>0\n" + 
    		"order by zy.jib,zy.xuh )c\n" + 
    		"where a.id = b.fuid and b.id = c.fuid order by fuid";
*/
    	String sql = 

    		"select z.id,z.mingc,z.fuid,(select count(z.id) from ziyxxb where fuid = z.id) childs,\n" +
    		"0 checked\n" + 
    		"  from ziyxxb z\n" + 
    		"where z.id in (\n" + 
    		"select distinct ziyxxb_id from zuqxb where zuxxb_id in\n" + 
    		"(select zuxxb_id from renyzqxb where renyxxb_id = "+visit.getRenyID()+")\n" + 
    		")\n" + 
    		"order by z.jib,z.xuh";

    	ExtTreeUtil etu = new ExtTreeUtil();
    	etu.setTreeDs(sql, "资源权限","wufuxk");
    	return etu.getDataset();
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

//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }

}
