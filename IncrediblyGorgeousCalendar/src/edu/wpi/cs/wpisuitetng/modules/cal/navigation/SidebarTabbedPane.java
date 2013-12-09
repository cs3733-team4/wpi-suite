package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

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
	
	// Category filter tab
	private JPanel categoryFilterTab;
	private JPanel categoryList;
	private JPanel categoryButtonPanel;
	private JButton selectAllButton;
	private JButton clearAllButton;
	private JScrollPane categoryScroll;
	private HashMap<JCheckBox, Category> checkBoxCategoryMap = new HashMap<JCheckBox, Category>();
	private Collection<UUID> selectedCategories = new ArrayList<UUID>();
	
	/**
	 * Tabbed panel in the navigation sidebar to hold additional details of selected items
	 */
	public SidebarTabbedPane() {
		
		//setup
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		setupTextStyles();
		setupDetailTab();
		setupCommitementTab();
		setUpCategoryFilterTab();
		
		//add tabs
		this.addTab("Details", detailTab);
		//this.addTab("Commitments", commitmentTab);
		this.addTab("Filters", categoryFilterTab);
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
	    detailButtonPane.add(detailDeleteButton);
	    
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
	 * Sets up the components of the category filter tab
	 */
	private void setUpCategoryFilterTab(){

		// Set up container panel
		categoryFilterTab = new JPanel();
		categoryFilterTab.setLayout(new BorderLayout());
		categoryFilterTab.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		categoryFilterTab.setBackground(Colors.TABLE_BACKGROUND);
		categoryFilterTab.setAlignmentY(LEFT_ALIGNMENT);
		
		// Set up panel with categories
		categoryList = new JPanel();
		categoryList.setLayout(new BoxLayout(categoryList, BoxLayout.Y_AXIS));
		categoryList.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		categoryList.setBackground(Colors.TABLE_BACKGROUND);
		categoryList.putClientProperty("html.disable", true);
		categoryList.setAlignmentY(LEFT_ALIGNMENT);
		
		// Add categories to panel
		populateCategoryList(categoryList);

		// Set up scroll panel
		categoryScroll = new JScrollPane(categoryList);
		categoryScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    categoryScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		categoryScroll.setBorder(new EmptyBorder(5,5,5,5));
		categoryScroll.setBackground(Colors.TABLE_BACKGROUND);
		categoryScroll.setAlignmentY(LEFT_ALIGNMENT);
		
		// Set up selection buttons
		categoryButtonPanel = new JPanel();
		categoryButtonPanel.setLayout(new GridLayout());
		categoryButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		categoryButtonPanel.setBackground(Colors.TABLE_BACKGROUND);
		categoryButtonPanel.putClientProperty("html.disable", true);
		
		selectAllButton = new JButton("Select All");
		selectAllButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectAllCategories();
			}
		});
		
		clearAllButton = new JButton("Clear");
		clearAllButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				deselectAllCategories();
			}
		});
		
		categoryButtonPanel.add(selectAllButton);
		categoryButtonPanel.add(clearAllButton);
		
		// Set up UI
		categoryFilterTab.add(categoryButtonPanel, BorderLayout.NORTH);
		categoryFilterTab.add(categoryScroll);
		categoryFilterTab.setFocusable(false); // Keep tab form grabbing focus from arrow keys
		
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
	
	/**
	 * Refreshes the category filter tab
	 */
	public void refreshFilterTab()
	{
		populateCategoryList(categoryList);
		categoryScroll.getVerticalScrollBar().setValue(0); // Scroll to top after adding element
		this.categoryFilterTab.revalidate();
		this.categoryFilterTab.repaint();
	}
	
	/**
	 * Populate provided JPanel with list of categories
	 * @param categoryListHolder the JPanel to populate
	 */
	public void populateCategoryList(JPanel categoryListHolder){
		// Clear category list panel
		categoryListHolder.removeAll();
		
		// Clear category list array
		selectedCategories.clear();
		checkBoxCategoryMap.clear();
		
		for(Category c : CategoryModel.getInstance().getAllCategories())
		{
			// Check box for current category
			JCheckBox categoryCheckBox = new JCheckBox(c.getName());
			categoryCheckBox.setAlignmentX(BOTTOM_ALIGNMENT);
			categoryCheckBox.setFocusable(false);
			categoryCheckBox.setSelected(true);
			categoryCheckBox.addItemListener(new CheckBoxListener(c));
			
			// Category color indicator for current category
			JPanel categoryColor = new JPanel();
			categoryColor.setPreferredSize(new Dimension(16, 15));
	    	categoryColor.setMaximumSize(new Dimension(16, 15));
	    	categoryColor.setMinimumSize(new Dimension(16, 15));
	    	categoryColor.setLayout(new GridLayout(1,1));
	    	categoryColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	    	categoryColor.setBackground(c.getColor());
	    	categoryColor.setAlignmentX(BOTTOM_ALIGNMENT);
			
			// Container for category color and check box
			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
			container.setAlignmentY(LEFT_ALIGNMENT);
			container.setAlignmentX(BOTTOM_ALIGNMENT);
			container.setBackground(Colors.TABLE_BACKGROUND);
			
			// Store reference to check boxes and categories
			if (categoryCheckBox.isSelected() && !(selectedCategories.contains(c.getCategoryID())))
				selectedCategories.add(c.getCategoryID());
			
			if (!checkBoxCategoryMap.containsKey(categoryCheckBox))
				checkBoxCategoryMap.put(categoryCheckBox, c);
			
			// Set up container UI
			container.add(categoryColor);
			container.add(Box.createHorizontalStrut(2));
			container.add(categoryCheckBox);
			container.add(Box.createHorizontalGlue());
			
			// Add container to category list holder
			categoryListHolder.add(container);
		}
	}
	
	/**
	 * Get the collection of selected categories
	 * @return the collection of selected UUIDs
	 */
	public Collection<UUID> getSelectedCategories()
	{
		return this.selectedCategories;
	}
	
	/**
	 * Custom listener for check boxes
	 *
	 * Maintains the selected category collection updated
	 */
	private class CheckBoxListener implements ItemListener{
		
		private Category referencedCategory;
		
		public CheckBoxListener(Category c) {
			this.referencedCategory = c;
		}
		
		public void itemStateChanged(ItemEvent e) {
			if(((JCheckBox)e.getSource()).isSelected())
			{
				if (! selectedCategories.contains(referencedCategory.getCategoryID()))
					selectedCategories.add(referencedCategory.getCategoryID());
			} else
			{
				if (selectedCategories.contains(referencedCategory.getCategoryID()))
					selectedCategories.remove(referencedCategory.getCategoryID());
			}
		}
	}
	
	/**
	 * Checks all category check boxes and adds UUID to selectedCategories collection
	 */
	public void selectAllCategories()
	{
		// Clear previous selected categories to re-populate list
		selectedCategories.clear();
		
		// Iterate over check boxes and categories, checking them and adding to list
		for (Map.Entry<JCheckBox, Category> entry : checkBoxCategoryMap.entrySet()){
			JCheckBox key = entry.getKey();
			Category value = entry.getValue();
			
			key.setSelected(true);
			if(! selectedCategories.contains(value.getCategoryID()))
					selectedCategories.add(value.getCategoryID());
		}
	}
	
	/**
	 * Un-checks all category check boxes and clears selectedCategories collection
	 */
	public void deselectAllCategories()
	{
		// Clear previous selected categories
		selectedCategories.clear();
		
		// Iterate over check boxes and uncheck them
		for (JCheckBox key : checkBoxCategoryMap.keySet())
			key.setSelected(false);
	}
	
	
}
