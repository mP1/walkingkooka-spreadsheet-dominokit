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
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * Common interface for any {@link walkingkooka.spreadsheet.dominokit.value.ValueComponent} that can appear within a {@link TextStyleDialogComponent},
 * with support for providing a {@link walkingkooka.spreadsheet.dominokit.value.ValueWatcher} to refresh themselves,
 * when the parent {@link walkingkooka.tree.text.TextStyle} changes.
 */
public interface TextStylePropertyComponent<E extends HTMLElement, V, C extends TextStylePropertyComponent<E, V, C>>
    extends FormValueComponent<E, V, C>,
    HasName<TextStylePropertyName<V>>,
    HasTextStyleValueWatcher {
}
