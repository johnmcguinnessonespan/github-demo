package com.esignlive.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

public class PostmanTransaction
  {
  public static final String DOCUMENT_ONE = "/home/john/Documents/OSS/sampleAgreement.pdf";
//  public static final String PACKAGE_TITLE = "CreatePackage via REST: " + LocalDateTime.now();

  public static void main(String[] args) throws MalformedURLException, IOException
    {
	String env = "CA.SBX";  
    String charset = "UTF-8";
    File uploadFile1 = new File(DOCUMENT_ONE);
    String boundary = Long.toHexString(System.currentTimeMillis());
    String CRLF = "\r\n";

    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");

    // 1 signer
    String jsonContent = "{ \"roles\": [{ \"data\": { \"localLanguage\": \"local\" }, \"reassign\": true, \"emailMessage\": { \"content\": \"Transaction for one Signer!\" }, \"id\": \"SignerOne\", \"signers\": [{ \"email\": \"john.cyclist.mcguinness+sOne@gmail.com\", \"firstName\": \"John\", \"lastName\": \"McGuinness\", \"id\": \"SignerOne\" }], \"type\": \"SIGNER\" }], \"documents\": [{ \"approvals\": [{ \"id\": \"ExampleSignatureId\", \"role\": \"SignerOne\", \"fields\": [{ \"page\": 0, \"top\": 200, \"subtype\": \"LABEL\", \"height\": 50, \"left\": 100, \"width\": 200, \"id\": \"myLabelField\", \"type\": \"INPUT\", \"value\": \"Example with input field\" }, { \"page\": 0, \"top\": 100, \"subtype\": \"FULLNAME\", \"height\": 50, \"left\": 100, \"width\": 200, \"type\": \"SIGNATURE\", \"name\": \"ExampleSignatureId\" } ] }], \"name\": \"SignatureDoc\" }], \"name\": \"1 Signer and input transaction\", \"senderVisible\": false, \"due\": \"2024-01-01T14:00:00Z\", \"status\": \"SENT\" }";

    HttpsURLConnection connection = null;
    URL url = new URL(prop.getProperty(env +".URL") + "/packages");
    connection = (HttpsURLConnection) url.openConnection();
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" +boundary);
    connection.setRequestProperty("Authorization", "Basic " + prop.getProperty(env +".API"));
    connection.setRequestProperty("Accept", "application/json; esl-api-version=11.0");
    OutputStream output = connection.getOutputStream();

    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

    try
      {
      // Add pdf file.
      writer.append("--" + boundary).append(CRLF);
      writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + uploadFile1.getName() + "\"").append(CRLF);
      writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(uploadFile1.getName())).append(CRLF);
      writer.append("Content-Transfer-Encoding: application/pdf").append(CRLF);
      writer.append(CRLF).flush();

      Files.copy(uploadFile1.toPath(), output);
      output.flush();

      writer.append(CRLF).flush();
      // add json payload
      writer.append("--" + boundary).append(CRLF);
      writer.append("Content-Disposition: form-data; name=\"payload\"").append(CRLF);
      writer.append("Content-Type: application/json; charset=" + charset).append(CRLF);
      writer.append(CRLF).append(jsonContent).append(CRLF).flush();
      // End of multipart/form-data.
      writer.append("--" + boundary + "--").append(CRLF).flush();
      }
    catch (IOException ex)
      {
      System.err.println(ex);
      }

    // get and write out response code
    int responseCode = ((HttpURLConnection) connection).getResponseCode();
    System.out.println(responseCode);

    if(responseCode == 200)
      {
      // get and write out response
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null)
        {
        response.append(inputLine);
        }

        in.close();

        System.out.println(response.toString());
        }
      else
        {
        // get and write out response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
          {
          response.append(inputLine);
          }

      in.close();

      System.out.println(response.toString());
      }
    }
  
  public static Properties readPropertiesFile(String fileName) throws IOException
    {
    FileInputStream fis = null;
    Properties prop = null;

    try
      {
      fis = new FileInputStream(fileName);
      prop = new Properties();
      prop.load(fis);
      }
    catch(FileNotFoundException fnfe)
      {
      fnfe.printStackTrace();
      }
    catch(IOException ioe)
      {
      ioe.printStackTrace();
      }
    finally
      {
      fis.close();
      }

    return prop;
    }
  }