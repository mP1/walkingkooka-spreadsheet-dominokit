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

package walkingkooka.spreadsheet.dominokit;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetViewportWidgetTest implements ClassTesting<SpreadsheetViewportWidget> {

    // id...............................................................................................................

    @Test
    public void testIdWithCell() {
        this.idAndCheck(
                SpreadsheetSelection.A1,
                "viewport-cell-A1"
        );
    }

    @Test
    public void testIdWithColumn() {
        this.idAndCheck(
                SpreadsheetSelection.parseColumn("B"),
                "viewport-column-B"
        );
    }

    @Test
    public void testIdWithRow() {
        this.idAndCheck(
                SpreadsheetSelection.parseRow("3"),
                "viewport-row-3"
        );
    }

    private void idAndCheck(final SpreadsheetSelection selection,
                            final String id) {
        this.checkEquals(
                id,
                SpreadsheetViewportWidget.id(selection),
                () -> selection + " id"
        );
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetViewportWidget> type() {
        return SpreadsheetViewportWidget.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
