package com.example.tudisenyo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.example.tudisenyo.ui.theme.TuDisenyoTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DesignEditorScreen()
                }
            }
        }
    }
}

private enum class EditorItemKind {
    Image,
    Text
}

private data class EditorItem(
    val id: Long,
    val kind: EditorItemKind,
    val uri: Uri? = null,
    val text: String = "Editar",
    val offset: Offset = Offset.Zero,
    val scale: Float = 1f,
    val rotation: Float = 0f,
    val isEditing: Boolean = false
)

@Composable
private fun DesignEditorScreen() {
    val items = remember { mutableStateListOf<EditorItem>() }

    var nextId by remember { mutableStateOf(1L) }
    var selectedItemId by remember { mutableStateOf<Long?>(null) }

    fun updateItem(id: Long, transform: (EditorItem) -> EditorItem) {
        val index = items.indexOfFirst { it.id == id }
        if (index != -1) {
            items[index] = transform(items[index])
        }
    }

    fun finishEditing() {
        for (i in items.indices) {
            items[i] = items[i].copy(isEditing = false)
        }
    }

    fun addTextItem() {
        finishEditing()

        val id = nextId
        nextId += 1

        items.add(
            EditorItem(
                id = id,
                kind = EditorItemKind.Text,
                text = "Your text",
                isEditing = true
            )
        )

        selectedItemId = id
    }

    fun deleteSelectedItem() {
        val id = selectedItemId ?: return
        val index = items.indexOfFirst { it.id == id }

        if (index != -1) {
            items.removeAt(index)
        }

        selectedItemId = null
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult

        finishEditing()

        val id = nextId
        nextId += 1

        items.add(
            EditorItem(
                id = id,
                kind = EditorItemKind.Image,
                uri = uri
            )
        )

        selectedItemId = id
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F8))
            .padding(16.dp)
    ) {
        Text(
            text = "Design Editor",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(390.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1500FF))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            TShirtBase(
                modifier = Modifier
                    .size(230.dp)
                    .align(Alignment.Center)
            )

            items.forEach { item ->
                key(item.id) {
                    EditorObject(
                        item = item,
                        selected = item.id == selectedItemId,
                        onSelect = {
                            selectedItemId = item.id
                        },
                        onStartEditing = {
                            updateItem(item.id) {
                                it.copy(isEditing = true)
                            }
                        },
                        onTextChanged = { newText ->
                            updateItem(item.id) {
                                it.copy(text = newText)
                            }
                        },
                        onTransform = { pan, zoom, rotation ->
                            updateItem(item.id) { current ->
                                current.copy(
                                    offset = current.offset + pan,
                                    scale = (current.scale * zoom).coerceIn(0.25f, 5f),
                                    rotation = current.rotation + rotation
                                )
                            }
                        }
                    )
                }
            }

            Text(
                text = "Tap text to edit. Drag items. Pinch to scale/rotate.",
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    pickImageLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Text("Upload Image")
            }

            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = { addTextItem() }
            ) {
                Text("Add Text")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                enabled = selectedItemId != null,
                onClick = { deleteSelectedItem() }
            ) {
                Text("Delete Selected")
            }

            Button(
                modifier = Modifier.weight(1f),
                enabled = items.any { it.isEditing },
                onClick = { finishEditing() }
            ) {
                Text("Done Editing")
            }
        }
    }
}

@Composable
private fun BoxScope.EditorObject(
    item: EditorItem,
    selected: Boolean,
    onSelect: () -> Unit,
    onStartEditing: () -> Unit,
    onTextChanged: (String) -> Unit,
    onTransform: (pan: Offset, zoom: Float, rotation: Float) -> Unit
) {
    val transformModifier =
        if (item.isEditing) {
            Modifier
        } else {
            Modifier.pointerInput(item.id) {
                detectTransformGestures { _, pan, zoom, rotation ->
                    onTransform(pan, zoom, rotation)
                }
            }
        }

    val selectionModifier =
        if (selected) {
            Modifier.border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(6.dp)
            )
        } else {
            Modifier
        }

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .zIndex(if (selected) 1f else 0f)
            .graphicsLayer {
                translationX = item.offset.x
                translationY = item.offset.y
                scaleX = item.scale
                scaleY = item.scale
                rotationZ = item.rotation
            }
            .pointerInput(item.id, item.kind) {
                detectTapGestures(
                    onTap = {
                        onSelect()

                        if (item.kind == EditorItemKind.Text) {
                            onStartEditing()
                        }
                    }
                )
            }
            .then(transformModifier)
            .then(selectionModifier)
            .padding(4.dp)
    ) {
        when (item.kind) {
            EditorItemKind.Image -> {
                AsyncImage(
                    model = item.uri,
                    contentDescription = "Added design image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(130.dp)
                )
            }

            EditorItemKind.Text -> {
                if (item.isEditing) {
                    EditableText(
                        text = item.text,
                        onTextChanged = onTextChanged
                    )
                } else {
                    Text(
                        text = item.text,
                        color = Color.White,
                        fontSize = 32.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun EditableText(
    text: String,
    onTextChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 32.sp
        ),
        modifier = Modifier
            .background(
                color = Color.Black.copy(alpha = 0.25f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(8.dp)
            .widthIn(min = 80.dp)
            .focusRequester(focusRequester)
    )
}

@Composable
private fun TShirtBase(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val shirtPath = Path().apply {
            moveTo(w * 0.38f, h * 0.18f)

            quadraticBezierTo(
                w * 0.40f,
                h * 0.32f,
                w * 0.50f,
                h * 0.32f
            )

            quadraticBezierTo(
                w * 0.60f,
                h * 0.32f,
                w * 0.62f,
                h * 0.18f
            )

            lineTo(w * 0.80f, h * 0.24f)

            quadraticBezierTo(
                w * 0.90f,
                h * 0.27f,
                w * 0.88f,
                h * 0.39f
            )

            lineTo(w * 0.85f, h * 0.54f)

            quadraticBezierTo(
                w * 0.84f,
                h * 0.58f,
                w * 0.79f,
                h * 0.58f
            )

            lineTo(w * 0.70f, h * 0.58f)
            lineTo(w * 0.70f, h * 0.84f)

            quadraticBezierTo(
                w * 0.70f,
                h * 0.93f,
                w * 0.60f,
                h * 0.93f
            )

            lineTo(w * 0.40f, h * 0.93f)

            quadraticBezierTo(
                w * 0.30f,
                h * 0.93f,
                w * 0.30f,
                h * 0.84f
            )

            lineTo(w * 0.30f, h * 0.58f)
            lineTo(w * 0.21f, h * 0.58f)

            quadraticBezierTo(
                w * 0.16f,
                h * 0.58f,
                w * 0.15f,
                h * 0.54f
            )

            lineTo(w * 0.12f, h * 0.39f)

            quadraticBezierTo(
                w * 0.10f,
                h * 0.27f,
                w * 0.20f,
                h * 0.24f
            )

            close()
        }

        drawPath(
            path = shirtPath,
            color = Color(0x661A2BFF)
        )

        drawPath(
            path = shirtPath,
            color = Color(0xFF0818B8),
            style = Stroke(width = 8.dp.toPx())
        )
    }
}