package cn.lucnen.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.apache.xmlbeans.XmlException;

import cn.lucene.model.FileInformation;

public class FileUtils {
	
	private static final String[] txtType = { "txt", "java", "cpp", "c",
		"cs", "css", "js", "php", "xml", "rb", "prl", "pl", "py"};
	
	private static final String[] imageType = { "gif", "jpeg", "jpg", "png",
		"psd", "bmp", "tiff", "tif", "swc", "jpc", "jp2", "jpx", "jb2",
		"xbm", "wbmp" };

	/**
	 * 转换Word类型文档
	 * @param inputStream 文件内容
	 * @param fileType 文件类型
	 * @return Word文档中的内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String convertWord(InputStream  inputStream, String fileType) throws FileNotFoundException, IOException {
		if(fileType.equals("doc")) {
			StringBuffer content = new StringBuffer("");
			Range range = new HWPFDocument(inputStream).getRange();
	        for (int i = 0; i < range.numParagraphs(); i++) {
	            content.append(range.getParagraph(i).text().trim());
	        }
	        return content.toString().trim();
		} else {
			return new XWPFWordExtractor(new XWPFDocument(inputStream)).getText().trim();
		}
	}
	
	/**
	 * 转换Excel类型文档
	 * @param inputStream 文件内容
	 * @return Excel文档中的内容
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static String convertXLS(InputStream inputStream) throws IOException, InvalidFormatException{
		StringBuffer content = new StringBuffer("");
		Sheet sheet = WorkbookFactory.create(inputStream).getSheetAt(0);
		Row row;
		for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
		    for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
		    	content.append(row.getCell(j).toString().trim());
		    }
		}
		return content.toString().trim();
	}
	
	/**
	 * 转换PPT类型文档
	 * @param inputStream 文件内容
	 * @param fileType 文件类型
	 * @return PPT文档中的内容
	 * @throws InvalidFormatException
	 * @throws XmlException
	 * @throws OpenXML4JException
	 * @throws IOException
	 */
	public static String convertPPT(InputStream inputStream, String fileType) throws InvalidFormatException, XmlException, OpenXML4JException, IOException {
		if(fileType.equals("ppt")) return new PowerPointExtractor(inputStream).getText();
		else return new XSLFPowerPointExtractor(OPCPackage.open(inputStream)).getText().trim();
	}
	
	/**
	 * 转换PDF类型文档
	 * @param inputStream 文件内容
	 * @return PDF文档中的内容
	 * @throws IOException
	 */
	public static String convertPDF(InputStream inputStream) throws IOException {
		return new PDFTextStripper().getText(PDDocument.load(inputStream)).trim();
	}
	
	/**
	 * 判断文件类型
	 * @param fileName 文件名
	 * @return 返回文件后缀名
	 */
	public static String checkFileType(String fileName) {
		if(fileName.substring(0,1).equals(".")) return "";
		Matcher matcher = Pattern.compile("^.*\\.([^\\.]*)$").matcher(fileName);
		if(matcher.matches()) return matcher.group(1).toLowerCase();
		else return "";
	}
	
	public static boolean isTextFile(final String filePath){
		List<String> list = Arrays.asList(txtType);
		String Extension = null;
		if (filePath != null) {
			Extension = FileUtils.getExtension(filePath);
			return "".equals(Extension)? true: list.contains(Extension);
		} else {
			return false;
		}
	}
	
	public static String getExtension(String fileName){
		Matcher matcher = Pattern.compile("^.*\\.([^\\.]*)$").matcher(fileName);
		if(matcher.matches()){
			return matcher.group(1).toLowerCase();
		}
		return "";
	}
	
	public static void downloadFile(FileInformation fileInformation, ServletOutputStream servletOutputStream) throws Exception {
		ByteArrayOutputStream baos = SVNUtils.getFileContent(fileInformation);
		InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
		byte[] buffer = null;
		if (baos.size() < 1024) buffer = new byte[(int) baos.size()];
		else buffer = new byte[1024];
        int readLength;
        try {
        	while (((readLength = inputStream.read(buffer)) != -1)) servletOutputStream.write(buffer, 0, readLength);
        	servletOutputStream.flush();
        } finally {
        	inputStream.close();
	        servletOutputStream.close();
        }
	}
	
	public static FileInformation createZipFile(FileInformation fileInformation) throws Exception {
		String filePath[] = fileInformation.getPathArray();
		FileInformation zipFile = new FileInformation();
		String zipFilePath = "E:\\downloadFloader";
		File downloadFloader = new File(zipFilePath);
		if (!downloadFloader.isDirectory())downloadFloader.mkdir();
		int readLength;
		InputStream inputStream = null;
		byte[] buffer = new byte[1024];
		ZipOutputStream zipOutputStream = null;
		String randomZipName = String.valueOf(new Date().getTime()) + ".zip";
		try {
			zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(downloadFloader + "/" + randomZipName)));
			try {
				for(int i=0; i<filePath.length; i++) {
					inputStream = new ByteArrayInputStream(SVNUtils.getFileContent(fileInformation).toByteArray());
					zipOutputStream.putNextEntry(new ZipEntry(filePath[i].substring(filePath[i].lastIndexOf("/")+1, filePath[i].length())));
					while((readLength = inputStream.read(buffer)) != -1) zipOutputStream.write(buffer, 0, readLength);
					zipOutputStream.setEncoding("UTF-8");
				}
			} finally {
					zipOutputStream.closeEntry();
					inputStream.close();
			}
			
		} finally {
			zipOutputStream.close();
		}
		zipFile.setName(randomZipName);
		zipFile.setPath(zipFilePath);
		return zipFile;
	}
	
	public static void deleteZipFile(FileInformation zipFile) {
		new File(zipFile.getPath() + "/" + zipFile.getName()).delete();
	}
	
	public static void downloadZipFile(FileInformation zipFile,
			ServletOutputStream servletOutputStream) throws IOException {
		File file = new File(zipFile.getPath() + "/" + zipFile.getName());
		InputStream inputStream = new FileInputStream(file);
		byte[] buffer = null;
		if (file.length() < 1024) buffer = new byte[(int) file.length()];
		else buffer = new byte[1024];
        int readLength;
        try {
        	while (((readLength = inputStream.read(buffer)) != -1)) servletOutputStream.write(buffer, 0, readLength);
        	servletOutputStream.flush();
        } finally {
        	inputStream.close();
	        servletOutputStream.close();
        }
		
	}
	
	public static boolean isImage(final String filePath) {
		List<String> list = Arrays.asList(imageType);
		String fileExtension = null;
		if (filePath != null) {
			fileExtension = getExtension(filePath);
			return (fileExtension != null) ? list.contains(fileExtension) : false;
		} else {
			return false;
		}
	}
}