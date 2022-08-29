package eu.arrowhead.client.skeleton.provider.controller;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import java.net.URISyntaxException; 

import eu.arrowhead.client.skeleton.provider.service.ProviderService;
import eu.arrowhead.client.skeleton.provider.ProviderConstants;
import eu.arrowhead.common.exception.BadPayloadException;

@RestController
public class ProviderController {
	//=================================================================================================
	// members

	@Autowired
	private ProviderService providerService;
	//TODO: add your variables here

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	//@GetMapping(path = ProviderConstants.GET_SERVICE_URI, produces = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody public String getCombinedText(@RequestParam (name = "file") final String textToAdd){
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String getCombinedText(@RequestBody final String textToAdd){

		System.out.print("\n Reached provider \n");
		return providerService.getCombText(textToAdd);
	}
	//-------------------------------------------------------------------------------------------------
	//Assistant methods
	//TODO: implement here your provider related REST end points
}
