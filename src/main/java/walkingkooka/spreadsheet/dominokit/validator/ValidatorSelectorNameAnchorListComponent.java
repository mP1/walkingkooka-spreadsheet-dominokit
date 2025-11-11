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

package walkingkooka.spreadsheet.dominokit.validator;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.validation.provider.ValidatorAlias;
import walkingkooka.validation.provider.ValidatorName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Holds a list of anchors for each given {@link ValidatorName}.
 */
public final class ValidatorSelectorNameAnchorListComponent implements ValueComponent<HTMLDivElement, ValidatorSelector, ValidatorSelectorNameAnchorListComponent>,
    HtmlComponentDelegator<HTMLDivElement, ValidatorSelectorNameAnchorListComponent>,
    SpreadsheetMetadataFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    public static ValidatorSelectorNameAnchorListComponent with(final String idPrefix,
                                                                final ValidatorSelectorNameAnchorListComponentContext context) {
        return new ValidatorSelectorNameAnchorListComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private ValidatorSelectorNameAnchorListComponent(final String idPrefix,
                                                     final ValidatorSelectorNameAnchorListComponentContext context) {
        super();

        this.idPrefix = idPrefix;

        this.anchors = AnchorListComponent.empty()
            .setId(idPrefix + "links");
        this.value = Optional.empty();
        this.context = context;
    }

    private void refresh() {
        final AnchorListComponent anchors = this.anchors;
        anchors.removeAllChildren();

        final ValidatorSelectorNameAnchorListComponentContext context = this.context;
        final HistoryToken historyToken = context.historyToken();
        final String idPrefix = this.idPrefix;

        final String value = this.value
            .map(ValidatorSelector::valueText)
            .orElse("");

        for (final ValidatorAlias validator : context.spreadsheetMetadata().getOrFail(SpreadsheetMetadataPropertyName.VALIDATION_VALIDATORS)) {
            final ValidatorName name = validator.name();

            anchors.appendChild(
                historyToken.validator()
                    .setSaveValue(
                        Optional.ofNullable(
                            ValidatorSelector.with(
                                name,
                                value
                            )
                        )
                    ).link(idPrefix + name)
                    .setTextContent(
                        CaseKind.kebabToTitle(name.text())
                    )
            );
        }
    }

    private final String idPrefix;

    private final ValidatorSelectorNameAnchorListComponentContext context;

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.refresh();
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
        // nop
    }

    // ValueComponent...................................................................................................

    @Override
    public Optional<ValidatorSelector> value() {
        return this.value;
    }

    @Override
    public ValidatorSelectorNameAnchorListComponent setValue(final Optional<ValidatorSelector> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;
        this.refresh();
        return this;
    }

    private Optional<ValidatorSelector> value;

    @Override
    public Runnable addValueWatcher(final ValueWatcher<ValidatorSelector> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public ValidatorSelectorNameAnchorListComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidatorSelectorNameAnchorListComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidatorSelectorNameAnchorListComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidatorSelectorNameAnchorListComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidatorSelectorNameAnchorListComponent focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidatorSelectorNameAnchorListComponent blur() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEditing() {
        return this.anchors.isEditing();
    }

    private final AnchorListComponent anchors;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.anchors;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        {
            printer.indent();
            {
                this.anchors.printTree(printer);
            }
            printer.outdent();
        }
    }
}
