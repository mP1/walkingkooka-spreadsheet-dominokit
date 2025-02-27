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

public final class SpreadsheetLabelMappingReferencesHistoryTokenTest extends SpreadsheetLabelMappingHistoryTokenTestCase<SpreadsheetLabelMappingReferencesHistoryToken> {

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
