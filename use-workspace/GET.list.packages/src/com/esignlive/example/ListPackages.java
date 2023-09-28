package com.esignlive.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Sets;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageStatus;
import com.silanis.esl.sdk.Page;
import com.silanis.esl.sdk.PageRequest;

public class ListPackages
  {
  public static final String PACKAGE_TITLE = "VM: ListPackages";
	  
  public static void main(String[] args) throws IOException	
    {
    String env = "CA.PROD";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
		    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL")); 
	
    PageRequest pageRequest = new PageRequest(1, 100);
    Boolean hasNext = false;
	
    do
      {
      Page<Map<String, String>> packages = eslClient.getPackageService().getPackagesFields(PackageStatus.COMPLETED, pageRequest, Sets.newHashSet("id"));
      hasNext = packages.hasNextPage();
      pageRequest = pageRequest.next();
	     
      List<Map<String,String>> results = packages.getResults();
	
      System.out.println("\nList of completed transactions in " + env + ":");
      
      for (Map<String, String> map : results)
        {
        String packageId = map.get("id");
        System.out.println(" -\t" + packageId);
        }
      } while (hasNext);
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