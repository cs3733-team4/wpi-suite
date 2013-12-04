package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.swing.JLabel;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.GoToPanel;

import org.joda.time.DateTime;
import org.junit.Test;

public class GoToPanelTest {

        MainPanel dummyPanel;
        
        DateTime now=new DateTime(2000, 6, 1, 0, 0), then, before, after;
        
        
   
        @Test
        public void testExists() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
                GoToPanel GTP=new GoToPanel(now);
                
                Field f= GTP.getClass().getDeclaredField("currentDate");
                f.setAccessible(true);
                
                assertNotNull("A GoToPanel can be created", GTP);
                assertEquals("It stores the correct daytime when initialized", f.get(GTP), now);
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
        public void testGotoErrorEarly() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
                GoToPanel GTP=new GoToPanel(now);
                
                Field f= GTP.getClass().getDeclaredField("gotoErrorText");
                f.setAccessible(true);
                
                assertEquals("The default error message is a single space character for a new GoToPanel", " ",((JLabel) f.get(GTP)).getText());
                
                GTP.parseGoto("6/1/1800");
                assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Year out of range (1900-2100)", ((JLabel) f.get(GTP)).getText());
                
                // The error text will remain in the GoToPanel even if a correct date is given after the error; it will simply stop displaying the error message until a new error occurs
                // It is therefore necessary to create a new GoToPanel for each test, so the error text will reset back to "  "
                GTP=new GoToPanel(now);
                GTP.parseGoto("6/1/1899");
                assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Year out of range (1900-2100)", ((JLabel) f.get(GTP)).getText());
               
                GTP=new GoToPanel(now);
                GTP.parseGoto("12/31/1899");
                assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Year out of range (1900-2100)", ((JLabel) f.get(GTP)).getText());
                
                GTP=new GoToPanel(now);
                GTP.parseGoto("12/12/-1000");
                assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Year out of range (1900-2100)", ((JLabel) f.get(GTP)).getText());
        }
        
        @Test
        public void testGotoErrorLate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
                GoToPanel GTP=new GoToPanel(now);
                
                Field f= GTP.getClass().getDeclaredField("gotoErrorText");
                f.setAccessible(true);
                
                GTP.parseGoto("6/1/2200");
                assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Year out of range (1900-2100)", ((JLabel) f.get(GTP)).getText());
                
                GTP=new GoToPanel(now);
                GTP.parseGoto("6/1/2101");
                assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Year out of range (1900-2100)", ((JLabel) f.get(GTP)).getText());
               
                GTP=new GoToPanel(now);
                GTP.parseGoto("1/1/2101");
                assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Year out of range (1900-2100)", ((JLabel) f.get(GTP)).getText());
               
                GTP=new GoToPanel(now);
                GTP.parseGoto("12/12/99999");
                assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Year out of range (1900-2100)", ((JLabel) f.get(GTP)).getText());
        }
        
        @Test
        public void testGotoErrorFormat() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
                GoToPanel GTP=new GoToPanel(now);
                
                Field f= GTP.getClass().getDeclaredField("gotoErrorText");
                f.setAccessible(true);
                
                GTP.parseGoto("12 1 2000");
                assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Use format: mm/dd/yyyy", ((JLabel) f.get(GTP)).getText());

                GTP=new GoToPanel(now);
                GTP.parseGoto("12.12.2000");
                assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Use format: mm/dd/yyyy", ((JLabel) f.get(GTP)).getText());
                
                GTP=new GoToPanel(now);
                GTP.parseGoto("August Nineteenth, Two-thousand Thirteen");
                assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Use format: mm/dd/yyyy", ((JLabel) f.get(GTP)).getText());
                
                GTP=new GoToPanel(now);
                GTP.parseGoto("12/2000");
                assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Use format: mm/dd/yyyy", ((JLabel) f.get(GTP)).getText());
        }
        
        @Test
        public void testGotoErrorInput() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
                GoToPanel GTP=new GoToPanel(now);
                
                Field f= GTP.getClass().getDeclaredField("gotoErrorText");
                f.setAccessible(true);
                
                GTP.parseGoto("12/0/2000");
                assertEquals("If the input isn't an acceptable mm/dd/yyyy , the gotoDate box function will cause an error to display on the gotoErrorText box", "* Use format: mm/dd/yyyy", ((JLabel) f.get(GTP)).getText());
                
                GTP=new GoToPanel(now);
                GTP.parseGoto("0/31/2000");
                assertEquals("If the input isn't an acceptable mm/dd/yyyy, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Use format: mm/dd/yyyy", ((JLabel) f.get(GTP)).getText());
                
                GTP=new GoToPanel(now);
                GTP.parseGoto("-1/-1/2000");
                assertEquals("If the input isn't an acceptable mm/dd/yyyy, the gotoDate box function will cause an error to display on the gotoErrorText box", "* Use format: mm/dd/yyyy", ((JLabel) f.get(GTP)).getText());
                
                GTP=new GoToPanel(now);
                GTP.parseGoto("31/12/2000");
                assertEquals("We do not accept the English version of dd/m/yyyy, regardless of how amazingly logical it is", "* Use format: mm/dd/yyyy", ((JLabel) f.get(GTP)).getText());
        }

}