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

package walkingkooka.spreadsheet.dominokit.ui.historytokenanchor;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuTarget;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Optional;

/**
 * Defines the public methods for a {@link HistoryTokenAnchorComponent}.
 */
public interface HistoryTokenAnchorComponentLike extends HtmlElementComponent<HTMLAnchorElement, HistoryTokenAnchorComponent>,
        SpreadsheetContextMenuTarget<HTMLAnchorElement>,
        TreePrintable {

    default boolean isDisabled() {
        return null == this.href();
    }

    HistoryTokenAnchorComponent setDisabled(final boolean disabled);

    // historyToken....................................................................................................

    /**
     * If the HREF contains a url with a fragment it will be parsed into a {@link HistoryToken} otherwise
     * an {@link Optional#empty()} will be returned.
     */
    default Optional<HistoryToken> historyToken() {
        final Url url = this.href();

        return Optional.ofNullable(
                url instanceof HasUrlFragment ?
                        HistoryToken.parse(
                                ((HasUrlFragment) url).urlFragment()
                        ) :
                        null
        );
    }

    /**
     * Setter takes the given {@link HistoryToken} updating the HREF.
     */
    default HistoryTokenAnchorComponent setHistoryToken(final Optional<HistoryToken> historyToken) {
        final HistoryToken historyTokenOrNull = historyToken.orElse(null);

        return this.setHref(
                null == historyTokenOrNull ?
                        null :
                        Url.parseAbsoluteOrRelative(
                                "" + Url.FRAGMENT_START + historyTokenOrNull.urlFragment()
                        )
        );
    }

    /**
     * The {@link #historyToken()} will be pushed if this anchor is clicked or ENTER key downed.
     */
    default HistoryTokenAnchorComponent addPushHistoryToken(final HistoryTokenContext context) {
        return this.addClickAndKeydownEnterListener(
                (e) -> {
                    e.preventDefault();

                    this.historyToken()
                            .ifPresent(
                                    context::pushHistoryToken
                            );
                }
        );
    }

    // checked.........................................................................................................

    boolean isChecked();

    HistoryTokenAnchorComponent setChecked(final boolean checked);

    // href.............................................................................................................

    /**
     * Getter that returns the HREF attribute of this ANCHOR.
     */
    Url href();

    /**
     * Setter that replaces the HREF attribute of this ANCHOR.
     */
    HistoryTokenAnchorComponent setHref(final Url url);

    // id...............................................................................................................

    String id();

    HistoryTokenAnchorComponent setId(final String id);

    // tabIndex.........................................................................................................

    int tabIndex();

    HistoryTokenAnchorComponent setTabIndex(final int tabIndex);

    // target.........................................................................................................

    String target();

    HistoryTokenAnchorComponent setTarget(final String target);

    // textContent......................................................................................................

    String textContent();

    HistoryTokenAnchorComponent setTextContent(final String text);

    // iconBefore | text Content | iconAfter

    // iconBefore......................................................................................................

    Optional<Icon<?>> iconBefore();

    HistoryTokenAnchorComponent setIconBefore(final Optional<Icon<?>> icon);

    // iconAfter......................................................................................................

    Optional<Icon<?>> iconAfter();

    HistoryTokenAnchorComponent setIconAfter(final Optional<Icon<?>> icon);

    // tooltip..........................................................................................................

    HistoryTokenAnchorComponent setTooltip(final String text,
                                           final DropDirection dropDirection);

    // events...........................................................................................................
    HistoryTokenAnchorComponent addClickListener(final EventListener listener);

    HistoryTokenAnchorComponent addFocusListener(final EventListener listener);

    HistoryTokenAnchorComponent addKeydownListener(final EventListener listener);

    /**
     * Adds a {@link EventListener} that receives click and keydown with ENTER events.
     */
    HistoryTokenAnchorComponent addClickAndKeydownEnterListener(final EventListener listener);

    // focus............................................................................................................

    void focus();

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.print(this.toString());

        // print any attached context menu
        final Optional<SpreadsheetContextMenu> menu = this.spreadsheetContextMenu();
        if (menu.isPresent()) {
            printer.indent();
            {
                printer.lineStart();
                menu.get()
                        .printTree(printer);
            }
            printer.outdent();
        }
    }
}
