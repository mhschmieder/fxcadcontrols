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

/**
 * {@code CadMessageFactory} is a factory class for methods related to CAD
 * messages.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class CadMessageFactory {

    /**
     * The default constructor is disabled, as this is a static factory class.
     */
    private CadMessageFactory() {}


    public static final String getCopyReferencePointTitle() {
        return "Copy Reference Point"; //$NON-NLS-1$
    }

    public static final String getGraphicsImportHelpBanner() {
        return "Graphics Import Help"; //$NON-NLS-1$
    }

    public static final String getGraphicsImportOptionsMasthead() {
        return "Graphics Import Options"; //$NON-NLS-1$
    }

    public static final String getInsertReferencePointTitle() {
        return "Insert Reference Point"; //$NON-NLS-1$
    }

    public static final String getSurfaceMaterialTooltip() {
        return "Double-Click for List of Materials from Elements of Acoustical Engineering (Olson); Click ESC to Cancel and Exit List and Cell"; //$NON-NLS-1$
    }
}
