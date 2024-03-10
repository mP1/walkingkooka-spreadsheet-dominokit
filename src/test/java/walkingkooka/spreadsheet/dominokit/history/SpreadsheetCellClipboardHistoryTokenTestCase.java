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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetCellClipboardHistoryTokenTestCase<T extends SpreadsheetCellClipboardHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T> {

    final static SpreadsheetCellClipboardValueKind KIND = SpreadsheetCellClipboardValueKind.CELL;

    SpreadsheetCellClipboardHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testWithNullKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        null
                )
        );
    }

    @Override //
    final T createHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name,
                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.createHistoryToken(
                id,
                name,
                anchoredSelection,
                KIND
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                  final SpreadsheetCellClipboardValueKind kind);
}
