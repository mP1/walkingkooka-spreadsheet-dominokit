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
import walkingkooka.spreadsheet.dominokit.pattern.SpreadsheetPatternEditorComponentContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateTimeFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateTimeParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
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
                            .appendChild(item)
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

    private SpreadsheetMetadataItemComponent<?> expressionNumberKind() {
        return this.enumValue(
                SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND,
                Lists.of(ExpressionNumberKind.values())
        );
    }

    private SpreadsheetMetadataItemComponent<?> precision() {
        return text(
                SpreadsheetMetadataPropertyName.PRECISION,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> roundingMode() {
        return this.enumValue(
                SpreadsheetMetadataPropertyName.ROUNDING_MODE,
                Lists.of(RoundingMode.values())
        );
    }

    private SpreadsheetMetadataItemComponent<?> currencySymbol() {
        return text(
                SpreadsheetMetadataPropertyName.CURRENCY_SYMBOL,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> decimalSeparator() {
        return text(
                SpreadsheetMetadataPropertyName.DECIMAL_SEPARATOR,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> exponentSymbol() {
        return text(
                SpreadsheetMetadataPropertyName.EXPONENT_SYMBOL,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> groupSeparator() {
        return text(
                SpreadsheetMetadataPropertyName.GROUP_SEPARATOR,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> negativeSign() {
        return text(
                SpreadsheetMetadataPropertyName.NEGATIVE_SIGN,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> percentageSymbol() {
        return text(
                SpreadsheetMetadataPropertyName.PERCENTAGE_SYMBOL,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> positiveSign() {
        return text(
                SpreadsheetMetadataPropertyName.POSITIVE_SIGN,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> valueSeparator() {
        return text(
                SpreadsheetMetadataPropertyName.VALUE_SEPARATOR,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> dateTimeOffset() {
        return text(
                SpreadsheetMetadataPropertyName.DATETIME_OFFSET,
                Object::toString
        );
    }

    private SpreadsheetMetadataItemComponent<?> defaultYear() {
        return number(
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR,
                1800,
                2100
        );
    }

    private SpreadsheetMetadataItemComponent<?> twoDigitYear() {
        return number(
                SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR,
                0,
                99
        );
    }

    private SpreadsheetMetadataItemComponent<SpreadsheetDateFormatPattern> dateFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN,
                SpreadsheetPatternKind.DATE_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataItemComponent<SpreadsheetDateParsePattern> dateParsePattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.DATE_PARSE_PATTERN,
                SpreadsheetPatternKind.DATE_PARSE_PATTERN
        );
    }

    private SpreadsheetMetadataItemComponent<SpreadsheetDateTimeFormatPattern> dateTimeFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.DATETIME_FORMAT_PATTERN,
                SpreadsheetPatternKind.DATE_TIME_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataItemComponent<SpreadsheetDateTimeParsePattern> dateTimeParsePattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.DATETIME_PARSE_PATTERN,
                SpreadsheetPatternKind.DATE_TIME_PARSE_PATTERN
        );
    }

    private SpreadsheetMetadataItemComponent<SpreadsheetNumberFormatPattern> numberFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.NUMBER_FORMAT_PATTERN,
                SpreadsheetPatternKind.NUMBER_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataItemComponent<SpreadsheetNumberParsePattern> numberParsePattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.NUMBER_PARSE_PATTERN,
                SpreadsheetPatternKind.NUMBER_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataItemComponent<SpreadsheetTextFormatPattern> textFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.TEXT_FORMAT_PATTERN,
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataItemComponent<SpreadsheetTimeFormatPattern> timeFormatPattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.TIME_FORMAT_PATTERN,
                SpreadsheetPatternKind.TIME_FORMAT_PATTERN
        );
    }

    private SpreadsheetMetadataItemComponent<SpreadsheetTimeParsePattern> timeParsePattern() {
        return spreadsheetPattern(
                SpreadsheetMetadataPropertyName.TIME_PARSE_PATTERN,
                SpreadsheetPatternKind.TIME_PARSE_PATTERN
        );
    }

    /**
     * Factory that displays a slider with thumbs for each Enum value.
     */
    private <T extends Enum<T>> SpreadsheetMetadataItemComponentEnum<T> enumValue(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                  final List<T> values) {
        return SpreadsheetMetadataItemComponent.enumValue(
                propertyName,
                values,
                this.context
        );
    }

    /**
     * Factory that creates a number text field to edit a integer {@link SpreadsheetMetadataPropertyName} value.
     */
    private SpreadsheetMetadataItemComponent<?> number(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                       final int min,
                                                       final int max) {
        return SpreadsheetMetadataItemComponent.number(
                propertyName,
                min,
                max,
                this.context
        );
    }

    /**
     * Factory that creates a single ROW without any default button.
     * TODO add default button later.
     */
    private <T extends SpreadsheetPattern> SpreadsheetMetadataItemComponent<T> spreadsheetPattern(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                                  final SpreadsheetPatternKind patternKind) {
        return SpreadsheetMetadataItemComponent.spreadsheetPattern(
                propertyName,
                patternKind,
                this.context
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
        return token instanceof SpreadsheetMetadataHistoryToken &&
                false == SpreadsheetPatternEditorComponentContexts.isMetadata(token); // hide if SpreadsheetPatternEditorComponent is open.
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

    // id...............................................................................................................

    static String id(final SpreadsheetMetadataPropertyName<?> propertyName) {
        return "metadata-" + propertyName.value();
    }
}
