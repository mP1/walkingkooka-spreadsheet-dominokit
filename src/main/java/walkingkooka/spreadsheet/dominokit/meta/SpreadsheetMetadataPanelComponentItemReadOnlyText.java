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
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} that only displays the current property value, formatting it to text
 * using the provided {@link Function} formatter.
 */
final class SpreadsheetMetadataPanelComponentItemReadOnlyText<T> extends SpreadsheetMetadataPanelComponentItem<T> {

    static <T> SpreadsheetMetadataPanelComponentItemReadOnlyText<T> with(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                         final Optional<String> label,
                                                                         final Function<T, String> formatter,
                                                                         final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemReadOnlyText<>(
            propertyName,
            label,
            formatter,
            context
        );
    }

    private SpreadsheetMetadataPanelComponentItemReadOnlyText(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                              final Optional<String> label,
                                                              final Function<T, String> formatter,
                                                              final SpreadsheetMetadataPanelComponentContext context) {
        super(
            propertyName,
            label,
            context
        );
        this.formatter = Objects.requireNonNull(formatter, "formatter");

        this.element = HtmlElementComponent.div()
            .setPaddingTop("5px")
            .setPaddingBottom("5px");
    }

    @Override
    void focus() {
        // NOP cant give focus to read only value.
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final RefreshContext context) {
        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();

        final SpreadsheetMetadataPropertyName<T> propertyName = this.propertyName;
        final String text = metadata.get(propertyName)
            .map(this.formatter)
            .orElse("");
        this.element.setText(text);
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

    private final DivComponent element;
}
