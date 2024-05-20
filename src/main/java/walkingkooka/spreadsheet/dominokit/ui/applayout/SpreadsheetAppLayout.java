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

package walkingkooka.spreadsheet.dominokit.ui.applayout;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.layout.AppLayout;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;

import java.util.Objects;

public final class SpreadsheetAppLayout extends AppLayout implements
        ComponentLifecycle,
        HtmlElementComponent<HTMLDivElement, SpreadsheetAppLayout> {

    public static SpreadsheetAppLayout empty() {
        return new SpreadsheetAppLayout();
    }

    private SpreadsheetAppLayout() {
        super();
    }

    @Override
    public boolean isOpen() {
        return Doms.isVisibilityHidden(
                this.element()
        );
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetNameHistoryToken;
    }

    @Override
    public void open(final AppContext context) {
        Doms.setVisibility(
                this.element(),
                true
        );
    }

    @Override
    public void refresh(final AppContext context) {
        // do nothing only want AppLayout to be hidden when not SpreadsheetNameHistoryToken
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        // do nothing
    }

    @Override
    public void close(final AppContext context) {
        Doms.setVisibility(
                this.element(),
                false
        );
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return false;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetAppLayout setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.element.cssText(css);
        return this;
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
    }
}
