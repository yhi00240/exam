package algorithm;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.*;
 

///////

/*
 * ¹®¼­¿¡ ³ªÅ¸³­ ´Ü¾î  °³¼ö 10°³ ÀÌÇÏ + º¸¹°ÀÇ °³¼ö 0ÀÎ °Íµé original ¹®¼­¿¡¼­ Á¦¿ÜÇÏ±â
 * ¹®¼­¿¡ ³ªÅ¸³­ ´Ü¾î  °³¼ö 10°³ ÀÌÇÏ + º¸¹°ÀÇ °³¼ö°¡ ÇÑ°³¶óµµ ³ªÅ¸³­ °ÍÀº µû·Î ÅØ½ºÆ® ¹®¼­·Î ¸¸µé°í original ¹®¼­¿¡¼­´Â Á¦¿ÜÇÏ±â
 * (´Ü¾î »çÀü ¹üÀ§°¡ ¸¹ÀÌ ÁÙ¾îµé¾úÀ½. ¿¹¸¦ µé¾î "¤¾ÆR¤¾ÆR "ÀÌ·± ´Ü¾î´Â ½ÇÁ¦·Î ¹®¼­¿¡¼­ 1¹ø¹Û¿¡ ¾È³ª¿È. -> »ç¿ëÀÚ°¡ ÀÌ»óÇÏ°Ô Ä£ ´Ü¾îµé)
 * ¿Ö 10°³ ÀÌÇÏÀÎ °ÍµéÀ» Á¦¿ÜÇß´À³Ä?
 * "¤¾ÆR¤¾ÆR " + "¾È³ç"
 * "¾È³ç"ÀÌ¶ó´Â ´Ü¾î´Â ¹®¼­ ÃâÇöµµ°¡ 2043517ÀÓ.
 * ³ªÁß¿¡ Á¼¾ÆÁø ´Ü¾î »çÀü ³»¿¡¼­ Á¶ÇÕÇÒ °æ¿ì, "¤¾ÆR¤¾ÆR"Àº ÇÊ¿ä¾øÀ½.
 * ¿Ö³Ä? °Ë»ö ¿£Áø¿¡¼­ ÃÖ´ë·Î º¸¿©ÁÖ´Â ¹®¼­°³¼ö´Â 10°³ÀÓ. 10°³¸é, ´Ù¸¥ ¹®¼­¿¡´Â Æ÷ÇÔµÇÁö ¾Ê¾Ò´Ù´Â ¼Ò¸®ÀÓ. Áï »èÁ¦ÇØµµ ¹«¹æÇÔ
 * ¶ÇÇÑ, ¹®¼­¿¡ ³ªÅ¸³­ ´Ü¾î °³¼ö°¡ 10°³ ÀÌÇÏÀÎ °æ¿ì + º¸¹°ÀÇ °³¼ö°¡ 1°³¶óµµ ³ªÅ¸³ª¸é
 * ±×°Íµé¸¸ ¸ð¾Æ¼­ µû·Î text¹®¼­¸¦ ¸¸µç´Ù. ÀÌ°Íµµ ¸¶Âù°¡Áö·Î, °Ë»ö ¿£Áø¿¡¼­ ÃÖ´ë·Î º¸¿©ÁÖ´Â ¹®¼­°³¼ö´Â 10°³ÀÓ.
 * "¾È³ç"ÀÌ Á¶ÇÕÇÒ°æ¿ì, original ¹®¼­¿¡¼­´Â Á¦¿ÜÇÏ°í ´Ù¸¥ ´Ü¾î¿Í Á¶ÇÕÇÏ´Â °ÍÀÌ ÈÎ¾À ´õ È¿°úÀûÀÓ.
 * µû·Î ÅØ½ºÆ® ¹®¼­·Î ¸¸µç °ÍÀº form data¸¦ º¸³»¸é, ´Ü¾î »çÀü ¹üÀ§°¡ ¸¹ÀÌ ÁÙ¾îµé°í, ´©Àû º¸¹°¼ö°¡ ´Ã¾î³­´Ù.
 * ( ´Ü¾î »çÀü¿¡ ÇÊ¿ä¾ø´Â(¹®¼­¿¡ ³ªÅ¸³­ ´Ü¾î °³¼ö 10°³ ÀÌÇÏ+º¸¹°ÀÇ °³¼ö 0°³ÀÎ°Íµé) ´Ü¾î°¡ »ó´ç¼öÀÓ )
 *  */

public class xml_step1 {
 
    public static void main(String[] args) {
 
        try{
 
            new xml_step1().start();
 
        }catch (Exception e){
            e.printStackTrace();
        }
 
    }
 
    private void start() throws Exception{
    	
    	String subURL = null;
    	
    	String inputName="C:/Users/jihyun/Desktop/input/termList50.txt";
    	File inputFile = new File(inputName);
    	
    	FileReader fileReader = new FileReader(inputFile);
    	BufferedReader reader = new BufferedReader(fileReader);
    	
    	String outputName="C:/Users/jihyun/Desktop/input/output1.txt";
    	File outputFile=new File(outputName);
    	
    	FileWriter fileWriter = new FileWriter(outputFile);
    	BufferedWriter writer = new BufferedWriter(fileWriter);
      	
    	while( (subURL = reader.readLine()) != null )
    	{
    		String tmpSubURL="";
    		tmpSubURL+=subURL;
    		XPath xpath = XPathFactory.newInstance().newXPath();
    		
    		String curUrl = "http://treasure.navercorp.com:8080/nx.search?query=";
    		subURL = URLEncoder.encode(subURL,"UTF-8");
        	curUrl = curUrl + subURL;
        	
            URL url = new URL("http://treasure.navercorp.com:8080/nx.search?query="+subURL);
            URLConnection connection = url.openConnection();
     
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(curUrl);
            NodeList descNodes = doc.getElementsByTagName("section");
     
            int total=0;
            int treasure_total=0;
            System.out.print(tmpSubURL+" ");
            
            NodeList totalNode = (NodeList)xpath.evaluate("//section/total", doc, XPathConstants.NODESET);
            for( int idx=0; idx<totalNode.getLength(); idx++ ){
                 total = Integer.parseInt(totalNode.item(idx).getTextContent());
            }
            System.out.print(total+" ");
            
            // º¸¹°ÀÇ NodeList °¡Á®¿À±â : item ¾Æ·¡¿¡ ÀÖ´Â ¸ðµç treasureÀ» ¼±ÅÃ
            NodeList treasures = (NodeList)xpath.evaluate("//item/treasure", doc, XPathConstants.NODESET);
            for( int idx=0; idx<treasures.getLength(); idx++ ){
                treasure_total = treasure_total+ Integer.parseInt(treasures.item(idx).getTextContent());
            }
     
            System.out.println(treasure_total);
           
            if(total<=10)
            {
            	if(treasure_total==0) // ¸¸¾à °³¼ö°¡ 10°³ÀÌÇÏ°í, º¸¹°ÀÌ ÇÏ³ªµµ ¾øÀ¸¸é! ´Ü¾îÀå¿¡¼­ ¾ø¾Ö±â
            	{	
            		//´Ü¾îÀå¿¡¼­ ¾ø¾Ø´Ù´Â °ÍÀº ¾Æ¹«°Íµµ ÇÏÁö ¾ÊÀ½.
            	}
            	else // bruteForce¸¦ ÇÏÁö ¾Ê´Â ´Ù°í °¡Á¤ÇßÀ»¶§(º¸¹°ÀÌ °ãÄ¡Áö ¾ÊÀ»¶§), º¸¹°ÀÌ ÇÑ°³ÀÌ»ó ÀÖÀ¸¸é Äõ¸®³¯·Á¼­ º¸¹° ÃÑ Á¡¼ö¾ò°í, ´Ü¾îÀå¿¡¼­ ¾ø¾Ö±â(´ÙÀ½ ´Ü°èºÎÅÍ´Â ÇÊ¿ä¾øÀ½)
            	{
            		//´Ü¾îÀå¿¡¼­ ¾ø¾Ø´Ù´Â °ÍÀº ¾Æ¹«°Íµµ ÇÏÁö ¾ÊÀ½.
            		//Äõ¸®³¯·Á¼­ º¸¹° ÃÑÁ¡ ½×±â
            	}
            }
            else if(total<=1000) // ³ªÅ¸³ª´Â ¹®¼­°³¼ö°¡ 11~1000µµ, ¹®¼­ÃâÇöµµ°¡ ¸î½Ê¸¸, ¸î¹é¸¸ÀÎ ´Ü¾î¿¡ ºñÇØ¼­ »ó´ëÀûÀ¸·Î ²Ï ÀÛÀºÆíÀÓ. 
            					 // ÇÏÁö¸¸ ½Ç½Ã°£À¸·Î ´Ü¾î»çÀüÀÇ Á¶ÇÕ»ý¼ºÀ» ÅëÇØ »ç¿ëÀÚ¿¡°Ô Á¦°øÇØÁà¾ßÇÏ±âÀ§ÇØ ´Ü¾î»çÀü¿¡ Æ÷ÇÔÇÏ´Â °ÍÀÌ È¿À²ÀûÀÌÁö ¾ÊÀ½.
            					 // if(total<=10)¿¡ °°ÀÌ Æ÷ÇÔÇØ¼­ ³Ö´Â´Ù. 
            {
            }
            else // ³ª¸ÓÁöµéÀº ´Ü¾î »çÀü¿¡ ³Ö´Â´Ù.
            {
            	writer.write(tmpSubURL);
            	writer.append("\n");
            }
            
            //////// ¸¸¾à °³¼ö°¡ 10°³ÀÌÇÏ°í, º¸¹°ÀÌ ÇÏ³ª¶óµµ ÀÖÀ¸¸é! ÁúÀÇ ¼­ºñ½º·Î ³¯¸®±â 
    	}
    	writer.close();
    }
 
    private Document parseXML(InputStream stream) throws Exception{
 
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
 
        try{
 
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
 
            doc = objDocumentBuilder.parse(stream);
 
        }catch(Exception ex){
            throw ex;
        }       
 
        return doc;
    }
 
}
