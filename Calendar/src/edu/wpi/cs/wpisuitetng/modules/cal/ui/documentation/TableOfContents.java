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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class TableOfContents extends JPanel {
    private JTree tree;
    private HashMap<String, DefaultMutableTreeNode> theMap;
    /**
     * Constructor for the TableOfContents
     * @param serverLocation the String location of the server
     */
    public TableOfContents(String serverLocation) {
        super(new GridLayout(1,0));
        theMap = new HashMap<>();
        this.setBackground(Color.getColor("FFFFFF"));
        //Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Team Calendar");
        populateFromTOC(top, serverLocation);
        
        
        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setBackground(Color.getColor("FFFFFF"));
        
        //Listen for when the selection changes.
        tree.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
	            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
	            if (path != null) {
	            	DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	                if (node==null)
	                	return;
	                if (node.getUserObject() instanceof ListInfo)
	                {
	                	
	                	DocumentMainPanel.getInstance().goToPage(((ListInfo)node.getUserObject()).getPageName(), true);
	                }
	            }
	            
			}
		});
        if (tree.getCellRenderer() instanceof DefaultTreeCellRenderer)
        {
            final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)(tree.getCellRenderer());
            renderer.setBackgroundNonSelectionColor(Color.getColor("FFFFFF"));
        }

        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);
        treeView.setBackground(Color.getColor("FFFFFF"));

        Dimension minimumSize = new Dimension(100, 50);
        treeView.setMinimumSize(minimumSize);
        add(treeView);
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
    	
    	
    	tree.setSelectionPath(new TreePath(theNode.getPath()));
    	tree.scrollPathToVisible(new TreePath(theNode.getPath()));
    	
    	
    	
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
    
	/**
	 * Extracts the ID name from the given string
	 * @param lineIn the line to parse
	 * @return the ID name
	 */
    private String getIDFromLine(String lineIn)
    {
    	return lineIn.substring(8, lineIn.length()-4);
    }
    
    /**
     * Extract the link from a string
     * @param lineIn the line to parse
     * @return the HREF
     */
    private String getHREFFromLine(String lineIn)
    {
    	return lineIn.substring(lineIn.indexOf("href=\"") + 6, lineIn.length()-1);  
    }
    
    /**
     * This parses the ToC page and generates a tree from it
     * @param top the top level node
     * @param server the location of the sever
     */
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
