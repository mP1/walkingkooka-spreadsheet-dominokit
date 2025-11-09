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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.KeyboardEvent;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuTarget;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.tooltip.TooltipComponent;
import walkingkooka.spreadsheet.dominokit.tooltip.TooltipComponentTarget;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;

/**
 * Defines the public methods for a {@link HistoryTokenAnchorComponent}.
 */
abstract class HistoryTokenAnchorComponentLike implements AnchorComponent<HistoryTokenAnchorComponent, HistoryToken>,
    SpreadsheetContextMenuTarget<HTMLAnchorElement>,
    TooltipComponentTarget<HTMLAnchorElement, HistoryTokenAnchorComponent> {

    HistoryTokenAnchorComponentLike() {
        super();
    }

    // Value............................................................................................................

    @Override
    public final Optional<HistoryToken> value() {
        return this.historyToken();
    }

    @Override
    public final HistoryTokenAnchorComponent setValue(final Optional<HistoryToken> value) {
        return this.setHistoryToken(value);
    }

    // addXXXListener...................................................................................................

    @Override
    public final HistoryTokenAnchorComponent addBlurListener(final EventListener listener) {
        return this.addEventListener(
            EventType.blur,
            listener
        );
    }

    @Override
    public final HistoryTokenAnchorComponent addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public final HistoryTokenAnchorComponent addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu,
            listener
        );
    }

    @Override
    public final HistoryTokenAnchorComponent addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public final HistoryTokenAnchorComponent addInputListener(final EventListener listener) {
        return this.addEventListener(
            EventType.input,
            listener
        );
    }

    @Override
    public final HistoryTokenAnchorComponent addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    @Override
    public final HistoryTokenAnchorComponent addKeyDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    abstract HistoryTokenAnchorComponent addEventListener(final EventType eventType,
                                                          final EventListener listener);

    // historyToken....................................................................................................

    /**
     * If the HREF contains a url with a fragment it will be parsed into a {@link HistoryToken} otherwise
     * an {@link Optional#empty()} will be returned.
     */
    public final Optional<HistoryToken> historyToken() {
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
    public final HistoryTokenAnchorComponent setHistoryToken(final Optional<? extends HistoryToken> historyToken) {
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
    public final HistoryTokenAnchorComponent clickOrEnterPushHistoryToken(final HistoryContext context) {
        Objects.requireNonNull(context, "context");

        final EventListener listener = (e) -> {
            e.preventDefault();

            this.historyToken()
                .ifPresent(
                    context::pushHistoryToken
                );
        };

        this.addKeyDownListener(
            e -> {
                final KeyboardEvent keyboardEvent = (KeyboardEvent) e;
                if(keyboardEvent.key.equals(Key.Enter)) {
                    e.preventDefault();
                    listener.handleEvent(e);
                }
            }
        );

        return this.addClickListener(listener);
    }

    // TooltipComponentTarget................................................................................

    /**
     * Retrieves any {@link TooltipComponent}
     */
    abstract Optional<TooltipComponent> spreadsheetTooltipComponent();

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.print(this.toString());

        final Optional<TooltipComponent> tooltip = this.spreadsheetTooltipComponent();
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

    // Object...........................................................................................................

    // "    test-clipboard-cut-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula]\n" +

    @Override
    public final String toString() {
        // cant use surround values because null href will become LEFT BRACKET NULL RIGHT BRACKET - [null]
        Url href = this.href();

        String hrefString = null;
        if (null != href) {
            hrefString = "[" + href + "]";
        }

        String disabled = "";
        if (this.isDisabled()) {
            disabled = "DISABLED";
        }

        String badge = this.badge();
        if (false == badge.isEmpty()) {
            badge = "(" + badge + ")";
        }

        return ToStringBuilder.empty()
            .disable(ToStringBuilderOption.QUOTE)
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .value(this.iconBefore().map(Icon::getName))
            .enable(ToStringBuilderOption.QUOTE)
            .value(this.textContent())
            .disable(ToStringBuilderOption.QUOTE)
            .value(disabled)
            .value(hrefString)
            .value(badge)
            .value(this.target())
            .value(this.isChecked() ? "CHECKED" : "")
            .value(this.iconAfter().map(Icon::getName))
            .label("id")
            .value(this.id())
            .build();
    }
}
