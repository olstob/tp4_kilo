package com.inf8430.kilo.factures_api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.launcher.SparkLauncher;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;

/**
 * Root resource (exposed at "resource" path)
 */
@Path("resource")
public class MyResource {
	
	private final static String CONNECTION_STRING = "mongodb://kilo:inf8430kilo@ds113693.mlab.com:13693/inf8430_tp4";
	private final static String DATABASE = "inf8430_tp4";
	private final static String FACTURES = "factures";
	private final static String PROPERTIES = "tp4.properties";
	
	@GET
    @Path("env")
    @Produces(MediaType.TEXT_PLAIN)
    public String check() {
		StringBuilder sb = new StringBuilder();
		
		Properties prop = new Properties();
    	try {
			InputStream propStream = new FileInputStream(new File(PROPERTIES));
			prop.load(propStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		sb.append(prop.getProperty("SPARK_HOME"));
		sb.append(prop.getProperty("SPARK_MASTER"));
		sb.append(prop.getProperty("FREQUENCY_JAR"));
		
		return sb.toString();
	}
	
    @POST
    @Path("add")
    @Produces(MediaType.TEXT_PLAIN)
    public String addFacture(String facture) {
    	
    	MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
    	MongoDatabase database = mongoClient.getDatabase(DATABASE);
    	MongoCollection<Document> collection = database.getCollection(FACTURES);
    	
    	
    	Document doc = Document.parse(facture);
    	
    	if(doc.containsKey("facture")) {
    		collection.insertOne(doc);
    	}
    	
    	
    	mongoClient.close();
    	
        return facture;
    }
    
    @GET
    @Path("get")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFrequents() {
    	MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
    	MongoDatabase database = mongoClient.getDatabase(DATABASE);
    	MongoCollection<Document> collection = database.getCollection(FACTURES);
    	
    	ArrayList<List<String>> factures = new ArrayList<>();
    	MongoCursor<Document> cursor = collection.find().iterator();
    	
    	try {
    	    while (cursor.hasNext()) {
    	    	Document doc = cursor.next();
    	    	if(doc.containsKey("facture")) {
    	    		ArrayList<Document> p = (ArrayList<Document>) doc.get("facture");
    	    		ArrayList<String> produits = new ArrayList<>();
    	    		for(Document el: p) {
    	    			produits.add(el.getString("produit"));
    	    		}
    	    		factures.add(produits);
    	    	}
    	    }
    	} finally {
    	    cursor.close();
    	}
    	
    	mongoClient.close();
    	
    	File data = new File("data.txt");
    	
    	try {
			data.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(data));
			
			for(List<String> list: factures) {
				for(String produit: list) {
					writer.write(produit + " ");
				}
				writer.write("\n");
			}
			writer.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	Properties prop = new Properties();
    	try {
			InputStream propStream = new FileInputStream(new File(PROPERTIES));
			prop.load(propStream);
		} catch (IOException e1) {
			e1.printStackTrace();
			return "Error: " + e1.getMessage();
		}
    	
    	
    	SparkLauncher sl = new SparkLauncher()
    			.setSparkHome(prop.getProperty("SPARK_HOME"))
    			.setAppResource(prop.getProperty("FREQUENCY_JAR"))
    			.setMainClass("tp4.FrequencyJob")
    			.setMaster(prop.getProperty("SPARK_MASTER"))
    			.addAppArgs(data.getAbsolutePath(), prop.getProperty("SPARK_MASTER"));
    	
    	Process job;
    	String content = "Error";
    	try {
			job = sl.launch();
			job.waitFor();
			String line;
			content = new Scanner(new File("result.txt")).useDelimiter("\\Z").next();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
    	
    	return content;
    }
}
