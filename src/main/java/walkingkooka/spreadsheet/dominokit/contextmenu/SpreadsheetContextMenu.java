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

package walkingkooka.spreadsheet.dominokit.contextmenu;

import elemental2.dom.Element;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.Menu;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetSelectionMenuContext;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * Abstraction for building a context menu.
 */
public final class SpreadsheetContextMenu implements TreePrintable {

    /**
     * Returns an empty {@link SpreadsheetContextMenu} for the given {@link Element}.
     */
    public static <T> SpreadsheetContextMenu wrap(final SpreadsheetContextMenuTarget<? extends Element> target,
                                                  final HistoryContext context) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(context, "context");

        final SpreadsheetContextMenu contextMenu = SpreadsheetContextMenuNative.empty(
            target,
            context
        );
        target.setSpreadsheetContextMenu(contextMenu);
        return contextMenu;
    }

    static SpreadsheetContextMenu with(final Menu<?> menu,
                                       final HistoryContext context) {
        return new SpreadsheetContextMenu(
            menu,
            context
        );
    }

    private SpreadsheetContextMenu(final Menu<?> menu,
                                   final HistoryContext context) {
        this.menu = menu;
        this.context = context;
        this.allowSeparator = false;
        this.empty = true;
    }

    /**
     * Creates an empty sub menu returning the {@link SpreadsheetContextMenu} which may be used to add items.
     */
    public SpreadsheetContextMenu subMenu(final String id,
                                          final String text) {
        return this.subMenu(
            id,
            text,
            Optional.empty(), // icon
            Optional.empty() // badge
        );
    }

    /**
     * Creates an empty sub menu returning the {@link SpreadsheetContextMenu} which may be used to add items.
     */
    public SpreadsheetContextMenu subMenu(final String id,
                                          final String text,
                                          final Icon<?> icon) {
        return this.subMenu(
            id,
            text,
            Optional.of(icon),
            Optional.empty()
        );
    }

    /**
     * Creates an empty sub menu returning the {@link SpreadsheetContextMenu} which may be used to add items.
     */
    public SpreadsheetContextMenu subMenu(final String id,
                                          final String text,
                                          final String badge) {
        return this.subMenu(
            id,
            text,
            Optional.empty(),
            Optional.of(badge)
        );
    }

    /**
     * Creates an empty sub menu returning the {@link SpreadsheetContextMenu} which may be used to add items.
     */
    public SpreadsheetContextMenu subMenu(final String id,
                                          final String text,
                                          final Optional<Icon<?>> icon,
                                          final Optional<String> badge) {
        CharSequences.failIfNullOrEmpty(id, "id");
        if (false == id.endsWith(SpreadsheetElementIds.SUB_MENU)) {
            throw new IllegalArgumentException(
                "Invalid subMenu id " +
                    CharSequences.quote(id) +
                    " missing " +
                    CharSequences.quote(SpreadsheetElementIds.SUB_MENU)
            );
        }

        CharSequences.failIfNullOrEmpty(text, "text");

        this.addSeparatorIfNecessary();

        final Menu<Object> subMenu = SpreadsheetContextMenuNative.addSubMenu(
            id,
            text,
            icon,
            badge,
            this
        );
        this.allowSeparator = true;
        this.empty = false;

        return new SpreadsheetContextMenu(
            subMenu,
            this.context
        );
    }

    /**
     * Adds a checked menu item, conditionally setting the check mark and conditional clearing/saving the value.
     */
    public <TT> SpreadsheetContextMenu checkedItem(final String id,
                                                   final String text,
                                                   final Optional<Icon<?>> icon,
                                                   final TextStylePropertyName<TT> stylePropertyName,
                                                   final TT stylePropertyValue,
                                                   final String key,
                                                   final SpreadsheetSelectionMenuContext context) {
        final boolean set = context.isChecked(
            stylePropertyName,
            stylePropertyValue
        );
        return this.item(
            this.context.historyToken()
                .setStylePropertyName(stylePropertyName)
                .setSaveValue(
                    Optional.ofNullable(
                        set ?
                            null :
                            stylePropertyValue
                    )
                ).<Object>contextMenuItem(
                    id,
                    text
                ).icon(icon)
                .checked(set)
                .key(key)
        );
    }

    /**
     * Adds the given {@link SpreadsheetContextMenuItem} to this menu.
     */
    public SpreadsheetContextMenu item(final SpreadsheetContextMenuItem<Object> item) {
        Objects.requireNonNull(item, "item");

        this.addSeparatorIfNecessary();

        SpreadsheetContextMenuNative.menuAppendChildSpreadsheetContextMenuItem(
            item,
            this
        );
        this.allowSeparator = true;
        this.empty = false;

        return this;
    }

    /**
     * Adds a {@link IsElement} which is responsible for providing the content of the menu item.
     */
    public SpreadsheetContextMenu item(final IsElement<?> component) {
        Objects.requireNonNull(component, "component");

        this.addSeparatorIfNecessary();

        SpreadsheetContextMenuNative.menuAppendChildIsElement(
            component,
            this
        );

        this.allowSeparator = true;
        return this;
    }

    /**
     * This method is used to lazily add a separator if one was added recently. This includes smarts so multiple
     * separators are not added in series or a separator with no items afterwards is also not possible.
     */
    private void addSeparatorIfNecessary() {
        if (this.addSeparator) {
            SpreadsheetContextMenuNative.menuAppendChildSeparator(this);
            this.addSeparator = false;
            this.allowSeparator = false;
        }
    }

    /**
     * Adds a separator before the next item is added.
     */
    public SpreadsheetContextMenu separator() {
        if (this.allowSeparator) {
            this.addSeparator = true;
        }
        return this;
    }

    private boolean addSeparator;

    /**
     * Used to prevent adding a separator as the first item, and to prevent adding to a separator immediately after another.
     */
    private boolean allowSeparator;

    /**
     * Disables this menu if its empty. This should only be called after all items have been added.
     */
    public SpreadsheetContextMenu disableIfEmpty() {
        if (this.empty) {
            SpreadsheetContextMenuNative.menuDisable(this);
        }
        return this;
    }

    private boolean empty;

    /**
     * Gives focus to the menu
     */
    public SpreadsheetContextMenu focus() {
        this.menu.open(true);
        this.context.addHistoryTokenWatcher(
            (final HistoryToken previous,
             final AppContext context) ->
                SpreadsheetContextMenu.this.close()
        );
        return this;
    }

    /**
     * Closes the menu if it is open and cleans up the menu target removing the right mouse listener that DominoUI adds.
     */
    // https://github.com/DominoKit/domino-ui/issues/1027
    // Cant cleanly remove MenuTarget from Menu - aka Menu.setTarget(null) fails with NPE
    public void close() {
        this.menu.close();
        this.menu.remove();

        // remove the context menu listener added by domino, so SpreadsheetViewportComponent rebuilds the menu
        this.menu.setTarget(null);
    }

    final Menu<?> menu;

    final HistoryContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.menu.toString();
    }

    // TreePrintable...................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        TreePrintable.printTreeOrToString(
            this.menu,
            printer
        );
    }
}
