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
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.color.Color;
import walkingkooka.net.header.HasMediaTypeTesting;
import walkingkooka.net.header.MediaType;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellClipboardValueTest implements ClassTesting<SpreadsheetCellClipboardValue<TextStyle>>,
        HashCodeEqualsDefinedTesting2<SpreadsheetCellClipboardValue<TextStyle>>,
        HasMediaTypeTesting,
        ToStringTesting<SpreadsheetCellClipboardValue<TextStyle>> {

    private final static MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON;

    private final static TextStyle VALUE = TextStyle.EMPTY.set(
            TextStylePropertyName.COLOR,
            Color.BLACK
    );

    @Test
    public void testWithNullMediaTypeFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellClipboardValue.with(
                        null,
                        VALUE
                )
        );
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellClipboardValue.with(
                        MEDIA_TYPE,
                        null
                )
        );
    }

    @Test
    public void testWith() {
        final SpreadsheetCellClipboardValue<TextStyle> clipboardValue = this.createObject();
        this.mediaTypeAndCheck(clipboardValue);
        this.checkValue(clipboardValue);
    }

    // setMediaType.....................................................................................................

    @Test
    public void testSetMediaTypeNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject().setMediaType(null)
        );
    }

    @Test
    public void testSetMediaTypeSame() {
        final SpreadsheetCellClipboardValue<TextStyle> clipboardValue = this.createObject();
        assertSame(
                clipboardValue,
                clipboardValue.setMediaType(MEDIA_TYPE)
        );
    }

    @Test
    public void testSetMediaTypeDifferent() {
        final SpreadsheetCellClipboardValue<TextStyle> clipboardValue = this.createObject();
        final MediaType differentMediaType = MediaType.TEXT_PLAIN;
        final SpreadsheetCellClipboardValue<TextStyle> different = clipboardValue.setMediaType(differentMediaType);

        assertNotSame(
                clipboardValue,
                different
        );

        this.mediaTypeAndCheck(clipboardValue);
        this.checkValue(clipboardValue);

        this.mediaTypeAndCheck(different, differentMediaType);
        this.checkValue(different);
    }

    // seValue.........................................................................................................

    @Test
    public void testSetValueNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject().setValue(null)
        );
    }

    @Test
    public void testSetValueSame() {
        final SpreadsheetCellClipboardValue<TextStyle> clipboardValue = this.createObject();
        assertSame(
                clipboardValue,
                clipboardValue.setValue(VALUE)
        );
    }

    @Test
    public void testSetValueDifferent() {
        final SpreadsheetCellClipboardValue<TextStyle> clipboardValue = this.createObject();

        final TextStyle differentValue = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER
        );
        final SpreadsheetCellClipboardValue<TextStyle> different = clipboardValue.setValue(differentValue);

        assertNotSame(
                clipboardValue,
                different
        );

        this.mediaTypeAndCheck(clipboardValue);
        this.checkValue(clipboardValue);

        this.mediaTypeAndCheck(different);
        this.checkValue(different, differentValue);
    }

    private void mediaTypeAndCheck(final SpreadsheetCellClipboardValue<TextStyle> clipboardValue) {
        this.mediaTypeAndCheck(
                clipboardValue,
                MEDIA_TYPE
        );
    }

    private void checkValue(final SpreadsheetCellClipboardValue<TextStyle> clipboardValue) {
        this.checkValue(
                clipboardValue,
                VALUE
        );
    }

    private void checkValue(final SpreadsheetCellClipboardValue<TextStyle> clipboardValue,
                            final TextStyle expected) {
        this.checkEquals(
                expected,
                clipboardValue.value(),
                "value"
        );
    }

    @Test
    public void testEqualsDifferentMediaType() {
        this.checkNotEquals(
                SpreadsheetCellClipboardValue.with(
                        MediaType.TEXT_PLAIN,
                        VALUE
                )
        );
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(
                SpreadsheetCellClipboardValue.with(
                        MEDIA_TYPE,
                        TextStyle.EMPTY.set(
                                TextStylePropertyName.TEXT_ALIGN,
                                TextAlign.CENTER
                        )
                )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createObject(),
                MEDIA_TYPE + " " + VALUE
        );
    }

    @Override
    public SpreadsheetCellClipboardValue<TextStyle> createObject() {
        return SpreadsheetCellClipboardValue.with(
                MEDIA_TYPE,
                VALUE
        );
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<SpreadsheetCellClipboardValue<TextStyle>> type() {
        return Cast.to(SpreadsheetCellClipboardValue.class);
    }
}
