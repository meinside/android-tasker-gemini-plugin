# Tasker Gemini Plugin

A [Tasker](https://tasker.joaoapps.com/) plugin for generating text from [Google Gemini](https://ai.google.dev/gemini-api/docs) API,

built with [Tasker Plugins Library](https://tasker.joaoapps.com/pluginslibrary.html).

## Installation

Build and install on your own, or install from [Google Play Store](https://play.google.com/store/apps/details?id=dev.meinside.taskergeminiplugin)

## Configuration

Firstly, get your Google AI API key from [here](https://makersuite.google.com/app/apikey).

Then

### For text generation with prompt text

In Tasker,

* Add an action > select action: `Gemini Text Generation`
* Configuration > put your API key and prompt
  * prompt can include [Tasker variables](https://tasker.joaoapps.com/userguide/en/variables.html)
 
<img src="https://github.com/meinside/android-tasker-gemini-plugin/assets/185988/d5f91071-a14d-4cae-bfc2-80b82fa13efc" width="320">
<img src="https://github.com/meinside/android-tasker-gemini-plugin/assets/185988/d9383748-1373-4638-84a5-5b5efce497a7" width="320">
<img src="https://github.com/meinside/android-tasker-gemini-plugin/assets/185988/50c9e387-19e9-46af-85f7-c23546f95bdb" width="320">

### For text generation with prompt text and images

You can pass an image file path for multimodal generation (text + image):

* In the configuration, set **Image Path** to a file path (e.g. `/sdcard/DCIM/photo.jpg`)
* Tasker variables work here too (e.g. `%photo_path`)
* Supported formats: JPEG, PNG, WebP, GIF, BMP

When an image path is provided, both the prompt text and the image are sent to the Gemini API together.

### Optional settings

| Setting | Description | Default |
|---------|-------------|---------|
| **System Instruction** | Set a role or persona for the model (e.g. "You are a translator") | None |
| **Temperature** | Controls creativity. 0.0 = deterministic, 2.0 = most creative | 1.0 |
| **Max Output Tokens** | Maximum length of the generated response | 2048 |

All optional settings support Tasker variables.

## Usage

When the configured action is triggered successfully,

the generated text will be saved as a Tasker variable named `%gemini_text`.

This resulting variable can be referenced in following actions or other tasks.

(e.g. sending it as an email, showing it as an Android notification, etc.)

<img src="https://github.com/meinside/android-tasker-gemini-plugin/assets/185988/f5af2b9c-0f35-45fb-958e-c86d6bb03845" width="320">
<img src="https://github.com/meinside/android-tasker-gemini-plugin/assets/185988/43cb8605-0f4e-4e62-86aa-f11b5a578497" width="320">

## Building

### Debug

```bash
./gradlew assembleDebug
```

### Release

1. Copy `local.properties.example` to `local.properties`
2. Fill in your keystore path and credentials
3. Build:

```bash
./gradlew bundleRelease  # AAB for Play Store
./gradlew assembleRelease # APK
```

## Todos / Improvements

- [X] Add screenshots for guiding configuration
- [X] Support other Gemini models
- [X] Add tests
- [X] Add an action which generates text from prompt and images
- [ ] Add some more useful usages
- [ ] Enable R8 minification for smaller release binary

## License

MIT
