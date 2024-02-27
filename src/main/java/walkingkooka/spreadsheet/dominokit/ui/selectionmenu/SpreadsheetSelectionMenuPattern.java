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

package walkingkooka.spreadsheet.dominokit.ui.selectionmenu;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * Builds the context menu for format patterns.
 */
abstract class SpreadsheetSelectionMenuPattern<P extends SpreadsheetPattern> {

    SpreadsheetSelectionMenuPattern(final HistoryToken historyToken,
                                    final Locale locale,
                                    final List<P> recents,
                                    final String idPrefix,
                                    final Optional<P> pattern) {
        this.historyToken = historyToken.clearAction();
        this.locale = locale;

        this.recents = recents;
        this.idPrefix = idPrefix;
        this.pattern = pattern;
    }

    void build(final SpreadsheetContextMenu menu) {
        this.date(
                menu.subMenu(
                        this.idPrefix + "date" + SpreadsheetIds.SUB_MENU,
                        "Date"
                )
        );
        this.dateTime(
                menu.subMenu(
                        this.idPrefix + "datetime" + SpreadsheetIds.SUB_MENU,
                        "Date Time"
                )
        );
        this.number(
                menu.subMenu(
                        this.idPrefix + "number" + SpreadsheetIds.SUB_MENU,
                        "Number"
                )
        );

        if (this.isFormat()) {
            this.text(
                    menu.subMenu(
                            this.idPrefix + "text" + SpreadsheetIds.SUB_MENU,
                            "Text"
                    )
            );
        }
        this.time(
                menu.subMenu(
                        this.idPrefix + "time" + SpreadsheetIds.SUB_MENU,
                        "Time"
                )
        );

        menu.separator();

        this.removeHistoryToken(
                menu
        );


        this.buildRecents(menu);
    }

    private void date(final SpreadsheetContextMenu menu) {
        this.dateMenuItem(
                "short",
                "Short",
                DateFormat.SHORT,
                menu
        );
        this.dateMenuItem(
                "medium",
                "Medium",
                DateFormat.MEDIUM,
                menu
        );
        this.dateMenuItem(
                "long",
                "Long",
                DateFormat.LONG,
                menu
        );
        this.dateMenuItem(
                "full",
                "Full",
                DateFormat.FULL,
                menu
        );
        this.edit(
                menu,
                this.editDatePatternKind()
        );
    }

    private void dateMenuItem(final String id,
                              final String label,
                              final int style,
                              final SpreadsheetContextMenu menu) {
        final P pattern = this.datePattern(style);

        menu.item(
                this.historyToken.setPattern(pattern)
                        .contextMenuItem(
                                this.idPrefix + "date-" + id + SpreadsheetIds.MENU_ITEM,
                                label + " " + pattern
                        ).checked(
                                this.checked(pattern)
                        )
        );
    }

    abstract P datePattern(final int style);


    abstract SpreadsheetPatternKind editDatePatternKind();

    private void dateTime(final SpreadsheetContextMenu menu) {
        this.dateTimeMenuItem(
                "short",
                "Short",
                DateFormat.SHORT,
                menu
        );
        this.dateTimeMenuItem(
                "medium",
                "Medium",
                DateFormat.MEDIUM,
                menu
        );
        this.dateTimeMenuItem(
                "long",
                "Long",
                DateFormat.LONG,
                menu
        );
        this.dateTimeMenuItem(
                "full",
                "Full",
                DateFormat.FULL,
                menu
        );
        this.edit(
                menu,
                this.editDateTimePatternKind()
        );
    }

    private void dateTimeMenuItem(final String id,
                                  final String label,
                                  final int style,
                                  final SpreadsheetContextMenu menu) {
        final P pattern = this.datePattern(style);

        menu.item(
                this.historyToken.setPattern(pattern)
                        .contextMenuItem(
                                this.idPrefix + "datetime-" + id + SpreadsheetIds.MENU_ITEM,
                                label + " " + pattern
                        ).checked(
                                this.checked(pattern)
                        )
        );
    }

    abstract SpreadsheetPattern dateTimePattern(final int style);

    abstract SpreadsheetPatternKind editDateTimePatternKind();

    private void number(final SpreadsheetContextMenu menu) {
        if (this.isFormat()) {
            this.numberMenuItemGeneral(
                    menu
            );
        }
        this.numberMenuItem(
                "number",
                "Number",
                DecimalFormat::getInstance,
                menu
        );
        this.numberMenuItem(
                "integer",
                "Integer",
                DecimalFormat::getIntegerInstance,
                menu
        );
        this.numberMenuItem(
                "percent",
                "Percent",
                DecimalFormat::getPercentInstance,
                menu
        );
        this.numberMenuItem(
                "currency",
                "Currency",
                DecimalFormat::getCurrencyInstance,
                menu
        );
        this.edit(
                menu,
                this.editNumberPatternKind()
        );
    }

    private final static SpreadsheetFormatPattern GENERAL = SpreadsheetPattern.parseNumberFormatPattern("General");

    private void numberMenuItemGeneral(final SpreadsheetContextMenu menu) {
        this.numberMenuItem(
                "general",
                "General",
                SpreadsheetPattern.numberFormatPattern(
                        GENERAL.toFormat()
                                .value()
                ),
                menu
        );
    }

    abstract SpreadsheetPatternKind editNumberPatternKind();

    private void numberMenuItem(final String id,
                                final String label,
                                final Function<Locale, NumberFormat> decimalFormat,
                                final SpreadsheetContextMenu menu) {
        this.numberMenuItem(
                id,
                label,
                this.numberPattern(decimalFormat),
                menu
        );
    }

    abstract P numberPattern(final Function<Locale, NumberFormat> decimalFormat);

    private void numberMenuItem(final String id,
                                final String label,
                                final SpreadsheetPattern pattern,
                                final SpreadsheetContextMenu menu) {
        menu.item(
                this.historyToken.setPattern(pattern)
                        .contextMenuItem(
                                this.idPrefix + "number-" + id + SpreadsheetIds.MENU_ITEM,
                                label + " " + pattern
                        ).checked(
                                this.checked(pattern)
                        )
        );
    }

    private void text(final SpreadsheetContextMenu menu) {
        final SpreadsheetFormatPattern pattern = SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN;

        menu.item(
                this.historyToken.setPattern(
                        pattern
                ).contextMenuItem(
                        this.idPrefix + "text-default" + SpreadsheetIds.MENU_ITEM,
                        "Default " + SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                ).checked(
                        this.checked(pattern)
                )
        );

        this.edit(
                menu,
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN
        );
    }

    private void time(final SpreadsheetContextMenu menu) {
        this.timeMenuItem(
                "short",
                "Short",
                DateFormat.SHORT,
                menu
        );
        this.timeMenuItem(
                "medium",
                "Medium",
                DateFormat.MEDIUM,
                menu
        );
        this.timeMenuItem(
                "long",
                "Long",
                DateFormat.LONG,
                menu
        );
        this.timeMenuItem(
                "full",
                "Full",
                DateFormat.FULL,
                menu
        );
        this.edit(
                menu,
                this.editTimePatternKind()
        );
    }

    private void timeMenuItem(final String id,
                              final String label,
                              final int style,
                              final SpreadsheetContextMenu menu) {
        final P pattern = this.timePattern(style);

        menu.item(
                this.historyToken.setPattern(pattern)
                        .contextMenuItem(
                                this.idPrefix + "time-" + id + SpreadsheetIds.MENU_ITEM,
                                label + " " + pattern
                        ).checked(
                                this.checked(pattern)
                        )
        );
    }

    abstract SpreadsheetPatternKind editTimePatternKind();

    abstract P timePattern(final int style);

    /**
     * Adds menu items which will save each of the most recent {@link SpreadsheetPattern}.
     */
    private void buildRecents(final SpreadsheetContextMenu menu) {
        final HistoryToken token = this.historyToken;
        int i = 0;

        for (final P recent : this.recents) {
            if (0 == i) {
                menu.separator();
            }

            final String text = recent.text();

            menu.item(
                    token.setPattern(recent)
                            .contextMenuItem(
                                    this.idPrefix + "recent-" + i + SpreadsheetIds.MENU_ITEM, // id
                                    text
                            )
            );

            i++;
        }
    }

    private void edit(final SpreadsheetContextMenu menu,
                      final SpreadsheetPatternKind kind) {
        menu.item(
                this.historyToken.setPatternKind(
                        Optional.of(kind)
                ).contextMenuItem(
                        this.idPrefix + "edit" + SpreadsheetIds.MENU_ITEM,
                        "Edit..."
                )
        );
    }

    abstract boolean isFormat();

    /**
     * If the cell selection has a format/parse pattern add a remove menu item followed by a separator.
     */
    private void removeHistoryToken(final SpreadsheetContextMenu menu) {
        final Optional<P> pattern = this.pattern;
        if (pattern.isPresent()) {
            menu.item(
                    this.historyToken.setPattern(
                                    pattern.get()
                            ).setDelete()
                            .contextMenuItem(
                                    this.idPrefix + "remove-" + (this.isFormat() ? "format" : "parse") + SpreadsheetIds.MENU_ITEM, // id
                                    "Remove" // text
                            ).icon(
                                    this.removeIcon()
                            )
            );

            menu.separator();
        }
    }

    abstract Optional<Icon<?>> removeIcon();

    final HistoryToken historyToken;

    final Locale locale;

    /**
     * Holds the most recent {@link SpreadsheetPattern}, these will fill a most recent patterns.
     */
    private final List<P> recents;

    private final String idPrefix;

    private boolean checked(final SpreadsheetPattern pattern) {
        return pattern.equals(
                this.pattern.orElse(null)
        );
    }

    private final Optional<P> pattern;

    @Override
    public final String toString() {
        return this.historyToken + " " + this.locale;
    }
}
