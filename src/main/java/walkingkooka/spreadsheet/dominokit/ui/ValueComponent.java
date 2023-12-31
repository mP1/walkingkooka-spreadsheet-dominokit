/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.ui;

import elemental2.dom.Element;
import elemental2.dom.EventListener;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Value;

import java.util.Optional;

/**
 * A component that supports entering or selecting a value.
 */
public interface ValueComponent<E extends Element, V> extends Component<E>, Value<Optional<V>> {

    ValueComponent<E, V> setId(final String id);

    ValueComponent<E, V> setLabel(final String label);

    ValueComponent<E, V> setValue(final Optional<V> value);

    ValueComponent<E, V> focus();

    ValueComponent<E, V> optional();

    ValueComponent<E, V> required();

    ValueComponent<E, V> addChangeListener(final ChangeListener<Optional<V>> listener);

    ValueComponent<E, V> addFocusListener(final EventListener listener);

    ValueComponent<E, V> hideMarginBottom();

    ValueComponent<E, V> removeBorders();
}
