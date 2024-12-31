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

package walkingkooka.spreadsheet.dominokit.fetcher;

import elemental2.dom.Headers;
import elemental2.dom.RequestInit;
import walkingkooka.Value;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;

/**
 * Holds a fetcher request body, such as a {@link String} or {@link elemental2.dom.Blob}.
 */
public abstract class FetcherRequestBody<T> implements Value<T> {

    /**
     * {@see FetcherRequestBodyFile}
     */
    public static FetcherRequestBody<BrowserFile> file(final BrowserFile file) {
        return FetcherRequestBodyFile.with(file);
    }

    /**
     * {@see FetcherRequestBodyString}
     */
    public static FetcherRequestBody<String> string(final String  value) {
        return FetcherRequestBodyString.with(value);
    }

    FetcherRequestBody() {
        super();
    }

    public final boolean isString() {
        return this instanceof FetcherRequestBodyString;
    }

    abstract void handleFetch(final Headers headers,
                              final RequestInit requestInit,
                              final Runnable doFetch);
}
