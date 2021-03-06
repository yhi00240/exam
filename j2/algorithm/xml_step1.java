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
 * 문서에 나타난 단어  개수 10개 이하 + 보물의 개수 0인 것들 original 문서에서 제외하기
 * 문서에 나타난 단어  개수 10개 이하 + 보물의 개수가 한개라도 나타난 것은 따로 텍스트 문서로 만들고 original 문서에서는 제외하기
 * (단어 사전 범위가 많이 줄어들었음. 예를 들어 "ㅎ힣ㅎ힣 "이런 단어는 실제로 문서에서 1번밖에 안나옴. -> 사용자가 이상하게 친 단어들)
 * 왜 10개 이하인 것들을 제외했느냐?
 * "ㅎ힣ㅎ힣 " + "안녕"
 * "안녕"이라는 단어는 문서 출현도가 2043517임.
 * 나중에 좁아진 단어 사전 내에서 조합할 경우, "ㅎ힣ㅎ힣"은 필요없음.
 * 왜냐? 검색 엔진에서 최대로 보여주는 문서개수는 10개임. 10개면, 다른 문서에는 포함되지 않았다는 소리임. 즉 삭제해도 무방함
 * 또한, 문서에 나타난 단어 개수가 10개 이하인 경우 + 보물의 개수가 1개라도 나타나면
 * 그것들만 모아서 따로 text문서를 만든다. 이것도 마찬가지로, 검색 엔진에서 최대로 보여주는 문서개수는 10개임.
 * "안녕"이 조합할경우, original 문서에서는 제외하고 다른 단어와 조합하는 것이 훨씬 더 효과적임.
 * 따로 텍스트 문서로 만든 것은 form data를 보내면, 단어 사전 범위가 많이 줄어들고, 누적 보물수가 늘어난다.
 * ( 단어 사전에 필요없는(문서에 나타난 단어 개수 10개 이하+보물의 개수 0개인것들) 단어가 상당수임 )
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
            
            // 보물의 NodeList 가져오기 : item 아래에 있는 모든 treasure을 선택
            NodeList treasures = (NodeList)xpath.evaluate("//item/treasure", doc, XPathConstants.NODESET);
            for( int idx=0; idx<treasures.getLength(); idx++ ){
                treasure_total = treasure_total+ Integer.parseInt(treasures.item(idx).getTextContent());
            }
     
            System.out.println(treasure_total);
           
            if(total<=10)
            {
            	if(treasure_total==0) // 만약 개수가 10개이하고, 보물이 하나도 없으면! 단어장에서 없애기
            	{	
            		//단어장에서 없앤다는 것은 아무것도 하지 않음.
            	}
            	else // bruteForce를 하지 않는 다고 가정했을때(보물이 겹치지 않을때), 보물이 한개이상 있으면 쿼리날려서 보물 총 점수얻고, 단어장에서 없애기(다음 단계부터는 필요없음)
            	{
            		//단어장에서 없앤다는 것은 아무것도 하지 않음.
            		//쿼리날려서 보물 총점 쌓기
            	}
            }
            else if(total<=1000) // 나타나는 문서개수가 11~1000도, 문서출현도가 몇십만, 몇백만인 단어에 비해서 상대적으로 꽤 작은편임. 
            					 // 하지만 실시간으로 단어사전의 조합생성을 통해 사용자에게 제공해줘야하기위해 단어사전에 포함하는 것이 효율적이지 않음.
            					 // if(total<=10)에 같이 포함해서 넣는다. 
            {
            }
            else // 나머지들은 단어 사전에 넣는다.
            {
            	writer.write(tmpSubURL);
            	writer.append("\n");
            }
            
            //////// 만약 개수가 10개이하고, 보물이 하나라도 있으면! 질의 서비스로 날리기 
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
