/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2019  FeatureIDE team, University of Magdeburg, Germany
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
package de.ovgu.featureide.fm.core.explanations.config;

import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.explanations.fm.FeatureModelExplanation;

/**
 * An explanation for a circumstance involving a {@link Configuration configuration}.
 *
 * @param <S> subject
 * @author Timo Günther
 */
public abstract class ConfigurationExplanation<S> extends FeatureModelExplanation<S> {

	/** The configuration. */
	private final Configuration config;

	/**
	 * Constructs a new instance of this class.
	 *
	 * @param subject the subject to be explained
	 * @param config the configuration
	 */
	protected ConfigurationExplanation(S subject, Configuration config) {
		super(subject);
		this.config = config;
	}

	/**
	 * Returns the configuration.
	 *
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return config;
	}

}
