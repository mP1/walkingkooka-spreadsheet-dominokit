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

package walkingkooka.spreadsheet.dominokit.history.recent;

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormatterSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLocaleSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellParserSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellStyleSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValidatorSaveHistoryToken;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link RecentValueSavesContext} that watches {@link HistoryToken} events and updates numerous recorders.
 */
final class HistoryContextRecentValueSavesContext implements RecentValueSavesContext,
    HistoryTokenWatcher {

    static HistoryContextRecentValueSavesContext with(final HistoryContext context) {
        return new HistoryContextRecentValueSavesContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private HistoryContextRecentValueSavesContext(final HistoryContext historyContext) {
        historyContext.addHistoryTokenWatcher(this);

        this.recorders = Lists.array();
        this.typeToRecorder = Maps.hash();

        this.addRecorder(
            SpreadsheetFormatterSelector.class,
            (HistoryToken historyToken) -> Optional.ofNullable(
                historyToken instanceof SpreadsheetCellFormatterSaveHistoryToken ?
                    historyToken.cast(SpreadsheetCellFormatterSaveHistoryToken.class)
                        .spreadsheetFormatterSelector()
                        .orElse(null) :
                    null
            )
        );

        this.addRecorder(
            Locale.class,
            (HistoryToken historyToken) -> Optional.ofNullable(
                historyToken instanceof SpreadsheetCellLocaleSaveHistoryToken ?
                    historyToken.cast(SpreadsheetCellLocaleSaveHistoryToken.class)
                        .value()
                        .orElse(null) :
                    null
            )
        );

        this.addRecorder(
            SpreadsheetParserSelector.class,
            (HistoryToken historyToken) -> Optional.ofNullable(
                historyToken instanceof SpreadsheetCellParserSaveHistoryToken ?
                    historyToken.cast(SpreadsheetCellParserSaveHistoryToken.class)
                        .spreadsheetParserSelector()
                        .orElse(null) :
                    null
            )
        );

        this.addRecorder(
            TextStyleProperty.class,
            (HistoryToken historyToken) -> Optional.ofNullable(
                historyToken instanceof SpreadsheetCellStyleSaveHistoryToken ?
                    historyToken.cast(SpreadsheetCellStyleSaveHistoryToken.class)
                        .textStyleProperty() :
                    null
            )
        );

        this.addRecorder(
            ValidatorSelector.class,
            (HistoryToken historyToken) -> Optional.ofNullable(
                historyToken instanceof SpreadsheetCellValidatorSaveHistoryToken ?
                    historyToken.cast(SpreadsheetCellValidatorSaveHistoryToken.class)
                        .value()
                        .orElse(null) :
                    null
            )
        );
    }

    private final static int MAX_RECENT_COUNT = 3;

    private <T> void addRecorder(final Class<T> classs,
                                 final Function<HistoryToken, Optional<T>> mapper) {
        final HistoryTokenRecorder<T> recorder = HistoryTokenRecorder.with(
            mapper,
            MAX_RECENT_COUNT
        );
        this.recorders.add(recorder);
        this.typeToRecorder.put(
            classs,
            recorder
        );
    }

    // RecentValueSavesContext..........................................................................................

    @Override
    public <T> List<T> recentValueSaves(final Class<T> type) {
        Objects.requireNonNull(type, "type");

        List<T> values = Lists.empty();
        final HistoryTokenRecorder<T> recorder = (HistoryTokenRecorder<T>) this.typeToRecorder.get(type);
        if(null != recorder) {
            values = recorder.values();
        }

        return values;
    }

    private final Map<Class<?>, HistoryTokenRecorder<?>> typeToRecorder;

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        this.recorders.forEach(
            recorder -> recorder.onHistoryTokenChange(previous, context)
        );
    }

    private final List<HistoryTokenRecorder<?>> recorders;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.typeToRecorder.toString();
    }
}
