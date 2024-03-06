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

### For text generation with prompt text and images

TODO

## Usage

When the configured action is triggered successfully,

the generated text will be saved as a Tasker variable named `%gemini_text`.

This resulting variable can be referenced in following actions or other tasks.

(eg. sending it as an email, showing it as an Android notification, or etc.)

## Todos / Improvements

- [ ] Add screenshots for guiding configuration
- [ ] Add some more useful usages
- [ ] Add tests
- [ ] Add an action which generates text from prompt and images using model `gemini-pro-vision`

## License

MIT

