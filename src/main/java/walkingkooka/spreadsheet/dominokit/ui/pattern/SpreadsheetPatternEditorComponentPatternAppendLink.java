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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import walkingkooka.spreadsheet.dominokit.ui.Anchor;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenKind;
import walkingkooka.text.CharSequences;

final class SpreadsheetPatternEditorComponentPatternAppendLink {

    static SpreadsheetPatternEditorComponentPatternAppendLink with(final SpreadsheetFormatParserTokenKind kind,
                                                                   final String pattern,
                                                                   final Anchor anchor) {
        return new SpreadsheetPatternEditorComponentPatternAppendLink(
                kind,
                pattern,
                anchor
        );
    }

    private SpreadsheetPatternEditorComponentPatternAppendLink(final SpreadsheetFormatParserTokenKind kind,
                                                               final String pattern,
                                                               final Anchor anchor) {
        this.kind = kind;
        this.pattern = pattern;
        this.anchor = anchor;
    }

    final SpreadsheetFormatParserTokenKind kind;

    final String pattern;

    final Anchor anchor;

    @Override
    public String toString() {
        return this.kind + " " + CharSequences.quoteAndEscape(pattern) + " " + this.anchor;
    }
}
