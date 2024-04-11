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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.OptionalInt;

public abstract class SpreadsheetListHistoryTokenTestCase<T extends SpreadsheetListHistoryToken> extends SpreadsheetHistoryTokenTestCase<T> {

    final static OptionalInt FROM = OptionalInt.of(1);

    final static OptionalInt COUNT = OptionalInt.of(23);

    SpreadsheetListHistoryTokenTestCase(){
        super();
    }

    @Test
    public final void testSetIdName() {
        this.setIdAndNameAndCheck(
                ID,
                NAME,
                HistoryToken.spreadsheetSelect(ID, NAME)
        );
    }

    @Test
    public final void testSetIdNameDifferentId() {
        final SpreadsheetId differentId = SpreadsheetId.with(9999);

        this.setIdAndNameAndCheck(
                differentId,
                NAME,
                HistoryToken.spreadsheetSelect(differentId, NAME)
        );
    }

    @Test
    public final void testSetIdNameDifferentName() {
        final SpreadsheetName differentName = SpreadsheetName.with("Different");

        this.setIdAndNameAndCheck(
                ID,
                differentName,
                HistoryToken.spreadsheetSelect(ID, differentName)
        );
    }

    @Test
    public final void testSetMetadataPropertyName() {
        this.setMetadataPropertyNameAndCheck(
                SpreadsheetMetadataPropertyName.LOCALE
        );
    }

    @Test
    public final void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
        );
    }

    final void reloadAndCheck(final SpreadsheetListHistoryToken historyToken,
                              final SpreadsheetListHistoryToken expected) {
        this.checkEquals(
                expected,
                historyToken.reload(),
                historyToken + " reload"
        );
    }

    @Override final T createHistoryToken() {
        return this.createHistoryToken(
                FROM,
                COUNT
        );
    }

    abstract T createHistoryToken(final OptionalInt from,
                                  final OptionalInt count);
}
