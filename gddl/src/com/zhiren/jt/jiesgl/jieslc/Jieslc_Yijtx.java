package com.zhiren.jt.jiesgl.jieslc;

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


public class Jieslc_Yijtx extends BasePage implements PageValidateListener {

	private String My_opinion;

	private String Histry_opinion;

	private String ShenHeYJChange;

	private String biaos; // ����ʶΪokʱ��ǰ̨ҳ�������ɺ󣬼��رմ���

	private String liuczt;

	private String gengXinTiShi;// ������Ϣ�����������ܸ���ʱ����ʾ�û�������(���ܸ��µĽ��㵥���)

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
			
//			Ŀǰ����Ϊ��Cwrz���������ˣ���Update���ϴ����ֹ�˾��
			((Visit) getPage().getVisit()).setString1(cycle.getRequestContext().getRequest().getParameter("lx"));
		}
		
		this.liuczt = ((Visit) getPage().getVisit()).getDropDownBean2().getValue();
		
		this.My_opinion="";
		
		this.menuId=cycle.getRequestContext().getRequest().getParameter("menuId");
		
		String liucclsb=((Visit) getPage().getVisit()).getLiucclsb().toString();
	
		
		this.TableNameIdStr=liucclsb.substring(0, liucclsb.indexOf("+"));
		this.Histry_opinion=liucclsb.substring(liucclsb.indexOf("+")+1);
		
		
		if(t){                //tΪtrueʱ ˵���Ǳ�ҳ���̨���������¼���  tΪfalse˵���Ǳ��ҳ����������ģ���ʼ��������
			t=false;
		}else{
			this.biaos="";
			this.gengXinTiShi = "";
		}
	}

	public void Submit(IRequestCycle cycle) {
		
		//�ж϶�������   ��������ɵĹ��� ���㻹�Ǻ�ͬ   �ύ���ǻ���
		this.dongzlx();
		
		t=true;
		if(((Visit) getPage().getVisit()).getString3().equals("����")){
			
			this.jiesdztij();
			
		}else if(((Visit) getPage().getVisit()).getString3().equals("��ͬ")){
			//�ȴ���ͬ����
		}
		
		this.biaos = "ok";
	}
	//�����ύ
	private void jiesdztij(){
		
	//	System.out.println(this.dongzlx+this.cwrz);
		//	����������������ҳ���ύ/����
		if(((Visit) getPage().getVisit()).getString1()!=null){
			
			if(((Visit) getPage().getVisit()).getString2().equals("�ύ")){
				
				this.Tij();
				
			}else if(((Visit) getPage().getVisit()).getString2().equals("����")){
				
				
				this.Huit();
				
			}
		}
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
				
				if(((Visit) getPage().getVisit()).getString1().equals("Cwrz")){  //��������  ��Ҫ��֤��Ϣ�Ƿ�����
					
					if(objName instanceof List){  //����Ʊ�ύ
						
						List listName=(List)objName;
						List listID=(List)objID;
						
						boolean t=false;
						for(int j=0;j<listName.size();j++){
							
							if(!this.shiFouGengXin((String)listID.get(j),(String)listName.get(j))){
								
								t=true;
								break;
							}
						}
						
						if(t){
							this.gengXinTiShi += "<font style='color:red'>"+this.getTablebm(listName.get(0).toString(), listID.get(0).toString()) + "</font><br>";
							continue;
						}
						
						for(int j=0;j<listName.size();j++){
							
							String name=(String)listName.get(j);
							long id=Long.valueOf((String)listID.get(j)).longValue();
							
							Liuc.tij(name, id, renyxxb_id,this.My_opinion);
							Jiesdcz.Zijsdlccl(name, id, renyxxb_id, this.My_opinion, 0, "TJ");
							this.gengXin(id+"", name, renyxxb_id);
							
							if(MainGlobal.getXitxx_item("�ɱ�", "�ݹ��㷨����", 
									String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "���ƹ���").trim().equals("���ƹ���")){
								
								Countcb((String)listName.get(j),(String)listID.get(j));
							}
						}
					}else{
						
//						һƱ����
						if(this.shiFouGengXin(objID.toString(), objName.toString())){
							
							Liuc.tij(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id,this.My_opinion);
							Jiesdcz.Zijsdlccl(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id, this.My_opinion, 0, "TJ");
							this.gengXin(objID.toString(), objName.toString(), renyxxb_id);
							Countcb(objName.toString(),objID.toString());
						}else{
							
							this.gengXinTiShi += "<font style='color:red'>"+this.getTablebm(objName.toString(), objID.toString()) + "</font><br>";
						}
						
					}
					
				}else if(((Visit) getPage().getVisit()).getString1().equals("")){  //�������ύ
					
					if(objName instanceof List){  //����Ʊ�ύ
						
						List listName=(List)objName;
						List listID=(List)objID;
						for(int j=0;j<listName.size();j++){
							
							String name=(String)listName.get(j);
							long id=Long.valueOf((String)listID.get(j)).longValue();
							
							Liuc.tij(name, id, renyxxb_id,this.My_opinion);
							Jiesdcz.Zijsdlccl(name, id, renyxxb_id, this.My_opinion, 0, "TJ");
							
						}
					}else{
						
						Liuc.tij(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id,this.My_opinion);
						Jiesdcz.Zijsdlccl(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id, this.My_opinion, 0, "TJ");
					}
					
				} else if(((Visit) getPage().getVisit()).getString1().equals("Update")){	//�ϴ����ϼ���λ"jiesb"��
					
//					List listName=(List)objName;
//					List listID=(List)objID;
					
					List listName=null;
					List listID=null;
					if(objName instanceof List){
						 listName=(List)objName;
						 listID=(List)objID;
					}else{
						listName=new ArrayList();
						listID=new ArrayList();
						listName.add(objName.toString());
						listID.add(objID.toString());
					}
					
					Update_Tj(listName,listID,renyxxb_id);					
				} else if(((Visit) getPage().getVisit()).getString1().equals("Insert")){	//�ϴ����ϼ���λ"jiesb"��
					
					List listName=null;
					List listID=null;
					if(objName instanceof List){
						 listName=(List)objName;
						 listID=(List)objID;
					}else{
						listName=new ArrayList();
						listID=new ArrayList();
						listName.add(objName.toString());
						listID.add(objID.toString());
					}
					
					Update_tj(listName, listID, renyxxb_id);					
				}
			}
		}
	}
	
	private void Update_Tj(List listName, List listID, long renyxxb_id){
		
//		1�������ؽ��㵥�ϴ����ֹ�˾��ok��
//			�����ı�
//				������
//					 jiesb/jiesyfb
//					 jieszbsjb
//					 danpcjsmxb
		
//			 	��˾����
//					 jiesb/jiesyfb
//					 jieszbsjb
//					 danpcjsmxb
		
//		2�����ֹ�˾�Ľ��㵥�·����糧(ok)
//			�����ı�
//				��˾����
//					 ��diancjsmkb/diancjsyfb�����ҵ�jiesb_id/jiesyfb_id
//					 �����¼��糧jiesb/jiesyfb��Ӧid��liucztb_id
				
//				 ������
//					 jiesb/jiesyfb
		
		
		JDBCcon con = new JDBCcon();
		ResultSet rs = null;
		String sql="";
		String name="";
		long id=0;
		
		try{
			
			for(int j=0;j<listName.size();j++){
				
				name=(String)listName.get(j);	//����
				id=Long.valueOf((String)listID.get(j)).longValue();	//��id
//				�����ύ����
				Liuc.tij(name, id, renyxxb_id,this.My_opinion);
				
//				�ӽ��㵥����
				sql="select id from "+name+" where fuid="+id;
				rs = con.getResultSet(sql);
				while(rs.next()) {
//					������ӽ��㵥���ֱ�����ύ
					Liuc.tij(name, rs.getLong("id"), renyxxb_id,this.My_opinion);
				}
			}
			
			if(rs!=null){
				
				rs.close();
			}
			
		}catch (SQLException e) {
			
			e.printStackTrace();
			
		}catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	private void Update_tj(List listName, List listID, long renyxxb_id){
		
		JDBCcon con = new JDBCcon();
		ResultSet rs = null;
		String sql="";
		String name="";
		long id=0;
		
		try{
			
			for(int j=0;j<listName.size();j++){
				
				name=(String)listName.get(j);	//����
				id=Long.valueOf((String)listID.get(j)).longValue();	//��id
//				�����ύ����
				Liuc.tij(name, id, renyxxb_id, this.My_opinion, "");
				
//				�ӽ��㵥����
				sql="select id from "+name+" where fuid="+id;
				rs = con.getResultSet(sql);
				while(rs.next()) {
//					������ӽ��㵥���ֱ�����ύ
					Liuc.tij(name, rs.getLong("id"), renyxxb_id, this.My_opinion, "");
				}
			}
			
			if(rs!=null){
				
				rs.close();
			}
			
		}catch (SQLException e) {
			
			e.printStackTrace();
			
		}catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	private void Countcb(String TableName, String TableId) {
		// TODO �Զ����ɷ������
		//		1���ȵõ�������id
		List fahb_id=new ArrayList();
		String sql="";
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		if(TableName.equals("jiesb")){
			
			sql="select id from fahb where jiesb_id="+TableId;
			rsl=con.getResultSetList(sql);
			while(rsl.next()){
				
				fahb_id.add(rsl.getString("id"));
			}
		}else if(TableName.equals("jiesyfb")){
			
			sql="select distinct c.fahb_id from chepb c,danjcpb dj\n" +
				"       where c.id=dj.chepb_id\n" + 
				"             and dj.yunfjsb_id="+TableId;

			rsl=con.getResultSetList(sql);
			while(rsl.next()){
				
				fahb_id.add(rsl.getString("fahb_id"));
			}
		}
		
		Chengbcl.CountCb_PerFah(fahb_id, true);
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
				
				if(((Visit) getPage().getVisit()).getString1().equals("Cwrz")){  //��������  ��Ҫ��֤��Ϣ�Ƿ�����
					
					if(objName instanceof List){  //����Ʊ����
						
						List listName=(List)objName;
						List listID=(List)objID;
						
						boolean t=false;
						for(int j=0;j<listName.size();j++){
							
							if(!this.shiFouGengXin((String)listID.get(j),(String)listName.get(j))){
								
								t=true;
								break;
							}
						}
						
						if(t){
							this.gengXinTiShi += "<font style='color:red'>"+this.getTablebm(listName.get(0).toString(), listID.get(0).toString()) + "</font><br>";
							
							continue;
						}
						
						for(int j=0;j<listName.size();j++){
							
							String name=(String)listName.get(j);
							long id=Long.valueOf((String)listID.get(j)).longValue();
						    Jiesdcz.Zijsdlccl(name, id, renyxxb_id, this.My_opinion, 0, "HT");
							Liuc.huit(name, id, renyxxb_id,this.My_opinion);
							this.gengXin(id+"", name, renyxxb_id);
						}
						
					}else{
						
						
//						if(this.shiFouGengXin(objID.toString(), objName.toString())){
						    Jiesdcz.Zijsdlccl(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id, this.My_opinion, 0, "HT");
							Liuc.huit(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id,this.My_opinion);
							this.gengXin(objID.toString(), objName.toString(), renyxxb_id);
//						}else{
//							
//							this.gengXinTiShi += "<font style='color:red'>"+this.getTablebm(objName.toString(), objID.toString()) + "</font><br>";
//							
//						}
					}
					
				}else if(((Visit) getPage().getVisit()).getString1().equals("")){  //����������
					
					if(objName instanceof List){  //����Ʊ����
						
						List listName=(List)objName;
						List listID=(List)objID;
						for(int j=0;j<listName.size();j++){
							
							String name=(String)listName.get(j);
							long id=Long.valueOf((String)listID.get(j)).longValue();
							Jiesdcz.Zijsdlccl(name, id, renyxxb_id, this.My_opinion, 0, "HT");
							Liuc.huit(name, id, renyxxb_id,this.My_opinion);
							
						}
					}else{
						Jiesdcz.Zijsdlccl(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id, this.My_opinion, 0, "HT");
						Liuc.huit(objName.toString(), Long.valueOf(objID.toString()).longValue(), renyxxb_id,this.My_opinion);
					}
				}else if(((Visit) getPage().getVisit()).getString1().equals("Update")){
					
					List listName=null;
					List listID=null;
					if(objName instanceof List){
						 listName=(List)objName;
						 listID=(List)objID;
					}else{
						listName=new ArrayList();
						listID=new ArrayList();
						listName.add(objName.toString());
						listID.add(objID.toString());
					}
					
					Update_Ht(listName,listID,renyxxb_id);
				}
			}
		}
	}
	
	private void Update_Ht(List listName, List listID, long renyxxb_id){
//		��ɾ���������յ��Ľ��㵥���ٸ��³��������̻���״̬,�ӷֹ�˾���ó����SQL��䣬�������Ľ��㵥���µ���һ��״̬
//		1����ɾ������Ľ��㵥��diancjsmkb��diancjsyfb����ļ�¼��jiesb��jiesyfb��ļ�¼������
//		ע�������۵��������ɾ��ʱ�ã�����ʱ����
		
//		1���ֹ�˾����˵糧�ϴ��Ľ��㵥�ǣ��緢�ִ��󣬵��ø÷������Ѵ������Ľ��㵥ɾ���������õ糧���㵥liucztb_id
//		�����ı�
//			������
//				 jiesb/jiesyfb
	
//		 	��˾����
//				 diancjsmkb/diancjsyfb
//				 jiesb/jiesyfb
//				 danpcjsmxb
//				 jieszbsjb
		
		JDBCcon con=new JDBCcon();
		ResultSet rs = null;
		String sql = "";
		String name="";
		long id=0;
		
		try {
			for(int j=0;j<listName.size();j++){
		
				boolean t=true;
				
				name = String.valueOf(listName.get(j));
				id = Long.parseLong(String.valueOf(listID.get(j)));
				
				t=Liuc.huit(name, id, renyxxb_id, this.My_opinion);
				
//				�ӽ��㵥����
				sql="select id from "+name+" where fuid="+id;
				rs = con.getResultSet(sql);
				while(rs.next()) {
//					������ӽ��㵥���ֱ�����ύ
					Liuc.huit(name, id, renyxxb_id, this.My_opinion);
				}
			}
			
			if(rs!=null){
				
				rs.close();
			}
			
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally{
			
			con.Close();
		}
	}
	

	//��ñ�ı���
	private String getTablebm(String tableName,String tableID ){
		
		String sql="select bianm from "+tableName+" where id="+tableID;
		JDBCcon con = new JDBCcon();
		ResultSet rs = con.getResultSet(sql);
		String s="";
		try{
			while(rs.next()){
				s=rs.getString("bianm");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return s;
	}
	//�ж��ܷ����
	
	private boolean checkFukInfo(){
		JDBCcon con = new JDBCcon();
		String value="��";
		try{
			ResultSet rs=con.getResultSet("select zhi from xitxxb where mingc='���˼�鸶����Ϣ' 	and beiz='ʹ��'");
			if(rs.next()){
				value=rs.getString("zhi");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		if (value.equals("��")){
			return true;
		}else{
			return false;
		}
	}
	private boolean shiFouGengXin(String tableId, String tableName) {
		if (!checkFukInfo()){
			return true;
		}
		
		String sql = "select SHOUKDW,KAIHYH,ZHANGH,FAPBH from " + tableName	+ " where id=" + tableId;
		JDBCcon con = new JDBCcon();
		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				if(MainGlobal.getXitxx_item("����", "�Ƿ�ֻ�ж��տλ�Ƿ�Ϊ��", "0", "��").equals("��") && rs.getString("SHOUKDW") != null){
					return true;
				}
				
				if (rs.getString("SHOUKDW") != null	&& rs.getString("KAIHYH") != null
						&& rs.getString("ZHANGH") != null && rs.getString("FAPBH") != null) {

					return true;
				}
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return false;
	}
	//����
	private void gengXin(String tableId, String tableName, long renyxxb_id) {

		Date date = new Date();
		JDBCcon con = new JDBCcon();
		try {
			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			String datestr = fm.format(date);
			String sql = "update " + tableName + " set ruzrq=to_date('"
					+ datestr + "','yyyy-MM-dd'),ruzry="
					+ " (select quanc from renyxxb where id=" + renyxxb_id
					+ ") where id=" + tableId;

			int res = con.getUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

	}

	private long getYunfbId(String MkTableName, String YfTableName, long MkId) {
		// TODO �Զ����ɷ������
		JDBCcon con = new JDBCcon();
		long Yfid = 0;
		try {

			String sql = "select id from " + YfTableName + " where "
					+ MkTableName + "_id=" + MkId + "";

			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				Yfid = rs.getLong("id");
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return Yfid;
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