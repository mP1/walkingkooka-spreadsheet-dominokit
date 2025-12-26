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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormatterSaveHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryTokenRecorderTest implements ClassTesting<HistoryTokenRecorder<?>>,
    TreePrintableTesting,
    ToStringTesting<HistoryTokenRecorder<?>> {

    private final static SpreadsheetId ID = SpreadsheetId.with(1);

    private final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName123");

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    @Test
    public void testWithNullPredicateFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryTokenRecorder.with(
                null,
                1
            )
        );
    }

    @Test
    public void testWithZeroMaxFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> HistoryTokenRecorder.with(
                (t) -> {
                    throw new UnsupportedOperationException();
                },
                0
            )
        );
    }

    // onHistoryTokenChange.............................................................................................

    @Test
    public void testOnHistoryTokenChange() {
        final HistoryTokenRecorder<SpreadsheetFormatterSelector> recorder = HistoryTokenRecorder.with(
            (t) -> Optional.ofNullable(
                t instanceof SpreadsheetCellFormatterSaveHistoryToken ?
                    t.cast(
                            SpreadsheetCellFormatterSaveHistoryToken.class
                        ).spreadsheetFormatterSelector()
                        .orElse(null) :
                    null
            ),
            3
        );

        final SpreadsheetFormatterSelector keep1 = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
            .spreadsheetFormatterSelector();

        final SpreadsheetFormatterSelector keep2 = SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
            .spreadsheetFormatterSelector();

        this.onHistoryChangeAndCheck(
            recorder,
            Lists.of(
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep1)
                ),
                HistoryToken.cellSelect(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor()
                ),
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep2)
                )
            ),
            keep2,
            keep1
        );
    }

    @Test
    public void testOnHistoryTokenChangeDuplicate() {
        final HistoryTokenRecorder<SpreadsheetFormatterSelector> recorder = HistoryTokenRecorder.with(
            (t) -> Optional.ofNullable(
                t instanceof SpreadsheetCellFormatterSaveHistoryToken ?
                    t.cast(
                            SpreadsheetCellFormatterSaveHistoryToken.class
                        ).spreadsheetFormatterSelector()
                        .orElse(null) :
                    null
            ),
            3
        );

        final SpreadsheetFormatterSelector keep1 = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
            .spreadsheetFormatterSelector();

        final SpreadsheetFormatterSelector keep2 = SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
            .spreadsheetFormatterSelector();

        final HistoryToken ignore1 = HistoryToken.cellSelect(
            ID,
            NAME,
            CELL.setDefaultAnchor()
        );

        this.onHistoryChangeAndCheck(
            recorder,
            Lists.of(
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep1)
                ),
                ignore1,
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep2)
                ),
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep1)
                )
            ),
            keep1,
            keep2
        );
    }

    @Test
    public void testOnHistoryTokenChangeMax() {
        final HistoryTokenRecorder<SpreadsheetFormatterSelector> recorder = HistoryTokenRecorder.with(
            (t) -> Optional.ofNullable(
                t instanceof SpreadsheetCellFormatterSaveHistoryToken ?
                    t.cast(
                            SpreadsheetCellFormatterSaveHistoryToken.class
                        ).spreadsheetFormatterSelector()
                        .orElse(null) :
                    null
            ),
            3
        );

        final SpreadsheetFormatterSelector keep1 = SpreadsheetPattern.parseTextFormatPattern("@")
            .spreadsheetFormatterSelector();

        final SpreadsheetFormatterSelector keep2 = SpreadsheetPattern.parseTextFormatPattern("@@")
            .spreadsheetFormatterSelector();

        final SpreadsheetFormatterSelector keep3 = SpreadsheetPattern.parseTextFormatPattern("@@@")
            .spreadsheetFormatterSelector();

        final SpreadsheetFormatterSelector keep4 = SpreadsheetPattern.parseTextFormatPattern("@@@@")
            .spreadsheetFormatterSelector();

        this.onHistoryChangeAndCheck(
            recorder,
            Lists.of(
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep1)
                ),
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep2)
                ),
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep3)
                ),
                HistoryToken.cellSelect(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor()
                ),
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep4)
                )
            ),
            keep4,
            keep3,
            keep2
        );
    }

    @Test
    public void testOnHistoryTokenChangeSpreadsheetIdChange() {
        final HistoryTokenRecorder<SpreadsheetFormatterSelector> recorder = HistoryTokenRecorder.with(
            (t) -> Optional.ofNullable(
                t instanceof SpreadsheetCellFormatterSaveHistoryToken ?
                    t.cast(
                            SpreadsheetCellFormatterSaveHistoryToken.class
                        ).spreadsheetFormatterSelector()
                        .orElse(null) :
                    null
            ),
            3
        );

        final SpreadsheetFormatterSelector keep1 = SpreadsheetPattern.parseTextFormatPattern("@")
            .spreadsheetFormatterSelector();

        final SpreadsheetFormatterSelector keep2 = SpreadsheetPattern.parseTextFormatPattern("@@")
            .spreadsheetFormatterSelector();

        this.onHistoryChangeAndCheck(
            recorder,
            Lists.of(
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep1)
                ),
                HistoryToken.cellFormatterSave(
                    SpreadsheetId.with(2),
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(keep2)
                )
            ),
            keep2
        );
    }

    private void onHistoryChangeAndCheck(final HistoryTokenRecorder<?> recorder,
                                         final List<HistoryToken> fired,
                                         final SpreadsheetFormatterSelector... expected) {
        HistoryToken previous = HistoryToken.unknown(UrlFragment.SLASH);

        for (final HistoryToken token : fired) {
            recorder.onHistoryTokenChange(
                previous,
                this.appContext(token)
            );
            previous = token;
        }

        this.checkEquals(
            Lists.of(
                expected
            ),
            recorder.values()
        );
    }

    // clear............................................................................................................

    @Test
    public void testClear() {
        final HistoryTokenRecorder<SpreadsheetFormatterSelector> recorder = HistoryTokenRecorder.with(
            (t) -> Optional.ofNullable(
                t instanceof SpreadsheetCellFormatterSaveHistoryToken ?
                    t.cast(
                            SpreadsheetCellFormatterSaveHistoryToken.class
                        ).spreadsheetFormatterSelector()
                        .orElse(null) :
                    null
            ),
            3
        );

        recorder.onHistoryTokenChange(
            HistoryToken.unknown(
                UrlFragment.SLASH
            ),
            this.appContext(
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(
                        SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                            .spreadsheetFormatterSelector()
                    )
                )
            )
        );

        recorder.clear();

        this.checkEquals(
            Lists.empty(),
            recorder.values(),
            "values"
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final HistoryTokenRecorder<SpreadsheetFormatterSelector> recorder = HistoryTokenRecorder.with(
            (t) -> Optional.ofNullable(
                t.cast(SpreadsheetCellFormatterSaveHistoryToken.class)
                    .spreadsheetFormatterSelector()
                    .get()
            ),
            3
        );

        recorder.onHistoryTokenChange(
            HistoryToken.unknown(
                UrlFragment.SLASH
            ),
            this.appContext(
                HistoryToken.cellFormatterSave(
                    ID,
                    NAME,
                    CELL.setDefaultAnchor(),
                    Optional.of(
                        SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                            .spreadsheetFormatterSelector()
                    )
                )
            )
        );

        this.toStringAndCheck(
            recorder,
            "max=3 date dd/mm/yyyy"
        );
    }

    private AppContext appContext(final HistoryToken token) {
        return new FakeAppContext() {
            @Override
            public HistoryToken historyToken() {
                return token;
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<HistoryTokenRecorder<?>> type() {
        return Cast.to(HistoryTokenRecorder.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
