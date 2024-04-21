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

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleRefresh;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.VisibleComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetToolbarComponentItemAnchorSwagger extends SpreadsheetToolbarComponentItemAnchor<SpreadsheetToolbarComponentItemAnchorSwagger>
        implements NopComponentLifecycleOpenGiveFocus,
        NopComponentLifecycleRefresh,
        VisibleComponentLifecycle<HTMLElement, SpreadsheetToolbarComponentItemAnchorSwagger> {

    static SpreadsheetToolbarComponentItemAnchorSwagger with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponentItemAnchorSwagger(
                context
        );
    }

    private SpreadsheetToolbarComponentItemAnchorSwagger(final HistoryTokenContext context) {
        super(
                SpreadsheetToolbarComponent.swaggerId(),
                Optional.of(
                        SpreadsheetIcons.swagger()
                ),
                "Swagger",
                "Click to open swagger html client",
                context
        );

        final HistoryTokenAnchorComponent anchor = this.anchor;

        final CSSStyleDeclaration style = anchor.iconBefore()
                .get()
                .elementStyle();
        style.setProperty("max-width", "18px");
        style.setProperty("max-height", "18px");
        style.setProperty("position", "relative");
        style.setProperty("top", "4px");
        style.setProperty("left", "-1px");

        anchor.setHref(Url.parseRelative("/api-doc/index.html"));
        anchor.setTarget("_blank");
    }

    // SpreadsheetToolbarComponentItemLink............................................................................

    @Override //
    void onFocus(final Event event) {
        // do nothing, do not update history token.
    }

    // ComponentLifecycle...............................................................................................

    @Override
    public void refresh(final AppContext context) {
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
