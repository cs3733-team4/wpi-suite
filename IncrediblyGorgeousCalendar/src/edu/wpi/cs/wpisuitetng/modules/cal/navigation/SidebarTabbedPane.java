package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class SidebarTabbedPane extends JTabbedPane{
	
	private JTextArea detailTab;
	private JTextArea commitmentTab;
	private DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("hh:mm aa");
	private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yy");
	public SidebarTabbedPane() {
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		detailTab = new JTextArea();
		detailTab.setEditable(false);
		detailTab.setLineWrap(true);
		detailTab.setCursor(null);  
		detailTab.setOpaque(false);  
		detailTab.setFocusable(false);
	    detailTab.setWrapStyleWord(true);
	    detailTab.setFont(new Font("Tahoma", Font.PLAIN, 13));
	    detailTab.putClientProperty("html.disable", true);
	    
	    JScrollPane detailsScrollPane = new JScrollPane(detailTab);
	    detailsScrollPane.setBorder( new EmptyBorder(5,5,5,5));
	    
		commitmentTab = new JTextArea();
		//detailTab.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.addTab("Details", detailsScrollPane);
		this.addTab("Commitments", commitmentTab);
	}
	
	public void showDetails(Displayable mDisplayable)
	{
		if (mDisplayable instanceof Event)
		{
			detailTab.setText(mDisplayable.getName() + "\n" + 
					((Event) mDisplayable).getStart().toString(timeFormatter) + " - " +
					((Event) mDisplayable).getEnd().toString(timeFormatter) + "\n" + 
					mDisplayable.getDescription());
		}else if (mDisplayable instanceof Commitment)
		{
			detailTab.setText(mDisplayable.getName() + "\n" + 
					((Commitment) mDisplayable).getDate().toString(dateFormatter) + "\n" +
					mDisplayable.getDescription());
		}
	}

	public void clearDetails() {
		detailTab.setText("");		
	}
}
