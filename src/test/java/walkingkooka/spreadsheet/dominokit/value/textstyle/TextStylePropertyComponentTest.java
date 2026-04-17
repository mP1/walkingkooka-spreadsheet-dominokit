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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.color.FakeTextStyleColorComponentContext;
import walkingkooka.spreadsheet.dominokit.value.textstyle.color.TextStyleColorComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textalign.FakeTextAlignComponentContext;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textalign.TextAlignComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

public final class TextStylePropertyComponentTest implements ClassTesting<TextStylePropertyComponent<?, ?, ?>> {

    // setLabelFromPropertyName.........................................................................................

    private final static String ID_PREFIX = "TestIdPrefix123-";

    @Test
    public void testSetLabelFromPropertyNameWithColor() {
        this.setLabelFromPropertyName(
            TextStyleColorComponent.with(
                ID_PREFIX,
                new FakeTextStyleColorComponentContext() {
                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return () -> {};
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString("/1/SpreadsheetName");
                    }

                    @Override
                    public SpreadsheetMetadata spreadsheetMetadata() {
                        return SpreadsheetMetadata.EMPTY;
                    }
                }
            ),
            "Color"
        );
    }

    @Test
    public void testSetLabelFromPropertyNameWithTextAlign() {
        this.setLabelFromPropertyName(
            TextAlignComponent.with(
                ID_PREFIX,
                new FakeTextAlignComponentContext() {
                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return () -> {};
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString("/1/SpreadsheetName");
                    }
                }
            ),
            "Text Align"
        );
    }

    private void setLabelFromPropertyName(final TextStylePropertyComponent<?, ?, ?> component,
                                          final String expected) {
        component.setLabelFromPropertyName();

        this.checkEquals(
            expected,
            component.label(),
            component::toString
        );
    }
    
    // class............................................................................................................

    @Override
    public Class<TextStylePropertyComponent<?, ?, ?>> type() {
        return Cast.to(TextStylePropertyComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
