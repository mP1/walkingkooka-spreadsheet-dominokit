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

import elemental2.dom.Element;
import elemental2.dom.EventListener;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.net.Url;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuTarget;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Objects;
import java.util.Optional;

/**
 * A duplicate of the original {@link HistoryTokenAnchorComponent} with almost all the public methods but without any
 * dependencies on browser objects.
 * <br>
 * The goal of this class is to allow JVM unit-testing of more complex components such as a Card that has a row of {@link HistoryTokenAnchorComponent}.
 */
public final class HistoryTokenAnchorComponent implements IsElement<Element>,
        SpreadsheetContextMenuTarget<Element>,
        TreePrintable {

    public static HistoryTokenAnchorComponent empty() {
        return new HistoryTokenAnchorComponent();
    }

    private HistoryTokenAnchorComponent() {

    }

    // disabled.........................................................................................................

    public boolean isDisabled() {
        return this.disabled;
    }

    public HistoryTokenAnchorComponent setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    // historyToken....................................................................................................

    public Optional<HistoryToken> historyToken() {
        return Optional.ofNullable(
                HistoryToken.parse(
                        UrlFragment.parse(this.href)
                )
        );
    }

    public HistoryTokenAnchorComponent setHistoryToken(final Optional<HistoryToken> historyToken) {
        Objects.requireNonNull(historyToken, "historyToken");
        final HistoryToken historyTokenOrNull = historyToken.orElse(null);

        return this.setHref(
                null == historyTokenOrNull ?
                        null :
                        Url.parseRelative(
                                "" + Url.FRAGMENT_START + historyTokenOrNull.urlFragment()
                        )
        );
    }

    // checked..........................................................................................................

    public boolean isChecked() {
        return this.checked;
    }

    public HistoryTokenAnchorComponent setChecked(final boolean checked) {
        this.checked = checked;
        return this;
    }

    private boolean checked;

    // href.............................................................................................................

    public Url href() {
        final String href = this.href;
        return CharSequences.isNullOrEmpty(href) ?
                null :
                Url.parseAbsoluteOrRelative(href);
    }

    public HistoryTokenAnchorComponent setHref(final Url url) {
        this.href =
                null == url ?
                        "" :
                        url.toString();
        return this.setDisabled(null == url);
    }

    private String href;

    // id..............................................................................................................

    public String id() {
        return this.id;
    }

    public HistoryTokenAnchorComponent setId(final String id) {
        this.id = id;
        return this;
    }

    private String id;

    // tabIndex.........................................................................................................

    public int tabIndex() {
        return this.tabIndex;
    }

    public HistoryTokenAnchorComponent setTabIndex(final int tabIndex) {
        this.tabIndex = tabIndex;
        return this;
    }

    private int tabIndex;


    // target.........................................................................................................

    public String target() {
        return this.target;
    }

    public HistoryTokenAnchorComponent setTarget(final String target) {
        this.target = target;
        return this;
    }

    private String target;

    private final static String TARGET = "target";

    // textContent......................................................................................................

    public String textContent() {
        return this.textContent;
    }

    public HistoryTokenAnchorComponent setTextContent(final String text) {
        this.textContent = text;
        return this;
    }

    private String textContent;

    // iconBefore | text Content | iconAfter

    // iconBefore.......................................................................................................

    public Optional<Icon<?>> iconBefore() {
        return this.iconBefore;
    }

    public HistoryTokenAnchorComponent setIconBefore(final Optional<Icon<?>> icon) {
        Objects.requireNonNull(icon, "icon");

        this.iconBefore = icon;
        return this;
    }

    private Optional<Icon<?>> iconBefore = Optional.empty();

    // iconAfter........................................................................................................

    public Optional<Icon<?>> iconAfter() {
        return this.iconAfter;
    }

    public HistoryTokenAnchorComponent setIconAfter(final Optional<Icon<?>> icon) {
        Objects.requireNonNull(icon, "icon");

        this.iconAfter = icon;
        return this;
    }

    private Optional<Icon<?>> iconAfter = Optional.empty();

    // tooltip..........................................................................................................

    public HistoryTokenAnchorComponent setTooltip(final String text,
                                                  final DropDirection dropDirection) {
        return this;
    }

    // events..........................................................................................................
    public HistoryTokenAnchorComponent addClickListener(final EventListener listener) {
        return this;
    }

    public HistoryTokenAnchorComponent addFocusListener(final EventListener listener) {
        return this;
    }

    public HistoryTokenAnchorComponent addKeydownListener(final EventListener listener) {
        return this;
    }

    public HistoryTokenAnchorComponent addClickAndKeydownEnterListener(final EventListener listener) {
        return this;
    }

    // "    test-clipboard-cut-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula]\n" +

    @Override
    public String toString() {
        // cant use surround values because null href will become LEFT BRACKET NULL RIGHT BRACKET - [null]
        String href = this.href;
        if (false == CharSequences.isNullOrEmpty(href)) {
            href = "[" + href + "]";
        }

        String disabled = "";
        if (this.disabled) {
            disabled = "DISABLED";
        }

        return ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                .value(this.iconBefore.map(Icon::getName))
                .enable(ToStringBuilderOption.QUOTE)
                .value(this.textContent)
                .disable(ToStringBuilderOption.QUOTE)
                .value(disabled)
                .value(href)
                .value(this.target)
                .value(this.checked ? "CHECKED" : "")
                .value(this.iconAfter.map(Icon::getName))
                .build();
    }

    // IsElement........................................................................................................

    @Override
    public Element element() {
        throw new UnsupportedOperationException();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.print(this.toString());

        // print any attached context menu
        final SpreadsheetContextMenu menu = this.menu;
        if (null != menu) {
            printer.indent();
            {
                printer.lineStart();
                menu.printTree(printer);
            }
            printer.outdent();
        }
    }

    // SpreadsheetContextMenuTarget.....................................................................................

    /**
     * Potentially some anchors could have a context menu attached to them.
     */
    @Override
    public void setSpreadsheetContextMenu(final SpreadsheetContextMenu menu) {
        Objects.requireNonNull(menu, "menu");

        this.menu = menu;
    }

    @Override
    public Optional<SpreadsheetContextMenu> spreadsheetContextMenu() {
        return Optional.ofNullable(this.menu);
    }

    private SpreadsheetContextMenu menu;
}
