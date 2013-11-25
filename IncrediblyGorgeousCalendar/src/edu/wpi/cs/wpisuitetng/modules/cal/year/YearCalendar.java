package edu.wpi.cs.wpisuitetng.modules.cal.year;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.VerticalLabelUI;

public class YearCalendar extends JPanel
{
	
	public YearCalendar(DateTime dt)
	{
		MutableDateTime jan1 = new MutableDateTime(new DateTime(dt.getYear(), 1, 1, 1, 1));
		jan1.addDays(0-(jan1.getDayOfWeek()%7));
		this.setLayout(new BorderLayout());
		drawCalendar(jan1);
	}
	
	
	
	
	private void drawCalendar(MutableDateTime mdt)
	{
		this.removeAll();
		
		JLabel title = new JLabel(mdt.getYear()+"");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("DejaVu Sans", Font.BOLD, 25));
		JPanel content = new JPanel();
		
		this.add(title, BorderLayout.NORTH);
		this.add(content, BorderLayout.CENTER);
		
		MutableDateTime monthCounter = mdt.copy();
		
		content.setLayout(new BoxLayout(content, 0));
		for(int i = 0; i < 3; i++)
		{
			content.add(new Box.Filler(new Dimension(0,0), new Dimension(0,0), new Dimension(65566, 0)));
			content.add(getFourMonthLabel(monthCounter.copy()));
			content.add(getFourMonthGrid(mdt.copy()));
			content.add(new Box.Filler(new Dimension(0,0), new Dimension(0,0), new Dimension(65566, 0)));
			mdt.addDays(18*7);
			monthCounter.addMonths(4);
		}
	}
	
	private JPanel getFourMonthLabel(MutableDateTime start)
	{
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(4, 1));
		for(int i = 0; i < 4; i++)
		{
			start.addMonths(1);
			JLabel l = new JLabel(start.monthOfYear().getAsText());
			l.setHorizontalAlignment(SwingConstants.CENTER);
			l.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
			l.setUI(new VerticalLabelUI(false));
			
			p.add(l);
			
		}
		
		int width = 50;
		int height = 570;
		
		p.setMinimumSize(new Dimension(width, height));
		p.setPreferredSize(new Dimension(width, height));
		p.setMaximumSize(new Dimension(width, height));
		
		return p;
	}
	
	
	private JPanel getFourMonthGrid(MutableDateTime start)
	{
		int gridHeight = 19;
		int gridWidth = 7;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(gridHeight, gridWidth));
		
		String[] days = {"S", "M", "T", "W", "R", "F", "S"};
		for(int i = 0; i < 7; i++)
		{
			JLabel header = new JLabel(days[i]);
			header.setBorder(BorderFactory.createMatteBorder(1, i==0?1:0, 1, 1, Color.BLACK));
			header.setHorizontalAlignment(SwingConstants.CENTER);
			p.add(header);
		}
		
		for(int j = 0; j < 18*7; j++)
		{
			Color dayBackground = start.getMonthOfYear()%2==0? Color.LIGHT_GRAY : Color.gray;
			
			YearlyDayHolder day = new YearlyDayHolder(start.toDateTime(), dayBackground);
			JLabel dayLabel = new JLabel(start.getDayOfMonth()+"");
			dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
			
			day.setLayout(new GridLayout(1,1));
			day.add(dayLabel);
			day.setBorder(BorderFactory.createMatteBorder(0, start.getDayOfWeek()%7==0?1:0, 1, 1, Color.BLACK));
			
			day.addMouseListener(new MouseListener(){
				
				@Override
				public void mouseClicked(MouseEvent me) {
					// possibly go to this day or something?
					// TODO: make it happen
				}

				@Override
				public void mouseEntered(MouseEvent me) {
					// this is just a demo of what it can do
					JPanel event = (JPanel)(me.getSource());
					event.setBackground(Color.green);
				}

				@Override
				public void mouseExited(MouseEvent me) {
					YearlyDayHolder event = (YearlyDayHolder)(me.getSource());
					event.resetColor();
				}

				@Override
				public void mousePressed(MouseEvent me) {
					
				}
				
				@Override
				public void mouseReleased(MouseEvent me) {
					YearlyDayHolder event = (YearlyDayHolder)(me.getSource());
					JOptionPane.showMessageDialog(null, event.getDateTime().toString());
				}
				
			});
			
			p.add(day);
			start.addDays(1);
		}
		
		int width = 280;
		int height = 570;
		
		p.setMinimumSize(new Dimension(width, height));
		p.setPreferredSize(new Dimension(width, height));
		p.setMaximumSize(new Dimension(width, height));
		
		
		return p;
	}
	
	public class YearlyDayHolder extends JPanel
	{
		private DateTime dt;
		private Color defaultBackground;
		
		public YearlyDayHolder(DateTime dt, Color dayBackground)
		{
			this.dt = dt;
			this.defaultBackground = dayBackground;
			this.resetColor();
		}
		
		public void resetColor()
		{
			this.setBackground(this.defaultBackground);
		}
		
		public DateTime getDateTime()
		{
			return this.dt;
		}
	}
	
	
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setLayout(new GridLayout(1,1));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(new Dimension(600,400));
		
		f.add(new YearCalendar(DateTime.now()));
		f.pack();
		f.setVisible(true);
	}
}