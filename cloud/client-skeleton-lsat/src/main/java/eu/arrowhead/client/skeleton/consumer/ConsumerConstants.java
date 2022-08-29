package eu.arrowhead.client.skeleton.consumer;

public class ConsumerConstants {
	
	//=================================================================================================
	// members
	
	public static final String BASE_PACKAGE = "ai.aitia";
	
	public static final String INTERFACE_SECURE = "HTTPS-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";

	public static final String GET_SERVICE_DEFINITION_SDF3 = "sdf3-provider";
	public static final String GET_SERVICE_DEFINITION_CIF = "cif-provider";
	public static final String GET_SERVICE_DEFINITION_TRANS = "trans-provider";
	public static final String GET_SERVICE_DEFINITION_MCRL2 = "mcrl2-provider";
	public static final String GET_SERVICE_DEFINITION_PLE = "ple-provider";
	
	
	public static final String GET_SERVICE_URI = "/consumer";
	public static final String REQUEST_PARAM_TEXT = "text";
	public static final String REQUEST_PARAM_KEY_TEXT = "request-param-text";
	public static final String REQUEST_PARAM_FILE= "file";
	public static final String REQUEST_PARAM_KEY_FILE = "request-param-file";
	
	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private ConsumerConstants() {
		throw new UnsupportedOperationException();
	}

}
