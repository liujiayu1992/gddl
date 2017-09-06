package com.zhiren.jt.het.hetshmenu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Liucdzcl;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.dc.chengbgl.Chengbcl;
import com.zhiren.main.Visit;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;


public class Hetlc_Yijtx extends BasePage implements PageValidateListener {

	private String My_opinion;

	private String Histry_opinion;

	private String ShenHeYJChange;

	private String biaos; // ����ʶΪokʱ��ǰ̨ҳ�������ɺ󣬼��رմ���

	private String liuczt;

	private String gengXinTiShi;//(����ͬ��Ÿ��ĳɹ�ʱ�����ʾ��Ϣ)

	private boolean t = false;
	
//	private String liuclb;  //�������   ����   ��ͬ
	
	private String TableNameIdStr;
	
	private String menuId;
	public String getGengXinTiShi() {
		return gengXinTiShi;
	}

	public void setGengXinTiShi(String gengXinTiShi) {
		this.gengXinTiShi = gengXinTiShi;
	}
    
	private String Xinbh ="";
	
	public String getXinbh(){
		
		  return Xinbh;
	}
	
	public void setXinbh(String Xinbh){
		
		this.Xinbh=Xinbh;
	}
	public String getLiuczt() {
		return liuczt;
	}

	public void setLiuczt(String liuczt) {
		this.liuczt = liuczt;
	}

	public String getShenHeYJChange() {
		return ShenHeYJChange;
	}

	public void setShenHeYJChange(String shenHeYJChange) {
		ShenHeYJChange = shenHeYJChange;
	}

	public String getHistry_opinion() {
		return Histry_opinion;
	}

	public void setHistry_opinion(String histry_opinion) {
		Histry_opinion = histry_opinion;
	}

	public String getMy_opinion() {
		return My_opinion;
	}

	public void setMy_opinion(String my_opinion) {
		My_opinion = my_opinion;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		
		((Visit) getPage().getVisit()).setString1("");		//Tpye �趨��ҳ��Ҫ������߼�����
		((Visit) getPage().getVisit()).setString2("");		//dongzlx �������ͣ��ύ�����ˣ�
		((Visit) getPage().getVisit()).setString3("");		//liuclb ������𣨽��㡢��ͬ��
//		((Visit) getPage().getVisit()).getString5();		//ǰ��洢��diancxxb��ID		
		
		if(cycle.getRequestContext().getRequest().getParameter("lx")!=null ){
			
			((Visit) getPage().getVisit()).setString1(cycle.getRequestContext().getRequest().getParameter("lx"));
		}
		
		this.liuczt = ((Visit) getPage().getVisit()).getDropDownBean2().getValue();
		
		this.My_opinion="";
		
		this.menuId=cycle.getRequestContext().getRequest().getParameter("menuId");
		
		String liucclsb=((Visit) getPage().getVisit()).getLiucclsb().toString();
	
		
		this.TableNameIdStr=liucclsb.substring(0, liucclsb.indexOf("+"));
		this.Histry_opinion=liucclsb.substring(liucclsb.indexOf("+")+1);
		
		
		if(t){                //tΪtrueʱ ˵���Ǳ�ҳ���̨���������¼���  tΪfalse˵���Ǳ��ҳ����������ģ���ʼ������
			t=false;
		}else{
			this.biaos="";
			this.gengXinTiShi = "";
		}
		getSelectData();
	}
	
	public void getSelectData() {   //�õ�ҳ���ԭ���ı��
		JDBCcon con=new JDBCcon();
		String sql="";
		String OldHtbh="";
		List TableName=new ArrayList();
		List TableID=new ArrayList();
		Liucdzcl.TableNameIdList(TableName, TableID, this.TableNameIdStr);
		ResultSetList rsl=null;
		if(TableName.size()==TableID.size()){
			
			for(int i=0;i<TableName.size();i++){
				
				Object objName=TableName.get(i);
				Object objID=TableID.get(i);
    			
				if(objName.equals("hetb")){
					
				   sql="select hetb.hetbh from  "+objName+" where hetb.id="+objID+""; 
				   
				   rsl=con.getResultSetList(sql);
				   try{
						
						if(rsl.next()){
							
							OldHtbh=rsl.getString("hetbh");
						}
						rsl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						con.Close();
					}
				}else if(objName.equals("hetb_mb")){
					
				   sql="select hetb_mb.hetbh from  "+objName+" where hetb_mb.id="+objID+""; 
				   rsl=con.getResultSetList(sql);
				   try{
						
						if(rsl.next()){
							
							OldHtbh=rsl.getString("hetbh");
						}
						rsl.close();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						con.Close();
					}
			    }
			}	
		}
		this.setXinbh(OldHtbh);
	}

	public void Submit(IRequestCycle cycle) {
		
		//�ж϶�������   ��������ɵĹ��� ���㻹�Ǻ�ͬ   �ύ���ǻ���
		this.dongzlx();
		
		t=true;
		if(((Visit) getPage().getVisit()).getString3().equals("����")){
			
			this.hetdztij();
			
		}else if(((Visit) getPage().getVisit()).getString3().equals("��ͬ")){
			
			this.hetdztij();
	
		}
		
		this.biaos = "ok";
	}
//	�ύ/����
	private void hetdztij(){
		
		if(((Visit) getPage().getVisit()).getString1()!=null){
			
			if(UpdateHetbh(this.getXinbh())){
				
				if(((Visit) getPage().getVisit()).getString2().equals("�ύ")){
					
					this.Tij();
					
				}else if(((Visit) getPage().getVisit()).getString2().equals("����")){
					
					
					this.Huit();
					
				}
			}else{
				
				this.gengXinTiShi="";
			}
			
			
		}
	}
	
	private boolean UpdateHetbh(String Xinbh) {
		JDBCcon con=new JDBCcon();
		String sql="";
		String hetbh=getXinbh();
		List TableName=new ArrayList();
		List TableID=new ArrayList();
		Liucdzcl.TableNameIdList(TableName, TableID, this.TableNameIdStr);
		
		if(TableName.size()==TableID.size()){
			
			for(int i=0;i<TableName.size();i++){
				
				Object objName=TableName.get(i);
				Object objID=TableID.get(i);
    			
				if(objName.equals("hetb")){
					
				   sql="update  "+objName+" set hetb.hetbh='"+hetbh+"'where  hetb.id="+objID+""; 
				
				   con.getUpdate(sql);		
	
				}else if(objName.equals("hetb_mb")){
					
					sql="update "+objName+" set hetb_mb.hetbh='"+hetbh+"'where  hetb_mb.id="+objID+"";
		
					con.getUpdate(sql);			
			    }
			}	
		}
         return true;
  
		}


	//�������� ���ж����ύ  ���ǻ���    �ǽ��㻹�Ǻ�ͬ
	private void dongzlx(){
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select distinct  liucdzb.mingc,liuclbb.mingc leib  from liucdzjsb,liucdzb,liucztb,liuclbb,leibztb ");
		bf.append(" where liucdzjsb.liucdzb_id=liucdzb.id ");
		bf.append(" and liucztb.leibztb_id=leibztb.id ");
		bf.append(" and liucdzb.liucztqqid=liucztb.id");
		bf.append(" and leibztb.liuclbb_id=liuclbb.id");
		bf.append(" and liucdzb.id=").append(this.menuId);
		bf.append(" and liucdzjsb.liucjsb_id in");
		bf.append(" (select liucjsb_id from renyjsb where renyxxb_id=").append(((Visit) getPage().getVisit()).getRenyID()).append(")");
		
		JDBCcon con = new JDBCcon();
		ResultSet rs=con.getResultSet(bf);
		try{
			
			if(rs.next()){
				
				((Visit) getPage().getVisit()).setString2(rs.getString("mingc"));
				((Visit) getPage().getVisit()).setString3(rs.getString("leib"));
			}
			rs.close();
			bf=null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}

	//������ҳ���ύ
public void Tij(){
		
		List TableName=new ArrayList();
		List TableID=new ArrayList();
		
		Liucdzcl.TableNameIdList(TableName, TableID, this.TableNameIdStr);
		
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		
		if(TableName.size()==TableID.size()){
			
			for(int i=0;i<TableName.size();i++){
				
				Object objName=TableName.get(i);
				Object objID=TableID.get(i);
    			Liuc.tij(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id,this.My_opinion);
				Jiesdcz.Zijsdlccl(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id, this.My_opinion, 0, "TJ");
				
					
			}
		}
	}

	public void Huit(){
		

		List TableName=new ArrayList();
		List TableID=new ArrayList();
		
		Liucdzcl.TableNameIdList(TableName, TableID, this.TableNameIdStr);
		
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		
		if(TableName.size()==TableID.size()){
			
			for(int i=0;i<TableName.size();i++){
				
				Object objName=TableName.get(i);
				Object objID=TableID.get(i);
				Jiesdcz.Zijsdlccl(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id, this.My_opinion, 0, "HT");
				Liuc.huit(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id,this.My_opinion);
					
			}
		}
	}
	
	
	

	public void pageValidate(PageEvent arg0) {
		// TODO �Զ����ɷ������

	}

	// �����Ҫ��ҳ��ˢ�£����Ե��ô˷���
	public void rush() {

		Histry_opinion = "";
		if (this.ShenHeYJChange != null && !this.ShenHeYJChange.equals("")) {

			String change[] = this.ShenHeYJChange.split(";");

			JDBCcon con = new JDBCcon();

			for (int i = 0; i < change.length; i++) {

				if (change[i] == null || "".equals(change[i])) {

					continue;
				}

				String sql = "select * from liucgzb where liucgzid in ( ";
				String record[] = change[i].split(",");
				sql += record[0];

				sql += ")  order by shij";

				ResultSetList rsl = con.getResultSetList(sql);

				String his = record[13].substring(0, record[13].indexOf(":"))
						+ "\n";
				while (rsl.next()) {
					his += rsl.getString("caozy") + ""
							+ rsl.getDateTimeString("shij") + ""
							+ rsl.getString("liucdzbmc") + ""
							+ rsl.getString("qianqztmc") + "   "
							+ rsl.getString("miaos") + ":\n";

				}
				his += "-------------------------------------\n";

				rsl.close();

				Histry_opinion += his;

			}

			con.Close();

		}

	}

}