# Tasker Gemini Plugin

A [Tasker](https://tasker.joaoapps.com/) plugin for generating text from [Google Gemini](https://ai.google.dev/tutorials/android_quickstart) API,

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

TODO

## Usage

When the configured action is triggered successfully,

the generated text will be saved as a Tasker variable named `%gemini_text`.

This resulting variable can be referenced in following actions or other tasks.

(eg. sending it as an email, showing it as an Android notification, or etc.)

<img src="https://github.com/meinside/android-tasker-gemini-plugin/assets/185988/f5af2b9c-0f35-45fb-958e-c86d6bb03845" width="320">
<img src="https://github.com/meinside/android-tasker-gemini-plugin/assets/185988/43cb8605-0f4e-4e62-86aa-f11b5a578497" width="320">

## Todos / Improvements

- [X] Add screenshots for guiding configuration
- [ ] Add some more useful usages
- [ ] Add tests
- [ ] Add an action which generates text from prompt and images using model `gemini-pro-vision`

## License

MIT

