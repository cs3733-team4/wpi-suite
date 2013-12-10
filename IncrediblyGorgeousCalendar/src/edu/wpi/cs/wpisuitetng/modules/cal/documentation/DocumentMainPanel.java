/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.documentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.SelectableField;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.CategoryManager;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class DocumentMainPanel extends JFrame{

    private JEditorPane webPage;
    private JScrollPane scroll;
    private URL url;
    private String serverLocation;
    private TableOfContents tableOfContents;
    private static DocumentMainPanel instance;
    private JButton openInBrowser;
    private JPanel tocView;
    private DocumentMainPanel()
    {
    	super();
    }
    private String extractPage(String sIn)
    {
    	String sOut="";
    	int index=0;
    	for (int i=sIn.length()-1; i>-1; i--)
    	{
    		if (sIn.charAt(i) == '/')
    		{
    			index=i;
    			break;
    		}
    	}
    	sOut=sIn.substring(index +1, sIn.length());
    	return sOut;
    }
    
    public void init()
    {

    	if (serverLocation!=null)
    		return;
    	tocView = new JPanel(new BorderLayout());
    	openInBrowser = new JButton("Open In Browser");
    	this.setTitle("YOCO Calendar Help");
    	this.setLayout(new BorderLayout());
    	
    	//serverLocation = Network.getInstance().makeRequest("docs/Calendar/", HttpMethod.GET).getUrl().toString().replace("API/", "");
        serverLocation = "http://www.wpi.edu/~bkmcleod/newDocs/";
    	System.out.println(serverLocation);
    	tableOfContents=new TableOfContents(serverLocation);
    	try
        {
        	url = new URL(serverLocation + "Introduction.html");
        }
        catch(MalformedURLException mue) {
            JOptionPane.showMessageDialog(null,mue);
        }
        
        //create the JEditorPane
        try {
            webPage = new JEditorPane(url);
            
            //set the editor pane to false.
            webPage.setEditable(false);
        }
        catch(IOException ioe) {
            JOptionPane.showMessageDialog(null,ioe);
        }
        
        
        //create the scroll pane and add the JEditorPane to it.
        scroll = new JScrollPane(webPage);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        openInBrowser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BareBonesBrowserLaunch.openURL(serverLocation + "YOCO Calendar.html?" + extractPage(webPage.getPage().getPath()));
				
			}
		});
        //create the JTextField that shows the HTML Page
        webPage.setBackground(Color.getColor("EFEFEF"));
        webPage.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                      	try {
                      		System.out.println(e.getURL().toString());
                      			if (doAction(e.getURL().toString()))
                      				return;
                      			else if (!e.getURL().toString().contains("html"))
                      			{
                      				BareBonesBrowserLaunch.openURL(e.getURL().toString());
                      				return;
                      			}
                      			else
                      				webPage.setPage(e.getURL());
                            }
                            catch(IOException | IllegalArgumentException ioe) {
                                JOptionPane.showMessageDialog(null,ioe);
                            }
                    }//end hyperlinkUpdate()
                });//end HyperlinkListener

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        tocView.add(openInBrowser, BorderLayout.NORTH);
        tocView.add(tableOfContents, BorderLayout.CENTER);
        splitPane.add(tocView);
        splitPane.add(scroll);
        this.add(splitPane, BorderLayout.CENTER);
    }
    /**
     * doAction will return true if there is/was an action committed as a result of the link
     * @param actionPath The URL Path that is requested
     * @return if an action was completed
     */
    private boolean doAction(String actionPath) 
    {
    	
    	if (actionPath.contains("#OpenNewEventWindow"))
		{
			AddEventDisplay ned = new AddEventDisplay();
			ned.setTabId(MainPanel.getInstance().addTopLevelTab(ned, "New Event", true));
			return true;
		}
    	else if (actionPath.contains("#OpenNewAddCommitmentBox"))
    	{
    		AddCommitmentDisplay ncm = new AddCommitmentDisplay();
    		ncm.setTabId(MainPanel.getInstance().addTopLevelTab(ncm, "New Commitment", true));
    		return true;
    	}
    	else if (actionPath.contains("#SaveNewEvent"))
		{
    		if (MainPanel.getInstance().getSelectedComponent() instanceof AddEventDisplay)
    		{
    			AddEventDisplay ned = (AddEventDisplay) MainPanel.getInstance().getSelectedComponent();
    			ned.attemptSave();
    		}
			return true;
		}
    	else if (actionPath.contains("#SaveNewCommitment"))
    	{
    		if (MainPanel.getInstance().getSelectedComponent() instanceof AddCommitmentDisplay)
    		{
    			AddCommitmentDisplay ncm = (AddCommitmentDisplay) MainPanel.getInstance().getSelectedComponent();
  			
    			ncm.attemptSave();
    		}
    		return true;
    	}
    	else if (actionPath.contains("#DeleteEventFromDetailsPane") || actionPath.contains("#DeleteSelectedEvent"))
    	{
    		if (MainPanel.getInstance().getSelectedDisplayable() instanceof Event)
    			MainPanel.getInstance().deleteDisplayable(MainPanel.getInstance().getSelectedDisplayable());
    		return true;
    	}
    	else if (actionPath.contains("#DeleteCommitmentFromDetailsPane"))
    	{
    		if (MainPanel.getInstance().getSelectedDisplayable() instanceof Commitment)
    			MainPanel.getInstance().deleteDisplayable(MainPanel.getInstance().getSelectedDisplayable());
    		return true;
    	}
    	else if (actionPath.contains("#EditSelectedEvent"))
    	{
    		if (MainPanel.getInstance().getSelectedDisplayable() instanceof Event)
    		{
	    		AddEventDisplay ned = new AddEventDisplay((Event)MainPanel.getInstance().getSelectedDisplayable());
	    		if (ned!=null)
	    			ned.setTabId(MainPanel.getInstance().addTopLevelTab(ned, "Edit Event", true));
    		}
    		return true;
    	}
    	else if (actionPath.contains("#EditSelectedCommitment"))
    	{
    		if (MainPanel.getInstance().getSelectedDisplayable() instanceof Commitment)
    		{
	    		AddCommitmentDisplay ned = new AddCommitmentDisplay((Commitment)MainPanel.getInstance().getSelectedDisplayable());
	    		if (ned!=null)
	    			ned.setTabId(MainPanel.getInstance().addTopLevelTab(ned, "Edit Commitment", true));
    		}
    		return true;
    	}
    	else if (actionPath.contains("#SaveEditingEvent"))
    	{
    		if (MainPanel.getInstance().getSelectedComponent() instanceof AddEventDisplay)
    		{
    			AddEventDisplay ned = (AddEventDisplay) MainPanel.getInstance().getSelectedComponent();
    			if (ned.editingEvent())
    			{
    				ned.attemptSave();
    			}
    		}
    		return true;
    	}
    	else if (actionPath.contains("#SaveEditingCommitment"))
    	{
    		if (MainPanel.getInstance().getSelectedComponent() instanceof AddCommitmentDisplay)
    		{
    			AddCommitmentDisplay ncd = (AddCommitmentDisplay) MainPanel.getInstance().getSelectedComponent();
    			if (ncd.editingCommitment())
    			{
    				ncd.attemptSave();
    			}
    		}
    		return true;
    	}
    	else if (actionPath.contains("#SelectNameInNewEvent"))
    	{
    		setSelectedForEvent(SelectableField.NAME);
			return true;
    	}
    	else if (actionPath.contains("#SetStartDateInNewEvent"))
    	{
    		setSelectedForEvent(SelectableField.START_DATE);
			return true;
    	}
    	else if (actionPath.contains("#SetStartTimeInNewEvent"))
    	{
    		setSelectedForEvent(SelectableField.START_TIME);
			return true;
    	}
    	else if (actionPath.contains("#SetEndDateInNewEvent"))
    	{
    		setSelectedForEvent(SelectableField.END_DATE);
			return true;
    	}
    	else if (actionPath.contains("#SetEndTimeInNewEvent"))
    	{
    		setSelectedForEvent(SelectableField.END_TIME);
			return true;
    	}
    	else if (actionPath.contains("#SetDescriptionInNewEvent"))
    	{
    		setSelectedForEvent(SelectableField.DESCRIPTION);
			return true;
    	}
    	else if (actionPath.contains("#SetCategoryInNewEvent"))
    	{
    		setSelectedForEvent(SelectableField.CATEGORY);
			return true;
    	}
    	else if (actionPath.contains("#SetParticipantsInNewEvent"))
    	{
    		setSelectedForEvent(SelectableField.PARTICIPANTS);
			return true;
    	}/////
    	else if (actionPath.contains("#SelectNameInNewComitment"))
    	{
    		setSelectedForCommitment(SelectableField.NAME);
			return true;
    	}
    	else if (actionPath.contains("#SelectDateInNewCommitment"))
    	{
    		setSelectedForCommitment(SelectableField.DATE);
			return true;
    	}
    	else if (actionPath.contains("#SelectTimeInNewCommitment"))
    	{
    		setSelectedForCommitment(SelectableField.TIME);
			return true;
    	}
    	else if (actionPath.contains("#SelectParticipantsInNewCommitment"))
    	{
    		setSelectedForCommitment(SelectableField.PARTICIPANTS);
			return true;
    	}
    	else if (actionPath.contains("#SelectDescriptionInNewCommitment"))
    	{
    		setSelectedForCommitment(SelectableField.DESCRIPTION);
			return true;
    	}
    	else if (actionPath.contains("#SwitchToDayView"))
    	{
    		MainPanel.getInstance().openCalendarViewTab();
    		MainPanel.getInstance().viewDay();
    		return true;
    	}
    	else if (actionPath.contains("#SwitchToMonthView"))
    	{
    		MainPanel.getInstance().openCalendarViewTab();
    		MainPanel.getInstance().viewMonth();
    		return true;
    	}
    	else if (actionPath.contains("#SwitchToYearView"))
    	{
    		MainPanel.getInstance().openCalendarViewTab();
    		MainPanel.getInstance().viewYear();
    		return true;
    	}
    	else if (actionPath.contains("#PreviousArrow"))
    	{
    		MainPanel.getInstance().openCalendarViewTab();
    		MainPanel.getInstance().getMOCA().previous();
    		return true;
    	}
    	else if (actionPath.contains("#NextArrow"))
    	{
    		MainPanel.getInstance().openCalendarViewTab();
    		MainPanel.getInstance().getMOCA().next();
    		return true;
    	}
    	else if (actionPath.contains("#OpenManageCategories"))
    	{
    		CategoryManager cat = MainPanel.getInstance().getCategoryManagerTab();
			if(cat==null){
				cat = new CategoryManager();
				cat.setTabId(MainPanel.getInstance().addTopLevelTab(cat, "Manage Categories", true));
			}
			else
				MainPanel.getInstance().setSelectedTab(cat);
    		return true;
    	}
    	else if (actionPath.contains("#SaveNewCategory"))
    	{

    		if (MainPanel.getInstance().getSelectedComponent() instanceof CategoryManager)
    		{
    			CategoryManager cat = MainPanel.getInstance().getCategoryManagerTab();
    			cat.attemptSave();
    		}
    		return true;
    	}
    	else if (actionPath.contains("#SelectNameInNewCategory"))
    	{

    		if (MainPanel.getInstance().getSelectedComponent() instanceof CategoryManager)
    		{
    			CategoryManager cat = MainPanel.getInstance().getCategoryManagerTab();
    			cat.focusOnName();
    		}
    		return true;
    	}
    	
    	
    	
    	
    	
    	if (actionPath.contains("#"))
    	{
    		System.out.println("Action: " + actionPath + " not yet implemented!");
    		return true;
    	}
    	return false;
    }
    /**
	 * Just a little helper function to re-use some code, it just pulls the open tab and relays the requested focus
	 * @param field the field to focus on
	 */
	private void setSelectedForCommitment(SelectableField field)
	{
		if (MainPanel.getInstance().getSelectedComponent() instanceof AddCommitmentDisplay)
		{
			AddCommitmentDisplay ned = (AddCommitmentDisplay) MainPanel.getInstance().getSelectedComponent();
			ned.setSelected(field);
		}
	}
    /**
     * Just a little helper function to re-use some code, it just pulls the open tab and relays the requested focus
     * @param field the field to focus on
     */
    private void setSelectedForEvent(SelectableField field)
    {
    	if (MainPanel.getInstance().getSelectedComponent() instanceof AddEventDisplay)
		{
			AddEventDisplay ned = (AddEventDisplay) MainPanel.getInstance().getSelectedComponent();
			ned.setSelected(field);
		}
    }
    /**
     * Allows for singleton of DocumentMainPanel
     * @return the only existing instance of DocumentMainPanel
     */
    public static DocumentMainPanel getInstance()
	{
		if (instance == null)
		{
			instance = new DocumentMainPanel();
		}
		return instance;
	}
    
    @Override
    public void setVisible(boolean vis)
    {
    	super.setVisible(vis);
    	if (vis)
    		this.setSize(800, 600);
    }
    
    /**
     * Navigates the documentation view to a specific page.  Will not work for link outside of the documentation
     * @param page the HTML page to navigate to. DO NOT INCLUDE THE SERVER PATH!!!!
     */
    public void goToPage(String page)
    {
    	try {
			webPage.setPage(new URL(serverLocation + page));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }

}
