/**
 * Created by liuzhiyu on 2017/4/11.
 */
function (){
Ext.MessageBox.confirm('��ʾ��Ϣ','���á������Ϣ�Ƿ���ȷ��',function(btn){if(btn == 'yes'){
var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();
for(i = 0; i< Mrcd.length; i++){
if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}
if(Mrcd[i].get('DANGRGM') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�DANGRGM ������Сֵ -100000000');return;
}if( Mrcd[i].get('DANGRGM') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� DANGRGM �������ֵ 100000000000');return;
}if(Mrcd[i].get('DANGRGM')!=0 && Mrcd[i].get('DANGRGM') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� DANGRGM ����Ϊ��');return;
}if(Mrcd[i].get('FADY') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶη����� ������Сֵ -100000000');return;
}if( Mrcd[i].get('FADY') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;
}if(Mrcd[i].get('FADY')!=0 && Mrcd[i].get('FADY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;
}if(Mrcd[i].get('GONGRY') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶι����� ������Сֵ -100000000');return;
}if( Mrcd[i].get('GONGRY') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;
}if(Mrcd[i].get('GONGRY')!=0 && Mrcd[i].get('GONGRY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;
}if(Mrcd[i].get('QITY') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ������� ������Сֵ -100000000');return;
}if( Mrcd[i].get('QITY') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;
}if(Mrcd[i].get('QITY')!=0 && Mrcd[i].get('QITY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;
}if(Mrcd[i].get('CUNS') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶδ��� ������Сֵ -100000000');return;
}if( Mrcd[i].get('CUNS') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ���� �������ֵ 100000000000');return;
}if(Mrcd[i].get('CUNS')!=0 && Mrcd[i].get('CUNS') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;
}if(Mrcd[i].get('TIAOZL') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶε����� ������Сֵ -100000000');return;
}if( Mrcd[i].get('TIAOZL') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;
}if(Mrcd[i].get('TIAOZL')!=0 && Mrcd[i].get('TIAOZL') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;
}if(Mrcd[i].get('SHUIFCTZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�ˮ�ֲ���� ������Сֵ -100000000');return;
}if( Mrcd[i].get('SHUIFCTZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ˮ�ֲ���� �������ֵ 100000000000');return;
}if(Mrcd[i].get('SHUIFCTZ')!=0 && Mrcd[i].get('SHUIFCTZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ˮ�ֲ���� ����Ϊ��');return;
}if(Mrcd[i].get('PANYK') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ���ӯ�� ������Сֵ -100000000');return;
}if( Mrcd[i].get('PANYK') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ��ӯ�� �������ֵ 100000000000');return;
}if(Mrcd[i].get('PANYK')!=0 && Mrcd[i].get('PANYK') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ��ӯ�� ����Ϊ��');return;
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
Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');
}else{
var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';
document.getElementById('SaveButton').click();
Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});
}
};});
}