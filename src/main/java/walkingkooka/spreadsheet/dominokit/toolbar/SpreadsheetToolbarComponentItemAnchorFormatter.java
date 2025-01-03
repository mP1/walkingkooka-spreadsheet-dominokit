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

package walkingkooka.spreadsheet.dominokit.toolbar;

import elemental2.dom.Event;
import walkingkooka.spreadsheet.dominokit.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.NopComponentLifecycleRefresh;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetCellComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;

import java.util.Objects;
import java.util.Optional;

/**
 * A link ui that may exist withing a toolbar, which actives the formatter dialog
 */
final class SpreadsheetToolbarComponentItemAnchorFormatter extends SpreadsheetToolbarComponentItemAnchor<SpreadsheetToolbarComponentItemAnchorFormatter>
        implements SpreadsheetCellComponentLifecycle,
        NopComponentLifecycleRefresh,
        NopComponentLifecycleOpenGiveFocus {

    static SpreadsheetToolbarComponentItemAnchorFormatter with(final SpreadsheetToolbarComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponentItemAnchorFormatter(
                context
        );
    }

    private SpreadsheetToolbarComponentItemAnchorFormatter(final SpreadsheetToolbarComponentContext context) {
        super(
                SpreadsheetToolbarComponent.formatterId(),
                Optional.of(
                        SpreadsheetIcons.formatter()
                ),
                "Formatting",
                "Formatting...",
                context
        );
    }

    @Override //
    void onFocus(final Event event) {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .anchoredSelectionHistoryTokenOrEmpty()
                .map(
                        t -> t.formatter()
                                .toolbar()
                ).ifPresent(context::pushHistoryToken);
    }


    @Override
    public void refresh(final RefreshContext context) {
        this.anchor.setHistoryToken(
                context.historyToken()
                        .anchoredSelectionHistoryTokenOrEmpty()
                        .map(
                                t -> t.formatter()
                        )
        );
    }
}
