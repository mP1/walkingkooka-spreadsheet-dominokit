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

package walkingkooka.spreadsheet.dominokit.file;

import org.junit.jupiter.api.Test;
import walkingkooka.Binary;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.net.HasUrlFragmentTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertThrows;

public final class FileBinaryTest implements ClassTesting2<FileBinary>,
        HashCodeEqualsDefinedTesting2<FileBinary>,
        ToStringTesting<FileBinary>,
        HasUrlFragmentTesting,
        ParseStringTesting<FileBinary> {

    // with.............................................................................................................

    @Test
    public void testWithNullBinaryFails() {
        assertThrows(
                NullPointerException.class,
                () -> FileBinary.withBinary0(null)
        );
    }

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParse() {
        this.parseStringAndCheck(
                "SGVsbG8xMjM=",
                FileBinary.withBinary0(
                        Binary.with(
                                "Hello123".getBytes(StandardCharsets.UTF_8)
                        )
                )
        );
    }

    @Test
    public void testParseEmptyString() {
        this.parseStringAndCheck(
                "",
                FileBinary.withBinary0(Binary.EMPTY)
        );
    }

    @Override
    public FileBinary parseString(final String string) {
        return FileBinary.parseFileBinary(string);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // HasUrlFragment...................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                FileBinary.withBinary0(
                        Binary.with(
                                "Hello123".getBytes(StandardCharsets.UTF_8)
                        )
                ),
                "SGVsbG8xMjM="
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
                FileBinary.withBinary0(
                        Binary.with(
                                "Different".getBytes(StandardCharsets.UTF_8)
                        )
                )
        );
    }

    @Override
    public FileBinary createObject() {
        return FileBinary.withBinary0(
                Binary.with(
                        "Hello".getBytes(StandardCharsets.UTF_8)
                )
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createObject(),
                "[72, 101, 108, 108, 111]"
        );
    }

    // class............................................................................................................

    @Override
    public Class<FileBinary> type() {
        return FileBinary.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
