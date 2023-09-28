package com.esignlive.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;

public class ConsentTransaction
  {
  public static final String DOCUMENT_ONE = "/home/john/Documents/OSS/sampleAgreement.pdf";
  public static final String DOCUMENT_TWO = "/home/john/Documents/OSS/sampleAgreement.pdf";
  public static final String PACKAGE_TITLE = "ConsentTransaction Title";

  public static void main(String[] args) throws IOException
    {
    String env = "EU.PROD";

    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
			    
	EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    DocumentPackage pkd = newPackageNamed("Consent me")
                .describedAs("This is a package created using the eSignLive SDK")
                .withSigner( newSignerWithEmail("john.cyclist.mcguinness+approve@gmail.com")
                        .withFirstName("John")
                        .withLastName("Approve")
                        )
                .withSigner( newSignerWithEmail("john.mcguinness+sign@skiff.com")
                        .withFirstName("John")
                        .withLastName("Signer")
                        )
                .withDocument(newDocumentWithName("First Document")
                        .fromStream(new FileInputStream(DOCUMENT_ONE), DocumentType.PDF)
                        .withSignature( acceptanceFor("john.cyclist.mcguinness+approve@gmail.com"))
                        )
                .withDocument(newDocumentWithName("Second Document")
                        .fromStream(new FileInputStream(DOCUMENT_TWO), DocumentType.PDF)
                        .withSignature( signatureFor("john.cyclist.mcguinness+approve@gmail.com")
                                .onPage(0)
                                .atPosition(100, 100))
                        )
                .build();

        Package packageId = eslClient.createPackage(pkg);
        eslClient.sendPackage(packageId);
//        retrievedPackage = eslClient.getPackage( packageId );	// TODO Auto-generated method stub
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
