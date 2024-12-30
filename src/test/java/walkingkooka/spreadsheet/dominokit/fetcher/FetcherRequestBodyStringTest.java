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

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FetcherRequestBodyStringTest implements HashCodeEqualsDefinedTesting2<FetcherRequestBodyString>,
        ClassTesting2<FetcherRequestBodyString> {

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> FetcherRequestBody.string(null)
        );
    }

    @Test
    public void testWithEmpty() {
        final String value = "";

        final FetcherRequestBodyString request = FetcherRequestBodyString.with(value);
        assertSame(value, request.value());
    }

    @Test
    public void testWithNotEmpty() {
        final String value = "abc123";

        final FetcherRequestBodyString request = FetcherRequestBodyString.with(value);
        assertSame(value, request.value());
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(
                FetcherRequestBodyString.with("different")
        );
    }

    @Override
    public FetcherRequestBodyString createObject() {
        return FetcherRequestBodyString.with("abc123");
    }

    // class............................................................................................................

    @Override
    public Class<FetcherRequestBodyString> type() {
        return FetcherRequestBodyString.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
