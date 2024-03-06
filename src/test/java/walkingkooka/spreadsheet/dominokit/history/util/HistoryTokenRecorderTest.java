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

package walkingkooka.spreadsheet.dominokit.history.util;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.UrlFragment;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSaveHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryTokenRecorderTest implements ClassTesting<HistoryTokenRecorder>,
        TreePrintableTesting,
        ToStringTesting<HistoryTokenRecorder> {

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
                        Predicates.fake(),
                        0
                )
        );
    }

    // onHistoryTokenChange.............................................................................................

    @Test
    public void testOnHistoryTokenChange() {
        final HistoryTokenRecorder recorder = HistoryTokenRecorder.with(
                (t) -> t instanceof SpreadsheetCellPatternSaveHistoryToken,
                3
        );

        final HistoryToken keep1 = HistoryToken.cellPatternSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetPatternKind.DATE_PARSE_PATTERN,
                Optional.of(
                        SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                )
        );

        final HistoryToken keep2 = HistoryToken.cellPatternSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                Optional.of(
                        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                )
        );

        final HistoryToken ignore1 = HistoryToken.cell(
                ID,
                NAME,
                CELL.setDefaultAnchor()
        );

        this.onHistoryChangeAndCheck(
                recorder,
                Lists.of(
                        keep1,
                        ignore1,
                        keep2
                ),
                keep2,
                keep1
        );
    }

    @Test
    public void testOnHistoryTokenChangeDuplicate() {
        final HistoryTokenRecorder recorder = HistoryTokenRecorder.with(
                (t) -> t instanceof SpreadsheetCellPatternSaveHistoryToken,
                3
        );

        final HistoryToken keep1 = HistoryToken.cellPatternSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetPatternKind.DATE_PARSE_PATTERN,
                Optional.of(
                        SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                )
        );

        final HistoryToken keep2 = HistoryToken.cellPatternSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                Optional.of(
                        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                )
        );

        final HistoryToken ignore1 = HistoryToken.cell(
                ID,
                NAME,
                CELL.setDefaultAnchor()
        );

        this.onHistoryChangeAndCheck(
                recorder,
                Lists.of(
                        keep1,
                        ignore1,
                        keep2,
                        keep1
                ),
                keep1,
                keep2
        );
    }

    @Test
    public void testOnHistoryTokenChangeMax() {
        final HistoryTokenRecorder recorder = HistoryTokenRecorder.with(
                (t) -> t instanceof SpreadsheetCellPatternSaveHistoryToken,
                3
        );

        final HistoryToken keep1 = HistoryToken.cellPatternSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                Optional.of(
                        SpreadsheetPattern.parseTextFormatPattern("@")
                )
        );

        final HistoryToken keep2 = HistoryToken.cellPatternSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                Optional.of(
                        SpreadsheetPattern.parseTextFormatPattern("@@")
                )
        );

        final HistoryToken keep3 = HistoryToken.cellPatternSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                Optional.of(
                        SpreadsheetPattern.parseTextFormatPattern("@@@")
                )
        );

        final HistoryToken ignore1 = HistoryToken.cell(
                ID,
                NAME,
                CELL.setDefaultAnchor()
        );

        final HistoryToken keep4 = HistoryToken.cellPatternSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                Optional.of(
                        SpreadsheetPattern.parseTextFormatPattern("@@@@")
                )
        );

        this.onHistoryChangeAndCheck(
                recorder,
                Lists.of(
                        keep1,
                        keep2,
                        keep3,
                        ignore1,
                        keep4
                ),
                keep4,
                keep3,
                keep2
        );
    }

    private void onHistoryChangeAndCheck(final HistoryTokenRecorder recorder,
                                         final List<HistoryToken> fired,
                                         final HistoryToken... expected) {
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
                recorder.tokens()
        );
    }

    // clear............................................................................................................

    @Test
    public void testClear() {
        final HistoryTokenRecorder recorder = HistoryTokenRecorder.with(
                Predicates.always(),
                3
        );

        recorder.onHistoryTokenChange(
                HistoryToken.unknown(
                        UrlFragment.SLASH
                ),
                this.appContext(
                        HistoryToken.cellPatternSave(
                                ID,
                                NAME,
                                CELL.setDefaultAnchor(),
                                SpreadsheetPatternKind.DATE_PARSE_PATTERN,
                                Optional.of(
                                        SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                                )
                        )
                )
        );

        recorder.clear();

        this.checkEquals(
                Lists.empty(),
                recorder.tokens(),
                "tokens"
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final HistoryTokenRecorder recorder = HistoryTokenRecorder.with(
                Predicates.always(),
                3
        );

        recorder.onHistoryTokenChange(
                HistoryToken.unknown(
                        UrlFragment.SLASH
                ),
                this.appContext(
                        HistoryToken.cellPatternSave(
                                ID,
                                NAME,
                                CELL.setDefaultAnchor(),
                                SpreadsheetPatternKind.DATE_PARSE_PATTERN,
                                Optional.of(
                                        SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                                )
                        )
                )
        );

        this.toStringAndCheck(
                recorder,
                "max=3 SpreadsheetCellPatternSaveHistoryToken \"/1/SpreadsheetName123/cell/A1/parse-pattern/date/save/dd/mm/yyyy\""
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
    public Class<HistoryTokenRecorder> type() {
        return HistoryTokenRecorder.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
