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

package walkingkooka.spreadsheet.dominokit.label;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponentDelegator;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponentSuggestionsProvider;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A text box component that includes support for finding a label.
 */
public final class SpreadsheetLabelComponent implements SpreadsheetSuggestBoxComponentDelegator<HTMLFieldSetElement, SpreadsheetLabelName, SpreadsheetLabelComponent>,
    SpreadsheetDeltaFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    public static SpreadsheetLabelComponent with(final Function<SpreadsheetLabelName, MenuItem<SpreadsheetLabelName>> optionMenuItemCreator,
                                                 final SpreadsheetLabelComponentContext context) {
        return new SpreadsheetLabelComponent(
            Objects.requireNonNull(optionMenuItemCreator, "optionMenuItemCreator"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetLabelComponent(final Function<SpreadsheetLabelName, MenuItem<SpreadsheetLabelName>> optionMenuItemCreator,
                                      final SpreadsheetLabelComponentContext context) {
        this.suggestBox = SpreadsheetSuggestBoxComponent.with(
            SpreadsheetSelection::labelName,
            new SpreadsheetSuggestBoxComponentSuggestionsProvider<>() {
                @Override
                public void filter(final String startsWith,
                                   final SpreadsheetSuggestBoxComponent<SpreadsheetLabelName> suggestBox) {
                    context.findLabelByName(
                        startsWith,
                        OptionalInt.of(0), // offset
                        OptionalInt.of(20) // count
                    );
                }

                @Override
                public void verifyOption(final SpreadsheetLabelName value,
                                         final SpreadsheetSuggestBoxComponent<SpreadsheetLabelName> suggestBox) {
                    suggestBox.setVerifiedOption(value);
                }

                @Override
                public String menuItemKey(final SpreadsheetLabelName value) {
                    return value.text();
                }
            },
            optionMenuItemCreator
        );

        this.required();
        this.validate();

        context.addSpreadsheetDeltaFetcherWatcher(this);
    }

    // StringValue......................................................................................................

    public SpreadsheetLabelComponent setStringValue(final Optional<String> value) {
        this.suggestBox.setStringValue(value);
        return this;
    }

    public Optional<String> stringValue() {
        return this.suggestBox.stringValue();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetLabelComponent setValue(final Optional<SpreadsheetLabelName> label) {
        this.suggestBox.setValue(label);
        return this;
    }

    @Override //
    public Optional<SpreadsheetLabelName> value() {
        return this.suggestBox.value();
    }

    // events...........................................................................................................

    @Override
    public SpreadsheetLabelComponent addChangeListener(final ChangeListener<Optional<SpreadsheetLabelName>> listener) {
        this.suggestBox.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent addClickListener(final EventListener listener) {
        this.suggestBox.addClickListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent addFocusListener(final EventListener listener) {
        this.suggestBox.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent addKeydownListener(final EventListener listener) {
        this.suggestBox.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent addKeyupListener(final EventListener listener) {
        this.suggestBox.addKeyupListener(listener);
        return this;
    }

    // focus............................................................................................................

    @Override
    public SpreadsheetLabelComponent focus() {
        this.suggestBox.focus();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.suggestBox.isEditing();
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetLabelComponent setCssText(final String css) {
        this.suggestBox.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetLabelComponent setCssProperty(final String name,
                                                    final String value) {
        this.suggestBox.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.suggestBox.element();
    }

    // SpreadsheetSuggestBoxComponentDelegator..........................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<SpreadsheetLabelName> spreadsheetSuggestBoxComponent() {
        return this.suggestBox;
    }

    private final SpreadsheetSuggestBoxComponent<SpreadsheetLabelName> suggestBox;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.suggestBox.printTree(printer);
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.value()
            .map(HasText::text)
            .orElse("");
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        final UrlPath path = url.path();

        if (SpreadsheetDeltaFetcher.isGetLabelMappingsFindByName(method, path)) {
            final List<SpreadsheetLabelName> labels = delta.labels()
                .stream()
                .map(SpreadsheetLabelMapping::label)
                .collect(Collectors.toList());

            try {
                final SpreadsheetLabelName label = SpreadsheetSelection.labelName(
                    path.namesList()
                        .get(7)
                        .value()
                );

                // if search label is missing from the matches insert at top of list.
                if (false == labels.contains(label)) {
                    labels.add(
                        0,
                        label
                    );
                }

            } catch (final RuntimeException cause) {
                // dont insert into top of list
            }

            this.suggestBox.setOptions(labels);
        }
    }
}
