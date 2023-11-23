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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class SpreadsheetViewportToolbarComponentTest implements ClassTesting<SpreadsheetViewportToolbarComponent> {

    // id...............................................................................................................

    @Test
    public void testIdWithoutValue() {
        this.idAndCheck(
                TextStylePropertyName.COLOR,
                Optional.empty(),
                "viewport-toolbar-color"
        );
    }

    @Test
    public void testIdWithValue() {
        this.idAndCheck(
                TextStylePropertyName.COLOR,
                Optional.of(Color.BLACK),
                "viewport-toolbar-color-#000000"
        );
    }

    @Test
    public void testIdWithValue2() {
        this.idAndCheck(
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.CENTER),
                "viewport-toolbar-text_align-CENTER"
        );
    }

    private <T> void idAndCheck(final TextStylePropertyName<T> property,
                                final Optional<T> value,
                                final String expected) {
        this.checkEquals(
                expected,
                SpreadsheetViewportToolbarComponent.id(
                        property,
                        value
                )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetViewportToolbarComponent> type() {
        return SpreadsheetViewportToolbarComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
