package edu.wpi.cs.wpisuitetng.modules.cal.documentation;

import java.awt.BorderLayout;
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

    private JEditorPane webPage;
    private JScrollPane scroll;
    private URL url;
    private static DocumentMainPanel instance;
    public DocumentMainPanel()
    {
    	super();
    	this.setLayout(new BorderLayout());
    	 //set the url
        try {
        	
            //url = new URL("file:///C:/Users/Brendan/Desktop/newDocs/GettingStarted.html");
            url = new URL("file:///C:/Users/Brendan/Desktop/newDocs/YOCO Calendar.html");
        }
        catch(MalformedURLException mue) {
            JOptionPane.showMessageDialog(null,mue);
        }
        
        //create the JEditorPane
        try {
            webPage = new JEditorPane(url);
            
            //set the editor pane to false.
            webPage.setEditable(false);
        }
        catch(IOException ioe) {
            JOptionPane.showMessageDialog(null,ioe);
        }
        //create the scroll pane and add the JEditorPane to it.
        scroll = new JScrollPane(webPage);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        //create the JTextField
        webPage.setBackground(Color.getColor("EFEFEF"));
        webPage.addHyperlinkListener(new HyperlinkListener() {
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
                      				webPage.setPage(e.getURL());
                      				System.out.println(e.getURL().toString());
                      			}
                            }
                            catch(IOException ioe) {
                                JOptionPane.showMessageDialog(null,ioe);
                            }
                    }//end hyperlinkUpdate()
                });//end HyperlinkListener
        this.add(scroll, BorderLayout.CENTER);
    }
    public static DocumentMainPanel getInstance()
	{
		if (instance == null)
		{
			instance = new DocumentMainPanel();
		}
		return instance;
	}
    
    @Override
    public void setVisible(boolean vis)
    {
    	super.setVisible(vis);
    	if (vis)
    		this.setPreferredSize(new Dimension(200, 1000));
    }
    public void goToPage(String Page)
    {
    	try {
			webPage.setPage(new URL(Page));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

}
