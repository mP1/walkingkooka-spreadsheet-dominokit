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
import walkingkooka.net.http.HttpMethod;

import java.util.Optional;

/**
 * The test version of this class only prints the parameters and does not actually fetch anything.
 * TODO add methods to stub class within a test and eventually fire events when invoked.
 */
final class FetcherFetch {

    static void fetch(final HttpMethod method,
                      final AbsoluteOrRelativeUrl url,
                      final Optional<String> body,
                      final Fetcher fetcher) {
        System.out.println(
                method + " " + url + "\n" +
                        body
        );
    }
}
