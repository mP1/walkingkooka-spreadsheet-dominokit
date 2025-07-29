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

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetLabelComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetLabelName, SpreadsheetLabelComponent> {

    private final static SpreadsheetLabelComponentContext CONTEXT = new FakeSpreadsheetLabelComponentContext() {
        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return null;
        }
    };

    @Test
    public void testSetValueMissingValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Text123")
                .setValue(
                    Optional.empty()
                ),
            "SpreadsheetLabelComponent\n" +
                "  SpreadsheetSuggestBoxComponent\n" +
                "    Text123 [] REQUIRED\n" +
                "    Errors\n" +
                "      Required\n"
        );
    }

    @Test
    public void testSetValueWithLabel() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Text123")
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.labelName("Label123")
                    )
                ),
            "SpreadsheetLabelComponent\n" +
                "  SpreadsheetSuggestBoxComponent\n" +
                "    Text123 [Label123] REQUIRED\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetLabelComponent createComponent() {
        return SpreadsheetLabelComponent.with(
            (l) -> {
                throw new UnsupportedOperationException();
            },
            CONTEXT
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetLabelComponent> type() {
        return SpreadsheetLabelComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
