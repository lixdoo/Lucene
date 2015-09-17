package test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class FullTextSearch {
	public static void main(String args[]) throws Exception {
		Document doc = null;
		IndexWriter indexWriter = createIndexWtiter();
		File fileFloder = new File("C:\\Word");
		File[] files = fileFloder.listFiles();
		for(int i=0; i<files.length; i++) {
			doc = new Document();
			/*Reader textReader = new FileReader(files[i]);
			FieldType type = new FieldType();*/
			//doc.add(new Field("content", textReader,type));
			//doc.add(new Field("content", FullTextSearch.getContent(files[i]) ,TextField.TYPE_STORED));
			//FileInputStream fis = new FileInputStream(files[i]);
			//System.out.println(new BufferedReader(new InputStreamReader(new FileInputStream(files[i]),"UTF-16")).readLine());
			//doc.add(new TextField("content", new BufferedReader(new InputStreamReader(fis))));
			//System.out.println(files[i].getName());
			doc.add(new Field("content", word(files[i]),TextField.TYPE_NOT_STORED));
			//doc.add(new Field("content", readWord(files[i]),TextField.TYPE_NOT_STORED));
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		indexWriter.close();
		fullTextRetrieval();
	}
	
	public static IndexWriter createIndexWtiter() throws IOException {
		File indexFloder = new File("C:\\Temp");
		if (!indexFloder.isDirectory())indexFloder.mkdir();
		Directory directory = FSDirectory.open(indexFloder);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,new StandardAnalyzer(Version.LUCENE_47));
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		return new IndexWriter(directory, config);
	}
	
	public static void fullTextRetrieval() throws IOException, ParseException {   
		Directory directory = FSDirectory.open(new File("C:\\Temp"));
		DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    QueryParser parser = new QueryParser(Version.LUCENE_47, "content", new StandardAnalyzer(Version.LUCENE_47));
	    Query query = parser.parse("Meeting");
	 	ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
	 	System.out.println(hits.length);
	 	for (int i = 0; i < hits.length; i++) {
	 		System.out.println(isearcher.doc(hits[i].doc).get("content"));
	 	}
	 	ireader.close();
	    directory.close();
	}
	
	 public static String getContent(File file) throws Exception{    
	        FileInputStream fis=new FileInputStream(file);  
	        InputStreamReader isr=new InputStreamReader(fis,"gbk");  
	        BufferedReader br=new BufferedReader(isr);  
	        StringBuffer sb=new StringBuffer();  
	        String line=br.readLine();  
	        while(line!=null){  
	            sb.append(line+"\n");  
	            line=null;  
	        }  
	        return sb.toString();  
	    }  
	 
	 public static BufferedReader readWord(File file) throws FileNotFoundException, IOException { 
		HWPFDocument doc = new HWPFDocument(new FileInputStream(file));
		StringBuffer content = new StringBuffer("");// 文档内容
		Range range = doc.getRange();
		int paragraphCount = range.numParagraphs();
		  for (int i = 0; i < paragraphCount; i++) {// 遍历段落读取数据
              Paragraph pp = range.getParagraph(i);
              content.append(pp.text());
          }
		//System.out.println(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(doc.getDataStream()),"UTF-8")).readLine());
		//System.out.println(new String(doc.getDataStream(),"UTF-8"));
		//System.out.println(doc.getDataStream().length);
		//System.out.println(content.toString().trim());
		//System.out.println(doc.getText().toString());
		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(doc.getDataStream()),"UTF-8"));
		//return content.toString().trim();
	 }
	 
	 public static Reader word(File file) throws FileNotFoundException, IOException {
		 HWPFDocument doc = new HWPFDocument(new FileInputStream(file));
		//System.out.println(new String(doc.getDataStream(),"ISO-8859-1"));
		//System.out.println(javax.xml.bind.DatatypeConverter.printBase64Binary(doc.getDataStream()));
		//System.out.println(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(doc.getDataStream()))).readLine());
		 return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(doc.getDataStream()),"UTF-8"));
	 }
	 
	 public static String getFileContent(File file) {  
	        FileReader fr = null;  
	        BufferedReader br = null;  
	        String content = "";  
	        try {  
	            fr = new FileReader(file);  
	            br = new BufferedReader(fr);  
	            StringBuffer sb = new StringBuffer();  
	            String line = br.readLine();  
	            while(null != line){  
	                sb.append(line);  
	                line = br.readLine();  
	            }  
	            content = sb.toString();  
	        }catch(Exception e) {  
	            e.printStackTrace();  
	        }finally {  
	            try {  
	                if(fr != null)  
	                    fr.close();  
	                if(br != null)  
	                    br.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return content;  
	    }  
	   
}
