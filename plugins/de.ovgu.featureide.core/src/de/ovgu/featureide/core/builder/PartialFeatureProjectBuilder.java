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
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.prop4j.And;
import org.prop4j.False;
import org.prop4j.Literal;
import org.prop4j.Node;
import org.prop4j.Not;
import org.prop4j.True;

import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.Constraint;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.io.manager.ConfigurationManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

/**
 * Modifies the copy of a FeatureIDE project using a configuration, such that it becomes a subset of the original project.
 *
 * @author Paul Westphal
 */
public class PartialFeatureProjectBuilder {

	private final IFeatureProject project;
	private Configuration config;
	private IFeatureModel modifiedModel;

	private final Path configPath;

	public PartialFeatureProjectBuilder(IFeatureProject project, Path configPath) {

		this.project = project;
		this.configPath = configPath;
	}

	public void transformProject() {
		config = getConfiguration();
		project.setCurrentConfiguration(configPath);
		deleteConfigurations();

		final ArrayList<String> featureNameList = getFeatureNames();
		final ArrayList<String> removedFeatureNameList = new ArrayList<String>(config.getUnselectedFeatureNames());
		final ArrayList<String> selectedFeatureNameList = new ArrayList<String>();
		selectedFeatureNameList.addAll(config.getSelectedFeatureNames());

		modifiedModel = modifyFeatureModel(project.getFeatureModel(), featureNameList, removedFeatureNameList, selectedFeatureNameList);

		final Path fmPath = Paths.get(project.getFeatureModel().getSourceFile().toString().replace("\\", "/"));
		FeatureModelManager.save(modifiedModel, fmPath, project.getComposer().getFeatureModelFormat());
		if (project.getComposer().supportsPartialFeatureProject()) {
			try {
				project.getComposer().buildPartialFeatureProjectAssets(project.getSourceFolder(), removedFeatureNameList, selectedFeatureNameList);
			} catch (IOException | CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private IFeatureModel modifyFeatureModel(IFeatureModel model, ArrayList<String> keptFeatures, ArrayList<String> removedFeatures,
			ArrayList<String> selectedFeatures) {
		final List<IConstraint> modelconstraints = model.getConstraints();
		final ArrayList<IConstraint> constraints = new ArrayList<IConstraint>();
		constraints.addAll(modelconstraints);
		modifyConstraints(constraints, removedFeatures, selectedFeatures, model);
		deleteFeatures(removedFeatures, model);
		return model;
	}

	private void modifyConstraints(ArrayList<IConstraint> constraints, ArrayList<String> removedFeatures, ArrayList<String> selectedFeatures,
			IFeatureModel model) {
		final ArrayList<IConstraint> constraintsToDelete = new ArrayList<IConstraint>();
		final ArrayList<IConstraint> constraintsToAdd = new ArrayList<IConstraint>();

		// Features that are now dead because their parents get removed
		final ArrayList<String> deadFeatures = new ArrayList<String>();
		for (final String feature : removedFeatures) {
			deadFeatures.addAll(getAllChildren(new ArrayList<String>(), feature, model));
		}
		deadFeatures.removeAll(removedFeatures);

		if (!deadFeatures.isEmpty()) {
			Node deadFeaturesConstraintNode = new Not(new Literal(deadFeatures.get(0)));
			for (int i = 1; i < deadFeatures.size(); i++) {
				deadFeaturesConstraintNode = new And(deadFeaturesConstraintNode, new Not(new Literal(deadFeatures.get(i))));
			}
			constraintsToAdd.add(new Constraint(model, deadFeaturesConstraintNode));
		}

		// Features that are now core features because they were selected in the configuration
		final ArrayList<String> coreFeatures = new ArrayList<String>();
		if (!coreFeatures.isEmpty()) {
			Node coreFeaturesConstraintNode = new Literal(coreFeatures.get(0));
			for (int i = 1; i < coreFeatures.size(); i++) {
				coreFeaturesConstraintNode = new And(coreFeaturesConstraintNode, new Literal(coreFeatures.get(i)));
			}
			constraintsToAdd.add(new Constraint(model, coreFeaturesConstraintNode));
		}

		for (final IConstraint constraint : constraints) {
			for (final String feature : removedFeatures) {
				if (constraint.getNode().getUniqueContainedFeatures().contains(feature)) {
					constraintsToDelete.add(constraint);

					final Node newNode = Node.replaceLiterals(constraint.getNode().toCNF(), removedFeatures, true);
					if (newNode instanceof True) {
						// Constraint is now tautology and was removed, nothing to do now.
					} else if (newNode instanceof False) {
						// Constraint is now contradiction and makes the feature model void.
						constraintsToAdd.add(new Constraint(model, new Not(new Literal(model.getStructure().getRoot().getFeature().getName()))));
					} else {
						constraintsToAdd.add(new Constraint(model, newNode));
					}
					break;
				}
			}
		}

		constraints.removeAll(constraintsToDelete);
		constraints.addAll(constraintsToAdd);
		model.setConstraints(constraints);
	}

	private void deleteFeatures(ArrayList<String> removedFeatures, IFeatureModel model) {
		for (final String feature : removedFeatures) {
			if ((model.getFeature(feature).getStructure().getParent().getChildrenCount() == 2)
				&& (model.getFeature(feature).getStructure().getParent().isAlternative() || model.getFeature(feature).getStructure().getParent().isOr())) {
				final List<IFeatureStructure> children = model.getFeature(feature).getStructure().getParent().getChildren();
				final IFeature otherChild;
				if (children.get(0).getFeature().getName().equals(feature)) {
					otherChild = model.getFeature(children.get(1).getFeature().getName());
				} else {
					otherChild = model.getFeature(children.get(0).getFeature().getName());
				}
				model.deleteFeature(model.getFeature(feature));
				otherChild.getStructure().setMandatory(true);
			} else {
				model.deleteFeature(model.getFeature(feature));
			}
		}
	}

	private ArrayList<String> getAllChildren(ArrayList<String> children, String feature, IFeatureModel model) {

		children.add(feature);

		final List<IFeatureStructure> nextChildren = model.getFeature(feature).getStructure().getChildren();

		for (final IFeatureStructure child : nextChildren) {

			getAllChildren(children, child.getFeature().getName(), model);
		}
		return children;
	}

	private void deleteConfigurations() {
		final ArrayList<IResource> configurations = new ArrayList<IResource>();
		try {
			Collections.addAll(configurations, project.getConfigFolder().members());
		} catch (final CoreException e) {
			e.printStackTrace();
		}

		for (final IResource resource : configurations) {
			if (!resource.getName().equals(configPath.getFileName().toString())) {
				try {
					resource.delete(true, null);
				} catch (final CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private ArrayList<String> getFeatureNames() {
		final ArrayList<String> featureNameList = new ArrayList<String>();
		featureNameList.addAll(config.getSelectedFeatureNames());
		featureNameList.addAll(config.getUndefinedFeatureNames());
		return featureNameList;
	}

	private Configuration getConfiguration() {
		if (ConfigurationManager.isFileSupported(configPath)) {
			return ConfigurationManager.getInstance(configPath).getObject();
		} else {
			return null;
		}
	}
}
