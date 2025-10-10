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

package walkingkooka.spreadsheet.dominokit.fetcher;

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetHateosResourceNames;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.SpreadsheetServerLinkRelations;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenuList;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Optional;

/**
 * Fetcher for {@link SpreadsheetFormatterSelector} end points.
 */
public final class SpreadsheetFormatterFetcher extends Fetcher<SpreadsheetFormatterFetcherWatcher> {

    public static SpreadsheetFormatterFetcher with(final SpreadsheetFormatterFetcherWatcher watcher,
                                                   final AppContext context) {
        return new SpreadsheetFormatterFetcher(
            watcher,
            context
        );
    }

    private SpreadsheetFormatterFetcher(final SpreadsheetFormatterFetcherWatcher watcher,
                                        final AppContext context) {
        super(
            watcher,
            context
        );
    }

    // GET /api/spreadsheet/SpreadsheetId/cell/SpreadsheetExpressionReference/formatter-edit
    public void getCellFormatterEdit(final SpreadsheetId id,
                                     final SpreadsheetExpressionReference cellOrLabel,
                                     final String selector) {
        this.get(
            cellFormatterEditUrl(
                id,
                cellOrLabel,
                selector
            )
        );
    }

    static AbsoluteOrRelativeUrl cellFormatterEditUrl(final SpreadsheetId id,
                                                      final SpreadsheetExpressionReference cellOrLabel,
                                                      final String selector) {
        return SpreadsheetDeltaFetcher.url(
            id,
            cellOrLabel,
            selector.isEmpty() ?
                FORMATTER_EDIT :
                FORMATTER_EDIT.append(
                    UrlPath.parse(selector)
                )
        );
    }

    // GET /api/spreadsheet/SpreadsheetId/cell/SpreadsheetExpressionMenu/formatter-menu
    public void getCellFormatterMenu(final SpreadsheetId id,
                                     final SpreadsheetExpressionReference cellOrLabel) {
        this.get(
            cellFormatterMenuUrl(
                id,
                cellOrLabel
            )
        );
    }

    static AbsoluteOrRelativeUrl cellFormatterMenuUrl(final SpreadsheetId id,
                                                      final SpreadsheetExpressionReference cellOrLabel) {
        return SpreadsheetDeltaFetcher.url(
            id,
            cellOrLabel,
            FORMATTER_MENU
        );
    }

    private final static UrlPath FORMATTER_MENU = UrlPath.ROOT.append(
        SpreadsheetServerLinkRelations.FORMATTER_MENU.toUrlPathName()
            .get()
    );

    // GET /api/spreadsheet/SpreadsheetId/metadata/SpreadsheetMetadataPropertyNameFormatterSelector/edit/SpreadsheetFormatterSelector
    public void getMetadataFormatterEdit(final SpreadsheetId id,
                                         final SpreadsheetMetadataPropertyName<SpreadsheetFormatterSelector> propertyName,
                                         final String selector) {
        this.get(
            metadataFormatterEditUrl(
                id,
                propertyName,
                selector
            )
        );
    }

    static AbsoluteOrRelativeUrl metadataFormatterEditUrl(final SpreadsheetId id,
                                                          final SpreadsheetMetadataPropertyName<SpreadsheetFormatterSelector> propertyName,
                                                          final String selector) {
        return SpreadsheetMetadataFetcher.propertyUrl(
            id,
            propertyName
        ).appendPathName(
            SpreadsheetServerLinkRelations.EDIT.toUrlPathName()
                .get()
        ).appendPath(
            UrlPath.parse(selector)
        );
    }

    private final static UrlPath FORMATTER_EDIT = UrlPath.ROOT.append(
        SpreadsheetServerLinkRelations.FORMATTER_EDIT.toUrlPathName()
            .get()
    );

    // GET /api/formatter/*
    public void getInfoSet() {
        this.get(
            GET_INFO_SET
        );
    }

    private final static AbsoluteOrRelativeUrl GET_INFO_SET = Url.EMPTY_RELATIVE_URL.appendPath(SpreadsheetHttpServer.API_FORMATTER);

    // api/spreadsheet/SpreadsheetId/formatter

    static RelativeUrl url(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(
                SpreadsheetFormatterName.HATEOS_RESOURCE_NAME.toUrlPathName()
            );
    }

    // Fetcher..........................................................................................................

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        final AppContext context = this.context;

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse(context);
                break;
            case "SpreadsheetFormatterInfoSet":
                // GET http://server/api/spreadsheet/1/formatter
                this.watcher.onSpreadsheetFormatterInfoSet(
                    this.parse(
                        body,
                        SpreadsheetFormatterInfoSet.class
                    ), // edit
                    context
                );
                break;
            case "SpreadsheetFormatterSelectorEdit":
                // http://server/api/spreadsheet/1/metadata/SpreadsheetMetadataPropertyNameFormatterSelector/edit/SpreadsheetFormatterSelector
                // http://server/api/spreadsheet/1/cell/SpreadsheetExpressionReference/formatter-edit/SpreadsheetFormatterSelector
                this.watcher.onSpreadsheetFormatterSelectorEdit(
                    SpreadsheetMetadataFetcher.extractSpreadsheetIdOrFail(url),
                    extractOptionalCellOrLabel(url.path()),
                    this.parse(
                        body,
                        SpreadsheetFormatterSelectorEdit.class
                    ), // edit
                    context
                );
                break;
            case "SpreadsheetFormatterMenuList":
                // http://server/api/spreadsheet/1/cell/SpreadsheetExpressionReference/formatter-menu
                this.watcher.onSpreadsheetFormatterMenuList(
                    SpreadsheetMetadataFetcher.extractSpreadsheetIdOrFail(url),
                    extractCellOrLabel(url.path()),
                    this.parse(
                        body,
                        SpreadsheetFormatterMenuList.class
                    ), // menu
                    context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    // http://server/api/spreadsheet/1/metadata/SpreadsheetMetadataPropertyNameFormatterSelector/edit/SpreadsheetFormatterSelector
    //              01   2           3 4        5                                                6    7
    // http://server/api/spreadsheet/1/cell/SpreadsheetExpressionReference/formatter-edit/SpreadsheetFormatterSelector
    //              01   2           3 4    5                              6              7
    // @VisibleForTesting
    static Optional<SpreadsheetExpressionReference> extractOptionalCellOrLabel(final UrlPath path) {
        final List<UrlPathName> nameList = path.namesList();
        return Optional.ofNullable(
            SpreadsheetHateosResourceNames.CELL.toUrlPathName()
                .equals(
                    nameList.get(4)
                ) ?
                SpreadsheetSelection.parseExpressionReference(
                    nameList.get(5)
                        .value()
                ) :
                null
        );
    }

    // http://server/api/spreadsheet/1/cell/SpreadsheetExpressionReference/formatter-edit/SpreadsheetFormatterSelector
    //              01   2           3 4    5                              6              7
    // @VisibleForTesting
    static SpreadsheetExpressionReference extractCellOrLabel(final UrlPath path) {
        final List<UrlPathName> nameList = path.namesList();
        return SpreadsheetSelection.parseExpressionReference(
            nameList.get(5)
                .value()
        );
    }
}
