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
import walkingkooka.net.UrlQueryString;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.OptionalInt;

public final class FetcherTest implements ClassTesting2<Fetcher> {

    // offsetAndCountQueryString........................................................................................

    @Test
    public void testOffsetAndCountQueryString() {
        this.offsetAndCountQueryStringAndCheck(
            0,
            0,
            UrlQueryString.EMPTY
        );
    }

    @Test
    public void testOffsetAndCountQueryStringWithNonZeroOffset() {
        this.offsetAndCountQueryStringAndCheck(
            1,
            0,
            UrlQueryString.parse("offset=1")
        );
    }

    @Test
    public void testOffsetAndCountQueryStringWithAndNonZeroCount() {
        this.offsetAndCountQueryStringAndCheck(
            0,
            2,
            UrlQueryString.parse("count=2")
        );
    }

    @Test
    public void testOffsetAndCountQueryStringWithNonZeroOffsetAndNonZeroCount() {
        this.offsetAndCountQueryStringAndCheck(
            1,
            2,
            UrlQueryString.parse("offset=1&count=2")
        );
    }

    private void offsetAndCountQueryStringAndCheck(final int offset,
                                                   final int count,
                                                   final UrlQueryString expected) {
        this.offsetAndCountQueryStringAndCheck(
            OptionalInt.of(offset),
            OptionalInt.of(count),
            expected
        );
    }

    private void offsetAndCountQueryStringAndCheck(final OptionalInt offset,
                                                   final OptionalInt count,
                                                   final UrlQueryString expected) {
        this.checkEquals(
            expected,
            Fetcher.offsetAndCountQueryString(
                offset,
                count
            ),
            () -> "offset=" + offset + " count=" + count
        );
    }

    // class............................................................................................................

    @Override
    public Class<Fetcher> type() {
        return Fetcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
