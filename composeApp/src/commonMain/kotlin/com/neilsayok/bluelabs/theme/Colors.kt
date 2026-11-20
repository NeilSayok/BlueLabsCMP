package com.neilsayok.bluelabs.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val CODE_BLOCK_BACKGROUND_COLOR = Color(0xFF282A35)


// ColorTokens.kt

object ColorLightTokens {
    val Primary = Color(0xFF0759D3)
    val OnPrimary = Color.White
    val PrimaryContainer = Color(0xFFD8E8FF)
    val OnPrimaryContainer = Color(0xFF172431)
    val InversePrimary = Color(0xFF8BB8FF)
    val Secondary = Color(0xFFDDECE9)
    val OnSecondary = Color(0xFF163E36)
    val SecondaryContainer = Color(0xFFDDECE9)
    val OnSecondaryContainer = Color(0xFF163E36)
    val Tertiary = Color(0xFF0759D3)
    val OnTertiary = Color.White
    val TertiaryContainer = Color(0xFFD8E8FF)
    val OnTertiaryContainer = Color(0xFF172431)
    val Background = Color(0xFFF7F9FC)
    val OnBackground = Color(0xFF172431)
    val Surface = Color.White
    val OnSurface = Color(0xFF172431)
    val SurfaceVariant = Color(0xFFEDF2F8)
    val OnSurfaceVariant = Color(0xFF526171)
    val InverseSurface = Color(0xFF172431)
    val InverseOnSurface = Color.White
    val Error = Color(0xFFA32228)
    val OnError = Color.White
    val ErrorContainer = Color(0xFFFFDAD4)
    val OnErrorContainer = Color(0xFF410001)
    val Outline = Color(0xFFC9D2DE)
    val OutlineVariant = Color(0xFFDFE5EE)
    val Scrim = Color(0x66000000)
    val SurfaceBright = Color.White
    val SurfaceDim = Color(0xFFEDF2F8)
    val SurfaceContainer = Color(0xFFEDF2F8)
    val SurfaceContainerLowest = Color.White
    val SurfaceContainerLow = Color(0xFFF7F9FC)
    val SurfaceContainerHigh = Color(0xFFE5ECF5)
    val SurfaceContainerHighest = Color(0xFFD8E8FF)
}

object ColorDarkTokens {
    val Primary = Color(0xFF8BB8FF)
    val OnPrimary = Color(0xFF111214)
    val PrimaryContainer = Color(0xFF172B47)
    val OnPrimaryContainer = Color(0xFFF1F3F5)
    val InversePrimary = Color(0xFF0759D3)
    val Secondary = Color(0xFF163E36)
    val OnSecondary = Color(0xFFF1F3F5)
    val SecondaryContainer = Color(0xFF163E36)
    val OnSecondaryContainer = Color(0xFFF1F3F5)
    val Tertiary = Color(0xFF8BB8FF)
    val OnTertiary = Color(0xFF111214)
    val TertiaryContainer = Color(0xFF172B47)
    val OnTertiaryContainer = Color(0xFFF1F3F5)
    val Background = Color(0xFF111214)
    val OnBackground = Color(0xFFF1F3F5)
    val Surface = Color(0xFF15171A)
    val OnSurface = Color(0xFFF1F3F5)
    val SurfaceVariant = Color(0xFF1C1E22)
    val OnSurfaceVariant = Color(0xFFAEB7C3)
    val InverseSurface = Color(0xFFF1F3F5)
    val InverseOnSurface = Color(0xFF111214)
    val Error = Color(0xFFFFB3B3)
    val OnError = Color(0xFF111214)
    val ErrorContainer = Color(0xFF93000A)
    val OnErrorContainer = Color(0xFFFFDAD4)
    val Outline = Color(0xFF414750)
    val OutlineVariant = Color(0xFF32373E)
    val Scrim = Color(0x88000000)
    val SurfaceBright = Color(0xFF1C1E22)
    val SurfaceDim = Color(0xFF111214)
    val SurfaceContainer = Color(0xFF1C1E22)
    val SurfaceContainerLowest = Color(0xFF111214)
    val SurfaceContainerLow = Color(0xFF15171A)
    val SurfaceContainerHigh = Color(0xFF25282D)
    val SurfaceContainerHighest = Color(0xFF172B47)
}

val LightColorScheme = lightColorScheme(
    primary = ColorLightTokens.Primary,
    onPrimary = ColorLightTokens.OnPrimary,
    primaryContainer = ColorLightTokens.PrimaryContainer,
    onPrimaryContainer = ColorLightTokens.OnPrimaryContainer,
    inversePrimary = ColorLightTokens.InversePrimary,
    secondary = ColorLightTokens.Secondary,
    onSecondary = ColorLightTokens.OnSecondary,
    secondaryContainer = ColorLightTokens.SecondaryContainer,
    onSecondaryContainer = ColorLightTokens.OnSecondaryContainer,
    tertiary = ColorLightTokens.Tertiary,
    onTertiary = ColorLightTokens.OnTertiary,
    tertiaryContainer = ColorLightTokens.TertiaryContainer,
    onTertiaryContainer = ColorLightTokens.OnTertiaryContainer,
    background = ColorLightTokens.Background,
    onBackground = ColorLightTokens.OnBackground,
    surface = ColorLightTokens.Surface,
    onSurface = ColorLightTokens.OnSurface,
    surfaceVariant = ColorLightTokens.SurfaceVariant,
    onSurfaceVariant = ColorLightTokens.OnSurfaceVariant,
    surfaceTint = ColorLightTokens.Primary,
    inverseSurface = ColorLightTokens.InverseSurface,
    inverseOnSurface = ColorLightTokens.InverseOnSurface,
    error = ColorLightTokens.Error,
    onError = ColorLightTokens.OnError,
    errorContainer = ColorLightTokens.ErrorContainer,
    onErrorContainer = ColorLightTokens.OnErrorContainer,
    outline = ColorLightTokens.Outline,
    outlineVariant = ColorLightTokens.OutlineVariant,
    scrim = ColorLightTokens.Scrim,
    surfaceBright = ColorLightTokens.SurfaceBright,
    surfaceDim = ColorLightTokens.SurfaceDim,
    surfaceContainer = ColorLightTokens.SurfaceContainer,
    surfaceContainerLowest = ColorLightTokens.SurfaceContainerLowest,
    surfaceContainerLow = ColorLightTokens.SurfaceContainerLow,
    surfaceContainerHigh = ColorLightTokens.SurfaceContainerHigh,
    surfaceContainerHighest = ColorLightTokens.SurfaceContainerHighest,
)

val DarkColorScheme = darkColorScheme(
    primary = ColorDarkTokens.Primary,
    onPrimary = ColorDarkTokens.OnPrimary,
    primaryContainer = ColorDarkTokens.PrimaryContainer,
    onPrimaryContainer = ColorDarkTokens.OnPrimaryContainer,
    inversePrimary = ColorDarkTokens.InversePrimary,
    secondary = ColorDarkTokens.Secondary,
    onSecondary = ColorDarkTokens.OnSecondary,
    secondaryContainer = ColorDarkTokens.SecondaryContainer,
    onSecondaryContainer = ColorDarkTokens.OnSecondaryContainer,
    tertiary = ColorDarkTokens.Tertiary,
    onTertiary = ColorDarkTokens.OnTertiary,
    tertiaryContainer = ColorDarkTokens.TertiaryContainer,
    onTertiaryContainer = ColorDarkTokens.OnTertiaryContainer,
    background = ColorDarkTokens.Background,
    onBackground = ColorDarkTokens.OnBackground,
    surface = ColorDarkTokens.Surface,
    onSurface = ColorDarkTokens.OnSurface,
    surfaceVariant = ColorDarkTokens.SurfaceVariant,
    onSurfaceVariant = ColorDarkTokens.OnSurfaceVariant,
    surfaceTint = ColorDarkTokens.Primary,
    inverseSurface = ColorDarkTokens.InverseSurface,
    inverseOnSurface = ColorDarkTokens.InverseOnSurface,
    error = ColorDarkTokens.Error,
    onError = ColorDarkTokens.OnError,
    errorContainer = ColorDarkTokens.ErrorContainer,
    onErrorContainer = ColorDarkTokens.OnErrorContainer,
    outline = ColorDarkTokens.Outline,
    outlineVariant = ColorDarkTokens.OutlineVariant,
    scrim = ColorDarkTokens.Scrim,
    surfaceBright = ColorDarkTokens.SurfaceBright,
    surfaceDim = ColorDarkTokens.SurfaceDim,
    surfaceContainer = ColorDarkTokens.SurfaceContainer,
    surfaceContainerLowest = ColorDarkTokens.SurfaceContainerLowest,
    surfaceContainerLow = ColorDarkTokens.SurfaceContainerLow,
    surfaceContainerHigh = ColorDarkTokens.SurfaceContainerHigh,
    surfaceContainerHighest = ColorDarkTokens.SurfaceContainerHighest,
)

val lightTagColors = listOf(
    Color(0xFF0066FF),
    Color(0xFF00C853),
    Color(0xFFFF6D00),
    Color(0xFFD81B60),
)

val darkTagColors = listOf(
    Color(0xFF82B1FF),
    Color(0xFFC6FF00),
    Color(0xFFFFAB00),
    Color(0xFFFF4081),
)


