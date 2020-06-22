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
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.DeleteResourcesOperation;

import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.io.manager.ConfigurationManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import de.ovgu.featureide.fm.core.job.SliceFeatureModel;

/**
 * Modifies the copy of a FeatureIDE project using a configuration, such that it becomes a subset of the original project.
 *
 * @author Paul Westphal
 */
public class PartialFeatureProjectBuilder {

	private final IWizardContainer container;

	private final IFeatureProject project;
	private Configuration config;
	private IFeatureModel slicedModel;
	private final IFolder sourceFolder;

	private final Path configPath;

	public PartialFeatureProjectBuilder(IWizardContainer container, IFeatureProject project, Path configPath) {
		this.container = container;

		this.project = project;
		this.configPath = configPath;
		sourceFolder = project.getSourceFolder();
	}

	public void transformProject() {
		config = getConfiguration();
		project.setCurrentConfiguration(configPath);
		deleteConfigurations();

		final ArrayList<String> featureNameList = getFeatureNames();
		final ArrayList<String> removedFeatureNameList = new ArrayList<String>(config.getUnSelectedFeatureNames());
		slicedModel = LongRunningWrapper.runMethod(new SliceFeatureModel(project.getFeatureModel(), featureNameList, true));

		final ArrayList<String> mandatoryFeatureNameList = new ArrayList<String>();
		mandatoryFeatureNameList.addAll(config.getSelectedFeatureNames());
		setFeaturesMandatory(mandatoryFeatureNameList);

		// overwrite old feature model
		final Path fmPath = Paths.get(project.getFeatureModel().getSourceFile().toString().replace("\\", "/"));
		FeatureModelManager.save(slicedModel, fmPath, project.getComposer().getFeatureModelFormat());
		if (project.getComposer().supportsPartialFeatureProject()) {
			try {
				project.getComposer().buildPartialFeatureProjectAssets(sourceFolder, removedFeatureNameList, mandatoryFeatureNameList);
			} catch (IOException | CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteConfigurations() {
		final ArrayList<IResource> configurations = new ArrayList<IResource>();
		try {
			Collections.addAll(configurations, project.getConfigFolder().members());
		} catch (final CoreException e) {
			e.printStackTrace();
		}

		for (final IResource resource : configurations) {
			if (resource.getName().equals(configPath.getFileName().toString())) {
				configurations.remove(resource);
				break;
			}
		}

		IResource[] configurationsArray = new IResource[configurations.size()];
		configurationsArray = configurations.toArray(configurationsArray);
		final IResource[] configurationsToDelete = configurationsArray;

		final IRunnableWithProgress runnable = new IRunnableWithProgress() {

			@Override
			public void run(final IProgressMonitor monitor) {
				final DeleteResourcesOperation op = new DeleteResourcesOperation(configurationsToDelete, "Deleting Configurations", true);
				try {
					op.execute(monitor, PlatformUI.getWorkbench());
				} catch (final ExecutionException e) {
					e.printStackTrace();
				}
			}
		};

		try {
			container.run(true, true, runnable);
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
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
