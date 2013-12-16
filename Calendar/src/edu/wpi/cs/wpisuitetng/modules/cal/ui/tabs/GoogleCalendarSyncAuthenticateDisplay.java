package edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class GoogleCalendarSyncAuthenticateDisplay extends JPanel {
	JTextField username = new JTextField(13);
	JPasswordField password = new JPasswordField(13);

	JButton l = new JButton("Authenticate");

	JPanel u = new JPanel();
	JPanel p = new JPanel();
	JPanel b = new JPanel();

	JPanel con = new JPanel();

	public GoogleCalendarSyncAuthenticateDisplay() {
		setLayout(new GridBagLayout());

		con.setLayout(new GridLayout(3, 1));

		u.add(new JLabel("username"));
		u.add(username);
		p.add(new JLabel("password"));
		p.add(password);

		b.add(l);

		con.add(u);
		con.add(p);
		con.add(b);

		add(con);
		con.setOpaque(false);

		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border compound = BorderFactory.createCompoundBorder(raisedbevel,
				loweredbevel);
		con.setBorder(compound);
		
		
		l.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(null, "Eggs are not supposed to be green.");
			}
			
		});
	}
}
