package com.zhiren.dc.tablesh;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Liucdzcl;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:tzf
 * ʱ��:2010-01-17
 * ����:������������������д
 */
public class Tableshyj extends BasePage implements PageValidateListener {

	private String My_opinion;

	private String Histry_opinion;

	private String biaos; // ����ʶΪokʱ��ǰ̨ҳ�������ɺ󣬼��رմ���

	private String liuczt;

	private String gengXinTiShi;// ������Ϣ�����������ܸ���ʱ����ʾ�û�������(���ܸ��µĽ��㵥���)

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
		((Visit) getPage().getVisit()).setString2("");		//dongzlx �������ͣ��ύ�����ˣ�
		((Visit) getPage().getVisit()).setString3("");		//liuclb ������𣨽��㡢��ͬ��
		
		this.liuczt = ((Visit) getPage().getVisit()).getDropDownBean2().getValue();
		this.My_opinion="";
		
		this.menuId=cycle.getRequestContext().getRequest().getParameter("menuId");
		
		String liucclsb=((Visit) getPage().getVisit()).getLiucclsb().toString();
		
		this.TableNameIdStr=liucclsb.substring(0, liucclsb.indexOf("+"));
		this.Histry_opinion=liucclsb.substring(liucclsb.indexOf("+")+1);
	}

	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		this.setBiaos("");
		this.setGengXinTiShi("");
	}
 
	private void active(){
		Visit visit=(Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
//		�ж϶�������   ��������ɵĹ��� ���㻹�Ǻ�ͬ�����   �ύ���ǻ���
		this.dongzlx(con);
		List TableName=new ArrayList();
		List TableID=new ArrayList();
		
		Liucdzcl.TableNameIdList(TableName, TableID, this.TableNameIdStr);
		
		if(TableName.size()==TableID.size()){
			for(int i=0;i<TableName.size();i++){
				Object objName=TableName.get(i);
				Object objID=TableID.get(i);
				
				 //�������ύ
					if(objName instanceof List){  //�Ƕ��ű�
						List listName=(List)objName;
						List listID=(List)objID;
						for(int j=0;j<listName.size();j++){
							String name=(String)listName.get(j);
							long id=Long.valueOf((String)listID.get(j)).longValue();
							if(visit.getString2().equals("�ύ")){
								Liuc.tij(con, name, id, ((Visit) getPage().getVisit())
										.getRenyID(), this.My_opinion);
							}else if(visit.getString2().equals("����")){

								Liuc.huit(con, name, id, ((Visit) getPage().getVisit())
										.getRenyID(), this.My_opinion);
							}
						}
					}else{
						if(visit.getString2().equals("�ύ")){
							Liuc.tij(con, objName.toString(), Long.valueOf(objID.toString()).longValue(), ((Visit) getPage().getVisit())
									.getRenyID(), this.My_opinion);
						}else if(visit.getString2().equals("����")){
							Liuc.huit(con, objName.toString(), Long.valueOf(objID.toString()).longValue(), ((Visit) getPage().getVisit())
									.getRenyID(), this.My_opinion);
						}
					}
			}//for
		}//if
		con.commit();
		con.Close();
	}
	
	public void Submit(IRequestCycle cycle) {
		active();
		this.biaos = "ok";
	}

	//�������� ���ж����ύ  ���ǻ���    �ǽ��㻹�Ǻ�ͬ �����
	private void dongzlx(JDBCcon con){
		StringBuffer bf=new StringBuffer();
		bf.append(" select distinct  liucdzb.mingc,liucztb.liucb_id leib  from liucdzjsb,liucdzb,liucztb,liuclbb,leibztb ");
		bf.append(" where liucdzjsb.liucdzb_id=liucdzb.id ");
		bf.append(" and liucztb.leibztb_id=leibztb.id ");
		bf.append(" and liucdzb.liucztqqid=liucztb.id");
		bf.append(" and leibztb.liuclbb_id=liuclbb.id");
		bf.append(" and liucdzb.id=").append(this.menuId);
		bf.append(" and liucdzjsb.liucjsb_id in");
		bf.append(" (select liucjsb_id from renyjsb where renyxxb_id=").append(((Visit) getPage().getVisit()).getRenyID()).append(")");
		
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
			
		}
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
}
