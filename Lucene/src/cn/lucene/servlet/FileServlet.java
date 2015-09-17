package cn.lucene.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tmatesoft.svn.core.SVNException;
import cn.lucene.model.FileInformation;
import cn.lucnen.utils.FileUtils;
import cn.lucnen.utils.SVNUtils;

public class FileServlet extends CTLServlet {
	
	private static final long serialVersionUID = 1L;

	public FileServlet() {
		super();
	}
	
	public void openFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		FileInformation fileInformation = new FileInformation(request);
		String filePath = fileInformation.getPath();
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
		ByteArrayOutputStream baos = SVNUtils.openFile(fileInformation);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition","inline; filename=\"" + new String(fileName.getBytes("GB2312"),"ISO8859-1"));
		String fileExt = FileUtils.getExtension(fileName);
		if(FileUtils.isTextFile(fileName)){
			response.setContentType("text/plain");
			request.setAttribute("data", baos);
			request.setAttribute("file", fileName);
			request.getRequestDispatcher("/SyntaxHighlighter/filecontent.jsp").forward(request, response);
		}else {
			if(FileUtils.isImage(fileName))
				response.setContentType("image/" + fileExt);
			else response.setContentType("application/octet-stream");
			ServletOutputStream out = response.getOutputStream();
			baos.writeTo(out);
			out.flush();
			out.close();
		}
	}
	
	public void openLog(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException, SVNException {
		JSONHelper.ResponseBean(new SVNUtils().openLog(new FileInformation(request)), response);
	}
	
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		FileInformation fileInformation = new FileInformation(request);
		ServletOutputStream  servletOutputStream = response.getOutputStream();
		String[] filePath = fileInformation.getPathArray();
		if(filePath.length == 1) {
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition","attachment; filename=" + new String(filePath[0].substring(filePath[0].lastIndexOf("/")+1, filePath[0].length()).getBytes("UTF-8"),"ISO8859-1"));
			FileUtils.downloadFile(fileInformation,servletOutputStream);
		} else{
			response.setHeader("Content-Type", "application/x-zip");
			FileInformation zipFile= FileUtils.createZipFile(fileInformation);
			response.setHeader("Content-Disposition", "attachment; filename =" + zipFile.getName());
			FileUtils.downloadZipFile(zipFile, servletOutputStream);
			FileUtils.deleteZipFile(zipFile);
		}
	}
	
	public void compareDifference() {
		
	}
}
