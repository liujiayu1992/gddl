package com.zhiren.jt.jiesgl.jieslc;
 
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Liucdzcl;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.menu.Menu;
import com.zhiren.common.ext.menu.TextItem;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jieslcext extends BasePage implements PageValidateListener {
	private String msg = "";
	List List_TableName=null;			//Ҫ���в����ı�
	List List_TableId=null;		
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	protected void initialize() {
		msg = "";
		List List_TableName=null;			//Ҫ���в����ı�
		List List_TableId=null;	
		MenuId="";
		OpenWindow="";
	}
	

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	
	private String ShenHeYJChange;
	
	
	public String getShenHeYJChange() {
		return ShenHeYJChange;
	}

	public void setShenHeYJChange(String shenHeYJChange) {
		ShenHeYJChange = shenHeYJChange;
	}
	
	private String Histry_opinion;
	
	public String getHistry_opinion() {
		return Histry_opinion;
	}

	public void setHistry_opinion(String histry_opinion) {
		Histry_opinion = histry_opinion;
	}
	
	private String My_opinion;
	
	
	public String getMy_opinion() {
		return My_opinion;
	}

	public void setMy_opinion(String my_opinion) {
		My_opinion = my_opinion;
	}

	private String MenuId;
	

	public String getMenuId() {
		return MenuId;
	}

	public void setMenuId(String menuId) {
		MenuId = menuId;
	}
	
	
	private String OpenWindow;
	

	public String getOpenWindow() {
		
		return OpenWindow;
	}

	public void setOpenWindow(String openWindow) {
		
		OpenWindow = openWindow;
		
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}


	
	private boolean DzclButton=false;
	
	public void DzclButton(IRequestCycle cycle){
	
		DzclButton=true;
	}

	public void submit(IRequestCycle cycle) {

		
		if(DzclButton){
			DzclButton=false;
		
			this.DzclButtonCF();
			
			getSelectData();
		}else{
			getSelectData();
		}
		
		
		
	}
	private void DzclButtonCF(){
		
//		��¼���㵥id
		Liucdzcl.idStrSource=this.Change;
//		����TalbeNameList��TableIdList �Ա�����ʹ��
		this.Dongzcl();
//		���ú���Ķ�������������Ҫ������ҳ��
		this.OpenWindow=Liucdzcl.Dongzcl( this.MenuId,List_TableName,List_TableId );
//		�ѱ����ͱ�id��list�������ַ���
//		���磺jiesb,3001;jiesyfb,3002;&jiesb,3003;jiesyfb,3004;&	
		String TableNameIdStr=Liucdzcl.TableNameIdStr(List_TableName, List_TableId);
		
		//((Visit) getPage().getVisit()).setString14(this.MenuId);
		
		((Visit) getPage().getVisit()).setLiucclsb(new StringBuffer(TableNameIdStr+"+"+this.Histry_opinion));
	}
	
	private long getYunfbId(String MkTableName,String YfTableName,String MkId) {
		// TODO �Զ����ɷ������
		JDBCcon con=new JDBCcon();
		long Yfid=0;
		try{
			
			String sql="select id from "+YfTableName+" where "+MkTableName+"_id="+MkId+"";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Yfid=rs.getLong("id");
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Yfid;
	}
	
	private void Huit() {
		// TODO �Զ����ɷ������
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		if(!(this.getChange().equals("")||this.getChange()==null)){
			
			String change[]=this.getChange().split(";");
			
			for(int i=0;i<change.length;i++){
				
				if(change[i] == null || "".equals(change[i])) {
					
					continue;
				}
				String record[] = change[i].split(",");
				
				if(record[9].equals("��Ʊ����")){
					
					if(((Visit) getPage().getVisit()).isDCUser()){
						
						Liuc.huit("jiesb", Long.parseLong(record[0]), renyxxb_id, record[12]);
						Liuc.huit("jiesyfb", getYunfbId("jiesb","jiesyfb",record[0]), renyxxb_id, record[12]);
						
					}else{
						
						if(record[10].equals("���۽��㵥")){
							
							Liuc.huit("diancjsmkb", Long.parseLong(record[0]), renyxxb_id, record[12]);
							Liuc.huit("diancjsyfb", getYunfbId("diancjsmkb","diancjsyfb",record[0]), renyxxb_id, record[12]);
						
						}else if(record[10].equals("�ɹ����㵥")){
							
							Liuc.huit("kuangfjsmkb",Long.parseLong(record[0]), renyxxb_id, record[12]);
							Liuc.huit("kuangfjsyfb", getYunfbId("kuangfjsmkb","kuangfjsyfb",record[0]), renyxxb_id, record[12]);
						}
					}
					
				}else if(record[9].equals("ú�����")){
					
					if(((Visit) getPage().getVisit()).isDCUser()){
						
						Liuc.huit("jiesb", Long.parseLong(record[0]), renyxxb_id, record[12]);
					}else{
						
						if(record[10].equals("���۽��㵥")){
							
							Liuc.huit("diancjsmkb", Long.parseLong(record[0]), renyxxb_id, record[12]);
						
						}else if(record[10].equals("�ɹ����㵥")){
							
							Liuc.huit("kuangfjsmkb",Long.parseLong(record[0]), renyxxb_id, record[12]);
						}
					}
					
				}else if(record[9].equals("�˷ѽ���")){
					
					if(((Visit) getPage().getVisit()).isDCUser()){
						
						Liuc.huit("jiesyfb", Long.parseLong(record[0]), renyxxb_id, record[12]);
					}else{
						
						if(record[10].equals("���۽��㵥")){
							
							Liuc.huit("diancjsyfb", Long.parseLong(record[0]), renyxxb_id, record[12]);
						
						}else if(record[10].equals("�ɹ����㵥")){
							
							Liuc.huit("kuangfjsyfb",Long.parseLong(record[0]), renyxxb_id, record[12]);
						}
					}
				}
			}
		}
	}
	
	public void Tij(){
		
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		
		if(!(this.getChange().equals("")||this.getChange()==null)){
			
			
			String change[]=this.getChange().split(";");
			
			for(int i=0;i<change.length;i++){
				
				if(change[i] == null || "".equals(change[i])) {
					
					continue;
				}
				String record[] = change[i].split(",");
				
				if(record[9].equals("��Ʊ����")){
					
					if(((Visit) getPage().getVisit()).isDCUser()){
						
						Liuc.tij("jiesb", Long.parseLong(record[0]), renyxxb_id, record[12]);
						Liuc.tij("jiesyfb", getYunfbId("diancjsyfb","jiesyfb",record[0]), renyxxb_id, record[12]);
					}else{
						
						if(record[10].equals("���۽��㵥")){
							
							
							Liuc.tij("diancjsmkb", Long.parseLong(record[0]), renyxxb_id, record[12]);
							Liuc.tij("diancjsyfb", getYunfbId("diancjsmkb","diancjsyfb",record[0]), renyxxb_id, record[12]);
										
						}else if(record[10].equals("�ɹ����㵥")){
							
							Liuc.tij("kuangfjsmkb",Long.parseLong(record[0]), renyxxb_id, record[12]);
							Liuc.tij("kuangfjsyfb", getYunfbId("kuangfjsmkb","kuangfjsyfb",record[0]), renyxxb_id, record[12]);
						}
					}
					
				}else if(record[9].equals("ú�����")){
					
					if(((Visit) getPage().getVisit()).isDCUser()){
						
						Liuc.tij("jiesb", Long.parseLong(record[0]), renyxxb_id, record[12]);
					}else{
						
						if(record[10].equals("���۽��㵥")){
							
							Liuc.tij("diancjsmkb", Long.parseLong(record[0]), renyxxb_id, record[12]);
						
						}else if(record[10].equals("�ɹ����㵥")){
							
							Liuc.tij("kuangfjsmkb",Long.parseLong(record[0]), renyxxb_id, record[12]);
						}
					}
					
				}else if(record[9].equals("�˷ѽ���")){
					
					if(((Visit) getPage().getVisit()).isDCUser()){
						
						Liuc.tij("jiesyfb", Long.parseLong(record[0]), renyxxb_id, record[12]);
					}else{
						
						if(record[10].equals("���۽��㵥")){
							
							Liuc.tij("diancjsyfb", Long.parseLong(record[0]), renyxxb_id, record[12]);
						
						}else if(record[10].equals("�ɹ����㵥")){
							
							Liuc.tij("kuangfjsyfb",Long.parseLong(record[0]), renyxxb_id, record[12]);
						}
					}
				}
			}
		}
	}

	public void getSelectData() {

		String sql = "";
		JDBCcon con = new JDBCcon();
		String leib = "����";
		int rsl_rows=0;
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		
		String table_mk = "";
		String table_yf = "";
		String guanlb = "";	//������
		String jiesdlx = "";//���㵥����
		
		String where="";
		if(((Visit) getPage().getVisit()).isDCUser()){
			
			table_mk = "jiesb";
			table_yf = "jiesyfb";
			guanlb = "diancjsmkb_id";
			jiesdlx = "���۽��㵥";
			
			if(Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString1())){
//				�ж��Ƿ�Ϊ�ֹ�˾�ɹ�����
				table_mk = "kuangfjsmkb";
				table_yf = "kuangfjsyfb";
				guanlb = "kuangfjsmkb_id";
				jiesdlx = "�ֹ�˾�ɹ����㵥";
			}
			
//			�糧��
			where=getTreeid();
			
			if (getWeizSelectValue().getId() ==1) {// �Լ�������
				
					sql=" select * from ("
						+ " (select dm.id,dm.bianm,dm.gongysmc,dm.xianshr,dm.jiessl,dm.jiesrq,"
						+ "          dm.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id,"
						+ "          decode(dm.jieslx,"+Locale.liangpjs_feiylbb_id+",'��Ʊ����','ú�����') as leib,nvl('"+jiesdlx+"','') as zhongl,"
						+ "			 nvl('�鿴','') as link,nvl('','') as yij,nvl('','') as histryyj"		
						+ "          from "+table_mk+" dm,liucztb lz,leibztb "
						+ "          where dm.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dm.fuid=0 and dm.diancxxb_id in ("+where+") and dm.id in ("
						+ Liuc.getWodrws(table_mk, renyxxb_id, leib)
						+ "))"
						+ "          union"
						+ " (select  decode(dy.jieslx,"+Locale.liangpjs_feiylbb_id+",dy."+guanlb+",dy.id) as id,dy.bianm,dy.gongysmc,dy.xianshr,\n"
						+ " 	     to_number(decode(dy.jieslx,"+Locale.liangpjs_feiylbb_id+",gettablecol('"+table_mk+"','jiessl','id',dy."+guanlb+"),dy.jiessl)) as jiessl,dy.jiesrq,"
						+ "          dy.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
						+ "          decode(dy.jieslx,"+Locale.liangpjs_feiylbb_id+",'��Ʊ����','�˷ѽ���') as leib,nvl('"+jiesdlx+"','') as zhongl,"
						+ "			 nvl('�鿴','') as link,nvl('','') as yij,nvl('','') as histryyj"
						+ "          from "+table_yf+" dy,liucztb lz,leibztb "
						+ "          where dy.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dy.fuid=0 and dy.diancxxb_id in ("+where+") and dy.id in ("
						+ Liuc.getWodrws(table_yf, renyxxb_id, leib)
						+ ")))";
						
	
			}else {
				
					sql=" select * from ("
						+ " (select dm.id,dm.bianm,dm.gongysmc,dm.xianshr,dm.jiessl,dm.jiesrq,"
						+ "         dm.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id,"
						+ "         decode(dm.jieslx,"+Locale.liangpjs_feiylbb_id+",'��Ʊ����','ú�����') as leib,nvl('"+jiesdlx+"','') as zhongl,"
						+ "			nvl('�鿴','') as link,nvl('','') as yij,nvl('','') as histryyj "
						+ "         from "+table_mk+" dm,liucztb lz,leibztb "
						+ "         where dm.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dm.fuid=0 and dm.diancxxb_id in ("+where+") and dm.id in ("
						+ Liuc.getLiuczs(table_mk, renyxxb_id, leib)
						+ "))"
						+ "          union"
						+ " (select decode(dy.jieslx,"+Locale.liangpjs_feiylbb_id+",dy."+guanlb+",dy.id) as id,dy.bianm,dy.gongysmc,dy.xianshr,\n"
						+ "			to_number(decode(dy.jieslx,"+Locale.liangpjs_feiylbb_id+",gettablecol('"+table_mk+"','jiessl','id',dy."+guanlb+"),dy.jiessl)) as jiessl,dy.jiesrq,"
						+ "         dy.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
						+ "         decode(dy.jieslx,"+Locale.liangpjs_feiylbb_id+",'��Ʊ����','�˷ѽ���') as leib,nvl('"+jiesdlx+"','') as zhongl,"
						+ "			nvl('�鿴','') as link,nvl('','') as yij,nvl('','') as histryyj"
						+ "         from jiesyfb dy,liucztb lz,leibztb "
						+ "         where dy.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dy.fuid=0 and dy.diancxxb_id in ("+where+") and dy.id in ("
						+ Liuc.getLiuczs("jiesyfb", renyxxb_id, leib)
						+ ")))";
			}
			
			
			
		}else{
//			��˾��
			
			where="(select id\n" + 
				" from(\n" + 
				" select id from diancxxb\n" + 
				" start with fuid="+getTreeid()+"\n" + 
				" connect by fuid=prior id\n" + 
				" )\n" + 
				" union\n" + 
				" select id\n" + 
				" from diancxxb\n" + 
				" where id="+getTreeid()+")";
			
			

			String zhonglwhere="";
			
			if(getJieslxValue().getId()==0 || getJieslxValue().getId()!=3){
				//���۽��㵥
				if(getJieslxValue().getId()!=0){
					
					zhonglwhere=" where zhongl='"+getJieslxValue().getValue()+"'";
				}
				
				if (getWeizSelectValue().getId() ==1) {// �Լ�������
					
							sql=" select * from ("
								+ " (select dm.id,dm.bianm,dm.gongysmc,dm.xianshr,dm.jiessl,dm.jiesrq,"
								+ "         dm.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id,"
								+ "         decode(dm.jieslx,1,'��Ʊ����','ú�����') as leib,decode(1,1,'���۽��㵥') as zhongl,"
								+ "			decode(1,1,'�鿴') as link,decode(1,1,'') as yij,decode(1,1,'') as histryyj"		
								+ "         from diancjsmkb dm,liucztb lz,leibztb "
								+ "         where dm.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dm.fuid=0 and dm.diancxxb_id in ("+where+") and dm.id in ("
								+ Liuc.getWodrws("diancjsmkb", renyxxb_id, leib)
								+ "))"
								+ "          union"
								+ " (select  decode(dy.jieslx,1,dy.diancjsmkb_id,dy.id) as id,dy.bianm,dy.gongysmc,dy.xianshr,\n"
								+ "			 to_number(decode(dy.jieslx,1,gettablecol('diancjsmkb','jiessl','id',dy.diancjsmkb_id),dy.jiessl)) as jiessl,dy.jiesrq,"
								+ "          dy.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
								+ "          decode(dy.jieslx,1,'��Ʊ����','�˷ѽ���') as leib,decode(1,1,'���۽��㵥') as zhongl,"
								+ "			 decode(1,1,'�鿴') as link,decode(1,1,'') as yij,decode(1,1,'') as histryyj"
								+ "          from diancjsyfb dy,liucztb lz,leibztb "
								+ "          where dy.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dy.fuid=0 and dy.diancxxb_id in ("+where+") and dy.id in ("
								+ Liuc.getWodrws("diancjsyfb", renyxxb_id, leib)
								+ "))"
								+ "			union"
								+ " (select km.id,km.bianm,km.gongysmc,km.xianshr,km.jiessl,km.jiesrq,"
								+ " 		km.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
								+ " 	    decode(km.jieslx,1,'��Ʊ����','ú�����') as leib,decode(1,1,'�ɹ����㵥') as zhongl,"
								+ "			decode(1,1,'�鿴') as link,decode(1,1,'') as yij,decode(1,1,'') as histryyj"
								+ " 		from kuangfjsmkb km,liucztb lz,leibztb "
								+ " 		where km.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and km.fuid=0 and km.diancxxb_id in ("+where+") and km.id in ("
								+ Liuc.getWodrws("kuangfjsmkb", renyxxb_id, leib)
								+ "))"
								+ " 		union"
								+ " (select ky.kuangfjsmkb_id as id,ky.bianm,ky.gongysmc,ky.xianshr,\n"
								+ "			to_number(decode(ky.jieslx,1,gettablecol('kuangfjsmkb','jiessl','id',ky.kuangfjsmkb_id),ky.jiessl)) as jiessl,ky.jiesrq,"
								+ " 		ky.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
								+ " 	    decode(ky.jieslx,1,'��Ʊ����','�˷ѽ���') as leib,decode(1,1,'�ɹ����㵥') as zhongl,"
								+ "			decode(1,1,'�鿴') as link,decode(1,1,'') as yij,decode(1,1,'') as histryyj"
								+ " 	    from kuangfjsyfb ky,liucztb lz,leibztb "
								+ " 		where ky.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and ky.fuid=0 and ky.diancxxb_id in ("+where+") and ky.id in ("
								+ Liuc.getWodrws("kuangfjsyfb", renyxxb_id, leib) + "))) "+zhonglwhere;
				
				}else {
					
							sql=" select * from ("
								+ " (select dm.id,dm.bianm,dm.gongysmc,dm.xianshr,dm.jiessl,dm.jiesrq,"
								+ "         dm.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id,"
								+ "         decode(dm.jieslx,1,'��Ʊ����','ú�����') as leib,decode(1,1,'���۽��㵥') as zhongl,"
								+ "			decode(1,1,'�鿴') as link,decode(1,1,'') as yij,decode(1,1,'') as histryyj"
								+ "         from diancjsmkb dm,liucztb lz,leibztb "
								+ "         where dm.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dm.fuid=0 and dm.diancxxb_id in ("+where+") and dm.id in ("
								+ Liuc.getLiuczs("diancjsmkb", renyxxb_id, leib)
								+ "))"
								+ "          union"
								+ " (select decode(dy.jieslx,1,dy.diancjsmkb_id,dy.id) as id,dy.bianm,dy.gongysmc,dy.xianshr,\n"
								+ "			to_number(decode(dy.jieslx,1,gettablecol('diancjsmkb','jiessl','id',dy.diancjsmkb_id),dy.jiessl)) as jiessl,dy.jiesrq,"
								+ "         dy.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
								+ "         decode(dy.jieslx,1,'��Ʊ����','�˷ѽ���') as leib,decode(1,1,'���۽��㵥') as zhongl,"
								+ "			decode(1,1,'�鿴') as link,decode(1,1,'') as yij,decode(1,1,'') as histryyj"
								+ "         from diancjsyfb dy,liucztb lz,leibztb "
								+ "         where dy.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dy.fuid=0 and dy.diancxxb_id in ("+where+") and dy.id in ("
								+ Liuc.getLiuczs("diancjsyfb", renyxxb_id, leib)
								+ "))"
								+ "			union"
								+ " (select km.id,km.bianm,km.gongysmc,km.xianshr,km.jiessl,km.jiesrq,"
								+ " 		km.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
								+ " 	    decode(km.jieslx,1,'��Ʊ����','ú�����') as leib,decode(1,1,'�ɹ����㵥') as zhongl,"
								+ "			decode(1,1,'�鿴') as link,decode(1,1,'') as yij,decode(1,1,'') as histryyj"
								+ " 		from kuangfjsmkb km,liucztb lz,leibztb "
								+ " 		where km.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and km.fuid=0 and km.diancxxb_id in ("+where+") and km.id in ("
								+ Liuc.getLiuczs("kuangfjsmkb", renyxxb_id, leib)
								+ "))"
								+ " 		union"
								+ " (select decode(ky.jieslx,1,ky.kuangfjsmkb_id,ky.id) as id,ky.bianm,ky.gongysmc,ky.xianshr,\n"
								+ "			to_number(decode(ky.jieslx,1,gettablecol('kuangfjsmkb','jiessl','id',ky.kuangfjsmkb_id),ky.jiessl)) as jiessl,ky.jiesrq,"
								+ " 		ky.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
								+ " 	    decode(ky.jieslx,1,'��Ʊ����','�˷ѽ���') as leib,decode(1,1,'�ɹ����㵥') as zhongl,"
								+ "			decode(1,1,'�鿴') as link,decode(1,1,'') as yij,decode(1,1,'') as histryyj"
								+ " 	    from kuangfjsyfb ky,liucztb lz,leibztb "
								+ " 		where ky.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and ky.fuid=0 and ky.diancxxb_id in ("+where+") and ky.id in ("
								+ Liuc.getLiuczs("kuangfjsyfb", renyxxb_id, leib) + "))) "+zhonglwhere;
				}
			}else if(getJieslxValue().getId()==3){
				if(getWeizSelectValue().getId() ==1){
				 sql=

					 "select distinct y.bianm,g.mingc as fahdw, m.mingc as meikdw,j.jies ,a.jiesrl as jiesrl,b.std as liuf ,lb.mingc as zhuangt,\n" +
					 "'����ָ������' as jieslb,decode(1,1,'�鿴') as link from yansbhb  y,gongysb g ,meikxxb m, gongysmkglb glb,\n" + 
					 "(select j.gongf as jiesrl from jieszbsjb j,zhibb where j.zhibb_id=z.id and z.bianm='"+Locale.Qnetar_zhibb+"')a,(select j.gongf as std from jieszbsjb j,zhibb z where j.zhibb_id=z.id and z.bianm='"+Locale.Std_zhibb+"')b,\n" + 
					 "jieszbsjb j, liucztb l,leibztb lb\n" + 
					 "where g.id=glb.gongysb_id and m.id=glb.meikxxb_id and y.bianm=j.yansbhb_id and l.id=y.liucztbid and lb.id=l.leibztb_id and y.id in ("
					 + Liuc.getWodrws("yansbhb", renyxxb_id, leib)+")";
				 
				}else {
					 sql= 
					 "select distinct y.bianm,g.mingc as fahdw, m.mingc as meikdw,j.jies ,a.jiesrl as jiesrl,b.std as liuf ,lb.mingc as zhuangt,\n" +
					 "'����ָ������' as jieslb,decode(1,1,'�鿴') as link from yansbhb  y,gongysb g ,meikxxb m, gongysmkglb glb,\n" + 
					 "(select j.gongf as jiesrl from jieszbsjb j,zhibb z where j.zhibb_id=z.id and z.bianm='"+Locale.Qnetar_zhibb+"')a,(select j.gongf as std from jieszbsjb j,zhibb z where j.zhibb_id=z.id and z.bianm='"+Locale.Std_zhibb+"')b,\n" + 
					 "jieszbsjb j, liucztb l,leibztb lb\n" + 
					 "where g.id=glb.gongysb_id and m.id=glb.meikxxb_id and y.bianm=j.yansbhb_id and l.id=y.liucztbid and lb.id=l.leibztb_id and y.id in ("
					 +Liuc.getLiuczs("yansbhb", renyxxb_id, leib)+")";
				}
			}
		}
//		System.out.println(sql);
		ResultSetList rsl=con.getResultSetList(sql);
		rsl_rows=rsl.getRows();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);//�趨��¼����Ӧ�ı�
//		egu.setTableName("yunslb");//Ҫ����ı���
		if(getJieslxValue().getId()==0 || getJieslxValue().getId()!=3){
			
			egu.getColumn("bianm").setHeader("�������");
			egu.getColumn("gongysmc").setHeader("������λ");
			egu.getColumn("xianshr").setHeader("�ջ���λ");
			egu.getColumn("jiessl").setHeader("������(��)");
			egu.getColumn("jiesrq").setHeader("��������");
			egu.getColumn("zhuangt").setHeader("״̬");
			egu.getColumn("ZHONGL").setHeader("����");
			egu.getColumn("leib").setHeader("�������");
			egu.getColumn("link").setHeader("");
			egu.getColumn("bianm").setWidth(170);
			egu.getColumn("gongysmc").setWidth(120);
			String str="";
			
			if(((Visit) getPage().getVisit()).isDCUser()){
				
				str="  var url = 'http://'+document.location.host+document.location.pathname;" +
				"var end = url.indexOf(';');" +
				"url = url.substring(0,end);" +
				"url = url + '?service=page/' + 'Jiesdcz&lx='+(record.data['ZHONGL']=='���۽��㵥'?'dianc':'dianc')+','+record.data['BIANM'];";
			}else{
				
				str="  var url = 'http://'+document.location.host+document.location.pathname;" +
				"var end = url.indexOf(';');" +
				"url = url.substring(0,end);" +
				"url = url + '?service=page/' + 'Jiesdcz&lx='+(record.data['ZHONGL']=='���۽��㵥'?'changf':'kuangf')+','+record.data['BIANM'];";
			}
			
			egu.getColumn("link").setRenderer(
			"function(value,p,record){" +str+
			"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>�鿴</a>\"}");
			egu.getColumn("id").setHidden(true);
			egu.getColumn("liucztb_id").setHidden(true);
			egu.getColumn("liucb_id").setHidden(true);
			egu.getColumn("yij").setHidden(true);
			egu.getColumn("histryyj").setHidden(true);
			List tmp= new ArrayList();
	
			for(int i=0;i<rsl_rows;i++){
				String strtmp="";
				tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,0)));
				for(int j=0;j<tmp.size();j++){
					strtmp+=((Yijbean)tmp.get(j)).getXitts()+"\\n  "+(((Yijbean)tmp.get(j)).getYij()==null?"":((Yijbean)tmp.get(j)).getYij())+"\\n ";
				}
				egu.setDataValue(i, 13, "������ "+egu.getDataValue(i,1)+":\\n "+strtmp);
			}
	
				egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
				egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
				//egu.getColumn("liucztb_id").editor = null;//�ø��ֶβ��ɱ༭
				//egu.setGridType(ExtGridUtil.Gridstyle_Edit);//���������ɱ༭
				egu.addPaging(18);
		}else{
			
			egu.getColumn("bianm").setHeader("���ձ��");
			egu.getColumn("fahdw").setHeader("������λ");
			egu.getColumn("meikdw").setHeader("ú��λ");
			egu.getColumn("jies").setHeader("������(��)");
			egu.getColumn("jiesrl").setHeader("��������");
			egu.getColumn("liuf").setHeader("������");
			egu.getColumn("zhuangt").setHeader("״̬");
	
			egu.getColumn("jieslb").setHeader("�������");		
			egu.getColumn("link").setHeader("");	
			egu.setWidth(1000);
			egu.getColumn("bianm").setWidth(170);
			egu.getColumn("fahdw").setWidth(120);
			String str="  var url = 'http://'+document.location.host+document.location.pathname;" +
				"var end = url.indexOf(';');" +
				"url = url.substring(0,end);" +
				"url = url + '?service=page/' + 'Jieszbsjb&yansbm='+record.data['BIANM']+'';";
	
			egu.getColumn("link").setRenderer(
					"function(value,p,record){" +str+
					"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>�鿴</a>\"}");
		}
		
		
if(!((Visit) this.getPage().getVisit()).isDCUser()){
			
			egu.addTbarText("��������:");
			ComboBox JieslxDropDown = new ComboBox();
			JieslxDropDown.setId("JieslxDrop");
			JieslxDropDown.setWidth(100);
			JieslxDropDown.setLazyRender(true);
			JieslxDropDown.setTransform("JieslxDropDown");
			egu.addToolbarItem(JieslxDropDown.getScript());
//			//��		
			egu.addTbarText("-");
		}
		
		if(getJieslxValue().getId()!=3){
			egu.addTbarText("��λ����:");
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)getPage().getVisit()).getDiancxxb_id(),getTreeid());
			
			setTree(etu);
			egu.addTbarTreeBtn("diancTree");
		}
		
//		
		egu.addTbarText("-");
		egu.addTbarText("����״̬");
		ComboBox WeizSelect = new ComboBox();
		WeizSelect.setId("Weizx");
		WeizSelect.setWidth(80);
		WeizSelect.setLazyRender(true);
		WeizSelect.setTransform("WeizSelectx");
		egu.addToolbarItem(WeizSelect.getScript());
		
		if(!((Visit) this.getPage().getVisit()).isDCUser()){
			
			egu.addOtherScript("JieslxDrop.on('select',function(){ document.forms[0].submit();});");
		}
		egu.addOtherScript("Weizx.on('select',function(){ document.forms[0].submit();});");
		egu.addOtherScript("gridDiv_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");
		
		egu.addTbarText("-");
		
		if(((Visit) getPage().getVisit()).getboolean4()){
			
	
			Menu MuWdqx=new Menu();
			
			sql="select distinct\n" +
				"       liucdzb.id,\n" + 
				"       decode(liucdzb.mingc,'�ύ','��<'||leibztb.mingc||'>'||liucdzb.mingc||'<'||GetLiuc_Hjzt(liucdzb.id)||'>',\n" + 
				"             '����','��<'||leibztb.mingc||'>'||liucdzb.mingc||'<'||GetLiuc_Hjzt(liucdzb.id)||'>') as mingc,\n" + 
				"             'onMenuItemClick' as dongz\n" + 
				"       from liucdzjsb,liucdzb,liucztb,liuclbb,leibztb\n" + 
				"       where liucdzjsb.liucdzb_id=liucdzb.id\n" + 
				"             and liucdzb.liucztqqid=liucztb.id\n" + 
				"             and liucztb.leibztb_id=leibztb.id\n" + 
				"             and leibztb.liuclbb_id=liuclbb.id\n" + 
				"             and liuclbb.mingc='����'\n" + 
				"             and liucdzjsb.liucjsb_id in\n" + 
				"             (select liucjsb_id from renyjsb\n" + 
				"                     where renyxxb_id="+renyxxb_id+")";
			List List_Wdqx=new ArrayList();
			rsl=con.getResultSetList(sql);
			while(rsl.next()){
				
				List_Wdqx.add(new TextItem(rsl.getString("id"),rsl.getString("mingc"),rsl.getString("dongz")));
			}
			MuWdqx.setItems(List_Wdqx);
			egu.addToolbarItem("{text:'�ҵ�Ȩ��',menu:"+MuWdqx.getScript()+"}");
			
			
			
			
			egu.addOtherScript("\nfunction onMenuItemClick(item){		\n");
			egu.addOtherScript("	var rc = gridDiv_grid.getSelectionModel().getSelections();		\n");
			egu.addOtherScript("	var value='';		\n");
			
		//	egu.addOtherScript("    if(item.id==2575459 && rc.length>1){Ext.MessageBox.alert('��ʾ��Ϣ','�˲���ֻ��ѡ��һ�Ž��㵥!');	return; \n}");
			
			egu.addOtherScript("var strmyp=''; document.all.Histry_opinion.value=''; document.all.My_opinion.value=''; document.all.ShenHeYJChange.value='';");
			
			egu.addOtherScript("	if(rc.length>0){ 	  \n\n");
			egu.addOtherScript("		for(var i=0;i<rc.length;i++){		\n\n");
			egu.addOtherScript("			value+=rc[i].get('ID')+','+rc[i].get('LEIB')+','+rc[i].get('ZHONGL')+';';		\n");
			egu.addOtherScript("            document.all.ShenHeYJChange.value+= rc[i].get('ID')+','+rc[i].get('BIANM')+','+rc[i].get('GONGYSMC')+','+rc[i].get('XIANSHR')+','+rc[i].get('JIESSL')+','+rc[i].get('JIESRQ')+','+rc[i].get('LIUCZTB_ID')+','+rc[i].get('ZHUANGT')+','+rc[i].get('LIUCB_ID')+','+rc[i].get('LEIB')+','+rc[i].get('ZHONGL')+','+rc[i].get('LINK')+','+rc[i].get('YIJ')+','+rc[i].get('HISTRYYJ')+';';");
			egu.addOtherScript(" 	if(strmyp.substring(rc[i].get('YIJ'))>-1){ " );
			egu.addOtherScript(" 		if(strmyp==''){ strmyp=rc[i].get('YIJ');}else{ strmyp+=','+rc[i].get('YIJ');}}");
			egu.addOtherScript(" 	var strtmp=rc[i].get('HISTRYYJ');");
			egu.addOtherScript(" 	document.all.Histry_opinion.value+=strtmp+'\\n';} ");
			
			egu.addOtherScript("		document.all.My_opinion.value=strmyp;		\n");
			egu.addOtherScript("		document.getElementById('CHANGE').value=value;		\n");
			egu.addOtherScript("		document.getElementById('MenuId').value=item.id;		\n");
			egu.addOtherScript("		document.getElementById('DzclButton').click();		\n");
			egu.addOtherScript("	}else{		\n\n");
			egu.addOtherScript("		Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ�����ļ�¼!');	\n");
			egu.addOtherScript("	}		\n");
			egu.addOtherScript("}	\n");
			
		}
		
		
		if(!((Visit) getPage().getVisit()).getboolean4()){
			
			egu.addTbarText("-");
			egu.addToolbarItem("{"+new GridButton("��ʷ���","function(){var rc = gridDiv_grid.getSelectionModel().getSelections();" +
					"var val=''; if(rc.length>0){  "+
					" for(var i=0;i<rc.length;i++){	val+=rc[i].get('HISTRYYJ')+'\\n'; }"+
					" document.all.tab.value=val;" +
					" window_panel.setVisible(true);"+
					" }else{Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ�����ļ�¼!');}"+
					"}").getScript()+"}");
			
		}

		
//		//��Toolbar��combobox
		
		
		//��combobox���
		setExtGrid(egu);
		con.Close();
		
		
		
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
	
	// λ��
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "�ҵ�����"));
		list.add(new IDropDownBean(2, "������"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
	}
	
	//��������
	public IDropDownBean getJieslxValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getJieslxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setJieslxValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}

	public IPropertySelectionModel getJieslxModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getJieslxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setJieslxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getJieslxModels() {

		List list = new ArrayList();
		list.add(new IDropDownBean(0, "ȫ��"));
		list.add(new IDropDownBean(1, "���۽��㵥"));
		list.add(new IDropDownBean(2, "�ɹ����㵥"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));
	}
	
	public boolean isQuanxkz() {
		return ((Visit) getPage().getVisit()).getboolean4();
	}

	public void setQuanxkz(boolean value) {
		((Visit) getPage().getVisit()).setboolean4(value);
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
			visit.setInt1(-1);
			visit.setString1("");	//���ڼ�¼��jsdwid���������Ƿ�Ϊ�ֹ�˾�ɹ�����
			//3
			setJieslxModel(null);
			setJieslxValue(null);
			//2
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			getJieslxModels();
			getWeizSelectModel();
			visit.setboolean4(true);//�ҵ�����������
			visit.setboolean1(false);//λ��
			visit.setboolean2(false);//��������
			visit.setboolean3(false);//��λ
			
			if(cycle.getRequestContext().getParameter("jsdwid")!=null){
				
				visit.setString1(cycle.getRequestContext().getParameters("jsdwid")[0]);
			}
			
			getSelectData();
		}

		
		if (((Visit) getPage().getVisit()).getboolean1()
				|| ((Visit) getPage().getVisit()).getboolean2()
				|| ((Visit) getPage().getVisit()).getboolean3()) {// �����ͬλ�øı�
			// 1, λ��2, ��������3, ��λ
			if (((Visit) getPage().getVisit()).getboolean1() == true) {
				if (getWeizSelectValue().getId() == 1) {
					visit.setboolean4(true);
				} else {
					visit.setboolean4(false);
				}
			}
			
			visit.setboolean1(false);
			visit.setboolean2(false);
			visit.setboolean3(false);
			getSelectData();
		}
	}
	
	private String treeid="";
	public String getTreeid() {
		
		if(treeid.equals("")){
			
			treeid=String.valueOf(((Visit)getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		
		if(!this.treeid.equals(treeid)){
			
			((Visit)getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	
	public int getEditTableRow() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}
	
	public void setEditTableRow(int value){
		
		((Visit) this.getPage().getVisit()).setInt1(value);
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
	
//	public String getWunScript(){
//		
//		return "for(var i=0;i<rec.length;i++){"
//    				
//                +"    rec[i].set('YIJ',document.getElementById('My_opinion').value);" 	
//    			+" }";
//	}
	
	public String getWunScript(){
		
		return "for(var i=0;i<rec.length;i++){"
    				
                +"    rec[i].set('YIJ',document.getElementById('My_opinion').value);" 	
    			+" }";
	}
	public void JiesFormYijSubmit(IRequestCycle cycle){
		
		cycle.activate("Jieslc_Yijtx");
	}
	
	public void Shuaxin(IRequestCycle cycle){
	
		this.OpenWindow="";
		this.getSelectData();
	}
	
	
	private void Dongzcl() {
		// TODO �Զ����ɷ������
//		�߼����ú�������˵�����¼���Ĳ���������˵���ش��������ݣ�
//			1��ѡ��Ľ��㵥id��leib����Ʊ��ú��˷ѣ���zhongl�����۽��㵥���ɹ����㵥����Щ��Ϣֱ�ӹ�ϵ������ҳ��Ĳ���
//			2��liucdzb.id�����̶�����id���������id������֪��liucdz���õĶ�����ʲô
//		��ģ��Ҫ����"Liucdzcl"�ķ���������TableName,TableID,liucdzb.id
		
		String table_mk = "jiesb";
		String table_yf = "jiesyfb";
		String guanlb = "diancjsmkb";
		
		if(Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString1())){
//			�ж��Ƿ�Ϊ�ֹ�˾�ɹ�����
			table_mk = "kuangfjsmkb";
			table_yf = "kuangfjsyfb";
			guanlb = "kuangfjsmkb";
		}
		if(!this.getChange().equals("")){
			
			List_TableName=new ArrayList();			//Ҫ���в����ı�
			List_TableId=new ArrayList();				//Ҫ���в������id
			String tmp[]=getChange().split(";");			//����ѡ�����¼��id����洢
			String jiesxx="";	//����������Ϣ
			String jiesbId="";	//�����id
			String jieslb="";	//�������
			String jieszl="";	//�������ࣨ�������󷽣�
			
			for(int i=0;i<tmp.length;i++){
				
				jiesxx=tmp[i].toString();
//				�õ�����id�ͽ������
				jiesbId=jiesxx.substring(0,jiesxx.indexOf(","));
				jieslb=jiesxx.substring(jiesxx.indexOf(",")+1,jiesxx.lastIndexOf(","));
				jieszl=jiesxx.substring(jiesxx.lastIndexOf(",")+1);
				
				if(((Visit) getPage().getVisit()).isDCUser()){
//					����
					if(jieslb.equals(Locale.liangpjs_feiylbb)){
//						�������Ϊ����Ʊ����,Ҫ����jiesb��jiesyf�����������״̬
						List listName=new ArrayList();
//						��jiesb����List
						listName.add(table_mk);
						listName.add(table_yf);
						List_TableName.add(listName);
						
						List listID=new ArrayList();
						listID.add(jiesbId);
						listID.add(getYunfbId(guanlb,table_yf,jiesbId)+"");
//						��jiesb_id����List
						
						List_TableId.add(listID);
						
					}else if(jieslb.equals(Locale.meikjs_feiylbb)){
//						�������Ϊ��ú�����,Ҫ����jiesb������״̬
						
//						��jiesb����List
						List_TableName.add(table_mk);
//						��jiesb_id����List
						List_TableId.add(jiesbId);
					}else if(jieslb.equals(Locale.yunfjs_feiylbb)){
//						�������Ϊ���˷ѽ���,Ҫ����jiesyfb ������״̬
						
//						��jiesyfb����List
						List_TableName.add(table_yf);
//						��jiesbyfb_id����List
						List_TableId.add(jiesbId);
					}
				}else if(((Visit) getPage().getVisit()).isGSUser()
						||((Visit) getPage().getVisit()).isJTUser()){
//					�ֹ�˾�ͼ���
					
					if(jieslb.equals(Locale.liangpjs_feiylbb)){
//						�������Ϊ����Ʊ����,
//							�����������Ϊ���������㡱
//								Ҫ����diancjsmkb��diancjsyfb�����������״̬
//							�����������Ϊ���󷽽��㡱
//								Ҫ����kuangfjsmkb��kuangfjsyfb�����������״̬
						
						if(jieszl.equals("���۽��㵥")){
							
//							��diancjsmkb����List
							List_TableName.add("diancjsmkb");
//							��diancjsmkb_id����List
							List_TableId.add(jiesbId);
							
//							��diancjsyfb��diancjsyfb_id�ֱ��������List
							List_TableName.add("diancjsyfb");
							List_TableId.add(getYunfbId("diancjsmkb","diancjsyfb",jiesbId)+"");
							
						}else if(jieszl.equals("�ɹ����㵥")){
							
//							��kuangfjsmkb����List
							List_TableName.add("kuangfjsmkb");
//							��kuangfjsmkb_id����List
							List_TableId.add(jiesbId);
							
//							��kuangfjsyfb��kuangfjsyfb_id�ֱ��������List
							List_TableName.add("kuangfjsyfb");
							List_TableId.add(getYunfbId("kuangfjsmkb","kuangfjsyfb",jiesbId)+"");
						}
						
					}else if(jieslb.equals(Locale.meikjs_feiylbb)){
//						�������Ϊ��ú�����
//							�����������Ϊ���������㡱
//								Ҫ����diancjsmkb������״̬
//							�����������Ϊ���󷽽��㡱
//								Ҫ����kuangfjsmkb������״̬
						
						if(jieszl.equals("���۽��㵥")){

//							��diancjsmkb����List
							List_TableName.add("diancjsmkb");
//							��diancjsmkb_id����List
							List_TableId.add(jiesbId);
							
						}else if(jieszl.equals("�ɹ����㵥")){
							
//							��kuangfjsmkb����List
							List_TableName.add("kuangfjsmkb");
//							��kuangfjsmkb_id����List
							List_TableId.add(jiesbId);
						}
						
					}else if(jieslb.equals(Locale.yunfjs_feiylbb)){
//						�������Ϊ���˷ѽ���
//							�����������Ϊ�����������㡰
//								Ҫ����diancjsyfb������״̬
//							�����������Ϊ�����󷽽��㡰
//								Ҫ����kuangfjsyfb������״̬
//						��jiesyfb����List
						
						if(jieszl.equals("���۽��㵥")){
							
//							��diancjsyfb����List
							List_TableName.add("diancjsyfb");
//							��diancjsyfb_id����List
							List_TableId.add(jiesbId);
							
						}else if(jieszl.equals("�ɹ����㵥")){
							
//							��kuangfjsyfb����List
							List_TableName.add("kuangfjsyfb");
//							��kuangfjsyfb_id����List
							List_TableId.add(jiesbId);
						}
					}
				}
			}
		}
	}

	
	
}