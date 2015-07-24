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
import bean.Movie;

/**
 * Created by zhuchao on 7/12/15.
 * This class is to save and open the configure file of movies that have been downloaded.
 * You will implement two functions below.
 */
public class SaveAndOpenMovies implements ParseXml {
    private static String TAG="MovieProcessXml";
    /**
     * save movie info
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
            xmlSerializer.startTag("","MovieShow");
            for(int i=0;i<objects.size();i++){
                //强制转换：父类型转换为子类型
                Movie movie=(Movie)objects.get(i);
                xmlSerializer.startTag("", "movie");
                xmlSerializer.startTag("", "Description");
                xmlSerializer.attribute("", "description", movie.getDescription());
                xmlSerializer.endTag("", "Description");
                xmlSerializer.startTag("", "FileSize");
                xmlSerializer.attribute("", "fileSize", String.valueOf(movie.getFileSize()));
                xmlSerializer.endTag("", "FileSize");
                xmlSerializer.startTag("", "Time");
                xmlSerializer.attribute("", "time", movie.getTime());
                xmlSerializer.endTag("", "Time");
                xmlSerializer.startTag("", "ImageUrl");
                xmlSerializer.attribute("", "imageUrl", movie.getImageUrl());
                xmlSerializer.endTag("", "ImageUrl");
                xmlSerializer.startTag("", "FileUrl");
                xmlSerializer.attribute("", "fileUrl", movie.getFileUrl());
                xmlSerializer.endTag("", "FileUrl");
                xmlSerializer.startTag("", "MovieId");
                xmlSerializer.attribute("", "movieId", movie.getMovieId());
                xmlSerializer.endTag("","MovieId");
                xmlSerializer.startTag("","PlayUrl");
                xmlSerializer.attribute("","playUrl",movie.getPlayUrl());
                xmlSerializer.endTag("","PlayUrl");
                xmlSerializer.startTag("","MovieName");
                xmlSerializer.attribute("","movieName",movie.getMovieName());
                xmlSerializer.endTag("","MovieName");
                xmlSerializer.startTag("","ViewNumber");
                xmlSerializer.attribute("","viewNumber",movie.getViewNumber());
                xmlSerializer.endTag("","ViewNumber");
                xmlSerializer.startTag("","CommentNumber");
                xmlSerializer.attribute("","commentNumber",movie.getCommentNumber());
                xmlSerializer.endTag("","CommentNumber");
                xmlSerializer.endTag("", "movie");
            }
            xmlSerializer.endTag("", "MovieShow");
            xmlSerializer.endDocument();
            String result=stringWriter.toString();
            Log.d("movie",result);
            //将数据写入movie.xml
            FileOutputStream outputStream=context.openFileOutput("movie.xml",Context.MODE_PRIVATE);
            outputStream.write(result.getBytes());
            outputStream.close();
        } catch (IllegalArgumentException e){
            Log.d(TAG, "movie xml save error:" + e.toString());
            e.printStackTrace();
            return;
        } catch (IllegalStateException e){
            Log.d(TAG, "movie xml save error:" + e.toString());
            return;
        } catch (IOException e) {
            Log.d(TAG, "movie xml save error:" + e.toString());
            return;
        }
    }

    /**
     * open movie info
     * Created by LMZ on 7/13/15
     * @param context
     * @return
     */
        @Override
    public ArrayList<BaseObject> Open(Context context) {
            ArrayList<BaseObject> list = new ArrayList<BaseObject>();
            Movie movie;
            DocumentBuilder builder;
            DocumentBuilderFactory factory = null;
            Document document = null;
            factory = DocumentBuilderFactory.newInstance();
            try {
                //将movie.xml写入InputStream流
                InputStream inputStream = context.openFileInput("movie.xml");
                builder = factory.newDocumentBuilder();
                document = builder.parse(inputStream);
                Element root = document.getDocumentElement();
//                Element movieInfo = (Element) root.getElementsByTagName("MovieShow").item(0);
                NodeList movies = root.getElementsByTagName("movie");
                for (int i = 0; i < movies.getLength(); i++) {
                    Element info = (Element) movies.item(i);
                    movie = new Movie();
                    Element descriptionElement = (Element) info.getElementsByTagName("Description").item(0);
                    movie.setDescription(descriptionElement.getAttribute("description"));
                    Element fileSizeElement = (Element) info.getElementsByTagName("FileSize").item(0);
                    movie.setFileSize(Long.parseLong(fileSizeElement.getAttribute("fileSize")));
                    Element timeElement = (Element) info.getElementsByTagName("Time").item(0);
                    movie.setTime(timeElement.getAttribute("time"));
                    Element imageUrlElement = (Element) info.getElementsByTagName("ImageUrl").item(0);
                    movie.setImageUrl(imageUrlElement.getAttribute("imageUrl"));
                    Element fileUrlElement = (Element) info.getElementsByTagName("FileUrl").item(0);
                    movie.setFileUrl(fileUrlElement.getAttribute("fileUrl"));
                    Element movieIdElement=(Element)info.getElementsByTagName("MovieId").item(0);
                    movie.setMovieId(movieIdElement.getAttribute("movieId"));
                    Element playUrlElement=(Element)info.getElementsByTagName("PlayUrl").item(0);
                    movie.setPlayUrl(playUrlElement.getAttribute("playUrl"));
                    Element movieNameElement=(Element)info.getElementsByTagName("MovieName").item(0);
                    movie.setMovieName(movieNameElement.getAttribute("movieName"));
                    Element viewNumberElement=(Element)info.getElementsByTagName("ViewNumber").item(0);
                    movie.setViewNumber(viewNumberElement.getAttribute("viewNumber"));
                    Element commentNumberElement=(Element)info.getElementsByTagName("CommentNumber").item(0);
                    movie.setCommentNumber(commentNumberElement.getAttribute("commentNumber"));
                    list.add(movie);
                }
                inputStream.close();
                return list;
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Get file error:" + e.toString());
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

    @Override
    public void Delete(Context context) {

    }
}