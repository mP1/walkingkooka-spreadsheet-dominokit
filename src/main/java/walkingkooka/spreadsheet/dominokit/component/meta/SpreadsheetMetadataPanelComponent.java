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

package walkingkooka.spreadsheet.dominokit.component.meta;

import elemental2.dom.HTMLTableElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.TBodyElement;
import org.dominokit.domino.ui.elements.TableElement;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.component.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.component.OpenableComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyStyleSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateTimeFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateTimeParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTextFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTimeFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTimeParsePattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * A component that displays numerous {@link SpreadsheetMetadata} properties with support for editing the individual values.
 */
public final class SpreadsheetMetadataPanelComponent implements ComponentLifecycle,
        IsElement<HTMLTableElement>,
        NopFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

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

        final List<SpreadsheetMetadataPanelComponentItem<?>> items = Lists.array();
        items.add(this.spreadsheetId());
        items.add(this.spreadsheetName());
        items.add(this.creator());
        items.add(this.createdDateTime());
        items.add(this.modifiedBy());
        items.add(this.modifiedDateTime());
        items.add(this.locale());

        // Number
        items.add(this.expressionNumberKind());
        items.add(this.precision());
        items.add(this.roundingMode());

        items.add(this.currencySymbol());
        items.add(this.decimalSeparator());
        items.add(this.exponentSymbol());
        items.add(this.groupSeparator());
        items.add(this.negativeSign());
        items.add(this.percentageSymbol());
        items.add(this.positiveSign());
        items.add(this.valueSeparator());

        items.add(this.dateTimeOffset());
        items.add(this.defaultYear());
        items.add(this.twoDigitYear());

        items.add(this.dateFormatPattern());
        items.add(this.dateParsePattern());

        items.add(this.dateTimeFormatPattern());
        items.add(this.dateTimeParsePattern());

        items.add(this.numberFormatPattern());
        items.add(this.numberParsePattern());

        items.add(this.textFormatPattern());

        items.add(this.timeFormatPattern());
        items.add(this.timeParsePattern());

        final TBodyElement tBody = ElementsFactory.elements.tbody();
        this.table = ElementsFactory.elements.table()
                .appendChild(tBody);

        for (final SpreadsheetMetadataPanelComponentItem<?> item : items) {
            tBody.appendChild(
                    ElementsFactory.elements.tr()
                            .appendChild(
                                    ElementsFactory.elements.td()
                                            .setCssProperty("text-wrap", "nowrap")
                                            .setPaddingTop("5px")
                                            .setPaddingBottom("5px")
                                            .setTextContent(
                                                    label(item.propertyName)
                                            )
                            ).appendChild(
                                    ElementsFactory.elements.td()
                                            .setPaddingTop("5px")
                                            .setPaddingBottom("5px")
                                            .appendChild(item)
                            )
            );
        }

        this.items = items;
    }

    // factory methods to create the individual SpreadsheetMetadataPanelComponentItem for a given property.

    private SpreadsheetMetadataPanelComponentItem<?> spreadsheetId() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                SpreadsheetId::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> spreadsheetName() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
                SpreadsheetName::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> creator() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.CREATOR,
                EmailAddress::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> createdDateTime() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.CREATE_DATE_TIME,
                this.context::formatDateTime
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> modifiedBy() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.MODIFIED_BY,
                EmailAddress::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> modifiedDateTime() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.MODIFIED_DATE_TIME,
                this.context::formatDateTime
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> locale() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.LOCALE,
                Locale::toLanguageTag
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> expressionNumberKind() {
        return this.enumValue(
                SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND,
                Lists.of(ExpressionNumberKind.values())
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> precision() {
        return SpreadsheetMetadataPanelComponentItem.precision(
                this.context
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> roundingMode() {
        return this.enumValue(
                SpreadsheetMetadataPropertyName.ROUNDING_MODE,
                Lists.of(RoundingMode.values())
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> currencySymbol() {
        return text(
                SpreadsheetMetadataPropertyName.CURRENCY_SYMBOL
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> decimalSeparator() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.DECIMAL_SEPARATOR,
                Object::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> exponentSymbol() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.EXPONENT_SYMBOL,
                Object::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> groupSeparator() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.GROUP_SEPARATOR,
                Object::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> negativeSign() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.NEGATIVE_SIGN,
                Object::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> percentageSymbol() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.PERCENTAGE_SYMBOL,
                Object::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> positiveSign() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.POSITIVE_SIGN,
                Object::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> valueSeparator() {
        return readOnlyText(
                SpreadsheetMetadataPropertyName.VALUE_SEPARATOR,
                Object::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> dateTimeOffset() {
        return SpreadsheetMetadataPanelComponentItem.dateTimeOffset(
                this.context
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> defaultYear() {
        return number(
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR,
                1800,
                2100
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?> twoDigitYear() {
        return number(
                SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR,
                0,
                99
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetDateFormatPattern> dateFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetDateParsePattern> dateParsePattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.DATE_PARSE_PATTERN
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetDateTimeFormatPattern> dateTimeFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.DATETIME_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetDateTimeParsePattern> dateTimeParsePattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.DATETIME_PARSE_PATTERN
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetNumberFormatPattern> numberFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.NUMBER_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetNumberParsePattern> numberParsePattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.NUMBER_PARSE_PATTERN
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetTextFormatPattern> textFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.TEXT_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetTimeFormatPattern> timeFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.TIME_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetTimeParsePattern> timeParsePattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.TIME_PARSE_PATTERN
        );
    }

    /**
     * Factory that displays a slider with thumbs for each Enum value.
     */
    private <T extends Enum<T>> SpreadsheetMetadataPanelComponentItemEnum<T> enumValue(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                       final List<T> values) {
        return SpreadsheetMetadataPanelComponentItem.enumValue(
                propertyName,
                values,
                this.context
        );
    }

    /**
     * Factory that creates a number text field to edit a integer {@link SpreadsheetMetadataPropertyName} value.
     */
    private SpreadsheetMetadataPanelComponentItem<?> number(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                            final int min,
                                                            final int max) {
        return SpreadsheetMetadataPanelComponentItem.number(
                propertyName,
                min,
                max,
                this.context
        );
    }

    /**
     * Factory that creates a single ROW.
     */
    private <T> SpreadsheetMetadataPanelComponentItem<?> readOnlyText(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                      final Function<T, String> formatter) {
        return SpreadsheetMetadataPanelComponentItem.readOnlyText(
                propertyName,
                formatter,
                this.context
        );
    }

    /**
     * Factory that creates a link that opens the pattern editor dialog
     */
    private <T extends SpreadsheetPattern> SpreadsheetMetadataPanelComponentItem<T> spreadsheetPattern(final SpreadsheetMetadataPropertyName<T> propertyName) {
        return SpreadsheetMetadataPanelComponentItem.spreadsheetPattern(
                propertyName,
                this.context
        );
    }

    /**
     * Factory that creates a single ROW with a single {@link org.dominokit.domino.ui.forms.TextBox}.
     */
    private <T> SpreadsheetMetadataPanelComponentItem<?> text(final SpreadsheetMetadataPropertyName<String> propertyName) {
        return SpreadsheetMetadataPanelComponentItem.text(
                propertyName,
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
        return token instanceof SpreadsheetMetadataHistoryToken &&
                false == token.isMetadataFormatPattern() &&
                false == token.isMetadataParsePattern(); // hide if SpreadsheetPatternEditorComponent is open.
    }

    @Override
    public boolean isOpen() {
        return this.drawer.isOpen();
    }

    @Override
    public void open(final AppContext context) {
        this.drawer.open(context);
        this.refresh(context);
    }

    @Override
    public void refresh(final AppContext context) {
        // Before refreshing verify a loaded Metadata with LOCALE and other properties are present otherwise
        // rendering the panel will fail complaining LOCALE Is absent.
        //
        // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/1018
        if (context.spreadsheetMetadata()
                .get(SpreadsheetMetadataPropertyName.LOCALE)
                .isPresent()) {
            context.debug(this.getClass().getSimpleName() + ".refresh");

            this.items.forEach(i -> i.refresh(context));
        } else {
            context.debug(this.getClass().getSimpleName() + ".refresh metadata missing " + SpreadsheetMetadataPropertyName.LOCALE + " skip until loaded.");
        }
    }

    private final List<SpreadsheetMetadataPanelComponentItem<?>> items;

    @Override
    public void close(final AppContext context) {
        this.drawer.close(context);
    }

    /**
     * Typically the right drawer of an {@link org.dominokit.domino.ui.layout.AppLayout}.
     */
    private final OpenableComponent drawer;

    // SpreadsheetMetadataFetcherWatcher.............................................,.........................................

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

    // id...............................................................................................................

    static String id(final SpreadsheetMetadataPropertyName<?> propertyName) {
        return "metadata-" + propertyName.value();
    }
}
