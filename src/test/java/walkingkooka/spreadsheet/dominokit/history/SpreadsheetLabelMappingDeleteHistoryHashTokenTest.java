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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

public final class SpreadsheetLabelMappingDeleteHistoryHashTokenTest extends SpreadsheetLabelMappingHistoryHashTokenTestCase<SpreadsheetLabelMappingDeleteHistoryHashToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                LABEL,
                "/label/Label123/delete"
        );
    }

    @Override
    SpreadsheetLabelMappingDeleteHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetLabelName label) {
        return SpreadsheetLabelMappingDeleteHistoryHashToken.with(label);
    }

    @Override
    public Class<SpreadsheetLabelMappingDeleteHistoryHashToken> type() {
        return SpreadsheetLabelMappingDeleteHistoryHashToken.class;
    }
}
