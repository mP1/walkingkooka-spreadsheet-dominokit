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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * A {@link BrowserFile} that holds the name and content encoded in base64. No attempt is made to decode the string, it is
 * assumed to be valid.
 */
final class BrowserFileBase64 extends BrowserFile {

    static BrowserFileBase64 with(final String name,
                                  final String content) {
        return new BrowserFileBase64(
                CharSequences.failIfNullOrEmpty(name, "name")
                        .replace(HistoryToken.SEPARATOR.string(), ""), // remove slash
                Objects.requireNonNull(content, "content")
        );
    }

    private BrowserFileBase64(final String name,
                              final String content) {
        this.name = name;
        this.content = content;
    }

    // UrlFragment......................................................................................................

    // plugin-upload/base64/filename/file-content
    @Override
    public UrlFragment urlFragment() {
        return BASE64_URLFRAGMENT.appendSlashThen(
                        UrlFragment.with(this.name)
                )
                .appendSlashThen(
                        UrlFragment.with(this.content)
                );
    }

    private final static UrlFragment BASE64_URLFRAGMENT = UrlFragment.with(BASE64);

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
                this.name,
                this.content
        );
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof BrowserFileBase64 && this.equals0((BrowserFileBase64) other);
    }

    private boolean equals0(final BrowserFileBase64 other) {
        return this.name.equals(other.name) &&
                this.content.equals(other.content);
    }

    @Override
    public String toString() {
        return this.urlFragment().toString();
    }

    // @VisibleForTesting
    final String name;

    // @VisibleForTesting
    final String content;
}
