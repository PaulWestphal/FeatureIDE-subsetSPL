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

import java.io.File;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.fm.ui.FMUIPlugin;
import de.ovgu.featureide.ui.UIPlugin;

/**
 * TODO description
 *
 * @author Paul Westphal
 */
public class NewPartialProjectWizard extends BasicNewProjectResourceWizard {

	private final static Image colorImage = FMUIPlugin.getDefault().getImageDescriptor("icons/FeatureIconSmall.ico").createImage();
	public static final String ID = UIPlugin.PLUGIN_ID + ".NewPartialProjectWizard";

	IFeatureProject featureProjectBase;
	File projectPath;

	public NewPartialProjectWizard(IFeatureProject featureproject) {
		featureProjectBase = featureproject;
		findProjectPathFile();
	}

	private void findProjectPathFile() {
		final IWorkspace workspace = featureProjectBase.getProject().getWorkspace();

		final File workspaceDirectory = workspace.getRoot().getLocation().toFile();

		final File projectPathFile = new File(workspaceDirectory.toString() + featureProjectBase.getProject().getFullPath().toOSString());

		projectPath = projectPathFile;
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish() == false) {
			return false;
		}
		return true;
	}
}
