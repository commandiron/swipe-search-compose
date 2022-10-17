package com.commandiron.swipe_search_compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SwipeSearch(
    modifier: Modifier = Modifier,
    textFieldAlignment: Alignment = Alignment.BottomCenter,
    textValue: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    bgColor: Color = Color(0xFFF0F0F3),
    containerColor: Color = Color(0xFF8E8E93),
    contentColor: Color = Color(0xFFFFFFFF),
    initialFontSize: TextUnit = 14.sp,
    targetFontSize: TextUnit = initialFontSize * 1.25f,
    initialWidth: Dp = 94.dp,
    targetWidth: Dp = Dp.Infinity,
    initialHeight: Dp = 36.dp,
    targetHeight: Dp = initialHeight * 1.4f,
    placeHolderText: String = "Search"
) {
    val textFieldValue = remember { mutableStateOf(TextFieldValue(textValue)) }

    val fontSizeState = remember { mutableStateOf(initialFontSize.value) }
    val animatedFontSize by animateFloatAsState(
        targetValue = fontSizeState.value,
        animationSpec = tween(
            durationMillis = 400
        )
    )

    val maximumWidth = remember { mutableStateOf(initialWidth) }

    val widthState = remember { mutableStateOf(initialWidth) }
    val animatedWidth by animateDpAsState(
        targetValue = widthState.value,
        animationSpec = tween(
            durationMillis = 400
        )
    )

    val maximumHeight = remember { mutableStateOf(initialWidth) }

    val heightState = remember { mutableStateOf(initialHeight) }
    val animatedHeight by animateDpAsState(
        targetValue = heightState.value,
        animationSpec = tween(
            durationMillis = 400
        )
    )
    val isFocused = interactionSource.collectIsFocusedAsState().value
    DisposableEffect(isFocused){
        if(isFocused) {
            fontSizeState.value = targetFontSize.value

            widthState.value = if(targetWidth == Dp.Infinity || targetWidth == Dp.Unspecified) {
                maximumWidth.value
            } else targetWidth

            heightState.value = if(targetHeight == Dp.Infinity || targetHeight == Dp.Unspecified) {
                maximumHeight.value
            } else targetHeight
        }
        onDispose {
            fontSizeState.value = initialFontSize.value
            widthState.value = initialWidth
            heightState.value = initialHeight
        }
    }

    val focusManager = LocalFocusManager.current

    val focusRequester = remember { FocusRequester() }
    val draggableState = rememberDraggableState(
        onDelta = { delta ->
            if (delta > 40) {
                focusRequester.requestFocus()
            }
            if (delta < -40) {
                focusManager.clearFocus()
            }
        }
    )

    Box(
        modifier = modifier
            .draggable(
                state = draggableState,
                orientation = Orientation.Vertical
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        contentAlignment = textFieldAlignment
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isFocused) {
                        bgColor
                    } else Color.Unspecified
                )
            ,
            contentAlignment = Alignment.Center
        ) {
            maximumWidth.value = maxWidth
            maximumHeight.value = maxHeight
            BasicTextField(
                value = textFieldValue.value,
                onValueChange = {
                    if(!isFocused && it.text.isNotEmpty()) {
                        textFieldValue.value =
                            TextFieldValue(
                                text = it.text,
                                selection = TextRange(0,it.text.lastIndex + 1)
                            )
                    }else {
                        textFieldValue.value =
                            TextFieldValue(
                                text = it.text,
                                selection = TextRange(it.text.lastIndex + 1)
                            )
                    }
                    onValueChange(it.text)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                enabled = enabled,
                readOnly = readOnly,
                textStyle = TextStyle(
                    color = contentColor,
                    fontSize = animatedFontSize.sp
                ),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                visualTransformation = visualTransformation,
                onTextLayout = onTextLayout,
                interactionSource = interactionSource,
                cursorBrush = Brush.verticalGradient(
                    listOf(
                        contentColor,
                        contentColor
                    )
                ),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .width(animatedWidth)
                            .height(animatedHeight)
                            .clip(CircleShape)
                            .background(containerColor)
                    ) {
                        Box(Modifier.align(Alignment.CenterVertically), Alignment.CenterStart) {
                            Icon(
                                modifier = Modifier
                                    .height(animatedHeight / 2)
                                    .padding(
                                        start = 12.dp,
                                        end = animatedHeight / 8
                                    ),
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = contentColor
                            )
                        }
                        if(textValue.isEmpty()) {
                            Box(Modifier.align(Alignment.CenterVertically), Alignment.CenterStart) {
                                innerTextField()
                                Text(
                                    text = placeHolderText,
                                    color = contentColor,
                                    fontSize = animatedFontSize.sp
                                )
                            }
                        } else {
                            Box(Modifier.align(Alignment.CenterVertically), Alignment.CenterStart) {
                                if(isFocused) {
                                    innerTextField()
                                } else {
                                    Text(
                                        text = placeHolderText,
                                        color = contentColor,
                                        fontSize = animatedFontSize.sp
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}