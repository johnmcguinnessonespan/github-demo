package com.esignlive.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;

public class TemplateOfPackage
  {
  public static final String DOCUMENT_ONE = "/home/john/Documents/OSS/sampleAgreement.pdf";
  public static final String TEMPLATE_NAME = "My API Template of Pkg3";
  public static final String PACKAGE_TITLE = "VM: TemplateFromPkg";
	
  public static void main(String[] args) throws IOException
    {
	String env = "CA.SBX";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
		    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));
	  
    PackageId packageId = new PackageId("JHIUmODmB3JRBh2VdZGoNS7qmOQ=");
    PackageId templateFromPackage = eslClient.getTemplateService()
    	.createTemplateFromPackage(packageId, TEMPLATE_NAME);
    
    System.out.println("New template creation from a transaction Complete");
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