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

import walkingkooka.spreadsheet.dominokit.log.LoggingContext;

import java.util.Objects;

/**
 * A {@link ClipboardContextWriteWatcher} that logs messages for success or failure.
 */
final class LoggingClipboardContextWriteWatcher implements ClipboardContextWriteWatcher {

    static LoggingClipboardContextWriteWatcher with(final ClipboardTextItem clipboardTextItem,
                                                    final LoggingContext context) {
        Objects.requireNonNull(clipboardTextItem, "clipboardTextItem");
        Objects.requireNonNull(context, "context");

        return new LoggingClipboardContextWriteWatcher(
                clipboardTextItem,
                context
        );
    }

    public LoggingClipboardContextWriteWatcher(ClipboardTextItem clipboardTextItem, LoggingContext context) {
        this.clipboardTextItem = clipboardTextItem;
        this.context = context;
    }

    @Override
    public void onSuccess() {
        final ClipboardTextItem clipboardTextItem = this.clipboardTextItem;

        context.debug(
                "Clipboard write success " + clipboardTextItem.types() + " to clipboard",
                clipboardTextItem
        );
    }

    @Override
    public void onFailure(final Object cause) {
        final ClipboardTextItem clipboardTextItem = this.clipboardTextItem;

        this.context.error(
                "Clipboard write failed " + clipboardTextItem.types() + " failed.",
                cause
        );
    }

    private final ClipboardTextItem clipboardTextItem;

    private final LoggingContext context;

    @Override
    public String toString() {
        return this.clipboardTextItem.toString();
    }
}
