/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.IterationModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.overview.OverviewButtonPanel;

public class NewBarChartPanel extends JScrollPane {
private static String title;
		public NewBarChartPanel(String title){
			NewBarChartPanel.title = title;
			JPanel panel = new JPanel();
			OverviewButtonPanel buttons = new OverviewButtonPanel();
			panel.add(createPanel(), BorderLayout.CENTER);
			panel.add(buttons, BorderLayout.SOUTH);
			
			
			this.setViewportView(panel);
		}
		
		private static CategoryDataset setData() {
			if (title.equals("Iteration")) {
				return setDataIteration();
			} else if (title.equals("Status")) {
				return setDataStatus();
			} else {
				return setDataAssignTo();
			}

		}
		private static CategoryDataset setDataStatus() {
			int numStatusNew = 0;
			int numStatusDeleted = 0;
			int numStatusInprogress = 0;
			int numStatusComplete = 0;
			int numStatusOpen = 0;
			DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
			List<Requirement> requirements = RequirementModel.getInstance().getRequirements();// list of requirements
			for (int i = 0; i < requirements.size(); i++) {
				if (requirements.get(i).getStatus() == RequirementStatus.NEW) {
					numStatusNew += 1;
				} else if (requirements.get(i).getStatus() == RequirementStatus.DELETED) {
					numStatusDeleted += 1;
				} else if (requirements.get(i).getStatus() == RequirementStatus.INPROGRESS) {
					numStatusInprogress += 1;
				} else if (requirements.get(i).getStatus() == RequirementStatus.COMPLETE) {
					numStatusComplete += 1;
				} else {
					numStatusOpen += 1;
				}
			}
			dataSet.setValue(numStatusNew, "New", "Status");
			dataSet.setValue(numStatusDeleted, "Deleted", "Status");
			dataSet.setValue(numStatusInprogress, "In Progress", "Status");
			dataSet.setValue(numStatusComplete, "Complete", "Status");
			dataSet.setValue(numStatusOpen, "Open", "Status");
			return dataSet;
		}

		/**
		 * @return the data of iterations to be displayed by the pie chart
		 */
		private static CategoryDataset setDataIteration() {
			DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
			List<Iteration> iterations = IterationModel.getInstance()
					.getIterations();// list of iterations
			List<Requirement> requirements = RequirementModel.getInstance()
					.getRequirements();// list of requirements
			int[] iterationNum = new int[iterations.size()];
			for (int i = 0; i < iterations.size(); i++) {
				for (int j = 0; j < requirements.size(); j++) {
					if (requirements.get(j).getIteration().toString()
							.equals(iterations.get(i).toString())) {
						iterationNum[i]++;// increments the number if the requiremet
											// belongs to the given iteration
					}
				}
			}
			for (int k = 0; k < iterationNum.length; k++) {
				dataSet.setValue(iterationNum[k], iterations.get(k).toString(), "Iteration");// sets
																				// the
																				// data
			}
			return dataSet;
		}

		/**
		 * @return the data of the number of requirements a user has assigned to
		 *         them
		 */
		private static CategoryDataset setDataAssignTo() {
			DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
			ArrayList<String> userNames = new ArrayList<String>();

			List<Requirement> requirements = RequirementModel.getInstance()
					.getRequirements();// list of requirements
			for (int i = 0; i < requirements.size(); i++) {
				List<String> users = requirements.get(i).getAssignedTo();// list of
																			// users
																			// for
																			// specific
																			// requirement
				for (int j = 0; j < users.size(); j++) {
					if (!userNames.contains(users.get(j))) {
						userNames.add(users.get(j));// populate a list of all users
					}
				}

			}
			int[] numReqAssigned = new int[userNames.size()];
			for (int i = 0; i < requirements.size(); i++) {
				List<String> users = requirements.get(i).getAssignedTo();
				for (int j = 0; j < userNames.size(); j++) {
					if (users.contains(userNames.get(j))) {
						numReqAssigned[j]++;// count the number of requirements a
											// user has assigned to them
					}
				}
			}
			for (int k = 0; k < userNames.size(); k++) {
				dataSet.setValue(numReqAssigned[k], userNames.get(k), "Assigned To");// populate
																		// pie chart
																		// data
			}
			return dataSet;
		}
		private static JFreeChart createChart(CategoryDataset dataset, String title){
			
		}
}
