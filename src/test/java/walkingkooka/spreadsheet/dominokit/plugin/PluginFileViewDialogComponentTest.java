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

package walkingkooka.spreadsheet.dominokit.plugin;

import org.junit.jupiter.api.Test;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Optional;

public final class PluginFileViewDialogComponentTest implements DialogComponentLifecycleTesting<PluginFileViewDialogComponent> {

    private final static PluginName PLUGIN_NAME = PluginName.with("test-plugin-name-111");

    private final static JarEntryInfoName EMPTY_FILENAME = JarEntryInfoName.with("/empty-file.txt");

    private final static Optional<String> EMPTY_FILE_CONTENT = Optional.of("");

    private final static JarEntryInfoName NON_EMPTY_FILENAME = JarEntryInfoName.with("/non-empty-file.txt");

    private final static Optional<String> NON_EMPTY_FILE_CONTENT = Optional.of("Line1\nLine2\n");

    @Test
    public void testEmptyBeforeLoadingFile() {
        this.onHistoryTokenChangeAndCheck(
            "/plugin/" + PLUGIN_NAME + "/file" + JarEntryInfoName.MANIFEST_MF,
            "PluginFileViewDialogComponent\n" +
                "  DialogComponent\n" +
                "    /META-INF/MANIFEST.MF\n" +
                "    id=pluginFileView-Dialog includeClose=true\n" +
                "      TextViewComponent\n" +
                "        \"\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Download\" [/api/plugin/test-plugin-name-111/download/META-INF/MANIFEST.MF] id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin/test-plugin-name-111] id=pluginFileView-close-Link\n"
        );
    }

    @Test
    public void testLoadEmptyFile() {
        final TestAppContext context = new TestAppContext(
            "/plugin/" + PLUGIN_NAME + "/file" + EMPTY_FILENAME
        );

        final PluginFileViewDialogComponent dialog = this.dialog(
            this.pluginDialogComponentContext(context)
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "PluginFileViewDialogComponent\n" +
                "  DialogComponent\n" +
                "    /empty-file.txt\n" +
                "    id=pluginFileView-Dialog includeClose=true\n" +
                "      TextViewComponent\n" +
                "        \"\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Download\" [/api/plugin/test-plugin-name-111/download/empty-file.txt] id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin/test-plugin-name-111] id=pluginFileView-close-Link\n"
        );
    }

    @Test
    public void testLoadNonEmptyFile() {
        final TestAppContext context = new TestAppContext("/plugin/" + PLUGIN_NAME + "/file" + NON_EMPTY_FILENAME);

        final PluginFileViewDialogComponent dialog = this.dialog(
            this.pluginDialogComponentContext(context)
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "PluginFileViewDialogComponent\n" +
                "  DialogComponent\n" +
                "    /non-empty-file.txt\n" +
                "    id=pluginFileView-Dialog includeClose=true\n" +
                "      TextViewComponent\n" +
                "        \"Line1\\nLine2\\n\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Download\" [/api/plugin/test-plugin-name-111/download/non-empty-file.txt] id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin/test-plugin-name-111] id=pluginFileView-close-Link\n"
        );
    }

    @Test
    public void testLoadDifferentFile() {
        final TestAppContext context = new TestAppContext(
            "/plugin/" + PLUGIN_NAME + "/file" + EMPTY_FILENAME
        );

        final PluginFileViewDialogComponent dialog = this.dialog(
            this.pluginDialogComponentContext(context)
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "PluginFileViewDialogComponent\n" +
                "  DialogComponent\n" +
                "    /empty-file.txt\n" +
                "    id=pluginFileView-Dialog includeClose=true\n" +
                "      TextViewComponent\n" +
                "        \"\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Download\" [/api/plugin/test-plugin-name-111/download/empty-file.txt] id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin/test-plugin-name-111] id=pluginFileView-close-Link\n"
        );

        context.pushHistoryToken(
            HistoryToken.pluginFileView(
                PLUGIN_NAME,
                Optional.of(NON_EMPTY_FILENAME)
            )
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "PluginFileViewDialogComponent\n" +
                "  DialogComponent\n" +
                "    /non-empty-file.txt\n" +
                "    id=pluginFileView-Dialog includeClose=true\n" +
                "      TextViewComponent\n" +
                "        \"Line1\\nLine2\\n\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Download\" [/api/plugin/test-plugin-name-111/download/non-empty-file.txt] id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin/test-plugin-name-111] id=pluginFileView-close-Link\n"
        );
    }

    private void onHistoryTokenChangeAndCheck(final String historyToken,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
            new TestAppContext(historyToken),
            expected
        );
    }

    static class TestAppContext extends FakeAppContext {

        TestAppContext(final String historyToken) {
            this.historyToken = HistoryToken.parseString(historyToken);
        }

        @Override
        public Runnable addPluginFetcherWatcher(final PluginFetcherWatcher watcher) {
            return this.pluginFetcherWatchers.addPluginFetcherWatcher(watcher);
        }

        private final PluginFetcherWatchers pluginFetcherWatchers = PluginFetcherWatchers.empty();

        void loadJarTextFile(final PluginName pluginName,
                             final JarEntryInfoName filename) {
            if (PLUGIN_NAME.equals(pluginName)) {
                if (EMPTY_FILENAME.equals(filename)) {
                    this.pluginFetcherWatchers.onJarEntryInfoName(
                        pluginName,
                        Optional.of(EMPTY_FILENAME),
                        EMPTY_FILE_CONTENT,
                        this
                    );
                }
                if (NON_EMPTY_FILENAME.equals(filename)) {
                    this.pluginFetcherWatchers.onJarEntryInfoName(
                        pluginName,
                        Optional.of(NON_EMPTY_FILENAME),
                        NON_EMPTY_FILE_CONTENT,
                        this
                    );
                }
            }
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.historyTokenWatchers.add(watcher);
        }

        private final HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        @Override
        public void pushHistoryToken(final HistoryToken token) {
            final HistoryToken previous = this.historyToken;
            this.historyToken = token;

            this.historyTokenWatchers.onHistoryTokenChange(
                previous,
                this
            );
        }

        private HistoryToken historyToken;
    }

    private void onHistoryTokenChangeAndCheck(final TestAppContext context,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
            this.dialog(
                this.pluginDialogComponentContext(context)
            ),
            context,
            expected
        );
    }

    private TestPluginFileViewDialogComponentContext pluginDialogComponentContext(final TestAppContext context) {
        return new TestPluginFileViewDialogComponentContext(context);
    }

    static class TestPluginFileViewDialogComponentContext extends FakePluginFileViewDialogComponentContext {

        TestPluginFileViewDialogComponentContext(final TestAppContext context) {
            this.context = context;
        }

        @Override
        public String dialogTitle() {
            return DialogComponent.class.getSimpleName();
        }

        @Override
        public Runnable addPluginFetcherWatcher(final PluginFetcherWatcher watcher) {
            return this.context.addPluginFetcherWatcher(watcher);
        }

        @Override
        public void loadJarTextFile(final PluginName pluginName,
                                    final JarEntryInfoName filename) {
            this.context.loadJarTextFile(
                pluginName,
                filename
            );
        }

        @Override
        public HistoryToken historyToken() {
            return this.context.historyToken();
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.context.addHistoryTokenWatcher(watcher);
        }

        private final TestAppContext context;
    }

    private PluginFileViewDialogComponent dialog(final PluginFileViewDialogComponentContext context) {
        return PluginFileViewDialogComponent.with(context);
    }

    @Override
    public PluginFileViewDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return PluginFileViewDialogComponent.with(
            new TestPluginFileViewDialogComponentContext(
                new TestAppContext(historyToken.toString())
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PluginFileViewDialogComponent> type() {
        return PluginFileViewDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
