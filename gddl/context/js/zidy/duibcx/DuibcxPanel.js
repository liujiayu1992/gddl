var gridDiv_history = "";
Ext.onReady(function(){

	var url = "http://"+document.location.host+document.location.pathname;
	var end = url.indexOf("/app");
	url = url.substring(0,end);
	Ext.BLANK_IMAGE_URL = url+'/ext/resources/images/s.gif';
/**
  * 左侧快捷添加处~
  */   
//初始化form数据
    
var DataSourceCombobox = new Ext.form.ComboBox({fieldLabel: '数据类型', selectOnFocus:true, listeners:{'select':function(own,rec,row){document.getElementById("DataSrcID").value = own.value;document.getElementById("CHANGE").value="1";document.getElementById("RefurbishDataSourceButton").click();WaitMsgShow();}}, transform:'DataSrcCombo', lazyRender:true, width:260, triggerAction:'all', typeAhead:true, forceSelection:true, editable:false }); 
var ColCombobox = new Ext.form.ComboBox({fieldLabel: '字段', selectOnFocus:true, listeners:{'select':function(own,rec,row){document.getElementById("ColID").value = own.value;document.getElementById("CHANGE").value="0";document.getElementById("RefurbishDataSourceButton").click();WaitMsgShow();}}, transform:'ColCombo', lazyRender:true, width:260, triggerAction:'all', typeAhead:true, forceSelection:true, editable:false }); 
var Title = new Ext.form.TextField({ fieldLabel: '标题', width:260, value:document.getElementById('Title').value, selectOnFocus:true }); 
var Colwidth = new Ext.form.NumberField({ fieldLabel: '列宽', width:260, value:document.getElementById('Width').value, selectOnFocus:true }); 
var SupplierSelectcombo = new Ext.zr.select.Selectcombo({ fieldLabel: '供应商', multiSelect:true, transform:'GongysCombo', lazyRender:true, triggerAction:'all', typeAhead:true, width:260, listeners :{ 'Change':function(){ var val; if(this.getRawValue().substr(0,1)==','){ val=this.getRawValue().substring(1,this.getRawValue().length); }else{ val=this.getRawValue(); } SupplierTextarea.setValue(val); } }, forceSelection:true }); 
var SupplierTextarea = new Ext.form.TextArea({ labelSeparator :'', width:260, height:90, readOnly:true }); 
var CompanySelectcombo = new Ext.zr.select.Selectcombo({ fieldLabel: '电厂', multiSelect:true, transform:'DiancCombo', lazyRender:true, triggerAction:'all', typeAhead:true, width:260, listeners :{'Change':function(){ var val; if(this.getRawValue().substr(0,1)==','){ val=this.getRawValue().substring(1,this.getRawValue().length); }else{ val=this.getRawValue(); } CompanyTextarea.setValue(val); } }, forceSelection:true }); 
var CompanyTextarea = new Ext.form.TextArea({ labelSeparator :'', width:260, height:90, readOnly:true });
var Current=new Ext.form.Checkbox({ labelSeparator :'', boxLabel:'本期', checked:true, listeners:{'check':function(own,checked){if(!checked){Lastcompared.setValue(false);Samecompared.setValue(false);SameCumcompared.setValue(false);}}}, width:'auto' }); 
var Last=new Ext.form.Checkbox({ labelSeparator :'', boxLabel:'上期', listeners:{'check':function(own,checked){if(!checked){Lastcompared.setValue(false)}}}, width:'auto' }); 
var Same=new Ext.form.Checkbox({ labelSeparator :'', boxLabel:'同期', listeners:{'check':function(own,checked){if(!checked){Samecompared.setValue(false)}}}, width:'auto' }); 
var Cumulative=new Ext.form.Checkbox({ labelSeparator :'', boxLabel:'累计', listeners:{'check':function(own,checked){if(!checked){SameCumcompared.setValue(false)}}}, width:'auto' }); 
var Samecumulative=new Ext.form.Checkbox({ labelSeparator :'', boxLabel:'同期累计', listeners:{'check':function(own,checked){if(!checked){SameCumcompared.setValue(false)}}}, width:'auto' }); 
var Lastcompared=new Ext.form.Checkbox({ labelSeparator :'', boxLabel:'环比', listeners:{'check':function(own,checked){if(checked){Current.setValue(true);Last.setValue(true);}}}, width:'auto' }); 
var Samecompared=new Ext.form.Checkbox({ labelSeparator :'', boxLabel:'同比', listeners:{'check':function(own,checked){if(checked){Current.setValue(true);Same.setValue(true);}}}, width:'auto' }); 
var SameCumcompared=new Ext.form.Checkbox({ labelSeparator :'', boxLabel:'累比', listeners:{'check':function(own,checked){if(checked){Cumulative.setValue(true);Samecumulative.setValue(true);}}}, width:'auto' }); 
var Custom=new Ext.form.Checkbox({ labelSeparator :'', boxLabel:'对比期', listeners:{'check':function(own,checked){var numdisabled = true;if(checked){numdisabled=false;}CustomBNumber.setDisabled(numdisabled);CustomENumber.setDisabled(numdisabled);}}, width:'auto' }); 
var CustomBNumber=new Ext.form.NumberField({width:80,disabled:true}); 
var CustomENumber=new Ext.form.NumberField({width:80,disabled:true}); 
var CheckFieldSet = new Ext.form.FieldSet({ title:'对比项选择', height:100, width:360, layout : 'column', items : [ {columnWidth : .3,items : [Current,Last,Lastcompared,Custom]}, {columnWidth : .3, items : [Cumulative,Same,Samecompared,CustomBNumber]}, {columnWidth : .4, items : [{},Samecumulative,SameCumcompared,CustomENumber]}] });
var FastSetForm = new Ext.form.FormPanel({ region: 'west',width : 380,labelAlign: 'right', title: '数据列信息', labelWidth: 80, frame: true, items:[DataSourceCombobox, ColCombobox, Title, Colwidth, SupplierSelectcombo, SupplierTextarea, CompanySelectcombo, CompanyTextarea, CheckFieldSet], buttons:[{text: '添加',minWidth : 65,handler:function(){InsertCol();}}] }); 
/**
  * center
  */   
//上部 公式的处理
var MainHead = new Ext.form.TextField({fieldLabel: '主标题', anchor:'95%'}); 
var SubHead = new Ext.form.TextField({fieldLabel: '副标题', anchor:'95%'}); 
var FormulaWidth = new Ext.form.NumberField({fieldLabel: '公式列宽', anchor:'95%'}); 
var FormulaTextArea = new Ext.form.TextArea({ fieldLabel:'公式', anchor:'95%', height:75 }); 
var FormulaForm = new Ext.form.FormPanel({width : bodyWidth-380, height:150, labelAlign: 'left', title: '公式', labelWidth: 60, frame: true, layout : 'column',items:[{columnWidth :.4, layout: 'form',items:[MainHead, SubHead, FormulaWidth]},{columnWidth :.6, layout: 'form',items:[FormulaTextArea]}],buttons:[{text:'添加',minwidth:75,handler:function(){InsertFormula();}}]}); 



//下部 列的处理
var gridDiv_sm = new Ext.grid.RowSelectionModel()
var gridDiv_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),{header:'ID',dataIndex:'ID',hidden :true},{header:'列标识',dataIndex:'COLSIGN',width:80,hidden :false},{header:'主标题',dataIndex:'MAINHEAD',width:80,hidden :false},{header:'副标题',dataIndex:'SUBHEAD',width:80,editor :new Ext.form.TextField({
	allowBlank:true,
	maxLength:100,
	width:'auto',
	selectOnFocus:true
}),hidden :false},{header:'列宽',dataIndex:'COLWIDTH',width:80,editor :new Ext.form.TextField({
	allowBlank:true,
	maxLength:100,
	width:'auto',
	selectOnFocus:true
}),hidden :false},{header:'对齐',dataIndex:'COLALIGN',width:80,editor :new Ext.form.ComboBox({
	selectOnFocus:true,
	transform:'cbo_gridDiv_COLALIGN',
	lazyRender:true,
	triggerAction:'all',
	typeAhead:true,
	forceSelection:true
}),hidden :false},{header:'格式',dataIndex:'COLFORMAT',width:80,editor :new Ext.form.TextField({
	allowBlank:true,
	maxLength:100,
	width:'auto',
	selectOnFocus:true
}),hidden :false}]);
 gridDiv_cm.defaultSortable=true;

var gridDiv_ds = new Ext.data.Store({
proxy : new Ext.zr.data.PagingMemoryProxy(gridDiv_data),
pruneModifiedRecords:true,
reader: new Ext.data.ArrayReader({}, [
{name:'ID'},{name:'COLSIGN'},{name:'MAINHEAD'},{name:'SUBHEAD'},{name:'COLWIDTH'},{name:'COLALIGN'},{name:'COLFORMAT'}])});

var gridDiv_plant = Ext.data.Record.create([{name: 'ID', type:'string'},{name: 'COLSIGN', type:'string'},{name: 'MAINHEAD', type:'string'},{name: 'SUBHEAD', type:'string'},{name: 'COLWIDTH', type:'string'},{name: 'COLALIGN', type:'string'},{name: 'COLFORMAT', type:'string'}]);
var gridDiv_grid = new Ext.grid.EditorGridPanel({
cm:gridDiv_cm,
sm:gridDiv_sm,
ds:gridDiv_ds,
width:bodyWidth-380,
height:bodyHeight-160,
clicksToEdit:1,frame:true,tbar: [{text:' 删除',icon:'imgs/btnicon/delete.gif',cls:'x-btn-text-icon',minWidth:75,handler:function() {
Ext.MessageBox.confirm('提示信息','删除操作将不可撤销,是否确认删除?',function(btn){if(btn=='yes'){
for(i=0;i<gridDiv_sm.getSelections().length;i++){
	record = gridDiv_sm.getSelections()[i];
gridDiv_history += '<result>' + '<sign>D</sign>' + '<ID update="true">' + record.get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'
+ '<COLSIGN update="true">' + record.get('COLSIGN').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</COLSIGN>'
+ '<MAINHEAD update="true">' + record.get('MAINHEAD').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MAINHEAD>'
+ '<SUBHEAD update="true">' + record.get('SUBHEAD').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SUBHEAD>'
+ '<COLWIDTH update="true">' + record.get('COLWIDTH').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</COLWIDTH>'
+ '<COLALIGN update="true">' + record.get('COLALIGN').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</COLALIGN>'
+ '<COLFORMAT update="true">' + record.get('COLFORMAT').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</COLFORMAT>'
 + '</result>' ;	gridDiv_ds.remove(gridDiv_sm.getSelections()[i--]);} var Cobj = document.getElementById('CHANGE');
 Cobj.value = '<result>'+gridDiv_history+'</result>';document.getElementById('SaveButton').click();}else{return;}})}},
 {text:' 保存',icon:'imgs/btnicon/save.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){
var Mrcd = gridDiv_ds.getModifiedRecords();
for(i = 0; i< Mrcd.length; i++){
if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}
if(Mrcd[i].get('COLALIGN') == ''){Ext.MessageBox.alert('提示信息','字段 对齐 不能为空');return;
}gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update="true">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'
+ '<COLSIGN update="true">' + Mrcd[i].get('COLSIGN').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</COLSIGN>'
+ '<MAINHEAD update="true">' + Mrcd[i].get('MAINHEAD').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MAINHEAD>'
+ '<SUBHEAD update="true">' + Mrcd[i].get('SUBHEAD').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SUBHEAD>'
+ '<COLWIDTH update="true">' + Mrcd[i].get('COLWIDTH').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</COLWIDTH>'
+ '<COLALIGN update="true">' + Mrcd[i].get('COLALIGN').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</COLALIGN>'
+ '<COLFORMAT update="true">' + Mrcd[i].get('COLFORMAT').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</COLFORMAT>'
 + '</result>' ; }
if(gridDiv_history==''){ 
Ext.MessageBox.alert('提示信息','没有进行改动无需保存');
}else{
var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+'</result>';document.getElementById('SaveButton').click();Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});
}
}}, {text: '返回',icon:'imgs/btnicon/return.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById("ReturnButton").click();}}],
stripeRows:true});
gridDiv_ds.load();
gridDiv_ds.on('datachanged',function(){ gridDiv_history = ''; });

var rptPanel = new Ext.Panel({region: 'center',items:[FormulaForm,gridDiv_grid]});

/**
 * 整体面板加载
 */            
var vp = new Ext.Viewport({layout: 'border',items: [FastSetForm, rptPanel]});
vp.show('body');
	
	
/**
 * 基础函数定义处  
 */
function validatenull(title,value){if(value==null || value==""){Ext.MessageBox.alert('提示信息',title + '不能为空,请输入后添加!');return true;}return false}
function InsertCol(){if(validatenull('标题',Title.getValue()))return;document.getElementById("Title").value = Title.getValue();document.getElementById("Width").value = Colwidth.getValue();document.getElementById('Gongys').value = SupplierTextarea.getValue();document.getElementById('Dianc').value = CompanyTextarea.getValue();var param = "";if(Current.getValue()){param += "本期;";}if(Last.getValue()){param += "上期;";}if(Same.getValue()){param += "同期;";}if(Cumulative.getValue()){param += "累计;";}if(Samecumulative.getValue()){param += "同期累计;";}if(Lastcompared.getValue()){param += "环比;";}if(Samecompared.getValue()){param += "同比;";}if(SameCumcompared.getValue()){param += "同期累比;";}if(Custom.getValue()){param += "对比期;";if(CustomBNumber.getValue()=="" || CustomENumber.getValue()==""){Ext.MessageBox.alert('提示信息','请输入对比期比较时间');return;}document.getElementById('FieldCHANGE').value=CustomBNumber.getValue() + ";" + CustomENumber.getValue();}document.getElementById('CHANGE').value = param;document.getElementById("InsertColButton").click();}
function InsertFormula(){if(validatenull('标题',MainHead.getValue()))return;if(validatenull('公式',FormulaTextArea.getValue()))return;document.getElementById("Title").value = MainHead.getValue();document.getElementById("Width").value = FormulaWidth.getValue();document.getElementById('CHANGE').value = SubHead.getValue();document.getElementById('FieldCHANGE').value=FormulaTextArea.getValue();document.getElementById("InsertFormulaButton").click();}
function WaitMsgShow(){Ext.MessageBox.show({msg: '正在进行处理,请等待...',progressText: '处理中...',width:280,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO });};
function MsgShow(){Ext.MessageBox.show({msg: '正在进行报表解析,请等待...',progressText: '计算中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO });};
    
});
    