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
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public abstract class SpreadsheetCellStyleHistoryTokenTestCase<T extends SpreadsheetCellStyleHistoryToken<Color>> extends SpreadsheetCellHistoryTokenTestCase<T> {

    final static TextStylePropertyName<Color> PROPERTY_NAME = TextStylePropertyName.COLOR;

    final static Color PROPERTY_VALUE = Color.parse("#123456");

    SpreadsheetCellStyleHistoryTokenTestCase() {
        super();
    }

    // patternKind......................................................................................................

    @Test
    public final void testPatternKind() {
        this.patternKindAndCheck(
            this.createHistoryToken()
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public final void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueFails(
            Optional.of(this)
        );
    }

    @Test
    public final void testSetSaveValueWithEmpty() {
        final Optional<Color> value = Optional.empty();

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            value,
            HistoryToken.cellStyleSave(
                ID,
                NAME,
                SELECTION,
                PROPERTY_NAME,
                value
            )
        );
    }

    @Test
    public final void testSetSaveValueWithNonEmpty() {
        final Optional<Color> value = Optional.of(
            Color.parse("#999")
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            value,
            HistoryToken.cellStyleSave(
                ID,
                NAME,
                SELECTION,
                PROPERTY_NAME,
                value
            )
        );
    }

    // helpers.........................................................................................................

    @Override
    T createHistoryToken(final SpreadsheetId id,
                         final SpreadsheetName name,
                         final AnchoredSpreadsheetSelection selection) {
        return this.createHistoryToken(
            id,
            name,
            selection,
            PROPERTY_NAME
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final AnchoredSpreadsheetSelection selection,
                                  final TextStylePropertyName<Color> propertyName);
}
