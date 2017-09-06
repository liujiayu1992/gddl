package com.zhiren.jt.het.yunshtsh;
	import java.sql.ResultSet;
import java.text.SimpleDateFormat;
	import java.util.ArrayList;
	import java.util.Date;
	import java.util.List;

import javax.servlet.http.HttpUtils;
	
	import org.apache.tapestry.IMarkupWriter;
	import org.apache.tapestry.IRequestCycle;
	import org.apache.tapestry.form.IPropertySelectionModel;
	import org.apache.tapestry.html.BasePage;
	
import com.zhiren.common.DateUtil;
	import com.zhiren.common.IDropDownBean;
	import com.zhiren.common.IDropDownModel;
	import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
	import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Button;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;
	public class Yunshtsh extends BasePage {
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
	public String getXiaox(){
		if(((Visit) getPage().getVisit()).getString1()==null){
			((Visit) getPage().getVisit()).setString1("");
		}
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setXiaox(String xiaox){
		((Visit) getPage().getVisit()).setString1(xiaox);
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
	private boolean chakwbButton = false;
	public void chakwbButton(IRequestCycle cycle) {
		chakwbButton = true;
	}
//	private boolean ChexButton = false;
//	public void ChexButton(IRequestCycle cycle) {
//		ChexButton = true;
//	}
	public void submit(IRequestCycle cycle) {
		if (TijButton) {
			TijButton = false;
			tij();
		}
		if (HuitButton) {
			HuitButton = false;
			huit();
		}
		if (chakwbButton) {
			chakwbButton = false;
			chakwb();
		}
//		if (ChexButton) {
//			ChexButton = false;
//			Chex();
//		}
	}
	private Yunshtshbean _EditValue;
	public List getEditValues() {
		if(((Visit) getPage().getVisit()).getList5()==null){
			((Visit) getPage().getVisit()).setList5(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList5();
	}
	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList5(editList);
	}
	public Yunshtshbean getEditValue() {
		return _EditValue;
	}
	public void setEditValue(Yunshtshbean EditValue) {
		_EditValue = EditValue;
	}
	
//***************************************************************************//
	
	
    
	private boolean tijsh;//�Ƿ����  �ύ����������˹���
	
	public boolean isTijsh(){
		
		return tijsh;
	}
	
	public void setTijsh(){
		
		tijsh=false;
		
		String sql=" select * from xitxxb  where mingc='�����ͬģ���ύ���' and leib='��ͬģ��' and zhi='��' and zhuangt=1 ";
	
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			tijsh=true;
		}
		
	}

	
	public void getSelectData() {
		List list=getEditValues();
		String sql="";
		list.clear();
		JDBCcon con=new JDBCcon();
		String tableName="hetys";
		String tableName1="hetys_mb";
		String leib="��ͬ";
		String leib1="��ͬģ��";
		long renyxxb_id=((Visit) getPage().getVisit()).getRenyID();
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),this.getTreeid());
	
		((Visit) getPage().getVisit()).setExtTree1(etu);
		String diancWhere="";
//��ʾ�跽
		boolean flag=MainGlobal.getXitxx_item("��ͬ", "��ʾ�跽", "0", "��").equals("��");
		if(flag){//){
			diancWhere=" and hetys.diancxxb_id in (select id \n"
			+"from( \n"
			+"select id from diancxxb \n"
			+"start with fuid="+getTreeid()+" \n"
			+"connect by fuid=prior id) \n"
			+"union \n"
			+"select id \n"
			+"from diancxxb \n"
			+"where id="+getTreeid()+") \n";
		}
		this.setTijsh();
		if(getweizSelectValue().getId()==1){//�Լ�������
			 sql="select ''Shend,hetys.id,hetbh,gongfdwmc,xufdwmc,qiandrq, \n"
				+"liucztb.id liucztb_id, \n"
				+"leibztb.mingc zhuangt,liucztb.liucb_id,nvl('','') as HISTRYYJ,nvl('��ͬ','') as leix,nvl('�鿴','') as chak \n"
				+"from hetys,liucztb,leibztb,liuclbb \n"
				+"where liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id \n"
				+"and hetys.liucztb_id=liucztb.id and hetys.id in ("+Liuc.getWodrws(tableName, renyxxb_id, leib)+") \n"
				+"and to_char(hetys.qiandrq,'YYYY')="+getNianfValue().getId()+" \n"
				+diancWhere
				+"group by  hetys.id,hetbh,gongfdwmc,xufdwmc,qiandrq,leibztb.mingc,liucztb.id,liucztb.liucb_id \n";
				
			 
			 if(this.isTijsh()){
				 
				 sql+=" union \n"
					 
					 +"select ''Shend,hetys_mb.id,hetys_mb.mingc hetbh,gongfdwmc,xufdwmc,qiandrq, \n"
						+"liucztb.id liucztb_id, \n"
						+"leibztb.mingc zhuangt,liucztb.liucb_id,''HISTRYYJ,nvl('ģ��','') as leix,nvl('�鿴','') as chak \n"
						+"from hetys_mb,liucztb,leibztb,liuclbb \n"
						+"where liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id \n"
						+"and hetys_mb.liucztb_id=liucztb.id and hetys_mb.id in ("+Liuc.getWodrws(tableName1, renyxxb_id, leib1)+") \n"
						+"and to_char(hetys_mb.qiandrq,'YYYY')="+getNianfValue().getId()+" \n"
						+diancWhere
						+"group by  hetys_mb.id,hetys_mb.mingc,gongfdwmc,xufdwmc,qiandrq,leibztb.mingc,liucztb.id,liucztb.liucb_id";
						
			 }
 			 ResultSetList rs=con.getResultSetList(sql);
			 while(rs.next()){
				 rs.setString("Shend", String.valueOf(Liuc.getShendId(rs.getLong("liucb_id"),rs.getLong("liucztb_id"))));
			 }
			 rs.beforefirst();
//				int i=0;
//				try{
//					while(rs.next()){
//						long id=rs.getLong("id");
//						String hetbh=rs.getString("hetbh");
//						String gongfdwmc=rs.getString("gongfdwmc");
//						String xufdwmc=rs.getString("xufdwmc");
//						Date qiandrq=rs.getDate("qiandrq");
//						String leix=rs.getString("leib");
//						String zhuangt=rs.getString("zhuangt");
//						long liucztb_id=rs.getLong("liucztb_id");
//						long hetl=rs.getLong("hetl");
//						long liucb_id=rs.getLong("liucb_id");
//						int Shend=;////1,����״̬0��ʾ������״̬
//						list.add(new Hetshbean(++i,id,hetbh,gongfdwmc,xufdwmc,hetl,qiandrq,zhuangt,leix,Shend,liucztb_id,liucb_id));
//					}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				con.Close();
			 ExtGridUtil egu = new ExtGridUtil("SelectFrmDiv", rs);
//			 egu.setGridType(ExtGridUtil.Gridstyle_Read);
				egu.getColumn("id").setHidden(true);
				egu.getColumn("liucb_id").setHidden(true);
				egu.getColumn("liucztb_id").setHidden(true);
				egu.getColumn("Shend").setHidden(true);
				egu.getColumn("HISTRYYJ").setHidden(true);
				egu.getColumn("hetbh").setHeader("��ͬ���");
				egu.getColumn("gongfdwmc").setHeader("������λ����");
				egu.getColumn("xufdwmc").setHeader("�跽��λ����");
				egu.getColumn("qiandrq").setHeader("ǩ������");
//				egu.getColumn("leib").setHeader("���");
				egu.getColumn("zhuangt").setHeader("״̬");
//				egu.getColumn("hetl").setHeader("��ͬ��");
				egu.getColumn("chak").setHeader("�鿴");
				egu.getColumn("leix").setHeader("����");
				
				if(!this.isTijsh()){
					egu.getColumn("leix").setHidden(true);
				}
				
				String str="";
				if(this.isTijsh()){
					 str=
			       		" var url = 'http://'+document.location.host+document.location.pathname;"+
			            "var end = url.indexOf(';');"+
						"url = url.substring(0,end);"+
			       	    "url = url + '?service=page/' + 'Yunshtshrz&hetys_id='+record.data['ID']+'&leix='+((record.data['LEIX']=='��ͬ')?'1':'2');";
				}else{
					 str=
			       		" var url = 'http://'+document.location.host+document.location.pathname;"+
			            "var end = url.indexOf(';');"+
						"url = url.substring(0,end);"+
			       	    "url = url + '?service=page/' + 'Yunshtshrz&hetys_id='+record.data['ID'];";
				}
				
				
				
				egu.getColumn("chak").setRenderer(
						"function(value,p,record){" +str+
						"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>�鿴</a>\"}"
				);

				egu.addToolbarItem("{"+new GridButton("�ύ","function(){document.getElementById('tijButton').click();}").getScript()+"}");
				egu.addToolbarItem("{"+new GridButton("����","function(){document.getElementById('huitButton').click();}").getScript()+"}");
				egu.addToolbarItem("{"+new GridButton("������","function(){ " +
						"if(SelectFrmDiv_sm.hasSelection()){" +
						" if(weiz.getRawValue()=='������'){ " +
						" document.getElementById('DivMy_opinion').className = 'x-hidden';}" +
						"	window_panel.show(); " +
						"  rec = SelectFrmDiv_grid.getSelectionModel().getSelections(); " +
						" for(var i=0;i<rec.length;i++){ " +
						" 	var strtmp=rec[i].get('HISTRYYJ').replace(/_/g,'\\n');" +
						" document.getElementById('Histry_opinion').value=strtmp;}"+
						" }else{ "+
						" 	alert('��ѡ��һ����ͬ!');} "+
						"}").getScript()+"}");
//				egu.addToolbarItem("{"+new GridButton("������","function(){ " +
//						"if(SelectFrmDiv_sm.hasSelection()){ " +
//						" if(Weiz.getRawValue()=='������'){ " +
//						" document.getElementById('DivMy_opinion').className = 'x-hidden';}" +
//						"	window_panel.show(); " +
//						"  rec = SelectFrmDiv_grid.getSelectionModel().getSelections(); " +
//						" document.getElementById('My_opinion').value='';" +
//						" document.getElementById('Histry_opinion').value='';" +
//						" for(var i=0;i<rec.length;i++){ " +
//						"   var strmyp='������'+rec[i].get('BIANM')+':\\n'+rec[i].get('YIJ')+'\\n'; "+
//						" 	document.getElementById('My_opinion').value+=strmyp;" +
//						" 	var strtmp=rec[i].get('HISTRYYJ').replace(/_/g,'\\n');" +
//						" document.getElementById('Histry_opinion').value=strtmp+'\\n';}"+
//						" }else{ "+
//						" 	alert('��ѡ��һ�Ž��㵥!');} "+
//						"}").getScript()+"}");
//				egu.addTbarText("��λ:");
//				ComboBox comb=new ComboBox();
//				comb.setId("danw");
//				comb.setTransform("danwSelect");
//				comb.setLazyRender(true);//��̬��
//				egu.addToolbarItem(comb.getScript());
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
				//				egu.setOtherScript("danw.on('change',function(){document.forms[0].submit();})");
//				egu.setOtherScript("weiz.on('change',function(){document.forms[0].submit();})");
				if(flag){
					egu.addTbarTreeBtn("diancTree");
				}
				//���
				List tmp= new ArrayList();
				
				for(int i=0;i<rs.getRows();i++){
					String strtmp="";
					tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,1)));
					for(int j=0;j<tmp.size();j++){
						strtmp+=((Yijbean)tmp.get(j)).getXitts()+"_"+(((Yijbean)tmp.get(j)).getYij()==null?"":((Yijbean)tmp.get(j)).getYij());
					}
//					egu.setDataValue(i, 13, "������ "+egu.getDataValue(i,1)+":_ "+strtmp);
					egu.setDataValue(i, 12, strtmp);
				}
			((Visit) this.getPage().getVisit()).setExtGrid1(egu);
		}else{//�����е����񣨲������Լ��ģ�
			sql="select ''Shend,hetys.id,hetbh,gongfdwmc,xufdwmc,qiandrq, \n"
				+"liucztb.id liucztb_id, \n"
				+"leibztb.mingc zhuangt,liucztb.liucb_id,''HISTRYYJ,nvl('��ͬ','') as leix,nvl('�鿴','') as chak \n"
				+"from hetys,liucztb,leibztb,liuclbb \n"
				+"where liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id \n"
				+"and hetys.liucztb_id=liucztb.id and hetys.id in ("+Liuc.getLiuczs(tableName, renyxxb_id, leib)+") \n"
				+"and to_char(hetys.qiandrq,'YYYY')="+getNianfValue().getId()+" \n"
				+diancWhere
				+"group by  hetys.id,hetbh,gongfdwmc,xufdwmc,qiandrq,leibztb.mingc,liucztb.id,liucztb.liucb_id \n";
			
			
			if(this.isTijsh()){
				
				
				sql+=" union \n "
					+"select ''Shend,hetys_mb.id,hetys_mb.mingc hetbh,gongfdwmc,xufdwmc,qiandrq, \n"
					+"liucztb.id liucztb_id, \n"
					+"leibztb.mingc zhuangt,liucztb.liucb_id,''HISTRYYJ,nvl('ģ��','') as leix,nvl('�鿴','') as chak \n"
					+"from hetys_mb,liucztb,leibztb,liuclbb \n"
					+"where liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id \n"
					+"and hetys_mb.liucztb_id=liucztb.id and hetys_mb.id in ("+Liuc.getLiuczsMB(tableName1, renyxxb_id, leib1)+") \n"
					+"and to_char(hetys_mb.qiandrq,'YYYY')="+getNianfValue().getId()+" \n"
					+diancWhere
					+"group by  hetys_mb.id,hetys_mb.mingc,gongfdwmc,xufdwmc,qiandrq,leibztb.mingc,liucztb.id,liucztb.liucb_id";
				
			}
				 ResultSetList rs=con.getResultSetList(sql);
				 while(rs.next()){
					 rs.setString("Shend", String.valueOf(Liuc.getShendId(rs.getLong("liucb_id"),rs.getLong("liucztb_id"))));
				 }
				 rs.beforefirst();
//			int i=0;
//			try{
//				while(rs.next()){
//					long id=rs.getLong("id");
//					String hetbh=rs.getString("hetbh");
//					String gongfdwmc=rs.getString("gongfdwmc");
//					String xufdwmc=rs.getString("xufdwmc");
//					Date qiandrq=rs.getDate("qiandrq");
//					String leix=rs.getString("leib");
//					String zhuangt=rs.getString("zhuangt");
//					long hetl=rs.getLong("hetl");
//					long liucztb_id=rs.getLong("liucztb_id");
//					long liucb_id=rs.getLong("liucb_id");
//					list.add(new Hetshbean(++i,id,hetbh,gongfdwmc,xufdwmc,hetl,qiandrq,zhuangt,leix,0,liucztb_id,liucb_id));
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			con.Close();
				ExtGridUtil egu = new ExtGridUtil("SelectFrmDiv", rs);
//				 egu.setGridType(ExtGridUtil.Gridstyle_Read);
				egu.getColumn("id").setHidden(true);
				egu.getColumn("liucb_id").setHidden(true);
				egu.getColumn("liucztb_id").setHidden(true);
				egu.getColumn("Shend").setHidden(true);
				egu.getColumn("HISTRYYJ").setHidden(true);
				egu.getColumn("hetbh").setHeader("��ͬ���");
				egu.getColumn("gongfdwmc").setHeader("������λ����");
				egu.getColumn("xufdwmc").setHeader("�跽��λ����");
				egu.getColumn("qiandrq").setHeader("ǩ������");
//				egu.getColumn("leib").setHeader("���");
				egu.getColumn("zhuangt").setHeader("״̬");
//				egu.getColumn("hetl").setHeader("��ͬ��");
				egu.getColumn("chak").setHeader("�鿴");
//				
				egu.getColumn("leix").setHeader("����");
				
				if(!this.isTijsh()){
					egu.getColumn("leix").setHidden(true);
				}
				String str="";
				if(this.isTijsh()){
					 str=
			       		" var url = 'http://'+document.location.host+document.location.pathname;"+
			            "var end = url.indexOf(';');"+
						"url = url.substring(0,end);"+
			       	    "url = url + '?service=page/' + ''Yunshtshrz&hetys_id='+record.data['ID']+'&leix='+((record.data['LEIX']=='��ͬ')?'1':'2');";
				}else{
					 str=
			       		" var url = 'http://'+document.location.host+document.location.pathname;"+
			            "var end = url.indexOf(';');"+
						"url = url.substring(0,end);"+
			       	    "url = url + '?service=page/' + ''Yunshtshrz&hetys_id='+record.data['ID'];";
				}
				
					
					egu.getColumn("chak").setRenderer(
							"function(value,p,record){" +str+
							"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>�鿴</a>\"}"
					);

//				egu.addTbarText("��λ:");
//				ComboBox comb=new ComboBox();
//				comb.setId("danw");
//				comb.setTransform("danwSelect");
//				comb.setLazyRender(true);//��̬��
//				egu.addToolbarItem(comb.getScript());
				//
				egu.addToolbarItem("{"+new GridButton("������","function(){ " +
						"if(SelectFrmDiv_sm.hasSelection()){" +
						" if(weiz.getRawValue()=='������'){ " +
						" document.getElementById('DivMy_opinion').className = 'x-hidden';}" +
						"	window_panel.show(); " +
						"  rec = SelectFrmDiv_grid.getSelectionModel().getSelections(); " +
						" for(var i=0;i<rec.length;i++){ " +
						" 	var strtmp=rec[i].get('HISTRYYJ').replace(/_/g,'\\n');" +
						" document.getElementById('Histry_opinion').value=strtmp;}"+
						" }else{ "+
						" 	alert('��ѡ��һ����ͬ!');} "+
						"}").getScript()+"}");	
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
				if(flag){
					egu.addTbarTreeBtn("diancTree");
				}
				((Visit) this.getPage().getVisit()).setExtGrid1(egu);
//				���
				List tmp= new ArrayList();
				
				for(int i=0;i<rs.getRows();i++){
					String strtmp="";
					tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,1)));
					for(int j=0;j<tmp.size();j++){
						strtmp+=((Yijbean)tmp.get(j)).getXitts()+"_"+(((Yijbean)tmp.get(i)).getYij()==null?"":((Yijbean)tmp.get(i)).getYij());
					}
//					egu.setDataValue(i, 13, "������ "+egu.getDataValue(i,1)+":_ "+strtmp);
					egu.setDataValue(i, 12, strtmp);
				}
		}
		setXiaox("");
	}
	
	/**1, ���ݺ�ͬ״̬�������ҳ���һ��״̬�����и���
	 * 2,�ڸ��º�ͬ״̬��ͬʱ��д��־
	 */
	private void tij(){
//		List list=getEditValues();
		if(getEditTableRow()!=-1){
			ExtGridUtil ExtGrid1=((Visit) this.getPage().getVisit()).getExtGrid1();
//			Hetshbean bean=(Hetshbean)list.get(getEditTableRow());
			String tableName="hetys";
			String tableName1="hetys_mb";
	//		String leib="��ͬ";
			long renyxxb_id=((Visit) getPage().getVisit()).getRenyID();
			
			if(ExtGrid1.griddata[getEditTableRow()][10]!=null && ExtGrid1.griddata[getEditTableRow()][10].equals("��ͬ")){
				Liuc.tij(tableName,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][1]) , renyxxb_id, getXiaox());
			}else if(ExtGrid1.griddata[getEditTableRow()][10]!=null && ExtGrid1.griddata[getEditTableRow()][10].equals("ģ��")){
				Liuc.tij(tableName1,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][1]) , renyxxb_id, getXiaox());
			}
			
			
//			getSelectData();
		}
	}
	/**1, ���ݺ�ͬ״̬�������ҳ���һ��״̬�����и���
	 * 2,�ڸ��º�ͬ״̬��ͬʱ��д��־
	 */
	private void huit(){
//		List list=getEditValues();
		if(getEditTableRow()!=-1){
			ExtGridUtil ExtGrid1=((Visit) this.getPage().getVisit()).getExtGrid1();
			String tableName="hetys";
			String tableName1="hetys_mb";
//			String leib="��ͬ";
			long renyxxb_id=((Visit) getPage().getVisit()).getRenyID();
			
			if(ExtGrid1.griddata[getEditTableRow()][10]!=null && ExtGrid1.griddata[getEditTableRow()][10].equals("��ͬ")){
				Liuc.huit(tableName,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][1]), renyxxb_id, getXiaox());
			}else if(ExtGrid1.griddata[getEditTableRow()][10]!=null && ExtGrid1.griddata[getEditTableRow()][10].equals("ģ��")){
				Liuc.huit(tableName1,Long.parseLong(ExtGrid1.griddata[getEditTableRow()][1]), renyxxb_id, getXiaox());
			}
			
//			getSelectData();
		}
	}
	private void chakwb(){
		
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
			setXiaox(null);
			setTreeid("");
//			getSelectData();
			visit.setboolean4(true);
			
			visit.setString2("");
		}
//		if(((Visit) getPage().getVisit()).getboolean1()||((Visit) getPage().getVisit()).getboolean2()||((Visit) getPage().getVisit()).getboolean3()){//�����ͬλ�øı�
//			//1, λ��2, ���3, ��λ
//			if(((Visit) getPage().getVisit()).getboolean1()==true){
//				if(getweizSelectValue().getId()==1){
//					visit.setboolean4(true);
//				}else{
//					visit.setboolean4(false);
//				}
//			}
//			getSelectData();
//		}
		getSelectData();
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}
	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}
	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
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
        list.add(new IDropDownBean(1,"�ҵ�����"));
        list.add(new IDropDownBean(2,"������"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
    //���
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(getIDropDownBean(getNianfModel(),new Date().getYear()+1900));
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
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date()); i++) {
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
//	�ҵ����
	public void setMy_opinion(String value){
		
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getMy_opinion(){
		
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	//��ʷ���
	public void setHistry_opinion(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getHistry_opinion(){
		
		return ((Visit) getPage().getVisit()).getString2();
	}
}
