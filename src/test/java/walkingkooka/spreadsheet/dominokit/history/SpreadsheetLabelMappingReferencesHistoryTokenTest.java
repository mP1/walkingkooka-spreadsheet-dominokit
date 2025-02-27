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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetLabelMappingReferencesHistoryTokenTest extends SpreadsheetLabelMappingHistoryTokenTestCase<SpreadsheetLabelMappingReferencesHistoryToken> {

    // offset...........................................................................................................

    @Test
    public void testOffset() {
        final SpreadsheetLabelMappingReferencesHistoryToken historyToken = this.createHistoryToken();
        this.checkEquals(
            OptionalInt.empty(),
            historyToken.offset()
        );
    }

    @Test
    public void testOffset2() {
        final int offset = 123;

        final SpreadsheetLabelMappingReferencesHistoryToken historyToken = SpreadsheetLabelMappingReferencesHistoryToken.with(
            ID,
            NAME,
            LABEL,
            HistoryTokenOffsetAndCount.EMPTY.setOffset(
                OptionalInt.of(offset)
            )
        );
        this.checkEquals(
            OptionalInt.of(offset),
            historyToken.offset()
        );
    }

    // setOffset........................................................................................................

    @Test
    public void testSetOffsetWithSame() {
        final SpreadsheetLabelMappingReferencesHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setOffset(historyToken.offset())
        );
    }

    @Test
    public void testSetOffsetWithDifferent() {
        final OptionalInt offset = OptionalInt.of(123);

        final SpreadsheetLabelMappingReferencesHistoryToken historyToken = this.createHistoryToken();

        final HistoryToken different = historyToken.setOffset(offset);

        assertNotSame(
            historyToken,
            different
        );

        this.checkEquals(
            offset,
            different.offset()
        );
    }

    // count............................................................................................................

    @Test
    public void testCount() {
        this.countAndCheck(
            this.createHistoryToken()
        );
    }

    @Test
    public void testCount2() {
        final int count = 123;

        this.countAndCheck(
            SpreadsheetLabelMappingReferencesHistoryToken.with(
                ID,
                NAME,
                LABEL,
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(count)
                )
            ),
            count
        );
    }

    // setCount.........................................................................................................

    @Test
    public void testSetCountWithSame() {
        final SpreadsheetLabelMappingReferencesHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setCount(historyToken.count())
        );
    }

    @Test
    public void testSetCountWithDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
            this.createHistoryToken(),
            count,
            SpreadsheetLabelMappingReferencesHistoryToken.with(
                ID,
                NAME,
                LABEL,
                HistoryTokenOffsetAndCount.EMPTY.setCount(count)
            )
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseWithoutOffsetAndCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/label/Label123/references",
            SpreadsheetLabelMappingReferencesHistoryToken.with(
                ID,
                NAME,
                LABEL,
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseWithOffset() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/label/Label123/references/offset/123",
            SpreadsheetLabelMappingReferencesHistoryToken.with(
                ID,
                NAME,
                LABEL,
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(123)
                )
            )
        );
    }

    @Test
    public void testParseWithCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/label/Label123/references/count/123",
            SpreadsheetLabelMappingReferencesHistoryToken.with(
                ID,
                NAME,
                LABEL,
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(123)
                )
            )
        );
    }

    @Test
    public void testParseWithOffsetAndCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/label/Label123/references/offset/123/count/456",
            SpreadsheetLabelMappingReferencesHistoryToken.with(
                ID,
                NAME,
                LABEL,
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(123)
                ).setCount(
                    OptionalInt.of(456)
                )
            )
        );
    }

    // labelMappingReference............................................................................................

    @Override
    public void testLabelMappingReference() {
        this.labelMappingReferenceAndCheck(
            this.createHistoryToken() // only holds label not mapping
        );
    }

    // setLabelMappingReference.........................................................................................

    @Test
    public void testSetLabelMappingReferenceWithCell() {
        this.setLabelMappingReferenceAndCheck();
    }

    // labelName........................................................................................................

    @Test
    public void testLabelName() {
        this.labelNameAndCheck(
            this.createHistoryToken(),
            LABEL
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/label/Label123/references"
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.labelMappingSelect(
                ID,
                NAME,
                Optional.of(LABEL)
            )
        );
    }

    @Override
    SpreadsheetLabelMappingReferencesHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final SpreadsheetLabelName label) {
        return SpreadsheetLabelMappingReferencesHistoryToken.with(
            id,
            name,
            label,
            HistoryTokenOffsetAndCount.EMPTY
        );
    }
    
    // class............................................................................................................

    @Override
    public Class<SpreadsheetLabelMappingReferencesHistoryToken> type() {
        return SpreadsheetLabelMappingReferencesHistoryToken.class;
    }
}
