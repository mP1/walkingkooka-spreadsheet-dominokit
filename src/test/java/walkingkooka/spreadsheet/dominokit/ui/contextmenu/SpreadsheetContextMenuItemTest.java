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

package walkingkooka.spreadsheet.dominokit.ui.contextmenu;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;

import java.util.Optional;

public class SpreadsheetContextMenuItemTest implements ClassTesting<SpreadsheetContextMenuItem>,
        ToStringTesting<SpreadsheetContextMenuItem> {

    @Test
    public void testToStringWithBadge() {
        this.toStringAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text-123")
                        .badge(
                                Optional.of(
                                        "Badge-text-123"
                                )
                        ),
                "id1-MenuItem \"text-123\" Badge-text-123"
        );
    }

    @Test
    public void testToStringWithIcons() {
        this.toStringAndCheck(
                SpreadsheetContextMenuItem.with("id1-MenuItem", "text2")
                        .icon(
                                Optional.of(
                                        SpreadsheetIcons.checked()
                                )
                        ),
                "id1-MenuItem \"text2\" mdi-check"
        );
    }

    @Test
    public void testToStringWithKey() {
        this.toStringAndCheck(
                SpreadsheetContextMenuItem.with("id3-MenuItem", "text3")
                        .key("A"),
                "id3-MenuItem \"A\" \"text3\""
        );
    }

    @Override
    public Class<SpreadsheetContextMenuItem> type() {
        return SpreadsheetContextMenuItem.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
