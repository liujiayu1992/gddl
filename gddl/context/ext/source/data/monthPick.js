//monthPick.js
//----------------
Ext.ux.MonthPicker=Ext.extend(Ext.Component,{
    format:"M-y",
    okText:Ext.DatePicker.prototype.okText,
    cancelText:Ext.DatePicker.prototype.cancelText,
    constrainToViewport:true,
    monthNames:Date.monthNames,
    startDay:0,
    value:0,
    noPastYears:false,
    initComponent:function () {
        Ext.ux.MonthPicker.superclass.initComponent.call(this);
        this.value=this.value?
            this.value.clearTime():new Date().clearTime();
        this.addEvents(
            'select'
        );
        if(this.handler) {
            this.on("select",this.handler,this.scope||this);
        }
    },
    focus:function () {
        if(this.el) {
            this.update(this.activeDate);
        }
    },
    onRender:function (container,position) {
        var m=['<div style="width: 200px; height:175px;"></div>']
        m[m.length]='<div class="x-date-mp"></div>';
        var el=document.createElement("div");
        el.className="x-date-picker";
        el.innerHTML=m.join("");
        container.dom.insertBefore(el,position);
        this.el=Ext.get(el);
        this.monthPicker=this.el.down('div.x-date-mp');
        this.monthPicker.enableDisplayMode('block');
        this.el.unselectable();
        this.showMonthPicker();
        if(Ext.isIE) {
            this.el.repaint();
        }
        this.update(this.value);
    },
    createMonthPicker:function () {
        if(!this.monthPicker.dom.firstChild) {
            var buf=['<table border="0" cellspacing="0">'];
            for(var i=0;i<6;i++) {
                buf.push(
                    '<tr><td class="x-date-mp-month"><a href="#">',this.monthNames[i].substr(0,3),'</a></td>',
                    '<td class="x-date-mp-month x-date-mp-sep"><a href="#">',this.monthNames[i+6].substr(0,3),'</a></td>',
                    i==0?
                        '<td class="x-date-mp-ybtn" align="center"><a class="x-date-mp-prev"></a></td><td class="x-date-mp-ybtn" align="center"><a class="x-date-mp-next"></a></td></tr>':
                        '<td class="x-date-mp-year"><a href="#"></a></td><td class="x-date-mp-year"><a href="#"></a></td></tr>'
                );
            }
            buf.push(
                '<tr class="x-date-mp-btns"><td colspan="4"><button type="button" class="x-date-mp-ok">',
                this.okText,
                '</button><button type="button" class="x-date-mp-cancel">',
                this.cancelText,
                '</button></td></tr>',
                '</table>'
            );
            this.monthPicker.update(buf.join(''));
            this.monthPicker.on('click',this.onMonthClick,this);
            this.monthPicker.on('dblclick',this.onMonthDblClick,this);
            this.mpMonths=this.monthPicker.select('td.x-date-mp-month');
            this.mpYears=this.monthPicker.select('td.x-date-mp-year');
            this.mpMonths.each(function (m,a,i) {
                i+=1;
                if((i%2)==0) {
                    m.dom.xmonth=5+Math.round(i*.5);
                }else {
                    m.dom.xmonth=Math.round((i-1)*.5);
                }
            });
        }
    },
    showMonthPicker:function () {
        this.createMonthPicker();
        var size=this.el.getSize();
        this.monthPicker.setSize(size);
        this.monthPicker.child('table').setSize(size);
        this.mpSelMonth=(this.activeDate||this.value).getMonth();
        this.updateMPMonth(this.mpSelMonth);
        this.mpSelYear=(this.activeDate||this.value).getFullYear();
        this.updateMPYear(this.mpSelYear);
        this.monthPicker.show();
        //this.monthPicker.slideIn('t', {duration:.2});
    },
    updateMPYear:function (y) {
        if(this.noPastYears) {
            var minYear=new Date().getFullYear();
            if(y<(minYear+4)) {
                y=minYear+4;
            }
        }
        this.mpyear=y;
        var ys=this.mpYears.elements;
        for(var i=1;i<=10;i++) {
            var td=ys[i-1],y2;
            if((i%2)==0) {
                y2=y+Math.round(i*.5);
                td.firstChild.innerHTML=y2;
                td.xyear=y2;
            }else {
                y2=y-(5-Math.round(i*.5));
                td.firstChild.innerHTML=y2;
                td.xyear=y2;
            }
            this.mpYears.item(i-1)[y2==this.mpSelYear?'addClass':'removeClass']('x-date-mp-sel');
        }
    },
    updateMPMonth:function (sm) {
        this.mpMonths.each(function (m,a,i) {
            m[m.dom.xmonth==sm?'addClass':'removeClass']('x-date-mp-sel');
        });
    },
    selectMPMonth:function (m) {
    },
    onMonthClick:function (e,t) {
        e.stopEvent();
        var el=new Ext.Element(t),pn;
        if(el.is('button.x-date-mp-cancel')) {
            this.hideMonthPicker();
            //this.fireEvent("select", this, this.value);
        }
        else if(el.is('button.x-date-mp-ok')) {
            this.update(new Date(this.mpSelYear,this.mpSelMonth,(this.activeDate||this.value).getDate()));
            //this.hideMonthPicker();
            this.fireEvent("select",this,this.value);
        }
        else if(pn=el.up('td.x-date-mp-month',2)) {
            this.mpMonths.removeClass('x-date-mp-sel');
            pn.addClass('x-date-mp-sel');
            this.mpSelMonth=pn.dom.xmonth;
        }
        else if(pn=el.up('td.x-date-mp-year',2)) {
            this.mpYears.removeClass('x-date-mp-sel');
            pn.addClass('x-date-mp-sel');
            this.mpSelYear=pn.dom.xyear;
        }
        else if(el.is('a.x-date-mp-prev')) {
            this.updateMPYear(this.mpyear-10);
        }
        else if(el.is('a.x-date-mp-next')) {
            this.updateMPYear(this.mpyear+10);
        }
    },
    onMonthDblClick:function (e,t) {
        e.stopEvent();
        var el=new Ext.Element(t),pn;
        if(pn=el.up('td.x-date-mp-month',2)) {
            this.update(new Date(this.mpSelYear,pn.dom.xmonth,(this.activeDate||this.value).getDate()));
            //this.hideMonthPicker();
            this.fireEvent("select",this,this.value);
        }
        else if(pn=el.up('td.x-date-mp-year',2)) {
            this.update(new Date(pn.dom.xyear,this.mpSelMonth,(this.activeDate||this.value).getDate()));
            //this.hideMonthPicker();
            this.fireEvent("select",this,this.value);
        }
    },
    hideMonthPicker:function (disableAnim) {
        Ext.menu.MenuMgr.hideAll();
    },
    showPrevMonth:function (e) {
        this.update(this.activeDate.add("mo",-1));
    },
    showNextMonth:function (e) {
        this.update(this.activeDate.add("mo",1));
    },
    showPrevYear:function () {
        this.update(this.activeDate.add("y",-1));
    },
    showNextYear:function () {
        this.update(this.activeDate.add("y",1));
    },
    update:function (date) {
        this.activeDate=date;
        this.value=date;
        if(!this.internalRender) {
            var main=this.el.dom.firstChild;
            var w=main.offsetWidth;
            this.el.setWidth(w+this.el.getBorderWidth("lr"));
            Ext.fly(main).setWidth(w);
            this.internalRender=true;
            if(Ext.isOpera&&!this.secondPass) {
                main.rows[0].cells[1].style.width=(w-(main.rows[0].cells[0].offsetWidth+main.rows[0].cells[2].offsetWidth))+"px";
                this.secondPass=true;
                this.update.defer(10,this,[date]);
            }
        }
    }
});
Ext.reg('monthpicker',Ext.ux.MonthPicker);

Ext.ux.MonthItem=function (config) {
    Ext.ux.MonthItem.superclass.constructor .call(this,new Ext.ux.MonthPicker(config),config);
    this.picker=this.component;
    this.addEvents('select');
    this.picker.on("render",function (picker) {
        picker.getEl().swallowEvent("click");
        picker.container.addClass("x-menu-date-item");
    });
    this.picker.on("select",this.onSelect,this);
};
Ext.extend(Ext.ux.MonthItem,Ext.menu.Adapter,{
    onSelect:function (picker,date) {
        this.fireEvent("select",this,date,picker);
        Ext.ux.MonthItem.superclass.handleClick.call(this);
    }
});
Ext.ux.MonthMenu=function (config) {
    Ext.ux.MonthMenu.superclass.constructor .call(this,config);
    this.plain=true;
    var mi=new Ext.ux.MonthItem(config);
    this.add(mi);
    this.picker=mi.picker;
    this.relayEvents(mi,["select"]);
};
Ext.extend(Ext.ux.MonthMenu,Ext.menu.Menu,{
    cls:'x-date-menu'
});
Ext.ux.MonthField=function (cfg) {
    Ext.ux.MonthField.superclass.constructor .call(this,Ext.apply({
    },cfg||{
        }));
}
Ext.extend(Ext.ux.MonthField,Ext.form.DateField,{
    format:"Y-m",
    triggerClass:"x-form-date-trigger",
    menuListeners:{
        select:function (m,d) {
            this.setValue(d.format(this.format));
        },
        show:function () {
            this.onFocus();
        },
        hide:function () {
            this.focus.defer(10,this);
            var ml=this.menuListeners;
            this.menu.un("select",ml.select,this);
            this.menu.un("show",ml.show,this);
            this.menu.un("hide",ml.hide,this);
        }
    },
    onTriggerClick:function () {
        if(this.disabled) {
            return ;
        }
        if(this.menu==null) {
            this.menu=new Ext.ux.MonthMenu();
        }
        Ext.apply(this.menu.picker,{
        });
        this.menu.on(Ext.apply({
        },this.menuListeners,{
            scope:this
        }));
        this.menu.show(this.el,"tl-bl?");
    }
});
Ext.reg("monthfield",Ext.ux.MonthField);