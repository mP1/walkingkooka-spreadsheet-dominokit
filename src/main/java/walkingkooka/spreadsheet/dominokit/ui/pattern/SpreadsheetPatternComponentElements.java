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
import org.dominokit.domino.ui.utils.DominoElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuNative;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A card that is dynamically updated with links which remove an individual component of the pattern. A context-menu
 * for each link provides alternatives.
 */
final class SpreadsheetPatternComponentElements implements HtmlElementComponent<HTMLDivElement, SpreadsheetPatternComponentElements> {

    /**
     * Creates an empty {@link SpreadsheetPatternComponentElements}.
     */
    static SpreadsheetPatternComponentElements empty() {
        return new SpreadsheetPatternComponentElements();
    }

    private SpreadsheetPatternComponentElements() {
        this.parent = SpreadsheetCard.empty();
        this.tokenKinds = Lists.array();
        this.texts = Lists.array();
    }

    /**
     * This is called anytime the pattern text is changed, rebuilding remove links.
     */
    void refresh(final SpreadsheetPattern pattern,
                 final String errorPattern,
                 final SpreadsheetPatternDialogComponentContext context) {
        final SpreadsheetCard parent = this.parent.clear();

        final List<SpreadsheetFormatParserTokenKind> tokenKinds = this.tokenKinds;
        tokenKinds.clear();

        final List<String> texts = this.texts;
        texts.clear();

        // pattern will be null when pattern is empty
        if (null == pattern) {
            context.debug(this.getClass().getSimpleName() + ".refresh no components");
        } else {
            pattern.components(
                    (kind, tokenPatternText) -> {
                        tokenKinds.add(kind);
                        texts.add(tokenPatternText);
                    }
            );

            context.debug(this.getClass().getSimpleName() + ".refresh " + texts.size() + " text ", texts);

            if (false == errorPattern.isEmpty()) {
                texts.add(errorPattern);
            }

            // now build the components
            int i = 0;

            for (final String text : texts) {
                final HistoryToken historyToken = context.historyToken();
                final String idPrefix = SpreadsheetPatternDialogComponent.ID_PREFIX + i;

                final List<String> removed = Lists.array();
                removed.addAll(texts);
                removed.remove(i);

                final HistoryTokenAnchorComponent patternElement = historyToken.link(idPrefix + "-component")
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

                    SpreadsheetContextMenu contextMenu = SpreadsheetContextMenuNative.empty(
                            new DominoElement<>(patternElement.element()),
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
                                                SpreadsheetPatternDialogComponent.ID_PREFIX + "alternative-" + j + SpreadsheetIds.MENU_ITEM,
                                                alternative
                                        )
                        );

                        j++;
                    }
                }
                parent.append(patternElement);

                i++;
            }
        }
    }

    /**
     * THe parent holding all the current ui pattern chips.
     */
    private final SpreadsheetCard parent;

    private final List<SpreadsheetFormatParserTokenKind> tokenKinds;

    private final List<String> texts;

    @Override
    public HTMLDivElement element() {
        return this.parent.element();
    }
}
