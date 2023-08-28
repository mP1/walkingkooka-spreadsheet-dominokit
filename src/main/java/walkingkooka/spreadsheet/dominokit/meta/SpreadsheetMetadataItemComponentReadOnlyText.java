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

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link SpreadsheetMetadataItemComponent} that only displays the current property value, formatting it to text
 * using the provided {@link Function} formatter.
 */
final class SpreadsheetMetadataItemComponentReadOnlyText<T> extends SpreadsheetMetadataItemComponent<T> {

    static <T> SpreadsheetMetadataItemComponentReadOnlyText<T> with(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                    final Function<T, String> formatter,
                                                                    final SpreadsheetMetadataPanelComponentContext context) {
        checkPropertyName(propertyName);
        Objects.requireNonNull(formatter, "formatter");
        checkContext(context);

        return new SpreadsheetMetadataItemComponentReadOnlyText<>(
                propertyName,
                formatter,
                context
        );
    }

    private SpreadsheetMetadataItemComponentReadOnlyText(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                         final Function<T, String> formatter,
                                                         final SpreadsheetMetadataPanelComponentContext context) {
        super(
                propertyName,
                context
        );
        this.formatter = formatter;
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

        final SpreadsheetMetadataPropertyName<T> propertyName = this.propertyName;
        final String text = metadata.getIgnoringDefaults(propertyName)
                .map(this.formatter)
                .orElse("");
        context.debug(this.getClass().getSimpleName() + ".refresh " + propertyName + "=" + CharSequences.quoteAndEscape(text));

        this.element.setTextContent(text);
    }

    /**
     * Used to format a value into text for display.
     */
    private final Function<T, String> formatter;

    // isElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.element.element();
    }

    private final DivElement element = ElementsFactory.elements.div();
}
