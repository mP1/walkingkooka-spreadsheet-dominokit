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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.Event;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.elements.SectionElement;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.layout.RightDrawerSize;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.meta.SpreadsheetMetadataHistoryTokenAwareComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.meta.SpreadsheetMetadataPanelComponent;
import walkingkooka.spreadsheet.dominokit.meta.SpreadsheetMetadataPanelComponentContexts;
import walkingkooka.spreadsheet.dominokit.toolbar.SpreadsheetToolbarComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponent;

import java.util.Objects;

final class SpreadsheetAppLayout extends AppLayout implements
        HistoryTokenAwareComponentLifecycle,
        HtmlElementComponent<HTMLDivElement, SpreadsheetAppLayout> {


    // header = metadata toggle | clickable(editable) spreadsheet name
    // right = editable metadata properties
    // content = toolbar
    //   formula,
    //   table holding spreadsheet cells

    static SpreadsheetAppLayout prepare(final SpreadsheetViewportComponent viewportComponent,
                                        final AppContext context) {
        Objects.requireNonNull(context, "context");
        final SpreadsheetAppLayout layout = new SpreadsheetAppLayout(context);

        layout.setOverFlowX("hidden")
                .setOverFlowY("hidden");

        layout.getContent()
                .setPadding("0px") // kills the dui-layout-content padding: 25px
                .setOverFlowY("hidden") // stop scrollbars on the cell viewport
                .appendChild(viewportComponent);

        layout.getNavBar()
                .withTitle(
                        (n, header) -> {
                            header.appendChild(
                                    AppHistoryTokenAnchorComponents.files()
                            );
                            header.appendChild(
                                    AppHistoryTokenAnchorComponents.spreadsheetName(context)
                            );
                        }
                ).getBody()
                .appendChild(
                        SpreadsheetToolbarComponent.with(context)
                );

        // right drawer.................................................................................................
        layout.setRightDrawerSize(RightDrawerSize.XLARGE)
                .getRightDrawerContent()
                .appendChild(
                        SpreadsheetMetadataHistoryTokenAwareComponentLifecycle.with(
                                SpreadsheetAppLayoutRightDrawerComponent.with(
                                        layout,
                                        SpreadsheetMetadataPanelComponent.with(
                                                SpreadsheetMetadataPanelComponentContexts.appContext(context)
                                        ).setCssText("padding-left: 5px; padding-bottom: var(--dui-right-drawer-padding-top);") // without this fix the bottom 64px are chopped and out of view
                                ),
                                context // HistoryTokenContext
                        )
                );

        layout.onRightDrawerClosed(
                layout::appLayoutRightPanelClosed
        );

        layout.setRightDrawerToggleIcon(
                Icons.menu_open()
                        .addClickListener(
                                layout::appLayoutRightToggleIconOnClick
                        )
        );

        return layout;
    }

    private SpreadsheetAppLayout(final AppContext context) {
        super();
        context.addHistoryTokenWatcher(this);
        Doms.setVisibility(
                this.element(),
                false
        );

        this.context = context;
    }

    /**
     * Handler that reacts to the right panel toggle icon being clicked, updating the history. The {@link SpreadsheetMetadataPanelComponent}
     * will see the history token change and then open or hide itself.
     */
    private void appLayoutRightToggleIconOnClick(final Event event) {
        final AppContext context = this.context;
        HistoryToken token = context.historyToken();

        context.pushHistoryToken(
                this.isRightDrawerOpen() ?
                        token.metadataHide() :
                        token.metadataShow()
        );
    }

    /**
     * This event is fired when the right panel closes, such as when the user clicks away from it and the history token needs to be updated.
     */
    private void appLayoutRightPanelClosed(final AppLayout layout,
                                           final SectionElement section) {
        final AppContext context = this.context;
        final HistoryToken token = context.historyToken();

        // HACK only hide metadata panel if NOT displaying a metadata editor dialog
        if (false == SpreadsheetDialogComponent.isAnyOpen()) {
            context.pushHistoryToken(
                    token.metadataHide()
            );
        }
    }

    private final AppContext context;

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public boolean isOpen() {
        return false == Doms.isVisibilityHidden(
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
