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

import elemental2.dom.HTMLTableElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.elements.TBodyElement;
import org.dominokit.domino.ui.elements.TableElement;
import org.dominokit.domino.ui.elements.TableRowElement;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.OpenableComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyStyleSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A component that displays numerous {@link SpreadsheetMetadata} properties with support for editing the individual values.
 */
public final class SpreadsheetMetadataPanelComponent implements ComponentLifecycle,
        IsElement<HTMLTableElement>,
        SpreadsheetMetadataWatcher {

    public static SpreadsheetMetadataPanelComponent with(final OpenableComponent drawer,
                                                         final SpreadsheetMetadataPanelComponentContext context) {
        Objects.requireNonNull(drawer, "drawer");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetMetadataPanelComponent(
                drawer,
                context
        );
    }

    private SpreadsheetMetadataPanelComponent(final OpenableComponent drawer,
                                              final SpreadsheetMetadataPanelComponentContext context) {
        this.drawer = drawer;
        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetMetadataWatcher(this);

        this.context = context;

        final List<SpreadsheetMetadataItemComponent<?>> items = Lists.array();
        items.add(this.spreadsheetId());
        items.add(this.spreadsheetName());
        items.add(this.creator());
        items.add(this.createdDateTime());
        items.add(this.modifiedBy());
        items.add(this.modifiedDateTime());
        items.add(this.locale());

        final TBodyElement tBody = ElementsFactory.elements.tbody();
        this.table = ElementsFactory.elements.table()
                .appendChild(tBody);

        for (final SpreadsheetMetadataItemComponent<?> item : items) {
            final TableRowElement row = ElementsFactory.elements.tr();
            row.appendChild(
                    ElementsFactory.elements.td()
                            .setTextContent(
                                    label(item.propertyName)
                            )
            );
            row.appendChild(
                    ElementsFactory.elements.td()
                            .appendChild(
                                    item.element()
                            )
            );

            final Optional<Button> defaultButton = item.defaultButton();
            if (defaultButton.isPresent()) {
                row.appendChild(
                        ElementsFactory.elements.td()
                                .appendChild(defaultButton.get())
                );
            }
            tBody.appendChild(row);
        }

        this.items = items;
    }

    // factory methods to create the individual SpreadsheetMetadataItemComponent for a given property.

    private SpreadsheetMetadataItemComponent<?> spreadsheetId() {
        return text(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                SpreadsheetId::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> spreadsheetName() {
        return text(
                SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
                SpreadsheetName::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> creator() {
        return text(
                SpreadsheetMetadataPropertyName.CREATOR,
                EmailAddress::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> createdDateTime() {
        return text(
                SpreadsheetMetadataPropertyName.CREATE_DATE_TIME,
                this.context::formatDateTime
        );
    }

    private SpreadsheetMetadataItemComponent<?> modifiedBy() {
        return text(
                SpreadsheetMetadataPropertyName.MODIFIED_BY,
                EmailAddress::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> modifiedDateTime() {
        return text(
                SpreadsheetMetadataPropertyName.MODIFIED_DATE_TIME,
                this.context::formatDateTime
        );
    }

    private SpreadsheetMetadataItemComponent<?> locale() {
        return text(
                SpreadsheetMetadataPropertyName.LOCALE,
                Locale::toLanguageTag
        );
    }

    /**
     * Factory that creates a single ROW without any default button.
     */
    private <T> SpreadsheetMetadataItemComponent<?> text(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                         final Function<T, String> formatter) {
        return SpreadsheetMetadataItemComponent.text(
                propertyName,
                formatter,
                this.context
        );
    }

    private final SpreadsheetMetadataPanelComponentContext context;

    /**
     * Helper that converts a {@link SpreadsheetMetadataPropertyName} into a label for display.
     */
    private static String label(final SpreadsheetMetadataPropertyName<?> propertyName) {
        return CaseKind.KEBAB.change(
                propertyName.value(),
                CaseKind.TITLE
        );
    }

    // IsElement........................................................................................................

    @Override
    public HTMLTableElement element() {
        return this.table.element();
    }

    private final TableElement table;

    // ComponentLifecycle...............................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken ||
                token instanceof SpreadsheetMetadataPropertyStyleSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataHistoryToken;
    }

    @Override
    public boolean isOpen() {
        return this.drawer.isOpen();
    }

    @Override
    public void open(final AppContext context) {
        this.drawer.open(context);
    }

    @Override
    public void refresh(final AppContext context) {
        context.debug("SpreadsheetMetadataPanelComponent.refresh");

        this.items.forEach(i -> i.refresh(context));
    }

    private final List<SpreadsheetMetadataItemComponent<?>> items;

    @Override
    public void close(final AppContext context) {
        this.drawer.close(context);
    }

    /**
     * Typically the right drawer of an {@link org.dominokit.domino.ui.layout.AppLayout}.
     */
    private final OpenableComponent drawer;

    // SpreadsheetMetadataWatcher.............................................,.........................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.refreshIfOpen(context);
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.drawer.toString();
    }
}
