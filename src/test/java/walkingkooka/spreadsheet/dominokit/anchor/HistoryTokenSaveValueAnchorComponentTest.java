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

package walkingkooka.spreadsheet.dominokit.anchor;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.ValueType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryTokenSaveValueAnchorComponentTest implements AnchorComponentTesting<HistoryTokenSaveValueAnchorComponent<Color>, Color> {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);
    private final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName1");

    @Test
    public void testWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryTokenSaveValueAnchorComponent.with(
                null,
                new FakeHistoryContext()
            )
        );
    }

    @Test
    public void testWithNullHistoryTokenContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryTokenSaveValueAnchorComponent.with(
                "hello-id",
                null
            )
        );
    }

    @Test
    public void testSetValueWithColor() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        Color.parse("#123456")
                    )
                ),
            "\"Save\" [#/1/SpreadsheetName1/cell/A1/style/color/save/%23123456] id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    @Test
    public void testSetValueWithSpreadsheetFormatterSelectorWithDollarSignAndHashes() {
        final Optional<SpreadsheetFormatterSelector> value = Optional.of(
            SpreadsheetFormatterSelector.parse("number $###.00")
        );

        this.treePrintAndCheck(
            HistoryTokenSaveValueAnchorComponent.<SpreadsheetFormatterSelector>with(
                "id123",
                this.createContext(
                    HistoryToken.cellFormatterSave(
                        SPREADSHEET_ID,
                        SPREADSHEET_NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        value
                    )
                )
            ).setValue(value),
            "\"Save\" [#/1/SpreadsheetName1/cell/A1/formatter/save/number%20$%23%23%23.00] id=id123"
        );
    }

    @Test
    public void testSetValueWithEmptyValue() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.cellValueSave(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.setDefaultAnchor(),
                    ValueType.TEXT,
                    Optional.empty()
                )
            ),
            "\"Save\" DISABLED id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    @Test
    public void testSetValueWithEmptyStringWhenSpreadsheetCellLabelSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.cellLabelSelect(
                    SpreadsheetId.with(1),
                    SpreadsheetName.with("SpreadsheetName1"),
                    SpreadsheetSelection.A1.setDefaultAnchor()
                )
            ).setValue(Optional.empty()),
            "\"Save\" DISABLED id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "\"Save\" [#/1/SpreadsheetName1/cell/A1/style/color/save/] id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    @Test
    public void testClearValueDisabled() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue()
                .disabled(),
            "\"Save\" DISABLED id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    @Test
    public void testSetValueSetTextContent() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        Color.parse("#123456")
                    )
                ).setTextContent("Clear"),
            "\"Clear\" [#/1/SpreadsheetName1/cell/A1/style/color/save/%23123456] id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    @Test
    public void testSetTextContentSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setTextContent("Hello")
                .setValue(
                    Optional.of(
                        Color.parse("#123456")
                    )
                ),
            "\"Hello\" [#/1/SpreadsheetName1/cell/A1/style/color/save/%23123456] id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    // autoDisableWhenMissingValue......................................................................................

    @Test
    public void testAutoDisableWhenMissingValueSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .autoDisableWhenMissingValue()
                .setValue(
                    Optional.of(
                        Color.parse("#123456")
                    )
                ),
            "\"Save\" [#/1/SpreadsheetName1/cell/A1/style/color/save/%23123456] id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    @Test
    public void testAutoDisableWhenMissingValueClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .autoDisableWhenMissingValue()
                .clearValue(),
            "\"Save\" DISABLED id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    @Test
    public void testAutoDisableWhenMissingValueClearValueSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .autoDisableWhenMissingValue()
                .clearValue()
                .setValue(
                    Optional.of(
                        Color.parse("#123456")
                    )
                ),
            "\"Save\" [#/1/SpreadsheetName1/cell/A1/style/color/save/%23123456] id=HistoryTokenSaveValueAnchorComponent-Link"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public HistoryTokenSaveValueAnchorComponent<Color> createComponent() {
        return this.createComponent(
            HistoryToken.cellStyleSave(
                SpreadsheetId.with(1),
                SpreadsheetName.with("SpreadsheetName1"),
                SpreadsheetSelection.A1.setDefaultAnchor(),
                TextStylePropertyName.COLOR,
                Optional.of(Color.BLACK)
            )
        );
    }

    private <T> HistoryTokenSaveValueAnchorComponent<T> createComponent(final HistoryToken historyToken) {
        return HistoryTokenSaveValueAnchorComponent.with(
            "HistoryTokenSaveValueAnchorComponent" + SpreadsheetElementIds.LINK,
            this.createContext(historyToken)
        );
    }

    private HistoryContext createContext(final HistoryToken historyToken) {
        return new FakeHistoryContext() {
            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<HistoryTokenSaveValueAnchorComponent<Color>> type() {
        return Cast.to(HistoryTokenSaveValueAnchorComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
