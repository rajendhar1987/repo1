
import java.io.*;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class dbemail
 */
@WebServlet("/dbemail")
public class dbemail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public dbemail() {
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
		
		String message = "Request for New Queue\n\n";
		message = message.concat("Sender: "+request.getParameter("email_sender")+"\n");
		message = message.concat("Receiver: "+request.getParameter("email_receiver")+"\n\n");
		message = message.concat("Topics interested: "+request.getParameter("email_topics"));
		
		Runtime.getRuntime().exec("echo \""+message+"\" | mailx -s \"Request for New Queue\" mqadmin@mbusa.com");

		request.getRequestDispatcher("dbconnection").forward(request, response);
	}

	// WEDNESDAY 10 AM
}
