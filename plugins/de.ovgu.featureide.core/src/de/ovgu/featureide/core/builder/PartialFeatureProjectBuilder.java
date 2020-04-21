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
package de.ovgu.featureide.core.builder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;

import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.fm.core.base.IFeature;
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
public class PartialFeatureProjectBuilder {

	private ComposerSpecificBuilderExtension compExtension;
	private final IFeatureProject project;
	private Configuration config;
	private IFeatureModel slicedModel;
	private final IFolder sourceFolder;

	private final Path configPath;

	public PartialFeatureProjectBuilder(IFeatureProject project, Path configPath, IFolder sourceFolder) {
		this.project = project;
		this.configPath = configPath;
		this.sourceFolder = sourceFolder;
	}

	public void transformProject() {
		config = getConfiguration();

		final ArrayList<String> featureNameList = getFeatureNames();
		final ArrayList<String> removedFeatureNameList = new ArrayList<String>(config.getUnSelectedFeatureNames());
		slicedModel = LongRunningWrapper.runMethod(new SliceFeatureModel(project.getFeatureModel(), featureNameList, true));

		final ArrayList<String> mandatoryFeatureNameList = new ArrayList<String>();
		featureNameList.addAll(config.getSelectedFeatureNames());
		setFeaturesMandatory(mandatoryFeatureNameList);

		// final Path fmPath = Paths.get(project.getProject().getLocation().toOSString().replace("\\", "/") + "/fmfile.xml");

		// overwrite old feature model
		final Path fmPath = Paths.get(project.getFeatureModel().getSourceFile().toString().replace("\\", "/"));
		FeatureModelManager.save(slicedModel, fmPath, project.getComposer().getFeatureModelFormat());
		if (project.getComposer().supportsPartialFeatureProject()) {
			try {
				project.getComposer().buildPartialFeatureProject(sourceFolder, removedFeatureNameList, mandatoryFeatureNameList);
			} catch (IOException | CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void setFeaturesMandatory(ArrayList<String> features) {
		features.forEach((selectedFeatureName) -> {
			final IFeature feature = slicedModel.getFeature(selectedFeatureName);
			if (feature != null) {
				feature.getStructure().setMandatory(true);
			}
		});
		return;
	}

	private ArrayList<String> getFeatureNames() {
		final ArrayList<String> featureNameList = new ArrayList<String>();
		featureNameList.addAll(config.getSelectedFeatureNames());
		featureNameList.addAll(config.getUndefinedFeatureNames());
		return featureNameList;
	}

	private Configuration getConfiguration() {
		if (ConfigurationManager.isFileSupported(configPath)) {
			return config = ConfigurationManager.getInstance(configPath).getObject();
		} else {
			return null;
		}
	}
}
