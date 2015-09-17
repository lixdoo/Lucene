package cn.lucene.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import cn.lucnen.utils.BeanUtils;
import cn.lucnen.utils.SVNAnalyzer;

public class FileInformation {
	private String name;
	private String type;
	private String author;
	private String path;
	private String date;
	private String size;
	private String version;
	private String message;
	private String contents;
	private String[] pathArray;
	private String[] versionArray;

	public FileInformation(HttpServletRequest request)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {
		BeanUtils.populate(this, request.getParameterMap());
	}

	public FileInformation() {

	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public String[] getPathArray() {
		return pathArray;
	}

	public void setPathArray(String[] pathArray) {
		this.pathArray = pathArray;
	}

	public String[] getVersionArray() {
		return versionArray;
	}

	public void setVersionArray(String[] versionArray) {
		this.versionArray = versionArray;
	}
	
	public List<FileInformation> fullTextRetrieval() throws IOException,
			ParseException {
		List<FileInformation> files = new ArrayList<FileInformation>();
		FileInformation fileInformation = null;
		Directory directory = FSDirectory.open(new File("E:\\Lucene\\index"));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "content",
				new SVNAnalyzer(Version.LUCENE_47));
		Query query = parser.parse(getContents());
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			fileInformation = new FileInformation();
			fileInformation.setName(isearcher.doc(hits[i].doc).get("name"));
			fileInformation.setType(isearcher.doc(hits[i].doc).get("type"));
			fileInformation.setAuthor(isearcher.doc(hits[i].doc).get("author"));
			fileInformation.setPath(isearcher.doc(hits[i].doc).get("path"));
			fileInformation.setSize(isearcher.doc(hits[i].doc).get("size"));
			fileInformation.setDate(isearcher.doc(hits[i].doc).get("date"));
			fileInformation.setVersion(isearcher.doc(hits[i].doc).get("version"));
			files.add(fileInformation);
		}
		ireader.close();
		directory.close();
		return files;
	}

	public List<FileInformation> nameSearch() throws IOException,
			ParseException {
		List<FileInformation> files = new ArrayList<FileInformation>();
		FileInformation fileInformation = null;
		Directory directory = FSDirectory.open(new File("E:\\Lucene\\index"));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "name",
				new SVNAnalyzer(Version.LUCENE_47));
		Query query = parser.parse(getName());
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			fileInformation = new FileInformation();
			fileInformation.setName(isearcher.doc(hits[i].doc).get("name"));
			fileInformation.setType(isearcher.doc(hits[i].doc).get("type"));
			fileInformation.setAuthor(isearcher.doc(hits[i].doc).get("author"));
			fileInformation.setPath(isearcher.doc(hits[i].doc).get("path"));
			fileInformation.setSize(isearcher.doc(hits[i].doc).get("size"));
			fileInformation.setDate(isearcher.doc(hits[i].doc).get("date"));
			fileInformation.setVersion(isearcher.doc(hits[i].doc).get("version"));
			files.add(fileInformation);
		}
		ireader.close();
		directory.close();
		return files;
	}

	public List<FileInformation> typeSearch() throws IOException,
			ParseException {
		List<FileInformation> files = new ArrayList<FileInformation>();
		FileInformation fileInformation = null;
		Directory directory = FSDirectory.open(new File("E:\\Lucene\\index"));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "type",
				new SVNAnalyzer(Version.LUCENE_47));
		Query query = parser.parse(getType());
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			fileInformation = new FileInformation();
			fileInformation.setName(isearcher.doc(hits[i].doc).get("name"));
			fileInformation.setType(isearcher.doc(hits[i].doc).get("type"));
			fileInformation.setAuthor(isearcher.doc(hits[i].doc).get("author"));
			fileInformation.setPath(isearcher.doc(hits[i].doc).get("path"));
			fileInformation.setSize(isearcher.doc(hits[i].doc).get("size"));
			fileInformation.setDate(isearcher.doc(hits[i].doc).get("date"));
			fileInformation.setVersion(isearcher.doc(hits[i].doc).get("version"));
			files.add(fileInformation);
		}
		ireader.close();
		directory.close();
		return files;
	}

	public List<FileInformation> authorSearch() throws IOException,
			ParseException {
		List<FileInformation> files = new ArrayList<FileInformation>();
		FileInformation fileInformation = null;
		Directory directory = FSDirectory.open(new File("E:\\Lucene\\index"));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "author",
				new SVNAnalyzer(Version.LUCENE_47));
		Query query = parser.parse(getAuthor());
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			fileInformation = new FileInformation();
			fileInformation.setName(isearcher.doc(hits[i].doc).get("name"));
			fileInformation.setType(isearcher.doc(hits[i].doc).get("type"));
			fileInformation.setAuthor(isearcher.doc(hits[i].doc).get("author"));
			fileInformation.setPath(isearcher.doc(hits[i].doc).get("path"));
			fileInformation.setSize(isearcher.doc(hits[i].doc).get("size"));
			fileInformation.setDate(isearcher.doc(hits[i].doc).get("date"));
			fileInformation.setVersion(isearcher.doc(hits[i].doc).get("version"));
			files.add(fileInformation);
		}
		ireader.close();
		directory.close();
		return files;
	}

	public List<FileInformation> dateSearch() throws IOException,
			ParseException {
		List<FileInformation> files = new ArrayList<FileInformation>();
		FileInformation fileInformation = null;
		Directory directory = FSDirectory.open(new File("E:\\Lucene\\index"));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "date",
				new SVNAnalyzer(Version.LUCENE_47));
		Query query = parser.parse(getDate());
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			fileInformation = new FileInformation();
			fileInformation.setName(isearcher.doc(hits[i].doc).get("name"));
			fileInformation.setType(isearcher.doc(hits[i].doc).get("type"));
			fileInformation.setAuthor(isearcher.doc(hits[i].doc).get("author"));
			fileInformation.setPath(isearcher.doc(hits[i].doc).get("path"));
			fileInformation.setSize(isearcher.doc(hits[i].doc).get("size"));
			fileInformation.setDate(isearcher.doc(hits[i].doc).get("date"));
			fileInformation.setVersion(isearcher.doc(hits[i].doc).get("version"));
			files.add(fileInformation);
		}
		ireader.close();
		directory.close();
		return files;
	}

	public List<FileInformation> versionSearch() throws IOException,
			ParseException {
		List<FileInformation> files = new ArrayList<FileInformation>();
		FileInformation fileInformation = null;
		Directory directory = FSDirectory.open(new File("E:\\Lucene\\index"));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "version",
				new SVNAnalyzer(Version.LUCENE_47));
		Query query = parser.parse(getName());
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			fileInformation = new FileInformation();
			fileInformation.setName(isearcher.doc(hits[i].doc).get("name"));
			fileInformation.setType(isearcher.doc(hits[i].doc).get("type"));
			fileInformation.setAuthor(isearcher.doc(hits[i].doc).get("author"));
			fileInformation.setPath(isearcher.doc(hits[i].doc).get("path"));
			fileInformation.setSize(isearcher.doc(hits[i].doc).get("size"));
			fileInformation.setDate(isearcher.doc(hits[i].doc).get("date"));
			fileInformation.setVersion(isearcher.doc(hits[i].doc).get("version"));
			files.add(fileInformation);
		}
		ireader.close();
		directory.close();
		return files;
	}

	public List<FileInformation> complexitySearch() throws IOException,
			ParseException {
		List<FileInformation> files = new ArrayList<FileInformation>();
		FileInformation fileInformation = null;
		Directory directory = FSDirectory.open(new File("E:\\Lucene\\index"));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "path",
				new SVNAnalyzer(Version.LUCENE_47));
		Query query = parser.parse(getPath());
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			fileInformation = new FileInformation();
			fileInformation.setName(isearcher.doc(hits[i].doc).get("name"));
			fileInformation.setType(isearcher.doc(hits[i].doc).get("type"));
			fileInformation.setAuthor(isearcher.doc(hits[i].doc).get("author"));
			fileInformation.setPath(isearcher.doc(hits[i].doc).get("path"));
			fileInformation.setSize(isearcher.doc(hits[i].doc).get("size"));
			fileInformation.setDate(isearcher.doc(hits[i].doc).get("date"));
			fileInformation.setVersion(isearcher.doc(hits[i].doc).get("version"));
			files.add(fileInformation);
		}
		ireader.close();
		directory.close();
		return files;
	}
}
