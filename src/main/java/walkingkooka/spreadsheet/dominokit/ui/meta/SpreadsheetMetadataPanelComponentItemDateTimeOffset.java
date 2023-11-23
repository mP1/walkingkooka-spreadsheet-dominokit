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

package walkingkooka.spreadsheet.dominokit.ui.meta;

import elemental2.dom.HTMLUListElement;
import org.dominokit.domino.ui.elements.UListElement;
import org.dominokit.domino.ui.forms.DateBox;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTime;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.Anchor;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;


/**
 * A {@link SpreadsheetMetadataPanelComponentItem} for {@link SpreadsheetMetadataPropertyName#DATETIME_OFFSET}
 */
final class SpreadsheetMetadataPanelComponentItemDateTimeOffset extends SpreadsheetMetadataPanelComponentItem<Long> {

    private final static SpreadsheetMetadataPropertyName<Long> PROPERTY_NAME = SpreadsheetMetadataPropertyName.DATETIME_OFFSET;

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
        checkContext(context);

        return new SpreadsheetMetadataPanelComponentItemDateTimeOffset(
                context
        );
    }

    private SpreadsheetMetadataPanelComponentItemDateTimeOffset(final SpreadsheetMetadataPanelComponentContext context) {
        super(
                PROPERTY_NAME,
                context
        );

        final UListElement list = this.uListElement();

        final DateBox dateBox = DateBox.create()
                .setId(SpreadsheetMetadataPanelComponent.id(PROPERTY_NAME) + "-DateBox")
                .setParseStrict(true) // use locale sensitive medium format.;
                .addChangeListener(
                        (final Date oldValue, final Date newValue) -> {
                            context.debug(this.getClass().getSimpleName() + ".onChange " + newValue);
                            this.save(
                                    null != newValue ?
                                            toLong(newValue).toString() :
                                            ""
                            );
                        }
                ).apply(
                        self ->
                                self.appendChild(
                                        PostfixAddOn.of(
                                                Icons.close_circle()
                                                        .clickable()
                                                        .addClickListener((e) -> self.clear())
                                        )
                                )
                );

        list.appendChild(
                liElement()
                        .appendChild(dateBox)
        );

        this.dateBox = dateBox.setWidth("200px")
                .setMarginBottom("0");

        // build links for 1900 | 1904
        final HistoryToken token = context.historyToken()
                .setMetadataPropertyName(PROPERTY_NAME);
        final Map<Long, Anchor> valueToAnchors = Maps.sorted();

        for (final Long value : Lists.of(_1900, _1904)) {
            final Anchor anchor = token
                    .setSave(value)
                    .link(SpreadsheetMetadataPanelComponent.id(PROPERTY_NAME) + "-" + value)
                    .setTabIndex(0)
                    .addPushHistoryToken(context)
                    .setTextContent(
                            formatValue(value)
                    );

            valueToAnchors.put(value, anchor);

            list.appendChild(
                    liElement()
                            .appendChild(
                                    anchor
                            )
            );
        }

        final Anchor defaultValueAnchor = this.defaultValueAnchor();
        list.appendChild(
                liElement()
                        .appendChild(
                                defaultValueAnchor
                        )
        );
        this.defaultValueAnchor = defaultValueAnchor;

        this.list = list;
        this.valueToAnchors = valueToAnchors;
    }

    private final DateBox dateBox;

    private final Anchor defaultValueAnchor;

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final AppContext context) {
        final Long metadataValue = context.spreadsheetMetadata()
                .getIgnoringDefaults(PROPERTY_NAME)
                .orElse(null);

        // refresh the pattern, locale might have changed
        this.dateBox.setPattern(this.context.datePattern())
                .setValue(
                        null != metadataValue ?
                                toDate(
                                        metadataValue
                                ) :
                                null
                );

        final HistoryToken token = context.historyToken()
                .setMetadataPropertyName(PROPERTY_NAME);

        // refresh the 1900 and 1904 links.
        for (final Entry<Long, Anchor> valueAndAnchor : this.valueToAnchors.entrySet()) {
            final Long value = valueAndAnchor.getKey();
            final Anchor anchor = valueAndAnchor.getValue();

            anchor.setHistoryToken(
                    Optional.of(
                            token.setSave(value)
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
                context.spreadsheetMetadata()
                        .defaults()
                        .get(this.propertyName)
                        .map(this::formatValue)
                        .orElse("")
        );
    }

    private final Map<Long, Anchor> valueToAnchors;

    // isElement........................................................................................................

    @Override
    public HTMLUListElement element() {
        return this.list.element();
    }

    private final UListElement list;

    private String formatValue(final long value) {
        return _1900 == value ? "1900" : "1904";
    }

    // Long <-> Date....................................................................................................

    // @VisibleForTesting
    static Date toDate(final Long longValue) {
        return DateTime.localDateTimeToDate(
                TO_DATE.convertOrFail(
                        longValue,
                        LocalDate.class,
                        CONVERTER_CONTEXT
                ).atTime(LocalTime.MIN)
        );
    }

    private final static Converter<ConverterContext> TO_DATE = Converters.numberLocalDate(
            Converters.EXCEL_1900_DATE_SYSTEM_OFFSET
    );

    // @VisibleForTesting
    static Long toLong(final Date date) {
        return TO_LONG.convertOrFail(
                DateTime.dateToLocalDateTime(date)
                        .toLocalDate(),
                Long.class,
                CONVERTER_CONTEXT
        );
    }

    private final static Converter<ConverterContext> TO_LONG = Converters.localDateNumber(
            -Converters.EXCEL_1900_DATE_SYSTEM_OFFSET
    );

    /**
     * A non null {@link ConverterContext} is required for the two methods to convert Long and Date for the {@link DateBox}
     */
    private final static ConverterContext CONVERTER_CONTEXT = ConverterContexts.fake();
}
