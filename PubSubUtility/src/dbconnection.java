

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class dbconnection
 */
@WebServlet("/dbconnection")
public class dbconnection extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public dbconnection() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				//response.getWriter().append("Served at: ").append(request.getContextPath());
		// request.getRequestDispatcher("/WEB-INF/configTable.jsp").forward(request, response);
		//request.setAttribute("message", totalentries);
		//request.getRequestDispatcher("configTable.jsp").forward(request,request);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		Context initCxt;
		RequestDispatcher rd = null;
        Connection conn = null;
        List<BusinessData> dbquery= null;
        List<String> queues = new ArrayList<String>();
		try {
			initCxt = new InitialContext();
			
	        Context envContext = (Context) initCxt.lookup("java:comp/env");
	        DataSource dataSource = (DataSource) envContext.lookup("jdbc/mqconfig");
	
			conn = dataSource.getConnection();
			
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery("select * from ROUTING_TABLE WHERE QMGR = 'NETSTAR'");
	        dbquery=convertResultSetToList(rs);

		} catch (NamingException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
        
		for (BusinessData bd : dbquery) {
			if (!queues.contains(bd.getQueue())) queues.add(bd.getQueue());
		}
		
        rd = request.getRequestDispatcher("/main.jsp");
        request.setAttribute("dbquery", dbquery);
        request.setAttribute("queues", queues);

		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		rd.forward(request, response);

    }

	public List<BusinessData> convertResultSetToList(ResultSet rs) throws SQLException {
	    List<BusinessData> list = new ArrayList<BusinessData>();

	    while (rs.next()) {
	    	BusinessData businessdata = new BusinessData();
	        businessdata.setBusiness_service(rs.getString(1));
	        businessdata.setQueue(rs.getString(3));
	        businessdata.setOrder(rs.getInt(4));
	        businessdata.setPrimaryKey(rs.getString(1).trim()+"_"+rs.getString(3).trim());
	        list.add(businessdata);
	    }

	    return list;
	}

}
