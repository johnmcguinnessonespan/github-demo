package com.esignlive.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.joda.time.LocalDateTime;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.PackageStatus;
import com.silanis.esl.sdk.SigningStatus;
import com.silanis.esl.sdk.io.Files;

public class StatusAndDownload
  {
  public static final String PACKAGE_TITLE = "SCPAID: API package: ";
  public static final String TRANSACTION = "XsriwLFJC9lgaZVnHFBa_tFZmkM%3D";

  public static void main(String[] args) throws IOException
    {
    String env = "AU.PROD";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
				    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // define the package
    PackageId packageId = new PackageId(TRANSACTION);
    DocumentPackage sentPackage = eslClient.getPackage(packageId);

    // check and print out status
    PackageStatus status = sentPackage.getStatus();
    System.out.println(status);
    SigningStatus sentSigningStatus = eslClient.getSigningStatus(packageId, null, null);
    System.out.println(sentSigningStatus.getToken());

    if(!sentSigningStatus.getToken().equals("COMPLETED"))
      {
      System.out.println("Cannot download: signing incomplete");
      }
    else
      {
      // download zip file	
      byte[] documentZip = eslClient.downloadZippedDocuments(packageId);
      Files.saveTo(documentZip, "documentZip.zip");
      System.out.println("Document zip file downloaded");

      // download evidence material
      byte[] evidenceSummary = eslClient.downloadEvidenceSummary(packageId);
      Files.saveTo(evidenceSummary, "evidenceSummary.pdf");
      System.out.println("Evidence Summary Downloaded");
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