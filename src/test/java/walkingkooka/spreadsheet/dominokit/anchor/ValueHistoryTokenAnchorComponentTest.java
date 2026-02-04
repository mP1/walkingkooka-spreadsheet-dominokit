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
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ValueHistoryTokenAnchorComponentTest implements ValueComponentTesting<HTMLAnchorElement, JarEntryInfoName, ValueHistoryTokenAnchorComponent<JarEntryInfoName>> {

    private final static PluginName PLUGIN_NAME = PluginName.with("test-plugin-name-123");

    private final static Function<HistoryTokenAnchorComponent, Optional<JarEntryInfoName>> GETTER = (a) -> {
        final RelativeUrl url = (RelativeUrl) a.href();

        JarEntryInfoName name = null;
        if (null != url) {
            // 1=api / 2=plugin / 3=download / 4=path
            name = JarEntryInfoName.with(
                url.path()
                    .namesList()
                    .stream()
                    .skip(5)
                    .map(n -> n.value())
                    .collect(
                        Collectors.joining(
                            JarEntryInfoName.SEPARATOR.string(),
                            JarEntryInfoName.SEPARATOR.string(),
                            ""
                        )
                    )
            );
        }

        return Optional.ofNullable(name);
    };

    private final static BiConsumer<Optional<JarEntryInfoName>, HistoryTokenAnchorComponent> SETTER = (v, c) ->
        c.setHref(
            PluginFetcher.downloadUrl(
                PLUGIN_NAME,
                v
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
        final JarEntryInfoName value = JarEntryInfoName.MANIFEST_MF;

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
                    Optional.of(
                        JarEntryInfoName.MANIFEST_MF
                    )
                ),
            "[/api/plugin/test-plugin-name-123/download/META-INF/MANIFEST.MF]"
        );
    }

    @Override
    public ValueHistoryTokenAnchorComponent<JarEntryInfoName> createComponent() {
        return ValueHistoryTokenAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            GETTER,
            SETTER
        );
    }

    // class............................................................................................................

    @Override
    public Class<ValueHistoryTokenAnchorComponent<JarEntryInfoName>> type() {
        return Cast.to(ValueHistoryTokenAnchorComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
