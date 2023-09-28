package com.esignlive.example;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.DocumentType;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;

import static com.silanis.esl.sdk.builder.DocumentBuilder.newDocumentWithName;
import static com.silanis.esl.sdk.builder.PackageBuilder.newPackageNamed;
import static com.silanis.esl.sdk.builder.SignatureBuilder.captureFor;
import static com.silanis.esl.sdk.builder.SignerBuilder.newSignerWithEmail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class CaptureTransaction
  {
  public static final String FILE_PATH = "/home/john/Documents/OSS/sampleAgreement.pdf";
  public static final String PACKAGE_TITLE = "VM: CapturePackage";
	
  public static void main(String[] args) throws IOException
    {
	String env = "EU.PROD";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
		    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));
  
    DocumentPackage pkg = newPackageNamed("Capture me") 
        .describedAs("This is a package created using the eSignLive SDK")
            .withSigner(newSignerWithEmail("john.cyclist.mcguinness+capture@gmail.com")
                .withFirstName("John")
                .withLastName("Capture")
                .withCustomId("Capture"))
            .withDocument(newDocumentWithName("First Document")
            .fromStream(new FileInputStream(FILE_PATH), DocumentType.PDF)
            .withSignature(captureFor("john.cyclist.mcguinness+capture@gmail.com")
                .onPage(0)
                .atPosition(300, 200)
                .setFromFile(true))
            )
        .build();

    PackageId packageId = eslClient.createPackageOneStep(pkg);
    eslClient.sendPackage(packageId);
    
    System.out.println("{\n" + packageId + "\n}");
    String signingUrlCapture = eslClient.getPackageService().getSigningUrl(packageId, "Capture");
    System.out.println("Link for Signer 1:\n" + signingUrlCapture);
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
