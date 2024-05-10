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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.format.parser.SpreadsheetFormatParserTokenKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A card that contains many links that append a component to the pattern being edited.
 */
final class SpreadsheetPatternComponentElementAppender implements HtmlElementComponent<HTMLDivElement, SpreadsheetPatternComponentElementAppender>,
        TreePrintable {

    /**
     * Creates an empty {@link SpreadsheetPatternComponentElementAppender}.
     */
    static SpreadsheetPatternComponentElementAppender empty() {
        return new SpreadsheetPatternComponentElementAppender();
    }

    private SpreadsheetPatternComponentElementAppender() {
        this.parent = SpreadsheetCard.empty();
        this.links = Lists.array();
    }

    /**
     * Uses the current {@link SpreadsheetPatternKind} to recreates all links for each and every pattern for each and every {@link SpreadsheetFormatParserTokenKind}.
     * Note a few {@link SpreadsheetFormatParserTokenKind} are skipped for now for technical and other reasons.
     */
    void recreate(final Consumer<String> setPatternText,
                  final SpreadsheetPatternDialogComponentContext context) {
        context.debug(this.getClass().getSimpleName() + ".recreate");

        final SpreadsheetCard parent = this.parent.clear();
        final List<SpreadsheetPatternComponentElementAppenderLink> links = this.links;
        links.clear();

        for (final SpreadsheetFormatParserTokenKind formatParserTokenKind : context.patternKind().spreadsheetFormatParserTokenKinds()) {

            switch (formatParserTokenKind) {
                case COLOR_NAME:
                case COLOR_NUMBER:
                    break; // skip for now insert color pick instead
                case CONDITION:
                    break;
                case GENERAL:
                    break; // skip GENERAL for now
                case TEXT_LITERAL:
                    break; // skip - let the user insert the text literal into the patternTextBox

                default:
                    for (final String pattern : formatParserTokenKind.patterns()) {
                        final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.empty()
                                .setId(
                                        SpreadsheetPatternDialogComponent.ID_PREFIX +
                                                "append-" +
                                                pattern +
                                                SpreadsheetIds.LINK
                                ).setTextContent(pattern);
                        anchor.addClickAndKeydownEnterListener(
                                (e) -> {
                                    e.preventDefault();
                                    setPatternText.accept(
                                            anchor.historyToken()
                                                    .map(t -> t.pattern()
                                                            .map(SpreadsheetPattern::text)
                                                            .orElse("")
                                                    ).orElse("")
                                    );
                                }
                        );
                        links.add(
                                SpreadsheetPatternComponentElementAppenderLink.with(
                                        formatParserTokenKind,
                                        pattern,
                                        anchor
                                )
                        );
                        parent.appendChild(anchor);
                    }
                    break;
            }
        }
    }

    /**
     * This should be invoked each time the pattern text is updated, and will update the HREF for each append link.
     */
    void refreshLinks(final String patternText,
                      final SpreadsheetPattern pattern,
                      final SpreadsheetPatternDialogComponentContext context) {

        final HistoryToken historyToken = context.historyToken();

        final List<SpreadsheetPatternComponentElementAppenderLink> links = this.links;
        context.debug(this.getClass().getSimpleName() + ".refreshLinks " + links.size() + " links patternText: " + CharSequences.quoteAndEscape(patternText));

        for (final SpreadsheetPatternComponentElementAppenderLink link : links) {
            String savePatternText = null;

            if (patternText.isEmpty()) {
                savePatternText = link.pattern;
            } else {
                if (null != pattern) {
                    // get last SpreadsheetFormatPatternKind
                    final SpreadsheetFormatParserTokenKind[] lastPatternKind = new SpreadsheetFormatParserTokenKind[1];
                    final String[] lastPatternText = new String[1];

                    pattern.components(
                            (kk, tt) -> {
                                lastPatternKind[0] = kk;
                                lastPatternText[0] = tt;
                            }
                    );

                    savePatternText = patternText;

                    // this exists so if a pattern text ends in "d" then "dd" should replace the "d" not append and make it "ddd".
                    if (lastPatternKind[0].isDuplicate(link.kind)) {
                        // replace last
                        savePatternText = patternText.substring(
                                0,
                                patternText.length() - lastPatternText[0].length()
                        ) + link.pattern;
                    } else {
                        savePatternText = savePatternText + link.pattern;
                    }
                }
            }

            HistoryToken save = null;
            if (null != savePatternText) {
                try {
                    save = historyToken.setSave(savePatternText);
                } catch (final RuntimeException invalidPattern) {
                    // ignore save is already null
                }
            }

            link.anchor.setHistoryToken(
                    Optional.ofNullable(save)
            );
        }
    }

    /**
     * THe {@link SpreadsheetCard} holding all the links.
     */
    private final SpreadsheetCard parent;

    /**
     * A cache of a single pattern from a {@link SpreadsheetFormatParserTokenKind} to its matching ANCHOR.
     * This is kept to support updates o the ANCHOR link as the pattern text box changes.
     */
    private final List<SpreadsheetPatternComponentElementAppenderLink> links;

    @Override
    public HTMLDivElement element() {
        return this.parent.element();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.parent.printTree(printer);
        printer.lineStart();
    }
}
