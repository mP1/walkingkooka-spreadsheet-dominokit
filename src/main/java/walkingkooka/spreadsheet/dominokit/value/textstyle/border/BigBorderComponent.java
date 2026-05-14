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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.HasFocusBlurEventListenerDelegator;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link TextStylePropertyComponent} that contains various components to view and edit components of a border.
 */
public final class BigBorderComponent implements TextStylePropertyComponent<HTMLFieldSetElement, Border, BigBorderComponent>,
    FormElementComponentDelegator<Border, BigBorderComponent>,
    HasFocusBlurEventListenerDelegator<BigBorderComponent> {

    public static BigBorderComponent with(final String idPrefix,
                                          final BigBorderComponentContext context) {
        return new BigBorderComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private BigBorderComponent(final String idPrefix,
                               final BigBorderComponentContext context) {
        super();

        // TestPrefix123-border-TextBox
        this.border = BorderComponent.with(idPrefix)
            .setLabel("Text");

        this.formElementComponent = FormElementComponent.with(
            FlexLayoutComponent.column()
                .appendChild(this.border)
                .setCssProperty("justify-content", "space-between")
        );

        this.context = context;

        context.addHistoryTokenWatcher(
            // force refresh of links
            (final HistoryToken previous,
             final AppContext appContext) -> BigBorderComponent.this.setValue(this.value())
        );

        this.setValue(Optional.empty());
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public boolean filterTest(final TextStylePropertyFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this);
    }

    @Override
    public Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds() {
        return TEXT_STYLE_PROPERTY_FILTER_KINDS_BORDER;
    }

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<Border> name() {
        return PROPERTY_NAME;
    }

    private final static TextStylePropertyName<Border> PROPERTY_NAME = TextStylePropertyName.BORDER;

    // FormValueComponent...............................................................................................

    @Override
    public BigBorderComponent alwaysShowHelperText() {
        this.border.alwaysShowHelperText();
        return this;
    }

    @Override
    public BigBorderComponent optional() {
        this.border.optional();
        return this;
    }

    @Override
    public BigBorderComponent required() {
        this.border.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        this.border.isRequired();
        return false;
    }

    @Override
    public Optional<Border> value() {
        return this.border.value();
    }

    @Override
    public BigBorderComponent setValue(final Optional<Border> value) {
        HistoryToken historyToken = this.context.historyToken()
            .setStylePropertyName(
                Optional.of(PROPERTY_NAME)
            );
        if (false == PROPERTY_NAME.equals(historyToken.stylePropertyName().orElse(null))) {
            historyToken = null;
        }

        final Optional<HistoryToken> optionalHistoryToken = Optional.of(historyToken);

        this.border.setValue(value);

        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<Border> watcher) {
        return this.border.addValueWatcher(watcher);
    }

    @Override
    public boolean isDisabled() {
        return this.border.isDisabled();
    }

    @Override
    public BigBorderComponent setDisabled(final boolean disabled) {
        this.border.setDisabled(disabled);

        return this;
    }

    @Override
    public BigBorderComponent validate() {
        this.border.validate();
        return this;
    }

    @Override
    public BigBorderComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigBorderComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigBorderComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigBorderComponent focus() {
        this.border.focus();
        return this;
    }

    @Override
    public BigBorderComponent blur() {
        this.formElementComponent.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.border.isEditing();
    }

    @Override
    public HTMLFieldSetElement element() {
        return this.formElementComponent.element();
    }

    // FormElementComponentDelegator....................................................................................

    @Override
    public FormElementComponent<Border, HTMLDivElement, ?> formElementComponent() {
        return this.formElementComponent;
    }

    private final FormElementComponent<Border, HTMLDivElement, ?> formElementComponent;

    // HasFocusBlurEventListenerDelegator...............................................................................

    @Override
    public BorderComponent hasFocusBlurEventListener() {
        return this.border;
    }

    // @VisibleForTesting
    final BorderComponent border;

    private final BigBorderComponentContext context;

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
