package com.sagendy.dailyquotes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.State
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sagendy.dailyquotes.ui.theme.DailyQuotesTheme
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyQuotesTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppRoot()
                }
            }
        }
    }
}

@Composable
private fun AppRoot() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            SplashScreen(onFinished = {
                navController.navigate(Routes.Main) {
                    popUpTo(Routes.Splash) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable(Routes.Main) {
            MainScreen()
        }
    }
}

@Composable
private fun SplashScreen(onFinished: () -> Unit) {
    var animateIn by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "splashAlpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0.9f,
        animationSpec = tween(durationMillis = 800),
        label = "splashScale"
    )

    LaunchedEffect(Unit) {
        animateIn = true
        delay(2000)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AppBackground {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .alpha(alpha)
                    .scale(scale)
            ) {
                Text(
                    text = "Daily Quotes",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = FontFamily.Cursive,
                        letterSpacing = 1.2.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Small words. Big shifts.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Serif
                    ),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun MainScreen(viewModel: QuotesViewModel = viewModel()) {
    val context = LocalContext.current
    val quote by viewModel.currentQuote
    val pulse by rememberInfiniteTransition(label = "newQuotePulse").animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "newQuotePulseValue"
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        AppBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .widthIn(max = 560.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Daily Quotes",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = FontFamily.Cursive
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "A calm minute for your mind",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Serif
                        ),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 560.dp)
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    QuoteCard(quote = quote)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.nextQuote() },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.graphicsLayer {
                                scaleX = pulse
                                scaleY = pulse
                            }
                        ) {
                            Text(text = "New Quote")
                        }
                        FilledTonalIconButton(
                            onClick = { shareQuote(context, quote) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Share quote"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuoteCard(quote: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Quote of the moment",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = FontFamily.Serif
                ),
                color = MaterialTheme.colorScheme.primary
            )
        Crossfade(
            targetState = quote,
            animationSpec = tween(durationMillis = 350),
            label = "quoteCrossfade"
        ) { currentQuote ->
            Text(
                text = currentQuote,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    lineHeight = 28.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        }
    }
}

private fun shareQuote(context: Context, quote: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, quote)
    }
    val chooser = Intent.createChooser(sendIntent, "Share quote")
    context.startActivity(chooser)
}

class QuotesViewModel : ViewModel() {
    private val quotes = listOf(
        "Success is the sum of small efforts, repeated day in and day out.",
        "Start where you are. Use what you have. Do what you can.",
        "Your future is created by what you do today, not tomorrow.",
        "The best way to predict the future is to create it.",
        "Believe you can and you're halfway there."
    )

    private val _currentQuote = mutableStateOf(quotes.random())
    val currentQuote: State<String> = _currentQuote

    fun nextQuote() {
        var next = quotes[Random.nextInt(quotes.size)]
        if (quotes.size > 1) {
            while (next == _currentQuote.value) {
                next = quotes[Random.nextInt(quotes.size)]
            }
        }
        _currentQuote.value = next
    }
}

private object Routes {
    const val Splash = "splash"
    const val Main = "main"
}

@Composable
private fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val base = MaterialTheme.colorScheme.background
    val primary = MaterialTheme.colorScheme.primary
    val gradient = Brush.linearGradient(
        colors = listOf(
            base,
            Color(0xFFEFF4FF),
            base.copy(alpha = 0.96f)
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Box(modifier = modifier.background(gradient)) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-40).dp, y = 40.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 30.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    DailyQuotesTheme {
        MainScreen()
    }
}