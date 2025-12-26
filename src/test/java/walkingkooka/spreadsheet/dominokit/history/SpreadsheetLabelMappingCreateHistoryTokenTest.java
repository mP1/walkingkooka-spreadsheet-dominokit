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
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Optional;

public final class SpreadsheetLabelMappingCreateHistoryTokenTest extends SpreadsheetLabelMappingHistoryTokenTestCase<SpreadsheetLabelMappingCreateHistoryToken> {

    @Test
    public void testWith() {
        final SpreadsheetLabelMappingCreateHistoryToken token = SpreadsheetLabelMappingCreateHistoryToken.with(
            ID,
            NAME
        );
        this.checkEquals(ID, token.id(), "id");
        this.checkEquals(NAME, token.name(), "name");
    }

    @Test
    public void testLabelMappingReference() {
        this.labelMappingReferenceAndCheck(this.createHistoryToken());
    }

    // label............................................................................................................

    @Test
    public void testLabelName() {
        this.labelNameAndCheck(
            SpreadsheetLabelMappingCreateHistoryToken.with(
                ID,
                NAME
            )
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(this)
        );
    }

    // hasUrlFragment...................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            this.createHistoryToken(),
            "/123/SpreadsheetName456/create-label"
        );
    }

    // clearAction.....................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    @Override
    SpreadsheetLabelMappingCreateHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                 final SpreadsheetName name) {
        return SpreadsheetLabelMappingCreateHistoryToken.with(
            id,
            name
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLabelMappingCreateHistoryToken> type() {
        return SpreadsheetLabelMappingCreateHistoryToken.class;
    }
}
