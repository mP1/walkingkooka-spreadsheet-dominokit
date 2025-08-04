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

package walkingkooka.spreadsheet.dominokit.formhandler;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.validation.form.provider.FormHandlerSelector;

import java.util.Optional;

public final class FormHandlerSelectorDialogComponentTest implements DialogComponentLifecycleTesting<FormHandlerSelectorDialogComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testOnHistoryTokenChange() {
        final AppContext context = this.appContext(
            "/1/Spreadsheet123/spreadsheet/defaultFormHandler"
        );

        final FormHandlerSelectorDialogComponent dialog = FormHandlerSelectorDialogComponent.with(
            new TestFormHandlerSelectorDialogComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public Optional<FormHandlerSelector> undo() {
                    return Optional.of(
                        FormHandlerSelector.parse("hello-form-handler")
                    );
                }
            }
        );

        dialog.refresh(context);

        this.treePrintAndCheck(
            dialog,
            "FormHandlerSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    FormHandler Title123\n" +
                "    id=selector-Dialog includeClose=true CLOSED\n" +
                "      FormHandlerSelectorComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            [hello-form-handler] id=selector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet123/spreadsheet/defaultFormHandler/save/hello-form-handler] id=selector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet123/spreadsheet/defaultFormHandler/save/] id=selector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet123/spreadsheet/defaultFormHandler/save/hello-form-handler] id=selector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet123/spreadsheet] id=selector-close-Link\n"
        );
    }

    private static class TestFormHandlerSelectorDialogComponentContext extends FakeFormHandlerSelectorDialogComponentContext {

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return () -> {
            };
        }

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return () -> {
            };
        }

        @Override
        public String dialogTitle() {
            return "FormHandler Title123";
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

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }
        };
    }

    @Override
    public FormHandlerSelectorDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return FormHandlerSelectorDialogComponent.with(
            FormHandlerSelectorDialogComponentContextDefaultFormHandler.with(
                this.appContext(historyToken.toString())
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<FormHandlerSelectorDialogComponent> type() {
        return FormHandlerSelectorDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
