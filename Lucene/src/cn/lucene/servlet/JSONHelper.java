package cn.lucene.servlet;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class JSONHelper {
	public static void ResponseBean(Object bean, HttpServletResponse response)
			throws IOException {

		ServletOutputStream out = response.getOutputStream();
		out.write(new Gson().toJson(bean).getBytes("UTF-8"));
		out.flush();
		out.close();
	}

}
