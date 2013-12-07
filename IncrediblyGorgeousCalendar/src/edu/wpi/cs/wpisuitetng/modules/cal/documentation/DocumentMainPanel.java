package edu.wpi.cs.wpisuitetng.modules.cal.documentation;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddEventDisplay;

public class DocumentMainPanel extends JPanel{

    private JEditorPane editor;
    private JScrollPane scroll;
    private URL url;
    private static DocumentMainPanel instance;
    public DocumentMainPanel()
    {
    	super();
    	 //set the url
        try {
        	
            url = new URL("file:///C:/Users/Brendan/Desktop/newDocs/GettingStarted.html");
        }
        catch(MalformedURLException mue) {
            JOptionPane.showMessageDialog(null,mue);
        }
        
        //create the JEditorPane
        try {
            editor = new JEditorPane(url);
            
            //set the editor pane to false.
            editor.setEditable(false);
        }
        catch(IOException ioe) {
            JOptionPane.showMessageDialog(null,ioe);
        }
        this.setMaximumSize(new Dimension(400, 1000));
        this.setPreferredSize(new Dimension(400, 1000));
        //create the scroll pane and add the JEditorPane to it.
        scroll = new JScrollPane(editor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        //create the JTextField
        editor.setBackground(Color.getColor("EFEFEF"));
        editor.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                      	try {
                      			if (e.getURL().toString().contains("OpenNewEventWindow"))
                      			{
                      				System.out.println("Action for new Event");
                      				AddEventDisplay ned = new AddEventDisplay();
                    				ned.setTabId(MainPanel.getInstance().addTopLevelTab(ned, "New Event", true));
                      			}
                      			else
                      			{
                      				editor.setPage(e.getURL());
                      				System.out.println(e.getURL().toString());
                      			}
                            }
                            catch(IOException ioe) {
                                JOptionPane.showMessageDialog(null,ioe);
                            }
                    }//end hyperlinkUpdate()
                });//end HyperlinkListener
        this.add(scroll);
    }
    public static DocumentMainPanel getInstance()
	{
		if (instance == null)
		{
			instance = new DocumentMainPanel();
		}
		return instance;
	}

}
