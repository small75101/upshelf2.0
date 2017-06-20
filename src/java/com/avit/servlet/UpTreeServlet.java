package com.avit.servlet;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.avit.upshelf.DBConnect;
import com.avit.upshelf.UpshelfThread;


public class UpTreeServlet extends HttpServlet {

	private static final long serialVersionUID = 10413685297361063L;

	/**
	 * Constructor of the object.
	 */
	public UpTreeServlet() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String operationCode = request.getParameter("operationCode");
		UpshelfThread[] tArray = new UpshelfThread[20];
		int length = 0;
		DBConnect db = new DBConnect();
		
		try {
			if (StringUtils.isEmpty(operationCode)) {
				response.getWriter().write("operation_code is null");
				return;
			}
			List<Integer> runUptreeList = db.queryRunUpTree(operationCode);
			if(runUptreeList != null && runUptreeList.size()>0){
				response.getWriter().write("running");
				return;
			}
			if (StringUtils.isEmpty(id)) {
				List<String> ids = db.getAutoIds(operationCode);
				for (String tmpID : ids) {
					// 上架所有树
					tArray[length] = new UpshelfThread(tmpID,operationCode);
					tArray[length].start();
					Thread.sleep(500);
					length++;
				}
				db.closeConnection();
				db = null;
			} else {
				tArray[length] = new UpshelfThread(id, operationCode);
				tArray[length].start();
				length++;
			}

			boolean breaks = false;
			while (!breaks) {
				breaks = true;
				for (int i = 0; i < length; i++) {
					State state = tArray[i].getState();
					if (state != State.TERMINATED) {
						breaks = false;
					}
				}
				Thread.sleep(1000);
			}
			response.getWriter().write("success");
		} catch (Exception ex) {
			ex.printStackTrace();
			response.getWriter().write("fail");
		} finally {
			db.closeConnection();
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
