/* FeatureIDE - An IDE to support feature-oriented software development
 * Copyright (C) 2005-2010  FeatureIDE Team, University of Magdeburg
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * See http://www.fosd.de/featureide/ for further information.
 */
package de.ovgu.featureide.ui.ahead.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.ovgu.featureide.ui.ahead.AheadUIPlugin;
import de.ovgu.featureide.ui.ahead.wizards.NewJakFileWizard;

/**
 * Class starts JakFileWizard for Button in the icon bar.
 * 
 * @author Christian Becker
 */
public class StartJakFileWizard implements IWorkbenchWindowActionDelegate {

	public static final String ID = AheadUIPlugin.PLUGIN_ID + ".NewJakFile";

	private IWorkbenchWindow window;

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {

		this.window = window;
	}

	public void run(IAction action) {

		NewJakFileWizard wizard = new NewJakFileWizard();
		ISelection selection = window.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			wizard.init(window.getWorkbench(),
							(IStructuredSelection) selection);

		} else {
			wizard.init(window.getWorkbench(), null);
		}
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();
	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

}
