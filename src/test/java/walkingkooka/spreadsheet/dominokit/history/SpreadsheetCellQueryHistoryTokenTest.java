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
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQueryRequest;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.validation.ValueType;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellQueryHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellQueryHistoryToken> {
    private final static SpreadsheetCellQueryRequest FIND = SpreadsheetCellQueryRequest.empty()
        .setPath(
            Optional.of(SpreadsheetCellRangeReferencePath.LRTD)
        ).setOffset(OptionalInt.of(123))
        .setCount(OptionalInt.of(456))
        .setValueType(Optional.of(SpreadsheetValueType.ANY))
        .setQuery(
            Optional.of(
                SpreadsheetCellQuery.parse("789+blah()")
            )
        );

    // setQuery.........................................................................................................

    @Test
    public void testSetQueryWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken()
                .setQuery(null)
        );
    }

    @Test
    public void testSetQueryWithSame() {
        final SpreadsheetCellQueryHistoryToken token = this.createHistoryToken();

        assertSame(
            token,
            token.setQuery(token.query())
        );
    }

    @Test
    public void testSetQueryWithDifferent() {
        final Optional<SpreadsheetCellRangeReferencePath> path = Optional.of(
            SpreadsheetCellRangeReferencePath.BULR
        );

        final SpreadsheetCellQueryRequest find = SpreadsheetCellQueryRequest.empty()
            .setPath(path);

        this.checkEquals(
            SpreadsheetCellQueryHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                find
            ),
            this.createHistoryToken()
                .setQuery(find)
        );
    }

    // setSaveStringValue...............................................................................................

    @Test
    public void testSetSaveValueWithFormula() {
        final SpreadsheetCellQueryHistoryToken token = this.createHistoryToken()
            .setQuery(
                SpreadsheetCellQueryRequest.parse("/path/LRTD/offset/123/count/456/value-type/*/query/old()")
            ).cast(SpreadsheetCellQueryHistoryToken.class);

        final String text = "/query/textMatch(\"*1*\",cellFormula())";

        this.setSaveStringValueAndCheck(
            token.setQuery(
                SpreadsheetCellQueryRequest.empty()
            ),
            text,
            token.setQuery(
                SpreadsheetCellQueryRequest.parse(text)
            )
        );
    }

    // navigation.......................................................................................................

    @Test
    public void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck2(
            CELL.setDefaultAnchor(),
            "/123/SpreadsheetName456/cell/A1/query"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck2(
            CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/query"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck2(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left/query"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck2(
            LABEL.setDefaultAnchor(),
            "/123/SpreadsheetName456/cell/Label123/query"
        );
    }

    private void urlFragmentAndCheck2(final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection,
                                      final String expected) {
        this.urlFragmentAndCheck(
            SpreadsheetCellQueryHistoryToken.with(
                ID,
                NAME,
                anchoredSpreadsheetSelection,
                SpreadsheetCellQueryRequest.empty()
            ),
            UrlFragment.with(expected),
            anchoredSpreadsheetSelection::toString
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseWithoutArguments() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query",
            Optional.empty(), // path
            OptionalInt.empty(), // offset
            OptionalInt.empty(), // count
            Optional.empty(), // valueType
            Optional.empty() // query
        );
    }

    @Test
    public void testParseInvalidComponent() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/!invalid",
            Optional.empty(), // path
            OptionalInt.empty(), // offset
            OptionalInt.empty(), // count
            Optional.empty(), // valueType
            Optional.empty() // query
        );
    }

    @Test
    public void testParsePath() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR",
            Optional.of(
                SpreadsheetCellRangeReferencePath.BULR
            ), // path
            OptionalInt.empty(), // offset
            OptionalInt.empty(), // count
            Optional.empty(), // valueType
            Optional.empty() // query
        );
    }

    @Test
    public void testParsePathInvalidOffset() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/!invalid",
            HistoryToken.cellSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    @Test
    public void testParsePathOffset() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/path/LRBU/offset/1",
            Optional.of(
                SpreadsheetCellRangeReferencePath.LRBU
            ), // path
            OptionalInt.of(1), // offset
            OptionalInt.empty(), // count
            Optional.empty(), // valueType
            Optional.empty() // query
        );
    }

    @Test
    public void testParsePathOffsetInvalidCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/0/count/!invalid",
            HistoryToken.cellSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    @Test
    public void testParsePathOffsetCount() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/12/count/34",
            Optional.of(
                SpreadsheetCellRangeReferencePath.BULR
            ), // path
            OptionalInt.of(12), // offset
            OptionalInt.of(34), // count
            Optional.empty(), // valueType
            Optional.empty() // query
        );
    }

    @Test
    public void testParsePathOffsetCountValueType() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/123/count/456/value-type/" + SpreadsheetValueType.NUMBER,
            Optional.of(
                SpreadsheetCellRangeReferencePath.BULR
            ), // path
            OptionalInt.of(123), // offset
            OptionalInt.of(456), // count
            Optional.of(SpreadsheetValueType.NUMBER),
            Optional.empty() // query
        );
    }

    @Test
    public void testParsePathOffsetCountValueTypeEmptyQuery() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/",
            Optional.of(
                SpreadsheetCellRangeReferencePath.BULR
            ), // path
            OptionalInt.of(1234), // offset
            OptionalInt.of(5678), // count
            Optional.of(SpreadsheetValueType.DATE),
            Optional.empty() // query
        );
    }

    @Test
    public void testParsePathOffsetCountValueTypeQuery() {
        final String query = "Hello()";

        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/" + query,
            Optional.of(
                SpreadsheetCellRangeReferencePath.BULR
            ), // path
            OptionalInt.of(1234), // offset
            OptionalInt.of(5678), // count
            Optional.of(SpreadsheetValueType.DATE),
            Optional.of(
                SpreadsheetCellQuery.parse(query)
            )// query
        );
    }

    @Test
    public void testParsePathOffsetCountValueTypeQuery2() {
        final String query = "1/23*4/5";

        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.TIME + "/query/" + query,
            Optional.of(
                SpreadsheetCellRangeReferencePath.BULR
            ), // path
            OptionalInt.of(1234), // offset
            OptionalInt.of(5678), // count
            Optional.of(SpreadsheetValueType.TIME),
            Optional.of(
                SpreadsheetCellQuery.parse(query)
            )// query
        );
    }

    @Test
    public void testParseOffset() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/offset/1234",
            Optional.empty(), // path
            OptionalInt.of(1234), // offset
            OptionalInt.empty(), // count
            Optional.empty(), // valueType
            Optional.empty() // query
        );
    }

    @Test
    public void testParseCount() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/count/5678/",
            Optional.empty(), // path
            OptionalInt.empty(), // offset
            OptionalInt.of(5678), // count
            Optional.empty(), // valueType
            Optional.empty() // query
        );
    }

    @Test
    public void testParseOffsetCount() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/offset/1234/count/5678/",
            Optional.empty(), // path
            OptionalInt.of(1234), // offset
            OptionalInt.of(5678), // count
            Optional.empty(), // valueType
            Optional.empty() // query
        );
    }

    @Test
    public void testParseValueType() {
        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/value-type/" + SpreadsheetValueType.TIME,
            Optional.empty(), // path
            OptionalInt.empty(), // offset
            OptionalInt.empty(), // count
            Optional.of(SpreadsheetValueType.TIME),
            Optional.empty() // query
        );
    }

    @Test
    public void testParseValueTypeQuery() {
        final String query = "1/23*4/5";

        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/value-type/" + SpreadsheetValueType.TIME + "/query/" + query,
            Optional.empty(), // path
            OptionalInt.empty(), // offset
            OptionalInt.empty(), // count
            Optional.of(SpreadsheetValueType.TIME),
            Optional.of(
                SpreadsheetCellQuery.parse(query)
            )// query
        );
    }

    @Test
    public void testParseQuery() {
        final String query = "1/23*4/5";

        this.parseAndCheck2(
            "/123/SpreadsheetName456/cell/A1/query/query/" + query,
            Optional.empty(), // path
            OptionalInt.empty(), // offset
            OptionalInt.empty(), // count
            Optional.empty(), // valueType
            Optional.of(
                SpreadsheetCellQuery.parse(query)
            ) // query
        );
    }

    private void parseAndCheck2(final String url,
                                final Optional<SpreadsheetCellRangeReferencePath> path,
                                final OptionalInt offset,
                                final OptionalInt count,
                                final Optional<ValueType> valueType,
                                final Optional<SpreadsheetCellQuery> query) {
        this.parseAndCheck(
            url,
            SpreadsheetCellQueryHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                SpreadsheetCellQueryRequest.empty()
                    .setPath(path)
                    .setOffset(offset)
                    .setCount(count)
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // helpers..........................................................................................................

    @Override
    SpreadsheetCellQueryHistoryToken createHistoryToken(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellQueryHistoryToken.with(
            id,
            name,
            selection,
            FIND
        );
    }

    @Override
    public Class<SpreadsheetCellQueryHistoryToken> type() {
        return SpreadsheetCellQueryHistoryToken.class;
    }
}
