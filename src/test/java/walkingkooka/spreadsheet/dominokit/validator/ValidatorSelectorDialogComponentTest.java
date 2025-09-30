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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Optional;

public final class ValidatorSelectorDialogComponentTest implements DialogComponentLifecycleTesting<ValidatorSelectorDialogComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testOnHistoryTokenChange() {
        final AppContext context = this.appContext(
            "/1/Spreadsheet123/cell/A1/validator"
        );

        final ValidatorSelectorDialogComponent dialog = ValidatorSelectorDialogComponent.with(
            new TestValidatorSelectorDialogComponentContext() {
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
            }
        );

        dialog.refresh(context);

        this.treePrintAndCheck(
            dialog,
            "ValidatorSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Validator Title123\n" +
                "    id=selector-Dialog includeClose=true CLOSED\n" +
                "      ValidatorSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [hello-validator] id=selector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet123/cell/A1/validator/save/hello-validator] id=selector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet123/cell/A1/validator/save/] id=selector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet123/cell/A1/validator/save/hello-validator] id=selector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet123/cell/A1] id=selector-close-Link\n"
        );
    }

    private static class TestValidatorSelectorDialogComponentContext extends FakeValidatorSelectorDialogComponentContext {

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return () -> {
            };
        }

        @Override
        public String dialogTitle() {
            return "Validator Title123";
        }
    }

    private AppContext appContext(final String historyToken) {
        return new FakeAppContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString(historyToken);
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }
        };
    }

    @Override
    public ValidatorSelectorDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return ValidatorSelectorDialogComponent.with(
            ValidatorSelectorDialogComponentContexts.appContext(
                this.appContext(historyToken.toString())
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
