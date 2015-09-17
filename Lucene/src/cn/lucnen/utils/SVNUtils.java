package cn.lucnen.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.index.IndexWriter;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import cn.lucene.model.FileInformation;
import cn.lucene.model.Log;

public class SVNUtils {

	private static void setupLibrary() {
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
	}

	public static SVNRepository initSVNRepository() throws SVNException {
		String url = "svn://localhost/Repository";
		String name = "gaotianyu";
		String password = "123456";
		setupLibrary();
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL
				.parseURIEncoded(url));
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(name, password);
		repository.setAuthenticationManager(authManager);
		return repository;
	}

	public SVNClientManager initSVNClient() throws SVNException {
		setupLibrary();
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		SVNClientManager clientManager = SVNClientManager.newInstance(
				(DefaultSVNOptions) options, "gaotianyu", "123456");
		return clientManager;
	}

	public static void getFiles(SVNRepository repository, String path,
			IndexWriter indexWriter) throws SVNException, IOException,
			XmlException, OpenXML4JException {
		String fileType = "";
		SVNDirEntry entry = null;
		ByteArrayOutputStream content = null;
		Collection<?> entries = repository.getDir(path, -1, null,
				(Collection<?>) null);
		Iterator<?> iterator = entries.iterator();
		List<String> list = Arrays
				.asList("doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf",
						"txt", "html", "xml", "java", "jsp", "css", "js");
		while (iterator.hasNext()) {
			entry = (SVNDirEntry) iterator.next();
			if (entry.getKind() == SVNNodeKind.DIR) {
				getFiles(repository, (path.equals("")) ? entry.getName() : path
						+ "/" + entry.getName(), indexWriter);
			} else {
				fileType = FileUtils.checkFileType(entry.getName());
				if (!fileType.equals("")) {
					if (list.contains(fileType)) {
						content = new ByteArrayOutputStream();
						repository.getFile(
								path + "/" + entry.getRelativePath(),
								entry.getRevision(), null, content);
						IndexUtils.convertFiles(entry, path, content, fileType,
								indexWriter);
					} else {
						indexWriter.addDocument(IndexUtils.addField(entry,
								path, fileType, ""));
					}
				}
			}
		}
	}

	public void downloadFile(FileInformation fileInformation)
			throws SVNException {
		File downloadFloader = new File("E:\\SVNDownload");
		if (!downloadFloader.isDirectory())
			downloadFloader.mkdir();
		SVNUpdateClient updateClient = initSVNClient().getUpdateClient();
		updateClient.setIgnoreExternals(false);
		updateClient.doCheckout(
				SVNURL.parseURIEncoded("svn://localhost/Repository"),
				downloadFloader, SVNRevision.HEAD, SVNRevision.HEAD,
				SVNDepth.INFINITY, false);
	}
	
	public static ByteArrayOutputStream openFile(FileInformation fileInformation) throws Exception {
		SVNRepository repository = initSVNRepository();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SVNNodeKind nodeKind = repository.checkPath(fileInformation.getPath(),
				Long.parseLong(fileInformation.getVersion()));
		if (nodeKind == SVNNodeKind.NONE)
			throw new Exception("the file does not exists.");
		else if (nodeKind == SVNNodeKind.DIR)
			throw new Exception(" It is a directory.");
		else if (nodeKind == SVNNodeKind.FILE) {
			repository.getFile(fileInformation.getPath(),
					Long.parseLong(fileInformation.getVersion()), null, baos);
		}
		return baos;
	}

	public Log openLog(FileInformation fileInformation) throws SVNException {
		SVNLogClient logClient = initSVNClient().getLogClient();
		logClient.setIgnoreExternals(false);
		Log log = new Log();
		Collection<?> logEntries = initSVNRepository().log(new String[] { "" },
				null, Long.parseLong(fileInformation.getVersion()),
				Long.parseLong(fileInformation.getVersion()), true, true);
		for (Iterator<?> entries = logEntries.iterator(); entries.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			log.setAuthor(logEntry.getAuthor());
			log.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(logEntry.getDate()));
			log.setVersion(Long.toString(logEntry.getRevision()));
			log.setMessage(logEntry.getMessage());
		}
		return log;
	}

	public void fileDifference() throws SVNException, IOException {
		File compFile = new File("e:/svntest/wc/impProject/juniper_config.txt");
		// 获得SVNDiffClient类的实例。
		SVNDiffClient diff = initSVNClient().getDiffClient();
		// 保存比较结果的输出流
		BufferedOutputStream result = new BufferedOutputStream(new FileOutputStream("E:/result.txt"));
		// 比较compFile文件的SVNRevision.WORKING版本和
		// SVNRevision.HEAD版本的差异，结果保存在E:/result.txt文件中。
		// SVNRevision.WORKING版本指工作副本中当前内容的版本，SVNRevision.HEAD版本指的是版本库中最新的版本。
		diff.doDiff(compFile, SVNRevision.HEAD, SVNRevision.WORKING,
				SVNRevision.HEAD, SVNDepth.INFINITY, true, result, null);
		result.close();
		System.out.println("比较的结果保存在E:/result.txt文件中！");
	}
	
	public static ByteArrayOutputStream getFileContent(FileInformation fileInformation) throws Exception {
		SVNRepository repository = initSVNRepository();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SVNNodeKind nodeKind = repository.checkPath(fileInformation.getPathArray()[0],
				Long.parseLong(fileInformation.getVersionArray()[0]));
		if (nodeKind == SVNNodeKind.NONE)
			throw new Exception("the file does not exists.");
		else if (nodeKind == SVNNodeKind.DIR)
			throw new Exception(" It is a directory.");
		else if (nodeKind == SVNNodeKind.FILE) {
			repository.getFile(fileInformation.getPathArray()[0],
					Long.parseLong(fileInformation.getVersionArray()[0]), null, baos);
		}
		return baos;
	}
}
