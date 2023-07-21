/**
 * MIT License
 *
 * Copyright (c) 2020, 2023 Mark Schmieder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the FxCadGui Library
 *
 * You should have received a copy of the MIT License along with the FxCadGui
 * Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/fxcadgui
 */
package com.mhschmieder.fxcadgui;

import java.net.URL;

import com.mhschmieder.commonstoolkit.util.SystemType;
import com.mhschmieder.fxguitoolkit.stage.NoticeBox;

/**
 * This is a utility class for grabbing Help resources and pop-ups for CAD
 * features.
 */
public final class CadHelpUtilities {

    public static NoticeBox getGraphicsImportHelp( final SystemType systemType ) {
        // Get the URL associated with the JAR-loaded HTML-based Help file.
        final URL graphicsImportHelpUrl = getGraphicsImportHelpAsUrl();

        // Make a Notice Box to display the local Help until dismissed.
        final String graphicsImportHelpBanner = CadMessageFactory.getGraphicsImportHelpBanner();
        final NoticeBox graphicsImportHelp = new NoticeBox( systemType,
                                                            graphicsImportHelpBanner,
                                                            graphicsImportHelpUrl );

        return graphicsImportHelp;
    }

    @SuppressWarnings("nls")
    public static URL getGraphicsImportHelpAsUrl() {
        // Get the URL associated with the JAR-loaded HTML-based Help file.
        final URL graphicsImportHelpUrl = CadHelpUtilities.class.getResource( 
                "/html/GraphicsImportHelp.html" );

        return graphicsImportHelpUrl;
    }
}
