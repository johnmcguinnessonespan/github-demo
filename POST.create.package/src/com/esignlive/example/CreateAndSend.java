package com.esignlive.example;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.EslException;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.builder.CeremonyLayoutSettingsBuilder;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.DocumentPackageSettingsBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;
import com.silanis.esl.sdk.internal.EslServerException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class CreateAndSend
  {
  public static final String[] DOCUMENT_ONE = {"sampleAgreement.pdf", "/home/john/Documents/OSS/sampleAgreement.pdf"};
  public static final String PACKAGE_TITLE = "VM: CreationAndSend";
  
  public static void main(String[] args) throws IOException, EslServerException
    {
	String env = "EU.PROD";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
	    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Build the DocumentPackage object
    DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .withSigner(SignerBuilder.newSignerWithEmail("john.cyclist.mcguinness+signer1@gmail.com")
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Signer_one")
            .withCompany("One")
            .withCustomId("Signer")
            .signingOrder(0)
            .withEmailMessage("You can go first")
            )
        .withSigner(SignerBuilder.newSignerWithEmail("john.cyclist.mcguinness+signer2@gmail.com")
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Signer_two")
            .withCompany("Two")
            .withCustomId("SignerTwo")
            .signingOrder(1)
            .withLanguage(Locale.FRENCH)
            )
        .withSigner(SignerBuilder.newSignerWithEmail("john.cyclist.mcguinness+signer3@gmail.com")
                .withFirstName(prop.getProperty("FORENAME"))
                .withLastName("Signer_three")
                .withCompany("Two")
                .withCustomId("SignerThree")
                .signingOrder(2)
                .withLanguage(Locale.SIMPLIFIED_CHINESE)
                .withEmailMessage("You can go last")
                )
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1])
            .withSignature(SignatureBuilder.signatureFor("john.cyclist.mcguinness+signer1@gmail.com").onPage(0).atPosition(175, 150))
            .withSignature(SignatureBuilder.captureFor("john.cyclist.mcguinness+signer1@gmail.com").onPage(0).atPosition(175, 250))
            .withSignature(SignatureBuilder.initialsFor("john.cyclist.mcguinness+signer1@gmail.com").onPage(0).atPosition(175, 350))
            .withSignature(SignatureBuilder.signatureFor("john.cyclist.mcguinness+signer2@gmail.com").onPage(0).atPosition(175, 450))
            .withSignature(SignatureBuilder.captureFor("john.cyclist.mcguinness+signer2@gmail.com").onPage(0).atPosition(175, 550))
            .withSignature(SignatureBuilder.initialsFor("john.cyclist.mcguinness+signer2@gmail.com").onPage(0).atPosition(175, 950))
            .withSignature(SignatureBuilder.signatureFor("john.cyclist.mcguinness+signer3@gmail.com").onPage(0).atPosition(175, 650))
            .withSignature(SignatureBuilder.captureFor("john.cyclist.mcguinness+signer3@gmail.com").onPage(0).atPosition(175, 750))
            .withSignature(SignatureBuilder.initialsFor("john.cyclist.mcguinness+signer3@gmail.com").onPage(0).atPosition(175, 850))
            )
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1]))
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1]))
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1]))
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1]))
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1]))
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1]))
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1]))
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1])) 
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1])
            .withSignature(SignatureBuilder.signatureFor("john.cyclist.mcguinness+signer1@gmail.com").onPage(0).atPosition(175, 150))
            .withSignature(SignatureBuilder.captureFor("john.cyclist.mcguinness+signer1@gmail.com").onPage(0).atPosition(175, 250))
            .withSignature(SignatureBuilder.initialsFor("john.cyclist.mcguinness+signer1@gmail.com").onPage(0).atPosition(175, 350))
            .withSignature(SignatureBuilder.signatureFor("john.cyclist.mcguinness+signer2@gmail.com").onPage(0).atPosition(175, 450))
            .withSignature(SignatureBuilder.captureFor("john.cyclist.mcguinness+signer2@gmail.com").onPage(0).atPosition(175, 550))
            .withSignature(SignatureBuilder.initialsFor("john.cyclist.mcguinness+signer2@gmail.com").onPage(0).atPosition(175, 950))
            .withSignature(SignatureBuilder.signatureFor("john.cyclist.mcguinness+signer3@gmail.com").onPage(0).atPosition(175, 650))
            .withSignature(SignatureBuilder.captureFor("john.cyclist.mcguinness+signer3@gmail.com").onPage(0).atPosition(175, 750))
            .withSignature(SignatureBuilder.initialsFor("john.cyclist.mcguinness+signer3@gmail.com").onPage(0).atPosition(175, 850))
            )
        .withSettings(DocumentPackageSettingsBuilder.newDocumentPackageSettings()
//            .withInPerson()
//            .withoutOptOut()
//            .withOptOutReason("Decline terms.")
            .withoutLanguageDropDown()
//            .hideOwnerInPersonDropDown()
//            .withoutOptOutOther()
//	          .disableFirstAffidavit()
//	          .disableSecondAffidavit()
//            .withoutDecline()
            .withoutWatermark()
            .withRemainingDays(2)
//            .withCaptureText()
            .withHandOverLinkHref("http://www.google.com")
            .withHandOverLinkText("Exit to site")
            .withHandOverLinkTooltip("GO GOOGLE")
//            .withoutDocumentToolbarDownloadButton()
//            .withDialogOnComplete()
            .withCeremonyLayoutSettings(CeremonyLayoutSettingsBuilder.newCeremonyLayoutSettings()
            .withNavigator()
                .withGlobalNavigation()
//                .withoutBreadCrumbs()
                .withSessionBar()
                .withTitle()
                .withProgressBar()
                .withoutGlobalDownloadButton()
//                .withoutGlobalSaveAsLayoutButton()
//                .withLogoSource("http://www.logo-maker.net/images/common/company-logo8.gif")
                )
            )
        .withEmailMessage(PACKAGE_TITLE)
        .build();
    
    // Issue the request to the e-SignLive server to create the DocumentPackage
//    try
//      {
      PackageId packageId = eslClient.createPackageOneStep(pkg);
//      }
//    catch (EslServerException serverException)
//      {
//      System.out.println( "The server could not complete the request." );
//      System.out.println( serverException.getMessage() );
//      System.out.println( "HTTP code: " + serverException.getServerError().getCode());
//      System.out.println( "Server message: " + serverException.getServerError().getMessage());
//      }
//    catch (EslException exception)
//      {
//      System.out.println( exception.getMessage() );
//      System.out.println( exception.getCause().getMessage() );
//      }
    
    // Send the package to be signed by the participants
    eslClient.sendPackage(packageId);
    System.out.println("{\n" + packageId + "\n}");
    
    String signingUrlOne = eslClient.getPackageService().getSigningUrl(packageId, "Signer");
    System.out.println("Link for Signer 1:\n" + signingUrlOne);
    String signingUrlTwo = eslClient.getPackageService().getSigningUrl(packageId, "SignerTwo");
    System.out.println("Link for Signer 2:\n" + signingUrlTwo);
    String signingUrlThree = eslClient.getPackageService().getSigningUrl(packageId, "SignerThree");
    System.out.println("Link for Signer 3:\n" + signingUrlThree);
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