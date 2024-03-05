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

import elemental2.core.JsArray;
import elemental2.promise.Promise;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;

/**
 * A class for interacting with the clipboard to read and write data.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Clipboard_API" target="_blank">MDN
 * Documentation</a>
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Clipboard")
public class ElementalClipboard {

    /**
     * Reads data from the clipboard.
     *
     * @return A promise that resolves to a list of ElementalClipboardItem objects.
     */
    public native Promise<JsArray<ElementalClipboardItem>> read();

    /**
     * Reads text data from the clipboard.
     *
     * @return A promise that resolves to the text data from the clipboard.
     */
    public native Promise<String> readText();

    /**
     * Writes data to the clipboard.
     *
     * @param item The ElementalClipboardItem to write to the clipboard.
     * @return A promise that resolves when the data is successfully written to the clipboard.
     */
    public native Promise<Any> write(JsArray<ElementalClipboardItem> items);

    /**
     * Writes text data to the clipboard.
     *
     * @param text The text data to write to the clipboard.
     * @return A promise that resolves when the text data is successfully written to the clipboard.
     */
    public native Promise<Any> writeText(String text);
}
