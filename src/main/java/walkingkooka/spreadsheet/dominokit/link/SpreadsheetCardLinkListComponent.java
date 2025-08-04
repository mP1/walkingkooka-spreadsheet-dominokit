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

package walkingkooka.spreadsheet.dominokit.link;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A card that holds links, each with a save history token link.
 * The initial use-case for this component is to display available spreadsheet formatter names when the user is editing a format.
 */
public final class SpreadsheetCardLinkListComponent implements HtmlComponent<HTMLDivElement, SpreadsheetCardLinkListComponent> {

    public static SpreadsheetCardLinkListComponent with(final String id,
                                                        final String title,
                                                        final Function<String, String> labelMaker) {
        return new SpreadsheetCardLinkListComponent(
            CharSequences.failIfNullOrEmpty(id, "id"),
            Objects.requireNonNull(title, "title"),
            Objects.requireNonNull(labelMaker, "labelMaker")
        );
    }

    private SpreadsheetCardLinkListComponent(final String id,
                                             final String title,
                                             final Function<String, String> labelMaker) {
        this.id = id;

        this.flex = SpreadsheetFlexLayout.row();
        this.root = SpreadsheetCard.empty()
            .setTitle(title)
            .appendChild(this.flex);

        this.labelMaker = labelMaker;
    }

    /**
     * Replaces all links and creates links for each of the given text items.
     */
    public void refresh(final List<String> texts,
                        final SpreadsheetCardLinkListComponentContext context) {
        this.refresh0(
            Lists.immutable(
                Objects.requireNonNull(texts, "texts")
            ),
            Objects.requireNonNull(context, "context")
        );
    }

    void refresh0(final List<String> texts,
                  final SpreadsheetCardLinkListComponentContext context) {
        this.root.hide();

        final SpreadsheetFlexLayout flex = this.flex.removeAllChildren();

        int i = 0;
        for (final String text : texts) {
            flex.appendChild(
                this.anchor(
                    text,
                    i,
                    context
                )
            );
            i++;
        }

        if (false == texts.isEmpty()) {
            this.root.show();
        }
    }

    /**
     * Creates an anchor with the given text and a save {@link HistoryToken}.
     */
    private HistoryTokenAnchorComponent anchor(final String text,
                                               final int index,
                                               final SpreadsheetCardLinkListComponentContext context) {
        final HistoryToken historyToken = context.historyToken();

        return historyToken.saveLink(
            this.id + index,
            this.labelMaker.apply(text),
            context.saveValueText(text)
        ).setDisabled(context.isDisabled(text));
    }

    private final Function<String, String> labelMaker;

    /**
     * The base id prefix
     */
    private final String id;

    /**
     * The root of parent card of this list of links.
     */
    private final SpreadsheetCard root;

    /**
     * The parent holding the links.
     */
    private final SpreadsheetFlexLayout flex;

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetCardLinkListComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.root.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetCardLinkListComponent setCssProperty(final String name,
                                                           final String value) {
        this.root.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public boolean isEditing() {
        return false;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.root.printTree(printer);
        }
        printer.outdent();
    }
}
