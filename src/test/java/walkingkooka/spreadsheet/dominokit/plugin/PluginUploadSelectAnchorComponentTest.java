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

package walkingkooka.spreadsheet.dominokit.plugin;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;

import java.util.Optional;

public final class PluginUploadSelectAnchorComponentTest implements AnchorComponentTesting<PluginUploadSelectAnchorComponent, Boolean> {

    @Test
    public void testSetIdSetTextContent() {
        this.treePrintAndCheck(
            PluginUploadSelectAnchorComponent.empty("Upload123")
                .setTextContent("Upload123!"),
            "\"Upload123!\" DISABLED id=Upload123"
        );
    }

    @Test
    public void testSetDisabledFalse() {
        this.treePrintAndCheck(
            PluginUploadSelectAnchorComponent.empty("Upload123")
                .setTextContent("Upload123!")
                .setDisabled(false),
            "\"Upload123!\" [#/plugin-upload] id=Upload123"
        );
    }

    @Test
    public void testSetDisabledTrue() {
        this.treePrintAndCheck(
            PluginUploadSelectAnchorComponent.empty("Upload123")
                .setTextContent("Upload123!")
                .setDisabled(true),
            "\"Upload123!\" DISABLED id=Upload123"
        );
    }

    @Test
    public void testSetValueWithTrue() {
        this.treePrintAndCheck(
            PluginUploadSelectAnchorComponent.empty("Upload123")
                .setTextContent("Upload123!")
                .setValue(
                    Optional.of(true)
                ),
            "\"Upload123!\" DISABLED id=Upload123"
        );
    }


    @Test
    public void testSetValueWithFalse() {
        this.treePrintAndCheck(
            PluginUploadSelectAnchorComponent.empty("Upload123")
                .setTextContent("Upload123!")
                .setValue(
                    Optional.of(false)
                ),
            "\"Upload123!\" [#/plugin-upload] id=Upload123"
        );
    }


    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            PluginUploadSelectAnchorComponent.empty("Upload123")
                .setTextContent("Upload123!")
                .clearValue(),
            "\"Upload123!\" [#/plugin-upload] id=Upload123"
        );
    }

    @Override
    public PluginUploadSelectAnchorComponent createComponent() {
        return PluginUploadSelectAnchorComponent.empty("Test999");
    }

    // class............................................................................................................

    @Override
    public Class<PluginUploadSelectAnchorComponent> type() {
        return PluginUploadSelectAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
