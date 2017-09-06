var gridDiv_history = "";
Ext.onReady(function(){
var url = "http://"+document.location.host+document.location.pathname;
var end = url.indexOf("/app");
url = url.substring(0,end);
Ext.BLANK_IMAGE_URL = url+'/ext/resources/images/s.gif';

var gridDiv_sm = new Ext.grid.RowSelectionModel({singleSelect:true})
var gridDiv_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
{header:'ID',dataIndex:'ID',hidden :true},
//{header:'����',dataIndex:'BIANM',editor :new Ext.form.TextField({maxLength:12,width:80,selectOnFocus:true}),hidden :false},
{header:'������',dataIndex:'FAHR',width:100,editor :new Ext.form.TextField({maxLength:50,width:'auto',selectOnFocus:true}),hidden :false},
{header:'�ջ���',dataIndex:'YUANSHDWB_ID',width:100,editor :new Ext.form.TextField({maxLength:50,width:'auto',selectOnFocus:true}),hidden :false},
{header:'Ʒ��',dataIndex:'PINZ',width:60,editor :new Ext.form.TextField({maxLength:20,width:'auto',selectOnFocus:true}),hidden :false},
{header:'����',dataIndex:'CHEC',width:60,editor :new Ext.form.TextField({maxLength:20,width:'auto',selectOnFocus:true}),hidden :false},
{header:'��ж����',dataIndex:'JIEXRQ',renderer:function(value){ return (value==null || value=='')?'':('object' != typeof(value)?value:value.dateFormat('Y-m-d'));},width:80,editor :new Ext.form.DateField({allowBlank:true,format:'Y-m-d'}),hidden :false},
{header:'����',dataIndex:'CHES',width:60,editor :new Ext.form.NumberField({decimalPrecision:3,selectOnFocus:true}),hidden :false},

//{header:'�ջ���',dataIndex:'YUANSHDWB_ID',width:100,editor :new Ext.form.TextField({maxLength:50,width:'auto',selectOnFocus:true}),hidden :false},
{header:'Ʊ��(��)',dataIndex:'BIAOZ',width:60,editor :new Ext.form.NumberField({decimalPrecision:3,selectOnFocus:true}),hidden :false},
{header:'����(��)',dataIndex:'JINGZ',width:60,editor :new Ext.form.NumberField({decimalPrecision:3,selectOnFocus:true}),hidden :false},
{header:'ȫˮ(Mt)',dataIndex:'MT',width:60,editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :false},
{header:'�ոɻ�ˮ(Mad)',dataIndex:'MAD',width:80,editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :false},
{header:'AAR',dataIndex:'AAR',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'�ոɻ���(Aad)',dataIndex:'AAD',width:80,editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :false},
{header:'AD',dataIndex:'AD',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'VAR',dataIndex:'VAR',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'VAD',dataIndex:'VAD',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'�ӷ���(Vdaf)',dataIndex:'VDAF',width:80,editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :false},
{header:'FCAD',dataIndex:'FCAD',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'QGRAD',dataIndex:'QGRAD',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'QGRD',dataIndex:'QGRD',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'QNET_AR',dataIndex:'QNET_AR',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'�ոɻ���(Stad)',dataIndex:'STAD',width:80,editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :false},
{header:'STD',dataIndex:'STD',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'QBAD',dataIndex:'QBAD',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'HAR',dataIndex:'HAR',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :true},
{header:'�ոɻ���(Had)',dataIndex:'HAD',width:80,editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:3,selectOnFocus:true}),hidden :false},
{header:'HDAF',dataIndex:'HDAF',editor :new Ext.form.NumberField({allowBlank:true,decimalPrecision:4,selectOnFocus:true}),hidden :true},
{header:'HUAYSJ',dataIndex:'HUAYSJ',renderer:function(value){ return (value==null || value=='')?'':('object' != typeof(value)?value:value.dateFormat('Y-m-d'));},editor :new Ext.form.DateField({allowBlank:true,allowBlank:true,format:'Y-m-d'}),hidden :true},
{header:'HUAYBH',dataIndex:'HUAYBH',editor :new Ext.form.TextField({allowBlank:true,maxLength:50,width:'auto',selectOnFocus:true}),hidden :true},
{header:'HUAYY',dataIndex:'HUAYY',editor :new Ext.form.TextField({allowBlank:true,maxLength:20,width:'auto',selectOnFocus:true}),hidden :true},
{header:'BEIZ',dataIndex:'BEIZ',editor :new Ext.form.TextField({allowBlank:true,maxLength:200,width:'auto',selectOnFocus:true}),hidden :true},
{header:'SHENHZT',dataIndex:'SHENHZT',hidden :true},
{header:'FAHBTMP_ID',dataIndex:'FAHBTMP_ID',hidden :true}]);
gridDiv_cm.defaultSortable=true;
var gridDiv_ds = new Ext.data.Store({proxy : new Ext.zr.data.PagingMemoryProxy(gridDiv_data),pruneModifiedRecords:true,reader: new Ext.data.ArrayReader({}, [{name:'ID'},/*{name:'BIANM'},*/{name:'FAHR'},{name:'YUANSHDWB_ID'},{name:'PINZ'},{name:'CHEC'},{name:'JIEXRQ'},{name:'CHES'},{name:'BIAOZ'},{name:'JINGZ'},{name:'MT'},{name:'MAD'},{name:'AAR'},{name:'AAD'},{name:'AD'},{name:'VAR'},{name:'VAD'},{name:'VDAF'},{name:'FCAD'},{name:'QGRAD'},{name:'QGRD'},{name:'QNET_AR'},{name:'STAD'},{name:'STD'},{name:'QBAD'},{name:'HAR'},{name:'HAD'},{name:'HDAF'},{name:'HUAYSJ'},{name:'HUAYBH'},{name:'HUAYY'},{name:'BEIZ'},{name:'SHENHZT'},{name:'FAHBTMP_ID'}])});
var gridDiv_plant = Ext.data.Record.create([{name: 'ID', type:'string'},/*{name: 'BIANM', type:'string'},*/{name: 'FAHR', type:'string'},{name:'YUANSHDWB_ID',type:'float'},{name: 'PINZ', type:'string'},{name: 'CHEC', type:'string'},{name: 'JIEXRQ', type:'date'},{name: 'CHES', type:'float'},{name: 'BIAOZ', type:'float'},{name: 'JINGZ', type:'float'},{name: 'MT', type:'float'},{name: 'MAD', type:'float'},{name: 'AAR', type:'float'},{name: 'AAD', type:'float'},{name: 'AD', type:'float'},{name: 'VAR', type:'float'},{name: 'VAD', type:'float'},{name: 'VDAF', type:'float'},{name: 'FCAD', type:'float'},{name: 'QGRAD', type:'float'},{name: 'QGRD', type:'float'},{name: 'QNET_AR', type:'float'},{name: 'STAD', type:'float'},{name: 'STD', type:'float'},{name: 'QBAD', type:'float'},{name: 'HAR', type:'float'},{name: 'HAD', type:'float'},{name: 'HDAF', type:'float'},{name: 'HUAYSJ', type:'date'},{name: 'HUAYBH', type:'string'},{name: 'HUAYY', type:'string'},{name: 'BEIZ', type:'string'},{name:'SHENHZT', type:'float'},{name:'FAHBTMP_ID',type:'string'}]);
var gridDiv_grid = new Ext.grid.GridPanel({
el:'gridDiv',
cm:gridDiv_cm,
sm:gridDiv_sm,
ds:gridDiv_ds,
width:bodyWidth,
clicksToEdit:1,frame:true,tbar: ['��ж����',jiexrq=new Ext.form.DateField({
	allowBlank:true,
	format:'Y-m-d',
	value:Date.parseDate(document.getElementById('RIQ').value,'Y-m-d'),
	listeners:{change:function(own,newValue,oldValue) {document.getElementById('RIQ').value = newValue.dateFormat('Y-m-d');}}
}),'��',jiexrqe=new Ext.form.DateField({
	allowBlank:true,
	format:'Y-m-d',
	value:Date.parseDate(document.getElementById('RIQE').value,'Y-m-d'),
	listeners:{change:function(own,newValue,oldValue) {document.getElementById('RIQE').value = newValue.dateFormat('Y-m-d');}}
}),'-','��λ����:',diancTree_text=new Ext.form.TextField({
	width:100,
	selectOnFocus:true,
	value:diancTree_treePanel_out.getNodeById(document.getElementById('TREEID').value).text
}),{icon:'ext/resources/images/list-items.gif',cls: 'x-btn-icon',handler:function(){diancTree_window_out.show();}},'-',

{text:' ˢ��',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('RefurbishButton').click();}},{text:'�༭',icon:'imgs/btnicon/insert.gif',cls:'x-btn-text-icon',minWidth:75,handler:EditForm},
{text:' ɾ��',icon:'imgs/btnicon/delete.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){
if(gridDiv_sm.hasSelection()){ 
if(shenhztChk()){
Ext.MessageBox.confirm('����', 'ȷ�������鵥ɾ����', function(btn) {  
if(btn=='yes'){
var Cobj = document.getElementById('CHANGE');
var record=gridDiv_sm.getSelected();
Cobj.value = record.get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;');
document.getElementById('DelButton').click();}
});}else{Ext.MessageBox.alert('����','�������Ϣ����ɾ��!');return;}
}else{Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����¼!');return;}
}},{text:' ����',icon:'imgs/btnicon/save.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){
 var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();
for(i = 0; i< Mrcd.length; i++){
if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}
//if(Mrcd[i].get('BIANM') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;
//}

if(Mrcd[i].get('FAHR') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;
}if(Mrcd[i].get('PINZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� Ʒ�� ����Ϊ��');return;
}if(Mrcd[i].get('CHEC') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;
}if(Mrcd[i].get('HUAYSJ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �������� ����Ϊ��');return;
}if(Mrcd[i].get('HUAYBH') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;
}if(Mrcd[i].get('HUAYY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ����Ա ����Ϊ��');return;
}if(Mrcd[i].get('CHES') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶγ��� ������Сֵ -100000000');return;
}if( Mrcd[i].get('CHES') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ���� �������ֵ 100000000000');return;
}if(Mrcd[i].get('CHES')!=0 && Mrcd[i].get('CHES') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;
}if(Mrcd[i].get('JIEXRQ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ��ж���� ����Ϊ��');return;
}if(Mrcd[i].get('BIAOZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�Ʊ��(��) ������Сֵ -100000000');return;
}if( Mrcd[i].get('BIAOZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� Ʊ��(��) �������ֵ 100000000000');return;
}if(Mrcd[i].get('BIAOZ')!=0 && Mrcd[i].get('BIAOZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� Ʊ��(��) ����Ϊ��');return;
}if(Mrcd[i].get('JINGZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶξ���(��) ������Сֵ -100000000');return;
}if( Mrcd[i].get('JINGZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ����(��) �������ֵ 100000000000');return;
}if(Mrcd[i].get('JINGZ')!=0 && Mrcd[i].get('JINGZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ����(��) ����Ϊ��');return;
}gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update="true">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'
//+ '<BIANM update="true">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'
+ '<FAHR update="true">' + Mrcd[i].get('FAHR').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</FAHR>'
+ '<PINZ update="true">' + Mrcd[i].get('PINZ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINZ>'
+ '<CHEC update="true">' + Mrcd[i].get('CHEC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHEC>'
+ '<CHES update="true">' + Mrcd[i].get('CHES')+ '</CHES>'
+ '<JIEXRQ update="true">' + ('object' != typeof(Mrcd[i].get('JIEXRQ'))?Mrcd[i].get('JIEXRQ'):Mrcd[i].get('JIEXRQ').dateFormat('Y-m-d'))+ '</JIEXRQ>'
+'<YUANSHDWB_ID update="true">'+Mrcd[i].get('YUANSHDWB_ID')+'</YUANSHDWB_ID>'
+ '<BIAOZ update="true">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'
+ '<JINGZ update="true">' + Mrcd[i].get('JINGZ')+ '</JINGZ>'
+ '<MT update="true">' + Mrcd[i].get('MT')+ '</MT>'
+ '<MAD update="true">' + Mrcd[i].get('MAD')+ '</MAD>'
+ '<AAR update="true">' + Mrcd[i].get('AAR')+ '</AAR>'
+ '<AAD update="true">' + Mrcd[i].get('AAD')+ '</AAD>'
+ '<AD update="true">' + Mrcd[i].get('AD')+ '</AD>'
+ '<VAR update="true">' + Mrcd[i].get('VAR')+ '</VAR>'
+ '<VAD update="true">' + Mrcd[i].get('VAD')+ '</VAD>'
+ '<VDAF update="true">' + Mrcd[i].get('VDAF')+ '</VDAF>'
+ '<FCAD update="true">' + Mrcd[i].get('FCAD')+ '</FCAD>'
+ '<QGRAD update="true">' + Mrcd[i].get('QGRAD')+ '</QGRAD>'
+ '<QGRD update="true">' + Mrcd[i].get('QGRD')+ '</QGRD>'
+ '<QNET_AR update="true">' + Mrcd[i].get('QNET_AR')+ '</QNET_AR>'
+ '<STAD update="true">' + Mrcd[i].get('STAD')+ '</STAD>'
+ '<STD update="true">' + Mrcd[i].get('STD')+ '</STD>'
+ '<QBAD update="true">' + Mrcd[i].get('QBAD')+ '</QBAD>'
+ '<HAR update="true">' + Mrcd[i].get('HAR')+ '</HAR>'
+ '<HAD update="true">' + Mrcd[i].get('HAD')+ '</HAD>'
+ '<HDAF update="true">' + Mrcd[i].get('HDAF')+ '</HDAF>'
+ '<HUAYSJ update="true">' + ('object' != typeof(Mrcd[i].get('HUAYSJ'))?Mrcd[i].get('HUAYSJ'):Mrcd[i].get('HUAYSJ').dateFormat('Y-m-d'))+ '</HUAYSJ>'
+ '<HUAYBH update="true">' + Mrcd[i].get('HUAYBH').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</HUAYBH>'
+ '<HUAYY update="true">' + Mrcd[i].get('HUAYY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</HUAYY>'
+ '<BEIZ update="true">' + Mrcd[i].get('BEIZ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BEIZ>'
+'<FAHBTMP_ID update="true">'+Mrcd[i].get('FAHBTMP_ID')+'</FAHBTMP_ID>'
 + '</result>' ; }
if(gridDiv_history=='' && gridDivsave_history==''){ 
Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');
}else{
var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';document.getElementById('SaveButton').click();Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});
}
}}],stripeRows:true});
gridDiv_grid.render();gridDiv_ds.load();

strdivs = '<div align=right valign=center style="width:90%">';
var Mt_e = new Ext.form.NumberField({width:100,height:20,listeners:{'change':function(){CountValue()},'specialkey':function(){Mad_e.focus(true)}} }); 
var Aar_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var Var_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var FCar_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var Qnetar_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var Star_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 

var Mad_e = new Ext.form.NumberField({width:100,height:20,listeners:{'change':function(){CountValue();},'specialkey':function(){Aad_e.focus(true)}}}); 
var Aad_e = new Ext.form.NumberField({width:100,height:20,listeners:{'change':function(){CountValue();},'specialkey':function(){Vad_e.focus(true)}}}); 
var Vad_e = new Ext.form.NumberField({width:100,height:20,listeners:{'change':function(){CountValue();},'specialkey':function(){Stad_e.focus(true)}}}); 
var FCad_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var Qgrad_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var Stad_e = new Ext.form.NumberField({width:100,height:20,listeners:{'change':function(){CountValue();},'specialkey':function(){Qbad_e.focus(true)}}}); 

var Ad_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var Vd_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var FCd_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var Qgrd_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var Std_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 

var Vdaf_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 
var Qbad_e = new Ext.form.NumberField({width:100,height:20,listeners:{'change':function(){CountValue();},'specialkey':function(){Had_e.focus(true)}}}); 
var Had_e = new Ext.form.NumberField({width:100,height:20,listeners:{'change':function(){CountValue();},'specialkey':function(){Huaybh_e.focus(true)}}});
var Hdaf_e = new Ext.form.NumberField({width:100,height:20,cls:'x-item-disabled'}); 

var Huaybh_e = new Ext.form.TextField({width:100,height:20});
var Huaysj_e = new Ext.form.DateField({width:100,height:20});
var Huayy_e  = new Ext.form.TextArea({width:'auto',height:50});
var Beiz_e  = new Ext.form.TextArea({width:'auto',height:50});

var AutoCountHad=new Ext.form.Checkbox({boxLabel:'��ֵ�Զ�����',height:20,checked:true,width:'auto'});

var ValueForm = new Ext.form.FormPanel({width : bodyWidth-380, height:320, labelAlign: 'right', title: '����ֵ', labelWidth: 60,frame: true, layout : 'column',
items:[{columnWidth :.25, items:[{html:'',height:24},{html:strdivs + 'ˮ�� M (%)</div>',height:21},{html:strdivs + '�ҷ� A (%)</div>',height:21},{html:strdivs + '�ӷ��� V (%)</div>',height:21},{html:strdivs + '�̶�̼ FC (%)</div>',height:21},{html:strdivs + '��λ�� Qgr (MJ/kg)</div>',height:21},{html:strdivs + '��λ�� Qnet (MJ/kg)</div>',height:21},{html:strdivs + 'ȫ�� St (%)</div>',height:18},{html:'<hr>',height:21},{html:strdivs + '�ӷ��� Vdaf (%)</div>',height:21}, {html:'',height:21},{html:strdivs + '����ʱ��</div>',height:21},{html:strdivs + '������Ա</div>',height:21}]},
{columnWidth :.25, items:[{html:'�յ���(ar)',height:22}, Mt_e, Aar_e, Var_e, FCar_e, {html:'<div align=center valign=center style="width:100"><hr></div>',height:20}, Qnetar_e, Star_e,{html:'<hr>'}, Vdaf_e, AutoCountHad, Huaysj_e, Huayy_e ]},
{columnWidth :.25, items:[{html:'���������(ad)',height:22}, Mad_e, Aad_e, Vad_e, FCad_e, Qgrad_e, {html:'<div align=center valign=center style="width:100"><hr></div>',height:20}, Stad_e,{html:'<hr>',height:21},{html:strdivs + '��Ͳ��ֵ(MJ/kg)</div>',height:21},{html:strdivs + '�����������(Had)</div>',height:21},{html:strdivs + '������</div>',height:21},{html:strdivs + '��ע</div>',height:21}]},
{columnWidth :.25, items:[{html:'�����(d)',height:22},{html:'<div align=center valign=center style="width:100"><hr></div>',height:20}, Ad_e, Vd_e, FCd_e, Qgrd_e, {html:'<div align=center valign=center style="width:100"><hr></div>',height:21}, Std_e,{html:'<hr>'}, Qbad_e, Had_e, Huaybh_e, Beiz_e ]}],
buttons:[{text:'ȷ��',minwidth:75,handler:function(){EditGrid();}},{text:'ȡ��',minwidth:75,handler:function(){Rpt_window.hide();}}]}); 

var Rpt_window =new Ext.Window({width:600,height:430,closable:false,modal:true,layout:'fit',items:[ValueForm]});

function  CountValue(){
	var Mt_v = Mt_e.getValue();
	var Mad_v = Mad_e.getValue();
	var Aad_v = Aad_e.getValue();
	var Vad_v = Vad_e.getValue();
	var Stad_v = Stad_e.getValue();
	var Qbad_v = Qbad_e.getValue();
	var Had_v = Had_e.getValue();
	if(Mt_v>0 && Mad_v>0 && Aad_v>0 && Vad_v>0 && Stad_v>0 && Qbad_v>0 && (Had_v>0 || AutoCountHad.getValue())){
	Aar_v = Round_New(Aad_v*(100-Mt_v)/(100-Mad_v),2);
		Ad_v  = Round_New(Aad_v*100/(100-Mad_v),2);

		Var_v = Round_New(Vad_v*(100-Mt_v)/(100-Mad_v),2);
		Vd_v  = Round_New(Vad_v*100/(100-Mad_v),2);

		Vdaf_v = Round_New(Vad_v*100/(100-Mad_v-Aad_v),2);
		FCar_v = Round_New(100-Mt_v-Aar_v-Var_v,2);
		FCad_v = Round_New(100-Mad_v-Aad_v-Vad_v,2);
		FCd_v = Round_New(100-Ad_v-Vd_v,2);
		Star_v = Round_New(Stad_v*(100-Mt_v)/(100-Mad_v),2);
		Std_v = Round_New(Stad_v*100/(100-Mad_v),2);
		Qgrad_v = 0.0;
		if(AutoCountHad.getValue()){
			Had_v = Round_New((Vd_v*100/(100-Ad_v)*0.074+2.16)*(100-Mad_v-Aad_v)/100,2);
		}
		Hdaf_v = Round_New(Had_v * 100 / (100 - Mad_v - Aad_v),2);
		if(Qbad_v <= 16.7){
			Qgrad_v = Round_New((Qbad_v*1000-94.1*Stad_v-Qbad_v)/1000,2);
		}else{
			 if(Qbad_v > 16.7 && Qbad_v <= 25.1){
				Qgrad_v = Round_New((Qbad_v*1000-94.1*Stad_v-1.2*Qbad_v)/1000,2);
			 }else{
				Qgrad_v = Round_New((Qbad_v*1000-94.1*Stad_v-1.6*Qbad_v)/1000,2);
			 }
		}
		Qgrd_v  = Round_New(Qgrad_v*100/(100-Mad_v),2);
		//Qnetar_v  = Round_New(((Qgrad_v*1000-206*Had_v)*(100-Aar_v)/(100-Mad_v)-23*Mt_v)/1000,3);
		Qnetar_v=Round_New(((Qgrad_v*1000-206*Had_v)*(100-Mt_v)/(100-Mad_v)-23*Mt_v)/1000,2);

		
		Aar_e.setValue(Aar_v);
		Ad_e.setValue(Ad_v);
		Var_e.setValue(Var_v);
		Vd_e.setValue(Vd_v);
		Vdaf_e.setValue(Vdaf_v);
		FCar_e.setValue(FCar_v);
		FCad_e.setValue(FCad_v);
		FCd_e.setValue(FCd_v);
		Star_e.setValue(Star_v);
		Std_e.setValue(Std_v);
		Had_e.setValue(Had_v);
		Hdaf_e.setValue(Hdaf_v);
		Qgrad_e.setValue(Qgrad_v);
		Qgrd_e.setValue(Qgrd_v);
		Qnetar_e.setValue(Qnetar_v);
	}
}
function EditForm(){
	var rec = gridDiv_sm.getSelected();
	if(rec==null){
		Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����¼��');
		return;
	}
if(shenhztChk()){
	Mt_e.setValue(rec.get('MT'));
	Mad_e.setValue(rec.get('MAD'));
	Aad_e.setValue(rec.get('AAD'));
	Vad_e.setValue(rec.get('VAD'));
	Stad_e.setValue(rec.get('STAD'));
	Qbad_e.setValue(rec.get('QBAD'));
	Aar_e.setValue(rec.get('AAR'));
	Ad_e.setValue(rec.get('AD'));
	Var_e.setValue(rec.get('VAR'));
	Vd_e.setValue('');
	Vdaf_e.setValue(rec.get('VDAF'));
	FCar_e.setValue('');
	FCad_e.setValue(rec.get('FCAD'));
	FCd_e.setValue('');
	Star_e.setValue('');
	Std_e.setValue(rec.get('STD'));
	Had_e.setValue(rec.get('HAD'));
	Hdaf_e.setValue(rec.get('HDAF'));
	Qgrad_e.setValue(rec.get('QGRAD'));
	Qgrd_e.setValue(rec.get('QGRD'));
	Qnetar_e.setValue(rec.get('QNETAR'));
	Huaybh_e.setValue(rec.get('HUAYBH'));
	Huaysj_e.setValue(rec.get('HUAYSJ'));
	Huayy_e.setValue(rec.get('HUAYY'));
	Beiz_e.setValue(rec.get('BEIZ'));
	Rpt_window.show();
	CountValue();
}else{Ext.MessageBox.alert('����','����Ϊ0���ܱ༭�����������Ϣ���ܱ༭!');return;}
}
function EditGrid(){
	var rec = gridDiv_sm.getSelected();
	rec.set('MT',Mt_e.getValue());
	rec.set('MAD',Mad_e.getValue());
	rec.set('AAR',Aar_e.getValue());
	rec.set('AAD',Aad_e.getValue());
	rec.set('AD',Ad_e.getValue());
	rec.set('VAR',Var_e.getValue());
	rec.set('VAD',Vad_e.getValue());
	rec.set('VDAF',Vdaf_e.getValue());
	rec.set('FCAD',FCad_e.getValue());
	rec.set('QGRAD',Qgrad_e.getValue());
	rec.set('QGRD',Qgrd_e.getValue());
	rec.set('QNET_AR',Qnetar_e.getValue());
	rec.set('STAD',Stad_e.getValue());
	rec.set('STD',Std_e.getValue());
	rec.set('QBAD',Qbad_e.getValue());
	//rec.set('HAR',Har_e.getValue());
	rec.set('HAD',Had_e.getValue());
	rec.set('HDAF',Hdaf_e.getValue());
	rec.set('HUAYSJ',Huaysj_e.getValue());
	rec.set('HUAYBH',Huaybh_e.getValue());
	rec.set('HUAYY',Huayy_e.getValue());
	rec.set('BEIZ',Beiz_e.getValue());
	Rpt_window.hide();
}
function shenhztChk(){
	var rec=gridDiv_sm.getSelected();
	//�Ծ����ж�
	var IsequalsZero=rec.get('JINGZ');
	//alert('bobo');
	if(IsequalsZero>0){return true;}
	    else{return false;}
	//�������״̬���ж�
	var chk=rec.get('SHENHZT');
	if(chk==0){	return true;}
	else{return false;}
	
}

});