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

package walkingkooka.spreadsheet.dominokit.datetimesymbols;

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.NeverError;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.csv.CsvStringListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.DateTimeSymbolsFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.locale.LocaleComponent;
import walkingkooka.spreadsheet.dominokit.locale.LocaleComponentContext;
import walkingkooka.spreadsheet.dominokit.locale.LocaleComponentSuggestionsValue;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.server.datetimesymbols.DateTimeSymbolsHateosResource;
import walkingkooka.spreadsheet.server.datetimesymbols.DateTimeSymbolsHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleTag;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A model dialog that includes numerous form fields supporting the editing on individual {@link walkingkooka.datetime.DateTimeSymbols}
 * which are aggregated and then saved.
 * <pre>
 * - ampm textbox
 * - monthNames textbox
 * - monthNameAbbreviations textbox
 * - weekDayNames textbox
 * - weekDayNameAbbreviations textbox
 * - dateTimeSymbols textbox
 * - save link
 * - clear link
 * - copy defaults link
 * - undo link
 * - close link
 * </pre>
 */
public final class DateTimeSymbolsDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    ComponentLifecycleMatcherDelegator,
    DateTimeSymbolsFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    /**
     * Creates a new {@link DateTimeSymbolsDialogComponent}.
     */
    public static DateTimeSymbolsDialogComponent with(final DateTimeSymbolsDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new DateTimeSymbolsDialogComponent(context);
    }

    private DateTimeSymbolsDialogComponent(final DateTimeSymbolsDialogComponentContext context) {
        this.context = context;

        this.ampms = this.ampms();

        this.monthNames = this.monthNames();
        this.monthNameAbbreviations = this.monthNameAbbreviations();

        this.weekDayNames = this.weekDayNames();
        this.weekDayNameAbbreviations = this.weekDayNameAbbreviations();

        this.dateTimeSymbols = this.dateTimeSymbols();

        this.localeLoad = this.localeLoad(context);

        this.save = this.<DateTimeSymbols>saveValueAnchor(context)
            .autoDisableWhenMissingValue();

        this.copyDefaults = this.copyDefaultValueAnchor(context);

        this.clear = this.clearValueAnchor(context);
        this.undo = this.undoAnchor(context);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addDateTimeSymbolsFetcherWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);
    }

    // dialog...........................................................................................................

    private DialogComponent dialogCreate() {
        final DateTimeSymbolsDialogComponentContext context = this.context;

        return DialogComponent.smallerPrompt(
            ID + SpreadsheetElementIds.DIALOG,
            DialogComponent.INCLUDE_CLOSE,
            context
        ).appendChild(
            FlexLayoutComponent.row()
                .appendChild(this.ampms)
                .appendChild(this.monthNames)
                .appendChild(this.monthNameAbbreviations)
                .appendChild(this.weekDayNames)
                .appendChild(this.weekDayNameAbbreviations)
                .appendChild(this.dateTimeSymbols)
        ).appendChild(
            this.localeLoad
        ).appendChild(
            AnchorListComponent.empty()
                .appendChild(this.save)
                .appendChild(this.clear)
                .appendChild(this.undo)
                .appendChild(this.copyDefaults)
                .appendChild(this.close)
        );
    }

    private final DialogComponent dialog;

    private final DateTimeSymbolsDialogComponentContext context;

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = DateTimeSymbols.class.getSimpleName();

    private final static String ID_PREFIX = ID + "-";

    // ampms............................................................................................................

    private CsvStringListComponent ampms() {
        return csvStringListComponent(
            "ampms", // id
            "AM/PM",
            DateTimeSymbols.AMPM_COUNT_MIN,
            DateTimeSymbols.AMPM_COUNT_MAX,
            AMPM_PROPERTY_INDEX

        );
    }

    private final CsvStringListComponent ampms;

    // monthNames.......................................................................................................

    private CsvStringListComponent monthNames() {
        return csvStringListComponent(
            "monthNames", // id
            "Month names",
            DateTimeSymbols.MONTH_COUNT_MIN,
            DateTimeSymbols.MONTH_COUNT_MAX,
            MONTH_NAMES_PROPERTY_INDEX
        );
    }

    private final CsvStringListComponent monthNames;

    // monthNameAbbreviations...........................................................................................

    private CsvStringListComponent monthNameAbbreviations() {
        return csvStringListComponent(
            "monthNameAbbreviations", // id
            "Month name abbreviations",
            DateTimeSymbols.MONTH_COUNT_MIN,
            DateTimeSymbols.MONTH_COUNT_MAX,
            MONTH_NAMES_ABBREVIATIONS_PROPERTY_INDEX
        );
    }

    private final CsvStringListComponent monthNameAbbreviations;

    // weekDayNames.....................................................................................................

    private CsvStringListComponent weekDayNames() {
        return csvStringListComponent(
            "weekDayNames", // id
            "Week day names",// label
            DateTimeSymbols.WEEK_DAY_COUNT_MIN,
            DateTimeSymbols.WEEK_DAY_COUNT_MAX,
            WEEK_DAY_NAMES_PROPERTY_INDEX
        );
    }

    private final CsvStringListComponent weekDayNames;

    // weekDayNameAbbreviations.........................................................................................

    private CsvStringListComponent weekDayNameAbbreviations() {
        return csvStringListComponent(
            "weekDayNameAbbreviations", // id
            "Week day names Abbreviations",// label
            DateTimeSymbols.WEEK_DAY_COUNT_MIN,
            DateTimeSymbols.WEEK_DAY_COUNT_MAX,
            WEEK_DAY_NAMES_ABBREVIATIONS_PROPERTY_INDEX
        );
    }

    private final CsvStringListComponent weekDayNameAbbreviations;

    private CsvStringListComponent csvStringListComponent(final String id,
                                                          final String label,
                                                          final int min,
                                                          final int max,
                                                          final int propertyIndex) {
        return CsvStringListComponent.empty(
                min,
                max,
                CsvStringListComponent.INCLUSIVE
            ).setId(ID + "-" + id + SpreadsheetElementIds.TEXT_BOX)
            .setLabel(label)
            .addValueWatcher2(
                (Optional<CsvStringList> value) ->
                    this.refreshDateTimeSymbols(
                        propertyIndex,
                        value
                    )
            );
    }

    /**
     * Target for a {@link CsvStringListComponent} onchange event to update {@link #dateTimeSymbols}.
     */
    private void refreshDateTimeSymbols(final int propertyIndex,
                                        final Optional<CsvStringList> value) {
        try {
            Optional<CsvStringList> ampm = this.ampms.value();
            Optional<CsvStringList> monthNames = this.monthNames.value();
            Optional<CsvStringList> monthNamesAbbreviations = this.monthNameAbbreviations.value();
            Optional<CsvStringList> weekDayNames = this.weekDayNames.value();
            Optional<CsvStringList> weekDayNamesAbbreviations = this.weekDayNameAbbreviations.value();


            switch (propertyIndex) {
                case AMPM_PROPERTY_INDEX:
                    ampm = value;
                    break;
                case MONTH_NAMES_PROPERTY_INDEX:
                    monthNames = value;
                    break;
                case MONTH_NAMES_ABBREVIATIONS_PROPERTY_INDEX:
                    monthNamesAbbreviations = value;
                    break;
                case WEEK_DAY_NAMES_PROPERTY_INDEX:
                    weekDayNames = value;
                    break;
                case WEEK_DAY_NAMES_ABBREVIATIONS_PROPERTY_INDEX:
                    weekDayNamesAbbreviations = value;
                    break;
                default:
                    NeverError.unhandledCase(
                        propertyIndex,
                        AMPM_PROPERTY_INDEX,
                        MONTH_NAMES_PROPERTY_INDEX,
                        MONTH_NAMES_ABBREVIATIONS_PROPERTY_INDEX,
                        WEEK_DAY_NAMES_PROPERTY_INDEX,
                        WEEK_DAY_NAMES_ABBREVIATIONS_PROPERTY_INDEX
                    );
            }

            final DateTimeSymbols dateTimeSymbols = DateTimeSymbols.with(
                ampm.orElse(CsvStringList.EMPTY), // List<String>
                monthNames.orElse(CsvStringList.EMPTY),
                monthNamesAbbreviations.orElse(CsvStringList.EMPTY),
                weekDayNames.orElse(CsvStringList.EMPTY),
                weekDayNamesAbbreviations.orElse(CsvStringList.EMPTY)
            );

            this.dateTimeSymbols.setValue(
                Optional.of(dateTimeSymbols)
            );

            this.refreshDateTimeSymbolsComponentsAndSave(
                Optional.of(dateTimeSymbols)
            );

        } catch (final RuntimeException ignore) {
            // unable to update #dateTimeSymbols
            ignore.printStackTrace();
        }
    }

    private final static int AMPM_PROPERTY_INDEX = 1;

    private final static int MONTH_NAMES_PROPERTY_INDEX = 2;

    private final static int MONTH_NAMES_ABBREVIATIONS_PROPERTY_INDEX = 3;

    private final static int WEEK_DAY_NAMES_PROPERTY_INDEX = 4;

    private final static int WEEK_DAY_NAMES_ABBREVIATIONS_PROPERTY_INDEX = 5;

    // dateTimeSymbols..................................................................................................

    private DateTimeSymbolsComponent dateTimeSymbols() {
        return DateTimeSymbolsComponent.empty()
            .setLabel("Date Time Symbols")
            .addValueWatcher2(
                this::refreshDateTimeSymbolsComponentsAndSave
            );
    }

    /**
     * Refreshes other components after a {@link #dateTimeSymbols} change listener event using its new {@link DateTimeSymbols}.
     */
    private void refreshDateTimeSymbolsComponentsAndSave(final Optional<DateTimeSymbols> maybeDateTimeSymbols) {
        if (maybeDateTimeSymbols.isPresent()) {
            final DateTimeSymbols dateTimeSymbols = maybeDateTimeSymbols.get();

            this.ampms.setValue(
                toCsvStringList(
                    dateTimeSymbols.ampms()
                )
            );
            this.monthNames.setValue(
                toCsvStringList(
                    dateTimeSymbols.monthNames()
                )
            );
            this.monthNameAbbreviations.setValue(
                toCsvStringList(
                    dateTimeSymbols.monthNameAbbreviations()
                )
            );
            this.weekDayNames.setValue(
                toCsvStringList(
                    dateTimeSymbols.weekDayNames()
                )
            );
            this.weekDayNameAbbreviations.setValue(
                toCsvStringList(
                    dateTimeSymbols.weekDayNameAbbreviations()
                )
            );
            this.save.setValue(
                Optional.of(dateTimeSymbols)
            );
        } else {
            this.clear();
        }
    }

    private static Optional<CsvStringList> toCsvStringList(final List<String> csv) {
        return Optional.ofNullable(
            csv.isEmpty() ?
                null :
                CsvStringList.EMPTY.setElements(csv)
        );
    }

    private void clear() {
        this.ampms.clearValue();
        this.monthNames.clearValue();
        this.monthNameAbbreviations.clearValue();
        this.weekDayNames.clearValue();
        this.weekDayNameAbbreviations.clearValue();
        this.dateTimeSymbols.clearValue();

        this.save.clearValue();
    }

    private final DateTimeSymbolsComponent dateTimeSymbols;

    // save.............................................................................................................

    private final HistoryTokenSaveValueAnchorComponent<DateTimeSymbols> save;

    // copyDefaults.....................................................................................................

    /**
     * This link will copy the {@link DateTimeSymbols} from the locale etc.
     */
    private HistoryTokenSaveValueAnchorComponent<DateTimeSymbols> copyDefaultValueAnchor(final DateTimeSymbolsDialogComponentContext context) {
        return HistoryTokenSaveValueAnchorComponent.<DateTimeSymbols>with(
                this.idPrefix() +
                    "copyDefaults" +
                    SpreadsheetElementIds.LINK,
                context
            ).setTextContent("Copy Defaults")
            .autoDisableWhenMissingValue();
    }

    private void refreshCopyDefaults() {
        this.copyDefaults.setValue(
            this.context.copyDateTimeSymbols()
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<DateTimeSymbols> copyDefaults;
    // clear.............................................................................................................

    private void refreshClear() {
        this.clear.clearValue();
    }

    private final HistoryTokenSaveValueAnchorComponent<DateTimeSymbols> clear;

    // loadLocale.......................................................................................................

    /**
     * A locale drop down that when selected loads the symbols for the selected Locale.
     */
    private LocaleComponent<DateTimeSymbols> localeLoad(final DateTimeSymbolsDialogComponentContext context) {
        return LocaleComponent.empty(
            new LocaleComponentContext<DateTimeSymbols>() {

                @Override
                public void filter(final String startsWith,
                                   final SuggestBoxComponent<LocaleComponentSuggestionsValue<DateTimeSymbols>> suggestBox) {
                    DateTimeSymbolsDialogComponent.this.context.findDateTimeSymbolsWithLocaleStartsWith(startsWith);
                }

                @Override
                public MenuItem<LocaleComponentSuggestionsValue<DateTimeSymbols>> createMenuItem(final LocaleComponentSuggestionsValue<DateTimeSymbols> value) {
                    return this.historyTokenMenuItem(
                        ID,
                        value,
                        DateTimeSymbolsDialogComponent.this.context
                    );
                }

                @Override
                public Optional<LocaleComponentSuggestionsValue<DateTimeSymbols>> toValue(final Locale locale) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void verifyOption(final LocaleComponentSuggestionsValue<DateTimeSymbols> value,
                                         final SuggestBoxComponent<LocaleComponentSuggestionsValue<DateTimeSymbols>> suggestBox) {
                    throw new UnsupportedOperationException();
                }
            }
        ).setLabel("Load from Locale")
            .optional();
    }

    /**
     * This {@link LocaleComponent} is only intended to support search for a Locale, and to allow the user
     * to click any of the suggestions. Operations such as {@link LocaleComponent#setValue(Optional)} are
     * not supported.
     */
    private final LocaleComponent<DateTimeSymbols> localeLoad;

    // undo.............................................................................................................

    private void refreshUndo() {
        this.undo.setValue(
            this.context.loadDateTimeSymbols()
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<DateTimeSymbols> undo;

    // close............................................................................................................

    private void refreshClose() {
        this.close.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .close()
            )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
    }

    @Override
    public void dialogReset() {
        this.clear();
    }

    /**
     * Give focus to the AM PM field.
     */
    @Override
    public void openGiveFocus(final RefreshContext context) {
        context.giveFocus(
            this.ampms::focus
        );
    }

    /**
     * Refreshes all components from the current {@link DateTimeSymbols} which may be the cell or metadata property.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        final Optional<DateTimeSymbols> dateTimeSymbols = this.context.loadDateTimeSymbols();
        this.dateTimeSymbols.setValue(dateTimeSymbols);
        this.refreshDateTimeSymbolsComponentsAndSave(dateTimeSymbols);

        this.refreshCopyDefaults();
        this.refreshClear();
        this.refreshUndo();
        this.refreshClose();
    }

    @Override
    public  boolean shouldLogLifecycleChanges() {
        return DATE_TIME_SYMBOLS_DIALOG_COMPONENT;
    }

    // DateTimeSymbolsFetcherWatcher....................................................................................

    @Override
    public void onDateTimeSymbolsHateosResource(final LocaleTag id,
                                                final DateTimeSymbolsHateosResource locale) {
        // NOP
    }

    @Override
    public void onDateTimeSymbolsHateosResourceSet(final String localeStartsWith,
                                                   final DateTimeSymbolsHateosResourceSet symbols) {
        this.localeLoad.suggestBoxComponent()
            .setOptions(
                symbols.stream()
                    .map(LocaleComponentSuggestionsValue::fromDateTimeSymbolsHateosResource)
                    .collect(Collectors.toList())
            );
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    // eventually refresh will read the updated *CELL* from the cache
    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta) {
        this.refreshIfOpen(this.context);
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.refreshIfOpen(this.context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
        // Ignore many
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
