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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLUListElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.datetime.DateComponent;
import walkingkooka.spreadsheet.dominokit.dom.UlComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;


/**
 * A {@link SpreadsheetMetadataPanelComponentItem} for {@link SpreadsheetMetadataPropertyName#DATE_TIME_OFFSET}
 */
final class SpreadsheetMetadataPanelComponentItemDateTimeOffset extends SpreadsheetMetadataPanelComponentItem<Long, SpreadsheetMetadataPanelComponentItemDateTimeOffset, HTMLUListElement> {

    private final static SpreadsheetMetadataPropertyName<Long> PROPERTY_NAME = SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET;

    /**
     * The save value for the 1900 link
     */
    private final static long _1900 = 0;

    /**
     * The save value for the 1904 link.
     */
    private final static long _1904 = Converters.EXCEL_1904_DATE_SYSTEM_OFFSET - Converters.EXCEL_1900_DATE_SYSTEM_OFFSET;

    /**
     * Factory that creates a new {@link SpreadsheetMetadataPanelComponentItemDateTimeOffset}.
     */
    static SpreadsheetMetadataPanelComponentItemDateTimeOffset with(final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemDateTimeOffset(
            context
        );
    }

    private SpreadsheetMetadataPanelComponentItemDateTimeOffset(final SpreadsheetMetadataPanelComponentContext context) {
        super(
            PROPERTY_NAME,
            context
        );

        final UlComponent list = this.ul();

        final DateComponent dateComponent = DateComponent.empty(
            SpreadsheetMetadataPanelComponent.id(PROPERTY_NAME) + SpreadsheetElementIds.DATE,
            () -> context.now()
                .toLocalDate()
        );
        dateComponent.addValueWatcher2(
            (Optional<LocalDate> newValue) ->
                this.save(
                    toLong(newValue)
                        .map(Object::toString)
                        .orElse("")
                )
        );
        this.dateComponent = dateComponent;

        list.appendChild(
            li()
                .appendChild(dateComponent)
        );

        // build links for 1900 | 1904
        final HistoryToken token = context.historyToken();
        final Map<Long, HistoryTokenAnchorComponent> valueToAnchors = Maps.sorted();

        for (final Long value : Lists.of(_1900, _1904)) {
            final HistoryTokenAnchorComponent anchor = token
                .link(SpreadsheetMetadataPanelComponent.id(PROPERTY_NAME) + "-" + value)
                .setTabIndex(0)
                .setTextContent(
                    formatValue(value)
                );

            valueToAnchors.put(value, anchor);

            list.appendChild(
                li()
                    .appendChild(
                        anchor
                    )
            );
        }

        final HistoryTokenAnchorComponent defaultValueAnchor = this.defaultValueAnchor();
        list.appendChild(
            li()
                .appendChild(
                    defaultValueAnchor
                )
        );
        this.defaultValueAnchor = defaultValueAnchor;

        this.list = list;
        this.valueToAnchors = valueToAnchors;
    }

    @Override//
    SpreadsheetMetadataPanelComponentItemDateTimeOffset addFocusListener(final EventListener listener) {
        this.list.addFocusListener(listener);
        return this;
    }

    @Override
    void focus() {
        this.dateComponent.focus();
    }

    private final DateComponent dateComponent;

    private final HistoryTokenAnchorComponent defaultValueAnchor;

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final RefreshContext context) {
        final Optional<Long> metadataValue = this.context.spreadsheetMetadata()
            .getIgnoringDefaults(PROPERTY_NAME);

        this.dateComponent.setValue(
            toDate(metadataValue)
        );

        final HistoryToken token = context.historyToken()
            .setMetadataPropertyName(PROPERTY_NAME);

        // refresh the 1900 and 1904 links.
        for (final Entry<Long, HistoryTokenAnchorComponent> valueAndAnchor : this.valueToAnchors.entrySet()) {
            final Long value = valueAndAnchor.getKey();
            final HistoryTokenAnchorComponent anchor = valueAndAnchor.getValue();

            anchor.setHistoryToken(
                Optional.of(
                    token.setSaveValue(
                        Optional.of(value)
                    )
                )
            );
            anchor.setDisabled(
                Objects.equals(
                    metadataValue,
                    value
                )
            );
        }

        this.refreshDefaultValue(
            this.defaultValueAnchor,
            this.context.spreadsheetMetadata()
                .defaults()
                .get(this.propertyName)
                .map(this::formatValue)
                .orElse("0")
        );
    }

    private final Map<Long, HistoryTokenAnchorComponent> valueToAnchors;

    private String formatValue(final long value) {
        return _1900 == value ? "1900" : "1904";
    }

    // Long <-> Date....................................................................................................

    // @VisibleForTesting
    static Optional<LocalDate> toDate(final Optional<Long> value) {
        return Optional.ofNullable(
            value.isPresent() ?
                NUMBER_TO_DATE.convertOrFail(
                    value.get(),
                    LocalDate.class,
                    CONVERTER_CONTEXT
                ) :
                null
        );
    }

    private final static Converter<ConverterContext> NUMBER_TO_DATE = Converters.numberToLocalDate();

    // @VisibleForTesting
    static Optional<Long> toLong(final Optional<LocalDate> date) {
        return Optional.ofNullable(
            date.isPresent() ?
                LOCAL_DATE_TO_NUMBER.convertOrFail(
                    date.get(),
                    Long.class,
                    CONVERTER_CONTEXT
                ) :
                null
        );
    }

    private final static Converter<ConverterContext> LOCAL_DATE_TO_NUMBER = Converters.localDateToNumber();

    /**
     * A non null {@link ConverterContext} is required for the two methods to convert Long and LocaleDate for the {@link DateComponent}
     */
    // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/3241
    private final static ConverterContext CONVERTER_CONTEXT = new FakeConverterContext() {

        @Override
        public long dateOffset() {
            return Converters.JAVA_EPOCH_OFFSET;
        }
    };

    // HtmlComponentDelegator...........................................................................................


    @Override
    public HtmlComponent<HTMLUListElement, ?> htmlComponent() {
        return this.list;
    }

    private final UlComponent list;
}
