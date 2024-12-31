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

import walkingkooka.EmptyTextException;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.text.CharSequences;

/**
 * Does not hold a native File but supports sub-classes that can represent a file in various text forms.
 */
public abstract class File implements HasUrlFragment {

    /**
     * Currently only supports parsing text in {@link FileBase64} form.
     */
    public static File parse(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        final int endOfFileName = text.indexOf(HistoryToken.SEPARATOR.character());
        if (-1 == endOfFileName) {
            throw new IllegalArgumentException("Missing " + HistoryToken.SEPARATOR + " after filename");
        }

        final String filename = text.substring(
                0,
                endOfFileName
        );
        if (filename.isEmpty()) {
            throw new EmptyTextException("Missing filename");
        }

        final int endOfType = text.indexOf(
                HistoryToken.SEPARATOR.character(),
                endOfFileName + 1
        );
        final String type = text.substring(
                endOfFileName + 1,
                -1 == endOfType ?
                        text.length() :
                        endOfType
        );

        switch (type) {
            case "":
                throw new EmptyTextException("Missing type");
            case BASE64:
                return base64(
                        filename,
                        -1 == endOfType ?
                                "" :
                                text.substring(
                                        endOfType + 1
                                )
                );
            default:
                throw new IllegalArgumentException("Invalid type " + CharSequences.quoteAndEscape(type));
        }
    }

    final static String BASE64 = "base64";

    /**
     * {@see FileBase64}.
     */
    public static File base64(final String name,
                              final String content) {
        return FileBase64.with(
                name,
                content
        );
    }

    File() {
        super();
    }

    // Object...........................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(final Object other);

    @Override
    public abstract String toString();
}
