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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.wizardextension.DefaultNewFeatureProjectWizardExtension;

/**
 * TODO description
 *
 * @author Paul Westphal
 */
public class NewPartialFeatureProjectWizardExtension extends DefaultNewFeatureProjectWizardExtension {

	/**
	 * Executed after FeatureIDE project is created and before editor is opened.
	 *
	 * @param project feature project to be created
	 * @param compID id of composer
	 * @param sourcePath path in which source code is stored
	 * @param configPath path in which config files are stored
	 * @param buildPath path in which class files created during build process are stored
	 * @param shouldCreateSourceFolder create source folder if wanted
	 * @param shouldCreateBuildFolder create build folder if wanted
	 * @throws CoreException exception
	 */
	@Override
	public void enhanceProject(IProject project, String compID, String sourcePath, String configPath, String buildPath, boolean shouldCreateSourceFolder,
			boolean shouldCreateBuildFolder) throws CoreException {
		CorePlugin.setupFeatureProject(project, compID, sourcePath, configPath, buildPath, true, true, shouldCreateSourceFolder, shouldCreateBuildFolder);
		extendedEnhanceProject(project, compID, sourcePath, configPath, buildPath);
	}
}
