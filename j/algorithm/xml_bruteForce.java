package algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.net.*;

/*
 * txt 파일 열어서 단어 1개씩 받아서 질의하는 bruteForce용
 * */
public class xml_bruteForce {

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
		
    	String inputName="C:/Users/jihyun/Desktop/input/termList50.txt";
    	File inputFile = new File(inputName);
    	
    	FileReader fileReader = new FileReader(inputFile);
    	BufferedReader reader = new BufferedReader(fileReader);
    	
    	String word = null;
    	
    	while( (word = reader.readLine()) != null )
    	{
    		XPath xpath = XPathFactory.newInstance().newXPath();
    		String tmpWord="";
    		tmpWord+=word;
    		String curUrl = "http://treasure.navercorp.com:8080/nx.search?query=";
    		word = URLEncoder.encode(word,"UTF-8");
        	curUrl = curUrl + word;
        	
            URL url = new URL(curUrl);
            URLConnection connection = url.openConnection();
     
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(curUrl);
            
            ////////////////////////// 출력용
            int treasure_total=0;
            // 보물의 NodeList 가져오기 : item 아래에 있는 모든 treasure을 선택
            NodeList treasures = (NodeList)xpath.evaluate("//item/treasure", doc, XPathConstants.NODESET);
            for( int idx=0; idx<treasures.getLength(); idx++ ){
                treasure_total = treasure_total+ Integer.parseInt(treasures.item(idx).getTextContent());
            }
     
    		 try {
    	         String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode("ji", "UTF-8");
    	         data += "&" + URLEncoder.encode("answer", "UTF-8") + "=" + word;
    	     
    	         // Send data
    	         URL Sendurl = new URL("http://treasure.navercorp.com/campus_hackday/evaluation/");
    	         URLConnection conn = Sendurl.openConnection();
    	         conn.setDoOutput(true);
    	         HttpURLConnection hurlc = (HttpURLConnection)connection;
    			 hurlc.setRequestMethod("POST");
    			 hurlc.setDoOutput(true);
    			 hurlc.setDoInput(true);
    			 hurlc.setUseCaches(false);
    			 hurlc.setDefaultUseCaches(false);
    				
    			 PrintWriter out = new PrintWriter(hurlc.getOutputStream());
    			 //out.println(query);
    			 out.flush();
    			 out.close();
    	        
    	     }
    	     catch (Exception e) {
    	     }
    		 
    		 //System.out.println(tmpWord+" "+treasure_total);
    	}
	}
}


