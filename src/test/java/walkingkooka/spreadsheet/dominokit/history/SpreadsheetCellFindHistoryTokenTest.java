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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellFind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellFindHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellFindHistoryToken> {
    private final static SpreadsheetCellFind FIND = SpreadsheetCellFind.empty()
            .setPath(
                    Optional.of(SpreadsheetCellRangeReferencePath.LRTD)
            ).setOffset(OptionalInt.of(123))
            .setMax(OptionalInt.of(456))
            .setValueType(Optional.of(SpreadsheetValueType.ANY))
            .setQuery(Optional.of("=789+blah()"));

    // setPath..........................................................................................................

    @Test
    public void testSetFindNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken()
                        .setFind(null)
        );
    }

    @Test
    public void testSetFindSame() {
        final SpreadsheetCellFindHistoryToken token = this.createHistoryToken();

        assertSame(
                token,
                token.setFind(token.find())
        );
    }

    @Test
    public void testSetFindDifferent() {
        final Optional<SpreadsheetCellRangeReferencePath> path = Optional.of(
                SpreadsheetCellRangeReferencePath.BULR
        );

        final SpreadsheetCellFind find = SpreadsheetCellFind.empty()
                .setPath(path);

        this.checkEquals(
                SpreadsheetCellFindHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        find
                ),
                this.createHistoryToken()
                        .setFind(find)
        );
    }

    // patternKind......................................................................................................

    @Test
    public void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck2(
                CELL.setDefaultAnchor(),
                "/123/SpreadsheetName456/cell/A1/find"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck2(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/find"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck2(
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/*/top-left/find"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck2(
                LABEL.setDefaultAnchor(),
                "/123/SpreadsheetName456/cell/Label123/find"
        );
    }

    private void urlFragmentAndCheck2(final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection,
                                      final String expected) {
        this.urlFragmentAndCheck(
                SpreadsheetCellFindHistoryToken.with(
                        ID,
                        NAME,
                        anchoredSpreadsheetSelection,
                        SpreadsheetCellFind.empty()
                ),
                UrlFragment.with(expected),
                anchoredSpreadsheetSelection::toString
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseWithoutArguments() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find",
                Optional.empty(), // path
                OptionalInt.empty(), // offset
                OptionalInt.empty(), // max
                Optional.empty(), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParseInvalidComponent() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/!invalid",
                Optional.empty(), // path
                OptionalInt.empty(), // offset
                OptionalInt.empty(), // max
                Optional.empty(), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParsePath() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR",
                Optional.of(
                        SpreadsheetCellRangeReferencePath.BULR
                ), // path
                OptionalInt.empty(), // offset
                OptionalInt.empty(), // max
                Optional.empty(), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParsePathInvalidOffset() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/!invalid",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    @Test
    public void testParsePathOffset() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/path/LRBU/offset/1",
                Optional.of(
                        SpreadsheetCellRangeReferencePath.LRBU
                ), // path
                OptionalInt.of(1), // offset
                OptionalInt.empty(), // max
                Optional.empty(), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParsePathOffsetInvalidMax() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/0/max/!invalid",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    @Test
    public void testParsePathOffsetMax() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/12/max/34",
                Optional.of(
                        SpreadsheetCellRangeReferencePath.BULR
                ), // path
                OptionalInt.of(12), // offset
                OptionalInt.of(34), // max
                Optional.empty(), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParsePathOffsetMaxValueType() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/123/max/456/value-type/" + SpreadsheetValueType.NUMBER,
                Optional.of(
                        SpreadsheetCellRangeReferencePath.BULR
                ), // path
                OptionalInt.of(123), // offset
                OptionalInt.of(456), // max
                Optional.of(SpreadsheetValueType.NUMBER), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParsePathOffsetMaxValueTypeEmptyQuery() {
        final String query = "'Hello'";

        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/max/5678/value-type/" + SpreadsheetValueType.DATE + "/query/",
                Optional.of(
                        SpreadsheetCellRangeReferencePath.BULR
                ), // path
                OptionalInt.of(1234), // offset
                OptionalInt.of(5678), // max
                Optional.of(SpreadsheetValueType.DATE), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParsePathOffsetMaxValueTypeQuery() {
        final String query = "'Hello'";

        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/max/5678/value-type/" + SpreadsheetValueType.DATE + "/query/" + query,
                Optional.of(
                        SpreadsheetCellRangeReferencePath.BULR
                ), // path
                OptionalInt.of(1234), // offset
                OptionalInt.of(5678), // max
                Optional.of(SpreadsheetValueType.DATE), // valueType
                Optional.of(query) // query
        );
    }

    @Test
    public void testParsePathOffsetMaxValueTypeQuery2() {
        final String query = "=1/23*4/5";

        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/max/5678/value-type/" + SpreadsheetValueType.TIME + "/query/" + query,
                Optional.of(
                        SpreadsheetCellRangeReferencePath.BULR
                ), // path
                OptionalInt.of(1234), // offset
                OptionalInt.of(5678), // max
                Optional.of(SpreadsheetValueType.TIME), // valueType
                Optional.of(query) // query
        );
    }

    @Test
    public void testParseOffset() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/offset/1234",
                Optional.empty(), // path
                OptionalInt.of(1234), // offset
                OptionalInt.empty(), // max
                Optional.empty(), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParseMax() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/max/5678/",
                Optional.empty(), // path
                OptionalInt.empty(), // offset
                OptionalInt.of(5678), // max
                Optional.empty(), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParseOffsetMax() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/offset/1234/max/5678/",
                Optional.empty(), // path
                OptionalInt.of(1234), // offset
                OptionalInt.of(5678), // max
                Optional.empty(), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParseValueType() {
        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/value-type/" + SpreadsheetValueType.TIME,
                Optional.empty(), // path
                OptionalInt.empty(), // offset
                OptionalInt.empty(), // max
                Optional.of(SpreadsheetValueType.TIME), // valueType
                Optional.empty() // query
        );
    }

    @Test
    public void testParseValueTypeQuery() {
        final String query = "=1/23*4/5";

        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/value-type/" + SpreadsheetValueType.TIME + "/query/" + query,
                Optional.empty(), // path
                OptionalInt.empty(), // offset
                OptionalInt.empty(), // max
                Optional.of(SpreadsheetValueType.TIME), // valueType
                Optional.of(query) // query
        );
    }

    @Test
    public void testParseQuery() {
        final String query = "=1/23*4/5";

        this.parseAndCheck2(
                "/123/SpreadsheetName456/cell/A1/find/query/" + query,
                Optional.empty(), // path
                OptionalInt.empty(), // offset
                OptionalInt.empty(), // max
                Optional.empty(), // valueType
                Optional.of(query) // query
        );
    }

    private void parseAndCheck2(final String url,
                                final Optional<SpreadsheetCellRangeReferencePath> path,
                                final OptionalInt offset,
                                final OptionalInt max,
                                final Optional<String> valueType,
                                final Optional<String> query) {
        this.parseAndCheck(
                url,
                SpreadsheetCellFindHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        SpreadsheetCellFind.empty()
                                .setPath(path)
                                .setOffset(offset)
                                .setMax(max)
                                .setValueType(valueType)
                                .setQuery(query)
                )
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
                this.createHistoryToken(),
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithCell() {
        this.setMenuWithCellAndCheck();
    }

    // setAnchoredSelection.............................................................................................

    @Test
    public void testSetAnchoredSelectionDifferent() {
        final AnchoredSpreadsheetSelection newSelection = SpreadsheetSelection.parseCellRange("B2:C3")
                .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
                this.createHistoryToken(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:B2")
                                .setDefaultAnchor()
                ),
                newSelection,
                this.createHistoryToken(
                        ID,
                        NAME,
                        newSelection
                )
        );
    }

    @Test
    public void testSetAnchoredSelectionDifferentColumn() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseColumn("B")
                .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
                this.createHistoryToken(),
                different,
                HistoryToken.column(
                        ID,
                        NAME,
                        different
                )
        );
    }

    @Test
    public void testSetAnchoredSelectionDifferentRow() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseRow("2")
                .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
                this.createHistoryToken(),
                different,
                HistoryToken.row(
                        ID,
                        NAME,
                        different
                )
        );
    }

    // ClassTesting....................................................................................................

    // helpers..........................................................................................................

    @Override
    SpreadsheetCellFindHistoryToken createHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellFindHistoryToken.with(
                id,
                name,
                selection,
                FIND
        );
    }

    @Override
    public Class<SpreadsheetCellFindHistoryToken> type() {
        return SpreadsheetCellFindHistoryToken.class;
    }
}
