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
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

/**
 * Common interface for any {@link walkingkooka.spreadsheet.dominokit.value.ValueComponent} that can appear within a {@link TextStyleDialogComponent},
 * with support for providing a {@link walkingkooka.spreadsheet.dominokit.value.ValueWatcher} to refresh themselves,
 * when the parent {@link walkingkooka.tree.text.TextStyle} changes.
 */
public interface TextStylePropertyComponent<E extends HTMLElement, V, C extends TextStylePropertyComponent<E, V, C>>
    extends FormValueComponent<E, V, C>,
    HasName<TextStylePropertyName<V>> {

    default C setIdPrefix(final String idPrefix,
                          final String suffix) {
        CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix");

        return this.setId(
            idPrefix +
                CaseKind.KEBAB.change(
                    this.name()
                        .text(),
                    CaseKind.CAMEL
                ) +
                suffix
        );
    }

    default C setLabelFromPropertyName() {
        return this.setLabel(
            CaseKind.KEBAB.change(
                this.name()
                    .value(),
                CaseKind.TITLE
            )
        );
    }

    /**
     * Getter that returns a {@link ValueWatcher} which will cause this component to be update its value, sourced from a
     * {@link TextStyle} value change.
     */
    default ValueWatcher<TextStyle> textStyleValueWatcher() {
        return (final Optional<TextStyle> value) ->
            TextStylePropertyComponent.this.setValue(
                value.flatMap(
                    (final TextStyle textStyle) -> textStyle.get(
                        TextStylePropertyComponent.this.name()
                    )
                )
            );
    }

    /**
     * Called by the filter text box in a {@link TextStyleDialogComponent} and is used to match components, which are
     * displayed and those that return false are hidden.
     * <br>
     * The {@link #name()}, {@link #value()} and for Enums possible values should be searched.
     */
    boolean filterTest(final TextStyleDialogComponentFilter filter);
}
