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

package walkingkooka.spreadsheet.dominokit.datetime;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDateComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, LocalDate, SpreadsheetDateComponent> {

    // setId............................................................................................................

    @Test
    public void testSetIdWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .setId(null)
        );
    }

    @Test
    public void testSetIdWithEmptyFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createComponent()
                .setId("")
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValueWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .setValue(null)
        );
    }

    @Test
    public void testClearValue() {
        final SpreadsheetDateComponent component = this.createComponent()
            .setValue(
                Optional.empty()
            );
        this.valueAndCheck(
            component,
            Optional.of(CLEAR_VALUE)
        );
    }

    // ValueComponent...................................................................................................

    @Override
     public SpreadsheetDateComponent createComponent() {
        return SpreadsheetDateComponent.empty(
            "id",
            () -> CLEAR_VALUE
        );
    }

    private final static LocalDate CLEAR_VALUE = LocalDate.of(1999, 12, 31);

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetDateComponent> type() {
        return SpreadsheetDateComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
