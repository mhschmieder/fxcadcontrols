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
package com.mhschmieder.fxcadgui.action;

import com.mhschmieder.commonstoolkit.util.ClientProperties;
import com.mhschmieder.fxguitoolkit.action.ActionFactory;
import com.mhschmieder.fxguitoolkit.action.XAction;

/**
 * This is a utility class for making labeled actions for CAD.
 */
public final class CadLabeledActionFactory {

    @SuppressWarnings("nls") public static final String BUNDLE_NAME =
                                                                    "com.mhschmieder.fxcadgui.action.CadActionLabels";

    @SuppressWarnings("nls")
    public static XAction getArchitectureToolChoice( final ClientProperties pClientProperties ) {
        return getMouseToolChoice( pClientProperties,
                                   "architectureTool",
                                   "/icons/yusukeKamiyamane/LayerShapeLine16.png" );
    }

    @SuppressWarnings("nls")
    public static XAction getMouseToolChoice( final ClientProperties pClientProperties,
                                              final String itemName,
                                              final String jarRelativeIconFilename ) {
        return ActionFactory.makeChoice( pClientProperties,
                                         BUNDLE_NAME,
                                         "mouseTool",
                                         itemName,
                                         jarRelativeIconFilename );
    }

    @SuppressWarnings("nls")
    public static XAction getRotateToolChoice( final ClientProperties pClientProperties ) {
        return getMouseToolChoice( pClientProperties,
                                   "rotateTool",
                                   "/icons/everaldo/RotateCWLight16.png" );
    }

    @SuppressWarnings("nls")
    public static XAction getSelectToolChoice( final ClientProperties pClientProperties ) {
        return getMouseToolChoice( pClientProperties,
                                   "selectTool",
                                   "/icons/everaldo/Select16.png" );
    }

    @SuppressWarnings("nls")
    public static XAction getViewZoomToDrawingLimitsAction( final ClientProperties pClientProperties ) {
        return ActionFactory.makeAction( pClientProperties,
                                         BUNDLE_NAME,
                                         "view",
                                         "zoomToDrawingLimits",
                                         "/icons/everaldo/ViewMagFit16.png" );
    }

    @SuppressWarnings("nls")
    public static XAction getViewZoomToPredictionPlaneAction( final ClientProperties pClientProperties ) {
        return ActionFactory.makeAction( pClientProperties,
                                         BUNDLE_NAME,
                                         "view",
                                         "zoomToPredictionPlane",
                                         "/icons/everaldo/ViewMagToReference16.png" );
    }

    @SuppressWarnings("nls")
    public static XAction getZoomToolChoice( final ClientProperties pClientProperties ) {
        return getMouseToolChoice( pClientProperties,
                                   "zoomTool",
                                   "/icons/everaldo/ViewMag16.png" );
    }

}
