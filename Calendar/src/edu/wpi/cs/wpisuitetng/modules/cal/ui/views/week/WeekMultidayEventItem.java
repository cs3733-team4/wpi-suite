/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.week;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class WeekMultidayEventItem extends JLabel {

	Event mEvent;
	boolean isSpacer = false;
	MultidayEventItemType type;

	int rows = 0;

	public WeekMultidayEventItem(Event event, MultidayEventItemType type) {
		this.mEvent = event;
		this.type = type;
		this.setOpaque(true);

		setupListeners();
	}

	public WeekMultidayEventItem(Event event, MultidayEventItemType type, String itemText) {
		this.mEvent = event;
		this.type = type;
		this.setText(itemText);
		this.setOpaque(true);

		setupListeners();
	}

	private void setupListeners() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() > 1) {
					MainPanel.getInstance().editSelectedDisplayable(mEvent);
				} else {
					MainPanel.getInstance().updateSelectedDisplayable(mEvent);
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				System.out.print(type);
			}
		});
	}

	public Event getEvent() {
		return mEvent;
	}

	public void setEvent(Event mEvent) {
		this.mEvent = mEvent;
	}

	public void setDynamicBorder(Color mColor, boolean isSelected) {
		if(isSpacer)
			this.setBorder(BorderFactory.createMatteBorder(rows == 0 ? 1 : 0,
					type == MultidayEventItemType.Start ? 1 : 0, 
					1,
					type == MultidayEventItemType.End ? 1 : 0, mColor));
		else
			this.setBorder(new CompoundBorder(
								BorderFactory.createMatteBorder(
									rows == 0 ? 1 : 0, 
									type == MultidayEventItemType.Start ? 1 : 0, 1,
									type == MultidayEventItemType.End ? 1 : 0, mColor), 
								new CompoundBorder(
										BorderFactory.createMatteBorder(2,
												type == MultidayEventItemType.Start ? 2 : 0, 
												2,
												type == MultidayEventItemType.End ? 2 : 0,
												isSelected ? Colors.SELECTED_BACKGROUND
												: mEvent.getColor()), 
										new EmptyBorder(2, 2, 2, 2))));
	}

	public void setSelected(boolean selected) {
		setDynamicBorder(mEvent.getColor().darker(), selected);
	}
	
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public boolean isSpacer() {
		return isSpacer;
	}

	public void setSpacer(boolean isSpacer) {
		this.isSpacer = isSpacer;
	}
	
	public MultidayEventItemType getType() {
		return type;
	}

	public void setType(MultidayEventItemType type) {
		this.type = type;
	}
}
