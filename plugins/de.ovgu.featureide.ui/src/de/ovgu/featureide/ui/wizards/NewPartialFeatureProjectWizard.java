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

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.ide.undo.CopyProjectOperation;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.core.builder.PartialFeatureProjectBuilder;
import de.ovgu.featureide.fm.ui.FMUIPlugin;
import de.ovgu.featureide.ui.UIPlugin;

/**
 * This wizard allows the creation of a Partial Feature Project, using a FeatureIDE project (with a composer that supports the creation of a partial feature
 * project) and a configuration. Copies the FeatureIDE project and passes it to @see de.ovgu.featureide.core.builder.PartialFeatureProjectBuilder for further
 * steps.
 *
 * @author Paul Westphal
 */
public class NewPartialFeatureProjectWizard extends BasicNewResourceWizard {

	private final static Image colorImage = FMUIPlugin.getDefault().getImageDescriptor("icons/FeatureIconSmall.ico").createImage();
	public static final String ID = UIPlugin.PLUGIN_ID + ".NewPartialProjectWizard";

	private ConfigurationSelectionPage page1;
	private WizardNewProjectCreationPage page2;

	private final IFeatureProject baseProject;

	public NewPartialFeatureProjectWizard(IFeatureProject featureproject) {
		super();
		setNeedsProgressMonitor(true);
		baseProject = featureproject;
	}

	@Override
	public void addPages() {

		final String configPath = baseProject.getConfigPath().replace("\\", "/") + "/";

		final List<java.nio.file.Path> configurationPaths = baseProject.getAllConfigurations();
		final ArrayList<String> configNames = new ArrayList<>();
		configurationPaths.forEach((configName) -> {
			configNames.add(configName.toString().replace("\\", "/").replace(configPath, ""));
		});

		final String currentConfig = baseProject.getCurrentConfiguration().toString().replace("\\", "/").replace(configPath, "");

		page1 = new ConfigurationSelectionPage(configNames, currentConfig);
		final Shell shell = getShell();
		if (shell != null) {
			shell.setImage(colorImage);
		}
		addPage(page1);

		page2 = new WizardNewProjectCreationPage("basicNewProjectPage") {

			@Override
			public void createControl(Composite parent) {
				super.createControl(parent);
				createWorkingSetGroup((Composite) getControl(), getSelection(), new String[] { "org.eclipse.ui.resourceWorkingSetPage" });
				Dialog.applyDialogFont(getControl());
			}
		};
		page2.setTitle("Derive new partial feature project.");
		page2.setDescription("");
		page2.setInitialProjectName(baseProject.getProject().getName() + "_partial");
		addPage(page2);

		super.addPages();
	}

	@Override
	public boolean performFinish() {
		copyBaseProject();

		final IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(page2.getProjectName());

		IFeatureProject newFeatureProject = CorePlugin.getFeatureProject(newProject);
		while ((newFeatureProject = CorePlugin.getFeatureProject(newProject)) == null) {}

		final java.nio.file.Path configPath = Paths.get(baseProject.getConfigPath() + "/" + page1.getSelectedConfiguration());
		final PartialFeatureProjectBuilder builder = new PartialFeatureProjectBuilder(getContainer(), newFeatureProject, configPath);
		builder.transformProject();

		return true;

	}

	private void copyBaseProject() {
		final IRunnableWithProgress runnable = new IRunnableWithProgress() {

			String projectName = page2.getProjectName();

			@Override
			public void run(IProgressMonitor monitor) {
				// TODO: fix target path
				final CopyProjectOperation c = new CopyProjectOperation(baseProject.getProject(), projectName, null, "Copying Project");
				try {
					c.execute(monitor, getWorkbench());
				} catch (final ExecutionException e) {
					e.printStackTrace();
				}
			}
		};

		try {
			getContainer().run(true, true, runnable);
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}
