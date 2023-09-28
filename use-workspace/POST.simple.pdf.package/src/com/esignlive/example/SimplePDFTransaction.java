package com.esignlive.example;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.ExtractionType;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.builder.CeremonyLayoutSettingsBuilder;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.DocumentPackageSettingsBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;
import com.silanis.esl.sdk.internal.EslServerException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SimplePDFTransaction
  {
  public static final String[] DOCUMENT_ONE = {"sampleAgreement.pdf", "/home/john/Documents/OSS/caseTT.pdf"};
  public static final String PACKAGE_TITLE = "VM: SimplePDF";
  
  public static void main(String[] args) throws IOException, EslServerException
    {
	String env = "EU.PROD";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
	    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Build the DocumentPackage object
    DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .withSigner(SignerBuilder.newSignerWithEmail("john.cyclist.mcguinness+simple-pdf@gmail.com")
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Signer_one")
            .withCompany("One")
            .withCustomId("robin")
            )
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0])
        	.fromFile(DOCUMENT_ONE[1])
            .enableExtraction()
            .withExtractionType(ExtractionType.TEXT_TAGS)
            )
        .withSettings(DocumentPackageSettingsBuilder.newDocumentPackageSettings()
            .withoutLanguageDropDown()
            .withoutWatermark()
            .withRemainingDays(2)
            .withCeremonyLayoutSettings(CeremonyLayoutSettingsBuilder.newCeremonyLayoutSettings()
            .withNavigator()
                .withGlobalNavigation()
                .withSessionBar()
                .withTitle()
                .withProgressBar()
                .withoutGlobalDownloadButton()
                )
            )
        .withEmailMessage(PACKAGE_TITLE)
        .build();
    
    PackageId packageId = eslClient.createPackageOneStep(pkg);
    // Send the package to be signed by the participants
    eslClient.sendPackage(packageId);
    System.out.println("{\n" + packageId + "\n}");
    
    String signingUrlOne = eslClient.getPackageService().getSigningUrl(packageId, "robin");
    System.out.println("Link for Signer 1:\n" + signingUrlOne);
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