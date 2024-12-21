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

import elemental2.dom.HTMLAnchorElement;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLike;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetContextMenuTarget;
import walkingkooka.spreadsheet.dominokit.tooltip.SpreadsheetTooltipComponent;
import walkingkooka.spreadsheet.dominokit.tooltip.SpreadsheetTooltipComponentTarget;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

/**
 * Defines the public methods for a {@link HistoryTokenAnchorComponent}.
 */
public interface HistoryTokenAnchorComponentLike extends AnchorComponentLike<HistoryTokenAnchorComponent>,
        SpreadsheetContextMenuTarget<HTMLAnchorElement>,
        SpreadsheetTooltipComponentTarget<HTMLAnchorElement, HistoryTokenAnchorComponent>{

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

    // SpreadsheetTooltipComponentTarget................................................................................

    /**
     * Retrieves any {@link SpreadsheetTooltipComponent}
     */
    Optional<SpreadsheetTooltipComponent> spreadsheetTooltipComponent();

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.print(this.toString());

        final Optional<SpreadsheetTooltipComponent> tooltip = this.spreadsheetTooltipComponent();
        if (tooltip.isPresent()) {
            printer.indent();
            {
                printer.lineStart();
                tooltip.get()
                        .printTree(printer);
            }
            printer.outdent();
        }

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
