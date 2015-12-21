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
package de.ovgu.featureide.fm.ui.editors.featuremodel.operations;

import static de.ovgu.featureide.fm.core.localization.StringTable.SOURCE_CHANGE;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.event.FeatureModelEvent;
import de.ovgu.featureide.fm.ui.editors.FeatureModelEditor;

/**
 * @author Sebastian Krieter
 * @author Marcus Pinnecke
 */
public class SourceChangedOperation extends AbstractFeatureModelOperation {

	private final FeatureModelEditor featureModelEditor;
	private final String newText, oldText;

	/**
	 * @param featureModel
	 * @param label
	 * @param featureModelEditor
	 * @param newText
	 * @param oldText
	 */
	public SourceChangedOperation(IFeatureModel featureModel, FeatureModelEditor featureModelEditor, String newText, String oldText) {
		super(featureModel, SOURCE_CHANGE);
		this.featureModelEditor = featureModelEditor;
		this.newText = newText;
		this.oldText = oldText;
	}

	@Override
	protected FeatureModelEvent operation() {
		featureModelEditor.readModel(newText);
		return new FeatureModelEvent(featureModel, editor, false, FeatureModelEvent.MODEL_DATA_CHANGED, null, null);
	}

	@Override
	protected FeatureModelEvent inverseOperation() {
		featureModelEditor.readModel(oldText);
		return new FeatureModelEvent(featureModel, editor, false, FeatureModelEvent.MODEL_DATA_CHANGED, null, null);
	}

}