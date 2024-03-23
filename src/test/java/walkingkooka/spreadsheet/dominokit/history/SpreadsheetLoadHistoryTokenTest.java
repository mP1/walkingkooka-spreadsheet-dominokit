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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetLoadHistoryTokenTest extends SpreadsheetIdHistoryTokenTestCase<SpreadsheetLoadHistoryToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/123");
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    @Test
    public void testSetAnchoredSelection() {
        final AnchoredSpreadsheetSelection anchored = SpreadsheetSelection.A1.setDefaultAnchor();
        final SpreadsheetLoadHistoryToken token = this.createHistoryToken();

        this.checkEquals(
                token,
                token.setAnchoredSelection(
                        Optional.of(anchored)
                )
        );
    }

    @Test
    public void testSetIdNameDifferentId() {
        final SpreadsheetId differentId = SpreadsheetId.with(9999);

        this.setIdAndNameAndCheck(
                differentId,
                NAME,
                HistoryToken.spreadsheetSelect(differentId, NAME)
        );
    }

    @Test
    public void testSetMetadataPropertyName() {
        this.setMetadataPropertyNameAndCheck(
                SpreadsheetMetadataPropertyName.LOCALE
        );
    }

    @Test
    public void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
        );
    }

    @Override
    SpreadsheetLoadHistoryToken createHistoryToken() {
        return SpreadsheetLoadHistoryToken.with(
                ID
        );
    }

    @Override
    public Class<SpreadsheetLoadHistoryToken> type() {
        return SpreadsheetLoadHistoryToken.class;
    }
}
