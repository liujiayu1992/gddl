package com.zhiren.common.ext;


import com.zhiren.common.DateUtil;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;

import java.util.Date;

/**
 * Created by zhiyu on 2017/3/15.
 */
public class HuayshExtGridUtil extends ExtGridUtil {

    public HuayshExtGridUtil(String gridId, ResultSetList rsl, String CustomKey) {
        this.gridId = gridId;
        setExtGridColumn(rsl,CustomKey);
    }
    public void readCustomSet(){

    }
   /* public void setExtGridColumn(ResultSetList rsl){
        addColumn(new GridColumn(GridColumn.ColType_Rownum));
        for (int i = 0; i < rsl.getColumnNames().length; i++) {
            GridColumn gc = new GridColumn(rsl.getColumnNames()[i], rsl.getColumnNames()[i]);
            if ("id".equals(rsl.getColumnNames()[i].toLowerCase())) {
                gc.setDefaultValue("0");
                gc.setEditor(null);
                gc.setHidden(true);
            } else {
                String extType = OratypeOfExt(rsl.getColumnTypes()[i]);
                gc.setDataType(extType);
                if (GridColumn.DataType_Float.equals(extType)) {
                    gc.setEditor(new NumberField());
                    gc.setAlign("right");
                    ((NumberField) gc.editor).setDecimalPrecision(rsl.getColScales()[i]);
                } else if (GridColumn.DataType_String.equals(extType)) {
                    gc.setEditor(new TextField());
                   // ((TextField) gc.editor).setMaxLength(rsl.getColPrecisions()[i]);
                } else if (GridColumn.DataType_Date.equals(extType)) {
                    gc.setEditor(new DateField());
                    gc.setRenderer(GridColumn.Renderer_Date);
                    for (int m = 0; m < rsl.getRows(); m++) {
                        if(rsl.getString(m, i) != null && !"".equals(rsl.getString(m,i))){
                            rsl.setString(m, i, DateUtil.FormatDate(new Date(Long
                                    .parseLong(rsl.getString(m, i)))));
                        }

                    }
                }
                if (gc.editor != null) {
                    gc.editor.allowBlank = rsl.isNullable(i);
                }
            }
            addColumn(gc);
        }
        initData(rsl.getRows(), rsl.getColumnCount());
        while (rsl.next()) {
            setRowData(rsl.getRow(), rsl.getRowString());
        }
        rsl.close();
    }*/
}
