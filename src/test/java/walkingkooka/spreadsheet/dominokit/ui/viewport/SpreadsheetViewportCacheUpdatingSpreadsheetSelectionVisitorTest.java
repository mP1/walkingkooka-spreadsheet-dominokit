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

import walkingkooka.collect.map.Maps;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.reference.SpreadsheetSelectionVisitorTesting;

import java.util.Collections;

public final class SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitorTest implements SpreadsheetSelectionVisitorTesting<SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor> {
    @Override
    public SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor createVisitor() {
        return SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor.accept(
                Collections.emptyList(),
                Maps.empty(),
                Collections.emptyMap(),
                SpreadsheetViewportWindows.parse("A1:Z99")
        );
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetViewportCache.class.getSimpleName();
    }

    @Override
    public Class<SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor> type() {
        return SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor.class;
    }
}
