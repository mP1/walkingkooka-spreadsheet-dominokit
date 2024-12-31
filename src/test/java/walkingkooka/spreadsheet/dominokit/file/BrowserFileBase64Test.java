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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.net.HasUrlFragmentTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.Assert.assertThrows;

public final class BrowserFileBase64Test implements ClassTesting2<BrowserFileBase64>,
        HashCodeEqualsDefinedTesting2<BrowserFileBase64>,
        ToStringTesting<BrowserFileBase64>,
        HasUrlFragmentTesting {

    private final static String NAME = "Filename123";

    private final static String CONTENT = "Filecontent456";

    // with.............................................................................................................

    @Test
    public void testWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> BrowserFileBase64.with(
                        null,
                        CONTENT
                )
        );
    }

    @Test
    public void testWithEmptyNameFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> BrowserFileBase64.with(
                        "",
                        CONTENT
                )
        );
    }

    @Test
    public void testWithNullContentFails() {
        assertThrows(
                NullPointerException.class,
                () -> BrowserFileBase64.with(
                        NAME,
                        null
                )
        );
    }

    @Test
    public void testWithEmptyContent() {
        final String content = "";

        final BrowserFileBase64 fileBase64 = BrowserFileBase64.with(
                NAME,
                content
        );

        this.checkEquals(NAME, fileBase64.name);
        this.checkEquals(content, fileBase64.content);
    }

    // HasUrlFragment...................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                BrowserFileBase64.with(
                        NAME,
                        CONTENT
                ),
                "base64/Filename123/Filecontent456"
        );
    }

    @Test
    public void testUrlFragmentWithEmptyContent() {
        this.urlFragmentAndCheck(
                BrowserFileBase64.with(
                        NAME,
                        ""
                ),
                "base64/Filename123"
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(
                BrowserFileBase64.with(
                        "different-name",
                        CONTENT
                )
        );
    }

    @Test
    public void testEqualsDifferentContent() {
        this.checkNotEquals(
                BrowserFileBase64.with(
                        NAME,
                        "different-content"
                )
        );
    }

    @Override
    public BrowserFileBase64 createObject() {
        return BrowserFileBase64.with(
                NAME,
                CONTENT
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createObject(),
                "base64/Filename123/Filecontent456"
        );
    }

    // class............................................................................................................

    @Override
    public Class<BrowserFileBase64> type() {
        return BrowserFileBase64.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
