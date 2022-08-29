package eu.arrowhead.client.skeleton.provider.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileWriter;
import java.lang.*;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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

import eu.arrowhead.common.exception.BadPayloadException;
import eu.arrowhead.client.skeleton.provider.ProviderConstants;


@RestController
public class ProviderService {

	//=================================================================================================
	// members

	final String filePath="classpath:testFile.txt";
	//TODO: add your variables here

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------

	public String getCombText(final String textToAdd){
		//Creating the file if not exist
		String locat=new String();
		String locat2=new String();

		locat=ProviderConstants.FILE_LOCATION+"\\"+"twilightTemp.activity";
		Path path=Paths.get(locat);

		locat2=ProviderConstants.FILE_LOCATION+"\\"+"LSAT2CIFTranslator";
		Path path2=Paths.get(locat2);

		try{
			File myObj = new File(path.toString());
			if(myObj.createNewFile()){
				System.out.println("\nTemporary File created in path "+path.toString()+"\n");		
			}else {
				System.out.println("\nTemporary File exists in path "+path.toString()+"\n");	
			}
			FileWriter myWriter = new FileWriter(path.toString());
			myWriter.write(textToAdd);
			System.out.println("\nText added!\n");	
			myWriter.close();

		}catch(IOException e){
			System.out.println("\nFile couldn't be created!");
		}


		try{
			ProcessBuilder launcher =new ProcessBuilder(path2.toString(), ProviderConstants.FILE_LOCATION);
			Process process=launcher.start();
			process.waitFor();
			String data1="Process completed successfully!";
			System.out.print("\n"+data1+"\n");
			String loc= ProviderConstants.FILE_LOCATION+"\\"+"Lsat2CIF.cif";
			String data_ =new String(Files.readAllBytes(Paths.get(loc)));
			return data_;

		} catch (Exception e) {
			String data2="\nProcess failed gloriously!"+e;
			return data2;

		}
	}

	//-------------------------------------------------------------------------------------------------
	//Assistant methods

	//TODO: implement here your provider related REST end points
}
