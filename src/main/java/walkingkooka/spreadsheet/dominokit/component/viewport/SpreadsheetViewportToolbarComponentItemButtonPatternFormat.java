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

package walkingkooka.spreadsheet.dominokit.component.viewport;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Objects;
import java.util.Optional;

/**
 * A button component that may exist withing a toolbar, which actives the format pattern editor.
 */
final class SpreadsheetViewportToolbarComponentItemButtonPatternFormat extends SpreadsheetViewportToolbarComponentItemButtonPattern<SpreadsheetFormatPattern> {

    static SpreadsheetViewportToolbarComponentItemButtonPatternFormat with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportToolbarComponentItemButtonPatternFormat(
                context
        );
    }

    private SpreadsheetViewportToolbarComponentItemButtonPatternFormat(final HistoryTokenContext context) {
        super(
                SpreadsheetViewportToolbarComponent.formatPattern(),
                SpreadsheetIcons.parsePattern(),
                "Format pattern(s)",
                context
        );
    }

    @Override //
    Optional<SpreadsheetFormatPattern> pattern(final SpreadsheetCell cell) {
        return cell.formatPattern();
    }

    @Override//
    SpreadsheetPatternKind defaultSpreadsheetPatternKind() {
        return SpreadsheetPatternKind.TEXT_FORMAT_PATTERN;
    }
}
