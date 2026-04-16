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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.HTMLElement;
import walkingkooka.naming.HasName;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

/**
 * Useful helper that implements {@link HasTextStyleValueWatcher}, saving duplication of this logic.
 */
public interface HasTextStyleValueWatcherValueComponent<E extends HTMLElement, V, C extends ValueComponent<E, V, C>> extends HasTextStyleValueWatcher,
    HasName<TextStylePropertyName<V>>,
    ValueComponent<E, V, C> {

    // HasTextStyleValueWatcher.........................................................................................

    /**
     * Getter that returns a {@link ValueWatcher} which will cause this component to be update its value, sourced from a
     * {@link TextStyle} value change.
     */
    @Override
    default ValueWatcher<TextStyle> textStyleValueWatcher() {
        return (final Optional<TextStyle> value) ->
            HasTextStyleValueWatcherValueComponent.this.setValue(
                value.flatMap(
                    (final TextStyle textStyle) -> textStyle.get(
                        HasTextStyleValueWatcherValueComponent.this.name()
                    )
                )
            );
    }
}
