/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2017  FeatureIDE team, University of Magdeburg, Germany
 *
 * This file is part of FeatureIDE.
 *
 * FeatureIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FeatureIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FeatureIDE.  If not, see <http://www.gnu.org/licenses/>.
 *
 * See http://featureide.cs.ovgu.de/ for further information.
 */
package de.ovgu.featureide.ui.wizards;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * TODO description
 *
 * @author Paul Westphal
 */
public class ConfigurationSelectionPage extends WizardPage {

	private Tree configurationTree;
	private String configurationName;
	private final String defaultConfig;

	private final List<String> configList;

	public ConfigurationSelectionPage(List<String> configList, String defaultConfig) {
		super("");
		this.configList = configList;
		this.defaultConfig = defaultConfig;
		configurationName = defaultConfig;
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		final GridLayout layout = new GridLayout();
		container.setLayout(layout);
		setControl(container);

		configurationTree = new Tree(container, SWT.SINGLE | SWT.CHECK);
		configurationTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		configurationTree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.detail == SWT.CHECK) {
					final TreeItem item = (TreeItem) e.item;
					if (item.getChecked()) {
						configurationName = item.getText();

						final TreeItem[] items = configurationTree.getItems();
						// uncheck all other boxes
						for (int i = 0; i < items.length; i++) {
							if (items[i] != e.item) {
								items[i].setChecked(false);
							}
						}
						setPageComplete(true);
					} else {
						// when config is unchecked
						setPageComplete(false);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

		setPageComplete(true);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			addConfigurationsToTree();
		}
		super.setVisible(visible);
	}

	private void addConfigurationsToTree() {
		configList.forEach((configName) -> {
			final TreeItem item = new TreeItem(configurationTree, SWT.NORMAL);
			item.setText(configName);
			if (configName.equals(defaultConfig)) {
				item.setChecked(true);
			}

		});
	}

	public String getSelectedConfiguration() {
		return configurationName;
	}
}
