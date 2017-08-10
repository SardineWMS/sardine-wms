package com.fr.data;  
  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.Reader;  
import java.util.logging.Level;  
import com.fr.base.FRContext;   
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;
  
public class GetXmlDate {  
    // ���巵��ֵ����  
    private String[] Value = new String[3];  
    // �����ѯ��nameֵ  
    private String[] Name = null;  
  
    protected String[] readerXMLSource(InputStream in, String[] name)  
            throws Exception {  
        Name = name;  
        InputStreamReader reader = new InputStreamReader(in, "utf-8");  
        readXMLSource(reader);  
        return Value;  
    }  
  
    protected void readXMLSource(Reader reader) throws Exception {  
        XMLableReader xmlReader = XMLableReader.createXMLableReader(reader);  
        if (xmlReader != null) {  
            xmlReader.readXMLObject(new Content());  
  
        }  
    }  
  
    private class Content implements XMLReadable {  
        public void readXML(XMLableReader reader) {  
            if (reader.isChildNode()) {  
                if (reader.getTagName().equals("Field")) {  
                    Field field = new Field();  
                    reader.readXMLObject(field);  
                    // ���name��Ӧ��valueֵ  
                    if (Name[0].equals(field.name)) {  
                        Value[0] = field.value;  
                    } else if (Name[1].equals(field.name)) {  
                        Value[1] = field.value;  
                    } else if (Name[2].equals(field.name)) {  
                        Value[2] = field.value;  
                    }  
                }  
            }  
        }  
    }  
  
    // ����ÿ��field�Ľṹ  
    private class Field implements XMLReadable {  
        private String name;  
        private String type;  
        private String value;  
  
        public void readXML(XMLableReader reader) {  
            if (reader.isChildNode()) {  
                String tagName = reader.getTagName();  
                if (tagName.equals("Name")) {  
                    this.name = reader.getElementValue();  
                } else if (tagName.equals("Type")) {  
                    this.type = reader.getElementValue();  
                } else if (tagName.equals("Value")) {  
                    this.value = reader.getElementValue();  
                }  
            }  
        }  
    }  
}