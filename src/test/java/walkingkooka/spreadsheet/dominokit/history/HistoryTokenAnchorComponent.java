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
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.tooltip.SpreadsheetTooltipComponent;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * A duplicate of the original {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent} with almost all the public methods but without any
 * dependencies on browser objects.
 * <br>
 * The goal of this class is to allow JVM unit-testing of more complex components such as a Card that has a row of {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent}.
 */
public final class HistoryTokenAnchorComponent implements HistoryTokenAnchorComponentLike,
        TestHtmlElementComponent<HTMLAnchorElement, walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent> {

    public static walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent empty() {
        return new walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent();
    }

    private HistoryTokenAnchorComponent() {
        this.tooltip = Optional.empty();
    }

    // setCssText.......................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setCssText(final String css) {
        return this;
    }

    // disabled.........................................................................................................

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    // checked..........................................................................................................

    @Override
    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setChecked(final boolean checked) {
        this.checked = checked;
        return this;
    }

    private boolean checked;

    // href.............................................................................................................

    @Override
    public Url href() {
        final String href = this.href;
        return CharSequences.isNullOrEmpty(href) ?
                null :
                Url.parseAbsoluteOrRelative(href);
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setHref(final Url url) {
        this.href =
                null == url ?
                        "" :
                        url.toString();
        return this.setDisabled(null == url);
    }

    private String href;

    // id..............................................................................................................

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setId(final String id) {
        this.id = id;
        return this;
    }

    private String id;

    // tabIndex.........................................................................................................

    @Override
    public int tabIndex() {
        return this.tabIndex;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setTabIndex(final int tabIndex) {
        this.tabIndex = tabIndex;
        return this;
    }

    private int tabIndex;


    // target.........................................................................................................

    @Override
    public String target() {
        return this.target;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setTarget(final String target) {
        this.target = target;
        return this;
    }

    private String target;

    // textContent......................................................................................................

    @Override
    public String textContent() {
        return this.textContent;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setTextContent(final String text) {
        this.textContent = text;
        return this;
    }

    private String textContent;

    // iconBefore | text Content | iconAfter

    // iconBefore.......................................................................................................

    @Override
    public Optional<Icon<?>> iconBefore() {
        return this.iconBefore;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setIconBefore(final Optional<Icon<?>> icon) {
        Objects.requireNonNull(icon, "icon");

        this.iconBefore = icon;
        return this;
    }

    private Optional<Icon<?>> iconBefore = Optional.empty();

    // iconAfter........................................................................................................

    @Override
    public Optional<Icon<?>> iconAfter() {
        return this.iconAfter;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent setIconAfter(final Optional<Icon<?>> icon) {
        Objects.requireNonNull(icon, "icon");

        this.iconAfter = icon;
        return this;
    }

    private Optional<Icon<?>> iconAfter = Optional.empty();

    // events..........................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent addClickListener(final EventListener listener) {
        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent addKeydownListener(final EventListener listener) {
        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent addClickAndKeydownEnterListener(final EventListener listener) {
        return this;
    }

    // focus............................................................................................................

    @Override
    public void focus() {
        // NOP
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

    // SpreadsheetTooltipComponentTarget................................................................................

    @Override
    public void tooltipAttached(final SpreadsheetTooltipComponent tooltip) {
        Objects.requireNonNull(tooltip, "tooltip");

        this.tooltip = Optional.of(tooltip);
    }

    @Override
    public void tooltipDetached() {
        this.tooltip = Optional.empty();
    }

    public Optional<SpreadsheetTooltipComponent> spreadsheetTooltipComponent() {
        return this.tooltip;
    }

    private Optional<SpreadsheetTooltipComponent> tooltip;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return HistoryTokenAnchorComponentToString.toString(this);
    }
}
