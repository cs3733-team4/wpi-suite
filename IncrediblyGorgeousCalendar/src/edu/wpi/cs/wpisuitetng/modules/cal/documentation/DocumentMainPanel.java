package edu.wpi.cs.wpisuitetng.modules.cal.documentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class DocumentMainPanel extends JFrame{

    private JEditorPane webPage;
    private JScrollPane scroll;
    private URL url;
    private String serverLocation;
    private TableOfContents tableOfContents;
    private static DocumentMainPanel instance;
    private DocumentMainPanel()
    {
    	super();
    	
    	this.setLayout(new BorderLayout());
    	
    	//serverLocation = Network.getInstance().makeRequest("docs/Calendar/", HttpMethod.GET).getUrl().toString().replace("API/", "");
        serverLocation = "http://www.wpi.edu/~bkmcleod/newDocs/";

    	tableOfContents=new TableOfContents(serverLocation);
    	try
        {
        	url = new URL(serverLocation + "Introduction.html");
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
        
        //create the JTextField that shows the HTML Page
        webPage.setBackground(Color.getColor("EFEFEF"));
        webPage.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                      	try {
                      			if (!doAction(e.getURL().toString()))
                      				webPage.setPage(e.getURL());
                            }
                            catch(IOException ioe) {
                                JOptionPane.showMessageDialog(null,ioe);
                            }
                    }//end hyperlinkUpdate()
                });//end HyperlinkListener

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add(tableOfContents);
        splitPane.add(scroll);
        this.add(splitPane, BorderLayout.CENTER);
    }
    
    /**
     * doAction will return true if there is/was an action committed as a result of the link
     * @param actionPath The URL Path that is requested
     * @return if an action was completed
     */
    private boolean doAction(String actionPath)
    {
    	
    	if (actionPath.contains("#OpenNewEventWindow"))
		{
			System.out.println("Action for new Event");
			AddEventDisplay ned = new AddEventDisplay();
			ned.setTabId(MainPanel.getInstance().addTopLevelTab(ned, "New Event", true));
			return true;
		}
    	else if (actionPath.contains("#SaveNewEvent"))
		{
    		if (MainPanel.getInstance().getSelectedComponent() instanceof AddEventDisplay)
    		{
    			System.out.println("Could try to save a new event!!");
    			AddEventDisplay ned = (AddEventDisplay) MainPanel.getInstance().getSelectedComponent();
    			ned.attemptSave();
    		}
			return true;
		}
<<<<<<< HEAD
    	
    	if (actionPath.contains("#"))
    	{
    		System.out.println("Action: " + actionPath + " not yet implemented!");
=======
    	else if (actionPath.contains("#OpenNewAddCommitmentBox"))
    	{
    		System.out.println("Action for new Commitment");
    		AddCommitmentDisplay ncm = new AddCommitmentDisplay();
    		ncm.setTabId(MainPanel.getInstance().addTopLevelTab(ncm, "New Commitment", true));
>>>>>>> ab1e37637de8af04f7abe22f35e8f5ca044eb41d
    		return true;
    	}
    	return false;
    }
    
    /**
     * Allows for singleton of DocumentMainPanel
     * @return the only existing instance of DocumentMainPanel
     */
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
    		this.setSize(800, 600);
    }
    
    /**
     * Navigates the documentation view to a specific page
     * @param page the HTML page to navigate to. DO NOT INCLUDE THE SERVER PATH!!!!
     */
    public void goToPage(String page)
    {
    	try {
			webPage.setPage(new URL(serverLocation + page));
			System.out.println(webPage.getPage().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }

}
