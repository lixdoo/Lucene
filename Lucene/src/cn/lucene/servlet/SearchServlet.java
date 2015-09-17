package cn.lucene.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

import cn.lucene.model.FileInformation;

public class SearchServlet extends CTLServlet{
	private static final long serialVersionUID = 1L;
	
	public SearchServlet() {
		super();
	}
	
	public void contentsSearch(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, SecurityException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ParseException {
		JSONHelper.ResponseBean(new FileInformation(request).fullTextRetrieval(), response);
	}
	
	public void nameSearch(HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ParseException {
		JSONHelper.ResponseBean(new FileInformation(request).nameSearch(), response);
	}
	
	public void authorSearch(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException, ParseException {
		JSONHelper.ResponseBean(new FileInformation(request).authorSearch(), response);
	}
	
	public void typeSearch(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException, ParseException {
		JSONHelper.ResponseBean(new FileInformation(request).typeSearch(), response);
	}     
	
	public void dateSearch(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException, ParseException {
		JSONHelper.ResponseBean(new FileInformation(request).dateSearch(), response);
	}
	
	public void versionSearch(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException, ParseException {
		JSONHelper.ResponseBean(new FileInformation(request).versionSearch(), response);
	}
	
	public void complexitySearch(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException, ParseException {
		JSONHelper.ResponseBean(new FileInformation(request).complexitySearch(), response);
	}
}
