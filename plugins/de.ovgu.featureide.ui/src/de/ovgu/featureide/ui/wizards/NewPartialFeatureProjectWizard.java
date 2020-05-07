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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.core.builder.PartialFeatureProjectBuilder;
import de.ovgu.featureide.core.wizardextension.DefaultNewFeatureProjectWizardExtension;
import de.ovgu.featureide.fm.ui.FMUIPlugin;
import de.ovgu.featureide.ui.UIPlugin;

/**
 * TODO description
 *
 * @author Paul Westphal
 */
public class NewPartialFeatureProjectWizard extends BasicNewProjectResourceWizard {

	private final static Image colorImage = FMUIPlugin.getDefault().getImageDescriptor("icons/FeatureIconSmall.ico").createImage();
	public static final String ID = UIPlugin.PLUGIN_ID + ".NewPartialProjectWizard";

	private final IFeatureProject baseProject;
	private final IPath baseProjectPath;
	private IPath newProjectPath;
	private DefaultNewFeatureProjectWizardExtension wizardExtension = null;
	private final String compositionToolID;
	private PartialFeatureProjectBuilder builder = null;

	private ConfigurationSelectionPage page;

	public NewPartialFeatureProjectWizard(IFeatureProject featureproject) {
		baseProject = featureproject;
		baseProjectPath = getBaseProjectPath();
		compositionToolID = baseProject.getComposerID();
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

		page = new ConfigurationSelectionPage(configNames, currentConfig);
		final Shell shell = getShell();
		if (shell != null) {
			shell.setImage(colorImage);
		}
		addPage(page);
		super.addPages();
	}

	@Override
	public boolean canFinish() {
		if (wizardExtension != null) {
			return wizardExtension.isFinished();
		} else {
			return super.canFinish();
		}
	}

	private IPath getBaseProjectPath() {
		final IWorkspace workspace = baseProject.getProject().getWorkspace();
		final IPath workspaceDirectory = workspace.getRoot().getLocation();
		return new Path(workspaceDirectory.toString() + baseProject.getProject().getFullPath().toOSString());
	}

	private IPath getNewProjectPath() {
		final IWorkspace workspace = getNewProject().getWorkspace();
		final IPath workspaceDirectory = workspace.getRoot().getLocation();
		return new Path(workspaceDirectory.toString() + getNewProject().getFullPath().toOSString());
	}

	@Override
	public boolean performFinish() {

		if (wizardExtension == null) {
			wizardExtension = new DefaultNewFeatureProjectWizardExtension();
			wizardExtension.setWizard(this);
		}

		if (super.performFinish() == false) {
			return false;
		}

		newProjectPath = getNewProjectPath();

		copyBaseProject();

		if (wizardExtension.isFinished()) {
			baseProject.getSourcePath();
			final IProject newProject = getNewProject();
			enhanceProject(newProject);
			CorePlugin.getDefault().addProject(newProject);

			final IFeatureProject newFeatureProject = CorePlugin.getFeatureProject(newProject);

			final java.nio.file.Path configPath = Paths.get(baseProject.getConfigPath() + "/" + page.getSelectedConfiguration());
			builder = new PartialFeatureProjectBuilder(newFeatureProject, configPath, newFeatureProject.getSourceFolder());
			builder.transformProject();
		}

		return true;
	}

	private void enhanceProject(IProject newProject) {
		// TODO: stringgebastel stabiler machen
		final String newSourcePath;
		final String newConfigPath;
		final String newBuildPath;

		try {
			wizardExtension.enhanceProject(newProject, compositionToolID, newSourcePath, newConfigPath, newBuildPath, false, false);
		} catch (final CoreException e) {
			UIPlugin.getDefault().logError(e);
		}
	}

	private void copyBaseProject() {

	}
}
