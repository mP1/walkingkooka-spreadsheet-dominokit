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

package walkingkooka.spreadsheet.dominokit.toolbar;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.HistoryTokenAwareComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class SpreadsheetToolbarComponentTest implements HistoryTokenAwareComponentLifecycleTesting<SpreadsheetToolbarComponent>,
        SpreadsheetMetadataTesting {

    // id...............................................................................................................

    @Test
    public void testIdWithoutValue() {
        this.idAndCheck(
                TextStylePropertyName.COLOR,
                Optional.empty(),
                "toolbar-color"
        );
    }

    @Test
    public void testIdWithValue() {
        this.idAndCheck(
                TextStylePropertyName.COLOR,
                Optional.of(Color.BLACK),
                "toolbar-color-#000000"
        );
    }

    @Test
    public void testIdWithValue2() {
        this.idAndCheck(
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.CENTER),
                "toolbar-text_align-CENTER"
        );
    }

    private <T> void idAndCheck(final TextStylePropertyName<T> property,
                                final Optional<T> value,
                                final String expected) {
        this.checkEquals(
                expected,
                SpreadsheetToolbarComponent.id(
                        property,
                        value
                )
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testSpreadsheetList() {
        final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();

        final AppContext context = this.appContext(
                watchers,
                "/"
        );

        // toolbar shoould be empty (hidden)
        this.onHistoryTokenChangeAndCheck(
                SpreadsheetToolbarComponent.with(context),
                watchers,
                context,
                "SpreadsheetToolbarComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    ROW\n"
        );
    }

    @Test
    public void testCell() {
        final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();

        final AppContext context = appContext(
                watchers,
                "/1/Spreadsheet123/cell/A1:B2"
        );

        this.onHistoryTokenChangeAndCheck(
                SpreadsheetToolbarComponent.with(context),
                watchers,
                context,
                "SpreadsheetToolbarComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    ROW\n" +
                        "      mdi-format-bold \"Bold\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/font-weight/save/bold] id=toolbar-font_weight-BOLD-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Bold\"\n" +
                        "      mdi-format-italic \"Italics\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/font-style/save/ITALIC] id=toolbar-font_style-ITALIC-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Italics\"\n" +
                        "      mdi-format-strikethrough \"Strike\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/text-decoration-line/save/LINE_THROUGH] id=toolbar-text_decoration_line-LINE_THROUGH-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Strike-thru\"\n" +
                        "      mdi-format-underline \"Underline\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/text-decoration-line/save/UNDERLINE] id=toolbar-text_decoration_line-UNDERLINE-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Underline\"\n" +
                        "      mdi-format-align-left \"Left\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/text-align/save/LEFT] id=toolbar-text_align-LEFT-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Left align\"\n" +
                        "      mdi-format-align-center \"Center\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/text-align/save/CENTER] id=toolbar-text_align-CENTER-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Center align\"\n" +
                        "      mdi-format-align-right \"Right\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/text-align/save/RIGHT] id=toolbar-text_align-RIGHT-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Right align\"\n" +
                        "      mdi-format-align-justify \"Justify\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/text-align/save/JUSTIFY] id=toolbar-text_align-JUSTIFY-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Justify\"\n" +
                        "      mdi-format-align-top \"Top\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/vertical-align/save/TOP] id=toolbar-vertical_align-TOP-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Align top\"\n" +
                        "      mdi-format-align-middle \"Middle\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/vertical-align/save/MIDDLE] id=toolbar-vertical_align-MIDDLE-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Align middle\"\n" +
                        "      mdi-format-align-bottom \"Bottom\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/vertical-align/save/BOTTOM] id=toolbar-vertical_align-BOTTOM-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Align bottom\"\n" +
                        "      mdi-format-letter-case \"Capitalize\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/text-transform/save/CAPITALIZE] id=toolbar-text_transform-CAPITALIZE-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Capitalize text\"\n" +
                        "      mdi-format-letter-case-lower \"Lower-case\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/text-transform/save/LOWERCASE] id=toolbar-text_transform-LOWERCASE-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Lower-case text\"\n" +
                        "      mdi-format-letter-case-lower \"Upper-case\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/text-transform/save/UPPERCASE] id=toolbar-text_transform-UPPERCASE-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Upper-case text\"\n" +
                        "      mdi-format-text \"Formatting\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/formatter] id=toolbar-formatter-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Formatting...\"\n" +
                        "      mdi-format-text \"Parsing\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/parser] id=toolbar-parser-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Parser(s)...\"\n" +
                        "      mdi-format-clear \"Clear styling\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/style/*/save/] CHECKED id=toolbar-*-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Clear styling\"\n" +
                        "      mdi-star \"Hide Zeros\" [#/1/Spreadsheet123/metadata/hide-zero-values/save/true] id=toolbar-hide-zero-values-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Show Zero Values\"\n" +
                        "      mdi-magnify \"Find\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/find] id=toolbar-find-cells-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Find cells...\"\n" +
                        "      mdi-spotlight-beam \"Highlight\" [#/1/Spreadsheet123/metadata/find-highlighting/save/true] id=toolbar-find-highlight-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Enable cell highlighting\"\n" +
                        "      mdi-sort \"Sort\" [#/1/Spreadsheet123/cell/A1:B2/bottom-right/sort/edit] id=toolbar-sort-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Sort cell(s), column(s), row(s)...\"\n" +
                        "      mdi-flag-checkered \"Create Label\" [#/1/Spreadsheet123/label] id=toolbar-label-create-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Create Label\"\n" +
                        "      mdi-reload \"Reload\" [#/1/Spreadsheet123/reload] id=toolbar-reload-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Reload\"\n" +
                        "      \"Swagger\" [/api-doc/index.html] _blank id=toolbar-swagger-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Click to open swagger html client\"\n"
        );
    }

    @Test
    public void testColumn() {
        final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();

        final AppContext context = appContext(
                watchers,
                "/1/Spreadsheet123/column/B:C"
        );

        this.onHistoryTokenChangeAndCheck(
                SpreadsheetToolbarComponent.with(context),
                watchers,
                context,
                "SpreadsheetToolbarComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    ROW\n" +
                        "      mdi-star \"Hide Zeros\" [#/1/Spreadsheet123/metadata/hide-zero-values/save/true] id=toolbar-hide-zero-values-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Show Zero Values\"\n" +
                        "      mdi-magnify \"Find\" [#/1/Spreadsheet123/cell/*/bottom-right/find] id=toolbar-find-cells-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Find cells...\"\n" +
                        "      mdi-spotlight-beam \"Highlight\" [#/1/Spreadsheet123/metadata/find-highlighting/save/true] id=toolbar-find-highlight-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Enable cell highlighting\"\n" +
                        "      mdi-sort \"Sort\" [#/1/Spreadsheet123/column/B:C/right/sort/edit] id=toolbar-sort-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Sort cell(s), column(s), row(s)...\"\n" +
                        "      mdi-flag-checkered \"Create Label\" [#/1/Spreadsheet123/label] id=toolbar-label-create-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Create Label\"\n" +
                        "      mdi-reload \"Reload\" [#/1/Spreadsheet123/reload] id=toolbar-reload-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Reload\"\n" +
                        "      \"Swagger\" [/api-doc/index.html] _blank id=toolbar-swagger-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Click to open swagger html client\"\n"
        );
    }

    @Test
    public void testRow() {
        final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();

        final AppContext context = appContext(
                watchers,
                "/1/Spreadsheet123/row/3:4"
        );

        this.onHistoryTokenChangeAndCheck(
                SpreadsheetToolbarComponent.with(context),
                watchers,
                context,
                "SpreadsheetToolbarComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    ROW\n" +
                        "      mdi-star \"Hide Zeros\" [#/1/Spreadsheet123/metadata/hide-zero-values/save/true] id=toolbar-hide-zero-values-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Show Zero Values\"\n" +
                        "      mdi-magnify \"Find\" [#/1/Spreadsheet123/cell/*/bottom-right/find] id=toolbar-find-cells-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Find cells...\"\n" +
                        "      mdi-spotlight-beam \"Highlight\" [#/1/Spreadsheet123/metadata/find-highlighting/save/true] id=toolbar-find-highlight-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Enable cell highlighting\"\n" +
                        "      mdi-sort \"Sort\" [#/1/Spreadsheet123/row/3:4/bottom/sort/edit] id=toolbar-sort-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Sort cell(s), column(s), row(s)...\"\n" +
                        "      mdi-flag-checkered \"Create Label\" [#/1/Spreadsheet123/label] id=toolbar-label-create-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Create Label\"\n" +
                        "      mdi-reload \"Reload\" [#/1/Spreadsheet123/reload] id=toolbar-reload-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Reload\"\n" +
                        "      \"Swagger\" [/api-doc/index.html] _blank id=toolbar-swagger-Link\n" +
                        "        SpreadsheetTooltipComponent\n" +
                        "          \"Click to open swagger html client\"\n"
        );
    }

    private AppContext appContext(final HistoryTokenWatchers watchers,
                                  final String historyToken) {
        return new FakeAppContext() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return watchers.add(watcher);
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString(historyToken);
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                        SpreadsheetId.with(1)
                ).set(
                        SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
                        SpreadsheetName.with("Spreadsheet123")
                );
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);
                cache.setWindows(
                        SpreadsheetViewportWindows.parse("A1:J10")
                );
                return cache;
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetToolbarComponent> type() {
        return SpreadsheetToolbarComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
