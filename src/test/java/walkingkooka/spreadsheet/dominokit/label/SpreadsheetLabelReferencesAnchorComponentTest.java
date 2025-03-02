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
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetLabelReferencesAnchorComponentTest implements AnchorComponentTesting<SpreadsheetLabelReferencesAnchorComponent, SpreadsheetLabelName> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetLabelReferencesAnchorComponent.with(
                "id123",
                null
            )
        );
    }

    // value............................................................................................................

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            SpreadsheetLabelReferencesAnchorComponent.with(
                "label-references-anchor-id",
                SpreadsheetLabelReferencesAnchorComponentContexts.fake()
            ).clearValue(),
            "\"References\" DISABLED id=label-references-anchor-id"
        );
    }

    @Test
    public void testSetValueWithLabel() {
        this.treePrintAndCheck(
            this.createComponent(
                "/"
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.labelName("Label123")
                )
            ),
            "\"Label123\" DISABLED id=label-references-anchor-id"
        );
    }

    @Override
    public SpreadsheetLabelReferencesAnchorComponent createComponent() {
        return this.createComponent("/1/SpreadsheetName22");
    }

    private SpreadsheetLabelReferencesAnchorComponent createComponent(final String currentHistoryToken) {
        return SpreadsheetLabelReferencesAnchorComponent.with(
            "label-references-anchor-id",
            new FakeSpreadsheetLabelReferencesAnchorComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString(currentHistoryToken);
                }

                @Override
                public String toString() {
                    return currentHistoryToken;
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLabelReferencesAnchorComponent> type() {
        return SpreadsheetLabelReferencesAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
