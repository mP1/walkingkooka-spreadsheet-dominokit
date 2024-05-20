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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.flexlayout.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A card that is dynamically updated with links which remove an individual component of the pattern. A context-menu
 * for each link provides alternatives if any are available. An example of alternatives might be for a day within a date format pattern.
 * Day alternatives might be the numeric day without leading zeroes, the day of the week and numeric day and so variations like this.
 * <ol>
 *     <li>d</li>
 *     <li>dd</li>
 *     <li>ddd</li>
 *     <li>dddd</li>
 *     <li>ddddd</li>
 * </ol>
 */
final class SpreadsheetPatternComponentElementRemover implements HtmlElementComponent<HTMLDivElement, SpreadsheetPatternComponentElementRemover>,
        TreePrintable {

    /**
     * Creates an empty {@link SpreadsheetPatternComponentElementRemover}.
     */
    static SpreadsheetPatternComponentElementRemover empty() {
        return new SpreadsheetPatternComponentElementRemover();
    }

    private SpreadsheetPatternComponentElementRemover() {
        this.flex = SpreadsheetFlexLayout.row();
        this.root = SpreadsheetCard.empty()
                .setTitle("Remove individual component(s)")
                .appendChild(this.flex);
        this.tokenKinds = Lists.array();
        this.texts = Lists.array();
    }

    /**
     * This is called anytime the pattern text is changed, rebuilding remove links.
     */
    void refresh(final SpreadsheetPattern pattern,
                 final String errorPattern,
                 final SpreadsheetPatternDialogComponentContext context) {
        this.root.hide();
        final SpreadsheetFlexLayout flex = this.flex.removeAllChildren();

        final List<SpreadsheetFormatParserTokenKind> tokenKinds = this.tokenKinds;
        tokenKinds.clear();

        final List<String> texts = this.texts;
        texts.clear();

        // pattern will be null when pattern is empty
        if (null != pattern) {
            pattern.components(
                    (kind, tokenPatternText) -> {
                        tokenKinds.add(kind);
                        texts.add(tokenPatternText);
                    }
            );

            if (false == errorPattern.isEmpty()) {
                texts.add(errorPattern);
            }

            // now build the components
            int i = 0;

            for (final String text : texts) {
                final HistoryToken historyToken = context.historyToken();
                final String id = SpreadsheetPatternDialogComponent.ID_PREFIX + "remove-" + i;

                final List<String> removed = Lists.array();
                removed.addAll(texts);
                removed.remove(i);

                final HistoryTokenAnchorComponent patternElement = historyToken.link(id)
                        .setTextContent(text)
                        .setHistoryToken(
                                Optional.of(
                                        historyToken.setSave(
                                                String.join(
                                                        "",
                                                        removed
                                                ) // compute the pattern-text without this component.
                                )
                                )
                        );

                final Set<String> alternatives = tokenKinds.get(i)
                        .alternatives();

                if (false == alternatives.isEmpty()) {

                    SpreadsheetContextMenu contextMenu = SpreadsheetContextMenu.wrap(
                            patternElement,
                            context
                    );


                    final int ii = i;
                    int j = 0;
                    for (final String alternative : alternatives) {

                        final String newPattern = IntStream.range(0, texts.size())
                                .mapToObj(k -> ii == k ? alternative : texts.get(k))
                                .collect(Collectors.joining());

                        contextMenu = contextMenu.item(
                                historyToken.setSave(newPattern)
                                        .contextMenuItem(
                                                id + "-alt-" + j + SpreadsheetIds.MENU_ITEM,
                                                alternative
                                        )
                        );

                        j++;
                    }
                }
                flex.appendChild(patternElement);

                i++;
            }
        }

        if (false == texts.isEmpty()) {
            this.root.show();
        }
    }

    private final SpreadsheetCard root;

    /**
     * The parent holding LINKS which contain the pattern without a component.
     */
    private final SpreadsheetFlexLayout flex;

    private final List<SpreadsheetFormatParserTokenKind> tokenKinds;

    private final List<String> texts;

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetPatternComponentElementRemover setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.root.setCssText(css);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
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
