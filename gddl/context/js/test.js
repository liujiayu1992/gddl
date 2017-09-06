/**
 * Created by liuzhiyu on 2017/4/11.
 */
function (){
Ext.MessageBox.confirm('提示信息','耗用、库存信息是否正确？',function(btn){if(btn == 'yes'){
var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();
for(i = 0; i< Mrcd.length; i++){
if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}
if(Mrcd[i].get('DANGRGM') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段DANGRGM 低于最小值 -100000000');return;
}if( Mrcd[i].get('DANGRGM') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 DANGRGM 高于最大值 100000000000');return;
}if(Mrcd[i].get('DANGRGM')!=0 && Mrcd[i].get('DANGRGM') == ''){Ext.MessageBox.alert('提示信息','字段 DANGRGM 不能为空');return;
}if(Mrcd[i].get('FADY') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段发电用 低于最小值 -100000000');return;
}if( Mrcd[i].get('FADY') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 发电用 高于最大值 100000000000');return;
}if(Mrcd[i].get('FADY')!=0 && Mrcd[i].get('FADY') == ''){Ext.MessageBox.alert('提示信息','字段 发电用 不能为空');return;
}if(Mrcd[i].get('GONGRY') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段供热用 低于最小值 -100000000');return;
}if( Mrcd[i].get('GONGRY') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 供热用 高于最大值 100000000000');return;
}if(Mrcd[i].get('GONGRY')!=0 && Mrcd[i].get('GONGRY') == ''){Ext.MessageBox.alert('提示信息','字段 供热用 不能为空');return;
}if(Mrcd[i].get('QITY') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段其它用 低于最小值 -100000000');return;
}if( Mrcd[i].get('QITY') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 其它用 高于最大值 100000000000');return;
}if(Mrcd[i].get('QITY')!=0 && Mrcd[i].get('QITY') == ''){Ext.MessageBox.alert('提示信息','字段 其它用 不能为空');return;
}if(Mrcd[i].get('CUNS') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段存损 低于最小值 -100000000');return;
}if( Mrcd[i].get('CUNS') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 存损 高于最大值 100000000000');return;
}if(Mrcd[i].get('CUNS')!=0 && Mrcd[i].get('CUNS') == ''){Ext.MessageBox.alert('提示信息','字段 存损 不能为空');return;
}if(Mrcd[i].get('TIAOZL') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段调整量 低于最小值 -100000000');return;
}if( Mrcd[i].get('TIAOZL') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 调整量 高于最大值 100000000000');return;
}if(Mrcd[i].get('TIAOZL')!=0 && Mrcd[i].get('TIAOZL') == ''){Ext.MessageBox.alert('提示信息','字段 调整量 不能为空');return;
}if(Mrcd[i].get('SHUIFCTZ') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段水分差调整 低于最小值 -100000000');return;
}if( Mrcd[i].get('SHUIFCTZ') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 水分差调整 高于最大值 100000000000');return;
}if(Mrcd[i].get('SHUIFCTZ')!=0 && Mrcd[i].get('SHUIFCTZ') == ''){Ext.MessageBox.alert('提示信息','字段 水分差调整 不能为空');return;
}if(Mrcd[i].get('PANYK') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段盘盈亏 低于最小值 -100000000');return;
}if( Mrcd[i].get('PANYK') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 盘盈亏 高于最大值 100000000000');return;
}if(Mrcd[i].get('PANYK')!=0 && Mrcd[i].get('PANYK') == ''){Ext.MessageBox.alert('提示信息','字段 盘盈亏 不能为空');return;
}gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\true\>' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'
+ '<FADL update=\true\>' + Mrcd[i].get('FADL')+ '</FADL>'
+ '<GONGRL update=\true\>' + Mrcd[i].get('GONGRL')+ '</GONGRL>'
+ '<JINGZ update=\true\>' + Mrcd[i].get('JINGZ')+ '</JINGZ>'
+ '<DANGRGM update=\true\>' + Mrcd[i].get('DANGRGM')+ '</DANGRGM>'
+ '<BIAOZ update=\true\>' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'
+ '<YUNS update=\true\>' + Mrcd[i].get('YUNS')+ '</YUNS>'
+ '<YINGD update=\true\>' + Mrcd[i].get('YINGD')+ '</YINGD>'
+ '<KUID update=\true\>' + Mrcd[i].get('KUID')+ '</KUID>'
+ '<FADY update=\true\>' + Mrcd[i].get('FADY')+ '</FADY>'
+ '<GONGRY update=\true\>' + Mrcd[i].get('GONGRY')+ '</GONGRY>'
+ '<QITY update=\true\>' + Mrcd[i].get('QITY')+ '</QITY>'
+ '<HAOYQKDR update=\true\>' + Mrcd[i].get('HAOYQKDR')+ '</HAOYQKDR>'
+ '<FEISCY update=\true\>' + Mrcd[i].get('FEISCY')+ '</FEISCY>'
+ '<CUNS update=\true\>' + Mrcd[i].get('CUNS')+ '</CUNS>'
+ '<TIAOZL update=\true\>' + Mrcd[i].get('TIAOZL')+ '</TIAOZL>'
+ '<SHUIFCTZ update=\true\>' + Mrcd[i].get('SHUIFCTZ')+ '</SHUIFCTZ>'
+ '<PANYK update=\true\>' + Mrcd[i].get('PANYK')+ '</PANYK>'
+ '<KUC update=\true\>' + Mrcd[i].get('KUC')+ '</KUC>'
+ '<CHANGWML update=\true\>' + Mrcd[i].get('CHANGWML')+ '</CHANGWML>'
+ '<BUKDML update=\true\>' + Mrcd[i].get('BUKDML')+ '</BUKDML>'
+ '<KEDKC update=\true\>' + Mrcd[i].get('KEDKC')+ '</KEDKC>'
 + '</result>' ; }
if(gridDiv_history=='' && gridDivsave_history==''){
Ext.MessageBox.alert('提示信息','没有进行改动无需保存');
}else{
var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';
document.getElementById('SaveButton').click();
Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});
}
};});
}