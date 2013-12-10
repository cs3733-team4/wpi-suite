/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;

public class SidebarTabbedPane extends JTabbedPane{
	
	private JPanel detailTab;
	private JTextArea detailTextPane;
	private Document detailTextDoc;
	private SimpleAttributeSet normalTextStyle;
	private SimpleAttributeSet boldBlueTextStyle;
	private SimpleAttributeSet boldRedTextStyle;
	private JScrollPane detailScrollPane;
	private JLabel detailTitleLabel;
	private JPanel detailButtonPane;
	private JButton detailEditButton;
	private JButton detailDeleteButton;
	private JTextPane commitmentTab;
	private DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("h:mm aa");
	private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yy");
	private Displayable currentDisplayable;
	
	/**
	 * Tabbed panel in the navigation sidebar to hold additional details of selected items
	 */
	public SidebarTabbedPane() {
		
		//setup
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.setFocusable(false);
		
		setupTextStyles();
		setupDetailTab();
		setupCommitementTab();
		
		//add tabs
		this.addTab("Details", detailTab);
		//this.addTab("Commitments", commitmentTab);
	}
	
	/**
	 * Initializes some text styles to be used in the JTextAreas
	 */
	private void setupTextStyles() {
		normalTextStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(normalTextStyle, "Tahoma");
        StyleConstants.setFontSize(normalTextStyle, 12);
        
        // These are unused in current formatting
        boldBlueTextStyle = new SimpleAttributeSet(normalTextStyle);
        StyleConstants.setBold(boldBlueTextStyle, true);
        StyleConstants.setForeground(boldBlueTextStyle, Colors.SELECTED_BACKGROUND);
        
        boldRedTextStyle = new SimpleAttributeSet(normalTextStyle);
        StyleConstants.setBold(boldRedTextStyle, true);
        StyleConstants.setForeground(boldRedTextStyle, Color.MAGENTA);
		
	}
	
	/**
	 * initializes all the components of the commitment tab
	 */
	private void setupCommitementTab() {
		commitmentTab = new JTextPane();
		commitmentTab.setEditable(false);
		commitmentTab.setCursor(null);
		commitmentTab.setFocusable(false);
		commitmentTab.setOpaque(false);
		commitmentTab.setFont(new Font("Tahoma", Font.PLAIN, 12));
		commitmentTab.putClientProperty("html.disable", true); //prevents html parsing
		DefaultCaret caret = (DefaultCaret) commitmentTab.getCaret(); //prevents scrollpane from autoscrolling to bottom
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
	}

	/**
	 * initializes all the components of the details tab
	 */
	private void setupDetailTab()
	{
		// setup container panel
		detailTab = new JPanel();
		detailTab.setLayout(new BorderLayout());
		detailTab.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		detailTab.setFocusable(false);
		
		// setup text area
		detailTextPane = new JTextArea();
		detailTextPane.setWrapStyleWord(true);
		detailTextPane.setLineWrap(true);
		detailTextPane.setMaximumSize(new Dimension(180, Short.MAX_VALUE));
		detailTextPane.setEditable(false);
		detailTextPane.setCursor(null);
		detailTextPane.setFocusable(false);
		detailTextPane.setOpaque(false);
		detailTextPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		detailTextPane.putClientProperty("html.disable", true); //prevents html parsing
		DefaultCaret caret = (DefaultCaret) detailTextPane.getCaret(); //prevents scrollpane from autoscrolling to bottom
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

	    detailTextDoc = detailTextPane.getDocument();

	   	//setup title label
		detailTitleLabel = new JLabel();
		detailTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		detailTitleLabel.putClientProperty("html.disable", true); //prevents html parsing
		detailTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		detailTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
	    // setup buttons and listeners
	    detailEditButton = new JButton("Edit");
	    detailEditButton.setFocusable(false);
	    detailEditButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainPanel instance = MainPanel.getInstance();
				instance.editSelectedDisplayable(currentDisplayable)	;
			}
		});
	    
	    detailDeleteButton = new JButton("Delete");
	    detailDeleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainPanel.getInstance().deleteDisplayable(currentDisplayable);
			}
		});

	    // buttons disabled by default
	    setButtonsEnabled(false);
	    
	    // add buttons to button container
	    detailButtonPane = new JPanel();
	    detailButtonPane.setLayout(new FlowLayout());
	    detailButtonPane.add(detailEditButton);
	    detailButtonPane.setFocusable(false);
	    detailButtonPane.add(detailDeleteButton);
	    
	    //for a later user story
	    //detailButtonPane.add(detailDeleteButton);
	    
	    // put entire tab into a scroll pane
	    detailScrollPane = new JScrollPane(detailTextPane);
	    detailScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    detailScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    detailScrollPane.setBorder( new EmptyBorder(5,5,5,5));
	    
	    // add text area and button container to detail tab
	    detailTab.add(detailTitleLabel, BorderLayout.NORTH);
	    detailTab.add(detailScrollPane, BorderLayout.CENTER);
	    detailTab.add(detailButtonPane, BorderLayout.SOUTH);
	}
	
	/**
	 * Sets the enabled status of the edit and cancel buttons
	 * @param enabled flag to set enabled status
	 */
	private void setButtonsEnabled(boolean enabled) {
		detailEditButton.setEnabled(enabled);
		detailDeleteButton.setEnabled(enabled);
	}
	
	/**
	 * Updates the text area to display information about a displayable
	 * @param mDisplayable the displayable to show
	 */
	public void showDetails(Displayable mDisplayable)
	{
	    clearDetails();
	    currentDisplayable = mDisplayable;
	    
		if (mDisplayable instanceof Event)
		{    
	        try
	        {
				detailTitleLabel.setOpaque(true);
	        	detailTitleLabel.setBackground(((Event)mDisplayable).getColor());
				detailTitleLabel.setText(mDisplayable.getName());
				if (!((Event) mDisplayable).isMultiDayEvent())
					detailTextDoc.insertString(detailTextDoc.getLength(), "Time:\n   " + ((Event) mDisplayable).getStart().toString(timeFormatter) + " - " + ((Event) mDisplayable).getEnd().toString(timeFormatter) + "\n", normalTextStyle);
				else
				{
					detailTextDoc.insertString(detailTextDoc.getLength(), "Starts:\n   " + ((Event) mDisplayable).getStart().toString(dateFormatter) + " " + ((Event) mDisplayable).getStart().toString(timeFormatter) + "\n", normalTextStyle);
					detailTextDoc.insertString(detailTextDoc.getLength(), "Ends:\n   " + ((Event) mDisplayable).getEnd().toString(dateFormatter) + " " + ((Event) mDisplayable).getEnd().toString(timeFormatter) + "\n", normalTextStyle);
				}
				
				detailTextDoc.insertString(detailTextDoc.getLength(), "Description:\n   " + mDisplayable.getDescription() + "\n", normalTextStyle);
				if (((Event)mDisplayable).getAssociatedCategory() != null)
					detailTextDoc.insertString(detailTextDoc.getLength(), "Category:\n   " + ((Event)mDisplayable).getAssociatedCategory().getName() + "\n", normalTextStyle);
		        
	        }catch(Exception e)
	        {
	        	e.printStackTrace();
	        }

	        setButtonsEnabled(true);
		}else if (mDisplayable instanceof Commitment)
		{
			try
	        {
				detailTitleLabel.setText(mDisplayable.getName());
				detailTitleLabel.setOpaque(false);
	        	detailTextDoc.insertString(detailTextDoc.getLength(), "Date:\n   " + ((Commitment) mDisplayable).getDate().toString(dateFormatter) + "\n", normalTextStyle);
	        	detailTextDoc.insertString(detailTextDoc.getLength(), "Time:\n   " + ((Commitment) mDisplayable).getDate().toString(timeFormatter) + "\n", normalTextStyle);
	        	detailTextDoc.insertString(detailTextDoc.getLength(), "Description:\n   " + mDisplayable.getDescription() + "\n", normalTextStyle);
	        }catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
			
			setButtonsEnabled(true);
		}
	}

	/**
	 * clears the text area of any details
	 */
	public void clearDetails() {
		detailTitleLabel.setOpaque(false);
		detailTitleLabel.setText("");
		detailTextPane.setText("");
		setButtonsEnabled(false);
	}
}
