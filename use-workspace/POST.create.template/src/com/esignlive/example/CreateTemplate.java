package com.esignlive.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.Placeholder;
import com.silanis.esl.sdk.Visibility;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

public class CreateTemplate
  {
  public static final String DOCUMENT_ONE = "/home/john/Documents/OSS/sampleAgreement.pdf";
  public static final String TEMPLATE_NAME = "My java API Template";
  public static final String PACKAGE_TITLE = "VM: CreateTemplate";
	
  public static void main(String[] args) throws IOException
    {
	String env = "EU.PROD";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
		    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));
	  
    DocumentPackage documentPackage = PackageBuilder.newPackageNamed("Template for 2 via Java SDK")
        .withVisibility(Visibility.ACCOUNT)
//      .withVisibility(Visibility.SENDER)
        .withSigner(SignerBuilder.newSignerPlaceholder(new Placeholder("Signer1")))
        .withSigner(SignerBuilder.newSignerPlaceholder(new Placeholder("Signer2")))
        .withDocument(DocumentBuilder.newDocumentWithName(TEMPLATE_NAME)
        	.fromFile(DOCUMENT_ONE)
        .withSignature(SignatureBuilder.signatureFor(new Placeholder("Signer1"))
            .onPage(0)
            .atPosition(100, 140)
            )
      .withSignature(SignatureBuilder.signatureFor(new Placeholder("Signer2"))
          .onPage(0)
          .atPosition(100, 205))
            )
        .build();
		         
    PackageId templateId = eslClient.getTemplateService().createTemplate(documentPackage);

    System.out.println("{\n" + templateId + "\n}");
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