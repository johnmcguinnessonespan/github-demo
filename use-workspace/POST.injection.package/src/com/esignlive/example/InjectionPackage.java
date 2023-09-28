package com.esignlive.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.builder.*;
import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.DocumentType;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;

public class InjectionPackage
  {
  public static final String DOCUMENT_ONE = "/home/john/Documents/OSS/sample_injection.pdf";
  public static final String PACKAGE_TITLE = "VM: InjectionPackage";

  public static void main(String[] args) throws IOException, FileNotFoundException
    {
    String env = "EU.PROD";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    DocumentPackage pkg = PackageBuilder.newPackageNamed("Field Injection Example")
        .withSigner(SignerBuilder.newSignerWithEmail("mc_cyclist@yahoo.co.uk")
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("mc_cyclist")
            .withCustomId("Client")
            .signingOrder(0))
        .withSigner(SignerBuilder.newSignerWithEmail("john.cyclist.mcguinness@gmail.com")
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("gmail")
            .withCustomId("Contractor")
            .signingOrder(1))
        .withDocument(DocumentBuilder.newDocumentWithName("Sample Contract")
            .enableExtraction()
            .fromStream(new FileInputStream(DOCUMENT_ONE), DocumentType.PDF)
            .withSignature(SignatureBuilder
                .signatureFor("mc_cyclist@yahoo.co.uk")
                .onPage(0)
                .atPosition(120, 730))
            .withSignature(SignatureBuilder
                .signatureFor("john.cyclist.mcguinness@gmail.com")
                .onPage(0)
                .atPosition(120, 820))
//                )
          .withInjectedField(FieldBuilder.checkBox()
              .withName("Text1")
              .withValue(true))
          .withInjectedField(FieldBuilder.textField()
              .withName("Text2")
              .withValue("Lawn mower"))
          .withInjectedField(FieldBuilder.textField()
              .withName("Text3")
              .withValue("Fertilizer"))
          .withInjectedField(FieldBuilder.textField()
              .withName("Text4")
              .withValue("100"))
          .withInjectedField(FieldBuilder.textField()
              .withName("Text5")
              .withValue("12   12   12")))
            .build();

    PackageId packageId = eslClient.createAndSendPackage(pkg);

    System.out.println("{\n" + packageId + "\n}");
    String signingUrlClient = eslClient.getPackageService().getSigningUrl(packageId, "Client");
    System.out.println("Link for Client:\n" + signingUrlClient);
    String signingUrlContractor = eslClient.getPackageService().getSigningUrl(packageId, "Contractor");
    System.out.println("Link for Contractor:\n" + signingUrlContractor);
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