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

package walkingkooka.spreadsheet.dominokit.form;

import elemental2.dom.HTMLAnchorElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.validation.SpreadsheetValidationReference;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetFormSelectHistoryTokenAnchorComponentTest implements AnchorComponentTesting<SpreadsheetFormSelectHistoryTokenAnchorComponent>,
    ValueComponentTesting<HTMLAnchorElement, SpreadsheetValidationReference, SpreadsheetFormSelectHistoryTokenAnchorComponent> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormSelectHistoryTokenAnchorComponent.with(
                HistoryTokenAnchorComponent.empty(),
                "id123",
                null
            )
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValueWithEmpty() {
        final SpreadsheetFormSelectHistoryTokenAnchorComponent component = this.createComponent();

        this.clearValueAndCheck(component);

        this.treePrintAndCheck(
            component,
            "DISABLED id=form-field-anchor-id"
        );
    }

    @Test
    public void testSetValueWithCell() {
        final SpreadsheetFormSelectHistoryTokenAnchorComponent component = this.createComponent();

        this.setValueAndCheck(
            component,
            SpreadsheetSelection.A1
        );

        this.treePrintAndCheck(
            component,
            "[#/1/SpreadsheetName222/form/Form123/field/A1] id=form-field-anchor-id"
        );
    }

    @Test
    public void testSetValueWithLabel() {
        final SpreadsheetFormSelectHistoryTokenAnchorComponent component = this.createComponent();

        this.setValueAndCheck(
            component,
            SpreadsheetSelection.labelName("Label123")
        );

        this.treePrintAndCheck(
            component,
            "[#/1/SpreadsheetName222/form/Form123/field/Label123] id=form-field-anchor-id"
        );
    }

    @Override
    public SpreadsheetFormSelectHistoryTokenAnchorComponent createComponent() {
        return this.createComponent("/1/SpreadsheetName222/form/Form123");
    }

    private SpreadsheetFormSelectHistoryTokenAnchorComponent createComponent(final String historyToken) {
        return SpreadsheetFormSelectHistoryTokenAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            "form-field-anchor-id",
            new TestHistoryContext(historyToken)
        );
    }

    final static class TestHistoryContext extends FakeHistoryContext {

        private TestHistoryContext(final String historyToken) {
            this.historyToken = HistoryToken.parseString(historyToken);
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        private final HistoryToken historyToken;

        @Override
        public String toString() {
            return this.historyToken.toString();
        }
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormSelectHistoryTokenAnchorComponent> type() {
        return SpreadsheetFormSelectHistoryTokenAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
