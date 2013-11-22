package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import javax.swing.Box.Filler;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCommitmentDisplay extends JPanel
{
	private JTextField Name;
	private JTextField Participants;
	private int tabid;
	
	public AddCommitmentDisplay()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		final JPanel me = this;
		JPanel NameLabelPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) NameLabelPanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		add(NameLabelPanel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setVerticalAlignment(SwingConstants.BOTTOM);
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		NameLabelPanel.add(lblName);
		
		JPanel NamePane = new JPanel();
		FlowLayout fl_NamePane = (FlowLayout) NamePane.getLayout();
		fl_NamePane.setAlignment(FlowLayout.LEFT);
		add(NamePane);
		
		Name = new JTextField();
		NamePane.add(Name);
		Name.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Name.setColumns(25);
		
		JPanel DateLabelPane = new JPanel();
		add(DateLabelPane);
		DateLabelPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblDateTime = new JLabel("Date and Time:");
		lblDateTime.setVerticalAlignment(SwingConstants.BOTTOM);
		lblDateTime.setHorizontalAlignment(SwingConstants.LEFT);
		DateLabelPane.add(lblDateTime);
		
		JPanel CommitDatePickerPanel = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) CommitDatePickerPanel.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		add(CommitDatePickerPanel);
		final CommitmentDatePicker commitTime = new CommitmentDatePicker(true, null);
		CommitDatePickerPanel.add(new JLabel("Time: "));
		CommitDatePickerPanel.add(commitTime);
		
		JPanel ParticipantsLabelPane = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) ParticipantsLabelPane.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(ParticipantsLabelPane);
		
		JLabel lblParticipants = new JLabel("Participants:");
		lblParticipants.setVerticalAlignment(SwingConstants.BOTTOM);
		ParticipantsLabelPane.add(lblParticipants);
		
		JPanel ParticipantsPanel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) ParticipantsPanel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		add(ParticipantsPanel);
		
		Participants = new JTextField();
		ParticipantsPanel.add(Participants);
		Participants.setColumns(30);
		
		JPanel DescriptionLabelPane = new JPanel();
		FlowLayout fl_DescriptionLabelPane = (FlowLayout) DescriptionLabelPane.getLayout();
		fl_DescriptionLabelPane.setAlignment(FlowLayout.LEFT);
		add(DescriptionLabelPane);
		final JCheckBox chckbxProjectEvent = new JCheckBox("Project Event");
		chckbxProjectEvent.setSelected(true);
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setVerticalAlignment(SwingConstants.BOTTOM);
		DescriptionLabelPane.add(lblDescription);
		lblDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblDescription.setHorizontalAlignment(SwingConstants.LEFT);
		
		JPanel DescriptionPanel = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) DescriptionPanel.getLayout();
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		DescriptionPanel.setMinimumSize(new Dimension(100, 100));
		add(DescriptionPanel);
        Filler filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767), new java.awt.Dimension(0, 32767));
        
        add(filler1);
		
		final JTextArea Description = new JTextArea(5,35);
		Description.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Description.setLineWrap(true);
		Description.setWrapStyleWord(true);
		
		JScrollPane descriptionScrollPane = new JScrollPane(Description);
		DescriptionPanel.add(descriptionScrollPane);
		
		JPanel SubmitPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) SubmitPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(SubmitPanel);
		
		final JLabel errorText = new JLabel();
		errorText.setForeground(Color.RED);
		errorText.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 13));
		
		final JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					commitTime.getDate();
					errorText.setVisible(true);
					
					if (Name.getText() == null || Name.getText().trim().length() == 0)
					{
						errorText.setText("* Please enter a commitment title");
					}
					else
					{
						errorText.setVisible(false);
						Event e = new Event();
						e.setName(Name.getText().trim());
						e.setDescription(Description.getText());
						e.setStart(commitTime.getDate());
						e.setProjectEvent(chckbxProjectEvent.isSelected());
						MainPanel.getInstance().addEvent(e);
						btnSave.setEnabled(false);
						btnSave.setText("Saved!");
						MainPanel.getInstance().closeTab(tabid);
						MainPanel.getInstance().refreshView();
					}
				}
				catch (IllegalArgumentException exception)
				{
					errorText.setText("* Invalid Date");
					errorText.setVisible(true);
				}
			}
		});
		btnSave.setHorizontalAlignment(SwingConstants.LEFT);
		SubmitPanel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				MainPanel.getInstance().closeTab(tabid);
			}
		});
		SubmitPanel.add(btnCancel);
		SubmitPanel.add(chckbxProjectEvent);
		SubmitPanel.add(errorText);
	}
	
	public void setTabId(int id)
	{
		tabid = id;
	}
}
