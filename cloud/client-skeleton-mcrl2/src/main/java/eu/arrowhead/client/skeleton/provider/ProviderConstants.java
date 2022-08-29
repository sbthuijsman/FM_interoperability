package eu.arrowhead.client.skeleton.provider;
public class ProviderConstants {
	
	//=================================================================================================
	// members
	
	public static final String BASE_PACKAGE = "ai.aitia";
	
	public static final String GET_SERVICE_DEFINITION = "mcrl2-provider";
	
	public static final String INTERFACE_SECURE = "HTTPS-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	public static final String GET_SERVICE_URI = "/provider";
	public static final String BY_ID_PATH = "/{id}";
	public static final String PATH_VARIABLE_ID = "id";
	public static final String REQUEST_PARAM_KEY_TEXT = "request-param-text";
	public static final String REQUEST_PARAM_TEXT = "text";
	public static final String REQUEST_PARAM_KEY_FILE = "request-param-file";
	public static final String REQUEST_PARAM_FILE = "file";
		
	
	//=================================================================================================
	// assistant methods
	public static String FILE_LOCATION="";

	//-------------------------------------------------------------------------------------------------
	public ProviderConstants() {
		throw new UnsupportedOperationException();
	}
	
}
