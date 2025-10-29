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

package walkingkooka.spreadsheet.dominokit.locale;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public final class LocaleDialogComponentTest implements DialogComponentLifecycleTesting<LocaleDialogComponent>,
    HistoryTokenTesting,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetMetadata METADATA = METADATA_EN_AU.set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
        SPREADSHEET_ID
    );

    // onHistoryToken...................................................................................................

    @Test
    public void testOnHistoryTokenWithSpreadsheetCellLocaleSelectHistoryTokenMissingLocale() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        final AppContext context = this.appContext(
            HistoryToken.cellLocaleSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                cell.setDefaultAnchor()
            ),
            Optional.empty() // no locale
        );

        context.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                METADATA,
                context
            );

        context.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell"),
                SpreadsheetDelta.EMPTY.setCells(
                    Sets.of(
                        cell.setFormula(SpreadsheetFormula.EMPTY) // no locale
                    )
                ),
                context
            );

        this.onHistoryTokenChangeAndCheck2(
            LocaleDialogComponent.with(
                LocaleDialogComponentContexts.appContextCellLocale(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            context,
            "LocaleDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Locale\n" +
                "    id=Locale-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          LocaleComponent\n" +
                "            SuggestBoxComponent\n" +
                "              []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName1/cell/A1/locale/save/] id=Locale-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/cell/A1/locale/save/] id=Locale-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/cell/A1/locale/save/] id=Locale-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=Locale-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenWithSpreadsheetCellLocaleSelectHistoryTokenWithLocale() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        final AppContext context = this.appContext(
            HistoryToken.cellLocaleSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                cell.setDefaultAnchor()
            ),
            Optional.of(LOCALE)
        );

        context.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                METADATA,
                context
            );

        context.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell"),
                SpreadsheetDelta.EMPTY.setCells(
                    Sets.of(
                        cell.setFormula(SpreadsheetFormula.EMPTY)
                            .setLocale(
                                Optional.of(LOCALE)
                            )
                    )
                ),
                context
            );

        this.onHistoryTokenChangeAndCheck2(
            LocaleDialogComponent.with(
                LocaleDialogComponentContexts.appContextCellLocale(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                cell.setDefaultAnchor()
            ),
            context,
            "LocaleDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Locale\n" +
                "    id=Locale-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          LocaleComponent\n" +
                "            SuggestBoxComponent\n" +
                "              [English (Australian)]\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName1/cell/A1/locale/save/en-AU] id=Locale-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/cell/A1/locale/save/] id=Locale-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/cell/A1/locale/save/en-AU] id=Locale-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=Locale-close-Link\n"
        );
    }

    @Override
    public LocaleDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return LocaleDialogComponent.with(
            LocaleDialogComponentContexts.appContextSpreadsheetMetadataLocale(
                this.appContext(
                    historyToken,
                    null
                )
            )
        );
    }

    private AppContext appContext(final HistoryToken historyToken,
                                  final Optional<Locale> locale) {
        return new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public void giveFocus(final Runnable focus) {
                // ignore
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.spreadsheetViewportCache;
            }

            private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(this);

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return METADATA.setOrRemove(
                    SpreadsheetMetadataPropertyName.LOCALE,
                    locale.orElse(Locale.FRANCE)
                );
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Set<Locale> availableLocales() {
                return Sets.of(
                    LOCALE,
                    Locale.ENGLISH,
                    Locale.FRENCH
                );
            }

            @Override
            public Optional<String> localeText(final Locale locale) {
                if (locale.equals(LOCALE)) {
                    return Optional.of("English (Australian)");
                }
                if (locale.equals(Locale.ENGLISH)) {
                    return Optional.of("English");
                }
                if (locale.equals(Locale.FRENCH)) {
                    return Optional.of("French");
                }
                return Optional.empty();
            }

            @Override
            public void debug(final Object... values) {
                System.out.println(
                    Arrays.toString(values)
                );
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<LocaleDialogComponent> type() {
        return LocaleDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
