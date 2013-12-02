package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.eventui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.eventui.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class SidebarTabbedPane extends JTabbedPane{
	
	private JPanel detailTab;
	private JTextPane detailTextPane;
	private Document detailTextDoc;
	private SimpleAttributeSet normalTextStyle;
	private SimpleAttributeSet boldBlueTextStyle;
	private SimpleAttributeSet boldRedTextStyle;
	private JScrollPane detailScrollPane;
	private JLabel detailTitleLabel;
	private JPanel detailButtonPane;
	private JButton detailEditButton;
	private JButton detailDeleteButton;
	private JTextArea commitmentTab;
	private DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("h:mm aa");
	private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yy");
	private Displayable currentDisplayable;
	
	/**
	 * Tabbed panel in the navigation sidebar to hold additional details of selected items
	 */
	public SidebarTabbedPane() {
		
		//setup
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		setupTextStyles();
		setupDetailTab();
		setupCommitementTab();
		
		//add tabs
		this.addTab("Details", detailScrollPane);
		this.addTab("Commitments", commitmentTab);
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
		commitmentTab = new JTextArea();
	}

	/**
	 * initializes all the components of the details tab
	 */
	private void setupDetailTab()
	{
		// setup container panel
		detailTab = new JPanel();
		detailTab.setLayout(new BorderLayout());
		
		// setup text area
		detailTextPane = new JTextPane();
		detailTextPane.setEditable(false);
		detailTextPane.setCursor(null);
		detailTextPane.setOpaque(false);
		detailTextPane.setFocusable(false);
		detailTextPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		detailTextPane.putClientProperty("html.disable", true); //prevents html parsing
		DefaultCaret caret = (DefaultCaret) detailTextPane.getCaret(); //prevents scrollpane from autoscrolling to bottom
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

	    detailTextDoc = ((StyledDocument) detailTextPane.getDocument());

	    //setup text title
		detailTitleLabel = new JLabel();
		detailTitleLabel.setOpaque(true);
		detailTitleLabel.putClientProperty("html.disable", true); //prevents html parsing
		detailTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		detailTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		detailTitleLabel.setPreferredSize(detailTextPane.getPreferredSize());
		
	    // setup buttons and listeners
	    detailEditButton = new JButton("Edit");
	    detailEditButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainPanel instance = MainPanel.getInstance();
				
				if (currentDisplayable instanceof Event) {
					AddEventDisplay mAddEventDisplay = new AddEventDisplay((Event) currentDisplayable);
					mAddEventDisplay.setTabId(instance.addTopLevelTab(mAddEventDisplay, "Edit Event", true));
				}
				else if (currentDisplayable instanceof Commitment) {
					AddCommitmentDisplay mAddCommitmentDisplay = new AddCommitmentDisplay((Commitment) currentDisplayable);
					mAddCommitmentDisplay.setTabId(instance.addTopLevelTab(mAddCommitmentDisplay, "Edit Commitment", true));
				}				
			}
		});
	    
	    detailDeleteButton = new JButton("Delete");
	    detailDeleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Add event and commitment deletion
				
			}
		});

	    // buttons disabled by default
	    setButtonsEnabled(false);
	    
	    // add buttons to button container
	    detailButtonPane = new JPanel();
	    detailButtonPane.setLayout(new FlowLayout());
	    detailButtonPane.add(detailEditButton);
	    detailButtonPane.add(detailDeleteButton);
	    
	    // add text area and button container to detail tab
	    detailTab.add(detailTitleLabel, BorderLayout.NORTH);
	    detailTab.add(detailTextPane, BorderLayout.CENTER);
	    detailTab.add(detailButtonPane, BorderLayout.SOUTH);
	    
	    // put entire tab into a scroll pane
	    detailScrollPane = new JScrollPane(detailTab);
	    detailScrollPane.setBorder( new EmptyBorder(5,5,5,5));
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
	        	detailTitleLabel.setBackground(Colors.SELECTED_BACKGROUND);
				detailTitleLabel.setForeground(Colors.SELECTED_TEXT);
				detailTitleLabel.setText(mDisplayable.getName());
				
	        	detailTextDoc.insertString(detailTextDoc.getLength(), ((Event) mDisplayable).getStart().toString(timeFormatter) + " - ", normalTextStyle);
	        	detailTextDoc.insertString(detailTextDoc.getLength(), ((Event) mDisplayable).getEnd().toString(timeFormatter) + "\n", normalTextStyle);
	        	detailTextDoc.insertString(detailTextDoc.getLength(), mDisplayable.getDescription() + "\n", normalTextStyle);
	        }catch(Exception e)
	        {
	        	e.printStackTrace();
	        }

	        setButtonsEnabled(true);
		}else if (mDisplayable instanceof Commitment)
		{
			try
	        {
				// TODO: Change this to a predefined color or category color
				detailTitleLabel.setBackground(Color.MAGENTA);
				detailTitleLabel.setForeground(Colors.SELECTED_TEXT);
				detailTitleLabel.setText(mDisplayable.getName());
				
	        	detailTextDoc.insertString(detailTextDoc.getLength(), ((Commitment) mDisplayable).getDate().toString(timeFormatter) + "\n", normalTextStyle);
	        	detailTextDoc.insertString(detailTextDoc.getLength(), mDisplayable.getDescription() + "\n", normalTextStyle);
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
		detailTitleLabel.setBackground(Colors.TABLE_BACKGROUND);
		detailTitleLabel.setText("");
		detailTextPane.setText("");
		setButtonsEnabled(false);
	}
}
