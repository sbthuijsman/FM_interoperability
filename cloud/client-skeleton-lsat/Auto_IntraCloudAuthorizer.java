
package eu.arrowhead.client.skeleton.consumer.service;

import java.util.*; 
import java.nio.file.*; 
import java.io.IOException;

import org.apache.logging.log4j.LogManager; 
import org.apache.logging.log4j.Logger; 
import org.apache.http.Header; 
import org.apache.http.HttpEntity; 
import org.apache.http.HttpHeaders; 
import org.apache.http.NameValuePair; 
import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.CloseableHttpResponse; 
import org.apache.http.client.methods.HttpGet; 
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.impl.client.CloseableHttpClient; 
import org.apache.http.impl.client.HttpClients; 
import org.apache.http.message.BasicNameValuePair; 
import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.ApplicationArguments; 
import org.springframework.boot.ApplicationRunner; 
import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.context.annotation.ComponentScan; 
import org.springframework.http.HttpMethod;

import java.nio.charset.StandardCharsets; 
import java.sql.Connection; import java.sql.DriverManager; 
import java.sql.SQLException; import java.sql.Statement; 
import java.sql.ResultSet;

import eu.arrowhead.client.library.ArrowheadService; 
import eu.arrowhead.common.CommonConstants; 
import eu.arrowhead.common.SSLProperties; 
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag; 
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO; 
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder; 
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO; 
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO; 
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO; 
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO; 
import eu.arrowhead.common.exception.InvalidParameterException;

import eu.arrowhead.client.skeleton.consumer.ConsumerConstants;

public class Auto_IntraCloudAuthorizer {

	//===========================================================================
	====================== // members 

			public final String hostid ="jdbc:mysql://localhost:3306/arrowhead?user=root&password=*1245Indus"; 
	public final String uname = "root"; 
	public final String upass= " *1245Indus ";

	private final CloseableHttpClient httpClient = HttpClients.createDefault();

	public void postIntracloudAuthRules(final String AUTH_INTRACLOUD_URL,final
			int consumerID_u,final int interfaceID_u,final int providerID_u, final int
			serviceDefinitionID_u) throws Exception{ HttpPost post = new
			HttpPost(AUTH_INTRACLOUD_URL); List<NameValuePair> urlParameters = new
			ArrayList<>(); urlParameters.add(new BasicNameValuePair("consumerId",
					Integer.toString(consumerID_u))); urlParameters.add(new
							BasicNameValuePair("interfaceIds", Integer.toString(interfaceID_u)));
					urlParameters.add(new BasicNameValuePair("providerIds",
							Integer.toString(providerID_u))); urlParameters.add(new
									BasicNameValuePair("serviceDefinitionIds",
											Integer.toString(serviceDefinitionID_u)));

							post.setEntity(new UrlEncodedFormEntity(urlParameters));

							try{ CloseableHttpResponse response = httpClient.execute(post);
							CloseableHttpClient httpClient = HttpClients.createDefault();
							System.out.println(EntityUtils.toString(response.getEntity())); }
							catch(Exception e) { System.out.println("Error in POST :"+e); } }

	public int getconsID() throws Exception { int cons_id_val=-1; try{
		Class.forName("com.mysql.jdbc.Driver"); Connection
		con=DriverManager.getConnection(hostid); Statement stmt =
		con.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY); String SQL =
				"SELECT id FROM system_ where system_name=consumer_skeleton"; ResultSet rs =
				stmt.executeQuery( SQL );

				while(rs.next()) { cons_id_val = rs.getInt("id"); }

	}catch(SQLException e){ System.out.println(e); } return cons_id_val; }

	public int getintID() throws Exception { int cons_id_val=-1; try{
		Class.forName("com.mysql.jdbc.Driver"); Connection
		con=DriverManager.getConnection(hostid); Statement stmt =
		con.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY); String SQL =
				"SELECT id FROM service_interface where interface_name="+ConsumerConstants.
				INTERFACE_SECURE; ResultSet rs = stmt.executeQuery( SQL );

				while(rs.next()) { cons_id_val = rs.getInt("id"); }

	}catch(SQLException e){ System.out.println(e); } return cons_id_val; }

	public int getservID() throws Exception { int cons_id_val=-1; try{
		Class.forName("com.mysql.jdbc.Driver"); Connection
		con=DriverManager.getConnection(hostid); Statement stmt =
		con.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY); String SQL =
				"SELECT id FROM service_definition where service_definition=get-provider-service"
				; ResultSet rs = stmt.executeQuery( SQL );

				while(rs.next()) { cons_id_val = rs.getInt("id"); } return cons_id_val;
	}catch(SQLException e){ System.out.println(e); } return cons_id_val; } public
	int getprovID() throws Exception { int cons_id_val=-1;

	try{ Class.forName("com.mysql.jdbc.Driver"); Connection
	con=DriverManager.getConnection(hostid); Statement stmt =
	con.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE,
			ResultSet.CONCUR_READ_ONLY); String SQL =
			"SELECT id FROM system_ where system_name=provider_skeleton"; ResultSet rs =
			stmt.executeQuery( SQL );

			while(rs.next()) { cons_id_val = rs.getInt("id"); } return cons_id_val;
	}catch(SQLException e){ System.out.println(e); } return cons_id_val; }


	//---------------------------------------------------------------------------
	----------------------

}
