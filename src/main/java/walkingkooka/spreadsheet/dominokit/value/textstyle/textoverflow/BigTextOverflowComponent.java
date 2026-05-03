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

package walkingkooka.spreadsheet.dominokit.value.textstyle.textoverflow;

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
import walkingkooka.spreadsheet.dominokit.value.text.TextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStyleDialogComponentFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextOverflow;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.HtmlComponent} that includes 2 links for CLIP, ECLIPSE and a {@link org.dominokit.domino.ui.forms.TextBox}
 * and a {@link TextOverflowComponent}. Updating components will update the {@link TextOverflowComponent}.
 */
public final class BigTextOverflowComponent implements TextStylePropertyComponent<HTMLFieldSetElement, TextOverflow, BigTextOverflowComponent>,
    FormElementComponentDelegator<TextOverflow, BigTextOverflowComponent> {

    public static BigTextOverflowComponent with(final String idPrefix,
                                                final BigTextOverflowComponentContext context) {
        return new BigTextOverflowComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private BigTextOverflowComponent(final String idPrefix,
                                     final BigTextOverflowComponentContext context) {
        super();

        final String width = "calc(33% - 5px)";

        this.clip = HistoryTokenAnchorComponent.empty()
            .setId(idPrefix + "clip" + SpreadsheetElementIds.LINK)
            .setTextContent("Clip")
            .setCssProperty("margin", "")
            .setCssProperty("width", width);

        this.ellipsis = HistoryTokenAnchorComponent.empty()
            .setId(idPrefix + "ellipsis" + SpreadsheetElementIds.LINK)
            .setTextContent("Ellipsis")
            .setCssProperty("margin", "")
            .setCssProperty("width", width);

        this.text = TextBoxComponent.empty()
            .setId(idPrefix + "text" + SpreadsheetElementIds.TEXT_BOX)
            .setCssProperty("width", width)
            .optional()
            .addValueWatcher2(
                (final Optional<String> value) -> this.setValue(
                    value.map(
                        TextOverflow::string
                    )
                )
            );

        this.textOverflow = TextOverflowComponent.with(idPrefix)
            .setLabel("Value");

        this.formElementComponent = FormElementComponent.with(
            FlexLayoutComponent.row()
                .appendChild(this.clip)
                .appendChild(this.ellipsis)
                .appendChild(this.text)
                .appendChild(this.textOverflow)
                .setCssProperty("justify-content", "space-between")
        );

        this.context = context;

        context.addHistoryTokenWatcher(
            // force refresh of links
            (final HistoryToken previous,
             final AppContext appContext) -> BigTextOverflowComponent.this.setValue(this.value())
        );

        this.setValue(Optional.empty());
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public boolean filterTest(final TextStyleDialogComponentFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this) ||
            filter.test(TextOverflow.CLIP.text()) ||
            filter.test(TextOverflow.ELLIPSIS.text());
    }

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<TextOverflow> name() {
        return PROPERTY_NAME;
    }

    private final static TextStylePropertyName<TextOverflow> PROPERTY_NAME = TextStylePropertyName.TEXT_OVERFLOW;

    // FormValueComponent...............................................................................................

    @Override
    public BigTextOverflowComponent alwaysShowHelperText() {
        this.textOverflow.alwaysShowHelperText();
        return this;
    }

    @Override
    public BigTextOverflowComponent optional() {
        this.textOverflow.optional();
        return this;
    }

    @Override
    public BigTextOverflowComponent required() {
        this.textOverflow.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        this.textOverflow.isRequired();
        return false;
    }

    @Override
    public Optional<TextOverflow> value() {
        return this.textOverflow.value();
    }

    @Override
    public BigTextOverflowComponent setValue(final Optional<TextOverflow> value) {
        HistoryToken historyToken = context.historyToken()
            .setStylePropertyName(
                Optional.of(PROPERTY_NAME)
            );
        if(false == PROPERTY_NAME.equals(historyToken.stylePropertyName().orElse(null))) {
            historyToken = null;
        }

        final Optional<HistoryToken> optionalHistoryToken = Optional.of(historyToken);

        this.textOverflow.setValue(value);

        final TextOverflow valueOrNull = value.orElse(null);
        this.clip.setValue(
                optionalHistoryToken.map(
                    h -> h.setSaveValue(
                        Optional.of(TextOverflow.CLIP)
                    )
                )
            ).setChecked(
                null != valueOrNull && valueOrNull.isClip()
        );
        this.ellipsis.setValue(
            optionalHistoryToken.map(
                h -> h.setSaveValue(
                    Optional.of(TextOverflow.ELLIPSIS)
                )
            )
        ).setChecked(
            null != valueOrNull && valueOrNull.isClip()
        );
        this.text.setValue(
            value.filter(
                TextOverflow::isString
            ).flatMap(
                (TextOverflow textOverflow) -> textOverflow.value()
            )
        ).setLabel("Text");

        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<TextOverflow> watcher) {
        return this.textOverflow.addValueWatcher(watcher);
    }

    @Override
    public boolean isDisabled() {
        return this.textOverflow.isDisabled();
    }

    @Override
    public BigTextOverflowComponent setDisabled(final boolean disabled) {
        this.clip.setDisabled(disabled);
        this.ellipsis.setDisabled(disabled);
        this.text.setDisabled(disabled);
        this.textOverflow.setDisabled(disabled);
        return this;
    }

    @Override
    public BigTextOverflowComponent validate() {
        this.textOverflow.validate();
        return this;
    }

    @Override
    public BigTextOverflowComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigTextOverflowComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigTextOverflowComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigTextOverflowComponent focus() {
        this.textOverflow.focus();
        return this;
    }

    @Override
    public BigTextOverflowComponent blur() {
        this.formElementComponent.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.clip.isEditing() ||
            this.ellipsis.isEditing() ||
            this.text.isEditing() ||
            this.textOverflow.isEditing();
    }

    @Override
    public HTMLFieldSetElement element() {
        return this.formElementComponent.element();
    }

    // FormElementComponentDelegator....................................................................................

    @Override
    public FormElementComponent<TextOverflow, HTMLDivElement, ?> formElementComponent() {
        return this.formElementComponent;
    }

    private final FormElementComponent<TextOverflow, HTMLDivElement, ?> formElementComponent;

    // @VisibleForTesting
    final HistoryTokenAnchorComponent clip;

    // @VisibleForTesting
    final HistoryTokenAnchorComponent ellipsis;

    // @VisibleForTesting
    final TextBoxComponent text;

    // @VisibleForTesting
    final TextOverflowComponent textOverflow;

    private final BigTextOverflowComponentContext context;

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
