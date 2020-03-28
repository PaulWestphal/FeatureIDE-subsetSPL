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

import java.nio.file.Path;
import java.nio.file.Paths;

import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.io.manager.ConfigurationManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import de.ovgu.featureide.fm.core.job.SliceFeatureModel;

/**
 * TODO description
 *
 * @author Paul Westphal
 */
public class TemporaryClassName {

	// private ComposerSpecificExtension compExtension;
	private final IFeatureProject project;
	private Configuration config;

	public TemporaryClassName(IFeatureProject project) {
		this.project = project;
	}

	public <T> void transformProject() {
		config = getConfiguration();
		final IFeatureModel slicedModel =
			LongRunningWrapper.runMethod(new SliceFeatureModel(project.getFeatureModel(), config.getSelectedFeatureNames(), true));

		final Path fmPath = Paths.get(project.getProject().getLocation().toOSString().replace("\\", "/") + "/fmfile.xml");
		FeatureModelManager.save(slicedModel, fmPath, project.getComposer().getFeatureModelFormat());
	}

	private Configuration getConfiguration() {
		final Path path = Paths.get(project.getCurrentConfiguration().toUri());

		if (ConfigurationManager.isFileSupported(path)) {
			return config = ConfigurationManager.getInstance(path).getObject();
		} else {
			return null;
		}
	}
}
