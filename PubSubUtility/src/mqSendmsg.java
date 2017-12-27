import javax.jms.TextMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer; 
import javax.jms.Connection;
import javax.jms.Session;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
public class mqSendmsg {

	@Resource(name = "jms/HelloConnectionFactory")
    private ConnectionFactory connectionFactoryRef;
    
    @Resource(name = "jms/Response_HelloQueue")
    private Destination resourceDestRef;
    

	
	
	public void sendResponse(String responseData) throws JMSException {
        Connection jmsConnection = null;
        Session session = null;
        
        try {
            jmsConnection = connectionFactoryRef.createConnection();
            session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            TextMessage responseMessage = session.createTextMessage();
            // ! Set correlation id from our original message id
            //responseMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());
            responseMessage.setText(responseData);
            
            MessageProducer mp = session.createProducer(null);
            // use 30 minute expiry
           // mp.setTimeToLive(1000 * 60 * 30);
            mp.send(resourceDestRef, responseMessage);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (JMSException e) {
                
            }
            
            try {
                if (jmsConnection != null) {
                    jmsConnection.close();
                }
            } catch (JMSException e) {
                
            }
        }
    }

	
	
	
	
}
