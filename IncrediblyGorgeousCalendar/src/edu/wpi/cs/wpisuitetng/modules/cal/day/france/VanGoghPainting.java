package edu.wpi.cs.wpisuitetng.modules.cal.day.france;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

import javax.swing.JLabel;
import javax.swing.BoxLayout;

import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.SwingConstants;

public class VanGoghPainting extends JPanel
{
	final long millisInDay = 86400000;
	int lastWidth = 0;
	Rational Width;
	Rational X;
	Event event;
	FontMetrics titleMetrics;
	FontMetrics descriptionMetrics;
	JLabel lblEventTitle;
	JLabel lblStarryNightdutch;
	
	public VanGoghPainting(TimeTraveller traveller)
	{
		event = traveller.getEvent();
		Color bg = event.getColor();
		setBorder(new CompoundBorder(new LineBorder(Colors.TABLE_BACKGROUND), new CompoundBorder(new LineBorder(bg.darker()), new EmptyBorder(6, 6, 6, 6))));
		setBackground(bg);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lblEventTitle = new JLabel(event.getName());
		add(lblEventTitle);
		lblEventTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		JLabel lblTimeInfo = new JLabel(formatTime(event.getStart()) + " - " + formatTime(event.getEnd()));
		lblTimeInfo.setBorder(new EmptyBorder(0,0,3,0));
		lblTimeInfo.setMaximumSize(new Dimension(32767, 20));
		lblTimeInfo.setFont(new Font("Tahoma", Font.ITALIC, 14));
		add(lblTimeInfo);
		lblStarryNightdutch = new JLabel();
		add(lblStarryNightdutch);
		lblStarryNightdutch.setVerticalAlignment(SwingConstants.TOP);
		lblStarryNightdutch.setBackground(bg);
		lblStarryNightdutch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStarryNightdutch.setText("<html>" + event.getDescription()+"</html>");
		lblStarryNightdutch.setMinimumSize(new Dimension(0,0));
		Width = new Rational(((traveller.getCollisions() > 1) ? 2 : 1), 1 + traveller.getCollisions());
		X = traveller.getXpos();
		
		if (X.toInt(10000) + Width.toInt(10000) > 10000)
			Width = new Rational(1,Width.getDenominator());
		recalcBounds(200, 1400);
	}
	
	@Override
	public void paint(Graphics g)
	{
		recalcBounds(getParent().getWidth(), getParent().getHeight());
		super.paint(g);
	}
	
	@Override
	public void doLayout()
	{
		int width = this.getParent().getWidth();
		if (recalcBounds(width, getParent().getHeight()))
			super.doLayout();
	}
	
	private boolean recalcBounds(int width, int parentHeight)
	{
		if (width != lastWidth)
		{
			lastWidth = width;
		}
		else
		{
			return false;
		}
		int height = (int) map(new Interval(event.getStart(), event.getEnd()).toDurationMillis(), parentHeight);
		lblStarryNightdutch.setMaximumSize(new Dimension(Width.toInt(width), height-20));
		int outWidth = Width.toInt(width);
		this.setBounds(X.toInt(width), (int) map(event.getStart().getMillisOfDay(), parentHeight), outWidth, height);
		return true;
	}
		
	private long map(long num, long high){
		return (long)(num/(double)millisInDay * high);
	}
	
	public String formatTime(DateTime when)
	{
		String ret;
		boolean pm = when.getHourOfDay() >= 12;
		if (when.getHourOfDay() == 0)
			ret = "12";
		else if (when.getHourOfDay() > 12)
			ret = Integer.toString(when.getHourOfDay() - 12);
		else
			ret = Integer.toString(when.getHourOfDay());

		if (when.getMinuteOfHour() != 0)
		{
			ret += ":";
			if (when.getMinuteOfHour() < 10)
				ret += "0";
			ret += Integer.toString(when.getMinuteOfHour());
		}
		
		if (pm)
			ret += "p";

		return ret;
	}
}
