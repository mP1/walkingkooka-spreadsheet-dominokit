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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;

public final class BrowserFileTest implements ParseStringTesting<BrowserFile>,
        ClassTesting<BrowserFile> {

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParse() {
        this.parseStringAndCheck(
                "base64/filename123/fileContent456",
                BrowserFileBase64.with(
                        "filename123",
                        "fileContent456"
                )
        );
    }

    @Test
    public void testParseWithEmptyContent() {
        this.parseStringAndCheck(
                "base64/filename123/",
                BrowserFileBase64.with(
                        "filename123",
                        ""
                )
        );
    }

    @Test
    public void testParseWithEmptyContent2() {
        this.parseStringAndCheck(
                "base64/filename123",
                BrowserFileBase64.with(
                        "filename123",
                        ""
                )
        );
    }

    @Override
    public BrowserFile parseString(final String string) {
        return BrowserFile.parse(string);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // class............................................................................................................

    @Override
    public Class<BrowserFile> type() {
        return BrowserFile.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
