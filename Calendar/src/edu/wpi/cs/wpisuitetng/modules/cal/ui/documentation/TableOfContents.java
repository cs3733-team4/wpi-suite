/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.documentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class TableOfContents extends JPanel implements TreeSelectionListener {
    private JTree tree;
    private HashMap<String, DefaultMutableTreeNode> theMap;
    private boolean fromTOC;
    /**
     * Constructor for the TableOfContents
     * @param serverLocation the String location of the server
     */
    public TableOfContents(String serverLocation) {
        super(new GridLayout(1,0));
        theMap = new HashMap<>();
        this.setBackground(Color.getColor("EFEFEF"));
        //Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Team YOCO Calendar");
        populateFromTOC(top, serverLocation);
        
        
        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setBackground(Color.getColor("EFEFEF"));
        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        if (tree.getCellRenderer() instanceof DefaultTreeCellRenderer)
        {
            final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)(tree.getCellRenderer());
            renderer.setBackgroundNonSelectionColor(Color.getColor("EFEFEF"));
        }

        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);
        treeView.setBackground(Color.getColor("EFEFEF"));

        Dimension minimumSize = new Dimension(100, 50);
        treeView.setMinimumSize(minimumSize);
        add(treeView);
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node==null)
        	return;
        if (node.getUserObject() instanceof ListInfo)
        {
        	DocumentMainPanel.getInstance().goToPage(((ListInfo)node.getUserObject()).getPageName());
        	fromTOC=true;
        }
        
  }
    /**
     * Expands the tree to only the shown page and selects the page
     * @param thePage
     */
    public void expandToPage(String thePage)
    {
    
    	DefaultMutableTreeNode theNode = theMap.get(thePage);

    	if (theNode==null)
    		return;
    	
    	
    	if (!fromTOC)
    	{
        	tree.setSelectionPath(new TreePath(theNode.getPath()));
        	tree.scrollPathToVisible(new TreePath(theNode.getPath()));
    	}
    	fromTOC=false;
    	
    	
    }
/**
 * A class that contains information about the tree node
 */
    private class ListInfo {
        private String Name;
        private String PageName;

        public ListInfo(String book, String filename) {
        	Name = book;
            PageName = filename;
        }

        public String toString() {
            return Name;
        }
        public String getPageName()
        {
        	return PageName;
        }
    }

    private String getIDFromLine(String lineIn)
    {
    	return lineIn.substring(8, lineIn.length()-4);
    }
    private String getHREFFromLine(String lineIn)
    {
    	return lineIn.substring(lineIn.indexOf("href=\"") + 6, lineIn.length()-1);  
    }
    
    private void populateFromTOC(DefaultMutableTreeNode top, String server) {
    	String holdLine;
    	String nameHold;
    	String dataHold;
    	
        DefaultMutableTreeNode[] levels = new DefaultMutableTreeNode[5];

        int level=0;
        
        try {
			URL TOC = new URL(server + "toc.html");
		    BufferedReader read = new BufferedReader(new InputStreamReader(TOC.openStream()));
			while (true)//loops through all the useless stuff at the top
	        {
	        	holdLine = read.readLine();
	        	if (holdLine.contains("<li "))
	        		break;
	        }
			while(true)
			{
				if (holdLine.contains("<ul>"))//if it contains a <ul> the the list index goes one deeper
					level++;
				if (holdLine.contains("</ul>"))
				{
					for (int i=0; i<holdLine.length(); i++)
					{
						if (holdLine.indexOf("</ul>", i)>0)//decrease for every instance of </ul>
						{

							level--;
							i=holdLine.indexOf("</ul>", i);
						}
					}
				}
				holdLine=read.readLine();
				if(holdLine.contains("div"))
					break;
				holdLine=read.readLine();
				dataHold = getHREFFromLine(holdLine);
				holdLine=read.readLine();
				holdLine=read.readLine();
				nameHold=getIDFromLine(holdLine);
				holdLine=read.readLine();
				holdLine=read.readLine();
				
				if (level ==0)
				{
					levels[level]=new DefaultMutableTreeNode(new ListInfo(nameHold, dataHold));
					theMap.put(dataHold, levels[level]);
					top.add(levels[level]);
				}
				else
				{
					levels[level] = new DefaultMutableTreeNode(new ListInfo(nameHold, dataHold));
					theMap.put(dataHold, levels[level]);
					levels[level-1].add(levels[level]);
				}
			}
        
        } catch (IOException e) {
			e.printStackTrace();
		}       
    }   
}
