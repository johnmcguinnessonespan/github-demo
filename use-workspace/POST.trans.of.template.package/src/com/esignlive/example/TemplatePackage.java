package com.esignlive.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.text.SimpleDateFormat;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.Placeholder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

public class TemplatePackage
  {
  public PackageId templateId;
  public static final String PLACEHOLDER_ID1 = "PlaceholderId1";
  public static final String PLACEHOLDER_ID2 = "PlaceholderId2";
  public static final String PACKAGE_TITLE = "VM: UsingTemplatePackage";

  public static void main(String[] args) throws IOException
    {
	String env = "CA.SBX";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
			    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));
    
    PackageId templateId = new PackageId("_e3X7laTA98dHBf3pU9fSHnEpdI=");    //  U3YIClR16PqZaBIla2eiFwk1mww=");

    DocumentPackage pkg = PackageBuilder.newPackageNamed("Transaction via a template")
        .describedAs("PACKAGE_DESCRIPTION")
            .withSigner(SignerBuilder.newSignerWithEmail("john.cyclist.mcguinness+placeholder1@gmail.com")
            .withFirstName("John")
            .withLastName("TemplateOne")
            .withCustomId(PLACEHOLDER_ID1)
            .replacing(new Placeholder(PLACEHOLDER_ID1))
            )
            .withSigner(SignerBuilder.newSignerWithEmail("john.cyclist.mcguinness+placeholder2@gmail.com")
            .withFirstName("John")
            .withLastName("TemplateTwo")
            .withCustomId(PLACEHOLDER_ID2)
            .replacing(new Placeholder(PLACEHOLDER_ID2))
            )
        .build();
    
    // Issue the request to the e-SignLive server to create the DocumentPackage    
	PackageId packageId = eslClient.getTemplateService().createPackageFromTemplate(templateId, pkg);

	// Send the package to be signed by the participants
    eslClient.sendPackage(packageId);
	
    System.out.println("{\n" + packageId + "\n}");
    String signingUrlPlaceholder1 = eslClient.getPackageService().getSigningUrl(packageId, PLACEHOLDER_ID1);
    System.out.println("Link for Placeholder1:\n" + signingUrlPlaceholder1);
    String signingUrlPlaceholder2 = eslClient.getPackageService().getSigningUrl(packageId, PLACEHOLDER_ID2);
    System.out.println("Link for Placeholder2:\n" + signingUrlPlaceholder2);

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