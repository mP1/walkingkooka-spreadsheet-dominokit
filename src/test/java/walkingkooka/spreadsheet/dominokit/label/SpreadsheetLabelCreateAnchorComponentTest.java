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

package walkingkooka.spreadsheet.dominokit.label;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetLabelCreateAnchorComponentTest implements AnchorComponentTesting<SpreadsheetLabelCreateAnchorComponent, SpreadsheetExpressionReference> {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1L);

    private final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName222");

    // with.............................................................................................................

    @Test
    public void testWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetLabelCreateAnchorComponent.with(
                null, // id,
                HistoryContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetLabelCreateAnchorComponent.with(
                "id",
                null
            )
        );
    }

    // clearValue.......................................................................................................

    @Test
    public void testClearValueAndSpreadsheetListHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
            ).clearValue(),
            "\"Create\" DISABLED id=cell-create-label-id"
        );
    }

    @Test
    public void testClearValueAndSpreadsheetNameHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            ).clearValue(),
            "\"Create\" DISABLED id=cell-create-label-id"
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValueWithCellAndSpreadsheetCellSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.setDefaultAnchor()
                )
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.A1
                )
            ),
            "\"A1\" [#/1/SpreadsheetName222/cell/A1/label] id=cell-create-label-id"
        );
    }

    @Test
    public void testSetValueWithCellRange() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseCellRange("A1:B2")
                    )
                ),
            "\"A1:B2\" [#/1/SpreadsheetName222/cell/A1:B2/bottom-right/label] id=cell-create-label-id"
        );
    }

    @Test
    public void testSetValueWithLabel() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.labelName("Label999")
                    )
                ),
            "\"Label999\" [#/1/SpreadsheetName222/cell/Label999/label] id=cell-create-label-id"
        );
    }

    @Test
    public void testSetValueWithNotSpreadsheetNameHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.spreadsheetListSelect(
                    HistoryTokenOffsetAndCount.EMPTY
                )
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.labelName("Label999")
                )
            ),
            "\"Label999\" DISABLED id=cell-create-label-id"
        );
    }

    // createComponent..................................................................................................

    @Override
    public SpreadsheetLabelCreateAnchorComponent createComponent() {
        return this.createComponent("/1/SpreadsheetName222");
    }

    private SpreadsheetLabelCreateAnchorComponent createComponent(final String historyToken) {
        return this.createComponent(
            HistoryToken.parseString(historyToken)
        );
    }

    private SpreadsheetLabelCreateAnchorComponent createComponent(final HistoryToken historyToken) {
        return SpreadsheetLabelCreateAnchorComponent.with(
            "cell-create-label-id",
            new FakeHistoryContext() {

                @Override
                public HistoryToken historyToken() {
                    return historyToken;
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLabelCreateAnchorComponent> type() {
        return SpreadsheetLabelCreateAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
