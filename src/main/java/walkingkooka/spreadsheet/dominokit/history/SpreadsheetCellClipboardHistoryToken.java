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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Objects;

/**
 * Base class for clipboard operations for a cell/cell-range. This represents a clipboard action the value is not
 * held in the token.
 */
public abstract class SpreadsheetCellClipboardHistoryToken extends SpreadsheetCellHistoryToken {
    SpreadsheetCellClipboardHistoryToken(final SpreadsheetId id,
                                         final SpreadsheetName name,
                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                         final SpreadsheetCellClipboardKind kind) {
        super(
            id,
            name,
            anchoredSelection
        );
        this.kind = Objects.requireNonNull(kind, "kind");
    }

    public final SpreadsheetCellClipboardKind kind() {
        return this.kind;
    }

    private final SpreadsheetCellClipboardKind kind;

    @Override
    public final HistoryToken clearAction() {
        return cellSelect(
            this.id,
            this.name(),
            this.anchoredSelection()
        );
    }

    // HasUrlFragment...................................................................................................

    // /cell/a1:A2/cut/cell
    @Override //
    final UrlFragment cellUrlFragment() {
        return this.clipboardUrlFragment()
            .appendSlashThen(
                this.kind.urlFragment()
            );
    }

    // cut | copy | paste SLASH kind SLASH serialized-value
    abstract UrlFragment clipboardUrlFragment();

    // HistoryToken.....................................................................................................

    @Override //
    final void onHistoryTokenChange0(final HistoryToken previous,
                                     final AppContext context) {
        context.pushHistoryToken(
            previous
        );
        this.onHistoryTokenChangeClipboard(context);
    }

    abstract void onHistoryTokenChangeClipboard(final AppContext context);
}
