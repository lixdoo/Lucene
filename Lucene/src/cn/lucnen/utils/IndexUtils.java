package cn.lucnen.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;

public class IndexUtils {
	public void manualIndex() throws IOException, XmlException, OpenXML4JException {
		IndexWriter indexWriter = createIndexWtiter();
		try {
			SVNUtils.getFiles(SVNUtils.initSVNRepository(),"", indexWriter);
		} catch (SVNException e) {
			e.printStackTrace();
		} finally {
			indexWriter.commit();
			indexWriter.close();
		}
	}
	
	public void cycleIndex() throws IOException {
		
	}
	
	public void monitorIndex() {  
		
	}
	
	public static void convertFiles(SVNDirEntry entry, String path, ByteArrayOutputStream content, String fileType, IndexWriter indexWriter) throws FileNotFoundException, IOException, XmlException, OpenXML4JException {
		String fileContent = null;
		if(fileType.equals("doc") || fileType.equals("docx")) {
			fileContent = FileUtils.convertWord(new ByteArrayInputStream(content.toByteArray()), fileType);
        } else if(fileType.equals("xls") || fileType.equals("xlsx")) {
        	fileContent = FileUtils.convertXLS(new ByteArrayInputStream(content.toByteArray()));
        } else if(fileType.equals("ppt") || fileType.equals("ppts")) {
        	fileContent = FileUtils.convertPPT(new ByteArrayInputStream(content.toByteArray()), fileType);
        } else if(fileType.equals("pdf")) {
        	fileContent = FileUtils.convertPDF(new ByteArrayInputStream(content.toByteArray()));
        } else {
        	fileContent = "";
        }
		indexWriter.addDocument(IndexUtils.addField(entry, path, fileType, fileContent));
	}
	
	public static Document addField(SVNDirEntry entry, String path, String fileType, String content) {
		Document document = new Document();
		if(!content.equals("") || content != null) document.add(new Field("content", content, TextField.TYPE_NOT_STORED));
		document.add(new Field("name", entry.getName(), TextField.TYPE_STORED));
		document.add(new Field("author", entry.getAuthor(), TextField.TYPE_STORED));
		document.add(new Field("path", path + "/" + entry.getRelativePath(), TextField.TYPE_STORED));
		document.add(new Field("type", fileType, TextField.TYPE_STORED));
		document.add(new Field("date",  new SimpleDateFormat("yyyy-MM-dd").format(entry.getDate()), TextField.TYPE_STORED));
		document.add(new Field("size", Long.toString(entry.getSize()), TextField.TYPE_STORED));
		document.add(new Field("version", Long.toString(entry.getRevision()), TextField.TYPE_STORED));
		return document;
	}
	
	private IndexWriter createIndexWtiter() throws IOException {
		File indexFloder = new File("E:\\Lucene\\index");
		if (!indexFloder.isDirectory())indexFloder.mkdir();
		Directory directory = FSDirectory.open(indexFloder);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,new SVNAnalyzer(Version.LUCENE_47));
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		return new IndexWriter(directory, config);
	}
}

