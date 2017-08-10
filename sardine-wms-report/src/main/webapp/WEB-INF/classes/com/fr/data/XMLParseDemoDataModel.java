package com.fr.data;  
  
import java.io.File;  
import java.util.ArrayList;  
import java.util.List;  
import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;  
import org.xml.sax.Attributes;  
import org.xml.sax.SAXException;  
import org.xml.sax.helpers.DefaultHandler;  
import com.fr.base.FRContext; 
import com.fr.data.AbstractDataModel;  
import com.fr.general.ComparatorUtils;
import com.fr.general.data.TableDataException;
  
/** 
 * XMLParseDemoDataModel 
 *  
 * DataModel�ǻ�ȡ���ݵĽӿ� 
 *  
 * ����ͨ��init����һ����ȡ���󣬹���һ����ά�������ʵ��DataModel�ĸ���ȡ������ 
 */  
public class XMLParseDemoDataModel extends AbstractDataModel {  
    // �������ͱ�ʶ  
    public static final int COLUMN_TYPE_STRING = 0;  
    public static final int COLUMN_TYPE_INTEGER = 1;  
    public static final int COLUMN_TYPE_BOOLEAN = 2;  
  
    // ����ȡ����������  
    protected List row_list = null;  
  
    // ���ݶ�Ӧ�Ľڵ�·��  
    private String[] xPath;  
    // �ڵ�·���°�������Ҫȡ���Ľڵ�  
    private XMLColumnNameType4Demo[] columns;  
  
    private String filePath;  
  
    public XMLParseDemoDataModel(String filename, String[] xPath,  
            XMLColumnNameType4Demo[] columns) {  
        this.filePath = filename;  
        this.xPath = xPath;  
        this.columns = columns;  
    }  
  
    /** 
     * ȡ���е����� 
     */  
    public int getColumnCount() throws TableDataException {  
        return columns.length;  
    }  
  
    /** 
     * ȡ����Ӧ���е����� 
     */  
    public String getColumnName(int columnIndex) throws TableDataException {  
        if (columnIndex < 0 || columnIndex >= columns.length)  
            return null;  
        String columnName = columns[columnIndex] == null ? null  
                : columns[columnIndex].getName();  
  
        return columnName;  
    }  
  
    /** 
     * ȡ���õ��Ľ�������ܵ����� 
     */  
    public int getRowCount() throws TableDataException {  
        this.init();  
        return row_list.size();  
    }  
  
    /** 
     * ȡ����Ӧλ�õ�ֵ 
     */  
    public Object getValueAt(int rowIndex, int columnIndex)  
            throws TableDataException {  
        this.init();  
        if (rowIndex < 0 || rowIndex >= row_list.size() || columnIndex < 0  
                || columnIndex >= columns.length)  
            return null;  
        return ((Object[]) row_list.get(rowIndex))[columnIndex];  
    }  
  
    /** 
     * �ͷ�һЩ��Դ��ȡ�������󣬵��ô˷������ͷ���Դ 
     */  
    public void release() throws Exception {  
        if (this.row_list != null) {  
            this.row_list.clear();  
            this.row_list = null;  
        }  
    }  
  
    /** ************************************************** */  
    /** ***********������ʵ��DataModel�ķ���*************** */  
    /** ************************************************** */  
  
    /** ************************************************** */  
    /** ************����Ϊ����XML�ļ��ķ���**************** */  
    /** ************************************************** */  
  
    // һ���Խ�����ȡ����  
    protected void init() throws TableDataException {  
        if (this.row_list != null)  
            return;  
  
        this.row_list = new ArrayList();  
        try {  
            // ʹ��SAX����XML�ļ��� ʹ�÷�����μ�JAVA SAX����  
            SAXParserFactory f = SAXParserFactory.newInstance();  
            SAXParser parser = f.newSAXParser();  
  
            parser.parse(new File(XMLParseDemoDataModel.this.filePath),  
                    new DemoHandler());  
        } catch (Exception e) {  
            e.printStackTrace();  
            FRContext.getLogger().error(e.getMessage(), e);  
        }  
    }  
  
    /** 
     * ����ԭ����ǽ������ڱ����ļ�ʱ ���ֽڵ㿪ʼ���ʱ������startElement���� ��ȡ�ڵ��ڲ�����ʱ������characters���� 
     * ���ֽڵ�������ʱ������endElement 
     */  
    private class DemoHandler extends DefaultHandler {  
        private List levelList = new ArrayList(); // ��¼��ǰ�ڵ��·��  
        private Object[] values; // ����һ����¼  
        private int recordIndex = -1; // ��ǰ��¼����Ӧ���е���ţ�-1��ʾ����Ҫ��¼  
  
        public void startElement(String uri, String localName, String qName,  
                Attributes attributes) throws SAXException {  
            // ��¼��  
            levelList.add(qName);  
  
            if (isRecordWrapTag()) {  
                // ��ʼһ�������ݵļ�¼  
                values = new Object[XMLParseDemoDataModel.this.columns.length];  
            } else if (needReadRecord()) {  
                // �������Ӧ������ţ������characters֮��ִ��ʱ��������������������ֵ��ŵ�λ�á�  
                recordIndex = getColumnIndex(qName);  
            }  
        }  
  
        public void characters(char[] ch, int start, int length)  
                throws SAXException {  
            if (recordIndex > -1) {  
                // ��ȡֵ  
                String text = new String(ch, start, length);  
                XMLColumnNameType4Demo type = XMLParseDemoDataModel.this.columns[recordIndex];  
                Object value = null;  
                if (type.getType() == COLUMN_TYPE_STRING) {  
                    value = text;  
                }  
                if (type.getType() == COLUMN_TYPE_INTEGER) {  
                    value = new Integer(text);  
                } else if (type.getType() == COLUMN_TYPE_BOOLEAN) {  
                    value = new Boolean(text);  
                }  
  
                values[recordIndex] = value;  
            }  
        }  
  
        public void endElement(String uri, String localName, String qName)  
                throws SAXException {  
            try {  
                if (isRecordWrapTag()) {  
                    // һ����¼��������add��list��  
                    XMLParseDemoDataModel.this.row_list.add(values);  
                    values = null;  
                } else if (needReadRecord()) {  
                    recordIndex = -1;  
                }  
            } finally {  
                levelList.remove(levelList.size() - 1);  
            }  
        }  
  
        // ����ƥ��·����ȷ���Ǽ�¼�ⲿ��Tag  
        private boolean isRecordWrapTag() {  
            if (levelList.size() == XMLParseDemoDataModel.this.xPath.length  
                    && compareXPath()) {  
                return true;  
            }  
  
            return false;  
        }  
  
        // ��Ҫ��¼һ����¼  
        private boolean needReadRecord() {  
            if (levelList.size() == (XMLParseDemoDataModel.this.xPath.length + 1)  
                    && compareXPath()) {  
                return true;  
            }  
  
            return false;  
        }  
  
        // �Ƿ�ƥ���趨��XPath·��  
        private boolean compareXPath() {  
            String[] xPath = XMLParseDemoDataModel.this.xPath;  
            for (int i = 0; i < xPath.length; i++) {  
                if (!ComparatorUtils.equals(xPath[i], levelList.get(i))) {  
                    return false;  
                }  
            }  
  
            return true;  
        }  
  
        // ��ȡ���ֶε����  
        private int getColumnIndex(String columnName) {  
            XMLColumnNameType4Demo[] nts = XMLParseDemoDataModel.this.columns;  
            for (int i = 0; i < nts.length; i++) {  
                if (ComparatorUtils.equals(nts[i].getName(), columnName)) {  
                    return i;  
                }  
            }  
  
            return -1;  
        }  
    }  
}