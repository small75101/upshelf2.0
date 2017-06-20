package com.avit.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.avit.getDate.BODateParse;
import com.avit.upshelf.DBConnect;
import com.avit.upshelf.UpshelfService;
import com.avit.util.InitConfig;

/**
 * @ClassName TestServlet
 * @Description
 * @author panxincheng
 * @date 2013-3-1 
 */
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 3440972257095860613L;

	/**
	 * Constructor of the object.
	 */
	public TestServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String json_url = "http://10.84.253.27:9005/stbservlet?attribute=ewf_json_stb_list_entry"
				+ "&parent_HUID=TVMN430";
		BODateParse json = new BODateParse();
		DBConnect jdbc = null;
		try {
			jdbc = new DBConnect();
			json.init(jdbc.getLocalConnection());
			json.dataRsync(json_url, 0, "118", "center");
			UpshelfService upshelf = new UpshelfService();
			upshelf.init(jdbc, "OP_CD");
			upshelf.dataUpshelf(json_url,2884L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			jdbc.closeConnection();
			jdbc = null;
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		InitConfig config = new InitConfig();	
		config.initConfig();
	}

}
