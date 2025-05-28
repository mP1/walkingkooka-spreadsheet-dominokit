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

import walkingkooka.NeverError;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.csv.CsvStringListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
 * - undo link
 * - close link
 * </pre>
 */
public final class DateTimeSymbolsDialogComponent implements SpreadsheetDialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    ComponentLifecycleMatcherDelegator {

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

        this.save = this.save();
        this.clear = this.clearAnchor();
        this.undo = this.undoAnchor();
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
    }

    // dialog...........................................................................................................

    private SpreadsheetDialogComponent dialogCreate() {
        final DateTimeSymbolsDialogComponentContext context = this.context;

        return SpreadsheetDialogComponent.smallerPrompt(
            ID + SpreadsheetElementIds.DIALOG,
            context.dialogTitle(),
            true, // includeClose
            context
        ).appendChild(
            SpreadsheetFlexLayout.row()
                .appendChild(this.ampms)
                .appendChild(this.monthNames)
                .appendChild(this.monthNameAbbreviations)
                .appendChild(this.weekDayNames)
                .appendChild(this.weekDayNameAbbreviations)
                .appendChild(this.dateTimeSymbols)
        ).appendChild(
            SpreadsheetLinkListComponent.empty()
                .appendChild(this.save)
                .appendChild(this.clear)
                .appendChild(this.undo)
                .appendChild(this.close)
        );
    }

    private final SpreadsheetDialogComponent dialog;

    private final DateTimeSymbolsDialogComponentContext context;

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = "dateTimeSymbols";

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
            ).setId(ID + id + SpreadsheetElementIds.TEXT_BOX)
            .setLabel(label)
            .addChangeListener(
                (Optional<CsvStringList> oldValue, Optional<CsvStringList> newValue) ->
                    this.refreshDateTimeSymbols(
                        propertyIndex,
                        newValue
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
            .addChangeListener(
                (Optional<DateTimeSymbols> oldValue, Optional<DateTimeSymbols> newValue) -> {
                    this.refreshDateTimeSymbolsComponentsAndSave(newValue);
                }
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
            this.save.setHistoryToken(
                Optional.of(
                    this.context.historyToken()
                        .setSaveValue(
                            Optional.of(dateTimeSymbols)
                        )
                )
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
        this.ampms.clear();
        this.monthNames.clear();
        this.monthNameAbbreviations.clear();
        this.weekDayNames.clear();
        this.weekDayNameAbbreviations.clear();
        this.dateTimeSymbols.clear();
    }

    private final DateTimeSymbolsComponent dateTimeSymbols;

    // save.............................................................................................................

    private HistoryTokenAnchorComponent save() {
        return this.anchor("Save");
    }

    private final HistoryTokenAnchorComponent save;

    // clear.............................................................................................................

    private HistoryTokenAnchorComponent clearAnchor() {
        return this.anchor("Clear");
    }

    private void refreshClear() {
        this.clear.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .clearSaveValue()
            )
        );
    }

    private final HistoryTokenAnchorComponent clear;

    // undo.............................................................................................................

    private HistoryTokenAnchorComponent undoAnchor() {
        return this.anchor("Undo");
    }

    private void refreshUndo() {
        this.undo.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .setSaveValue(
                        this.context.loadDateTimeSymbols()
                    )
            )
        );
    }

    private final HistoryTokenAnchorComponent undo;

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
        final Optional<DateTimeSymbols> dateTimeSymbols = this.context.loadDateTimeSymbols();
        this.dateTimeSymbols.setValue(dateTimeSymbols);
        this.refreshDateTimeSymbolsComponentsAndSave(dateTimeSymbols);

        this.refreshClear();
        this.refreshUndo();
        this.refreshClose();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
