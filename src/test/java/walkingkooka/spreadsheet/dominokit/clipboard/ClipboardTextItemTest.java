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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.MediaType;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.printer.TreePrintableTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ClipboardTextItemTest implements ClassTesting<ClipboardTextItem>,
        HashCodeEqualsDefinedTesting2<ClipboardTextItem>,
        ToStringTesting<ClipboardTextItem>,
        TreePrintableTesting {

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
    public void testWithEmptyMediaTypeFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ClipboardTextItem.with(
                        Lists.empty(),
                        "abc123"
                )
        );
    }

    @Test
    public void testWithNullTextFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        null
                )
        );
    }

    @Test
    public void testEqualsDifferentTypes() {
        this.checkNotEquals(
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.parse("text/different")
                        ),
                        "Text123"
                )
        );
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
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
                        Lists.of(type),
                        text
                ),
                type + " \"" + text + "\""
        );
    }

    // TreePrinterTesting...............................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "123"
                ),
                "types\n" +
                        "  text/plain\n" +
                        "text\n" +
                        "  123\n"
        );
    }

    @Test
    public void testTreePrintSeveralMediaTypes() {
        this.treePrintAndCheck(
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN,
                                MediaType.APPLICATION_JSON
                        ),
                        "123"
                ),
                "types\n" +
                        "  text/plain\n" +
                        "  application/json\n" +
                        "text\n" +
                        "  123\n"
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
                Lists.of(
                        MediaType.TEXT_PLAIN
                ),
                "Text123"
        );
    }
}
