
import javax.annotation.Resource;
import javax.jms.JMSException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * Servlet implementation class dbedit
 */
@WebServlet("/dbedit")
public class dbedit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public dbedit() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub


		//System.out.println("inside doPost");
		Context initCxt;
		Connection conn = null;
		List<BusinessData> dbquery= null;
		try {
			initCxt = new InitialContext();

			Context envContext = (Context) initCxt.lookup("java:comp/env");
			DataSource dataSource = (DataSource) envContext.lookup("jdbc/mqconfig");

			conn = dataSource.getConnection();

			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("select * from ROUTING_TABLE WHERE QMGR = 'NETSTAR'");
			dbquery=BusinessData.convertResultSetToList(rs);
			
			BusinessData businessdata = new BusinessData();
			String queueName=null;
			String topicName=null;
			String primaryKey=null;
			for(int i=0;i<dbquery.size();i++)
			{
				businessdata=dbquery.get(i);
				queueName=businessdata.getQueue();
				topicName=businessdata.getBusiness_service();
				primaryKey=businessdata.getPrimaryKey();
				String query;
				
				if ((request.getParameter(businessdata.getDeleteKey()))!=null) {
					query = "DELETE FROM ROUTING_TABLE WHERE BUSINESS_SERVICE=? AND QUEUE=? AND QMGR='NETSTAR'";
					PreparedStatement stmt1 = conn.prepareStatement(query);
					stmt1.setString(1, topicName);
					stmt1.setString(2, queueName);
					stmt1.executeUpdate();
					continue;
				}
				
				query ="UPDATE ROUTING_TABLE SET ORDER=? Where QUEUE=? and BUSINESS_SERVICE=? and QMGR='NETSTAR'";
				PreparedStatement stmt2 = conn.prepareStatement(query);
				stmt2.setInt(1, request.getParameter(primaryKey)==null ? 0 : 1);
				stmt2.setString(2, queueName);
				stmt2.setString(3, topicName);
				stmt2.executeUpdate();
			}
			
			Date now = new Date();
			mqSendmsg mqsend = new mqSendmsg();
			try {
				mqsend.sendResponse(now.toString());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}


		request.getRequestDispatcher("dbconnection").forward(request, response);
	}

}
