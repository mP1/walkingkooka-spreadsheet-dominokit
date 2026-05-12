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

package walkingkooka.spreadsheet.dominokit.value.textstyle.filter;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.HtmlComponent} that includes links for BOLD, NORMAL and a text box.
 */
public final class BigTextStylePropertyFilterComponent implements FormElementComponentDelegator<TextStylePropertyFilter, BigTextStylePropertyFilterComponent> {

    public static BigTextStylePropertyFilterComponent with(final String idPrefix,
                                                           final BigTextStylePropertyFilterComponentContext context) {
        return new BigTextStylePropertyFilterComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private BigTextStylePropertyFilterComponent(final String idPrefix,
                                                final BigTextStylePropertyFilterComponentContext context) {
        super();

        final AnchorListComponent kindParent = AnchorListComponent.empty();

        final List<HistoryTokenAnchorComponent> kinds = Lists.array();

        for (final TextStylePropertyFilterKind kind : TextStylePropertyFilterKind.values()) {
            final String name = kind.name();

            final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.empty()
                .setId(
                    idPrefix + name + SpreadsheetElementIds.LINK
                ).setTextContent(
                    CaseKind.SNAKE.change(
                        name,
                        CaseKind.TITLE
                    )
                );

            kinds.add(anchor);
            kindParent.appendChild(anchor);
        }

        this.kinds = kinds;

        // TestPrefix123-filter-TextBox
        this.filter = TextStylePropertyFilterComponent.with(idPrefix)
            .setId(
                idPrefix +
                    "text" +
                    SpreadsheetElementIds.TEXT_BOX
            );

        this.formElementComponent = FormElementComponent.with(
            FlexLayoutComponent.column()
                .appendChild(this.filter)
                .appendChild(kindParent)
                .setCssProperty("justify-content", "space-between")
        );

        this.context = context;

        context.addHistoryTokenWatcher(
            // force refresh of links
            (final HistoryToken previous,
             final AppContext appContext) -> BigTextStylePropertyFilterComponent.this.setValue(
                this.value()
            )
        );

        this.setValue(Optional.empty());
    }

    // FormValueComponent...............................................................................................

    @Override
    public BigTextStylePropertyFilterComponent alwaysShowHelperText() {
        this.filter.alwaysShowHelperText();
        return this;
    }

    @Override
    public BigTextStylePropertyFilterComponent optional() {
        this.filter.optional();
        return this;
    }

    @Override
    public BigTextStylePropertyFilterComponent required() {
        this.filter.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        this.filter.isRequired();
        return false;
    }

    @Override
    public Optional<TextStylePropertyFilter> value() {
        return this.filter.value();
    }

    @Override
    public BigTextStylePropertyFilterComponent setValue(final Optional<TextStylePropertyFilter> value) {
        final HistoryToken historyToken = this.context.historyToken();

        this.filter.setValue(value);

        TextStylePropertyFilter filter = value.orElse(TextStylePropertyFilter.ALL);

        final Iterator<HistoryTokenAnchorComponent> anchors = this.kinds.iterator();
        for (final TextStylePropertyFilterKind kind : TextStylePropertyFilterKind.values()) {

            final HistoryTokenAnchorComponent anchor = anchors.next();

            anchor.setHistoryToken(
                Optional.of(
                    historyToken.setFilter(
                        Optional.of(
                            filter.add(kind)
                                .toString()
                        )
                    )
                )
            );

            anchor.setChecked(
                value.map((TextStylePropertyFilter f) -> f.contains(kind))
                    .orElse(false)
            );
        }
        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<TextStylePropertyFilter> watcher) {
        return this.filter.addValueWatcher(watcher);
    }

    private final List<HistoryTokenAnchorComponent> kinds;

    @Override
    public boolean isDisabled() {
        return this.filter.isDisabled();
    }

    @Override
    public BigTextStylePropertyFilterComponent setDisabled(final boolean disabled) {
        for (HistoryTokenAnchorComponent kind : this.kinds) {
            kind.setDisabled(disabled);
        }
        this.filter.setDisabled(disabled);

        return this;
    }

    @Override
    public BigTextStylePropertyFilterComponent validate() {
        this.filter.validate();
        return this;
    }

    @Override
    public BigTextStylePropertyFilterComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigTextStylePropertyFilterComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigTextStylePropertyFilterComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigTextStylePropertyFilterComponent focus() {
        this.filter.focus();
        return this;
    }

    @Override
    public BigTextStylePropertyFilterComponent blur() {
        this.formElementComponent.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.filter.isEditing();
    }

    @Override
    public HTMLFieldSetElement element() {
        return this.formElementComponent.element();
    }

    // FormElementComponentDelegator....................................................................................

    @Override
    public FormElementComponent<TextStylePropertyFilter, HTMLDivElement, ?> formElementComponent() {
        return this.formElementComponent;
    }

    private final FormElementComponent<TextStylePropertyFilter, HTMLDivElement, ?> formElementComponent;

    // @VisibleForTesting
    final TextStylePropertyFilterComponent filter;

    private final BigTextStylePropertyFilterComponentContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.formElementComponent.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.formElementComponent.printTree(printer);
        }
        printer.outdent();
    }
}
