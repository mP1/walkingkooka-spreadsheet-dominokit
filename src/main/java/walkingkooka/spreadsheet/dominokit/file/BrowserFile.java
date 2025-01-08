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

package walkingkooka.spreadsheet.dominokit.file;

import elemental2.dom.Headers;
import elemental2.dom.RequestInit;
import walkingkooka.EmptyTextException;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.TreePrintable;

/**
 * Does not hold a native BrowserFile but supports sub-classes that can represent a file in various text forms.
 */
public abstract class BrowserFile implements HasUrlFragment,
    TreePrintable {

    /**
     * Currently only supports parsing text in {@link BrowserFileBase64} form.
     */
    public static BrowserFile parse(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        final int endOfType = text.indexOf(HistoryToken.SEPARATOR.character());
        if (-1 == endOfType) {
            throw new IllegalArgumentException("Missing filename");
        }

        final String type = text.substring(
            0,
            endOfType
        );
        if (type.isEmpty()) {
            throw new EmptyTextException("Missing type");
        }

        switch (type) {
            case BASE64:
                return parseBase64(
                    text.substring(endOfType + 1)
                );
            default:
                throw new IllegalArgumentException("Invalid type " + CharSequences.quoteAndEscape(type));
        }
    }

    private static BrowserFile parseBase64(final String text) {
        final int endOfFilename = text.indexOf(
            HistoryToken.SEPARATOR.character()
        );

        return base64(
            text.substring(
                0,
                -1 == endOfFilename ?
                    text.length() :
                    endOfFilename
            ),
            -1 == endOfFilename ?
                "" :
                text.substring(
                    endOfFilename + 1
                )
        );
    }

    final static String BASE64 = "base64";

    /**
     * {@see BrowserFileBase64}.
     */
    public static BrowserFile base64(final String name,
                                     final String content) {
        return BrowserFileBase64.with(
            name,
            content
        );
    }

    BrowserFile() {
        super();
    }

    /**
     * Completes the request and fetch
     */
    abstract public void handleFetch(final Headers headers,
                                     final RequestInit requestInit,
                                     final Runnable doFetch);

    // Object...........................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(final Object other);

    @Override
    public abstract String toString();
}
