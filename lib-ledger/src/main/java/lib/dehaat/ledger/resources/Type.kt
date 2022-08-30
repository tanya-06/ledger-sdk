package lib.dehaat.ledger.resources

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val Typography = Typography(
    h4 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    h5 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    h6 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
        color = Color.White
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = TextBlack,
        lineHeight = 19.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = TextBlack,
        lineHeight = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.White,
        lineHeight = 16.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextBlack,
        lineHeight = 14.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextGrey,
        lineHeight = 14.sp
    )
)

/**
 * fontWeight = FontWeight.Normal,
 * fontSize = 14.sp,
 * color = TextBlack
 */
@Composable
fun subTitleTextStyle() = MaterialTheme.typography.subtitle1

/**
 * fontWeight = FontWeight.Normal,
 * fontSize = 14.sp,
 * color = TextGrey
 */
@Composable
fun subTitleTextStyle2() = MaterialTheme.typography.subtitle2

/**
 * fontWeight = FontWeight.Normal,
 * fontSize = 16.sp,
 * color = TextBlack
 */
@Composable
fun bodyTextStyle() = MaterialTheme.typography.body1

/**
 * fontWeight = FontWeight.Normal,
 * fontSize = 18.sp,
 * color = TextBlack
 */
@Composable
fun captionTextStyle() = MaterialTheme.typography.caption

/**
 * fontWeight = FontWeight.Bold,
 * fontSize = 14.sp,
 * color = TextBlack
 */
@Composable
fun subTitleBoldTextStyle() = MaterialTheme.typography.h6

/**
 * fontWeight = FontWeight.Bold,
 * fontSize = 16.sp,
 * color = TextBlack
 */
@Composable
fun bodyTextBoldStyle() = MaterialTheme.typography.h5

/**
 * fontWeight = FontWeight.Bold,
 * fontSize = 18.sp,
 * color = TextBlack
 */
@Composable
fun captionTextBoldStyle() = MaterialTheme.typography.h4

/**
 * fontWeight = FontWeight.W500,
 * fontSize = 18.sp,
 * color = WHITE
 */
@Composable
fun buttonTextStyle() = MaterialTheme.typography.button

@Composable
fun textMedium14Sp(textColor: Color = TextBlack) =
    text14Sp(fontWeight = FontWeight.Medium, textColor = textColor)

@Composable
fun textMedium16Sp(textColor: Color = TextBlack) =
    TextStyle(
        fontSize = 16.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        color = textColor,
    )

@Composable
fun textMedium18Sp(textColor: Color = TextBlack) =
    TextStyle(
        fontSize = 18.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Medium,
        color = textColor,
    )

@Composable
fun textBold14Sp(textColor: Color = TextBlack) =
    TextStyle(
        fontSize = 14.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Bold,
        color = textColor
    )

@Composable
fun textMedium20Sp(textColor: Color = TextBlack) =
    TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = textColor,
    )

@Composable
fun text14Sp(
    textColor: Color = TextBlack,
    fontWeight: FontWeight = FontWeight.Normal,
    lineHeight: TextUnit = 14.sp,
    textAlign: TextAlign? = null
) = TextStyle(
    fontSize = 14.sp,
    lineHeight = lineHeight,
    fontWeight = fontWeight,
    color = textColor,
    textAlign = textAlign
)

@Composable
fun text16Sp(
    textColor: Color = TextBlack,
    fontWeight: FontWeight = FontWeight.Normal,
    lineHeight: TextUnit = 16.sp
) = TextStyle(
    fontSize = 16.sp,
    lineHeight = lineHeight,
    fontWeight = fontWeight,
    color = textColor,
)

@Composable
fun textMedium(
    fontSize: TextUnit,
    textColor: Color = TextBlack,
    lineHeight: TextUnit = 16.sp
) = TextStyle(
    fontSize = fontSize,
    lineHeight = lineHeight,
    fontWeight = FontWeight.Medium,
    color = textColor,
)

@Composable
fun textBold(
    fontSize: TextUnit,
    textColor: Color = TextBlack
) = TextStyle(
    fontSize = fontSize,
    fontWeight = FontWeight.Bold,
    color = textColor,
)

@Composable
fun text18Sp(
    textColor: Color = TextBlack,
    fontWeight: FontWeight = FontWeight.Normal
) =
    TextStyle(
        fontSize = 18.sp,
        lineHeight = 18.sp,
        fontWeight = fontWeight,
        color = textColor,
    )

@Composable
fun text12Sp(
    textColor: Color = TextBlack,
    fontWeight: FontWeight = FontWeight.Normal,
    lineHeight: TextUnit = 12.sp
) = TextStyle(
    fontSize = 12.sp,
    lineHeight = lineHeight,
    fontWeight = fontWeight,
    color = textColor,
)