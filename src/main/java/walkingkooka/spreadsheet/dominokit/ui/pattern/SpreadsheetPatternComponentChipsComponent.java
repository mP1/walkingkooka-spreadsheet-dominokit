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

import elemental2.dom.Element;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.utils.DominoElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.Component;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A card that is dynamically updated with chips repreenting each of the components in the edited pattern.
 */
final class SpreadsheetPatternComponentChipsComponent implements Component<HTMLDivElement> {

    /**
     * Creates an empty {@link SpreadsheetPatternComponentChipsComponent}.
     */
    static SpreadsheetPatternComponentChipsComponent empty() {
        return new SpreadsheetPatternComponentChipsComponent();
    }

    private SpreadsheetPatternComponentChipsComponent() {
        this.parent = Card.create();
        this.tokenKinds = Lists.array();
        this.texts = Lists.array();

    }

    /**
     * This is called anytime the pattern text is changed, rebuilding chips.
     */
    void refresh(final SpreadsheetPattern pattern,
                 final String errorPattern,
                 final Consumer<String> setPatternText,
                 final SpreadsheetPatternComponentContext context) {
        final Card parent = this.parent.clearElement();

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

            // now build the chips
            int i = 0;

            for (final String text : texts) {
                final Chip chip = Chip.create(text)
                        .setId(
                                SpreadsheetPatternComponent.ID_PREFIX +
                                        i +
                                        SpreadsheetIds.CHIP
                        ).setRemovable(true)
                        .addOnRemoveListener(
                                this.onRemove(
                                        i,
                                        setPatternText,
                                        context
                                )
                        ).setMarginRight("5px");

                final Set<String> alternatives = tokenKinds.get(i)
                        .alternatives();

                if (false == alternatives.isEmpty()) {
                    final HistoryToken historyToken = context.historyToken();
                    SpreadsheetContextMenu contextMenu = SpreadsheetContextMenu.empty(
                            new DominoElement<Element>(chip.element()),
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
                                                SpreadsheetPatternComponent.ID_PREFIX + "alternative-" + j + SpreadsheetIds.MENU_ITEM,
                                                alternative
                                        )
                        );

                        j++;
                    }
                }
                parent.appendChild(chip);

                i++;
            }
        }

        // hide the card if there are no chips
        parent.setDisplay(
                texts.isEmpty() ?
                        "none" :
                        ""
        );
    }

    /**
     * This listener is fired when a chip is removed by clicking the X. It will recompute a new pattern and update the pattern text.
     */
    private Consumer<Chip> onRemove(final int index,
                                    final Consumer<String> setPatternText,
                                    final SpreadsheetPatternComponentContext context) {
        return (chip) -> {
            final String removed = this.texts.remove(index);
            context.debug(this.getClass().getSimpleName() + ".onRemove removed " + CharSequences.quoteAndEscape(removed));
            setPatternText.accept(
                    this.texts.stream()
                            .collect(Collectors.joining())
            );
        };
    }

    /**
     * THe parent holding all the current ui pattern chips.
     */
    private final Card parent;

    private final List<SpreadsheetFormatParserTokenKind> tokenKinds;

    private final List<String> texts;

    @Override
    public HTMLDivElement element() {
        return this.parent.element();
    }
}
