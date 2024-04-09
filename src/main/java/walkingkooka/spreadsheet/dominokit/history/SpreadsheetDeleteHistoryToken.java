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

import elemental2.dom.Headers;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public final class SpreadsheetDeleteHistoryToken extends SpreadsheetNameHistoryToken {

    static SpreadsheetDeleteHistoryToken with(final SpreadsheetId id,
                                              final SpreadsheetName name) {
        return new SpreadsheetDeleteHistoryToken(
                id,
                name
        );
    }

    private SpreadsheetDeleteHistoryToken(final SpreadsheetId id,
                                          final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override //
    HistoryToken setClear0() {
        return null;
    }

    @Override //
    HistoryToken setDelete0() {
        return this;
    }

    @Override //
    HistoryToken setFormatPattern() {
        return this;
    }

    @Override
    public HistoryToken setFormula() {
        return this; // should not happen
    }

    @Override //
    HistoryToken setFreeze0() {
        return this;
    }

    @Override
    HistoryToken setInsertAfter0(final OptionalInt count) {
        return this;
    }

    @Override //
    HistoryToken setInsertBefore0(final OptionalInt count) {
        return this;
    }

    @Override //
    HistoryToken setMenu1() {
        return this;
    }

    @Override //
    AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        return selection.setDefaultAnchor();
    }

    @Override
    HistoryToken setParsePattern() {
        return this;
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return HistoryToken.spreadsheetDelete(
                id,
                name
        );
    }

    @Override //
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override //
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override //
    HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return this;
    }

    @Override //
    HistoryToken setUnfreeze0() {
        return this;
    }

    @Override
    UrlFragment spreadsheetUrlFragment() {
        return DELETE;
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return spreadsheetSelect(
                this.id(),
                SpreadsheetName.with(component)
        ).parse(cursor);
    }

    @Override
    public void onHistoryTokenChange0(final HistoryToken previous,
                                     final AppContext context) {
        context.pushHistoryToken(
                previous.clearAction()
        );

        context.addSpreadsheetMetadataWatcherOnce(
                new SpreadsheetMetadataFetcherWatcher() {
                    @Override
                    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                                      final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onSpreadsheetMetadataList(final List<SpreadsheetMetadata> metadatas,
                                                          final AppContext context) {
                        // ignore
                    }

                    @Override
                    public void onBegin(final HttpMethod method,
                                        final Url url,
                                        final Optional<String> body,
                                        final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onFailure(final HttpMethod method,
                                          final AbsoluteOrRelativeUrl url,
                                          final HttpStatus status,
                                          final Headers headers,
                                          final String body,
                                          final AppContext context) {
                        context.pushHistoryToken(
                                previous.clearAction()
                        );
                    }

                    @Override
                    public void onError(final Object cause,
                                        final AppContext context) {
                        context.pushHistoryToken(
                                previous.clearAction()
                        );
                    }

                    @Override
                    public void onNoResponse(final AppContext context) {

                    }
                }
        );
        context.spreadsheetMetadataFetcher()
                .deleteSpreadsheetMetadata(this.id());
    }
}
