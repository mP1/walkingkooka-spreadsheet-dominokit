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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.Element;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.button.Button;
import walkingkooka.spreadsheet.dominokit.ComponentRefreshable;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Base class for a item that may be displayed within a {@link SpreadsheetMetadataPanelComponent}. It only
 * implements {@link ComponentRefreshable}, it is assumed it will only be refreshed when the parent panel is open and refreshed.
 */
abstract class SpreadsheetMetadataItemComponent<T> implements ComponentRefreshable, IsElement<Element> {

    /**
     * {@see SpreadsheetMetadataItemComponentText}
     */
    static <T extends SpreadsheetPattern> SpreadsheetMetadataItemComponentSpreadsheetPattern<T> spreadsheetPattern(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                                                   final SpreadsheetPatternKind patternKind,
                                                                                                                   final SpreadsheetMetadataPanelComponentContext context) {
        return SpreadsheetMetadataItemComponentSpreadsheetPattern.with(
                propertyName,
                patternKind,
                context
        );
    }

    /**
     * {@see SpreadsheetMetadataItemComponentText}
     */
    static <T> SpreadsheetMetadataItemComponent<T> text(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                        final Function<T, String> formatter,
                                                        final SpreadsheetMetadataPanelComponentContext context) {
        return SpreadsheetMetadataItemComponentText.with(
                propertyName,
                formatter,
                context
        );
    }

    // factory helpers..................................................................................................

    static void checkPropertyName(final SpreadsheetMetadataPropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");
    }

    static void checkContext(final SpreadsheetMetadataPanelComponentContext context) {
        Objects.requireNonNull(context, "context");
    }

    /**
     * Package private ctor to limit sub-classing.
     */
    SpreadsheetMetadataItemComponent(final SpreadsheetMetadataPropertyName<T> propertyName,
                                     final SpreadsheetMetadataPanelComponentContext context) {
        this.propertyName = propertyName;
        this.context = context;
    }

    /**
     * Returns the {@link Button} belonging to a default button if there is one.
     */
    abstract Optional<Button> defaultButton();

    // properties......................................................................................................

    final SpreadsheetMetadataPropertyName<T> propertyName;

    /**
     * The parent {@link SpreadsheetMetadataPanelComponentContext} this will be used primarily to save updated values.
     */
    final SpreadsheetMetadataPanelComponentContext context;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.propertyName.toString();
    }
}
