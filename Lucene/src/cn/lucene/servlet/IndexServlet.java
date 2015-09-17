package cn.lucene.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.tmatesoft.svn.core.SVNException;

import cn.lucnen.utils.IndexUtils;

public class IndexServlet extends CTLServlet {
	
	private static final long serialVersionUID = 1L;

	public IndexServlet() {
		super();
	}
	
	public void manualIndex(HttpServletRequest request, HttpServletResponse response) throws IOException, SVNException, XmlException, OpenXML4JException {
		new IndexUtils().manualIndex();
	}
	
	public void cycleIndex(HttpServletRequest request, HttpServletResponse response) throws IOException {
		new IndexUtils().cycleIndex();
	}
	
	public void monitorIndex(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	public void detailIndex(HttpServletRequest request, HttpServletResponse response) {
		
	}
}
