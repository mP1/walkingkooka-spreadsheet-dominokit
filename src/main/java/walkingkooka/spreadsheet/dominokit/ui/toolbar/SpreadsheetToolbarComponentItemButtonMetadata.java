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

package walkingkooka.spreadsheet.dominokit.ui.toolbar;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.icons.MdiIcon;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.ui.NopRefreshComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.VisibleComponentLifecycle;

/**
 * Base class for buttons that update a {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata} property.
 */
abstract class SpreadsheetToolbarComponentItemButtonMetadata extends SpreadsheetToolbarComponentItemButton
        implements SpreadsheetCellComponentLifecycle,
        NopComponentLifecycleOpenGiveFocus,
        NopRefreshComponentLifecycle,
        VisibleComponentLifecycle<HTMLElement> {

    SpreadsheetToolbarComponentItemButtonMetadata(final String id,
                                                  final MdiIcon icon,
                                                  final String tooltipText,
                                                  final AppContext context) {
        super(
                id,
                icon,
                tooltipText,
                context
        );
        this.context = context;
    }

    @Override //
    final void onToolbarRefreshBegin() {

    }

    @Override //
    final void onToolbarRefreshSelectedCell(final SpreadsheetCell cell,
                                            final AppContext context) {

    }

    @Override //
    final void onToolbarRefreshEnd(final int cellPresentCount,
                                   final AppContext context) {
        this.refreshButton(context);
    }

    abstract void refreshButton(final AppContext context);

    /**
     * {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata} properties are always shown.
     */
    @Override
    public final boolean isMatch(final HistoryToken token) {
        return true; // always show
    }

    /**
     * Must be {@link AppContext} so sub-classes can get current {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata}.
     */
    final AppContext context;
}
