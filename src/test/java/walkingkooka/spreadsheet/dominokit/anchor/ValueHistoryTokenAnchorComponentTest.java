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

package walkingkooka.spreadsheet.dominokit.anchor;

import elemental2.dom.HTMLAnchorElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ValueHistoryTokenAnchorComponentTest implements ValueComponentTesting<HTMLAnchorElement, String, ValueHistoryTokenAnchorComponent<String>> {

    private final static Function<HistoryTokenAnchorComponent, Optional<String>> GETTER = (a) -> {
        final RelativeUrl url = (RelativeUrl) a.href();
        final UrlPath path = null != url ?
            url.path() :
            null;

        return Optional.ofNullable(
            null == path || path.isRoot() ?
                null :
                path.value()
        );
    };

    private final static BiConsumer<Optional<String>, HistoryTokenAnchorComponent> SETTER = (v, c) ->
        c.setHref(
            RelativeUrl.parseRelative(
                v.orElse("")
            )
        );

    // with.............................................................................................................

    @Test
    public void testWithNullAnchorFails() {
        assertThrows(
            NullPointerException.class,
            () -> ValueHistoryTokenAnchorComponent.with(
                null,
                GETTER,
                SETTER
            )
        );
    }

    @Test
    public void testWithNullGetterFails() {
        assertThrows(
            NullPointerException.class,
            () -> ValueHistoryTokenAnchorComponent.with(
                HistoryTokenAnchorComponent.empty(),
                null,
                SETTER
            )
        );
    }

    @Test
    public void testWithNullSetterFails() {
        assertThrows(
            NullPointerException.class,
            () -> ValueHistoryTokenAnchorComponent.with(
                HistoryTokenAnchorComponent.empty(),
                GETTER,
                null
            )
        );
    }

    // value............................................................................................................

    @Test
    public void testValueWhenEmpty() {
        this.valueAndCheck(
            ValueHistoryTokenAnchorComponent.with(
                HistoryTokenAnchorComponent.empty(),
                GETTER,
                SETTER
            )
        );
    }

    @Test
    public void testSetValueThenValue() {
        final String value = "/Hello";

        this.valueAndCheck(
            ValueHistoryTokenAnchorComponent.with(
                HistoryTokenAnchorComponent.empty(),
                GETTER,
                SETTER
            ).setValue(
                Optional.of(value)
            ),
            value
        );
    }

    // TreePrint........................................................................................................

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "DISABLED"
        );
    }

    @Test
    public void testTreePrintWithoutValueAndDisabled() {
        this.treePrintAndCheck(
            this.createComponent()
                .setDisabled(true),
            "DISABLED"
        );
    }

    @Test
    public void testTreePrintWithoutValueAndSetId() {
        this.treePrintAndCheck(
            this.createComponent()
                .setId("Anchor123"),
            "DISABLED id=Anchor123"
        );
    }

    @Test
    public void testTreePrintAfterSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of("/Hello")
                ),
            "[/Hello]"
        );
    }

    @Override
    public ValueHistoryTokenAnchorComponent<String> createComponent() {
        return ValueHistoryTokenAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            GETTER,
            SETTER
        );
    }

    // class............................................................................................................

    @Override
    public Class<ValueHistoryTokenAnchorComponent<String>> type() {
        return Cast.to(ValueHistoryTokenAnchorComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
