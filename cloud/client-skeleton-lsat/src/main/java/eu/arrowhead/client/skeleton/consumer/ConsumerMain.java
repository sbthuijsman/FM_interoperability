package eu.arrowhead.client.skeleton.consumer;

import java.util.*;
import java.nio.file.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.json.*;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE}) //TODO: add custom packages if any
public class ConsumerMain implements ApplicationRunner {

	//=================================================================================================
	// members
	@Autowired
	private ArrowheadService arrowheadService;

	@Autowired
	protected SSLProperties sslProperties;

	private final Logger logger = LogManager.getLogger( ConsumerMain.class );

	//=================================================================================================
	// methods

	//------------------------------------------------------------------------------------------------
	public static void main( final String[] args ) {
		SpringApplication.run(ConsumerMain.class, args);
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void run(final ApplicationArguments args) throws Exception {
		getConsumerServiceOrchestrationAndConsumption();
	}
	//SIMPLE EXAMPLE OF INITIATING AN ORCHESTRATION
	//-------------------------------------------------------------------------------------------------
	public void setOrchestrationRules() {

	}
	public void getConsumerServiceOrchestrationAndConsumption() {

		Scanner cho= new Scanner(System.in);
		System.out.print("\nEnter (0) for Model Translation: ");
		System.out.print("\nEnter (1) for PLE: ");
		System.out.print("\nEnter (2) for Supervisor Synthesis: ");
		System.out.print("\nEnter (3) for Timing Analysis: ");
		System.out.print("\nEnter (4) for mCRL2: ");
		int choice= cho.nextInt();

		if(choice==0) { //Code for Model Translation service
			logger.info("Orchestration request for " + ConsumerConstants.GET_SERVICE_DEFINITION_TRANS + " service:");
			final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(ConsumerConstants.GET_SERVICE_DEFINITION_TRANS)
					.interfaces(getInterface())
					.build();

			final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
			final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder.requestedService(serviceQueryForm)
					.flag(Flag.MATCHMAKING, true)
					.flag(Flag.OVERRIDE_STORE, true)
					.build();

			printOut(orchestrationFormRequest);		

			final OrchestrationResponseDTO orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationFormRequest);

			logger.info("Orchestration response:");
			printOut(orchestrationResponse);		

			if (orchestrationResponse == null) {
				logger.info("No orchestration response received");
			} else if (orchestrationResponse.getResponse().isEmpty()) {
				logger.info("No provider found during the orchestration");
			} else {
				final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
				validateOrchestrationResult(orchestrationResult, ConsumerConstants.GET_SERVICE_DEFINITION_TRANS);
				final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
				String loc1= new String();
				String data_="";
				Path loc=Paths.get(loc1);
				try{
					Scanner sc= new Scanner(System.in);
					System.out.print("Enter directory of activity file: ");
					loc1= sc.nextLine();//reads string 
					loc=Paths.get(loc1);
					data_ =new String(Files.readAllBytes(loc));

				} catch(IOException e) {
					logger.info("Can't read activity file!"); // Or something more intellegent
				}

				//final String[] queryParamPayload= {orchestrationResult.getMetadata().get(ConsumerConstants.REQUEST_PARAM_KEY_FILE), json};

				String combText=new String();
				try {

					@SuppressWarnings("unchecked")
					final String combTextTemp = arrowheadService.consumeServiceHTTP(String.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
							orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
							getInterface(), token, data_, new String[0]);
					combText=combTextTemp;

				}catch (Exception e) {

					logger.info("Problem at server end: "+e);
					System.exit(0);
				}

				logger.info("Provider result received!");

				try{
					File myObj = new File((loc.getParent()).toString()+"\\"+"twilightConverted.cif");
					if(myObj.createNewFile()){
						logger.info("Temporary File created in path "+(loc.getParent()).toString());		
					}else {
						logger.info("Temporary File exists in path "+(loc.getParent()).toString());	
					}
					FileWriter myWriter = new FileWriter((loc.getParent()).toString()+"\\"+"twilightConverted.cif");
					myWriter.write(combText);
					logger.info("File written successfully!");	
					myWriter.close();

				}catch(IOException e){
					logger.info("File couldn't be created");
				}
			}

		}else if(choice==1) { //Code for PLE service
			logger.info("Orchestration request for " + ConsumerConstants.GET_SERVICE_DEFINITION_PLE + " service:");
			final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(ConsumerConstants.GET_SERVICE_DEFINITION_PLE)
					.interfaces(getInterface())
					.build();

			final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
			final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder.requestedService(serviceQueryForm)
					.flag(Flag.MATCHMAKING, true)
					.flag(Flag.OVERRIDE_STORE, true)
					.build();

			printOut(orchestrationFormRequest);		

			final OrchestrationResponseDTO orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationFormRequest);

			logger.info("Orchestration response:");
			printOut(orchestrationResponse);		

			if (orchestrationResponse == null) {
				logger.info("No orchestration response received");
			} else if (orchestrationResponse.getResponse().isEmpty()) {
				logger.info("No provider found during the orchestration");
			} else {
				final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
				validateOrchestrationResult(orchestrationResult, ConsumerConstants.GET_SERVICE_DEFINITION_PLE);
				final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
				
				String loc1= new String();
				Path loc=Paths.get(loc1);
				Scanner sc= new Scanner(System.in);
				System.out.print("Enter directory where variant model will be received: ");
				loc1= sc.nextLine();//reads string 
				loc=Paths.get(loc1);
				
				
				System.out.print("Enter your configuration selection: ");
				String data_= sc.nextLine();//reads string  
				
				//final String[] queryParamPayload= {orchestrationResult.getMetadata().get(ConsumerConstants.REQUEST_PARAM_KEY_FILE), json};

				
				String combText=new String();
				try {

					@SuppressWarnings("unchecked")
					final String combTextTemp = arrowheadService.consumeServiceHTTP(String.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
							orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
							getInterface(), token, data_, new String[0]);
					combText=combTextTemp;

				}catch (Exception e) {

					logger.info("Problem at server end: "+e);
					System.exit(0);
				}

				logger.info("Provider result received!");

				try{
					File myObj = new File((loc.getParent()).toString()+"\\"+"twilight.activity");
					if(myObj.createNewFile()){
						logger.info("twilight.activity file created in path "+(loc.getParent()).toString());		
					}else {
						logger.info("twilight.activity file exists in path "+(loc.getParent()).toString());	
					}
					FileWriter myWriter = new FileWriter((loc.getParent()).toString()+"\\"+"twilight.activity");
					myWriter.write(combText);
					logger.info("File written successfully!");	
					myWriter.close();

				}catch(IOException e){
					logger.info("File couldn't be created");
				}
			}

		}else if(choice==2) { //Code for CIF servcice
			logger.info("Orchestration request for " + ConsumerConstants.GET_SERVICE_DEFINITION_CIF + " service:");
			final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(ConsumerConstants.GET_SERVICE_DEFINITION_CIF)
					.interfaces(getInterface())
					.build();

			final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
			final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder.requestedService(serviceQueryForm)
					.flag(Flag.MATCHMAKING, true)
					.flag(Flag.OVERRIDE_STORE, true)
					.build();

			printOut(orchestrationFormRequest);		

			final OrchestrationResponseDTO orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationFormRequest);

			logger.info("Orchestration response:");
			printOut(orchestrationResponse);		

			if (orchestrationResponse == null) {
				logger.info("No orchestration response received");
			} else if (orchestrationResponse.getResponse().isEmpty()) {
				logger.info("No provider found during the orchestration");
			} else {
				final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
				validateOrchestrationResult(orchestrationResult, ConsumerConstants.GET_SERVICE_DEFINITION_CIF);
				final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
				String loc= new String();
				String loc2= new String();
				String data_="";
				String data_2="";
				String combText="";
				Path path=Paths.get("");
				Path path2=Paths.get("");
				try{

					Scanner sc= new Scanner(System.in);
					System.out.print("Enter file location of translated CIF file: ");
					loc= sc.nextLine();//reads string  
					path=Paths.get(loc);
					data_ =new String(Files.readAllBytes(path));

					System.out.print("Enter file location of requirement CIF file: ");
					loc2= sc.nextLine();//reads string  
					path2=Paths.get(loc2);
					data_2 =new String(Files.readAllBytes(path2));

					data_=data_+"\n"+data_2;

				} catch(IOException e) {
					java.lang.System.out.println("Can't read CIF file!"); // Or something more intellegent
				}

				//final String[] queryParamPayload= {orchestrationResult.getMetadata().get(ConsumerConstants.REQUEST_PARAM_KEY_FILE), json};
				final int maxAttempts=3;
				int attempt=0;
				while(attempt++ <= maxAttempts) {
					try {
						@SuppressWarnings("unchecked")
						String combTextTemp = arrowheadService.consumeServiceHTTP(String.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
								orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
								getInterface(), token, data_, new String[0]);
						combText=combTextTemp;
						break;
					}catch (Exception e) {
						logger.info("Problem is at server end: "+e+"Attempt: "+attempt);
						System.exit(0);
					}
				}
				logger.info("Provider result received!");

				try{
					File myObj = new File((path.getParent()).toString()+"\\"+"ctrlsys.cif");
					if(myObj.createNewFile()){
						logger.info("Temporary File created in path "+(path.getParent()).toString());		
					}else {
						logger.info("Temporary File exists in path "+(path.getParent()).toString());	
					}
					FileWriter myWriter = new FileWriter((path.getParent()).toString()+"\\"+"ctrlsys.cif");
					myWriter.write(combText);
					logger.info("File written successfully!");	
					myWriter.close();

				}catch(IOException e){
					logger.info("File couldn't be created!");
				}

			}
		}else if(choice==3) { //Code for SDF3 servcice
			logger.info("Orchestration request for " + ConsumerConstants.GET_SERVICE_DEFINITION_SDF3 + " service:");
			final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(ConsumerConstants.GET_SERVICE_DEFINITION_SDF3)
					.interfaces(getInterface())
					.build();

			final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
			final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder.requestedService(serviceQueryForm)
					.flag(Flag.MATCHMAKING, true)
					.flag(Flag.OVERRIDE_STORE, true)
					.build();

			printOut(orchestrationFormRequest);		

			final OrchestrationResponseDTO orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationFormRequest);

			logger.info("Orchestration response:");
			printOut(orchestrationResponse);		

			if (orchestrationResponse == null) {
				logger.info("No orchestration response received");
			} else if (orchestrationResponse.getResponse().isEmpty()) {
				logger.info("No provider found during the orchestration");
			} else {
				final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
				validateOrchestrationResult(orchestrationResult, ConsumerConstants.GET_SERVICE_DEFINITION_SDF3);
				final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
				
				String loc1= new String();
				Path loc=Paths.get(loc1);
				Scanner sc= new Scanner(System.in);
				System.out.print("Enter directory where analysis result will be received: ");
				loc1= sc.nextLine();//reads string 
				loc=Paths.get(loc1);
				
				
				System.out.print("Enter your cif file: ");
				
				String fileLoc= sc.nextLine();//reads string  
				String data_="";
				try {
					data_ = new String(Files.readAllBytes(Paths.get(fileLoc)));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//final String[] queryParamPayload= {orchestrationResult.getMetadata().get(ConsumerConstants.REQUEST_PARAM_KEY_FILE), json};

				
				String combText=new String();
				try {

					@SuppressWarnings("unchecked")
					final String combTextTemp = arrowheadService.consumeServiceHTTP(String.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
							orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
							getInterface(), token, data_, new String[0]);
					combText=combTextTemp;

				}catch (Exception e) {

					logger.info("Problem at server end: "+e);
					System.exit(0);
				}

				logger.info("Provider result received!");

				try{
//					File myObj = new File(loc.toString()+"\\"+"xcps.ioa");
//					if(myObj.createNewFile()){
//						logger.info("xcps.mpanalysis file created in path "+(loc.getParent()).toString());		
//					}else {
//						logger.info("xcps.mpanalysis file exists in path "+(loc.getParent()).toString());	
//					}
					FileWriter myWriter = new FileWriter(loc.toString()+"\\"+"xcps.mpanalysis");
					myWriter.write(combText);
					logger.info("File written successfully!");	
					myWriter.close();

				}catch(IOException e){
					logger.info("File couldn't be created");
				}
			}
			
		}else if(choice==4) { //Code for mCRL2 servcice
			logger.info("Orchestration request for " + ConsumerConstants.GET_SERVICE_DEFINITION_MCRL2 + " service:");
			final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(ConsumerConstants.GET_SERVICE_DEFINITION_MCRL2)
					.interfaces(getInterface())
					.build();

			final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
			final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder.requestedService(serviceQueryForm)
					.flag(Flag.MATCHMAKING, true)
					.flag(Flag.OVERRIDE_STORE, true)
					.build();

			printOut(orchestrationFormRequest);		

			final OrchestrationResponseDTO orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationFormRequest);

			logger.info("Orchestration response:");
			printOut(orchestrationResponse);		

			if (orchestrationResponse == null) {
				logger.info("No orchestration response received");
			} else if (orchestrationResponse.getResponse().isEmpty()) {
				logger.info("No provider found during the orchestration");
			} else {
				final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
				validateOrchestrationResult(orchestrationResult, ConsumerConstants.GET_SERVICE_DEFINITION_CIF);
				final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
				String loc= new String();
				String loc2= new String();
				String combText="";
				Path path=Paths.get("");
				Path path2=Paths.get("");
				try{

					Scanner sc= new Scanner(System.in);
					System.out.print("Enter file location of translated mcrl2 file: ");
					loc= sc.nextLine();//reads string  
					path=Paths.get(loc);
					data_ =new String(Files.readAllBytes(path));

				} catch(IOException e) {
					java.lang.System.out.println("Can't read mCRL2 file!"); // Or something more intellegent
				}

				//final String[] queryParamPayload= {orchestrationResult.getMetadata().get(ConsumerConstants.REQUEST_PARAM_KEY_FILE), json};
				final int maxAttempts=3;
				int attempt=0;
				while(attempt++ <= maxAttempts) {
					try {
						@SuppressWarnings("unchecked")
						String combTextTemp = arrowheadService.consumeServiceHTTP(String.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
								orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
								getInterface(), token, data_, new String[0]);
						combText=combTextTemp;
						break;
					}catch (Exception e) {
						logger.info("Problem is at server end: "+e+"Attempt: "+attempt);
						System.exit(0);
					}
				}
				logger.info("Provider result received!");

				try{
					File myObj = new File((path.getParent()).toString()+"\\"+"result.txt");
					if(myObj.createNewFile()){
						logger.info("Temporary File created in path "+(path.getParent()).toString());		
					}else {
						logger.info("Temporary File exists in path "+(path.getParent()).toString());	
					}
					FileWriter myWriter = new FileWriter((path.getParent()).toString()+"\\"+"result.txt");
					myWriter.write(combText);
					logger.info("File written successfully!");	
					myWriter.close();

				}catch(IOException e){
					logger.info("File couldn't be created!");
				}
			}
			
		}
		else {
			printOut("Invalid choice entered!");
		}
	}

	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private String getInterface() {
		return sslProperties.isSslEnabled() ? ConsumerConstants.INTERFACE_SECURE : ConsumerConstants.INTERFACE_INSECURE;
	}

	//-------------------------------------------------------------------------------------------------
	private void validateOrchestrationResult(final OrchestrationResultDTO orchestrationResult, final String serviceDefinitin) {
		if (!orchestrationResult.getService().getServiceDefinition().equalsIgnoreCase(serviceDefinitin)) {
			throw new InvalidParameterException("Requested and orchestrated service definition do not match");
		}

		boolean hasValidInterface = false;
		for (final ServiceInterfaceResponseDTO serviceInterface : orchestrationResult.getInterfaces()) {
			if (serviceInterface.getInterfaceName().equalsIgnoreCase(getInterface())) {
				hasValidInterface = true;
				break;
			}
		}
		if (!hasValidInterface) {
			throw new InvalidParameterException("Requested and orchestrated interface do not match");
		}
	}

	//-------------------------------------------------------------------------------------------------
	private void printOut(final Object object) {
		java.lang.System.out.println(Utilities.toPrettyJson(Utilities.toJson(object)));
	}
	//-----------------------------------------------------------------


}

