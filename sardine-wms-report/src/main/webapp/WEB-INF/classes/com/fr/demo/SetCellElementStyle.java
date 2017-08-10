//��Ԫ���ʽ����
package com.fr.demo;  
  
import java.awt.Color;  
import java.awt.Font;
import java.util.Map;

import com.fr.base.Style;  
import com.fr.base.background.ColorBackground;  
import com.fr.general.FRFont;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.Constants;
import com.fr.stable.unit.OLDPIX;
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;
import com.fr.main.TemplateWorkBook;  
import com.fr.main.impl.WorkBook;
  
  
public class SetCellElementStyle extends Reportlet {  
    public TemplateWorkBook createReport(ReportletRequest arg0) {  
        // �½�����  
        WorkBook workbook = new WorkBook();  
        WorkSheet worksheet = new WorkSheet();  
        // �½�һ����Ԫ��λ��Ϊ(1,1),��ռ2��Ԫ����ռ2��Ԫ���ı�ֵΪ "FineReport"  
        TemplateCellElement cellElement = new DefaultTemplateCellElement(1, 1,  
                2, 2, "FineReport");  
        // �����п�Ϊ300px�������и�Ϊ30px  
        worksheet.setColumnWidth(1, new OLDPIX(300));  
        worksheet.setRowHeight(1, new OLDPIX(30));  
        // �õ�CellElement����ʽ�����û���½�Ĭ����ʽ  
        Style style = cellElement.getStyle();  
        if (style == null) {  
            style = Style.getInstance();  
        }  
        // ���������ǰ������ɫ  
        FRFont frFont = FRFont.getInstance("Dialog", Font.BOLD, 16);  
        frFont = frFont.applyForeground(new Color(21, 76, 160));  
        style = style.deriveFRFont(frFont);  
        // ���ñ���  
        ColorBackground background = ColorBackground.getInstance(new Color(255,  
                255, 177));  
        style = style.deriveBackground(background);  
        // ����ˮƽ����  
        style = style.deriveHorizontalAlignment(Constants.CENTER);  
        // ���ñ߿�  
        style = style.deriveBorder(Constants.LINE_DASH, Color.red,  
                Constants.LINE_DOT, Color.gray, Constants.LINE_DASH_DOT,  
                Color.BLUE, Constants.LINE_DOUBLE, Color.CYAN);  
        // �ı䵥Ԫ�����ʽ  
        cellElement.setStyle(style);  
        // ����Ԫ����ӵ�������  
        worksheet.addCellElement(cellElement);  
        workbook.addReport(worksheet);  
        return workbook;  
    }

	@Override
	public void setParameterMap(Map arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTplPath(String arg0) {
		// TODO Auto-generated method stub
		
	}  
}