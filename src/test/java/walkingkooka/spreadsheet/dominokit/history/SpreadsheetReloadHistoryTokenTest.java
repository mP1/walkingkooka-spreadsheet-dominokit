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

import org.junit.Test;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class SpreadsheetReloadHistoryTokenTest extends SpreadsheetNameHistoryTokenTestCase<SpreadsheetReloadHistoryToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/reload");
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                )
        );
    }

    @Test
    public void testSetMetadataPropertyName() {
        final SpreadsheetMetadataPropertyName<?> propertyName = SpreadsheetMetadataPropertyName.LOCALE;

        this.setMetadataPropertyNameAndCheck(
                propertyName,
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        propertyName
                )
        );
    }

    @Test
    public void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
        );
    }

    @Override
    SpreadsheetReloadHistoryToken createHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name) {
        return SpreadsheetReloadHistoryToken.with(
                id,
                name
        );
    }

    @Override
    public Class<SpreadsheetReloadHistoryToken> type() {
        return SpreadsheetReloadHistoryToken.class;
    }
}
