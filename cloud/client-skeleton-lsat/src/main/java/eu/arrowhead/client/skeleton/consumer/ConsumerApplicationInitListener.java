package eu.arrowhead.client.skeleton.consumer;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import eu.arrowhead.client.library.ArrowheadService;
import eu.arrowhead.client.library.config.ApplicationInitListener;
import eu.arrowhead.client.library.util.ClientCommonConstants;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.core.CoreSystem;
import eu.arrowhead.common.dto.shared.ServiceRegistryRequestDTO;
import eu.arrowhead.common.dto.shared.ServiceSecurityType;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.client.skeleton.consumer.security.ConsumerSecurityConfig;
@Component
public class ConsumerApplicationInitListener extends ApplicationInitListener {

	//=================================================================================================
	// members
	
	@Autowired
	private ArrowheadService arrowheadService;
	
	@Value(ClientCommonConstants.$TOKEN_SECURITY_FILTER_ENABLED_WD)
	private boolean tokenSecurityFilterEnabled;
	
	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;
	
	@Value(ClientCommonConstants.$CLIENT_SYSTEM_NAME)
	private String mySystemName;
	
	@Value(ClientCommonConstants.$CLIENT_SERVER_ADDRESS_WD)
	private String mySystemAddress;
	
	@Value(ClientCommonConstants.$CLIENT_SERVER_PORT_WD)
	private int mySystemPort;
	
	@Autowired
	private ConsumerSecurityConfig consumerSecurityConfig;
	
	private final Logger logger = LogManager.getLogger(ConsumerApplicationInitListener.class);
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Override
	protected void customInit(final ContextRefreshedEvent event) {
		
		//Checking the availability of necessary core systems
		checkCoreSystemReachability(CoreSystem.SERVICE_REGISTRY);
		checkCoreSystemReachability(CoreSystem.ORCHESTRATOR);
		if (sslEnabled && tokenSecurityFilterEnabled) {
			checkCoreSystemReachability(CoreSystem.AUTHORIZATION);			

			//Initialize Arrowhead Context
			arrowheadService.updateCoreServiceURIs(CoreSystem.AUTHORIZATION);			
		
			setTokenSecurityFilter();
		
		}else {
			logger.info("TokenSecurityFilter in not active");
		}
		
		//Initialize Arrowhead Context
		arrowheadService.updateCoreServiceURIs(CoreSystem.ORCHESTRATOR);

		//TODO: implement here any custom behavior on application start up
		/*
		 * final ServiceRegistryRequestDTO createServiceRequest =
		 * createServiceRegistryRequest(ConsumerConstants.GET_SERVICE_DEFINITION,
		 * ConsumerConstants.GET_SERVICE_URI, HttpMethod.GET);
		 * 
		 * arrowheadService.forceRegisterServiceToServiceRegistry(createServiceRequest);
		 * logger.info("Service registered: {}",
		 * ConsumerConstants.GET_SERVICE_DEFINITION);
		 */
		
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void customDestroy() {
		//TODO: implement here any custom behavior on application shutdown
		//arrowheadService.unregisterServiceFromServiceRegistry(ConsumerConstants.GET_SERVICE_DEFINITION);
		//logger.info("Service unregistered: {}", ConsumerConstants.GET_SERVICE_DEFINITION);
	}
	//------------------------------------------------
	//Assistant methods
	private void setTokenSecurityFilter() {
		final PublicKey authorizationPublicKey = arrowheadService.queryAuthorizationPublicKey();
		if (authorizationPublicKey == null) {
			throw new ArrowheadException("Authorization public key is null");
		}
		
		KeyStore keystore;
		try {
			keystore = KeyStore.getInstance(sslProperties.getKeyStoreType());
			keystore.load(sslProperties.getKeyStore().getInputStream(), sslProperties.getKeyStorePassword().toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException ex) {
			throw new ArrowheadException(ex.getMessage());
		}			
		final PrivateKey consumerPrivateKey = Utilities.getPrivateKey(keystore, sslProperties.getKeyPassword());
		
		consumerSecurityConfig.getTokenSecurityFilter().setAuthorizationPublicKey(authorizationPublicKey);
		consumerSecurityConfig.getTokenSecurityFilter().setMyPrivateKey(consumerPrivateKey);

	}
	
	//-------------------------------------------------------------------------------------------------
	private ServiceRegistryRequestDTO createServiceRegistryRequest(final String serviceDefinition, final String serviceUri, final HttpMethod httpMethod) {
		final ServiceRegistryRequestDTO serviceRegistryRequest = new ServiceRegistryRequestDTO();
		serviceRegistryRequest.setServiceDefinition(serviceDefinition);
		final SystemRequestDTO systemRequest = new SystemRequestDTO();
		systemRequest.setSystemName(mySystemName);
		systemRequest.setAddress(mySystemAddress);
		systemRequest.setPort(mySystemPort);		

		if (tokenSecurityFilterEnabled) {
			systemRequest.setAuthenticationInfo(Base64.getEncoder().encodeToString(arrowheadService.getMyPublicKey().getEncoded()));
			serviceRegistryRequest.setSecure(ServiceSecurityType.TOKEN.name());
			serviceRegistryRequest.setInterfaces(List.of(ConsumerConstants.INTERFACE_SECURE));
		} else if (sslEnabled) {
			systemRequest.setAuthenticationInfo(Base64.getEncoder().encodeToString(arrowheadService.getMyPublicKey().getEncoded()));
			serviceRegistryRequest.setSecure(ServiceSecurityType.CERTIFICATE.name());
			serviceRegistryRequest.setInterfaces(List.of(ConsumerConstants.INTERFACE_SECURE));
		} else {
			serviceRegistryRequest.setSecure(ServiceSecurityType.NOT_SECURE.name());
			serviceRegistryRequest.setInterfaces(List.of(ConsumerConstants.INTERFACE_INSECURE));
		}
		serviceRegistryRequest.setProviderSystem(systemRequest);
		serviceRegistryRequest.setServiceUri(serviceUri);
		serviceRegistryRequest.setMetadata(new HashMap<>());
		serviceRegistryRequest.getMetadata().put(ConsumerConstants.HTTP_METHOD, httpMethod.name());
		return serviceRegistryRequest;
	}
}

