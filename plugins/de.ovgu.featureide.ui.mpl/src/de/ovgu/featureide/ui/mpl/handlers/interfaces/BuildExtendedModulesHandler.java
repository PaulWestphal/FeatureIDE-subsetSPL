/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2015  FeatureIDE team, University of Magdeburg, Germany
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
package de.ovgu.featureide.ui.mpl.handlers.interfaces;

import de.ovgu.featureide.core.mpl.MPLPlugin;
import de.ovgu.featureide.ui.mpl.handlers.AProjectJobHandler;
import de.ovgu.featureide.ui.mpl.wizards.AbstractWizard;
import de.ovgu.featureide.ui.mpl.wizards.BuildExtendedModulesWizard;
import de.ovgu.featureide.ui.mpl.wizards.WizardConstants;

/**
 * Action to build extended interfaces from the current configuration.
 * 
 * @author Reimar Schroeter
 */
public class BuildExtendedModulesHandler extends AProjectJobHandler {

	@Override
	protected AbstractWizard instantiateWizard() {
		return new BuildExtendedModulesWizard("Folder of extended modules", "ExtendedModules");
	}
	
	@Override
	protected void endAction() {
		MPLPlugin.getDefault().buildExtendedModules(projects, 
				(String) wizard.getData(WizardConstants.KEY_OUT_FOLDER));
	}
	
}