
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * Servlet implementation class dbnew
 */
@WebServlet("/dbnew")
public class dbnew extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public dbnew() {
		super();
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
		Context initCxt;
		Connection conn = null;
		try {
			initCxt = new InitialContext();

			Context envContext = (Context) initCxt.lookup("java:comp/env");
			DataSource dataSource = (DataSource) envContext.lookup("jdbc/mqconfig");

			conn = dataSource.getConnection();
			
			String topic = request.getParameter("newtopic");
			topic = topic.replaceAll("_", "");
			topic = topic.replaceAll(" ", "");
			topic = topic.replaceAll("'", "");
			topic = topic.replaceAll("\"", "");
			topic = "/Netstar/".concat(topic);

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from ROUTING_TABLE WHERE QMGR = 'NETSTAR'");
			List<BusinessData> dbquery=BusinessData.convertResultSetToList(rs);
			BusinessData tmp;
			boolean repeat = false;
			
			for (int i=0; i<dbquery.size(); i++) {
				tmp = dbquery.get(i);
				if (topic.equals(tmp.getBusiness_service()) && request.getParameter("newqueue").trim().equals(tmp.getQueue())) {
					repeat = true;
					String query = "UPDATE ROUTING_TABLE SET ORDER=? Where QUEUE=? and BUSINESS_SERVICE=? and QMGR='NETSTAR'";
					PreparedStatement stmt2 = conn.prepareStatement(query);
					stmt2.setInt(1, request.getParameter("newsubscribe")==null ? 0 : 1);
					stmt2.setString(2, tmp.getQueue());
					stmt2.setString(3, tmp.getBusiness_service());
					stmt2.executeUpdate();
					break;
				}
			}
			
			if (!repeat) {
				String query = "INSERT INTO ROUTING_TABLE (BUSINESS_SERVICE, QMGR, QUEUE, ORDER, TIMEOUT_SEC) VALUES (?, 'NETSTAR', ?, ?, -1)";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, topic);
				pstmt.setString(2, request.getParameter("newqueue").trim());
				pstmt.setInt(3, (request.getParameter("newsubscribe")==null ? 0 : 1));
				pstmt.executeUpdate();
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
