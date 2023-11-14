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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.Event;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.lib.Icons;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.text.TextStyle;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * A button component that may exist withing a toolbar, which actives the parse pattern editor.
 */
final class SpreadsheetViewportToolbarComponentItemButtonPatternParse extends SpreadsheetViewportToolbarComponentItemButtonPattern<SpreadsheetParsePattern> {

    static SpreadsheetViewportToolbarComponentItemButtonPatternParse with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportToolbarComponentItemButtonPatternParse(
                context
        );
    }

    private SpreadsheetViewportToolbarComponentItemButtonPatternParse(final HistoryTokenContext context) {
        super(
                SpreadsheetViewportToolbarComponent.parsePattern(),
                "Parse pattern(s)",
                context
        );
    }

    @Override //
    Optional<SpreadsheetParsePattern> pattern(final SpreadsheetCell cell) {
        return cell.parsePattern();
    }

    @Override//
    SpreadsheetPatternKind defaultSpreadsheetPatternKind() {
        return SpreadsheetPatternKind.NUMBER_PARSE_PATTERN;
    }
}
