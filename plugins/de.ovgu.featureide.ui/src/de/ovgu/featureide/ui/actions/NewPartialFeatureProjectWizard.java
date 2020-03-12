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
package de.ovgu.featureide.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.core.wizardextension.DefaultNewFeatureProjectWizardExtension;
import de.ovgu.featureide.fm.ui.FMUIPlugin;
import de.ovgu.featureide.fm.ui.editors.FeatureModelEditor;
import de.ovgu.featureide.ui.UIPlugin;

/**
 * TODO description
 *
 * @author Paul Westphal
 */
public class NewPartialFeatureProjectWizard extends BasicNewProjectResourceWizard {

	private final static Image colorImage = FMUIPlugin.getDefault().getImageDescriptor("icons/FeatureIconSmall.ico").createImage();
	public static final String ID = UIPlugin.PLUGIN_ID + ".NewPartialProjectWizard";

	IFeatureProject baseProject;
	IPath baseProjectPath;
	IPath newProjectPath;
	private DefaultNewFeatureProjectWizardExtension wizardExtension = null;
	String compositionToolID;

	public NewPartialFeatureProjectWizard(IFeatureProject featureproject) {
		baseProject = featureproject;
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

		baseProjectPath = getBaseProjectPath();
		newProjectPath = getNewProjectPath();

		copyBaseProject();

		if (wizardExtension.isFinished()) {
			baseProject.getSourcePath();
			final IProject newProject = getNewProject();
			enhanceProject(newProject);
			// open editor
			UIPlugin.getDefault().openEditor(FeatureModelEditor.ID, newProject.getFile("model.xml"));

		}

		return true;
	}

	private void enhanceProject(IProject newProject) {
		final String newSourcePath = newProjectPath + removeBaseProjectPathPrefix(baseProject.getSourcePath());
		final String newConfigPath = newProjectPath + removeBaseProjectPathPrefix(baseProject.getConfigPath());
		final String newBuildPath = newProjectPath + removeBaseProjectPathPrefix(baseProject.getBuildPath());

		try {
			wizardExtension.enhanceProject(newProject, compositionToolID, newSourcePath, newConfigPath, newBuildPath, false, false);
		} catch (final CoreException e) {
			UIPlugin.getDefault().logError(e);
		}
	}

	private String removeBaseProjectPathPrefix(String s) {
		if ((s != null) && (baseProjectPath != null) && s.startsWith(baseProjectPath.toOSString())) {
			return s.substring(baseProjectPath.toOSString().length());
		}
		return s;
	}

	private void copyBaseProject() {

	}
}
