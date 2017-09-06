package com.zhiren.dtrlgs.shoumgl.shoumjs;
/*
	2009-6-16 ��
	zsj
	��visit�е�String13 ��� fahb_id��������һ��ҳ��
*/
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.Locale;
//import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Shoumjsxz extends BasePage implements PageValidateListener {
	private String msg = "";
	private static String sql="";
	public String getMsg() {
		return msg;
	} 

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}
//	�ϴν�����
	private String scjsl="";
	public String getScjsl(){
		
		return scjsl;
	}
	
	public void setScjsl(String value){
		
		scjsl=value;
	}
		
	// ������
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}

	}

//	��������ѡ��
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
	}
	
	//ҵ������������
//	public String Yewlxvalue="";
	public String getYewlx(){
		return ((Visit)this.getPage().getVisit()).getString14();
	}
	public void setYewlx(String Yewlxvalue){
         ((Visit)this.getPage().getVisit()).setString14(Yewlxvalue);
	}
//	����������ȡֵ

	public String getChangb() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}
	
//	���䵥λ������
	public String getYunsdw() {
		return ((Visit) this.getPage().getVisit()).getString10();
	}
	public void setYunsdw(String changb) {
		((Visit) this.getPage().getVisit()).setString10(changb);
	}
	
//	Ʒ��������
	private String _pinz="";
	public String getPinz() {
		return _pinz;
	}
	
	public void setPinz(String pinz) {
		_pinz=pinz;
	}
	
/*//	���ձ��������ȡֵ
	
	public String getYansbh() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}
	public void setYansbh(String yansbh) {
		((Visit) this.getPage().getVisit()).setString4(yansbh);
	}
	*/
	//����۶���ȡֵ
	public String getJieskdl() {
		
		return ((Visit) this.getPage().getVisit()).getString7()==""?"0":((Visit) this.getPage().getVisit()).getString7();
	}
	public void setJieskdl(String jieskdl) {
		((Visit) this.getPage().getVisit()).setString7(jieskdl);
	}
//	Qnet,ar����
	private String _Qu="";
	public String getQu() {
		return _Qu;
	}
	public void setQu(String qu) {

		_Qu=qu;
	}
//	Qnet,ar����
	private String _Qd="";
	public String getQd() {
		return _Qd;
	}
	public void setQd(String qd) {
		_Qd = qd;
	}
//	Std����
	private String _Stdu="";
	public String getStdu() {
		return _Stdu;
	}
	public void setStdu(String su) {
		_Stdu = su;
	}
//	Std����
	private String _Stdd="";
	public String getStdd() {
		return _Stdd;
	}
	public void setStdd(String sd) {
		_Stdd=sd;
	}
//	Star����
	private String _Staru="";
	public String getStaru() {
		return _Staru;
	}
	public void setStaru(String su) {
		_Staru = su ;
	}
//	Star����
	private String _Stard="";
	public String getStard() {
		return _Stard;
	}
	public void setStard(String sd) {
		_Stard = sd;
	}
/*	//��Ʊ�˶�״̬
	public String getHuopztvalue() {
		return ((Visit) this.getPage().getVisit()).getString9();
	}

	public void setHuopztvalue(String huopztvalue) {
		((Visit) this.getPage().getVisit()).setString9(huopztvalue);
	}
	*/
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //Ϊ  "ˢ��"  ��ť��Ӵ������
//    	getSelectData();
    }
//
	private boolean _JiesChick = false;

	public void JiesButton(IRequestCycle cycle) {
		_JiesChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
		
		if (_JiesChick) {
			_JiesChick = false;
			GotoJiesd(cycle);
		}
	}
	
	private void GotoJiesd(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		
		ExtGridUtil egu=this.getExtGrid();
    	ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
    	String mstr_fahb_id="";
    	String mstr_lieid="";
    	String mstrchaij_lieid="";
    	String mstr_jieszbtz="��";

    	while(mdrsl.next()){
    		mstr_lieid+=mdrsl.getString("lie_id")+",";
    		mstr_fahb_id+=mdrsl.getString("id")+",";
    	}
    	//�������Ϊ��ֵ��׼��
    	 String SelIds="";long Diancxxb_id=0;long Jieslx=0;long Gongysb_id=0;
 				long Hetb_id=0;String Jieszbsftz="";String Yansbh="";double Jieskdl=0;long Yunsdwb_id=0;double Shangcjsl=0;
//    	�糧Id��ֵ
 		/*		
    	if(((Visit) getPage().getVisit()).isGSUser()){
//    		((Visit) getPage().getVisit()).setLong1(MainGlobal.getProperId(this.getFencbModel(),this.getChangb()));
    		Diancxxb_id=MainGlobal.getProperId(this.getFencbModel(),this.getChangb());
    	}
    	else{
//    		((Visit) getPage().getVisit()).setLong1(((Visit) getPage().getVisit()).getDiancxxb_id());
    		Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
    	}*/     Visit  visit=(Visit)this.getPage().getVisit();
 			   String dian_id=visit.getDiancxxb_id()+"";
 			   if(this.getJib(visit.getDiancxxb_id())==3){
 				   
 			 
 				try {
 					dian_id=MainGlobal.getTableCol("diancxxb","fuid","id",dian_id);
				} catch (Exception e1) {
					// TODO �Զ����� catch ��
					e1.printStackTrace();
					return;
				}
 			  }
//             ����ҵ������
 	    	    visit.setLong10(MainGlobal.getProperId(this.getYewlxModel(),getYewlx()));
 				((Visit) getPage().getVisit()).setLong1(new Long(dian_id).longValue());
//    	������Lie_Idd
    	//((Visit) getPage().getVisit()).setString1(mstr_lieid.substring(0,mstr_lieid.lastIndexOf(",")));
    	 SelIds=mstr_lieid.substring(0,mstr_lieid.lastIndexOf(","));
    	 ((Visit) getPage().getVisit()).setString1(SelIds);
//    	����Id
    	 mstr_fahb_id=mstr_fahb_id.substring(0,mstr_fahb_id.lastIndexOf(","));
    	 ((Visit) getPage().getVisit()).setString13(mstr_fahb_id);
//    	�������ͣ���Ʊ��ú��˷�...��
    	((Visit) getPage().getVisit()).setLong2(this.getJieslxValue().getId());
    	Jieslx=this.getJieslxValue().getId();
    	//������λid
//    	((Visit) getPage().getVisit()).setLong3(Long.parseLong(MainGlobal.getLeaf_ParentNodeId(getTree(), getTreeid())));
    	Gongysb_id=Long.parseLong(MainGlobal.getLeaf_ParentNodeId(getTree(), getTreeid()));
    	((Visit) getPage().getVisit()).setLong3(Gongysb_id);
    	//����ʱ�Ŀ۶������ò��������Ѽ���������
    	if(((Visit) getPage().getVisit()).getString7().trim().equals("")){
    		Jieskdl=0;
    		((Visit) getPage().getVisit()).setString7("0");
    	}
    	
//    	����ʱ���ϴν�������Ϊ���ۼ������������ۼ��ã�
    	if(this.getScjsl().trim().equals("")){
    		
    		Shangcjsl=0;
    	}else{
    		
    		Shangcjsl=Double.valueOf(this.getScjsl()).doubleValue();
    	}
    	((Visit) getPage().getVisit()).setString12(String.valueOf(Shangcjsl));
    	
//    	����ѡ�з������hetb_idΪgetHthValue().getId()
    	if(setFahb_HetbId(((Visit) getPage().getVisit()).getString1(),this.getHthValue().getId())){
    		
//    		��Hetb_id����Long8
//    		((Visit) getPage().getVisit()).setLong8(this.getHthValue().getId());
    		Hetb_id=this.getHthValue().getId();
    		((Visit) getPage().getVisit()).setLong8(Hetb_id);
//    		Ϊ��ֹgongysb_id=0ȡ�������÷���ʱ�����ؼ���gongysb_id���¸�ֵ�����
    		Gongysb_id=Shoumjsdcz.getGongysb_id(SelIds, 
    				Diancxxb_id, Gongysb_id, 
    				Hetb_id, Double.parseDouble(getJieskdl()));
    		((Visit) getPage().getVisit()).setLong3(Gongysb_id);
    		
    		Jieszbsftz=mstr_jieszbtz=Shoumjsdcz.getJiessz_item(Diancxxb_id, 
    				Gongysb_id,Hetb_id,Locale.jieszbtz_jies, mstr_jieszbtz);
    		((Visit) getPage().getVisit()).setString2(mstr_jieszbtz);
    		
//    		���䵥λ��_id
    		
    		Yunsdwb_id=MainGlobal.getProperId(this.getYunsdwModel(), this.getYunsdw());
    		((Visit) getPage().getVisit()).setLong9(Yunsdwb_id);
    		//�ϴν�����
    		Shangcjsl=Double.parseDouble(((Visit) getPage().getVisit()).getString12()==""?"0":((Visit) getPage().getVisit()).getString12());
//    		����۶���ȡֵ
    		Jieskdl=Double.parseDouble( getJieskdl());
//    		���ձ������  
    		//Yansbh=getYansbh();
//   ������
    		Balances bls = new Balances();
			Balances_variable bsv = new Balances_variable(); // Balances����
			//bsv.setDaibcc(MainGlobal.getShouwch(SelIds));
			bls.setBsv(bsv);

			try {
				bls.getBalanceData(SelIds, new Long(dian_id).longValue(), Jieslx, Gongysb_id, Hetb_id, Jieszbsftz, Yansbh, Jieskdl, Yunsdwb_id, Shangcjsl);
			
			} catch (NumberFormatException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			} catch (Exception e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			//ҳ�洫ֵ
	    		((Visit)this.getPage().getVisit()).setObject1(bls);
	    		//cycle.activate("ShoumBalance");
//	    		
	    		if(mstr_jieszbtz.equals("��")){
		    		
		    		cycle.activate("ShoumBalance");
		    	}else{
//		    		��Ҫָ�����
//		    		1��ϵͳ��Ϣ�в�ʹ�����ձ��
		    		((Visit) getPage().getVisit()).setString4("");
//		    		2��ϵͳ��Ϣ��ʹ�����ձ��
//		    		((Visit) getPage().getVisit()).setString4((this.getYansbh().equals("")?"":this.getYansbh()));
		    		cycle.activate("Shoumjszbtz");
		    	}
	
    	}else{
    		this.setMsg("���ִ���");
    	}
	}
	
	/*private void setChaifl(String mstrchaij_lieid) {
		// TODO �Զ����ɷ������
//		����У��ҵ�Ҫ��ֵ���
		JDBCcon con=new JDBCcon();
		try{
			
			long lngFahb_id=0;
			String sql="select id from fahb f where f.lie_id="+mstrchaij_lieid;
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
				
				lngFahb_id=rsl.getLong("id");
				Chaif(lngFahb_id);
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}*/
//	
	/*private void Chaif(long lngFahb_id){
//		ʵ�ʲ�ַ�����
		JDBCcon con=new JDBCcon();
		try{
			String strNew_FahbId="";
			StringBuffer sqlbf=new StringBuffer("begin 		\n");
			
//			1�����ȱ���Ҫ��ֵķ���,�����µķ���
			strNew_FahbId=Jilcz.CopyFahb(con, String.valueOf(lngFahb_id), ((Visit) getPage().getVisit()).getDiancxxb_id());
			sqlbf.append(" update fahb set lie_id="+strNew_FahbId+" where id="+strNew_FahbId+"; 	\n");
			
			
//			2�������Ѻ˶Ե�chepb��fahb_idΪ�·�����id
			sqlbf.append(" update chepb set fahb_id="+strNew_FahbId+" where id in (select cp.id from chepb cp,fahb f,danjcpb dj 	\n");
			sqlbf.append(" 		where f.id=cp.fahb_id and dj.chepb_id=cp.id and f.id="+lngFahb_id+");	\n");
			sqlbf.append("end;	");
			
			con.getUpdate(sqlbf.toString());
			
//			3�������·�����
			Jilcz.updateFahb(con, strNew_FahbId);
			
//			4�������Ϸ�����
			Jilcz.updateFahb(con, String.valueOf(lngFahb_id));
			
			if(((Visit) getPage().getVisit()).getString8().equals("")){
				
				((Visit) getPage().getVisit()).setString8(strNew_FahbId);
			}else{
				
				((Visit) getPage().getVisit()).setString8(","+strNew_FahbId);
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}*/

	private boolean setFahb_HetbId(String FahbSelLieId, long HetbId) {
		// TODO �Զ����ɷ������
//		����ѡ�з������hetb_idΪgetHthValue().getId()
		JDBCcon con=new JDBCcon(); 
		boolean flag=false;
		;String sql="update fahb set hetb_id="+HetbId+" where lie_id in ("+FahbSelLieId+")";
		if(con.getUpdate(sql)>=0){
			
			flag=true;
		}
		con.Close();
		return flag;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
//		 ********************������************************************************
	    String biaoq="��������";
	    String riq="fahrq";
	    String danx="";
        String danx1="";
        String yihd="";
        String weihd="";
        String fencb="";
        String yansbh="";
        String huophdzt="";
        String yunsdw="";
        String pinz="";
        String shangcjsl="";	//�ϴν�����
        String tiaoj_returnvalue_changb="";
     //   String tiaoj_returnvalue_yansbh="";
       // String tiaoj_returnvalue_huophdzt="";
        String tiaoj_returnvalue_yunsdw="";
        String tiaoj_returnvalue_pinz="";
        String tiaoj_returnvalue_shangcjsl="";
        String jieskdl=" ,{  \n"
			+ "	\titems:jieskdl=new Ext.form.Field({	\n" 
			+ "	\twidth:100,	\n" 
			+ " \tfieldLabel: '����۶���',\n"
			+ " \tselectOnFocus:true,	\n" 
			+ " \tvalue:document.getElementById('Jieskdl').value,	\n" 
			+ " \ttransform:'Jieskdl',	\n" 
			+ " \tlazyRender:true,	\n" 
			+ "	\ttriggerAction:'all',	\n" 
			+ " \ttypeAhead:true,	\n" 
			+ " \tforceSelection:true,	\n" 
			+ " \teditable:false,	\n"
			+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('Jieskdl').selectedIndex=index}}"
			+ "	\t})}";
//        
//        String Qnet = 
//        	",{ \n" +
//        	" \t\titems: [{\n" +
//        	" \t\t\txtype:'textfield',\n" + 
//        	" \t\t\tfieldLabel:'Qnet,ar(mj/kg)',\n" + 
//        	"\t\t\twidth:0\n" + 
//        	"\t\t } ]}";
//        	
        String Qd =
        	",{\n" + 
        	"\t\t\titems:qd=new Ext.form.Field({\n" + 
        	"\t\t\twidth:100,\n" + 
        	" \t\t\tfieldLabel: 'Qnet,ar(mj/kg)����',\n" + 
        	" \t\t\tvalue:document.getElementById('Qd').value,	\n" +
        	" \t\t\tselectOnFocus:true,\n" + 
        	" \t\t\ttransform:'Qd',\n" + 
        	" \t\t\tlazyRender:true,\n" + 
        	"\t\t\ttriggerAction:'all',\n" + 
        	" \t\t\ttypeAhead:true,\n" + 
        	" \t\t\tforceSelection:true,\n" + 
        	" \t\t\teditable:false,\n" + 
        	"	listeners:{\n" +
        	"     'change':function(own,rec,index){\n" + 
        	"        var obj1 = own.getRawValue();\n" + 
        	"      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
        	"\t\t\t\tif(!MatchNum(obj1 ,num )){\n" + 
        	"\t\t\t\t\tqd.setRawValue(0);\n" + 
        	"\t\t\t\t}\n" + 
        	"      }\n" + 
        	"}\n" + 
        	"})}\n";
        
        String Qu =
        	",{\n" +
        	"\t\t\titems:qu=new Ext.form.Field({\n" + 
        	"\t\t\twidth:100,\n" + 
        	" \t\t\tfieldLabel: 'Qnet,ar(mj/kg)����',\n" + 
        	" \t\t\tvalue:document.getElementById('Qu').value,	\n" +
        	" \t\t\tselectOnFocus:true,\n" + 
        	" \t\t\ttransform:'Qu',\n" + 
        	" \t\t\tlazyRender:true,\n" + 
        	"\t\t\ttriggerAction:'all',\n" + 
        	" \t\t\ttypeAhead:true,\n" + 
        	" \t\t\tforceSelection:true,\n" + 
        	" \t\t\teditable:false,\n" + 
        	"	listeners:{\n" +
        	"     'change':function(own,rec,index){\n" + 
        	"        var obj1 = own.getRawValue();\n" + 
        	"      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
        	"\t\t\t\tif(!MatchNum(obj1 ,num )){\n" + 
        	"\t\t\t\t\tqu.setRawValue(0);\n" + 
        	"\t\t\t\t}\n" + 
        	"      }\n" + 
        	"}\n" + 
        	"})}\n";
        
        String Stard =
        	",{\n" + 
        	"\t\t\titems:stard=new Ext.form.Field({\n" + 
        	"\t\t\twidth:100,\n" + 
        	" \t\t\tfieldLabel: 'Star(%)����',\n" + 
        	" \t\t\tselectOnFocus:true,\n" + 
        	" \t\t\tvalue:document.getElementById('Stard').value,	\n" +
        	" \t\t\ttransform:'Stard',\n" + 
        	" \t\t\tlazyRender:true,\n" + 
        	"\t\t\ttriggerAction:'all',\n" + 
        	" \t\t\ttypeAhead:true,\n" + 
        	" \t\t\tforceSelection:true,\n" + 
        	" \t\t\teditable:false,\n" + 
        	"	listeners:{\n" +
        	"     'change':function(own,rec,index){\n" + 
        	"        var obj1 = own.getRawValue();\n" + 
        	"      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
        	"\t\t\t\tif(!MatchNum(obj1 ,num )){\n" + 
        	"\t\t\t\t\tstard.setRawValue(0);\n" + 
        	"\t\t\t\t}\n" + 
        	"      }\n" + 
        	"}\n" + 
        	"})}\n";
        
        String Staru =
        	",{\n" +
        	"\t\t\titems:staru=new Ext.form.Field({\n" + 
        	"\t\t\twidth:100,\n" + 
        	" \t\t\tfieldLabel: 'Star(%)����',\n" + 
        	" \t\t\tvalue:document.getElementById('Staru').value,	\n" +
        	" \t\t\tselectOnFocus:true,\n" + 
        	" \t\t\ttransform:'Staru',\n" + 
        	" \t\t\tlazyRender:true,\n" + 
        	"\t\t\ttriggerAction:'all',\n" + 
        	" \t\t\ttypeAhead:true,\n" + 
        	" \t\t\tforceSelection:true,\n" + 
        	" \t\t\teditable:false,\n" + 
        	"	listeners:{\n" +
        	"     'change':function(own,rec,index){\n" + 
        	"        var obj1 = own.getRawValue();\n" + 
        	"      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
        	"\t\t\t\tif(!MatchNum(obj1 ,num )){\n" + 
        	"\t\t\t\t\tstaru.setRawValue(0);\n" + 
        	"\t\t\t\t}\n" + 
        	"      }\n" + 
        	"}\n" + 
        	"})}\n";
        String Stdd =
        	",{\n" + 
        	"\t\t\titems:stdd=new Ext.form.Field({\n" + 
        	"\t\t\twidth:100,\n" + 
        	" \t\t\tfieldLabel: 'Std(%)����',\n" + 
        	" \t\t\tvalue:document.getElementById('Stdd').value,	\n" +
        	" \t\t\tselectOnFocus:true,\n" + 
        	" \t\t\ttransform:'Stdd',\n" + 
        	" \t\t\tlazyRender:true,\n" + 
        	"\t\t\ttriggerAction:'all',\n" + 
        	" \t\t\ttypeAhead:true,\n" + 
        	" \t\t\tforceSelection:true,\n" + 
        	" \t\t\teditable:false,\n" + 
        	"	listeners:{\n" +
        	"     'change':function(own,rec,index){\n" + 
        	"        var obj1 = own.getRawValue();\n" + 
        	"      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
        	"\t\t\t\tif(!MatchNum(obj1 ,num )){\n" + 
        	"\t\t\t\t\tstdd.setRawValue(0);\n" + 
        	"\t\t\t\t}\n" + 
        	"      }\n" + 
        	"}\n" + 
        	"})}\n";
        
        String Stdu =
        	",{\n" +
        	"\t\t\titems:stdu=new Ext.form.Field({\n" + 
        	"\t\t\twidth:100,\n" + 
        	" \t\t\tfieldLabel: 'Std(%)����',\n" + 
        	" \t\t\tvalue:document.getElementById('Stdu').value,	\n" +
        	" \t\t\tselectOnFocus:true,\n" + 
        	" \t\t\ttransform:'Stdu',\n" + 
        	" \t\t\tlazyRender:true,\n" + 
        	"\t\t\ttriggerAction:'all',\n" + 
        	" \t\t\ttypeAhead:true,\n" + 
        	" \t\t\tforceSelection:true,\n" + 
        	" \t\t\teditable:false,\n" + 
        	"	listeners:{\n" +
        	"     'change':function(own,rec,index){\n" + 
        	"        var obj1 = own.getRawValue();\n" + 
        	"      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
        	"\t\t\t\tif(!MatchNum(obj1 ,num )){\n" + 
        	"\t\t\t\t\tstdu.setRawValue(0);\n" + 
        	"\t\t\t\t}\n" + 
        	"      }\n" + 
        	"}\n" + 
        	"})}\n";


        

		if(getRbvalue().equals("fahrq")||this.getRbvalue().equals("")){
			
			
			biaoq="��������";
			riq="fahrq";
			danx="checked:true ,   \n";
		}else if(getRbvalue().equals("daohrq")){
			biaoq="��������";
			riq="daohrq";
			danx1="checked:true ,   \n";
		
		}
//        if(visit.isFencb()==true){
         fencb=",{\n "
    	    +"items:Changb=new Ext.form.ComboBox({ \n"
    		+"width:100,\n"
    		+"fieldLabel: '����',\n"
    		+"selectOnFocus:true,\n"
    		+"transform:'FencbDropDown',\n"
    		+"lazyRender:true,\n"
    		+"triggerAction:'all',\n"
    		+"typeAhead:true,\n"
    		+"forceSelection:true,\n"
    		+"editable:false \n"
    	+"})}";
         
        tiaoj_returnvalue_changb="	document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue();	\n";
	    ComboBox yewlx=new ComboBox();
	       yewlx.setEditable(true);
	      yewlx.setId("yewlxCo");
	      yewlx.setWidth("100");
	      
	      yewlx.setFieldLabel("ҵ������");
	     // YewlxSelect    
	      yewlx.setTransform("YewlxSelect");

        shangcjsl=",{\n" +
	        "      items:scjsl=new Ext.form.Field({\n" + 
	        "      width:100,\n" + 
	        "      fieldLabel: '�ϴν�����(��)',\n" + 
	        "      selectOnFocus:true,\n" + 
	        "	   value:document.getElementById('Scjsl').value, \n" +
	        "      transform:'Scjsl',\n" + 
	        "      lazyRender:true,\n" + 
	        "      triggerAction:'all',\n" + 
	        "      typeAhead:true,\n" + 
	        "      forceSelection:true,\n" + 
	        " \t\t\teditable:false,\n" + 
	        "\t\t\tlisteners:{'change':function(own,rec,index){\n" + 
	        "\t\t        \t\tvar obj1 = own.getRawValue();\n" + 
	        "\t\t      \t\t\tvar num = /^[0-9]{0,7}\\.[0-9]{0,4}$|^[0-9]{0,7}$/;\n" + 
	        "\t\t\t\t\t\tif(!MatchNum(obj1 ,num )){\n" + 
	        "\t\t\t\t\t\t\tscjsl.setRawValue(0);\n" + 
	        "\t\t\t\t\t\t}\n" + 
	        "      \t\t\t\t}\n" + 
	        "\t\t\t}\n" + 
	        "})},{items:"+yewlx.getScript()+"}";

        tiaoj_returnvalue_shangcjsl=" document.getElementById('Scjsl').value=scjsl.getRawValue();	\n";
        
//       }else{
//        	fencb="";
//        	tiaoj_returnvalue_changb="";	
//        }
//    	if(MainGlobal.getXitxx_item("����", Locale.shiyysbh_jies, String.valueOf(visit.isFencb()?MainGlobal.getProperId(getFencbModel(), this.getChangb()):visit.getDiancxxb_id()), "��").equals("��")){
//    		tiaoj_returnvalue_yansbh="document.getElementById('TEXT_YANSBH_VALUE').value=yansbh.getRawValue();";
			/*yansbh=	 "	,{\n" 
				+ " \titems:yansbh=new Ext.form.ComboBox({	\n" 
				+ "	\twidth:100,	\n" 
				+ " \tfieldLabel: '���ձ��',\n"
				+ " \tselectOnFocus:true,	\n" 
				+ " \ttransform:'YansbhDropDown',	\n" 
				+ " \tlazyRender:true,	\n" 
				+ "	\ttriggerAction:'all',	\n" 
				+ " \ttypeAhead:true,	\n" 
				+ " \tforceSelection:true,	\n" 
				+ " \teditable:false,	\n"
				+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('YansbhDropDown').selectedIndex=index}}"
				+ "	\t})}";*/
//			}else{
//				yansbh="";
//			}
//         if(this.getJieslxValue().getId()==Locale.liangpjs_feiylbb_id
//        		 ||this.getJieslxValue().getId()==Locale.guotyf_feiylbb_id){
        	 
//        	 yunsdw=",{		\n"  
//        		 + " xtype:'textfield',	\n"
//        		 + " fieldLabel:'���䵥λ',	\n"
//        		 + " width:0	\n"	
//        		 + "}	\n";
        	 
        	 yunsdw =	"	,{\n" 
        		 	+ "items:YunsdwCb=new Ext.form.ComboBox({	\n"
        	 		+ "	width:100,	\n"
        	 		+ " fieldLabel: '���䵥λ',\n"
        	 		+ "	selectOnFocus:true,		\n"
        	 		+ "	transform:'YunsdwDropDown',		\n"
        	 		+ "	lazyRender:true,		\n"
        	 		+ " triggerAction:'all',	\n"
        	 		+ " typeAhead:true,		\n"
        	 		+ "	forceSelection:true,	\n"
        	 		+ "	editable:true		\n"
        	 		+ "	})}";
        	 
        	 tiaoj_returnvalue_yunsdw="document.getElementById('TEXT_YUNSDW_VALUE').value=YunsdwCb.getRawValue();";
        	 
//        	 pinz=",{		\n"  
//        		 + " xtype:'textfield',	\n"
//        		 + " fieldLabel:'Ʒ��',	\n"
//        		 + " width:0	\n"	
//        		 + "}	\n";
        	 
        	 pinz = "	,{\n"
        		 	+ "items:PinzCb=new Ext.form.ComboBox({	\n"
        	 		+ "	width:100,	\n"
        	 		+ " fieldLabel: 'Ʒ��',\n"
        	 		+ "	selectOnFocus:true,		\n"
        	 		+ "	transform:'PinzDropDown',		\n"
        	 		+ "	lazyRender:true,		\n"
        	 		+ " triggerAction:'all',	\n"
        	 		+ " typeAhead:true,		\n"
        	 		+ "	forceSelection:true,	\n"
        	 		+ "	editable:true		\n"
        	 		+ "	})}";
        	 
        	 tiaoj_returnvalue_pinz="document.getElementById('TEXT_PINZ_VALUE').value=PinzCb.getRawValue();";
        	 
        	/* 	if(getHuopztvalue().equals("yiwc")||this.getHuopztvalue().equals("")){
        	 		yihd="checked:true ,   \n";
     			}else if(getHuopztvalue().equals("weiwc")){
     				weihd="checked:true ,   \n";
     			}*/
        	/* huophdzt=" ,{items: [{  \n"
				+ " \txtype:'textfield',\n" 
				+ " \tfieldLabel:'��Ʊ�˶�״̬',\n" 
				+ "	\twidth:0	\n"  
				+ " },	\n"
				+ " { \n"
				+ "     xtype:'radio', \n"
				+ "		boxLabel:'�Ѻ˶�', \n"                		
			    + "     anchor:'95%', \n"
			    +       yihd 
			    + "     Value:'yiwc', \n"			            			            												
				+ "		name:'r2',\n"
				+ "		listeners:{ \n"
//				+ "				'focus':function(r,c){\n"
//				+ "					document.getElementById('huopztvalue').value=r.Value;\n"
//				+ "				},\n"
				+ "				'check':function(r,c){ \n"
				+ "					var val='weiwc';if(c) val='yiwc';"
				+ "					document.getElementById('huopztvalue').value=val;\n"
				+ "					\tif(document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value==''){	\n"
				+ " 				\t	document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value=document.getElementById('huopztvalue').value;} \n"
				+ "					  }\n"
				+ "		} \n"
				
				+ "	},\n"
				+ "	{  \n"   
				+ "     xtype:'radio', \n"
				+ "		boxLabel:'δ�˶�',\n"  
				+ "		Value:'weiwc', \n"             		
				+ "     anchor:'95%',	\n"	
				+       weihd
				+ "		name:'r2'"
//				+	",\n"	
//				+ "		listeners:{ \n"
////				+ "				'focus':function(r,c){ \n"
////				+ "					document.getElementById('huopztvalue').value=r.Value;\n"
////				+ "				}, \n"
//				+ "				'check':function(r,c){ \n"
//				+ "					alert(r.Value);"
//				+ "					document.getElementById('huopztvalue').value=r.Value;\n"
//				+ "					\tif(document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value==''){	\n"
//				+ " 				\t	document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value=document.getElementById('huopztvalue').value;} \n"
//				+ "					  }\n"
//				+ "		}	\n"																	        
				+ "}]}";*/
        	 
        //	 tiaoj_returnvalue_huophdzt="document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value=document.getElementById('huopztvalue').value;";
//         }else{
//        	 huophdzt="";
//         }
        
        
	    String Strtmpfunction=   
	    	"var form =new Ext.Panel({\n" +
	    	"\tlabelAlign:'left',buttonAlign:'right',bodyStyle:'padding:5px;',\n" + 
	    	"    frame:true,labelWidth:115,monitorValid:true,\n" + 
	    	"    items:[{\n" + 
	    	"      layout:'column',border:false,labelSeparator:':',\n" + 
	    	"        defaults:{layout: 'form',border:false,columnWidth:.5},\n" + 
	    	"        items:[\n" + 
	    	"              {items: [{\n" + 
	    	"              xtype:'textfield',\n" + 
	    	"              fieldLabel:'����ѡ��',\n" + 
	    	"              width:0\n" + 
	    	"            },{xtype:'radio',\n" + 
	    	"                    boxLabel:'��������',\n" + 
	    	"               anchor:'95%',\n" + 
	    					danx	 + 
	    	"               Value:'fahrq',\n" + 
	    	"            name:'test',\n" + 
	    	"            listeners:{\n" + 
	    	"                  'focus':function(r,c){\n" + 
	    	"                  document.getElementById('rbvalue').value=r.Value;\n" + 
	    	"                },\n" + 
	    	"                'check':function(r,c){\n" + 
	    	"                    document.getElementById('rbvalue').value=r.Value;\n" + 
	    	"                    if(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){\n" + 
	    	"                     document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;}\n" + 
	    	"                }\n" + 
	    	"            }\n" + 
	    	"          },{\n" + 
	    	"            xtype:'radio',\n" + 
	    	"            boxLabel:'��������',\n" + 
	    	"            Value:'daohrq',\n" + 
	    	"               anchor:'95%',\n" + 
	    				danx1 	+
	    	"            name:'test',\n" + 
	    	"            listeners:{\n" + 
	    	"                'focus':function(r,c){\n" + 
	    	"                document.getElementById('rbvalue').value=r.Value;\n" + 
	    	"            },\n" + 
	    	"            'check':function(r,c){\n" + 
	    	"                document.getElementById('rbvalue').value=r.Value;\n" + 
	    	"                if(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){\n" + 
	    	"                 document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;}\n" + 
	    	"            }\n" + 
	    	"          }\n" + 
	    	"        }]}\n" + 
	    	yansbh +
	    	yunsdw +
	    	pinz +
	    	huophdzt +
	    	jieskdl +
//	    	Qnet +
	    	Qd +
	    	Qu +
	    	Stdd +
	    	Stard +
	    	Stdu +
	    	Staru +
	    	fencb +	    	
	    	shangcjsl+
	    	"\n" + 
	    	"        ]//items\n" + 
	    	"      }]//items\n" + 
	    	"});//FormPanel\n"

        
			+ " win = new Ext.Window({\n"
//			+ " el:'hello-win',\n"
			+ "layout:'fit',\n"
			+ "width:500,\n"
			+ "height:400,\n"
			+ "closeAction:'hide',\n"
			+ "plain: true,\n"
			+ "title:'����',\n"
			+ "modal:true,"
			+ "items: [form],\n"
            				
			+ "buttons: [{\n"
			+ "   text:'ȷ��',\n"
			+ "   handler:function(){  \n"                  		
			+ 		tiaoj_returnvalue_changb+"\n"
			//+		tiaoj_returnvalue_yansbh+"\n"
			+ "      	document.getElementById('Jieskdl').value=jieskdl.getRawValue();\n"
			+ "      	document.getElementById('Qd').value=qd.getRawValue();\n"
			+ "      	document.getElementById('Qu').value=qu.getRawValue();\n"
			+ "      	document.getElementById('Stdd').value=stdd.getRawValue();\n"
			+ "      	document.getElementById('Stdu').value=stdu.getRawValue();\n"
			+ "      	document.getElementById('Stard').value=stard.getRawValue();\n"
			+ "      	document.getElementById('Staru').value=staru.getRawValue();\n"
			+			tiaoj_returnvalue_shangcjsl+"\n"
			//+       	tiaoj_returnvalue_huophdzt+"\n"
			+ 			tiaoj_returnvalue_yunsdw+"\n"
			+ 			tiaoj_returnvalue_pinz+"\n"
			+"          document.getElementById('TEXT_YewlxSelect_VALUE').value=yewlxCo.getRawValue();\n"
			+ "			document.getElementById('TEXT_RADIO_RQSELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
			+ " 		document.getElementById('RefurbishButton').click(); \n"
			+ "  	    win.hide();\n"
			+ "  	}   \n"                
			+ "},{\n"
			+ "   text: 'ȡ��',\n"
			+ "   handler: function(){\n"
			+ "       win.hide();\n"
			+ "		document.getElementById('TEXT_CHANGB_VALUE').value='';	\n"
		//	+ "		document.getElementById('TEXT_YANSBH_VALUE').value='';	\n"
		//	+ "		document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value='';	\n"
            + "     document.getElementById('Jieskdl').value='';  \n"
            + "     document.getElementById('Qd').value='';  \n"
            + "     document.getElementById('Qu').value='';  \n"
            + "     document.getElementById('Stdd').value='';  \n"
            + "     document.getElementById('Stdu').value='';  \n"
            + "     document.getElementById('Stard').value='';  \n"
            + "     document.getElementById('Staru').value='';  \n"
            + "     document.getElementById('Scjsl').value='';  \n"
			+ "   }\n"
			+ "}]\n"
           
			+ " });win.show();win.hide();";
	    
	       
//      ��ȡ�������ñ��н������ò���
//      	1����������
//        	2�������Ȩ����
//        	3��������ʾָ��
//        	4��������������С��λ
//        	5����������ȡ����ʽ
//        	6��Mt����С��λ
//        	7��Mad����С��λ
//        	8��Aar����С��λ
//        	9��Aad����С��λ
//        	10��Adb����С��λ
//        	11��Vad����С��λ
//        	12��Vdaf����С��λ
//        	13��Stad����С��λ
//        	14��Std����С��λ
//        	15��Had����С��λ
//        	16��Qnetar����С��λ
//        	17��Qbad����С��λ
//        	18��Qgrad����С��λ
//        	19������ָ�����
        
        String jies_Jssl="f.biaoz+f.yingk";	
        String jies_Jqsl="jingz";
        String jies_Jsxszb="Mt,Qnetar,Std";
        String jies_Jsslblxs="0";
        String jies_Jieslqzfs="sum(round())";
        String jies_Mtblxs="1";
        String jies_Madblxs="2";
        String jies_Aarblxs="2";
        String jies_Aadblxs="2";
        String jies_Adblxs="2";
        String jies_Vadblxs="2";
        String jies_Vdafblxs="2";
        String jies_Stadblxs="2";
        String jies_Starblxs="2";
        String jies_Stdblxs="2";
        String jies_Hadblxs="2";
        String jies_Qnetarblxs="2";
        String jies_Qbadblxs="2";
        String jies_Qgradblxs="2";
        
        long feiylbb_id=0;
        String strDiancxxb_id="0";

       if(this.getTree()!=null&&getHthValue().getId()!=-1){
    	   
	        if(Shoumjsdcz.getJiessz_items((visit.isFencb()?MainGlobal.getProperId(getFencbModel(), this.getChangb()):visit.getDiancxxb_id()),
	        					Long.parseLong(MainGlobal.getLeaf_ParentNodeId(getTree(),getTreeid())),this.getHthValue().getId())!=null){
	        	
	        	String JiesszArray[][]=null;
	        			
	        	JiesszArray=Shoumjsdcz.getJiessz_items((visit.isFencb()?MainGlobal.getProperId(getFencbModel(), this.getChangb()):visit.getDiancxxb_id()),
	        			Long.parseLong(MainGlobal.getLeaf_ParentNodeId(getTree(),getTreeid())),this.getHthValue().getId());
	            	
	    		for(int i=0;i<JiesszArray.length;i++){
	    			
	    			if(JiesszArray[i][0]!=null){
	    				
	    				if(JiesszArray[i][0].equals(Locale.jiesslzcfs_jies)){
							
							jies_Jssl=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.jiesjqsl_jies)){
							
							jies_Jqsl=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.jiesxszb_jies)){
							
							jies_Jsxszb=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.mtblxsw_jies)){
							
							jies_Mtblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.madblxsw_jies)){
							
							jies_Madblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.aarblxsw_jies)){
							
							jies_Aarblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.aadblxsw_jies)){
							
							jies_Aadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.adblxsw_jies)){
							
							jies_Adblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.vadblxsw_jies)){
							
							jies_Vadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.vdafblxsw_jies)){
							
							jies_Vdafblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.stadblxsw_jies)){
							
							jies_Stadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.stdblxsw_jies)){
							
							jies_Stdblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.hadblxsw_jies)){
							
							jies_Hadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.Qnetarblxsw_jies)){
							
							jies_Qnetarblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.Qbadblxsw_jies)){
							
							jies_Qbadblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.Qgradblxsw_jies)){
							
							jies_Qgradblxs=JiesszArray[i][1];
						}else if(JiesszArray[i][0].equals(Locale.jiesslqzfs_jies)){
							
							jies_Jieslqzfs=JiesszArray[i][1];
						}
	    			}
				}
	        }
       }	
	       if(getJieslxValue().getId()==Locale.liangpjs_feiylbb_id
	    		   ||getJieslxValue().getId()==Locale.guotyf_feiylbb_id){
//				�������ת�����������Ʊ������ǹ���������ôͳͳת��Ϊ�����˷ѡ�
				feiylbb_id=Locale.guotyf_feiylbb_id;
			}else{
				
				feiylbb_id=getJieslxValue().getId();
			}
	       
	       String YunsfsWhere="";
	    /*   if(!((Visit) getPage().getVisit()).getString11().equals("")){
	    	   
	    	   if(((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_tiel)){
//	    		   ��·
	    		   YunsfsWhere=" and f.yunsfsb_id=1 ";
	    	   }else if(((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_gongl)){
//	    		   ��·
	    		   YunsfsWhere=" and f.yunsfsb_id=2 ";
	    	   }else if(((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_haiy)){
//	    		   ����
	    		   YunsfsWhere=" and f.yunsfsb_id=3 ";
	    	   }else if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_pidc)){
//	    		   Ƥ����
	    		   YunsfsWhere=" and f.yunsfsb_id=4 ";
	    	   }
	       }*/
           
             String gongysb_id="";
             String rqlx="";
             if(!getTreeid().equals("0")){
              gongysb_id= " and f.gongysb_id="+getTreeid();
             }
             
             if(getRbvalue()==null||getRbvalue().equals("")){
            	 rqlx="f.fahrq";
     	}else{
     		if(getRbvalue().equals("fahrq")){
     			rqlx="f.fahrq";
     		}
     		else rqlx= "f.daohrq";
     	}
        	 sql = 
   
     			" select f.id as id,decode(g.mingc,null,'�ϼ�',g.mingc) as fahdw,m.mingc as meikdw,		\n" +
     			" 		decode(min(to_char(f.fahrq,'yyyy-MM-dd')),max(to_char(f.fahrq,'yyyy-MM-dd')),	\n" +
     			"       min(to_char(f.fahrq,'yyyy-MM-dd')),\n" + 
     			"       min(to_char(f.fahrq,'yyyy-MM-dd'))	\n" + 
     			"       ||'--'||max(to_char(f.fahrq,'yyyy-MM-dd'))) as fahrq ," +
     			" 		decode(min(to_char(f.daohrq,'yyyy-MM-dd')),max(to_char(f.daohrq,'yyyy-MM-dd')),	\n" +
     			"       min(to_char(f.daohrq,'yyyy-MM-dd')),\n" + 
     			"       min(to_char(f.daohrq,'yyyy-MM-dd'))	\n" + 
     			"       ||'--'||max(to_char(f.daohrq,'yyyy-MM-dd'))) as daohrq ," +
     			"       max( y.mingc) yewlx,max(c.mingc) as faz,max(f.chec)as chec,sum(f.ches) as ches,sum(f.biaoz) as biaoz,	sum(f.yingk) as yingk," +
     			"		sum(f.yuns) as yuns,	\n" + 
     			"       decode(g.mingc,null,'',sum(f.jingz)) as jingz,decode(g.mingc,null,'',h.hetbh) as hetbh,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz),"+jies_Qnetarblxs+"))) as Qnetar,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round(z.qnet_ar*1000/4.1816,0))/sum(f.jingz),"+jies_Qnetarblxs+"))) as Qnetarzh,\n" +
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz),"+jies_Stdblxs+"))) as Std,\n" +
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.stad)/sum(f.jingz),"+jies_Stadblxs+"))) as Stad,\n" +
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.star)/sum(f.jingz),"+jies_Starblxs+"))) as Star,\n" +
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz),"+jies_Vdafblxs+"))) as Vdaf,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz),"+jies_Mtblxs+"))) as Mt,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mad)/sum(f.jingz),"+jies_Madblxs+"))) as Mad,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz),"+jies_Aarblxs+"))) as Aar,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aad)/sum(f.jingz),"+jies_Aadblxs+"))) as Aad,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.ad)/sum(f.jingz),"+jies_Adblxs+"))) as Ad,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.var)/sum(f.jingz),"+jies_Adblxs+"))) as Var,\n" +
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vad)/sum(f.jingz),"+jies_Vadblxs+"))) as Vad,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.had)/sum(f.jingz),"+jies_Hadblxs+"))) as Had,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qbad)/sum(f.jingz),"+jies_Qbadblxs+"))) as Qbad,\n" + 
     			"       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qgrad)/sum(f.jingz),"+jies_Qgradblxs+"))) as Qgrad\n" + 
     			"       ,f.lie_id\n"+
     			"       from fahb f,gongysb g,meikxxb m,hetb h,chezxxb c,zhilb z,yewlxb y,diancxxb di\n"+
     			"       where f.gongysb_id=g.id and f.meikxxb_id=m.id 						\n" + 
     			"       and f.hetb_id=h.id(+) and f.faz_id=c.id 	  and f.zhilb_id=z.id	\n" +
     			"       and f.diancxxb_id=di.id\n"+
     			        gongysb_id+"	\n" + 
     			"       and  f.yewlxb_id="+MainGlobal.getProperId(this.getYewlxModel(),getYewlx())+"													\n" + 
     			"       and f.jiesb_id =0 /*and f.yewlxb_id in(1,3)*/ and y.id= f.yewlxb_id\n"+//����δ����ĺ�ҵ������Ϊ �ɹ� �� ֱ���  
//     			���䷽ʽ����
//     					YunsfsWhere+	
     					this.getWhere(riq)+
     			//   	this.getSupplyWhere()+
//     			"       group by rollup(f.lie_id,g.mingc,m.mingc,h.hetbh,c.mingc)	\n" + 
     			"        group by rollup((f.id,g.mingc,m.mingc,h.hetbh,f.lie_id,c.mingc)) \n"+
     			"      -- having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)			\n" + 
     			"       order by g.mingc,fahrq";
         
        ResultSetList rsl = con.getResultSetList(sql);  
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("fahb");
		egu.setDefaultsortable(false);
		egu.setWidth(Locale.Grid_DefaultWidth+"-5");// ����ҳ��Ŀ��,������������ʱ��ʾ������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("fahdw").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikdw").setHeader(Locale.meikxxb_id_fahb);
		
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("yewlx").setHeader("ҵ������");
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("chec").setHeader("����");
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("yingk").setHeader("ӯ��");
		egu.getColumn("yuns").setHeader("����");
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("hetbh").setHeader("��ͬ��");
		
		egu.getColumn("Std").setHeader("Std(%)");
		egu.getColumn("Qnetarzh").setHeader("Qnetar(ǧ��ǧ��)");
		egu.getColumn("Mt").setHeader("Mt(%)");
		egu.getColumn("Mad").setHeader("Mad(%)");
		egu.getColumn("Aar").setHeader("Aar(%)");
		egu.getColumn("Aad").setHeader("Aad(%)");
		egu.getColumn("Ad").setHeader("Ad(%)");
		egu.getColumn("Vad").setHeader("Vad(%)");
		egu.getColumn("Vdaf").setHeader("Vdaf(%)");
		egu.getColumn("Stad").setHeader("Stad(%)");	
		egu.getColumn("Star").setHeader("Star(%)");	
		egu.getColumn("Had").setHeader("Had(%)");
		egu.getColumn("Var").setHeader("Var(%)");
		egu.getColumn("Qnetar").setHeader("Qnetar(mj/kg)");
		egu.getColumn("Qbad").setHeader("Qbad(mj/kg)");
		egu.getColumn("Qgrad").setHeader("Qgrad(mj/kg)");
		
		egu.getColumn("Std").setHidden(true);
		egu.getColumn("Mt").setHidden(true);
		egu.getColumn("Mad").setHidden(true);
//		egu.getColumn("Aar").setHidden(true);
		egu.getColumn("Aad").setHidden(true);
		egu.getColumn("Ad").setHidden(true);
		egu.getColumn("Vad").setHidden(true);
		egu.getColumn("Vdaf").setHidden(true);
//		egu.getColumn("Star").setHidden(true);	
		egu.getColumn("Stad").setHidden(true);		
		egu.getColumn("Had").setHidden(true);
		egu.getColumn("Qnetar").setHidden(true);
		egu.getColumn("Qbad").setHidden(true);
		egu.getColumn("Qgrad").setHidden(true);
		egu.getColumn("lie_id").setHidden(true);
//		��ϵͳ��Ϣ���ж�̬���ø��ӵ���ʾָ��
		if(((Visit) getPage().getVisit()).isFencb()){
        	
			strDiancxxb_id=String.valueOf(MainGlobal.getProperId(this.getFencbModel(),this.getChangb()));
        }else{
        	
        	strDiancxxb_id=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
        }
		
		String Xitxxvalue="";	
		String Xitxx[]= null;
		Xitxxvalue=MainGlobal.getXitxx_item("����", Locale.jiesxzfjxszb_xitxx, 
				strDiancxxb_id, "");
		
		Xitxx = Xitxxvalue.split(",");
		
		for(int i=0;i<Xitxx.length;i++){
			
			if(egu.getColumn(Xitxx[i])!=null){
				
				egu.getColumn(Xitxx[i]).setHidden(false);
			}
		}
//		��ϵͳ��Ϣ���ж�̬���ø��ӵ���ʾָ��_end
		
		
//		ͨ��jiesszb�н���ָ����ʾ�ֶ���Ŀ���ã����ý���ѡ�����ʾָ��
		String xszb[]=jies_Jsxszb.split(",");
		
		for(int i=0;i<xszb.length;i++){
			
			egu.getColumn(xszb[i]).setHidden(false);
		}
		
		/*
		
//		ͨ���������������ֶε���ʾ���
		
		if(this.getJieslxValue().getId()==Locale.liangpjs_feiylbb_id
				||this.getJieslxValue().getId()==Locale.guotyf_feiylbb_id
				||this.getJieslxValue().getId()==Locale.dityf_feiylbb_id){
//			��Ʊ����
			
//			������ʾ�ֶ�
			egu.getColumn("yihd_ches").setHeader("�Ѻ˶Գ���");
			egu.getColumn("yihd_biaoz").setHeader("�Ѻ˶�Ʊ��");
			egu.getColumn("Huophdzt").setHeader("��Ʊ�˶�״̬");
			egu.getColumn("Huophdzt").setRenderer("renderHdzt");
			
//			�����ֶο��
			egu.getColumn("yihd_ches").setWidth(70);
			egu.getColumn("yihd_biaoz").setWidth(70);
			egu.getColumn("Huophdzt").setWidth(80);
		}
		   */
		// �趨�г�ʼ���
//		egu.getColumn("id").setWidth(80);
		egu.getColumn("fahdw").setWidth(100);
		egu.getColumn("meikdw").setWidth(100);
		
		
		egu.getColumn("fahrq").setWidth(130);
		egu.getColumn("daohrq").setWidth(130);
		egu.getColumn("yewlx").setWidth(80);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yingk").setWidth(60);
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("hetbh").setWidth(70);
		egu.getColumn("Std").setWidth(55);
//		egu.getColumn("Star").setWidth(55);
		egu.getColumn("Mt").setWidth(55);
		egu.getColumn("Mad").setWidth(60);
		egu.getColumn("Aar").setWidth(60);
		egu.getColumn("Aad").setWidth(60);
		egu.getColumn("Ad").setWidth(60);
		egu.getColumn("Vad").setWidth(60);
		egu.getColumn("Vdaf").setWidth(60);
		egu.getColumn("Stad").setWidth(60);		
		egu.getColumn("Had").setWidth(60);
		egu.getColumn("Qnetar").setWidth(80);
		egu.getColumn("Qbad").setWidth(80);
		egu.getColumn("Qgrad").setWidth(100);

		egu.setGridType(ExtGridUtil.Gridstyle_Read);// �趨grid���Ա༭
		egu.addPaging(0);// ���÷�ҳ
        
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		egu.addTbarText(biaoq);
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		// ������
		egu.addTbarText(Locale.gongysb_id_fahb);
		String condition=" and "+rb1()+">=to_date('"+this.getRiq1()+"','yyyy-MM-dd') and "+rb1()+"<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') ";
		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");
		

		egu.addTbarText("-");// ���÷ָ���
	
		//���ձ�Ÿ��ݲ�ͬ�û���������
		//JieslxDropDown
		egu.addTbarText("��������:");
		ComboBox comb4 = new ComboBox();
		comb4.setEditable(true);
		//JieslxDropDown
		comb4.setTransform("JieslxDropDown");
		comb4.setId("Jieslx");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(80);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Jieslx.on('select',function(){document.forms[0].submit();});");
		
		egu.addTbarText("-");
		egu.addTbarText("��ͬ��:");
		ComboBox comb5 = new ComboBox();
		comb5.setEditable(true);
		comb5.setTransform("HthDropDown");
		comb5.setId("Heth");
		comb5.setEditable(false);
		comb5.setLazyRender(true);// ��̬��
		comb5.setWidth(100);
		comb5.setListWidth(150);
		comb5.setReadOnly(true);
		egu.addToolbarItem(comb5.getScript());
		

		// �趨�������������Զ�ˢ��
//		egu.addOtherScript("faz.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		//egu.addToolbarButton(GridButton.ButtonType_ShowDIV, null);
		egu.addToolbarItem("{"+new GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		egu.addTbarText("-");	
		egu.addToolbarItem("{"+new GridButton("����","function(){ if(win){ win.show(this);"+"}"
//				+ " win.show(this);	\n" 
				+ " \tif(document.getElementById('TEXT_CHANGB_VALUE').value!=''){	\n" 
		 		+ "		\tChangb.setRawValue(document.getElementById('TEXT_CHANGB_VALUE').value);	\n"
		 		+ "	\t}	\n"
//			 	+ " \tif(document.getElementById('TEXT_YANSBH_VALUE').value!=''){	\n"
//			 	+ "		\tyansbh.setRawValue(document.getElementById('TEXT_YANSBH_VALUE').value);	\n"
//			 	+ "	\t}	\n"
			 	+ " \tif(document.getElementById('TEXT_YewlxSelect_VALUE').value!=''){	\n"
			 	+ "		\tyewlxCo.setRawValue(document.getElementById('TEXT_YewlxSelect_VALUE').value);	\n"
			 	+ "	\t}	\n"
				+ "}").getScript()+"}");  
        egu.addTbarText("-");
        egu.addToolbarButton("����",GridButton.ButtonType_SubmitSel_condition,"JiesButton","if(Heth.getRawValue()=='��ѡ��'&&Jieslx.getRawValue()!='�����˷�'&&Jieslx.getRawValue()!='�����˷�'){	\n	"
        																	+ " Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���ͬ��');	\n"
        																	+ " return ;	\n"
        																	+ " }");
        
        egu.addOtherScript("function gridDiv_save(rec){	\n" +
				"	\tif(rec.get('FAHDW')=='�ϼ�'){	\n" +
				"	\treturn 'continue';	\n" +
				"	\t}}");

		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
	/*	StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// sb.append("if (e.row==7){e.cancel=true;}");//�趨��8�в��ɱ༭,�����Ǵ�0��ʼ��
		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");// �趨��Ӧ���в��ɱ༭
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");// �趨�糧��Ϣ�в��ɱ༭
		sb.append("});");
		
		egu.addOtherScript(sb.toString());*/
        //ҳ���Զ�����
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(own,row,e){ \n"
				+ " \tvar ches=0,biaoz=0,yihd_ches=0,yihd_biaoz=0,yingk=0,yuns=0,jingz=0,std=0, mt=0,mad=0,aar=0,aad=0,ad=0,vad=0,vdaf=0,stad=0,had=0,qnet_ar=0,qbad=0,qgrad_daf=0;	\n"
				+ "	var rec = gridDiv_grid.getSelectionModel().getSelections();	\n"	
				+ " for(var i=0;i<rec.length;i++){ \n"
				+ " \tif(''!=rec[i].get('ID')){ \n"
				+ "		\tches+=eval(rec[i].get('CHES'));	\n"		
				+ "		\tbiaoz+=eval(rec[i].get('BIAOZ'));	\n"
				+ "		\tyihd_ches+=eval(rec[i].get('YIHD_CHES'));	\n"		
				+ "		\tyihd_biaoz+=eval(rec[i].get('YIHD_BIAOZ'));	\n"	
				+ " 	\tyingk+=eval(rec[i].get('YINGK')); \n" 
				+ " 	\tyuns+=eval(rec[i].get('YUNS'));	\n"	
				+ " 	\tjingz+=eval(rec[i].get('JINGZ'));	\n"
				+ " 	\tstd=eval(rec[i].get('STD'));	\n"
				+ "		\tmt=eval(rec[i].get('MT'));"
				+ " 	\tmad=eval(rec[i].get('MAD')); \n" 
				+ " 	\taar=eval(rec[i].get('AAR'));	\n"	
				+ " 	\taad=eval(rec[i].get('AAD'));	\n"
				+ " 	\tad=eval(rec[i].get('AD'));	\n"
				+ "		\tvad=eval(rec[i].get('VAD'));"
				+ " 	\tvdaf=eval(rec[i].get('VDAF')); \n" 
				+ " 	\tstad=eval(rec[i].get('STAD'));	\n"	
				+ " 	\thad=eval(rec[i].get('HAD'));	\n"
				+ " 	\tqnet_ar=eval(rec[i].get('QNET_AR'));	\n"
				+ "		\tqbad=eval(rec[i].get('QBAD'));"
				+ " 	\tqgrad_daf=eval(rec[i].get('QGRAD_DAF')); \n" 

				+ " \t}	\n"
				+ " }	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_CHES',yihd_ches);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_BIAOZ',yihd_biaoz);	\n"
				+ " });		\n");
		
		
//		��ѡ��ȫѡ�����¼���ѡ����ĺϼ�ֵ
		egu.addOtherScript("gridDiv_grid.on('click',function(){		\n"
						+ "		reCountToolbarNum(this);			\n"
						+ "});		\n");
		
		
//		���¼���ѡ����ĺϼ�ֵ
		egu.addOtherScript(" function reCountToolbarNum(obj){	\n "
 				+ " \tvar rec;	\n"
 				+ " \tvar ches=0,biaoz=0,yihd_ches=0,yihd_biaoz=0,yingk=0,yuns=0,jingz=0;	\n"
 				+ " \trec = obj.getSelectionModel().getSelections();				\n"		
 				+ " \tfor(var i=0;i<rec.length;i++){								\n" 
 				+ " 	\tif(0!=rec[i].get('ID')){									\n"
 				+ " 		\tches+=eval(rec[i].get('CHES'));						\n"
				+ " 		\tbiaoz+=eval(rec[i].get('BIAOZ'));						\n" 
				+ " 		\tyingk+=eval(rec[i].get('YINGK'));						\n"
				+ " 		\tyuns+=eval(rec[i].get('YUNS'));						\n"	
				+ " 		\tjingz+=eval(rec[i].get('JINGZ'));						\n"
				+ " 		\tyihd_ches+=eval(rec[i].get('YIHD_CHES'));				\n"
				+ " 		\tyihd_biaoz+=eval(rec[i].get('YIHD_BIAOZ'));			\n"	
				+ " 	\t}															\n"
				+ " \t}																\n"
				+ " if(gridDiv_ds.getCount()>0){									\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_CHES',yihd_ches);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_BIAOZ',yihd_biaoz);	\n"
				+ "	}\n"
				+ " \t} \n");
		
//		��ѡ���ĸ�ֵ
//		egu.addOtherScript(
//				"gongysTree_treePanel.on(\"checkchange\",function(node,checked){\n" +
//				"    if(checked){\n" + 
//				"      addNode(node);\n" + 
//				"    }else{\n" + 
//				"      subNode(node);\n" + 
//				"    }\n" + 
//				"    node.expand();\n" + 
//				"    node.attributes.checked = checked;\n" + 
//				"    node.eachChild(function(child) {\n" + 
//				"      if(child.attributes.checked != checked){\n" + 
//				"        if(checked){\n" + 
//				"          addNode(child);\n" + 
//				"        }else{\n" + 
//				"          subNode(child);\n" + 
//				"        }\n" + 
//				"        child.ui.toggleCheck(checked);\n" + 
//				"              child.attributes.checked = checked;\n" + 
//				"              child.fireEvent('checkchange', child, checked);\n" + 
//				"      }\n" + 
//				"    });\n" + 
//				"  },gongysTree_treePanel);\n" + 
//				"  function addNode(node){\n" + 
//				"    var history = node.id+\",\";\n" + 
//				"    writesrcipt(node,history);\n" + 
//				"  }\n" + 
//				"\n" + 
//				"  function subNode(node){\n" + 
//				"    var history = node.id+\",\";\n" + 
//				"    writesrcipt(node,history);\n" + 
//				"  }\n" + 
//				"  function writesrcipt(node,history){\n" + 
//				"    if(gongysTree_history==\"\"){\n" + 
//				"      gongysTree_history = history;\n" + 
//				"    }else{\n" + 
//				"      var his = gongysTree_history.split(\",\");\n" + 
//				"      var reset = false;\n" + 
//				"      for(i=0;i<his.length;i++){\n" + 
//				"        if(his[i] == node.id){\n" + 
//				"          his[i] = \"\";\n" + 
//				"          reset = true;\n" + 
//				"          break;\n" + 
//				"        }\n" + 
//				"      }\n" + 
//				"	   gongysTree_history += history;\n" +
//				"    }\n" + 
//				"  }");
		
		// ---------------ҳ��js�������--------------------------
		egu.addOtherScript(Strtmpfunction);
		setExtGrid(egu);
		con.Close();
	}
	
	private String getSupplyTable(String DateType){
		
		String supplytable="";
	/*	long feiylbb_id=0;
		String where="";
//      ʹ�����ձ��
    	if(this.getJieslxValue().getId()==1 		//        	��Ʊ����
    			||this.getJieslxValue().getId()==3	//        	�����˷�
    			||this.getJieslxValue().getId()==4){//        	�����˷�
    		
    		if(getJieslxValue().getId()==1||getJieslxValue().getId()==3){
    			
    			feiylbb_id=Locale.guotyf_feiylbb_id;
    		}else{
    			
    			feiylbb_id=getJieslxValue().getId();
    		}
    		
    		supplytable=",yansbhb ys,(select c.id as chepb_id,yfzl.jifzl from chepb c,yunfjszlb yfzl				\n"
                    + "       						where c.id=yfzl.chepb_id																			\n"
                    + "       						and yfzl.feiylbb_id="+feiylbb_id+") yfzl,									\n" 
                    
                    + " (select distinct cp.id,decode(yfzl.jifzl,null,nvl(cp.biaoz,0),yfzl.jifzl) as yihd_biaoz 			\n" 
					+ " 	from chepb cp,danjcpb dj,yunfdjb yf,(select c.id as chepb_id,yfzl.jifzl from chepb c,yunfjszlb yfzl				\n"
                    + "       where c.id=yfzl.chepb_id																			\n"
                    + "       and yfzl.feiylbb_id="+feiylbb_id+") yfzl,fahb f,meikxxb m,gongysb g,yansbhb ys,zhilb z 		\n"
					+ " 	where cp.id=dj.chepb_id and f.id=cp.fahb_id 															\n"	 
                    + "     and f.meikxxb_id=m.id and f.gongysb_id=g.id	and dj.yunfdjb_id=yf.id and yf.feiylbb_id="+feiylbb_id
                    + " 	and cp.id=yfzl.chepb_id(+) "+this.getWhere(DateType)
                    + " ) yihd,\n" +

                    "	(select distinct cp.id,dj.yunfjsb_id\n" +
                    "                  from chepb cp,yunfdjb yd,danjcpb dj,fahb f,zhilb z,yansbhb ys,\n" +
                    "						gongysb g,meikxxb m\n" + 
                    "                  where yd.id=dj.yunfdjb_id\n" + 
                    "                        and dj.chepb_id=cp.id\n" + 
                    "                        and f.id=cp.fahb_id\n" + 
                    "						 and f.gongysb_id=g.id\n" +
                    "						 and f.meikxxb_id=m.id\n" +
                    "                        and yd.feiylbb_id="+feiylbb_id+"\n" +this.getWhere(DateType) + 
                    "     ) djcp";

    	}else{
    		
    		supplytable=",yansbhb ys ";
    	}*/
		
		return supplytable;
	}
	
	private String getSupplyWhere(){
		
		String supplywhere="";
		long feiylbb_id=0;
		
		if(this.getJieslxValue().getId()==1			//��Ʊ����
				||this.getJieslxValue().getId()==3	//�����˷�
				||this.getJieslxValue().getId()==4	//�����˷�
				){
			
			if(getJieslxValue().getId()==1||getJieslxValue().getId()==3){
				
				feiylbb_id=Locale.guotyf_feiylbb_id;
			}else{
				
				feiylbb_id=getJieslxValue().getId();
			}
			
			supplywhere="	and cp.id=yihd.id(+)\n" +
					"       and cp.id=djcp.id(+)\n" + 
					"       and (djcp.yunfjsb_id is null or djcp.yunfjsb_id=0) \n" + 
					"		and cp.id=yfzl.chepb_id(+) \n ";

//			and (yd.feiylbb_id="+feiylbb_id+" or yd.feiylbb_id is null)
		}
		
		return supplywhere;
	}
	
	private String getSupplyCol(){
		
		String supplycol="";
		
		if(this.getJieslxValue().getId()==1
				||this.getJieslxValue().getId()==3
				||this.getJieslxValue().getId()==Locale.dityf_feiylbb_id){
//			��Ʊ����
			supplycol=" decode(g.mingc,null,'',count(yihd.id)) as yihd_ches,decode(g.mingc,null,'',sum(nvl(yihd.yihd_biaoz,0))) as yihd_biaoz,	\n" +
					" decode(g.mingc,null,'',case when (count(cp.id)-count(yihd.id))=0 then '�����' else 'δ���' end) as Huophdzt,	\n";
		}
		
		return supplycol;
	}
	
	private String getWhere(String DateType){
		
		String where="";
		/*
		//��Ӧ��
//		2008-12-19���޸ģ�����û�û���������������г�ȫ���ķ�����Ϣ
		if(!this.getYansbh().equals("��ѡ��")&&!this.getYansbh().equals("")){
        	
//			�����ձ��
        	where=" and f.yansbhb_id="+MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh());
        	
        	if(((Visit) getPage().getVisit()).isFencb()){
            	
            	where+="and f.diancxxb_id="+MainGlobal.getProperId(this.getFencbModel(),this.getChangb());
            }else{
            	
            	where+="and f.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id();
            }
        	
        	if(getJieslxValue().getId()==1){	//��Ʊ����
        		
        		where+=" and f.jiesb_id=0	\n";
        	}else if(getJieslxValue().getId()==2){	//ú�����	
        		
        		where+=" and f.jiesb_id=0	\n";
        	}else {	
        		
        		where+=" and ((dj.yunfjsb_id=0 or yd.feiylbb_id<>"+getJieslxValue().getId()+" or yd.feiylbb_id is null) or dj.yunfdjb_id is null)	\n";
        	}
        	
        }else {
        	
//        	û�����ձ��
        	if(!(getTreeid()==null||getTreeid().equals("0"))){
    			
    			where+=" and (m.id="+getTreeid()+" or g.id="+getTreeid()+")	\n";
    		}
            	
        	if(getJieslxValue().getId()==Locale.liangpjs_feiylbb_id){	//��Ʊ����
        		
        		where+=" and f.jiesb_id=0 and f.zhilb_id=z.id and z.liucztb_id=1 	\n";
        	}else if(getJieslxValue().getId()==Locale.meikjs_feiylbb_id){	//ú�����	
        		
        		where+=" and f.jiesb_id=0 and f.zhilb_id=z.id and z.liucztb_id=1	\n";
        	
        	}else if(getJieslxValue().getId()==Locale.guotyf_feiylbb_id
        			||getJieslxValue().getId()==Locale.dityf_feiylbb_id){		//�˷ѽ���
//        		where+=" and (dj.yunfjsb_id=0 or dj.yunfdjb_id is null) and f.zhilb_id=z.id(+) ";
        		if(MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw())>-1){
        			
        			where+=" and cp.yunsdwb_id="+MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw());
        		}
        		
        		where+=" and f.zhilb_id=z.id and z.liucztb_id=1 \n";
        	}
        	*/
		    //�糧����
	         	long dcid=0; 
		     if((dcid=MainGlobal.getProperId(getFencbModel(), this.getChangb()))>-1){
		    	        int jib=this.getJib(dcid);
		    	  if(jib==2){
		    	      where+=" and (di.id="+dcid+" or di.fuid="+dcid+")\n";
		    	  }else if(jib==3){
		    		  where+=" and f.diancxxb_id="+dcid+"\n";
		    	  }
		     }	 
		     
		
        	//Ʒ��
        	if(MainGlobal.getProperId(getPinzModel(), this.getPinz())>-1){
        		where+=" and f.pinzb_id = " + MainGlobal.getProperId(getPinzModel(), this.getPinz()) +"\n";
        	}
        	
        	//��λ����
//        	����
        	if(!this.getQd().equals("")&&!this.getQd().equals("0")){
        		where+=" and z.qnet_ar>="+this.getQd()+"\n";
        	}
//        	����
        	if(!this.getQu().equals("")&&!this.getQu().equals("0")){
        		where+=" and z.qnet_ar<="+this.getQu()+"\n";
        	}
        	//Std
//        	����
        	if(!this.getStdd().equals("")&&!this.getStdd().equals("0")){
        		where+=" and z.std>="+this.getStdd()+"\n";
        	}
//        	����
        	if(!this.getStdu().equals("")&&!this.getStdu().equals("0")){
        		where+=" and z.std<="+this.getStdu()+"\n";
        	}
        	//Star
//        	����
        	if(!this.getStard().equals("")&&!this.getStard().equals("0")){
        		where+=" and z.star>="+this.getStard()+"\n";
        	}
//        	����
        	if(!this.getStaru().equals("")&&!this.getStaru().equals("0")){
        		where+=" and z.star<="+this.getStaru()+"\n";
        	}
      	

//            if(((Visit) getPage().getVisit()).isGSUser()){
//            	
//            	where+="and f.diancxxb_id="+MainGlobal.getProperId(this.getFencbModel(),this.getChangb());
//            }else{
//            	
//            	where+="and f.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id();
//            }
//            
//          �����ж�
            
            where+="and "+DateType+" >= to_date('"+getRiq1()+"', 'yyyy-mm-dd')				\n" + 
            	"	and "+DateType+" < to_date('"+getRiq2()+"', 'yyyy-mm-dd')+1				\n";
            
            /*
//            ʹ�����ձ��
            if(MainGlobal.getXitxx_item("����", Locale.shiyysbh_jies, String.valueOf(((Visit) getPage().getVisit()).isFencb()?MainGlobal.getProperId(this.getFencbModel(),this.getChangb()):((Visit) getPage().getVisit()).getDiancxxb_id()), "��").equals("��")){
            	
            	where+=" and f.yansbhb_id=ys.id(+) ";
            	
            	if(this.getTree()!=null&&getHthValue().getId()!=-1){
            		
            		if(Jiesdcz.getJiessz_item(((Visit) getPage().getVisit()).isFencb()?MainGlobal.getProperId(this.getFencbModel(),this.getChangb()):((Visit) getPage().getVisit()).getDiancxxb_id(), 
            				Long.parseLong(MainGlobal.getLeaf_ParentNodeId(this.getTree(), this.getTreeid())),this.getHthValue().getId() ,Locale.jieszbtz_jies, "��").equals("��")){
            		
            			//where+=" and ys.liucztbid=1";	
            		}
            	}
            }
        }
		
		
		*/
		
		return where;
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
			visit.setObject1(null);
			visit.setString1("");	//Rbvalue��������ѡ��
			visit.setString2("");	//Treeid
			visit.setString3("");	//���e
			visit.setString4("");	//���ձ��
			visit.setString5("");	//riq1
			visit.setString6("");	//riq2
			visit.setString7("");	//����۶���
			visit.setString8("");	//�������е���id
			visit.setString9("");	//���ú˶�״̬
			visit.setString10("");	//���䵥λ
			visit.setString12("");	//�ϴν�����
			visit.setString13("");	//fahb_id
			
			visit.setLong10(0);       //ҵ�����ͱ�
			visit.setLong1(0);		//�糧��Ϣ��id
			visit.setLong2(0);		//��������
			visit.setLong3(0);		//��Ӧ�̱�id
			visit.setLong8(0);		//��ͬ��id
			visit.setLong9(0);		//���䵥λ��id
			visit.setList1(null);
			setJieslxValue(null);	//3
			setJieslxModel(null);
			getJieslxModels();		//3
			setHthValue(null);		//4
			setHthModel(null);
			getHthModels();			//4
//			setYansbhValue(null);	//2
//			setYansbhModel(null);
//			getYansbhModels();		//2
			setFencbValue(null);	//5
			setFencbModel(null);
			getFencbModels();		//5
			
			setYunsdwValue(null);	//6
			setYunsdwModel(null);	//6
			getYunsdwModels();		//6
			
			setPinzValue(null);		//7
			setPinzModel(null);		//7
			getPinzModels();		//7
			
			setYewlxValue(null);
			setYewlxModel(null);
			this.getYewlxOpitions();
			visit.setboolean1(true);	//����change
			visit.setboolean2(false);	//������λchange����������,���䵥λ,Ʒ��
			this.setChangb(visit.getDiancmc());
			this.setYewlx(this.getYewlxValue().getValue());
			((Visit) this.getPage().getVisit()).setString11(""); //����ҳ����ز���
			if(cycle.getRequestContext().getParameters("lx")!=null) {
				
				((Visit) this.getPage().getVisit()).setString11(cycle.getRequestContext().getParameters("lx")[0]);
	        }
		}

		if(visit.getboolean1()||visit.getboolean2()){
		
			visit.setboolean1(false);
			visit.setboolean2(false);
//			visit.setString9("");
			getHthModels();
//			getYansbhModels();
			getYunsdwModels();
			this.getPinzModels();
		}

		getSelectData();
	}


     //��������
	public boolean _Jieslxchange = false;
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
			
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
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

	public IPropertySelectionModel getJieslxModels() {
		
		String sql ="select id, mingc from feiylbb where (leib<2 or leib=3) order by id";
			
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
//	�������� end
    public String rb1(){
    	if(getRbvalue()==null||getRbvalue().equals("")){
    		
    		
    	    	return "fh.fahrq";
    	    	
    		
    	}else{
    		if(getRbvalue().equals("fahrq")){
    			return "fh.fahrq";
    		}
    		else return "fh.daohrq";
    	}
    }
	/*// ���ձ��
	public boolean _Yansbhchange = false;

	public IDropDownBean getYansbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getYansbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setYansbhValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setYansbhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getYansbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getYansbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getYansbhModels() {
		
		String sql="";
		
		if(this.getJieslxValue().getId()==1||getJieslxValue().getId()==2){
//			��Ʊ�����ú�����
			
			sql = "select distinct y.id,y.bianm from yansbhb y, chepb cp, fahb fh where fh.yansbhb_id=y.id and cp.fahb_id=fh.id  " 
				+ " and fh.jiesb_id=0 and fh.diancxxb_id="+String.valueOf(((Visit) getPage().getVisit()).isFencb()?MainGlobal.getProperId(getFencbModel(), 
						this.getChangb()):((Visit) getPage().getVisit()).getDiancxxb_id())+" order by bianm";
		}else{
//			�˷ѽ���
			sql = "select distinct y.id,y.bianm from yansbhb y, chepb cp, danjcpb dj,fahb fh where fh.yansbhb_id=y.id and cp.id=dj.chepb_id and cp.fahb_id=fh.id " 
				+ " and dj.yunfjsb_id=0 and fh.diancxxb_id="+String.valueOf(((Visit) getPage().getVisit()).isFencb()?MainGlobal.getProperId(getFencbModel(), 
						this.getChangb()):((Visit) getPage().getVisit()).getDiancxxb_id())+" order by bianm";
		}

			((Visit) getPage().getVisit())
			.setProSelectionModel2(new IDropDownModel(sql, "��ѡ��"));
	return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}*/
	//��ͬ�� 
	public boolean _Hthchange = false;

	public IDropDownBean getHthValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getHthModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setHthValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setHthModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getHthModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getHthModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getHthModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

//			һ��ú����û�й�Ӧ��
//			�����ж�ϵͳ����û�к�ͬ
//			2008-10-17�޸ķ���������ͬδ������ʱ���Խ����Խ��㣬�����ܱ���
			
			String strHetspzt="";
			String sql="";
			String stryunsdw="";
			//String stryansbh="";//���ձ���ж�����
			String riq="fahrq";
			
			if(getRbvalue().equals("fahrq")||this.getRbvalue().equals("")){
				
				
				riq="fahrq";
			}else if(getRbvalue().equals("daohrq")){
				riq="daohrq";
			}
			
			List.add(new IDropDownBean(-1, "��ѡ��"));
			
			if(MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw())>-1){
//				ѡ�������䵥λ
				stryunsdw=" and yunsdwb_id="+MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw());
			}else{
				
				stryunsdw="";
			}
			
//			if(MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh())>-1){
//				
//				stryansbh=" and f.yansbhb_id="+MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh());
//			}
			 String where ="";
			 String het_where="";
				long dcid=0; 
				
			     if((dcid=MainGlobal.getProperId(getFencbModel(), this.getChangb()))>-1){
			    	        int jib=this.getJib(dcid);
			    	  if(jib==2){
			    	      where=" and (di.id="+dcid+" or di.fuid="+dcid+") and cangkb_id<>1\n";
			    		  het_where=" and di.id="+dcid;
			    	  }else if(jib==3){
			    		  where=" and di.id="+dcid+"\n";
			    		  het_where=" and di.id=(select fuid from diancxxb where id="+dcid+")";
			    	  }
			     }	
			if(this.getTreeid()==null||this.getTreeid().equals("0")){	//ȫ��
				
//				if(!((Visit) getPage().getVisit()).isGSUser()){
					
//					�ֳ���
					if(getJieslxValue().getId()!=Locale.guotyf_feiylbb_id
							&&getJieslxValue().getId()!=Locale.dityf_feiylbb_id){
						  
					   
						
						
						sql = "  select distinct h.id,h.hetbh\n"
								+ "from hetb h,hetslb hs,diancxxb di\n "
								+ "where  di.id=h.diancxxb_id     and \n"
								+ "h.id = hs.hetb_id(+) and h.gongysb_id in (\n"
								+ "   select distinct decode(gongysb_id,12,20,gongysb_id) as gongysb_id from fahb f,diancxxb di where fahrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n "
								+ "  and fahrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') and f.diancxxb_id=di.id\n"
								+ "  and liucztb_id=0    \n"
								+ where
								+ ") \n"
								+ het_where
								+ "and h.leib=2 and not(to_date('"+this.getRiq1()+"','yyyy-MM-dd') >guoqrq or to_date('"+this.getRiq2()+"','yyyy-MM-dd')<qisrq)   "
								+ " order by hetbh ";
						
						
						
						
//						
//						sql = " select distinct h.id,h.hetbh from hetb h, hetslb hs,diancxxb d where d.id= h.diancxxb_id\n"
//							+ where
//							+ " and h.id = hs.hetb_id(+) and h.gongysb_id in (select distinct gongysb_id from fahb f,diancxxb_id di where di.id=f.diancxxb_id";
//								
//								sql+=riq+">=to_date('"+this.getRiq1()+"','yyyy-MM-dd') \n and "
//									+ riq+"<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') and \n ";
//							sql+= " liucztb_id=0 "+where+"  \n"
//								+stryansbh+" ) \n";
//							
//								sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') order by hetbh ";
								
					}else{
						

					sql="	 select distinct hetys.id, hetbh\n"+
						 "  from hetysjgb, hetys, diancxxb di\n"+
						"  where hetysjgb.hetys_id = hetys.id\n"+
						 "   and di.id = hetys.diancxxb_id\n"+
						  "  and meikxxb_id in (select distinct meikxxb_id\n"+
						   "                      from fahb f, diancxxb di\n"+
						    "                    where fahrq >= to_date('"+this.getRiq1()+"', 'yyyy-MM-dd')\n"+
						     "                     and fahrq <= to_date('"+this.getRiq2()+"', 'yyyy-MM-dd')\n"+
						      "                    and f.diancxxb_id = di.id\n"+
						      where+
						       "                   )\n"+
						       het_where+
						    "and not (to_date('"+this.getRiq1()+"', 'yyyy-MM-dd') > guoqrq or\n"+
						     "    to_date('"+this.getRiq2()+"', 'yyyy-MM-dd') < qisrq)\n"+
						  "order by hetbh\n";
						
//						sql=" select distinct hetys.id,hetbh from hetysjgb,hetys,diancxxb di where hetysjgb.hetys_id=hetys.id \n";
//							
//								sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') ";
//							
//						sql+=" and meikxxb_id in (select distinct meikxxb_id from fahb f,diancxxb di where \n ";
//							
//								
//								sql+=riq+">=to_date('"+this.getRiq1()+"','yyyy-MM-dd') \n and "
//									+ riq+"<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n ";
//							
//						sql+=dcid+" ) \n"
//							+ stryunsdw+dcid+" 	\n" 
//							+ " order by hetbh ";
					}
					
//				}else{
//					
//					if(getJieslxValue().getId()!=Locale.guotyf_feiylbb_id
//							&&getJieslxValue().getId()!=Locale.dityf_feiylbb_id){
//						
//						sql = "select distinct h.id,h.hetbh from hetb h,hetslb hs,diancxxb di where h.diancxxb_id=di.id and  h.id = hs.hetb_id(+) and\n "
//							+ where
//							+ " and h.gongysb_id in (select distinct gongysb_id from fahb f where \n";
//							
//							if(stryansbh.equals("")){
//								
//								sql+=riq+">=to_date('"+this.getRiq1()+"','yyyy-MM-dd') \n and "
//									+ riq+"<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') and \n ";
//							}
//							
//							
//						sql+= " liucztb_id=0 "+stryansbh+") \n";
//							
//							sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') order by hetbh ";
//						
//					}else{
//						
//						sql = " select distinct hetys.id,hetbh from hetysjgb,hetys where hetysjgb.hetys_id=hetys.id 	\n";
//								
//								sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') ";
//						sql+= " and meikxxb_id in (select distinct meikxxb_id from fahb where \n ";
//							
//							if(stryansbh.equals("")){
//								
//								sql+=riq+">=to_date('"+this.getRiq1()+"','yyyy-MM-dd') \n and "
//									+ riq+"<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') and\n ";
//							}
//							
//						sql+=" liucztb_id=0) \n"
//							+ stryunsdw+stryansbh+" and diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" 	\n" 
//							+ " order by hetbh ";
//					}
//				}
			
			}else{
//				ѡ���˹�Ӧ��
				String mstr_gongys_id=MainGlobal.getLeaf_ParentNodeId(this.getTree(), this.getTreeid());
				
				if(mstr_gongys_id.equals("")){
					
					setMsg("��ú��û�ж�Ӧ�Ĺ�Ӧ����Ϣ��");
				}else{
					if(mstr_gongys_id.equals("12")){//��ɽ�ô�ͬúҵ�ĺ�ͬ
						mstr_gongys_id="20";
					}
//					if(((Visit) getPage().getVisit()).isGSUser()){
						
						if(getJieslxValue().getId()!=Locale.guotyf_feiylbb_id
								&&getJieslxValue().getId()!=Locale.dityf_feiylbb_id){
							
							sql = "  select distinct h.id,h.hetbh\n"
								+ "from hetb h,hetslb hs,diancxxb di\n "
								+ "where  di.id=h.diancxxb_id     and \n"
								+ "h.id = hs.hetb_id(+) and h.gongysb_id="+mstr_gongys_id
								+ het_where
								+ "and h.leib=2 and not(to_date('"+this.getRiq1()+"','yyyy-MM-dd') >guoqrq or to_date('"+this.getRiq2()+"','yyyy-MM-dd')<qisrq)   "
								+ " order by hetbh ";
						
							
							
//							sql = "select distinct h.id,h.hetbh from hetb h,hetslb hs where h.id=hs.hetb_id(+) and (h.diancxxb_id="
//								+MainGlobal.getProperId(getFencbModel(),this.getChangb())
//								+" or hs.diancxxb_id = "
//								+MainGlobal.getProperId(getFencbModel(),this.getChangb())
//								+") and gongysb_id="+mstr_gongys_id+" \n ";
//								
//									
//									sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') ";
//							sql+=" order by hetbh ";
							
						}else{
						sql=	" select distinct hetys.id,hetbh from \n"+
							" hetysjgb,hetys,diancxxb di where hetysjgb.hetys_id=hetys.id \n"+
							"  and 	di.id=hetys.diancxxb_id\n"+
							" and meikxxb_id in	\n"+
							" (select mk.id	\n"+	
							" from gongysmkglb glb,gongysb gys,meikxxb mk\n"+		
							" where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id\n"+	
							 "and (gys.id=decode("+getTreeid()+",12,20,"+getTreeid()+") or mk.id=decode("+getTreeid()+",12,20,"+getTreeid()+")))"+ het_where +	
							 "and not (to_date('"+this.getRiq1()+"', 'yyyy-MM-dd') > guoqrq or\n"+
						    "    to_date('"+this.getRiq2()+"', 'yyyy-MM-dd') < qisrq)\n"+
							 "order by hetbh"; 
							
//							sql = " select distinct hetys.id,hetbh from hetysjgb,hetys where hetysjgb.hetys_id=hetys.id 	\n"
//								+ " and meikxxb_id in	\n" 
//								+ " (select mk.id		\n" 
//								+ " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
//								+ " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
//								+ " and (gys.id="+getTreeid()+" or mk.id="+getTreeid()+")) and diancxxb_id="+MainGlobal.getProperId(getFencbModel(),this.getChangb())+" 	\n"; 
//									
//									sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') ";
//								
//								sql+= " order by hetbh ";
						}
						
//					}else{
//						
//						if(getJieslxValue().getId()!=Locale.guotyf_feiylbb_id
//								&&getJieslxValue().getId()!=Locale.dityf_feiylbb_id){
//							
//							sql = "select distinct h.id,h.hetbh from hetb h,hetslb hs where h.id=hs.hetb_id(+) and (h.diancxxb_id="
//								+((Visit) getPage().getVisit()).getDiancxxb_id()
//								+ " or hs.diancxxb_id = "
//								+((Visit) getPage().getVisit()).getDiancxxb_id()
//								+" ) and gongysb_id="+mstr_gongys_id+" \n ";
//								sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') ";
//							sql+=" order by hetbh ";
//						}else {
//							
//							sql = " select distinct hetys.id,hetbh from hetysjgb,hetys where hetysjgb.hetys_id=hetys.id 	\n"
//								+ " and meikxxb_id in	\n" 
//								+ " (select mk.id		\n" 
//								+ " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
//								+ " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
//								+ " and (gys.id="+getTreeid()+" or mk.id="+getTreeid()+")) 	\n"
//								+ " and diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" 	\n"; 
//									
//									sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') ";
//							sql+= " order by hetbh ";
//						}
//					}
				}
			}
			ResultSetList rsl = con.getResultSetList(sql);
			if(rsl.getRows()==0&&!this.getTreeid().equals("0")){
				
				setMsg("�ù�Ӧ����ϵͳ��û�ж�Ӧ�ĺ�ͬ��");
			}
			
			while (rsl.next()) {

				List.add(new IDropDownBean(rsl.getLong("id"), rsl.getString("hetbh")));
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	


	// �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}
	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����d
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

//����
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		Visit visit=((Visit) getPage().getVisit());
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			
		int optioncount=getFencbModel().getOptionCount();
			for(int i=0;i<optioncount;i++){
				if(((IDropDownBean) getFencbModel().getOption(i)).getId()==visit.getDiancxxb_id())
				{
			         ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getFencbModel().getOption(i));
				break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {
		
		String sql="";
		    if(((Visit) getPage().getVisit()).isGSUser()){
			 sql="select id,mingc from diancxxb d where d.id="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+" or d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"   order by mingc";
		    }else{
		    	 sql="select id,mingc from diancxxb d where d.id="+
					((Visit) getPage().getVisit()).getDiancxxb_id()+" order by mingc";
		    }

			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql,""));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

//	���䵥λ_begin
	
	public IDropDownBean getYunsdwValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getYunsdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setYunsdwValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean6()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public IPropertySelectionModel getYunsdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {

			getYunsdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setYunsdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getYunsdwModels() {
		
		 String where ="";
			long dcid=0; 
		     if((dcid=MainGlobal.getProperId(getFencbModel(), this.getChangb()))>-1){
		    	        int jib=this.getJib(dcid);
		    	  if(jib==2){
		    	      where+=" and (di.id="+dcid+" or di.fuid="+dcid+") and cangkb_id<>1\n";
		    	  }else if(jib==3){
		    		  where+=" and di.id="+dcid+"\n";
		    	  }
		     }	
		
			String sql = " select distinct y.id,y.mingc from diancxxb di,yunsdwb y where y.diancxxb_id=di.id\n"
				+where 

				+"               order by y.mingc";

			((Visit) getPage().getVisit())
			.setProSelectionModel6(new IDropDownModel(sql,"ȫ��"));
			return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}
//	���䵥λ_end
//ҵ�����������˵�_begin
	public IDropDownBean getYewlxValue(){
		if(((Visit)this.getPage().getVisit()).getDropDownBean8()==null){
			IPropertySelectionModel ipsm=getYewlxModel();
		
			setYewlxValue((IDropDownBean)ipsm.getOption(0));
		}
	return	((Visit)this.getPage().getVisit()).getDropDownBean8();
	}
	public void setYewlxValue(IDropDownBean YewlxValue){
		((Visit)this.getPage().getVisit()).setDropDownBean8(YewlxValue);
	}
	public IPropertySelectionModel getYewlxModel(){
		if(((Visit)this.getPage().getVisit()).getProSelectionModel8()==null){
			getYewlxOpitions();
		}
	return	((Visit)this.getPage().getVisit()).getProSelectionModel8();
	}
	public void setYewlxModel(IPropertySelectionModel YewlxModel){
		((Visit)this.getPage().getVisit()).setProSelectionModel8(YewlxModel);
	}
	public void getYewlxOpitions(){
		JDBCcon con=new JDBCcon();
		ResultSetList rsl= con.getResultSetList("select id,mingc from yewlxb ");
		List list=new ArrayList();
		while(rsl.next()){
			list.add(new IDropDownBean(rsl.getLong("id"),rsl.getString("mingc")));
		}
		setYewlxModel(new IDropDownModel(list));
	}
//	ҵ�����������˵�_end
//	Ʒ��_begin
	
	public IDropDownBean getPinzValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getPinzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setPinzValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean7()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public IPropertySelectionModel getPinzModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

			getPinzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public void setPinzModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getPinzModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
			
			Diancxxb_id=this.getFencbValue().getId();
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
			String sql ="select id,mingc from pinzb where leib='ú'" ;

			((Visit) getPage().getVisit())
			.setProSelectionModel7(new IDropDownModel(sql,"ȫ��"));
			return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}
//	Ʒ��_end

	private String treeid;

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		// if(treeid==null||treeid.equals("")){
		// ((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit)
		// this.getPage().getVisit()).getDiancxxb_id()));
		// }
		if(((Visit) getPage().getVisit()).getString2()==null||((Visit) getPage().getVisit()).getString2().equals("")){
			
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString2(treeid);
			((Visit) getPage().getVisit()).setboolean2(true);
		}
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

	// �õ�����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
    private int getJib(long dcid){
    	int jib=0;
    	JDBCcon con=new JDBCcon();
    	ResultSetList rsl=con.getResultSetList(" select jib from diancxxb where id="+dcid+"\n");
    	if(rsl.next()){
    		jib=rsl.getInt("jib");
    	}
    	con.Close();
    	return jib;
    }
	public String getWunScript(){
		
		return "";
	}
}