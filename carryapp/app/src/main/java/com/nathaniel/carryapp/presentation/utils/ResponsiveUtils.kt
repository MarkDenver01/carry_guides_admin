package com.nathaniel.carryapp.presentation.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun responsiveHeightFraction(
    fraction: Float,
    min: Dp = 100.dp,
    max: Dp = 300.dp
): Dp {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val calculated = (screenHeight * fraction).dp
    return calculated.coerceIn(min, max)
}

@Composable
fun responsiveWidthFraction(
    fraction: Float,
    min: Dp = 80.dp,
    max: Dp = 300.dp
): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val calculated = (screenWidth * fraction).dp
    return calculated.coerceIn(min, max)
}

@Composable
fun responsiveTextSp(
    base: Float = 16f,
    scaleFactor: Float = 0.05f,
    min: Float = 12f,
    max: Float = 28f
): TextUnit {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val calculated = (base + (screenWidth * scaleFactor)).coerceIn(min, max)
    return calculated.sp
}

@Composable
fun responsiveDp(
    base: Float,
    designWidthDp: Float = 360f
): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scale = screenWidth / designWidthDp
    return (base * scale).dp
}

@Composable
fun responsiveSp(
    base: Float,
    designWidthDp: Float = 360f
): TextUnit {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scale = screenWidth / designWidthDp
    return (base * scale).sp
}


@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    leadingIcon: ImageVector,
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize, // dynamic default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.LightGray,
                fontSize = fontSize
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = placeholder,
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(6.dp)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White.copy(alpha = 0.6f),
            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.White.copy(alpha = 0.1f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = LocalTextStyle.current.copy(
            color = Color.White,
            fontSize = fontSize
        )
    )
}

@Composable
fun AuthSocialButton(
    icon: Int,
    label: String,
    onClick: () -> Unit,
    height: Dp = LocalResponsiveSizes.current.buttonHeight,
    width: Dp = LocalResponsiveSizes.current.buttonWidth,
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize,
    backgroundColor: Color = Color.White.copy(alpha = 0.08f),
    pressedBackgroundColor: Color = Color(0xFF2E7D32)  // Dark green on press
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest { interaction ->
            isPressed = interaction is PressInteraction.Press || interaction is HoverInteraction.Enter
        }
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) pressedBackgroundColor else backgroundColor,
        label = "ButtonBackgroundAnimation"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isPressed) pressedBackgroundColor else Color.White.copy(alpha = 0.4f),
        label = "ButtonBorderAnimation"
    )

    OutlinedButton(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.5.dp, animatedBorderColor),
        modifier = Modifier
            .width(width)
            .height(height),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = animatedBackgroundColor,
            contentColor = Color.White
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 4.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = fontSize,
            color = Color.White
        )
    }
}

@Composable
fun DynamicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    height: Dp = LocalResponsiveSizes.current.buttonHeight,
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize,
    backgroundColor: Color = Color.White.copy(alpha = 0.08f),
    pressedBackgroundColor: Color = Color(0xFF2E7D32),
    content: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) pressedBackgroundColor else backgroundColor,
        label = "ButtonBackgroundAnimation"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isPressed) pressedBackgroundColor else Color.White.copy(alpha = 0.4f),
        label = "ButtonBorderAnimation"
    )

    Button(
        onClick = onClick,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedBackgroundColor,
            contentColor = Color.White
        ),
        border = BorderStroke(1.5.dp, animatedBorderColor),
        interactionSource = interactionSource
    ) {
        Text(
            text = content,
            fontSize = fontSize,
            color = Color.White
        )
    }
}
