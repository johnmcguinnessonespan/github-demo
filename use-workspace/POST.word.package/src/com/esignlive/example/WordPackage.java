package com.esignlive.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.ExtractionType;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.DocumentPackageSettingsBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

public class WordPackage
  {
  public static final String DOCUMENT_ONE = "/home/john/Documents/OSS/sample_contract_text_tags.docx";
  public static final String PACKAGE_TITLE = "VM: WordPackage";

  public static void main(String[] args) throws IOException
    {
	String env = "AU.PROD";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
		    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .withSettings(DocumentPackageSettingsBuilder.newDocumentPackageSettings()
            .withDefaultTimeBasedExpiry()
            .withRemainingDays(1)
            )
        .withSigner(SignerBuilder.newSignerWithEmail("mc_cyclist@hotmail.com")
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Hotmail")
            .withCustomId("Signer1")
            )
        .withDocument(DocumentBuilder.newDocumentWithName("Using Text Tags")
            .fromFile(DOCUMENT_ONE)
            .enableExtraction()
            .withExtractionType(ExtractionType.TEXT_TAGS)
            )
        .autocomplete(false)
        .withEmailMessage("Created with .docx file")
    .build();

    PackageId packageId = eslClient.createAndSendPackage(pkg);

    System.out.println("{\n" + packageId + "\n}");
    String signingUrlOne = eslClient.getPackageService().getSigningUrl(packageId, "Signer1");
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