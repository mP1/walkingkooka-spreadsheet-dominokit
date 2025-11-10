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
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.NopComponentLifecycleRefresh;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;

import java.util.Optional;

final class ToolbarComponentItemAnchorSwagger extends ToolbarComponentItemAnchor<ToolbarComponentItemAnchorSwagger>
    implements NopComponentLifecycleOpenGiveFocus,
    NopComponentLifecycleRefresh {

    static ToolbarComponentItemAnchorSwagger with(final ToolbarComponentContext context) {
        return new ToolbarComponentItemAnchorSwagger(context);
    }

    private ToolbarComponentItemAnchorSwagger(final ToolbarComponentContext context) {
        super(
            ToolbarComponent.SWAGGER_ID,
            Optional.of(
                SpreadsheetIcons.swagger()
            ),
            "Swagger",
            "Click to open swagger html client",
            context
        );

        final HistoryTokenAnchorComponent anchor = this.anchor;

        anchor.iconBefore()
            .get()
            .cssText("position: relative; left: -1px; top: 4px; max-width: 18px; max-height: 18px;");
        anchor.setHref(Url.parseRelative("/api-doc/index.html"));
        anchor.setTarget("_blank");
    }

    // SpreadsheetToolbarComponentItemLink............................................................................

    @Override //
    void onFocus(final Event event) {
        // do nothing, do not update history token.
    }

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public void refresh(final RefreshContext context) {
        // NOP
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return true; // always show
    }
}
