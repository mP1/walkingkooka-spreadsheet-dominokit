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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontweight;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.HtmlComponent} that includes links for BOLD, NORMAL and a text box.
 */
public final class BigFontWeightComponent implements TextStylePropertyComponent<HTMLFieldSetElement, FontWeight, BigFontWeightComponent>,
    FormElementComponentDelegator<FontWeight, BigFontWeightComponent> {

    public static BigFontWeightComponent with(final String idPrefix,
                                              final BigFontWeightComponentContext context) {
        return new BigFontWeightComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private BigFontWeightComponent(final String idPrefix,
                                   final BigFontWeightComponentContext context) {
        super();

        this.bold = HistoryTokenAnchorComponent.empty()
            .setId(idPrefix + FontWeight.BOLD + SpreadsheetElementIds.LINK)
            .setTextContent("Bold")
            .setCssProperty("margin", "");

        this.normal = HistoryTokenAnchorComponent.empty()
            .setId(idPrefix + FontWeight.NORMAL + SpreadsheetElementIds.LINK)
            .setTextContent("Normal")
            .setCssProperty("margin", "");

        // TestPrefix123-text-TextBox
        this.fontWeight = FontWeightComponent.with(idPrefix)
            .setLabel("Text")
            .setId(
                idPrefix +
                    "text" +
                    SpreadsheetElementIds.TEXT_BOX
            );

        this.formElementComponent = FormElementComponent.with(
            FlexLayoutComponent.row()
                .setCssText("display: flex; flex-direction: row; gap: 5px;")
                .appendChild(this.bold)
                .appendChild(this.normal)
                .appendChild(this.fontWeight)
        );

        this.context = context;

        context.addHistoryTokenWatcher(
            // force refresh of links
            (final HistoryToken previous,
             final AppContext appContext) -> BigFontWeightComponent.this.setValue(this.value())
        );

        this.setValue(Optional.empty());
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public boolean filterTest(final TextStylePropertyFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this) ||
            filter.test(FontWeight.BOLD.toString()) ||
            filter.test(FontWeight.NORMAL.toString());
    }

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<FontWeight> name() {
        return PROPERTY_NAME;
    }

    private final static TextStylePropertyName<FontWeight> PROPERTY_NAME = TextStylePropertyName.FONT_WEIGHT;

    // FormValueComponent...............................................................................................

    @Override
    public BigFontWeightComponent alwaysShowHelperText() {
        this.fontWeight.alwaysShowHelperText();
        return this;
    }

    @Override
    public BigFontWeightComponent optional() {
        this.fontWeight.optional();
        return this;
    }

    @Override
    public BigFontWeightComponent required() {
        this.fontWeight.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        this.fontWeight.isRequired();
        return false;
    }

    @Override
    public Optional<FontWeight> value() {
        return this.fontWeight.value();
    }

    @Override
    public BigFontWeightComponent setValue(final Optional<FontWeight> value) {
        HistoryToken historyToken = context.historyToken()
            .setStylePropertyName(
                Optional.of(PROPERTY_NAME)
            );
        if (false == PROPERTY_NAME.equals(historyToken.stylePropertyName().orElse(null))) {
            historyToken = null;
        }

        final Optional<HistoryToken> optionalHistoryToken = Optional.of(historyToken);

        this.fontWeight.setValue(value);

        final FontWeight valueOrNull = value.orElse(null);

        this.bold.setValue(
            optionalHistoryToken.map(
                h -> h.setSaveValue(
                    Optional.of(FontWeight.BOLD)
                )
            )
        ).setChecked(
            null != valueOrNull && valueOrNull.isNormal()
        );

        this.normal.setValue(
            optionalHistoryToken.map(
                h -> h.setSaveValue(
                    Optional.of(FontWeight.NORMAL)
                )
            )
        ).setChecked(
            null != valueOrNull && valueOrNull.isNormal()
        );

        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<FontWeight> watcher) {
        return this.fontWeight.addValueWatcher(watcher);
    }

    @Override
    public boolean isDisabled() {
        return this.fontWeight.isDisabled();
    }

    @Override
    public BigFontWeightComponent setDisabled(final boolean disabled) {
        this.bold.setDisabled(disabled);
        this.normal.setDisabled(disabled);
        this.fontWeight.setDisabled(disabled);

        return this;
    }

    @Override
    public BigFontWeightComponent validate() {
        this.fontWeight.validate();
        return this;
    }

    @Override
    public BigFontWeightComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigFontWeightComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigFontWeightComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigFontWeightComponent focus() {
        this.fontWeight.focus();
        return this;
    }

    @Override
    public BigFontWeightComponent blur() {
        this.formElementComponent.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.bold.isEditing() ||
            this.normal.isEditing() ||
            this.fontWeight.isEditing();
    }

    @Override
    public HTMLFieldSetElement element() {
        return this.formElementComponent.element();
    }

    // FormElementComponentDelegator....................................................................................

    @Override
    public FormElementComponent<FontWeight, HTMLDivElement, ?> formElementComponent() {
        return this.formElementComponent;
    }

    private final FormElementComponent<FontWeight, HTMLDivElement, ?> formElementComponent;

    // @VisibleForTesting
    final HistoryTokenAnchorComponent bold;

    // @VisibleForTesting
    final HistoryTokenAnchorComponent normal;

    // @VisibleForTesting
    final FontWeightComponent fontWeight;

    private final BigFontWeightComponentContext context;

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
