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

package walkingkooka.spreadsheet.dominokit.clipboard;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.net.header.MediaType;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ClipboardTextItemTest implements ClassTesting<ClipboardTextItem>,
        HashCodeEqualsDefinedTesting2<ClipboardTextItem>,
        ToStringTesting<ClipboardTextItem> {

    @Test
    public void testWithNullMediaTypeFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.with(
                        null,
                        "abc123"
                )
        );
    }

    @Test
    public void testWithNullTextFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.with(
                        MediaType.TEXT_PLAIN,
                        null
                )
        );
    }

    @Test
    public void testEqualsDifferentType() {
        this.checkNotEquals(
                ClipboardTextItem.with(
                        MediaType.parse("text/different"),
                        "Text123"
                )
        );
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(
                ClipboardTextItem.with(
                        MediaType.TEXT_PLAIN,
                        "different"
                )
        );
    }

    @Test
    public void testToString() {
        final MediaType type = MediaType.TEXT_PLAIN;
        final String text = "abc123";

        this.toStringAndCheck(
                ClipboardTextItem.with(
                        type,
                        text
                ),
                type + " \"" + text + "\""
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ClipboardTextItem> type() {
        return ClipboardTextItem.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefinedTesting2....................................................................................

    @Override
    public ClipboardTextItem createObject() {
        return ClipboardTextItem.with(
                MediaType.TEXT_PLAIN,
                "Text123"
        );
    }
}
