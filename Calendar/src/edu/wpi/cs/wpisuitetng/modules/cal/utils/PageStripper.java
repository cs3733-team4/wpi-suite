/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * In order to use this page stripper, point the "path" variable to the locatino of the documentation, and the "pathOut" to the output location.
 * Run this file as a standalone program
 * @author Team YOCO
 *
 */
public class PageStripper {
	static String backColor = "FFFFFF";
	
	static String path = "C:/Users/Brendan/Desktop/Build html documentation/"; 
	static String pathOut = "C:/Users/Brendan/Desktop/Test Out/";
	public static void strip(String dIn, String dOut, String name, boolean toc)
	{
		String holdLine;
		int starts=0;
		try {
			BufferedReader read = new BufferedReader(new FileReader(dIn+name));
			BufferedWriter write = new BufferedWriter(new FileWriter(dOut+name));
			while (true)
			{
				holdLine=read.readLine();
				if (holdLine==null)
					break;
				if (holdLine=="")
					continue;
				
				if (toc)
				{
					if (holdLine.contains("background"))
					{
						System.err.println("Editingt TOC!");
						continue;
					}
				}
				else{
					//skips the watermark on the bottom
					if (holdLine.contains("Created with the Personal Edition"))
					{
	
						//System.err.println("Skipped: "+ holdLine );
						continue;
					}
					
					if (holdLine.contains("topic_header_text"))
						continue;
					if (holdLine.contains("<body>"))
					{
	
						//System.err.println("Altering: "+ holdLine );
						holdLine="<body style=\"background-color:#" + backColor + "\">";
					}
					if (holdLine.contains("background-color: #EFEFEF;"))
					{
	
						//System.err.println("Altering: "+ holdLine );
						holdLine="background-color:#" + backColor + ";";
					}
					if (holdLine.contains("topic_footer"))
					{
	
						//System.err.println("Altering: "+ holdLine );
						holdLine="<div id=\"topic_footer\" style=\"background-color:#" + backColor + "\">";
					}
					
					
					/*if (holdLine.contains("<img"))
					{
						holdLine=holdLine.replace("\"padding : 1px;\"", "\"padding : 1px; border-style:solid; border-width:3px;\"");
						System.err.println("Adding Border: " + holdLine);
					}*/
					//This section removes the thing on the left
					if (holdLine.contains("<script"))
					{
						starts++;
						//System.err.println("Start on: " + holdLine);
	
					}
					if (holdLine.contains("/script"))
					{
						starts--;
	
						//System.err.println("End on: " + holdLine);
						continue;
					}
					if(starts!=0)
					{
	
						//System.err.println("Skipped: (" +starts+") "+ holdLine );
						continue;
					}
				}
				//System.out.println(holdLine);
				write.write(holdLine);
				write.newLine();
			}
		
			write.close();
		
		
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void strip(String dIn, String dOut, String name)
	{
		strip(dIn, dOut, name, false);
		
	}
public static void copyDirectory(File sourceDir, File destDir) throws IOException {
		
		if(!destDir.exists()) {
			destDir.mkdir();
		}
		
		File[] children = sourceDir.listFiles();
		
		for(File sourceChild : children) {
			String name = sourceChild.getName();
			File destChild = new File(destDir, name);
			if(sourceChild.isDirectory()) {
				copyDirectory(sourceChild, destChild);
			}
			else {
				copyFile(sourceChild, destChild);
			}
		}	
	}
public static void copyFile(File source, File dest) throws IOException {
	/*
	if (source.getName().contains("base"))
	{
		System.err.println("Skipping base.css");
		return;
	}
	if (source.getName().contains("reset"))
	{
		System.err.println("Skipping reset.css");
		return;
	}
	if (source.getName().contains("hnd"))
	{
		System.err.println("Skipping hnd.css");
		return;
	}*/
	if (source.getName().contains("toc"))
	{
		strip(path, pathOut, source.getName(), true);
		return;
	}
	/*if (source.getName().contains("ui.dynatree"))
	{
		System.err.println("Skipping ui.dynatree.css");
		return;
	}
	if (source.getName().contains("jquery-ui-1.8.12.custom"))
	{
		System.err.println("Skipping jquery-ui-1.8.12.custom.css");
		return;
	}
	*/
	if (source.getName().contains(".png") && source.getPath().contains("css"))
	{
		System.err.println("Skipping " + source.getName());
		return;
	}
	System.out.println("Copying: " + source.getName());
	if(!dest.exists()) {
		dest.createNewFile();
	}
    InputStream in = null;
    OutputStream out = null;
    try {
    	in = new FileInputStream(source);
    	out = new FileOutputStream(dest);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
    }
    finally {
    	in.close();
        out.close();
    }
    
}
	
	public static void main(String[] args) {
		
		
		 //String pathOut = "Z:/public_html/newDocs/";
		 //String pathOut = "C:/Users/Brendan/Documents/GitHub/wpi-suite/IncrediblyGorgeousCalendar/documentation_html/";
		 
		  String files;
		  File folder = new File(path);
		  File[] listOfFiles = folder.listFiles(); 
		 
		  for (int i = 0; i < listOfFiles.length; i++) 
		  {
			  //if its a single file
			  if (listOfFiles[i].isFile()) 
			  {
				   files = listOfFiles[i].getName();
				   if (listOfFiles[i].getName().contains("toc") || listOfFiles[i].getName().contains("Calendar.html"))//don't filter toc or the main calendar
					  {
						  try {
							copyFile(new File(listOfFiles[i].getPath()), new File(pathOut + listOfFiles[i].getName()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						  continue;
					  }
				   strip(path, pathOut, files);
				   System.out.println("Altering: " + files);
		      }
			  else if (listOfFiles[i].isDirectory() && listOfFiles[i].getName().contains("lib"))
			  {
				  System.out.println("Moving lib folder");
				  try {
					copyDirectory(new File(path+listOfFiles[i].getName()), new File(pathOut + listOfFiles[i].getName()));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
			  else if (listOfFiles[i].isDirectory() && listOfFiles[i].getName().contains("css"))
			  {
				  System.out.println("Moving css folder");
				  try {
					copyDirectory(new File(path+listOfFiles[i].getName()), new File(pathOut + listOfFiles[i].getName()));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
			  else if (listOfFiles[i].isDirectory() && listOfFiles[i].getName().contains("js"))
			  {
				  System.out.println("Moving js folder");
				  try {
					copyDirectory(new File(path+listOfFiles[i].getName()), new File(pathOut + listOfFiles[i].getName()));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
			  else if (listOfFiles[i].isDirectory() && listOfFiles[i].getName().contains("js"))
			  {
				  System.out.println("Moving js folder");
				  try {
					copyDirectory(new File(path+listOfFiles[i].getName()), new File(pathOut + listOfFiles[i].getName()));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
			   
		  }
		
		  System.out.println("Done!!!!");
	}

}
