package com.esignlive.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.DocumentType;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.DocumentPackageSettingsBuilder;
import com.silanis.esl.sdk.builder.FieldBuilder;
import com.silanis.esl.sdk.builder.FieldValidatorBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

public class ValidationPackage
  {
  public static final String DOCUMENT_ONE = "/home/john/Documents/OSS/sample_field_validators.pdf";
  public static final String PACKAGE_TITLE = "VM: ValidationPackage";
  
  public static void main(String[] args) throws IOException
    {
	String env = "EU.PROD";
	Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
		   
	EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

	InputStream fs = new FileInputStream(DOCUMENT_ONE);          // package definition

	DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .withSettings(DocumentPackageSettingsBuilder.newDocumentPackageSettings()
            .withDefaultTimeBasedExpiry()
            .withRemainingDays(1)
            )
        .withSigner(SignerBuilder.newSignerWithEmail("john.cyclist.mcguinness+validation@gmail.com")
            .withFirstName("John")
            .withLastName("Validation")
            .withCustomId("Client")
            .signingOrder(0)
            )
//          .withLanguage(Locale.TRADITIONAL_CHINESE)
//      .withSigner(SignerBuilder.newSignerWithEmail("john.cyclist.mcguinness+validator2@gmail.com")
//          .withFirstName("Signer2")
//          .withLastName("Validation")
//          .withCustomId("Manager").signingOrder(1))
        .withDocument(DocumentBuilder.newDocumentWithName("Contract").fromStream(fs, DocumentType.PDF).enableExtraction()
//      .withSignature(SignatureBuilder.captureFor("john.cyclist.mcguinness+validator2@gmail.com")
//          .withName("manager_signature")
//          .withPositionExtracted()
//          .withPositionExtracted()))
        .withSignature(SignatureBuilder.signatureFor("john.cyclist.mcguinness+validation@gmail.com")
            .withName("client_signature").withPositionExtracted()
            .withField(FieldBuilder.signatureDate()
            .withName("client_date").withPositionExtracted())
        .withField(FieldBuilder.textField()
            .withName("forename").withPositionExtracted()
            .withValidation(FieldValidatorBuilder.alphabetic().required().withErrorMessage("Please enter a valid first name."))
            )
        .withField(FieldBuilder.textField()
            .withName("surname").withPositionExtracted()
            .withValidation(FieldValidatorBuilder.alphabetic().required().withErrorMessage("Please enter a valid last name."))
            )
        .withField(FieldBuilder.textField()
            .withName("address").withPositionExtracted()
            .withValidation(FieldValidatorBuilder.basic().required().withErrorMessage("Please enter a valid address."))
   	        )
        .withField(FieldBuilder.textField()
            .withName("town").withPositionExtracted()
            .withValidation(FieldValidatorBuilder.alphabetic().required().withErrorMessage("Please enter a valid town."))
            )
//      .withField(FieldBuilder.textField()
//        .withName("state").withPositionExtracted()
//        .withValidation(FieldValidatorBuilder.alphabetic().required().maxLength(2).minLength(2).withErrorMessage("Please enter a valid state."))
//          )
        .withField(FieldBuilder.textField()
            .withName("postcode").withPositionExtracted()
            .withValidation(FieldValidatorBuilder.regex("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$").required().withErrorMessage("Please enter a valid post code."))
            )
//        .withField(FieldBuilder.textField()
//            .withName("country").withPositionExtracted()
//            .withValidation(FieldValidatorBuilder.alphabetic().required().withErrorMessage("Please enter a valid country."))
//            )
//      .withField(FieldBuilder.textField()
//        .withName("phone_number").withPositionExtracted()
//        .withValidation(FieldValidatorBuilder.regex("^[2-9]\\d{2}-\\d{3}-\\d{4}$").required().withErrorMessage("Please enter a valid phone number (XXX-XXX-XXXX)"))
//           )
        .withField(FieldBuilder.textField()
            .withName("email").withPositionExtracted()
            .withValidation(FieldValidatorBuilder.email().required().withErrorMessage("Please enter a valid email."))
            )
        .withField(FieldBuilder.textField()
            .withName("company").withPositionExtracted()
            .withValidation(FieldValidatorBuilder.alphabetic().required().withErrorMessage("Please enter a valid company."))
            )
//        .withField(FieldBuilder.textField()
//      .withName("policy_number").withPositionExtracted()
        //.withValidation(FieldValidatorBuilder.numeric().required().withErrorMessage("Please enter a valid policy number."))
            ))
//        )
        .build();
    PackageId packageId = eslClient.createAndSendPackage(pkg);
    
    System.out.println("{\n" + packageId + "\n}");
    String signingUrlClient = eslClient.getPackageService().getSigningUrl(packageId, "Client");
    System.out.println("Link for Client:\n" + signingUrlClient);
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