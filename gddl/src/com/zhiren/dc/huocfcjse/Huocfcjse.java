package com.zhiren.dc.huocfcjse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:tzf
 * ʱ��:2009-07-30
 * ����:ʵ�� ����ⷭ��������
 */
public class Huocfcjse extends BasePage implements PageValidateListener {

	private static final String xit_mingc="��Ƥ������ֵ";//xitxxb�������ֶ�
	private static final String xit_leib="����";//xitxxb������ֶ�
	private static final int record_show=10;//ҳ�������ʾ��¼��
	
	private int nextGroup;
	
	public int getNextGroup(){
		return nextGroup;
	}
	
	public void setNextGroup(int value){
		nextGroup=value;
	}
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}
	
	public void setChange(String change) {
		Change = change;
	}
	
	private String DelChange;
	
	public String getDelChange() {
		return DelChange;
	}

	public void setDelChange(String delChange) {
		DelChange = delChange;
	}
	private String Filename;
	
	public void setFilename(String filename){
		Filename=filename;
	}
	public String getFilename(){
		return Filename;
	}
	
	
	private String Pzljz;//Ƥ���ٽ�ֵ
	
	public void setPzljz(String pzljz){
		this.Pzljz=pzljz;
	}
	
	public String getPzljz(){
		
		String sql=" select x.zhi from xitxxb x  where x.mingc='"+xit_mingc+"' and x.leib='"+xit_leib+"' and  x.zhuangt=1";
		
		String zhi="35";//Ĭ��Ϊ35��
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		return " ljz="+zhi+";";
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
//		this.setFilename("cancel");
//		this.setNextGroup(1);
		this.Pzljz="";
		
	}
	
	private boolean _RefreshChick=false;
	
	public void RefreshButton(IRequestCycle cycle){
		_RefreshChick=true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _insertChick=false;
	
	public void InsertButton(IRequestCycle cycle){
		_insertChick=true;
	}
	
	private boolean _deleteChick=false;
	
	public void DeleteButton(IRequestCycle cycle){
		_deleteChick=true;
	}
	
	private void xialkcl(){
//		this.setXiaosValue(null);
	
		
		if(this.getXiaosModel()==null) return;
		this.setXiaosModel(null);
		
		if(this.getXiaosValue()==null){
			this.setXiaosValue(null);
			return;
		}
		
		boolean flag=false;
		for (int i=0;i<this.getXiaosModel().getOptionCount();i++){
			IDropDownBean xm=(IDropDownBean)this.getXiaosModel().getOption(i);
			
			if(xm!=null){
				if( xm.getValue().equals(this.getXiaosValue().getValue()) ){
					flag=true;
					
					this.setXiaosValue(xm);
					break;
				}
			}
			
		}
		
		if(!flag){
			this.setXiaosValue(null);
		}
		
	}
	public void submit(IRequestCycle cycle) {

		if (_SaveChick) {
			_SaveChick = false;
			Save();
			this.xialkcl();
			
		}else if(_RefreshChick){
			_RefreshChick=false;
		
		}
		
		if(riqBoo){
			riqBoo=false;
			this.setXiaosValue(null);
			this.setXiaosModel(null);
		}
		this.getSelectData();
	}

	
	public void Save(){
		
//		System.out.println(this.getDelChange());
//		System.out.println(this.getChange());
		JDBCcon con=new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		String sql="";
		
		int count=0;//������������sql��䱻ִ����
		if(this.getDelChange()!=null && !this.getDelChange().equals("")){//ɾ����¼
			String[] del=this.getDelChange().split(";");
			
			for(int i=0;i<del.length;i++){
				
				String tem=del[i];
				
				if(tem.indexOf("+")!=-1){//����ɾ����¼
					
					String id=tem.substring(1);
					
					if(id.equals("0")){//������¼  ����Ҫ����
						
					}else{
						 count++;
						 sql+=" delete from fancjghb where id= "+id+";\n";
					}
				}else{//���¹�Ƥ
					
					String id=tem.substring(1);
					
					if(id.equals("0")){//������¼  ����Ҫ����
						
					}else{
						count++;
						sql+=" update fancjghb set piz=0 , qingcsj='' where id= "+id+";\n";
					}
				}
			}
		}
		
		if(this.getChange()!=null && !this.getChange().equals("")){
			
			String[] ms=this.getChange().split(";");
			
			for(int i=0;i<ms.length;i++){
				
				count++;
				
				
				String[] res=ms[i].split(",");
				String id=res[0];
				String cheph=res[1];
				String maoz=res[2];
				String piz=res[3];
				
				String zhongcsj="''";
				if(res[4]!=null && !res[4].equals("") && !res[4].equals("0")){
					zhongcsj="to_date('"+res[4]+"','yyyy-MM-dd HH24:mi:ss')";
				}
				
				String qingcsj="''";
				if(res[5]!=null && !res[5].equals("") && !res[5].equals("0")){
					qingcsj="to_date('"+res[5]+"','yyyy-MM-dd HH24:mi:ss')";
				}
				
				String beiz=res[6];
				String riq=res[7];
				
				String mk=res[8];
				String pz=res[9];
				
				String mk_id=this.getMKid(mk);
				String pz_id=this.getPZid(pz);
				
				if(mk_id==null || mk_id.equals("")){
					mk_id="''";
				}
				if(pz_id==null || pz_id.equals("")){
					pz_id="''";
				}
				
				if(id.equals("0")){//�����ӵļ�¼
					
					sql+=" insert into fancjghb(id,cheph,riq,maoz,piz,zhongcsj,qingcsj,beiz,meikxxb_id,pinzb_id) " +
							"values(getnewid("+visit.getDiancxxb_id()+"),'"+cheph+"'," +
							   "to_date('"+riq+"','yyyy-MM-dd HH24:mi:ss')," +
							   		""+maoz+","+piz+"," +
							   		zhongcsj+"," +qingcsj+",'"+beiz+"',"+mk_id+","+pz_id+");\n";
				}else{//����ԭ�м�¼
					sql+=" update fancjghb set cheph='"+cheph+"',riq=to_date('"+riq+"','yyyy-MM-dd HH24:mi:ss')," +
							"maoz="+maoz+",piz="+piz+",zhongcsj="+zhongcsj+"," +
									"qingcsj="+qingcsj+",beiz='"+beiz+"',meikxxb_id="+mk_id+",pinzb_id="+pz_id+" where id="+id+";\n";
				}
				
				
				
			}
			
		}
		
		
		if(count==0){//û����� ��ִ��
			this.setMsg("û�иĶ����������!");
		}else{
			if(count==1){
				sql=sql.substring(0, sql.lastIndexOf(";"));
			}else{//�������
				 sql=" begin \n"+sql+"\n end;";
			}
			
//			System.out.println(sql);
			int flag=con.getUpdate(sql);
			if(flag>=0){
				this.setFilename("true");
				this.setMsg("���ݲ����ɹ�!");
			}else{
				this.setFilename("false");
				this.setMsg("���ݲ���ʧ��!");
			}
		}
		
		this.setDelChange("");
		con.Close();
	}
	
	public void getSelectData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String tableName="";
		String sql = "";
		
		
//			String rq1=DateUtil.FormatOracleDate(this.getRiq());
//			String rq2=DateUtil.FormatOracleDate(this.getRiq1());
		
			String ts="00:00:00";
			if(!this.getXiaosValue().getStrId().equals("-1")){
				ts=this.getXiaosValue().getValue();
			}
			String rq1=" to_date('"+this.getRiq()+" "+ts+"','yyyy-MM-dd HH24:mi:ss')";
			
			
			sql=" select f.id,f.cheph,f.maoz,f.piz,round_new(maoz-piz,2) jingz,to_char(f.zhongcsj,'yyyy-MM-dd HH24:mi:ss') zhongcsj," +
					"to_char(f.qingcsj,'yyyy-MM-dd HH24:mi:ss') qingcsj,m.mingc mk,p.mingc pz, f.beiz ,to_char(f.riq,'yyyy-MM-dd HH24:mi:ss') riq from fancjghb f,meikxxb m,pinzb p where f.meikxxb_id=m.id(+) and f.pinzb_id=p.id(+)  and   f.riq="+rq1;
		
			
			sql+="\n   union \n " +
					"  select -1 id,'�ܼ�' cheph,sum(maoz) maoz,sum(piz) piz,sum( round_new(maoz - piz, 2)) jingz,\n" +
					" '' zhongcsj,'' qingcsj,'' mk,'' pz,'' beiz, to_char(riq, 'yyyy-MM-dd HH24:mi:ss') riq\n" +
					" from  fancjghb \n" +
					"  where riq="+rq1+"\n" +
					"  group by riq\n" +
					"  order by id\n";
//		System.out.println(sql);
		
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName(tableName);
		egu.addPaging(-1);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("cheph").setHeader("��Ƥ��");
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("zhongcsj").setHidden(true);
		egu.getColumn("qingcsj").setHidden(true);
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("maoz").setDefaultValue("0");
		egu.getColumn("piz").setDefaultValue("0");
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("mk").setHeader("ú��");
		egu.getColumn("pz").setHeader("Ʒ��");
		
		ComboBox mk=new ComboBox();
		mk.setEditable(false);
		
		egu.getColumn("mk").setEditor(mk);
		egu.getColumn("mk").setComboEditor(egu.gridId,new IDropDownModel(" " +
				" select m.id,m.mingc from meikxxb m,kuangzglb k where k.meikxxb_id=m.id and k.chezxxb_id<>1 " +
				""));
		
		ComboBox pz=new ComboBox();
		pz.setEditable(false);
		
		egu.getColumn("pz").setEditor(pz);
		egu.getColumn("pz").setComboEditor(egu.gridId, new IDropDownModel("select p.id,p.mingc from pinzb p"));
		
		
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setEditor(null);
		
		egu.getColumn("cheph").setRenderer("function(value){return '<font size=4>'+value+'</font>';}");
		egu.getColumn("maoz").setRenderer("function(value){return '<font size=4>'+value+'</font>';}");
		egu.getColumn("piz").setRenderer("function(value){return '<font size=4>'+value+'</font>';}");
		egu.getColumn("jingz").setRenderer("function(value){return '<font size=4>'+value+'</font>';}");
		
		egu.getColumn("mk").setRenderer("function(value){return '<font size=4>'+value+'</font>';}");
		egu.getColumn("pz").setRenderer("function(value){return '<font size=4>'+value+'</font>';}");
		
		TextField  cheph=new TextField();
		cheph.setListeners("specialkey:function(field,e){\n" +
				
				
				"if(e!=null && e.getCharCode()==e.ENTER){" +
				
				" if(row_line==gridDiv_ds.getCount()){return;}\n"+
				
				" var _count=gridDiv_ds.getCount()-1;\n" +
				" var rec=gridDiv_sm.getSelected();\n"+
				"if(!SelectLike.checked){_count=row_line+2;};\n" +
				
				
				"for(i=row_line;i<_count;i++){\n" +
				"if(i==gridDiv_ds.getCount()){continue;}\n" +
				"gridDiv_ds.getAt(i).set('MK',rec.get('MK')); \n" +
				"gridDiv_ds.getAt(i).set('PZ',rec.get('PZ')); \n" +
				"" +
				"}\n" +
				
				" createTempFile(grid.getStore());\n"+
				
				"}" +
				"" +
				"" +
				"\n}");
		egu.getColumn("cheph").setEditor(cheph);
		
		
		egu.getColumn("cheph").setWidth(120);
		egu.getColumn("maoz").setWidth(120);
		egu.getColumn("piz").setWidth(120);
		egu.getColumn("jingz").setWidth(120);
		egu.getColumn("beiz").setWidth(200);
		
		
		
		TextField zs=new TextField();
		zs.setReadOnly(true);
		zs.allowBlank=true;
		egu.getColumn("zhongcsj").setEditor(zs);
		
		TextField cs=new TextField();
		cs.setReadOnly(true);
		cs.allowBlank=true;
		egu.getColumn("qingcsj").setEditor(cs);
		
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		
		NumberField nf=new NumberField();
//		nf.setId("nf");
		nf.setDecimalPrecision(2l);
		nf.setReadOnly(true);
		nf.setEmptyText("0");
		egu.getColumn("jingz").setEditor(nf);
	
		
		
		egu.addTbarText("ʱ��:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		
//		egu.addTbarText("��ʶ:");
		
		ComboBox xs=new ComboBox();
		xs.setWidth(80);
		xs.setTransform("XS");
		xs.setId("XS");//���Զ�ˢ�°�
		xs.setLazyRender(true);//��̬��
		egu.addToolbarItem(xs.getScript());
		
		
//		egu.addTbarText("��:");
//		
//		ComboBox fen=new ComboBox();
//		fen.setWidth(50);
//		fen.setTransform("FEN");
//		fen.setId("FEN");//���Զ�ˢ�°�
//		fen.setLazyRender(true);//��̬��
//		egu.addToolbarItem(fen.getScript());
		
		
//		egu.addTbarText("��:");
//		
//		ComboBox sec=new ComboBox();
//		sec.setWidth(50);
//		sec.setTransform("SEC");
//		sec.setId("SEC");//���Զ�ˢ�°�
//		sec.setLazyRender(true);//��̬��
//		egu.addToolbarItem(sec.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		
		
//		
//		egu.addTbarText("����Դ:");
//		ComboBox comb2=new ComboBox();
//		comb2.setWidth(80);
//		comb2.setTransform("DS");
//		comb2.setId("DS");//���Զ�ˢ�°�
//		comb2.setLazyRender(true);//��̬��
//		egu.addToolbarItem(comb2.getScript());
//		egu.addTbarText("-");// ���÷ָ���
		
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		
		
	
		
		
//		StringBuffer str_de=new StringBuffer();
//		str_de.append("function(){ document.all.DeleteButton.click();");
//		str_de.append("}");
//		GridButton de=new GridButton("��һ��",str_de.toString());
//		de.setIcon(SysConstant.Btn_Icon_Search);
//		egu.addTbarBtn(de);
		
		egu.addTbarText("-");
		
		
		StringBuffer str_in=new StringBuffer();
		str_in.append("function(){ Daords();");
		str_in.append("}");
		GridButton in=new GridButton("����",str_in.toString());
		in.setId("DRDS");
		in.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(in);
		
		egu.addTbarText("-");
		
		String in1Str=" Ext.MessageBox.prompt('��ʾ', '��������Ӽ�¼��', function(btn,text){" +
				"if(btn=='ok'){\n" +
				
				"" +
				"if(text>0){\n" +
				
				" var flag=false;\n" +
				" if(gridDiv_ds.getCount()<=0) flag=true;\n"+
				
				" var tes=getRiq();"+
				
				"for(i=0;i<text;i++){\n" +
				"var plant = new gridDiv_plant({ID:'0',CHEPH:'',MAOZ:'0',PIZ:'0',JINGZ:'0',ZHONGCSJ:'',QINGCSJ:'',BEIZ:'',MK:'',PZ:'',RIQ:tes });\n" +
				" var posi=gridDiv_ds.getCount();\n" +
				"if(posi>0){ posi-=1;}\n"+
				"gridDiv_ds.insert(posi,plant);\n" +
				"}\n" +
				
				" if(flag) {\n"+
				" plant = new gridDiv_plant({ID:'-1',CHEPH:'�ܼ�',MAOZ:'0',PIZ:'0',JINGZ:'0',ZHONGCSJ:'',QINGCSJ:'',BEIZ:'',MK:'',PZ:'',RIQ:tes });\n" +
				"gridDiv_ds.insert(gridDiv_ds.getCount(),plant);\n" +
				"}\n"+
				
//				" var res=gridDiv_ds.getRange();\n" +
//				"var maoz=0;\n" +
//				"var piz=0;\n" +
//				"var recCount;\n" +
//				
//				" for(var  i=0;i<res.length;i++){\n" +
//				" var rec=res[i];\n" +
//				" if(rec.get('ID')=='-1'){ recCount=res[i];continue;}\n"+
//				" maoz+=parseFloat(rec.get('MAOZ'));\n" +
//				" piz+=parseFloat(rec.get('PIZ'));\n" +
//				"}\n"+
//				
//				"recCount.set('MAOZ',maoz);\n" +
//				"recCount.set('PIZ',piz);\n" +
//				"recCount.set('JINGZ',(maoz-piz));\n"+
				
				"gridDiv_grid.view.refresh();" +
				"}\n" +
				
				
				"}\n" +
				"" +
				"" +
				"});";
		GridButton in1=new GridButton("��ӹ�����Ϣ","function(){"+in1Str+"}");
		in1.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(in1);
		
		egu.addTbarText("-");
		
		
		String in2Str=" var rec=gridDiv_sm.getSelected();\n" +
				"if(rec==null){Ext.Msg.alert('��ʾ��Ϣ','��ѡ��һ����¼�����!');return;}\n" +
				"var plant = new gridDiv_plant({ID:'0',CHEPH:'',MAOZ:'0',PIZ:'0',JINGZ:'0',ZHONGCSJ:'',QINGCSJ:'',MK:'',PZ:'',BEIZ:'',RIQ:''+rec.get('RIQ') });\n" +
				"gridDiv_ds.insert(row_line,plant);" +
				"gridDiv_grid.view.refresh();\n";
		GridButton in2=new GridButton("���","function(){"+in2Str+"}");
		in2.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(in2);
		
		egu.addTbarText("-");
		
		
		String delStr=" if(gridDiv_sm.getSelections()==null || gridDiv_sm.getSelections().length==0){Ext.Msg.alert('��ʾ��Ϣ','��ѡ���¼!');return;}\n" +
				"Ext.Msg.show({\n" +
				"title:'ɾ��',\n" +
				"msg:'��:����ɾ��&nbsp;��:���¹�Ƥ&nbsp;ȡ��:ȡ������',\n" +
				"buttons:Ext.Msg.YESNOCANCEL,\n" +
				"fn:function(btn){\n" +
				"if(btn=='yes'){" +
				
				"for(i=0;i<gridDiv_sm.getSelections().length;i++){\n" +
				" var rec = gridDiv_sm.getSelections()[i];\n" +
				"if(rec.get('ID')=='-1'){continue;}\n"+
				" document.all.DELCHANGE.value+='+'+rec.get('ID')+';';\n" +
				"gridDiv_ds.remove(gridDiv_sm.getSelections()[i--]);\n"+
				"}\n" +
				
				"}\n" +
				
				"if(btn=='no'){" +
				"" +
				"for(i=0;i<gridDiv_sm.getSelections().length;i++){\n" +
				" var rec = gridDiv_sm.getSelections()[i];\n" +
				" document.all.DELCHANGE.value+='-'+rec.get('ID')+';';\n" +
				"gridDiv_ds.remove(gridDiv_sm.getSelections()[i--]);\n"+
				"}\n" +
				
				"}\n" +
				
				
				"if(btn=='cancel'){return;}\n" +
				
				
				
				"if(gridDiv_ds.getCount()<=1){gridDiv_ds.removeAll();return;}\n"+
				
				" var res=gridDiv_ds.getRange();\n" +
				"var maoz=0;\n" +
				"var piz=0;\n" +
				"var recCount;\n" +
				
				" for(var  i=0;i<res.length;i++){\n" +
				" var recc=res[i];\n" +
				" if(recc.get('ID')=='-1'){ recCount=res[i];continue;}\n"+
				" maoz+=parseFloat(recc.get('MAOZ'));\n" +
				" piz+=parseFloat(recc.get('PIZ'));\n" +
				"}\n"+
				
				"recCount.set('MAOZ',maoz);\n" +
				"recCount.set('PIZ',piz);\n" +
				"recCount.set('JINGZ',(maoz-piz));\n"+
				
				
				
				"}\n" +
				"});";
		GridButton del=new GridButton("ɾ��","function(){"+delStr+"}");
		del.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(del);
		
		egu.addTbarText("-");
		
//		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
		
		GridButton sav=new GridButton("����","function(){  grid_save(grid.getStore()); }");
		sav.setIcon(SysConstant.Btn_Icon_Save);
		egu.addTbarBtn(sav);
		
		
		
		Checkbox cbselectlike=new Checkbox();
		
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("�����滻");
		
		
		egu.addOtherScript(" grid=gridDiv_grid;");
		egu.addOtherScript(" gridDiv_sm.addListener('rowselect',function(sml,rowIndex,re){if(re.get('ID')=='-1'){sml.deselectRow(rowIndex);return;}  row_line=rowIndex;});");
		egu.addOtherScript(" gridDiv_grid.addListener('afteredit',function(e){if(e.field=='CHEPH' || e.field=='BEIZ' || e.field=='MK' || e.field=='PZ') createTempFile(grid.getStore());});");
		
		egu.addOtherScript(" gridDiv_grid.addListener('beforeedit',function(e){grid.getSelectionModel().selectRow(e.row);});\n");
		setExtGrid(egu);
		rsl.close();
		con.Close(); 
	}
	
//***************************************************************************//
	
	private String riq;
	private boolean riqBoo=false;
	public void setRiq(String value) {
		
		if(riq!=null && !riq.equals(value)){
			riqBoo=true;
		}
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	private String riq1;
	public void setRiq1(String value) {
		riq1 = value;
	}
	public String getRiq1() {
		if ("".equals(riq1) || riq1 == null) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setOriRiq1(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	public String getOriRiq1() {
		return ((Visit) getPage().getVisit()).getString2();
	}
	//-------------------------------------------------
	
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

	
	//ȡ���ݴ��ڱ���ָ��·���µ��ļ�����
	private String initData;
	
	public String getInitData(){
		
		String s=" gridDiv_data=[ ";
		String filepath="D:/zhiren/huocfcjds/temp/huocfcj_temp";
		File file=new File(filepath);
//		File file = new File(visit.getXitwjjwz()+"/huocfcj/temp/huocfcj_temp");
		int flag=0;
		if(file.exists()){
			
			try {
				FileReader fr=new FileReader(file);
				BufferedReader br=new BufferedReader(fr);
				
				while(true){
					String readLine=br.readLine();
					if(readLine==null){//�Ѿ����� �˳�
						
						if(flag==0){
							return "";
						}
						break;
					}
					flag++;
					String[] Str_temp=readLine.split(",");
					
					s+="[";
					for(int i=0;i<Str_temp.length;i++){
						s+="'"+Str_temp[i]+"'";
						if(i!=Str_temp.length-1){
							s+=",";
						}
					}
					s+="],";
					
				}
				
				s=s.substring(0,s.lastIndexOf(","))+"];";
				return s;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}else{
			return "";
		}
		
	}
	
	
	
	public void beginResponse(IMarkupWriter writer,IRequestCycle cycle){
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setRiq(null);
	//		this.setRiq1(null);
	//		this.setNextGroup(1);
			this.setXiaosValue(null);
			this.setXiaosModel(null);
			
			this.setFenModel(null);
			this.setFenValue(null);
			
			this.setMiaoModel(null);
			this.setMiaoValue(null);
			
			this.setFilename("cancel");
			getSelectData();
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
	
	
	
	// ��ѯ��ʽ������ ������Դ��
	private boolean falg1 = false;

	private IDropDownBean DsValue;

	public IDropDownBean getDsValue() {
		if (DsValue == null) {
			DsValue = (IDropDownBean) getDsModel().getOption(0);
		}
		return DsValue;
	}

	public void setDsValue(IDropDownBean Value) {
		if (!(DsValue == Value)) {
			DsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel DsModel;

	public void setDsModel(IPropertySelectionModel value) {
		DsModel = value;
	}

	public IPropertySelectionModel getDsModel() {
		if (DsModel == null) {
			getDsModels();
		}
		return DsModel;
	}

	public IPropertySelectionModel getDsModels() {
		
		List list=new ArrayList();
		list.add(new IDropDownBean(1,"�ѵ���"));
		list.add(new IDropDownBean(2,"δ����"));
		DsModel = new IDropDownModel(list);
		return DsModel;
	}
//Сʱ--------------------
	
	private IDropDownBean XiaosValue;

	public IDropDownBean getXiaosValue() {
		if (XiaosValue == null) {
			XiaosValue = (IDropDownBean) getXiaosModel().getOption(0);
		}
		return XiaosValue;
	}

	public void setXiaosValue(IDropDownBean Value) {
		if (!(XiaosValue == Value)) {
			XiaosValue = Value;
		//	falg1 = true;
		}
	}

	private IPropertySelectionModel XiaosModel;

	public void setXiaosModel(IPropertySelectionModel value) {
		XiaosModel = value;
	}

	public IPropertySelectionModel getXiaosModel() {
		if (XiaosModel == null) {
			getXiaosModels();
		}
		return XiaosModel;
	}

	public IPropertySelectionModel getXiaosModels() {
		
		String sql=" select rownum id,mingc from (select distinct to_char(riq,'HH24:mi:ss') mingc from fancjghb \n" +
				"where to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('"+this.getRiq()+"','yyyy-MM-dd')) order by mingc desc";
		XiaosModel = new IDropDownModel(sql,"��ѡ��");
		return XiaosModel;
	}
	
	//ú����Ϣ------------------
	
	
	private String getMKid(String mingc){
		
		if(this.getFenModel()==null){return null;}
		
		String s=null;
		for(int i=0;i<this.getFenModel().getOptionCount();i++){
			IDropDownBean mk=(IDropDownBean)this.getFenModel().getOption(i);
			
			if(mk==null){
				continue;
			}
			
			if(mk.getValue()!=null && mk.getValue().equals(mingc)){
				s=mk.getStrId();
				break;
			}
		}
		
		return s;
	}
	private IDropDownBean FenValue;

	public IDropDownBean getFenValue() {
		if (FenValue == null) {
			FenValue = (IDropDownBean) getFenModel().getOption(0);
		}
		return FenValue;
	}

	
	public void setFenValue(IDropDownBean Value) {
		if (!(FenValue == Value)) {
			FenValue = Value;
		//	falg1 = true;
		}
	}

	private IPropertySelectionModel FenModel;

	public void setFenModel(IPropertySelectionModel value) {
		FenModel = value;
	}

	public IPropertySelectionModel getFenModel() {
		if (FenModel == null) {
			getFenModels();
		}
		return FenModel;
	}

	public IPropertySelectionModel getFenModels() {
		
//		List list=new ArrayList();
//		for(int i=0;i<60;i++){
//			list.add(new IDropDownBean(i,i<10?("0"+i):(i+"")));
//		}
//		FenModel = new IDropDownModel(list);
		FenModel = new IDropDownModel("select m.id,m.mingc from meikxxb m,kuangzglb k where k.meikxxb_id=m.id and k.chezxxb_id<>1 ");
		return FenModel;
	}
	
	
	//Ʒ��-----
	
	
	private String getPZid(String mingc){
		
		if(this.getMiaoModel()==null){return null;}
		
		String s=null;
		for(int i=0;i<this.getMiaoModel().getOptionCount();i++){
			IDropDownBean pz=(IDropDownBean)this.getMiaoModel().getOption(i);
			
			if(pz==null){
				continue;
			}
			
			if(pz.getValue()!=null && pz.getValue().equals(mingc)){
				s=pz.getStrId();
				break;
			}
		}
		
		return s;
	}
	
	
	private IDropDownBean MiaoValue;

	public IDropDownBean getMiaoValue() {
		if (MiaoValue == null) {
			MiaoValue = (IDropDownBean) getMiaoModel().getOption(0);
		}
		return MiaoValue;
	}

	public void setMiaoValue(IDropDownBean Value) {
		if (!(MiaoValue == Value)) {
			MiaoValue = Value;
		//	falg1 = true;
		}
	}

	private IPropertySelectionModel MiaoModel;

	public void setMiaoModel(IPropertySelectionModel value) {
		MiaoModel = value;
	}

	public IPropertySelectionModel getMiaoModel() {
		if (MiaoModel == null) {
			getMiaoModels();
		}
		return MiaoModel;
	}

	public IPropertySelectionModel getMiaoModels() {
		
//		List list=new ArrayList();
//		
//		for(int i=0;i<60;i++){
//			list.add(new IDropDownBean(i,i<10?("0"+i):(i+"")));
//		}
//		
//		
//		MiaoModel = new IDropDownModel(list);
		MiaoModel = new IDropDownModel(" select p.id,p.mingc from pinzb p ");
		return MiaoModel;
	}

	
}
