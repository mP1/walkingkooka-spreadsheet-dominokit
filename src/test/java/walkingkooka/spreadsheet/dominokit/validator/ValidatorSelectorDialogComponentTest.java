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

package walkingkooka.spreadsheet.dominokit.validator;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.validation.form.SpreadsheetForms;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Arrays;
import java.util.Optional;

public final class ValidatorSelectorDialogComponentTest implements DialogComponentLifecycleTesting<ValidatorSelectorDialogComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testOnHistoryTokenChange() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.cellValidatorSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final ValidatorSelectorDialogComponent dialog = ValidatorSelectorDialogComponent.with(
            new TestValidatorSelectorDialogComponentContext(context) {
                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public Optional<ValidatorSelector> undo() {
                    return Optional.of(
                        ValidatorSelector.parse("hello-validator")
                    );
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return context.spreadsheetViewportCache();
                }
            }
        );

        dialog.onHistoryTokenChange(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            context
        );
        dialog.refresh(context);

        this.treePrintAndCheck(
            dialog,
            "ValidatorSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Validator Title123\n" +
                "    id=ValidatorSelector-Dialog includeClose=true\n" +
                "      ValidatorSelectorNameAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              id=ValidatorSelector-links\n" +
                "                \"Validator 1\" [#/1/SpreadsheetName1/cell/A1/validator/save/validator-1] id=ValidatorSelector-validator-1-Link\n" +
                "                \"Validator 2\" [#/1/SpreadsheetName1/cell/A1/validator/save/validator-2] id=ValidatorSelector-validator-2-Link\n" +
                "                \"Validator 3\" [#/1/SpreadsheetName1/cell/A1/validator/save/validator-3] id=ValidatorSelector-validator-3-Link\n" +
                "        ValidatorSelectorComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              [hello-validator] id=ValidatorSelector-TextBox\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/1/SpreadsheetName1/cell/A1/validator/save/hello-validator] id=ValidatorSelector-save-Link\n" +
                "              \"Clear\" [#/1/SpreadsheetName1/cell/A1/validator/save/] id=ValidatorSelector-clear-Link\n" +
                "              \"Undo\" [#/1/SpreadsheetName1/cell/A1/validator/save/hello-validator] id=ValidatorSelector-undo-Link\n" +
                "              \"Close\" [#/1/SpreadsheetName1/cell/A1] id=ValidatorSelector-close-Link\n"
        );
    }

    @Test
    public void testOnSpreadsheetDeltaWhenCellWithError() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.cellValidatorSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        context.metadataWatchers.onSpreadsheetMetadata(
            context.spreadsheetMetadata(),
            context
        );

        final ValidatorSelectorDialogComponent dialog = ValidatorSelectorDialogComponent.with(
            new TestValidatorSelectorDialogComponentContext(context) {
                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public Optional<ValidatorSelector> undo() {
                    return Optional.of(
                        ValidatorSelector.parse("hello-validator")
                    );
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return context.spreadsheetViewportCache();
                }
            }
        );

        dialog.onHistoryTokenChange(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            context
        );
        dialog.refresh(context);

        context.deltaWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/1/cell/A1"),
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setError(
                            SpreadsheetError.validationErrors(
                                Lists.of(
                                    SpreadsheetForms.error(SpreadsheetSelection.A1)
                                        .setMessage("Validator Fail Message 123")
                                )
                            )
                        )
                    )
                )
            ),
            context
        );

        this.treePrintAndCheck(
            dialog,
            "ValidatorSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Validator Title123\n" +
                "    id=ValidatorSelector-Dialog includeClose=true\n" +
                "      ValidatorSelectorNameAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              id=ValidatorSelector-links\n" +
                "                \"Validator 1\" [#/1/SpreadsheetName1/cell/A1/validator/save/validator-1] id=ValidatorSelector-validator-1-Link\n" +
                "                \"Validator 2\" [#/1/SpreadsheetName1/cell/A1/validator/save/validator-2] id=ValidatorSelector-validator-2-Link\n" +
                "                \"Validator 3\" [#/1/SpreadsheetName1/cell/A1/validator/save/validator-3] id=ValidatorSelector-validator-3-Link\n" +
                "        ValidatorSelectorComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              [hello-validator] id=ValidatorSelector-TextBox\n" +
                "              Errors\n" +
                "                Validator Fail Message 123\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/1/SpreadsheetName1/cell/A1/validator/save/hello-validator] id=ValidatorSelector-save-Link\n" +
                "              \"Clear\" [#/1/SpreadsheetName1/cell/A1/validator/save/] id=ValidatorSelector-clear-Link\n" +
                "              \"Undo\" [#/1/SpreadsheetName1/cell/A1/validator/save/hello-validator] id=ValidatorSelector-undo-Link\n" +
                "              \"Close\" [#/1/SpreadsheetName1/cell/A1] id=ValidatorSelector-close-Link\n"
        );
    }

    private static class TestValidatorSelectorDialogComponentContext extends FakeValidatorSelectorDialogComponentContext {

        TestValidatorSelectorDialogComponentContext(final AppContext context) {
            this.context = context;
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return () -> {
            };
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
        }

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return this.context.spreadsheetMetadata();
        }

        private final AppContext context;

        @Override
        public String dialogTitle() {
            return "Validator Title123";
        }
    }

    final static class TestAppContext extends FakeAppContext {

        TestAppContext(final HistoryToken historyToken) {
            this.historyToken = historyToken;
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        private final HistoryToken historyToken;

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return null;
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.deltaWatchers.addSpreadsheetDeltaFetcherWatcher(watcher);
        }

        final SpreadsheetDeltaFetcherWatchers deltaWatchers = SpreadsheetDeltaFetcherWatchers.empty();

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return this.metadataWatchers.addSpreadsheetMetadataFetcherWatcher(watcher);
        }

        final SpreadsheetMetadataFetcherWatchers metadataWatchers = SpreadsheetMetadataFetcherWatchers.empty();

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                SpreadsheetId.with(1)
            ).set(
                SpreadsheetMetadataPropertyName.VALIDATION_VALIDATORS,
                ValidatorAliasSet.parse("validator-1, validator-2, validator-3")
            );
        }

        @Override
        public void giveFocus(final Runnable focus) {
            // NOP
        }

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return this.cache;
        }

        private final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

        @Override
        public void debug(final Object... values) {
            System.out.println("DEBUG: " + Arrays.toString(values));
        }

        @Override
        public void error(final Object... values) {
            System.out.println("ERROR: " + Arrays.toString(values));
        }
    }

    @Override
    public ValidatorSelectorDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return ValidatorSelectorDialogComponent.with(
            ValidatorSelectorDialogComponentContexts.appContext(
                new TestAppContext(historyToken)
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<ValidatorSelectorDialogComponent> type() {
        return ValidatorSelectorDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
