/*
 * MIT License
 *
 * Copyright (c) 2020, 2025 Mark Schmieder
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
package com.mhschmieder.fxcadgui.control;

import com.mhschmieder.fxcadgraphics.SurfaceMaterial;
import com.mhschmieder.fxguitoolkit.control.IntegerSelector;
import com.mhschmieder.fxguitoolkit.control.ListViewUtilities;
import com.mhschmieder.fxguitoolkit.control.XComboBox;
import com.mhschmieder.jcommons.util.ClientProperties;

public final class CadControlFactory {

    public static XComboBox< SurfaceMaterial > getSurfaceMaterialSelector(
            final ClientProperties pClientProperties,
            final String tooltipText,
            final boolean applyToolkitCss ) {
        final SurfaceMaterial[] supportedValues = {
                SurfaceMaterial.ACOUSTIC_TILE_ON_RIGID_SURF_KF,
                SurfaceMaterial.BRICK_WALL_PAINTED_LB,
                SurfaceMaterial.BRICK_WALL_UNPAINTED_LB,
                SurfaceMaterial.CARPET_HEAVY_ON_CONCRETE_CH,
                SurfaceMaterial.CONCRETE_BLOCK_PAINTED_CH,
                SurfaceMaterial.CONCRETE_BLOCK_UNPAINTED_CH,
                SurfaceMaterial.PLASTER_ON_LATHE_CH,
                SurfaceMaterial.POURED_CONCRETE_PAINTED_LB,
                SurfaceMaterial.POURED_CONCRETE_UNPAINTED_LB,
                SurfaceMaterial.RIGID,
                SurfaceMaterial.VELOUR_TEN_OZ_PER_YARD_SQR_TOUCHING_WALL_CH };
        return ListViewUtilities.makeLabeledSelector(
                pClientProperties,
                supportedValues,
                tooltipText,
                SurfaceMaterial.defaultValue() );
    }

    public static IntegerSelector getProjectionZonesSelector( final ClientProperties pClientProperties,   
                                                              final boolean applyToolkitCss,
                                                              final String tooltipText) {
        // NOTE: Limit to 12 zones vs. 24 for now, due to moire patterns that
        // occur in integer-based pixel systems such as AWT (JavaFX is
        // floating-point based), when the number of zones is large relative to
        // the number of pixels between start and end points of the Linear Object.
        final int minimumNumberOfProjectionZones = 1;
        final int maximumNumberOfProjectionZones = 12; // 24
        final int projectionZonesIncrement = 1;

        return new IntegerSelector(
                pClientProperties,
                true,
                tooltipText,
                applyToolkitCss,
                false,
                false,
                minimumNumberOfProjectionZones,
                maximumNumberOfProjectionZones,
                projectionZonesIncrement );
    }
}