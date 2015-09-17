package test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import cn.lucnen.utils.FileUtils;
import cn.lucnen.utils.IndexUtils;
import cn.lucnen.utils.SVNUtils;

public class SVN {
	
	public static void main(String args[]) throws Exception {
		new FullTextSearch();
		IndexWriter indexWriter = FullTextSearch.createIndexWtiter();
		Document doc = new Document();
		doc.add(new Field("content", getFile(initSVN(),""), TextField.TYPE_NOT_STORED));
		indexWriter.addDocument(doc);
		indexWriter.commit();
		indexWriter.close();
		search();
	}
	
	public static SVNRepository initSVN() {
        String url = "svn://10.64.44.100/Repository";
        String name = "gaotianyu";
        String password = "123456"; 
        setupLibrary();
        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            System.exit(1);
        }
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);
        try {
            SVNNodeKind nodeKind = repository.checkPath("", -1);
            if (nodeKind == SVNNodeKind.NONE) {
                System.exit(1);
            } else if (nodeKind == SVNNodeKind.FILE) {
                System.exit(1);
            }
        } catch (SVNException svne) {
            System.exit(1);
        }
		return repository;  
    }
	
	private static void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }
	
	public static Reader getFile(SVNRepository repository, String path) throws SVNException, IOException, XmlException, OpenXML4JException {
		String filePath="SVNKit.doc";
		SVNProperties fileProperties = new SVNProperties();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
        repository.getFile(filePath, -1, fileProperties, baos);
       // String[] a = FileUtils.getFileKeyWord(FileUtils.convertWord(new ByteArrayInputStream(baos.toByteArray()), filePath));
        InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()),"UTF-8");
        BufferedReader b = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
        System.out.println(b.readLine());
		return in;
	}
	
	public static void search() throws IOException, ParseException {
		Directory directory = FSDirectory.open(new File("C:\\Temp"));
		DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    QueryParser parser = new QueryParser(Version.LUCENE_47, "content", new StandardAnalyzer(Version.LUCENE_47));
	    Query query = parser.parse("java");
	 	ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
	 	System.out.println(hits.length);
	 	for (int i = 0; i < hits.length; i++) {
	 		System.out.println(isearcher.doc(hits[i].doc).get("content"));
	 	}
	 	ireader.close();
	    directory.close();
	}
	
}  
