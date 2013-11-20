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

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;

@SuppressWarnings("serial")
public class GoToPanel extends JPanel {

	final private static DateTimeFormatter gotoExampleField = DateTimeFormat.forPattern("M/d/yyyy");
	final private static DateTimeFormatter gotoField = DateTimeFormat.forPattern("M/d/yy");
	final private static DateTimeFormatter gotoFieldShort = DateTimeFormat.forPattern("M/d");
	private JLabel gotoErrorText;
	private JLabel gotoDateText;
	private JTextField gotoDateField;
	private JButton updateGotoButton;
	private DateTime currentDate;
	private AbstractCalendar mCalendar;
	
	public GoToPanel(DateTime date, AbstractCalendar mCalendar) {
		
		JPanel top = new JPanel();
		JPanel bot = new JPanel();
		
		
		this.currentDate = date;
		this.mCalendar = mCalendar;
		this.setBorder(new EmptyBorder(5, 0, 0, 0));

		// Go to field
		this.gotoDateField = new JTextField(currentDate.toString(gotoExampleField));
		
		// Go to label
		gotoDateText = new JLabel("Go to: ");
		
		// Go to error label
		gotoErrorText = new JLabel(" ");
		gotoErrorText.setHorizontalAlignment(SwingConstants.CENTER);
		gotoErrorText.setForeground(Color.RED);
		
		// Go to button
		updateGotoButton = new JButton(">");
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
		
		gotoDateField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parseGoto(gotoDateField.getText());
			}
		});
		
		// Set up pane
		top.setLayout(new BorderLayout());
		bot.setLayout(new BorderLayout());
		
		this.setLayout(new BorderLayout());
		
		top.add(gotoDateText, BorderLayout.WEST);
		top.add(gotoDateField, BorderLayout.CENTER);
		top.add(updateGotoButton, BorderLayout.EAST);

		bot.add(gotoErrorText);
		top.add(bot, BorderLayout.SOUTH);
		
		this.add(top, BorderLayout.NORTH);
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
		if (dt != null)
		{
			mCalendar.display(dt);
			MainPanel.getInstance().refreshView();
		}else
		{
			if(isValidYear)
				gotoErrorText.setText("* Use format: mm/dd/yyyy");
			else
				gotoErrorText.setText("* Year out of range (1900-2100)");
		}
	}
	
	public void displayGoto(DateTime mDateTime)
	{
		gotoDateField.setText(mDateTime.toString(gotoExampleField));
	}

}

