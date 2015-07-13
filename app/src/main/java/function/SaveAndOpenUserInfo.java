package function;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import bean.BaseObject;
import bean.UserInfo;

/**
 * Created by zhuchao on 7/12/15.
 * This class is to save and open the configure file of user info
 * You will implement two functions below.
 */
public class SaveAndOpenUserInfo implements ParseXml{
    private static String TAG="UserInfoProcessXml";
    /**
     * save user info
     * Created by LMZ on 7/13/15
     * @param context
     * @param objects
     */
    @Override
    public void Save(Context context, ArrayList<BaseObject> objects) {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
        try{
            xmlSerializer.setOutput(stringWriter);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag("", "UserShow");
            for(int i=0;i<objects.size();i++){
                //强制转换：父类型转换为子类型
                UserInfo info=(UserInfo)objects.get(i);
                xmlSerializer.startTag("", "userInfo");
                xmlSerializer.startTag("", "Number");
                xmlSerializer.attribute("", "number", info.getNumber());
                xmlSerializer.endTag("", "Number");
                xmlSerializer.startTag("", "Nick_name");
                xmlSerializer.attribute("", "nick_name", info.getNick_name());
                xmlSerializer.endTag("", "Nick_name");
                xmlSerializer.startTag("", "Head_url");
                xmlSerializer.attribute("", "head_url", info.getHead_url());
                xmlSerializer.endTag("", "Head_url");
                xmlSerializer.startTag("", "Signature");
                xmlSerializer.attribute("", "signature", info.getSignature());
                xmlSerializer.endTag("", "Signature");
                xmlSerializer.startTag("", "Stamp");
                xmlSerializer.attribute("", "stamp", info.getStamp());
                xmlSerializer.endTag("", "Stamp");
                xmlSerializer.endTag("", "userInfo");
            }
            xmlSerializer.endTag("", "UserShow");
            xmlSerializer.endDocument();
            String result=stringWriter.toString();
            Log.d("userInfo",result);
            //将数据写入userInfo.xml
            FileOutputStream outputStream=context.openFileOutput("userInfo.xml",Context.MODE_PRIVATE);
            outputStream.write(result.getBytes());
            outputStream.close();
        } catch (IllegalArgumentException e){
            Log.d(TAG, "useInfo xml save error:" + e.toString());
            e.printStackTrace();
            return;
        } catch (IllegalStateException e){
            Log.d(TAG, "useInfo xml save error:" + e.toString());
            return;
        } catch (IOException e) {
            Log.d(TAG, "useInfo xml save error:" + e.toString());
            return;
        }
    }

    /**
     * open user info
     * Created by LMZ on 7/13/15
     * @param context
     * @return list & null
     */
    @Override
    public ArrayList<BaseObject> Open(Context context) {
        ArrayList<BaseObject> list=new ArrayList<BaseObject>();
        UserInfo userInfo;
        DocumentBuilder builder;
        DocumentBuilderFactory factory = null;
        Document document = null;
        factory = DocumentBuilderFactory.newInstance();
        try{
            //将userInfo.xml写入InputStream流
            InputStream inputStream = context.openFileInput("userInfo.xml");
            builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
            Element root = document.getDocumentElement();
            Element user = (Element)root.getElementsByTagName("UsersShow").item(0);
            NodeList users = user.getElementsByTagName("userInfo");
            for (int i = 0; i < users.getLength(); i++){
                Element info = (Element)users.item(i);
                userInfo = new UserInfo();
                Element numberElement = (Element)info.getElementsByTagName("Number").item(0);
                userInfo.setNumber(numberElement.getAttribute("number"));
                Element nick_nameElement = (Element)info.getElementsByTagName("Nick_name").item(0);
                userInfo.setNick_name(nick_nameElement.getAttribute("nick_name"));
                Element head_urlElement = (Element)info.getElementsByTagName("Head_url").item(0);
                userInfo.setHead_url(head_urlElement.getAttribute("head_url"));
                Element signatureElement = (Element)info.getElementsByTagName("Signature").item(0);
                userInfo.setSignature(signatureElement.getAttribute("signature"));
                Element stampElement = (Element)info.getElementsByTagName("Stamp").item(0);
                userInfo.setStamp(stampElement.getAttribute("stamp"));
                list.add(userInfo);
            }
            inputStream.close();
            return list;
        } catch (FileNotFoundException e) {
            Log.d(TAG,"Get file error:"+e.toString());
            return null;
        } catch (ParserConfigurationException e) {
            Log.d(TAG, "Analysis error:" + e.toString());
            return null;
        } catch (SAXException e) {
            Log.d(TAG, "Analysis SAX error:" + e.toString());
            return null;
        } catch (IOException e) {
            Log.d(TAG, "IO output error:" + e.toString());
            return null;
        }
    }
}
