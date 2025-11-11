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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;

/**
 * A history token that saves the given file.
 */
public final class PluginUploadSaveHistoryToken extends PluginUploadHistoryToken
    implements Value<BrowserFile>,
    HistoryTokenWatcher {

    /**
     * Factory
     */
    static PluginUploadSaveHistoryToken with(final BrowserFile file) {
        return new PluginUploadSaveHistoryToken(
            Objects.requireNonNull(file, "file")
        );
    }

    private PluginUploadSaveHistoryToken(final BrowserFile file) {
        super();
        this.file = file;
    }

    @Override
    public BrowserFile value() {
        return this.file;
    }

    private final BrowserFile file;

    // HistoryToken.....................................................................................................

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return this; // ignore anything that follows /plugin-upload
    }

    //
    // /plugin-upload/save/base64/filename/filecontent
    //
    @Override
    UrlFragment pluginUploadUrlFragment() {
        return SAVE.appendSlashThen(
            this.file.urlFragment()
        );
    }

    // /plugin-upload/

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.pluginUploadSelect();
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        context.pluginFetcher()
            .postPluginUpload(
                FetcherRequestBody.file(this.file)
            );
        context.pushHistoryToken(previous);
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitPluginUploadSave(this.file);
    }
}
