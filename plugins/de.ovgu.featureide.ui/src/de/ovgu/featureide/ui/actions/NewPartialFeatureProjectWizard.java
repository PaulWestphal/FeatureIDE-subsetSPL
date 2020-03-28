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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.core.wizardextension.DefaultNewFeatureProjectWizardExtension;
import de.ovgu.featureide.fm.core.io.manager.ConfigurationManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
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

	private final IFeatureProject baseProject;
	private IPath baseProjectPath;
	private IPath newProjectPath;
	private DefaultNewFeatureProjectWizardExtension wizardExtension = null;
	private final String compositionToolID;
	private TemporaryClassName tempClassName = null;

	public NewPartialFeatureProjectWizard(IFeatureProject featureproject) {
		baseProject = featureproject;
		compositionToolID = baseProject.getComposerID();
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

		System.out.println(System.getProperty("java.version"));

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

			UIPlugin.getDefault().openEditor(FeatureModelEditor.ID, newProject.getFile("model.xml"));

			final IFeatureProject newFeatureProject = CorePlugin.getFeatureProject(newProject);
			tempClassName = new TemporaryClassName(newFeatureProject);
			tempClassName.transformProject();
		}

		return true;
	}

	private void enhanceProject(IProject newProject) {
		// TODO: stringgebastel stabiler machen
		final String newSourcePath = removeBaseProjectPathPrefix(baseProject.getSourcePath()).replace("\\", "/").substring(1);
		final String newConfigPath = removeBaseProjectPathPrefix(baseProject.getConfigPath()).replace("\\", "/").substring(1);
		final String newBuildPath = removeBaseProjectPathPrefix(baseProject.getBuildPath()).replace("\\", "/").substring(1);

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
		final java.nio.file.Path targetPath = java.nio.file.Paths.get(newProjectPath.toOSString());
		final java.nio.file.Path sourcePath = java.nio.file.Paths.get(baseProjectPath.toOSString());

		try {
			Files.walkFileTree(sourcePath, new SimpleFileVisitor<java.nio.file.Path>() {

				@Override
				public FileVisitResult preVisitDirectory(final java.nio.file.Path dir, final BasicFileAttributes attrs) throws IOException {
					Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(final java.nio.file.Path file, final BasicFileAttributes attrs) throws IOException {
					// exception for classpath and project files
					if (!(file.endsWith(".classpath") || file.endsWith(".project"))) {
						Files.copy(file, targetPath.resolve(sourcePath.relativize(file)));
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (final IOException e) {
			UIPlugin.getDefault().logError(e);
		}

		final java.nio.file.Path fmPath =
			java.nio.file.Paths.get(newProjectPath.toOSString() + removeBaseProjectPathPrefix(baseProject.getFeatureModel().getSourceFile().toString()));

		baseProject.getComposer().getFeatureModelFormat();
		FeatureModelManager.registerExistingFeatureModel(fmPath, baseProject.getComposer().getFeatureModelFormat());

		final java.nio.file.Path configPath = java.nio.file.Paths.get(newProjectPath.toOSString() + removeBaseProjectPathPrefix(baseProject.getConfigPath()));
		ConfigurationManager.getInstance(configPath);
	}
}
