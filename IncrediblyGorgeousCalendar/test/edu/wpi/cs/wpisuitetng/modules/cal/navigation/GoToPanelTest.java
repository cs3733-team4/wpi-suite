package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import static org.junit.Assert.*;

import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.GoToPanel;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoToPanelTest {

        MainPanel dummyPanel;
        
        DateTime now=new DateTime(2000, 6, 1, 0, 0), then, before, after;
        
        
        @BeforeClass public static void setup() {
        	 MainPanel dummyPanel=new MainPanel();
        	 
        	 DateTime now =new DateTime(2000, 6, 1, 0, 0);
             DateTime then =new DateTime(2000, 7, 1, 0, 0);
             DateTime before =new DateTime(1900, 7, 1, 0, 0);
             DateTime after =new DateTime(2100, 7, 1, 0, 0);
        }
        
        
   
        @Test
        public void testExists() {
                GoToPanel GTP=new GoToPanel(now);
                
                assertNotNull("A GoToPanel can be created", GTP);
                assertEquals("It stores the correct daytime when initialized", GTP.getDate(), now);
        }
        
        @Test
        public void testGoto() {
                
                GoToPanel GTP=new GoToPanel(now);
                GTP.parseGoto("07/01/2000");
                // Currently broken; dummyPanel isn't actually initialized, and has nullpointer errors trying to display the correct month on the uninitialized mCalendar
                assertEquals("If the input is acceptable, the gotoDate box function will cause the calendar at mainPanel to display the designated month on the main screen", ((MonthCalendar)MainPanel.getInstance().getMOCA()).getTime().getMonthOfYear(), then.getMonthOfYear());
                GTP.parseGoto("7/1/1900");
                assertEquals("If the input is acceptable, the gotoDate box function will cause the calendar at mainPanel to display the designated month on the main screen", ((MonthCalendar)MainPanel.getInstance().getMOCA()).getTime().getMonthOfYear(), before.getMonthOfYear());
                GTP.parseGoto("7/1/2100");
                assertEquals("If the input is acceptable, the gotoDate box function will cause the calendar at mainPanel to display the designated month on the main screen", ((MonthCalendar)MainPanel.getInstance().getMOCA()).getTime().getMonthOfYear(), after.getMonthOfYear());
                GTP.parseGoto("7/1/00");
                assertEquals("If the input is acceptable, the gotoDate box function will cause the calendar at mainPanel to display the designated month on the main screen", ((MonthCalendar)MainPanel.getInstance().getMOCA()).getTime().getMonthOfYear(), before.getMonthOfYear());
                
        }
        
        @Test
        public void testGotoErrorEarly() {
                GoToPanel GTP=new GoToPanel(now);
                GTP.parseGoto("6/1/1800");
                assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Year out of range (1900-2100)");
                GTP.parseGoto("6/1/1899");
                assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Year out of range (1900-2100)");
                GTP.parseGoto("12/31/1899");
                assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Year out of range (1900-2100)");
                GTP.parseGoto("12/12/-1000");
                assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Year out of range (1900-2100)");
        }
        
        @Test
        public void testGotoErrorLate() {
                GoToPanel GTP=new GoToPanel(now);
                GTP.parseGoto("6/1/2200");
                assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Year out of range (1900-2100)");
                GTP.parseGoto("6/1/2101");
                assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Year out of range (1900-2100)");
                GTP.parseGoto("1/1/2101");
                assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Year out of range (1900-2100)");
                GTP.parseGoto("12/12/99999");
                assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Year out of range (1900-2100)");
        }
        
        @Test
        public void testGotoErrorFormat() {
                GoToPanel GTP=new GoToPanel(now);
                GTP.parseGoto("12 1 2000");
                assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Use format: mm/dd/yyyy");
                
                // Note: the error message displayed is saved in the GoToPanel, but refreshing the display for a correct input will remove the error message display.
                // This means we have to test with a new GTP each time.
                
                GoToPanel GTP1=new GoToPanel(now);
                GTP1.parseGoto("12.12.2000");
                assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP1.getError(), "* Use format: mm/dd/yyyy");
                
                GoToPanel GTP2=new GoToPanel(now);
                GTP2.parseGoto("August Nineteenth, Two-thousand Thirteen");
                assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP2.getError(), "* Use format: mm/dd/yyyy");
                
                GoToPanel GTP3=new GoToPanel(now);
                GTP3.parseGoto("12/2000");
                assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP3.getError(), "* Use format: mm/dd/yyyy");
        }
        
        @Test
        public void testGotoErrorInput() {
                GoToPanel GTP=new GoToPanel(now);
                GTP.parseGoto("12/0/2000");
                assertEquals("If the input isn't an acceptable mm/dd/yyyy , the gotoDate box function will cause an error to display on the gotoErrorText box", GTP.getError(), "* Use format: mm/dd/yyyy");
                
                // Note: the error message displayed is saved in the GoToPanel, but refreshing the display for a correct input will remove the error message display.
                // This means we have to test with a new GTP each time.
                
                GoToPanel GTP1=new GoToPanel(now);
                GTP1.parseGoto("0/31/2000");
                assertEquals("If the input isn't an acceptable mm/dd/yyyy, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP1.getError(), "* Use format: mm/dd/yyyy");
                
                GoToPanel GTP2=new GoToPanel(now);
                GTP2.parseGoto("-1/-1/2000");
                assertEquals("If the input isn't an acceptable mm/dd/yyyy, the gotoDate box function will cause an error to display on the gotoErrorText box", GTP2.getError(), "* Use format: mm/dd/yyyy");
                
                GoToPanel GTP3=new GoToPanel(now);
                GTP3.parseGoto("31/12/2000");
                assertEquals("We do not accept the English version of dd/m/yyyy, regardless of how amazingly logical it is", GTP3.getError(), "* Use format: mm/dd/yyyy");
        }

}