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
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;

import java.util.Objects;

/**
 * A fetcher request body that holds a {@link BrowserFile file} value.
 */
final class FetcherRequestBodyFile extends FetcherRequestBody<BrowserFile> {

    static FetcherRequestBodyFile with(final BrowserFile file) {
        return new FetcherRequestBodyFile(
            Objects.requireNonNull(file, "file")
        );
    }

    private FetcherRequestBodyFile(final BrowserFile file) {
        this.file = file;
    }

    @Override
    void handleFetch(final Headers headers,
                     final RequestInit requestInit,
                     final Runnable doFetch) {
        this.file.handleFetch(
            headers,
            requestInit,
            doFetch
        );
    }

    // Value............................................................................................................

    @Override
    public BrowserFile value() {
        return this.file;
    }

    private final BrowserFile file;

    // Object...........................................................................................................


    @Override
    public int hashCode() {
        return this.file.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof FetcherRequestBodyFile && this.equals0((FetcherRequestBodyFile) other));
    }

    private boolean equals0(final FetcherRequestBodyFile other) {
        return this.file.equals(other.file);
    }

    @Override
    public String toString() {
        return this.file.toString();
    }
}
