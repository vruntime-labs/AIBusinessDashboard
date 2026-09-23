# AI Business Dashboard - Android App

Professional AI content generation dashboard for business users.

## Features

- **Professional Business Theme** (Light + Dark mode)
- **File Upload** – PDF, Word, Excel, Images, Text (multiple files)
- **AI Generation** with:
  - Multiple **Formats**: Report, Email, Summary, Presentation, Blog, Letter, Proposal, Analysis, Social, Custom
  - Multiple **Styles**: Professional, Concise, Detailed, Executive, Casual, Technical, Persuasive, Analytical
  - Multiple **Themes/Tones**: Business, Formal, Creative, Analytical, Inspirational, Neutral, Authoritative
- **Text + Image** generation support
- **Generation History** (Room database)
- **Login screen** (demo mode)
- **Settings** – API Key support for real AI (OpenAI / Grok / compatible)
- **Export / Share** generated content
- Modern **Jetpack Compose + Material 3** UI

## How to Open in Android Studio

1. Open **Android Studio**
2. Click **File → Open**
3. Select the `AIBusinessDashboard` folder
4. Wait for Gradle sync
5. Run on emulator or device

## Project Structure

```
app/src/main/java/com/aibusiness/dashboard/
├── MainActivity.kt
├── data/
│   ├── model/Models.kt
│   ├── local/AppDatabase.kt
│   ├── remote/AIService.kt
│   └── repository/AIRepository.kt
├── ui/
│   ├── theme/ (Color, Type, Theme)
│   ├── screens/ (Login, Dashboard, Generate, Result, History, Settings)
│   ├── navigation/NavGraph.kt
│   └── components/
├── viewmodel/MainViewModel.kt
└── utils/
```

## Using Real AI

1. Go to **Settings**
2. Paste your API key (OpenAI `sk-...` or xAI `xai-...`)
3. Enable **Use Real AI**
4. In Generate screen, keep the toggle ON

> Currently the real API call is structured but uses a realistic placeholder.  
> You can easily complete the Retrofit implementation inside `AIService.kt`.

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room (History)
- Retrofit (ready for real API)
- Coil (images)
- DataStore ready

## Notes

- Min SDK 26
- Target SDK 35
- No real authentication (demo login accepts anything)
- File picker uses system document picker

---

Built for professional business use.
