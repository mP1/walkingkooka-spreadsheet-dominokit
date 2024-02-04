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
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.menu.Menu;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.Component;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
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

        final List<String> patternComponentTexts = this.texts;
        patternComponentTexts.clear();

        // pattern will be null when pattern is empty
        if (null == pattern) {
            context.debug(this.getClass().getSimpleName() + ".refresh no components");
        } else {
            pattern.components(
                    (kind, tokenPatternText) -> {
                        tokenKinds.add(kind);
                        patternComponentTexts.add(tokenPatternText);
                    }
            );

            context.debug(this.getClass().getSimpleName() + ".refresh " + patternComponentTexts.size() + " text ", patternComponentTexts);

            if (false == errorPattern.isEmpty()) {
                patternComponentTexts.add(errorPattern);
            }

            // now build the chips
            int i = 0;

            for (final String patternComponentText : patternComponentTexts) {
                final Chip chip = Chip.create(patternComponentText)
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
                    final Menu<Void> menu = Menu.create();
                    SpreadsheetContextMenu contextMenu = SpreadsheetContextMenu.menu(
                            menu,
                            context
                    );

                    final int ii = i;
                    int j = 0;
                    int selectedItem = -1;
                    for (final String alternative : alternatives) {

                        final String newPattern = IntStream.range(0, patternComponentTexts.size())
                                .mapToObj(k -> ii == k ? alternative : patternComponentTexts.get(k))
                                .collect(Collectors.joining());

                        contextMenu = contextMenu.item(
                                SpreadsheetPatternComponent.ID_PREFIX + "alternative-" + j,
                                alternative,
                                historyToken.setSave(newPattern)
                        );

                        if (alternative.equalsIgnoreCase(patternComponentText)) {
                            selectedItem = j;
                        }

                        j++;
                    }

                    chip.setDropMenu(menu);
                    menu.selectAt(
                            selectedItem,
                            true // silent = dont fire selection events
                    );
                }
                parent.appendChild(chip);

                i++;
            }
        }
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
