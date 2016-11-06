package org.iisiplusone.eareport.contextmodel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparx.Repository;

public class EARepositoryFactory {

	private static final Logger logger = LoggerFactory.getLogger(EARepositoryFactory.class);
	
	public static EARepositoryFactory INSTANCE;
	
	private Repository repo; 	
	private String eapFileLocation;
	private static Map<String,EARepositoryFactory> INSTANCES = new HashMap<>();
	
	/**
	 * Note file locations of the form /C:\path\to\file give internal errors on COM level!
	 * 
	 * @param eapFileLocation
	 */
	public static void registerEapFile(String eapFileLocation){
		if( !INSTANCES.containsKey("DEFAULT")){
			INSTANCES.put( "DEFAULT", new EARepositoryFactory (eapFileLocation));
		}
		
	}
	
	public static EARepositoryFactory getInstance(){
			
		return INSTANCES.get("DEFAULT");
	}
	
	private EARepositoryFactory(String eapFileLocation) {	
		this.eapFileLocation = eapFileLocation;
		

	}
	
	private void openRepository(){
		
		logger.info("Opening eap file: "+this.eapFileLocation);
		
		repo = new Repository();	
		repo.OpenFile(this.eapFileLocation);
		
				
		Runtime.getRuntime().addShutdownHook(new Thread( new Runnable() {
			
			@Override
			public void run() {
				logger.info("Requesting repository to close file.");
				repo.CloseFile();
				repo.Exit();
			}
		}));
	}
	
	public Repository getRepository(){
		
		if( repo == null){
			openRepository();
		}
		
		return repo;
	}

}
