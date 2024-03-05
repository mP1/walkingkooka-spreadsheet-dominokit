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

import walkingkooka.Context;
import walkingkooka.net.header.MediaType;

import java.util.function.Predicate;

/**
 * A {@link Context} that abstracts the native implementation for interacting with the clipboard. Interfaces are used
 * here so JRE unit testing is possible, without requiring a real browser.
 */
public interface ClipboardContext extends Context {

    /**
     * Reads selected clipboard items first using the {@link Predicate} and then because clipboard operations involve
     * Promises, eventually delivers the selected clipboard items to the {@link ClipboardContextReadWatcher}.
     */
    void read(final Predicate<MediaType> filter,
              final ClipboardContextReadWatcher watcher);

    /**
     * Writes the given {@link ClipboardTextItem} to the clipboard, and because this is async, notifies the
     * {@link ClipboardContextWriteWatcher} with the outcome.
     */
    void write(final ClipboardTextItem item,
               final ClipboardContextWriteWatcher watcher);
}
