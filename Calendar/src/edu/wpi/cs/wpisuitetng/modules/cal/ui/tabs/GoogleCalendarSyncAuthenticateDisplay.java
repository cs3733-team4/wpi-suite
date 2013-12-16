package edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.google.gdata.util.AuthenticationException;

import edu.wpi.cs.wpisuitetng.modules.cal.models.google.AuthRequest;
import edu.wpi.cs.wpisuitetng.modules.cal.models.google.GoogleSync;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.Pair;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.field.JSearchPasswordField;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.field.JSearchTextField;

public class GoogleCalendarSyncAuthenticateDisplay extends JPanel {
	JSearchTextField username = new JSearchTextField("example@gmail.com", 23);
	JSearchPasswordField password = new JSearchPasswordField("google account password", 23);

	JButton authenticateButton = new JButton("Authenticate");

	JPanel usernamePanel = new JPanel();
	JPanel passwordPanel = new JPanel();
	JPanel buttonPanel = new JPanel();

	JPanel panelContainer = new JPanel();

	public GoogleCalendarSyncAuthenticateDisplay() {
		setLayout(new GridBagLayout());

		panelContainer.setLayout(new GridLayout(3, 1));

		usernamePanel.add(new JLabel("username"));
		usernamePanel.add(username);
		passwordPanel.add(new JLabel("password"));
		passwordPanel.add(password);

		buttonPanel.add(authenticateButton);

		panelContainer.add(usernamePanel);
		panelContainer.add(passwordPanel);
		panelContainer.add(buttonPanel);

		add(panelContainer);
		panelContainer.setOpaque(false);

		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border compound = BorderFactory.createCompoundBorder(raisedbevel,
				loweredbevel);
		panelContainer.setBorder(compound);
		
		
		authenticateButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				MainPanel.getInstance()
						 .setGoogleCalendarSyncer(GoogleSync.getInstance(new AuthRequest()
						 {
							@Override
							public Pair<String, String> getAuthenticationInformation()
							{
								return new Pair<String, String>(username.getText(), password.getText());
							}

							@Override
							public void handleError(AuthenticationException ae)
							{
								//this will error on screen soon
								System.out.println("YOU FAILED AUTHENTICATION");
								password.setText("");
							}

							@Override
							public void handleError(MalformedURLException ae)
							{
								System.out.println("you have a broken google account");
							}

							@Override
							public void succede() {
								//close the tab
								System.out.println("YISSSSS");
							}
							
							
							 
						 }));	 
			}
			
		});
	}
}
