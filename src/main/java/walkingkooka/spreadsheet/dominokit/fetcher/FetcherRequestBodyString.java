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

import elemental2.dom.RequestInit;

import java.util.Objects;

/**
 * A fetcher request body that holds a {@link String} value.
 */
final class FetcherRequestBodyString extends FetcherRequestBody<String> {

    static FetcherRequestBodyString with(final String value) {
        return new FetcherRequestBodyString(
                Objects.requireNonNull(value, "value")
        );
    }

    private FetcherRequestBodyString(final String value) {
        this.value = value;
    }

    @Override
    void requestInit(final RequestInit requestInit) {
        requestInit.setBody(this.value);
    }

    // Value............................................................................................................

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    // Object...........................................................................................................


    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                (other instanceof FetcherRequestBodyString && this.equals0((FetcherRequestBodyString) other));
    }

    private boolean equals0(final FetcherRequestBodyString other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
