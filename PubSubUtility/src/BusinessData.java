import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusinessData {
	
	private String business_service;
	private String queue;
	private int order;
	private String primaryKey;
	
	public String getBusiness_service() {
		return business_service;
	}
	public void setBusiness_service(String business_service) {
		this.business_service = business_service;
	}
	
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getDeleteKey() {
		return primaryKey.concat("_delete");
	}
	
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	public static List<BusinessData> convertResultSetToList(ResultSet rs) throws SQLException {
		List<BusinessData> list = new ArrayList<BusinessData>();

		while (rs.next()) {
			BusinessData businessdata = new BusinessData();
			businessdata.setBusiness_service(rs.getString(1).trim());
			businessdata.setQueue(rs.getString(3).trim());
			businessdata.setOrder(rs.getInt(4));
			businessdata.setPrimaryKey(rs.getString(1).trim()+"_"+rs.getString(3).trim());
			list.add(businessdata);
		}

		return list;
	}
}
