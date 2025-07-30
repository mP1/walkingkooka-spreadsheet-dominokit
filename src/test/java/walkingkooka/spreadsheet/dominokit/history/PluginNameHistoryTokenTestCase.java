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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.plugin.PluginName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class PluginNameHistoryTokenTestCase<T extends PluginNameHistoryToken> extends PluginHistoryTokenTestCase<T> {

    final PluginName PLUGIN_NAME = PluginName.with("TestPluginName123");

    PluginNameHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken(null)
        );
    }

    @Test
    public void testPluginName() {
        this.pluginNameAndCheck(
            this.createHistoryToken(),
            PLUGIN_NAME
        );
    }

    @Test
    public final void testSetSaveStringValue() {
        final String value = "save-value-456";

        this.setSaveStringValueAndCheck(
            this.createHistoryToken(),
            value,
            PluginSaveHistoryToken.with(
                PLUGIN_NAME,
                value
            )
        );
    }

    @Override final T createHistoryToken() {
        return this.createHistoryToken(PLUGIN_NAME);
    }

    abstract T createHistoryToken(final PluginName pluginName);
}
