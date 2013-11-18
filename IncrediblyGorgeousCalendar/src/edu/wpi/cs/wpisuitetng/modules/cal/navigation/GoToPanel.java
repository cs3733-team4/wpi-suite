package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class GoToPanel extends JPanel {

	final private static DateTimeFormatter gotoExampleField = DateTimeFormat.forPattern("M/d/yyyy");
	final private static DateTimeFormatter gotoField = DateTimeFormat.forPattern("M/d/yy");
	final private static DateTimeFormatter gotoFieldShort = DateTimeFormat.forPattern("M/d");
	private JLabel gotoErrorText;
	JTextField gotoDateField;
	private DateTime currentDate;
	
	public GoToPanel(DateTime date) {
		
		//TODO max size goto field
		
		currentDate = date;
		
		this.gotoDateField = new JTextField(currentDate.toString(gotoExampleField));
		gotoDateField.setSize(new Dimension(200,200));
		
		// Go to label
		JLabel gotoDateText = new JLabel("Go to: ");
		gotoErrorText = new JLabel(" ");
		gotoErrorText.setHorizontalAlignment(SwingConstants.CENTER);
		gotoErrorText.setForeground(Color.RED);
		
		// Go to button
		JButton updateGotoButton = new JButton(">");
		updateGotoButton.setFocusable(false);
		updateGotoButton.setBackground(UIManager.getDefaults().getColor("Panel.background"));
		updateGotoButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		// Set up listener
		updateGotoButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parseGoto(gotoDateField.getText());
			}
		});
		
		// Set up pane
		this.setLayout(new BorderLayout());
		this.add(gotoDateText, BorderLayout.WEST);
		this.add(gotoDateField, BorderLayout.CENTER);
		this.add(updateGotoButton, BorderLayout.EAST);
		this.add(gotoErrorText, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Parses the input to the goTo field to ensure proper formatting and handle
	 * syntax errors
	 * @param text string to parse
	 */
	private void parseGoto(String text) {
		
		DateTime dt;
		boolean isValidYear = true;
		
		try
		{
			dt = gotoField.parseDateTime(text);
			if(dt.getYear() < 1900 || dt.getYear() > 2100)
			{
				isValidYear=false;
				dt = null;
			}
		}
		
		catch (IllegalArgumentException illArg)
		{
			try
			{
				MutableDateTime mdt = gotoFieldShort.parseMutableDateTime(text);
				mdt.setYear(currentDate.getYear()); // this format does not provide years. add it
				dt = mdt.toDateTime();						
			}
			catch (IllegalArgumentException varArg)
			{
				dt = null;
			}
		}
		/*if (dt != null)
			// mainPanel.display(dt);
			//TODO Wait for Brian
			
		else*/
		{
			if(isValidYear)
				gotoErrorText.setText("Use format: mm/dd/yyyy");
			else
				gotoErrorText.setText("Year out of range (1900-2100)");
		}
	}
	

}