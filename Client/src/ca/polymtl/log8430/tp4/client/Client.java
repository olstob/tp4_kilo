package ca.polymtl.log8430.tp4.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Client {
	
	// Fonction d'exécution du client
	public static void main(String[] args) {
		if (args.length < 3 || args.length > 3) {
			System.out.println("Nombre d'arguments pour la requête invalide");
		}
		else {
			String url = args[0]; 
			String requestType = args[1];
			String fileName = args[2];
			
			Client client = new Client();
			
			// Validation des arguments
			boolean isValid = client.validateArgs(url, requestType, fileName);
			
			// Exécution de la requête
			if (!isValid) {
				System.out.println("Argument(s) invalide(s). Veuillez réessayer.");
			}
			else {
				client.executeRequest(url, requestType, fileName);
			}
		}
	}
	
	// Constructeur par défaut
	public Client() {
		super();
	}

	private boolean validateArgs(String url, String requestType, String fileName) {
		// Vérification du type de requête
		if (requestType.toLowerCase().equals("get") && requestType.toLowerCase().equals("post")) {
			return false;
		}
		
		// Vérification du fileName
		if (fileName.length() < 6) {
			return false;
		}
		
		// Vérification du url
		if (!url.contains("http://")) {
			return false;
		}
		return true;
	}
	
	private void executeRequest(String link, String requestType, String fileName) {
		final String USER_AGENT = "Mozilla/5.0";
		
		URL url;
		HttpURLConnection http;
	
		
		if (requestType.toLowerCase().equals("get")) {
			
			try {
				// Steps:
				// 1. Envoyer la requête au serveur
				url = new URL(link);
				http = (HttpURLConnection) url.openConnection();
				
				http.setRequestMethod("GET");
				http.setRequestProperty("User-Agent", USER_AGENT);
				
				// 2. Handle la réponse
				// int responseCode = http.getResponseCode();
				
				// 3. Écrire la data dans le fileName
				BufferedReader input = new BufferedReader(new InputStreamReader(http.getInputStream()));
				writeDataToJSONFile(input, fileName);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (requestType.toLowerCase().equals("post")) {
			
			try {
				// Steps:
				// 1. Récupérer le contenu du fichier fileName
				String content = retrieveJSONFileContent(fileName);
				
				// 2. Envoyer au serveur
				url = new URL(link);
				http = (HttpURLConnection) url.openConnection();
				
				http.setRequestMethod("POST");
				http.setRequestProperty("User-Agent", USER_AGENT);
				
				// 3. Handle la réponse
				http.setDoOutput(true);
				DataOutputStream writer = new DataOutputStream(http.getOutputStream());
				writer.writeBytes(content);
				writer.flush();
				writer.close();	
				
				//int responseCode = http.getResponseCode()
				
				BufferedReader input = new BufferedReader(new InputStreamReader(http.getInputStream()));
				String line;
				StringBuffer buffer = new StringBuffer();
				
				while ((line = input.readLine()) != null) {
					buffer.append(line);
				}
				
				input.close();
				
				System.out.println(buffer.toString());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String retrieveJSONFileContent(String fileName) {
		String result = "";
		try 
		{
			// Obtention du path du fichier local
			String userFile = fileName;
			
			result = new String(Files.readAllBytes(Paths.get(userFile)));
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	private void writeDataToJSONFile(BufferedReader input, String fileName) {
		String line;
		StringBuffer buffer = new StringBuffer();
		
		try {
			while((line = input.readLine()) != null) {
				buffer.append(line);
			}
			
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.println(buffer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
