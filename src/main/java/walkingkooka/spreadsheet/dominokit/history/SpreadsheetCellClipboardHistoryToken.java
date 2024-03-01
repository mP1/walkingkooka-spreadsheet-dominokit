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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for clipboard operations for a cell/cell-range. This represents a clipboard action the value is not
 * held in the token.
 */
public abstract class SpreadsheetCellClipboardHistoryToken extends SpreadsheetCellHistoryToken {
    SpreadsheetCellClipboardHistoryToken(final SpreadsheetId id,
                                         final SpreadsheetName name,
                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                         final SpreadsheetCellClipboardValueSelector clipboardValueSelector) {
        super(
                id,
                name,
                anchoredSelection
        );
        this.clipboardValueSelector = Objects.requireNonNull(clipboardValueSelector, "clipboardValueSelector");
    }

    public final SpreadsheetCellClipboardValueSelector clipboardValueSelector() {
        return this.clipboardValueSelector;
    }

    private final SpreadsheetCellClipboardValueSelector clipboardValueSelector;

    @Override
    public final HistoryToken clearAction() {
        return cell(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override
    public final HistoryToken setFormula() {
        return formula(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override final HistoryToken setFormatPattern() {
        return cellFormatPattern(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setParsePattern() {
        return cellParsePattern(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override //
    final HistoryToken setSave0(final String value) {
        return this;
    }

    // HasUrlFragment...................................................................................................

    // /cell/a1:A2/cut/cell
    @Override //
    final UrlFragment cellUrlFragment() {
        return this.clipboardUrlFragment()
                .append(UrlFragment.SLASH)
                .append(this.clipboardValueSelector.urlFragment());
    }

    // cut | copy | paste SLASH clipboardValueSelector SLASH serialized-value
    abstract UrlFragment clipboardUrlFragment();
}
